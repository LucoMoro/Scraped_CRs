/*Incorrect response code after pull of invalid vCard handle.

When calling pullVcardEntry with an invalid / non-existing
vCard handle the response code is always "OK".

Instead the phone should respond "NOT FOUND" according
to Bluetooth PBAP specification.

The problem exists both for phonebook and call logs.

Change-Id:I339b1fe0cdb7fa9b6c6b425f1b1686c24cab9626*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index eccd3a2..7cd9492 100644

//Synthetic comment -- @@ -869,7 +869,7 @@
int size = mVcardManager.getPhonebookSize(appParamValue.needTag);
if (size == 0) {
if (V) Log.v(TAG, "PhonebookSize is 0, return.");
            return ResponseCodes.OBEX_HTTP_OK;
}

boolean vcard21 = appParamValue.vcard21;
//Synthetic comment -- @@ -879,7 +879,7 @@
} else if (appParamValue.needTag == ContentType.PHONEBOOK) {
if (intIndex < 0 || intIndex >= size) {
Log.w(TAG, "The requested vcard is not acceptable! name= " + name);
                return ResponseCodes.OBEX_HTTP_OK;
} else if (intIndex == 0) {
// For PB_PATH, 0.vcf is the phone number of this phone.
String ownerVcard = mVcardManager.getOwnerPhoneNumberVcard(vcard21);
//Synthetic comment -- @@ -891,7 +891,7 @@
} else {
if (intIndex <= 0 || intIndex > size) {
Log.w(TAG, "The requested vcard is not acceptable! name= " + name);
                return ResponseCodes.OBEX_HTTP_OK;
}
// For others (ich/och/cch/mch), 0.vcf is meaningless, and must
// begin from 1.vcf







