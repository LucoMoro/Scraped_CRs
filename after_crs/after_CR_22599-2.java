/*Handle OBEX Abort packets sent from remote device

This patch adds support for handling OBEX Abort packets
sent from remote device, where the remote device is acting
as a client, sending files to the server.

Depends-On:I0fc2255c463c5ce1e8fa1d7febf144b60965a1beChange-Id:I4bce05a9007ba02e9d9987ec3cbbf1db380247f0Signed-off-by: christian bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index a58df0d..02a687b 100644

//Synthetic comment -- @@ -473,7 +473,12 @@
}
} catch (IOException e1) {
Log.e(TAG, "Error when receiving file");
                /* OBEX Abort packet received from remote device */
                if (e1.getMessage().equals("Abort Received")) {
                    status = BluetoothShare.STATUS_CANCELED;
                } else {
                    status = BluetoothShare.STATUS_OBEX_DATA_ERROR;
                }
error = true;
}
}







