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
                    if (svpc.isUnmountInProgress() &&
                            Environment.MEDIA_UNMOUNTED.equals(newState)) {
                        svpc.setUnmountInProgress(false);
                    }
break;
}
}
//Synthetic comment -- @@ -215,6 +219,7 @@
String state = mStorageManager.getVolumeState(storageVolume.getPath());
if (Environment.MEDIA_MOUNTED.equals(state) ||
Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    svpc.setUnmountInProgress(true);
unmount();
} else {
mount();








//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 39a08d8..a12c5d9 100644

//Synthetic comment -- @@ -65,6 +65,23 @@

private boolean mAllowFormat;

    /**
     * Flag which prevent to enable mount toggle during slow unmount.
     * When external SD card contain many media files,  unmount procedure is slow,
     * up to 10-15 seconds. During this time, widget will update 2-3 times that
     * leads to invalid state of mount/unmount toggle. The solution is to set this
     * flag that prevent of updating toggle title and state.
     */
    private boolean mUnmountInProgress;

    public boolean isUnmountInProgress() {
        return mUnmountInProgress;
    }

    public void setUnmountInProgress(boolean value) {
        mUnmountInProgress = value;
    }

static class CategoryInfo {
final int mTitle;
final int mColor;
//Synthetic comment -- @@ -204,6 +221,8 @@
mFormatPreference.setTitle(R.string.sd_format);
mFormatPreference.setSummary(R.string.sd_format_summary);
}

        mUnmountInProgress = false;
}

public StorageVolume getStorageVolume() {
//Synthetic comment -- @@ -235,7 +254,9 @@
addPreference(mFormatPreference);
}

        if (!mUnmountInProgress) {
            mMountTogglePreference.setEnabled(true);
        }
}

private void updatePreferencesFromState() {
//Synthetic comment -- @@ -263,20 +284,25 @@

if (Environment.MEDIA_MOUNTED.equals(state)) {
mPreferences[AVAILABLE].setSummary(mPreferences[AVAILABLE].getSummary() + readOnly);
            if (!mUnmountInProgress) {
                mMountTogglePreference.setEnabled(true);
                mMountTogglePreference.setTitle(mResources.getString(R.string.sd_eject));
                mMountTogglePreference.setSummary(mResources.getString(R.string.sd_eject_summary));
            }
} else {
if (Environment.MEDIA_UNMOUNTED.equals(state) || Environment.MEDIA_NOFS.equals(state)
|| Environment.MEDIA_UNMOUNTABLE.equals(state)) {
                if (!mUnmountInProgress) {
                    mMountTogglePreference.setEnabled(true);
                    mMountTogglePreference.setTitle(mResources.getString(R.string.sd_mount));
                    mMountTogglePreference.setSummary(mResources.getString(R.string.sd_mount_summary));
                }
} else {
                if (!mUnmountInProgress) {
                    mMountTogglePreference.setEnabled(false);
                    mMountTogglePreference.setTitle(mResources.getString(R.string.sd_mount));
                    mMountTogglePreference.setSummary(mResources.getString(R.string.sd_insert_summary));
                }
}

removePreference(mUsageBarPreference);







