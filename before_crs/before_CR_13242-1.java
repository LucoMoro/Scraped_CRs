/*ViewAnimator.setAnimateFirstView() doesn't work for out-animation.*/
//Synthetic comment -- diff --git a/core/java/android/widget/ViewAnimator.java b/core/java/android/widget/ViewAnimator.java
//Synthetic comment -- index fa8935e3..33123c8 100644

//Synthetic comment -- @@ -121,14 +121,15 @@
final int count = getChildCount();
for (int i = 0; i < count; i++) {
final View child = getChildAt(i);
if (i == childIndex) {
                if ((!mFirstTime || mAnimateFirstTime) && mInAnimation != null) {
child.startAnimation(mInAnimation);
}
child.setVisibility(View.VISIBLE);
mFirstTime = false;
} else {
                if (mOutAnimation != null && child.getVisibility() == View.VISIBLE) {
child.startAnimation(mOutAnimation);
} else if (child.getAnimation() == mInAnimation)
child.clearAnimation();







