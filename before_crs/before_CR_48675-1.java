/*Files with AMR_NB decoder and 3ga container could not be recognized.

Adding support for audio files with ".3ga" extension. Modified code in -
MediaFile.java - file with 3GA extension are added with mime-type as "audio/3ga".

Change-Id:I29fa29c4786c10c1d95da6e3afa1954e7b1f3e71Author: Gurudatta Bhakte <gurudattax.bhakte@intel.com>
Signed-off-by: Gurudatta Bhakte <gurudattax.bhakte@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18401*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaFile.java b/media/java/android/media/MediaFile.java
//Synthetic comment -- index 06d43a2..9fad95b 100644

//Synthetic comment -- @@ -47,13 +47,14 @@
public static final int FILE_TYPE_AAC     = 8;
public static final int FILE_TYPE_MKA     = 9;
public static final int FILE_TYPE_FLAC    = 10;
private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_FLAC;

// MIDI file types
    public static final int FILE_TYPE_MID     = 11;
    public static final int FILE_TYPE_SMF     = 12;
    public static final int FILE_TYPE_IMY     = 13;
private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

//Synthetic comment -- @@ -177,6 +178,7 @@
addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav", MtpConstants.FORMAT_WAV);
addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
if (isWMAEnabled()) {
addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma", MtpConstants.FORMAT_WMA);
}







