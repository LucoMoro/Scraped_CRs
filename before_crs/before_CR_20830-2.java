/*Support Audio ogg type in Multimedia message

Change-Id:If9dcd59da854df64ea2ac25ed97309b7600e6421Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/ContentType.java b/core/java/com/google/android/mms/ContentType.java
//Synthetic comment -- index b066fad..5fad9d9 100644

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







