/*Fix unexpected rotation change when re-enabling auto-rotate

getProposedRotation() method returns old value when re-enabling
auto-rotate option. So you can see unexpected rotation change
with the following steps.

settings -> display -> enable auto-rotate
-> rotate device to landscape -> disable auto-rotate
-> rotate device to portrait -> enable auto-rotate

This patch makes mSensorEventListener reset before it is registered again.

Change-Id:I6a8d287bbd9809ef31de67c54efb885e9a4fd76fSigned-off-by: Sangkyu Lee <sk82.lee@lge.com>*/




//Synthetic comment -- diff --git a/core/java/android/view/WindowOrientationListener.java b/core/java/android/view/WindowOrientationListener.java
//Synthetic comment -- index 4c34dd4..495f4bcf 100644

//Synthetic comment -- @@ -98,6 +98,7 @@
if (LOG) {
Log.d(TAG, "WindowOrientationListener enabled");
}
            mSensorEventListener.reset();
mSensorManager.registerListener(mSensorEventListener, mSensor, mRate);
mEnabled = true;
}
//Synthetic comment -- @@ -637,7 +638,7 @@
return true;
}

        public void reset() {
mLastFilteredTimestampNanos = Long.MIN_VALUE;
mProposedRotation = -1;
mFlatTimestampNanos = Long.MIN_VALUE;







