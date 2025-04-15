/*Let ApkBuilder package bitcode library into APK.

Change-Id:I54e99cff338a62147af3247446148d88a73a4ddb*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index 626e61f..f5abe9e 100644

//Synthetic comment -- @@ -51,6 +51,8 @@

private final static Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
Pattern.CASE_INSENSITIVE);
    private final static Pattern PATTERN_BITCODELIB_EXT = Pattern.compile("^.+\\.bc$",
            Pattern.CASE_INSENSITIVE);

/**
* A No-op zip filter. It's used to detect conflicts.
//Synthetic comment -- @@ -125,7 +127,7 @@
mAddedFiles.put(archivePath, mInputFile);
}

                if (archivePath.endsWith(".so") || archivePath.endsWith(".bc")) {
mNativeLibs.add(archivePath);

// only .so located in lib/ will interfere with the installation
//Synthetic comment -- @@ -677,6 +679,7 @@
// are gdbserver executables
if (lib.isFile() &&
(PATTERN_NATIVELIB_EXT.matcher(lib.getName()).matches() ||
                                    PATTERN_BITCODELIB_EXT.matcher(lib.getName()).matches() ||
(mDebugMode &&
SdkConstants.FN_GDBSERVER.equals(
lib.getName())))) {
//Synthetic comment -- @@ -751,6 +754,7 @@
// are gdbserver executables
if (lib.isFile() &&
(PATTERN_NATIVELIB_EXT.matcher(lib.getName()).matches() ||
                                    PATTERN_BITCODELIB_EXT.matcher(lib.getName()).matches() ||
(debugMode &&
SdkConstants.FN_GDBSERVER.equals(
lib.getName())))) {







