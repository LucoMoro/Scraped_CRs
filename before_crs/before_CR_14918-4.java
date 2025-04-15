/*Change in media scanner to set correct date

When the media scanner scanned a single file, the modified
date (DATE_MODIFIED) was not correctly set in the content
provider. It was set as milliseconds but should have been
set as seconds. This caused downloaded media items to display
wrongly in the camera album, since the date was wrong.

Change-Id:I24cd92215c26f579eb33a4a3890f96c6ef9ec8c0*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 4296afb..66a1555 100644

//Synthetic comment -- @@ -333,17 +333,17 @@
Uri mTableUri;
long mRowId;
String mPath;
        long mLastModified;
boolean mSeenInFileSystem;
        boolean mLastModifiedChanged;

        FileCacheEntry(Uri tableUri, long rowId, String path, long lastModified) {
mTableUri = tableUri;
mRowId = rowId;
mPath = path;
            mLastModified = lastModified;
mSeenInFileSystem = false;
            mLastModifiedChanged = false;
}

@Override
//Synthetic comment -- @@ -394,11 +394,12 @@
private int mYear;
private int mDuration;
private String mPath;
        private long mLastModified;
private long mFileSize;
private String mWriter;

        public FileCacheEntry beginFile(String path, String mimeType, long lastModified, long fileSize) {

// special case certain file names
// I use regionMatches() instead of substring() below
//Synthetic comment -- @@ -456,10 +457,10 @@
entry.mSeenInFileSystem = true;

// add some slack to avoid a rounding error
            long delta = lastModified - entry.mLastModified;
if (delta > 1 || delta < -1) {
                entry.mLastModified = lastModified;
                entry.mLastModifiedChanged = true;
}

if (mProcessPlaylists && MediaFile.isPlayListFileType(mFileType)) {
//Synthetic comment -- @@ -479,20 +480,20 @@
mYear = 0;
mDuration = 0;
mPath = path;
            mLastModified = lastModified;
mWriter = null;

return entry;
}

        public void scanFile(String path, long lastModified, long fileSize) {
// This is the callback funtion from native codes.
// Log.v(TAG, "scanFile: "+path);
            doScanFile(path, null, lastModified, fileSize, false);
}

        public void scanFile(String path, String mimeType, long lastModified, long fileSize) {
            doScanFile(path, mimeType, lastModified, fileSize, false);
}

private boolean isMetadataSupported(int fileType) {
//Synthetic comment -- @@ -511,13 +512,14 @@
}
return false;
}
        public Uri doScanFile(String path, String mimeType, long lastModified, long fileSize, boolean scanAlways) {
Uri result = null;
//            long t1 = System.currentTimeMillis();
try {
                FileCacheEntry entry = beginFile(path, mimeType, lastModified, fileSize);
// rescan for metadata if file was modified since last scan
                if (entry != null && (entry.mLastModifiedChanged || scanAlways)) {
String lowpath = path.toLowerCase();
boolean ringtones = (lowpath.indexOf(RINGTONES_DIR) > 0);
boolean notifications = (lowpath.indexOf(NOTIFICATIONS_DIR) > 0);
//Synthetic comment -- @@ -632,7 +634,7 @@

map.put(MediaStore.MediaColumns.DATA, mPath);
map.put(MediaStore.MediaColumns.TITLE, mTitle);
            map.put(MediaStore.MediaColumns.DATE_MODIFIED, mLastModified);
map.put(MediaStore.MediaColumns.SIZE, mFileSize);
map.put(MediaStore.MediaColumns.MIME_TYPE, mMimeType);

//Synthetic comment -- @@ -640,13 +642,13 @@
map.put(Video.Media.ARTIST, (mArtist != null && mArtist.length() > 0 ? mArtist : MediaFile.UNKNOWN_STRING));
map.put(Video.Media.ALBUM, (mAlbum != null && mAlbum.length() > 0 ? mAlbum : MediaFile.UNKNOWN_STRING));
map.put(Video.Media.DURATION, mDuration);
                map.put(Video.Media.DATE_TAKEN, mLastModified * 1000);
// FIXME - add RESOLUTION
} else if (MediaFile.isImageFileType(mFileType)) {
// FIXME - add DESCRIPTION
// DATE_TAKEN will be overridden later if this is a JPEG image whose EXIF data
// contains date time information.
                map.put(Images.Media.DATE_TAKEN, mLastModified * 1000);
} else if (MediaFile.isAudioFileType(mFileType)) {
map.put(Audio.Media.ARTIST, (mArtist != null && mArtist.length() > 0 ? mArtist : MediaFile.UNKNOWN_STRING));
map.put(Audio.Media.ALBUM, (mAlbum != null && mAlbum.length() > 0 ? mAlbum : MediaFile.UNKNOWN_STRING));
//Synthetic comment -- @@ -918,14 +920,14 @@
while (c.moveToNext()) {
long rowId = c.getLong(ID_AUDIO_COLUMN_INDEX);
String path = c.getString(PATH_AUDIO_COLUMN_INDEX);
                        long lastModified = c.getLong(DATE_MODIFIED_AUDIO_COLUMN_INDEX);

String key = path;
if (mCaseInsensitivePaths) {
key = path.toLowerCase();
}
mFileCache.put(key, new FileCacheEntry(mAudioUri, rowId, path,
                                lastModified));
}
} finally {
c.close();
//Synthetic comment -- @@ -946,14 +948,14 @@
while (c.moveToNext()) {
long rowId = c.getLong(ID_VIDEO_COLUMN_INDEX);
String path = c.getString(PATH_VIDEO_COLUMN_INDEX);
                        long lastModified = c.getLong(DATE_MODIFIED_VIDEO_COLUMN_INDEX);

String key = path;
if (mCaseInsensitivePaths) {
key = path.toLowerCase();
}
mFileCache.put(key, new FileCacheEntry(mVideoUri, rowId, path,
                                lastModified));
}
} finally {
c.close();
//Synthetic comment -- @@ -976,14 +978,14 @@
while (c.moveToNext()) {
long rowId = c.getLong(ID_IMAGES_COLUMN_INDEX);
String path = c.getString(PATH_IMAGES_COLUMN_INDEX);
                       long lastModified = c.getLong(DATE_MODIFIED_IMAGES_COLUMN_INDEX);

String key = path;
if (mCaseInsensitivePaths) {
key = path.toLowerCase();
}
mFileCache.put(key, new FileCacheEntry(mImagesUri, rowId, path,
                                lastModified));
}
} finally {
c.close();
//Synthetic comment -- @@ -1007,14 +1009,15 @@

if (path != null && path.length() > 0) {
long rowId = c.getLong(ID_PLAYLISTS_COLUMN_INDEX);
                                long lastModified = c.getLong(DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX);

String key = path;
if (mCaseInsensitivePaths) {
key = path.toLowerCase();
}
mFileCache.put(key, new FileCacheEntry(mPlaylistsUri, rowId, path,
                                        lastModified));
}
}
} finally {
//Synthetic comment -- @@ -1112,7 +1115,7 @@
if (fileMissing) {
// do not delete missing playlists, since they may have been modified by the user.
// the user can delete them in the media player instead.
                // instead, clear the path and lastModified fields in the row
MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
int fileType = (mediaFileType == null ? 0 : mediaFileType.fileType);

//Synthetic comment -- @@ -1203,8 +1206,12 @@
prescan(path);

File file = new File(path);
// always scan the file, so we can return the content://media Uri for existing files
            return mClient.doScanFile(path, mimeType, file.lastModified(), file.length(), true);
} catch (RemoteException e) {
Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
return null;
//Synthetic comment -- @@ -1436,7 +1443,7 @@
String path = entry.mPath;

// only process playlist files if they are new or have been modified since the last scan
            if (entry.mLastModifiedChanged) {
ContentValues values = new ContentValues();
int lastSlash = path.lastIndexOf('/');
if (lastSlash < 0) throw new IllegalArgumentException("bad path " + path);
//Synthetic comment -- @@ -1449,15 +1456,17 @@
String name = (lastDot < 0 ? path.substring(lastSlash + 1) : path.substring(lastSlash + 1, lastDot));
values.put(MediaStore.Audio.Playlists.NAME, name);
values.put(MediaStore.Audio.Playlists.DATA, path);
                    values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, entry.mLastModified);
uri = mMediaProvider.insert(mPlaylistsUri, values);
rowId = ContentUris.parseId(uri);
membersUri = Uri.withAppendedPath(uri, Playlists.Members.CONTENT_DIRECTORY);
} else {
uri = ContentUris.withAppendedId(mPlaylistsUri, rowId);

                    // update lastModified value of existing playlist
                    values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, entry.mLastModified);
mMediaProvider.update(uri, values, null, null);

// delete members of existing playlist








//Synthetic comment -- diff --git a/media/java/android/media/MediaScannerClient.java b/media/java/android/media/MediaScannerClient.java
//Synthetic comment -- index 258c3b4..ec18dd1 100644

//Synthetic comment -- @@ -21,9 +21,9 @@
*/
public interface MediaScannerClient
{    
    public void scanFile(String path, long lastModified, long fileSize);
    
    public void scanFile(String path, String mimeType, long lastModified, long fileSize);

public void addNoMediaFolder(String path);








