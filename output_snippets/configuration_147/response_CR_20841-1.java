//<Beginning of snippet n. 0>
import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@TestTargetClass(SomeClass.class)
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
        BitmapDrawable expectedDrawable = (BitmapDrawable) mActivity.getResources().getDrawable(R.drawable.testimage);
        Bitmap actualBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        WidgetTestUtils.assertEquals(expectedDrawable.getBitmap(), actualBitmap);
    } finally {
        if (imageFile != null) {
            imageFile.delete();
        }
    }
}

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
        BitmapDrawable expectedDrawable = (BitmapDrawable) mActivity.getResources().getDrawable(R.drawable.testimage);
        Bitmap actualBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        WidgetTestUtils.assertEquals(expectedDrawable.getBitmap(), actualBitmap);

        mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
        try {
            mRemoteViews.reapply(mActivity, mResult);
            fail("Should throw ActionException");
        } catch (ActionException e) {
            // expected
        }
    } finally {
        if (imageFile != null) {
            imageFile.delete();
        }
    }
}

@TestTargetNew(
//<End of snippet n. 0>