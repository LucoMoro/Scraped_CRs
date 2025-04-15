/*Use a circular buffer in VelocityTracker

Optimizes the frequently called addPoint() method while keeping the
same velocity computation.

Thx Cyril Mottier for help and tests.*/
//Synthetic comment -- diff --git a/core/java/android/view/VelocityTracker.java b/core/java/android/view/VelocityTracker.java
//Synthetic comment -- index 5d89c46..aeeb82c1 100644

//Synthetic comment -- @@ -59,6 +59,8 @@
final float mPastY[] = new float[NUM_PAST];
final long mPastTime[] = new long[NUM_PAST];

float mYVelocity;
float mXVelocity;

//Synthetic comment -- @@ -105,7 +107,10 @@
* Reset the velocity tracker back to its initial state.
*/
public void clear() {
        mPastTime[0] = 0;
}

/**
//Synthetic comment -- @@ -128,42 +133,11 @@
}

private void addPoint(float x, float y, long time) {
        int drop = -1;
        int i;
        if (localLOGV) Log.v(TAG, "Adding past y=" + y + " time=" + time);
        final long[] pastTime = mPastTime;
        for (i=0; i<NUM_PAST; i++) {
            if (pastTime[i] == 0) {
                break;
            } else if (pastTime[i] < time-LONGEST_PAST_TIME) {
                if (localLOGV) Log.v(TAG, "Dropping past too old at "
                        + i + " time=" + pastTime[i]);
                drop = i;
            }
        }
        if (localLOGV) Log.v(TAG, "Add index: " + i);
        if (i == NUM_PAST && drop < 0) {
            drop = 0;
        }
        if (drop == i) drop--;
        final float[] pastX = mPastX;
        final float[] pastY = mPastY;
        if (drop >= 0) {
            if (localLOGV) Log.v(TAG, "Dropping up to #" + drop);
            final int start = drop+1;
            final int count = NUM_PAST-drop-1;
            System.arraycopy(pastX, start, pastX, 0, count);
            System.arraycopy(pastY, start, pastY, 0, count);
            System.arraycopy(pastTime, start, pastTime, 0, count);
            i -= (drop+1);
        }
        pastX[i] = x;
        pastY[i] = y;
        pastTime[i] = time;
        i++;
        if (i < NUM_PAST) {
            pastTime[i] = 0;
        }
}

/**
//Synthetic comment -- @@ -193,35 +167,39 @@
final float[] pastX = mPastX;
final float[] pastY = mPastY;
final long[] pastTime = mPastTime;

// Kind-of stupid.
        final float oldestX = pastX[0];
        final float oldestY = pastY[0];
        final long oldestTime = pastTime[0];
float accumX = 0;
float accumY = 0;
        int N=0;
        while (N < NUM_PAST) {
            if (pastTime[N] == 0) {
                break;
            }
            N++;
        }
// Skip the last received event, since it is probably pretty noisy.
if (N > 3) N--;
        
for (int i=1; i < N; i++) {
            final int dur = (int)(pastTime[i] - oldestTime);
if (dur == 0) continue;
            float dist = pastX[i] - oldestX;
float vel = (dist/dur) * units;   // pixels/frame.
            if (accumX == 0) accumX = vel;
            else accumX = (accumX + vel) * .5f;

            dist = pastY[i] - oldestY;
vel = (dist/dur) * units;   // pixels/frame.
            if (accumY == 0) accumY = vel;
            else accumY = (accumY + vel) * .5f;
}
mXVelocity = accumX < 0.0f ? Math.max(accumX, -maxVelocity) : Math.min(accumX, maxVelocity);
mYVelocity = accumY < 0.0f ? Math.max(accumY, -maxVelocity) : Math.min(accumY, maxVelocity);







