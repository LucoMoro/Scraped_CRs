/*Add support for soft properties in multi-apk export.

Change-Id:If68bc520ba4014bbd4b144e71d54bc1161a56e2d*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index f2cd97f..fe5a021 100644

//Synthetic comment -- @@ -52,6 +52,9 @@
private int mGlVersion = ManifestData.GL_ES_VERSION_NOT_SET;
private SupportsScreens mSupportsScreens;

    // additional apk generation that doesn't impact the build info.
    private boolean mSplitDensity;

ApkData() {
// do nothing.
}







