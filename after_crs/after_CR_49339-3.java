/*Using a single counter for outstanding requests.

Change-Id:Ie99239fbe02f4ee1528c0dc6e1a1750cad404b2c*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..8518c7e 100644

//Synthetic comment -- @@ -224,15 +224,12 @@
RILReceiver mReceiver;
WakeLock mWakeLock;
int mWakeLockTimeout;
    // The number of requests outstanding - we've receive the request
    // but haven't gotten the response yet.  It increases before calling
    // EVENT_SEND and decreases while handling a response.  It should match
    // mRequestList.size() unless there are requests not replied to when
// WAKE_LOCK_TIMEOUT occurs.
    AtomicInteger mRequestMessagesOutstanding = new AtomicInteger(0);

//I'd rather this be LinkedList or something
ArrayList<RILRequest> mRequestList = new ArrayList<RILRequest>();
//Synthetic comment -- @@ -300,35 +297,23 @@

switch (msg.what) {
case EVENT_SEND:
                    LocalSocket s;

                    s = mSocket;

                    if (s == null) {
                        rr.onError(RADIO_NOT_AVAILABLE, null);
                        rr.release();
                        decrementWithFloor(mRequestMessagesOutstanding);
                        releaseWakeLockIfDone();
                        return;
                    }

try {
synchronized (mRequestList) {
mRequestList.add(rr);
}

byte[] data;

data = rr.mp.marshall();
//Synthetic comment -- @@ -355,7 +340,7 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
}
//Synthetic comment -- @@ -364,21 +349,17 @@
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
//Synthetic comment -- @@ -390,18 +371,16 @@
// new send request. So when WAKE_LOCK_TIMEOUT occurs
// all requests in mRequestList already waited at
// least DEFAULT_WAKE_LOCK_TIMEOUT but no response.
                            // Reset mRequestMessagesOutstanding to enable
// releaseWakeLockIfDone().
//
// Note: Keep mRequestList so that delayed response
// can still be handled when response finally comes.
                            if (mRequestMessagesOutstanding.getAndSet(0) != 0) {
                                Rlog.d(LOG_TAG, "NOTE: mRequestMessagesOutstanding is NOT 0 but"
                                        + mRequestMessagesOutstanding + " at TIMEOUT, reset!"
+ " There still msg waitng for response");

if (RILJ_LOGD) {
synchronized (mRequestList) {
int count = mRequestList.size();
//Synthetic comment -- @@ -416,19 +395,6 @@
}
}
}
mWakeLock.release();
}
}
//Synthetic comment -- @@ -627,8 +593,7 @@
mWakeLock.setReferenceCounted(false);
mWakeLockTimeout = SystemProperties.getInt(TelephonyProperties.PROPERTY_WAKE_LOCK_TIMEOUT,
DEFAULT_WAKE_LOCK_TIMEOUT);
        mRequestMessagesOutstanding.set(0);

mSenderThread = new HandlerThread("RILSender");
mSenderThread.start();
//Synthetic comment -- @@ -2089,10 +2054,19 @@
*/

private void
    decrementWithFloor(AtomicInteger i) {
        /* note this function has a sync hole, but this whole file has a sync hole unless
         * we introduce track generation numbers or something on list-clear.  We
         * don't think it's worth the bother and this will self-correct in time */
        int old = i.getAndDecrement();
        if (old <= 0) i.incrementAndGet();
    }

    private void
acquireWakeLock() {
synchronized (mWakeLock) {
mWakeLock.acquire();
            mRequestMessagesOutstanding.getAndIncrement();

mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
Message msg = mSender.obtainMessage(EVENT_WAKE_LOCK_TIMEOUT);
//Synthetic comment -- @@ -2104,8 +2078,7 @@
releaseWakeLockIfDone() {
synchronized (mWakeLock) {
if (mWakeLock.isHeld() &&
                (mRequestMessagesOutstanding.get() <= 0)) {
mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
mWakeLock.release();
}
//Synthetic comment -- @@ -2155,7 +2128,7 @@
int count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                        " mRequestMessagesOutstanding=" + mRequestMessagesOutstanding.get() +
" mRequestList=" + count);
}

//Synthetic comment -- @@ -2168,8 +2141,10 @@
rr.onError(error, null);
rr.release();
}
            for(; count > 0; count--) {
                decrementWithFloor(mRequestMessagesOutstanding);
            }
mRequestList.clear();
}
}

//Synthetic comment -- @@ -2180,8 +2155,7 @@

if (rr.mSerial == serial) {
mRequestList.remove(i);
                    decrementWithFloor(mRequestMessagesOutstanding);
return rr;
}
}
//Synthetic comment -- @@ -3881,8 +3855,7 @@
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
synchronized (mRequestList) {
          pw.println(" mRequestMessagesOutstanding=" + mRequestMessagesOutstanding.get());
int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {







