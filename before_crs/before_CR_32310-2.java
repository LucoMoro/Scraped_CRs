/*Fix don't interrupt ongoing PIN/PUK verification

This fixes an issue which only occurs if
1. you use a SIM PIN
2. the PIN verification takes longer than the screen timeout is. e.g. when then screen turns black while "unlocking SIM" message is displayed!

It might be possible to simulate the bug by pressing power quickly after the PIN has ben entered and "unlocking SIM" message is still being displayed.

If the PIN verification takes longer thant the screen timeout is then the screen turns black.
If that happens LockPatternKeyguardView.reset() is called which then forces a re-creation of the view. But then the original created
SimUnlockScreen.onSimLockChangedResponse callback never receives the success message for the SIM unlock.
What then happens is that the LockPatternKeyguardView is re-created again and again whenever screen turns off while waiting
until we receive a SIM state changed event e.g. until we are back on network.
But when that happens we are presented the pattern unlock screen. With a single touch we can then (finally) get on the homescreen.

The fix removes the forced re-creation of the LockPatternKeyguardView if there is a PIN/PUK verification in progress. Which leads to a 'normal' unlock behavior:
1. enter pin
2. "unlocking SIM" message is displayed
3. maybe screen turns off after timeout
4. after some seconds you get on the homescreen directly

Change-Id:Ib3080a8844a36e27226f6d5e6ca4f50d79364c75*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/AccountUnlockScreen.java b/policy/src/com/android/internal/policy/impl/AccountUnlockScreen.java
//Synthetic comment -- index a4baeed..398d6b3 100644

//Synthetic comment -- @@ -133,6 +133,11 @@
}

/** {@inheritDoc} */
public boolean needsInput() {
return true;
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/KeyguardScreen.java b/policy/src/com/android/internal/policy/impl/KeyguardScreen.java
//Synthetic comment -- index bbb6875..d27caa1 100644

//Synthetic comment -- @@ -42,4 +42,9 @@
* This view is going away; a hook to do cleanup.
*/
void cleanUp();
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/LockPatternKeyguardView.java b/policy/src/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 6eff4b6..054a36e 100644

//Synthetic comment -- @@ -227,7 +227,8 @@

private Runnable mRecreateRunnable = new Runnable() {
public void run() {
            updateScreen(mMode, true);
restoreWidgetState();
}
};








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/LockScreen.java b/policy/src/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index 24a2420..0e0e2ec 100644

//Synthetic comment -- @@ -430,6 +430,11 @@
}

/** {@inheritDoc} */
public boolean needsInput() {
return false;
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PasswordUnlockScreen.java b/policy/src/com/android/internal/policy/impl/PasswordUnlockScreen.java
//Synthetic comment -- index 06cd69e..4e37976 100644

//Synthetic comment -- @@ -261,6 +261,11 @@
}

/** {@inheritDoc} */
public boolean needsInput() {
return mUseSystemIME && mIsAlpha;
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PatternUnlockScreen.java b/policy/src/com/android/internal/policy/impl/PatternUnlockScreen.java
//Synthetic comment -- index 9a6d2cc..a8ab4de 100644

//Synthetic comment -- @@ -250,6 +250,11 @@
public void onKeyboardChange(boolean isKeyboardOpen) {}

/** {@inheritDoc} */
public boolean needsInput() {
return false;
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/SimPukUnlockScreen.java b/policy/src/com/android/internal/policy/impl/SimPukUnlockScreen.java
//Synthetic comment -- index ba06996..53bb2b7 100644

//Synthetic comment -- @@ -43,6 +43,8 @@
public class SimPukUnlockScreen extends LinearLayout implements KeyguardScreen,
View.OnClickListener, View.OnFocusChangeListener {

private static final int DIGIT_PRESS_WAKE_MILLIS = 5000;

private final KeyguardUpdateMonitor mUpdateMonitor;
//Synthetic comment -- @@ -115,6 +117,11 @@
}

/** {@inheritDoc} */
public boolean needsInput() {
return false;
}
//Synthetic comment -- @@ -238,6 +245,7 @@
return;
}

getSimUnlockProgressDialog().show();

new CheckSimPuk(mPukText.getText().toString(),
//Synthetic comment -- @@ -245,6 +253,7 @@
void onSimLockChangedResponse(final boolean success) {
mPinText.post(new Runnable() {
public void run() {
if (mSimUnlockProgressDialog != null) {
mSimUnlockProgressDialog.hide();
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java b/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index 9604cdc..c1e4da9 100644

//Synthetic comment -- @@ -41,6 +41,8 @@
*/
public class SimUnlockScreen extends LinearLayout implements KeyguardScreen, View.OnClickListener {

private static final int DIGIT_PRESS_WAKE_MILLIS = 5000;

private final KeyguardUpdateMonitor mUpdateMonitor;
//Synthetic comment -- @@ -106,6 +108,11 @@
}

/** {@inheritDoc} */
public boolean needsInput() {
return true;
}
//Synthetic comment -- @@ -211,12 +218,15 @@
mCallback.pokeWakelock();
return;
}
getSimUnlockProgressDialog().show();

new CheckSimPin(mPinText.getText().toString()) {
void onSimLockChangedResponse(final boolean success) {
mPinText.post(new Runnable() {
public void run() {
if (mSimUnlockProgressDialog != null) {
mSimUnlockProgressDialog.hide();
}








//Synthetic comment -- diff --git a/policy/tests/src/com/android/internal/policy/impl/LockPatternKeyguardViewTest.java b/policy/tests/src/com/android/internal/policy/impl/LockPatternKeyguardViewTest.java
//Synthetic comment -- index bdfe652..e08cfcb 100644

//Synthetic comment -- @@ -91,6 +91,11 @@
}

/** {@inheritDoc} */
public boolean needsInput() {
return false;
}







