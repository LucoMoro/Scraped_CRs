/*Fix CTS case testGetDeviceId of TelephonyManagerTest

If the device has no telephony and wifi feature, this test
case is will fail.
The fix is to check the wifi feature when phone type is NONE.

Change-Id:Ic71313b1abf258c4efb020cf5a5b405f47a83677*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 645eb81..a562191 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
private TelephonyManager mTelephonyManager;
    private PackageManager mPackageManager;
private boolean mOnCellLocationChangedCalled = false;
private final Object mLock = new Object();
private static final int TOLERANCE = 1000;
//Synthetic comment -- @@ -49,6 +50,7 @@
super.setUp();
mTelephonyManager =
(TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPackageManager = getContext().getPackageManager();
}

@Override
//Synthetic comment -- @@ -296,6 +298,10 @@
break;

case TelephonyManager.PHONE_TYPE_NONE:
                if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
                    break;
                }

assertSerialNumber();
assertMacAddressReported();
break;







