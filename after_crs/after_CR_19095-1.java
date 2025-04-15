/*Fix database Lock.

Change-Id:I938042eee5b34134765086f2e4b96d5aff7e66d2*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherProvider.java b/src/com/android/launcher2/LauncherProvider.java
//Synthetic comment -- index a02e449..8d25b3d 100644

//Synthetic comment -- @@ -66,6 +66,7 @@
private static final String DATABASE_NAME = "launcher.db";

private static final int DATABASE_VERSION = 8;
    protected final Object mLock = new Object();

static final String AUTHORITY = "com.android.launcher2.settings";

//Synthetic comment -- @@ -106,67 +107,77 @@
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
qb.setTables(args.table);

        synchronized (mLock) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Cursor result = qb.query(db, projection, args.where, args.args, null, null, sortOrder);
            result.setNotificationUri(getContext().getContentResolver(), uri);

            return result;
        }
}

@Override
    public synchronized Uri insert(Uri uri, ContentValues initialValues) {
SqlArguments args = new SqlArguments(uri);

        synchronized (mLock) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final long rowId = db.insert(args.table, null, initialValues);
            if (rowId <= 0) return null;

            uri = ContentUris.withAppendedId(uri, rowId);
            sendNotify(uri);

            return uri;
        }
}

@Override
public int bulkInsert(Uri uri, ContentValues[] values) {
SqlArguments args = new SqlArguments(uri);

        synchronized (mLock) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                int numValues = values.length;
                for (int i = 0; i < numValues; i++) {
                    if (db.insert(args.table, null, values[i]) < 0) return 0;
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
}

            sendNotify(uri);
            return values.length;
}
}

@Override
    public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
SqlArguments args = new SqlArguments(uri, selection, selectionArgs);

        synchronized (mLock) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int count = db.delete(args.table, args.where, args.args);
            if (count > 0) sendNotify(uri);

            return count;
        }
}

@Override
    public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
SqlArguments args = new SqlArguments(uri, selection, selectionArgs);

        synchronized (mLock) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int count = db.update(args.table, values, args.where, args.args);
            if (count > 0) sendNotify(uri);

            return count;
        }
}

private void sendNotify(Uri uri) {







