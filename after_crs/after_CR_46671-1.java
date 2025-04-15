/*Adjust the build and target comboboxes to ensure >= minSdkVersion

Change-Id:Ia0b0d611dfb5aa1952ebae573355be1726b4f342*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index e698ac1..0b003f3 100644

//Synthetic comment -- @@ -546,9 +546,6 @@
Object source = e.getSource();
if (source == mMinSdkCombo) {
mValues.minSdk = getSelectedMinSdk();
Integer minSdk = mMinNameToApi.get(mValues.minSdk);
if (minSdk == null) {
try {
//Synthetic comment -- @@ -559,6 +556,49 @@
}
mValues.iconState.minSdk = minSdk.intValue();
mValues.minSdkLevel = minSdk.intValue();

            // If higher than build target, adjust build target
            if (mValues.minSdkLevel > mValues.getBuildApi()) {
                // Try to find a build target with an adequate build API
                IAndroidTarget[] targets = (IAndroidTarget[]) mBuildSdkCombo.getData();
                IAndroidTarget best = null;
                int bestApi = Integer.MAX_VALUE;
                int bestTargetIndex = -1;
                for (int i = 0; i < targets.length; i++) {
                    IAndroidTarget target = targets[i];
                    if (!target.isPlatform()) {
                        continue;
                    }
                    int api = target.getVersion().getApiLevel();
                    if (api >= mValues.minSdkLevel && api < bestApi) {
                        best = target;
                        bestApi = api;
                        bestTargetIndex = i;
                    }
                }

                if (best != null) {
                    assert bestTargetIndex != -1;
                    mValues.target = best;
                    try {
                        mIgnore = true;
                        mBuildSdkCombo.select(bestTargetIndex);
                    } finally {
                        mIgnore = false;
                    }
                }
            }

            // If higher than targetSdkVersion, adjust targetSdkVersion
            if (mValues.minSdkLevel > mValues.targetSdkLevel) {
                mValues.targetSdkLevel = mValues.minSdkLevel;
                try {
                    mIgnore = true;
                    setSelectedTargetSdk(mValues.targetSdkLevel);
                } finally {
                    mIgnore = false;
                }
            }
} else if (source == mBuildSdkCombo) {
mValues.target = getSelectedBuildTarget();

//Synthetic comment -- @@ -614,6 +654,10 @@
mMinSdkCombo.select(api - 1); // -1: First API level (at index 0) is 1
}

    private void setSelectedTargetSdk(int api) {
        mTargetSdkCombo.select(api - 1); // -1: First API level (at index 0) is 1
    }

@Nullable
private IAndroidTarget getSelectedBuildTarget() {
IAndroidTarget[] targets = (IAndroidTarget[]) mBuildSdkCombo.getData();







