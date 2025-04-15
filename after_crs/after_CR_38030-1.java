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
import android.provider.ContactsContract.Contacts;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -466,6 +467,54 @@
}
}

    final static class VcardBuilder {
        private StringBuilder buffer = new StringBuilder();
        private int count = 0;
        private final AppParamValue appParamValue;
        private final String order;

        private VcardBuilder(AppParamValue appParamValue, int orderBy) {
            this.appParamValue = appParamValue;
            order = (orderBy == BluetoothPbapObexServer.ORDER_BY_INDEXED) ?
                    Contacts._ID : Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
            buffer.append("<?xml version=\"1.0\"?>");
            buffer.append("<!DOCTYPE vcard-listing SYSTEM \"vcard-listing.dtd\">");
            buffer.append("<vCard-listing version=\"1.0\">");
        }

        @Override
        public String toString() {
            if (V) Log.v(TAG, "itemsFound =" + count);
            return buffer.toString() + "</vCard-listing>";
        }

        void append(final long handle, final String name) {
            if (V) Log.v(TAG, "got: '" + name + "' with handle '" + handle + "'");
            buffer.append("<card handle=\"");
            buffer.append(handle);
            buffer.append(".vcf\" name=\"");
            buffer.append(name);
            buffer.append("\"/>");
            count++;
        }

        boolean needMore() {
            return (count < appParamValue.maxListCount);
        }
        String getSearchValue() {
            return appParamValue.searchValue;
        }
        int getType() {
            return appParamValue.needTag;
        }
        String getOrder() {
            return order;
        }
        int getStartOffset() {
            return appParamValue.listStartOffset;
        }
    }

/** To parse obex application parameter */
private final boolean parseApplicationParameter(final byte[] appParam,
AppParamValue appParamValue) {
//Synthetic comment -- @@ -541,100 +590,22 @@
}

/** Form and Send an XML format String to client for Phone book listing */
    private final int sendVcardListingXml(Operation op, final AppParamValue appParamValue) {
        VcardBuilder builder = new VcardBuilder(appParamValue, mOrderBy);

        if (builder.getType() == ContentType.PHONEBOOK) {
            if (appParamValue.searchAttr.equals("0")) { // search by name
                mVcardManager.buildPhonebookContactsByName(builder);
            } else if (appParamValue.searchAttr.equals("1")) { // search by number
                mVcardManager.buildPhonebookContactsByNumber(builder);
}// end of search by number
else {
return ResponseCodes.OBEX_HTTP_PRECON_FAILED;
}
} else {
            mVcardManager.buildCallHistory(builder);
}
        return pushBytes(op, builder.toString());
}

/**
//Synthetic comment -- @@ -810,9 +781,7 @@
mOrderBy = ORDER_BY_ALPHABETICAL;
}

        int sendResult = sendVcardListingXml(op, appParamValue);
return sendResult;
}

//Synthetic comment -- @@ -844,7 +813,7 @@
Log.w(TAG, "wrong path!");
return ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE;
} else if (appParamValue.needTag == ContentType.PHONEBOOK) {
            if (intIndex < 0) {
Log.w(TAG, "The requested vcard is not acceptable! name= " + name);
return ResponseCodes.OBEX_HTTP_NOT_FOUND;
} else if (intIndex == 0) {
//Synthetic comment -- @@ -852,8 +821,7 @@
String ownerVcard = mVcardManager.getOwnerPhoneNumberVcard(vcard21);
return pushBytes(op, ownerVcard);
} else {
                return mVcardManager.composeAndSendPhonebookOneVcard(op, intIndex, vcard21);
}
} else {
if (intIndex <= 0 || intIndex > size) {








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 659add6..c465e41 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import android.util.Log;

import com.android.bluetooth.R;
import com.android.bluetooth.pbap.BluetoothPbapObexServer.VcardBuilder;
import com.android.vcard.VCardComposer;
import com.android.vcard.VCardConfig;
import com.android.internal.telephony.CallerInfo;
//Synthetic comment -- @@ -163,9 +164,9 @@
return size;
}

    void buildCallHistory(VcardBuilder builder) {
final Uri myUri = CallLog.Calls.CONTENT_URI;
        String selection = BluetoothPbapObexServer.createSelectionPara(builder.getType());
String[] projection = new String[] {
Calls.NUMBER, Calls.CACHED_NAME
};
//Synthetic comment -- @@ -173,13 +174,12 @@
final int CALLS_NAME_COLUMN_INDEX = 1;

Cursor callCursor = null;
try {
callCursor = mResolver.query(myUri, projection, selection, null,
CALLLOG_SORT_ORDER);

            if (callCursor != null && callCursor.moveToPosition(builder.getStartOffset())) {
                while (!callCursor.isAfterLast() && builder.needMore()) {
String name = callCursor.getString(CALLS_NAME_COLUMN_INDEX);
if (TextUtils.isEmpty(name)) {
// name not found, use number instead
//Synthetic comment -- @@ -190,7 +190,9 @@
name = mContext.getString(R.string.unknownNumber);
}
}
                    // Need to add one since the most recent call shall be 1.vcf.
                    builder.append(callCursor.getPosition() + 1, name);
                    callCursor.moveToNext();
}
}
} finally {
//Synthetic comment -- @@ -198,78 +200,88 @@
callCursor.close();
}
}
}

    void buildPhonebookContactsByName(VcardBuilder builder) {
Cursor contactCursor = null;

try {
            int startOffset = builder.getStartOffset();

            // If no search value is provided myself contact will be added first.
            // If a search value is provided myself contact will be matched towards this
            // value. Since my self contact always is located at the first position only add it
            // if start offset is zero. If it matches but start offset is not zero make sure
            // the start offset is compensated. This is needed since myself contact is not
            // stored in the database.
            String localPhoneName = BluetoothPbapService.getLocalPhoneName();
            String searchValue = builder.getSearchValue();
            if (TextUtils.isEmpty(searchValue) || localPhoneName.startsWith(searchValue)) {
                if (builder.getStartOffset() == 0) {
                    builder.append(0, localPhoneName);
                } else {
                    startOffset--;
}
}

            if (V) {
                Log.v(TAG, "getPhonebookContactsByName, order by " + builder.getOrder() +
                        ", search for " + builder.getSearchValue());
            }

            String where = CLAUSE_ONLY_VISIBLE + " AND " + Contacts.DISPLAY_NAME + " like ?";
            contactCursor = mResolver.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION, where,
                    new String[] { builder.getSearchValue() + '%' }, builder.getOrder());

            addContactVcards(builder, contactCursor, startOffset);
} finally {
if (contactCursor != null) {
contactCursor.close();
}
}
}

    void buildPhonebookContactsByNumber(VcardBuilder builder) {
Cursor contactCursor = null;

try {
            // If no search value is provided it will be the same as search by name
            if (TextUtils.isEmpty(builder.getSearchValue())) {
                buildPhonebookContactsByName(builder);
                return;
}

            if (V) {
                Log.v(TAG, "getPhonebookContactsByNumber, order by " + builder.getOrder() +
                        ", search for " + builder.getSearchValue());
            }

            Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                   Uri.encode(builder.getSearchValue()));
            contactCursor = mResolver.query(uri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE, null,
                   builder.getOrder());

            addContactVcards(builder, contactCursor, builder.getStartOffset());
} finally {
if (contactCursor != null) {
contactCursor.close();
}
}
    }

    private void addContactVcards(VcardBuilder builder, Cursor cursor, int startOffset) {
        if (cursor != null && cursor.moveToPosition(startOffset)) {
            while (!cursor.isAfterLast() && builder.needMore()) {
                String name = cursor.getString(CONTACTS_NAME_COLUMN_INDEX);
                long handle = cursor.getLong(CONTACTS_ID_COLUMN_INDEX);

                if (TextUtils.isEmpty(name)) {
                    name = mContext.getString(android.R.string.unknownName);
                }
                builder.append(handle, name);
                cursor.moveToNext();
            }
        }
}

public final int composeAndSendCallLogVcards(final int type, Operation op,
//Synthetic comment -- @@ -378,53 +390,28 @@
return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
}

    public final int composeAndSendPhonebookOneVcard(Operation op, final int handle,
            final boolean vcardType21) {
        Cursor cursor = null;
        boolean found = false;
        String selection = Contacts._ID + "=" + handle + " AND " + CLAUSE_ONLY_VISIBLE;

        // check if handle exists
        try {
            cursor = mResolver.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION, selection, null,
                    Contacts._ID);
            found = cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // if handle exists compose vcard otherwise return error
        if (found) {
            return composeAndSendVCards(op, selection, vcardType21, null, true);
        }
        return ResponseCodes.OBEX_HTTP_NOT_FOUND;
}

public final int composeAndSendVCards(Operation op, final String selection,







