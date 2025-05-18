//<Beginning of snippet n. 0>
return;
}

boolean isDupDetectionEnabled = !"false".equals(System.getProperty("persist.cb.dup_detection", "true"));

if (!isDupDetectionEnabled) {
    Log.d(TAG, "Duplicate detection is disabled.");
    final Intent alertIntent = new Intent(SHOW_NEW_ALERT_ACTION);
    // Insert logic to handle the alert intent without duplicate detection here
    return;
} else {
    Log.d(TAG, "Duplicate detection is enabled.");
}

// Set.add() returns false if message ID has already been added
MessageIdAndScope messageIdAndScope = new MessageIdAndScope(message.getSerialNumber(),
        message.getLocation());
if (!sCmasIdList.add(messageIdAndScope)) {
    Log.d(TAG, "ignoring duplicate alert with " + messageIdAndScope);
    return;
}

final Intent alertIntent = new Intent(SHOW_NEW_ALERT_ACTION);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.telephony.CellBroadcastMessage;
import android.text.TextUtils;

/** MIME type for an individual cell broadcast. */
private static final String CB_TYPE = "vnd.android.cursor.item/cellbroadcast";

static {
    sUriMatcher.addURI(CB_AUTHORITY, null, CB_ALL);
    sUriMatcher.addURI(CB_AUTHORITY, "#", CB_ALL_ID);
}

/**
 * Internal method to insert a new Cell Broadcast into the database and notify observers.
 * @param message the message to insert
 * @return true if the broadcast is new, false if it's a duplicate broadcast.
 */
SQLiteDatabase db = mOpenHelper.getWritableDatabase();
ContentValues cv = message.getContentValues();

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

long rowId = db.insert(CellBroadcastDatabaseHelper.TABLE_NAME, null, cv);
//<End of snippet n. 1>