/*Use a circular buffer in VelocityTracker

Optimizes the frequently called addPoint() method while keeping the
same velocity computation.

Thx Cyril Mottier for help and tests.*/




//Synthetic comment -- diff --git a/core/java/android/view/VelocityTracker.java b/core/java/android/view/VelocityTracker.java
//Synthetic comment -- index 5d89c46..fd173eb 100644

//Synthetic comment -- @@ -59,6 +59,8 @@
final float mPastY[] = new float[NUM_PAST];
final long mPastTime[] = new long[NUM_PAST];

    int mLastTouch;

float mYVelocity;
float mXVelocity;

//Synthetic comment -- @@ -105,7 +107,10 @@
* Reset the velocity tracker back to its initial state.
*/
public void clear() {
        final long[] pastTime = mPastTime;
        for (int i = 0; i < NUM_PAST; i++) {
            pastTime[i] = 0;
        }
}

/**
//Synthetic comment -- @@ -128,42 +133,11 @@
}

private void addPoint(float x, float y, long time) {
        final int lastTouch = (mLastTouch + 1) % NUM_PAST;
        mPastX[lastTouch] = x;
        mPastY[lastTouch] = y;
        mPastTime[lastTouch] = time;
        mLastTouch = lastTouch;
}

/**
//Synthetic comment -- @@ -193,35 +167,39 @@
final float[] pastX = mPastX;
final float[] pastY = mPastY;
final long[] pastTime = mPastTime;
        final int lastTouch = mLastTouch;
        
        // find oldest acceptable time
        int oldestTouch = lastTouch;
        if (pastTime[lastTouch] > 0) { // cleared ?
            oldestTouch = (lastTouch + 1) % NUM_PAST;
            final float acceptableTime = pastTime[lastTouch] - LONGEST_PAST_TIME;
            while (pastTime[oldestTouch] < acceptableTime) {
                oldestTouch = (oldestTouch + 1) % NUM_PAST;
            }
        }

// Kind-of stupid.
        final float oldestX = pastX[oldestTouch];
        final float oldestY = pastY[oldestTouch];
        final long oldestTime = pastTime[oldestTouch];
float accumX = 0;
float accumY = 0;
        float N = (lastTouch - oldestTouch + NUM_PAST) % NUM_PAST + 1;
// Skip the last received event, since it is probably pretty noisy.
if (N > 3) N--;

for (int i=1; i < N; i++) {
            final int j = (oldestTouch + i) % NUM_PAST;
            final int dur = (int)(pastTime[j] - oldestTime);
if (dur == 0) continue;
            float dist = pastX[j] - oldestX;
float vel = (dist/dur) * units;   // pixels/frame.
            accumX = (accumX == 0) ? vel : (accumX + vel) * .5f;

            dist = pastY[j] - oldestY;
vel = (dist/dur) * units;   // pixels/frame.
            accumY = (accumY == 0) ? vel : (accumY + vel) * .5f;
}
mXVelocity = accumX < 0.0f ? Math.max(accumX, -maxVelocity) : Math.min(accumX, maxVelocity);
mYVelocity = accumY < 0.0f ? Math.max(accumY, -maxVelocity) : Math.min(accumY, maxVelocity);







