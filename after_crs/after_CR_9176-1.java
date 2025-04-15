/*Merge commit 'remotes/korg/cupcake' into merge*/




//Synthetic comment -- diff --git a/mid/com/android/internal/policy/impl/MidWindow.java b/mid/com/android/internal/policy/impl/MidWindow.java
//Synthetic comment -- index 17293b5..536ee65 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.EventLog;
//Synthetic comment -- @@ -147,6 +148,8 @@
private long mVolumeKeyUpTime;

private KeyguardManager mKeyguardManager = null;

    private TelephonyManager mTelephonyManager = null;

private boolean mSearchKeyDownReceived;

//Synthetic comment -- @@ -1117,8 +1120,19 @@
return true;
}

            case KeyEvent.KEYCODE_PLAYPAUSE:
                /* Suppress PLAYPAUSE toggle if Phone is ringing or in-call,
                 * to avoid music playback */
                if (mTelephonyManager == null) {
                    mTelephonyManager = (TelephonyManager) getContext().getSystemService(
                            Context.TELEPHONY_SERVICE);
                }
                if (mTelephonyManager != null &&
                        mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
                    return true;  // suppress key event
                }
            case KeyEvent.KEYCODE_MUTE: 
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_STOP: 
case KeyEvent.KEYCODE_NEXTSONG: 
case KeyEvent.KEYCODE_PREVIOUSSONG: 








//Synthetic comment -- diff --git a/mid/com/android/internal/policy/impl/MidWindowManager.java b/mid/com/android/internal/policy/impl/MidWindowManager.java
//Synthetic comment -- index b9ed39a..8d7f28a 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import android.util.Config;
import android.util.EventLog;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
//Synthetic comment -- @@ -80,6 +81,7 @@
import static android.view.WindowManager.LayoutParams.TYPE_TOAST;
import android.view.WindowManagerImpl;
import android.view.WindowManagerPolicy;
import android.view.WindowManagerPolicy.WindowState;
import android.media.IAudioService;
import android.media.AudioManager;

//Synthetic comment -- @@ -131,6 +133,7 @@
private IWindowManager mWindowManager;
private LocalPowerManager mPowerManager;

    boolean mSafeMode;
private WindowState mStatusBar = null;
private WindowState mSearchBar = null;
private WindowState mKeyguard = null;
//Synthetic comment -- @@ -766,7 +769,7 @@
if (mTopFullscreenOpaqueWindowState == null
&& attrs.type >= FIRST_APPLICATION_WINDOW
&& attrs.type <= LAST_APPLICATION_WINDOW
            && win.fillsScreenLw(mW, mH, true, false)
&& win.isDisplayedLw()) {
mTopFullscreenOpaqueWindowState = win;
} else if ((attrs.flags & FLAG_FORCE_NOT_FULLSCREEN) != 0) {
//Synthetic comment -- @@ -968,17 +971,28 @@
mContext.sendBroadcast(intent);
}

    public int rotationForOrientationLw(int orientation, int lastRotation,
            boolean displayEnabled) {
// get rid of rotation for now. Always rotation of 0
return Surface.ROTATION_0;
}

    public boolean detectSafeMode() {
        try {
            int menuState = mWindowManager.getKeycodeState(KeyEvent.KEYCODE_MENU);
            mSafeMode = menuState > 0;
            Log.i(TAG, "Menu key state: " + menuState + " safeMode=" + mSafeMode);
            return mSafeMode;
        } catch (RemoteException e) {
            // Doom! (it's also local)
            throw new RuntimeException("window manager dead");
        }
    }
    
/** {@inheritDoc} */
public void systemReady() {
try {
            if (mSafeMode) {
// If the user is holding the menu key code, then we are
// going to boot into safe mode.
ActivityManagerNative.getDefault().enterSafeMode();
//Synthetic comment -- @@ -1035,9 +1049,16 @@
return true;
}

    public void setCurrentOrientationLw(int newOrientation) {
if(newOrientation != mCurrentAppOrientation) {
mCurrentAppOrientation = newOrientation;
}
}
    
    public boolean performHapticFeedbackLw(WindowState win, int effectId, boolean always) {
        return false;
    }
    
    public void screenOnStoppedLw() {
    }
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/AccountUnlockScreen.java b/phone/com/android/internal/policy/impl/AccountUnlockScreen.java
//Synthetic comment -- index 98cb548..65102c6 100644

//Synthetic comment -- @@ -28,8 +28,11 @@
import android.graphics.Rect;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -47,13 +50,16 @@
* IAccountsService.
*/
public class AccountUnlockScreen extends RelativeLayout implements KeyguardScreen,
        View.OnClickListener, ServiceConnection, TextWatcher {
private static final String LOCK_PATTERN_PACKAGE = "com.android.settings";
private static final String LOCK_PATTERN_CLASS =
"com.android.settings.ChooseLockPattern";

    /**
     * The amount of millis to stay awake once this screen detects activity
     */
    private static final int AWAKE_POKE_MILLIS = 30000;

private final KeyguardScreenCallback mCallback;
private final LockPatternUtils mLockPatternUtils;
private IAccountsService mAccountsService;
//Synthetic comment -- @@ -87,8 +93,10 @@

mLogin = (EditText) findViewById(R.id.login);
mLogin.setFilters(new InputFilter[] { new LoginFilter.UsernameFilterGeneric() } );
        mLogin.addTextChangedListener(this);

mPassword = (EditText) findViewById(R.id.password);
        mPassword.addTextChangedListener(this);

mOk = (Button) findViewById(R.id.ok);
mOk.setOnClickListener(this);
//Synthetic comment -- @@ -105,6 +113,16 @@
}
}

    public void afterTextChanged(Editable s) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCallback.pokeWakelock(AWAKE_POKE_MILLIS);
    }

@Override
protected boolean onRequestFocusInDescendants(int direction,
Rect previouslyFocusedRect) {
//Synthetic comment -- @@ -113,6 +131,11 @@
}

/** {@inheritDoc} */
    public boolean needsInput() {
        return true;
    }

    /** {@inheritDoc} */
public void onPause() {

}
//Synthetic comment -- @@ -132,6 +155,7 @@

/** {@inheritDoc} */
public void onClick(View v) {
        mCallback.pokeWakelock();
if (v == mOk) {
if (checkPassword()) {
// clear out forgotten password
//Synthetic comment -- @@ -167,11 +191,75 @@
return super.dispatchKeyEvent(event);
}

    /**
     * Given the string the user entered in the 'username' field, find
     * the stored account that they probably intended.  Prefer, in order:
     *
     *   - an exact match for what was typed, or
     *   - a case-insensitive match for what was typed, or
     *   - if they didn't include a domain, an exact match of the username, or
     *   - if they didn't include a domain, a case-insensitive
     *     match of the username.
     *
     * If there is a tie for the best match, choose neither --
     * the user needs to be more specific.
     *
     * @return an account name from the database, or null if we can't
     * find a single best match.
     */
    private String findIntendedAccount(String username) {
        String[] accounts = null;
        try {
            accounts = mAccountsService.getAccounts();
        } catch (RemoteException e) {
            return null;
        }
        if (accounts == null) {
            return null;
        }

        // Try to figure out which account they meant if they
        // typed only the username (and not the domain), or got
        // the case wrong.

        String bestAccount = null;
        int bestScore = 0;
        for (String a: accounts) {
            int score = 0;
            if (username.equals(a)) {
                score = 4;
            } else if (username.equalsIgnoreCase(a)) {
                score = 3;
            } else if (username.indexOf('@') < 0) {
                int i = a.indexOf('@');
                if (i >= 0) {
                    String aUsername = a.substring(0, i);
                    if (username.equals(aUsername)) {
                        score = 2;
                    } else if (username.equalsIgnoreCase(aUsername)) {
                        score = 1;
                    }
                }
            }
            if (score > bestScore) {
                bestAccount = a;
                bestScore = score;
            } else if (score == bestScore) {
                bestAccount = null;
            }
        }
        return bestAccount;
    }

private boolean checkPassword() {
final String login = mLogin.getText().toString();
final String password = mPassword.getText().toString();
try {
            String account = findIntendedAccount(login);
            if (account == null) {
                return false;
            }
            return mAccountsService.shouldUnlock(account, password);
} catch (RemoteException e) {
return false;
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/GlobalActions.java b/phone/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index 7f8e57b..eeb875a 100644

//Synthetic comment -- @@ -16,29 +16,30 @@

package com.android.internal.policy.impl;

import android.app.AlertDialog;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
import com.google.android.collect.Lists;

import java.util.ArrayList;

//Synthetic comment -- @@ -49,34 +50,41 @@
*/
class GlobalActions implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener  {

    private static final String TAG = "GlobalActions";

private StatusBarManager mStatusBar;

private final Context mContext;
private final AudioManager mAudioManager;

private ArrayList<Action> mItems;
private AlertDialog mDialog;

private ToggleAction mSilentModeToggle;
    private ToggleAction mAirplaneModeOn;

private MyAdapter mAdapter;

private boolean mKeyguardShowing = false;
private boolean mDeviceProvisioned = false;
    private ToggleAction.State mAirplaneState = ToggleAction.State.Off;

/**
     * @param context everything needs a context :(
*/
    public GlobalActions(Context context) {
mContext = context;
mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

// receive broadcasts
IntentFilter filter = new IntentFilter();
filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
context.registerReceiver(mBroadcastReceiver, filter);

        // get notified of phone state changes
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);
}

/**
//Synthetic comment -- @@ -123,29 +131,48 @@
}
};

        mAirplaneModeOn = new ToggleAction(
                R.drawable.ic_lock_airplane_mode,
                R.drawable.ic_lock_airplane_mode_off,
                R.string.global_actions_toggle_airplane_mode,
                R.string.global_actions_airplane_mode_on_status,
                R.string.global_actions_airplane_mode_off_status) {

            void onToggle(boolean on) {
                // Change the system setting
                Settings.System.putInt(
                        mContext.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON,
                        on ? 1 : 0);
                Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                intent.putExtra("state", on);
                mContext.sendBroadcast(intent);
            }

            @Override
            protected void changeStateFromPress(boolean buttonOn) {
                mState = buttonOn ? State.TurningOn : State.TurningOff;
                mAirplaneState = mState;
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return false;
            }
        };

mItems = Lists.newArrayList(
                // silent mode
mSilentModeToggle,
                // next: airplane mode
                mAirplaneModeOn,
// last: power off
                new SinglePressAction(
                        com.android.internal.R.drawable.ic_lock_power_off,
                        R.string.global_action_power_off) {

public void onPress() {
// shutdown by making sure radio and power are handled accordingly.
//Synthetic comment -- @@ -180,10 +207,11 @@
}

private void prepareDialog() {
final boolean silentModeOn =
mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL;
        mSilentModeToggle.updateState(
                silentModeOn ? ToggleAction.State.On : ToggleAction.State.Off);
        mAirplaneModeOn.updateState(mAirplaneState);
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

        @Override
        public boolean isEnabled(int position) {
            return getItem(position).isEnabled();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

public Action getItem(int position) {

int filteredPos = 0;
//Synthetic comment -- @@ -259,7 +298,7 @@

public View getView(int position, View convertView, ViewGroup parent) {
Action action = getItem(position);
            return action.create(mContext, convertView, parent, LayoutInflater.from(mContext));
}
}

//Synthetic comment -- @@ -273,7 +312,7 @@
* What each item in the global actions dialog must be able to support.
*/
private interface Action {
        View create(Context context, View convertView, ViewGroup parent, LayoutInflater inflater);

void onPress();

//Synthetic comment -- @@ -288,6 +327,8 @@
*   device is provisioned.
*/
boolean showBeforeProvisioning();

        boolean isEnabled();
}

/**
//Synthetic comment -- @@ -303,12 +344,17 @@
mMessageResId = messageResId;
}

        public boolean isEnabled() {
            return true;
        }

abstract public void onPress();

        public View create(
                Context context, View convertView, ViewGroup parent, LayoutInflater inflater) {
            View v = (convertView != null) ?
convertView :
                    inflater.inflate(R.layout.global_actions_item, parent, false);

ImageView icon = (ImageView) v.findViewById(R.id.icon);
TextView messageView = (TextView) v.findViewById(R.id.message);
//Synthetic comment -- @@ -326,9 +372,26 @@
* A toggle action knows whether it is on or off, and displays an icon
* and status message accordingly.
*/
    private static abstract class ToggleAction implements Action {

        enum State {
            Off(false),
            TurningOn(true),
            TurningOff(true),
            On(false);

            private final boolean inTransition;

            State(boolean intermediate) {
                inTransition = intermediate;
            }

            public boolean inTransition() {
                return inTransition;
            }
        }

        protected State mState = State.Off;

// prefs
private final int mEnabledIconResId;
//Synthetic comment -- @@ -356,12 +419,12 @@
mDisabledStatusMessageResId = disabledStatusMessageResId;
}

        public View create(Context context, View convertView, ViewGroup parent,
LayoutInflater inflater) {
            View v = (convertView != null) ?
convertView :
inflater.inflate(R
                            .layout.global_actions_item, parent, false);

ImageView icon = (ImageView) v.findViewById(R.id.icon);
TextView messageView = (TextView) v.findViewById(R.id.message);
//Synthetic comment -- @@ -369,23 +432,50 @@

messageView.setText(mMessageResId);

            boolean on = ((mState == State.On) || (mState == State.TurningOn));
icon.setImageDrawable(context.getResources().getDrawable(
                    (on ? mEnabledIconResId : mDisabledIconResid)));
            statusView.setText(on ? mEnabledStatusMessageResId : mDisabledStatusMessageResId);
statusView.setVisibility(View.VISIBLE);

            final boolean enabled = isEnabled();
            messageView.setEnabled(enabled);
            statusView.setEnabled(enabled);
            icon.setEnabled(enabled);
            v.setEnabled(enabled);

return v;
}

        public final void onPress() {
            if (mState.inTransition()) {
                Log.w(TAG, "shouldn't be able to toggle when in transition");
                return;
            }

            final boolean nowOn = !(mState == State.On);
            onToggle(nowOn);
            changeStateFromPress(nowOn);
        }

        public boolean isEnabled() {
            return !mState.inTransition();
        }

        /**
         * Implementations may override this if their state can be in on of the intermediate
         * states until some notification is received (e.g airplane mode is 'turning off' until
         * we know the wireless connections are back online
         * @param buttonOn Whether the button was turned on or off
         */
        protected void changeStateFromPress(boolean buttonOn) {
            mState = buttonOn ? State.On : State.Off;
}

abstract void onToggle(boolean on);

        public void updateState(State state) {
            mState = state;
}
}

//Synthetic comment -- @@ -401,6 +491,16 @@
}
};

    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            final boolean inAirplaneMode = serviceState.getState() == ServiceState.STATE_POWER_OFF;
            mAirplaneState = inAirplaneMode ? ToggleAction.State.On : ToggleAction.State.Off;
            mAirplaneModeOn.updateState(mAirplaneState);
            mAdapter.notifyDataSetChanged();
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
     * Return true if your view needs input, so should allow the soft
     * keyboard to be displayed.
     */
    boolean needsInput();
    
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
     * Is the (hard) keyboard currently open?
*/
boolean queryKeyboardOpen() {
final Configuration configuration = mContext.getResources().getConfiguration();

        return configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
}

/**








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewBase.java b/phone/com/android/internal/policy/impl/KeyguardViewBase.java
//Synthetic comment -- index 5a3ebde..975c820 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
//Synthetic comment -- @@ -36,6 +37,7 @@

private KeyguardViewCallback mCallback;
private AudioManager mAudioManager;
    private TelephonyManager mTelephonyManager = null;

public KeyguardViewBase(Context context) {
super(context);
//Synthetic comment -- @@ -131,8 +133,18 @@
final int keyCode = event.getKeyCode();
if (event.getAction() == KeyEvent.ACTION_DOWN) {
switch (keyCode) {
                case KeyEvent.KEYCODE_PLAYPAUSE:
                    /* Suppress PLAYPAUSE toggle when phone is ringing or
                     * in-call to avoid music playback */
                    if (mTelephonyManager == null) {
                        mTelephonyManager = (TelephonyManager) getContext().getSystemService(
                                Context.TELEPHONY_SERVICE);
                    }
                    if (mTelephonyManager != null &&
                            mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
                        return true;  // suppress key event
                    }
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_STOP: 
case KeyEvent.KEYCODE_NEXTSONG: 
case KeyEvent.KEYCODE_PREVIOUSSONG: 
//Synthetic comment -- @@ -167,6 +179,7 @@
}
} else if (event.getAction() == KeyEvent.ACTION_UP) {
switch (keyCode) {
                case KeyEvent.KEYCODE_MUTE:
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
case KeyEvent.KEYCODE_STOP: 








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewManager.java b/phone/com/android/internal/policy/impl/KeyguardViewManager.java
//Synthetic comment -- index f5dd3e5..297d62f 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.internal.R;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Canvas;
import android.util.Log;
//Synthetic comment -- @@ -34,7 +35,7 @@
* the wake lock and report that the keyguard is done, which is in turn,
* reported to this class by the current {@link KeyguardViewBase}.
*/
public class KeyguardViewManager implements KeyguardWindowController {
private final static boolean DEBUG = false;
private static String TAG = "KeyguardViewManager";

//Synthetic comment -- @@ -45,6 +46,9 @@

private final KeyguardUpdateMonitor mUpdateMonitor;

    private WindowManager.LayoutParams mWindowLayoutParams;
    private boolean mNeedsInput = false;
    
private FrameLayout mKeyguardHost;
private KeyguardViewBase mKeyguardView;

//Synthetic comment -- @@ -96,20 +100,27 @@
mKeyguardHost = new KeyguardViewHost(mContext, mCallback);

final int stretch = ViewGroup.LayoutParams.FILL_PARENT;
            int flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                    /*| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR*/ ;
            if (!mNeedsInput) {
                flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            }
WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
stretch, stretch, WindowManager.LayoutParams.TYPE_KEYGUARD,
flags, PixelFormat.OPAQUE);
            lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
            lp.windowAnimations = com.android.internal.R.style.Animation_LockScreen;
            lp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
lp.setTitle("Keyguard");
            mWindowLayoutParams = lp;

mViewManager.addView(mKeyguardHost, lp);
}

if (mKeyguardView == null) {
if (DEBUG) Log.d(TAG, "keyguard view is null, creating it...");
            mKeyguardView = mKeyguardViewProperties.createKeyguardView(mContext, mUpdateMonitor, this);
mKeyguardView.setId(R.id.lock_screen);
mKeyguardView.setCallback(mCallback);

//Synthetic comment -- @@ -128,6 +139,20 @@
mKeyguardView.requestFocus();
}

    public void setNeedsInput(boolean needsInput) {
        mNeedsInput = needsInput;
        if (mWindowLayoutParams != null) {
            if (needsInput) {
                mWindowLayoutParams.flags &=
                    ~WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            } else {
                mWindowLayoutParams.flags |=
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            }
            mViewManager.updateViewLayout(mKeyguardHost, mWindowLayoutParams);
        }
    }
    
/**
* Reset the state of the view.
*/
//Synthetic comment -- @@ -183,7 +208,7 @@
public synchronized void hide() {
if (DEBUG) Log.d(TAG, "hide()");
if (mKeyguardHost != null) {
            mKeyguardHost.setVisibility(View.GONE);
if (mKeyguardView != null) {
mKeyguardHost.removeView(mKeyguardView);
mKeyguardView.cleanUp();








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewMediator.java b/phone/com/android/internal/policy/impl/KeyguardViewMediator.java
//Synthetic comment -- index 2431ffe..ead1348 100644

//Synthetic comment -- @@ -204,6 +204,11 @@
private boolean mKeyboardOpen = false;

/**
     * we send this intent when the keyguard is dismissed.
     */
    private Intent mUserPresentIntent;

    /**
* {@link #setKeyguardEnabled} waits on this condition when it reenables
* the keyguard.
*/
//Synthetic comment -- @@ -248,6 +253,7 @@
context, WindowManagerImpl.getDefault(), this,
mKeyguardViewProperties, mUpdateMonitor);

        mUserPresentIntent = new Intent(Intent.ACTION_USER_PRESENT);
}

/**
//Synthetic comment -- @@ -641,6 +647,7 @@
switch (keyCode) {
case KeyEvent.KEYCODE_VOLUME_UP:
case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_MUTE:
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
case KeyEvent.KEYCODE_STOP: 
//Synthetic comment -- @@ -771,6 +778,7 @@
handleHide();
mPM.userActivity(SystemClock.uptimeMillis(), true);
mWakeLock.release();
        mContext.sendBroadcast(mUserPresentIntent);
}

/**








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewProperties.java b/phone/com/android/internal/policy/impl/KeyguardViewProperties.java
//Synthetic comment -- index a449653..bda08eb 100644

//Synthetic comment -- @@ -29,9 +29,12 @@
* Create a keyguard view.
* @param context the context to use when creating the view.
* @param updateMonitor configuration may be based on this.
     * @param controller for talking back with the containing window.
* @return the view.
*/
    KeyguardViewBase createKeyguardView(Context context,
            KeyguardUpdateMonitor updateMonitor,
            KeyguardWindowController controller);

/**
* Would the keyguard be secure right now?








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardWindowController.java b/phone/com/android/internal/policy/impl/KeyguardWindowController.java
new file mode 100644
//Synthetic comment -- index 0000000..4ad48fb

//Synthetic comment -- @@ -0,0 +1,29 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.policy.impl;

/**
 * Interface passed to the keyguard view, for it to call up to control
 * its containing window.
 */
public interface KeyguardWindowController {
    /**
     * Control whether the window needs input -- that is if it has
     * text fields and thus should allow input method interaction.
     */
    void setNeedsInput(boolean needsInput);
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 4ff87e3..cb3131d 100644

//Synthetic comment -- @@ -32,6 +32,12 @@
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.ColorFilter;
import com.android.internal.R;
import com.android.internal.widget.LockPatternUtils;

//Synthetic comment -- @@ -53,6 +59,8 @@
private static final String TAG = "LockPatternKeyguardView";

private final KeyguardUpdateMonitor mUpdateMonitor;
    private final KeyguardWindowController mWindowController;
    
private View mLockScreen;
private View mUnlockScreen;

//Synthetic comment -- @@ -147,7 +155,8 @@
public LockPatternKeyguardView(
Context context,
KeyguardUpdateMonitor updateMonitor,
            LockPatternUtils lockPatternUtils,
            KeyguardWindowController controller) {
super(context);

asyncCheckForAccount();
//Synthetic comment -- @@ -157,6 +166,7 @@

mUpdateMonitor = updateMonitor;
mLockPatternUtils = lockPatternUtils;
        mWindowController = controller;

mMode = getInitialMode();

//Synthetic comment -- @@ -229,7 +239,8 @@
(LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET 
- LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)) {
showAlmostAtAccountLoginDialog();
                } else if (mHasAccount
                        && failedAttempts >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET) {
mLockPatternUtils.setPermanentlyLocked(true);
updateScreen(mMode);
} else if ((failedAttempts % LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)
//Synthetic comment -- @@ -250,6 +261,11 @@
setFocusableInTouchMode(true);
setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        // wall paper background
        final BitmapDrawable drawable = (BitmapDrawable) context.getWallpaper();
        setBackgroundDrawable(
                new FastBitmapDrawable(drawable.getBitmap()));

// create both the lock and unlock screen so they are quickly available
// when the screen turns on
mLockScreen = createLockScreen();
//Synthetic comment -- @@ -404,6 +420,10 @@
final View visibleScreen = (mode == Mode.LockScreen)
? mLockScreen : getUnlockScreenForCurrentUnlockMode();

        // do this before changing visibility so focus isn't requested before the input
        // flag is set
        mWindowController.setNeedsInput(((KeyguardScreen)visibleScreen).needsInput());
        

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

    /**
     * Used to put wallpaper on the background of the lock screen.  Centers it Horizontally and
     * vertically.
     */
    static private class FastBitmapDrawable extends Drawable {
        private Bitmap mBitmap;

        private FastBitmapDrawable(Bitmap bitmap) {
            mBitmap = bitmap;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawBitmap(
                    mBitmap,
                    (getBounds().width() - mBitmap.getWidth()) / 2,
                    (getBounds().height() - mBitmap.getHeight()) / 2,
                    null);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmap.getWidth();
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmap.getHeight();
        }

        @Override
        public int getMinimumWidth() {
            return mBitmap.getWidth();
        }

        @Override
        public int getMinimumHeight() {
            return mBitmap.getHeight();
        }
    }
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java
//Synthetic comment -- index 697c038..4e0cf09 100644

//Synthetic comment -- @@ -43,8 +43,10 @@
}

public KeyguardViewBase createKeyguardView(Context context,
            KeyguardUpdateMonitor updateMonitor,
            KeyguardWindowController controller) {
        return new LockPatternKeyguardView(context, updateMonitor,
                mLockPatternUtils, controller);
}

public boolean isSecure() {








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index 0825c3b..82a52f9 100644

//Synthetic comment -- @@ -340,7 +340,6 @@
}

public void onOrientationChange(boolean inPortrait) {
}

public void onKeyboardChange(boolean isKeyboardOpen) {
//Synthetic comment -- @@ -351,6 +350,11 @@


/** {@inheritDoc} */
    public boolean needsInput() {
        return false;
    }
    
    /** {@inheritDoc} */
public void onPause() {

}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindow.java b/phone/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index 80a2120..8d9a733 100644

//Synthetic comment -- @@ -1,5 +1,4 @@
/*
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -41,12 +40,14 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -67,6 +68,7 @@
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
//Synthetic comment -- @@ -141,46 +143,127 @@

private ContextMenuBuilder mContextMenu;
private MenuDialogHelper mContextMenuHelper;

private int mVolumeControlStreamType = AudioManager.USE_DEFAULT_STREAM_TYPE;
private long mVolumeKeyUpTime;

private KeyguardManager mKeyguardManager = null;

    private TelephonyManager mTelephonyManager = null;

private boolean mSearchKeyDownReceived;

    private boolean mKeycodeCallTimeoutActive = false;

    private boolean mKeycodeCameraTimeoutActive = false;

    static final int MSG_MENU_LONG_PRESS = 1;
    static final int MSG_MENU_LONG_PRESS_COMPLETE = 2;
    static final int MSG_CALL_LONG_PRESS = 3;
    static final int MSG_CALL_LONG_PRESS_COMPLETE = 4;
    static final int MSG_CAMERA_LONG_PRESS = 5;
    static final int MSG_CAMERA_LONG_PRESS_COMPLETE = 6;
    static final int MSG_SEARCH_LONG_PRESS = 7;
    static final int MSG_SEARCH_LONG_PRESS_COMPLETE = 8;

    private final Handler mKeycodeMenuTimeoutHandler = new Handler() {
@Override
public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MENU_LONG_PRESS: {
                    if (mPanelChordingKey == 0) return;
                    // Before actually doing the long press, enqueue another
                    // message and do the processing there.  This helps if
                    // the app isn't being responsive, and finally woke up --
                    // if the window manager wasn't told about it processing
                    // the down key for too long, it would enqueue the key up
                    // at a time after the timeout of this message.  So we go
                    // through another message, to make sure we process an up
                    // before continuing.
                    mKeycodeMenuTimeoutHandler.sendEmptyMessage(
                            MSG_MENU_LONG_PRESS_COMPLETE);
                    break;
                }
                case MSG_CALL_LONG_PRESS: {
                    if (!mKeycodeCallTimeoutActive) return;
                    // See above.
                    mKeycodeMenuTimeoutHandler.sendEmptyMessage(
                            MSG_CALL_LONG_PRESS_COMPLETE);
                    break;
                }
                case MSG_CAMERA_LONG_PRESS: {
                    if (!mKeycodeCameraTimeoutActive) return;
                    // See above.
                    Message newMessage = Message.obtain(msg);
                    newMessage.what = MSG_CAMERA_LONG_PRESS_COMPLETE;
                    mKeycodeMenuTimeoutHandler.sendMessage(newMessage);
                    break;
                }
                case MSG_SEARCH_LONG_PRESS: {
                    if (!mSearchKeyDownReceived) return;
                    // See above.
                    Message newMessage = Message.obtain(msg);
                    newMessage.what = MSG_SEARCH_LONG_PRESS_COMPLETE;
                    mKeycodeMenuTimeoutHandler.sendMessage(newMessage);
                    break;
                }
                case MSG_MENU_LONG_PRESS_COMPLETE: {
                    if (mPanelChordingKey == 0) return;
                    mPanelChordingKey = 0;
                    mDecor.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    InputMethodManager imm = (InputMethodManager)
                            getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                } break;
                case MSG_CALL_LONG_PRESS_COMPLETE: {
                    if (!mKeycodeCallTimeoutActive) return;
                    mKeycodeCallTimeoutActive = false;
                    mDecor.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    // launch the VoiceDialer
                    Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        sendCloseSystemWindows();
                        getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        startCallActivity();
                    }
                } break;
                case MSG_CAMERA_LONG_PRESS_COMPLETE: {
                    if (!mKeycodeCameraTimeoutActive) return;
                    mKeycodeCameraTimeoutActive = false;
                    mDecor.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    // Broadcast an intent that the Camera button was longpressed
                    Intent intent = new Intent(Intent.ACTION_CAMERA_BUTTON, null);
                    intent.putExtra(Intent.EXTRA_KEY_EVENT, (KeyEvent) msg.obj);
                    getContext().sendOrderedBroadcast(intent, null);
                    sendCloseSystemWindows();
                } break;
                case MSG_SEARCH_LONG_PRESS_COMPLETE: {
                    if (getKeyguardManager().inKeyguardRestrictedInputMode() ||
                            !mSearchKeyDownReceived) {
                        mSearchKeyDownReceived = false;
                        return;
                    }
                    mDecor.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    // launch the search activity
                    Intent intent = new Intent(Intent.ACTION_SEARCH_LONG_PRESS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        sendCloseSystemWindows();
                        getContext().startActivity(intent);
                        // Only clear this if we successfully start the
                        // activity; otherwise we will allow the normal short
                        // press action to be performed.
                        mSearchKeyDownReceived = false;
                    } catch (ActivityNotFoundException e) {
                    }
                } break;
}
}
};

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
            if (getContext().getResources().getConfiguration().keyboard
                    == Configuration.KEYBOARD_NOKEYS) {
                mKeycodeMenuTimeoutHandler.removeMessages(MSG_MENU_LONG_PRESS);
                mKeycodeMenuTimeoutHandler.sendMessageDelayed(
                        mKeycodeMenuTimeoutHandler.obtainMessage(MSG_MENU_LONG_PRESS),
                        ViewConfiguration.getLongPressTimeout());
            }
return preparePanel(st, event);
}

//Synthetic comment -- @@ -579,37 +669,40 @@
*/
public final void onKeyUpPanel(int featureId, KeyEvent event) {
// The panel key was released, so clear the chording key
        if (mPanelChordingKey != 0) {
            mPanelChordingKey = 0;
            mKeycodeMenuTimeoutHandler.removeMessages(MSG_MENU_LONG_PRESS);

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


            case KeyEvent.KEYCODE_PLAYPAUSE:
                /* Suppress PLAYPAUSE toggle when phone is ringing or in-call
                 * to avoid music playback */
                if (mTelephonyManager == null) {
                    mTelephonyManager = (TelephonyManager) getContext().getSystemService(
                            Context.TELEPHONY_SERVICE);
                }
                if (mTelephonyManager != null &&
                        mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
                    return true;  // suppress key event
                }
            case KeyEvent.KEYCODE_MUTE:
            case KeyEvent.KEYCODE_HEADSETHOOK:
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
                mKeycodeMenuTimeoutHandler.removeMessages(MSG_CAMERA_LONG_PRESS);
                Message message = mKeycodeMenuTimeoutHandler.obtainMessage(MSG_CAMERA_LONG_PRESS);
message.obj = event;
                mKeycodeMenuTimeoutHandler.sendMessageDelayed(message,
ViewConfiguration.getLongPressTimeout());
return true;
}
//Synthetic comment -- @@ -1191,16 +1296,29 @@
}
if (event.getRepeatCount() > 0) break;
mKeycodeCallTimeoutActive = true;
                mKeycodeMenuTimeoutHandler.removeMessages(MSG_CALL_LONG_PRESS);
                mKeycodeMenuTimeoutHandler.sendMessageDelayed(
                        mKeycodeMenuTimeoutHandler.obtainMessage(MSG_CALL_LONG_PRESS),
ViewConfiguration.getLongPressTimeout());
return true;
}

case KeyEvent.KEYCODE_SEARCH: {
if (event.getRepeatCount() == 0) {
mSearchKeyDownReceived = true;
                    Configuration config = getContext().getResources().getConfiguration(); 
                    if (config.keyboard == Configuration.KEYBOARD_NOKEYS
                            || config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
                        // If this device does not have a hardware keyboard,
                        // or that keyboard is hidden, then we can't use the
                        // search key for chording to perform shortcuts;
                        // instead, we will let the user long press,
                        mKeycodeMenuTimeoutHandler.removeMessages(MSG_SEARCH_LONG_PRESS);
                        mKeycodeMenuTimeoutHandler.sendMessageDelayed(
                                mKeycodeMenuTimeoutHandler.obtainMessage(MSG_SEARCH_LONG_PRESS),
                                ViewConfiguration.getLongPressTimeout());
                    }
                    return true;
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
                mKeycodeMenuTimeoutHandler.removeMessages(MSG_CAMERA_LONG_PRESS);
if (!mKeycodeCameraTimeoutActive) break;
mKeycodeCameraTimeoutActive = false;
// Add short press behavior here if desired
//Synthetic comment -- @@ -1281,7 +1399,7 @@
break;
}
if (event.getRepeatCount() > 0) break;
                mKeycodeMenuTimeoutHandler.removeMessages(MSG_CALL_LONG_PRESS);
if (!mKeycodeCallTimeoutActive) break;
mKeycodeCallTimeoutActive = false;
startCallActivity();
//Synthetic comment -- @@ -1295,6 +1413,7 @@
*/
if (getKeyguardManager().inKeyguardRestrictedInputMode() ||
!mSearchKeyDownReceived) {
                    mSearchKeyDownReceived = false;
break;
}
mSearchKeyDownReceived = false;
//Synthetic comment -- @@ -1307,6 +1426,7 @@
}

private void startCallActivity() {
        sendCloseSystemWindows();
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
            mKeycodeMenuTimeoutHandler.removeMessages(MSG_MENU_LONG_PRESS);
            mKeycodeMenuTimeoutHandler.removeMessages(MSG_CALL_LONG_PRESS);
            mKeycodeMenuTimeoutHandler.removeMessages(MSG_CAMERA_LONG_PRESS);
mKeycodeCallTimeoutActive = false;
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

        if (params.windowAnimations == 0) {
            params.windowAnimations = a.getResourceId(
                    com.android.internal.R.styleable.Window_windowAnimationStyle, 0);
        }

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

    void sendCloseSystemWindows() {
        PhoneWindowManager.sendCloseSystemWindows(getContext(), null);
    }

    void sendCloseSystemWindows(String reason) {
        PhoneWindowManager.sendCloseSystemWindows(getContext(), reason);
    }
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 346470f..3e2b927 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.provider.Settings;

import com.android.internal.policy.PolicyManager;
//Synthetic comment -- @@ -47,10 +48,11 @@
import android.util.EventLog;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowOrientationListener;
import android.view.RawInputEvent;
import android.view.Surface;
import android.view.View;
//Synthetic comment -- @@ -88,11 +90,12 @@
import android.media.IAudioService;
import android.media.AudioManager;

/**
 * WindowManagerPolicy implementation for the Android phone UI.  This
 * introduces a new method suffix, Lp, for an internal lock of the
 * PhoneWindowManager.  This is used to protect some internal state, and
 * can be acquired with either thw Lw and Li lock held, so has the restrictions
 * of both of those when held.
*/
public class PhoneWindowManager implements WindowManagerPolicy {
static final String TAG = "WindowManager";
//Synthetic comment -- @@ -138,13 +141,20 @@
static public final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
static public final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

    // Vibrator pattern for haptic feedback of a long press.
    private static final long[] LONG_PRESS_VIBE_PATTERN = {0, 1, 20, 21};
    
    final Object mLock = new Object();
    
Context mContext;
IWindowManager mWindowManager;
LocalPowerManager mPowerManager;
    Vibrator mVibrator; // Vibrator for giving feedback of orientation changes

/** If true, hitting shift & menu will broadcast Intent.ACTION_BUG_REPORT */
boolean mEnableShiftMenuBugReports = false;

    boolean mSafeMode;
WindowState mStatusBar = null;
WindowState mSearchBar = null;
WindowState mKeyguard = null;
//Synthetic comment -- @@ -229,58 +239,51 @@

public void update() {
ContentResolver resolver = mContext.getContentResolver();
            synchronized (mLock) {
                mEndcallBehavior = Settings.System.getInt(resolver,
                        Settings.System.END_BUTTON_BEHAVIOR, DEFAULT_ENDCALL_BEHAVIOR);
                int accelerometerDefault = Settings.System.getInt(resolver,
                        Settings.System.ACCELEROMETER_ROTATION, DEFAULT_ACCELEROMETER_ROTATION);
                if (mAccelerometerDefault != accelerometerDefault) {
                    mAccelerometerDefault = accelerometerDefault;
                    updateOrientationListenerLp();
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
}

    class MyOrientationListener extends WindowOrientationListener {
MyOrientationListener(Context context) {
super(context);
}

@Override
        public void onOrientationChanged(int rotation) {
            // Send updates based on orientation value
            if (rotation != mSensorRotation) {
                if(localLOGV) Log.i(TAG, "onOrientationChanged, rotation changed from "+rotation+" to "+mSensorRotation);
                // Update window manager.  The lid rotation hasn't changed,
                // but we want it to re-evaluate the final rotation in case
                // it needs to call back and get the sensor orientation.
                mSensorRotation = rotation;
                try {
                    mWindowManager.setRotation(rotation, false);
                } catch (RemoteException e) {
                    // Ignore
}
}
}                                      
}
MyOrientationListener mOrientationListener;

    boolean useSensorForOrientationLp() {
if(mCurrentAppOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
return true;
}
//Synthetic comment -- @@ -292,6 +295,26 @@
return false;
}

    boolean needSensorRunningLp() {
        if (mCurrentAppOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
            // If the application has explicitly requested to follow the
            // orientation, then we need to turn the sensor or.
            return true;
        }
        if (mAccelerometerDefault != 0) {
            // If the setting for using the sensor by default is enabled, then
            // we will always leave it on.  Note that the user could go to
            // a window that forces an orientation that does not use the
            // sensor and in theory we could turn it off... however, when next
            // turning it on we won't have a good value for the current
            // orientation for a little bit, which can cause orientation
            // changes to lag, so we'd like to keep it always on.  (It will
            // still be turned off when the screen is off.)
            return true;
        }
        return false;
    }
    
/*
* Various use cases for invoking this function
* screen turning off, should always disable listeners if already enabled
//Synthetic comment -- @@ -302,28 +325,36 @@
* screen turning on and current app has sensor based orientation, enable listeners if needed
* screen turning on and current app has nosensor based orientation, do nothing
*/
    void updateOrientationListenerLp() {
        if (!mOrientationListener.canDetectOrientation()) {
            // If sensor is turned off or nonexistent for some reason
            return;
        }
//Could have been invoked due to screen turning on or off or
//change of the currently visible window's orientation
        if (localLOGV) Log.i(TAG, "Screen status="+mScreenOn+
", current orientation="+mCurrentAppOrientation+
", SensorEnabled="+mOrientationSensorEnabled);
boolean disable = true;
        if (mScreenOn) {
            if (needSensorRunningLp()) {
disable = false;
//enable listener if not already enabled
                if (!mOrientationSensorEnabled) {
mOrientationListener.enable();
if(localLOGV) Log.i(TAG, "Enabling listeners");
                    // We haven't had the sensor on, so don't yet know
                    // the rotation.
                    mSensorRotation = -1;
mOrientationSensorEnabled = true;
}
} 
} 
//check if sensors need to be disabled
        if (disable && mOrientationSensorEnabled) {
mOrientationListener.disable();
if(localLOGV) Log.i(TAG, "Disabling listeners");
            mSensorRotation = -1;
mOrientationSensorEnabled = false;
}
}
//Synthetic comment -- @@ -331,6 +362,7 @@
Runnable mEndCallLongPress = new Runnable() {
public void run() {
mShouldTurnOffOnKeyUp = false;
            performHapticFeedbackLw(null, HapticFeedbackConstants.LONG_PRESS, false);
sendCloseSystemWindows(SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS);
showGlobalActionsDialog();
}
//Synthetic comment -- @@ -338,7 +370,7 @@

void showGlobalActionsDialog() {
if (mGlobalActions == null) {
            mGlobalActions = new GlobalActions(mContext);
}
final boolean keyguardShowing = mKeyguardMediator.isShowing();
mGlobalActions.showDialog(keyguardShowing, isDeviceProvisioned());
//Synthetic comment -- @@ -364,6 +396,7 @@
* the user lets go of the home key
*/
mHomePressed = false;
            performHapticFeedbackLw(null, HapticFeedbackConstants.LONG_PRESS, false);
sendCloseSystemWindows(SYSTEM_DIALOG_REASON_RECENT_APPS);
showRecentAppsDialog();
}
//Synthetic comment -- @@ -471,16 +504,6 @@
config.hardKeyboardHidden = lidOpen
? Configuration.KEYBOARDHIDDEN_NO
: Configuration.KEYBOARDHIDDEN_YES;
}

public boolean isCheekPressedAgainstScreen(MotionEvent ev) {
//Synthetic comment -- @@ -589,11 +612,17 @@

win.setType(
WindowManager.LayoutParams.TYPE_APPLICATION_STARTING);
        // Force the window flags: this is a fake window, so it is not really
        // touchable or focusable by the user.  We also add in the ALT_FOCUSABLE_IM
        // flag because we do know that the next window will take input
        // focus, so we want to get the IME window up on top of us right away.
win.setFlags(
WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

win.setLayout(WindowManager.LayoutParams.FILL_PARENT,
WindowManager.LayoutParams.FILL_PARENT);
//Synthetic comment -- @@ -732,6 +761,11 @@
return keyguardIsShowingTq() || inKeyguardRestrictedKeyInputMode();
}

    private static final int[] WINDOW_TYPES_WHERE_HOME_DOESNT_WORK = {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
        };

/** {@inheritDoc} */
public boolean interceptKeyTi(WindowState win, int code, int metaKeys, boolean down, 
int repeatCount) {
//Synthetic comment -- @@ -739,7 +773,7 @@

if (false) {
Log.d(TAG, "interceptKeyTi code=" + code + " down=" + down + " repeatCount="
                    + repeatCount + " keyguardOn=" + keyguardOn + " mHomePressed=" + mHomePressed);
}

// Clear a pending HOME longpress if the user releases Home
//Synthetic comment -- @@ -795,15 +829,19 @@
// right now to interact with applications.
WindowManager.LayoutParams attrs = win != null ? win.getAttrs() : null;
if (attrs != null) {
                final int type = attrs.type;
                if (type == WindowManager.LayoutParams.TYPE_KEYGUARD
                        || type == WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG) {
                    // the "app" is keyguard, so give it the key
return false;
}
                final int typeCount = WINDOW_TYPES_WHERE_HOME_DOESNT_WORK.length;
                for (int i=0; i<typeCount; i++) {
                    if (type == WINDOW_TYPES_WHERE_HOME_DOESNT_WORK[i]) {
                        // don't do anything, but also don't pass it to the app
                        return true;
                    }
                }
}

if (down && repeatCount == 0) {
//Synthetic comment -- @@ -950,7 +988,7 @@
pf.bottom = df.bottom = vf.bottom = displayHeight;

mStatusBar.computeFrameLw(pf, df, vf, vf);
            if (mStatusBar.isVisibleLw()) {
// If the status bar is hidden, we don't want to cause
// windows behind it to scroll.
mDockTop = mContentTop = mCurTop = mStatusBar.getFrameLw().bottom;
//Synthetic comment -- @@ -1154,35 +1192,57 @@
/** {@inheritDoc} */
public void animatingWindowLw(WindowState win,
WindowManager.LayoutParams attrs) {
        if (win.isVisibleLw()) {
            if ((attrs.flags & FLAG_FORCE_NOT_FULLSCREEN) != 0) {
                mForceStatusBar = true;
            } else if (mTopFullscreenOpaqueWindowState == null
                    && attrs.type >= FIRST_APPLICATION_WINDOW
                    && attrs.type <= LAST_APPLICATION_WINDOW
                    && win.fillsScreenLw(mW, mH, true, false)
                    && win.isVisibleLw()) {
                mTopFullscreenOpaqueWindowState = win;
            }
}
}

/** {@inheritDoc} */
public boolean finishAnimationLw() {
        boolean changed = false;
        boolean hiding = false;
if (mStatusBar != null) {
            //Log.i(TAG, "force=" + mForceStatusBar
            //        + " top=" + mTopFullscreenOpaqueWindowState);
if (mForceStatusBar) {
                changed |= mStatusBar.showLw(true);
} else if (mTopFullscreenOpaqueWindowState != null) {
                //Log.i(TAG, "frame: " + mTopFullscreenOpaqueWindowState.getFrameLw()
                //        + " shown frame: " + mTopFullscreenOpaqueWindowState.getShownFrameLw());
                //Log.i(TAG, "attr: " + mTopFullscreenOpaqueWindowState.getAttrs());
                WindowManager.LayoutParams lp =
                    mTopFullscreenOpaqueWindowState.getAttrs();
                boolean hideStatusBar =
                    (lp.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
                if (hideStatusBar) {
                    changed |= mStatusBar.hideLw(true);
                    hiding = true;
                } else {
                    changed |= mStatusBar.showLw(true);
                }
            }
}
        
        if (changed && hiding) {
            IStatusBar sbs = IStatusBar.Stub.asInterface(ServiceManager.getService("statusbar"));
            if (sbs != null) {
                try {
                    // Make sure the window shade is hidden.
                    sbs.deactivate();
                } catch (RemoteException e) {
                }
            }
        }
        
        return changed;
}

/** {@inheritDoc} */
//Synthetic comment -- @@ -1193,6 +1253,22 @@
// lid changed state
mLidOpen = event.value == 0;
updateRotation();
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
}
return false;
//Synthetic comment -- @@ -1299,6 +1375,11 @@
final boolean isWakeKey = isWakeKeyTq(event);
final boolean keyguardShowing = keyguardIsShowingTq();

        if (false) {
            Log.d(TAG, "interceptKeyTq event=" + event + " keycode=" + event.keycode
                  + " screenIsOn=" + screenIsOn + " keyguardShowing=" + keyguardShowing);
        }

if (keyguardShowing) {
if (screenIsOn) {
// when the screen is on, always give the event to the keyguard
//Synthetic comment -- @@ -1402,6 +1483,65 @@
mBroadcastWakeLock.acquire();
mHandler.post(new PassHeadsetKey(keyEvent));
}
            } else if (code == KeyEvent.KEYCODE_CALL) {
                // If an incoming call is ringing, answer it!
                // (We handle this key here, rather than in the InCallScreen, to make
                // sure we'll respond to the key even if the InCallScreen hasn't come to
                // the foreground yet.)

                // We answer the call on the DOWN event, to agree with
                // the "fallback" behavior in the InCallScreen.
                if (down) {
                    try {
                        ITelephony phoneServ = getPhoneInterface();
                        if (phoneServ != null) {
                            if (phoneServ.isRinging()) {
                                Log.i(TAG, "interceptKeyTq:"
                                      + " CALL key-down while ringing: Answer the call!");
                                phoneServ.answerRingingCall();

                                // And *don't* pass this key thru to the current activity
                                // (which is presumably the InCallScreen.)
                                result &= ~ACTION_PASS_TO_USER;
                            }
                        } else {
                            Log.w(TAG, "CALL button: Unable to find ITelephony interface");
                        }
                    } catch (RemoteException ex) {
                        Log.w(TAG, "CALL button: RemoteException from getPhoneInterface()", ex);
                    }
                }
            } else if ((code == KeyEvent.KEYCODE_VOLUME_UP)
                       || (code == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                // If an incoming call is ringing, either VOLUME key means
                // "silence ringer".  We handle these keys here, rather than
                // in the InCallScreen, to make sure we'll respond to them
                // even if the InCallScreen hasn't come to the foreground yet.

                // Look for the DOWN event here, to agree with the "fallback"
                // behavior in the InCallScreen.
                if (down) {
                    try {
                        ITelephony phoneServ = getPhoneInterface();
                        if (phoneServ != null) {
                            if (phoneServ.isRinging()) {
                                Log.i(TAG, "interceptKeyTq:"
                                      + " VOLUME key-down while ringing: Silence ringer!");
                                // Silence the ringer.  (It's safe to call this
                                // even if the ringer has already been silenced.)
                                phoneServ.silenceRinger();

                                // And *don't* pass this key thru to the current activity
                                // (which is probably the InCallScreen.)
                                result &= ~ACTION_PASS_TO_USER;
                            }
                        } else {
                            Log.w(TAG, "VOLUME button: Unable to find ITelephony interface");
                        }
                    } catch (RemoteException ex) {
                        Log.w(TAG, "VOLUME button: RemoteException from getPhoneInterface()", ex);
                    }
                }
}
}

//Synthetic comment -- @@ -1416,10 +1556,12 @@
}

public void run() {
            if (ActivityManagerNative.isSystemReady()) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
                intent.putExtra(Intent.EXTRA_KEY_EVENT, mKeyEvent);
                mContext.sendOrderedBroadcast(intent, null, mBroadcastDone,
                        mHandler, Activity.RESULT_OK, null, null);
            }
}
}

//Synthetic comment -- @@ -1460,18 +1602,22 @@

/** {@inheritDoc} */
public void screenTurnedOff(int why) {
        synchronized (mLock) {
            EventLog.writeEvent(70000, 0);
            mKeyguardMediator.onScreenTurnedOff(why);
            mScreenOn = false;
            updateOrientationListenerLp();
        }
}

/** {@inheritDoc} */
public void screenTurnedOn() {
        synchronized (mLock) {
            EventLog.writeEvent(70000, 1);
            mKeyguardMediator.onScreenTurnedOn();
            mScreenOn = true;
            updateOrientationListenerLp();
        }
}

/** {@inheritDoc} */
//Synthetic comment -- @@ -1501,47 +1647,66 @@
sendCloseSystemWindows();
}

    void sendCloseSystemWindows() {
        sendCloseSystemWindows(mContext, null);
}

    void sendCloseSystemWindows(String reason) {
        sendCloseSystemWindows(mContext, reason);
}

    static void sendCloseSystemWindows(Context context, String reason) {
        if (ActivityManagerNative.isSystemReady()) {
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            if (reason != null) {
                intent.putExtra(SYSTEM_DIALOG_REASON_KEY, reason);
}
            context.sendBroadcast(intent);
        }
    }

    public int rotationForOrientationLw(int orientation, int lastRotation,
            boolean displayEnabled) {
        synchronized (mLock) {
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
                if (useSensorForOrientationLp()) {
                    // If the user has enabled auto rotation by default, do it.
                    return mSensorRotation >= 0 ? mSensorRotation : lastRotation;
                }
                return Surface.ROTATION_0;
            }
        }
    }

    public boolean detectSafeMode() {
        try {
            int menuState = mWindowManager.getKeycodeState(KeyEvent.KEYCODE_MENU);
            mSafeMode = menuState > 0;
            Log.i(TAG, "Menu key state: " + menuState + " safeMode=" + mSafeMode);
            return mSafeMode;
        } catch (RemoteException e) {
            // Doom! (it's also local)
            throw new RuntimeException("window manager dead");
}
}

/** {@inheritDoc} */
public void systemReady() {
try {
            if (mSafeMode) {
// If the user is holding the menu key code, then we are
// going to boot into safe mode.
ActivityManagerNative.getDefault().enterSafeMode();
//Synthetic comment -- @@ -1549,12 +1714,16 @@
// tell the keyguard
mKeyguardMediator.onSystemReady();
android.os.SystemProperties.set("dev.bootcomplete", "1"); 
            synchronized (mLock) {
                updateOrientationListenerLp();
                mVibrator = new Vibrator();
            }
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
}

/**
//Synthetic comment -- @@ -1620,10 +1773,32 @@
return true;
}

    public void setCurrentOrientationLw(int newOrientation) {
        synchronized (mLock) {
            if (newOrientation != mCurrentAppOrientation) {
                mCurrentAppOrientation = newOrientation;
                updateOrientationListenerLp();
            }
        }
    }
    
    public boolean performHapticFeedbackLw(WindowState win, int effectId, boolean always) {
        if (!always && Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.HAPTIC_FEEDBACK_ENABLED, 0) == 0) {
            return false;
        }
        switch (effectId) {
            case HapticFeedbackConstants.LONG_PRESS:
                mVibrator.vibrate(LONG_PRESS_VIBE_PATTERN, -1);
                return true;
        }
        return false;
    }
    
    public void screenOnStoppedLw() {
        if (!mKeyguardMediator.isShowing()) {
            long curTime = SystemClock.uptimeMillis();
            mPowerManager.userActivity(curTime, false, LocalPowerManager.OTHER_EVENT);
}
}
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/SimUnlockScreen.java b/phone/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index fceec5f..0f7fe32 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
private final boolean mCreatedWithKeyboardOpen;

private TextView mHeaderText;
    private TextView mPinText;

private TextView mOkButton;
private TextView mEmergencyCallButton;
//Synthetic comment -- @@ -75,7 +75,7 @@
}

mHeaderText = (TextView) findViewById(R.id.headerText);
        mPinText = (TextView) findViewById(R.id.pinDisplay);
mBackSpaceButton = findViewById(R.id.backspace);
mBackSpaceButton.setOnClickListener(this);

//Synthetic comment -- @@ -93,6 +93,11 @@
}

/** {@inheritDoc} */
    public boolean needsInput() {
        return true;
    }
    
    /** {@inheritDoc} */
public void onPause() {

}
//Synthetic comment -- @@ -154,7 +159,7 @@

public void onClick(View v) {
if (v == mBackSpaceButton) {
            final Editable digits = mPinText.getEditableText();
final int len = digits.length();
if (len > 0) {
digits.delete(len-1, len);








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/UnlockScreen.java b/phone/com/android/internal/policy/impl/UnlockScreen.java
//Synthetic comment -- index 65ab439..9aedf90 100644

//Synthetic comment -- @@ -16,14 +16,10 @@

package com.android.internal.policy.impl;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
//Synthetic comment -- @@ -230,6 +226,11 @@
public void onKeyboardChange(boolean isKeyboardOpen) {}

/** {@inheritDoc} */
    public boolean needsInput() {
        return false;
    }
    
    /** {@inheritDoc} */
public void onPause() {
if (mCountdownTimer != null) {
mCountdownTimer.cancel();
//Synthetic comment -- @@ -288,7 +289,7 @@
mLockPatternView
.setDisplayMode(LockPatternView.DisplayMode.Correct);
mUnlockIcon.setVisibility(View.GONE);
                mUnlockHeader.setText("");
mCallback.keyguardDone(true);
} else {
mCallback.pokeWakelock(UNLOCK_PATTERN_WAKE_INTERVAL_MS);
//Synthetic comment -- @@ -299,8 +300,7 @@
mCallback.reportFailedPatternAttempt();
}
if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                    long deadline = mLockPatternUtils.setLockoutAttemptDeadline();
handleAttemptLockout(deadline);
return;
}







