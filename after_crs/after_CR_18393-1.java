/*Code added to support speed dial

Change-Id:I0888c32334abe4836f6c0dd2d97a483d5141c8ff*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsDatabaseHelper.java b/src/com/android/providers/contacts/ContactsDatabaseHelper.java
//Synthetic comment -- index edd73ff..d8d9361 100644

//Synthetic comment -- @@ -56,6 +56,7 @@
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.SpeedDial;
import android.provider.SocialContract.Activities;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
//Synthetic comment -- @@ -99,6 +100,12 @@
public static final String STATUS_UPDATES = "status_updates";
public static final String PROPERTIES = "properties";
public static final String ACCOUNTS = "accounts";
        //Code added for speed dial support starts
        /**
         * Speed Dial Table Name
         */
        public static final String SPEED_DIAL = "speed_dial";
        //Code added for speed dial support ends

public static final String DATA_JOIN_MIMETYPES = "data "
+ "JOIN mimetypes ON (data.mimetype_id = mimetypes._id)";
//Synthetic comment -- @@ -174,6 +181,12 @@
public static final String CONTACTS_RESTRICTED = "view_contacts_restricted";

public static final String GROUPS_ALL = "view_groups";
        //Code added for speed dial support starts
        /*
         * Speed dial view
         */
        public static final String SPEED_DIAL = "view_speed_dial";
        //Code added for speed dial support ends
}

public interface Clauses {
//Synthetic comment -- @@ -451,6 +464,16 @@
String PROPERTY_VALUE = "property_value";
}

    //Code added for speed dial support starts
    /**
     * Interface for Speed Dial table columns
     */
    public interface SpeedDialColumns {
        public static final String KEY_ID = "key_id";
        public static final String PHONE_ID = "phone_id";
    }
    //Code added for speed dial support ends

/** In-memory cache of previously found MIME-type mappings */
private final HashMap<String, Long> mMimetypeCache = new HashMap<String, Long>();
/** In-memory cache of previously found package name mappings */
//Synthetic comment -- @@ -641,6 +664,14 @@

mSyncState.createDatabase(db);

        //Code added for speed dial support starts
        //Creating Speed Dial Table Structure
        db.execSQL("CREATE TABLE " +Tables.SPEED_DIAL + "( "+
                SpeedDial.KEY_ID + " INTEGER PRIMARY KEY,"+
                SpeedDial.PHONE_ID + " INTEGER"+
                ");");
        //Code added for speed dial support ends

// One row per group of contacts corresponding to the same person
db.execSQL("CREATE TABLE " + Tables.CONTACTS + " (" +
BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//Synthetic comment -- @@ -1030,6 +1061,10 @@
+ " END");

db.execSQL("DROP TRIGGER IF EXISTS " + Tables.DATA + "_deleted;");

        //Code added for speed dial support starts
        //Added the statement to the "data_delete" trigger
        //Added the statement "DELETE FROM Tables.SPEED_DIAL WHERE phone_id=OLD.Data._ID"
db.execSQL("CREATE TRIGGER " + Tables.DATA + "_deleted BEFORE DELETE ON " + Tables.DATA
+ " BEGIN "
+ "   UPDATE " + Tables.RAW_CONTACTS
//Synthetic comment -- @@ -1041,6 +1076,8 @@
+ "     WHERE " + StatusUpdatesColumns.DATA_ID + "=OLD." + Data._ID + ";"
+ "   DELETE FROM " + Tables.NAME_LOOKUP
+ "     WHERE " + NameLookupColumns.DATA_ID + "=OLD." + Data._ID + ";"
                + "   DELETE FROM " + Tables.SPEED_DIAL
                + "     WHERE " + "phone_id" + "=OLD." + Data._ID + ";"
+ " END");


//Synthetic comment -- @@ -1084,6 +1121,35 @@
db.execSQL("DROP VIEW IF EXISTS " + Views.DATA_RESTRICTED + ";");
db.execSQL("DROP VIEW IF EXISTS " + Views.RAW_CONTACTS_ALL + ";");
db.execSQL("DROP VIEW IF EXISTS " + Views.RAW_CONTACTS_RESTRICTED + ";");
        //Code added for speed dial support starts
        db.execSQL("DROP VIEW IF EXISTS " + Views.SPEED_DIAL + ";");
        String speedDialColumns =
                SpeedDial.KEY_ID + ", "
                + Data.IS_PRIMARY + ", "
                + Data.IS_SUPER_PRIMARY + ", "
                + Data.DATA_VERSION + ", "
                + PackagesColumns.PACKAGE + " AS " + Data.RES_PACKAGE + ","
                + MimetypesColumns.MIMETYPE + " AS " + Data.MIMETYPE + ", "
                + Data.DATA1 + " AS " + SpeedDial.PHONE_NUMBER + ", "
                + Data.DATA2 + " AS " + SpeedDial.PHONE_TYPE + ", "
                + Data.DATA3 + ", "
                + Data.DATA4 + ", "
                + Data.DATA5 + ", "
                + Data.DATA6 + ", "
                + Data.DATA7 + ", "
                + Data.DATA8 + ", "
                + Data.DATA9 + ", "
                + Data.DATA10 + ", "
                + Data.DATA11 + ", "
                + Data.DATA12 + ", "
                + Data.DATA13 + ", "
                + Data.DATA14 + ", "
                + Data.DATA15 + ", "
                + Data.SYNC1 + ", "
                + Data.SYNC2 + ", "
                + Data.SYNC3 + ", "
                + Data.SYNC4;
        //Code added for speed dial support ends

String dataColumns =
Data.IS_PRIMARY + ", "
//Synthetic comment -- @@ -1182,6 +1248,39 @@
+   "' AND " + GroupsColumns.CONCRETE_ID + "="
+ Tables.DATA + "." + GroupMembership.GROUP_ROW_ID + ")";

        //Code added for speed dial support starts
        String speedDialSelect = "SELECT "
                + DataColumns.CONCRETE_ID + " AS " + SpeedDial.PHONE_ID + ", "
                + Data.RAW_CONTACT_ID + ", "
                + RawContactsColumns.CONCRETE_CONTACT_ID + " AS " + RawContacts.CONTACT_ID + ", "
                + syncColumns + ", "
                + speedDialColumns + ", "
                + contactOptionColumns + ", "
                + contactNameColumns + ", "
                + Contacts.LOOKUP_KEY + ", "
                + Contacts.PHOTO_ID + ", "
                + Contacts.NAME_RAW_CONTACT_ID + ","
                + ContactsColumns.LAST_STATUS_UPDATE_ID + ", "
                + Tables.GROUPS + "." + Groups.SOURCE_ID + " AS " + GroupMembership.GROUP_SOURCE_ID
                + " FROM " + Tables.SPEED_DIAL + " JOIN " + Tables.DATA +" ON ("
                + Tables.SPEED_DIAL + "." +SpeedDial.PHONE_ID +" = " + Tables.DATA + "." +Data._ID + ")"
                + " JOIN " + Tables.MIMETYPES + " ON ("
                + DataColumns.CONCRETE_MIMETYPE_ID + "=" + MimetypesColumns.CONCRETE_ID + ")"
                + " JOIN " + Tables.RAW_CONTACTS + " ON ("
                + DataColumns.CONCRETE_RAW_CONTACT_ID + "=" + RawContactsColumns.CONCRETE_ID + ")"
                + " JOIN " + Tables.CONTACTS + " ON ("
                + RawContactsColumns.CONCRETE_CONTACT_ID + "=" + ContactsColumns.CONCRETE_ID + ")"
                + " JOIN " + Tables.RAW_CONTACTS + " AS name_raw_contact ON("
                + Contacts.NAME_RAW_CONTACT_ID + "=name_raw_contact." + RawContacts._ID + ")"
                + " LEFT OUTER JOIN " + Tables.PACKAGES + " ON ("
                + DataColumns.CONCRETE_PACKAGE_ID + "=" + PackagesColumns.CONCRETE_ID + ")"
                + " LEFT OUTER JOIN " + Tables.GROUPS + " ON ("
                + MimetypesColumns.CONCRETE_MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE
                + "' AND " + GroupsColumns.CONCRETE_ID + "="
                + Tables.DATA + "." + GroupMembership.GROUP_ROW_ID + ")";
        db.execSQL("CREATE VIEW " + Views.SPEED_DIAL + " AS " + speedDialSelect);
        //Code added for speed dial support ends

db.execSQL("CREATE VIEW " + Views.DATA_ALL + " AS " + dataSelect);
db.execSQL("CREATE VIEW " + Views.DATA_RESTRICTED + " AS " + dataSelect + " WHERE "
+ RawContactsColumns.CONCRETE_IS_RESTRICTED + "=0");
//Synthetic comment -- @@ -2965,6 +3064,15 @@
return false;
}

    //Code added for speed dial support starts
    /**
     * getSpeedDailview returns speed dial view
     */
    public String getSpeedDialView() {
        return Views.SPEED_DIAL;
    }
    //Code added for speed dial support ends

public String getDataView() {
return getDataView(false);
}








//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index f4a4c5a..0d00320 100644

//Synthetic comment -- @@ -110,6 +110,7 @@
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.SpeedDial;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
//Synthetic comment -- @@ -241,6 +242,10 @@
private static final int RAW_CONTACT_ENTITIES = 15001;

private static final int PROVIDER_STATUS = 16001;
    //Code added for speed dial support starts
    //Id for speed dial table
    private static final int SPEED_DIAL = 16002;
    //Code added for speed dial support ends

private interface DataContactsQuery {
public static final String TABLE = "data "
//Synthetic comment -- @@ -362,6 +367,12 @@
private static final HashMap<String, String> sRawContactsProjectionMap;
/** Contains the columns from the raw contacts entity view*/
private static final HashMap<String, String> sRawContactsEntityProjectionMap;

    //Code added for speed dial support starts
    // hasMap for speed dial columns
    private static final HashMap<String, String> sSpeedDialProjectionMap;
    //Code added for speed dial support ends

/** Contains columns from the data view */
private static final HashMap<String, String> sDataProjectionMap;
/** Contains columns from the data view */
//Synthetic comment -- @@ -503,9 +514,24 @@
LIVE_FOLDERS_CONTACTS_FAVORITES);

matcher.addURI(ContactsContract.AUTHORITY, "provider_status", PROVIDER_STATUS);
        //Code added for speed dial support starts
        matcher.addURI(ContactsContract.AUTHORITY, "speed_dial", SPEED_DIAL);
        //Code added for speed dial support ends
}

static {

        //Code added for speed dial support starts
        sSpeedDialProjectionMap = new HashMap<String, String>();
        sSpeedDialProjectionMap.put(SpeedDial.KEY_ID, SpeedDial.KEY_ID);
        sSpeedDialProjectionMap.put(SpeedDial.PHONE_ID, SpeedDial.PHONE_ID);
        sSpeedDialProjectionMap.put(SpeedDial.PHONE_TYPE, SpeedDial.PHONE_TYPE);
        sSpeedDialProjectionMap.put(Contacts.DISPLAY_NAME, Contacts.DISPLAY_NAME);
        sSpeedDialProjectionMap.put(SpeedDial.PHONE_NUMBER, SpeedDial.PHONE_NUMBER);
        sSpeedDialProjectionMap.put(Contacts.PHOTO_ID, Contacts.PHOTO_ID);
        sSpeedDialProjectionMap.put(Data.CONTACT_ID, Data.CONTACT_ID);
        //Code added for speed dial support ends

sCountProjectionMap = new HashMap<String, String>();
sCountProjectionMap.put(BaseColumns._COUNT, "COUNT(*)");

//Synthetic comment -- @@ -2440,7 +2466,12 @@
id = insertStatusUpdate(values);
break;
}
            //Code added for speed dial support starts
            case SPEED_DIAL: {
                id = insertSpeedDial(uri, values);
                break;
            }
            //Code added for speed dial support ends
default:
mSyncToNetwork = true;
return mLegacyApiSupport.insert(uri, values);
//Synthetic comment -- @@ -3019,6 +3050,16 @@
return id;
}

    //Code added for speed dial support starts
    /**
     * insertSpeedDial insert the values into the speed dial table
     * @return 1 if successfully inserted 0 if not inserted
     */
    private long insertSpeedDial(Uri uri, ContentValues values) {
        final long id = mDb.insert(Tables.SPEED_DIAL, null, values);
        return id;
    }
    //Code added for speed dial support ends
/**
* Inserts a status update.
*/
//Synthetic comment -- @@ -3215,6 +3256,10 @@
readBooleanQueryParameter(uri, ContactsContract.CALLER_IS_SYNCADAPTER, false);
final int match = sUriMatcher.match(uri);
switch (match) {
            //Code added for speed dial support starts
            case SPEED_DIAL:
                return mDb.delete(Tables.SPEED_DIAL, selection, selectionArgs);
            //Code added for speed dial support ends
case SYNCSTATE:
return mDbHelper.getSyncState().delete(mDb, selection, selectionArgs);

//Synthetic comment -- @@ -3458,6 +3503,11 @@
final boolean callerIsSyncAdapter =
readBooleanQueryParameter(uri, ContactsContract.CALLER_IS_SYNCADAPTER, false);
switch(match) {
            //Code added for speed dial support starts
            case SPEED_DIAL: {
                return updateSpeedDial(uri,values,selection, selectionArgs);
            }
            //Code added for speed dial support ends
case SYNCSTATE:
return mDbHelper.getSyncState().update(mDb, values,
appendAccountToSelection(uri, selection), selectionArgs);
//Synthetic comment -- @@ -3843,6 +3893,18 @@
}
}

    //Code added for speed dial support starts
    /**
     * updates the speed dial table
     */
    private int updateSpeedDial(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int count = 0;
        count=mDb.update(Tables.SPEED_DIAL, values, selection, selectionArgs);
        return count;
    }
    //Code added for speed dial support ends

private int updateContactOptions(ContentValues values, String selection,
String[] selectionArgs) {
int count = 0;
//Synthetic comment -- @@ -4190,6 +4252,12 @@
// write a new query() block to make sure it protects restricted data.
final int match = sUriMatcher.match(uri);
switch (match) {
            //Code added for speed dial support starts
            case SPEED_DIAL: {
                setTablesAndProjectionMapForSpeedDial(qb, uri, projection);
                break;
            }
            //Code added for speed dial support ends
case SYNCSTATE:
return mDbHelper.getSyncState().query(db, projection, selection,  selectionArgs,
sortOrder);
//Synthetic comment -- @@ -5236,6 +5304,19 @@
qb.setProjectionMap(sRawContactsEntityProjectionMap);
appendAccountFromParameter(qb, uri);
}
    //Code added for speed dial support starts
    /**
     * setTablesAndProjectionMapForSpeedDial set the table and the projection for speed dial URI
     */
    private void setTablesAndProjectionMapForSpeedDial(SQLiteQueryBuilder qb, Uri uri,
            String[] projection) {
        StringBuilder sb = new StringBuilder();
        sb.append(mDbHelper.getSpeedDialView());
        qb.setTables(sb.toString());
        qb.setProjectionMap(sSpeedDialProjectionMap);
    }
    //Code added for speed dial support ends


private void setTablesAndProjectionMapForData(SQLiteQueryBuilder qb, Uri uri,
String[] projection, boolean distinct) {







