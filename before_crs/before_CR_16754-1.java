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

int mPointerLocationMode = 0;
PointerLocationView mPointerLocationView = null;

//Synthetic comment -- @@ -1639,15 +1652,19 @@
KeyEvent.KEYCODE_POWER);
}
} else {
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
}
return false;
//Synthetic comment -- @@ -1806,6 +1823,11 @@
}
}

int type = event.type;
int code = event.keycode;
boolean down = event.value != 0;
//Synthetic comment -- @@ -2133,6 +2155,24 @@
//always return portrait if orientation set to portrait
return mPortraitRotation;
}
// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
//Synthetic comment -- @@ -2244,12 +2284,38 @@
/** {@inheritDoc} */
public void enableScreenAfterBoot() {
readLidState();
updateRotation(Surface.FLAGS_ORIENTATION_ANIMATION_DISABLE);
}

void updateRotation(int animFlags) {
mPowerManager.setKeyboardVisibility(mLidOpen);
int rotation = Surface.ROTATION_0;
if (mLidOpen) {
rotation = mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR && mCarDockRotation >= 0) {
//Synthetic comment -- @@ -2424,4 +2490,71 @@
// disable key repeat when screen is off
return mScreenOn;
}
}







