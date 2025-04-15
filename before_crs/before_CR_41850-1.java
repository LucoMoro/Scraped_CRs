/*Update thumbnail when file is updated

When a file is updated, even if it's different content but the same
file name, the thumbnail file is not updated. We need to renew the
thumbnail for another content that uses an existing file name.
"Media.MINI_THUMB_MAGIC" should be set to 0 to trigger creation/re-creation
of thumbnail in MediaProvider.

Change-Id:Iaf5a0ea0c1069a0d7b3b47ee89c2357793278c17*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 6f8b809..7c4ab1a 100644

//Synthetic comment -- @@ -890,12 +890,18 @@

Uri tableUri = mFilesUri;
MediaInserter inserter = mMediaInserter;
if (!mNoMedia) {
if (MediaFile.isVideoFileType(mFileType)) {
tableUri = mVideoUri;
} else if (MediaFile.isImageFileType(mFileType)) {
tableUri = mImagesUri;
} else if (MediaFile.isAudioFileType(mFileType)) {
tableUri = mAudioUri;
}
}
//Synthetic comment -- @@ -956,7 +962,9 @@
result = ContentUris.withAppendedId(tableUri, rowId);
// path should never change, and we want to avoid replacing mixed cased paths
// with squashed lower case paths
                values.remove(MediaStore.MediaColumns.DATA);

int mediaType = 0;
if (!MediaScanner.isNoMediaPath(entry.mPath)) {







