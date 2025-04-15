/*Fixes scroll/measure bug in EditText

Bug and fix described in:http://code.google.com/p/android/issues/detail?id=4184*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index aed2d5a..88730aac 100644

//Synthetic comment -- @@ -5171,7 +5171,7 @@
mDesiredHeightAtMeasure = desired;

if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(desired, height);
}
}








