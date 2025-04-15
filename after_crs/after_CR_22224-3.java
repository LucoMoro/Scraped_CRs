/*Fixed issue 15069

Fixed the OnSyncStateUpdated() method so that the
checkbox gets checked/unchecked properly

Patch set 2) Updated with a fix Ive tested and that I have had
merged with CM7. Works great!

Patch set 3) Removed whitespace

Change-Id:I923dd20cdd01f218e2efd900adfbeed0b981993d*/




//Synthetic comment -- diff --git a/src/com/android/settings/ManageAccountsSettings.java b/src/com/android/settings/ManageAccountsSettings.java
//Synthetic comment -- index c9aec63..88bed5b 100644

//Synthetic comment -- @@ -99,6 +99,7 @@
if (backgroundDataSetting) {
setBackgroundDataInt(true);
onSyncStateUpdated();
                    mBackgroundDataCheckBox.setChecked(true);
} else {
// This will get unchecked only if the user hits "Ok"
mBackgroundDataCheckBox.setChecked(true);
//Synthetic comment -- @@ -130,6 +131,7 @@
setBackgroundDataInt(false);
pref.setChecked(false);
onSyncStateUpdated();
                                    mBackgroundDataCheckBox.setChecked(false);
}
})
.setNegativeButton(android.R.string.cancel, null)







