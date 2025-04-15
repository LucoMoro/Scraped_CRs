/*allow TelephonyManager.getDeviceId to return an ESN instead of an MEID

Older CDMA networks use ESNs only, not MEIDs.

Change-Id:I12f7f4df730eafc08e8923d0248ddd82bb7bde18*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index a1d6432..ebc33a6 100644

//Synthetic comment -- @@ -275,8 +275,9 @@
}

/**
     * Tests that the device properly reports either a valid IMEI if GSM,
     * a valid MEID if CDMA, or a valid MAC address if only a WiFi device.
*/
@TestTargetNew(
level = TestLevel.COMPLETE,
//Synthetic comment -- @@ -292,7 +293,7 @@
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                assertMeidDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:
//Synthetic comment -- @@ -368,9 +369,23 @@
assertEquals(message, decimalIdentifier, reportingBodyIdentifier < decimalBound);
}

    private static void assertMeidDeviceId(String deviceId) {
        assertHexadecimalMeidFormat(deviceId);
        assertReportingBodyIdentifier(deviceId, false); // Must be hexadecimal identifier
}

private static void assertHexadecimalMeidFormat(String deviceId) {







