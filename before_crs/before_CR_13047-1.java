/*setThumbOffset was missing in the previous merge.*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsSeekBar.java b/core/java/android/widget/AbsSeekBar.java
//Synthetic comment -- index d25530b..50e0680 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
// ...but allow layout to override this
int thumbOffset =
a.getDimensionPixelOffset(com.android.internal.R.styleable.SeekBar_thumbOffset, getThumbOffset());
a.recycle();

a = context.obtainStyledAttributes(attrs,







