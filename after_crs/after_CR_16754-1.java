/*This is a patch for clamshell shape device support.

- use lid switch for clamshell open/close switch
- disable orientation changed when lid switch state is changed
- notify the clamshell state change to PowerManagerService for
  screen-off/on control

Change-Id:Ia926b983ed6d2c10cc1dfa7f7aa774ace6216a46*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index dec495d..526d27d 100755

//Synthetic comment -- @@ -217,6 +217,19 @@
int mAccelerometerDefault = DEFAULT_ACCELEROMETER_ROTATION;
boolean mHasSoftInput = false;

    private static final int CLAMSHELL_STATE_UNINITIALIZED = 0; // not initialized
    private static final int CLAMSHELL_STATE_NOT_CLAMSHELL = 1; // device is not clam shell type
    private static final int CLAMSHELL_STATE_OPENED = 2;        // clam shell is opened (clam shell device)
    private static final int CLAMSHELL_STATE_CLOSED = 3;        // clam shell is closed (clam shell device)
    private int mClamshellState = CLAMSHELL_STATE_UNINITIALIZED;

    private static final int CLAMSHELL_ORIENTATION_UNINITIALIZED = 0;  // not initialized
    private static final int CLAMSHELL_ORIENTATION_DEFAULT = 1;        // not landscape
    private static final int CLAMSHELL_ORIENTATION_LANDSCAPE = 2;      // landscape when opened
    private int mClamshellOrientation = CLAMSHELL_ORIENTATION_UNINITIALIZED;
    // true if display has been enabled to draw.
    private boolean mLastDisplayEnabled = false;

int mPointerLocationMode = 0;
PointerLocationView mPointerLocationView = null;

//Synthetic comment -- @@ -1639,15 +1652,19 @@
KeyEvent.KEYCODE_POWER);
}
} else {
                        if (!isClamshell()) {
                            // Light up the keyboard if we are sliding up.
                            if (mLidOpen) {
                                mPowerManager.userActivity(SystemClock.uptimeMillis(), false,
                                        LocalPowerManager.BUTTON_EVENT);
                            } else {
                                mPowerManager.userActivity(SystemClock.uptimeMillis(), false,
                                        LocalPowerManager.OTHER_EVENT);
                            }
}
}
                    // Notify clam-schell open state.
                    notifyClamshcellOpenStateChange(event.value == 0);
}
}
return false;
//Synthetic comment -- @@ -1806,6 +1823,11 @@
}
}

        // MediaKey should be pass to user activity even if the clam-shell is closed.
        if ((mClamshellState == CLAMSHELL_STATE_CLOSED) && isMediaKey(event.keycode)) {
            result |= ACTION_PASS_TO_USER;
        }

int type = event.type;
int code = event.keycode;
boolean down = event.value != 0;
//Synthetic comment -- @@ -2133,6 +2155,24 @@
//always return portrait if orientation set to portrait
return mPortraitRotation;
}

            if (isClamshell()) {
                if (useSensorForOrientationLp(orientation)) {
                    // If the user has enabled auto rotation by default, do it.
                    int curRotation = mOrientationListener.getCurrentRotation();
                    if (curRotation >= 0) {
                        return curRotation;
                    } else if (mLastDisplayEnabled != displayEnabled) {
                        //caused wrong orientaion when displayEnabled first turns on.
                        mLastDisplayEnabled = displayEnabled;
                        return getRotationOnClamshellOpen();
                    } else {
                        return lastRotation;
                    }
                }
                return getRotationOnClamshellOpen();
            }

// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
//Synthetic comment -- @@ -2244,12 +2284,38 @@
/** {@inheritDoc} */
public void enableScreenAfterBoot() {
readLidState();

        // Initialize clam shell state and rotatation.
        mClamshellState = CLAMSHELL_STATE_NOT_CLAMSHELL;
        try {
            if (!isEmulator()
                && mContext.getResources().getBoolean(
                    com.android.internal.R.bool.config_clamshell_type)) {
                // set clam shell state and notify clam shell state to PowerManager
                mClamshellState = mLidOpen ? CLAMSHELL_STATE_CLOSED : CLAMSHELL_STATE_OPENED;

                mClamshellOrientation = CLAMSHELL_ORIENTATION_DEFAULT;
                if (mContext.getResources().getBoolean(com.android.internal.R.bool
                    .config_clamshell_orientation_to_landscape)) {
                    mClamshellOrientation = CLAMSHELL_ORIENTATION_LANDSCAPE;
                }

                notifyClamshcellOpenStateChange(mLidOpen);
            }
        } catch (Resources.NotFoundException e) {
        }

updateRotation(Surface.FLAGS_ORIENTATION_ANIMATION_DISABLE);
}

void updateRotation(int animFlags) {
mPowerManager.setKeyboardVisibility(mLidOpen);
int rotation = Surface.ROTATION_0;

        if (isClamshell()) {
            rotation = getRotationOnClamshellOpen();
        } else

if (mLidOpen) {
rotation = mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR && mCarDockRotation >= 0) {
//Synthetic comment -- @@ -2424,4 +2490,71 @@
// disable key repeat when screen is off
return mScreenOn;
}

    /**
     * Notify clam shell open state to PowerManager when open state is changed.
     *
     * @param isOpen clam shell open state.
     */
    private void notifyClamshcellOpenStateChange(final boolean isOpen) {
        switch (mClamshellState) {
        case CLAMSHELL_STATE_OPENED:
            if (!isOpen) {
                mPowerManager.notifyClamshcellClosed();
                mClamshellState = CLAMSHELL_STATE_CLOSED;
            }
            break;
        case CLAMSHELL_STATE_CLOSED:
            if (isOpen) {
                mPowerManager.notifyClamshellOpend();
                mClamshellState = CLAMSHELL_STATE_OPENED;
            }
            break;
        }
    }

    /**
     * Returns whether device is clamshell type or not.
     *
     * @return True if device is clamshell type.
     */
    private boolean isClamshell() {
        return (mClamshellState == CLAMSHELL_STATE_OPENED)
            || (mClamshellState == CLAMSHELL_STATE_CLOSED);
    }

    /**
     * Returns whether environment is emulator or not.
     *
     * @return True if environment is emulator.
     */
    private boolean isEmulator() {
        return SystemProperties.get("ro.kernel.qemu").equals("1");
    }

    /**
     * Gets rotation angle which appliciate for opening clamshell.
     *
     * @return Rotation angle for opening clamshell.
     */
    private int getRotationOnClamshellOpen() {
        if (mPortraitRotation < 0) {
            // Initialize the rotation angles for each orientation once.
            Display d = ((WindowManager) mContext.getSystemService(
                    Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (d.getWidth() > d.getHeight()) {
                mPortraitRotation = Surface.ROTATION_90;
                mLandscapeRotation = Surface.ROTATION_0;
            } else {
                mPortraitRotation = Surface.ROTATION_0;
                mLandscapeRotation = Surface.ROTATION_90;
            }
        }

        if (mClamshellOrientation == CLAMSHELL_ORIENTATION_LANDSCAPE) {
            return mLandscapeRotation;
        } else {
            return mPortraitRotation;
        }
    }
}







