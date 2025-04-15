/*Don't drop the drawable cache completely on configuration change

There was a lot of fancy code just above the clear to ensure
that drawables that aren't affected by the change are kept,
then the entire array was cleared.  This patch removes the
clear, so that the drawables that haven't changed are really
kept, matching the logs, comments and larger part of the code.

This patch also fixes the various constant states to return
correct ChangingConfigurations.

Change-Id:Ic11f6179537318d3de16dc58286989eb62a07f15Old-Change-Id:I22495e6ed232dfe056207ce5155405af1fa82428*/
//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
//Synthetic comment -- index a6513aa..e9a2929 100755

//Synthetic comment -- @@ -1352,7 +1352,6 @@
}
}
}
        cache.clear();
}

/**








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/AnimatedRotateDrawable.java b/graphics/java/android/graphics/drawable/AnimatedRotateDrawable.java
//Synthetic comment -- index 58206d4..49f497c 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
@Override
public ConstantState getConstantState() {
if (mState.canConstantState()) {
            mState.mChangingConfigurations = super.getChangingConfigurations();
return mState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/BitmapDrawable.java b/graphics/java/android/graphics/drawable/BitmapDrawable.java
//Synthetic comment -- index 32111e8..0b8465e 100644

//Synthetic comment -- @@ -427,7 +427,7 @@

@Override
public final ConstantState getConstantState() {
        mBitmapState.mChangingConfigurations = super.getChangingConfigurations();
return mBitmapState;
}









//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/ClipDrawable.java b/graphics/java/android/graphics/drawable/ClipDrawable.java
//Synthetic comment -- index a772871..2b3bd80 100644

//Synthetic comment -- @@ -229,7 +229,7 @@
@Override
public ConstantState getConstantState() {
if (mClipState.canConstantState()) {
            mClipState.mChangingConfigurations = super.getChangingConfigurations();
return mClipState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/ColorDrawable.java b/graphics/java/android/graphics/drawable/ColorDrawable.java
//Synthetic comment -- index 604c602..0985c1b 100644

//Synthetic comment -- @@ -124,7 +124,7 @@

@Override
public ConstantState getConstantState() {
        mState.mChangingConfigurations = super.getChangingConfigurations();
return mState;
}









//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/DrawableContainer.java b/graphics/java/android/graphics/drawable/DrawableContainer.java
//Synthetic comment -- index c6f57d4..b13f26fc 100644

//Synthetic comment -- @@ -236,7 +236,7 @@
@Override
public ConstantState getConstantState() {
if (mDrawableContainerState.canConstantState()) {
            mDrawableContainerState.mChangingConfigurations = super.getChangingConfigurations();
return mDrawableContainerState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/GradientDrawable.java b/graphics/java/android/graphics/drawable/GradientDrawable.java
//Synthetic comment -- index 33ecbea..308fd08 100644

//Synthetic comment -- @@ -832,7 +832,7 @@

@Override
public ConstantState getConstantState() {
        mGradientState.mChangingConfigurations = super.getChangingConfigurations();
return mGradientState;
}









//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/InsetDrawable.java b/graphics/java/android/graphics/drawable/InsetDrawable.java
//Synthetic comment -- index a9c983e..67c928c 100644

//Synthetic comment -- @@ -238,7 +238,7 @@
@Override
public ConstantState getConstantState() {
if (mInsetState.canConstantState()) {
            mInsetState.mChangingConfigurations = super.getChangingConfigurations();
return mInsetState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/LayerDrawable.java b/graphics/java/android/graphics/drawable/LayerDrawable.java
//Synthetic comment -- index 8047dd4..234b80d 100644

//Synthetic comment -- @@ -523,7 +523,7 @@
@Override
public ConstantState getConstantState() {
if (mLayerState.canConstantState()) {
            mLayerState.mChangingConfigurations = super.getChangingConfigurations();
return mLayerState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/NinePatchDrawable.java b/graphics/java/android/graphics/drawable/NinePatchDrawable.java
//Synthetic comment -- index 00416d8..6768186 100644

//Synthetic comment -- @@ -327,7 +327,7 @@

@Override
public ConstantState getConstantState() {
        mNinePatchState.mChangingConfigurations = super.getChangingConfigurations();
return mNinePatchState;
}









//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/RotateDrawable.java b/graphics/java/android/graphics/drawable/RotateDrawable.java
//Synthetic comment -- index 9c47dab..1428efa 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
@Override
public ConstantState getConstantState() {
if (mState.canConstantState()) {
            mState.mChangingConfigurations = super.getChangingConfigurations();
return mState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/ScaleDrawable.java b/graphics/java/android/graphics/drawable/ScaleDrawable.java
//Synthetic comment -- index b623d80..a95eb06 100644

//Synthetic comment -- @@ -237,7 +237,7 @@
@Override
public ConstantState getConstantState() {
if (mScaleState.canConstantState()) {
            mScaleState.mChangingConfigurations = super.getChangingConfigurations();
return mScaleState;
}
return null;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/ShapeDrawable.java b/graphics/java/android/graphics/drawable/ShapeDrawable.java
//Synthetic comment -- index be1892e..0201fb0 100644

//Synthetic comment -- @@ -348,7 +348,7 @@

@Override
public ConstantState getConstantState() {
        mShapeState.mChangingConfigurations = super.getChangingConfigurations();
return mShapeState;
}








