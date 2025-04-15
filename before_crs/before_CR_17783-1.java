/*SDK Manager: local package list can be empty.

Change-Id:I44a063ffb69002b10c7db5141036e82f23e7da49*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 2bf377a..d2999bd 100755

//Synthetic comment -- @@ -109,31 +109,33 @@
float currentAddonScore = 0;
float currentDocScore = 0;
HashMap<String, Float> currentExtraScore = new HashMap<String, Float>();
        for (Package p : localPkgs) {
            int rev = p.getRevision();
            int api = 0;
            boolean isPreview = false;
            if (p instanceof IPackageVersion) {
                AndroidVersion vers = ((IPackageVersion) p).getVersion();
                api = vers.getApiLevel();
                isPreview = vers.isPreview();
            }

            // The score is 10*api + (1 if preview) + rev/100
            // This allows previews to rank above a non-preview and
            // allows revisions to rank appropriately.
            float score = api * 10 + (isPreview ? 1 : 0) + rev/100.f;

            if (p instanceof PlatformPackage) {
                currentPlatformScore = Math.max(currentPlatformScore, score);
            } else if (p instanceof SamplePackage) {
                currentSampleScore = Math.max(currentSampleScore, score);
            } else if (p instanceof AddonPackage) {
                currentAddonScore = Math.max(currentAddonScore, score);
            } else if (p instanceof ExtraPackage) {
                currentExtraScore.put(((ExtraPackage) p).getPath(), score);
            } else if (p instanceof DocPackage) {
                currentDocScore = Math.max(currentDocScore, score);
}
}








