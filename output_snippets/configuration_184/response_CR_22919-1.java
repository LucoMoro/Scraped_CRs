//<Beginning of snippet n. 0>

if (mLidOpen) {
    return (mLidOpenRotation >= 0) ? mLidOpenRotation : Surface.ROTATION_0;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
    return (mCarDockRotation >= 0) ? mCarDockRotation : Surface.ROTATION_0;
}

void updateRotation(int animFlags) {
    mPowerManager.setKeyboardVisibility(mLidOpen);
    int rotation = Surface.ROTATION_0;
    if (mLidOpen) {
        rotation = (mLidOpenRotation >= 0) ? mLidOpenRotation : Surface.ROTATION_0;
    } else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
        rotation = (mCarDockRotation >= 0) ? mCarDockRotation : Surface.ROTATION_0;
    }
}

//<End of snippet n. 0>