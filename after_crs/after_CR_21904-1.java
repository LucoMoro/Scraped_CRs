/*Require bonding and encryption for PBAP server

The Phonebook Access Profile specification requires bonding and
encryption. For devices not supporting SSP (Secure Simple Pairing),
InsecureRfcomm will require neither. Adding EncryptedRfcomm to force
bonding and encryption but not requiring authenticated link key.

Change-Id:If47cca9c5ffd89358bcd61d64f7785d17e0ca7cc*/




//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothAdapter.java b/core/java/android/bluetooth/BluetoothAdapter.java
//Synthetic comment -- index a7175e3..4cff700 100644

//Synthetic comment -- @@ -898,6 +898,27 @@
return socket;
}

     /**
     * Construct an encrypted, but not authenticated link key, RFCOMM server socket.
     * Call #accept to retrieve connections to this socket.
     * @return An RFCOMM BluetoothServerSocket
     * @throws IOException On error, for example Bluetooth not available, or
     *                     insufficient permissions.
     * @hide
     */
    public BluetoothServerSocket listenUsingEncryptedRfcommOn(int port) throws IOException {
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







