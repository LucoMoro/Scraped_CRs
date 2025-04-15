/*Fix build.

Change 9099 was a little bit too aggressive and removed a line it shouldn't have.*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsSeekBar.java b/core/java/android/widget/AbsSeekBar.java
//Synthetic comment -- index 80fcac1..9ebfa86 100644

//Synthetic comment -- @@ -280,6 +280,7 @@
progress = mTouchProgressOffset;
}

progress += scale * max;

setProgress((int) progress, true);







