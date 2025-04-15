/*Added mpga as valied MediaFile extension

Added mpga as a valied media file extension, it will be treated as a
mp3.

Change-Id:Ie39036d61b6ca50439861fa10be0d824dcc65ed3*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaFile.java b/media/java/android/media/MediaFile.java
//Synthetic comment -- index 9d1d420..7ec34ad 100644

//Synthetic comment -- @@ -124,6 +124,7 @@

static {
addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("MPGA", FILE_TYPE_MP3, "audio/mpeg");
addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
addFileType("AMR", FILE_TYPE_AMR, "audio/amr");







