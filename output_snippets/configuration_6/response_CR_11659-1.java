//<Beginning of snippet n. 0>


Context mContext;
private Worker mRingThread;
private Handler mRingHandler;
private boolean mRingPending;
private long mFirstRingEventTime = -1;
private long mFirstRingStartTime = -1;

return;
}

if (!isRingtonePlaying() && !mRingPending) {
    makeLooper();
    mRingHandler.removeCallbacksAndMessages(null);
    mRingPending = true;

    long currentTime = SystemClock.elapsedRealtime();

    if (mFirstRingStartTime < 0) {
        mFirstRingEventTime = currentTime;
        mFirstRingStartTime = currentTime; // Set start time for first ring
        mRingHandler.sendEmptyMessage(PLAY_RING_ONCE);
    } else {
        mFirstRingEventTime = currentTime; // Reset event time for subsequent events
        if (DBG) {
            log("delaying ring by " + (mFirstRingStartTime - mFirstRingEventTime));
        }
        mRingHandler.sendEmptyMessageDelayed(PLAY_RING_ONCE, mFirstRingStartTime - mFirstRingEventTime);
    }
} else {
    if (DBG) log("skipping ring, already playing or pending: " + mRingtone + "/" + mRingHandler);
}

mRingtone = null;
mFirstRingEventTime = -1;
mFirstRingStartTime = -1;
mRingPending = false;

if (r != null && !hasMessages(STOP_RING)) {
    PhoneUtils.setAudioMode(mContext, AudioManager.MODE_RINGTONE);
    r.play();
    synchronized (Ringer.this) {
        mRingPending = false;
        if (mFirstRingStartTime < 0) {
            mFirstRingStartTime = SystemClock.elapsedRealtime(); 
        }
    }
}
//<End of snippet n. 0>