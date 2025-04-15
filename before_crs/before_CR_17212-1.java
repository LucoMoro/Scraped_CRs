/*CameraTest Experiment

This change started off as an experiment to see how useful
a @RequiredFeatures annotation would be to omit tests for
devices that may not have the camera feature. Alas, it seems
it may be important to check the no-op behavior of an API
on such devices. As a result, most of the tests still make
sense even without a camera, because the callbacks should be
called (I think). Only parts of the test like actually
returning the JPEG data wouldn't work, so wrap those around
a feature check.

Change-Id:Ie4ad826c6a5817c7f8a26c24e35a0bbf15f908c6*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 864a1ab..904c144 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
//Synthetic comment -- @@ -329,10 +330,16 @@
terminateMessageLooper();
assertTrue(mShutterCallbackResult);
assertTrue(mJpegPictureCallbackResult);
        assertTrue(mJpegData != null);
        Bitmap b = BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length);
        assertEquals(b.getWidth(), pictureSize.width);
        assertEquals(b.getHeight(), pictureSize.height);
}

/*
//Synthetic comment -- @@ -682,12 +689,18 @@
mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
waitForSnapshotDone();
assertEquals(mJpegPictureCallbackResult, true);
        ExifInterface exif = new ExifInterface(JPEG_PATH);
        assertTrue(exif.hasThumbnail());
        byte[] thumb = exif.getThumbnail();
        Bitmap b = BitmapFactory.decodeByteArray(thumb, 0, thumb.length);
        assertEquals(b.getWidth(), size.width);
        assertEquals(b.getHeight(), size.height);

// Test no thumbnail case.
p.setJpegThumbnailSize(0, 0);
//Synthetic comment -- @@ -696,8 +709,12 @@
mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
waitForSnapshotDone();
assertEquals(mJpegPictureCallbackResult, true);
        exif = new ExifInterface(JPEG_PATH);
        assertTrue(!exif.hasThumbnail());

terminateMessageLooper();
}
//Synthetic comment -- @@ -713,22 +730,27 @@
double focalLength = (double)parameters.getFocalLength();
mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
waitForSnapshotDone();
        ExifInterface exif = new ExifInterface(JPEG_PATH);
        assertTrue(exif.getAttribute(ExifInterface.TAG_MAKE) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_MODEL) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_DATETIME) != null);
        assertTrue(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0) != 0);
        assertTrue(exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0) != 0);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE), null);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE), null);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF), null);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF), null);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP), null);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP), null);
        assertEquals(exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD), null);
        double exifFocalLength = (double)exif.getAttributeDouble(
                ExifInterface.TAG_FOCAL_LENGTH, -1);
        assertEquals(focalLength, exifFocalLength, 0.001);

// Test gps exif tags.
mCamera.startPreview();
//Synthetic comment -- @@ -741,16 +763,20 @@
mCamera.setParameters(parameters);
mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
waitForSnapshotDone();
        exif = new ExifInterface(JPEG_PATH);
        assertTrue(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP) != null);
        assertTrue(exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP) != null);
        assertEquals(thirtyTwoCharacters,
                exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD));
        terminateMessageLooper();
}

@TestTargets({







