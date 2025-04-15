/*Skip Bluetooth tests if the device doesn't support Bluetooth.

Change-Id:Iad247a9538cdbe5ac8fc1e6a9522791cfde20262*/




//Synthetic comment -- diff --git a/tests/tests/bluetooth/src/android/bluetooth/cts/BasicAdapterTest.java b/tests/tests/bluetooth/src/android/bluetooth/cts/BasicAdapterTest.java
//Synthetic comment -- index a3032bb..f988a97 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
private static final int DISABLE_TIMEOUT = 5000;  // ms timeout for BT disable
private static final int ENABLE_TIMEOUT = 10000;  // ms timeout for BT enable
private static final int POLL_TIME = 100;         // ms to poll BT state
    private boolean isSupport = BluetoothAdapter.getDefaultAdapter() != null;

public void test_getDefaultAdapter() {
/*
//Synthetic comment -- @@ -42,6 +43,10 @@
* you won't bother running this test on a target that doesn't
* purport to support Bluetooth.
*/
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
assertNotNull(BluetoothAdapter.getDefaultAdapter());
}

//Synthetic comment -- @@ -105,6 +110,10 @@

/** Checks enable(), disable(), getState(), isEnabled() */
public void test_enableDisable() {
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

for (int i=0; i<5; i++) {
//Synthetic comment -- @@ -114,6 +123,10 @@
}

public void test_getAddress() {
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -121,6 +134,10 @@
}

public void test_getName() {
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -129,6 +146,10 @@
}

public void test_getBondedDevices() {
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);

//Synthetic comment -- @@ -140,6 +161,10 @@
}

public void test_getRemoteDevice() {
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
// getRemoteDevice() should work even with Bluetooth disabled
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
disable(adapter);
//Synthetic comment -- @@ -161,6 +186,10 @@
}

public void test_listenUsingRfcommWithServiceRecord() throws IOException {
        if (!isSupport) {
            // Bluetooth is not supported on this device
            return;
        }
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
enable(adapter);








