/*Fix android.graphics2.cts.TextureViewTest's wrong assumption

Not all device has rear camera. Add a check in this test case to avoid
failure in CTS report on those devices.

Change-Id:I6937ed33bbc4871d2ad074f03b467c7681db1665*/




//Synthetic comment -- diff --git a/tests/tests/graphics2/src/android/graphics2/cts/TextureViewCameraActivity.java b/tests/tests/graphics2/src/android/graphics2/cts/TextureViewCameraActivity.java
//Synthetic comment -- index d947565..5bd0f8f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.graphics2.cts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
//Synthetic comment -- @@ -45,6 +46,8 @@
private int mHeight;
private float mRotation = 0f;
private final CountDownLatch mLatch = new CountDownLatch(1);
    private boolean mHasRearCamera = false;
    private boolean mHasFrontCamera = false;

public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
Assert.assertTrue(mTextureView.getLayerType() == View.LAYER_TYPE_HARDWARE);
//Synthetic comment -- @@ -54,7 +57,14 @@
Assert.assertTrue(mTextureView.isOpaque());
mWidth = width;
mHeight = height;

        if (mHasRearCamera) {
            mCamera = Camera.open();
        } else if (mHasFrontCamera) {
            mCamera = Camera.open(0);
        } else {
            mCamera = null;
        }

try {
mCamera.setPreviewTexture(surface);
//Synthetic comment -- @@ -103,6 +113,10 @@
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

        PackageManager packageManager = this.getPackageManager();
        mHasRearCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        mHasFrontCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

mTextureView = new TextureView(this);
mTextureView.setSurfaceTextureListener(this);
mTextureView.setOpaque(true);







