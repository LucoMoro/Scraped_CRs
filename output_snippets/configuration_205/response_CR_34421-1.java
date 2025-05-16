//<Beginning of snippet n. 0>
if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    int volumeID = FileUtils.getFatVolumeId(path);
    if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

    if (volumeID != -1) {
        // generate database name based on volume ID
        String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
        db = new DatabaseHelper(context, dbName, false);
    } else {
        Log.e(TAG, "Invalid volume ID: " + volumeID);
        // Notify user or handle error accordingly
    }
} else {
    Log.e(TAG, "External storage is not mounted.");
    // Notify user or handle error accordingly
}
//<End of snippet n. 0>