/*update requirements for TelephonyManager.getDeviceId

We allow all-decimal MEIDs (for LTE devices).

Change-Id:Ib9ae3ca1aacb34b2f2279ded0e6fb38ce602c40a*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 4cfddb1..6e8aa27 100644

//Synthetic comment -- @@ -288,7 +288,7 @@
int phoneType = mTelephonyManager.getPhoneType();
switch (phoneType) {
case TelephonyManager.PHONE_TYPE_GSM:
                assertImeiDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_CDMA:
//Synthetic comment -- @@ -306,17 +306,15 @@
}
}

    private static void assertImeiDeviceId(String deviceId) {
        assertImeiFormat(deviceId);
        assertImeiCheckDigit(deviceId);
        assertReportingBodyIdentifier(deviceId, true); // Must be decimal identifier
    }

    private static void assertImeiFormat(String deviceId) {
        // IMEI must include the check digit
        String imeiPattern = "[0-9]{15}";
assertTrue("IMEI device id " + deviceId + " does not match pattern " + imeiPattern,
Pattern.matches(imeiPattern, deviceId));
}

private static void assertImeiCheckDigit(String deviceId) {
//Synthetic comment -- @@ -356,19 +354,10 @@
return sum == 0 ? 0 : 10 - sum;
}

    private static void assertReportingBodyIdentifier(String deviceId, boolean decimalIdentifier) {
        // Check the reporting body identifier
        int reportingBodyIdentifier = Integer.parseInt(deviceId.substring(0, 2), 16);
        int decimalBound = 0xA0;
        String message = String.format("%s RR %x not %s than %x",
                decimalIdentifier ? "IMEI" : "MEID",
                reportingBodyIdentifier,
                decimalIdentifier ? "<" : ">=",
                decimalBound);
        assertEquals(message, decimalIdentifier, reportingBodyIdentifier < decimalBound);
    }

private static void assertCdmaDeviceId(String deviceId) {
if (deviceId.length() == 14) {
assertMeidFormat(deviceId);
} else if (deviceId.length() == 8) {
//Synthetic comment -- @@ -387,18 +376,10 @@
}

private static void assertMeidFormat(String deviceId) {
        if (deviceId.substring(0, 2).matches("99|98|97")) {
            // MEID must NOT include the check digit.
            String meidPattern = "(99|98|97)[0-9]{12}";
            assertTrue("MEID device id " + deviceId + " does not match pattern " + meidPattern,
                    Pattern.matches(meidPattern, deviceId));
        } else {
            // MEID must NOT include the check digit.
            String meidPattern = "[0-9a-fA-F]{14}";
            assertTrue("MEID device id " + deviceId + " does not match pattern " + meidPattern,
                    Pattern.matches(meidPattern, deviceId));
            assertReportingBodyIdentifier(deviceId, false);
        }
}

private void assertSerialNumber() {







