
//<Beginning of snippet n. 0>



import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
method = "setImageViewUri",
args = {int.class, android.net.Uri.class}
)
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
            BitmapDrawable d = (BitmapDrawable) mActivity
            .getResources().getDrawable(R.drawable.testimage);
            WidgetTestUtils.assertEquals(d.getBitmap(),
                    ((BitmapDrawable) image.getDrawable()).getBitmap());
} finally {
            if (imageFile != null) {
                // remove the test image file
                imageFile.delete();
            }
}
}

method = "setUri",
args = {int.class, java.lang.String.class, android.net.Uri.class}
)
    @BrokenTest("needs investigation")
public void testSetUri() throws IOException {
        // create the test image first
String path = getTestImagePath();
File imagefile = new File(path);
        createSampleImage(imagefile, R.raw.testimage);

        Uri uri = Uri.parse(path);
        ImageView image = (ImageView) mResult.findViewById(R.id.remoteView_image);
        assertNull(image.getDrawable());

        mRemoteViews.setUri(R.id.remoteView_image, "setImageURI", uri);
        mRemoteViews.reapply(mActivity, mResult);
        BitmapDrawable d = (BitmapDrawable) mActivity
                .getResources().getDrawable(R.drawable.testimage);
        WidgetTestUtils.assertEquals(d.getBitmap(),
                ((BitmapDrawable) image.getDrawable()).getBitmap());

        mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
try {
            mRemoteViews.reapply(mActivity, mResult);
            fail("Should throw ActionException");
        } catch (ActionException e) {
            // expected
        }

        // remove the test image file
        imagefile.delete();
}

@TestTargetNew(

//<End of snippet n. 0>








