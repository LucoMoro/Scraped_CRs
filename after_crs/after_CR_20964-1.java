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
//Synthetic comment -- index 7cd9492..f2c4eb8 100644

//Synthetic comment -- @@ -588,31 +588,65 @@
return pushBytes(op, result.toString());
}

    final static class ListContact {
        private final String mName;
        private final long mId;

        public ListContact(final String name, final long id) {
            mName = name == null ? "" : name.trim();
            mId = id;
        }

        public String getName() {
            return mName;
        }

        public long getId() {
            return mId;
        }

        public boolean matches(final ListContact lc) {
            if (lc == null) {
                return false;
            }
            return mName.startsWith(lc.getName()) && mId == lc.getId();
        }

        public boolean matches(final String name) {
            return name == null ? false : mName.startsWith(name.trim());
        }

        @Override
        public String toString() {
            return "name=" + mName + ", mId=" + mId;
        }
    }

private int createList(final int maxListCount, final int listStartOffset,
final String searchValue, StringBuilder result, String type) {
int itemsFound = 0;
        ArrayList<ListContact> contacts = mVcardManager.getPhonebookContacts(mOrderBy);
        final int listSize = contacts.size();
        final int requestSize = listSize >= maxListCount ? maxListCount : listSize;

if (D) Log.d(TAG, "search by " + type + ", requestSize=" + requestSize + " offset="
+ listStartOffset + " searchValue=" + searchValue);

if (type.equals("number")) {
// query the number, to get the names
            ArrayList<ListContact> compareContacts =
                mVcardManager.getContactsByNumber(searchValue);
            for (int i = 0; i < compareContacts.size(); i++) {
                ListContact compareContact = compareContacts.get(i);
                if (D) Log.d(TAG, "compareContact: " + compareContact.toString());
for (int pos = listStartOffset; pos < listSize &&
itemsFound < requestSize; pos++) {
                    ListContact currentContact = contacts.get(pos);
                    if (D) Log.d(TAG, "currentContact: " + currentContact.toString());
                    if (currentContact.matches(compareContact)) {
itemsFound++;
result.append("<card handle=\"" + pos + ".vcf\" name=\""
                                + currentContact.getName() + "\"" + "/>");
}
}
if (itemsFound >= requestSize) {
//Synthetic comment -- @@ -620,17 +654,14 @@
}
}
} else {
for (int pos = listStartOffset; pos < listSize &&
itemsFound < requestSize; pos++) {
                ListContact currentContact = contacts.get(pos);
                if (D) Log.d(TAG, "currentContact: " + currentContact.toString());
                if (currentContact.matches(searchValue)) {
itemsFound++;
result.append("<card handle=\"" + pos + ".vcf\" name=\""
                            + currentContact.getName() + "\"" + "/>");
}
}
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 118403b..6638fc0 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import android.util.Log;

import com.android.bluetooth.R;
import com.android.bluetooth.pbap.BluetoothPbapObexServer.ListContact;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -196,9 +197,13 @@
return list;
}

    public final ArrayList<ListContact>
            getPhonebookContacts(final int orderByWhat) {
        final ArrayList<ListContact> nameList = new ArrayList<ListContact>();

        ListContact lContact = new ListContact(BluetoothPbapService.getLocalPhoneName(), 0);
        if (V) Log.v(TAG, "added contact: " + lContact.toString());
        nameList.add(lContact);

final Uri myUri = Contacts.CONTENT_URI;
Cursor contactCursor = null;
//Synthetic comment -- @@ -219,7 +224,10 @@
if (TextUtils.isEmpty(name)) {
name = mContext.getString(android.R.string.unknownName);
}
                    lContact = new ListContact(name,
                        contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX));
                    if (V) Log.v(TAG, "added contact: " + lContact.toString());
                    nameList.add(lContact);
}
}
} finally {
//Synthetic comment -- @@ -230,8 +238,10 @@
return nameList;
}

    public final ArrayList<ListContact>
            getContactsByNumber(final String phoneNumber) {
        ArrayList<ListContact> nameList = new ArrayList<ListContact>();
        ListContact lContact;

Cursor contactCursor = null;
final Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
//Synthetic comment -- @@ -245,12 +255,16 @@
for (contactCursor.moveToFirst(); !contactCursor.isAfterLast(); contactCursor
.moveToNext()) {
String name = contactCursor.getString(CONTACTS_NAME_COLUMN_INDEX);
if (TextUtils.isEmpty(name)) {
name = mContext.getString(android.R.string.unknownName);
}
                    lContact = new ListContact(name,
                        contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX));
                    if (V) {
                        Log.v(TAG, "got contact: " + lContact.toString() + " by number " +
                                phoneNumber);
                    }
                    nameList.add(lContact);
}
}
} finally {







