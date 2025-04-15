/*Send vCard with Unicode encoded Strings over BT

The standard Android code doesn't correctly handle creation of byte
packets when sending vCard data.  The length of a String is used to
specify the number of bytes being sent, which works if all
characters are ASCII (1 byte per char), but if Unicode characters
exist within the string, then bytes vs String length differ which
causes a truncation of the vCard during Bluetooth send.

Change-Id:Ic39dd53a38f1f353523c625940b0317b00e86d4f*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index de8a3fd..e9e7805 100644

//Synthetic comment -- @@ -535,16 +535,16 @@
if (V) Log.v(TAG, "The length of this vcard is: " + vcardLen);

mVcardResults.append(vcard);
            int vcardStringLen = mVcardResults.toString().length();
            if (V) Log.v(TAG, "The length of this vcardResults is: " + vcardStringLen);

            if (vcardStringLen >= maxPacketSize) {
long timestamp = 0;
int position = 0;

// Need while loop to handle the big vcard case
while (!BluetoothPbapObexServer.sIsAborted
                        && position < (vcardStringLen - maxPacketSize)) {
if (V) timestamp = System.currentTimeMillis();

String subStr = mVcardResults.toString().substring(position,
//Synthetic comment -- @@ -567,9 +567,9 @@

public void onTerminate() {
// Send out last packet
            String lastStr = mVcardResults.toString();
try {
                outputStream.write(lastStr.getBytes(), 0, lastStr.length());
} catch (IOException e) {
Log.e(TAG, "write outputstrem failed" + e.toString());
}







