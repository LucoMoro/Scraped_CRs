/*When Bluetooth does not exist in feature, do not add a Bluetooth service

Change-Id:Iffc912bc2483ad84766a1e5d8d034bf484ef5401*/
//Synthetic comment -- diff --git a/services/java/com/android/server/SystemServer.java b/services/java/com/android/server/SystemServer.java
//Synthetic comment -- index f300d74..ffb30f0 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.res.Configuration;
import android.media.AudioService;
import android.net.wifi.p2p.WifiP2pService;
//Synthetic comment -- @@ -239,10 +240,18 @@
// Skip Bluetooth if we have an emulator kernel
// TODO: Use a more reliable check to see if this product should
// support Bluetooth - see bug 988521
if (SystemProperties.get("ro.kernel.qemu").equals("1")) {
Slog.i(TAG, "No Bluetooh Service (emulator)");
} else if (factoryTest == SystemServer.FACTORY_TEST_LOW_LEVEL) {
Slog.i(TAG, "No Bluetooth Service (factory test)");
} else {
Slog.i(TAG, "Bluetooth Service");
bluetooth = new BluetoothService(context);







