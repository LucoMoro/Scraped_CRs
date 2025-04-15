/*Move individual ThreadLocal objects to a combined object

The ValueAnimator will do at least 2 calls to ThreadLocal.get each
time the animation is redrawn. This is a significant overhead when
animating using the Android Animation framework.

These objects are moved to a "holder" class so that only a single
ThreadLocal.get will ge required.

Data, as meassured in TraceView:
Starting Google Chrome for Android Beta, the inital tab there are
two animating blocks.

Before fix:
The total time (% of runtime) spent in ThreadLocal.get is 14.1%

After fix:
The total time (% of runtime) spent in ThreadLocal.get is 4.4%

Change-Id:Icc68ac756bcad4d9ab75dce3f6842b4c1bb37665*/




//Synthetic comment -- diff --git a/core/java/android/animation/ValueAnimator.java b/core/java/android/animation/ValueAnimator.java
//Synthetic comment -- index 55e95b0..1dd9693 100755

//Synthetic comment -- @@ -90,59 +90,28 @@
*/
long mSeekTime = -1;

// The static sAnimationHandler processes the internal timing loop on which all animations
// are based
private static ThreadLocal<AnimationHandler> sAnimationHandler =
new ThreadLocal<AnimationHandler>();

    private static final class AnimationHolder {
        ArrayList<ValueAnimator> mAnimations = new ArrayList<ValueAnimator>();
        ArrayList<ValueAnimator> mPendingAnimations = new ArrayList<ValueAnimator>();
        /**
         * Internal per-thread collections used to avoid set collisions as animations start and end
         * while being processed.
         */
        ArrayList<ValueAnimator> mDelayedAnims = new ArrayList<ValueAnimator>();
        ArrayList<ValueAnimator> mEndingAnims = new ArrayList<ValueAnimator>();
        ArrayList<ValueAnimator> mReadyAnims = new ArrayList<ValueAnimator>();
    }

    private static final ThreadLocal<AnimationHolder> sAnimationHolder =
            new ThreadLocal<AnimationHolder>() {
@Override
                protected AnimationHolder initialValue() {
                    return new AnimationHolder();
}
};

//Synthetic comment -- @@ -589,13 +558,14 @@
@Override
public void handleMessage(Message msg) {
boolean callAgain = true;
            final AnimationHolder holder = sAnimationHolder.get();
            ArrayList<ValueAnimator> animations = holder.mAnimations;
            ArrayList<ValueAnimator> delayedAnims = holder.mDelayedAnims;
switch (msg.what) {
// TODO: should we avoid sending frame message when starting if we
// were already running?
case ANIMATION_START:
                    ArrayList<ValueAnimator> pendingAnimations = holder.mPendingAnimations;
if (animations.size() > 0 || delayedAnims.size() > 0) {
callAgain = false;
}
//Synthetic comment -- @@ -624,8 +594,8 @@
// currentTime holds the common time for all animations processed
// during this frame
long currentTime = AnimationUtils.currentAnimationTimeMillis();
                    ArrayList<ValueAnimator> readyAnims = holder.mReadyAnims;
                    ArrayList<ValueAnimator> endingAnims = holder.mEndingAnims;

// First, process animations currently sitting on the delayed queue, adding
// them to the active animations if they are ready
//Synthetic comment -- @@ -928,7 +898,7 @@
mPlayingState = STOPPED;
mStarted = true;
mStartedDelay = false;
        sAnimationHolder.get().mPendingAnimations.add(this);
if (mStartDelay == 0) {
// This sets the initial value of the animation, prior to actually starting it running
setCurrentPlayTime(getCurrentPlayTime());
//Synthetic comment -- @@ -961,8 +931,9 @@
public void cancel() {
// Only cancel if the animation is actually running or has been started and is about
// to run
        final AnimationHolder holder = sAnimationHolder.get();
        if (mPlayingState != STOPPED || holder.mPendingAnimations.contains(this) ||
                holder.mDelayedAnims.contains(this)) {
// Only notify listeners if the animator has actually started
if (mRunning && mListeners != null) {
ArrayList<AnimatorListener> tmpListeners =
//Synthetic comment -- @@ -977,7 +948,9 @@

@Override
public void end() {
        final AnimationHolder holder = sAnimationHolder.get();
        if (holder.mAnimations.contains(this) &&
            !holder.mPendingAnimations.contains(this)) {
// Special case if the animation has not yet started; get it ready for ending
mStartedDelay = false;
startAnimation();
//Synthetic comment -- @@ -1028,9 +1001,10 @@
* called on the UI thread.
*/
private void endAnimation() {
        final AnimationHolder holder = sAnimationHolder.get();
        holder.mAnimations.remove(this);
        holder.mPendingAnimations.remove(this);
        holder.mDelayedAnims.remove(this);
mPlayingState = STOPPED;
if (mRunning && mListeners != null) {
ArrayList<AnimatorListener> tmpListeners =
//Synthetic comment -- @@ -1050,7 +1024,7 @@
*/
private void startAnimation() {
initAnimation();
        sAnimationHolder.get().mAnimations.add(this);
if (mStartDelay > 0 && mListeners != null) {
// Listeners were already notified in start() if startDelay is 0; this is
// just for delayed animations
//Synthetic comment -- @@ -1242,7 +1216,7 @@
* @hide
*/
public static int getCurrentAnimationsCount() {
        return sAnimationHolder.get().mAnimations.size();
}

/**
//Synthetic comment -- @@ -1252,9 +1226,10 @@
* @hide
*/
public static void clearAllAnimations() {
        final AnimationHolder holder = sAnimationHolder.get();
        holder.mAnimations.clear();
        holder.mPendingAnimations.clear();
        holder.mDelayedAnims.clear();
}

@Override







