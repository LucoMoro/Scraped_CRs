/*Handle PBAP response packet as raw bytes

Adjust PBAP response packet to be always handled as bytes.
Currently, it is handled as string when is read from internal data.
This causes the lack of PullvCardListing packet and the abort of
PullPhonebook transaction when multi-byte characters are contained
into corresponding contacts.
This fix will avoid the lack and the abort by writing whole packet
into the target stream as byte.

Change-Id:I17ee3ada25718cc263c9e0942f75605bd971b638*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index fa645ff..0d60236 100755

//Synthetic comment -- @@ -670,51 +670,17 @@
return ResponseCodes.OBEX_HTTP_OK;
}

        int vcardStringLen = vcardString.length();
        if (D) Log.d(TAG, "Send Data: len=" + vcardStringLen);

OutputStream outputStream = null;
int pushResult = ResponseCodes.OBEX_HTTP_OK;
try {
outputStream = op.openOutputStream();
} catch (IOException e) {
            Log.e(TAG, "open outputstrem failed" + e.toString());
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
}

        int position = 0;
        long timestamp = 0;
        int outputBufferSize = op.getMaxPacketSize();
        if (V) Log.v(TAG, "outputBufferSize = " + outputBufferSize);
        while (position != vcardStringLen) {
            if (sIsAborted) {
                ((ServerOperation)op).isAborted = true;
                sIsAborted = false;
                break;
            }
            if (V) timestamp = System.currentTimeMillis();
            int readLength = outputBufferSize;
            if (vcardStringLen - position < outputBufferSize) {
                readLength = vcardStringLen - position;
            }
            String subStr = vcardString.substring(position, position + readLength);
            try {
                outputStream.write(subStr.getBytes(), 0, readLength);
            } catch (IOException e) {
                Log.e(TAG, "write outputstrem failed" + e.toString());
                pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
                break;
            }
            if (V) {
                Log.v(TAG, "Sending vcard String position = " + position + " readLength "
                        + readLength + " bytes took " + (System.currentTimeMillis() - timestamp)
                        + " ms");
            }
            position += readLength;
        }

        if (V) Log.v(TAG, "Send Data complete!");

if (!closeStream(outputStream, op)) {
pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3c6f389..29c03ec

//Synthetic comment -- @@ -68,8 +68,6 @@

private Context mContext;

    private StringBuilder mVcardResults = null;

static final String[] PHONES_PROJECTION = new String[] {
Data._ID, // 0
CommonDataKinds.Phone.TYPE, // 1
//Synthetic comment -- @@ -510,6 +508,8 @@

private String phoneOwnVCard = null;

public HandlerForStringBuffer(Operation op, String ownerVCard) {
operation = op;
maxPacketSize = operation.getMaxPacketSize();
//Synthetic comment -- @@ -522,64 +522,27 @@
}

public boolean onInit(Context context) {
            try {
                outputStream = operation.openOutputStream();
                mVcardResults = new StringBuilder();
                if (phoneOwnVCard != null) {
                    mVcardResults.append(phoneOwnVCard);
                }
            } catch (IOException e) {
                Log.e(TAG, "open outputstrem failed" + e.toString());
                return false;
}
            if (V) Log.v(TAG, "openOutputStream() ok.");
return true;
}

public boolean onEntryCreated(String vcard) {
            int vcardLen = vcard.length();
            if (V) Log.v(TAG, "The length of this vcard is: " + vcardLen);

mVcardResults.append(vcard);
            int vcardByteLen = mVcardResults.toString().getBytes().length;
            if (V) Log.v(TAG, "The byte length of this vcardResults is: " + vcardByteLen);

            if (vcardByteLen >= maxPacketSize) {
                long timestamp = 0;
                int position = 0;

                // Need while loop to handle the big vcard case
                while (!BluetoothPbapObexServer.sIsAborted
                        && position < (vcardByteLen - maxPacketSize)) {
                    if (V) timestamp = System.currentTimeMillis();

                    String subStr = mVcardResults.toString().substring(position,
                            position + maxPacketSize);
                    try {
                        outputStream.write(subStr.getBytes(), 0, maxPacketSize);
                    } catch (IOException e) {
                        Log.e(TAG, "write outputstrem failed" + e.toString());
                        return false;
                    }
                    if (V) Log.v(TAG, "Sending vcard String " + maxPacketSize + " bytes took "
                            + (System.currentTimeMillis() - timestamp) + " ms");

                    position += maxPacketSize;
                }
                mVcardResults.delete(0, position);
            }
return true;
}

public void onTerminate() {
            // Send out last packet
            byte[] lastBytes = mVcardResults.toString().getBytes();
try {
                outputStream.write(lastBytes, 0, lastBytes.length);
} catch (IOException e) {
Log.e(TAG, "write outputstrem failed" + e.toString());
}
            if (V) Log.v(TAG, "Last packet sent out, sending process complete!");

if (!BluetoothPbapObexServer.closeStream(outputStream, operation)) {
if (V) Log.v(TAG, "CloseStream failed!");







