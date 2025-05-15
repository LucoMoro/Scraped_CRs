//<Beginning of snippet n. 0>
} catch (RemoteException e) {
    Log.e(TAG, "Failed to get volume state", e);
    return null;
} catch (IllegalArgumentException e) {
    Log.e(TAG, "Invalid argument encountered", e);
    throw e;
}
//<End of snippet n. 0>