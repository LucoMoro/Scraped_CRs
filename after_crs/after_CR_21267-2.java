/*Fix for #12945: ProgressBar.setMax() will always redraw.

Fix for issue #12945: Changing the maximum of a progress bar does
not cause it to be redrawn, even though a new maximum changes the
position of the current progress in relation to its maximum. With this fix,
setMax() will always cause refreshProgress() to be called if the maximum is
different than it was before.

Change-Id:I971ec3302953bcadc0aac3dd8241481bab2b5a91*/




//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index ec7d927..2a695e5 100644

//Synthetic comment -- @@ -666,8 +666,8 @@

if (mProgress > max) {
mProgress = max;
}
            refreshProgress(R.id.progress, mProgress, false);
}
}








