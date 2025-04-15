/*pullFile exception handling.

Make SuyncService.pullFile throw SyncException instead of IOException
if it cannot write to local file.

Bug 4600585

Change-Id:Ic3ab9fd7d1a05280ee14a310b9c8053371bb4e37*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncException.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncException.java
//Synthetic comment -- index 5f84f64..76de367 100644

//Synthetic comment -- @@ -44,6 +44,8 @@
REMOTE_PATH_LENGTH("Remote path is too long."),
/** error while reading local file. */
FILE_READ_ERROR("Reading local file failed!"),
/** attempting to push a directory. */
LOCAL_IS_DIRECTORY("Local path is a directory."),
/** attempting to push a non-existent file. */








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
//Synthetic comment -- index 03a68aa..6a94d22 100644

//Synthetic comment -- @@ -516,7 +516,13 @@
// create the stream to write in the file. We use a new try/catch block to differentiate
// between file and network io exceptions.
FileOutputStream fos = null;
        fos = new FileOutputStream(f);

// the buffer to read the data
byte[] data = new byte[SYNC_DATA_MAX];







