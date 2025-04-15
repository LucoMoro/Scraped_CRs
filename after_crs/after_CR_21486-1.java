/*Remove Reference to Internal Phone Constant

Use the constant defined in the test instead.

Change-Id:Ia4c85a56663df0c79910395aaae534407952aaf6*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index dcb4e1d..3b85e9f 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -294,7 +292,7 @@
fail("Broadcast receiver waiting for ConnectivityManager interrupted.");
} finally {
mCm.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE,
                    FEATURE_ENABLE_HIPRI);
if (!isWifiConnected) {
mWifiManager.setWifiEnabled(false);
}







