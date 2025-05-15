//<Beginning of snippet n. 0>

if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
    int volumeID = FileUtils.getFatVolumeId(path);
    if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

    if (volumeID != -1) {
        // generate database name based on volume ID
        String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
        db = new DatabaseHelper(context, dbName, false);
    } else {
        Log.e(TAG, "Invalid volume ID: " + volumeID);
    }
} else {
    Log.e(TAG, "External storage is not mounted.");
}

//<End of snippet n. 0>