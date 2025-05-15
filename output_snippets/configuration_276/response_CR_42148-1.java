//<Beginning of snippet n. 1>
StorageVolume[] volumes = storageManager.getVolumeList();
boolean massStorageSupported = false;
StorageVolume primaryVolume = null;

for (StorageVolume volume : volumes) {
    if (volume.isPrimary()) {
        primaryVolume = volume;
        massStorageSupported = primaryVolume.allowMassStorage();
        break;
    }
}

if (primaryVolume == null) {
    // Handle error: no primary volume found
    // Set appropriate flags or throw an exception as needed
}

mUseUsbNotification = !massStorageSupported;
//<End of snippet n. 1>