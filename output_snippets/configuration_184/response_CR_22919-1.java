//<Beginning of snippet n. 0>


// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
            if (mLidOpen) {
                return mLidOpenRotation < 0 ? (mCarDockRotation >= 0 ? mCarDockRotation : Surface.ROTATION_0) : mLidOpenRotation;
            } else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
                return mCarDockRotation < 0 ? Surface.ROTATION_0 : mCarDockRotation;
            } else {
                return Surface.ROTATION_0; // Default case if both values are negative
            }
        }
        
        void updateRotation(int animFlags) {
            mPowerManager.setKeyboardVisibility(mLidOpen);
            int rotation = Surface.ROTATION_0;
            if (mLidOpen) {
                rotation = mLidOpenRotation < 0 ? (mCarDockRotation >= 0 ? mCarDockRotation : Surface.ROTATION_0) : mLidOpenRotation;
            } else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR) {
                rotation = mCarDockRotation < 0 ? Surface.ROTATION_0 : mCarDockRotation;
            } else {
                rotation = Surface.ROTATION_0; // Default case if both values are negative
            }
//<End of snippet n. 0>