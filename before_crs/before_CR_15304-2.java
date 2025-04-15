/*Prevent MediaScanner from removing Micro thumbnail database after scan.

After scanning the sdcard for the first time, the MediaScanner deletes
the micro thumbnail database files. The MiniThumbFile class attempts to
recreate the files but ends up with 0-sized db-files that it cannot
store any more micro thumbs in, resulting in that all micro thumbnails
that are results from thumbnail request are never stored properly until
the device is restarted.

Change-Id:I2b49580ae3360447525dbee80f6ba15b4bc658dd*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 84a67cf..71d27f6 100644

//Synthetic comment -- @@ -303,7 +303,7 @@

// used when scanning the image database so we know whether we have to prune
// old thumbnail files
    private int mOriginalCount;
/** Whether the scanner has set a default sound for the ringer ringtone. */
private boolean mDefaultRingtoneSet;
/** Whether the scanner has set a default sound for the notification ringtone. */
//Synthetic comment -- @@ -892,6 +892,7 @@
Cursor c = null;
String where = null;
String[] selectionArgs = null;

if (mFileCache == null) {
mFileCache = new HashMap<String, FileCacheEntry>();
//Synthetic comment -- @@ -943,6 +944,7 @@

if (c != null) {
try {
while (c.moveToNext()) {
long rowId = c.getLong(ID_VIDEO_COLUMN_INDEX);
String path = c.getString(PATH_VIDEO_COLUMN_INDEX);
//Synthetic comment -- @@ -967,12 +969,12 @@
} else {
where = null;
}
            mOriginalCount = 0;
c = mMediaProvider.query(mImagesUri, IMAGES_PROJECTION, where, selectionArgs, null);

if (c != null) {
try {
                    mOriginalCount = c.getCount();
while (c.moveToNext()) {
long rowId = c.getLong(ID_IMAGES_COLUMN_INDEX);
String path = c.getString(PATH_IMAGES_COLUMN_INDEX);
//Synthetic comment -- @@ -1029,6 +1031,19 @@
c.close();
}
}
}

private boolean inScanDirectory(String path, String[] directories) {
//Synthetic comment -- @@ -1071,7 +1086,10 @@
if (Config.LOGV)
Log.v(TAG, "fileToDelete is " + fileToDelete);
try {
                    (new File(fileToDelete)).delete();
} catch (SecurityException ex) {
}
}
//Synthetic comment -- @@ -1133,7 +1151,7 @@
processPlayLists();
}

        if (mOriginalCount == 0 && mImagesUri.equals(Images.Media.getContentUri("external")))
pruneDeadThumbnailFiles();

// allow GC to clean up








//Synthetic comment -- diff --git a/media/java/android/media/MiniThumbFile.java b/media/java/android/media/MiniThumbFile.java
//Synthetic comment -- index f6e6317..46812e4 100644

//Synthetic comment -- @@ -145,6 +145,18 @@
}
}

// Get the magic number for the specified id in the mini-thumb file.
// Returns 0 if the magic is not available.
public synchronized long getMagic(long id) {







