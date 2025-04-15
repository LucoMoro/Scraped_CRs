/*Handle pullVcardListing req without search value for Number attr.

When sending pullVcardListing request to the phone while using
the search attribute "Number" without any search value, the
phone responds with error code "internal error".

Instead the phone should return all numbers in the phonebook.

Change-Id:I7c11b76bbd61882c62be3533b1c45c2c72f497b2*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 118403b..3c6f389 100644

//Synthetic comment -- @@ -234,8 +234,14 @@
ArrayList<String> nameList = new ArrayList<String>();

Cursor contactCursor = null;
        final Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
Uri.encode(phoneNumber));

try {
contactCursor = mResolver.query(uri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE,







