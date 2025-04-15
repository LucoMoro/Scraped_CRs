/*Revert "CTS: Fixes a possible "out of memory" exception"

This reverts commit 7eade134e4c8fdaee5b7dfb0446dc90085b0408a.

This causes the test to fail on Nexus S, so I'm reverting
the change for Gingerbread. However, it will be included in
a future CTS release.

Additional info from Wu-cheng:
"The change made the test faster. If apps call setParameters
right after jpeg callback is received, it will fail. This is
not a regression and I believe it does not impact users."

Change-Id:I14ce1235581dac5543983f60ed047ae9331a754a*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 7650560..60b8459 100644

//Synthetic comment -- @@ -331,11 +331,9 @@
assertTrue(mShutterCallbackResult);
assertTrue(mJpegPictureCallbackResult);
assertNotNull(mJpegData);
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length, bmpOptions);
        assertEquals(pictureSize.width, bmpOptions.outWidth);
        assertEquals(pictureSize.height, bmpOptions.outHeight);
}

@TestTargets({
//Synthetic comment -- @@ -763,11 +761,9 @@
ExifInterface exif = new ExifInterface(JPEG_PATH);
assertTrue(exif.hasThumbnail());
byte[] thumb = exif.getThumbnail();
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(thumb, 0, thumb.length, bmpOptions);
        assertEquals(size.width, bmpOptions.outWidth);
        assertEquals(size.height, bmpOptions.outHeight);

// Test no thumbnail case.
p.setJpegThumbnailSize(0, 0);
//Synthetic comment -- @@ -1455,11 +1451,11 @@
waitForSnapshotDone();
assertTrue(mJpegPictureCallbackResult);
assertNotNull(mJpegData);
                BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
                bmpOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length, bmpOptions);
                assertEquals(pictureSize.width, bmpOptions.outWidth);
                assertEquals(pictureSize.height, bmpOptions.outHeight);
}
}
terminateMessageLooper();







