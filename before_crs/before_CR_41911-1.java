/*Fix the issue Browser freeze when dragging the page and rotate the phone at the same times

When drag event is cancelled, the tab removing isn't needed. Or if it is the last one, browser will be closed and windows manager may be ANR.

Change-Id:Ia786d98727cecbe303cf9d153a471fd4a165b797Author: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30621*/
//Synthetic comment -- diff --git a/src/com/android/browser/NavTabScroller.java b/src/com/android/browser/NavTabScroller.java
//Synthetic comment -- index c940bf5..3eddbd4 100644

//Synthetic comment -- @@ -439,15 +439,17 @@
protected void onOrthoDragFinished(View downView) {
if (mAnimator != null) return;
if (mIsOrthoDragged && downView != null) {
            // offset
            float diff = mHorizontal ? downView.getTranslationY() : downView.getTranslationX();
            if (Math.abs(diff) > (mHorizontal ? downView.getHeight() : downView.getWidth()) / 2) {
                // remove it
                animateOut(downView, Math.signum(diff) * mFlingVelocity, diff);
            } else {
                // snap back
                offsetView(downView, 0);
}
}
}

//Synthetic comment -- @@ -566,4 +568,4 @@

}

}
\ No newline at end of file








//Synthetic comment -- diff --git a/src/com/android/browser/view/ScrollerView.java b/src/com/android/browser/view/ScrollerView.java
//Synthetic comment -- index 545dd25..98ccbff 100644

//Synthetic comment -- @@ -97,6 +97,8 @@
*/
protected boolean mIsBeingDragged = false;

/**
* Determines speed during touch scrolling
*/
//Synthetic comment -- @@ -740,6 +742,7 @@
}
break;
case MotionEvent.ACTION_CANCEL:
if (mIsOrthoDragged) {
onOrthoDragFinished(mDownView);
mActivePointerId = INVALID_POINTER;
//Synthetic comment -- @@ -757,6 +760,7 @@
mActivePointerId = INVALID_POINTER;
endDrag();
}
break;
case MotionEvent.ACTION_POINTER_DOWN: {
final int index = ev.getActionIndex();







