/*Bluetooth: Add vCard version support for call history.

Under current implementation, PSE only deliver call history
under format VCard2.1 to PCE, this is not correct according to
PBAP specification, which says "The PSE shall support both
vCard2.1 and vCard3.0 versions and deliver the Entries to the
PCE under the format version that is requested by the PCE."
This fix is to make implementation following such PBAP
specificaiton.

Change-Id:Id0146d0887bad3bd066109ecd3be674b3106bced*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java
//Synthetic comment -- index f395d21..b10ec2a 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
}
}

    public boolean createOneEntry() {
if (mCursor == null || mCursor.isAfterLast()) {
mErrorReason = FAILURE_REASON_NOT_INITIALIZED;
return false;
//Synthetic comment -- @@ -160,7 +160,7 @@

final String vcard;
try {
            vcard = createOneCallLogEntryInternal();
} catch (OutOfMemoryError error) {
Log.e(TAG, "OutOfMemoryError occured. Ignore the entry");
System.gc();
//Synthetic comment -- @@ -184,10 +184,9 @@
return true;
}

    private String createOneCallLogEntryInternal() {
        // We should not allow vCard composer to re-format phone numbers, since
        // some characters are (inappropriately) removed and devices do not work fine.
        final int vcardType = VCardConfig.VCARD_TYPE_V21_GENERIC |
VCardConfig.FLAG_REFRAIN_PHONE_NUMBER_FORMATTING;
final VCardBuilder builder = new VCardBuilder(vcardType);
String name = mCursor.getString(CALLER_NAME_COLUMN_INDEX);








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 118403b..ac750ac 100644

//Synthetic comment -- @@ -473,7 +473,7 @@
BluetoothPbapObexServer.sIsAborted = false;
break;
}
                    if (!composer.createOneEntry()) {
Log.e(TAG, "Failed to read a contact. Error reason: "
+ composer.getErrorReason());
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;







