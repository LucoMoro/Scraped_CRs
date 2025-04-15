/*guess default mMaxHeight but allow layout to override this.

without this code, progress bar height gets smaller when thumb gets bigger
in AbsSeekBar.java onSizeChanged().*/




//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index 2f28d9f..caa3f0b 100644

//Synthetic comment -- @@ -177,7 +177,7 @@
Drawable drawable = a.getDrawable(R.styleable.ProgressBar_progressDrawable);
if (drawable != null) {
drawable = tileify(drawable, false);
            setProgressDrawable(drawable); // will guess mMaxHeight.but allow layout to override this
}


//Synthetic comment -- @@ -421,6 +421,11 @@
public void setProgressDrawable(Drawable d) {
if (d != null) {
d.setCallback(this);

            // without this code, progress bar height gets smaller when thumb height gets bigger in AbsSeekBar onSizeChanged().
            int drawableHeight = drawable.getMinimumHeight();
            if (mMaxHeight < drawableHeight)
                mMaxHeight = drawableHeight;
}
mProgressDrawable = d;
if (!mIndeterminate) {







