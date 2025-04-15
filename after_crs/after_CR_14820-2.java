/*Verify Device IMEI/MEID Ids

Bug 2694172

Test that TelephonyManager#getDeviceId returns either a 15
long IMEI id for GSM phones, 14 hex MEID id for CDMA, or 18
decimal MEID ids. Check the Luhn check digit for IMEI ids.
Finally, check that a valid MAC address is returned by
devices that are neither GSM or CDMA.

Change-Id:I0a8696d76ece9802ac7c43111ed230c6a0412efb*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 90b2bf9..9b4c8dc 100644

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
//Synthetic comment -- @@ -266,4 +270,153 @@
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
        String deviceId = mTelephonyManager.getDeviceId();
        int phoneType = mTelephonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                assertImeiDeviceId(deviceId);
                break;

            case TelephonyManager.PHONE_TYPE_CDMA:
                assertMeidDeviceId(deviceId);
                break;

            case TelephonyManager.PHONE_TYPE_NONE:
                assertMacAddressReported();
                break;

            default:
                throw new IllegalArgumentException("Did you add a new phone type? " + phoneType);
        }
    }

    private static void assertImeiDeviceId(String deviceId) {
        assertImeiFormat(deviceId);
        assertImeiCheckDigit(deviceId);
        assertImeiReportingBodyIdentifier(deviceId);
    }

    private static void assertImeiFormat(String deviceId) {
        // IMEI must include the check digit
        String imeiPattern = "[0-9]{15}";
        assertTrue("IMEI device id " + deviceId + " does not match pattern " + imeiPattern,
                Pattern.matches(imeiPattern, deviceId));
    }

    private static void assertImeiCheckDigit(String deviceId) {
        int expectedCheckDigit = getLuhnCheckDigit(deviceId.substring(0, 14));
        int actualCheckDigit = Character.digit(deviceId.charAt(14), 10);
        assertEquals("Incorrect check digit for " + deviceId, expectedCheckDigit, actualCheckDigit);
    }

    /**
     * Use decimal value (0-9) to index into array to get sum of its digits
     * needed by Lunh check.
     *
     * Example: DOUBLE_DIGIT_SUM[6] = 3 because 6 * 2 = 12 => 1 + 2 = 3
     */
    private static final int[] DOUBLE_DIGIT_SUM = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

    /**
     * Calculate the check digit by starting from the right, doubling every
     * each digit, summing all the digits including the doubled ones, and
     * finding a number to make the sum divisible by 10.
     *
     * @param deviceId not including the check digit
     * @return the check digit
     */
    private static int getLuhnCheckDigit(String deviceId) {
        int sum = 0;
        for (int i = deviceId.length() - 1; i >= 0; --i) {
            int digit = Character.digit(deviceId.charAt(i), 10);
            if (i % 2 == 0) {
                sum += digit;
            } else {
                sum += DOUBLE_DIGIT_SUM[digit];
            }
        }
        sum %= 10;
        return sum == 0 ? 0 : 10 - sum;
    }

    private static void assertImeiReportingBodyIdentifier(String deviceId) {
        // Check the reporting body identifier
        int reportingBodyIdentifier = Integer.parseInt(deviceId.substring(0, 2), 16);
        assertTrue(String.format("IMEI RR %x not < %x", reportingBodyIdentifier, 0xA0),
                reportingBodyIdentifier < 0xA0);
    }

    private static void assertMeidDeviceId(String deviceId) {
        // MEID has two display formats either hexadecimal or decimal.
        if (isDecimalMeidFormat(deviceId)) {
            assertDecimalMeidFormat(deviceId);
        } else {
            assertHexadecimalMeidFormat(deviceId);
            assertMeidReportingBodyIdentifier(deviceId);
        }
    }

    private static boolean isDecimalMeidFormat(String deviceId) {
        return Pattern.matches("[0-9]{18}", deviceId);
    }

    private static void assertHexadecimalMeidFormat(String deviceId) {
        // MEID must NOT include the check digit.
        String meidPattern = "[0-9a-fA-F]{14}";
        assertTrue("MEID hex device id " + deviceId + " does not match pattern " + meidPattern,
                Pattern.matches(meidPattern, deviceId));
    }

    private static void assertMeidReportingBodyIdentifier(String deviceId) {
        int regionalCode = Integer.parseInt(deviceId.substring(0, 2), 16);
        assertTrue(String.format("MEID RR %x not >= %x", regionalCode, 0xA0),
                regionalCode >= 0xA0);
    }

    private static void assertDecimalMeidFormat(String deviceId) {
        // MEID must NOT include the check digit.
        String meidPattern = "[0-9]{18}"; // Optional check digit could be reported...
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







