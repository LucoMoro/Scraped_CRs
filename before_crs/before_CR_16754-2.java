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

int mPointerLocationMode = 0;
PointerLocationView mPointerLocationView = null;

//Synthetic comment -- @@ -1630,6 +1643,9 @@
// keyguard up, then we can turn on the screen
// immediately.
mKeyguardMediator.pokeWakelock();
} else if (keyguardIsShowingTq()) {
if (mLidOpen) {
// If we are opening the lid and not hiding the
//Synthetic comment -- @@ -1769,7 +1785,8 @@

final boolean isKeyDown =
(event.type == RawInputEvent.EV_KEY) && (event.value != 0);
                if (isWakeKey && isKeyDown) {

// tell the mediator about a wake key, it may decide to
// turn on the screen depending on whether the key is
//Synthetic comment -- @@ -1806,6 +1823,12 @@
}
}

int type = event.type;
int code = event.keycode;
boolean down = event.value != 0;
//Synthetic comment -- @@ -2133,6 +2156,24 @@
//always return portrait if orientation set to portrait
return mPortraitRotation;
}
// case for nosensor meaning ignore sensor and consider only lid
// or orientation sensor disabled
//or case.unspecified
//Synthetic comment -- @@ -2244,18 +2285,45 @@
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
            rotation = mCarDockRotation;
        } else if (mDockMode == Intent.EXTRA_DOCK_STATE_DESK && mDeskDockRotation >= 0) {
            rotation = mDeskDockRotation;
}
//if lid is closed orientation will be portrait
try {
//Synthetic comment -- @@ -2424,4 +2492,63 @@
// disable key repeat when screen is off
return mScreenOn;
}
}







