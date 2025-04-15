/*BT - Not possible to accept incoming obex transfers using key-presses.

It is not possible to accept/reject an incoming obex file transfer
using key-press. Only touch screen press was working.

With this fix, key press down is not ignored and it is possible
to accept/reject an incoming obex file transfer with key-press.

ST-Ericsson Change ID: ER260817
Change-Id:Ic7f668bd1b0efa549d0218edc8719efee1984941Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppIncomingFileConfirmActivity.java b/src/com/android/bluetooth/opp/BluetoothOppIncomingFileConfirmActivity.java
//Synthetic comment -- index fdc332c..ec0f8b2 100644

//Synthetic comment -- @@ -171,8 +171,9 @@

Toast.makeText(this, getString(R.string.bt_toast_2), Toast.LENGTH_SHORT).show();
finish();
}
        return true;
}

@Override







