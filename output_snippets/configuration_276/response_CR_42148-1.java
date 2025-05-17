//<Beginning of snippet n. 0>
mContext.getSystemService(Context.STORAGE_SERVICE);
StorageVolume[] volumes = storageManager.getVolumeList();
boolean primaryVolumeFound = false;

for (StorageVolume volume : volumes) {
    if (volume.isPrimary()) {
        massStorageSupported = volume.allowMassStorage();
        primaryVolumeFound = true;
        break;
    }
}

if (!primaryVolumeFound) {
    massStorageSupported = false;
}

mUseUsbNotification = !massStorageSupported;
//<End of snippet n. 0>