/*It is neccessary to keep a reference to ParcelFileDescriptor

When obtainDescriptor() returns back to test methods,
ParcelFileDescriptor object is not reachable from them.
So it might be destroyed by gc if a memory was low.
ParcelFileDescriptor would close a real file descriptor
when it was finalized. In that case, bitmap related tests will fail.
(Because FileDescriptor$descriptor is gone! i.e. "-1")

To avoid this fail, the test methods keep the reference to
ParcelFileDescriptor object in them.

Change-Id:I3980179b0545086e522fd327e265211c1f6d1d53*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/BitmapFactoryTest.java b/tests/tests/graphics/src/android/graphics/cts/BitmapFactoryTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 4c6259c..fdffd51

//Synthetic comment -- @@ -174,7 +174,8 @@
android.graphics.BitmapFactory.Options.class}
)
public void testDecodeFileDescriptor1() throws IOException {
        FileDescriptor input = obtainDescriptor(obtainPath());
Rect r = new Rect(1, 1, 1, 1);
Bitmap b = BitmapFactory.decodeFileDescriptor(input, r, mOpt1);
assertNotNull(b);
//Synthetic comment -- @@ -191,7 +192,8 @@
args = {java.io.FileDescriptor.class}
)
public void testDecodeFileDescriptor2() throws IOException {
        FileDescriptor input = obtainDescriptor(obtainPath());
Bitmap b = BitmapFactory.decodeFileDescriptor(input);
assertNotNull(b);
// Test the bitmap size
//Synthetic comment -- @@ -240,10 +242,11 @@
return mRes.openRawResource(R.drawable.start);
}

    private FileDescriptor obtainDescriptor(String path) throws IOException {
      File file = new File(path);
      return(ParcelFileDescriptor.open(file,
              ParcelFileDescriptor.MODE_READ_ONLY).getFileDescriptor());
}

private String obtainPath() throws IOException {








//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/BitmapRegionDecoderTest.java b/tests/tests/graphics/src/android/graphics/cts/BitmapRegionDecoderTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index e1ba12a..5cda2cb

//Synthetic comment -- @@ -157,7 +157,8 @@
public void testNewInstanceStringAndFileDescriptor() throws IOException {
for (int i = 0; i < RES_IDS.length; ++i) {
String filepath = obtainPath(i);
            FileDescriptor fd = obtainDescriptor(filepath);
try {
BitmapRegionDecoder decoder1 =
BitmapRegionDecoder.newInstance(filepath, false);
//Synthetic comment -- @@ -245,9 +246,11 @@
Bitmap wholeImage = BitmapFactory.decodeFile(filepath, opts);
compareRegionByRegion(decoder, opts, wholeImage);

                    FileDescriptor fd1 = obtainDescriptor(filepath);
decoder = BitmapRegionDecoder.newInstance(fd1, false);
                    FileDescriptor fd2 = obtainDescriptor(filepath);
compareRegionByRegion(decoder, opts, wholeImage);
wholeImage.recycle();
}
//Synthetic comment -- @@ -357,12 +360,14 @@
return (file.getPath());
}

    private FileDescriptor obtainDescriptor(String path) throws IOException {
File file = new File(path);
return(ParcelFileDescriptor.open(file,
                ParcelFileDescriptor.MODE_READ_ONLY).getFileDescriptor());
}

// Compare expected to actual to see if their diff is less then mseMargin.
// lessThanMargin is to indicate whether we expect the diff to be
// "less than" or "no less than".







