/*Check for OpenGL2.0 support in GlAppSwitchTest/GlVboPerfTest

Add OpenGL2.0 support check in GlAppSwitchTest and GlVboPerfTest
and skip on non supported devices.
(OpenGL2.0 support may be ommitted as per Android2.3 CDD)

Signed-off-by: SweeYee Fonn <swee.yee.fonn@intel.com>*/




//Synthetic comment -- diff --git a/tests/tests/openglperf/src/android/openglperf/cts/GlAppSwitchTest.java b/tests/tests/openglperf/src/android/openglperf/cts/GlAppSwitchTest.java
//Synthetic comment -- index 84514c2..7d4167d 100644

//Synthetic comment -- @@ -32,6 +32,9 @@

import junit.framework.Assert;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.util.Log;
/**
* Tests OpenGl rendering after task switching to other GL-using application
* This test needs to control two applications and task switch between them to
//Synthetic comment -- @@ -53,11 +56,17 @@
private int mTaskIdSelf;
private int mTaskIdReplica;

    private boolean mSupportsGLES20;
    private static final String TAG = "GlAppSwitchTest";
public GlAppSwitchTest() {
super(GlPlanetsActivity.class);
}

public void testGlActivitySwitchingFast() throws InterruptedException {
        if (mSupportsGLES20 == false) {
            Log.i(TAG, "OpenGL ES2.0 not supported, skip testGlActivitySwitchingFast.");
            return;
        }
runTaskSwitchTest(TASK_SWITCH_FAST_WAIT_TIME_MS, NUMBER_OF_TASK_SWITCHES_FAST);
// wait for more time at the last run to allow watch dog timer in replica island to kick
Thread.sleep(TASK_SWITCH_SLOW_WAIT_TIME_MS);
//Synthetic comment -- @@ -65,6 +74,10 @@
}

public void testGlActivitySwitchingSlow() throws InterruptedException {
        if (mSupportsGLES20 == false) {
            Log.i(TAG, "OpenGL ES2.0 not supported, skip testGlActivitySwitchingSlow.");
            return;
        }
runTaskSwitchTest(TASK_SWITCH_SLOW_WAIT_TIME_MS, NUMBER_OF_TASK_SWITCHES_SLOW);
}

//Synthetic comment -- @@ -75,6 +88,18 @@
Context context = instrument.getContext();

mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        /**
        * Detects if OpenGL ES 2.0 exists
        */
        ConfigurationInfo info = mActivityManager.getDeviceConfigurationInfo();
        Log.i(TAG, "OpenGL Ver:" + info.getGlEsVersion());
        if (info.reqGlEsVersion >= 0x20000) {
            mSupportsGLES20 = true;
        }
        if (mSupportsGLES20 == false) {
            Log.i(TAG, "OpenGL ES2.0 not supported, skip setup.");
            return;
        }

Intent intentPlanets = new Intent();
intentPlanets.putExtra(GlPlanetsActivity.INTENT_EXTRA_NUM_FRAMES, 0); //runs forever








//Synthetic comment -- diff --git a/tests/tests/openglperf/src/android/openglperf/cts/GlVboPerfTest.java b/tests/tests/openglperf/src/android/openglperf/cts/GlVboPerfTest.java
//Synthetic comment -- index afd435c..806db47 100644

//Synthetic comment -- @@ -21,6 +21,11 @@
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import android.app.Instrumentation;
import android.content.Context;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;

public class GlVboPerfTest extends
ActivityInstrumentationTestCase2<GlPlanetsActivity> {
private static final String TAG = "GlVboPerfTest";
//Synthetic comment -- @@ -34,10 +39,30 @@
private float mFps;
private int mNumTriangles;

    private boolean mSupportsGLES20;

public GlVboPerfTest() {
super(GlPlanetsActivity.class);
}

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
        * Detects if OpenGL ES 2.0 exists
        */
        Instrumentation instrument = getInstrumentation();
        Context context = instrument.getContext();

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        Log.i(TAG, "OpenGL Ver:" + info.getGlEsVersion());
        if (info.reqGlEsVersion >= 0x20000) {
            mSupportsGLES20 = true;
        }
    }

public void testVboWithVaryingIndexBufferNumbers() throws Exception {
final int[] numIndexBuffers = {1, 10, 100, 200, 400}; // per vertex buffer
float[] fpsVbo = new float[numIndexBuffers.length];
//Synthetic comment -- @@ -79,10 +104,18 @@
}

public void testVboVsNonVboPerfGeometry0() throws Exception {
        if (mSupportsGLES20 == false) {
            Log.i(TAG, "OpenGL ES2.0 not supported, skip testVboVsNonVboPerfGeometry0.");
            return;
        }
doRunVboVsNonVboPerfTest(0);
}

public void testVboVsNonVboPerfGeometry1() throws Exception {
        if (mSupportsGLES20 == false) {
            Log.i(TAG, "OpenGL ES2.0 not supported, skip testVboVsNonVboPerfGeometry1.");
            return;
        }
doRunVboVsNonVboPerfTest(4);
}








