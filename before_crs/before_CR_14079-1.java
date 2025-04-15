/*setCornerRadii should be called when either of corners radius is specified
as 0dp and thus while checking for condition, it should be ORed and not ANDed.

It solves Android Issue: 939http://code.google.com/p/android/issues/detail?id=939Change-Id:Ic18fae769480972f763f634e7462c6ed3853220b*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/GradientDrawable.java b/graphics/java/android/graphics/drawable/GradientDrawable.java
//Synthetic comment -- index 91a2bc1..63d1446 100644

//Synthetic comment -- @@ -778,8 +778,8 @@
com.android.internal.R.styleable.DrawableCorners_bottomLeftRadius, radius);
int bottomRightRadius = a.getDimensionPixelSize(
com.android.internal.R.styleable.DrawableCorners_bottomRightRadius, radius);
                if (topLeftRadius != radius && topRightRadius != radius &&
                        bottomLeftRadius != radius && bottomRightRadius != radius) {
setCornerRadii(new float[] {
topLeftRadius, topLeftRadius,
topRightRadius, topRightRadius,







