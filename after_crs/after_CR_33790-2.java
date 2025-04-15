/*Lid switch state is incorrect

InputManager#getSwitchState() returns OPEN(0) and CLOSE(1).
So PhoneWindowManager should handle the same as its value.

Change-Id:I40aa4e0defb95d82b6144ff6b7514f721bf9030f*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java b/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 0b223c1..b2b0dee 100755

//Synthetic comment -- @@ -308,10 +308,10 @@
int mRecentAppsDialogHeldModifiers;

private static final int LID_ABSENT = -1;
    private static final int LID_CLOSED = 1;
    private static final int LID_OPEN = 0;

    int mLidState = LID_ABSENT;

boolean mSystemReady;
boolean mSystemBooted;
//Synthetic comment -- @@ -1083,11 +1083,11 @@
try {
int sw = mWindowManager.getSwitchState(SW_LID);
if (sw > 0) {
                mLidState = LID_CLOSED;
} else if (sw == 0) {
                mLidState = LID_OPEN;
} else {
                mLidState = LID_ABSENT;
}
} catch (RemoteException e) {
// Ignore
//Synthetic comment -- @@ -1095,12 +1095,12 @@
}

private int determineHiddenState(int mode, int hiddenValue, int visibleValue) {
        if (mLidState != LID_ABSENT) {
switch (mode) {
case 1:
                    return mLidState == LID_OPEN ? visibleValue : hiddenValue;
case 2:
                    return mLidState == LID_OPEN ? hiddenValue : visibleValue;
}
}
return visibleValue;
//Synthetic comment -- @@ -2507,7 +2507,7 @@
/** {@inheritDoc} */
public void notifyLidSwitchChanged(long whenNanos, boolean lidOpen) {
// lid changed state
        mLidState = lidOpen ? LID_OPEN : LID_CLOSED;
updateKeyboardVisibility();

boolean awakeNow = mKeyguardMediator.doLidChangeTq(lidOpen);
//Synthetic comment -- @@ -3168,7 +3168,7 @@
}

final int preferredRotation;
            if (mLidState == LID_OPEN && mLidOpenRotation >= 0) {
// Ignore sensor when lid switch is open and rotation is forced.
preferredRotation = mLidOpenRotation;
} else if (mDockMode == Intent.EXTRA_DOCK_STATE_CAR
//Synthetic comment -- @@ -3513,7 +3513,7 @@
}

private void updateKeyboardVisibility() {
        mPowerManager.setKeyboardVisibility(mLidState == LID_OPEN);
}

void updateRotation(boolean alwaysSendConfiguration) {
//Synthetic comment -- @@ -3745,7 +3745,7 @@
pw.print(prefix); pw.print("mSafeMode="); pw.print(mSafeMode);
pw.print(" mSystemReady="); pw.print(mSystemReady);
pw.print(" mSystemBooted="); pw.println(mSystemBooted);
        pw.print(prefix); pw.print("mLidState="); pw.print(mLidState);
pw.print(" mLidOpenRotation="); pw.print(mLidOpenRotation);
pw.print(" mHdmiPlugged="); pw.println(mHdmiPlugged);
if (mLastSystemUiFlags != 0 || mResettingSystemUiFlags != 0







