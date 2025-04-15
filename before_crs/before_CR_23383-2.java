/*Skip Bluetooth tests if the device doesn't support Bluetooth.

Change-Id:Iad247a9538cdbe5ac8fc1e6a9522791cfde20262*/
//Synthetic comment -- diff --git a/tests/tests/bluetooth/src/android/bluetooth/cts/BasicAdapterTest.java b/tests/tests/bluetooth/src/android/bluetooth/cts/BasicAdapterTest.java
//Synthetic comment -- index a3032bb..796298d 100644

//Synthetic comment -- @@ -19,30 +19,41 @@
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import junit.framework.TestCase;

/**
* Very basic test, just of the static methods of {@link
* BluetoothAdapter}.
*/
public class BasicAdapterTest extends TestCase {
private static final int DISABLE_TIMEOUT = 5000;  // ms timeout for BT disable
private static final int ENABLE_TIMEOUT = 10000;  // ms timeout for BT enable
private static final int POLL_TIME = 100;         // ms to poll BT state

public void test_getDefaultAdapter() {
/*
* Note: If the target doesn't support Bluetooth at all, then
         * this method will return null. The assumption here is that
         * you won't bother running this test on a target that doesn't
         * purport to support Bluetooth.
*/
        assertNotNull(BluetoothAdapter.getDefaultAdapter());
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
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

for (int i=0; i<5; i++) {
//Synthetic comment -- @@ -114,6 +129,10 @@
}

public void test_getAddress() {
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -121,6 +140,10 @@
}

public void test_getName() {
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -129,6 +152,10 @@
}

public void test_getBondedDevices() {
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -140,6 +167,10 @@
}

public void test_getRemoteDevice() {
// getRemoteDevice() should work even with Bluetooth disabled
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
disable(adapter);
//Synthetic comment -- @@ -161,6 +192,10 @@
}

public void test_listenUsingRfcommWithServiceRecord() throws IOException {
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);








