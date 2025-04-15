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

		Rect actualRect = new Rect(rect2);
        actual = compressDecompress(image, actualRect);

        Rect expectedRect = sameRect ? actualRect : rect1;
        expected = Bitmap.createBitmap(testBitmap, expectedRect.left, expectedRect.top, expectedRect.width(), expectedRect.height());
        
compareBitmaps(expected, actual, mMseMargin, sameRect);
}








