/*Using a single counter for outstanding requests.

Change-Id:Ie99239fbe02f4ee1528c0dc6e1a1750cad404b2c*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..7cfab6f 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.telephony.Rlog;
import android.util.SparseArray;

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
//Synthetic comment -- @@ -68,6 +69,8 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

/**
* {@hide}
//Synthetic comment -- @@ -76,8 +79,7 @@
static final String LOG_TAG = "RILJ";

//***** Class Variables
    static AtomicInteger sNextSerial = new AtomicInteger(0);
private static Object sPoolSync = new Object();
private static RILRequest sPool = null;
private static int sPoolSize = 0;
//Synthetic comment -- @@ -113,9 +115,8 @@
rr = new RILRequest();
}

        rr.mSerial = sNextSerial.getAndIncrement();

rr.mRequest = request;
rr.mResult = result;
rr.mp = Parcel.obtain();
//Synthetic comment -- @@ -152,9 +153,9 @@

static void
resetSerial() {
        // use a random so that on recovery we probably don't mix old requests
        // with new.
        sNextSerial.set(new Random().nextInt());
}

String
//Synthetic comment -- @@ -224,18 +225,14 @@
RILReceiver mReceiver;
WakeLock mWakeLock;
int mWakeLockTimeout;
    // The number of requests outstanding - we've receive the request
    // but haven't gotten the response yet.  It increases before calling
    // EVENT_SEND and decreases while handling a response.  It should match
    // mRequestList.size() unless there are requests not replied to when
// WAKE_LOCK_TIMEOUT occurs.
    int mRequestMessagesOutstanding;

    SparseArray<RILRequest> mRequestList = new SparseArray<RILRequest>();

Object     mLastNITZTimeInfo;

//Synthetic comment -- @@ -300,12 +297,6 @@

switch (msg.what) {
case EVENT_SEND:
try {
LocalSocket s;

//Synthetic comment -- @@ -314,21 +305,16 @@
if (s == null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
                            synchronized (mRequestList) {
                                if (mRequestMessagesOutstanding > 0) mRequestMessagesOutstanding--;
                            }
return;
}

synchronized (mRequestList) {
                            mRequestList.append(rr.mSerial, rr);
}

byte[] data;

data = rr.mp.marshall();
//Synthetic comment -- @@ -355,7 +341,7 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
}
//Synthetic comment -- @@ -364,71 +350,52 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null) {
rr.onError(GENERIC_FAILURE, null);
rr.release();
}
} finally {
// Note: We are "Done" only if there are no outstanding
                        // requests. Thus this code path will only release
// the wake lock on errors.
releaseWakeLockIfDone();
}

break;

case EVENT_WAKE_LOCK_TIMEOUT:
// Haven't heard back from the last request.  Assume we're
// not getting a response and  release the wake lock.
                    synchronized (mRequestList) {
if (mWakeLock.isHeld()) {
// The timer of WAKE_LOCK_TIMEOUT is reset with each
// new send request. So when WAKE_LOCK_TIMEOUT occurs
// all requests in mRequestList already waited at
// least DEFAULT_WAKE_LOCK_TIMEOUT but no response.
                            // Reset mRequestMessagesOutstanding to enable
// releaseWakeLockIfDone().
//
// Note: Keep mRequestList so that delayed response
// can still be handled when response finally comes.
                            if (mRequestMessagesOutstanding != 0) {
                                Rlog.d(LOG_TAG, "NOTE: mRequestMessagesOutstanding is NOT 0 but"
                                        + mRequestMessagesOutstanding + " at TIMEOUT, reset!"
+ " There still msg waitng for response");

                                mRequestMessagesOutstanding = 0;

if (RILJ_LOGD) {
                                    int count = mRequestList.size();
                                    Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                                            " mRequestList=" + count);

                                    for (int i = 0; i < count; i++) {
                                        rr = mRequestList.valueAt(i);
                                        Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] "
                                                + requestToString(rr.mRequest));
}
}
}
mWakeLock.release();
}
}
//Synthetic comment -- @@ -618,6 +585,8 @@
riljLog("RIL(context, preferredNetworkType=" + preferredNetworkType +
" cdmaSubscription=" + cdmaSubscription + ")");
}

        RILRequest.resetSerial();
mCdmaSubscription  = cdmaSubscription;
mPreferredNetworkType = preferredNetworkType;
mPhoneType = RILConstants.NO_PHONE;
//Synthetic comment -- @@ -627,8 +596,7 @@
mWakeLock.setReferenceCounted(false);
mWakeLockTimeout = SystemProperties.getInt(TelephonyProperties.PROPERTY_WAKE_LOCK_TIMEOUT,
DEFAULT_WAKE_LOCK_TIMEOUT);
        mRequestMessagesOutstanding = 0;

mSenderThread = new HandlerThread("RILSender");
mSenderThread.start();
//Synthetic comment -- @@ -2090,9 +2058,9 @@

private void
acquireWakeLock() {
        synchronized (mRequestList) {
mWakeLock.acquire();
            mRequestMessagesOutstanding++;

mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
Message msg = mSender.obtainMessage(EVENT_WAKE_LOCK_TIMEOUT);
//Synthetic comment -- @@ -2102,12 +2070,13 @@

private void
releaseWakeLockIfDone() {
        synchronized (mRequestList) {
            if (mWakeLock.isHeld()) {
                if (mRequestMessagesOutstanding <= 0) {
                    mRequestMessagesOutstanding = 0;
                    mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
                    mWakeLock.release();
                }
}
}
}
//Synthetic comment -- @@ -2155,12 +2124,12 @@
int count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                        " mRequestMessagesOutstanding=" + mRequestMessagesOutstanding +
" mRequestList=" + count);
}

for (int i = 0; i < count ; i++) {
                rr = mRequestList.valueAt(i);
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] " +
requestToString(rr.mRequest));
//Synthetic comment -- @@ -2168,26 +2137,23 @@
rr.onError(error, null);
rr.release();
}
            mRequestMessagesOutstanding -= count;
            if (mRequestMessagesOutstanding < 0) mRequestMessagesOutstanding = 0;
mRequestList.clear();
}
}

private RILRequest findAndRemoveRequestFromList(int serial) {
        RILRequest rr = null;
synchronized (mRequestList) {
            rr = mRequestList.get(serial);
            if (rr != null) {
                mRequestList.remove(serial);
                if (mRequestMessagesOutstanding > 0) mRequestMessagesOutstanding--;
}
}

        return rr;
}

private void
//Synthetic comment -- @@ -3881,12 +3847,11 @@
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
synchronized (mRequestList) {
          pw.println(" mRequestMessagesOutstanding=" + mRequestMessagesOutstanding);
int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {
                RILRequest rr = mRequestList.valueAt(i);
pw.println("  [" + rr.mSerial + "] " + requestToString(rr.mRequest));
}
}







