/*Merge remote branch 'korg/froyo' into manualmerge

Conflicts:
	tests/tests/os/src/android/os/cts/BuildVersionTest.java
	tests/tests/webkit/src/android/webkit/cts/WebChromeClientTest.java

Change-Id:I1befa31b0cce0a72a6d71760d18cae1bb97ca937*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index 1861ae2..e3eb46d 100644

//Synthetic comment -- @@ -73,8 +73,8 @@
final long eventTime = SystemClock.uptimeMillis();
// use coordinates for MotionEvent that do not include the status bar
// TODO: is there a more deterministic way to get these values
        final long x = 50;
        final long y = 50;
final int metaState = 0;
mMotionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
metaState);








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index 17a3ac1..be47953 100644

//Synthetic comment -- @@ -16,26 +16,43 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.test.AndroidTestCase;

@TestTargetClass(ConnectivityManager.class)
public class ConnectivityManagerTest extends AndroidTestCase {

public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
private static final int HOST_ADDRESS = 0x7f000001;// represent ip 127.0.0.1
private ConnectivityManager mCm;
// must include both mobile data + wifi
private static final int MIN_NUM_NETWORK_TYPES = 2;

//Synthetic comment -- @@ -43,6 +60,8 @@
protected void setUp() throws Exception {
super.setUp();
mCm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
}

@TestTargetNew(
//Synthetic comment -- @@ -174,4 +193,91 @@
public void testTest() {
mCm.getBackgroundDataSetting();
}
}







