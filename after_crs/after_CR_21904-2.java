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

     /**
     * Create a listening, encrypted,
     * RFCOMM Bluetooth socket with Service Record.
     * <p>The link will be encrypted, but the link key is not required to be authenticated
     * i.e the communication is vulnerable to Man In the Middle attacks. Use
     * {@link #listenUsingRfcommWithServiceRecord}, to ensure an authenticated link key.
     * <p> Use this socket if authentication of link key is not possible.
     * For example, for Bluetooth 2.1 devices, if any of the devices does not have
     * an input and output capability or just has the ability to display a numeric key,
     * a secure socket connection is not possible and this socket can be used.
     * For more details, refer to the Security Model section 5.2 (vol 3) of
     * Bluetooth Core Specification version 2.1 + EDR.
     * <p>Use {@link BluetoothServerSocket#accept} to retrieve incoming
     * connections from a listening {@link BluetoothServerSocket}.
     * <p>The system will assign an unused RFCOMM channel to listen on.
     * <p>The system will also register a Service Discovery
     * Protocol (SDP) record with the local SDP server containing the specified
     * UUID, service name, and auto-assigned channel. Remote Bluetooth devices
     * can use the same UUID to query our SDP server and discover which channel
     * to connect to. This SDP record will be removed when this socket is
     * closed, or if this application closes unexpectedly.
     * <p>Use {@link BluetoothDevice#createRfcommSocketToServiceRecord} to
     * connect to this socket from another device using the same {@link UUID}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH}
     * @param name service name for SDP record
     * @param uuid uuid for SDP record
     * @return a listening RFCOMM BluetoothServerSocket
     * @throws IOException on error, for example Bluetooth not available, or
     *                     insufficient permissions, or channel in use.
     * @hide
     */
    public BluetoothServerSocket listenUsingEncryptedRfcommWithServiceRecord(
            String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, false, true);
    }

private BluetoothServerSocket createNewRfcommSocketAndRecord(String name, UUID uuid,
boolean auth, boolean encrypt) throws IOException {
RfcommChannelPicker picker = new RfcommChannelPicker(uuid);
//Synthetic comment -- @@ -898,6 +934,28 @@
return socket;
}

     /**
     * Construct an encrypted, RFCOMM server socket.
     * Call #accept to retrieve connections to this socket.
     * @return An RFCOMM BluetoothServerSocket
     * @throws IOException On error, for example Bluetooth not available, or
     *                     insufficient permissions.
     * @hide
     */
    public BluetoothServerSocket listenUsingEncryptedRfcommOn(int port)
            throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(
                BluetoothSocket.TYPE_RFCOMM, false, true, port);
        int errno = socket.mSocket.bindListen();
        if (errno != 0) {
            try {
                socket.close();
            } catch (IOException e) {}
            socket.mSocket.throwErrnoNative(errno);
        }
        return socket;
    }

/**
* Construct a SCO server socket.
* Call #accept to retrieve connections to this socket.







