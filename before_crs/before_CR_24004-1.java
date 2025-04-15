/*Extended Compression testcase for WEBP compression.

The Bitmap testcase for checking the 'CompressFormat' enum and
compression for JPEG & PNG format has been extended for WEBP format.

Change-Id:I5396d6d960fb8e85b5b41819123a1e92ff40aafc*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/Bitmap_CompressFormatTest.java b/tests/tests/graphics/src/android/graphics/cts/Bitmap_CompressFormatTest.java
//Synthetic comment -- index 44c91e3..afe3409 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
public void testValueOf(){
assertEquals(CompressFormat.JPEG, CompressFormat.valueOf("JPEG"));
assertEquals(CompressFormat.PNG, CompressFormat.valueOf("PNG"));
}

@TestTargets({
//Synthetic comment -- @@ -55,13 +56,15 @@
public void testValues(){
CompressFormat[] comFormat = CompressFormat.values();

        assertEquals(2, comFormat.length);
assertEquals(CompressFormat.JPEG, comFormat[0]);
assertEquals(CompressFormat.PNG, comFormat[1]);

//CompressFormat is used as a argument here for all the methods that use it
Bitmap b = Bitmap.createBitmap(10, 24, Config.ARGB_8888);
assertTrue(b.compress(CompressFormat.JPEG, 24, new ByteArrayOutputStream()));
assertTrue(b.compress(CompressFormat.PNG, 24, new ByteArrayOutputStream()));
}
}







