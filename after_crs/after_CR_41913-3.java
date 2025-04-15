/*Settings: Disable SIM lock UIs in Airplane mode/SIM removal

If the airplane mode is activated using the power
button and if the current activity is one of the
SIM lock activities, then the SIM lock UI is not
disabled. Since the framework doesn't completely
support SIM access in airplane mode, there are few
issues seen. Same applies to if the SIM is removed
when the current activity is SIM lock activity.

This patch disables/greys out the SIM lock UIs when
the airplane mode is activated using the power button
or upon SIM removal.

Change-Id:Ib3b311da98d34392482669d253b396d066fe5e3aAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 58530*/




//Synthetic comment -- diff --git a/src/com/android/settings/EditPinPreference.java b/src/com/android/settings/EditPinPreference.java
//Synthetic comment -- index 1877d43..164af55 100644

//Synthetic comment -- @@ -78,4 +78,11 @@
showDialog(null);
}
}

    public void cancelPinDialog() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}








//Synthetic comment -- diff --git a/src/com/android/settings/IccLockSettings.java b/src/com/android/settings/IccLockSettings.java
//Synthetic comment -- index ab12587..82801ac 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

import com.android.internal.telephony.Phone;
//Synthetic comment -- @@ -94,6 +95,7 @@
private static final int MSG_ENABLE_ICC_PIN_COMPLETE = 100;
private static final int MSG_CHANGE_ICC_PIN_COMPLETE = 101;
private static final int MSG_SIM_STATE_CHANGED = 102;
    private static final int MSG_AIRPLANE_MODE_CHANGED = 103;

// For replies from IccCard interface
private Handler mHandler = new Handler() {
//Synthetic comment -- @@ -106,6 +108,7 @@
case MSG_CHANGE_ICC_PIN_COMPLETE:
iccPinChanged(ar.exception == null);
break;
                case MSG_AIRPLANE_MODE_CHANGED:
case MSG_SIM_STATE_CHANGED:
updatePreferences();
break;
//Synthetic comment -- @@ -115,11 +118,13 @@
}
};

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
public void onReceive(Context context, Intent intent) {
final String action = intent.getAction();
if (TelephonyIntents.ACTION_SIM_STATE_CHANGED.equals(action)) {
mHandler.sendMessage(mHandler.obtainMessage(MSG_SIM_STATE_CHANGED));
            } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                mHandler.sendMessage(mHandler.obtainMessage(MSG_AIRPLANE_MODE_CHANGED));
}
}
};
//Synthetic comment -- @@ -185,7 +190,16 @@
}

private void updatePreferences() {
        boolean isAirplaneModeOn = Settings.System.getInt(getContentResolver(),
                                                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

        if (mPhone.getIccCard().hasIccCard() && !isAirplaneModeOn) {
            getPreferenceScreen().setEnabled(true);
            mPinToggle.setChecked(mPhone.getIccCard().getIccLockEnabled());
        } else {
            mPinDialog.cancelPinDialog();
            getPreferenceScreen().setEnabled(false);
        }
}

@Override
//Synthetic comment -- @@ -194,8 +208,10 @@

// ACTION_SIM_STATE_CHANGED is sticky, so we'll receive current state after this call,
// which will call updatePreferences().
        final IntentFilter filter = new IntentFilter();
        filter.addAction(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(mReceiver, filter);

if (mDialogState != OFF_MODE) {
showPinDialog();
//Synthetic comment -- @@ -208,7 +224,7 @@
@Override
protected void onPause() {
super.onPause();
        unregisterReceiver(mReceiver);
}

@Override








//Synthetic comment -- diff --git a/src/com/android/settings/SecuritySettings.java b/src/com/android/settings/SecuritySettings.java
//Synthetic comment -- index a12da3e..f2588b5 100644

//Synthetic comment -- @@ -22,15 +22,18 @@
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserId;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
//Synthetic comment -- @@ -39,7 +42,9 @@
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.widget.LockPatternUtils;

import java.util.ArrayList;
//Synthetic comment -- @@ -50,6 +55,8 @@
public class SecuritySettings extends SettingsPreferenceFragment
implements OnPreferenceChangeListener, DialogInterface.OnClickListener {

    private TelephonyManager mTelephonyManager;

// Lock Settings
private static final String KEY_UNLOCK_SET_OR_CHANGE = "unlock_set_or_change";
private static final String KEY_BIOMETRIC_WEAK_IMPROVE_MATCHING =
//Synthetic comment -- @@ -89,6 +96,25 @@
private DialogInterface mWarnInstallApps;
private CheckBoxPreference mPowerButtonInstantlyLocks;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (TelephonyIntents.ACTION_SIM_STATE_CHANGED.equals(action)) {
                String stateExtra = intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE);
                if (stateExtra != null &&
                        (IccCardConstants.INTENT_VALUE_ICC_UNKNOWN.equals(stateExtra) ||
                        IccCardConstants.INTENT_VALUE_ICC_NOT_READY.equals(stateExtra) ||
                        IccCardConstants.INTENT_VALUE_ICC_ABSENT.equals(stateExtra) ||
                        IccCardConstants.INTENT_VALUE_ICC_LOCKED.equals(stateExtra) ||
                        IccCardConstants.INTENT_VALUE_ICC_READY.equals(stateExtra))) {
                    createPreferenceHierarchy();
                }
            } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                createPreferenceHierarchy();
            }
        }
    };

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -96,6 +122,7 @@
mLockPatternUtils = new LockPatternUtils(getActivity());

mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

mChooseLockSettingsHelper = new ChooseLockSettingsHelper(getActivity());
}
//Synthetic comment -- @@ -202,16 +229,20 @@
addPreferencesFromResource(R.xml.security_settings_misc);

// Do not display SIM lock for devices without an Icc card
        boolean isAirplaneModeOn = Settings.System.getInt(getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

        if (mTelephonyManager != null) {
            if ((!mTelephonyManager.hasIccCard() || isAirplaneModeOn)) {
                root.removePreference(root.findPreference(KEY_SIM_LOCK));
            } else {
                 // Disable SIM lock if sim card is missing or unknown
                 if ((mTelephonyManager.getSimState() ==
TelephonyManager.SIM_STATE_ABSENT) ||
                        (mTelephonyManager.getSimState() ==
TelephonyManager.SIM_STATE_UNKNOWN)) {
                    root.findPreference(KEY_SIM_LOCK).setEnabled(false);
                }
}
}

//Synthetic comment -- @@ -361,6 +392,17 @@
if (mResetCredentials != null) {
mResetCredentials.setEnabled(state != KeyStore.State.UNINITIALIZED);
}

        IntentFilter filter = new IntentFilter();
        filter.addAction(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
}

@Override







