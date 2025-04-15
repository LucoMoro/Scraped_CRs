/*Improve shutdown speed by disabling radio/bt early

Improve shutdown speed by disabling radio/bt as the first thing.
Before buzzing we still check radio/bt status to be sure they are disabled.
If not we wait as much as before but with a more often (100ms) check on
status compared to previous 500ms. This way other shutdown operations can
be done in paralel, reducing total time to take the system down. This
shaves a few seconds of the total shutdown time.

Change-Id:Icd5eb5083ec64b4c57d32413a258b499a0711de7*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ShutdownThread.java b/core/java/com/android/internal/app/ShutdownThread.java
//Synthetic comment -- index cd1b569..0da68ed 100644

//Synthetic comment -- @@ -45,8 +45,8 @@
public final class ShutdownThread extends Thread {
// constants
private static final String TAG = "ShutdownThread";
    private static final int MAX_NUM_PHONE_STATE_READS = 16;
    private static final int PHONE_STATE_POLL_SLEEP_MSEC = 500;
// maximum time we wait for the shutdown broadcast before going on.
private static final int MAX_BROADCAST_TIME = 10*1000;
private static final int MAX_SHUTDOWN_WAIT_TIME = 20*1000;
//Synthetic comment -- @@ -96,8 +96,6 @@
}
}

        Log.d(TAG, "Notifying thread to start radio shutdown");

if (confirm) {
final AlertDialog dialog = new AlertDialog.Builder(context)
.setIcon(android.R.drawable.ic_dialog_alert)
//Synthetic comment -- @@ -145,6 +143,8 @@
sIsStarted = true;
}

// throw up an indeterminate system dialog to indicate radio is
// shutting down.
ProgressDialog pd = new ProgressDialog(context);
//Synthetic comment -- @@ -227,6 +227,35 @@
SystemProperties.set(SHUTDOWN_ACTION_PROPERTY, reason);
}

Log.i(TAG, "Sending shutdown broadcast...");

// First send the high-level shut down broadcast.
//Synthetic comment -- @@ -260,67 +289,10 @@
}
}

        final ITelephony phone =
                ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
        final IBluetooth bluetooth =
                IBluetooth.Stub.asInterface(ServiceManager.checkService(
                        BluetoothAdapter.BLUETOOTH_SERVICE));

final IMountService mount =
IMountService.Stub.asInterface(
ServiceManager.checkService("mount"));

        try {
            bluetoothOff = bluetooth == null ||
                           bluetooth.getBluetoothState() == BluetoothAdapter.STATE_OFF;
            if (!bluetoothOff) {
                Log.w(TAG, "Disabling Bluetooth...");
                bluetooth.disable(false);  // disable but don't persist new state
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "RemoteException during bluetooth shutdown", ex);
            bluetoothOff = true;
        }

        try {
            radioOff = phone == null || !phone.isRadioOn();
            if (!radioOff) {
                Log.w(TAG, "Turning off radio...");
                phone.setRadio(false);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "RemoteException during radio shutdown", ex);
            radioOff = true;
        }

        Log.i(TAG, "Waiting for Bluetooth and Radio...");
        
        // Wait a max of 32 seconds for clean shutdown
        for (int i = 0; i < MAX_NUM_PHONE_STATE_READS; i++) {
            if (!bluetoothOff) {
                try {
                    bluetoothOff =
                            bluetooth.getBluetoothState() == BluetoothAdapter.STATE_OFF;
                } catch (RemoteException ex) {
                    Log.e(TAG, "RemoteException during bluetooth shutdown", ex);
                    bluetoothOff = true;
                }
            }
            if (!radioOff) {
                try {
                    radioOff = !phone.isRadioOn();
                } catch (RemoteException ex) {
                    Log.e(TAG, "RemoteException during radio shutdown", ex);
                    radioOff = true;
                }
            }
            if (radioOff && bluetoothOff) {
                Log.i(TAG, "Radio and Bluetooth shutdown complete.");
                break;
            }
            SystemClock.sleep(PHONE_STATE_POLL_SLEEP_MSEC);
        }

// Shutdown MountService to ensure media is in a safe state
IMountShutdownObserver observer = new IMountShutdownObserver.Stub() {
public void onShutDownComplete(int statusCode) throws RemoteException {
//Synthetic comment -- @@ -356,6 +328,34 @@
}
}

rebootOrShutdown(mReboot, mRebootReason);
}








