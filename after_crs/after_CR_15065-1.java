/*Fix ABI ordering for the multi-apk build info.

Change-Id:I3241ec881549218f4cb21ce723c257ee69b9f8a5*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index c32a360..e46559b 100644

//Synthetic comment -- @@ -41,6 +41,18 @@
private static final String PROP_BUILDINFO = "buildinfo";
private static final String PROP_DENSITY = "splitDensity";

    /**
     * List of ABI order.
     * This is meant to be a list of CPU/CPU2 to indicate the order required by the build info.
     * If the ABI being compared in {@link #compareTo(ApkData)} are in the same String array,
     * then the value returned must ensure that the {@link ApkData} will ordered the same as they
     * array.
     * If the ABIs are not in the same array, any order can be returned.
     */
    private static final String[][] ABI_SORTING = new String[][] {
        new String[] { "armeabi", "armeabi-v7a" }
    };

private final HashMap<String, String> mOutputNames = new HashMap<String, String>();
private String mRelativePath;
private File mProject;
//Synthetic comment -- @@ -213,21 +225,13 @@
return minSdkDiff;
}

        // only compare if they have don't have the same size support. This is because
        // this compare method throws an exception if the values cannot be compared.
        if (mSupportsScreens.hasSameScreenSupportAs(o.mSupportsScreens) == false) {
            return mSupportsScreens.compareScreenSizesWith(o.mSupportsScreens);
}

        int comp;
if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
if (o.mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
comp = mGlVersion - o.mGlVersion;
//Synthetic comment -- @@ -239,6 +243,35 @@
return 1;
}

        // here the returned value is only important if both abi are non null.
        if (mAbi != null && o.mAbi != null) {
            comp = compareAbi(mAbi, o.mAbi);
            if (comp != 0) return comp;
        }

        return 0;
    }

    private int compareAbi(String abi, String abi2) {
        // look for the abis in each of the ABI sorting array
        for (String[] abiArray : ABI_SORTING) {
            int abiIndex = -1, abiIndex2 = -1;
            final int count = abiArray.length;
            for (int i = 0 ; i < count ; i++) {
                if (abiArray[i].equals(abi)) {
                    abiIndex = i;
                }
                if (abiArray[i].equals(abi2)) {
                    abiIndex2 = i;
                }
            }

            // if both were found
            if (abiIndex != -1 && abiIndex != -1) {
                return abiIndex - abiIndex2;
            }
        }

return 0;
}








