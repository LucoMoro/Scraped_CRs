/*Change the way the Phone app determines when to skip rings while ringing for
incoming phone calls, to fix a problem where a long initial delay before the
first ring (a busy phone) would cause the phone to needlessly skip successive
rings.  In short, the Phone app was considering the synthetic delay time as
a period where it was actively ringing, causing the phone to not queue
successive ring events triggered by the RIL (which themselves would have been
delayed, so would not have overlapped).

For a more thorough explanation, please see the writeup onhttp://code.google.com/p/android/issues/detail?id=3652*/
//Synthetic comment -- diff --git a/src/com/android/phone/Ringer.java b/src/com/android/phone/Ringer.java
//Synthetic comment -- index eec0323..8ec3160 100644

//Synthetic comment -- @@ -58,7 +58,6 @@
Context mContext;
private Worker mRingThread;
private Handler mRingHandler;
    private boolean mRingPending;
private long mFirstRingEventTime = -1;
private long mFirstRingStartTime = -1;

//Synthetic comment -- @@ -149,35 +148,28 @@
return;
}

            if (!isRingtonePlaying() && !mRingPending) {
                makeLooper();
                mRingHandler.removeCallbacksAndMessages(null);
                mRingPending = true;
                if (mFirstRingEventTime < 0) {
                    mFirstRingEventTime = SystemClock.elapsedRealtime();
                    mRingHandler.sendEmptyMessage(PLAY_RING_ONCE);
                } else {
                    // For repeat rings, figure out by how much to delay
                    // the ring so that it happens the correct amount of
                    // time after the previous ring
                    if (mFirstRingStartTime > 0) {
                        // Delay subsequent rings by the delta between event
                        // and play time of the first ring
                        if (DBG) {
                            log("delaying ring by " + (mFirstRingStartTime - mFirstRingEventTime));
                        }
                        mRingHandler.sendEmptyMessageDelayed(PLAY_RING_ONCE,
                                mFirstRingStartTime - mFirstRingEventTime);
                    } else {
                        // We've gotten two ring events so far, but the ring
                        // still hasn't started. Reset the event time to the
                        // time of this event to maintain correct spacing.
                        mFirstRingEventTime = SystemClock.elapsedRealtime();
                    }
                }
} else {
                if (DBG) log("skipping ring, already playing or pending: "
                             + mRingtone + "/" + mRingHandler);
}
}
}
//Synthetic comment -- @@ -212,7 +204,6 @@
mRingtone = null;
mFirstRingEventTime = -1;
mFirstRingStartTime = -1;
                mRingPending = false;
} else {
if (DBG) log("- stopRing: null mRingHandler!");
}
//Synthetic comment -- @@ -302,11 +293,10 @@
}
}
r = mRingtone;
                            if (r != null && !hasMessages(STOP_RING)) {
PhoneUtils.setAudioMode(mContext, AudioManager.MODE_RINGTONE);
r.play();
synchronized (Ringer.this) {
                                    mRingPending = false;
if (mFirstRingStartTime < 0) {
mFirstRingStartTime = SystemClock.elapsedRealtime();
}







