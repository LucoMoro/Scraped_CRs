/*fixing non-telephony device test failures

Some of the telephony tests are not-applicable to non-telephony devices.

Change-Id:I527fbd93cfc51c982df8cef94b924783f2272288*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 6c13007..f84a0f6 100644

//Synthetic comment -- @@ -444,12 +444,18 @@
private static final String ISO_COUNTRY_CODE_PATTERN = "[a-z]{2}";

public void testGetNetworkCountryIso() {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) 
            // TODO: workaround for non-telephony devices
        return;
String countryCode = mTelephonyManager.getNetworkCountryIso();
assertTrue("Country code '" + countryCode + "' did not match " + ISO_COUNTRY_CODE_PATTERN,
Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
}

public void testGetSimCountryIso() {
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) 
            // TODO: workaround for non-telephony devices
        return;
String countryCode = mTelephonyManager.getSimCountryIso();
assertTrue("Country code '" + countryCode + "' did not match " + ISO_COUNTRY_CODE_PATTERN,
Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));







