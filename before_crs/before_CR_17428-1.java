/*Fix InstrumentationCtsTestRunner NPE

...when iterating over the features using the emulator.

Change-Id:Id1958239786c900b72e0b9244602a7f66c485ed1*/
//Synthetic comment -- diff --git a/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java b/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java
//Synthetic comment -- index 660cb0b..f2098ea 100644

//Synthetic comment -- @@ -297,8 +297,10 @@
// Run the test only if the device supports all the features.
PackageManager packageManager = getContext().getPackageManager();
FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
                for (FeatureInfo featureInfo : featureInfos) {
                    features.remove(featureInfo.name);
}
return features.isEmpty();
}







