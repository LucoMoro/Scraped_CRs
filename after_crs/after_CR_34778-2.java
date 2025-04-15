/*use front camera if rear isn't defined.

fix issue cts fails if a device has a front camera without rear camera.
-use front camera instead of rear when rear camera isn't declared
 in feature definitions.
-skip a test case for rear camera only if rear isn't declared.

Signed-off-by: fujita <fujita@brilliantservice.co.jp>
Change-Id:I5d88c0281ea125fa97dda173ed4c6051f7c931e1*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java b/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java
//Synthetic comment -- index 990ab9b..08c3280 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.media.CamcorderProfile;
import android.test.AndroidTestCase;
import android.util.Log;
import android.content.pm.PackageManager;

import java.util.List;

//Synthetic comment -- @@ -70,6 +71,10 @@
)
})
public void testGet() {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return;
        }
int nCamera = Camera.getNumberOfCameras();
if (nCamera != 0) {
CamcorderProfile lowProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);








//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index fd4e3b3..b6b1e32 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package android.mediastress.cts;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
//Synthetic comment -- @@ -69,11 +70,18 @@
private boolean mRemoveVideo = true;
private int mRecordDuration = 5000;

    private boolean hasRearCamera = false;
    private boolean hasFrontCamera = false;

public MediaRecorderStressTest() {
super(MediaFrameworkTest.class);
}

protected void setUp() throws Exception {
        PackageManager packageManager =
                getInstrumentation().getTargetContext().getPackageManager();
        hasRearCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        hasFrontCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
int cameraId = 0;
CamcorderProfile profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
mVideoEncoder = profile.videoCodec;
//Synthetic comment -- @@ -170,9 +178,18 @@
runOnLooper(new Runnable() {
@Override
public void run() {
                    if (hasRearCamera) {
                        mCamera = Camera.open();
                    } else if (hasFrontCamera) {
                        mCamera = Camera.open(0);
                    } else {
                        mCamera = null;
                    }
}
});
            if (mCamera == null) {
                break;
            }
mCamera.setErrorCallback(mCameraErrorCallback);
mCamera.setPreviewDisplay(mSurfaceHolder);
mCamera.startPreview();
//Synthetic comment -- @@ -252,9 +269,18 @@
runOnLooper(new Runnable() {
@Override
public void run() {
                    if (hasRearCamera) {
                        mCamera = Camera.open();
                    } else if (hasFrontCamera) {
                        mCamera = Camera.open(0);
                    } else {
                        mCamera = null;
                    }
}
});
            if (mCamera == null) {
                break;
            }
mCamera.setErrorCallback(mCameraErrorCallback);
mCamera.setPreviewDisplay(mSurfaceHolder);
mCamera.startPreview();







