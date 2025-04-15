/*Sempiternal status bar support.

Platforms on which config_statusBarCanHide is false do not
permit activities to hide the status bar using
FLAG_FULLSCREEN. (The flag is allowed, but it has no
effect.) In this case, FLAG_LAYOUT_IN_SCREEN and
FLAG_LAYOUT_INSET_DECOR are similarly ignored; it is as if
the status bar lies outside the addressable display
rectangle.

Change-Id:I6c1d9f617693eb30ebf96072f50c1520e6b4427e*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index dec495d..b8d1969 100755

//Synthetic comment -- @@ -191,6 +191,7 @@

boolean mSafeMode;
WindowState mStatusBar = null;
    boolean mStatusBarCanHide;
final ArrayList<WindowState> mStatusBarPanels = new ArrayList<WindowState>();
WindowState mKeyguard = null;
KeyguardViewMediator mKeyguardMediator;
//Synthetic comment -- @@ -548,6 +549,9 @@
com.android.internal.R.array.config_safeModeDisabledVibePattern);
mSafeModeEnabledVibePattern = getLongIntArray(mContext.getResources(),
com.android.internal.R.array.config_safeModeEnabledVibePattern);

        // Note: the Configuration is not stable here, so we cannot load mStatusBarCanHide from
        // config_statusBarCanHide because the latter depends on the screen size
}

public void updateSettings() {
//Synthetic comment -- @@ -964,6 +968,11 @@
return WindowManagerImpl.ADD_MULTIPLE_SINGLETON;
}
mStatusBar = win;

                // Determine whether applications may hide the status bar.
                // The Configuration will be stable by now, so we can load this safely.
                mStatusBarCanHide = mContext.getResources().getBoolean(
                        com.android.internal.R.bool.config_statusBarCanHide);
break;
case TYPE_STATUS_BAR_PANEL:
mStatusBarPanels.add(win);
//Synthetic comment -- @@ -1232,7 +1241,7 @@
public void getContentInsetHintLw(WindowManager.LayoutParams attrs, Rect contentInset) {
final int fl = attrs.flags;

        if (mStatusBarCanHide && (fl &
(FLAG_LAYOUT_IN_SCREEN | FLAG_FULLSCREEN | FLAG_LAYOUT_INSET_DECOR))
== (FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR)) {
contentInset.set(mCurLeft, mCurTop, mW - mCurRight, mH - mCurBottom);
//Synthetic comment -- @@ -1353,7 +1362,7 @@
attrs.gravity = Gravity.BOTTOM;
mDockLayer = win.getSurfaceLayer();
} else {
            if (mStatusBarCanHide && (fl &
(FLAG_LAYOUT_IN_SCREEN | FLAG_FULLSCREEN | FLAG_LAYOUT_INSET_DECOR))
== (FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR)) {
// This is the case for a normal activity window: we want it
//Synthetic comment -- @@ -1385,7 +1394,7 @@
vf.right = mCurRight;
vf.bottom = mCurBottom;
}
            } else if (mStatusBarCanHide && (fl & FLAG_LAYOUT_IN_SCREEN) != 0) {
// A window that has requested to fill the entire screen just
// gets everything, period.
pf.left = df.left = cf.left = 0;
//Synthetic comment -- @@ -1529,9 +1538,13 @@
boolean hideStatusBar =
(lp.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
if (hideStatusBar) {
                    if (mStatusBarCanHide) {
                        if (DEBUG_LAYOUT) Log.v(TAG, "Hiding status bar");
                        if (mStatusBar.hideLw(true)) changes |= FINISH_LAYOUT_REDO_LAYOUT;
                        hiding = true;
                    } else if (localLOGV) {
                        Log.v(TAG, "Preventing status bar from hiding by policy");
                    }
} else {
if (DEBUG_LAYOUT) Log.v(TAG, "Showing status bar");
if (mStatusBar.showLw(true)) changes |= FINISH_LAYOUT_REDO_LAYOUT;







