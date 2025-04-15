/*Require bonding and encryption for PBAP server

The Phonebook Access Profile specification requires bonding and
encryption. For devices not supporting SSP (Secure Simple Pairing),
InsecureRfcomm will require neither. Adding EncryptedRfcomm to force
bonding and encryption but not requiring authenticated link key.

Change-Id:If47cca9c5ffd89358bcd61d64f7785d17e0ca7cc*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothAdapter.java b/core/java/android/bluetooth/BluetoothAdapter.java
//Synthetic comment -- index a7175e3..7d95b6a 100644

//Synthetic comment -- @@ -828,6 +828,42 @@
return createNewRfcommSocketAndRecord(name, uuid, false, false);
}

private BluetoothServerSocket createNewRfcommSocketAndRecord(String name, UUID uuid,
boolean auth, boolean encrypt) throws IOException {
RfcommChannelPicker picker = new RfcommChannelPicker(uuid);
//Synthetic comment -- @@ -898,6 +934,28 @@
return socket;
}

/**
* Construct a SCO server socket.
* Call #accept to retrieve connections to this socket.







