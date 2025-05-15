//<Beginning of snippet n. 0>
} catch (IllegalArgumentException e) {
    Log.e(TAG, "Invalid volume state due to discontinuous partition", e);
    return null;
} catch (RemoteException e) {
    Log.e(TAG, "Failed to get volume state", e);
    return null;
}
//<End of snippet n. 0>