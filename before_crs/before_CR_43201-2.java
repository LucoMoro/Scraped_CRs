/*Framework: Use holo alert drawables

Replaces the current usage of the GB alert
drawable with theme-consistent holo ones.

Change-Id:I8594ef8276db5bb0e192f3a541937d7281321043*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebCoreThreadWatchdog.java b/core/java/android/webkit/WebCoreThreadWatchdog.java
//Synthetic comment -- index a22e6e8..c27bb5f 100644

//Synthetic comment -- @@ -270,7 +270,7 @@
SUBSEQUENT_TIMEOUT_PERIOD);
}
})
                    .setIcon(android.R.drawable.ic_dialog_alert)
.show();
}
}








//Synthetic comment -- diff --git a/packages/VpnDialogs/src/com/android/vpndialogs/ConfirmDialog.java b/packages/VpnDialogs/src/com/android/vpndialogs/ConfirmDialog.java
//Synthetic comment -- index 13d8019..7a1e66c 100644

//Synthetic comment -- @@ -66,7 +66,7 @@
getString(R.string.prompt, app.loadLabel(pm)));
((CompoundButton) view.findViewById(R.id.check)).setOnCheckedChangeListener(this);

            mAlertParams.mIconId = android.R.drawable.ic_dialog_alert;
mAlertParams.mTitle = getText(android.R.string.dialog_alert_title);
mAlertParams.mPositiveButtonText = getText(android.R.string.ok);
mAlertParams.mPositiveButtonListener = this;








//Synthetic comment -- diff --git a/services/java/com/android/server/accessibility/AccessibilityManagerService.java b/services/java/com/android/server/accessibility/AccessibilityManagerService.java
//Synthetic comment -- index e42ec84..2c9a465 100644

//Synthetic comment -- @@ -1078,7 +1078,7 @@
return;
}
mEnableTouchExplorationDialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.ok, new OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {







