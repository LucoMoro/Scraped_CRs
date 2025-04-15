/*Support for vCard 2.1 REV Field in AAB

Adding support for the property name REV
which is part of vCard 2.1.

Change-Id:I6a66491d8579ecdd8952c6416e27b529ce82c4b0*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsDatabaseHelper.java b/src/com/android/providers/contacts/ContactsDatabaseHelper.java
//Synthetic comment -- index 8f1253a..e47abcf 100644

//Synthetic comment -- @@ -85,7 +85,7 @@
*   400-499 Honeycomb
* </pre>
*/
    static final int DATABASE_VERSION = 353;

private static final String DATABASE_NAME = "contacts2.db";
private static final String DATABASE_PRESENCE = "presence_db";
//Synthetic comment -- @@ -283,7 +283,10 @@
public static final String DISPLAY_NAME_SOURCE = RawContacts.DISPLAY_NAME_SOURCE;
public static final String AGGREGATION_NEEDED = "aggregation_needed";
public static final String CONTACT_IN_VISIBLE_GROUP = "contact_in_visible_group";

public static final String CONCRETE_DISPLAY_NAME =
Tables.RAW_CONTACTS + "." + DISPLAY_NAME;
public static final String CONCRETE_CONTACT_ID =
//Synthetic comment -- @@ -735,7 +738,8 @@
RawContacts.SYNC1 + " TEXT, " +
RawContacts.SYNC2 + " TEXT, " +
RawContacts.SYNC3 + " TEXT, " +
                RawContacts.SYNC4 + " TEXT " +
");");

db.execSQL("CREATE INDEX raw_contacts_contact_id_index ON " + Tables.RAW_CONTACTS + " (" +
//Synthetic comment -- @@ -1145,6 +1149,8 @@
+ RawContactsColumns.CONCRETE_NAME_VERIFIED + " AS " + RawContacts.NAME_VERIFIED + ","
+ RawContactsColumns.CONCRETE_VERSION + " AS " + RawContacts.VERSION + ","
+ RawContactsColumns.CONCRETE_DIRTY + " AS " + RawContacts.DIRTY + ","
+ RawContactsColumns.CONCRETE_SYNC1 + " AS " + RawContacts.SYNC1 + ","
+ RawContactsColumns.CONCRETE_SYNC2 + " AS " + RawContacts.SYNC2 + ","
+ RawContactsColumns.CONCRETE_SYNC3 + " AS " + RawContacts.SYNC3 + ","
//Synthetic comment -- @@ -1308,6 +1314,8 @@
+ RawContactsColumns.CONCRETE_SOURCE_ID + " AS " + RawContacts.SOURCE_ID + ","
+ RawContactsColumns.CONCRETE_VERSION + " AS " + RawContacts.VERSION + ","
+ RawContactsColumns.CONCRETE_DIRTY + " AS " + RawContacts.DIRTY + ","
+ RawContactsColumns.CONCRETE_DELETED + " AS " + RawContacts.DELETED + ","
+ RawContactsColumns.CONCRETE_NAME_VERIFIED + " AS " + RawContacts.NAME_VERIFIED + ","
+ PackagesColumns.PACKAGE + " AS " + Data.RES_PACKAGE + ","
//Synthetic comment -- @@ -1526,6 +1534,12 @@
oldVersion = 353;
}

if (upgradeViewsAndTriggers) {
createContactsViews(db);
createGroupsView(db);
//Synthetic comment -- @@ -1705,6 +1719,13 @@
");");
}

private interface StructName205Query {
String TABLE = Tables.DATA_JOIN_RAW_CONTACTS;









//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index 3bee54d..d05f8cc 100644

//Synthetic comment -- @@ -121,14 +121,17 @@
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
//Synthetic comment -- @@ -404,8 +407,12 @@
private SQLiteStatement mSetSuperPrimaryStatement;
/** Precompiled sql statement for updating a contact display name */
private SQLiteStatement mRawContactDisplayNameUpdate;
/** Precompiled sql statement for updating an aggregated status update */
private SQLiteStatement mLastStatusUpdate;
private SQLiteStatement mNameLookupInsert;
private SQLiteStatement mNameLookupDelete;
private SQLiteStatement mStatusUpdateAutoTimestamp;
//Synthetic comment -- @@ -585,6 +592,9 @@
sRawContactsProjectionMap.put(RawContacts.SOURCE_ID, RawContacts.SOURCE_ID);
sRawContactsProjectionMap.put(RawContacts.VERSION, RawContacts.VERSION);
sRawContactsProjectionMap.put(RawContacts.DIRTY, RawContacts.DIRTY);
sRawContactsProjectionMap.put(RawContacts.DELETED, RawContacts.DELETED);
sRawContactsProjectionMap.put(RawContacts.DISPLAY_NAME_PRIMARY,
RawContacts.DISPLAY_NAME_PRIMARY);
//Synthetic comment -- @@ -674,8 +684,10 @@
columns.put(RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_NAME);
columns.put(RawContacts.ACCOUNT_TYPE, RawContacts.ACCOUNT_TYPE);
columns.put(RawContacts.SOURCE_ID, RawContacts.SOURCE_ID);
columns.put(RawContacts.VERSION, RawContacts.VERSION);
columns.put(RawContacts.DIRTY, RawContacts.DIRTY);
columns.put(RawContacts.DELETED, RawContacts.DELETED);
columns.put(RawContacts.IS_RESTRICTED, RawContacts.IS_RESTRICTED);
columns.put(RawContacts.SYNC1, RawContacts.SYNC1);
//Synthetic comment -- @@ -1046,6 +1058,7 @@

if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
}

return true;
//Synthetic comment -- @@ -1882,6 +1895,9 @@

initForDefaultLocale();

mSetPrimaryStatement = mDb.compileStatement(
"UPDATE " + Tables.DATA +
" SET " + Data.IS_PRIMARY + "=(_id=?)" +
//Synthetic comment -- @@ -1912,6 +1928,8 @@
RawContacts.SORT_KEY_ALTERNATIVE + "=?" +
" WHERE " + RawContacts._ID + "=?");

mLastStatusUpdate = mDb.compileStatement(
"UPDATE " + Tables.CONTACTS +
" SET " + ContactsColumns.LAST_STATUS_UPDATE_ID + "=" +
//Synthetic comment -- @@ -2612,6 +2630,7 @@
id = rowHandler.insert(mDb, rawContactId, mValues);
if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
}
mUpdatedRawContacts.add(rawContactId);
return id;
//Synthetic comment -- @@ -2952,6 +2971,7 @@
count += rowHandler.delete(mDb, c);
if (!callerIsSyncAdapter) {
setRawContactDirty(rawContactId);
}
}
} finally {
//Synthetic comment -- @@ -5672,6 +5692,18 @@
mDirtyRawContacts.add(rawContactId);
}

/*
* Sets the given dataId record in the "data" table to primary, and resets all data records of
* the same mimetype and under the same contact to not be primary.








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/BaseContactsProvider2Test.java b/tests/src/com/android/providers/contacts/BaseContactsProvider2Test.java
//Synthetic comment -- index ebca24d..a702d33 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.providers.contacts.ContactsActor.PACKAGE_GREY;

import android.accounts.Account;
import android.content.ContentProvider;
import android.content.ContentResolver;
//Synthetic comment -- @@ -52,14 +54,18 @@
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
//Synthetic comment -- @@ -713,6 +719,37 @@
}

/**
* Constructs a selection (where clause) out of all supplied values, uses it
* to query the provider and verifies that a single row is returned and it
* has the same values as requested.








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/ContactsProvider2Test.java b/tests/src/com/android/providers/contacts/ContactsProvider2Test.java
//Synthetic comment -- index ce2828d..5a0342f 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.internal.util.ArrayUtils;
import com.android.providers.contacts.ContactsDatabaseHelper.PresenceColumns;
import com.google.android.collect.Lists;

import android.accounts.Account;
//Synthetic comment -- @@ -81,6 +82,7 @@

private static final Account ACCOUNT_1 = new Account("account_name_1", "account_type_1");
private static final Account ACCOUNT_2 = new Account("account_name_2", "account_type_2");

public void testRawContactsInsert() {
ContentValues values = new ContentValues();
//Synthetic comment -- @@ -100,6 +102,8 @@
values.put(RawContacts.SYNC2, "f");
values.put(RawContacts.SYNC3, "g");
values.put(RawContacts.SYNC4, "h");

Uri rowUri = mResolver.insert(RawContacts.CONTENT_URI, values);
long rawContactId = ContentUris.parseId(rowUri);
//Synthetic comment -- @@ -127,6 +131,7 @@
Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
Uri rawContactDataUri =
Uri.withAppendedPath(rawContactUri, RawContacts.Data.CONTENT_DIRECTORY);
assertSelection(rawContactDataUri, values, Data._ID, dataId);

// Access the same data through the directory under Contacts
//Synthetic comment -- @@ -2166,6 +2171,20 @@
assertStoredValue(contactUri, Contacts.STARRED, "1");
}

public void testLiveFolders() {
long rawContactId1 = createRawContactWithName("James", "Sullivan");
insertPhoneNumber(rawContactId1, "5234567890");







