//<Beginning of snippet n. 0>


import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;

@BrokenTest("needs investigation")
public void testSetImageViewUri() throws IOException {
    File imageFile = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false; // Set consistent BitmapFactory.Options

    try {
        String path = getTestImagePath();
        imageFile = new File(path);
        createSampleImage(imageFile, R.raw.testimage);

        Uri uri = Uri.parse(path);
        mRemoteViews.setImageViewUri(R.id.remoteView_image, uri);
        mRemoteViews.reapply(mActivity, mResult);

        BitmapDrawable d = (BitmapDrawable) mActivity.getResources().getDrawable(R.drawable.testimage, options);
        BitmapDrawable loadedDrawable = (BitmapDrawable) ((ImageView) mResult.findViewById(R.id.remoteView_image)).getDrawable();
        
        assertNotNull("Loaded drawable should not be null", loadedDrawable);
        assertEquals(d.getBitmap().getWidth(), loadedDrawable.getBitmap().getWidth());
        assertEquals(d.getBitmap().getHeight(), loadedDrawable.getBitmap().getHeight());
        WidgetTestUtils.assertEquals(d.getBitmap(), loadedDrawable.getBitmap());

    } catch (IOException e) {
        throw new IOException("Error creating or loading test image", e);
    } finally {
        if (imageFile != null && imageFile.exists()) {
            if (!imageFile.delete()) {
                throw new IOException("Failed to delete test image file");
            }
        }
    }
}

@BrokenTest("needs investigation")
public void testSetUri() throws IOException {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false; // Set consistent BitmapFactory.Options

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

        BitmapDrawable d = (BitmapDrawable) mActivity.getResources().getDrawable(R.drawable.testimage, options);
        BitmapDrawable loadedDrawable = (BitmapDrawable) image.getDrawable();
        
        assertNotNull("Loaded drawable should not be null", loadedDrawable);
        assertEquals(d.getBitmap().getWidth(), loadedDrawable.getBitmap().getWidth());
        assertEquals(d.getBitmap().getHeight(), loadedDrawable.getBitmap().getHeight());
        WidgetTestUtils.assertEquals(d.getBitmap(), loadedDrawable.getBitmap());

        mRemoteViews.setUri(R.id.remoteView_absolute, "setImageURI", uri);
        try {
            mRemoteViews.reapply(mActivity, mResult);
            fail("Should throw ActionException");
        } catch (ActionException e) {
            // expected
        }

    } catch (IOException e) {
        throw new IOException("Error creating or loading test image", e);
    } finally {
        if (imageFile != null && imageFile.exists()) {
            if (!imageFile.delete()) {
                throw new IOException("Failed to delete test image file");
            }
        }
    }
}

@TestTargetNew(

//<End of snippet n. 0>