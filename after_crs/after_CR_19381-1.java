/*Taking wakelock in PopularUrlsTest

When running PopularUrlsTest on a target device the screen goes into
sleep in the middle of the test run.

Adding aquire wakelock in setUp of PopularUrlsTests so screen does not
go black when running a long stability test. Releasing wakelock again
in the tearDown.

Change-Id:I73d225cadfb4cdbc5c2bf572391ee4be12c33c8c*/




//Synthetic comment -- diff --git a/tests/src/com/android/browser/PopularUrlsTest.java b/tests/src/com/android/browser/PopularUrlsTest.java
//Synthetic comment -- index d3806a0..873e249 100644

//Synthetic comment -- @@ -30,10 +30,12 @@
import java.util.concurrent.TimeUnit;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.os.PowerManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.webkit.HttpAuthHandler;
//Synthetic comment -- @@ -61,6 +63,7 @@

private BrowserActivity mActivity = null;
private Instrumentation mInst = null;
    private PowerManager.WakeLock mWakeLock = null;
private CountDownLatch mLatch = new CountDownLatch(1);
private RunStatus mStatus;

//Synthetic comment -- @@ -73,6 +76,12 @@
super.setUp();

mActivity = getActivity();

        // need to fix wake lock so we do not go into sleep mode
        PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        mWakeLock.acquire();

mInst = getInstrumentation();
mInst.waitForIdleSync();

//Synthetic comment -- @@ -85,6 +94,10 @@
mStatus.cleanUp();
}

        if (mWakeLock != null) {
            mWakeLock.release();
        }

super.tearDown();
}








