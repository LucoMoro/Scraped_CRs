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

OutputStream outputStream = null;
int pushResult = ResponseCodes.OBEX_HTTP_OK;
try {
outputStream = op.openOutputStream();
            outputStream.write(vcardString.getBytes());
            if (V) Log.v(TAG, "Send Data complete!");
} catch (IOException e) {
            Log.e(TAG, "write outputstrem failed" + e.toString());
            pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
}

if (!closeStream(outputStream, op)) {
pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3c6f389..012dc45

//Synthetic comment -- @@ -68,8 +68,6 @@

private Context mContext;

static final String[] PHONES_PROJECTION = new String[] {
Data._ID, // 0
CommonDataKinds.Phone.TYPE, // 1
//Synthetic comment -- @@ -499,21 +497,18 @@
}

/**
     * Handler to emit vCards to PCE.
*/
public class HandlerForStringBuffer implements OneEntryHandler {
        @SuppressWarnings("hiding")
private Operation operation;

private OutputStream outputStream;

private String phoneOwnVCard = null;

public HandlerForStringBuffer(Operation op, String ownerVCard) {
operation = op;
if (ownerVCard != null) {
phoneOwnVCard = ownerVCard;
if (V) Log.v(TAG, "phone own number vcard:");
//Synthetic comment -- @@ -521,66 +516,36 @@
}
}

        private boolean write(String vCard) {
try {
                if (vCard != null) {
                    outputStream.write(vCard.getBytes());
                    return true;
}
} catch (IOException e) {
Log.e(TAG, "write outputstrem failed" + e.toString());
}
            return false;
        }

        public boolean onInit(Context context) {
            try {
                outputStream = operation.openOutputStream();
                if (phoneOwnVCard != null) {
                    return write(phoneOwnVCard);
                }
                return true;
            } catch (IOException e) {
                Log.e(TAG, "open outputstrem failed" + e.toString());
            }
            return false;
        }

        public boolean onEntryCreated(String vcard) {
            return write(vcard);
        }

        public void onTerminate() {
if (!BluetoothPbapObexServer.closeStream(outputStream, operation)) {
if (V) Log.v(TAG, "CloseStream failed!");
} else {







