/*Loop over volume list to find primary for mass storage.

Modify UsbDeviceManager::systemReady() to loop over the volume list
until it finds the primary volume.  Add a boolean method to
StorageVolume that returns true if the volume in question is the
primary storage.

Fixes a problem where the primary storage volume in the list returned
by StorageManager is not the first item.

Change-Id:Iab0927826dfe2629695c7ab5a07d224e02e8fc28Signed-off-by: Jim Bride <jim.bride@intel.com>
Signed-off-by: Andrew Boie <andrew.p.boie@intel.com>*/




//Synthetic comment -- diff --git a/core/java/android/os/storage/StorageVolume.java b/core/java/android/os/storage/StorageVolume.java
//Synthetic comment -- index 79c8f3b..ab86f02 100644

//Synthetic comment -- @@ -128,6 +128,15 @@
}

/**
     * Returns true if this volume is primary storage.
     *
     * @return whether this volume is primary storage
     */
    public boolean isPrimaryStorage() {
        return (mStorageId == 0x00010001);
    }

    /**
* Number of megabytes of space to leave unallocated by MTP.
* MTP will subtract this value from the free space it reports back
* to the host via GetStorageInfo, and will not allow new files to








//Synthetic comment -- diff --git a/services/java/com/android/server/usb/UsbDeviceManager.java b/services/java/com/android/server/usb/UsbDeviceManager.java
//Synthetic comment -- index a115345c..14b9eb9 100644

//Synthetic comment -- @@ -181,7 +181,14 @@
mContext.getSystemService(Context.STORAGE_SERVICE);
StorageVolume[] volumes = storageManager.getVolumeList();
if (volumes.length > 0) {
            for (StorageVolume vol : volumes) {
                // Once we find the primary storage we need not
                // go further.
                if (vol.isPrimaryStorage()) {
                    massStorageSupported = vol.allowMassStorage();
                    break;
                }
            }
}
mUseUsbNotification = !massStorageSupported;








