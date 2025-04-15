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
import android.os.Looper;
import android.os.cts.TestThread;
import android.telephony.CellLocation;
//Synthetic comment -- @@ -29,6 +31,8 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
private TelephonyManager mTelephonyManager;
//Synthetic comment -- @@ -266,4 +270,103 @@
mTelephonyManager.getDeviceId();
mTelephonyManager.getDeviceSoftwareVersion();
}
}







