//<Beginning of snippet n. 0>
package com.example.simflags;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

public class SimFlagsProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.simflags.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/flags");

    private static final String TABLE_FLAGS = "operator_flags";
    private static final int FLAG = 1;
    
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return database.query(TABLE_FLAGS, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(TABLE_FLAGS, null, values);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(TABLE_FLAGS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(TABLE_FLAGS, values, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/" + AUTHORITY + ".flags";
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "sim_flags.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_FLAGS + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "flag_name TEXT," +
                    "flag_value TEXT" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLAGS);
            onCreate(db);
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void loadEFTransparent(int fileid, Message onLoaded) {
    Message response = obtainMessage(EVENT_GET_BINARY_SIZE_DONE, fileid, 0, onLoaded);
    phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid), 0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

/**
 * Load a SIM Transparent EF-IMG. Used right after loadEFImgLinearFixed to
 * retrieve STK's icon data.
 */
public void loadEFImgTransparent(int fileid, Message response) {
    phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(fileid), 0, 0, size, null, null, obtainMessage(EVENT_READ_BINARY_DONE, fileid, 0, response));
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
void unsetOnEcbModeExitResponse(Handler h) {
    // Implement appropriate management of exiting ECB mode response
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public boolean isCspPlmnEnabled() {
    return mActivePhone.isCspPlmnEnabled();
}
//<End of snippet n. 3>