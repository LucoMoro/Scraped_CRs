/*add test on turning on and off the screen

Change-Id:I4afec685d5ed34d0990cfa990ee0899c5ff244fc*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/ScreenOnOffTest.java b/tests/tests/os/src/android/os/cts/ScreenOnOffTest.java
new file mode 100644
//Synthetic comment -- index 0000000..5998c0e

//Synthetic comment -- @@ -0,0 +1,166 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.os.cts;

import android.app.cts.MockActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Window;
import android.view.WindowManager;
import dalvik.annotation.BrokenTest;

public class ScreenOnOffTest extends
        ActivityInstrumentationTestCase2<MockActivity> {

    private boolean isBroadcastReceived;

    public ScreenOnOffTest() {
        super(MockActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testTurnScreenOff() throws Exception {
        ScreenOnOffReceiver mReceiver = registerReceiver(Intent.ACTION_SCREEN_OFF);
        WakeLock mWakeLock = null;
        try {
            final PowerManager mPowerManager = (PowerManager) getInstrumentation()
                    .getContext().getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ScreenOnOffTestLock");
            if (!mPowerManager.isScreenOn()) {
                mWakeLock.acquire();
            }
            try {
                runTestOnUiThread(new Runnable() {
                    public void run() {
                        setBright(0);
                    }
                });
            } catch (Throwable e) {
            }
            mReceiver.waitForCalls(1, 2000);
            assertTrue(isBroadcastReceived);
        } finally {
            unregisterReceiver(mReceiver);
            if (mWakeLock != null && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
    }

    @BrokenTest("Can't turn the screen on after turning screen off")
    public void testTurnScreenOn() throws Exception {
        ScreenOnOffReceiver mReceiver = registerReceiver(Intent.ACTION_SCREEN_ON);
        WakeLock mWakeLock = null;
        try {
            final PowerManager mPowerManager = (PowerManager) getInstrumentation()
                   .getContext().getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK
                   | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ScreenOnOffTestLock");
            if (mPowerManager.isScreenOn()) {
                try {
                    runTestOnUiThread(new Runnable() {
                        public void run() {
                            setBright(0);
                        }
                    });
                } catch (Throwable e) {
                }
            }
            getInstrumentation().waitForIdleSync();
            mWakeLock.acquire();
            mReceiver.waitForCalls(1, 2000);
            assertTrue(isBroadcastReceived);
        } finally {
            unregisterReceiver(mReceiver);
            if (mWakeLock != null) {
                mWakeLock.release();
            }
        }
    }

    private ScreenOnOffReceiver registerReceiver(String action) {
        isBroadcastReceived = false;
        ScreenOnOffReceiver mReceiver = new ScreenOnOffReceiver(action);
        IntentFilter filter = new IntentFilter(action);
        getInstrumentation().getTargetContext().registerReceiver(mReceiver,
                filter);
        return mReceiver;
    }

    private void unregisterReceiver(ScreenOnOffReceiver receiver) {
        isBroadcastReceived = false;
        getInstrumentation().getTargetContext().unregisterReceiver(receiver);
    }

    public void setBright(float value) {
        Window mywindow = getActivity().getWindow();
        WindowManager.LayoutParams lp = mywindow.getAttributes();
        lp.screenBrightness = value;
        mywindow.setAttributes(lp);
    }

    private class ScreenOnOffReceiver extends BroadcastReceiver {
        private int mCalls;
        private int mExpectedCalls;
        private Object mLock;
        private String mAction;

        ScreenOnOffReceiver(String action) {
            mAction = action;
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
                    isBroadcastReceived = true;
                    mCalls += 1;
                    if (mCalls >= mExpectedCalls) {
                        mLock.notify();
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
}







