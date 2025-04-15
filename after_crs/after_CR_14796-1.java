/*Test to Verify Device IMEI/MEID

Bug 2694172

Test that TelephonyManager#getDeviceId returns either a 14-15
long IMEI id for GSM phones, 14-15 hex MEID id for CDMA, or 18-19
decimal MEID ids. Check that a valid MAC address is returned by
devices that are neither GSM or CDMA.

Change-Id:I68a9973bfa4d9fdd205f1486289c5d03c301ac50*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 90b2bf9..f3b6331 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.os.cts.TestThread;
import android.telephony.CellLocation;
//Synthetic comment -- @@ -29,6 +31,8 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.regex.Pattern;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
private TelephonyManager mTelephonyManager;
//Synthetic comment -- @@ -266,4 +270,103 @@
mTelephonyManager.getDeviceId();
mTelephonyManager.getDeviceSoftwareVersion();
}

    /**
     * Tests that the device properly reports either a valid IMEI if GSM,
     * a valid MEID if CDMA, or a valid MAC address if only a WiFi device.
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getDeviceId",
        args = {}
    )
    public void testGetDeviceId() {
        int phoneType = mTelephonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                assertImeiDeviceId();
                break;

            case TelephonyManager.PHONE_TYPE_CDMA:
                assertMeidDeviceId();
                break;

            case TelephonyManager.PHONE_TYPE_NONE:
                assertMacAddressReported();
                break;

            default:
                throw new IllegalArgumentException("Did you add a new phone type? " + phoneType);
        }
    }

    private void assertImeiDeviceId() {
        String deviceId = mTelephonyManager.getDeviceId();

        String imeiPattern = "[0-9]{14,15}"; // Optional check digit could be reported...
        assertTrue("IMEI device id " + deviceId + " does not match pattern " + imeiPattern,
                Pattern.matches(imeiPattern, deviceId));

        int regionalCode = Integer.parseInt(deviceId.substring(0, 2), 16);
        assertTrue(String.format("IMEI RR %x should be < than %x", regionalCode, 0xA0),
                regionalCode < 0xA0);
    }

    private void assertMeidDeviceId() {
        String deviceId = mTelephonyManager.getDeviceId();
        if (isDecimalMeidFormat(deviceId)) {
            assertDecimalMeidFormat(deviceId);
        } else {
            assertHexadecimalMeidFormat(deviceId);
        }
    }

    private boolean isDecimalMeidFormat(String deviceId) {
        return deviceId.length() >= 18;
    }

    private void assertHexadecimalMeidFormat(String deviceId) {
        String meidPattern = "[0-9a-fA-F]{14,15}"; // Optional check digit could be reported...
        assertTrue("MEID hex device id " + deviceId + " does not match pattern " + meidPattern,
                Pattern.matches(meidPattern, deviceId));

        int regionalCode = Integer.parseInt(deviceId.substring(0, 2), 16);
        assertTrue(String.format("MEID RR %x should be >= than %x", regionalCode, 0xA0),
                regionalCode >= 0xA0);
    }

    private void assertDecimalMeidFormat(String deviceId) {
        String meidPattern = "[0-9]{18,19}"; // Optional check digit could be reported...
        assertTrue("MEID dec device id " + deviceId + " does not match pattern " + meidPattern,
                Pattern.matches(meidPattern, deviceId));
    }

    private void assertMacAddressReported() {
        String macAddress = getMacAddress();
        String macPattern = "([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}";
        assertTrue("MAC Address " + macAddress + " does not match pattern " + macPattern,
                Pattern.matches(macPattern, macAddress));
    }

    /** @return mac address which requires the WiFi system to be enabled */
    private String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getContext()
                .getSystemService(Context.WIFI_SERVICE);

        boolean enabled = wifiManager.isWifiEnabled();

        try {
            if (!enabled) {
                wifiManager.setWifiEnabled(true);
            }

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();

        } finally {
            if (!enabled) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }
}







