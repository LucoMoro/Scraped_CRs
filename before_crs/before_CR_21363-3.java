/*Implementing more robust equality check.

Change-Id:I2d71bd0ad6524c015676f0c57c6070eede968709*/
//Synthetic comment -- diff --git a/core/java/android/gesture/GestureUtils.java b/core/java/android/gesture/GestureUtils.java
//Synthetic comment -- index dd221fc..05d9f7b 100755

//Synthetic comment -- @@ -548,7 +548,7 @@
float rightside = (float) Math.sqrt(Math.pow(value, 2) - b);
float lambda1 = -value + rightside;
float lambda2 = -value - rightside;
        if (lambda1 == lambda2) {
targetVector[0] = 0;
targetVector[1] = 0;
} else {







