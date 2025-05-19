//<Beginning of snippet n. 0>
}

/**
* Number of megabytes of space to leave unallocated by MTP.
* MTP will subtract this value from the free space it reports back
* to the host via GetStorageInfo, and will not allow new files to
*/

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
mContext.getSystemService(Context.STORAGE_SERVICE);
StorageVolume[] volumes = storageManager.getVolumeList();
StorageVolume primaryVolume = null;

for (StorageVolume volume : volumes) {
    if (volume.isPrimary()) {
        primaryVolume = volume;
        break;
    }
}

boolean massStorageSupported = (primaryVolume != null) && primaryVolume.allowMassStorage();
mUseUsbNotification = !massStorageSupported;

// Error handling for no primary volume found
if (primaryVolume == null) {
    // Handle the absence of a primary volume as needed
}

//<End of snippet n. 1>