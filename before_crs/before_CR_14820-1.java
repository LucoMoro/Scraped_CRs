/*Verify Device IMEI/MEID Ids

Bug 2694172

Test that TelephonyManager#getDeviceId returns either a 15
long IMEI id for GSM phones, 14 hex MEID id for CDMA, or 18
decimal MEID ids. Check the Luhn check digit for IMEI ids.
Finally, check that a valid MAC address is returned by
devices that are neither GSM or CDMA.

Change-Id:I0a8696d76ece9802ac7c43111ed230c6a0412efb*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 90b2bf9..1af4adf 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.os.Looper;
import android.os.cts.TestThread;
import android.telephony.CellLocation;
//Synthetic comment -- @@ -29,6 +31,8 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
private TelephonyManager mTelephonyManager;
//Synthetic comment -- @@ -266,4 +270,151 @@
mTelephonyManager.getDeviceId();
mTelephonyManager.getDeviceSoftwareVersion();
}
}







