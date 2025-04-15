/*BT: Adjust Ouf Of Band pairing handling to new BlueZ

Agent is no longer required to provide OOB data while pairing is
ongoing as those data are already provided to BlueZ daemon before
pairing is started.

Change-Id:Ia64e35e84161b7d79395d654efb157c24234a342Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothPairingDialog.java b/src/com/android/settings/bluetooth/BluetoothPairingDialog.java
//Synthetic comment -- index 940d8d0..bdbfbe3 100755

//Synthetic comment -- @@ -125,7 +125,6 @@
break;

case BluetoothDevice.PAIRING_VARIANT_CONSENT:
createConsentDialog(deviceManager);
break;

//Synthetic comment -- @@ -229,7 +228,6 @@
break;

case BluetoothDevice.PAIRING_VARIANT_CONSENT:
messageText = getString(R.string.bluetooth_incoming_pairing_msg, name);
break;

//Synthetic comment -- @@ -328,10 +326,6 @@
// Do nothing.
break;

default:
Log.e(TAG, "Incorrect pairing type received");
}







