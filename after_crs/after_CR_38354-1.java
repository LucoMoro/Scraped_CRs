/*Fix CTS case testUsbAccessory of SystemFeatureTest

According to Compatibility Definition Document,
Usb Accessory is not MUST.

Change-Id:If09755f14cf60b8f45c613436f31a03defa3fb8f*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index a68cfb8..76af30f 100644

//Synthetic comment -- @@ -338,10 +338,6 @@
// TODO: Add tests for the other touchscreen features.
}

public void testWifiFeature() throws Exception {
if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
// no WiFi, skip the test







