
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
        // When LAST_REVISION added to ContactsContract.RawContacts, refer to that constant instead
        public static final String LAST_REVISION = "rev";
        public static final String CONCRETE_LAST_REVISION =
                Tables.RAW_CONTACTS + "." + LAST_REVISION;
public static final String CONCRETE_DISPLAY_NAME =
Tables.RAW_CONTACTS + "." + DISPLAY_NAME;
public static final String CONCRETE_CONTACT_ID =
RawContacts.SYNC1 + " TEXT, " +
RawContacts.SYNC2 + " TEXT, " +
RawContacts.SYNC3 + " TEXT, " +
                RawContacts.SYNC4 + " TEXT, " +
                RawContactsColumns.LAST_REVISION + " TEXT " +
");");

db.execSQL("CREATE INDEX raw_contacts_contact_id_index ON " + Tables.RAW_CONTACTS + " (" +
+ RawContactsColumns.CONCRETE_NAME_VERIFIED + " AS " + RawContacts.NAME_VERIFIED + ","
+ RawContactsColumns.CONCRETE_VERSION + " AS " + RawContacts.VERSION + ","
+ RawContactsColumns.CONCRETE_DIRTY + " AS " + RawContacts.DIRTY + ","
                + RawContactsColumns.CONCRETE_LAST_REVISION + " AS "
                + RawContactsColumns.LAST_REVISION + ","
+ RawContactsColumns.CONCRETE_SYNC1 + " AS " + RawContacts.SYNC1 + ","
+ RawContactsColumns.CONCRETE_SYNC2 + " AS " + RawContacts.SYNC2 + ","
+ RawContactsColumns.CONCRETE_SYNC3 + " AS " + RawContacts.SYNC3 + ","
+ RawContactsColumns.CONCRETE_SOURCE_ID + " AS " + RawContacts.SOURCE_ID + ","
+ RawContactsColumns.CONCRETE_VERSION + " AS " + RawContacts.VERSION + ","
+ RawContactsColumns.CONCRETE_DIRTY + " AS " + RawContacts.DIRTY + ","
                + RawContactsColumns.CONCRETE_LAST_REVISION + " AS "
                + RawContactsColumns.LAST_REVISION + ","
+ RawContactsColumns.CONCRETE_DELETED + " AS " + RawContacts.DELETED + ","
+ RawContactsColumns.CONCRETE_NAME_VERIFIED + " AS " + RawContacts.NAME_VERIFIED + ","
+ PackagesColumns.PACKAGE + " AS " + Data.RES_PACKAGE + ","
oldVersion = 353;
}

        if (oldVersion == 353) {
            upgradeToVersion354(db);
            upgradeViewsAndTriggers = true;
            oldVersion = 354;
        }

if (upgradeViewsAndTriggers) {
createContactsViews(db);
createGroupsView(db);
");");
}

    private void upgradeToVersion354(SQLiteDatabase db) {
        // Adding REV column
        db.execSQL(
                "ALTER TABLE " + Tables.RAW_CONTACTS +
                " ADD COLUMN " + RawContactsColumns.LAST_REVISION + " TEXT;");
    }

private interface StructName205Query {
String TABLE = Tables.DATA_JOIN_RAW_CONTACTS;


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

/**
private SQLiteStatement mSetSuperPrimaryStatement;
/** Precompiled sql statement for updating a contact display name */
private SQLiteStatement mRawContactDisplayNameUpdate;
    /** Precompiled sql statement for setting the last revision column of a raw contact. */
    private SQLiteStatement mRawContactRevUpdate;
/** Precompiled sql statement for updating an aggregated status update */
private SQLiteStatement mLastStatusUpdate;
    /** Date format of last revision column of a raw contact. */
    private SimpleDateFormat mRevFormatter;
private SQLiteStatement mNameLookupInsert;
private SQLiteStatement mNameLookupDelete;
private SQLiteStatement mStatusUpdateAutoTimestamp;
sRawContactsProjectionMap.put(RawContacts.SOURCE_ID, RawContacts.SOURCE_ID);
sRawContactsProjectionMap.put(RawContacts.VERSION, RawContacts.VERSION);
sRawContactsProjectionMap.put(RawContacts.DIRTY, RawContacts.DIRTY);
        // When LAST_REVISION added to ContactsContract.RawContacts, refer to that constant instead
        sRawContactsProjectionMap.put(RawContactsColumns.LAST_REVISION,
                RawContactsColumns.LAST_REVISION);
sRawContactsProjectionMap.put(RawContacts.DELETED, RawContacts.DELETED);
sRawContactsProjectionMap.put(RawContacts.DISPLAY_NAME_PRIMARY,
RawContacts.DISPLAY_NAME_PRIMARY);
columns.put(RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_NAME);
columns.put(RawContacts.ACCOUNT_TYPE, RawContacts.ACCOUNT_TYPE);
columns.put(RawContacts.SOURCE_ID, RawContacts.SOURCE_ID);
        // When LAST_REVISION added to ContactsContract.RawContacts, refer to that constant instead
columns.put(RawContacts.VERSION, RawContacts.VERSION);
columns.put(RawContacts.DIRTY, RawContacts.DIRTY);
        columns.put(RawContactsColumns.LAST_REVISION, RawContactsColumns.LAST_REVISION);
columns.put(RawContacts.DELETED, RawContacts.DELETED);
columns.put(RawContacts.IS_RESTRICTED, RawContacts.IS_RESTRICTED);
columns.put(RawContacts.SYNC1, RawContacts.SYNC1);

if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
                setRawContactLastRevision(rawContactId);
}

return true;

initForDefaultLocale();

        mRevFormatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        mRevFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

mSetPrimaryStatement = mDb.compileStatement(
"UPDATE " + Tables.DATA +
" SET " + Data.IS_PRIMARY + "=(_id=?)" +
RawContacts.SORT_KEY_ALTERNATIVE + "=?" +
" WHERE " + RawContacts._ID + "=?");

        mRawContactRevUpdate = mDb.compileStatement("UPDATE " + Tables.RAW_CONTACTS + " SET "
                + RawContactsColumns.LAST_REVISION + "=?" + " WHERE " + RawContacts._ID + "=?");
mLastStatusUpdate = mDb.compileStatement(
"UPDATE " + Tables.CONTACTS +
" SET " + ContactsColumns.LAST_STATUS_UPDATE_ID + "=" +
id = rowHandler.insert(mDb, rawContactId, mValues);
if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
            setRawContactLastRevision(rawContactId);
}
mUpdatedRawContacts.add(rawContactId);
return id;
count += rowHandler.delete(mDb, c);
if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
                    setRawContactLastRevision(rawContactId);
}
}
} finally {
mDirtyRawContacts.add(rawContactId);
}

    /**
     * Sets the {@link RawContactsColumns#LAST_REVISION} for the specified raw contact.
     */
    private void setRawContactLastRevision(long rawContactId) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        String rev = mRevFormatter.format(cal.getTime());

        mRawContactRevUpdate.bindString(1, rev);
        mRawContactRevUpdate.bindLong(2, rawContactId);
        mRawContactRevUpdate.execute();
     }

/*
* Sets the given dataId record in the "data" table to primary, and resets all data records of
* the same mimetype and under the same contact to not be primary.

//<End of snippet n. 1>










//<Beginning of snippet n. 2>



import static com.android.providers.contacts.ContactsActor.PACKAGE_GREY;

import com.android.providers.contacts.ContactsDatabaseHelper.RawContactsColumns;

import android.accounts.Account;
import android.content.ContentProvider;
import android.content.ContentResolver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


/**
* A common superclass for {@link ContactsProvider2}-related tests.
*/
}

/**
     *  assert contain REV column
     *
     *  @param Uri need to be assert contain REV column
     */
    protected void assertREV(Uri uri) {
        String rev = getRev(uri);
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;

        try {
            date = curFormater.parse(rev);
            assertNotNull(date);
        } catch(ParseException e) {
            fail("Could not parse revision");
        }
    }

    private String getRev(Uri uri) {
        String[] projection = new String[] {RawContactsColumns.LAST_REVISION};
        Cursor c = mResolver.query(uri, projection, null, null, null);
        String result = null;
        try {
            assertTrue(c.moveToFirst());
            result = c.getString(c.getColumnIndex(RawContactsColumns.LAST_REVISION));
        } finally {
            c.close();
        }
        return result;
    }

    /**
* Constructs a selection (where clause) out of all supplied values, uses it
* to query the provider and verifies that a single row is returned and it
* has the same values as requested.

//<End of snippet n. 2>










//<Beginning of snippet n. 3>



import com.android.internal.util.ArrayUtils;
import com.android.providers.contacts.ContactsDatabaseHelper.PresenceColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.RawContactsColumns;
import com.google.android.collect.Lists;

import android.accounts.Account;

private static final Account ACCOUNT_1 = new Account("account_name_1", "account_type_1");
private static final Account ACCOUNT_2 = new Account("account_name_2", "account_type_2");
    private static final String TEST_REV_DATA = "20101223101213";

public void testRawContactsInsert() {
ContentValues values = new ContentValues();
values.put(RawContacts.SYNC2, "f");
values.put(RawContacts.SYNC3, "g");
values.put(RawContacts.SYNC4, "h");
        //When LAST_REVISION added to ContactsContract.RawContacts, refer to that constant instead
        values.put(RawContactsColumns.LAST_REVISION, TEST_REV_DATA);

Uri rowUri = mResolver.insert(RawContacts.CONTENT_URI, values);
long rawContactId = ContentUris.parseId(rowUri);
Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
Uri rawContactDataUri =
Uri.withAppendedPath(rawContactUri, RawContacts.Data.CONTENT_DIRECTORY);
        assertREV(rawContactUri);
assertSelection(rawContactDataUri, values, Data._ID, dataId);

// Access the same data through the directory under Contacts
assertStoredValue(contactUri, Contacts.STARRED, "1");
}

    public void testUpdateRawContactSetRev() {
        long rawContactId1 = createRawContactWithName();
        Uri rawContactUri1 = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId1);

        ContentValues values = new ContentValues();
        // When LAST_REVISION added to ContactsContract.RawContacts, refer to that constant instead
        values.put(RawContactsColumns.LAST_REVISION, TEST_REV_DATA);

        mResolver.update(rawContactUri1, values, null, null);

        // When LAST_REVISION added to ContactsContract.RawContacts, refer to that constant instead
        assertStoredValue(rawContactUri1, RawContactsColumns.LAST_REVISION, TEST_REV_DATA);
    }

public void testLiveFolders() {
long rawContactId1 = createRawContactWithName("James", "Sullivan");
insertPhoneNumber(rawContactId1, "5234567890");

//<End of snippet n. 3>








