/*Merge commit 'remotes/korg/cupcake' into merge*/
//Synthetic comment -- diff --git a/mid/com/android/internal/policy/impl/MidWindow.java b/mid/com/android/internal/policy/impl/MidWindow.java
//Synthetic comment -- index 17293b5..536ee65 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.CallLog.Calls;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.EventLog;
//Synthetic comment -- @@ -147,6 +148,8 @@
private long mVolumeKeyUpTime;

private KeyguardManager mKeyguardManager = null;

private boolean mSearchKeyDownReceived;

//Synthetic comment -- @@ -1117,8 +1120,19 @@
return true;
}

case KeyEvent.KEYCODE_HEADSETHOOK: 
            case KeyEvent.KEYCODE_PLAYPAUSE: 
case KeyEvent.KEYCODE_STOP: 
case KeyEvent.KEYCODE_NEXTSONG: 
case KeyEvent.KEYCODE_PREVIOUSSONG: 








//Synthetic comment -- diff --git a/mid/com/android/internal/policy/impl/MidWindowManager.java b/mid/com/android/internal/policy/impl/MidWindowManager.java
//Synthetic comment -- index b9ed39a..8d7f28a 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import android.util.Config;
import android.util.EventLog;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
//Synthetic comment -- @@ -80,6 +81,7 @@
import static android.view.WindowManager.LayoutParams.TYPE_TOAST;
import android.view.WindowManagerImpl;
import android.view.WindowManagerPolicy;
import android.media.IAudioService;
import android.media.AudioManager;

//Synthetic comment -- @@ -131,6 +133,7 @@
private IWindowManager mWindowManager;
private LocalPowerManager mPowerManager;

private WindowState mStatusBar = null;
private WindowState mSearchBar = null;
private WindowState mKeyguard = null;
//Synthetic comment -- @@ -766,7 +769,7 @@
if (mTopFullscreenOpaqueWindowState == null
&& attrs.type >= FIRST_APPLICATION_WINDOW
&& attrs.type <= LAST_APPLICATION_WINDOW
            && win.fillsScreenLw(mW, mH, true)
&& win.isDisplayedLw()) {
mTopFullscreenOpaqueWindowState = win;
} else if ((attrs.flags & FLAG_FORCE_NOT_FULLSCREEN) != 0) {
//Synthetic comment -- @@ -968,17 +971,28 @@
mContext.sendBroadcast(intent);
}

    public int rotationForOrientation(int orientation) {
// get rid of rotation for now. Always rotation of 0
return Surface.ROTATION_0;
}

/** {@inheritDoc} */
public void systemReady() {
try {
            int menuState = mWindowManager.getKeycodeState(KeyEvent.KEYCODE_MENU);
            Log.i(TAG, "Menu key state: " + menuState);
            if (menuState > 0) {
// If the user is holding the menu key code, then we are
// going to boot into safe mode.
ActivityManagerNative.getDefault().enterSafeMode();
//Synthetic comment -- @@ -1035,9 +1049,16 @@
return true;
}

    public void setCurrentOrientation(int newOrientation) {
if(newOrientation != mCurrentAppOrientation) {
mCurrentAppOrientation = newOrientation;
}
}
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/AccountUnlockScreen.java b/phone/com/android/internal/policy/impl/AccountUnlockScreen.java
//Synthetic comment -- index 98cb548..65102c6 100644

//Synthetic comment -- @@ -28,8 +28,11 @@
import android.graphics.Rect;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -47,13 +50,16 @@
* IAccountsService.
*/
public class AccountUnlockScreen extends RelativeLayout implements KeyguardScreen,
        View.OnClickListener, ServiceConnection {


private static final String LOCK_PATTERN_PACKAGE = "com.android.settings";
private static final String LOCK_PATTERN_CLASS =
"com.android.settings.ChooseLockPattern";

private final KeyguardScreenCallback mCallback;
private final LockPatternUtils mLockPatternUtils;
private IAccountsService mAccountsService;
//Synthetic comment -- @@ -87,8 +93,10 @@

mLogin = (EditText) findViewById(R.id.login);
mLogin.setFilters(new InputFilter[] { new LoginFilter.UsernameFilterGeneric() } );

mPassword = (EditText) findViewById(R.id.password);

mOk = (Button) findViewById(R.id.ok);
mOk.setOnClickListener(this);
//Synthetic comment -- @@ -105,6 +113,16 @@
}
}

@Override
protected boolean onRequestFocusInDescendants(int direction,
Rect previouslyFocusedRect) {
//Synthetic comment -- @@ -113,6 +131,11 @@
}

/** {@inheritDoc} */
public void onPause() {

}
//Synthetic comment -- @@ -132,6 +155,7 @@

/** {@inheritDoc} */
public void onClick(View v) {
if (v == mOk) {
if (checkPassword()) {
// clear out forgotten password
//Synthetic comment -- @@ -167,11 +191,75 @@
return super.dispatchKeyEvent(event);
}

private boolean checkPassword() {
final String login = mLogin.getText().toString();
final String password = mPassword.getText().toString();
try {
            return mAccountsService.shouldUnlock(login, password);
} catch (RemoteException e) {
return false;
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/GlobalActions.java b/phone/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index 7f8e57b..eeb875a 100644

//Synthetic comment -- @@ -16,29 +16,30 @@

package com.android.internal.policy.impl;

import com.android.internal.R;
import com.google.android.collect.Lists;

import android.app.AlertDialog;
import android.app.StatusBarManager;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.LocalPowerManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//Synthetic comment -- @@ -49,34 +50,41 @@
*/
class GlobalActions implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener  {

private StatusBarManager mStatusBar;

private final Context mContext;
    private final LocalPowerManager mPowerManager;
private final AudioManager mAudioManager;
private ArrayList<Action> mItems;
private AlertDialog mDialog;

private ToggleAction mSilentModeToggle;

private MyAdapter mAdapter;

private boolean mKeyguardShowing = false;
private boolean mDeviceProvisioned = false;

/**
     * @param context everything needs a context :)
     * @param powerManager used to turn the screen off (the lock action).
*/
    public GlobalActions(Context context, LocalPowerManager powerManager) {
mContext = context;
        mPowerManager = powerManager;
mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

// receive broadcasts
IntentFilter filter = new IntentFilter();
filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
context.registerReceiver(mBroadcastReceiver, filter);
}

/**
//Synthetic comment -- @@ -123,29 +131,48 @@
}
};

mItems = Lists.newArrayList(
                /* Disabled pending bug 1304831 -- key or touch events wake up device before it
                 * can go to sleep.
                // first: lock screen
                new SinglePressAction(com.android.internal.R.drawable.ic_lock_lock, R.string.global_action_lock) {

                    public void onPress() {
                        mPowerManager.goToSleep(SystemClock.uptimeMillis() + 1);
                    }

                    public boolean showDuringKeyguard() {
                        return false;
                    }

                    public boolean showBeforeProvisioning() {
                        return false;
                    }
                },
                */
                // next: silent mode
mSilentModeToggle,
// last: power off
                new SinglePressAction(com.android.internal.R.drawable.ic_lock_power_off, R.string.global_action_power_off) {

public void onPress() {
// shutdown by making sure radio and power are handled accordingly.
//Synthetic comment -- @@ -180,10 +207,11 @@
}

private void prepareDialog() {
        // TODO: May need another 'Vibrate' toggle button, but for now treat them the same
final boolean silentModeOn =
mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL;
        mSilentModeToggle.updateState(silentModeOn);
mAdapter.notifyDataSetChanged();
if (mKeyguardShowing) {
mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//Synthetic comment -- @@ -192,6 +220,7 @@
}
}

/** {@inheritDoc} */
public void onDismiss(DialogInterface dialog) {
mStatusBar.disable(StatusBarManager.DISABLE_NONE);
//Synthetic comment -- @@ -229,6 +258,16 @@
return count;
}

public Action getItem(int position) {

int filteredPos = 0;
//Synthetic comment -- @@ -259,7 +298,7 @@

public View getView(int position, View convertView, ViewGroup parent) {
Action action = getItem(position);
            return action.create(mContext, (LinearLayout) convertView, LayoutInflater.from(mContext));
}
}

//Synthetic comment -- @@ -273,7 +312,7 @@
* What each item in the global actions dialog must be able to support.
*/
private interface Action {
        LinearLayout create(Context context, LinearLayout convertView, LayoutInflater inflater);

void onPress();

//Synthetic comment -- @@ -288,6 +327,8 @@
*   device is provisioned.
*/
boolean showBeforeProvisioning();
}

/**
//Synthetic comment -- @@ -303,12 +344,17 @@
mMessageResId = messageResId;
}

abstract public void onPress();

        public LinearLayout create(Context context, LinearLayout convertView, LayoutInflater inflater) {
            LinearLayout v = (LinearLayout) ((convertView != null) ?
convertView :
                    inflater.inflate(R.layout.global_actions_item, null));

ImageView icon = (ImageView) v.findViewById(R.id.icon);
TextView messageView = (TextView) v.findViewById(R.id.message);
//Synthetic comment -- @@ -326,9 +372,26 @@
* A toggle action knows whether it is on or off, and displays an icon
* and status message accordingly.
*/
    static abstract class ToggleAction implements Action {

        private boolean mOn = false;

// prefs
private final int mEnabledIconResId;
//Synthetic comment -- @@ -356,12 +419,12 @@
mDisabledStatusMessageResId = disabledStatusMessageResId;
}

        public LinearLayout create(Context context, LinearLayout convertView,
LayoutInflater inflater) {
            LinearLayout v = (LinearLayout) ((convertView != null) ?
convertView :
inflater.inflate(R
                            .layout.global_actions_item, null));

ImageView icon = (ImageView) v.findViewById(R.id.icon);
TextView messageView = (TextView) v.findViewById(R.id.message);
//Synthetic comment -- @@ -369,23 +432,50 @@

messageView.setText(mMessageResId);

icon.setImageDrawable(context.getResources().getDrawable(
                    (mOn ? mEnabledIconResId : mDisabledIconResid)));
            statusView.setText(mOn ? mEnabledStatusMessageResId : mDisabledStatusMessageResId);
statusView.setVisibility(View.VISIBLE);

return v;
}

        public void onPress() {
            updateState(!mOn);
            onToggle(mOn);
}

abstract void onToggle(boolean on);

        public void updateState(boolean on) {
            mOn = on;
}
}

//Synthetic comment -- @@ -401,6 +491,16 @@
}
};

private static final int MESSAGE_DISMISS = 0;
private Handler mHandler = new Handler() {
public void handleMessage(Message msg) {








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardScreen.java b/phone/com/android/internal/policy/impl/KeyguardScreen.java
//Synthetic comment -- index 739664b..bbb6875 100644

//Synthetic comment -- @@ -23,6 +23,12 @@
public interface KeyguardScreen {

/**
* This screen is no longer in front of the user.
*/
void onPause();
//Synthetic comment -- @@ -36,5 +42,4 @@
* This view is going away; a hook to do cleanup.
*/
void cleanUp();

}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
//Synthetic comment -- index 4671957..3a25d38 100644

//Synthetic comment -- @@ -375,12 +375,12 @@
}

/**
     * Is the keyboard currently open?
*/
boolean queryKeyboardOpen() {
final Configuration configuration = mContext.getResources().getConfiguration();

        return configuration.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO;
}

/**








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewBase.java b/phone/com/android/internal/policy/impl/KeyguardViewBase.java
//Synthetic comment -- index 5a3ebde..975c820 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
//Synthetic comment -- @@ -36,6 +37,7 @@

private KeyguardViewCallback mCallback;
private AudioManager mAudioManager;

public KeyguardViewBase(Context context) {
super(context);
//Synthetic comment -- @@ -131,8 +133,18 @@
final int keyCode = event.getKeyCode();
if (event.getAction() == KeyEvent.ACTION_DOWN) {
switch (keyCode) {
case KeyEvent.KEYCODE_HEADSETHOOK: 
                case KeyEvent.KEYCODE_PLAYPAUSE: 
case KeyEvent.KEYCODE_STOP: 
case KeyEvent.KEYCODE_NEXTSONG: 
case KeyEvent.KEYCODE_PREVIOUSSONG: 
//Synthetic comment -- @@ -167,6 +179,7 @@
}
} else if (event.getAction() == KeyEvent.ACTION_UP) {
switch (keyCode) {
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
case KeyEvent.KEYCODE_STOP: 








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewManager.java b/phone/com/android/internal/policy/impl/KeyguardViewManager.java
//Synthetic comment -- index f5dd3e5..297d62f 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.internal.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Canvas;
import android.util.Log;
//Synthetic comment -- @@ -34,7 +35,7 @@
* the wake lock and report that the keyguard is done, which is in turn,
* reported to this class by the current {@link KeyguardViewBase}.
*/
public class KeyguardViewManager {
private final static boolean DEBUG = false;
private static String TAG = "KeyguardViewManager";

//Synthetic comment -- @@ -45,6 +46,9 @@

private final KeyguardUpdateMonitor mUpdateMonitor;

private FrameLayout mKeyguardHost;
private KeyguardViewBase mKeyguardView;

//Synthetic comment -- @@ -96,20 +100,27 @@
mKeyguardHost = new KeyguardViewHost(mContext, mCallback);

final int stretch = ViewGroup.LayoutParams.FILL_PARENT;
            int flags = WindowManager.LayoutParams.FLAG_DITHER
                    | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
stretch, stretch, WindowManager.LayoutParams.TYPE_KEYGUARD,
flags, PixelFormat.OPAQUE);
lp.setTitle("Keyguard");

mViewManager.addView(mKeyguardHost, lp);
}

if (mKeyguardView == null) {
if (DEBUG) Log.d(TAG, "keyguard view is null, creating it...");
            mKeyguardView = mKeyguardViewProperties.createKeyguardView(mContext, mUpdateMonitor);
mKeyguardView.setId(R.id.lock_screen);
mKeyguardView.setCallback(mCallback);

//Synthetic comment -- @@ -128,6 +139,20 @@
mKeyguardView.requestFocus();
}

/**
* Reset the state of the view.
*/
//Synthetic comment -- @@ -183,7 +208,7 @@
public synchronized void hide() {
if (DEBUG) Log.d(TAG, "hide()");
if (mKeyguardHost != null) {
            mKeyguardHost.setVisibility(View.INVISIBLE);
if (mKeyguardView != null) {
mKeyguardHost.removeView(mKeyguardView);
mKeyguardView.cleanUp();








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewMediator.java b/phone/com/android/internal/policy/impl/KeyguardViewMediator.java
//Synthetic comment -- index 2431ffe..ead1348 100644

//Synthetic comment -- @@ -204,6 +204,11 @@
private boolean mKeyboardOpen = false;

/**
* {@link #setKeyguardEnabled} waits on this condition when it reenables
* the keyguard.
*/
//Synthetic comment -- @@ -248,6 +253,7 @@
context, WindowManagerImpl.getDefault(), this,
mKeyguardViewProperties, mUpdateMonitor);

}

/**
//Synthetic comment -- @@ -641,6 +647,7 @@
switch (keyCode) {
case KeyEvent.KEYCODE_VOLUME_UP:
case KeyEvent.KEYCODE_VOLUME_DOWN:
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
case KeyEvent.KEYCODE_STOP: 
//Synthetic comment -- @@ -771,6 +778,7 @@
handleHide();
mPM.userActivity(SystemClock.uptimeMillis(), true);
mWakeLock.release();
}

/**








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewProperties.java b/phone/com/android/internal/policy/impl/KeyguardViewProperties.java
//Synthetic comment -- index a449653..bda08eb 100644

//Synthetic comment -- @@ -29,9 +29,12 @@
* Create a keyguard view.
* @param context the context to use when creating the view.
* @param updateMonitor configuration may be based on this.
* @return the view.
*/
    KeyguardViewBase createKeyguardView(Context context, KeyguardUpdateMonitor updateMonitor);

/**
* Would the keyguard be secure right now?








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardWindowController.java b/phone/com/android/internal/policy/impl/KeyguardWindowController.java
new file mode 100644
//Synthetic comment -- index 0000000..4ad48fb

//Synthetic comment -- @@ -0,0 +1,29 @@








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 4ff87e3..cb3131d 100644

//Synthetic comment -- @@ -32,6 +32,12 @@
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import com.android.internal.R;
import com.android.internal.widget.LockPatternUtils;

//Synthetic comment -- @@ -53,6 +59,8 @@
private static final String TAG = "LockPatternKeyguardView";

private final KeyguardUpdateMonitor mUpdateMonitor;
private View mLockScreen;
private View mUnlockScreen;

//Synthetic comment -- @@ -147,7 +155,8 @@
public LockPatternKeyguardView(
Context context,
KeyguardUpdateMonitor updateMonitor,
            LockPatternUtils lockPatternUtils) {
super(context);

asyncCheckForAccount();
//Synthetic comment -- @@ -157,6 +166,7 @@

mUpdateMonitor = updateMonitor;
mLockPatternUtils = lockPatternUtils;

mMode = getInitialMode();

//Synthetic comment -- @@ -229,7 +239,8 @@
(LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET 
- LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)) {
showAlmostAtAccountLoginDialog();
                } else if (mHasAccount && failedAttempts >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET) {
mLockPatternUtils.setPermanentlyLocked(true);
updateScreen(mMode);
} else if ((failedAttempts % LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)
//Synthetic comment -- @@ -250,6 +261,11 @@
setFocusableInTouchMode(true);
setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

// create both the lock and unlock screen so they are quickly available
// when the screen turns on
mLockScreen = createLockScreen();
//Synthetic comment -- @@ -404,6 +420,10 @@
final View visibleScreen = (mode == Mode.LockScreen)
? mLockScreen : getUnlockScreenForCurrentUnlockMode();


if (mScreenOn) {
if (goneScreen.getVisibility() == View.VISIBLE) {
//Synthetic comment -- @@ -417,6 +437,7 @@
goneScreen.setVisibility(View.GONE);
visibleScreen.setVisibility(View.VISIBLE);

if (!visibleScreen.requestFocus()) {
throw new IllegalStateException("keyguard screen must be able to take "
+ "focus when shown " + visibleScreen.getClass().getCanonicalName());
//Synthetic comment -- @@ -561,4 +582,58 @@
WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
dialog.show();
}
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java
//Synthetic comment -- index 697c038..4e0cf09 100644

//Synthetic comment -- @@ -43,8 +43,10 @@
}

public KeyguardViewBase createKeyguardView(Context context,
            KeyguardUpdateMonitor updateMonitor) {
        return new LockPatternKeyguardView(context, updateMonitor, mLockPatternUtils);
}

public boolean isSecure() {








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index 0825c3b..82a52f9 100644

//Synthetic comment -- @@ -340,7 +340,6 @@
}

public void onOrientationChange(boolean inPortrait) {
	mCallback.pokeWakelock();
}

public void onKeyboardChange(boolean isKeyboardOpen) {
//Synthetic comment -- @@ -351,6 +350,11 @@


/** {@inheritDoc} */
public void onPause() {

}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindow.java b/phone/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index 80a2120..8d9a733 100644

//Synthetic comment -- @@ -1,5 +1,4 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -41,12 +40,14 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -67,6 +68,7 @@
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
//Synthetic comment -- @@ -141,46 +143,127 @@

private ContextMenuBuilder mContextMenu;
private MenuDialogHelper mContextMenuHelper;
    
private int mVolumeControlStreamType = AudioManager.USE_DEFAULT_STREAM_TYPE;
private long mVolumeKeyUpTime;

private KeyguardManager mKeyguardManager = null;

private boolean mSearchKeyDownReceived;
    
    private final Handler mKeycodeCallTimeoutHandler = new Handler() {
@Override
public void handleMessage(Message msg) {
            if (!mKeycodeCallTimeoutActive) return;
            mKeycodeCallTimeoutActive = false;
            // launch the VoiceDialer
            Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startCallActivity();
}
}
};
    private boolean mKeycodeCallTimeoutActive = false;

    // Ideally the call and camera buttons would share a common base class, and each implement their
    // own onShortPress() and onLongPress() methods, but to reduce the chance of regressions I'm
    // keeping them separate for now.
    private final Handler mKeycodeCameraTimeoutHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!mKeycodeCameraTimeoutActive) return;
            mKeycodeCameraTimeoutActive = false;
            // Broadcast an intent that the Camera button was longpressed
            Intent intent = new Intent(Intent.ACTION_CAMERA_BUTTON, null);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, (KeyEvent) msg.obj);
            getContext().sendOrderedBroadcast(intent, null);
        }
    };
    private boolean mKeycodeCameraTimeoutActive = false;

public PhoneWindow(Context context) {
super(context);
//Synthetic comment -- @@ -322,7 +405,7 @@
if (cb != null) {
st.createdPanelView = cb.onCreatePanelView(st.featureId);
}
        
if (st.createdPanelView == null) {
// Init the panel state's menu--return false if init failed
if (st.menu == null) {
//Synthetic comment -- @@ -333,16 +416,16 @@
if ((cb == null) || !cb.onCreatePanelMenu(st.featureId, st.menu)) {
// Ditch the menu created above
st.menu = null;
    
return false;
}
}
    
// Callback and return if the callback does not want to show the menu
if (!cb.onPreparePanel(st.featureId, st.createdPanelView, st.menu)) {
return false;
}
    
// Set the proper keymap
KeyCharacterMap kmap = KeyCharacterMap.load(event != null ? event.getDeviceId() : 0);
st.qwertyMode = kmap.getKeyboardType() != KeyCharacterMap.NUMERIC;
//Synthetic comment -- @@ -362,7 +445,7 @@
PanelFeatureState st = getPanelState(FEATURE_OPTIONS_PANEL, false);
if ((st != null) && (st.menu != null)) {
final MenuBuilder menuBuilder = (MenuBuilder) st.menu;
            
if (st.isOpen) {
// Freeze state
final Bundle state = new Bundle();
//Synthetic comment -- @@ -371,7 +454,7 @@
// Remove the menu views since they need to be recreated
// according to the new configuration
clearMenuViews(st);
                
// Re-open the same menu
reopenMenu(false);

//Synthetic comment -- @@ -384,23 +467,23 @@
clearMenuViews(st);
}
}
        
}

private static void clearMenuViews(PanelFeatureState st) {

// This can be called on config changes, so we should make sure
// the views will be reconstructed based on the new orientation, etc.
        
// Allow the callback to create a new panel view
st.createdPanelView = null;
        
        // Causes the decor view to be recreated 
st.refreshDecorView = true;
        
((MenuBuilder) st.menu).clearMenuViews();
}
    
@Override
public final void openPanel(int featureId, KeyEvent event) {
openPanel(getPanelState(featureId, true), event);
//Synthetic comment -- @@ -420,7 +503,7 @@
closePanel(st, true);
return;
}
        
final WindowManager wm = getWindowManager();
if (wm == null) {
return;
//Synthetic comment -- @@ -451,7 +534,7 @@
lp = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
}

            int backgroundResId; 
if (lp.width == ViewGroup.LayoutParams.FILL_PARENT) {
// If the contents is fill parent for the width, set the
// corresponding background
//Synthetic comment -- @@ -462,10 +545,10 @@
}
st.decorView.setWindowBackground(getContext().getResources().getDrawable(
backgroundResId));
            

st.decorView.addView(st.shownPanelView, lp);
            
/*
* Give focus to the view, if it or one of its children does not
* already have it.
//Synthetic comment -- @@ -530,7 +613,7 @@

// This view is no longer shown, so null it out
st.shownPanelView = null;
        
if (st.isInExpandedMode) {
// Next time the menu opens, it should not be in expanded mode, so
// force a refresh of the decor
//Synthetic comment -- @@ -566,6 +649,13 @@

PanelFeatureState st = getPanelState(featureId, true);
if (!st.isOpen) {
return preparePanel(st, event);
}

//Synthetic comment -- @@ -579,37 +669,40 @@
*/
public final void onKeyUpPanel(int featureId, KeyEvent event) {
// The panel key was released, so clear the chording key
        mPanelChordingKey = 0;

        boolean playSoundEffect = false;
        PanelFeatureState st = getPanelState(featureId, true);
        if (st.isOpen || st.isHandled) {
         
            // Play the sound effect if the user closed an open menu (and not if
            // they just released a menu shortcut)
            playSoundEffect = st.isOpen;

            // Close menu
            closePanel(st, true);
            
        } else if (st.isPrepared) {
            
            // Write 'menu opened' to event log
            EventLog.writeEvent(50001, 0);
            
            // Show menu
            openPanel(st, event);
            
            playSoundEffect = true;
        }

        if (playSoundEffect) {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(
                    Context.AUDIO_SERVICE);
            if (audioManager != null) {
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
            } else {
                Log.w(TAG, "Couldn't get audio manager");
}
}
}
//Synthetic comment -- @@ -650,7 +743,7 @@
*/
private synchronized void dismissContextMenu() {
mContextMenu = null;
        
if (mContextMenuHelper != null) {
mContextMenuHelper.dismiss();
mContextMenuHelper = null;
//Synthetic comment -- @@ -823,12 +916,12 @@
* @return Whether the initialization was successful.
*/
protected boolean initializePanelContent(PanelFeatureState st) {
        
if (st.createdPanelView != null) {
st.shownPanelView = st.createdPanelView;
return true;
}
        
final MenuBuilder menu = (MenuBuilder)st.menu;
if (menu == null) {
return false;
//Synthetic comment -- @@ -1136,12 +1229,24 @@
return true;
}

            case KeyEvent.KEYCODE_HEADSETHOOK: 
            case KeyEvent.KEYCODE_PLAYPAUSE: 
            case KeyEvent.KEYCODE_STOP: 
            case KeyEvent.KEYCODE_NEXTSONG: 
            case KeyEvent.KEYCODE_PREVIOUSSONG: 
            case KeyEvent.KEYCODE_REWIND: 
case KeyEvent.KEYCODE_FORWARD: {
Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
intent.putExtra(Intent.EXTRA_KEY_EVENT, event);
//Synthetic comment -- @@ -1155,10 +1260,10 @@
}
if (event.getRepeatCount() > 0) break;
mKeycodeCameraTimeoutActive = true;
                mKeycodeCameraTimeoutHandler.removeMessages(0);
                Message message = mKeycodeCameraTimeoutHandler.obtainMessage(0);
message.obj = event;
                mKeycodeCameraTimeoutHandler.sendMessageDelayed(message,
ViewConfiguration.getLongPressTimeout());
return true;
}
//Synthetic comment -- @@ -1191,16 +1296,29 @@
}
if (event.getRepeatCount() > 0) break;
mKeycodeCallTimeoutActive = true;
                mKeycodeCallTimeoutHandler.removeMessages(0);
                mKeycodeCallTimeoutHandler.sendMessageDelayed(
                        mKeycodeCallTimeoutHandler.obtainMessage(0),
ViewConfiguration.getLongPressTimeout());
return true;
}
            
case KeyEvent.KEYCODE_SEARCH: {
if (event.getRepeatCount() == 0) {
mSearchKeyDownReceived = true;
}
break;
}
//Synthetic comment -- @@ -1251,12 +1369,12 @@
return true;
}

            case KeyEvent.KEYCODE_HEADSETHOOK: 
            case KeyEvent.KEYCODE_PLAYPAUSE: 
            case KeyEvent.KEYCODE_STOP: 
            case KeyEvent.KEYCODE_NEXTSONG: 
            case KeyEvent.KEYCODE_PREVIOUSSONG: 
            case KeyEvent.KEYCODE_REWIND: 
case KeyEvent.KEYCODE_FORWARD: {
Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
intent.putExtra(Intent.EXTRA_KEY_EVENT, event);
//Synthetic comment -- @@ -1269,7 +1387,7 @@
break;
}
if (event.getRepeatCount() > 0) break; // Can a key up event repeat?
                mKeycodeCameraTimeoutHandler.removeMessages(0);
if (!mKeycodeCameraTimeoutActive) break;
mKeycodeCameraTimeoutActive = false;
// Add short press behavior here if desired
//Synthetic comment -- @@ -1281,7 +1399,7 @@
break;
}
if (event.getRepeatCount() > 0) break;
                mKeycodeCallTimeoutHandler.removeMessages(0);
if (!mKeycodeCallTimeoutActive) break;
mKeycodeCallTimeoutActive = false;
startCallActivity();
//Synthetic comment -- @@ -1295,6 +1413,7 @@
*/
if (getKeyguardManager().inKeyguardRestrictedInputMode() ||
!mSearchKeyDownReceived) {
break;
}
mSearchKeyDownReceived = false;
//Synthetic comment -- @@ -1307,6 +1426,7 @@
}

private void startCallActivity() {
Intent intent = new Intent(Intent.ACTION_CALL_BUTTON);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
getContext().startActivity(intent);
//Synthetic comment -- @@ -1434,7 +1554,7 @@
// The panel must not have been required, and is currently not around, skip it
continue;
}
            
st.onRestoreInstanceState(icicles.get(curFeatureId));
}

//Synthetic comment -- @@ -1518,7 +1638,7 @@
mVolumeControlStreamType, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
}
}
            
if (isDown && (event.getRepeatCount() == 0)) {
// First handle chording of panel key: if a panel key is held
// but not released, try to execute a shortcut in it.
//Synthetic comment -- @@ -1528,7 +1648,7 @@
// prepared panel or menu).
boolean handled = performPanelShortcut(mPreparedPanel, keyCode, event,
Menu.FLAG_PERFORM_NO_CLOSE);
                    
if (!handled) {
/*
* If not handled, then pass it to the view hierarchy
//Synthetic comment -- @@ -1564,7 +1684,7 @@
return isDown ? PhoneWindow.this.onKeyDown(mFeatureId, event.getKeyCode(), event)
: PhoneWindow.this.onKeyUp(mFeatureId, event.getKeyCode(), event);
}
        
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
final Callback cb = getCallback();
//Synthetic comment -- @@ -1731,7 +1851,7 @@
}
}

        
@Override
public boolean showContextMenuForChild(View originalView) {
// Reuse the context menu builder
//Synthetic comment -- @@ -1778,7 +1898,7 @@
drawableChanged();
}
}
        
@Override
protected boolean fitSystemWindows(Rect insets) {
mFrameOffsets.set(insets);
//Synthetic comment -- @@ -1852,11 +1972,12 @@
@Override
public void onWindowFocusChanged(boolean hasWindowFocus) {
super.onWindowFocusChanged(hasWindowFocus);
            
// no KEYCODE_CALL events active across focus changes
            mKeycodeCallTimeoutHandler.removeMessages(0);
mKeycodeCallTimeoutActive = false;
            mKeycodeCameraTimeoutHandler.removeMessages(0);
mKeycodeCameraTimeoutActive = false;

// If the user is chording a menu shortcut, release the chord since
//Synthetic comment -- @@ -1864,7 +1985,7 @@
if (!hasWindowFocus && mPanelChordingKey > 0) {
closePanel(FEATURE_OPTIONS_PANEL);
}
            
final Callback cb = getCallback();
if (cb != null && mFeatureId < 0) {
cb.onWindowFocusChanged(hasWindowFocus);
//Synthetic comment -- @@ -1931,7 +2052,7 @@
} else {
setFlags(FLAG_LAYOUT_IN_SCREEN|FLAG_LAYOUT_INSET_DECOR, flagsToUpdate);
}
        
if (a.getBoolean(com.android.internal.R.styleable.Window_windowNoTitle, false)) {
requestFeature(FEATURE_NO_TITLE);
}
//Synthetic comment -- @@ -1941,13 +2062,13 @@
}

WindowManager.LayoutParams params = getAttributes();
        
if (!hasSoftInputMode()) {
params.softInputMode = a.getInt(
com.android.internal.R.styleable.Window_windowSoftInputMode,
params.softInputMode);
}
        
if (a.getBoolean(com.android.internal.R.styleable.Window_backgroundDimEnabled,
mIsFloating)) {
/* All dialogs should have the window dimmed */
//Synthetic comment -- @@ -1958,10 +2079,11 @@
android.R.styleable.Window_backgroundDimAmount, 0.5f);
}

        params.windowAnimations = a.getResourceId(
                com.android.internal.R.styleable.Window_windowAnimationStyle,
                params.windowAnimations);
        
// The rest are only done if this window is not embedded; otherwise,
// the values are inherited from our container.
if (getContainer() == null) {
//Synthetic comment -- @@ -2135,7 +2257,7 @@

/**
* Gets a panel's state based on its feature ID.
     * 
* @param featureId The feature ID of the panel.
* @param required Whether the panel is required (if it is required and it
*            isn't in our features, this throws an exception).
//Synthetic comment -- @@ -2144,10 +2266,10 @@
private PanelFeatureState getPanelState(int featureId, boolean required) {
return getPanelState(featureId, required, null);
}
    
/**
* Gets a panel's state based on its feature ID.
     * 
* @param featureId The feature ID of the panel.
* @param required Whether the panel is required (if it is required and it
*            isn't in our features, this throws an exception).
//Synthetic comment -- @@ -2237,7 +2359,7 @@
}

private void updateInt(int featureId, int value, boolean fromResume) {
        
// Do nothing if the decor is not yet installed... an update will
// need to be forced when we eventually become active.
if (mContentParent == null) {
//Synthetic comment -- @@ -2308,7 +2430,7 @@
* callback. This method will grab whatever extra state is needed for the
* callback that isn't given in the parameters. If the panel is not open,
* this will not perform the callback.
     * 
* @param featureId Feature ID of the panel that was closed. Must be given.
* @param panel Panel that was closed. Optional but useful if there is no
*            menu given.
//Synthetic comment -- @@ -2344,7 +2466,7 @@
/**
* Helper method for adding launch-search to most applications. Opens the
* search window using default settings.
     * 
* @return true if search window opened
*/
private boolean launchDefaultSearch() {
//Synthetic comment -- @@ -2355,7 +2477,7 @@
return cb.onSearchRequested();
}
}
    
@Override
public void setVolumeControlStream(int streamType) {
mVolumeControlStreamType = streamType;
//Synthetic comment -- @@ -2391,7 +2513,7 @@
}

private static final class PanelFeatureState {
        
/** Feature ID for this panel. */
int featureId;

//Synthetic comment -- @@ -2415,11 +2537,11 @@

/** The panel that was returned by onCreatePanelView(). */
View createdPanelView;
        
/** The panel that we are actually showing. */
View shownPanelView;

        /** Use {@link #setMenu} to set this. */ 
Menu menu;

/**
//Synthetic comment -- @@ -2451,7 +2573,7 @@
* Contains the state of the menu when told to freeze.
*/
Bundle frozenMenuState;
        
PanelFeatureState(int featureId) {
this.featureId = featureId;

//Synthetic comment -- @@ -2471,7 +2593,7 @@

void setMenu(Menu menu) {
this.menu = menu;
            
if (frozenMenuState != null) {
((MenuBuilder) menu).restoreHierarchyState(frozenMenuState);
frozenMenuState = null;
//Synthetic comment -- @@ -2488,7 +2610,7 @@
savedState.menuState = new Bundle();
((MenuBuilder) menu).saveHierarchyState(savedState.menuState);
}
            
return savedState;
}

//Synthetic comment -- @@ -2511,13 +2633,13 @@
shownPanelView = null;
decorView = null;
}
        
private static class SavedState implements Parcelable {
int featureId;
boolean isOpen;
boolean isInExpandedMode;
Bundle menuState;
            
public int describeContents() {
return 0;
}
//Synthetic comment -- @@ -2531,20 +2653,20 @@
dest.writeBundle(menuState);
}
}
            
private static SavedState readFromParcel(Parcel source) {
SavedState savedState = new SavedState();
savedState.featureId = source.readInt();
savedState.isOpen = source.readInt() == 1;
savedState.isInExpandedMode = source.readInt() == 1;
                
if (savedState.isOpen) {
savedState.menuState = source.readBundle();
}
                
return savedState;
}
            
public static final Parcelable.Creator<SavedState> CREATOR
= new Parcelable.Creator<SavedState>() {
public SavedState createFromParcel(Parcel in) {
//Synthetic comment -- @@ -2556,7 +2678,7 @@
}
};
}
        
}

/**
//Synthetic comment -- @@ -2568,7 +2690,7 @@
private final class ContextMenuCallback implements MenuBuilder.Callback {
private int mFeatureId;
private MenuDialogHelper mSubMenuHelper;
        
public ContextMenuCallback(int featureId) {
mFeatureId = featureId;
}
//Synthetic comment -- @@ -2577,11 +2699,11 @@
if (allMenusAreClosing) {
Callback callback = getCallback();
if (callback != null) callback.onPanelClosed(mFeatureId, menu);
                
if (menu == mContextMenu) {
dismissContextMenu();
}
                
// Dismiss the submenu, if it is showing
if (mSubMenuHelper != null) {
mSubMenuHelper.dismiss();
//Synthetic comment -- @@ -2610,8 +2732,16 @@
// The window manager will give us a valid window token
mSubMenuHelper = new MenuDialogHelper(subMenu);
mSubMenuHelper.show(null);
            
return true;
}
}
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 346470f..3e2b927 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;

import com.android.internal.policy.PolicyManager;
//Synthetic comment -- @@ -47,10 +48,11 @@
import android.util.EventLog;
import android.util.Log;
import android.view.Gravity;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationListener;
import android.view.RawInputEvent;
import android.view.Surface;
import android.view.View;
//Synthetic comment -- @@ -88,11 +90,12 @@
import android.media.IAudioService;
import android.media.AudioManager;

import java.util.Observable;
import java.util.Observer;

/**
 * WindowManagerPolicy implementation for the Android phone UI.
*/
public class PhoneWindowManager implements WindowManagerPolicy {
static final String TAG = "WindowManager";
//Synthetic comment -- @@ -138,13 +141,20 @@
static public final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
static public final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

Context mContext;
IWindowManager mWindowManager;
LocalPowerManager mPowerManager;

/** If true, hitting shift & menu will broadcast Intent.ACTION_BUG_REPORT */
boolean mEnableShiftMenuBugReports = false;

WindowState mStatusBar = null;
WindowState mSearchBar = null;
WindowState mKeyguard = null;
//Synthetic comment -- @@ -229,58 +239,51 @@

public void update() {
ContentResolver resolver = mContext.getContentResolver();
            mEndcallBehavior = Settings.System.getInt(resolver,
                    Settings.System.END_BUTTON_BEHAVIOR, DEFAULT_ENDCALL_BEHAVIOR);
            int accelerometerDefault = Settings.System.getInt(resolver,
                    Settings.System.ACCELEROMETER_ROTATION, DEFAULT_ACCELEROMETER_ROTATION);
            if (mAccelerometerDefault != accelerometerDefault) {
                mAccelerometerDefault = accelerometerDefault;
                updateOrientationListener();
            }
            String imId = Settings.Secure.getString(resolver,
                    Settings.Secure.DEFAULT_INPUT_METHOD);
            boolean hasSoftInput = imId != null && imId.length() > 0;
            if (mHasSoftInput != hasSoftInput) {
                mHasSoftInput = hasSoftInput;
                updateRotation();
}
}
}

    class MyOrientationListener extends OrientationListener {

MyOrientationListener(Context context) {
super(context);
}

@Override
        public void onOrientationChanged(int orientation) {
            // ignore orientation changes unless the value is in a range that
            // matches portrait or landscape
            // portrait range is 270+45 to 359 and 0 to 45
            // landscape range is 270-45 to 270+45
            if ((orientation >= 0 && orientation <= 45) || (orientation >= 270 - 45)) {
                int rotation =  (orientation >= 270 - 45
                        && orientation <= 270 + 45)
                        ? Surface.ROTATION_90 : Surface.ROTATION_0;
                if (rotation != mSensorRotation) {
                	if(localLOGV) Log.i(TAG, "onOrientationChanged, rotation changed from "+rotation+" to "+mSensorRotation);
                    // Update window manager.  The lid rotation hasn't changed,
                    // but we want it to re-evaluate the final rotation in case
                    // it needs to call back and get the sensor orientation.
                    mSensorRotation = rotation;
                    try {
                        mWindowManager.setRotation(USE_LAST_ROTATION, false);
                    } catch (RemoteException e) {
                        // Ignore
                    }
}
}
}                                      
}
MyOrientationListener mOrientationListener;

    boolean useSensorForOrientation() {
if(mCurrentAppOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
return true;
}
//Synthetic comment -- @@ -292,6 +295,26 @@
return false;
}

/*
* Various use cases for invoking this function
* screen turning off, should always disable listeners if already enabled
//Synthetic comment -- @@ -302,28 +325,36 @@
* screen turning on and current app has sensor based orientation, enable listeners if needed
* screen turning on and current app has nosensor based orientation, do nothing
*/
    void updateOrientationListener() {
//Could have been invoked due to screen turning on or off or
//change of the currently visible window's orientation
        if(localLOGV) Log.i(TAG, "Screen status="+mScreenOn+
", current orientation="+mCurrentAppOrientation+
", SensorEnabled="+mOrientationSensorEnabled);
boolean disable = true;
        if(mScreenOn) {
            if(useSensorForOrientation()) {
disable = false;
//enable listener if not already enabled
                if(!mOrientationSensorEnabled) {
mOrientationListener.enable();
if(localLOGV) Log.i(TAG, "Enabling listeners");
mOrientationSensorEnabled = true;
}
} 
} 
//check if sensors need to be disabled
        if(disable && mOrientationSensorEnabled) {
mOrientationListener.disable();
if(localLOGV) Log.i(TAG, "Disabling listeners");
mOrientationSensorEnabled = false;
}
}
//Synthetic comment -- @@ -331,6 +362,7 @@
Runnable mEndCallLongPress = new Runnable() {
public void run() {
mShouldTurnOffOnKeyUp = false;
sendCloseSystemWindows(SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS);
showGlobalActionsDialog();
}
//Synthetic comment -- @@ -338,7 +370,7 @@

void showGlobalActionsDialog() {
if (mGlobalActions == null) {
            mGlobalActions = new GlobalActions(mContext, mPowerManager);
}
final boolean keyguardShowing = mKeyguardMediator.isShowing();
mGlobalActions.showDialog(keyguardShowing, isDeviceProvisioned());
//Synthetic comment -- @@ -364,6 +396,7 @@
* the user lets go of the home key
*/
mHomePressed = false;
sendCloseSystemWindows(SYSTEM_DIALOG_REASON_RECENT_APPS);
showRecentAppsDialog();
}
//Synthetic comment -- @@ -471,16 +504,6 @@
config.hardKeyboardHidden = lidOpen
? Configuration.KEYBOARDHIDDEN_NO
: Configuration.KEYBOARDHIDDEN_YES;
        if (keyguardIsShowingTq()) {
            if (mLidOpen) {
                // only do this if it's opening -- closing the device shouldn't turn it
                // off, but it also shouldn't turn it on.
                mKeyguardMediator.pokeWakelock();
            }
        } else {
            mPowerManager.userActivity(SystemClock.uptimeMillis(), false,
                    LocalPowerManager.OTHER_EVENT);
        }
}

public boolean isCheekPressedAgainstScreen(MotionEvent ev) {
//Synthetic comment -- @@ -589,11 +612,17 @@

win.setType(
WindowManager.LayoutParams.TYPE_APPLICATION_STARTING);
win.setFlags(
WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

win.setLayout(WindowManager.LayoutParams.FILL_PARENT,
WindowManager.LayoutParams.FILL_PARENT);
//Synthetic comment -- @@ -732,6 +761,11 @@
return keyguardIsShowingTq() || inKeyguardRestrictedKeyInputMode();
}

/** {@inheritDoc} */
public boolean interceptKeyTi(WindowState win, int code, int metaKeys, boolean down, 
int repeatCount) {
//Synthetic comment -- @@ -739,7 +773,7 @@

if (false) {
Log.d(TAG, "interceptKeyTi code=" + code + " down=" + down + " repeatCount="
                    + repeatCount + " keyguardOn=" + keyguardOn);
}

// Clear a pending HOME longpress if the user releases Home
//Synthetic comment -- @@ -795,15 +829,19 @@
// right now to interact with applications.
WindowManager.LayoutParams attrs = win != null ? win.getAttrs() : null;
if (attrs != null) {
                int type = attrs.type;
                if (type >= WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW
                        && type <= WindowManager.LayoutParams.LAST_SYSTEM_WINDOW) {
                    // Only do this once, so home-key-longpress doesn't close itself
                    if (repeatCount == 0 && down) {
                		sendCloseSystemWindows();
                    }
return false;
}
}

if (down && repeatCount == 0) {
//Synthetic comment -- @@ -950,7 +988,7 @@
pf.bottom = df.bottom = vf.bottom = displayHeight;

mStatusBar.computeFrameLw(pf, df, vf, vf);
            if (mStatusBar.isDisplayedLw()) {
// If the status bar is hidden, we don't want to cause
// windows behind it to scroll.
mDockTop = mContentTop = mCurTop = mStatusBar.getFrameLw().bottom;
//Synthetic comment -- @@ -1154,35 +1192,57 @@
/** {@inheritDoc} */
public void animatingWindowLw(WindowState win,
WindowManager.LayoutParams attrs) {
        if (mTopFullscreenOpaqueWindowState == null
            && attrs.type >= FIRST_APPLICATION_WINDOW
            && attrs.type <= LAST_APPLICATION_WINDOW
            && win.fillsScreenLw(mW, mH, true)
            && win.isVisibleLw()) {
            mTopFullscreenOpaqueWindowState = win;
        } else if ((attrs.flags & FLAG_FORCE_NOT_FULLSCREEN) != 0) {
            mForceStatusBar = true;
}
}

/** {@inheritDoc} */
public boolean finishAnimationLw() {
if (mStatusBar != null) {
if (mForceStatusBar) {
                mStatusBar.showLw(true);
} else if (mTopFullscreenOpaqueWindowState != null) {
               WindowManager.LayoutParams lp =
                   mTopFullscreenOpaqueWindowState.getAttrs();
               boolean hideStatusBar =
                   (lp.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
               if (hideStatusBar) {
                   mStatusBar.hideLw(true);
               } else {
                   mStatusBar.showLw(true);
               }
           }
}
       return false;
}

/** {@inheritDoc} */
//Synthetic comment -- @@ -1193,6 +1253,22 @@
// lid changed state
mLidOpen = event.value == 0;
updateRotation();
}
}
return false;
//Synthetic comment -- @@ -1299,6 +1375,11 @@
final boolean isWakeKey = isWakeKeyTq(event);
final boolean keyguardShowing = keyguardIsShowingTq();

if (keyguardShowing) {
if (screenIsOn) {
// when the screen is on, always give the event to the keyguard
//Synthetic comment -- @@ -1402,6 +1483,65 @@
mBroadcastWakeLock.acquire();
mHandler.post(new PassHeadsetKey(keyEvent));
}
}
}

//Synthetic comment -- @@ -1416,10 +1556,12 @@
}

public void run() {
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, mKeyEvent);
            mContext.sendOrderedBroadcast(intent, null, mBroadcastDone,
                    mHandler, Activity.RESULT_OK, null, null);
}
}

//Synthetic comment -- @@ -1460,18 +1602,22 @@

/** {@inheritDoc} */
public void screenTurnedOff(int why) {
        EventLog.writeEvent(70000, 0);
        mKeyguardMediator.onScreenTurnedOff(why);
        mScreenOn = false;
        updateOrientationListener();
}

/** {@inheritDoc} */
public void screenTurnedOn() {
        EventLog.writeEvent(70000, 1);
        mKeyguardMediator.onScreenTurnedOn();
        mScreenOn = true;
        updateOrientationListener();
}

/** {@inheritDoc} */
//Synthetic comment -- @@ -1501,47 +1647,66 @@
sendCloseSystemWindows();
}

    private void sendCloseSystemWindows() {
        sendCloseSystemWindows(null);
}

    private void sendCloseSystemWindows(String reason) {
        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        if (reason != null) {
            intent.putExtra(SYSTEM_DIALOG_REASON_KEY, reason);
        }
        mContext.sendBroadcast(intent);
}

    public int rotationForOrientation(int orientation) {
        switch (orientation) {
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                //always return landscape if orientation set to landscape
                return Surface.ROTATION_90;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                //always return portrait if orientation set to portrait
                return Surface.ROTATION_0;
        }
        // case for nosensor meaning ignore sensor and consider only lid
        // or orientation sensor disabled
        //or case.unspecified
        if (mLidOpen) {
            return Surface.ROTATION_90;
        } else {
            if (useSensorForOrientation()) {
                // If the user has enabled auto rotation by default, do it.
                return mSensorRotation >= 0 ? mSensorRotation : Surface.ROTATION_0;
}
            return Surface.ROTATION_0;
}
}

/** {@inheritDoc} */
public void systemReady() {
try {
            int menuState = mWindowManager.getKeycodeState(KeyEvent.KEYCODE_MENU);
            Log.i(TAG, "Menu key state: " + menuState);
            if (menuState > 0) {
// If the user is holding the menu key code, then we are
// going to boot into safe mode.
ActivityManagerNative.getDefault().enterSafeMode();
//Synthetic comment -- @@ -1549,12 +1714,16 @@
// tell the keyguard
mKeyguardMediator.onSystemReady();
android.os.SystemProperties.set("dev.bootcomplete", "1"); 
            updateOrientationListener();
} catch (RemoteException e) {
// Ignore
}
}
    
/** {@inheritDoc} */
public void enableScreenAfterBoot() {
readLidState();
//Synthetic comment -- @@ -1575,22 +1744,6 @@
} catch (RemoteException e) {
// Ignore
}
        if (keyguardIsShowingTq()) {
            if (mLidOpen) {
                // only do this if it's opening -- closing the device shouldn't turn it
                // off, but it also shouldn't turn it on.
                mKeyguardMediator.pokeWakelock();
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

/**
//Synthetic comment -- @@ -1620,10 +1773,32 @@
return true;
}

    public void setCurrentOrientation(int newOrientation) {
        if(newOrientation != mCurrentAppOrientation) {
            mCurrentAppOrientation = newOrientation;
            updateOrientationListener();
}
}
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/SimUnlockScreen.java b/phone/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index fceec5f..0f7fe32 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
private final boolean mCreatedWithKeyboardOpen;

private TextView mHeaderText;
    private EditText mPinText;

private TextView mOkButton;
private TextView mEmergencyCallButton;
//Synthetic comment -- @@ -75,7 +75,7 @@
}

mHeaderText = (TextView) findViewById(R.id.headerText);
        mPinText = (EditText) findViewById(R.id.pinDisplay);
mBackSpaceButton = findViewById(R.id.backspace);
mBackSpaceButton.setOnClickListener(this);

//Synthetic comment -- @@ -93,6 +93,11 @@
}

/** {@inheritDoc} */
public void onPause() {

}
//Synthetic comment -- @@ -154,7 +159,7 @@

public void onClick(View v) {
if (v == mBackSpaceButton) {
            final Editable digits = mPinText.getText();
final int len = digits.length();
if (len > 0) {
digits.delete(len-1, len);








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/UnlockScreen.java b/phone/com/android/internal/policy/impl/UnlockScreen.java
//Synthetic comment -- index 65ab439..9aedf90 100644

//Synthetic comment -- @@ -16,14 +16,10 @@

package com.android.internal.policy.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
//Synthetic comment -- @@ -230,6 +226,11 @@
public void onKeyboardChange(boolean isKeyboardOpen) {}

/** {@inheritDoc} */
public void onPause() {
if (mCountdownTimer != null) {
mCountdownTimer.cancel();
//Synthetic comment -- @@ -288,7 +289,7 @@
mLockPatternView
.setDisplayMode(LockPatternView.DisplayMode.Correct);
mUnlockIcon.setVisibility(View.GONE);
                mUnlockHeader.setText(R.string.lockscreen_pattern_correct);
mCallback.keyguardDone(true);
} else {
mCallback.pokeWakelock(UNLOCK_PATTERN_WAKE_INTERVAL_MS);
//Synthetic comment -- @@ -299,8 +300,7 @@
mCallback.reportFailedPatternAttempt();
}
if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                    long deadline = SystemClock.elapsedRealtime() + LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS;
                    mLockPatternUtils.setLockoutAttemptDeadline(deadline);
handleAttemptLockout(deadline);
return;
}







