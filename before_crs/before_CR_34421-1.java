/*Check external storage volume ID to ensure media is actually mounted.

Programs may request to media scan files (via MediaScannerConnection::scanFile)
before external storage is mounted.  If that happens, MediaProvider creates
an external-ffffffff.db (volume ID -1) database once external storage is
mounted, instead of the one with the expected volume ID.

Later, if android.process.media is killed and respawned, the real external
database (that is, the one corresponding to the actual external storage
volume ID) is attached.  At best, that database contains stale records.
However, in conjunction with another bug (where the LRU database garbage
collector prunes all but one external database), most often a new, empty
database is created.  This results in the sudden disappearance of all
ringtones and media from programs that depend on the media database.*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 1f5c940..c3114d3 100644

//Synthetic comment -- @@ -4344,6 +4344,29 @@
int volumeID = FileUtils.getFatVolumeId(path);
if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

// generate database name based on volume ID
String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
db = new DatabaseHelper(context, dbName, false,







