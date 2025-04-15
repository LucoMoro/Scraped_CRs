/*Require bonding and encryption for PBAP server

The Phonebook Access Profile specification requires bonding and
encryption. For devices not supporting SSP (Secure Simple Pairing),
InsecureRfcomm will require neither. Adding EncryptedRfcomm to force
bonding and encryption but not requiring authenticated link key.

Change-Id:I987ca50ff7f60813b5311cead8d6a30a9123d54b*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapService.java b/src/com/android/bluetooth/pbap/BluetoothPbapService.java
//Synthetic comment -- index 3b1216e..0391075 100644

//Synthetic comment -- @@ -326,13 +326,7 @@
try {
// It is mandatory for PSE to support initiation of bonding and
// encryption.
                // InsecureRfcomm => encryption is on, authentication of link
                // key is off. For legacy pairing it doesn't matter, for 2.1
                // pairing - if we have already done MITM protection, then
                // the same linkeys will be used. If not, then all core profiles
                // will have the same level of protection. This API has to be
                // renamed - its no as insecure as the name suggests.
                mServerSocket = mAdapter.listenUsingInsecureRfcommOn(PORT_NUM);
} catch (IOException e) {
Log.e(TAG, "Error create RfcommServerSocket " + e.toString());
initSocketOK = false;







