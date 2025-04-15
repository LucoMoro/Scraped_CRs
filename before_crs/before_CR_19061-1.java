/*Add support for IPv6 PDP/PDN type selection in ApnEditor

This extends the ApnEditor with two types; IPv6 and IPv4v6.
The type is passed down to the RIL, backward compatibility is maintained as the value is added last in the data array.

Change-Id:I9ee670b7248f521bf0b3e83a6ec89638d01b66d6*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index c31d88c..3e87041 100644

//Synthetic comment -- @@ -135,6 +135,7 @@
"mmsc TEXT," +
"authtype INTEGER," +
"type TEXT," +
"current INTEGER);");

initDatabase(db);
//Synthetic comment -- @@ -201,6 +202,8 @@

db.execSQL("ALTER TABLE " + CARRIERS_TABLE +
" ADD COLUMN authtype INTEGER DEFAULT -1;");

oldVersion = 5 << 16 | 6;
}
//Synthetic comment -- @@ -255,6 +258,11 @@
map.put(Telephony.Carriers.TYPE, type);
}

String auth = parser.getAttributeValue(null, "authtype");
if (auth != null) {
map.put(Telephony.Carriers.AUTH_TYPE, Integer.parseInt(auth));
//Synthetic comment -- @@ -430,7 +438,9 @@
if (values.containsKey(Telephony.Carriers.AUTH_TYPE) == false) {
values.put(Telephony.Carriers.AUTH_TYPE, -1);
}


long rowID = db.insert(CARRIERS_TABLE, null, values);
if (rowID > 0)







