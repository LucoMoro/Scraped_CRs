/*"Erase SD card" is always available.

It is possible to select "Erase SD card" from the menu even
though the card is unavailable. This change disables the
menu option when the SD card is not available.

Change-Id:Ibdcc9c130f66bb322174265ec11b1e832070575d*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index 0d00528..9a69085 100644

//Synthetic comment -- @@ -265,6 +265,8 @@
Log.e(TAG, "Mount service is null, can't mount");
}
} catch (RemoteException ex) {
}
}

//Synthetic comment -- @@ -276,6 +278,8 @@
readOnly = mRes.getString(R.string.read_only);
}

if (status.equals(Environment.MEDIA_MOUNTED)) {
if (!Environment.isExternalStorageRemovable()) {
// This device has built-in storage that is not removable.
//Synthetic comment -- @@ -325,6 +329,7 @@
mSdMountToggle.setEnabled(true);
mSdMountToggle.setTitle(mRes.getString(R.string.sd_mount));
mSdMountToggle.setSummary(mRes.getString(R.string.sd_mount_summary));
} else {
mSdMountToggle.setEnabled(false);
mSdMountToggle.setTitle(mRes.getString(R.string.sd_mount));







