/*System Property to Ignore Out of State Indications in DataStateTracker

We ignore the out of service bursts, if there service is indeed lost, the
data call disconnect indication will be sent by the modem, else we wait
optimistically for a quick reattach.
This change is guarded by a system property

Change-Id:I827797afe98f489a717ed56aacf63b4a6e5137df*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1f69f7a..4167657 100755

//Synthetic comment -- @@ -138,6 +138,10 @@
new AtomicReference<UiccCardApplication>();
public SMSDispatcher mSMS;

    // Flag that indicates that Out Of Service is considered as data call disconnect
    protected boolean mOosIsDisconnect = SystemProperties.getBoolean(
            TelephonyProperties.PROPERTY_OOS_IS_DISCONNECT, true);

/**
* Set a system property, unless we're in unit test mode
*/
//Synthetic comment -- @@ -259,6 +263,7 @@
mSmsUsageMonitor = new SmsUsageMonitor(context);
mUiccController = UiccController.getInstance();
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
        Log.d(LOG_TAG, "mOosIsDisconnect=" + mOosIsDisconnect);
}

public void dispose() {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java b/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java
//Synthetic comment -- index cc59b67..e40cc56 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.ServiceState;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
//Synthetic comment -- @@ -122,6 +123,11 @@
// removeReferences() have already been called

ret = PhoneConstants.DataState.DISCONNECTED;
        } else if (mDataConnectionTracker.checkForConnectivity() &&
                            mSST.getCurrentDataConnectionState() != ServiceState.STATE_IN_SERVICE &&
                            mOosIsDisconnect) {
            ret = PhoneConstants.DataState.DISCONNECTED;
            log("getDataConnectionState: Data is Out of Service. ret = " + ret);
} else if (mDataConnectionTracker.isApnTypeEnabled(apnType) == false) {
ret = PhoneConstants.DataState.DISCONNECTED;
} else {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 910b9c1..f09debc 100755

//Synthetic comment -- @@ -613,10 +613,12 @@
// already been called

ret = PhoneConstants.DataState.DISCONNECTED;
        } else if (mSST.getCurrentDataConnectionState() != ServiceState.STATE_IN_SERVICE
                && mOosIsDisconnect) {
// If we're out of service, open TCP sockets may still work
// but no data will flow
ret = PhoneConstants.DataState.DISCONNECTED;
            log("getDataConnectionState: Data is Out of Service. ret = " + ret);
} else if (mDataConnectionTracker.isApnTypeEnabled(apnType) == false ||
mDataConnectionTracker.isApnTypeActive(apnType) == false) {
ret = PhoneConstants.DataState.DISCONNECTED;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..7c73f63 100644

//Synthetic comment -- @@ -290,10 +290,12 @@

ret = PhoneConstants.DataState.DISCONNECTED;
} else if (mSST.getCurrentGprsState()
                != ServiceState.STATE_IN_SERVICE
                && mOosIsDisconnect) {
// If we're out of service, open TCP sockets may still work
// but no data will flow
ret = PhoneConstants.DataState.DISCONNECTED;
            log("getDataConnectionState: Data is Out of Service. ret = " + ret);
} else if (mDataConnectionTracker.isApnTypeEnabled(apnType) == false ||
mDataConnectionTracker.isApnTypeActive(apnType) == false) {
//TODO: isApnTypeActive() is just checking whether ApnContext holds







