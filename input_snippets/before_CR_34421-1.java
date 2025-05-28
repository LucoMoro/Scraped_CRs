
//<Beginning of snippet n. 0>


int volumeID = FileUtils.getFatVolumeId(path);
if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

// generate database name based on volume ID
String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
db = new DatabaseHelper(context, dbName, false,

//<End of snippet n. 0>








