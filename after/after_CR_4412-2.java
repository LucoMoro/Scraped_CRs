Extend ANDROID with CDMA mobile technology support - With review comments fixed.

    This project has the goal to extend the Android telephony layers with CDMA
    mobile technology support.
    The current release 1 of Android supports GSM/WCDMA as mobile communication
    standards.
    Our contribution will contain changes in the phone related applications, the
    application framework telephony packages and in the RIL daemon library space.
    The implementation of the CDMA support requires architectural changes in the
    telephony package and extensions of the RIL interface.
    The application interface (SDK interface) will be extended to provide
    CDMA specific features/information to the phone related application and other
    applications.
    Where ever possible the actual used radio technology is transparent for the
    application using mobile connections.

    Each increment of the contribution will provide a pre-tested set of use case
    implementations.
    The final contribution will support CDMA functionality for Android phones
    supporting
    either CDMA mobile technology only or a world mode including GSM/WCDMA and CDMA.
    The following CDMA technologies are considered: IS-95, CDMA2000 1xRTT, CDMA2000
    1x EVDO.

    This contribution implements the following use cases:
    UC Startup-Phone
    UC Initialize Phone
    UC Access SIM/RUIM
    UC Network Indications
    UC Mobile Originated Call
    UC Mobile Terminated Call
    UC Network / Phone Settings
    UC Supplementary Services (partly)

    With these use cases the phone will
    - start up,
    - access the CDMA subscription and other information from memory of from the card (either SIM, USIM or RUIM),
    - register to the network,
    - provides registration status to the application for displaying
    - be able to handle incoming and outgoing voice calls,
    - provide phone and call settings in the settings application
    - provide supplementary services in the settings application

    Various review comments are also fixed with this contribution.

    Approved By :- Aravind Mahishi , aravind.mahishi@teleca.com
                   Wolfgang Schmidt, wolfgang.schmidt@teleca.com




diff --git a/src/com/android/phone/CallFeaturesSetting.java b/src/com/android/phone/CallFeaturesSetting.java
old mode 100644
new mode 100755
index 7859f1a..abc8f76

@@ -45,7 +45,6 @@
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.cdma.CDMAPhone;
import android.telephony.cdma.TtyIntent;
import android.content.BroadcastReceiver;
import android.content.Context;

@@ -61,7 +60,7 @@

// debug data
private static final String LOG_TAG = "call features settings";
    private static final boolean DBG = false;

// string contants
private static final String NUM_PROJECTION[] = {PhonesColumns.NUMBER};
@@ -100,13 +99,13 @@
private static final int EVENT_CLIR_EXECUTED         = 200;
private static final int EVENT_CW_EXECUTED           = 300;
private static final int EVENT_CF_EXECUTED           = 400;
/** Event for Async voicemail change call */
private static final int EVENT_VOICEMAIL_CHANGED     = 500;
/** track the query cancel event. */
private static final int EVENT_INITAL_QUERY_CANCELED = 600;
/** Event for TTY mode change */
    private static final int EVENT_TTY_EXECUTED          = 700;
    private static final int EVENT_ENHANCED_VP_EXECUTED  = 1000;

// preferred TTY mode
// 0 = disabled
@@ -230,11 +229,11 @@
return true;
} else if (preference == mButtonMoreExpand){
setDisplayMode(DISP_MODE_MORE);
        } else if (preference == mButtonVoicePrivacy) {
            handleVoicePrivacyClickRequest(mButtonVoicePrivacy.isChecked());
nextState = AppState.BUSY_NETWORK_CONNECT;
        } else if (preference == mButtonTTY) {
            handleTTYClickRequest(mButtonTTY.isChecked());
}

if (nextState != AppState.INPUT_READY) {
@@ -1000,25 +999,25 @@
if (status != MSG_OK) {
setAppState(AppState.NETWORK_ERROR, status);
} else {
                        mPhone.getCallWaiting(Message.obtain(mGetMoreOptionsComplete, EVENT_CW_EXECUTED));
}
break;

case EVENT_CW_EXECUTED:
status = handleGetCWMessage(ar);
if (DBG) {
                        log("mGetAllOptionsComplete: CW query done, querying VP.");
}
if (status != MSG_OK) {
                        setAppState(AppState.NETWORK_ERROR, status);
} else {
                        if (PhoneFactory.getDefaultPhone().getPhoneName().equals("GSM")) {
                            mMoreDataStale = false;
                            setAppState(AppState.INPUT_READY);
                        } else {
                            mPhone.getEnhancedVoicePrivacy(Message.obtain(mGetMoreOptionsComplete,
                                EVENT_ENHANCED_VP_EXECUTED));
                        }
}
break;








