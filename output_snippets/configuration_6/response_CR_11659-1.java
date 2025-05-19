//<Beginning of snippet n. 0>


Context mContext;
private Worker mRingThread;
private Handler mRingHandler;
private boolean mRingPending;
private long mFirstRingEventTime = -1;
private long mFirstRingStartTime = -1;

return;
}

if (!isRingtonePlaying()) {
    makeLooper();
    mRingHandler.removeCallbacksAndMessages(null);
    mRingPending = true;

    if (mFirstRingEventTime < 0) {
        mFirstRingEventTime = SystemClock.elapsedRealtime();
        mRingHandler.sendEmptyMessage(PLAY_RING_ONCE);
    } else {
        long delay = calculateDelay(mFirstRingStartTime, mFirstRingEventTime);
        if (DBG) {
            log("delaying ring by " + delay);
        }
        mRingHandler.sendEmptyMessageDelayed(PLAY_RING_ONCE, delay);
    }
} else {
    if (DBG) log("skipping ring, already playing or pending: "
                 + mRingtone + "/" + mRingHandler);
}
}
}
mRingtone = null;
mFirstRingEventTime = -1;
mRingPending = false;
} else {
    if (DBG) log("- stopRing: null mRingHandler!");
}
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
    }
}

// Ensure mFirstRingStartTime is reset only after a successful ringing event
if (mRingPending) {
    // mFirstRingStartTime should not be reset during delays
    // So removing the resetting logic here
}

//<End of snippet n. 0>