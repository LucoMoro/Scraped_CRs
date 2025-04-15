/*Bluetooth:LCD wakeup during remote BT pairing request.
This change fixes the issue of LCD not coming up during remote
initiated pairing request when android is in suspend

Change-Id:Ib09b1c8b39614f9dace0e775bf3f35d5822f122eSigned-off-by: Bheemsen Kulkarni <x0099674@ti.com>*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index b28cf43..455b27a 100644

//Synthetic comment -- @@ -30,6 +30,13 @@

import java.util.HashMap;
import java.util.Set;
/*
        * The Power manager is imported for accesing the Wakelock
        * service during Pin Request from Remote device during
        * Android Suspend
         */
import android.os.PowerManager;


/**
* TODO: Move this to
//Synthetic comment -- @@ -51,6 +58,9 @@
private final BluetoothService mBluetoothService;
private final BluetoothAdapter mAdapter;
private final Context mContext;
    // The WakeLock is used for bringing up the LCD during a pairing request
    // from remote device when Android is in Suspend state.
    private PowerManager.WakeLock mWakeLock;

private static final int EVENT_AUTO_PAIRING_FAILURE_ATTEMPT_DELAY = 1;
private static final int EVENT_RESTART_BLUETOOTH = 2;
//Synthetic comment -- @@ -121,7 +131,12 @@
mContext = context;
mPasskeyAgentRequestData = new HashMap();
mAdapter = adapter;
	//WakeLock instantiation in BluetoothEventLoop class
	PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
	mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
		       	| PowerManager.ON_AFTER_RELEASE, TAG);
       	mWakeLock.setReferenceCounted(false);
       	initializeNativeDataNative();
}

protected void finalize() throws Throwable {
//Synthetic comment -- @@ -519,10 +534,14 @@
}
}
}
	//Acquire wakelock during PIN code request to bring up LCD display
	mWakeLock.acquire();
	Intent intent = new Intent(BluetoothDevice.ACTION_PAIRING_REQUEST);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, mAdapter.getRemoteDevice(address));
intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.PAIRING_VARIANT_PIN);
mContext.sendBroadcast(intent, BLUETOOTH_ADMIN_PERM);
	//Release wakelock to allow the LCD to go off after the PIN popup notifcation.
	mWakeLock.release();
return;
}








