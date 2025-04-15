/*AbsSeekBar slightly optimized by removing a redundant snippet of code*/




//Synthetic comment -- diff --git a/core/java/android/widget/AbsSeekBar.java b/core/java/android/widget/AbsSeekBar.java
//Synthetic comment -- index b046a6b..80fcac1 100644

//Synthetic comment -- @@ -280,13 +280,7 @@
progress = mTouchProgressOffset;
}

progress += scale * max;

setProgress((int) progress, true);
}







