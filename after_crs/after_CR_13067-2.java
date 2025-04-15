/*guess default mMaxHeight but allow layout to override this.

without this code, progress bar height gets smaller when thumb gets bigger
in AbsSeekBar.java onSizeChanged().*/




//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index 2f28d9f..5490632 100644

//Synthetic comment -- @@ -177,7 +177,8 @@
Drawable drawable = a.getDrawable(R.styleable.ProgressBar_progressDrawable);
if (drawable != null) {
drawable = tileify(drawable, false);
            setProgressDrawable(drawable); // will guess mMaxHeight.
            // but allow XML layout to override this.
}


//Synthetic comment -- @@ -421,6 +422,14 @@
public void setProgressDrawable(Drawable d) {
if (d != null) {
d.setCallback(this);

            // without this code, progress bar height gets smaller
            // when thumb height gets bigger.
            int drawableHeight = drawable.getMinimumHeight();
            if (mMaxHeight < drawableHeight) {
                mMaxHeight = drawableHeight;
                requestLayout();
            }
}
mProgressDrawable = d;
if (!mIndeterminate) {







