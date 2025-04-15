/*Change settings dialogs from unmanged to managed

Unmanaged dialogs were changed to managed dialogs so they do not
disappear after an orientation change. The "Allow Unknown Sources",
"USB Debugging", and "Accessibility" dialogs are affected by this.

Change-Id:I14756e31ac018691a5c491c3746200b2d6feefad*/




//Synthetic comment -- diff --git a/src/com/android/settings/AccessibilitySettings.java b/src/com/android/settings/AccessibilitySettings.java
//Synthetic comment -- index d78d2d8..5325e12 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
//Synthetic comment -- @@ -57,14 +58,23 @@
private static final String ACCESSIBILITY_SERVICES_CATEGORY =
"accessibility_services_category";

    private static final String SERVICE_PREFERENCE_KEY =
        "service_preference_key";

private static final String POWER_BUTTON_CATEGORY =
"power_button_category";

private final String POWER_BUTTON_ENDS_CALL_CHECKBOX =
"power_button_ends_call";

    private static final int ACCESSIBILITY_DIALOG_ID = 1;

    private static final int ACCESSIBILITY_SERVICE_DIALOG_ID = 2;

private CheckBoxPreference mToggleCheckBox;

    private CheckBoxPreference mServiceCheckBox;

private PreferenceCategory mPowerButtonCategory;
private CheckBoxPreference mPowerButtonEndsCallCheckBox;

//Synthetic comment -- @@ -183,25 +193,92 @@
public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
final String key = preference.getKey();

        if (preference instanceof CheckBoxPreference) {
            if (TOGGLE_ACCESSIBILITY_SERVICE_CHECKBOX.equals(key)) {
                boolean isChecked = ((CheckBoxPreference) preference).isChecked();
                handleEnableAccessibilityStateChange((CheckBoxPreference) preference);
            } else if (POWER_BUTTON_ENDS_CALL_CHECKBOX.equals(key)) {
                boolean isChecked = ((CheckBoxPreference) preference).isChecked();
                // The checkbox is labeled "Power button ends call"; thus the in-call
                // Power button behavior is INCALL_POWER_BUTTON_BEHAVIOR_HANGUP if
                // checked, and INCALL_POWER_BUTTON_BEHAVIOR_SCREEN_OFF if unchecked.
                Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR,
                        (isChecked ? Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_HANGUP
                                : Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_SCREEN_OFF));
            } else if (preference instanceof CheckBoxPreference) {
                handleEnableAccessibilityServiceStateChange((CheckBoxPreference) preference);
            }
}

return super.onPreferenceTreeClick(preferenceScreen, preference);
}

    public void onSaveInstanceState(Bundle outState) {
        if(mServiceCheckBox != null) {
            outState.putCharSequence(SERVICE_PREFERENCE_KEY, mServiceCheckBox.getKey());
        }
    }

    public void onRestoreInstanceState(Bundle state) {
        if(state.containsKey(SERVICE_PREFERENCE_KEY)) {
            mServiceCheckBox = (CheckBoxPreference)findPreference(state.getCharSequence(SERVICE_PREFERENCE_KEY));
        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog;
        switch (id) {

            case ACCESSIBILITY_DIALOG_ID:
                dialog = (new AlertDialog.Builder(this)).setTitle(
                        android.R.string.dialog_alert_title).setIcon(
                        android.R.drawable.ic_dialog_alert).setMessage(
                        getString(R.string.accessibility_service_disable_warning)).setCancelable(
                        true).setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.Secure.putInt(getContentResolver(),
                                        Settings.Secure.ACCESSIBILITY_ENABLED, 0);
                                setAccessibilityServicePreferencesState(false);
                                mToggleCheckBox.setChecked(false);
                            }
                        }).setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mToggleCheckBox.setChecked(true);
                            }
                        }).create();
                break;

            case ACCESSIBILITY_SERVICE_DIALOG_ID:
                dialog = (new AlertDialog.Builder(this))
                        .setTitle(android.R.string.dialog_alert_title)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(
                                getString(
                                        R.string.accessibility_service_security_warning,
                                        mAccessibilityServices.get(mServiceCheckBox.getKey()).applicationInfo
                                                .loadLabel(getPackageManager()))).setCancelable(
                                true).setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mServiceCheckBox.setChecked(true);
                                        persistEnabledAccessibilityServices();
                                    }
                                }).setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mServiceCheckBox.setChecked(false);
                                    }
                                }).create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

/**
* Handles the change of the accessibility enabled setting state.
*
//Synthetic comment -- @@ -213,28 +290,7 @@
Settings.Secure.ACCESSIBILITY_ENABLED, 1);
setAccessibilityServicePreferencesState(true);
} else {
            showDialog(ACCESSIBILITY_DIALOG_ID);
}
}

//Synthetic comment -- @@ -245,29 +301,7 @@
*/
private void handleEnableAccessibilityServiceStateChange(CheckBoxPreference preference) {
if (preference.isChecked()) {
            showDialog(ACCESSIBILITY_SERVICE_DIALOG_ID);
} else {
persistEnabledAccessibilityServices();
}








//Synthetic comment -- diff --git a/src/com/android/settings/ApplicationSettings.java b/src/com/android/settings/ApplicationSettings.java
//Synthetic comment -- index a919ae8..b65f0e2 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
//Synthetic comment -- @@ -28,8 +29,7 @@
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

public class ApplicationSettings extends PreferenceActivity {

private static final String KEY_TOGGLE_INSTALL_APPLICATIONS = "toggle_install_applications";
private static final String KEY_APP_INSTALL_LOCATION = "app_install_location";
//Synthetic comment -- @@ -39,6 +39,7 @@
private static final int APP_INSTALL_AUTO = 0;
private static final int APP_INSTALL_DEVICE = 1;
private static final int APP_INSTALL_SDCARD = 2;
    private static final int WARN_INSTALL_DIALOG_ID = 3;

private static final String APP_INSTALL_DEVICE_ID = "device";
private static final String APP_INSTALL_SDCARD_ID = "sdcard";
//Synthetic comment -- @@ -48,8 +49,6 @@

private ListPreference mInstallLocation;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -102,19 +101,11 @@
}

@Override
public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
if (preference == mToggleAppInstallation) {
if (mToggleAppInstallation.isChecked()) {
mToggleAppInstallation.setChecked(false);
                showDialog(WARN_INSTALL_DIALOG_ID);
} else {
setNonMarketAppsAllowed(false);
}
//Synthetic comment -- @@ -123,13 +114,6 @@
return super.onPreferenceTreeClick(preferenceScreen, preference);
}

private void setNonMarketAppsAllowed(boolean enabled) {
// Change the system setting
Settings.Secure.putInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 
//Synthetic comment -- @@ -156,13 +140,26 @@
}
}

    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog;
        switch (id) {
            case WARN_INSTALL_DIALOG_ID:
                dialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.error_title))
                        .setIcon(com.android.internal.R.drawable.ic_dialog_alert)
                        .setMessage(getResources().getString(R.string.install_all_warning))
                        .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setNonMarketAppsAllowed(true);
                                mToggleAppInstallation.setChecked(true);
                            }
                        }).setNegativeButton(android.R.string.no, null)
                        .create();
                break;
            default:
                dialog = null;
        }
        return dialog;
}
}








//Synthetic comment -- diff --git a/src/com/android/settings/DevelopmentSettings.java b/src/com/android/settings/DevelopmentSettings.java
//Synthetic comment -- index b0e5c07..d00eb2a 100644

//Synthetic comment -- @@ -32,22 +32,17 @@
/*
* Displays preferences for application developers.
*/
public class DevelopmentSettings extends PreferenceActivity {

private static final String ENABLE_ADB = "enable_adb";
private static final String KEEP_SCREEN_ON = "keep_screen_on";
private static final String ALLOW_MOCK_LOCATION = "allow_mock_location";
    private static final int USB_DEBUG_DIALOG_ID = 1;

private CheckBoxPreference mEnableAdb;
private CheckBoxPreference mKeepScreenOn;
private CheckBoxPreference mAllowMockLocation;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -80,16 +75,8 @@

if (preference == mEnableAdb) {
if (mEnableAdb.isChecked()) {
                mEnableAdb.setChecked(false);
                showDialog(USB_DEBUG_DIALOG_ID);
} else {
Settings.Secure.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
}
//Synthetic comment -- @@ -105,32 +92,27 @@
return false;
}

    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog;
        switch (id) {
            case USB_DEBUG_DIALOG_ID:
                dialog = new AlertDialog.Builder(this).setMessage(
                        getResources().getString(R.string.adb_warning_message))
                        .setTitle(R.string.adb_warning_title)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Settings.Secure.putInt(getContentResolver(),
                                                Settings.Secure.ADB_ENABLED, 1);
                                        mEnableAdb.setChecked(true);
                                    }
                                }).setNegativeButton(android.R.string.no, null)
                                .create();
                break;
            default:
                dialog = null;
}
        return dialog;
}
}







