/*Test startUsingNetworkFeature TYPE_MOBILE_HIPRI

Bug 3307293

Change-Id:I03b3e11e1de20333ece772e3448937c61ca0fe91*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index edcea9a2..de4de01 100644

//Synthetic comment -- @@ -22,20 +22,37 @@
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.animation.cts.DelayedCheck;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestTargetClass(ConnectivityManager.class)
public class ConnectivityManagerTest extends AndroidTestCase {

    /**
     *
     */
    private static final String FEATURE_ENABLE_HIPRI = "enableHIPRI";

    private static final String TAG = ConnectivityManagerTest.class.getSimpleName();

public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
private static final int HOST_ADDRESS = 0x7f000001;// represent ip 127.0.0.1
private ConnectivityManager mCm;
    private WifiManager mWifiManager;
// must include both mobile data + wifi
private static final int MIN_NUM_NETWORK_TYPES = 2;

//Synthetic comment -- @@ -43,6 +60,7 @@
protected void setUp() throws Exception {
super.setUp();
mCm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
}

@TestTargetNew(
//Synthetic comment -- @@ -235,4 +253,90 @@
public void testTest() {
mCm.getBackgroundDataSetting();
}

    /** Test that hipri can be brought up when Wifi is enabled. */
    public void testStartUsingNetworkFeature_enableHipri() {
        boolean isWifiEnabled = mWifiManager.isWifiEnabled();

        try {
            if (!isWifiEnabled) {
                enableWifi(true);
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
            assertTrue("Couldn't requestRouteToHost using HIPRI.",
                    mCm.requestRouteToHost(ConnectivityManager.TYPE_MOBILE, HOST_ADDRESS));

            // TODO: Send some traffic using HIPRI.

            // Check that the ConnectivityManager reported that it connected using hipri...
            assertTrue("Broadcast receiver did not receive ConnectivityManager broadcast.",
                    receiver.hasOnReceiveFinished());
            assertEquals(State.CONNECTED, receiver.getNetworkState());

        } catch (InterruptedException e) {
            fail("Broadcast receiver waiting for ConnectivityManager interrupted.");
        } finally {
            mCm.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, FEATURE_ENABLE_HIPRI);

            if (!isWifiEnabled) {
                enableWifi(false);
            }
        }
    }

    private void enableWifi(final boolean enable) {
        assertTrue(mWifiManager.setWifiEnabled(enable));
        new DelayedCheck() {
            protected boolean check() {
                return mWifiManager.isWifiEnabled() == enable;
            }
        }.run();
    }

    /** Receiver that captures the last connectivity change's network type and state. */
    private class ConnectivityActionReceiver extends BroadcastReceiver {

        private final CountDownLatch mReceiveLatch = new CountDownLatch(1);

        private int mMatchNetworkType;

        private State mState;

        public ConnectivityActionReceiver(int matchNetworkType) {
            mMatchNetworkType = matchNetworkType;
        }

        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = intent.getExtras()
                    .getParcelable(ConnectivityManager.EXTRA_NETWORK_INFO);
            int networkType = networkInfo.getType();
            if (networkType == mMatchNetworkType) {
                synchronized (this) {
                    mState = networkInfo.getState();
                }
                mReceiveLatch.countDown();
            } else {
                Log.i(TAG, "Ignoring event for network type: " + networkType);
            }
        }

        public boolean hasOnReceiveFinished() throws InterruptedException {
            return mReceiveLatch.await(10, TimeUnit.SECONDS);
        }

        public synchronized State getNetworkState() {
            return mState;
        }
    }
}







