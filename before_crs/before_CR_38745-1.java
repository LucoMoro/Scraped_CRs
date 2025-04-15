/*Check for OpenGL2.0 support in GlAppSwitchTest/GlVboPerfTest

Add OpenGL2.0 support check in GlAppSwitchTest and GlVboPerfTest
and skip on non supported devices.
(OpenGL2.0 support may be ommitted as per Android2.3 CDD)

Signed-off-by: SweeYee Fonn <swee.yee.fonn@intel.com>*/
//Synthetic comment -- diff --git a/tests/tests/openglperf/src/android/openglperf/cts/GlAppSwitchTest.java b/tests/tests/openglperf/src/android/openglperf/cts/GlAppSwitchTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 84514c2..de94944

//Synthetic comment -- @@ -32,6 +32,9 @@

import junit.framework.Assert;

/**
* Tests OpenGl rendering after task switching to other GL-using application
* This test needs to control two applications and task switch between them to
//Synthetic comment -- @@ -53,11 +56,17 @@
private int mTaskIdSelf;
private int mTaskIdReplica;

public GlAppSwitchTest() {
super(GlPlanetsActivity.class);
}

public void testGlActivitySwitchingFast() throws InterruptedException {
runTaskSwitchTest(TASK_SWITCH_FAST_WAIT_TIME_MS, NUMBER_OF_TASK_SWITCHES_FAST);
// wait for more time at the last run to allow watch dog timer in replica island to kick
Thread.sleep(TASK_SWITCH_SLOW_WAIT_TIME_MS);
//Synthetic comment -- @@ -65,6 +74,10 @@
}

public void testGlActivitySwitchingSlow() throws InterruptedException {
runTaskSwitchTest(TASK_SWITCH_SLOW_WAIT_TIME_MS, NUMBER_OF_TASK_SWITCHES_SLOW);
}

//Synthetic comment -- @@ -75,6 +88,16 @@
Context context = instrument.getContext();

mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

Intent intentPlanets = new Intent();
intentPlanets.putExtra(GlPlanetsActivity.INTENT_EXTRA_NUM_FRAMES, 0); //runs forever








//Synthetic comment -- diff --git a/tests/tests/openglperf/src/android/openglperf/cts/GlVboPerfTest.java b/tests/tests/openglperf/src/android/openglperf/cts/GlVboPerfTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index afd435c..bf63508

//Synthetic comment -- @@ -21,6 +21,11 @@
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class GlVboPerfTest extends
ActivityInstrumentationTestCase2<GlPlanetsActivity> {
private static final String TAG = "GlVboPerfTest";
//Synthetic comment -- @@ -34,10 +39,28 @@
private float mFps;
private int mNumTriangles;

public GlVboPerfTest() {
super(GlPlanetsActivity.class);
}

public void testVboWithVaryingIndexBufferNumbers() throws Exception {
final int[] numIndexBuffers = {1, 10, 100, 200, 400}; // per vertex buffer
float[] fpsVbo = new float[numIndexBuffers.length];
//Synthetic comment -- @@ -79,10 +102,18 @@
}

public void testVboVsNonVboPerfGeometry0() throws Exception {
doRunVboVsNonVboPerfTest(0);
}

public void testVboVsNonVboPerfGeometry1() throws Exception {
doRunVboVsNonVboPerfTest(4);
}








