/*pullVcard of unknown phone number

When performing a pullVcardListing, pullVcardEntry or pullPhonebook
on a phone with a call list which contains an unknown phone number,
the phone returns "-1" as number/name. Corresponding errors exist
for private and payphone numbers, where the phone returns the name
"-2" and "-3", respectively. During pullVcardListing the Name
attribute may contain the phone number, in case the name is not
known. The latter is already implemented today, however, instead of
using e.g. "-1" as name when the phone number is not known the
phone should use Name = "Unknown". Currently there are no other
matching corresponding strings defined for private and payphone
number, which means that for now "Unknown" may be returned as name
also for these numbers.
During pullVcardEntry and during pullPhonebook when the phone number
is unknown then the Number attribute should contain the string
"Unknown". During the same scenario the FN and N attributes both
should contain the string "" when the name is not known. When it
comes to private and payphone numbers the same as for
pullVcardListing apply here, i.e. "Unknown" may be used in the
Number attribute also for these numbers

Change-Id:I33cf6ceecf5c8fba426eacbb3a507228f8e4de4d*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java
//Synthetic comment -- index f395d21..0ee8b6f 100644

//Synthetic comment -- @@ -194,12 +194,16 @@
if (TextUtils.isEmpty(name)) {
name = mCursor.getString(NUMBER_COLUMN_INDEX);
}
        if (name != null && (name.equals("-1") || name.equals("-2") || name.equals("-3"))) {
            name = "";
        }
final boolean needCharset = !(VCardUtils.containsOnlyPrintableAscii(name));
builder.appendLine(VCardConstants.PROPERTY_FN, name, needCharset, false);
builder.appendLine(VCardConstants.PROPERTY_N, name, needCharset, false);

String number = mCursor.getString(NUMBER_COLUMN_INDEX);
        if (number != null && (number.equals("-1") || number.equals("-2") ||
                number.equals("-3"))) {
number = mContext.getString(R.string.unknownNumber);
}
final int type = mCursor.getInt(CALLER_NUMBERTYPE_COLUMN_INDEX);








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 118403b..701261b 100644

//Synthetic comment -- @@ -184,6 +184,10 @@
if (TextUtils.isEmpty(name)) {
// name not found,use number instead
name = callCursor.getString(CALLS_NUMBER_COLUMN_INDEX);
                        if (name != null && (name.equals("-1") || name.equals("-2") ||
                                name.equals("-3"))) {
                            name = mContext.getString(R.string.unknownNumber);
                        }
}
list.add(name);
}







