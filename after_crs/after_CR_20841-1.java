/*Fix Broken RemoteViewsTest

The Bitmaps weren't being loaded using the same configuration and
options, so these tests were failing.

Change-Id:If74280f1e1f2d8f499b131c2db1d7f438e8701c7*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/RemoteViewsTest.java b/tests/tests/widget/src/android/widget/cts/RemoteViewsTest.java
//Synthetic comment -- index 1671f28..9331ff9 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -218,14 +217,11 @@
method = "setImageViewUri",
args = {int.class, android.net.Uri.class}
)
public void testSetImageViewUri() throws IOException {
        String path = getTestImagePath();
        File imageFile = new File(path);

try {
createSampleImage(imageFile, R.raw.testimage);

Uri uri = Uri.parse(path);
//Synthetic comment -- @@ -234,15 +230,13 @@

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

//Synthetic comment -- @@ -602,34 +596,36 @@
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







