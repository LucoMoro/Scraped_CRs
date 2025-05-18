//<Beginning of snippet n. 0>
try {
    if (volume == null || !isVolumeValid(volume) || isDiscontinuousPartition(volume)) {
        Log.e(TAG, "Invalid volume state or discontinuous partition: " + (volume != null ? volume.getId() : "null"));
        return null;
    }
    return getVolumeState(volume);
} catch (IllegalArgumentException e) {
    Log.e(TAG, "Illegal argument while retrieving volume state for volume ID: " + (volume != null ? volume.getId() : "null"), e);
    return null;
} catch (RemoteException e) {
    Log.e(TAG, "Failed to get volume state for volume ID: " + (volume != null ? volume.getId() : "null"), e);
    return null;
}
//<End of snippet n. 0>