/*pullVcardListing(Number) when multiple contacts have same name.

When performing pullVcardListing with the search attribute
Number set to a value belonging to a contact named "A", and
there is another contact also named "A" but holding completely
different phone numbers, then the response of the
pullVcardListing procedure contains both of the "A" contacts.

Instead only the contact actually holding the Number value
that is search for should be returned, i.e. in the case above
only one contact "A".

Change-Id:Id278e1def30c8906230048fc6dd8236e94719c44*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 7cd9492..bdc4526 100644

//Synthetic comment -- @@ -588,31 +588,65 @@
return pushBytes(op, result.toString());
}

private int createList(final int maxListCount, final int listStartOffset,
final String searchValue, StringBuilder result, String type) {
int itemsFound = 0;
        ArrayList<String> nameList = mVcardManager.getPhonebookNameList(mOrderBy);
        final int requestSize = nameList.size() >= maxListCount ? maxListCount : nameList.size();
        final int listSize = nameList.size();
        String compareValue = "", currentValue;

if (D) Log.d(TAG, "search by " + type + ", requestSize=" + requestSize + " offset="
+ listStartOffset + " searchValue=" + searchValue);

if (type.equals("number")) {
// query the number, to get the names
            ArrayList<String> names = mVcardManager.getContactNamesByNumber(searchValue);
            for (int i = 0; i < names.size(); i++) {
                compareValue = names.get(i).trim();
                if (D) Log.d(TAG, "compareValue=" + compareValue);
for (int pos = listStartOffset; pos < listSize &&
itemsFound < requestSize; pos++) {
                    currentValue = nameList.get(pos);
                    if (D) Log.d(TAG, "currentValue=" + currentValue);
                    if (currentValue.startsWith(compareValue)) {
itemsFound++;
result.append("<card handle=\"" + pos + ".vcf\" name=\""
                                + currentValue + "\"" + "/>");
}
}
if (itemsFound >= requestSize) {
//Synthetic comment -- @@ -620,17 +654,14 @@
}
}
} else {
            if (searchValue != null) {
                compareValue = searchValue.trim();
            }
for (int pos = listStartOffset; pos < listSize &&
itemsFound < requestSize; pos++) {
                currentValue = nameList.get(pos);
                if (D) Log.d(TAG, "currentValue=" + currentValue);
                if (searchValue == null || currentValue.startsWith(compareValue)) {
itemsFound++;
result.append("<card handle=\"" + pos + ".vcf\" name=\""
                            + currentValue + "\"" + "/>");
}
}
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 118403b..6d454a5 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import android.util.Log;

import com.android.bluetooth.R;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -196,9 +197,13 @@
return list;
}

    public final ArrayList<String> getPhonebookNameList(final int orderByWhat) {
        ArrayList<String> nameList = new ArrayList<String>();
        nameList.add(BluetoothPbapService.getLocalPhoneName());

final Uri myUri = Contacts.CONTENT_URI;
Cursor contactCursor = null;
//Synthetic comment -- @@ -219,7 +224,10 @@
if (TextUtils.isEmpty(name)) {
name = mContext.getString(android.R.string.unknownName);
}
                    nameList.add(name);
}
}
} finally {
//Synthetic comment -- @@ -230,8 +238,10 @@
return nameList;
}

    public final ArrayList<String> getContactNamesByNumber(final String phoneNumber) {
        ArrayList<String> nameList = new ArrayList<String>();

Cursor contactCursor = null;
final Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
//Synthetic comment -- @@ -245,12 +255,16 @@
for (contactCursor.moveToFirst(); !contactCursor.isAfterLast(); contactCursor
.moveToNext()) {
String name = contactCursor.getString(CONTACTS_NAME_COLUMN_INDEX);
                    long id = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
if (TextUtils.isEmpty(name)) {
name = mContext.getString(android.R.string.unknownName);
}
                    if (V) Log.v(TAG, "got name " + name + " by number " + phoneNumber + " @" + id);
                    nameList.add(name);
}
}
} finally {







