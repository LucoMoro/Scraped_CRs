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
ringtones and media from programs that depend on the media database.

Change-Id:If4e23b52cd09a640e26852916eef53710c27d933*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 1f5c940..0c7c553 100644

//Synthetic comment -- @@ -4344,6 +4344,29 @@
int volumeID = FileUtils.getFatVolumeId(path);
if (LOCAL_LOGV) Log.v(TAG, path + " volume ID: " + volumeID);

                    // Must check for failure!
                    // If the volume is not (yet) mounted, this will create a new
                    // external-ffffffff.db database instead of the one we expect.  Then, if
                    // android.process.media is later killed and respawned, the real external
                    // database will be attached, containing stale records, or worse, be empty.
                    if (volumeID == -1) {
                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state) ||
                                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                            // This may happen if external storage was _just_ mounted.  It may also
                            // happen if the volume ID is _actually_ 0xffffffff, in which case it
                            // must be changed since FileUtils::getFatVolumeId doesn't allow for
                            // that.  It may also indicate that FileUtils::getFatVolumeId is broken
                            // (missing ioctl), which is also impossible to disambiguate.
                            Log.e(TAG, "Can't obtain external volume ID even though it's mounted.");
                        } else {
                            Log.i(TAG, "External volume is not (yet) mounted, cannot attach.");
                        }

                        throw new IllegalArgumentException("Can't obtain external volume ID for " +
                                volume + " volume.");
                    }

// generate database name based on volume ID
String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
db = new DatabaseHelper(context, dbName, false,







