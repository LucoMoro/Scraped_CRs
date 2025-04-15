/*Added world writable dirs from Pantech Element

FileSystemPermissionTest:
add /data/local/mono
add /data/local/mono/pulse
add /data/local/purple
add /data/local/purple/sound
add /data/local/skel
add /data/local/skel/defualt

Change-Id:If48ddffd4674a8e9a9e4cf45bda9fbc804cbe2bcSigned-off-by: Jon Sawyer <jon@cunninglogic.com>*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 4b86fbb..3c5a77c 100644

//Synthetic comment -- @@ -240,7 +240,13 @@
"/data/local/logs/logcat",
"/data/local/logs/resetlog",
"/data/local/logs/smem",
                    "/data/local/mono",
                    "/data/local/mono/pulse",
                    "/data/local/purple",
                    "/data/local/purple/sound",
"/data/local/rwsystag",
                    "/data/local/skel",
                    "/data/local/skel/defualt",
"/data/local/tmp",
"/data/local/tmp/com.nuance.android.vsuite.vsuiteapp",
"/data/log",







