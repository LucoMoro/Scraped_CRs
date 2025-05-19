
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
mRingtone = null;
mFirstRingEventTime = -1;
mFirstRingStartTime = -1;
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

//<End of snippet n. 0>








