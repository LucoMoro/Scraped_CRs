//<Beginning of snippet n. 0>

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@TestTargetNew(
    method = "setImageViewUri",
    args = {int.class, android.net.Uri.class}
)
@BrokenTest("needs investigation")
public void testSetImageViewUri() throws IOException {
    File imageFile = null;
    try {
        String path = getTestImagePath();
        imageFile = new File(path);
        createSampleImage(imageFile, R.raw.testimage);

        Uri uri = Uri.parse(path);
        mRemoteViews.setImageViewUri(R.id.remoteView_image, uri);
        mRemoteViews.reapply(mActivity, mResult);
        BitmapDrawable d = createBitmapDrawable(R.drawable.testimage);
        WidgetTestUtils.assertBitmapEquals(d.getBitmap(), ((BitmapDrawable) ((ImageView) mResult.findViewById(R.id.remoteView_image)).getDrawable()).getBitmap());
    } finally {
        cleanUp(imageFile);
    }
}

@TestTargetNew(
    method = "setUri",
    args = {int.class, java.lang.String.class, android.net.Uri.class}
)
@BrokenTest("needs investigation")
public void testSetUri() throws IOException {
    File imageFile = null;
    try {
        String path = getTestImagePath();
        imageFile = new File(path);
        createSampleImage(imageFile, R.raw.testimage);
        Uri uri = Uri.parse(path);
        ImageView image = (ImageView) mResult.findViewById(R.id.remoteView_image);
        assertNull(image.getDrawable());

        mRemoteViews.setUri(R.id.remoteView_image, "setImageURI", uri);
        mRemoteViews.reapply(mActivity, mResult);
        BitmapDrawable d = createBitmapDrawable(R.drawable.testimage);
        WidgetTestUtils.assertBitmapEquals(d.getBitmap(), ((BitmapDrawable) image.getDrawable()).getBitmap());

        mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
        try {
            mRemoteViews.reapply(mActivity, mResult);
            fail("Should throw ActionException");
        } catch (ActionException e) {
            // expected
        }
    } finally {
        cleanUp(imageFile);
    }
}

private BitmapDrawable createBitmapDrawable(int resId) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    return (BitmapDrawable) mActivity.getResources().getDrawable(resId, options);
}

private void cleanUp(File imageFile) {
    if (imageFile != null && imageFile.exists()) {
        assertTrue(imageFile.delete());
        assertFalse(imageFile.exists());
    }
}

//<End of snippet n. 0>