/*Fixing indeterminate progress bar animation state.

When showing a progress bar instance more than once
it will not animate after the first time.

Change-Id:I5104c551d561755005e533f2ab5257454567bf71Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index 6dc9f78..6393056 100644

//Synthetic comment -- @@ -934,4 +934,20 @@
setProgress(ss.progress);
setSecondaryProgress(ss.secondaryProgress);
}

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIndeterminate) {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIndeterminate) {
            stopAnimation();
        }
    }
}







