/*Settings: fix storage measurement for device without emulated sdcard

StorageMeasurement: misc files should only be measured for internal
storage only if it is the emulated storage, otherwise misc count
for the primary storage will be erroneously duplicated on the
internal storage count.

StorageVolumePreferenceCategory:
  - Always create mMountTogglePreference to avoid null pointer
    exception for code that relies on it existing
  - Check if the keys exist before calculating totalValues to
    avoid null pointer exception on storage where media is not
    calculated
  - For available/readonly, set title, not summary (summary is
    supposed to be the amount available)

Change-Id:Ib217f79ee562dffc514696fff038e58ed0dba7d9*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageMeasurement.java b/src/com/android/settings/deviceinfo/StorageMeasurement.java
//Synthetic comment -- index 772ac0d..a22ba77 100644

//Synthetic comment -- @@ -422,7 +422,7 @@
}

// Measure misc files not counted under media
            if (mIsInternal || mIsPrimary) {
final File path = mIsInternal ? currentEnv.getExternalStorageDirectory()
: mVolume.getPathFile();
details.miscSize = measureMisc(imcs, path);








//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java b/src/com/android/settings/deviceinfo/StorageVolumePreferenceCategory.java
//Synthetic comment -- index 1599ec7..61188ec 100644

//Synthetic comment -- @@ -194,8 +194,9 @@
}

final boolean isRemovable = mVolume != null ? mVolume.isRemovable() : false;
if (isRemovable) {
            mMountTogglePreference = new Preference(context);
mMountTogglePreference.setTitle(R.string.sd_eject);
mMountTogglePreference.setSummary(R.string.sd_eject_summary);
addPreference(mMountTogglePreference);
//Synthetic comment -- @@ -240,12 +241,12 @@
final String state = mStorageManager.getVolumeState(mVolume.getPath());

if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mItemAvailable.setSummary(R.string.memory_available_read_only);
if (mFormatPreference != null) {
removePreference(mFormatPreference);
}
} else {
            mItemAvailable.setSummary(R.string.memory_available);
}

if (Environment.MEDIA_MOUNTED.equals(state)
//Synthetic comment -- @@ -310,7 +311,9 @@
private static long totalValues(HashMap<String, Long> map, String... keys) {
long total = 0;
for (String key : keys) {
            total += map.get(key);
}
return total;
}







