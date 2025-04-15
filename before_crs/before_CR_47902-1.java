/*fix res folder ordering in getResourceInputs

The current order of the res folders returned by this function doesn't work well with the CrunchResourcesTask. This task issues a aapt crunch command and that command assumes a different order of folders.
E.g.: getResourceInputs will produce something like this: flavor1 ,main and PNGs in main will override those in flavor1
Now it will produce something like this: main, flavor1 and PNGs in flavor1 will override those in main

Change-Id:I660468628a547c1823b761ff98ecc57e25f57a90*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/VariantConfiguration.java b/builder/src/main/java/com/android/builder/VariantConfiguration.java
//Synthetic comment -- index 7088f6c..840ef86 100644

//Synthetic comment -- @@ -482,29 +482,29 @@
public List<File> getResourceInputs() {
List<File> inputs = Lists.newArrayList();

        if (mBuildTypeSourceProvider != null) {
            File typeResLocation = mBuildTypeSourceProvider.getResourcesDir();
            if (typeResLocation != null) {
                inputs.add(typeResLocation);
}
}

        for (SourceProvider sourceProvider : mFlavorSourceProviders) {
File flavorResLocation = sourceProvider.getResourcesDir();
if (flavorResLocation != null) {
inputs.add(flavorResLocation);
}
}

        File mainResLocation = mDefaultSourceProvider.getResourcesDir();
        if (mainResLocation != null) {
            inputs.add(mainResLocation);
        }

        for (AndroidDependency dependency : mFlatLibraries) {
            File resFolder = dependency.getResFolder();
            if (resFolder != null) {
                inputs.add(resFolder);
}
}








