/*Added the lock internet setting feature for APN.

The APN will be locked following the customization file specified.
So that the locked APN can not be edit or delete by end user.

Change-Id:I2b26025e72a44b9582cd2af3b7b963f96d01fcba*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index c31d88c..948b32d 100644

//Synthetic comment -- @@ -135,7 +135,8 @@
"mmsc TEXT," +
"authtype INTEGER," +
"type TEXT," +
                    "current INTEGER);");

initDatabase(db);
}
//Synthetic comment -- @@ -204,6 +205,18 @@

oldVersion = 5 << 16 | 6;
}
}

/**
//Synthetic comment -- @@ -260,6 +273,11 @@
map.put(Telephony.Carriers.AUTH_TYPE, Integer.parseInt(auth));
}

return map;
}

//Synthetic comment -- @@ -430,7 +448,9 @@
if (values.containsKey(Telephony.Carriers.AUTH_TYPE) == false) {
values.put(Telephony.Carriers.AUTH_TYPE, -1);
}


long rowID = db.insert(CARRIERS_TABLE, null, values);
if (rowID > 0)







