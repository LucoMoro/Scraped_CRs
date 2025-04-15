/*Adding missing columns to Telephony Provider.

The PROTOCOL and ROAMING_PROTOCOL columns have
been added to the framework, but the respective
columns are not available in the provider database
causing several crashes to the phone application.

Change-Id:I7cdc80d76a72ae0aac69028e9f293d5b50638bb4Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index 85bd0f6..ae9bd5b 100644

//Synthetic comment -- @@ -135,6 +135,8 @@
"mmsc TEXT," +
"authtype INTEGER," +
"type TEXT," +
"current INTEGER);");

initDatabase(db);







