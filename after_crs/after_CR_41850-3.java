/*Update thumbnail when file is updated

When a file is updated, even if it's different content but the same
file name, the thumbnail file is not updated. We need to renew the
thumbnail for another content that uses an existing file name.
"Media.MINI_THUMB_MAGIC" should be set to 0 to trigger creation/re-creation
of thumbnail in MediaProvider.

Change-Id:Iaf5a0ea0c1069a0d7b3b47ee89c2357793278c17*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 0117d73..165a6e5 100644

//Synthetic comment -- @@ -907,12 +907,18 @@

Uri tableUri = mFilesUri;
MediaInserter inserter = mMediaInserter;
            boolean clearThumbMagic = false;
if (!mNoMedia) {
if (MediaFile.isVideoFileType(mFileType)) {
                    values.put(Video.Media.MINI_THUMB_MAGIC, 0);
                    clearThumbMagic = true;
tableUri = mVideoUri;
} else if (MediaFile.isImageFileType(mFileType)) {
                    values.put(Images.Media.MINI_THUMB_MAGIC, 0);
                    clearThumbMagic = true;
tableUri = mImagesUri;
} else if (MediaFile.isAudioFileType(mFileType)) {
                    clearThumbMagic = true;
tableUri = mAudioUri;
}
}
//Synthetic comment -- @@ -973,7 +979,9 @@
result = ContentUris.withAppendedId(tableUri, rowId);
// path should never change, and we want to avoid replacing mixed cased paths
// with squashed lower case paths
                if (!clearThumbMagic) {
                    values.remove(MediaStore.MediaColumns.DATA);
                }

int mediaType = 0;
if (!MediaScanner.isNoMediaPath(entry.mPath)) {







