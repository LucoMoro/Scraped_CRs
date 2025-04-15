/*Musicplayer application can not display DRM files of internal storage.

Make DRM-protected files transferred over MTP be detected.
Previously it was inserted into the database as MEDIA_TYPE_NONE,
the result of this was that the file was not detected properly.
With this fix the file is scanned from the beginning.

Change-Id:Idf17d39e3d529394cd4a03d71fb250e69b395533*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 6f8b809..fd37bcf 100644

//Synthetic comment -- @@ -1399,7 +1399,8 @@
long lastModifiedSeconds = file.lastModified() / 1000;

if (!MediaFile.isAudioFileType(fileType) && !MediaFile.isVideoFileType(fileType) &&
            !MediaFile.isImageFileType(fileType) && !MediaFile.isPlayListFileType(fileType)) {

// no need to use the media scanner, but we need to update last modified and file size
ContentValues values = new ContentValues();







