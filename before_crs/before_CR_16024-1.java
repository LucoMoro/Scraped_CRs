/*Add Telephony Feature Tests

Bug 2816994

Check that the device's getPhoneType return value makes sense with
the features reported by PackageManager#getSystemAvailableFeatures
and PackageManager#hasFeature.

Change-Id:Ibc5137f1339d656029c1558f6783520e24bc3393*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 9ecbf20..c07c557 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
//Synthetic comment -- @@ -31,6 +33,9 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.regex.Pattern;

@TestTargetClass(TelephonyManager.class)
//Synthetic comment -- @@ -403,4 +408,71 @@
}
}
}
}







