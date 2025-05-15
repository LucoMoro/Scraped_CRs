
//<Beginning of snippet n. 0>


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
                            // happen if the volume ID is _actually_ 0xffffffff, in which case it must
                            // be changed since FileUtils::getFatVolumeId doesn't allow for that.  It
                            // may also indicate that FileUtils::getFatVolumeId is broken (missing
                            // ioctl), which is also impossible to disambiguate.
                            Log.e(TAG, "Unable to obtain external storage volume ID even though it's mounted.");
                        } else {
                            Log.i(TAG, "External storage is not (yet) mounted, cannot attach volume.");
                        }

                        throw new IllegalArgumentException("Unable to obtain external storage volume ID for " +
                                volume + " volume.");
                    }

// generate database name based on volume ID
String dbName = "external-" + Integer.toHexString(volumeID) + ".db";
db = new DatabaseHelper(context, dbName, false,

//<End of snippet n. 0>








