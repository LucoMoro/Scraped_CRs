/*modify compressRects Api in YuvImageTest.java

Change-Id:Ic375fe0327d19f89d5747e0388dcddc100a16325*/
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








