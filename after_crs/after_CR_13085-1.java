/*Fix the build.*/




//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index 54269e1..1dcb203 100644

//Synthetic comment -- @@ -177,7 +177,8 @@
Drawable drawable = a.getDrawable(R.styleable.ProgressBar_progressDrawable);
if (drawable != null) {
drawable = tileify(drawable, false);
            // Calling this method can set mMaxHeight, make sure the corresponding
            // XML attribute for mMaxHeight is read after calling this method
setProgressDrawable(drawable);
}

//Synthetic comment -- @@ -423,8 +424,8 @@
if (d != null) {
d.setCallback(this);

            // Make sure the ProgressBar is always tall enough
            int drawableHeight = d.getMinimumHeight();
if (mMaxHeight < drawableHeight) {
mMaxHeight = drawableHeight;
requestLayout();







