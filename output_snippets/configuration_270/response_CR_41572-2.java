//<Beginning of snippet n. 0>
} catch (RemoteException e) {
    Log.e(TAG, "Failed to get volume state for volume: " + volumeId, e);
    return null;
} catch (IllegalArgumentException e) {
    Log.e(TAG, "IllegalArgumentException encountered for volume: " + volumeId, e);
    return null; 
}
//<End of snippet n. 0>