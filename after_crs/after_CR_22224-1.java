/*Fixed issue 15069

Fixed the OnSyncStateUpdated() method so that the
checkbox gets checked/unchecked properly

Change-Id:I923dd20cdd01f218e2efd900adfbeed0b981993d*/




//Synthetic comment -- diff --git a/src/com/android/settings/ManageAccountsSettings.java b/src/com/android/settings/ManageAccountsSettings.java
//Synthetic comment -- index c9aec63..37c4176 100644

//Synthetic comment -- @@ -150,7 +150,7 @@
// Set background connection state
ConnectivityManager connManager =
(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean backgroundDataSetting = mBackgroundDataCheckBox.isChecked(); 
mBackgroundDataCheckBox.setChecked(backgroundDataSetting);
boolean masterSyncAutomatically = ContentResolver.getMasterSyncAutomatically();
mAutoSyncCheckbox.setChecked(masterSyncAutomatically);







