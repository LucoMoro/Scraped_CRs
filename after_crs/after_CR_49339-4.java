/*Using a single counter for outstanding requests.

Change-Id:Ie99239fbe02f4ee1528c0dc6e1a1750cad404b2c*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..81c97ef 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
* {@hide}
//Synthetic comment -- @@ -76,8 +77,8 @@
static final String LOG_TAG = "RILJ";

//***** Class Variables
    static AtomicInteger sNextSerial = new AtomicInteger(0);
    static AtomicInteger sGenerationNumber = new AtomicInteger(0);
private static Object sPoolSync = new Object();
private static RILRequest sPool = null;
private static int sPoolSize = 0;
//Synthetic comment -- @@ -89,6 +90,7 @@
Message mResult;
Parcel mp;
RILRequest mNext;
    int mGeneration;

/**
* Retrieves a new RILRequest instance from the pool.
//Synthetic comment -- @@ -113,9 +115,9 @@
rr = new RILRequest();
}

        rr.mSerial = sNextSerial.getAndIncrement();
        rr.mGeneration = sGenerationNumber.get();

rr.mRequest = request;
rr.mResult = result;
rr.mp = Parcel.obtain();
//Synthetic comment -- @@ -152,9 +154,7 @@

static void
resetSerial() {
        sNextSerial.set(0);
}

String
//Synthetic comment -- @@ -224,15 +224,12 @@
RILReceiver mReceiver;
WakeLock mWakeLock;
int mWakeLockTimeout;
    // The number of requests outstanding - we've receive the request
    // but haven't gotten the response yet.  It increases before calling
    // EVENT_SEND and decreases while handling a response.  It should match
    // mRequestList.size() unless there are requests not replied to when
// WAKE_LOCK_TIMEOUT occurs.
    int mRequestMessagesOutstanding;

//I'd rather this be LinkedList or something
ArrayList<RILRequest> mRequestList = new ArrayList<RILRequest>();
//Synthetic comment -- @@ -300,35 +297,25 @@

switch (msg.what) {
case EVENT_SEND:
                    LocalSocket s;

                    s = mSocket;

                    if (s == null) {
                        rr.onError(RADIO_NOT_AVAILABLE, null);
                        rr.release();
                        synchronized (mRequestList) {
                            if (mRequestMessagesOutstanding > 0) mRequestMessagesOutstanding--;
}
                        releaseWakeLockIfDone();
                        return;
                    }

                    try {
synchronized (mRequestList) {
mRequestList.add(rr);
}

byte[] data;

data = rr.mp.marshall();
//Synthetic comment -- @@ -355,7 +342,7 @@
req = findAndRemoveRequestFromList(rr.mSerial);
// make sure this request has not already been handled,
// eg, if RILReceiver cleared the list.
                        if (req != null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
}
//Synthetic comment -- @@ -364,21 +351,17 @@
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
//Synthetic comment -- @@ -390,20 +373,19 @@
// new send request. So when WAKE_LOCK_TIMEOUT occurs
// all requests in mRequestList already waited at
// least DEFAULT_WAKE_LOCK_TIMEOUT but no response.
                            // Reset mRequestMessagesOutstanding to enable
// releaseWakeLockIfDone().
//
// Note: Keep mRequestList so that delayed response
// can still be handled when response finally comes.
                            synchronized (mRequestList) {
                                if (mRequestMessagesOutstanding != 0) {
                                    mRequestMessagesOutstanding = 0;
                                    Rlog.d(LOG_TAG, "NOTE: mRequestMessagesOutstanding is NOT 0 but"
                                            + mRequestMessagesOutstanding + " at TIMEOUT, reset!"
                                            + " There still msg waitng for response");

                                    if (RILJ_LOGD) {
int count = mRequestList.size();
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mRequestList=" + count);
//Synthetic comment -- @@ -416,19 +398,6 @@
}
}
}
mWakeLock.release();
}
}
//Synthetic comment -- @@ -627,8 +596,7 @@
mWakeLock.setReferenceCounted(false);
mWakeLockTimeout = SystemProperties.getInt(TelephonyProperties.PROPERTY_WAKE_LOCK_TIMEOUT,
DEFAULT_WAKE_LOCK_TIMEOUT);
        mRequestMessagesOutstanding = 0;

mSenderThread = new HandlerThread("RILSender");
mSenderThread.start();
//Synthetic comment -- @@ -2089,10 +2057,21 @@
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
            synchronized (mRequestList) {
                mRequestMessagesOutstanding++;
            }

mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
Message msg = mSender.obtainMessage(EVENT_WAKE_LOCK_TIMEOUT);
//Synthetic comment -- @@ -2103,11 +2082,13 @@
private void
releaseWakeLockIfDone() {
synchronized (mWakeLock) {
            if (mWakeLock.isHeld()) {
                synchronized (mRequestList) {
                    if (mRequestMessagesOutstanding <= 0) {
                        mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
                        mWakeLock.release();
                    }
                }
}
}
}
//Synthetic comment -- @@ -2152,10 +2133,11 @@
private void clearRequestList(int error, boolean loggable) {
RILRequest rr;
synchronized (mRequestList) {
            RILRequest.sGenerationNumber.getAndIncrement();
int count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
                        " mRequestMessagesOutstanding=" + mRequestMessagesOutstanding +
" mRequestList=" + count);
}

//Synthetic comment -- @@ -2168,8 +2150,9 @@
rr.onError(error, null);
rr.release();
}
            mRequestMessagesOutstanding -= count;
            if (mRequestMessagesOutstanding < 0) mRequestMessagesOutstanding = 0;
mRequestList.clear();
}
}

//Synthetic comment -- @@ -2180,8 +2163,12 @@

if (rr.mSerial == serial) {
mRequestList.remove(i);

                    // only dec if this request is from the current generation
                    // and not from before we reset the Outstanding count
                    if (RILRequest.sGenerationNumber.get() == rr.mGeneration) {
                        if (mRequestMessagesOutstanding > 0) mRequestMessagesOutstanding--;
                    }
return rr;
}
}
//Synthetic comment -- @@ -3881,8 +3868,7 @@
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
synchronized (mRequestList) {
          pw.println(" mRequestMessagesOutstanding=" + mRequestMessagesOutstanding);
int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {







