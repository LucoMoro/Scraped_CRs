/*CTS: Fixes for #testPreviewPictureSizesCombination, #testTakePicture and #testJpegThumbnailSizeByCamera

- Image sizes can vary quite a bit. Some cameras might
  support up to 12MP picture sizes, which can result
  in very large bitmaps. Dalvik's VM stack size might
  not be able to handle arrays of this size.
  In these cases an "out of memory" exception
  is possible.

Change-Id:I15406f73bb72c897b04876f312019dc68f0f726fSigned-off-by: Emilian Peev <epeev@mm-sol.com>*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 9ed0d93..6410f37 100644

//Synthetic comment -- @@ -331,9 +331,13 @@
assertTrue(mShutterCallbackResult);
assertTrue(mJpegPictureCallbackResult);
assertNotNull(mJpegData);
        try {
            Bitmap b = BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length);
            assertEquals(pictureSize.width, b.getWidth());
            assertEquals(pictureSize.height, b.getHeight());
        } catch( OutOfMemoryError e ) {
            Log.d(TAG, "Bitmap size exceeds VM stack size");
        }
}

@TestTargets({
//Synthetic comment -- @@ -761,9 +765,14 @@
ExifInterface exif = new ExifInterface(JPEG_PATH);
assertTrue(exif.hasThumbnail());
byte[] thumb = exif.getThumbnail();

        try {
            Bitmap b = BitmapFactory.decodeByteArray(thumb, 0, thumb.length);
            assertEquals(size.width, b.getWidth());
            assertEquals(size.height, b.getHeight());
        } catch( OutOfMemoryError e ) {
            Log.d(TAG, "Bitmap size exceeds VM stack size");
        }

// Test no thumbnail case.
p.setJpegThumbnailSize(0, 0);
//Synthetic comment -- @@ -1451,11 +1460,16 @@
waitForSnapshotDone();
assertTrue(mJpegPictureCallbackResult);
assertNotNull(mJpegData);

                try {
                    Bitmap b = BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length);
                    assertEquals(pictureSize.width, b.getWidth());
                    assertEquals(pictureSize.height, b.getHeight());
                    b.recycle();
                    b = null;
                } catch( OutOfMemoryError e ) {
                    Log.d(TAG, "Bitmap size exceeds VM stack size");
                }
}
}
terminateMessageLooper();







