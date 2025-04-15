/*Settings: Modify the UI behavior on SIM removal

When there is no SIM, "Set up SIM card lock" screen/view
will be disabled.

Change-Id:Ib3b311da98d34392482669d253b396d066fe5e3aAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18674*/




//Synthetic comment -- diff --git a/src/com/android/settings/IccLockSettings.java b/src/com/android/settings/IccLockSettings.java
//Synthetic comment -- index 755be83..9f6c3fc 100644

//Synthetic comment -- @@ -29,6 +29,9 @@
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.Phone;
//Synthetic comment -- @@ -89,6 +92,7 @@
private CheckBoxPreference mPinToggle;

private Resources mRes;
    private TelephonyManager mTelephonyManager;

// For async handler to identify request type
private static final int MSG_ENABLE_ICC_PIN_COMPLETE = 100;
//Synthetic comment -- @@ -146,6 +150,9 @@
return;
}

        mTelephonyManager = (TelephonyManager) (getApplicationContext().getSystemService(
                                                            Context.TELEPHONY_SERVICE));

addPreferencesFromResource(R.xml.sim_lock_settings);

mPinDialog = (EditPinPreference) findPreference(PIN_DIALOG);
//Synthetic comment -- @@ -197,6 +204,14 @@
final IntentFilter filter = new IntentFilter(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
registerReceiver(mSimStateReceiver, filter);

        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);
        if (!mTelephonyManager.hasIccCard()) {
            finish();
            return;
        }

       mPinToggle.setChecked(mPhone.getIccCard().getIccLockEnabled());

if (mDialogState != OFF_MODE) {
showPinDialog();
} else {
//Synthetic comment -- @@ -212,6 +227,20 @@
}

@Override
    protected void onStop() {
        super.onStop();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onServiceStateChanged(ServiceState state) {
            if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
                finish();
        }
    };

    @Override
protected void onSaveInstanceState(Bundle out) {
// Need to store this state for slider open/close
// There is one case where the dialog is popped up by the preference








//Synthetic comment -- diff --git a/src/com/android/settings/SecuritySettings.java b/src/com/android/settings/SecuritySettings.java
//Synthetic comment -- index a12da3e..3e0e932 100644

//Synthetic comment -- @@ -31,11 +31,14 @@
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.security.KeyStore;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

//Synthetic comment -- @@ -50,6 +53,8 @@
public class SecuritySettings extends SettingsPreferenceFragment
implements OnPreferenceChangeListener, DialogInterface.OnClickListener {

    private TelephonyManager mTelephonyManager;

// Lock Settings
private static final String KEY_UNLOCK_SET_OR_CHANGE = "unlock_set_or_change";
private static final String KEY_BIOMETRIC_WEAK_IMPROVE_MATCHING =
//Synthetic comment -- @@ -96,6 +101,7 @@
mLockPatternUtils = new LockPatternUtils(getActivity());

mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

mChooseLockSettingsHelper = new ChooseLockSettingsHelper(getActivity());
}
//Synthetic comment -- @@ -337,6 +343,13 @@
// depend on others...
createPreferenceHierarchy();

        PreferenceCategory simLockPrefCategory = (PreferenceCategory) findPreference(KEY_SIM_LOCK);
        if (simLockPrefCategory != null) {
            simLockPrefCategory.setEnabled(mTelephonyManager.hasIccCard());
        }

        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);

final LockPatternUtils lockPatternUtils = mChooseLockSettingsHelper.utils();
if (mBiometricWeakLiveliness != null) {
mBiometricWeakLiveliness.setChecked(
//Synthetic comment -- @@ -364,6 +377,22 @@
}

@Override
    public void onStop() {
        super.onStop();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onServiceStateChanged(ServiceState state) {
            PreferenceCategory simLockPrefCategory = (PreferenceCategory) findPreference(KEY_SIM_LOCK);
            if (simLockPrefCategory != null) {
                simLockPrefCategory.setEnabled(mTelephonyManager.hasIccCard());
            }
        }
    };

    @Override
public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
final String key = preference.getKey();








