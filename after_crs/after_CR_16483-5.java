/*DO NOT MERGE Check "hardware_id" Property

Assert that the "hardware_id" property is set to something sane
for non-telephony devices.

Change-Id:I86fe6b4654d6710e5e7df7cd5d1d39968ddf8ee5*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index 8044752..086397a 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

import junit.framework.TestCase;

import java.util.regex.Pattern;

@TestTargetClass(Build.class)
public class BuildTest extends TestCase {

//Synthetic comment -- @@ -46,4 +48,12 @@
assertEquals(message, "unknown", Build.CPU_ABI2);
}
}

    private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9a-z_]+)$");

    /** Tests that check for valid values of constants in Build. */
    public void testBuildConstants() {
        assertTrue(DEVICE_PATTERN.matcher(Build.DEVICE).matches());
    }
}








//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/SettingsTest.java b/tests/tests/provider/src/android/provider/cts/SettingsTest.java
//Synthetic comment -- index 73fd836..e3ca242 100644

//Synthetic comment -- @@ -28,10 +28,10 @@
import android.os.RemoteException;
import android.provider.Settings;
import android.test.AndroidTestCase;

@TestTargetClass(android.provider.Settings.class)
public class SettingsTest extends AndroidTestCase {

public void testSystemTable() throws RemoteException {
final String[] SYSTEM_PROJECTION = new String[] {
Settings.System._ID, Settings.System.NAME, Settings.System.VALUE








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 239b3e5..a1d6432 100644

//Synthetic comment -- @@ -21,11 +21,14 @@
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.os.cts.TestThread;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -294,6 +297,7 @@

case TelephonyManager.PHONE_TYPE_NONE:
assertNull(deviceId);
                assertHardwareId();
assertMacAddressReported();
break;

//Synthetic comment -- @@ -376,6 +380,17 @@
Pattern.matches(meidPattern, deviceId));
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
String macAddress = getMacAddress();
String macPattern = "([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}";







