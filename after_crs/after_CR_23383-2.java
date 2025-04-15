/*Skip Bluetooth tests if the device doesn't support Bluetooth.

Change-Id:Iad247a9538cdbe5ac8fc1e6a9522791cfde20262*/




//Synthetic comment -- diff --git a/tests/tests/bluetooth/src/android/bluetooth/cts/BasicAdapterTest.java b/tests/tests/bluetooth/src/android/bluetooth/cts/BasicAdapterTest.java
//Synthetic comment -- index a3032bb..796298d 100644

//Synthetic comment -- @@ -19,30 +19,41 @@
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
* Very basic test, just of the static methods of {@link
* BluetoothAdapter}.
*/
public class BasicAdapterTest extends AndroidTestCase {
private static final int DISABLE_TIMEOUT = 5000;  // ms timeout for BT disable
private static final int ENABLE_TIMEOUT = 10000;  // ms timeout for BT enable
private static final int POLL_TIME = 100;         // ms to poll BT state

    private boolean mHasBluetooth;

    public void setUp() throws Exception {
        super.setUp();

        mHasBluetooth = getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH);
    }

public void test_getDefaultAdapter() {
/*
* Note: If the target doesn't support Bluetooth at all, then
         * this method should return null.
*/
        if (mHasBluetooth) {
            assertNotNull(BluetoothAdapter.getDefaultAdapter());
        } else {
            assertNull(BluetoothAdapter.getDefaultAdapter());
        }
}

public void test_checkBluetoothAddress() {
//Synthetic comment -- @@ -94,7 +105,7 @@
"00:00:e0:00:00:00"));
assertFalse(BluetoothAdapter.checkBluetoothAddress(
"00:00:0f:00:00:00"));

assertTrue(BluetoothAdapter.checkBluetoothAddress(
"00:00:00:00:00:00"));
assertTrue(BluetoothAdapter.checkBluetoothAddress(
//Synthetic comment -- @@ -105,6 +116,10 @@

/** Checks enable(), disable(), getState(), isEnabled() */
public void test_enableDisable() {
        if (!mHasBluetooth) {
            // Skip the test if bluetooth is not present.
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

for (int i=0; i<5; i++) {
//Synthetic comment -- @@ -114,6 +129,10 @@
}

public void test_getAddress() {
        if (!mHasBluetooth) {
            // Skip the test if bluetooth is not present.
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -121,6 +140,10 @@
}

public void test_getName() {
        if (!mHasBluetooth) {
            // Skip the test if bluetooth is not present.
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -129,6 +152,10 @@
}

public void test_getBondedDevices() {
        if (!mHasBluetooth) {
            // Skip the test if bluetooth is not present.
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -140,6 +167,10 @@
}

public void test_getRemoteDevice() {
        if (!mHasBluetooth) {
            // Skip the test if bluetooth is not present.
            return;
        }
// getRemoteDevice() should work even with Bluetooth disabled
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
disable(adapter);
//Synthetic comment -- @@ -161,6 +192,10 @@
}

public void test_listenUsingRfcommWithServiceRecord() throws IOException {
        if (!mHasBluetooth) {
            // Skip the test if bluetooth is not present.
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);








