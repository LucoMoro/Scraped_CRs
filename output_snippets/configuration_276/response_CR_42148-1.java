//<Beginning of snippet n. 1>
mContext.getSystemService(Context.STORAGE_SERVICE);
StorageVolume[] volumes = storageManager.getVolumeList();
massStorageSupported = false;

if (volumes.length > 0) {
    for (StorageVolume volume : volumes) {
        if (volume.isPrimaryStorage()) {
            massStorageSupported = volume.allowMassStorage();
            break;
        }
    }
}

mUseUsbNotification = !massStorageSupported;
//<End of snippet n. 1>