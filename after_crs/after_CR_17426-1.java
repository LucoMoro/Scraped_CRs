/*Backport TelephonyManagerTest Enhancements

- Support MEIDS Starting with 97, 98, and 99 (Bug 3041176)
- Checking of ESNs
- Probe "hardware_id" for non-telephony devices

Change-Id:If0ef902bd5262348fb82e93230fec8683a4e2c09*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 9ecbf20..b89124e 100644

//Synthetic comment -- @@ -21,11 +21,13 @@
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.os.cts.TestThread;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -289,10 +291,12 @@
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                assertCdmaDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:
                assertNull(deviceId);
                assertHardwareId();
assertMacAddressReported();
break;

//Synthetic comment -- @@ -363,16 +367,48 @@
assertEquals(message, decimalIdentifier, reportingBodyIdentifier < decimalBound);
}

    private static void assertCdmaDeviceId(String deviceId) {
        if (deviceId.length() == 14) {
            assertMeidFormat(deviceId);
        } else if (deviceId.length() == 8) {
            assertHexadecimalEsnFormat(deviceId);
        } else {
            fail("device id on CDMA must be 14-digit hex MEID or 8-digit hex ESN.");
        }
}

    private static void assertHexadecimalEsnFormat(String deviceId) {
        String esnPattern = "[0-9a-fA-F]{8}";
        assertTrue("ESN hex device id " + deviceId + " does not match pattern " + esnPattern,
                   Pattern.matches(esnPattern, deviceId));
        assertFalse("ESN hex device id " + deviceId + " must not be a pseudo-ESN",
                    "80".equals(deviceId.substring(0, 2)));
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

    private void assertHardwareId() {
        ContentResolver resolver = mContext.getContentResolver();
        String hardwareId = Settings.Secure.getString(resolver, "hardware_id");
        assertNotNull("Non-telephony devices must define a Settings.Secure 'hardware_id' property.",
                hardwareId);
        assertTrue("Hardware id must be no longer than 20 characters.",
                hardwareId.length() <= 20);
        assertTrue("Hardware id must be alphanumeric.",
                Pattern.matches("[0-9A-Za-z]+", hardwareId));
}

private void assertMacAddressReported() {







