/*BT: Fix for vCard Listing error

This patch fixes listing of the incomplete vCard xml file, which
contains special characters which are larger than one byte.

Change-Id:Ice5a056004aa30255a06f8742428d4bcfa5813c8Signed-off-by: christian bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index fa645ff..42c078d 100755

//Synthetic comment -- @@ -670,7 +670,8 @@
return ResponseCodes.OBEX_HTTP_OK;
}

        byte[] vcardBytes = vcardString.getBytes();
        int vcardStringLen = vcardBytes.length;
if (D) Log.d(TAG, "Send Data: len=" + vcardStringLen);

OutputStream outputStream = null;
//Synthetic comment -- @@ -697,9 +698,9 @@
if (vcardStringLen - position < outputBufferSize) {
readLength = vcardStringLen - position;
}
            byte[] subByteArray = Arrays.copyOfRange(vcardBytes, position, position + readLength);
try {
                outputStream.write(subByteArray, 0, readLength);
} catch (IOException e) {
Log.e(TAG, "write outputstrem failed" + e.toString());
pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;







