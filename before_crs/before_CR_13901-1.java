/*Bluetooth:LCD wakeup during remote BT pairing request.
This change fixes the issue of LCD not coming up during remote
initiated pairing request when android is in suspend

Change-Id:I395b25aec4a39523b34d2f0a4d86028f7b6cdb9e*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index b28cf43..884f4a0 100644

//Synthetic comment -- @@ -30,6 +30,13 @@

import java.util.HashMap;
import java.util.Set;

/**
* TODO: Move this to
//Synthetic comment -- @@ -51,6 +58,9 @@
private final BluetoothService mBluetoothService;
private final BluetoothAdapter mAdapter;
private final Context mContext;

private static final int EVENT_AUTO_PAIRING_FAILURE_ATTEMPT_DELAY = 1;
private static final int EVENT_RESTART_BLUETOOTH = 2;
//Synthetic comment -- @@ -121,7 +131,12 @@
mContext = context;
mPasskeyAgentRequestData = new HashMap();
mAdapter = adapter;
        initializeNativeDataNative();
}

protected void finalize() throws Throwable {
//Synthetic comment -- @@ -519,10 +534,14 @@
}
}
}
        Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.PAIRING_VARIANT_PIN);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
return;
}








