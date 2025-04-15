/*PBAP: Fix NAME tag value of vCard

When the phone receives Pull vCard Entry Request from a PCE device,
PBAP application sends the phone book object which is represented
as one single file that contains all of the corresponding phone
book entries. Currently the phone book entry contains a phone number
in N and FN fields when phone number is not locally saved in
contacts. This behaviour is not according to PBAP_SPEC V11r00.

The solution is to change the value used in N and FN fields to blank
when phone number is not locally saved in contacts.

Change-Id:Idab259befa778fad7649d765fe8174f35b0fb6d1*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java
//Synthetic comment -- index c5449fa..586d711 100755

//Synthetic comment -- @@ -188,7 +188,7 @@
final VCardBuilder builder = new VCardBuilder(VCardConfig.VCARD_TYPE_V21_GENERIC_UTF8);
String name = mCursor.getString(CALLER_NAME_COLUMN_INDEX);
if (TextUtils.isEmpty(name)) {
            name = mCursor.getString(NUMBER_COLUMN_INDEX);
}
final boolean needCharset = !(VCardUtils.containsOnlyPrintableAscii(name));
builder.appendLine(VCardConstants.PROPERTY_FN, name, needCharset, false);







