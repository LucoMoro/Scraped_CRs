/*packages/providers/TelephonyProvider: IP version support

Add support for creating carriers table in telephony.db for profiles
that may have a "bearertype" field.

The bearertype column can have three values: "IP", "IPV6", "IPV4V6".
If there is no value in the column, IPV4 support is assumed.

Change-Id:I65259682435cd1742597d11c2fd067799cb57eb6*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index c31d88c..dab4097 100644

//Synthetic comment -- @@ -89,7 +89,7 @@
private Context mContext;

/**
         * DatabaseHelper helper class for loading apns into a database.
*
* @param parser the system-default parser for apns.xml
* @param confidential an optional parser for confidential APNS (stored separately)
//Synthetic comment -- @@ -135,6 +135,7 @@
"mmsc TEXT," +
"authtype INTEGER," +
"type TEXT," +
"current INTEGER);");

initDatabase(db);
//Synthetic comment -- @@ -148,7 +149,7 @@
try {
XmlUtils.beginDocument(parser, "apns");
publicversion = Integer.parseInt(parser.getAttributeValue(null, "version"));
                loadApns(db, parser);
} catch (Exception e) {
Log.e(TAG, "Got exception while loading APN database.", e);
} finally {
//Synthetic comment -- @@ -173,7 +174,7 @@
+ confFile.getAbsolutePath());
}

                loadApns(db, confparser);
} catch (FileNotFoundException e) {
// It's ok if the file isn't found. It means there isn't a confidential file
// Log.e(TAG, "File not found: '" + confFile.getAbsolutePath() + "'");
//Synthetic comment -- @@ -189,7 +190,7 @@
if (oldVersion < (5 << 16 | 6)) {
// 5 << 16 is the Database version and 6 in the xml version.

                // This change adds a new authtype column to the database.
// The auth type column can have 4 values: 0 (None), 1 (PAP), 2 (CHAP)
// 3 (PAP or CHAP). To avoid breaking compatibility, with already working
// APNs, the unset value (-1) will be used. If the value is -1.
//Synthetic comment -- @@ -198,25 +199,30 @@
// pre-configured APNs and hence it is set to -1 for them. Similarly,
// if the user, has added a new APN, we set the authentication type
// to -1.

db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
" ADD COLUMN authtype INTEGER DEFAULT -1;");

oldVersion = 5 << 16 | 6;
}
}

/**
         * Gets the next row of apn values.
*
* @param parser the parser
* @return the row or null if it's not an apn
*/
private ContentValues getRow(XmlPullParser parser) {
            if (!"apn".equals(parser.getName())) {
return null;
}

ContentValues map = new ContentValues();

String mcc = parser.getAttributeValue(null, "mcc");
//Synthetic comment -- @@ -227,11 +233,8 @@
map.put(Telephony.Carriers.MCC, mcc);
map.put(Telephony.Carriers.MNC, mnc);
map.put(Telephony.Carriers.NAME, parser.getAttributeValue(null, "carrier"));
            map.put(Telephony.Carriers.APN, parser.getAttributeValue(null, "apn"));
map.put(Telephony.Carriers.USER, parser.getAttributeValue(null, "user"));
            map.put(Telephony.Carriers.SERVER, parser.getAttributeValue(null, "server"));
map.put(Telephony.Carriers.PASSWORD, parser.getAttributeValue(null, "password"));

// do not add NULL to the map so that insert() will set the default value
String proxy = parser.getAttributeValue(null, "proxy");
if (proxy != null) {
//Synthetic comment -- @@ -260,17 +263,32 @@
map.put(Telephony.Carriers.AUTH_TYPE, Integer.parseInt(auth));
}

return map;
}

/*
         * Loads apns from xml file into the database
*
* @param db the sqlite database to write to
* @param parser the xml parser
*
*/
        private void loadApns(SQLiteDatabase db, XmlPullParser parser) {
if (parser != null) {
try {
while (true) {







