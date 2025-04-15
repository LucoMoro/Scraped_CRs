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
//Synthetic comment -- index b10ec2a..ebdc8b9 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.bluetooth.pbap;

import com.android.bluetooth.R;
import com.android.internal.telephony.CallerInfo;

import android.content.ContentResolver;
import android.content.Context;
//Synthetic comment -- @@ -193,13 +194,19 @@
if (TextUtils.isEmpty(name)) {
name = mCursor.getString(NUMBER_COLUMN_INDEX);
}
        if (CallerInfo.UNKNOWN_NUMBER.equals(name) || CallerInfo.PRIVATE_NUMBER.equals(name) ||
                CallerInfo.PAYPHONE_NUMBER.equals(name)) {
            name = "";
        }
final boolean needCharset = !(VCardUtils.containsOnlyPrintableAscii(name));
builder.appendLine(VCardConstants.PROPERTY_FN, name, needCharset, false);
builder.appendLine(VCardConstants.PROPERTY_N, name, needCharset, false);

String number = mCursor.getString(NUMBER_COLUMN_INDEX);
        if (CallerInfo.UNKNOWN_NUMBER.equals(name) ||
                CallerInfo.PRIVATE_NUMBER.equals(name) ||
                CallerInfo.PAYPHONE_NUMBER.equals(name)) {
            name = mContext.getString(R.string.unknownNumber);
}
final int type = mCursor.getInt(CALLER_NUMBERTYPE_COLUMN_INDEX);
String label = mCursor.getString(CALLER_NUMBERLABEL_COLUMN_INDEX);








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 3e3fa44..aaca906 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import android.util.Log;

import com.android.bluetooth.R;
import com.android.internal.telephony.CallerInfo;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -182,8 +183,13 @@
callCursor.moveToNext()) {
String name = callCursor.getString(CALLS_NAME_COLUMN_INDEX);
if (TextUtils.isEmpty(name)) {
                        // name not found, use number instead
name = callCursor.getString(CALLS_NUMBER_COLUMN_INDEX);
                        if (CallerInfo.UNKNOWN_NUMBER.equals(name) ||
                                CallerInfo.PRIVATE_NUMBER.equals(name) ||
                                CallerInfo.PAYPHONE_NUMBER.equals(name)) {
                            name = mContext.getString(R.string.unknownNumber);
                        }
}
list.add(name);
}







