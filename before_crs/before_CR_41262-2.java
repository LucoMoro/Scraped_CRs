/*"audio/mp4" missing from array of supported content types.

"audio/mp4" is included in the array of supported audio types,
but isn't included in the array of supproted content types,
causing problems with proper use of MP4 audio in MMS.

Change-Id:I7be9ac581fcf7594234000d37f56d901174d54c0*/
//Synthetic comment -- diff --git a/src/java/com/google/android/mms/ContentType.java b/src/java/com/google/android/mms/ContentType.java
//Synthetic comment -- index 12a1343..51e52be 100644

//Synthetic comment -- @@ -100,6 +100,7 @@
sSupportedContentTypes.add(AUDIO_MID);
sSupportedContentTypes.add(AUDIO_MIDI);
sSupportedContentTypes.add(AUDIO_MP3);
sSupportedContentTypes.add(AUDIO_MPEG3);
sSupportedContentTypes.add(AUDIO_MPEG);
sSupportedContentTypes.add(AUDIO_MPG);







