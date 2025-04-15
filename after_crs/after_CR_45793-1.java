/*CB: Add system property to enable/disable dup detection

If system property persist.cb.dup_detection is set to false, duplicate
detection is disabled.  If it is set to true, duplicate detection is
enabled.  If the system property does not exist, using true/enabled
as a default value.

Change-Id:I7c69e3840d6a61981cfc4cdaf7c62fb421a74d03*/




//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java
//Synthetic comment -- index 04fea0f..9319d2b 100644

//Synthetic comment -- @@ -136,12 +136,14 @@
return;
}

        if (CellBroadcastContentProvider.mUseDupDetection) {
            // Set.add() returns false if message ID has already been added
            MessageIdAndScope messageIdAndScope = new MessageIdAndScope(message.getSerialNumber(),
                    message.getLocation());
            if (!sCmasIdList.add(messageIdAndScope)) {
                Log.d(TAG, "ignoring duplicate alert with " + messageIdAndScope);
                return;
            }
}

final Intent alertIntent = new Intent(SHOW_NEW_ALERT_ACTION);








//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastContentProvider.java b/src/com/android/cellbroadcastreceiver/CellBroadcastContentProvider.java
//Synthetic comment -- index f368725..d28d358 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemProperties;
import android.provider.Telephony;
import android.telephony.CellBroadcastMessage;
import android.text.TextUtils;
//Synthetic comment -- @@ -59,6 +60,12 @@
/** MIME type for an individual cell broadcast. */
private static final String CB_TYPE = "vnd.android.cursor.item/cellbroadcast";

    /** system property to enable/disable broadcast duplicate detecion.  */
    private static final String CB_DUP_DETECTION = "persist.cb.dup_detection";

    /** Check for system property to enable/disable duplicate detection.  */
    static boolean mUseDupDetection = SystemProperties.getBoolean(CB_DUP_DETECTION, true);

static {
sUriMatcher.addURI(CB_AUTHORITY, null, CB_ALL);
sUriMatcher.addURI(CB_AUTHORITY, "#", CB_ALL_ID);
//Synthetic comment -- @@ -196,6 +203,8 @@

/**
* Internal method to insert a new Cell Broadcast into the database and notify observers.
     * If duplicate detection is disabled by the system property {@link #mUseDupDetection}
     * message will be treated as a new Cell Broadcast.
* @param message the message to insert
* @return true if the broadcast is new, false if it's a duplicate broadcast.
*/
//Synthetic comment -- @@ -203,38 +212,40 @@
SQLiteDatabase db = mOpenHelper.getWritableDatabase();
ContentValues cv = message.getContentValues();

        if (mUseDupDetection) {
            // Check for existing alert with same serial number and geo scope
            String serial = cv.getAsString(Telephony.CellBroadcasts.SERIAL_NUMBER);
            String plmn = cv.getAsString(Telephony.CellBroadcasts.PLMN);
            String lac = cv.getAsString(Telephony.CellBroadcasts.LAC);
            String cid = cv.getAsString(Telephony.CellBroadcasts.CID);
            String selection;
            String[] selectionArgs;

            if (plmn != null) {
                if (lac != null) {
                    if (cid != null) {
                        selection = QUERY_BY_SERIAL_PLMN_LAC_CID;
                        selectionArgs = new String[] {serial, plmn, lac, cid};
                    } else {
                        selection = QUERY_BY_SERIAL_PLMN_LAC;
                        selectionArgs = new String[] {serial, plmn, lac};
                    }
} else {
                    selection = QUERY_BY_SERIAL_PLMN;
                    selectionArgs = new String[] {serial, plmn};
}
} else {
                selection = QUERY_BY_SERIAL;
                selectionArgs = new String[] {serial};
}

            Cursor c = db.query(CellBroadcastDatabaseHelper.TABLE_NAME, SELECT_ID_COLUMN,
                    selection, selectionArgs, null, null, null);

            if (c.getCount() != 0) {
                Log.d(TAG, "ignoring dup broadcast serial=" + serial + " found " + c.getCount());
                return false;
            }
}

long rowId = db.insert(CellBroadcastDatabaseHelper.TABLE_NAME, null, cv);







