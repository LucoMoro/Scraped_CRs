
//<Beginning of snippet n. 0>



import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
method = "setImageViewUri",
args = {int.class, android.net.Uri.class}
)
public void testSetImageViewUri() throws IOException {
        String path = getTestImagePath();
        File imageFile = new File(path);

try {
createSampleImage(imageFile, R.raw.testimage);

Uri uri = Uri.parse(path);

mRemoteViews.setImageViewUri(R.id.remoteView_image, uri);
mRemoteViews.reapply(mActivity, mResult);

            Bitmap imageViewBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            Bitmap expectedBitmap = WidgetTestUtils.getUnscaledAndDitheredBitmap(
                    mActivity.getResources(), R.raw.testimage, imageViewBitmap.getConfig());
            WidgetTestUtils.assertEquals(expectedBitmap, imageViewBitmap);
} finally {
            imageFile.delete();
}
}

method = "setUri",
args = {int.class, java.lang.String.class, android.net.Uri.class}
)
public void testSetUri() throws IOException {
String path = getTestImagePath();
File imagefile = new File(path);

try {
            createSampleImage(imagefile, R.raw.testimage);

            Uri uri = Uri.parse(path);
            ImageView image = (ImageView) mResult.findViewById(R.id.remoteView_image);
            assertNull(image.getDrawable());

            mRemoteViews.setUri(R.id.remoteView_image, "setImageURI", uri);
            mRemoteViews.reapply(mActivity, mResult);

            Bitmap imageViewBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            Bitmap expectedBitmap = WidgetTestUtils.getUnscaledAndDitheredBitmap(
                    mActivity.getResources(), R.raw.testimage, imageViewBitmap.getConfig());
            WidgetTestUtils.assertEquals(expectedBitmap, imageViewBitmap);

            mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
            try {
                mRemoteViews.reapply(mActivity, mResult);
                fail("Should throw ActionException");
            } catch (ActionException e) {
                // expected
            }
        } finally {
            // remove the test image file
            imagefile.delete();
        }
}

@TestTargetNew(

//<End of snippet n. 0>








