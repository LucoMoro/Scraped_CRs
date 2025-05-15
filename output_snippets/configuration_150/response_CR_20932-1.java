//<Beginning of snippet n. 0>
*   400-499 Honeycomb
* </pre>
*/
    static final int DATABASE_VERSION = 354;

private static final String DATABASE_NAME = "contacts2.db";
private static final String DATABASE_PRESENCE = "presence_db";
public static final String DISPLAY_NAME_SOURCE = RawContacts.DISPLAY_NAME_SOURCE;
public static final String AGGREGATION_NEEDED = "aggregation_needed";
public static final String CONTACT_IN_VISIBLE_GROUP = "contact_in_visible_group";

public static final String CONCRETE_DISPLAY_NAME =
Tables.RAW_CONTACTS + "." + DISPLAY_NAME;
public static final String CONCRETE_CONTACT_ID =
RawContacts.SYNC1 + " TEXT, " +
RawContacts.SYNC2 + " TEXT, " +
RawContacts.SYNC3 + " TEXT, " +
RawContacts.SYNC4 + " TEXT, " +
RawContacts.REV + " TEXT " +
");");

db.execSQL("CREATE INDEX raw_contacts_contact_id_index ON " + Tables.RAW_CONTACTS + " (" +
+ RawContactsColumns.CONCRETE_NAME_VERIFIED + " AS " + RawContacts.NAME_VERIFIED + ","
+ RawContactsColumns.CONCRETE_VERSION + " AS " + RawContacts.VERSION + ","
+ RawContactsColumns.CONCRETE_DIRTY + " AS " + RawContacts.DIRTY + ","
+ RawContactsColumns.CONCRETE_SYNC1 + " AS " + RawContacts.SYNC1 + ","
+ RawContactsColumns.CONCRETE_SYNC2 + " AS " + RawContacts.SYNC2 + ","
+ RawContactsColumns.CONCRETE_SYNC3 + " AS " + RawContacts.SYNC3 + ","
+ RawContactsColumns.CONCRETE_SOURCE_ID + " AS " + RawContacts.SOURCE_ID + ","
+ RawContactsColumns.CONCRETE_VERSION + " AS " + RawContacts.VERSION + ","
+ RawContactsColumns.CONCRETE_DIRTY + " AS " + RawContacts.DIRTY + ","
+ RawContactsColumns.CONCRETE_DELETED + " AS " + RawContacts.DELETED + ","
+ RawContactsColumns.CONCRETE_NAME_VERIFIED + " AS " + RawContacts.NAME_VERIFIED + ","
+ PackagesColumns.PACKAGE + " AS " + Data.RES_PACKAGE + ","
oldVersion = 354;
}

if (upgradeViewsAndTriggers) {
createContactsViews(db);
createGroupsView(db);
");");
}

private interface StructName205Query {
String TABLE = Tables.DATA_JOIN_RAW_CONTACTS;
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
private SQLiteStatement mSetSuperPrimaryStatement;
/** Precompiled sql statement for updating a contact display name */
private SQLiteStatement mRawContactDisplayNameUpdate;
/** Precompiled sql statement for updating an aggregated status update */
private SQLiteStatement mLastStatusUpdate;
private SQLiteStatement mNameLookupInsert;
private SQLiteStatement mNameLookupDelete;
private SQLiteStatement mStatusUpdateAutoTimestamp;
sRawContactsProjectionMap.put(RawContacts.SOURCE_ID, RawContacts.SOURCE_ID);
sRawContactsProjectionMap.put(RawContacts.VERSION, RawContacts.VERSION);
sRawContactsProjectionMap.put(RawContacts.DIRTY, RawContacts.DIRTY);
sRawContactsProjectionMap.put(RawContacts.DELETED, RawContacts.DELETED);
sRawContactsProjectionMap.put(RawContacts.DISPLAY_NAME_PRIMARY,
RawContacts.DISPLAY_NAME_PRIMARY);
columns.put(RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_NAME);
columns.put(RawContacts.ACCOUNT_TYPE, RawContacts.ACCOUNT_TYPE);
columns.put(RawContacts.SOURCE_ID, RawContacts.SOURCE_ID);
columns.put(RawContacts.VERSION, RawContacts.VERSION);
columns.put(RawContacts.DIRTY, RawContacts.DIRTY);
columns.put(RawContacts.DELETED, RawContacts.DELETED);
columns.put(RawContacts.IS_RESTRICTED, RawContacts.IS_RESTRICTED);
columns.put(RawContacts.SYNC1, RawContacts.SYNC1);
columns.put(RawContacts.REV, RawContacts.REV);

if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
}

return true;

initForDefaultLocale();

mSetPrimaryStatement = mDb.compileStatement(
"UPDATE " + Tables.DATA +
" SET " + Data.IS_PRIMARY + "=(_id=?)" +
RawContacts.SORT_KEY_ALTERNATIVE + "=?" +
" WHERE " + RawContacts._ID + "=?");

mLastStatusUpdate = mDb.compileStatement(
"UPDATE " + Tables.CONTACTS +
" SET " + ContactsColumns.LAST_STATUS_UPDATE_ID + "=" +
id = rowHandler.insert(mDb, rawContactId, mValues);
if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
}
mUpdatedRawContacts.add(rawContactId);
return id;
count += rowHandler.delete(mDb, c);
if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
}
}
} finally {
mDirtyRawContacts.add(rawContactId);
}

/*
* Sets the given dataId record in the "data" table to primary, and resets all data records of
* the same mimetype and under the same contact to not be primary.
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import static com.android.providers.contacts.ContactsActor.PACKAGE_GREY;

import android.accounts.Account;
import android.content.ContentProvider;
import android.content.ContentResolver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* A common superclass for {@link ContactsProvider2}-related tests.
*/
}

/**
* Constructs a selection (where clause) out of all supplied values, uses it
* to query the provider and verifies that a single row is returned and it
* has the same values as requested.
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
import com.android.internal.util.ArrayUtils;
import com.android.providers.contacts.ContactsDatabaseHelper.PresenceColumns;
import com.google.android.collect.Lists;

import android.accounts.Account;

private static final Account ACCOUNT_1 = new Account("account_name_1", "account_type_1");
private static final Account ACCOUNT_2 = new Account("account_name_2", "account_type_2");

public void testRawContactsInsert() {
ContentValues values = new ContentValues();
values.put(RawContacts.SYNC2, "f");
values.put(RawContacts.SYNC3, "g");
values.put(RawContacts.SYNC4, "h");
values.put(RawContacts.REV, "2023-10-01T12:34:56Z");

Uri rowUri = mResolver.insert(RawContacts.CONTENT_URI, values);
long rawContactId = ContentUris.parseId(rowUri);
Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
Uri rawContactDataUri =
Uri.withAppendedPath(rawContactUri, RawContacts.Data.CONTENT_DIRECTORY);
assertSelection(rawContactDataUri, values, Data._ID, dataId);

// Access the same data through the directory under Contacts
assertStoredValue(contactUri, Contacts.STARRED, "1");
}

public void testLiveFolders() {
long rawContactId1 = createRawContactWithName("James", "Sullivan");
insertPhoneNumber(rawContactId1, "5234567890");
//<End of snippet n. 3>