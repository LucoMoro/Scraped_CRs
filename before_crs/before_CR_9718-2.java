/*Add of Javadoc comments on undocumented methods.
Use of a constant defined in SensorManager for computing deceleration.*/
//Synthetic comment -- diff --git a/core/java/android/widget/Scroller.java b/core/java/android/widget/Scroller.java
//Synthetic comment -- index c9ace0a..7c9e06e 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.widget;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
//Synthetic comment -- @@ -79,9 +80,9 @@
mFinished = true;
mInterpolator = interpolator;
float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mDeceleration = 9.8f   // g (m/s^2)
                      * 39.37f // inch/meter
                      * ppi    // pixels per inch
* ViewConfiguration.getScrollFriction();
}

//Synthetic comment -- @@ -347,7 +348,11 @@
}

/**
     * 
*/
public void abortAnimation() {
mCurrX = mFinalX;
//Synthetic comment -- @@ -356,10 +361,12 @@
}

/**
     * Extend the scroll animation. This allows a running animation to 
     * scroll further and longer, when used with setFinalX() or setFinalY().
*
* @param extend Additional time to scroll in milliseconds.
*/
public void extendDuration(int extend) {
int passed = timePassed();
//Synthetic comment -- @@ -367,18 +374,37 @@
mDurationReciprocal = 1.0f / (float)mDuration;
mFinished = false;
}
    
public int timePassed() {
return (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
}
    
public void setFinalX(int newX) {
mFinalX = newX;
mDeltaX = mFinalX - mStartX;
mFinished = false;
}

   public void setFinalY(int newY) {
mFinalY = newY;
mDeltaY = mFinalY - mStartY;
mFinished = false;







