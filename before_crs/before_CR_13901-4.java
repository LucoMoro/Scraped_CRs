/*Bluetooth:LCD wakeup during remote BT pairing request.
This change fixes the issue of LCD not coming up during remote
initiated pairing request when android is in suspend

Change-Id:I658954857584ae1a4737261b1ae1749f4d0a7998Signed-off-by: Bheemsen Kulkarni <x0099674@ti.com>*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index b28cf43..e380d11 100644

//Synthetic comment -- @@ -30,6 +30,8 @@

import java.util.HashMap;
import java.util.Set;

/**
* TODO: Move this to
//Synthetic comment -- @@ -51,6 +53,9 @@
private final BluetoothService mBluetoothService;
private final BluetoothAdapter mAdapter;
private final Context mContext;

private static final int EVENT_AUTO_PAIRING_FAILURE_ATTEMPT_DELAY = 1;
private static final int EVENT_RESTART_BLUETOOTH = 2;
//Synthetic comment -- @@ -121,6 +126,11 @@
mContext = context;
mPasskeyAgentRequestData = new HashMap();
mAdapter = adapter;
initializeNativeDataNative();
}

//Synthetic comment -- @@ -451,37 +461,46 @@
mHandler.sendMessageDelayed(message, 1500);
return;
}

Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,
BluetoothDevice.PAIRING_VARIANT_CONSENT);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
return;
}

private void onRequestPasskeyConfirmation(String objectPath, int passkey, int nativeData) {
String address = checkPairingRequestAndGetAddress(objectPath, nativeData);
if (address == null) return;

Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PASSKEY, passkey);
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,
BluetoothDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
return;
}

private void onRequestPasskey(String objectPath, int nativeData) {
String address = checkPairingRequestAndGetAddress(objectPath, nativeData);
if (address == null) return;

Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,
BluetoothDevice.PAIRING_VARIANT_PASSKEY);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
return;
}

//Synthetic comment -- @@ -519,10 +538,14 @@
}
}
}
Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.PAIRING_VARIANT_PIN);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
return;
}

//Synthetic comment -- @@ -530,12 +553,16 @@
String address = checkPairingRequestAndGetAddress(objectPath, nativeData);
if (address == null) return;

Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PASSKEY, passkey);
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,
BluetoothDevice.PAIRING_VARIANT_DISPLAY_PASSKEY);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
}

private boolean onAgentAuthorize(String objectPath, String deviceUuid) {







