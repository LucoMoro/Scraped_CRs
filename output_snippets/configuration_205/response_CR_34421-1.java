//<Beginning of snippet n. 0>
if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
    int volumeID = FileUtils.getFatVolumeId(path);
    if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

    if (volumeID != -1) {
        String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
        db = new DatabaseHelper(context, dbName, false);
    } else {
        Log.e(TAG, "Invalid volume ID: " + volumeID);
        throw new IllegalArgumentException("Invalid volume ID encountered.");
    }
} else {
    Log.e(TAG, "External storage is not mounted.");
    throw new IllegalStateException("External storage must be mounted to proceed.");
}
//<End of snippet n. 0>