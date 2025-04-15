/*Using a single counter for outstanding requests.

Change-Id:Ie99239fbe02f4ee1528c0dc6e1a1750cad404b2c*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..ec5319d 100644

//Synthetic comment -- @@ -224,15 +224,12 @@
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
//Synthetic comment -- @@ -300,35 +297,25 @@

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

                        s = mSocket;

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
//Synthetic comment -- @@ -355,7 +342,7 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null || !alreadySubtracted) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
}
//Synthetic comment -- @@ -364,21 +351,17 @@
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
//Synthetic comment -- @@ -390,17 +373,17 @@
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
//Synthetic comment -- @@ -416,19 +399,6 @@
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
//Synthetic comment -- @@ -627,8 +597,7 @@
mWakeLock.setReferenceCounted(false);
mWakeLockTimeout = SystemProperties.getInt(TelephonyProperties.PROPERTY_WAKE_LOCK_TIMEOUT,
DEFAULT_WAKE_LOCK_TIMEOUT);
        mRequestMessagesPending = 0;
        mRequestMessagesWaiting = 0;

mSenderThread = new HandlerThread("RILSender");
mSenderThread.start();
//Synthetic comment -- @@ -2092,7 +2061,7 @@
acquireWakeLock() {
synchronized (mWakeLock) {
mWakeLock.acquire();
            mRequestMessagesPending++;

mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
Message msg = mSender.obtainMessage(EVENT_WAKE_LOCK_TIMEOUT);
//Synthetic comment -- @@ -2104,8 +2073,7 @@
releaseWakeLockIfDone() {
synchronized (mWakeLock) {
if (mWakeLock.isHeld() &&
                (mRequestMessagesPending == 0) &&
                (mRequestMessagesWaiting == 0)) {
mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
mWakeLock.release();
}
//Synthetic comment -- @@ -2155,7 +2123,7 @@
int count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                        " mReqPending=" + mRequestMessagesPending +
" mRequestList=" + count);
}

//Synthetic comment -- @@ -2168,8 +2136,9 @@
rr.onError(error, null);
rr.release();
}
mRequestList.clear();
            mRequestMessagesWaiting = 0;
}
}

//Synthetic comment -- @@ -2180,8 +2149,8 @@

if (rr.mSerial == serial) {
mRequestList.remove(i);
                    if (mRequestMessagesWaiting > 0)
                        mRequestMessagesWaiting--;
return rr;
}
}
//Synthetic comment -- @@ -3881,8 +3850,7 @@
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
synchronized (mRequestList) {
          pw.println(" mRequestMessagesPending=" + mRequestMessagesPending);
          pw.println(" mRequestMessagesWaiting=" + mRequestMessagesWaiting);
int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {







