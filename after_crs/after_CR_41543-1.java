/*wakelock with timeout under-locked exception risk

When a wakelock is acquired with a timeout
and is released explicitely before the timeout ends,
it will issue a java runtime under-locked exception.
It happens because the releaser doesn't check
if the wakelock is still held before releasing it.

The fix consists in having the releaser check
if the wakelock is still held before releasing
it in an atomic way to prevent explicit release
concurrency between isHeld() and release() calls.
To be fully thread safe the explicit release needs
to use safeRelease() method as well.

Change-Id:I6de27a5aa8931187877b13e30c8a9a069b7bd622Author: Fabien Peix <fabienx.peix@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 6705 17106*/




//Synthetic comment -- diff --git a/core/java/android/os/PowerManager.java b/core/java/android/os/PowerManager.java
//Synthetic comment -- index 903c8b3..f7e92d2 100644

//Synthetic comment -- @@ -236,7 +236,7 @@

Runnable mReleaser = new Runnable() {
public void run() {
                safeRelease();
}
};
	
//Synthetic comment -- @@ -247,6 +247,7 @@
boolean mRefCounted = true;
boolean mHeld = false;
WorkSource mWorkSource;
        Object mSafeReleaseLock;

WakeLock(int flags, String tag)
{
//Synthetic comment -- @@ -264,6 +265,7 @@
mFlags = flags;
mTag = tag;
mToken = new Binder();
            mSafeReleaseLock = new Object();
}

/**
//Synthetic comment -- @@ -326,6 +328,26 @@

/**
* Release your claim to the CPU or screen being on.
         * Checks atomically if the wakelock is still held before releasing it.
         * This method should be used every time there's a possibility that a
         * timeout release happens at the same time as a manual release.
         *
         * <p>
         * It may turn off shortly after you release it, or it may not if there
         * are other wake locks held.
         *
         * {@hide}
         */
        public void safeRelease() {
            synchronized(mSafeReleaseLock) {
                if (isHeld())
                    release(0);
            }
        }


        /**
         * Release your claim to the CPU or screen being on.
* @param flags Combination of flag values to modify the release behavior.
*              Currently only {@link #WAIT_FOR_PROXIMITY_NEGATIVE} is supported.
*







