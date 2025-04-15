/*Adjust the build and target comboboxes to ensure >= minSdkVersion

Change-Id:Ia0b0d611dfb5aa1952ebae573355be1726b4f342*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index e698ac1..0b003f3 100644

//Synthetic comment -- @@ -546,9 +546,6 @@
Object source = e.getSource();
if (source == mMinSdkCombo) {
mValues.minSdk = getSelectedMinSdk();
            // If higher than build target, adjust build target
            // TODO: implement

Integer minSdk = mMinNameToApi.get(mValues.minSdk);
if (minSdk == null) {
try {
//Synthetic comment -- @@ -559,6 +556,49 @@
}
mValues.iconState.minSdk = minSdk.intValue();
mValues.minSdkLevel = minSdk.intValue();
} else if (source == mBuildSdkCombo) {
mValues.target = getSelectedBuildTarget();

//Synthetic comment -- @@ -614,6 +654,10 @@
mMinSdkCombo.select(api - 1); // -1: First API level (at index 0) is 1
}

@Nullable
private IAndroidTarget getSelectedBuildTarget() {
IAndroidTarget[] targets = (IAndroidTarget[]) mBuildSdkCombo.getData();







