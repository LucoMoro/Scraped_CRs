/*Adding a conditional expression of whether it's PHONE_TYPE_CDMA or not.*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 0528897..838cd76 100644

//Synthetic comment -- @@ -448,9 +448,11 @@
PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getNetworkCountryIso();
if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            assertTrue("Country code '" + countryCode + "' did not match "
                    + ISO_COUNTRY_CODE_PATTERN,
                    Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
} else {
// Non-telephony may still have the property defined if it has a SIM.
}







