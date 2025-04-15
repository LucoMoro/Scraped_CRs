/*Code re-factoring*/
//Synthetic comment -- diff --git a/core/java/android/widget/Scroller.java b/core/java/android/widget/Scroller.java
//Synthetic comment -- index c9ace0a..c8fcd5e6 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
/**
* This class encapsulates scrolling.  The duration of the scroll
* can be passed in the constructor and specifies the maximum time that
 * the scrolling animation should take.  Past this time, the scrolling is 
* automatically moved to its final stage and computeScrollOffset()
* will always return false to indicate that scrolling is over.
*/
//Synthetic comment -- @@ -84,83 +84,83 @@
* ppi    // pixels per inch
* ViewConfiguration.getScrollFriction();
}
    
/**
     * 
* Returns whether the scroller has finished scrolling.
     * 
* @return True if the scroller has finished scrolling, false otherwise.
*/
public final boolean isFinished() {
return mFinished;
}
    
/**
* Force the finished field to a particular value.
     *  
* @param finished The new finished value.
*/
public final void forceFinished(boolean finished) {
mFinished = finished;
}
    
/**
* Returns how long the scroll event will take, in milliseconds.
     * 
* @return The duration of the scroll in milliseconds.
*/
public final int getDuration() {
return mDuration;
}
    
/**
     * Returns the current X offset in the scroll. 
     * 
* @return The new X offset as an absolute distance from the origin.
*/
public final int getCurrX() {
return mCurrX;
}
    
/**
     * Returns the current Y offset in the scroll. 
     * 
* @return The new Y offset as an absolute distance from the origin.
*/
public final int getCurrY() {
return mCurrY;
}
    
/**
     * Returns the start X offset in the scroll. 
     * 
* @return The start X offset as an absolute distance from the origin.
*/
public final int getStartX() {
return mStartX;
}
    
/**
     * Returns the start Y offset in the scroll. 
     * 
* @return The start Y offset as an absolute distance from the origin.
*/
public final int getStartY() {
return mStartY;
}
    
/**
* Returns where the scroll will end. Valid only for "fling" scrolls.
     * 
* @return The final X offset as an absolute distance from the origin.
*/
public final int getFinalX() {
return mFinalX;
}
    
/**
* Returns where the scroll will end. Valid only for "fling" scrolls.
     * 
* @return The final Y offset as an absolute distance from the origin.
*/
public final int getFinalY() {
//Synthetic comment -- @@ -171,24 +171,24 @@
* Call this when you want to know the new location.  If it returns true,
* the animation is not yet finished.  loc will be altered to provide the
* new location.
     */ 
public boolean computeScrollOffset() {
if (mFinished) {
return false;
}

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    
if (timePassed < mDuration) {
switch (mMode) {
case SCROLL_MODE:
float x = (float)timePassed * mDurationReciprocal;
    
if (mInterpolator == null)
                    x = viscousFluid(x); 
else
x = mInterpolator.getInterpolation(x);
    
mCurrX = mStartX + Math.round(x * mDeltaX);
mCurrY = mStartY + Math.round(x * mDeltaY);
if ((mCurrX == mFinalX) && (mCurrY == mFinalY)) {
//Synthetic comment -- @@ -199,12 +199,12 @@
float timePassedSeconds = timePassed / 1000.0f;
float distance = (mVelocity * timePassedSeconds)
- (mDeceleration * timePassedSeconds * timePassedSeconds / 2.0f);
                
mCurrX = mStartX + Math.round(distance * mCoeffX);
// Pin to mMinX <= mCurrX <= mMaxX
mCurrX = Math.min(mCurrX, mMaxX);
mCurrX = Math.max(mCurrX, mMinX);
                
mCurrY = mStartY + Math.round(distance * mCoeffY);
// Pin to mMinY <= mCurrY <= mMaxY
mCurrY = Math.min(mCurrY, mMaxY);
//Synthetic comment -- @@ -213,7 +213,7 @@
if (mCurrX == mFinalX && mCurrY == mFinalY) {
mFinished = true;
}
                
break;
}
}
//Synthetic comment -- @@ -224,12 +224,12 @@
}
return true;
}
    
/**
* Start scrolling by providing a starting point and the distance to travel.
* The scroll will use the default value of 250 milliseconds for the
* duration.
     * 
* @param startX Starting horizontal scroll offset in pixels. Positive
*        numbers will scroll the content to the left.
* @param startY Starting vertical scroll offset in pixels. Positive numbers
//Synthetic comment -- @@ -245,7 +245,7 @@

/**
* Start scrolling by providing a starting point and the distance to travel.
     * 
* @param startX Starting horizontal scroll offset in pixels. Positive
*        numbers will scroll the content to the left.
* @param startY Starting vertical scroll offset in pixels. Positive numbers
//Synthetic comment -- @@ -278,7 +278,7 @@
/**
* Start scrolling based on a fling gesture. The distance travelled will
* depend on the initial velocity of the fling.
     * 
* @param startX Starting point of the scroll (X)
* @param startY Starting point of the scroll (Y)
* @param velocityX Initial velocity of the fling (X) measured in pixels per
//Synthetic comment -- @@ -300,7 +300,7 @@
mFinished = false;

float velocity = (float)Math.hypot(velocityX, velocityY);
     
mVelocity = velocity;
mDuration = (int) (1000 * velocity / mDeceleration); // Duration is in
// milliseconds
//Synthetic comment -- @@ -308,30 +308,30 @@
mStartX = startX;
mStartY = startY;

        mCoeffX = velocity == 0 ? 1.0f : velocityX / velocity; 
mCoeffY = velocity == 0 ? 1.0f : velocityY / velocity;

int totalDistance = (int) ((velocity * velocity) / (2 * mDeceleration));
        
mMinX = minX;
mMaxX = maxX;
mMinY = minY;
mMaxY = maxY;
        
        
mFinalX = startX + Math.round(totalDistance * mCoeffX);
// Pin to mMinX <= mFinalX <= mMaxX
mFinalX = Math.min(mFinalX, mMaxX);
mFinalX = Math.max(mFinalX, mMinX);
        
mFinalY = startY + Math.round(totalDistance * mCoeffY);
// Pin to mMinY <= mFinalY <= mMaxY
mFinalY = Math.min(mFinalY, mMaxY);
mFinalY = Math.max(mFinalY, mMinY);
}
    
    
    
private float viscousFluid(float x)
{
x *= mViscousFluidScale;
//Synthetic comment -- @@ -345,18 +345,18 @@
x *= mViscousFluidNormalize;
return x;
}
    
/**
     * 
*/
public void abortAnimation() {
mCurrX = mFinalX;
mCurrY = mFinalY;
mFinished = true;
}
    
/**
     * Extend the scroll animation. This allows a running animation to 
* scroll further and longer, when used with setFinalX() or setFinalY().
*
* @param extend Additional time to scroll in milliseconds.
//Synthetic comment -- @@ -367,11 +367,11 @@
mDurationReciprocal = 1.0f / (float)mDuration;
mFinished = false;
}
    
public int timePassed() {
return (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
}
    
public void setFinalX(int newX) {
mFinalX = newX;
mDeltaX = mFinalX - mStartX;







