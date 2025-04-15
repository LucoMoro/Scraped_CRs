/*Fix unit test for LocalSdkParser.

Change-Id:I630cb4fc123e3d6b6efc36eea543f7556b2ccc87*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/LocalSdkParserTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/LocalSdkParserTest.java
//Synthetic comment -- index 645d2bb..4989ec3 100755

//Synthetic comment -- @@ -36,7 +36,8 @@
// a legacy armeabi system image (this is not a separate system image package)

assertEquals(
                "[Android SDK Tools, revision 0, " +
                 "SDK Platform Android 0.0, API 0, revision 1, " +
"Sources for Android SDK, API 0, revision 0]",
Arrays.toString(parser.parseSdk(sdkman.getLocation(), sdkman, monitor)));

//Synthetic comment -- @@ -63,7 +64,7 @@
monitor)));

assertEquals(
                "[Android SDK Tools, revision 0]",
Arrays.toString(parser.parseSdk(sdkman.getLocation(),
sdkman,
LocalSdkParser.PARSE_TOOLS,
//Synthetic comment -- @@ -82,7 +83,8 @@
t = sdkman.getTargets()[0];

assertEquals(
                "[Android SDK Tools, revision 0, " +
                 "SDK Platform Android 0.0, API 0, revision 1, " +
"Sources for Android SDK, API 0, revision 0]",
Arrays.toString(parser.parseSdk(sdkman.getLocation(), sdkman, monitor)));

//Synthetic comment -- @@ -98,7 +100,8 @@
sdkman.reloadSdk(getLog());

assertEquals(
                "[Android SDK Tools, revision 0, " +
                 "SDK Platform Android 0.0, API 0, revision 1, " +
"ARM EABI v7a System Image, Android API 0, revision 0, " +
"ARM EABI System Image, Android API 0, revision 0, " +
"Sources for Android SDK, API 0, revision 0]",
//Synthetic comment -- @@ -112,7 +115,8 @@
sdkman, t, LocationType.IN_SYSTEM_IMAGE, SdkConstants.ABI_INTEL_ATOM));

assertEquals(
                "[Android SDK Tools, revision 0, " +
                 "SDK Platform Android 0.0, API 0, revision 1, " +
"ARM EABI v7a System Image, Android API 0, revision 0, " +
"ARM EABI System Image, Android API 0, revision 0, " +
"Sources for Android SDK, API 0, revision 0, " +
//Synthetic comment -- @@ -120,7 +124,8 @@
Arrays.toString(parser.parseSdk(sdkman.getLocation(), sdkman, monitor)));

assertEquals(
                "[Android SDK Tools, revision 0, " +
                 "SDK Platform Android 0.0, API 0, revision 1, " +
"ARM EABI v7a System Image, Android API 0, revision 0, " +
"ARM EABI System Image, Android API 0, revision 0, " +
"Sources for Android SDK, API 0, revision 0, " +







