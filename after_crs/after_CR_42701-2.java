/*Fix high current drain issue

The log shows the cause of the high current drain was many wakelock
were acquired by RILJ, and were not released until the hardware was
power cycled.
So, the fix is: added mutex protection around accesses of a global
variable to count instances of a wake lock.

Change-Id:I2df952417082c78f922edb3373df61c9f2843f44*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c359652..f735bda 100755

//Synthetic comment -- @@ -225,6 +225,9 @@
// EVENT_SEND and decreases while handling EVENT_SEND. It gets cleared while
// WAKE_LOCK_TIMEOUT occurs.
int mRequestMessagesPending;

    private final Object mPendingLock = new Object();

// The number of requests sent out but waiting for response. It increases while
// sending request and decreases while handling response. It should match
// mRequestList.size() unless there are requests no replied while
//Synthetic comment -- @@ -311,8 +314,10 @@
if (s == null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
                            synchronized (mPendingLock) {
                                if (mRequestMessagesPending > 0)
                                    mRequestMessagesPending--;
                            }
alreadySubtracted = true;
return;
}
//Synthetic comment -- @@ -322,8 +327,10 @@
mRequestMessagesWaiting++;
}

                        synchronized (mPendingLock) {
                            if (mRequestMessagesPending > 0)
                                mRequestMessagesPending--;
                        }
alreadySubtracted = true;

byte[] data;
//Synthetic comment -- @@ -372,8 +379,10 @@
releaseWakeLockIfDone();
}

                    synchronized (mPendingLock) {
                        if (!alreadySubtracted && mRequestMessagesPending > 0) {
                            mRequestMessagesPending--;
                        }
}

break;
//Synthetic comment -- @@ -420,11 +429,12 @@
// is the expected time to get response, all requests
// should already sent out (i.e.
// mRequestMessagesPending is 0 )while TIMEOUT occurs.
                            synchronized (mPendingLock) {
                                if (mRequestMessagesPending != 0) {
                                    Log.e(LOG_TAG, "ERROR: mReqPending is NOT 0 but"
                                            + mRequestMessagesPending + " at TIMEOUT, reset!");
                                    mRequestMessagesPending = 0;
                                }
}
mWakeLock.release();
}
//Synthetic comment -- @@ -2081,19 +2091,26 @@
acquireWakeLock() {
synchronized (mWakeLock) {
mWakeLock.acquire();

mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
Message msg = mSender.obtainMessage(EVENT_WAKE_LOCK_TIMEOUT);
mSender.sendMessageDelayed(msg, mWakeLockTimeout);
}
        synchronized (mPendingLock) {
            mRequestMessagesPending++;
        }
}

private void
releaseWakeLockIfDone() {
        boolean hasMessagesPending = true;

        synchronized (mPendingLock) {
            if (mRequestMessagesPending == 0) hasMessagesPending = false;
        }
synchronized (mWakeLock) {
if (mWakeLock.isHeld() &&
                !hasMessagesPending &&
(mRequestMessagesWaiting == 0)) {
mSender.removeMessages(EVENT_WAKE_LOCK_TIMEOUT);
mWakeLock.release();







