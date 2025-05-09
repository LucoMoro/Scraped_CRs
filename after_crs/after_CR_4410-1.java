/*Extend ANDROID with CDMA mobile technology support

This is a clean upload of the first contribution of a project that has the goal to extend the
Android telephony layers with CDMA mobile technology support.
The current release 1 of Android supports GSM/WCDMA as mobile communication
standards.
Our contribution will contain changes in the phone related applications, the
application framework telephony packages and in the RIL daemon library space.
The implementation of the CDMA support requires architectural changes in the
telephony package and extensions of the RIL interface.
The application interface (SDK interface) will be extended to provide
CDMA specific features/information to the phone related application and other
applications.
Where ever possible the actual used radio technology is transparent for the
application using mobile connections.

Each increment of the contribution will provide a pre-tested set of use case
implementations.
The final contribution will support CDMA functionality for Android phones
supporting
either CDMA mobile technology only or a world mode including GSM/WCDMA and CDMA.
The following CDMA technologies are considered: IS-95, CDMA2000 1xRTT, CDMA2000
1x EVDO.

This contribution implements the following use cases:
UC Startup-Phone
UC Initialize Phone
UC Access SIM/RUIM
UC Network Indications
UC Mobile Originated Call
UC Mobile Terminated Call
UC Network / Phone Settings
UC Supplementary Services (partly)

With these use cases the phone will
- start up,
- access the CDMA subscription and other information from memory of from the card (either SIM, USIM or RUIM),
- register to the network,
- provides registration status to the application for displaying
- be able to handle incoming and outgoing voice calls,
- provide phone and call settings in the settings application
- provide supplementary services in the settings application

Various review comments are also fixed with this contribution.

Approved By :- Aravind Mahishi , aravind.mahishi@teleca.com
               Wolfgang Schmidt, wolfgang.schmidt@teleca.com*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
//Synthetic comment -- index ab7c744..4cc2fe2 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
import static android.provider.Telephony.Intents.EXTRA_SHOW_SPN;
import static android.provider.Telephony.Intents.EXTRA_SPN;
import static android.provider.Telephony.Intents.SPN_STRINGS_UPDATED_ACTION;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.TelephonyIntents;
import android.util.Log;
import com.android.internal.R;
//Synthetic comment -- @@ -61,7 +61,7 @@

private final Context mContext;

    private IccCard.State mSimState = IccCard.State.READY;
private boolean mInPortrait;
private boolean mKeyboardOpen;

//Synthetic comment -- @@ -94,38 +94,39 @@


/**
     * When we receive a 
     * {@link com.android.internal.telephony.TelephonyIntents#ACTION_SIM_STATE_CHANGED} broadcast, 
     * and then pass a result via our handler to {@link KeyguardUpdateMonitor#handleSimStateChange},
* we need a single object to pass to the handler.  This class helps decode
* the intent and provide a {@link SimCard.State} result. 
*/
private static class SimArgs {

        public final IccCard.State simState;

private SimArgs(Intent intent) {
if (!TelephonyIntents.ACTION_SIM_STATE_CHANGED.equals(intent.getAction())) {
throw new IllegalArgumentException("only handles intent ACTION_SIM_STATE_CHANGED");
}
            String stateExtra = intent.getStringExtra(IccCard.INTENT_KEY_ICC_STATE);
            if (IccCard.INTENT_VALUE_ICC_ABSENT.equals(stateExtra)) {
                this.simState = IccCard.State.ABSENT;
            } else if (IccCard.INTENT_VALUE_ICC_READY.equals(stateExtra)) {
                this.simState = IccCard.State.READY;
            } else if (IccCard.INTENT_VALUE_ICC_LOCKED.equals(stateExtra)) {
final String lockedReason = intent
                        .getStringExtra(IccCard.INTENT_KEY_LOCKED_REASON);
                if (IccCard.INTENT_VALUE_LOCKED_ON_PIN.equals(lockedReason)) {
                    this.simState = IccCard.State.PIN_REQUIRED;
                } else if (IccCard.INTENT_VALUE_LOCKED_ON_PUK.equals(lockedReason)) {
                    this.simState = IccCard.State.PUK_REQUIRED;
} else {
                    this.simState = IccCard.State.UNKNOWN;
}
            } else if (IccCard.INTENT_VALUE_LOCKED_NETWORK.equals(stateExtra)) {
                this.simState = IccCard.State.NETWORK_LOCKED;
} else {
                this.simState = IccCard.State.UNKNOWN;
}
}

//Synthetic comment -- @@ -195,7 +196,7 @@
mKeyboardOpen = queryKeyboardOpen();

// take a guess to start
        mSimState = IccCard.State.READY;
mDevicePluggedIn = true;
mBatteryLevel = 100;

//Synthetic comment -- @@ -317,14 +318,14 @@
* Handle {@link #MSG_SIM_STATE_CHANGE}
*/
private void handleSimStateChange(SimArgs simArgs) {
        final IccCard.State state = simArgs.simState;

if (DEBUG) {
Log.d(TAG, "handleSimStateChange: intentValue = " + simArgs + " "
+ "state resolved to " + state.toString());
}

        if (state != IccCard.State.UNKNOWN && state != mSimState) {
mSimState = state;
for (int i = 0; i < mSimStateCallbacks.size(); i++) {
mSimStateCallbacks.get(i).onSimStateChanged(state);
//Synthetic comment -- @@ -461,7 +462,7 @@
* Callback to notify of sim state change.
*/
interface SimStateCallback {
        void onSimStateChanged(IccCard.State simState);
}

/**
//Synthetic comment -- @@ -489,7 +490,7 @@
mSimStateCallbacks.add(callback);
}

    public IccCard.State getSimState() {
return mSimState;
}

//Synthetic comment -- @@ -499,7 +500,7 @@
* broadcast from the telephony code.
*/
public void reportSimPinUnlocked() {
        mSimState = IccCard.State.READY;
}

public boolean isInPortrait() {








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewMediator.java b/phone/com/android/internal/policy/impl/KeyguardViewMediator.java
//Synthetic comment -- index ac816b0..50d4f20 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
import android.view.KeyEvent;
import android.view.WindowManagerImpl;
import android.view.WindowManagerPolicy;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.widget.LockPatternUtils;

//Synthetic comment -- @@ -88,7 +88,8 @@

private final static String TAG = "KeyguardViewMediator";

    private static final String DELAYED_KEYGUARD_ACTION = 
        "com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD";

// used for handler messages
private static final int TIMEOUT = 1;
//Synthetic comment -- @@ -289,7 +290,8 @@
0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, when,
sender);
                if (DEBUG) Log.d(TAG, "setting alarm to turn off keyguard, seq = " 
                                 + mDelayedShowingSequence);
} else {
doKeyguard();
}
//Synthetic comment -- @@ -438,8 +440,8 @@

// if the setup wizard hasn't run yet, don't show
final boolean provisioned = mUpdateMonitor.isDeviceProvisioned();
            final IccCard.State state = mUpdateMonitor.getSimState();
            final boolean lockedOrMissing = state.isPinLocked() || (state == IccCard.State.ABSENT);
if (!lockedOrMissing && !provisioned) {
if (DEBUG) Log.d(TAG, "doKeyguard: not showing because device isn't provisioned"
+ " and the sim is not locked or missing");
//Synthetic comment -- @@ -553,7 +555,7 @@
}

/** {@inheritDoc} */
    public void onSimStateChanged(IccCard.State simState) {
if (DEBUG) Log.d(TAG, "onSimStateChanged: " + simState);

switch (simState) {
//Synthetic comment -- @@ -562,7 +564,7 @@
// gone through setup wizard
if (!mUpdateMonitor.isDeviceProvisioned()) {
if (!isShowing()) {
                        if (DEBUG) Log.d(TAG, "INTENT_VALUE_ICC_ABSENT and keygaurd isn't showing, we need "
+ "to show the keyguard since the device isn't provisioned yet.");
doKeyguard();
} else {
//Synthetic comment -- @@ -573,7 +575,7 @@
case PIN_REQUIRED:
case PUK_REQUIRED:
if (!isShowing()) {
                    if (DEBUG) Log.d(TAG, "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing, we need "
+ "to show the keyguard so the user can enter their sim pin");
doKeyguard();
} else {
//Synthetic comment -- @@ -716,10 +718,8 @@
*/
private Handler mHandler = new Handler() {
@Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
case TIMEOUT:
handleTimeout(msg.arg1);
return ;








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 66a4c4e..5a0d03f 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import com.android.internal.telephony.IccCard;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
//Synthetic comment -- @@ -122,7 +122,7 @@
private boolean stuckOnLockScreenBecauseSimMissing() {
return mRequiresSim
&& (!mUpdateMonitor.isDeviceProvisioned())
                && (mUpdateMonitor.getSimState() == IccCard.State.ABSENT);
}

/**
//Synthetic comment -- @@ -159,9 +159,9 @@
}

public void goToUnlockScreen() {
                final IccCard.State simState = mUpdateMonitor.getSimState();
if (stuckOnLockScreenBecauseSimMissing()
                         || (simState == IccCard.State.PUK_REQUIRED)){
// stuck on lock screen when sim missing or puk'd
return;
}
//Synthetic comment -- @@ -211,7 +211,8 @@
mUpdateMonitor.reportFailedAttempt();
final int failedAttempts = mUpdateMonitor.getFailedAttempts();
if (failedAttempts ==
                        (LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET - 
                         LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)) {
showAlmostAtAccountLoginDialog();
} else if (failedAttempts >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET) {
mLockPatternUtils.setPermanentlyLocked(true);
//Synthetic comment -- @@ -299,7 +300,7 @@
public void wakeWhenReadyTq(int keyCode) {
if (DEBUG) Log.d(TAG, "onWakeKey");
if (keyCode == KeyEvent.KEYCODE_MENU && isSecure() && (mMode == Mode.LockScreen)
                && (mUpdateMonitor.getSimState() != IccCard.State.PUK_REQUIRED)) {
if (DEBUG) Log.d(TAG, "switching screens to unlock screen because wake key was MENU");
updateScreen(Mode.UnlockScreen);
getCallback().pokeWakelock();
//Synthetic comment -- @@ -337,8 +338,8 @@
if (unlockMode == UnlockMode.Pattern) {
return mLockPatternUtils.isLockPatternEnabled();
} else if (unlockMode == UnlockMode.SimPin) {
            return mUpdateMonitor.getSimState() == IccCard.State.PIN_REQUIRED
                        || mUpdateMonitor.getSimState() == IccCard.State.PUK_REQUIRED;
} else if (unlockMode == UnlockMode.Account) {
return true;
} else {
//Synthetic comment -- @@ -451,8 +452,8 @@
* the lock screen (lock or unlock).
*/
private Mode getInitialMode() {
        final IccCard.State simState = mUpdateMonitor.getSimState();
        if (stuckOnLockScreenBecauseSimMissing() || (simState == IccCard.State.PUK_REQUIRED)) {
return Mode.LockScreen;
} else if (mUpdateMonitor.isKeyboardOpen() && isSecure()) {
return Mode.UnlockScreen;
//Synthetic comment -- @@ -465,8 +466,8 @@
* Given the current state of things, what should the unlock screen be?
*/
private UnlockMode getUnlockMode() {
        final IccCard.State simState = mUpdateMonitor.getSimState();
        if (simState == IccCard.State.PIN_REQUIRED || simState == IccCard.State.PUK_REQUIRED) {
return UnlockMode.SimPin;
} else {
return mLockPatternUtils.isPermanentlyLocked() ?
//Synthetic comment -- @@ -497,7 +498,8 @@
int timeoutInSeconds = (int) LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS / 1000;
String message = mContext.getString(
R.string.lockscreen_failed_attempts_almost_glogin,
                LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET
                - LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT,
LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT,
timeoutInSeconds);
final AlertDialog dialog = new AlertDialog.Builder(mContext)








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java
//Synthetic comment -- index 697c038..2ce7f64 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.internal.widget.LockPatternUtils;

import android.content.Context;
import com.android.internal.telephony.IccCard;

/**
* Knows how to create a lock pattern keyguard view, and answer questions about
//Synthetic comment -- @@ -57,9 +57,9 @@
}

private boolean isSimPinSecure() {
        final IccCard.State simState = mUpdateMonitor.getSimState();
        return (simState == IccCard.State.PIN_REQUIRED || simState == IccCard.State.PUK_REQUIRED
            || simState == IccCard.State.ABSENT);
}

}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
//Synthetic comment -- index c717cc3..0a9a4cd 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.telephony.IccCard;

import java.util.Date;

//Synthetic comment -- @@ -193,7 +193,8 @@
final View view = mOnlyVisibleWhenSimNotOk[i];
view.setVisibility(View.GONE);
}
            refreshSimOkHeaders(mUpdateMonitor.getTelephonyPlmn(), 
            mUpdateMonitor.getTelephonySpn());
refreshAlarmDisplay();
refreshBatteryDisplay();
} else {
//Synthetic comment -- @@ -210,11 +211,11 @@
}

private void refreshSimBadInfo() {
        final IccCard.State simState = mUpdateMonitor.getSimState();
        if (simState == IccCard.State.PUK_REQUIRED) {
mHeaderSimBad1.setText(R.string.lockscreen_sim_puk_locked_message);
mHeaderSimBad2.setText(R.string.lockscreen_sim_puk_locked_instructions);
        } else if (simState == IccCard.State.ABSENT) {
mHeaderSimBad1.setText(R.string.lockscreen_missing_sim_message);
mHeaderSimBad2.setVisibility(View.GONE);
//mHeaderSimBad2.setText(R.string.lockscreen_missing_sim_instructions);
//Synthetic comment -- @@ -226,7 +227,7 @@

private void refreshUnlockIntructions() {
if (mLockPatternUtils.isLockPatternEnabled()
                || mUpdateMonitor.getSimState() == IccCard.State.PIN_REQUIRED) {
mLockInstructions.setText(R.string.lockscreen_instructions_when_pattern_enabled);
} else {
mLockInstructions.setText(R.string.lockscreen_instructions_when_pattern_disabled);
//Synthetic comment -- @@ -293,8 +294,8 @@
}

private void refreshSimOkHeaders(CharSequence plmn, CharSequence spn) {
        final IccCard.State simState = mUpdateMonitor.getSimState();
        if (simState == IccCard.State.READY) {
if (plmn != null) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(plmn);
//Synthetic comment -- @@ -308,22 +309,22 @@
} else {
mHeaderSimOk2.setVisibility(View.GONE);
}
        } else if (simState == IccCard.State.PIN_REQUIRED) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(R.string.lockscreen_sim_locked_message);
mHeaderSimOk2.setVisibility(View.GONE);
        } else if (simState == IccCard.State.ABSENT) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(R.string.lockscreen_missing_sim_message_short);
mHeaderSimOk2.setVisibility(View.GONE);
        } else if (simState == IccCard.State.NETWORK_LOCKED) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(R.string.lockscreen_network_locked_message);
mHeaderSimOk2.setVisibility(View.GONE);
}
}

    public void onSimStateChanged(IccCard.State simState) {
mSimOk = isSimOk(simState);
refreshViewsWRTSimOk();
}
//Synthetic comment -- @@ -333,14 +334,14 @@
*   a special screen with the emergency call button and keep them from
*   doing anything else.
*/
    private boolean isSimOk(IccCard.State simState) {
boolean missingAndNotProvisioned = (!mUpdateMonitor.isDeviceProvisioned()
                && simState == IccCard.State.ABSENT);
        return !(missingAndNotProvisioned || simState == IccCard.State.PUK_REQUIRED);
}

public void onOrientationChange(boolean inPortrait) {
    mCallback.pokeWakelock();
}

public void onKeyboardChange(boolean isKeyboardOpen) {







