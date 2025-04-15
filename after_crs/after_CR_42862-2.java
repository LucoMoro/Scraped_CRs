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
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastXmlSerializer;

//Synthetic comment -- @@ -26,6 +28,9 @@

import android.accounts.Account;
import android.accounts.AccountAndUser;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//Synthetic comment -- @@ -359,6 +364,10 @@
mPendingFile = new AtomicFile(new File(syncDir, "pending.bin"));
mStatisticsFile = new AtomicFile(new File(syncDir, "stats.bin"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        mContext.registerReceiver(mBroadcastReceiver, filter);

readAccountInfoLocked();
readStatusLocked();
readPendingOperationsLocked();
//Synthetic comment -- @@ -1515,7 +1524,21 @@
Random random = new Random(System.currentTimeMillis());
mSyncRandomOffset = random.nextInt(86400);
}

                String autoSyncDefault = parser.getAttributeValue(null, "syncAutoDefault");
                if (autoSyncDefault == null) {
                    mDefaultMasterSyncAutomatically = mContext.getResources().getBoolean(
                        com.android.internal.R.bool.
                        config_syncstorageengine_masterSyncAutomatically);
                } else {
                    mDefaultMasterSyncAutomatically = Boolean.parseBoolean(autoSyncDefault);
                }

                if (listen == null) {
                    mMasterSyncAutomatically.put(0, mDefaultMasterSyncAutomatically);
                } else {
                    mMasterSyncAutomatically.put(0, Boolean.parseBoolean(listen));
                }
eventType = parser.next();
AuthorityInfo authority = null;
Pair<Bundle, Long> periodicSync = null;
//Synthetic comment -- @@ -1624,7 +1647,10 @@
Log.e(TAG, "the user in listen-for-tickles is null", e);
}
String enabled = parser.getAttributeValue(null, XML_ATTR_ENABLED);
        boolean listen = mDefaultMasterSyncAutomatically;
        if (enabled != null) {
            listen = Boolean.parseBoolean(enabled);
        }
mMasterSyncAutomatically.put(userId, listen);
}

//Synthetic comment -- @@ -1757,6 +1783,8 @@
out.attribute(null, XML_ATTR_NEXT_AUTHORITY_ID, Integer.toString(mNextAuthorityId));
out.attribute(null, XML_ATTR_SYNC_RANDOM_OFFSET, Integer.toString(mSyncRandomOffset));

            out.attribute(null, "syncAutoDefault",
                    Boolean.toString(mDefaultMasterSyncAutomatically));
// Write the Sync Automatically flags for each user
final int M = mMasterSyncAutomatically.size();
for (int m = 0; m < M; m++) {
//Synthetic comment -- @@ -1944,7 +1972,11 @@
String value = c.getString(c.getColumnIndex("value"));
if (name == null) continue;
if (name.equals("listen_for_tickles")) {
                    if (value == null) {
                        setMasterSyncAutomatically(mDefaultMasterSyncAutomatically, 0);
                    } else {
                        setMasterSyncAutomatically(Boolean.parseBoolean(value), 0);
                    }
} else if (name.startsWith("sync_provider_")) {
String provider = name.substring("sync_provider_".length(),
name.length());
//Synthetic comment -- @@ -2289,4 +2321,30 @@
}
}
}

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TelephonyIntents.ACTION_SIM_STATE_CHANGED.equals(action) &&
                IccCardConstants.INTENT_VALUE_ICC_LOADED.equals(intent.getStringExtra(
                    IccCardConstants.INTENT_KEY_ICC_STATE))) {
                /* If the SIM has changed, that means that we need to see if the default
                 * sync value has changed. If it has changed, MasterSyncAutomatically needs
                 * to be updated for all users.
                 */
                synchronized (mAuthorities) {
                    boolean newDefault = mContext.getResources().getBoolean(com.android.
                        internal.R.bool.config_syncstorageengine_masterSyncAutomatically);
                    if (newDefault != mDefaultMasterSyncAutomatically) {
                        mDefaultMasterSyncAutomatically = newDefault;
                        final int numberOfUsers = mMasterSyncAutomatically.size();
                        for (int i = 0; i < numberOfUsers; i++) {
                            int userId = mMasterSyncAutomatically.keyAt(i);
                            setMasterSyncAutomatically(mDefaultMasterSyncAutomatically, userId);
                        }
                    }
                }
            }
        }
    };
}







