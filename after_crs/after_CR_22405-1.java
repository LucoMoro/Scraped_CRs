/*Eliminating dead logic - bitmap state simply cannot be null here.

Change-Id:I33fdf650b06242efa22ca30b3f7252f6854b42f6*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/BitmapDrawable.java b/graphics/java/android/graphics/drawable/BitmapDrawable.java
//Synthetic comment -- index 0b8465e..501374b 100644

//Synthetic comment -- @@ -474,10 +474,8 @@
mBitmapState = state;
if (res != null) {
mTargetDensity = res.getDisplayMetrics().densityDpi;
} else {
            mTargetDensity = state.mTargetDensity;
}
setBitmap(state.mBitmap);
}







