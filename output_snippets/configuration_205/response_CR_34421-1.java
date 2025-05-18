//<Beginning of snippet n. 0>

if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    int volumeID = FileUtils.getFatVolumeId(path);
    if (volumeID != -1) {
        if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);
        
        // generate database name based on volume ID
        String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
        db = new DatabaseHelper(context, dbName, false);
    } else {
        if (LOCAL_LOGV) Log.w(TAG, "Invalid volume ID: " + volumeID);
        // Queue pending operations and notify users
        notifyUser("Invalid volume ID. Operation queued until valid ID is available.");
        queuePendingOperations();
    }
} else {
    if (LOCAL_LOGV) Log.w(TAG, "External storage is not mounted.");
    // Notify user and queue operations
    notifyUser("External storage is not available. Operations have been queued.");
    queuePendingOperations();
}

//<End of snippet n. 0>