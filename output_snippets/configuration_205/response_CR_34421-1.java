//<Beginning of snippet n. 0>
String externalStorageState = Environment.getExternalStorageState();
if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
    int volumeID = FileUtils.getFatVolumeId(path);
    if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

    if (volumeID != -1) {
        String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
        db = new DatabaseHelper(context, dbName, false);
    } else {
        Log.w(TAG, "Invalid volume ID: " + volumeID);
    }
} else {
    Log.e(TAG, "External storage is not mounted.");
}
//<End of snippet n. 0>