/*Added OnKeyListener to alert dialogs.

Bug:
A confirmation dialog is shown when Accessibility is disabled or one of accessibility services is enabled.
The Accessibility is disabled or the accessibility service is enabled even though user presses Cancel key.

Change-Id:Id110193b8fdfec5a7892bd8e6bcc8d08c3a99d83*/
//Synthetic comment -- diff --git a/src/com/android/settings/AccessibilitySettings.java b/src/com/android/settings/AccessibilitySettings.java
//Synthetic comment -- index d78d2d8..0831f9e 100644

//Synthetic comment -- @@ -233,6 +233,13 @@
checkBoxPreference.setChecked(true);
}
})
.create();
dialog.show();
}
//Synthetic comment -- @@ -246,6 +253,7 @@
private void handleEnableAccessibilityServiceStateChange(CheckBoxPreference preference) {
if (preference.isChecked()) {
final CheckBoxPreference checkBoxPreference = preference;
AlertDialog dialog = (new AlertDialog.Builder(this))
.setTitle(android.R.string.dialog_alert_title)
.setIcon(android.R.drawable.ic_dialog_alert)
//Synthetic comment -- @@ -266,6 +274,13 @@
checkBoxPreference.setChecked(false);
}
})
.create();
dialog.show();
} else {







