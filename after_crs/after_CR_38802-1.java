/*Reduce an influence of a device cpu ability

The Textureview performance test is unfavorable to a single core
device because the other processing time would be added into the
frame-rate statistics.

So we need to improve the calculation of frame-rate statistics
by optimizing the time for thread wait.

(before)
  end     start=current              end
   |        |                         |
 --+--------+-------------------------+---->
            |<-- tick-(start%tick) -->|
            |<------- wait() -------->|

(after)
  end=start current         end
   |        |                |
 --+--------+----------------+---->
   |<-- tick-(start%tick) -->|
	    |<--  wait() --->|

Change-Id:I3c2d1e760a23a66c7c51e78a8c73c68887986eaf*/




//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java b/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6d86540..4fb1a02

//Synthetic comment -- @@ -54,6 +54,8 @@

private Random mRandom;

    private long mPrevQueueTime;

public FrameStats mUploadStats = new FrameStats();
public FrameStats mFirstUseStats = new FrameStats();

//Synthetic comment -- @@ -70,6 +72,7 @@
mRecycleTextures = recycleTextures;
mUploadType = uploadType;
mRandom = new Random(0);
        mPrevQueueTime = System.nanoTime();
}

@Override
//Synthetic comment -- @@ -228,13 +231,14 @@
public long WaitUntilNext60HzTick() {
// Wait until the next 60Hz tick.
long tickTime = 16666666; //60Hz tick
        long startTime = mPrevQueueTime;
long endTime = startTime + (tickTime - (startTime % tickTime));
        long currentTime = System.nanoTime();
while (currentTime < endTime) {
LockSupport.parkNanos(endTime - currentTime);
currentTime = System.nanoTime();
}
        mPrevQueueTime = currentTime;
return currentTime;
}








