/*Add support for bmp and wav content types in MMS

This change adds support for bitmap images (.bmp) and wave audio
(.wav) formats for MMS. This is a must-have requirement for some
operators.*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/ContentType.java b/core/java/com/google/android/mms/ContentType.java
//Synthetic comment -- index b066fad..12a1343 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
public static final String IMAGE_GIF         = "image/gif";
public static final String IMAGE_WBMP        = "image/vnd.wap.wbmp";
public static final String IMAGE_PNG         = "image/png";
    public static final String IMAGE_X_MS_BMP    = "image/x-ms-bmp";

public static final String AUDIO_UNSPECIFIED = "audio/*";
public static final String AUDIO_AAC         = "audio/aac";
//Synthetic comment -- @@ -58,6 +59,7 @@
public static final String AUDIO_X_MPEG      = "audio/x-mpeg";
public static final String AUDIO_X_MPG       = "audio/x-mpg";
public static final String AUDIO_3GPP        = "audio/3gpp";
    public static final String AUDIO_X_WAV       = "audio/x-wav";
public static final String AUDIO_OGG         = "application/ogg";

public static final String VIDEO_UNSPECIFIED = "video/*";
//Synthetic comment -- @@ -89,6 +91,7 @@
sSupportedContentTypes.add(IMAGE_WBMP);
sSupportedContentTypes.add(IMAGE_PNG);
sSupportedContentTypes.add(IMAGE_JPG);
        sSupportedContentTypes.add(IMAGE_X_MS_BMP);
//supportedContentTypes.add(IMAGE_SVG); not yet supported.

sSupportedContentTypes.add(AUDIO_AAC);
//Synthetic comment -- @@ -106,6 +109,7 @@
sSupportedContentTypes.add(AUDIO_X_MPEG3);
sSupportedContentTypes.add(AUDIO_X_MPEG);
sSupportedContentTypes.add(AUDIO_X_MPG);
        sSupportedContentTypes.add(AUDIO_X_WAV);
sSupportedContentTypes.add(AUDIO_3GPP);
sSupportedContentTypes.add(AUDIO_OGG);

//Synthetic comment -- @@ -127,6 +131,7 @@
sSupportedImageTypes.add(IMAGE_WBMP);
sSupportedImageTypes.add(IMAGE_PNG);
sSupportedImageTypes.add(IMAGE_JPG);
        sSupportedImageTypes.add(IMAGE_X_MS_BMP);

// add supported audio types
sSupportedAudioTypes.add(AUDIO_AAC);
//Synthetic comment -- @@ -145,6 +150,7 @@
sSupportedAudioTypes.add(AUDIO_X_MPEG3);
sSupportedAudioTypes.add(AUDIO_X_MPEG);
sSupportedAudioTypes.add(AUDIO_X_MPG);
        sSupportedAudioTypes.add(AUDIO_X_WAV);
sSupportedAudioTypes.add(AUDIO_3GPP);
sSupportedAudioTypes.add(AUDIO_OGG);








