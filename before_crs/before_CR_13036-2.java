/*Add support for file metadata scanning in more formats.

Add support for WMA, WMV, ASF, AAC, AMR (amr-nb), AWB (amr-wb)
and WAV file metadata scanning.

Change-Id:I5b7e334a98f3902d3cd5d1c14d2252c5bbded92b*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 4296afb..231033f 100644

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







