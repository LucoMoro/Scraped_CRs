//<Beginning of snippet n. 0>


// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
            if (mLidOpen) {
                if (mLidOpenRotation < 0) {
                    return 0; // fallback value
                }
                return mLidOpenRotation;
            } else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
                if (mCarDockRotation < 0) {
                    return 0; // fallback value
                }
                return mCarDockRotation;
            }

void updateRotation(int animFlags) {
    mPowerManager.setKeyboardVisibility(mLidOpen);
    int rotation = Surface.ROTATION_0;
    if (mLidOpen) {
        if (mLidOpenRotation < 0) {
            rotation = Surface.ROTATION_0; // fallback value
        } else {
            rotation = mLidOpenRotation;
        }
    } else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
        if (mCarDockRotation < 0) {
            rotation = Surface.ROTATION_0; // fallback value
        } else {
            rotation = mCarDockRotation;
        }
    }

//<End of snippet n. 0>