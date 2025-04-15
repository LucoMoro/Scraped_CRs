/*Fix use of holo drawables in a few more places

Change-Id:Ie549ff14812da807729641fab1871fffe35f80b6*/
//Synthetic comment -- diff --git a/src/com/android/settings/DevelopmentSettings.java b/src/com/android/settings/DevelopmentSettings.java
//Synthetic comment -- index 4e6ebfb..46ffe83 100644

//Synthetic comment -- @@ -757,7 +757,7 @@
getActivity().getResources().getString(
R.string.dev_settings_warning_message))
.setTitle(R.string.dev_settings_warning_title)
                            .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.yes, this)
.setNegativeButton(android.R.string.no, this)
.show();








//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
//Synthetic comment -- index ccbf371..7fe3711 100644

//Synthetic comment -- @@ -909,7 +909,7 @@
case DLG_DISABLE_NOTIFICATIONS:
return new AlertDialog.Builder(getActivity())
.setTitle(getActivity().getText(R.string.app_disable_notifications_dlg_title))
                    .setIcon(android.R.drawable.ic_dialog_alert)
.setMessage(getActivity().getText(R.string.app_disable_notifications_dlg_text))
.setPositiveButton(R.string.dlg_ok,
new DialogInterface.OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/InputMethodAndSubtypeEnabler.java b/src/com/android/settings/inputmethod/InputMethodAndSubtypeEnabler.java
//Synthetic comment -- index ea3ddf4..13f4435 100644

//Synthetic comment -- @@ -155,7 +155,7 @@
if (mDialog == null) {
mDialog = (new AlertDialog.Builder(getActivity()))
.setTitle(android.R.string.dialog_alert_title)
                            .setIcon(android.R.drawable.ic_dialog_alert)
.setCancelable(true)
.setPositiveButton(android.R.string.ok,
new DialogInterface.OnClickListener() {







