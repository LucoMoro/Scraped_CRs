/*If you call setDrawingCacheEnabled(true), updateCursorControllerPosition is never called.
I've added PreDrawListener() in constructor to call UpdateCursorControllerPosition()

Change-Id:I964f0f75b1d604081f60f5fa869af1471c2b0124*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 68600cf..d5febd6 100644

//Synthetic comment -- @@ -961,6 +961,14 @@
setLongClickable(longClickable);

prepareCursorControllers();
}

private void setTypefaceByIndex(int typefaceIndex, int styleIndex) {
//Synthetic comment -- @@ -997,6 +1005,10 @@
}
super.setEnabled(enabled);
}

/**
* Sets the typeface and style in which the text should be displayed,







