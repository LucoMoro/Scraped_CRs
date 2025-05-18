//<Beginning of snippet n. 0>
package com.example.operatorflags;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

public class OperatorFlagsProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.operatorflags.provider";
    private static final String TABLE_NAME = "operator_flags";
    private static final int OPERATOR_FLAGS = 1;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, OPERATOR_FLAGS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) != OPERATOR_FLAGS) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        if (getContext() == null || checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Permission not granted to access SIM card data.");
        }

        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            String operatorName = telephonyManager.getNetworkOperatorName();
            MatrixCursor cursor = new MatrixCursor(new String[]{"operator_name"});
            cursor.addRow(new String[]{operatorName});
            return cursor;
        }

        throw new SQLException("Failed to retrieve operator flags from SIM card.");
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OPERATOR_FLAGS:
                return "vnd.android.cursor.dir/vnd.com.example.operatorflags";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Insert operation is not supported.");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete operation is not supported.");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Update operation is not supported.");
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void loadEFTransparent(int fileid, Message onLoaded) {
    Message response = obtainMessage(EVENT_GET_BINARY_SIZE_DONE, fileid, 0, onLoaded);
    phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid), 0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

public void loadEFImgLinearFixed(int fileid, Message response) {
    phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(fileid), 0, 0, size, null, null, obtainMessage(EVENT_READ_BINARY_DONE, fileid, 0, response));
    size = ((data[RESPONSE_DATA_FILE_SIZE_1] & 0xff) << 8) + (data[RESPONSE_DATA_FILE_SIZE_2] & 0xff);
    if (TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) {
        throw new IccFileTypeMismatch();
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
void unsetOnEcbModeExitResponse(Handler h) { }

//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public boolean isCspPlmnEnabled() {
    return mActivePhone.isCspPlmnEnabled();
}
//<End of snippet n. 3>