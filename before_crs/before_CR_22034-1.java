/*Remove Some Broken Paint Tests

Bug 3188260

Change-Id:I7e6b4fc06f2f771b3a96f764518ee3b3cb624f50*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 28da75a..ca08c18 100644

//Synthetic comment -- @@ -892,7 +892,6 @@
method = "getTextBounds",
args = {java.lang.String.class, int.class, int.class, android.graphics.Rect.class}
)
    @BrokenTest("Test result will be different when run in batch mode")
public void testGetTextBounds1() throws Exception {
Paint p = new Paint();
Rect r = new Rect();
//Synthetic comment -- @@ -959,7 +958,6 @@
method = "getTextBounds",
args = {char[].class, int.class, int.class, android.graphics.Rect.class}
)
    @BrokenTest("Test result will be different when run in batch mode")
public void testGetTextBounds2() throws Exception {
Paint p = new Paint();
Rect r = new Rect();







