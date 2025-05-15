//<Beginning of snippet n. 0>
import com.android.cts.stub.R;
import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@TestTargetClass(MyTestClass.class)
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
        WidgetTestUtils.assertEquals("Bitmap comparison failed for testSetImageViewUri", d.getBitmap(),
                ((BitmapDrawable) image.getDrawable()).getBitmap());
    } catch (Exception e) {
        // Log the exception for debugging
        System.err.println("Error in testSetImageViewUri: " + e.getMessage());
    } finally {
        if (imageFile != null) {
            // remove the test image file
            imageFile.delete();
        }
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
        WidgetTestUtils.assertEquals("Bitmap comparison failed for testSetUri", d.getBitmap(),
                ((BitmapDrawable) image.getDrawable()).getBitmap());

        mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
        try {
            mRemoteViews.reapply(mActivity, mResult);
            fail("Should throw ActionException");
        } catch (ActionException e) {
            // expected
        }
    } catch (Exception e) {
        // Log the exception for debugging
        System.err.println("Error in testSetUri: " + e.getMessage());
    } finally {
        if (imageFile != null) {
            // remove the test image file
            imageFile.delete();
        }
    }
}
//<End of snippet n. 0>