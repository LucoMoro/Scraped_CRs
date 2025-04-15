/*Phone: Basic PhoneApp changes for DSDS.

Basic phone app changes to support dual sim dual standby(DSDS).

Change-Id:Icffc8c0b167d19087374926233e6a121b0bdab94*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index 80c6f57..c568882 100644

//Synthetic comment -- @@ -50,6 +50,9 @@
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
//Synthetic comment -- @@ -153,9 +156,6 @@
boolean mShowBluetoothIndication = false;
static int mDockState = Intent.EXTRA_DOCK_STATE_UNDOCKED;

    // Internal PhoneApp Call state tracker
    CdmaPhoneCallState cdmaPhoneCallState;

// The InCallScreen instance (or null if the InCallScreen hasn't been
// created yet.)
private InCallScreen mInCallScreen;
//Synthetic comment -- @@ -208,21 +208,28 @@
/** boolean indicating restoring mute state on InCallScreen.onResume() */
private boolean mShouldRestoreMuteOnInCallResume;

// Following are the CDMA OTA information Objects used during OTA Call.
// cdmaOtaProvisionData object store static OTA information that needs
// to be maintained even during Slider open/close scenarios.
// cdmaOtaConfigData object stores configuration info to control visiblity
// of each OTA Screens.
// cdmaOtaScreenState object store OTA Screen State information.
    public OtaUtils.CdmaOtaProvisionData cdmaOtaProvisionData;
    public OtaUtils.CdmaOtaConfigData cdmaOtaConfigData;
    public OtaUtils.CdmaOtaScreenState cdmaOtaScreenState;
    public OtaUtils.CdmaOtaInCallScreenUiState cdmaOtaInCallScreenUiState;

// TTY feature enabled on this platform
private boolean mTtyEnabled;
// Current TTY operating mode selected by user
private int mPreferredTtyMode = Phone.TTY_MODE_OFF;

/**
* Set the restore mute state flag. Used when we are setting the mute state
//Synthetic comment -- @@ -364,6 +371,7 @@
mInCallScreen.requestUpdateTouchUi();
}
}

case EVENT_TTY_PREFERRED_MODE_CHANGED:
// TTY mode is only applied if a headset is connected
//Synthetic comment -- @@ -396,16 +404,27 @@
if (VDBG) Log.v(LOG_TAG, "onCreate()...");

ContentResolver resolver = getContentResolver();

if (phone == null) {
// Initialize the telephony framework
PhoneFactory.makeDefaultPhones(this);

            // Get the default phone
            phone = PhoneFactory.getDefaultPhone();

mCM = CallManager.getInstance();
            mCM.registerPhone(phone);


NotificationMgr.init(this);
//Synthetic comment -- @@ -422,12 +441,6 @@

int phoneType = phone.getPhoneType();

            if (phoneType == Phone.PHONE_TYPE_CDMA) {
                // Create an instance of CdmaPhoneCallState and initialize it to IDLE
                cdmaPhoneCallState = new CdmaPhoneCallState();
                cdmaPhoneCallState.CdmaPhoneCallStateInit();
            }

if (BluetoothAdapter.getDefaultAdapter() != null) {
mBtHandsfree = new BluetoothHandsfree(this, mCM);
startService(new Intent(this, BluetoothHeadsetService.class));
//Synthetic comment -- @@ -498,6 +511,7 @@
intentFilter.addAction(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
intentFilter.addAction(TelephonyIntents.ACTION_SERVICE_STATE_CHANGED);
intentFilter.addAction(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED);
if (mTtyEnabled) {
intentFilter.addAction(TtyIntent.TTY_PREFERRED_MODE_CHANGE_ACTION);
}
//Synthetic comment -- @@ -528,15 +542,6 @@
PhoneUtils.setAudioMode(mCM);
}

        boolean phoneIsCdma = (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA);

        if (phoneIsCdma) {
            cdmaOtaProvisionData = new OtaUtils.CdmaOtaProvisionData();
            cdmaOtaConfigData = new OtaUtils.CdmaOtaConfigData();
            cdmaOtaScreenState = new OtaUtils.CdmaOtaScreenState();
            cdmaOtaInCallScreenUiState = new OtaUtils.CdmaOtaInCallScreenUiState();
        }

// XXX pre-load the SimProvider so that it's ready
resolver.getType(Uri.parse("content://icc/adn"));

//Synthetic comment -- @@ -563,11 +568,41 @@
android.provider.Settings.System.HEARING_AID,
0);
AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setParameter(CallFeaturesSetting.HAC_KEY, hac != 0 ?
                                      CallFeaturesSetting.HAC_VAL_ON :
                                      CallFeaturesSetting.HAC_VAL_OFF);
}
   }

@Override
public void onConfigurationChanged(Configuration newConfig) {
//Synthetic comment -- @@ -616,8 +651,10 @@
* This intent can only be used from within the Phone app, since the
* InCallScreen is not exported from our AndroidManifest.
*/
    /* package */ static Intent createInCallIntent() {
Intent intent = new Intent(Intent.ACTION_MAIN, null);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//Synthetic comment -- @@ -631,7 +668,8 @@
* comes up.
*/
/* package */ static Intent createInCallIntent(boolean showDialpad) {
        Intent intent = createInCallIntent();
intent.putExtra(InCallScreen.SHOW_DIALPAD_EXTRA, showDialpad);
return intent;
}
//Synthetic comment -- @@ -645,7 +683,7 @@
*/
private void displayCallScreen() {
if (VDBG) Log.d(LOG_TAG, "displayCallScreen()...");
        startActivity(createInCallIntent());
Profiler.callScreenRequested();
}

//Synthetic comment -- @@ -653,6 +691,11 @@
return mIsSimPinEnabled;
}

boolean authenticateAgainstCachedSimPin(String pin) {
return (mCachedSimPin != null && mCachedSimPin.equals(pin));
}
//Synthetic comment -- @@ -665,6 +708,10 @@
mInCallScreen = inCallScreen;
}

/**
* @return true if the in-call UI is running as the foreground
* activity.  (In other words, from the perspective of the
//Synthetic comment -- @@ -690,7 +737,7 @@
* For OTA Call, it call InCallScreen api to handle OTA Call End scenario
* to display OTA Call End screen.
*/
    void dismissCallScreen() {
if (mInCallScreen != null) {
if ((phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) &&
(mInCallScreen.isOtaCallInActiveState()
//Synthetic comment -- @@ -1219,8 +1266,8 @@
}
}

    /* package */ Phone.State getPhoneState() {
        return mLastPhoneState;
}

/**
//Synthetic comment -- @@ -1237,35 +1284,24 @@

private void onMMIComplete(AsyncResult r) {
if (VDBG) Log.d(LOG_TAG, "onMMIComplete()...");
MmiCode mmiCode = (MmiCode) r.result;
        PhoneUtils.displayMMIComplete(phone, getInstance(), mmiCode, null, null);
}

    private void initForNewRadioTechnology() {
if (DBG) Log.d(LOG_TAG, "initForNewRadioTechnology...");

        if (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
            // Create an instance of CdmaPhoneCallState and initialize it to IDLE
            cdmaPhoneCallState = new CdmaPhoneCallState();
            cdmaPhoneCallState.CdmaPhoneCallStateInit();

            //create instances of CDMA OTA data classes
            if (cdmaOtaProvisionData == null) {
                cdmaOtaProvisionData = new OtaUtils.CdmaOtaProvisionData();
            }
            if (cdmaOtaConfigData == null) {
                cdmaOtaConfigData = new OtaUtils.CdmaOtaConfigData();
            }
            if (cdmaOtaScreenState == null) {
                cdmaOtaScreenState = new OtaUtils.CdmaOtaScreenState();
            }
            if (cdmaOtaInCallScreenUiState == null) {
                cdmaOtaInCallScreenUiState = new OtaUtils.CdmaOtaInCallScreenUiState();
            }
        } else {
            //Clean up OTA data in GSM/UMTS. It is valid only for CDMA
clearOtaState();
}

ringer.updateRingerContextAfterRadioTechnologyChange(this.phone);
notifier.updateCallNotifierRegistrationsAfterRadioTechnologyChange();
//Synthetic comment -- @@ -1389,10 +1425,20 @@
@Override
public void onReceive(Context context, Intent intent) {
String action = intent.getAction();
if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                boolean enabled = System.getInt(getContentResolver(),
                        System.AIRPLANE_MODE_ON, 0) == 0;
                phone.setRadioPower(enabled);
} else if (action.equals(BluetoothHeadset.ACTION_STATE_CHANGED)) {
mBluetoothHeadsetState = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE,
BluetoothHeadset.STATE_ERROR);
//Synthetic comment -- @@ -1450,8 +1496,10 @@
Log.d(LOG_TAG, "Radio technology switched. Now " + newPhone + " is active.");
initForNewRadioTechnology();
} else if (action.equals(TelephonyIntents.ACTION_SERVICE_STATE_CHANGED)) {
                handleServiceStateChanged(intent);
} else if (action.equals(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED)) {
if (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
Log.d(LOG_TAG, "Emergency Callback Mode arrived in PhoneApp.");
// Start Emergency Callback Mode service
//Synthetic comment -- @@ -1480,6 +1528,10 @@
if(ringerMode == AudioManager.RINGER_MODE_SILENT) {
notifier.silenceRinger();
}
}
}
}
//Synthetic comment -- @@ -1526,7 +1578,7 @@
}
}

    private void handleServiceStateChanged(Intent intent) {
/**
* This used to handle updating EriTextWidgetProvider this routine
* and and listening for ACTION_SERVICE_STATE_CHANGED intents could
//Synthetic comment -- @@ -1543,7 +1595,7 @@

if (ss != null) {
int state = ss.getState();
            NotificationMgr.getDefault().updateNetworkSelection(state);
switch (state) {
case ServiceState.STATE_OUT_OF_SERVICE:
case ServiceState.STATE_POWER_OFF:
//Synthetic comment -- @@ -1664,4 +1716,124 @@
// System process is dead.
}
}
}








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..2c7811a 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.CallManager;

import java.util.List;
import java.util.ArrayList;
//Synthetic comment -- @@ -47,6 +49,8 @@
private static final String LOG_TAG = "PhoneInterfaceManager";
private static final boolean DBG = (PhoneApp.DBG_LEVEL >= 2);

// Message codes used with mMainThreadHandler
private static final int CMD_HANDLE_PIN_MMI = 1;
private static final int CMD_HANDLE_NEIGHBORING_CELL = 2;
//Synthetic comment -- @@ -65,13 +69,16 @@
* request after sending. The main thread will notify the request when it is complete.
*/
private static final class MainThreadRequest {
        /** The argument to use for the request */
        public Object argument;
/** The result of the request that is run on the main thread */
public Object result;

        public MainThreadRequest(Object argument) {
            this.argument = argument;
}
}

//Synthetic comment -- @@ -97,8 +104,11 @@
switch (msg.what) {
case CMD_HANDLE_PIN_MMI:
request = (MainThreadRequest) msg.obj;
request.result = Boolean.valueOf(
                            mPhone.handlePinMmi((String) request.argument));
// Wake up the requesting thread
synchronized (request) {
request.notifyAll();
//Synthetic comment -- @@ -138,14 +148,15 @@
case CMD_END_CALL:
request = (MainThreadRequest) msg.obj;
boolean hungUp = false;
                    int phoneType = mPhone.getPhoneType();
if (phoneType == Phone.PHONE_TYPE_CDMA) {
// CDMA: If the user presses the Power button we treat it as
// ending the complete call session
                        hungUp = PhoneUtils.hangupRingingAndActive(mPhone);
} else if (phoneType == Phone.PHONE_TYPE_GSM) {
                        // GSM: End the call as per the Phone state
                        hungUp = PhoneUtils.hangup(mCM);
} else {
throw new IllegalStateException("Unexpected phone type: " + phoneType);
}
//Synthetic comment -- @@ -169,12 +180,12 @@
* waits for the request to complete, and returns the result.
* @see sendRequestAsync
*/
    private Object sendRequest(int command, Object argument) {
if (Looper.myLooper() == mMainThreadHandler.getLooper()) {
throw new RuntimeException("This method will deadlock if called from the main thread.");
}

        MainThreadRequest request = new MainThreadRequest(argument);
Message msg = mMainThreadHandler.obtainMessage(command, request);
msg.sendToTarget();

//Synthetic comment -- @@ -215,12 +226,23 @@
ServiceManager.addService("phone", this);
}

//
// Implementation of the ITelephony interface.
//

public void dial(String number) {
if (DBG) log("dial: " + number);
// No permission check needed here: This is just a wrapper around the
// ACTION_DIAL intent, which is available to any app since it puts up
// the UI before it does anything.
//Synthetic comment -- @@ -231,16 +253,22 @@
}

// PENDING: should we just silently fail if phone is offhook or ringing?
        Phone.State state = mPhone.getState();
if (state != Phone.State.OFFHOOK && state != Phone.State.RINGING) {
Intent  intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
mApp.startActivity(intent);
}
}

public void call(String number) {
if (DBG) log("call: " + number);

// This is just a wrapper around the ACTION_CALL intent, but we still
// need to do a permission check since we're calling startActivity()
//Synthetic comment -- @@ -253,6 +281,7 @@
}

Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
intent.setClassName(mApp, PhoneApp.getCallScreenClassName());
mApp.startActivity(intent);
//Synthetic comment -- @@ -270,7 +299,7 @@
if (specifyInitialDialpadState) {
intent = PhoneApp.createInCallIntent(initialDialpadState);
} else {
                intent = PhoneApp.createInCallIntent();
}
mApp.startActivity(intent);
} finally {
//Synthetic comment -- @@ -296,12 +325,25 @@
* @return true is a call was ended
*/
public boolean endCall() {
enforceCallPermission();
        return (Boolean) sendRequest(CMD_END_CALL, null);
}

public void answerRingingCall() {
if (DBG) log("answerRingingCall...");
// TODO: there should eventually be a separate "ANSWER_PHONE" permission,
// but that can probably wait till the big TelephonyManager API overhaul.
// For now, protect this call with the MODIFY_PHONE_STATE permission.
//Synthetic comment -- @@ -323,10 +365,10 @@
* return value, so let's just return void for now.
*/
private void answerRingingCallInternal() {
        final boolean hasRingingCall = !mPhone.getRingingCall().isIdle();
if (hasRingingCall) {
            final boolean hasActiveCall = !mPhone.getForegroundCall().isIdle();
            final boolean hasHoldingCall = !mPhone.getBackgroundCall().isIdle();
if (hasActiveCall && hasHoldingCall) {
// Both lines are in use!
// TODO: provide a flag to let the caller specify what
//Synthetic comment -- @@ -362,7 +404,7 @@
* @see silenceRinger
*/
private void silenceRingerInternal() {
        if ((mPhone.getState() == Phone.State.RINGING)
&& mApp.notifier.isRinging()) {
// Ringer is actually playing, so silence it.
if (DBG) log("silenceRingerInternal: silencing...");
//Synthetic comment -- @@ -371,25 +413,42 @@
}

public boolean isOffhook() {
        return (mPhone.getState() == Phone.State.OFFHOOK);
}

public boolean isRinging() {
        return (mPhone.getState() == Phone.State.RINGING);
}

public boolean isIdle() {
        return (mPhone.getState() == Phone.State.IDLE);
}

public boolean isSimPinEnabled() {
enforceReadPermission();
        return (PhoneApp.getInstance().isSimPinEnabled());
}

public boolean supplyPin(String pin) {
enforceModifyPermission();
        final CheckSimPin checkSimPin = new CheckSimPin(mPhone.getIccCard());
checkSimPin.start();
return checkSimPin.checkPin(pin);
}
//Synthetic comment -- @@ -468,75 +527,110 @@
}

public void updateServiceLocation() {
// No permission check needed here: this call is harmless, and it's
// needed for the ServiceState.requestStateUpdate() call (which is
// already intentionally exposed to 3rd parties.)
        mPhone.updateServiceLocation();
}

public boolean isRadioOn() {
        return mPhone.getServiceState().getState() != ServiceState.STATE_POWER_OFF;
}

public void toggleRadioOnOff() {
        enforceModifyPermission();
        mPhone.setRadioPower(!isRadioOn());
}
    public boolean setRadio(boolean turnOn) {
enforceModifyPermission();
        if ((mPhone.getServiceState().getState() != ServiceState.STATE_POWER_OFF) != turnOn) {
            toggleRadioOnOff();
}
return true;
}

public boolean enableDataConnectivity() {
enforceModifyPermission();
        return mPhone.enableDataConnectivity();
}

public int enableApnType(String type) {
enforceModifyPermission();
        return mPhone.enableApnType(type);
}

public int disableApnType(String type) {
enforceModifyPermission();
        return mPhone.disableApnType(type);
}

public boolean disableDataConnectivity() {
enforceModifyPermission();
        return mPhone.disableDataConnectivity();
}

public boolean isDataConnectivityPossible() {
        return mPhone.isDataConnectivityPossible();
}

public boolean handlePinMmi(String dialString) {
enforceModifyPermission();
        return (Boolean) sendRequest(CMD_HANDLE_PIN_MMI, dialString);
}

public void cancelMissedCallsNotification() {
enforceModifyPermission();
NotificationMgr.getDefault().cancelMissedCallNotification();
}

public int getCallState() {
        return DefaultPhoneNotifier.convertCallState(mPhone.getState());
}

public int getDataState() {
        return DefaultPhoneNotifier.convertDataState(mPhone.getDataConnectionState());
}

public int getDataActivity() {
        return DefaultPhoneNotifier.convertDataActivityState(mPhone.getDataActivityState());
}

public Bundle getCellLocation() {
try {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.ACCESS_FINE_LOCATION, null);
//Synthetic comment -- @@ -548,24 +642,37 @@
android.Manifest.permission.ACCESS_COARSE_LOCATION, null);
}
Bundle data = new Bundle();
        mPhone.getCellLocation().fillInNotifierBundle(data);
return data;
}

public void enableLocationUpdates() {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.CONTROL_LOCATION_UPDATES, null);
        mPhone.enableLocationUpdates();
}

public void disableLocationUpdates() {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.CONTROL_LOCATION_UPDATES, null);
        mPhone.disableLocationUpdates();
}

@SuppressWarnings("unchecked")
public List<NeighboringCellInfo> getNeighboringCellInfo() {
try {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.ACCESS_FINE_LOCATION, null);
//Synthetic comment -- @@ -582,7 +689,7 @@

try {
cells = (ArrayList<NeighboringCellInfo>) sendRequest(
                    CMD_HANDLE_NEIGHBORING_CELL, null);
} catch (RuntimeException e) {
Log.e(LOG_TAG, "getNeighboringCellInfo " + e);
}
//Synthetic comment -- @@ -638,14 +745,22 @@
}

public int getActivePhoneType() {
        return mPhone.getPhoneType();
}

/**
* Returns the CDMA ERI icon index to display
*/
public int getCdmaEriIconIndex() {
        return mPhone.getCdmaEriIconIndex();
}

/**
//Synthetic comment -- @@ -654,26 +769,38 @@
* 1 - FLASHING
*/
public int getCdmaEriIconMode() {
        return mPhone.getCdmaEriIconMode();
}

/**
* Returns the CDMA ERI text,
*/
public String getCdmaEriText() {
        return mPhone.getCdmaEriText();
}

/**
* Returns true if CDMA provisioning needs to run.
*/
public boolean getCdmaNeedsProvisioning() {
        if (getActivePhoneType() == Phone.PHONE_TYPE_GSM) {
return false;
}

boolean needsProvisioning = false;
        String cdmaMin = mPhone.getCdmaMin();
try {
needsProvisioning = OtaUtils.needsActivation(cdmaMin);
} catch (IllegalArgumentException e) {
//Synthetic comment -- @@ -687,14 +814,28 @@
* Returns the unread count of voicemails
*/
public int getVoiceMessageCount() {
        return mPhone.getVoiceMessageCount();
}

/**
* Returns the network type
*/
public int getNetworkType() {
        int radiotech = mPhone.getServiceState().getRadioTechnology();
switch(radiotech) {
case ServiceState.RADIO_TECHNOLOGY_GPRS:
return TelephonyManager.NETWORK_TYPE_GPRS;
//Synthetic comment -- @@ -728,6 +869,37 @@
* @return true if a ICC card is present
*/
public boolean hasIccCard() {
        return mPhone.getIccCard().hasIccCard();
}
}








//Synthetic comment -- diff --git a/src/com/android/phone/SinglePhone.java b/src/com/android/phone/SinglePhone.java
new file mode 100644
//Synthetic comment -- index 0000000..4efa55a

//Synthetic comment -- @@ -0,0 +1,154 @@







