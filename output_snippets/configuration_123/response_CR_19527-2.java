//<Beginning of snippet n. 0>

public static final String IMAGE_GIF         = "image/gif";
public static final String IMAGE_WBMP        = "image/vnd.wap.wbmp";
public static final String IMAGE_PNG         = "image/png";
public static final String IMAGE_BMP         = "image/bmp"; // Added BMP support
public static final String AUDIO_UNSPECIFIED = "audio/*";
public static final String AUDIO_AAC         = "audio/aac";
public static final String AUDIO_3GPP        = "audio/3gpp";
public static final String AUDIO_OGG         = "application/ogg";
public static final String AUDIO_WAV         = "audio/wav"; // Added WAV support

public static final String VIDEO_UNSPECIFIED = "video/*";

if (sSupportedContentTypes == null) {
    sSupportedContentTypes = new HashSet<>();
}
sSupportedContentTypes.add(IMAGE_WBMP);
sSupportedContentTypes.add(IMAGE_PNG);
sSupportedContentTypes.add(IMAGE_BMP); // Added BMP to supported content types
sSupportedContentTypes.add(IMAGE_JPG);

sSupportedContentTypes.add(AUDIO_AAC);
sSupportedContentTypes.add(AUDIO_3GPP);
sSupportedContentTypes.add(AUDIO_OGG);
sSupportedContentTypes.add(AUDIO_WAV); // Added WAV to supported content types

if (sSupportedImageTypes == null) {
    sSupportedImageTypes = new HashSet<>();
}
sSupportedImageTypes.add(IMAGE_WBMP);
sSupportedImageTypes.add(IMAGE_PNG);
sSupportedImageTypes.add(IMAGE_BMP); // Added BMP to supported image types
sSupportedImageTypes.add(IMAGE_JPG);

if (sSupportedAudioTypes == null) {
    sSupportedAudioTypes = new HashSet<>();
}
// add supported audio types
sSupportedAudioTypes.add(AUDIO_AAC);
sSupportedAudioTypes.add(AUDIO_3GPP);
sSupportedAudioTypes.add(AUDIO_OGG);
sSupportedAudioTypes.add(AUDIO_WAV); // Added WAV to supported audio types

//<End of snippet n. 0>