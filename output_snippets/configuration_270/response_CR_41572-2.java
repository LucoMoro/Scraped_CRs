//<Beginning of snippet n. 0>
try {
    // Code to retrieve volume state
} catch (IllegalArgumentException e) {
    Log.e(TAG, "Illegal argument while getting volume state", e);
    return null;
} catch (RemoteException e) {
    Log.e(TAG, "Failed to get volume state", e);
    return null;
}
//<End of snippet n. 0>