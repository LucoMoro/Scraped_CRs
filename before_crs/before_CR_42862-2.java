/*Fixing cases where master sync config was not read correctly

One typically want to bind the master sync config option
to the mnc/mcc. In this case that did not work very well
as the SyncStorage engine was initalized before the SIM
was. This change listens for the SIM to update the config
option.

Change-Id:Ibee8f9c9891f7939f56db031c8a2afe021f1b361*/
//Synthetic comment -- diff --git a/core/java/android/content/SyncStorageEngine.java b/core/java/android/content/SyncStorageEngine.java
//Synthetic comment -- index 7a9fc65..4934e4b 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package android.content;

import com.android.internal.os.AtomicFile;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastXmlSerializer;

//Synthetic comment -- @@ -26,6 +28,9 @@

import android.accounts.Account;
import android.accounts.AccountAndUser;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//Synthetic comment -- @@ -359,6 +364,10 @@
mPendingFile = new AtomicFile(new File(syncDir, "pending.bin"));
mStatisticsFile = new AtomicFile(new File(syncDir, "stats.bin"));

readAccountInfoLocked();
readStatusLocked();
readPendingOperationsLocked();
//Synthetic comment -- @@ -1515,7 +1524,21 @@
Random random = new Random(System.currentTimeMillis());
mSyncRandomOffset = random.nextInt(86400);
}
                mMasterSyncAutomatically.put(0, listen == null || Boolean.parseBoolean(listen));
eventType = parser.next();
AuthorityInfo authority = null;
Pair<Bundle, Long> periodicSync = null;
//Synthetic comment -- @@ -1624,7 +1647,10 @@
Log.e(TAG, "the user in listen-for-tickles is null", e);
}
String enabled = parser.getAttributeValue(null, XML_ATTR_ENABLED);
        boolean listen = enabled == null || Boolean.parseBoolean(enabled);
mMasterSyncAutomatically.put(userId, listen);
}

//Synthetic comment -- @@ -1757,6 +1783,8 @@
out.attribute(null, XML_ATTR_NEXT_AUTHORITY_ID, Integer.toString(mNextAuthorityId));
out.attribute(null, XML_ATTR_SYNC_RANDOM_OFFSET, Integer.toString(mSyncRandomOffset));

// Write the Sync Automatically flags for each user
final int M = mMasterSyncAutomatically.size();
for (int m = 0; m < M; m++) {
//Synthetic comment -- @@ -1944,7 +1972,11 @@
String value = c.getString(c.getColumnIndex("value"));
if (name == null) continue;
if (name.equals("listen_for_tickles")) {
                    setMasterSyncAutomatically(value == null || Boolean.parseBoolean(value), 0);
} else if (name.startsWith("sync_provider_")) {
String provider = name.substring("sync_provider_".length(),
name.length());
//Synthetic comment -- @@ -2289,4 +2321,30 @@
}
}
}
}







