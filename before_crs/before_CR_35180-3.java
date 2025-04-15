/*Added world writable dirs from Pantech Element
Mispelled "defualt" is intentional

FileSystemPermissionTest:
add /data/local/mono
add /data/local/mono/pulse
add /data/local/purple
add /data/local/purple/sound
add /data/local/skel
add /data/local/skel/default
add /data/local/skel/defualt

Change-Id:If48ddffd4674a8e9a9e4cf45bda9fbc804cbe2bcSigned-off-by: Jon Sawyer <jon@cunninglogic.com>*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 4b86fbb..e89f9fb 100644

//Synthetic comment -- @@ -1,4 +1,4 @@
/*
* Copyright (C) 2010 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
//Synthetic comment -- @@ -240,7 +240,14 @@
"/data/local/logs/logcat",
"/data/local/logs/resetlog",
"/data/local/logs/smem",
"/data/local/rwsystag",
"/data/local/tmp",
"/data/local/tmp/com.nuance.android.vsuite.vsuiteapp",
"/data/log",







