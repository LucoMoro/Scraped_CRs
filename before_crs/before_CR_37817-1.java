/*SDK: don't override user.home property in Unit Tests.

Apparently that's not even needed anymore, it might
be a left-over from a previous test.

Change-Id:I622fbba13975d14cd45a4724751abdd33756824f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/SdkManagerTestCase.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/SdkManagerTestCase.java
//Synthetic comment -- index 3a5de9a..b00dfc9 100755

//Synthetic comment -- @@ -142,7 +142,6 @@
sdkDir.mkdirs();

AndroidLocation.resetFolder();
        System.setProperty("user.home", sdkDir.getAbsolutePath());
File addonsDir = new File(sdkDir, SdkConstants.FD_ADDONS);
addonsDir.mkdir();
File toolsLibEmuDir = new File(sdkDir, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");







