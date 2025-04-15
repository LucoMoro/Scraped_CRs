/*Test startUsingNetworkFeature TYPE_MOBILE_HIPRI

Bug 3307293

Change-Id:I03b3e11e1de20333ece772e3448937c61ca0fe91*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 42e557c..fb06015 100644

//Synthetic comment -- @@ -16,6 +16,12 @@

package android.graphics.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
//Synthetic comment -- @@ -30,15 +36,11 @@
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.os.Debug;
import android.test.AndroidTestCase;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;

@TestTargetClass(Paint.class)
public class PaintTest extends AndroidTestCase {
//Synthetic comment -- @@ -74,67 +76,67 @@
method = "breakText",
args = {char[].class, int.class, int.class, float.class, float[].class}
)
public void testBreakText1() {
Paint p = new Paint();

char[] chars = {'H', 'I', 'J', 'K', 'L', 'M', 'N'};
        float[] width = new float[chars.length];
float[] f = new float[1];

        p.getTextWidths(chars, 0, chars.length, width);

for (int i = 0; i < chars.length; i++) {
assertEquals(1, p.breakText(chars, i, 1, 20.0f, f));
assertEquals(width[i], f[0]);
}

        float totalWidth = 0;
        for (int i = 0; i < chars.length; i++) {
            totalWidth += width[i];
        }

// start from 'H'
int indexH = 0;
        assertEquals(4, p.breakText(chars, indexH, 4, totalWidth, f));
        assertEquals(width[0] + width[1] + width[2] + width[3], f[0]);
        assertEquals(3, p.breakText(chars, indexH, 3, totalWidth, f));
        assertEquals(width[0] + width[1] + width[2], f[0]);
        assertEquals(2, p.breakText(chars, indexH, 2, totalWidth, f));
        assertEquals(width[0] + width[1], f[0]);
        assertEquals(1, p.breakText(chars, indexH, 1, totalWidth, f));
        assertEquals(width[0], f[0]);
        assertEquals(0, p.breakText(chars, indexH, 0, totalWidth, f));
assertEquals(0.0f, f[0]);

        assertEquals(1, p.breakText(chars, 6, -2, totalWidth, f));
        assertEquals(width[5] + width[6], f[0]);


        assertEquals(1, p.breakText(chars, indexH + 2, 1, totalWidth, f));
        assertEquals(width[2], f[0]);
        assertEquals(1, p.breakText(chars, indexH + 2, -1, totalWidth, f));
        assertEquals(width[2], f[0]);
        assertEquals(2, p.breakText(chars, indexH + 2, -2, totalWidth, f));
        assertEquals(width[2] + width[3], f[0]);
        assertEquals(3, p.breakText(chars, indexH + 2, -3, totalWidth, f));
        assertEquals(width[2] + width[3] + width[4], f[0]);

        assertEquals(1, p.breakText(chars, indexH, -1, totalWidth, f));
        assertEquals(width[0], f[0]);
        assertEquals(2, p.breakText(chars, indexH, -2, totalWidth, f));
        assertEquals(width[0] + width[1], f[0]);
        assertEquals(3, p.breakText(chars, indexH, -3, totalWidth, f));
        assertEquals(width[0] + width[1] + width[2], f[0]);
        assertEquals(4, p.breakText(chars, indexH, -4, totalWidth, f));
        assertEquals(width[0] + width[1] + width[2] + width[3], f[0]);

        assertEquals(7, p.breakText(chars, indexH, 7, totalWidth * 2, f));
        assertEquals(totalWidth, f[0]);
        assertEquals(6, p.breakText(chars, indexH, 7, totalWidth - width[chars.length - 1], f));
        assertEquals(totalWidth - width[chars.length - 1], f[0]);

        assertEquals(7, p.breakText(chars, indexH, -7, totalWidth, null));
        assertEquals(7, p.breakText(chars, indexH, 7, totalWidth, null));
}

@TestTargetNew(








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index 354428b..8c45095 100644

//Synthetic comment -- @@ -16,26 +16,43 @@

package android.net.cts;

import com.android.internal.telephony.Phone;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestTargetClass(ConnectivityManager.class)
public class ConnectivityManagerTest extends AndroidTestCase {

    private static final String TAG = ConnectivityManagerTest.class.getSimpleName();

    private static final String FEATURE_ENABLE_HIPRI = "enableHIPRI";

public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
private static final int HOST_ADDRESS = 0x7f000001;// represent ip 127.0.0.1
private ConnectivityManager mCm;
    private WifiManager mWifiManager;
    private PackageManager mPackageManager;
// must include both mobile data + wifi
private static final int MIN_NUM_NETWORK_TYPES = 2;

//Synthetic comment -- @@ -43,6 +60,8 @@
protected void setUp() throws Exception {
super.setUp();
mCm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        mPackageManager = getContext().getPackageManager();
}

@TestTargetNew(
//Synthetic comment -- @@ -235,4 +254,91 @@
public void testTest() {
mCm.getBackgroundDataSetting();
}

    /** Test that hipri can be brought up when Wifi is enabled. */
    public void testStartUsingNetworkFeature_enableHipri() throws Exception {
        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
                || !mPackageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            // This test requires a mobile data connection and WiFi.
            return;
        }

        boolean isWifiConnected = mWifiManager.isWifiEnabled()
                && mWifiManager.getConnectionInfo().getSSID() != null;

        try {
            // Make sure WiFi is connected to an access point.
            if (!isWifiConnected) {
                connectToWifi();
            }

            // Register a receiver that will capture the connectivity change for hipri.
            ConnectivityActionReceiver receiver =
                new ConnectivityActionReceiver(ConnectivityManager.TYPE_MOBILE_HIPRI);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(receiver, filter);

            // Try to start using the hipri feature...
            int result = mCm.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE,
                    FEATURE_ENABLE_HIPRI);
            assertTrue("Couldn't start using the HIPRI feature.", result != -1);

            // Check that the ConnectivityManager reported that it connected using hipri...
            assertTrue("Couldn't connect using hipri...", receiver.waitForConnection());

            assertTrue("Couldn't requestRouteToHost using HIPRI.",
                    mCm.requestRouteToHost(ConnectivityManager.TYPE_MOBILE_HIPRI, HOST_ADDRESS));

        } catch (InterruptedException e) {
            fail("Broadcast receiver waiting for ConnectivityManager interrupted.");
        } finally {
            mCm.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE,
                    Phone.FEATURE_ENABLE_HIPRI);
            if (!isWifiConnected) {
                mWifiManager.setWifiEnabled(false);
            }
        }
    }

    private void connectToWifi() throws InterruptedException {
        ConnectivityActionReceiver receiver =
            new ConnectivityActionReceiver(ConnectivityManager.TYPE_WIFI);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(receiver, filter);

        assertTrue(mWifiManager.setWifiEnabled(true));
        assertTrue("Wifi must be configured to connect to an access point for this test.",
                receiver.waitForConnection());

        mContext.unregisterReceiver(receiver);
    }

    /** Receiver that captures the last connectivity change's network type and state. */
    private class ConnectivityActionReceiver extends BroadcastReceiver {

        private final CountDownLatch mReceiveLatch = new CountDownLatch(1);

        private final int mNetworkType;

        ConnectivityActionReceiver(int networkType) {
            mNetworkType = networkType;
        }

        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = intent.getExtras()
                    .getParcelable(ConnectivityManager.EXTRA_NETWORK_INFO);
            int networkType = networkInfo.getType();
            State networkState = networkInfo.getState();
            Log.i(TAG, "Network type: " + networkType + " state: " + networkState);
            if (networkType == mNetworkType && networkInfo.getState() == State.CONNECTED) {
                mReceiveLatch.countDown();
            }
        }

        public boolean waitForConnection() throws InterruptedException {
            return mReceiveLatch.await(10, TimeUnit.SECONDS);
        }
    }
}







