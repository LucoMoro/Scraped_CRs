
//<Beginning of snippet n. 0>


}

/**
* Number of megabytes of space to leave unallocated by MTP.
* MTP will subtract this value from the free space it reports back
* to the host via GetStorageInfo, and will not allow new files to

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


mContext.getSystemService(Context.STORAGE_SERVICE);
StorageVolume[] volumes = storageManager.getVolumeList();
if (volumes.length > 0) {
            massStorageSupported = volumes[0].allowMassStorage();
}
mUseUsbNotification = !massStorageSupported;


//<End of snippet n. 1>








