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

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.os.cts.TestThread;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -289,10 +291,12 @@
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                assertMeidDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:
assertMacAddressReported();
break;

//Synthetic comment -- @@ -363,16 +367,48 @@
assertEquals(message, decimalIdentifier, reportingBodyIdentifier < decimalBound);
}

    private static void assertMeidDeviceId(String deviceId) {
        assertHexadecimalMeidFormat(deviceId);
        assertReportingBodyIdentifier(deviceId, false); // Must be hexadecimal identifier
}

    private static void assertHexadecimalMeidFormat(String deviceId) {
        // MEID must NOT include the check digit.
        String meidPattern = "[0-9a-fA-F]{14}";
        assertTrue("MEID hex device id " + deviceId + " does not match pattern " + meidPattern,
                Pattern.matches(meidPattern, deviceId));
}

private void assertMacAddressReported() {







