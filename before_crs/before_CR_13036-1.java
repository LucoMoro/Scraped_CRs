/*Add support for WMA, WMV, ASF, AAC, AMR (amr-nb), AWB (amr-wb) and WAV file metadata scanning.*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index afdc8f7..45b89b2 100644

//Synthetic comment -- @@ -502,10 +502,16 @@
mFileType == MediaFile.FILE_TYPE_3GPP ||
mFileType == MediaFile.FILE_TYPE_3GPP2 ||
mFileType == MediaFile.FILE_TYPE_OGG ||
                    mFileType == MediaFile.FILE_TYPE_AAC ||
mFileType == MediaFile.FILE_TYPE_MID ||
                    mFileType == MediaFile.FILE_TYPE_WMA) {
                // we only extract metadata from MP3, M4A, OGG, MID, AAC and WMA files.
// check MP4 files, to determine if they contain only audio.
return true;
}







