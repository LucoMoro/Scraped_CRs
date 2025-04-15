/*Use more tokens when tokenizing contact name - usefull for better searching.*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider.java b/src/com/android/providers/contacts/ContactsProvider.java
//Synthetic comment -- index 777aa61..e680274 100644

//Synthetic comment -- @@ -117,6 +117,9 @@
private static final String PREFS_NAME_OWNER = "owner-info";
private static final String PREF_OWNER_ID = "owner-id";

    /** this string is SQL escaped */
    private static final String TOKENS = " !\"#$%&''()*+,-./:;<=>?@[\\]^_`{|}~";

/** this is suitable for use by insert/update/delete/query and may be passed
* as a method call parameter. Only insert/update/delete/query should call .clear() on it */
private final ContentValues mValues = new ContentValues();
//Synthetic comment -- @@ -390,15 +393,16 @@
// "peopleLookup" table with token_index.
// 2) Another type has "peopleLookup" table with token_index but does not have
// "peopleLookupWithPhoneticName" table.
        // In version 82 are more tokens, so regenerate is needed too.
// For simplicity, both databases are once dropped here.
// This is slow but should be done just once anyway...
        if (oldVersion == 80 || oldVersion == 81 || oldVersion == 82) {
Log.i(TAG, "Upgrading contacts database from version " + oldVersion + " to " +
newVersion + ", which will preserve existing data");

recreatePeopleLookupTable(db);
try {
                String query = "SELECT _TOKENIZE('peopleLookup', _id, name, '" + TOKENS + "', 1) FROM people";
Cursor cursor = db.rawQuery(query, null);
try {
int rows = cursor.getCount();
//Synthetic comment -- @@ -411,12 +415,12 @@
} catch (SQLiteException e) {
Log.e(TAG, e.toString() + ": " + e.getMessage());
}

recreatePeopleLookupWithPhoneticNameTable(db);
try {
String query = "SELECT _TOKENIZE('peopleLookupWithPhoneticName', _id, "
+ PHONETIC_LOOKUP_SQL_SIMPLE +
                    ", '" + TOKENS + "', 1) FROM people";
Cursor cursor = db.rawQuery(query, null);
try {
int rows = cursor.getCount();
//Synthetic comment -- @@ -429,8 +433,8 @@
} catch (SQLiteException e) {
Log.e(TAG, e.toString() + ": " + e.getMessage());
}

            oldVersion = 83;
}

return upgradeWasLossless;
//Synthetic comment -- @@ -474,11 +478,11 @@
db.execSQL("CREATE TRIGGER peopleLookup_update UPDATE OF name ON people " +
"BEGIN " +
"DELETE FROM peopleLookup WHERE source = new._id;" +
                        "SELECT _TOKENIZE('peopleLookup', new._id, new.name, '" + TOKENS + "', 1);" +
"END");
db.execSQL("CREATE TRIGGER peopleLookup_insert AFTER INSERT ON people " +
"BEGIN " +
                        "SELECT _TOKENIZE('peopleLookup', new._id, new.name, '" + TOKENS + "', 1);" +
"END");
}

//Synthetic comment -- @@ -506,13 +510,13 @@
"DELETE FROM peopleLookupWithPhoneticName WHERE source = new._id;" +
"SELECT _TOKENIZE('peopleLookupWithPhoneticName', new._id, " +
PHONETIC_LOOKUP_SQL_SIMPLE_WITH_NEW +
                        ", '" + TOKENS + "', 1);" +
"END");
db.execSQL("CREATE TRIGGER peopleLookupWithPhoneticName_insert AFTER INSERT ON people " +
"BEGIN " +
"SELECT _TOKENIZE('peopleLookupWithPhoneticName', new._id, " +
PHONETIC_LOOKUP_SQL_SIMPLE_WITH_NEW +
                        ", '" + TOKENS + "', 1);" +
"END");
}

//Synthetic comment -- @@ -3984,7 +3988,7 @@
private static final String TAG = "ContactsProvider";

/* package private */ static final String DATABASE_NAME = "contacts.db";
    /* package private */ static final int DATABASE_VERSION = 83;

protected static final String CONTACTS_AUTHORITY = "contacts";
protected static final String CALL_LOG_AUTHORITY = "call_log";







