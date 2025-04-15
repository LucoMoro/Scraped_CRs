/*Prevent MediaScanner from removing Micro thumbnail database after scan.

After scanning the sdcard for the first time, the MediaScanner deletes
the micro thumbnail database files. The MiniThumbFile class attempts to
recreate the files but ends up with 0-sized db-files that it cannot
store any more micro thumbs in, resulting in that all micro thumbnails
that are results from thumbnail request are never stored properly until
the device is restarted.

Change-Id:I2b49580ae3360447525dbee80f6ba15b4bc658dd*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 3333268..8d40c7d 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
//Synthetic comment -- @@ -308,7 +309,7 @@

// used when scanning the image database so we know whether we have to prune
// old thumbnail files
    private int mOriginalImageCount;
/** Whether the scanner has set a default sound for the ringer ringtone. */
private boolean mDefaultRingtoneSet;
/** Whether the scanner has set a default sound for the notification ringtone. */
//Synthetic comment -- @@ -889,6 +890,7 @@
Cursor c = null;
String where = null;
String[] selectionArgs = null;
        int originalVideoCount = 0;

if (mFileCache == null) {
mFileCache = new HashMap<String, FileCacheEntry>();
//Synthetic comment -- @@ -945,6 +947,7 @@

if (c != null) {
try {
                    originalVideoCount = c.getCount();
while (c.moveToNext()) {
long rowId = c.getLong(ID_VIDEO_COLUMN_INDEX);
String path = c.getString(PATH_VIDEO_COLUMN_INDEX);
//Synthetic comment -- @@ -974,12 +977,12 @@
} else {
where = null;
}
            mOriginalImageCount = 0;
c = mMediaProvider.query(mImagesUri, IMAGES_PROJECTION, where, selectionArgs, null);

if (c != null) {
try {
                    mOriginalImageCount = c.getCount();
while (c.moveToNext()) {
long rowId = c.getLong(ID_IMAGES_COLUMN_INDEX);
String path = c.getString(PATH_IMAGES_COLUMN_INDEX);
//Synthetic comment -- @@ -1041,6 +1044,19 @@
c.close();
}
}

        // Clear micro thumb databases now if applicable, instead of after scan
        // to avoid undoing work that has been done by the thumbnail queue
        // during scan.
        if (originalVideoCount == 0 && mImagesUri.equals(Video.Media.getContentUri("external"))) {
            MiniThumbFile.instance(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    .removeMiniThumbDataFile();
        }

        if (mOriginalImageCount == 0 && mImagesUri.equals(Images.Media.getContentUri("external"))) {
            MiniThumbFile.instance(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    .removeMiniThumbDataFile();
        }
}

private boolean inScanDirectory(String path, String[] directories) {
//Synthetic comment -- @@ -1055,7 +1071,16 @@
private void pruneDeadThumbnailFiles() {
HashSet<String> existingFiles = new HashSet<String>();
String directory = "/sdcard/DCIM/.thumbnails";
        String[] files = (new File(directory)).list(new FilenameFilter() {

            // Do not process non-thumbnail files.
            public boolean accept(File dir, String filename) {
                return filename.toLowerCase().endsWith(".jpg") ||
                       filename.toLowerCase().endsWith(".jpeg");
            }

        });

if (files == null)
files = new String[0];

//Synthetic comment -- @@ -1145,7 +1170,7 @@
processPlayLists();
}

        if (mOriginalImageCount == 0 && mImagesUri.equals(Images.Media.getContentUri("external")))
pruneDeadThumbnailFiles();

// allow GC to clean up








//Synthetic comment -- diff --git a/media/java/android/media/MiniThumbFile.java b/media/java/android/media/MiniThumbFile.java
//Synthetic comment -- index df141c1..a4c5c34 100644

//Synthetic comment -- @@ -144,6 +144,18 @@
}
}

    public synchronized void removeMiniThumbDataFile() {
        String path = randomAccessFilePath(MINI_THUMB_DATA_FILE_VERSION);
        File file = new File(path);
        if (file.exists()) {
            try {
                file.delete();
            } catch (SecurityException ex) {
                // ignore
            }
        }
    }

// Get the magic number for the specified id in the mini-thumb file.
// Returns 0 if the magic is not available.
public synchronized long getMagic(long id) {







