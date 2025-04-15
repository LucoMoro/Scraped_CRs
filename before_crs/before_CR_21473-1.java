/*Fix leak when keylock is recreated.

DigitalClock could sometimes leak when the keylock was recreated.
This happened because onDetachedFromWindow() was called BEFORE
onAttachedFromWindow().
This is the flow that causes the memory leak:
1) The LockPatternKeyGuardView is created and added. This will start
a loop dispatching onAttachedToWindow() to all views involved.
2) PatternUnlockScreen.onAttachedToWindow() is called
3) If the configuration has changed since creation, recreateMe() in
LockPatternKeyguardView.java is called.
4) recreateScreens() is called
5) PatternUnlockScreen is removed (to be re-added later) in
LockPatternKeyguardView.recreateUnlockScreen()
6) Since DigitalClock is a part of PatternUnlockScreen, its
onDetachedFromWindow() will be called.
7) The loop started in 1) will continue to dispatch
onAttachedToWindow() - and will eventually call
DigitalClock.onAttachedToWindow()
8) DigitalClock.onAttachedToWindow() registers a receiver that is
normally unregistered in onDetachedFromWindow(). But since
onDetachedFromWindow was already called in 6), it will not be called
again.
9) The receiver has leaked, and it has a reference to DigitalClock,
so that will leak as well, together with its parents e.g.
PatternUnlockScreen and LockPatternKeyguardView

The fix is to wait with the recreation of the screens (in 4) until
the loop (in 1) is finished. This is done by posting this as an event
instead of calling recreateScreens() immediately.

It is possible that this a fix for the root cause mentioned in
"Fix 3106227: use WeakReferences for receivers in DigitalClock class"
8b886fab5496b0b1f5193f21855220176deddc37 by Jim Miller
<jaggies@google.com>.

Change-Id:I6a5f6f49a565d459bf4e285f34f053cc1022286f*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/LockPatternKeyguardView.java b/policy/src/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 27706ef..85d09f9 100644

//Synthetic comment -- @@ -163,6 +163,12 @@
*/
private Configuration mConfiguration;

/**
* @return Whether we are stuck on the lock screen because the sim is
*   missing.
//Synthetic comment -- @@ -244,7 +250,8 @@

public void recreateMe(Configuration config) {
mConfiguration = config;
                recreateScreens();
}

public void takeEmergencyCallAction() {
//Synthetic comment -- @@ -463,6 +470,12 @@
}

@Override
public void wakeWhenReadyTq(int keyCode) {
if (DEBUG) Log.d(TAG, "onWakeKey");
if (keyCode == KeyEvent.KEYCODE_MENU && isSecure() && (mMode == Mode.LockScreen)







