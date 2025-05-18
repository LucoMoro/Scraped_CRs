//<Beginning of snippet n. 0>

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@BrokenTest("needs investigation")
public void testSetImageViewUri() throws IOException {
    File imageFile = null;

    try {
        // create the test image first
        String path = getTestImagePath();
        imageFile = new File(path);
        createSampleImage(imageFile, R.raw.testimage);

        Uri uri = Uri.parse(path);

        mRemoteViews.setImageViewUri(R.id.remoteView_image, uri);
        mRemoteViews.reapply(mActivity, mResult);

        BitmapDrawable d = (BitmapDrawable) mActivity.getResources().getDrawable(R.drawable.testimage);
        
        WidgetTestUtils.assertBitmapsEqual(d.getBitmap(), loadBitmapFromDrawable(mActivity, R.id.remoteView_image));

    } finally {
        cleanupImageFile(imageFile);
    }
}

@BrokenTest("needs investigation")
public void testSetUri() throws IOException {
    File imageFile = null;

    try {
        // create the test image first
        String path = getTestImagePath();
        imageFile = new File(path);
        createSampleImage(imageFile, R.raw.testimage);

        Uri uri = Uri.parse(path);
        ImageView image = (ImageView) mResult.findViewById(R.id.remoteView_image);
        assertNull(image.getDrawable());

        mRemoteViews.setUri(R.id.remoteView_image, "setImageURI", uri);
        mRemoteViews.reapply(mActivity, mResult);

        BitmapDrawable d = (BitmapDrawable) mActivity.getResources().getDrawable(R.drawable.testimage);

        WidgetTestUtils.assertBitmapsEqual(d.getBitmap(), loadBitmapFromDrawable(mActivity, R.id.remoteView_image));

        mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
        try {
            mRemoteViews.reapply(mActivity, mResult);
            fail("Should throw ActionException");
        } catch (ActionException e) {
            // expected
        }
    } finally {
        cleanupImageFile(imageFile);
    }
}

private Bitmap loadBitmapFromDrawable(Context context, int viewId) {
    ImageView image = (ImageView) ((ViewGroup) mResult).findViewById(viewId);
    return ((BitmapDrawable) image.getDrawable()).getBitmap();
}

private void cleanupImageFile(File imageFile) throws IOException {
    if (imageFile != null && imageFile.exists()) {
        // remove the test image file
        if (!imageFile.delete()) {
            throw new IOException("Failed to delete test image file");
        } else {
            // Log successful deletion
            System.out.println("Successfully deleted test image file: " + imageFile.getAbsolutePath());
        }
    }
}

private BitmapFactory.Options createBitmapOptions() {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 1; // Set according to test requirements
    options.inPreferredConfig = Bitmap.Config.ARGB_8888; // Use preferred config
    return options;
}

//<End of snippet n. 0>