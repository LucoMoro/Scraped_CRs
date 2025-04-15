/*Add of Javadoc comments on undocumented methods.
Use of a constant defined in SensorManager for computing deceleration.*/




//Synthetic comment -- diff --git a/core/java/android/widget/Scroller.java b/core/java/android/widget/Scroller.java
//Synthetic comment -- index c9ace0a..52a5a5b 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.widget;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
//Synthetic comment -- @@ -79,9 +80,9 @@
mFinished = true;
mInterpolator = interpolator;
float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mDeceleration = SensorManager.GRAVITY_EARTH   // g (m/s^2)
                      * 39.37f                        // inch/meter
                      * ppi                           // pixels per inch
* ViewConfiguration.getScrollFriction();
}

//Synthetic comment -- @@ -347,7 +348,9 @@
}

/**
     * Abort the animation. Contrary to a forceFinished(true), abortAnimation
     * will set getCurrX() and getCurrY() respectively to getFinalX() and
     * getFinalY().
*/
public void abortAnimation() {
mCurrX = mFinalX;
//Synthetic comment -- @@ -367,18 +370,33 @@
mDurationReciprocal = 1.0f / (float)mDuration;
mFinished = false;
}

    /**
     * Returns the time elapsed since the beginning of the scrolling.
     *
     * @return The time elapsed since scrolling starts.
     */
public int timePassed() {
return (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
}

    /**
     * Use it to change where the scroll animation will end.
     *
     * @param newX The new X offset as an absolute distance from the origin.
     */
public void setFinalX(int newX) {
mFinalX = newX;
mDeltaX = mFinalX - mStartX;
mFinished = false;
}

    /**
     * Use it to change where the scroll animation will end.
     *
     * @param newY The new Y offset as an absolute distance from the origin.
     */
    public void setFinalY(int newY) {
mFinalY = newY;
mDeltaY = mFinalY - mStartY;
mFinished = false;







