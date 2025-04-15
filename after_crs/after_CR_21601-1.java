/*update requirements for TelephonyManager.getDeviceId

We allow all-decimal MEIDs (for LTE devices).

Change-Id:Ib9ae3ca1aacb34b2f2279ded0e6fb38ce602c40a*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 4cfddb1..6e8aa27 100644

//Synthetic comment -- @@ -288,7 +288,7 @@
int phoneType = mTelephonyManager.getPhoneType();
switch (phoneType) {
case TelephonyManager.PHONE_TYPE_GSM:
                assertGsmDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_CDMA:
//Synthetic comment -- @@ -306,17 +306,15 @@
}
}

    private static void assertGsmDeviceId(String deviceId) {
        // IMEI may include the check digit
        String imeiPattern = "[0-9]{14,15}";
assertTrue("IMEI device id " + deviceId + " does not match pattern " + imeiPattern,
Pattern.matches(imeiPattern, deviceId));
        if (deviceId.length() == 15) {
            // if the ID is 15 digits, the 15th must be a check digit.
            assertImeiCheckDigit(deviceId);
        }
}

private static void assertImeiCheckDigit(String deviceId) {
//Synthetic comment -- @@ -356,19 +354,10 @@
return sum == 0 ? 0 : 10 - sum;
}

private static void assertCdmaDeviceId(String deviceId) {
        // CDMA device IDs may either be a 14-hex-digit MEID or an
        // 8-hex-digit ESN.  If it's an ESN, it may not be a
        // pseudo-ESN.
if (deviceId.length() == 14) {
assertMeidFormat(deviceId);
} else if (deviceId.length() == 8) {
//Synthetic comment -- @@ -387,18 +376,10 @@
}

private static void assertMeidFormat(String deviceId) {
        // MEID must NOT include the check digit.
        String meidPattern = "[0-9a-fA-F]{14}";
        assertTrue("MEID device id " + deviceId + " does not match pattern " + meidPattern,
                   Pattern.matches(meidPattern, deviceId));
}

private void assertSerialNumber() {







