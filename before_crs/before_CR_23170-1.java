/*If you call setDrawingCacheEnabled(true), updateCursorControllerPosition is never called.
I've added PreDrawListener() in constructor to call UpdateCursorControllerPosition()

Change-Id:Iad32e40ca65994b679e0461a7369dd9caa4d4084*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 68600cf..003a1d5 100644

//Synthetic comment -- @@ -961,6 +961,14 @@
setLongClickable(longClickable);

prepareCursorControllers();
}

private void setTypefaceByIndex(int typefaceIndex, int styleIndex) {







