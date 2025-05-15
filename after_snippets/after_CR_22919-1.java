
//<Beginning of snippet n. 0>


// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
            if (mLidOpen && mLidOpenRotation >= 0) {
return mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR && mCarDockRotation >= 0) {
return mCarDockRotation;
void updateRotation(int animFlags) {
mPowerManager.setKeyboardVisibility(mLidOpen);
int rotation = Surface.ROTATION_0;
        if (mLidOpen && mLidOpenRotation >= 0) {
rotation = mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR && mCarDockRotation >= 0) {
rotation = mCarDockRotation;

//<End of snippet n. 0>








