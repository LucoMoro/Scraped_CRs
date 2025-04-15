/*Removed packet_id unique constraint (issue 1221/2599)

The packet_id inside the ImProvider is marked as unique.
But it can happen often that this is not the case. This results
in messages not shown inside the Im application.

Because the messages are stored inside an in memory table, the
database version does not need to be upgraded.

More details athttp://code.google.com/p/android/issues/detail?id=1221orhttp://code.google.com/p/android/issues/detail?id=2599*/
//Synthetic comment -- diff --git a/src/com/android/providers/im/ImProvider.java b/src/com/android/providers/im/ImProvider.java
//Synthetic comment -- index 319a197..56374a9 100644

//Synthetic comment -- @@ -470,7 +470,7 @@
// across IM sessions, store the message table in memory db only)
db.execSQL("CREATE TABLE IF NOT EXISTS " + cpDbName + TABLE_MESSAGES + " (" +
"_id INTEGER PRIMARY KEY," +
                    "packet_id TEXT UNIQUE," +
"contact TEXT," +
"provider INTEGER," +
"account INTEGER," +
//Synthetic comment -- @@ -515,7 +515,7 @@
// group chat messages
db.execSQL("CREATE TABLE IF NOT EXISTS " + cpDbName + TABLE_GROUP_MESSAGES + " (" +
"_id INTEGER PRIMARY KEY," +
                    "packet_id TEXT UNIQUE," +
"contact TEXT," +
"groupId INTEGER," +
"body TEXT," +







