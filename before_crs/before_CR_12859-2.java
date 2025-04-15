/*Add support for scanning WAV and AMR files

Change-Id:I6a2f098c9cec965a508f993d6bd83f55d341f375*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 4296afb..fe0e20d 100644

//Synthetic comment -- @@ -503,10 +503,11 @@
mFileType == MediaFile.FILE_TYPE_3GPP2 ||
mFileType == MediaFile.FILE_TYPE_OGG ||
mFileType == MediaFile.FILE_TYPE_AAC ||
mFileType == MediaFile.FILE_TYPE_MID ||
mFileType == MediaFile.FILE_TYPE_WMA) {
                // we only extract metadata from MP3, M4A, OGG, MID, AAC and WMA files.
                // check MP4 files, to determine if they contain only audio.
return true;
}
return false;







