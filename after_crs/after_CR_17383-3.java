/*Added mpga as valid MediaFile extension

Added mpga as a valid media file extension, it is treated as mp3.

Change-Id:Ie39036d61b6ca50439861fa10be0d824dcc65ed3*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaFile.java b/media/java/android/media/MediaFile.java
//Synthetic comment -- index e275aa6..d674374 100644

//Synthetic comment -- @@ -172,6 +172,7 @@

static {
addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg", MtpConstants.FORMAT_MP3);
        addFileType("MPGA", FILE_TYPE_MP3, "audio/mpeg", MtpConstants.FORMAT_MP3);
addFileType("M4A", FILE_TYPE_M4A, "audio/mp4", MtpConstants.FORMAT_MPEG);
addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav", MtpConstants.FORMAT_WAV);
addFileType("AMR", FILE_TYPE_AMR, "audio/amr");







