/*Fix FEATURES test(USB Accessory) in CTS Verifier

According to Compatibility Definition Document,
android.hardware.usb.accessory is not MUST.

Change-Id:Ieba797d151fa460a3958a4725066b9a3c1903062*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index 3ea9451..e541069 100644

//Synthetic comment -- @@ -166,6 +166,7 @@

public static final Feature[] ALL_ICE_CREAM_SANDWICH_FEATURES = {
new Feature(PackageManager.FEATURE_WIFI_DIRECT, false),
};

@Override







