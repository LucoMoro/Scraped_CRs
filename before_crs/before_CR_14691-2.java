/*Fix window leak problems in settings.

There were window leak in the settings application.
These leak happens when an AlertDialog displays, rotate the phone,
then it would cause window leak.

Change-Id:I914897bf657933efea72eeea66076dc288098420*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index e097854..a3ba6f5 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.settings;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
//Synthetic comment -- @@ -51,6 +52,7 @@
private static final int MENU_DELETE = Menu.FIRST;
private static final int MENU_SAVE = Menu.FIRST + 1;
private static final int MENU_CANCEL = Menu.FIRST + 2;

private static String sNotSet;
private EditTextPreference mName;
//Synthetic comment -- @@ -347,19 +349,8 @@
String mcc = checkNotSet(mMcc.getText());
String mnc = checkNotSet(mMnc.getText());

        String errorMsg = null;
        if (name.length() < 1) {
            errorMsg = mRes.getString(R.string.error_name_empty);
        } else if (apn.length() < 1) {
            errorMsg = mRes.getString(R.string.error_apn_empty);
        } else if (mcc.length() != 3) {
            errorMsg = mRes.getString(R.string.error_mcc_not3);
        } else if ((mnc.length() & 0xFFFE) != 2) {
            errorMsg = mRes.getString(R.string.error_mnc_not23);
        }

        if (errorMsg != null && !force) {
            showErrorMessage(errorMsg);
return false;
}

//Synthetic comment -- @@ -414,12 +405,57 @@
return true;
}

    private void showErrorMessage(String message) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.error_title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show();
}

private void deleteApn() {








//Synthetic comment -- diff --git a/src/com/android/settings/LanguageSettings.java b/src/com/android/settings/LanguageSettings.java
//Synthetic comment -- index 1b9f0c1..d115b90 100644

//Synthetic comment -- @@ -53,6 +53,8 @@

private String mLastInputMethodId;
private String mLastTickedInputMethodId;

static public String getInputMethodIdFromKey(String key) {
return key;
//Synthetic comment -- @@ -229,29 +231,35 @@
if (selImi == null) {
return super.onPreferenceTreeClick(preferenceScreen, preference);
}
                AlertDialog d = (new AlertDialog.Builder(this))
                        .setTitle(android.R.string.dialog_alert_title)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(getString(R.string.ime_security_warning,
                                selImi.getServiceInfo().applicationInfo.loadLabel(
                                        getPackageManager())))
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        chkPref.setChecked(true);
                                        mLastTickedInputMethodId = id;
                                    }
                            
                        })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                            
                        })
                        .create();
                d.show();
} else if (id.equals(mLastTickedInputMethodId)) {
mLastTickedInputMethodId = null;
}
//Synthetic comment -- @@ -271,4 +279,13 @@
return super.onPreferenceTreeClick(preferenceScreen, preference);
}

}








//Synthetic comment -- diff --git a/src/com/android/settings/MasterClear.java b/src/com/android/settings/MasterClear.java
//Synthetic comment -- index fd4a411..604a628 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//Synthetic comment -- @@ -43,6 +44,7 @@
public class MasterClear extends Activity {

private static final int KEYGUARD_REQUEST = 55;

private LayoutInflater mInflater;
private LockPatternUtils mLockUtils;
//Synthetic comment -- @@ -85,10 +87,7 @@
* the implementation of masterClear() may have returned instead
* of resetting the device.
*/
                new AlertDialog.Builder(MasterClear.this)
                        .setMessage(getText(R.string.master_clear_failed))
                        .setPositiveButton(getText(android.R.string.ok), null)
                        .show();
}
};

//Synthetic comment -- @@ -200,4 +199,17 @@
establishInitialState();
}

}








//Synthetic comment -- diff --git a/src/com/android/settings/ProxySelector.java b/src/com/android/settings/ProxySelector.java
//Synthetic comment -- index 80fe3c9..66c81c6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Proxy;
//Synthetic comment -- @@ -73,6 +74,7 @@
HOSTNAME_PATTERN = Pattern.compile(HOSTNAME_REGEXP);
}


public void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -84,13 +86,32 @@
populateFields(false);
}

    protected void showError(int error) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.proxy_error)
                .setMessage(error)
                .setPositiveButton(R.string.proxy_error_dismiss, null)
                .show();
}

void initView() {
//Synthetic comment -- @@ -188,7 +209,7 @@

int result = validate(hostname, portStr);
if (result > 0) {
            showError(result);
return false;
}









//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index ce236fd..aebbc97 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
//Synthetic comment -- @@ -96,6 +97,8 @@
private static final int MENU_ITEM_TOGGLE_DATA  = 5;
private static final int MENU_ITEM_TOGGLE_DATA_ON_BOOT = 6;

private TextView mDeviceId; //DeviceId is the IMEI in GSM and the MEID in CDMA
private TextView number;
private TextView callState;
//Synthetic comment -- @@ -988,9 +991,7 @@
}

private void displayQxdmEnableResult() {
        String status = mQxdmLogEnabled ? "Start QXDM Log" : "Stop QXDM Log";

        new AlertDialog.Builder(this).setMessage(status).show();

mHandler.postDelayed(
new Runnable() {
//Synthetic comment -- @@ -1000,6 +1001,27 @@
}, 2000);
}

private MenuItem.OnMenuItemClickListener mViewADNCallback = new MenuItem.OnMenuItemClickListener() {
public boolean onMenuItemClick(MenuItem item) {
Intent intent = new Intent(Intent.ACTION_VIEW);








//Synthetic comment -- diff --git a/src/com/android/settings/SettingsSafetyLegalActivity.java b/src/com/android/settings/SettingsSafetyLegalActivity.java
//Synthetic comment -- index 619dc94..4e4620d 100644

//Synthetic comment -- @@ -40,6 +40,8 @@

private WebView mWebView;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -81,14 +83,31 @@
}

private void showErrorAndFinish(String url) {
        new AlertDialog.Builder(this)
                .setMessage(getResources()
                        .getString(R.string.settings_safetylegal_activity_unreachable, url))
                .setTitle(R.string.settings_safetylegal_activity_title)
                .setPositiveButton(android.R.string.ok, this)
                .setOnCancelListener(this)
                .setCancelable(true)
                .show();
}

@Override








//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/CachedBluetoothDevice.java b/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
//Synthetic comment -- index 57bffa9..12b7e0b 100644

//Synthetic comment -- @@ -71,6 +71,8 @@

private final LocalBluetoothManager mLocalManager;

private List<Callback> mCallbacks = new ArrayList<Callback>();

/**
//Synthetic comment -- @@ -367,12 +369,29 @@
}
};

        new AlertDialog.Builder(context)
                .setTitle(getName())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, disconnectListener)
                .setNegativeButton(android.R.string.cancel, null)
                .show();
}

public void connect() {








//Synthetic comment -- diff --git a/src/com/android/settings/vpn/VpnEditor.java b/src/com/android/settings/vpn/VpnEditor.java
//Synthetic comment -- index 497f4bf..349befb 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.settings.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.vpn.L2tpIpsecProfile;
//Synthetic comment -- @@ -44,6 +45,7 @@
public class VpnEditor extends PreferenceActivity {
private static final int MENU_SAVE = Menu.FIRST;
private static final int MENU_CANCEL = Menu.FIRST + 1;
private static final String KEY_PROFILE = "profile";
private static final String KEY_ORIGINAL_PROFILE_NAME = "orig_profile_name";

//Synthetic comment -- @@ -98,7 +100,7 @@

case MENU_CANCEL:
if (profileChanged()) {
                    showCancellationConfirmDialog();
} else {
finish();
}
//Synthetic comment -- @@ -171,21 +173,39 @@
}
}

    private void showCancellationConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle(android.R.string.dialog_alert_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(mAddingProfile
                        ? R.string.vpn_confirm_add_profile_cancellation
                        : R.string.vpn_confirm_edit_profile_cancellation)
                .setPositiveButton(R.string.vpn_yes_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int w) {
                                finish();
                            }
                        })
                .setNegativeButton(R.string.vpn_mistake_button, null)
                .show();
}

private VpnProfile getProfile() {








//Synthetic comment -- diff --git a/src/com/android/settings/wifi/AccessPointDialog.java b/src/com/android/settings/wifi/AccessPointDialog.java
//Synthetic comment -- index 4804d78..9c6f926 100644

//Synthetic comment -- @@ -102,6 +102,7 @@
private TextView mPasswordText;
private EditText mPasswordEdit;
private CheckBox mShowPasswordCheckBox;

// Enterprise fields
private TextView mEapText;
//Synthetic comment -- @@ -542,12 +543,15 @@
(mState.security != null) &&
!mState.security.equals(AccessPointState.OPEN) &&
!mState.isEnterprise()) {
          new AlertDialog.Builder(getContext())
                  .setTitle(R.string.error_title)
                  .setIcon(android.R.drawable.ic_dialog_alert)
                  .setMessage(R.string.wifi_password_incorrect_error)
                  .setPositiveButton(android.R.string.ok, null)
                  .show();
return;
}

//Synthetic comment -- @@ -556,6 +560,16 @@
}
}

private void handleSave() {
replaceStateWithWifiLayerInstance();








