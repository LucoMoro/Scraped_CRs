/*Support Audio ogg type in Multimedia message

Change-Id:If9dcd59da854df64ea2ac25ed97309b7600e6421*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/ContentType.java b/core/java/com/google/android/mms/ContentType.java
old mode 100644
new mode 100755
//Synthetic comment -- index b066fad..ca4d6d8

//Synthetic comment -- @@ -180,15 +180,15 @@
}

public static boolean isImageType(String contentType) {
        return (null != contentType) && contentType.startsWith("image/");
}

public static boolean isAudioType(String contentType) {
        return (null != contentType) && contentType.startsWith("audio/");
}

public static boolean isVideoType(String contentType) {
        return (null != contentType) && contentType.startsWith("video/");
}

public static boolean isDrmType(String contentType) {







