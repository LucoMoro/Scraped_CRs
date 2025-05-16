//<Beginning of snippet n. 0>
} catch (RemoteException e) {
    Log.e(TAG, "Failed to get volume state", e);
    return null;
} catch (IllegalArgumentException e) {
    Log.e(TAG, "Invalid volume state encountered", e);
    return null;
}
//<End of snippet n. 0>