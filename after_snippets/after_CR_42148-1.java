
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


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


//<End of snippet n. 1>








