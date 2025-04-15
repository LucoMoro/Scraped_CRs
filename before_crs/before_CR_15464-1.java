/*Lockscreen: Revert patch to skip draws

This patch reverts:

"Fix for 2292713: Remove workaround that hides SlidingTab
widget while internal state is inconsistent."

The problem with this fix is that sometimes,
mIsPortrait is assigned a incorrect value,
during bootup.

If this happens, the condition to skip the
lockscreen draw is always met, and we dont see
the lockscreen on bootup.

Change-Id:I504a90549b7ff87a424ef7d6b7ce621498eeab52Signed-off-by: Axel Haslam <axelhaslam@ti.com>*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 85918fb..00dc929 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
//Synthetic comment -- @@ -41,7 +40,6 @@
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.IOException;
//Synthetic comment -- @@ -141,8 +139,6 @@
*/
private final LockPatternUtils mLockPatternUtils;

    private boolean mIsPortrait;

/**
* @return Whether we are stuck on the lock screen because the sim is
*   missing.
//Synthetic comment -- @@ -270,7 +266,7 @@
public void reportFailedPatternAttempt() {
mUpdateMonitor.reportFailedAttempt();
final int failedAttempts = mUpdateMonitor.getFailedAttempts();
                if (DEBUG) Log.d(TAG,
"reportFailedPatternAttempt: #" + failedAttempts +
" (enableFallback=" + mEnableFallback + ")");
if (mEnableFallback && failedAttempts ==
//Synthetic comment -- @@ -311,7 +307,7 @@
mLockScreen = createLockScreen();
addView(mLockScreen);
final UnlockMode unlockMode = getUnlockMode();
        if (DEBUG) Log.d(TAG,
"LockPatternKeyguardView ctor: about to createUnlockScreenFor; mEnableFallback="
+ mEnableFallback);
mUnlockScreen = createUnlockScreenFor(unlockMode);
//Synthetic comment -- @@ -338,33 +334,6 @@
}


    // TODO:
    // This overloaded method was added to workaround a race condition in the framework between
    // notification for orientation changed, layout() and switching resources.  This code attempts
    // to avoid drawing the incorrect layout while things are in transition.  The method can just
    // be removed once the race condition is fixed. See bugs 2262578 and 2292713.
    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int orientation = getResources().getConfiguration().orientation;
        if (mIsPortrait && Configuration.ORIENTATION_PORTRAIT != orientation
                || getResources().getBoolean(R.bool.lockscreen_isPortrait) != mIsPortrait) {
            // Make sure we redraw once things settle down.
            // Log.v(TAG, "dispatchDraw(): not drawing because state is inconsistent");
            postInvalidate();

            // In order to minimize flashing, draw the first child's background for now.
            ViewGroup view = (ViewGroup) (mMode == Mode.LockScreen ? mLockScreen : mUnlockScreen);
            if (view != null && view.getChildAt(0) != null) {
                Drawable background = view.getChildAt(0).getBackground();
                if (background != null) {
                    background.draw(canvas);
                }
            }
            return;
        }
        super.dispatchDraw(canvas);
    }

@Override
public void reset() {
mIsVerifyUnlockOnly = false;
//Synthetic comment -- @@ -513,9 +482,6 @@
}

View createUnlockScreenFor(UnlockMode unlockMode) {
        // Capture the orientation this layout was created in.
        mIsPortrait = getResources().getBoolean(R.bool.lockscreen_isPortrait);

if (unlockMode == UnlockMode.Pattern) {
UnlockScreen view = new UnlockScreen(
mContext,







