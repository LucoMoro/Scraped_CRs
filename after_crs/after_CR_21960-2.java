/*Pull vCard listing and pull vCard entry improvements

The functionality has been improved to solve the following bugs:
1 List start offset is not correct when a search value is used. The
offset is not used on the searched values but on the whole phonebook.
This is not correct according to 5.3.4.3 and 5.3.4.5 in the PBAP
specification.
2 Handle in phonebook listing is only a counter and can’t be used
to actually find the real vCard in pull vCard entry. This is clearly
visible when using a listStartOffset in the phonebook vCard listing.
This is not correct according to 3.1.5.1 in the PBAP specification.
3 Alphabetical order does not work in search by number.
The common solution for all these problems is to use the database
more extensively for lookup. Furthermore to ensure unique handles
in the phonebook listing the handle has been changed from a counter
to database ID.

Change-Id:I6cdabd98614ee602f24446e23d947f84c5b44204*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 42c078d..65ef1fc 100755

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.text.TextUtils;
import android.util.Log;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.Contacts;
import android.provider.CallLog;

import java.io.IOException;
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
//Synthetic comment -- @@ -845,9 +816,7 @@
mOrderBy = ORDER_BY_ALPHABETICAL;
}

        int sendResult = sendVcardListingXml(op, appParamValue);
return sendResult;
}

//Synthetic comment -- @@ -857,11 +826,11 @@
if (D) Log.d(TAG, "Name is Null, or the length of name < 5 !");
return ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE;
}
        String strHandle = name.substring(0, name.length() - VCARD_NAME_SUFFIX_LENGTH + 1);
        int handle = 0;
        if (strHandle.trim().length() != 0) {
try {
                handle = Integer.parseInt(strHandle);
} catch (NumberFormatException e) {
Log.e(TAG, "catch number format exception " + e.toString());
return ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE;
//Synthetic comment -- @@ -879,27 +848,26 @@
Log.w(TAG, "wrong path!");
return ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE;
} else if (appParamValue.needTag == ContentType.PHONEBOOK) {
            if (handle < 0) {
Log.w(TAG, "The requested vcard is not acceptable! name= " + name);
return ResponseCodes.OBEX_HTTP_NOT_FOUND;
            } else if (handle == 0) {
// For PB_PATH, 0.vcf is the phone number of this phone.
String ownerVcard = mVcardManager.getOwnerPhoneNumberVcard(vcard21);
return pushBytes(op, ownerVcard);
} else {
                return mVcardManager.composeAndSendPhonebookOneVcard(op, handle, vcard21);
}
} else {
            if (handle <= 0 || handle > size) {
Log.w(TAG, "The requested vcard is not acceptable! name= " + name);
return ResponseCodes.OBEX_HTTP_NOT_FOUND;
}
// For others (ich/och/cch/mch), 0.vcf is meaningless, and must
// begin from 1.vcf
            if (handle >= 1) {
return mVcardManager.composeAndSendCallLogVcards(appParamValue.needTag, op,
                        handle, handle, vcard21);
}
}
return ResponseCodes.OBEX_HTTP_OK;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index aaca906..b0a7651 100644

//Synthetic comment -- @@ -51,6 +51,7 @@

import com.android.bluetooth.R;
import com.android.internal.telephony.CallerInfo;
import com.android.bluetooth.pbap.BluetoothPbapObexServer.VcardBuilder;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -164,9 +165,9 @@
return size;
}

    void buildCallHistory(VcardBuilder builder) {
final Uri myUri = CallLog.Calls.CONTENT_URI;
        String selection = BluetoothPbapObexServer.createSelectionPara(builder.getType());
String[] projection = new String[] {
Calls.NUMBER, Calls.CACHED_NAME
};
//Synthetic comment -- @@ -174,13 +175,12 @@
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
//Synthetic comment -- @@ -191,7 +191,9 @@
name = mContext.getString(R.string.unknownNumber);
}
}
                    // Need to add one since the most recent call shall be 1.vcf.
                    builder.append(callCursor.getPosition() + 1, name);
                    callCursor.moveToNext();
}
}
} finally {
//Synthetic comment -- @@ -199,78 +201,88 @@
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
//Synthetic comment -- @@ -379,53 +391,28 @@
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







