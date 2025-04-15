/*Fix ABI ordering for the multi-apk build info.

Change-Id:I3241ec881549218f4cb21ce723c257ee69b9f8a5*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index c32a360..4ba59b4 100644

//Synthetic comment -- @@ -41,6 +41,18 @@
private static final String PROP_BUILDINFO = "buildinfo";
private static final String PROP_DENSITY = "splitDensity";

private final HashMap<String, String> mOutputNames = new HashMap<String, String>();
private String mRelativePath;
private File mProject;
//Synthetic comment -- @@ -213,21 +225,13 @@
return minSdkDiff;
}

        int comp;
        if (mAbi != null) {
            if (o.mAbi != null) {
                comp = mAbi.compareTo(o.mAbi);
                if (comp != 0) return comp;
            } else {
                return -1;
            }
        } else if (o.mAbi != null) {
            return 1;
}

        comp = mSupportsScreens.compareScreenSizesWith(o.mSupportsScreens);
        if (comp != 0) return comp;

if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
if (o.mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
comp = mGlVersion - o.mGlVersion;
//Synthetic comment -- @@ -239,6 +243,35 @@
return 1;
}

return 0;
}








