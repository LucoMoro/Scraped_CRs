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

    // TODO: We access the following ThreadLocal variables often, some of them on every update.
    // If ThreadLocal access is significantly expensive, we may want to put all of these
    // fields into a structure sot hat we just access ThreadLocal once to get the reference
    // to that structure, then access the structure directly for each field.

// The static sAnimationHandler processes the internal timing loop on which all animations
// are based
private static ThreadLocal<AnimationHandler> sAnimationHandler =
new ThreadLocal<AnimationHandler>();

    // The per-thread list of all active animations
    private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations =
            new ThreadLocal<ArrayList<ValueAnimator>>() {
                @Override
                protected ArrayList<ValueAnimator> initialValue() {
                    return new ArrayList<ValueAnimator>();
                }
            };

    // The per-thread set of animations to be started on the next animation frame
    private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations =
            new ThreadLocal<ArrayList<ValueAnimator>>() {
@Override
                protected ArrayList<ValueAnimator> initialValue() {
                    return new ArrayList<ValueAnimator>();
                }
            };

    /**
     * Internal per-thread collections used to avoid set collisions as animations start and end
     * while being processed.
     */
    private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims =
            new ThreadLocal<ArrayList<ValueAnimator>>() {
                @Override
                protected ArrayList<ValueAnimator> initialValue() {
                    return new ArrayList<ValueAnimator>();
                }
            };

    private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims =
            new ThreadLocal<ArrayList<ValueAnimator>>() {
                @Override
                protected ArrayList<ValueAnimator> initialValue() {
                    return new ArrayList<ValueAnimator>();
                }
            };

    private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims =
            new ThreadLocal<ArrayList<ValueAnimator>>() {
                @Override
                protected ArrayList<ValueAnimator> initialValue() {
                    return new ArrayList<ValueAnimator>();
}
};

//Synthetic comment -- @@ -589,13 +558,14 @@
@Override
public void handleMessage(Message msg) {
boolean callAgain = true;
            ArrayList<ValueAnimator> animations = sAnimations.get();
            ArrayList<ValueAnimator> delayedAnims = sDelayedAnims.get();
switch (msg.what) {
// TODO: should we avoid sending frame message when starting if we
// were already running?
case ANIMATION_START:
                    ArrayList<ValueAnimator> pendingAnimations = sPendingAnimations.get();
if (animations.size() > 0 || delayedAnims.size() > 0) {
callAgain = false;
}
//Synthetic comment -- @@ -624,8 +594,8 @@
// currentTime holds the common time for all animations processed
// during this frame
long currentTime = AnimationUtils.currentAnimationTimeMillis();
                    ArrayList<ValueAnimator> readyAnims = sReadyAnims.get();
                    ArrayList<ValueAnimator> endingAnims = sEndingAnims.get();

// First, process animations currently sitting on the delayed queue, adding
// them to the active animations if they are ready
//Synthetic comment -- @@ -928,7 +898,7 @@
mPlayingState = STOPPED;
mStarted = true;
mStartedDelay = false;
        sPendingAnimations.get().add(this);
if (mStartDelay == 0) {
// This sets the initial value of the animation, prior to actually starting it running
setCurrentPlayTime(getCurrentPlayTime());
//Synthetic comment -- @@ -961,8 +931,9 @@
public void cancel() {
// Only cancel if the animation is actually running or has been started and is about
// to run
        if (mPlayingState != STOPPED || sPendingAnimations.get().contains(this) ||
                sDelayedAnims.get().contains(this)) {
// Only notify listeners if the animator has actually started
if (mRunning && mListeners != null) {
ArrayList<AnimatorListener> tmpListeners =
//Synthetic comment -- @@ -977,7 +948,9 @@

@Override
public void end() {
        if (!sAnimations.get().contains(this) && !sPendingAnimations.get().contains(this)) {
// Special case if the animation has not yet started; get it ready for ending
mStartedDelay = false;
startAnimation();
//Synthetic comment -- @@ -1028,9 +1001,10 @@
* called on the UI thread.
*/
private void endAnimation() {
        sAnimations.get().remove(this);
        sPendingAnimations.get().remove(this);
        sDelayedAnims.get().remove(this);
mPlayingState = STOPPED;
if (mRunning && mListeners != null) {
ArrayList<AnimatorListener> tmpListeners =
//Synthetic comment -- @@ -1050,7 +1024,7 @@
*/
private void startAnimation() {
initAnimation();
        sAnimations.get().add(this);
if (mStartDelay > 0 && mListeners != null) {
// Listeners were already notified in start() if startDelay is 0; this is
// just for delayed animations
//Synthetic comment -- @@ -1242,7 +1216,7 @@
* @hide
*/
public static int getCurrentAnimationsCount() {
        return sAnimations.get().size();
}

/**
//Synthetic comment -- @@ -1252,9 +1226,10 @@
* @hide
*/
public static void clearAllAnimations() {
        sAnimations.get().clear();
        sPendingAnimations.get().clear();
        sDelayedAnims.get().clear();
}

@Override







