/*Using a single counter for outstanding requests.

Change-Id:Ie99239fbe02f4ee1528c0dc6e1a1750cad404b2c*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..7cfab6f 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.telephony.Rlog;

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
//Synthetic comment -- @@ -68,6 +69,8 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

/**
* {@hide}
//Synthetic comment -- @@ -76,8 +79,7 @@
static final String LOG_TAG = "RILJ";

//***** Class Variables
    static int sNextSerial = 0;
    static Object sSerialMonitor = new Object();
private static Object sPoolSync = new Object();
private static RILRequest sPool = null;
private static int sPoolSize = 0;
//Synthetic comment -- @@ -113,9 +115,8 @@
rr = new RILRequest();
}

        synchronized(sSerialMonitor) {
            rr.mSerial = sNextSerial++;
        }
rr.mRequest = request;
rr.mResult = result;
rr.mp = Parcel.obtain();
//Synthetic comment -- @@ -152,9 +153,9 @@

static void
resetSerial() {
        synchronized(sSerialMonitor) {
            sNextSerial = 0;
        }
}

String
//Synthetic comment -- @@ -224,18 +225,14 @@
RILReceiver mReceiver;
WakeLock mWakeLock;
int mWakeLockTimeout;
    // The number of requests pending to be sent out, it increases before calling
    // EVENT_SEND and decreases while handling EVENT_SEND. It gets cleared while
// WAKE_LOCK_TIMEOUT occurs.
    int mRequestMessagesPending;
    // The number of requests sent out but waiting for response. It increases while
    // sending request and decreases while handling response. It should match
    // mRequestList.size() unless there are requests no replied while
    // WAKE_LOCK_TIMEOUT occurs.
    int mRequestMessagesWaiting;

    //I'd rather this be LinkedList or something
    ArrayList<RILRequest> mRequestList = new ArrayList<RILRequest>();

Object     mLastNITZTimeInfo;

//Synthetic comment -- @@ -300,12 +297,6 @@

switch (msg.what) {
case EVENT_SEND:
                    /**
                     * mRequestMessagePending++ already happened for every
                     * EVENT_SEND, thus we must make sure
                     * mRequestMessagePending-- happens once and only once
                     */
                    boolean alreadySubtracted = false;
try {
LocalSocket s;

//Synthetic comment -- @@ -314,21 +305,16 @@
if (s == null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
                            if (mRequestMessagesPending > 0)
                                mRequestMessagesPending--;
                            alreadySubtracted = true;
return;
}

synchronized (mRequestList) {
                            mRequestList.add(rr);
                            mRequestMessagesWaiting++;
}

                        if (mRequestMessagesPending > 0)
                            mRequestMessagesPending--;
                        alreadySubtracted = true;

byte[] data;

data = rr.mp.marshall();
//Synthetic comment -- @@ -355,7 +341,7 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null || !alreadySubtracted) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
}
//Synthetic comment -- @@ -364,71 +350,52 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null || !alreadySubtracted) {
rr.onError(GENERIC_FAILURE, null);
rr.release();
}
} finally {
// Note: We are "Done" only if there are no outstanding
                        // requests or replies. Thus this code path will only release
// the wake lock on errors.
releaseWakeLockIfDone();
}

                    if (!alreadySubtracted && mRequestMessagesPending > 0) {
                        mRequestMessagesPending--;
                    }

break;

case EVENT_WAKE_LOCK_TIMEOUT:
// Haven't heard back from the last request.  Assume we're
// not getting a response and  release the wake lock.
                    synchronized (mWakeLock) {
if (mWakeLock.isHeld()) {
// The timer of WAKE_LOCK_TIMEOUT is reset with each
// new send request. So when WAKE_LOCK_TIMEOUT occurs
// all requests in mRequestList already waited at
// least DEFAULT_WAKE_LOCK_TIMEOUT but no response.
                            // Reset mRequestMessagesWaiting to enable
// releaseWakeLockIfDone().
//
// Note: Keep mRequestList so that delayed response
// can still be handled when response finally comes.
                            if (mRequestMessagesWaiting != 0) {
                                Rlog.d(LOG_TAG, "NOTE: mReqWaiting is NOT 0 but"
                                        + mRequestMessagesWaiting + " at TIMEOUT, reset!"
+ " There still msg waitng for response");

                                mRequestMessagesWaiting = 0;

if (RILJ_LOGD) {
                                    synchronized (mRequestList) {
                                        int count = mRequestList.size();
                                        Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                                                " mRequestList=" + count);

                                        for (int i = 0; i < count; i++) {
                                            rr = mRequestList.get(i);
                                            Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] "
                                                    + requestToString(rr.mRequest));
                                        }
}
}
}
                            // mRequestMessagesPending shows how many
                            // requests are waiting to be sent (and before
                            // to be added in request list) since star the
                            // WAKE_LOCK_TIMEOUT timer. Since WAKE_LOCK_TIMEOUT
                            // is the expected time to get response, all requests
                            // should already sent out (i.e.
                            // mRequestMessagesPending is 0 )while TIMEOUT occurs.
                            if (mRequestMessagesPending != 0) {
                                Rlog.e(LOG_TAG, "ERROR: mReqPending is NOT 0 but"
                                        + mRequestMessagesPending + " at TIMEOUT, reset!");
                                mRequestMessagesPending = 0;

                            }
mWakeLock.release();
}
}
//Synthetic comment -- @@ -618,6 +585,8 @@
riljLog("RIL(context, preferredNetworkType=" + preferredNetworkType +
" cdmaSubscription=" + cdmaSubscription + ")");
}
mCdmaSubscription  = cdmaSubscription;
mPreferredNetworkType = preferredNetworkType;
mPhoneType = RILConstants.NO_PHONE;
//Synthetic comment -- @@ -627,8 +596,7 @@
mWakeLock.setReferenceCounted(false);
mWakeLockTimeout = SystemProperties.getInt(TelephonyProperties.PROPERTY_WAKE_LOCK_TIMEOUT,
DEFAULT_WAKE_LOCK_TIMEOUT);
        mRequestMessagesPending = 0;
        mRequestMessagesWaiting = 0;

mSenderThread = new HandlerThread("RILSender");
mSenderThread.start();
//Synthetic comment -- @@ -2090,9 +2058,9 @@

private void
acquireWakeLock() {
        synchronized (mWakeLock) {
mWakeLock.acquire();
            mRequestMessagesPending++;

mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
Message msg = mSender.obtainMessage(EVENT_WAKE_LOCK_TIMEOUT);
//Synthetic comment -- @@ -2102,12 +2070,13 @@

private void
releaseWakeLockIfDone() {
        synchronized (mWakeLock) {
            if (mWakeLock.isHeld() &&
                (mRequestMessagesPending == 0) &&
                (mRequestMessagesWaiting == 0)) {
                mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
                mWakeLock.release();
}
}
}
//Synthetic comment -- @@ -2155,12 +2124,12 @@
int count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                        " mReqPending=" + mRequestMessagesPending +
" mRequestList=" + count);
}

for (int i = 0; i < count ; i++) {
                rr = mRequestList.get(i);
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] " +
requestToString(rr.mRequest));
//Synthetic comment -- @@ -2168,26 +2137,23 @@
rr.onError(error, null);
rr.release();
}
mRequestList.clear();
            mRequestMessagesWaiting = 0;
}
}

private RILRequest findAndRemoveRequestFromList(int serial) {
synchronized (mRequestList) {
            for (int i = 0, s = mRequestList.size() ; i < s ; i++) {
                RILRequest rr = mRequestList.get(i);

                if (rr.mSerial == serial) {
                    mRequestList.remove(i);
                    if (mRequestMessagesWaiting > 0)
                        mRequestMessagesWaiting--;
                    return rr;
                }
}
}

        return null;
}

private void
//Synthetic comment -- @@ -3881,12 +3847,11 @@
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
synchronized (mRequestList) {
          pw.println(" mRequestMessagesPending=" + mRequestMessagesPending);
          pw.println(" mRequestMessagesWaiting=" + mRequestMessagesWaiting);
int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {
                RILRequest rr = mRequestList.get(i);
pw.println("  [" + rr.mSerial + "] " + requestToString(rr.mRequest));
}
}







