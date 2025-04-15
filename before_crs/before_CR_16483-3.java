/*Check "hardware_id" Property

Assert that the "hardware_id" property is set to something sane
for non-telephony devices.

Change-Id:I86fe6b4654d6710e5e7df7cd5d1d39968ddf8ee5*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/SettingsTest.java b/tests/tests/provider/src/android/provider/cts/SettingsTest.java
//Synthetic comment -- index 73fd836..e3ca242 100644

//Synthetic comment -- @@ -28,10 +28,10 @@
import android.os.RemoteException;
import android.provider.Settings;
import android.test.AndroidTestCase;
import android.util.Log;

@TestTargetClass(android.provider.Settings.class)
public class SettingsTest extends AndroidTestCase {
public void testSystemTable() throws RemoteException {
final String[] SYSTEM_PROJECTION = new String[] {
Settings.System._ID, Settings.System.NAME, Settings.System.VALUE








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 239b3e5..cbcc7eb 100644

//Synthetic comment -- @@ -21,11 +21,14 @@
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
//Synthetic comment -- @@ -294,6 +297,7 @@

case TelephonyManager.PHONE_TYPE_NONE:
assertNull(deviceId);
assertMacAddressReported();
break;

//Synthetic comment -- @@ -376,6 +380,19 @@
Pattern.matches(meidPattern, deviceId));
}

private void assertMacAddressReported() {
String macAddress = getMacAddress();
String macPattern = "([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}";







