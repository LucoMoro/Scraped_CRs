/*Merge "Require bonding and encryption for PBAP server"

Change-Id:I4c091d33539de863e1dc156b87863019db353923*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothAdapter.java b/core/java/android/bluetooth/BluetoothAdapter.java
//Synthetic comment -- index a7175e3..66a7450 100644

//Synthetic comment -- @@ -799,10 +799,10 @@

/**
* Create a listening, insecure RFCOMM Bluetooth socket with Service Record.
     * <p>The link key will be unauthenticated i.e the communication is
* vulnerable to Man In the Middle attacks. For Bluetooth 2.1 devices,
     * the link key will be encrypted, as encryption is mandartory.
     * For legacy devices (pre Bluetooth 2.1 devices) the link key will not
* be encrypted. Use {@link #listenUsingRfcommWithServiceRecord}, if an
* encrypted and authenticated communication channel is desired.
* <p>Use {@link BluetoothServerSocket#accept} to retrieve incoming
//Synthetic comment -- @@ -828,6 +828,44 @@
return createNewRfcommSocketAndRecord(name, uuid, false, false);
}

private BluetoothServerSocket createNewRfcommSocketAndRecord(String name, UUID uuid,
boolean auth, boolean encrypt) throws IOException {
RfcommChannelPicker picker = new RfcommChannelPicker(uuid);
//Synthetic comment -- @@ -898,6 +936,28 @@
return socket;
}

/**
* Construct a SCO server socket.
* Call #accept to retrieve connections to this socket.







