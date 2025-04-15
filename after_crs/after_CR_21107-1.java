/*Add a conditional expression of whether it's PHONE_TYPE_CDMA.

Change-Id:I260bda2e5f9e31df1e8215f5ea08901592190c88*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index ae3d940..9c21cb1 100644

//Synthetic comment -- @@ -442,8 +442,10 @@

public void testGetNetworkCountryIso() {
String countryCode = mTelephonyManager.getNetworkCountryIso();
        if (mTelephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
            assertTrue("Country code '" + countryCode + "' did not match " + ISO_COUNTRY_CODE_PATTERN,
                    Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
        }
}

public void testGetSimCountryIso() {







