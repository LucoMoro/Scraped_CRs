/*This is a patch for clamshell shape device support.

- use lid switch for clamshell open/close switch
- disable orientation changed when lid switch state is changed
- notify the clamshell state change to PowerManagerService for
  screen-off/on control

Change-Id:Ia926b983ed6d2c10cc1dfa7f7aa774ace6216a46*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index dec495d..859341d 100755

//Synthetic comment -- @@ -217,6 +217,19 @@
int mAccelerometerDefault = DEFAULT_ACCELEROMETER_ROTATION;
boolean mHasSoftInput = false;

    private static final int CLAMSHELL_STATE_UNINITIALIZED = 0; // not initialized
    private static final int CLAMSHELL_STATE_NOT_CLAMSHELL = 1; // device is not clam shell type
    private static final int CLAMSHELL_STATE_OPENED = 2;        // clamshell is opened
    private static final int CLAMSHELL_STATE_CLOSED = 3;        // clamshell is closed
    private int mClamshellState = CLAMSHELL_STATE_UNINITIALIZED;

    private static final int CLAMSHELL_ORIENTATION_UNINITIALIZED = 0;  // not initialized
    private static final int CLAMSHELL_ORIENTATION_DEFAULT = 1;        // not landscape
    private static final int CLAMSHELL_ORIENTATION_LANDSCAPE = 2;      // landscape when opened
    private int mClamshellOrientation = CLAMSHELL_ORIENTATION_UNINITIALIZED;
    // true if display has been enabled to draw.
    private boolean mLastDisplayEnabled = false;

int mPointerLocationMode = 0;
PointerLocationView mPointerLocationView = null;

//Synthetic comment -- @@ -1630,6 +1643,9 @@
// keyguard up, then we can turn on the screen
// immediately.
mKeyguardMediator.pokeWakelock();
                    } else if (isClamshell()) {
                        // Notify clamshell open state.
                        notifyClamshellOpenStateChange(mLidOpen);
} else if (keyguardIsShowingTq()) {
if (mLidOpen) {
// If we are opening the lid and not hiding the
//Synthetic comment -- @@ -1769,7 +1785,8 @@

final boolean isKeyDown =
(event.type == RawInputEvent.EV_KEY) && (event.value != 0);
                if (isWakeKey && isKeyDown
                        && (mClamshellState != CLAMSHELL_STATE_CLOSED)) {

// tell the mediator about a wake key, it may decide to
// turn on the screen depending on whether the key is
//Synthetic comment -- @@ -1806,6 +1823,12 @@
}
}

        // MediaKey should be pass to user activity even if the clamshell is closed.
        if ((mClamshellState == CLAMSHELL_STATE_CLOSED)
                && isMediaKey(event.keycode)) {
            result |= ACTION_PASS_TO_USER;
        }

int type = event.type;
int code = event.keycode;
boolean down = event.value != 0;
//Synthetic comment -- @@ -2133,6 +2156,24 @@
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
//Synthetic comment -- @@ -2244,18 +2285,45 @@
/** {@inheritDoc} */
public void enableScreenAfterBoot() {
readLidState();

        // Initialize clamshell state and rotatation.
        mClamshellState = CLAMSHELL_STATE_NOT_CLAMSHELL;
        try {
            if (mContext.getResources().getBoolean(
                    com.android.internal.R.bool.config_clamshellType)) {
                // set clamshell state and notify it to PowerManagerService
                mClamshellState = mLidOpen ?
                        CLAMSHELL_STATE_CLOSED : CLAMSHELL_STATE_OPENED;

                mClamshellOrientation = CLAMSHELL_ORIENTATION_DEFAULT;
                if (mLidOpenRotation == Surface.ROTATION_90) {
                    mClamshellOrientation = CLAMSHELL_ORIENTATION_LANDSCAPE;
                }

                notifyClamshellOpenStateChange(mLidOpen);
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
        } else {
            if (mLidOpen) {
                rotation = mLidOpenRotation;
            } else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR &&
                    mCarDockRotation >= 0) {
                rotation = mCarDockRotation;
            } else if (mDockMode == Intent.EXTRA_DOCK_STATE_DESK &&
                    mDeskDockRotation >= 0) {
                rotation = mDeskDockRotation;
            }
}
//if lid is closed orientation will be portrait
try {
//Synthetic comment -- @@ -2424,4 +2492,63 @@
// disable key repeat when screen is off
return mScreenOn;
}

    /**
     * Notify clam shell open state to PowerManager when open state is changed.
     *
     * @param isOpen clam shell open state.
     */
    private void notifyClamshellOpenStateChange(final boolean isOpen) {
        switch (mClamshellState) {
        case CLAMSHELL_STATE_OPENED:
            if (!isOpen) {
                mPowerManager.notifyClamshellClosed();
                mClamshellState = CLAMSHELL_STATE_CLOSED;
            }
            break;
        case CLAMSHELL_STATE_CLOSED:
            if (isOpen) {
                mPowerManager.notifyClamshellOpened();
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








