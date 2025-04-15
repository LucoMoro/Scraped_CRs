/*Fix CTS test failure

CTS test case android.telephony.cts.TelephonyManagerTest#testGetDeviceId
was failed on CDMA+LTE device.
When phone is in CDMA+LTE, Android considers it to be PHONE_TYPE_CDMA
and in turn TelephonyManager.getDeviceId() will return MEID as device
ID. However, LTE is a 3GPP protocol, and reasonable to use IMEI for
device ID when phone is in LTE mode.
So, the fix is: added a check for LTE mode to use IMEI instead of
MEID.

Change-Id:I0b42555a9aafb79093218c4fc4b4ca0c06d2fb59*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 0055330..8b76021 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.telephony.cts;


import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
//Synthetic comment -- @@ -29,6 +28,8 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.regex.Pattern;

public class TelephonyManagerTest extends AndroidTestCase {
//Synthetic comment -- @@ -165,7 +166,12 @@
break;

case TelephonyManager.PHONE_TYPE_CDMA:
                assertCdmaDeviceId(deviceId);
break;

case TelephonyManager.PHONE_TYPE_NONE:







