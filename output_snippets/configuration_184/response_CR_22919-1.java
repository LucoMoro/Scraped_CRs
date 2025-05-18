//<Beginning of snippet n. 0>
void updateRotation(int animFlags) {
    mPowerManager.setKeyboardVisibility(mLidOpen);
    int rotation = Surface.ROTATION_0;

    if (mLidOpenRotation < 0 && mCarDockRotation < 0) {
        return rotation; // Default return value
    }

    if (mLidOpenRotation >= 0) {
        if (mLidOpen) {
            return mLidOpenRotation;
        }
    }

    if (mCarDockRotation >= 0 && mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
        return mCarDockRotation;
    }

    return rotation; // Default return value
}
//<End of snippet n. 0>