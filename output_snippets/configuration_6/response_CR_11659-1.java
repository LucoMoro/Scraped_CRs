//<Beginning of snippet n. 0>


Context mContext;
private Worker mRingThread;
private Handler mRingHandler;
private boolean mRingPending;
private long mFirstRingEventTime = -1;

return;
}

if (!isRingtonePlaying() && !mRingPending) {
    makeLooper();
    mRingHandler.removeCallbacksAndMessages(null);
    mRingPending = true;
    
    // Set mFirstRingEventTime only on actual ringing events, excluding synthetic delays.
    mFirstRingEventTime = SystemClock.elapsedRealtime();
    mRingHandler.sendEmptyMessage(PLAY_RING_ONCE);
} else if (mFirstRingEventTime >= 0) {
    // For repeat rings, delay by actual ring events
    if (DBG) {
        log("delaying ring based on actual ring timing");
    }
    mRingHandler.sendEmptyMessageDelayed(PLAY_RING_ONCE,
            SystemClock.elapsedRealtime() - mFirstRingEventTime);
} else {
    // We've gotten two ring events so far, but the ring hasn't started.
    mFirstRingEventTime = SystemClock.elapsedRealtime();
}

} else {
    if (DBG) log("skipping ring, already playing or pending: "
                 + mRingtone + "/" + mRingHandler);
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
        if (mFirstRingEventTime < 0) {
            mFirstRingEventTime = SystemClock.elapsedRealtime();
        }
    }
}

//<End of snippet n. 0>