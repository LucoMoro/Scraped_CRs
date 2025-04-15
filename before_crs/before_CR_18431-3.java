/*modify compressRects Api in YuvImageTest.java

Change-Id:I12900812052fac3760c14204ab13e8b166d21d4a*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/YuvImageTest.java b/tests/tests/graphics/src/android/graphics/cts/YuvImageTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index ccacc62..6613e92

//Synthetic comment -- @@ -245,9 +245,13 @@
Bitmap expected = null;
Bitmap actual = null;
boolean sameRect = rect1.equals(rect2) ? true : false;
        expected = Bitmap.createBitmap(testBitmap, rect1.left, rect1.top,
                rect1.width(), rect1.height());
        actual = compressDecompress(image, rect2);
compareBitmaps(expected, actual, mMseMargin, sameRect);
}








