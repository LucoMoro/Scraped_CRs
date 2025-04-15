/*Check mLidOpenRotation for negative value.

There are valid use cases where you might not want to rotate the
screen based on a lid open event. Behave just like docking and if
the rotation is negative, then ignore it.

Change-Id:I93226cc36a59b01ffe68dadbd8184a9a25caf25d*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java b/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index f934e6b..cd4d67a 100755

//Synthetic comment -- @@ -2098,7 +2098,7 @@
// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
            if (mLidOpen) {
return mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR && mCarDockRotation >= 0) {
return mCarDockRotation;
//Synthetic comment -- @@ -2244,7 +2244,7 @@
void updateRotation(int animFlags) {
mPowerManager.setKeyboardVisibility(mLidOpen);
int rotation = Surface.ROTATION_0;
        if (mLidOpen) {
rotation = mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR && mCarDockRotation >= 0) {
rotation = mCarDockRotation;







