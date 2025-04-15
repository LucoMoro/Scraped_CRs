/*Fix CTS case testGetDeviceId of TelephonyManagerTest

If the device has no telephony and wifi feature, this test
case is will fail.
The fix is to check the wifi feature when phone type is NONE.

Change-Id:Ic71313b1abf258c4efb020cf5a5b405f47a83677*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 645eb81..045aeb3 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
//Synthetic comment -- @@ -43,12 +44,14 @@
private static final int TOLERANCE = 1000;
private Looper mLooper;
private PhoneStateListener mListener;

@Override
protected void setUp() throws Exception {
super.setUp();
mTelephonyManager =
(TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
}

@Override
//Synthetic comment -- @@ -296,8 +299,14 @@
break;

case TelephonyManager.PHONE_TYPE_NONE:
                assertSerialNumber();
                assertMacAddressReported();
break;

default:







