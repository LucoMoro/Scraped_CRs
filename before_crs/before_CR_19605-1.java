/*pullVcardListing of unknown/hidden phone number.

When performing a pullVcardListing on a phone with a
call list which contains a unknown phone number, the phone
returns "-1" as name. Corresponding errors exist for private
and payphone numbers, where the phone returns the name "-2"
and "-3", respectively.

Instead the phone should use name = "Unknown" when
the phone number is unknown. Currently there are no other
matching corresponding strings defined for private
and payphone number, which means that for now "Unknown"
may be returned as name also for these numbers.

Change-Id:I33cf6ceecf5c8fba426eacbb3a507228f8e4de4d*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 118403b..701261b 100644

//Synthetic comment -- @@ -184,6 +184,10 @@
if (TextUtils.isEmpty(name)) {
// name not found,use number instead
name = callCursor.getString(CALLS_NUMBER_COLUMN_INDEX);
}
list.add(name);
}







