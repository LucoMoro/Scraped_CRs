/*Support MEIDS Starting with 97, 98, and 99

Bug 3041176

97, 98, and 99 are valid reporting body identifiers now.http://www.tiaonline.org/standards/resources/meid/Change-Id:I59d563e8193d900d34cff1d80dd6330f7841ee9f*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index ebc33a6..1ced4cd 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.os.cts.TestThread;
import android.provider.Settings;
//Synthetic comment -- @@ -371,8 +370,7 @@

private static void assertCdmaDeviceId(String deviceId) {
if (deviceId.length() == 14) {
            assertHexadecimalMeidFormat(deviceId);
            assertReportingBodyIdentifier(deviceId, false); // Must be hexadecimal identifier
} else if (deviceId.length() == 8) {
assertHexadecimalEsnFormat(deviceId);
} else {
//Synthetic comment -- @@ -388,11 +386,19 @@
"80".equals(deviceId.substring(0, 2)));
}

    private static void assertHexadecimalMeidFormat(String deviceId) {
        // MEID must NOT include the check digit.
        String meidPattern = "[0-9a-fA-F]{14}";
        assertTrue("MEID hex device id " + deviceId + " does not match pattern " + meidPattern,
                Pattern.matches(meidPattern, deviceId));
}

private void assertHardwareId() {







