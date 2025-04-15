/*Updated features test to check new features reported in Gingerbread to fix warning of device reporting a disallowed feature for front-facing camera, nfc, gyroscope, sip, etc).

Change-Id:Id10bf78dd0681c739e8f313d8e602665b1bf3dcf*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index 4f6e0a3..c83a470 100644

//Synthetic comment -- @@ -92,8 +92,9 @@
};

/**
     * A list of all features added in FroYo (API=8). Because we want to run on
     * Eclair devices, we can't use static references to constants added later
* than Eclair. We could use Reflection, but we'd still need a list of
* string literals (for constant names) anyway, and there's little point in
* using Reflection to to look up a constant String value for a constant
//Synthetic comment -- @@ -113,6 +114,17 @@
new Feature("android.hardware.wifi", false),
};

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -147,6 +159,9 @@
if (apiVersion >= Build.VERSION_CODES.FROYO) {
Collections.addAll(features, ALL_FROYO_FEATURES);
}
for (Feature f : features) {
HashMap<String, Object> row = new HashMap<String, Object>();
listViewData.add(row);








//Synthetic comment -- diff --git a/apps/CtsVerifier/tests/src/com/android/cts/verifier/features/FeatureSummaryActivityTest.java b/apps/CtsVerifier/tests/src/com/android/cts/verifier/features/FeatureSummaryActivityTest.java
//Synthetic comment -- index ca7ced4..d05075b 100644

//Synthetic comment -- @@ -43,6 +43,11 @@
actualFeatures.add(feature.name);
}
}

assertEquals("Feature list needs to be updated.",
expectedFeatures.size(), actualFeatures.size());







