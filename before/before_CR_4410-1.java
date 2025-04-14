Extend ANDROID with CDMA mobile technology support

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
               Wolfgang Schmidt, wolfgang.schmidt@teleca.com
diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
index ab7c744..4cc2fe2 100644

@@ -34,7 +34,7 @@
import static android.provider.Telephony.Intents.EXTRA_SHOW_SPN;
import static android.provider.Telephony.Intents.EXTRA_SPN;
import static android.provider.Telephony.Intents.SPN_STRINGS_UPDATED_ACTION;
import com.android.internal.telephony.SimCard;
import com.android.internal.telephony.TelephonyIntents;
import android.util.Log;
import com.android.internal.R;
@@ -61,7 +61,7 @@

private final Context mContext;

    private SimCard.State mSimState = SimCard.State.READY;
private boolean mInPortrait;
private boolean mKeyboardOpen;

@@ -94,38 +94,39 @@


/**
     * When we receive a {@link com.android.internal.telephony.TelephonyIntents#ACTION_SIM_STATE_CHANGED} broadcast, and
     * then pass a result via our handler to {@link KeyguardUpdateMonitor#handleSimStateChange},
* we need a single object to pass to the handler.  This class helps decode
* the intent and provide a {@link SimCard.State} result. 
*/
private static class SimArgs {

        public final SimCard.State simState;

private SimArgs(Intent intent) {
if (!TelephonyIntents.ACTION_SIM_STATE_CHANGED.equals(intent.getAction())) {
throw new IllegalArgumentException("only handles intent ACTION_SIM_STATE_CHANGED");
}
            String stateExtra = intent.getStringExtra(SimCard.INTENT_KEY_SIM_STATE);
            if (SimCard.INTENT_VALUE_SIM_ABSENT.equals(stateExtra)) {
                this.simState = SimCard.State.ABSENT;
            } else if (SimCard.INTENT_VALUE_SIM_READY.equals(stateExtra)) {
                this.simState = SimCard.State.READY;
            } else if (SimCard.INTENT_VALUE_SIM_LOCKED.equals(stateExtra)) {
final String lockedReason = intent
                        .getStringExtra(SimCard.INTENT_KEY_LOCKED_REASON);
                if (SimCard.INTENT_VALUE_LOCKED_ON_PIN.equals(lockedReason)) {
                    this.simState = SimCard.State.PIN_REQUIRED;
                } else if (SimCard.INTENT_VALUE_LOCKED_ON_PUK.equals(lockedReason)) {
                    this.simState = SimCard.State.PUK_REQUIRED;
} else {
                    this.simState = SimCard.State.UNKNOWN;
}
            } else if (SimCard.INTENT_VALUE_LOCKED_NETWORK.equals(stateExtra)) {
                this.simState = SimCard.State.NETWORK_LOCKED;
} else {
                this.simState = SimCard.State.UNKNOWN;
}
}

@@ -195,7 +196,7 @@
mKeyboardOpen = queryKeyboardOpen();

// take a guess to start
        mSimState = SimCard.State.READY;
mDevicePluggedIn = true;
mBatteryLevel = 100;

@@ -317,14 +318,14 @@
* Handle {@link #MSG_SIM_STATE_CHANGE}
*/
private void handleSimStateChange(SimArgs simArgs) {
        final SimCard.State state = simArgs.simState;

if (DEBUG) {
Log.d(TAG, "handleSimStateChange: intentValue = " + simArgs + " "
+ "state resolved to " + state.toString());
}

        if (state != SimCard.State.UNKNOWN && state != mSimState) {
mSimState = state;
for (int i = 0; i < mSimStateCallbacks.size(); i++) {
mSimStateCallbacks.get(i).onSimStateChanged(state);
@@ -461,7 +462,7 @@
* Callback to notify of sim state change.
*/
interface SimStateCallback {
        void onSimStateChanged(SimCard.State simState);
}

/**
@@ -489,7 +490,7 @@
mSimStateCallbacks.add(callback);
}

    public SimCard.State getSimState() {
return mSimState;
}

@@ -499,7 +500,7 @@
* broadcast from the telephony code.
*/
public void reportSimPinUnlocked() {
        mSimState = SimCard.State.READY;
}

public boolean isInPortrait() {








diff --git a/phone/com/android/internal/policy/impl/KeyguardViewMediator.java b/phone/com/android/internal/policy/impl/KeyguardViewMediator.java
index ac816b0..50d4f20 100644

@@ -36,7 +36,7 @@
import android.view.KeyEvent;
import android.view.WindowManagerImpl;
import android.view.WindowManagerPolicy;
import com.android.internal.telephony.SimCard;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.widget.LockPatternUtils;

@@ -88,7 +88,8 @@

private final static String TAG = "KeyguardViewMediator";

    private static final String DELAYED_KEYGUARD_ACTION = "com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD";

// used for handler messages
private static final int TIMEOUT = 1;
@@ -289,7 +290,8 @@
0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, when,
sender);
                if (DEBUG) Log.d(TAG, "setting alarm to turn off keyguard, seq = " + mDelayedShowingSequence);
} else {
doKeyguard();
}
@@ -438,8 +440,8 @@

// if the setup wizard hasn't run yet, don't show
final boolean provisioned = mUpdateMonitor.isDeviceProvisioned();
            final SimCard.State state = mUpdateMonitor.getSimState();
            final boolean lockedOrMissing = state.isPinLocked() || (state == SimCard.State.ABSENT);
if (!lockedOrMissing && !provisioned) {
if (DEBUG) Log.d(TAG, "doKeyguard: not showing because device isn't provisioned"
+ " and the sim is not locked or missing");
@@ -553,7 +555,7 @@
}

/** {@inheritDoc} */
    public void onSimStateChanged(SimCard.State simState) {
if (DEBUG) Log.d(TAG, "onSimStateChanged: " + simState);

switch (simState) {
@@ -562,7 +564,7 @@
// gone through setup wizard
if (!mUpdateMonitor.isDeviceProvisioned()) {
if (!isShowing()) {
                        if (DEBUG) Log.d(TAG, "INTENT_VALUE_SIM_ABSENT and keygaurd isn't showing, we need "
+ "to show the keyguard since the device isn't provisioned yet.");
doKeyguard();
} else {
@@ -573,7 +575,7 @@
case PIN_REQUIRED:
case PUK_REQUIRED:
if (!isShowing()) {
                    if (DEBUG) Log.d(TAG, "INTENT_VALUE_SIM_LOCKED and keygaurd isn't showing, we need "
+ "to show the keyguard so the user can enter their sim pin");
doKeyguard();
} else {
@@ -716,10 +718,8 @@
*/
private Handler mHandler = new Handler() {
@Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
case TIMEOUT:
handleTimeout(msg.arg1);
return ;








diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java
index 66a4c4e..5a0d03f 100644

@@ -20,7 +20,7 @@
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import com.android.internal.telephony.SimCard;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
@@ -122,7 +122,7 @@
private boolean stuckOnLockScreenBecauseSimMissing() {
return mRequiresSim
&& (!mUpdateMonitor.isDeviceProvisioned())
                && (mUpdateMonitor.getSimState() == SimCard.State.ABSENT);
}

/**
@@ -159,9 +159,9 @@
}

public void goToUnlockScreen() {
                final SimCard.State simState = mUpdateMonitor.getSimState();
if (stuckOnLockScreenBecauseSimMissing()
                         || (simState == SimCard.State.PUK_REQUIRED)){
// stuck on lock screen when sim missing or puk'd
return;
}
@@ -211,7 +211,8 @@
mUpdateMonitor.reportFailedAttempt();
final int failedAttempts = mUpdateMonitor.getFailedAttempts();
if (failedAttempts ==
                        (LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET - LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)) {
showAlmostAtAccountLoginDialog();
} else if (failedAttempts >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET) {
mLockPatternUtils.setPermanentlyLocked(true);
@@ -299,7 +300,7 @@
public void wakeWhenReadyTq(int keyCode) {
if (DEBUG) Log.d(TAG, "onWakeKey");
if (keyCode == KeyEvent.KEYCODE_MENU && isSecure() && (mMode == Mode.LockScreen)
                && (mUpdateMonitor.getSimState() != SimCard.State.PUK_REQUIRED)) {
if (DEBUG) Log.d(TAG, "switching screens to unlock screen because wake key was MENU");
updateScreen(Mode.UnlockScreen);
getCallback().pokeWakelock();
@@ -337,8 +338,8 @@
if (unlockMode == UnlockMode.Pattern) {
return mLockPatternUtils.isLockPatternEnabled();
} else if (unlockMode == UnlockMode.SimPin) {
            return mUpdateMonitor.getSimState() == SimCard.State.PIN_REQUIRED
                        || mUpdateMonitor.getSimState() == SimCard.State.PUK_REQUIRED;
} else if (unlockMode == UnlockMode.Account) {
return true;
} else {
@@ -451,8 +452,8 @@
* the lock screen (lock or unlock).
*/
private Mode getInitialMode() {
        final SimCard.State simState = mUpdateMonitor.getSimState();
        if (stuckOnLockScreenBecauseSimMissing() || (simState == SimCard.State.PUK_REQUIRED)) {
return Mode.LockScreen;
} else if (mUpdateMonitor.isKeyboardOpen() && isSecure()) {
return Mode.UnlockScreen;
@@ -465,8 +466,8 @@
* Given the current state of things, what should the unlock screen be?
*/
private UnlockMode getUnlockMode() {
        final SimCard.State simState = mUpdateMonitor.getSimState();
        if (simState == SimCard.State.PIN_REQUIRED || simState == SimCard.State.PUK_REQUIRED) {
return UnlockMode.SimPin;
} else {
return mLockPatternUtils.isPermanentlyLocked() ?
@@ -497,7 +498,8 @@
int timeoutInSeconds = (int) LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS / 1000;
String message = mContext.getString(
R.string.lockscreen_failed_attempts_almost_glogin,
                LockPatternUtils.FAILED_ATTEMPTS_BEFORE_RESET - LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT,
LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT,
timeoutInSeconds);
final AlertDialog dialog = new AlertDialog.Builder(mContext)








diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardViewProperties.java
index 697c038..2ce7f64 100644

@@ -19,7 +19,7 @@
import com.android.internal.widget.LockPatternUtils;

import android.content.Context;
import com.android.internal.telephony.SimCard;

/**
* Knows how to create a lock pattern keyguard view, and answer questions about
@@ -57,9 +57,9 @@
}

private boolean isSimPinSecure() {
        final SimCard.State simState = mUpdateMonitor.getSimState();
        return (simState == SimCard.State.PIN_REQUIRED || simState == SimCard.State.PUK_REQUIRED
            || simState == SimCard.State.ABSENT);
}

}








diff --git a/phone/com/android/internal/policy/impl/LockScreen.java b/phone/com/android/internal/policy/impl/LockScreen.java
index c717cc3..0a9a4cd 100644

@@ -29,7 +29,7 @@
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.telephony.SimCard;

import java.util.Date;

@@ -193,7 +193,8 @@
final View view = mOnlyVisibleWhenSimNotOk[i];
view.setVisibility(View.GONE);
}
            refreshSimOkHeaders(mUpdateMonitor.getTelephonyPlmn(), mUpdateMonitor.getTelephonySpn());
refreshAlarmDisplay();
refreshBatteryDisplay();
} else {
@@ -210,11 +211,11 @@
}

private void refreshSimBadInfo() {
        final SimCard.State simState = mUpdateMonitor.getSimState();
        if (simState == SimCard.State.PUK_REQUIRED) {
mHeaderSimBad1.setText(R.string.lockscreen_sim_puk_locked_message);
mHeaderSimBad2.setText(R.string.lockscreen_sim_puk_locked_instructions);
        } else if (simState == SimCard.State.ABSENT) {
mHeaderSimBad1.setText(R.string.lockscreen_missing_sim_message);
mHeaderSimBad2.setVisibility(View.GONE);
//mHeaderSimBad2.setText(R.string.lockscreen_missing_sim_instructions);
@@ -226,7 +227,7 @@

private void refreshUnlockIntructions() {
if (mLockPatternUtils.isLockPatternEnabled()
                || mUpdateMonitor.getSimState() == SimCard.State.PIN_REQUIRED) {
mLockInstructions.setText(R.string.lockscreen_instructions_when_pattern_enabled);
} else {
mLockInstructions.setText(R.string.lockscreen_instructions_when_pattern_disabled);
@@ -293,8 +294,8 @@
}

private void refreshSimOkHeaders(CharSequence plmn, CharSequence spn) {
        final SimCard.State simState = mUpdateMonitor.getSimState();
        if (simState == SimCard.State.READY) {
if (plmn != null) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(plmn);
@@ -308,22 +309,22 @@
} else {
mHeaderSimOk2.setVisibility(View.GONE);
}
        } else if (simState == SimCard.State.PIN_REQUIRED) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(R.string.lockscreen_sim_locked_message);
mHeaderSimOk2.setVisibility(View.GONE);
        } else if (simState == SimCard.State.ABSENT) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(R.string.lockscreen_missing_sim_message_short);
mHeaderSimOk2.setVisibility(View.GONE);
        } else if (simState == SimCard.State.NETWORK_LOCKED) {
mHeaderSimOk1.setVisibility(View.VISIBLE);
mHeaderSimOk1.setText(R.string.lockscreen_network_locked_message);
mHeaderSimOk2.setVisibility(View.GONE);
}
}

    public void onSimStateChanged(SimCard.State simState) {
mSimOk = isSimOk(simState);
refreshViewsWRTSimOk();
}
@@ -333,14 +334,14 @@
*   a special screen with the emergency call button and keep them from
*   doing anything else.
*/
    private boolean isSimOk(SimCard.State simState) {
boolean missingAndNotProvisioned = (!mUpdateMonitor.isDeviceProvisioned()
                && simState == SimCard.State.ABSENT);
        return !(missingAndNotProvisioned || simState == SimCard.State.PUK_REQUIRED);
}

public void onOrientationChange(boolean inPortrait) {
	mCallback.pokeWakelock();
}

public void onKeyboardChange(boolean isKeyboardOpen) {







