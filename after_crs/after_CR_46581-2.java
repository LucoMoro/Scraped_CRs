/*Disable usage of WAL in DatabaseHelper

According to SQLite documentation, WAL can not be used when database
partition is not accessible for writing (even when the client just
performs read operation).
However, such situation may happen when the partition is full.
As SettingsProvider should always be able to read a value of ANDROID_ID
from corresponding database, this commit disables usage of WAL.
Without this we risk ending up in a situation where /data is full
and the phone is unable to boot properly.

Change-Id:I1f79bbcd8d0f64bf35dc9d7b846bcfb2664d2eac*/




//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index b649b43..cc52a83 100644

//Synthetic comment -- @@ -109,7 +109,6 @@
super(context, dbNameForUser(userHandle), null, DATABASE_VERSION);
mContext = context;
mUserHandle = userHandle;
}

public static boolean isValidTable(String name) {







