/*Settings: Use ICS alert drawable

Point to the correct holo alert drawable.

Change-Id:Ia15aaa028c8371cb2478baeecd2da31520c43a5d*/




//Synthetic comment -- diff --git a/src/com/android/settings/AccessibilitySettings.java b/src/com/android/settings/AccessibilitySettings.java
//Synthetic comment -- index 827af13..cc446c5 100644

//Synthetic comment -- @@ -812,7 +812,7 @@
}
return new AlertDialog.Builder(getActivity())
.setTitle(title)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(message)
.setCancelable(true)
.setPositiveButton(android.R.string.ok, this)








//Synthetic comment -- diff --git a/src/com/android/settings/CredentialStorage.java b/src/com/android/settings/CredentialStorage.java
//Synthetic comment -- index e246fce..d5ce224 100644

//Synthetic comment -- @@ -209,7 +209,7 @@
private ResetDialog() {
AlertDialog dialog = new AlertDialog.Builder(CredentialStorage.this)
.setTitle(android.R.string.dialog_alert_title)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(R.string.credentials_reset_hint)
.setPositiveButton(android.R.string.ok, this)
.setNegativeButton(android.R.string.cancel, this)
//Synthetic comment -- @@ -279,7 +279,7 @@
private ConfigureKeyGuardDialog() {
AlertDialog dialog = new AlertDialog.Builder(CredentialStorage.this)
.setTitle(android.R.string.dialog_alert_title)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(R.string.credentials_configure_lock_screen_hint)
.setPositiveButton(android.R.string.ok, this)
.setNegativeButton(android.R.string.cancel, this)








//Synthetic comment -- diff --git a/src/com/android/settings/CryptKeeperSettings.java b/src/com/android/settings/CryptKeeperSettings.java
//Synthetic comment -- index 41a4be5..f8c374d 100644

//Synthetic comment -- @@ -91,7 +91,7 @@
// TODO replace (or follow) this dialog with an explicit launch into password UI
new AlertDialog.Builder(getActivity())
.setTitle(R.string.crypt_keeper_dialog_need_password_title)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(R.string.crypt_keeper_dialog_need_password_message)
.setPositiveButton(android.R.string.ok, null)
.create()








//Synthetic comment -- diff --git a/src/com/android/settings/DevelopmentSettings.java b/src/com/android/settings/DevelopmentSettings.java
//Synthetic comment -- index 2ffae19..d349c41 100644

//Synthetic comment -- @@ -422,7 +422,7 @@
mOkDialog = new AlertDialog.Builder(getActivity()).setMessage(
getActivity().getResources().getString(R.string.adb_warning_message))
.setTitle(R.string.adb_warning_title)
                        .setIconAttribute(android.R.attr.alertDialogIcon)
.setPositiveButton(android.R.string.yes, this)
.setNegativeButton(android.R.string.no, this)
.show();








//Synthetic comment -- diff --git a/src/com/android/settings/PrivacySettings.java b/src/com/android/settings/PrivacySettings.java
//Synthetic comment -- index 3f3b9ad..ce4376e 100644

//Synthetic comment -- @@ -119,7 +119,7 @@
// TODO: DialogFragment?
mConfirmDialog = new AlertDialog.Builder(getActivity()).setMessage(msg)
.setTitle(R.string.backup_erase_dialog_title)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setPositiveButton(android.R.string.ok, this)
.setNegativeButton(android.R.string.cancel, this)
.show();








//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
//Synthetic comment -- index faa531a..d77e08f 100644

//Synthetic comment -- @@ -689,7 +689,7 @@
case DLG_CLEAR_DATA:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.clear_data_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getText(R.string.clear_data_dlg_text))
.setPositiveButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {
//Synthetic comment -- @@ -703,7 +703,7 @@
case DLG_FACTORY_RESET:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.app_factory_reset_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getText(R.string.app_factory_reset_dlg_text))
.setPositiveButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {
//Synthetic comment -- @@ -717,7 +717,7 @@
case DLG_APP_NOT_FOUND:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.app_not_found_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getText(R.string.app_not_found_dlg_title))
.setNeutralButton(getActivity().getText(R.string.dlg_ok),
new DialogInterface.OnClickListener() {
//Synthetic comment -- @@ -730,7 +730,7 @@
case DLG_CANNOT_CLEAR_DATA:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.clear_failed_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getText(R.string.clear_failed_dlg_text))
.setNeutralButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {
//Synthetic comment -- @@ -744,7 +744,7 @@
case DLG_FORCE_STOP:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.force_stop_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getText(R.string.force_stop_dlg_text))
.setPositiveButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {
//Synthetic comment -- @@ -760,14 +760,14 @@
getOwner().getMoveErrMsg(moveErrorCode));
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.move_app_failed_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(msg)
.setNeutralButton(R.string.dlg_ok, null)
.create();
case DLG_DISABLE:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.app_disable_dlg_title))
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getText(R.string.app_disable_dlg_text))
.setPositiveButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/settings/applications/RunningServiceDetails.java b/src/com/android/settings/applications/RunningServiceDetails.java
//Synthetic comment -- index 08cb0e0..ea1c88c 100644

//Synthetic comment -- @@ -507,7 +507,7 @@

return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getString(R.string.runningservicedetails_stop_dlg_title))
                            .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getActivity().getString(R.string.runningservicedetails_stop_dlg_text))
.setPositiveButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/Utils.java b/src/com/android/settings/bluetooth/Utils.java
//Synthetic comment -- index 01e72e0..fb44d5a 100755

//Synthetic comment -- @@ -93,7 +93,7 @@
Context activity = manager.getForegroundActivity();
if(manager.isForegroundActivity()) {
new AlertDialog.Builder(activity)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle(R.string.bluetooth_error_title)
.setMessage(message)
.setPositiveButton(android.R.string.ok, null)








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/InputMethodPreference.java b/src/com/android/settings/inputmethod/InputMethodPreference.java
//Synthetic comment -- index 4ecdb8e..8041e34 100644

//Synthetic comment -- @@ -235,7 +235,7 @@
}
mDialog = (new AlertDialog.Builder(mFragment.getActivity()))
.setTitle(android.R.string.dialog_alert_title)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setCancelable(true)
.setPositiveButton(android.R.string.ok,
new DialogInterface.OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/SpellCheckersSettings.java b/src/com/android/settings/inputmethod/SpellCheckersSettings.java
//Synthetic comment -- index d3e5181..8b1b867 100644

//Synthetic comment -- @@ -122,7 +122,7 @@
}
mDialog = (new AlertDialog.Builder(getActivity()))
.setTitle(android.R.string.dialog_alert_title)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setCancelable(true)
.setPositiveButton(android.R.string.ok,
new DialogInterface.OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/settings/quicklaunch/QuickLaunchSettings.java b/src/com/android/settings/quicklaunch/QuickLaunchSettings.java
//Synthetic comment -- index 47fa34a..5654323 100644

//Synthetic comment -- @@ -144,7 +144,7 @@
// Create the dialog for clearing a shortcut
return new AlertDialog.Builder(this)
.setTitle(getString(R.string.quick_launch_clear_dialog_title))
                        .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getString(R.string.quick_launch_clear_dialog_message,
mClearDialogShortcut, mClearDialogBookmarkTitle))
.setPositiveButton(R.string.quick_launch_clear_ok_button, this)








//Synthetic comment -- diff --git a/src/com/android/settings/tts/TextToSpeechSettings.java b/src/com/android/settings/tts/TextToSpeechSettings.java
//Synthetic comment -- index 517eade..f4dfdf0 100644

//Synthetic comment -- @@ -347,7 +347,7 @@
Log.i(TAG, "Displaying data alert for :" + key);
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder.setTitle(android.R.string.dialog_alert_title);
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
builder.setMessage(getActivity().getString(
R.string.tts_engine_security_warning, mEnginesHelper.getEngineInfo(key).label));
builder.setCancelable(true);







