/*Audio ogg type support

Change-Id:Id35241768c478604b9edad71b26b4466b8e7b015*/




//Synthetic comment -- diff --git a/core/java/com/google/android/mms/ContentType.java b/core/java/com/google/android/mms/ContentType.java
//Synthetic comment -- index b066fad..275bf68 100644

//Synthetic comment -- @@ -180,15 +180,15 @@
}

public static boolean isImageType(String contentType) {
        return (null != contentType) && contentType.startsWith("image/")||sSupportedImageTypes.contains(contentType));
}

public static boolean isAudioType(String contentType) {
        return (null != contentType) && contentType.startsWith("audio/")||sSupportedAudioTypes.contains(contentType));
}

public static boolean isVideoType(String contentType) {
        return (null != contentType) && contentType.startsWith("video/")||sSupportedVideoTypes.contains(contentType));
}

public static boolean isDrmType(String contentType) {







