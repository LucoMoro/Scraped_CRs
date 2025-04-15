/*add broadcast receiver RingModeReceiver to test the notification of ringer mode changes from AudioManager
Signed-off-by: Zhihong GUO <zhihong.guo@orange-ftgroup.com>

Change-Id:Ie5fb053bfd4969f3673e4f354069209ba59b794d*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index b4053cf..d152a98 100644

//Synthetic comment -- @@ -45,7 +45,10 @@
import dalvik.annotation.ToBeFixed;

import android.app.cts.CTSResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
//Synthetic comment -- @@ -61,6 +64,11 @@
private int mResultCode;
private Sync mSync = new Sync();

    private static final int TIME_OUT = 1000 * 4;
    private boolean isBroadcastReceived = false;
    private int ringMode = -1;
    private RingModeReceiver mReceiver = new RingModeReceiver();

private static class Sync {
private boolean notified;

//Synthetic comment -- @@ -621,4 +629,83 @@
mSync.notifyResult();
mResultCode = resultCode;
}

    private class RingModeReceiver extends BroadcastReceiver {
        private int mCalls;
        private int mExpectedCalls;
        private Object mLock;
        private String mAction;

        RingModeReceiver() {
            mAction = AudioManager.RINGER_MODE_CHANGED_ACTION;
            reset();
            mLock = new Object();
        }

        void reset() {
            mExpectedCalls = Integer.MAX_VALUE;
            mCalls = 0;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mAction)) {
                synchronized (mLock) {
                    if (mAudioManager.getRingerMode() == ringMode) {
                        isBroadcastReceived = true;
                        mCalls += 1;
                        if (mCalls >= mExpectedCalls) {
                            mLock.notify();
                        }
                    }
                }
            }
        }

        public void waitForCalls(int expectedCalls, long timeout)
            throws InterruptedException {
            synchronized (mLock) {
                mExpectedCalls = expectedCalls;
                if (mCalls < mExpectedCalls) {
                    mLock.wait(timeout);
                }
            }
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        getContext().registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(mReceiver);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getRingerMode",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setRingerMode",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "RingModeReceiver.onReceive",
            args = {Context.class, Intent.class }
        )
    })
    public void testRingModeReceiver() throws InterruptedException {
        isBroadcastReceived = false;
        registerReceiver();
        ringMode = (mAudioManager.getRingerMode()+1) % 3;
        mAudioManager.setRingerMode(ringMode);
        mReceiver.waitForCalls(1, TIME_OUT);
        unregisterReceiver();
        assertTrue(isBroadcastReceived);
    }
}







