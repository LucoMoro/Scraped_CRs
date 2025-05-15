//<Beginning of snippet n. 1>
mContext.getSystemService(Context.STORAGE_SERVICE);
StorageVolume[] volumes = storageManager.getVolumeList();
boolean primaryVolumeFound = false;
massStorageSupported = false;

for (StorageVolume volume : volumes) {
    if (volume.isPrimary()) {
        primaryVolumeFound = true;
        massStorageSupported = volume.allowMassStorage();
        break;
    }
}

if (!primaryVolumeFound) {
    // Handle the case where there is no primary volume
    massStorageSupported = false; // or set a default value
}

mUseUsbNotification = !massStorageSupported;
//<End of snippet n. 1>