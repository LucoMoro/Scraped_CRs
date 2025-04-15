/*Test for GLSurfaceView#setEGLContextClientVersion

Bug 2845550

Check that the GL version string reports 1.0 by default and
then reports 2.0 when changing the client version.

Change-Id:I240f30fb2a19b5e168bb5ba0c0da23268895b31d*/
//Synthetic comment -- diff --git a/tests/src/android/opengl/cts/GLSurfaceViewStubActivity.java b/tests/src/android/opengl/cts/GLSurfaceViewStubActivity.java
//Synthetic comment -- index 5a8f310..753b14f 100644

//Synthetic comment -- @@ -17,9 +17,13 @@
package android.opengl.cts;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//Synthetic comment -- @@ -28,7 +32,9 @@
*/
public class GLSurfaceViewStubActivity extends Activity {

    private static class Renderer implements GLSurfaceView.Renderer {

public void onDrawFrame(GL10 gl) {
// Do nothing.
//Synthetic comment -- @@ -39,16 +45,34 @@
}

public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Do nothing.
}
}

private GLSurfaceView mView;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
mView = new GLSurfaceView(this);
mView.setRenderer(new Renderer());
setContentView(mView);
}
//Synthetic comment -- @@ -57,6 +81,13 @@
return mView;
}

@Override
protected void onResume() {
super.onResume();








//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/opengl/cts/GLSurfaceViewTest.java b/tests/tests/graphics/src/android/opengl/cts/GLSurfaceViewTest.java
//Synthetic comment -- index 4f55f96..e32dd3c 100644

//Synthetic comment -- @@ -16,12 +16,17 @@

package android.opengl.cts;

import android.opengl.GLSurfaceView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;

import dalvik.annotation.TestTargetClass;

/**
* Tests for the GLSurfaceView class.
//Synthetic comment -- @@ -102,4 +107,38 @@
view.onResume();
}
}
}







