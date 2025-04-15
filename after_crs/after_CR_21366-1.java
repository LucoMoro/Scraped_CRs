/*Add wait in testOnWindowFocusChanged.

This test case sometimes fails when isOnWindowFocusChangedCalled is done before asynchronous callback of onWindowFocusChanged is done, which is called by showDialog().

To avoid this, I added wait so that asynchronous process of onWindowFocusChanged is finished completely.*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index 4d02896..28c2c20 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import android.os.Looper;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
//Synthetic comment -- @@ -60,6 +61,7 @@
private static final float MOTION_X = -20.0f;
private static final float MOTION_Y = -20.0f;
private static final String STUB_ACTIVITY_PACKAGE = "com.android.cts.stub";
    private static final long TEST_TIMEOUT = 1000L;

/**
*  please refer to Dialog
//Synthetic comment -- @@ -698,7 +700,12 @@
});
mInstrumentation.waitForIdleSync();

        // Wait until TestDialog#OnWindowFocusChanged() is called
        new DelayedCheck(TEST_TIMEOUT) {
            protected boolean check() {
                return d.isOnWindowFocusChangedCalled;
            }
        }.run();
}

@TestTargets({







