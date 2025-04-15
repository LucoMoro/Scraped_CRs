/*"audio/mp4" missing from array of supported content types.

"audio/mp4" is included in the array of supported audio types,
but isn't included in the array of supproted content types,
causing problems with proper use of MP4 audio in MMS.

Change-Id:I7be9ac581fcf7594234000d37f56d901174d54c0*/




//Synthetic comment -- diff --git a/src/java/com/google/android/mms/ContentType.java b/src/java/com/google/android/mms/ContentType.java
//Synthetic comment -- index 12a1343..a59e834 100644

//Synthetic comment -- @@ -116,6 +116,7 @@
sSupportedContentTypes.add(VIDEO_3GPP);
sSupportedContentTypes.add(VIDEO_3G2);
sSupportedContentTypes.add(VIDEO_H263);
        sSupportedContentTypes.add(AUDIO_MP4);
sSupportedContentTypes.add(VIDEO_MP4);

sSupportedContentTypes.add(APP_SMIL);







