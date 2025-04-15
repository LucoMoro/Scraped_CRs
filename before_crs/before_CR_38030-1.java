/*Pull vCard listing and pull vCard entry improvements

Solving the following bugs:

* List start offset is not correct when a search value is used. The
  offset is not used on the searched values but on the whole phonebook.
  This is not correct according to 5.3.4.3 and 5.3.4.5 in the PBAP
  specification.

* Handle in phonebook listing is only a counter and can’t be used
  to actually find the real vCard in pull vCard entry. This is clearly
  visible when using a listStartOffset in the phonebook vCard listing.
  This is not correct according to 3.1.5.1 in the PBAP specification.

* Alphabetical order does not work in search by number.
  The common solution for all these problems is to use the database
  more extensively for lookup. Furthermore to ensure unique handles
  in the phonebook listing the handle has been changed from a counter
  to database ID.

Change-Id:I294507252e69c7f6ec89ae2c8de3319cd693e47f*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 6002263..6897fbe 100755

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.util.Log;
import android.provider.CallLog.Calls;
import android.provider.CallLog;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -466,6 +467,54 @@
}
}

/** To parse obex application parameter */
private final boolean parseApplicationParameter(final byte[] appParam,
AppParamValue appParamValue) {
//Synthetic comment -- @@ -541,100 +590,22 @@
}

/** Form and Send an XML format String to client for Phone book listing */
    private final int sendVcardListingXml(final int type, Operation op,
            final int maxListCount, final int listStartOffset, final String searchValue,
            String searchAttr) {
        StringBuilder result = new StringBuilder();
        int itemsFound = 0;
        result.append("<?xml version=\"1.0\"?>");
        result.append("<!DOCTYPE vcard-listing SYSTEM \"vcard-listing.dtd\">");
        result.append("<vCard-listing version=\"1.0\">");

        // Phonebook listing request
        if (type == ContentType.PHONEBOOK) {
            if (searchAttr.equals("0")) { // search by name
                itemsFound = createList(maxListCount, listStartOffset, searchValue, result,
                        "name");
            } else if (searchAttr.equals("1")) { // search by number
                itemsFound = createList(maxListCount, listStartOffset, searchValue, result,
                        "number");
}// end of search by number
else {
return ResponseCodes.OBEX_HTTP_PRECON_FAILED;
}
        }
        // Call history listing request
        else {
            ArrayList<String> nameList = mVcardManager.loadCallHistoryList(type);
            int requestSize = nameList.size() >= maxListCount ? maxListCount : nameList.size();
            int startPoint = listStartOffset;
            int endPoint = startPoint + requestSize;
            if (endPoint > nameList.size()) {
                endPoint = nameList.size();
            }
            if (D) Log.d(TAG, "call log list, size=" + requestSize + " offset=" + listStartOffset);

            for (int j = startPoint; j < endPoint; j++) {
                // listing object begin with 1.vcf
                result.append("<card handle=\"" + (j + 1) + ".vcf\" name=\"" + nameList.get(j)
                        + "\"" + "/>");
                itemsFound++;
            }
        }
        result.append("</vCard-listing>");

        if (V) Log.v(TAG, "itemsFound =" + itemsFound);

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
                    break;
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
        return itemsFound;
}

/**
//Synthetic comment -- @@ -810,9 +781,7 @@
mOrderBy = ORDER_BY_ALPHABETICAL;
}

        int sendResult = sendVcardListingXml(appParamValue.needTag, op, appParamValue.maxListCount,
                appParamValue.listStartOffset, appParamValue.searchValue,
                appParamValue.searchAttr);
return sendResult;
}

//Synthetic comment -- @@ -844,7 +813,7 @@
Log.w(TAG, "wrong path!");
return ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE;
} else if (appParamValue.needTag == ContentType.PHONEBOOK) {
            if (intIndex < 0 || intIndex >= size) {
Log.w(TAG, "The requested vcard is not acceptable! name= " + name);
return ResponseCodes.OBEX_HTTP_NOT_FOUND;
} else if (intIndex == 0) {
//Synthetic comment -- @@ -852,8 +821,7 @@
String ownerVcard = mVcardManager.getOwnerPhoneNumberVcard(vcard21);
return pushBytes(op, ownerVcard);
} else {
                return mVcardManager.composeAndSendPhonebookOneVcard(op, intIndex, vcard21, null,
                        mOrderBy );
}
} else {
if (intIndex <= 0 || intIndex > size) {








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 659add6..c465e41 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import android.util.Log;

import com.android.bluetooth.R;
import com.android.vcard.VCardComposer;
import com.android.vcard.VCardConfig;
import com.android.internal.telephony.CallerInfo;
//Synthetic comment -- @@ -163,9 +164,9 @@
return size;
}

    public final ArrayList<String> loadCallHistoryList(final int type) {
final Uri myUri = CallLog.Calls.CONTENT_URI;
        String selection = BluetoothPbapObexServer.createSelectionPara(type);
String[] projection = new String[] {
Calls.NUMBER, Calls.CACHED_NAME
};
//Synthetic comment -- @@ -173,13 +174,12 @@
final int CALLS_NAME_COLUMN_INDEX = 1;

Cursor callCursor = null;
        ArrayList<String> list = new ArrayList<String>();
try {
callCursor = mResolver.query(myUri, projection, selection, null,
CALLLOG_SORT_ORDER);
            if (callCursor != null) {
                for (callCursor.moveToFirst(); !callCursor.isAfterLast();
                        callCursor.moveToNext()) {
String name = callCursor.getString(CALLS_NAME_COLUMN_INDEX);
if (TextUtils.isEmpty(name)) {
// name not found, use number instead
//Synthetic comment -- @@ -190,7 +190,9 @@
name = mContext.getString(R.string.unknownNumber);
}
}
                    list.add(name);
}
}
} finally {
//Synthetic comment -- @@ -198,78 +200,88 @@
callCursor.close();
}
}
        return list;
}

    public final ArrayList<String> getPhonebookNameList(final int orderByWhat) {
        ArrayList<String> nameList = new ArrayList<String>();
        nameList.add(BluetoothPbapService.getLocalPhoneName());

        final Uri myUri = Contacts.CONTENT_URI;
Cursor contactCursor = null;
try {
            if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_INDEXED) {
                if (V) Log.v(TAG, "getPhonebookNameList, order by index");
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE,
                        null, Contacts._ID);
            } else if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_ALPHABETICAL) {
                if (V) Log.v(TAG, "getPhonebookNameList, order by alpha");
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE,
                        null, Contacts.DISPLAY_NAME);
            }
            if (contactCursor != null) {
                for (contactCursor.moveToFirst(); !contactCursor.isAfterLast(); contactCursor
                        .moveToNext()) {
                    String name = contactCursor.getString(CONTACTS_NAME_COLUMN_INDEX);
                    if (TextUtils.isEmpty(name)) {
                        name = mContext.getString(android.R.string.unknownName);
                    }
                    nameList.add(name);
}
}
} finally {
if (contactCursor != null) {
contactCursor.close();
}
}
        return nameList;
}

    public final ArrayList<String> getContactNamesByNumber(final String phoneNumber) {
        ArrayList<String> nameList = new ArrayList<String>();

Cursor contactCursor = null;
        Uri uri = null;

        if (phoneNumber != null && phoneNumber.length() == 0) {
            uri = Contacts.CONTENT_URI;
        } else {
            uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        }

try {
            contactCursor = mResolver.query(uri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE,
                        null, Contacts._ID);

            if (contactCursor != null) {
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
if (contactCursor != null) {
contactCursor.close();
}
}
        return nameList;
}

public final int composeAndSendCallLogVcards(final int type, Operation op,
//Synthetic comment -- @@ -378,53 +390,28 @@
return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
}

    public final int composeAndSendPhonebookOneVcard(Operation op, final int offset,
            final boolean vcardType21, String ownerVCard, int orderByWhat) {
        if (offset < 1) {
            Log.e(TAG, "Internal error: offset is not correct.");
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        final Uri myUri = Contacts.CONTENT_URI;
        Cursor contactCursor = null;
        String selection = null;
        long contactId = 0;
        if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_INDEXED) {
            try {
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE,
                        null, Contacts._ID);
                if (contactCursor != null) {
                    contactCursor.moveToPosition(offset - 1);
                    contactId = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
                    if (V) Log.v(TAG, "Query startPointId = " + contactId);
                }
            } finally {
                if (contactCursor != null) {
                    contactCursor.close();
                }
            }
        } else if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_ALPHABETICAL) {
            try {
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE,
                        null, Contacts.DISPLAY_NAME);
                if (contactCursor != null) {
                    contactCursor.moveToPosition(offset - 1);
                    contactId = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
                    if (V) Log.v(TAG, "Query startPointId = " + contactId);
                }
            } finally {
                if (contactCursor != null) {
                    contactCursor.close();
                }
            }
        } else {
            Log.e(TAG, "Parameter orderByWhat is not supported!");
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        selection = Contacts._ID + "=" + contactId;

        if (V) Log.v(TAG, "Query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
}

public final int composeAndSendVCards(Operation op, final String selection,







