/*Fixed unmount toggle in "Storage" part of Settings

Unmount toggle enables again before unmount is done. This is happen
because external USB/SD partition was busy and unmount was slow.
This patch adds the flag that allows to track unmounting progress
and prevent change toggle state until unmount will finish.

Change-Id:Ib1846f96b2cb6592e97880317380a4d9456c38eaSigned-off-by: Andrii Beregovenko <a.beregovenko@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index 728e558..d016bde 100644

//Synthetic comment -- @@ -135,6 +135,10 @@
StorageVolumePreferenceCategory svpc = mStorageVolumePreferenceCategories[i];
if (path.equals(svpc.getStorageVolume().getPath())) {
svpc.onStorageStateChanged();
break;
}
}
//Synthetic comment -- @@ -215,6 +219,7 @@
String state = mStorageManager.getVolumeState(storageVolume.getPath());
if (Environment.MEDIA_MOUNTED.equals(state) ||
Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
unmount();
} else {
mount();








//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 39a08d8..a12c5d9 100644

//Synthetic comment -- @@ -65,6 +65,23 @@

private boolean mAllowFormat;

static class CategoryInfo {
final int mTitle;
final int mColor;
//Synthetic comment -- @@ -204,6 +221,8 @@
mFormatPreference.setTitle(R.string.sd_format);
mFormatPreference.setSummary(R.string.sd_format_summary);
}
}

public StorageVolume getStorageVolume() {
//Synthetic comment -- @@ -235,7 +254,9 @@
addPreference(mFormatPreference);
}

        mMountTogglePreference.setEnabled(true);
}

private void updatePreferencesFromState() {
//Synthetic comment -- @@ -263,20 +284,25 @@

if (Environment.MEDIA_MOUNTED.equals(state)) {
mPreferences[AVAILABLE].setSummary(mPreferences[AVAILABLE].getSummary() + readOnly);

            mMountTogglePreference.setEnabled(true);
            mMountTogglePreference.setTitle(mResources.getString(R.string.sd_eject));
            mMountTogglePreference.setSummary(mResources.getString(R.string.sd_eject_summary));
} else {
if (Environment.MEDIA_UNMOUNTED.equals(state) || Environment.MEDIA_NOFS.equals(state)
|| Environment.MEDIA_UNMOUNTABLE.equals(state)) {
                mMountTogglePreference.setEnabled(true);
                mMountTogglePreference.setTitle(mResources.getString(R.string.sd_mount));
                mMountTogglePreference.setSummary(mResources.getString(R.string.sd_mount_summary));
} else {
                mMountTogglePreference.setEnabled(false);
                mMountTogglePreference.setTitle(mResources.getString(R.string.sd_mount));
                mMountTogglePreference.setSummary(mResources.getString(R.string.sd_insert_summary));
}

removePreference(mUsageBarPreference);







