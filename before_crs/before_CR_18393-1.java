/*Code added to support speed dial

Change-Id:I0888c32334abe4836f6c0dd2d97a483d5141c8ff*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsDatabaseHelper.java b/src/com/android/providers/contacts/ContactsDatabaseHelper.java
//Synthetic comment -- index edd73ff..d8d9361 100644

//Synthetic comment -- @@ -56,6 +56,7 @@
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.SocialContract.Activities;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
//Synthetic comment -- @@ -99,6 +100,12 @@
public static final String STATUS_UPDATES = "status_updates";
public static final String PROPERTIES = "properties";
public static final String ACCOUNTS = "accounts";

public static final String DATA_JOIN_MIMETYPES = "data "
+ "JOIN mimetypes ON (data.mimetype_id = mimetypes._id)";
//Synthetic comment -- @@ -174,6 +181,12 @@
public static final String CONTACTS_RESTRICTED = "view_contacts_restricted";

public static final String GROUPS_ALL = "view_groups";
}

public interface Clauses {
//Synthetic comment -- @@ -451,6 +464,16 @@
String PROPERTY_VALUE = "property_value";
}

/** In-memory cache of previously found MIME-type mappings */
private final HashMap<String, Long> mMimetypeCache = new HashMap<String, Long>();
/** In-memory cache of previously found package name mappings */
//Synthetic comment -- @@ -641,6 +664,14 @@

mSyncState.createDatabase(db);

// One row per group of contacts corresponding to the same person
db.execSQL("CREATE TABLE " + Tables.CONTACTS + " (" +
BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//Synthetic comment -- @@ -1030,6 +1061,10 @@
+ " END");

db.execSQL("DROP TRIGGER IF EXISTS " + Tables.DATA + "_deleted;");
db.execSQL("CREATE TRIGGER " + Tables.DATA + "_deleted BEFORE DELETE ON " + Tables.DATA
+ " BEGIN "
+ "   UPDATE " + Tables.RAW_CONTACTS
//Synthetic comment -- @@ -1041,6 +1076,8 @@
+ "     WHERE " + StatusUpdatesColumns.DATA_ID + "=OLD." + Data._ID + ";"
+ "   DELETE FROM " + Tables.NAME_LOOKUP
+ "     WHERE " + NameLookupColumns.DATA_ID + "=OLD." + Data._ID + ";"
+ " END");


//Synthetic comment -- @@ -1084,6 +1121,35 @@
db.execSQL("DROP VIEW IF EXISTS " + Views.DATA_RESTRICTED + ";");
db.execSQL("DROP VIEW IF EXISTS " + Views.RAW_CONTACTS_ALL + ";");
db.execSQL("DROP VIEW IF EXISTS " + Views.RAW_CONTACTS_RESTRICTED + ";");

String dataColumns =
Data.IS_PRIMARY + ", "
//Synthetic comment -- @@ -1182,6 +1248,39 @@
+   "' AND " + GroupsColumns.CONCRETE_ID + "="
+ Tables.DATA + "." + GroupMembership.GROUP_ROW_ID + ")";

db.execSQL("CREATE VIEW " + Views.DATA_ALL + " AS " + dataSelect);
db.execSQL("CREATE VIEW " + Views.DATA_RESTRICTED + " AS " + dataSelect + " WHERE "
+ RawContactsColumns.CONCRETE_IS_RESTRICTED + "=0");
//Synthetic comment -- @@ -2965,6 +3064,15 @@
return false;
}

public String getDataView() {
return getDataView(false);
}








//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index f4a4c5a..0d00320 100644

//Synthetic comment -- @@ -110,6 +110,7 @@
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
//Synthetic comment -- @@ -241,6 +242,10 @@
private static final int RAW_CONTACT_ENTITIES = 15001;

private static final int PROVIDER_STATUS = 16001;

private interface DataContactsQuery {
public static final String TABLE = "data "
//Synthetic comment -- @@ -362,6 +367,12 @@
private static final HashMap<String, String> sRawContactsProjectionMap;
/** Contains the columns from the raw contacts entity view*/
private static final HashMap<String, String> sRawContactsEntityProjectionMap;
/** Contains columns from the data view */
private static final HashMap<String, String> sDataProjectionMap;
/** Contains columns from the data view */
//Synthetic comment -- @@ -503,9 +514,24 @@
LIVE_FOLDERS_CONTACTS_FAVORITES);

matcher.addURI(ContactsContract.AUTHORITY, "provider_status", PROVIDER_STATUS);
}

static {
sCountProjectionMap = new HashMap<String, String>();
sCountProjectionMap.put(BaseColumns._COUNT, "COUNT(*)");

//Synthetic comment -- @@ -2440,7 +2466,12 @@
id = insertStatusUpdate(values);
break;
}

default:
mSyncToNetwork = true;
return mLegacyApiSupport.insert(uri, values);
//Synthetic comment -- @@ -3019,6 +3050,16 @@
return id;
}

/**
* Inserts a status update.
*/
//Synthetic comment -- @@ -3215,6 +3256,10 @@
readBooleanQueryParameter(uri, ContactsContract.CALLER_IS_SYNCADAPTER, false);
final int match = sUriMatcher.match(uri);
switch (match) {
case SYNCSTATE:
return mDbHelper.getSyncState().delete(mDb, selection, selectionArgs);

//Synthetic comment -- @@ -3458,6 +3503,11 @@
final boolean callerIsSyncAdapter =
readBooleanQueryParameter(uri, ContactsContract.CALLER_IS_SYNCADAPTER, false);
switch(match) {
case SYNCSTATE:
return mDbHelper.getSyncState().update(mDb, values,
appendAccountToSelection(uri, selection), selectionArgs);
//Synthetic comment -- @@ -3843,6 +3893,18 @@
}
}

private int updateContactOptions(ContentValues values, String selection,
String[] selectionArgs) {
int count = 0;
//Synthetic comment -- @@ -4190,6 +4252,12 @@
// write a new query() block to make sure it protects restricted data.
final int match = sUriMatcher.match(uri);
switch (match) {
case SYNCSTATE:
return mDbHelper.getSyncState().query(db, projection, selection,  selectionArgs,
sortOrder);
//Synthetic comment -- @@ -5236,6 +5304,19 @@
qb.setProjectionMap(sRawContactsEntityProjectionMap);
appendAccountFromParameter(qb, uri);
}

private void setTablesAndProjectionMapForData(SQLiteQueryBuilder qb, Uri uri,
String[] projection, boolean distinct) {







