//<Beginning of snippet n. 0>
}

/**
* Number of megabytes of space to leave unallocated by MTP.
* MTP will subtract this value from the free space it reports back
* to the host via GetStorageInfo, and will not allow new files to
*/

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
StorageVolume[] volumes = storageManager.getVolumeList();
boolean massStorageSupported = false;

if (volumes.length > 0) {
    for (StorageVolume volume : volumes) {
        if (volume.isPrimary()) {
            massStorageSupported = volume.allowMassStorage();
            break;
        }
    }
}

if (!massStorageSupported) {
    // Handle case where no primary volume is found or does not support mass storage
    // Possible logging or fallback logic can be placed here
}

mUseUsbNotification = !massStorageSupported;

//<End of snippet n. 1>