/*Fix NPE when comparing project export properties with api split = false.

Change-Id:I1d3135737e7c13503e0fe4e0559c62959014cd83*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java
//Synthetic comment -- index c25af2e..d883f5c 100644

//Synthetic comment -- @@ -316,13 +316,15 @@
if (mSplitByAbi != Boolean.valueOf(abis[0])) { // first value is always the split boolean
return "Property split.abi changed";
}
        // now compare the rest.
        if (abis.length - 1 != mAbis.size()) {
            return "The number of ABIs available in the project changed";
        }
        for (int i = 1 ; i < abis.length ; i++) {
            if (mAbis.indexOf(abis[i]) == -1) {
                return "The list of ABIs available in the project changed";
}
}








