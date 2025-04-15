/*Fixed issue 15069

Fixed the OnSyncStateUpdated() method so that the
checkbox gets checked/unchecked properly

Patch set 2) Updated with a fix Ive tested and that I have had
merged with CM7. Works great!

Change-Id:I923dd20cdd01f218e2efd900adfbeed0b981993d*/
//Synthetic comment -- diff --git a/src/com/android/settings/ManageAccountsSettings.java b/src/com/android/settings/ManageAccountsSettings.java
//Synthetic comment -- index c9aec63..7d1f903 100644

//Synthetic comment -- @@ -99,6 +99,7 @@
if (backgroundDataSetting) {
setBackgroundDataInt(true);
onSyncStateUpdated();
} else {
// This will get unchecked only if the user hits "Ok"
mBackgroundDataCheckBox.setChecked(true);
//Synthetic comment -- @@ -130,6 +131,7 @@
setBackgroundDataInt(false);
pref.setChecked(false);
onSyncStateUpdated();
}
})
.setNegativeButton(android.R.string.cancel, null)
//Synthetic comment -- @@ -150,7 +152,7 @@
// Set background connection state
ConnectivityManager connManager =
(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean backgroundDataSetting = connManager.getBackgroundDataSetting();
mBackgroundDataCheckBox.setChecked(backgroundDataSetting);
boolean masterSyncAutomatically = ContentResolver.getMasterSyncAutomatically();
mAutoSyncCheckbox.setChecked(masterSyncAutomatically);







