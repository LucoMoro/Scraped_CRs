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
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//Synthetic comment -- @@ -28,7 +32,9 @@
*/
public class GLSurfaceViewStubActivity extends Activity {

    private static final String EGL_CONTEXT_CLIENT_VERSION = "eglContextClientVersion";

    private class Renderer implements GLSurfaceView.Renderer {

public void onDrawFrame(GL10 gl) {
// Do nothing.
//Synthetic comment -- @@ -39,16 +45,34 @@
}

public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            synchronized (GLSurfaceViewStubActivity.this) {
                mVersion = gl.glGetString(GL10.GL_VERSION);
            }
            mVersionLatch.countDown();
}
}

private GLSurfaceView mView;

    private String mVersion;

    private CountDownLatch mVersionLatch = new CountDownLatch(1);

    public static Intent createClientVersionIntent(int eglContextClientVersion) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra(EGL_CONTEXT_CLIENT_VERSION, eglContextClientVersion);
        return intent;
    }

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
mView = new GLSurfaceView(this);

        Intent intent = getIntent();
        int eglContextClientVersion = intent.getIntExtra(EGL_CONTEXT_CLIENT_VERSION, 1);
        mView.setEGLContextClientVersion(eglContextClientVersion);

mView.setRenderer(new Renderer());
setContentView(mView);
}
//Synthetic comment -- @@ -57,6 +81,13 @@
return mView;
}

    public String getVersion() throws InterruptedException {
        mVersionLatch.await(10, TimeUnit.SECONDS);
        synchronized (this) {
            return mVersion;
        }
    }

@Override
protected void onResume() {
super.onResume();








//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/opengl/cts/GLSurfaceViewTest.java b/tests/tests/graphics/src/android/opengl/cts/GLSurfaceViewTest.java
//Synthetic comment -- index 4f55f96..e32dd3c 100644

//Synthetic comment -- @@ -16,12 +16,17 @@

package android.opengl.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;

import java.util.regex.Pattern;

/**
* Tests for the GLSurfaceView class.
//Synthetic comment -- @@ -102,4 +107,38 @@
view.onResume();
}
}

    @TestTargetNew(
        level = TestLevel.PARTIAL,
        method = "setEGLContextClientVersion",
        args = {}
    )
    public void testSetEGLContextClientVersion() throws InterruptedException {
        assertClientVersion(1);

        restartActivityWithClientVersion(2);
        assertClientVersion(2);
    }

    /**
     * Check that the version string has some form of "Open GL ES X.Y" in it where X is the major
     * version and Y must be some digit.
     */
    private void assertClientVersion(int majorVersion) throws InterruptedException {
        String message = "OpenGL version '" + mActivity.getVersion()
                + "' should be version " + majorVersion + ".0+.";
        assertTrue(message, Pattern.matches(".*OpenGL.*ES.*" + majorVersion + "\\.\\d.*",
                mActivity.getVersion()));
    }

    /** Restart {@link GLSurfaceViewStubActivity} with a specific client version. */
    private void restartActivityWithClientVersion(int version) {
        mActivity.finish();
        setActivity(null);

        Intent intent = GLSurfaceViewStubActivity.createClientVersionIntent(version);
        setActivityIntent(intent);

        mActivity = getActivity();
    }
}







