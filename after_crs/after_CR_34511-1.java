/*BT: Adding support for wrf files to BT OPP

This patch adds support for wrf files to BT OPP.
Proper mime type /application/octet-stream with extension
wrf was added to webkit framework.

Change-Id:I2cdd961a78281129b96fba51d5df37c534c5c893Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/MimeUtils.java b/luni/src/main/java/libcore/net/MimeUtils.java
//Synthetic comment -- index 8371c68..4681bbc 100644

//Synthetic comment -- @@ -349,6 +349,7 @@
add("video/x-ms-wvx", "wvx");
add("video/x-msvideo", "avi");
add("video/x-sgi-movie", "movie");
        add("video/x-webex", "wrf");
add("x-conference/x-cooltalk", "ice");
add("x-epoc/x-sisx-app", "sisx");
applyOverrides();







