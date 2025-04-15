/*add RTSP scheme

When user types rtsp scheme at the address tool bar, the url is changed
to http scheme. To prevent this, RTSP scheme handling code is added.

Change-Id:I97edc4f4249f8bab9a8657b529473814f8938cc1*/




//Synthetic comment -- diff --git a/core/java/android/webkit/URLUtil.java b/core/java/android/webkit/URLUtil.java
//Synthetic comment -- index b47f04f..7867e6f 100644

//Synthetic comment -- @@ -58,6 +58,8 @@
if (inUrl.startsWith("file:")) return inUrl;
// Do not try to interpret javascript scheme URLs
if (inUrl.startsWith("javascript:")) return inUrl;
        // Do not try to interpret rtsp scheme URLs
        if (inUrl.startsWith("rtsp:")) return inUrl;

// bug 762454: strip period off end of url
if (inUrl.endsWith(".") == true) {







