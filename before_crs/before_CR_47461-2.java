/*Remove unnecessary references in WindowManagerService

Clean message routines in WindowManagerService.

Change-Id:I3e3106b6499c67d1fd7133c987c0452a73141a3fSigned-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index 51edb44..a1fc4e0 100644

//Synthetic comment -- @@ -4116,8 +4116,7 @@
mStartingIconInTransition = false;
mSkipAppTransitionAnimation = false;
mH.removeMessages(H.APP_TRANSITION_TIMEOUT);
                mH.sendMessageDelayed(mH.obtainMessage(H.APP_TRANSITION_TIMEOUT),
                        5000);
}
}
}
//Synthetic comment -- @@ -4688,8 +4687,7 @@
if (mAppsFreezingScreen == 1) {
startFreezingDisplayLocked(false, 0, 0);
mH.removeMessages(H.APP_FREEZE_TIMEOUT);
                    mH.sendMessageDelayed(mH.obtainMessage(H.APP_FREEZE_TIMEOUT),
                            5000);
}
}
final int N = wtoken.allAppWindows.size();
//Synthetic comment -- @@ -5209,8 +5207,7 @@
try {
startFreezingDisplayLocked(false, exitAnim, enterAnim);
mH.removeMessages(H.CLIENT_FREEZE_TIMEOUT);
                    mH.sendMessageDelayed(mH.obtainMessage(H.CLIENT_FREEZE_TIMEOUT),
                            5000);
} finally {
Binder.restoreCallingIdentity(origId);
}
//Synthetic comment -- @@ -5338,7 +5335,7 @@
}

// Persist setting
        mH.obtainMessage(H.PERSIST_ANIMATION_SCALE).sendToTarget();
}

public void setAnimationScales(float[] scales) {
//Synthetic comment -- @@ -5360,7 +5357,7 @@
}

// Persist setting
        mH.obtainMessage(H.PERSIST_ANIMATION_SCALE).sendToTarget();
}

private void setAnimatorDurationScale(float scale) {
//Synthetic comment -- @@ -5470,8 +5467,7 @@
hideBootMessagesLocked();
// If the screen still doesn't come up after 30 seconds, give
// up and turn it on.
            Message msg = mH.obtainMessage(H.BOOT_TIMEOUT);
            mH.sendMessageDelayed(msg, 30*1000);
}

mPolicy.systemBooted();
//Synthetic comment -- @@ -5494,7 +5490,7 @@
if (!mSystemBooted && !mShowingBootMessages) {
return;
}
        mH.sendMessage(mH.obtainMessage(H.ENABLE_SCREEN));
}

public void performBootTimeout() {
//Synthetic comment -- @@ -6056,7 +6052,7 @@

mWindowsFreezingScreen = true;
mH.removeMessages(H.WINDOW_FREEZE_TIMEOUT);
        mH.sendMessageDelayed(mH.obtainMessage(H.WINDOW_FREEZE_TIMEOUT),
WINDOW_FREEZE_TIMEOUT_DURATION);
mWaitingForConfig = true;
getDefaultDisplayContentLocked().layoutNeeded = true;
//Synthetic comment -- @@ -7585,8 +7581,7 @@
if (mAnimator.mAnimating || mLayoutToAnim.mAnimationScheduled) {
// If we are animating, don't do the gc now but
// delay a bit so we don't interrupt the animation.
                                mH.sendMessageDelayed(mH.obtainMessage(H.FORCE_GC),
                                        2000);
return;
}
// If we are currently rotating the display, it will
//Synthetic comment -- @@ -7711,7 +7706,7 @@
// Used to send multiple changes from the animation side to the layout side.
synchronized (mWindowMap) {
if (copyAnimToLayoutParamsLocked()) {
                            mH.sendEmptyMessage(CLEAR_PENDING_ACTIONS);
performLayoutAndPlaceSurfacesLocked();
}
}
//Synthetic comment -- @@ -8300,7 +8295,7 @@

if (mWindowsChanged && !mWindowChangeListeners.isEmpty()) {
mH.removeMessages(H.REPORT_WINDOWS_CHANGE);
                mH.sendMessage(mH.obtainMessage(H.REPORT_WINDOWS_CHANGE));
}
} catch (RuntimeException e) {
mInLayout = false;
//Synthetic comment -- @@ -8496,8 +8491,8 @@
// XXX should probably keep timeout from
// when we first froze the display.
mH.removeMessages(H.WINDOW_FREEZE_TIMEOUT);
                mH.sendMessageDelayed(mH.obtainMessage(
                        H.WINDOW_FREEZE_TIMEOUT), WINDOW_FREEZE_TIMEOUT_DURATION);
}
}
}
//Synthetic comment -- @@ -10174,8 +10169,7 @@
// processes holds on others can be released if they are
// no longer needed.
mH.removeMessages(H.FORCE_GC);
        mH.sendMessageDelayed(mH.obtainMessage(H.FORCE_GC),
                2000);

mScreenFrozenLock.release();








