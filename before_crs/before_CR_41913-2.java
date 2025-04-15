/*Settings: Modify UI behavior on SIM removal/insert

Currently, sim lock preference screen is not
getting enabled/disabled based on the sim state.
This results in sim lock settings shown even when
there is no sim.

This patch fixes the issue by enabling/removing
the sim lock preference based on the sim state
changes.

Change-Id:Ib3b311da98d34392482669d253b396d066fe5e3aAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18674*/
//Synthetic comment -- diff --git a/src/com/android/settings/EditPinPreference.java b/src/com/android/settings/EditPinPreference.java
//Synthetic comment -- index 1877d43..164af55 100644

//Synthetic comment -- @@ -78,4 +78,11 @@
showDialog(null);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/settings/IccLockSettings.java b/src/com/android/settings/IccLockSettings.java
//Synthetic comment -- index ab12587..e296e27 100644

//Synthetic comment -- @@ -185,7 +185,13 @@
}

private void updatePreferences() {
        mPinToggle.setChecked(mPhone.getIccCard().getIccLockEnabled());
}

@Override








//Synthetic comment -- diff --git a/src/com/android/settings/SecuritySettings.java b/src/com/android/settings/SecuritySettings.java
//Synthetic comment -- index a12da3e..68306fb 100644

//Synthetic comment -- @@ -22,15 +22,18 @@
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserId;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
//Synthetic comment -- @@ -39,7 +42,9 @@
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.internal.widget.LockPatternUtils;

import java.util.ArrayList;
//Synthetic comment -- @@ -50,6 +55,8 @@
public class SecuritySettings extends SettingsPreferenceFragment
implements OnPreferenceChangeListener, DialogInterface.OnClickListener {

// Lock Settings
private static final String KEY_UNLOCK_SET_OR_CHANGE = "unlock_set_or_change";
private static final String KEY_BIOMETRIC_WEAK_IMPROVE_MATCHING =
//Synthetic comment -- @@ -89,6 +96,23 @@
private DialogInterface mWarnInstallApps;
private CheckBoxPreference mPowerButtonInstantlyLocks;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -96,6 +120,7 @@
mLockPatternUtils = new LockPatternUtils(getActivity());

mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

mChooseLockSettingsHelper = new ChooseLockSettingsHelper(getActivity());
}
//Synthetic comment -- @@ -202,17 +227,8 @@
addPreferencesFromResource(R.xml.security_settings_misc);

// Do not display SIM lock for devices without an Icc card
        TelephonyManager tm = TelephonyManager.getDefault();
        if (!tm.hasIccCard()) {
root.removePreference(root.findPreference(KEY_SIM_LOCK));
        } else {
            // Disable SIM lock if sim card is missing or unknown
            if ((TelephonyManager.getDefault().getSimState() ==
                                 TelephonyManager.SIM_STATE_ABSENT) ||
                (TelephonyManager.getDefault().getSimState() ==
                                 TelephonyManager.SIM_STATE_UNKNOWN)) {
                root.findPreference(KEY_SIM_LOCK).setEnabled(false);
            }
}

// Show password
//Synthetic comment -- @@ -361,6 +377,15 @@
if (mResetCredentials != null) {
mResetCredentials.setEnabled(state != KeyStore.State.UNINITIALIZED);
}
}

@Override







