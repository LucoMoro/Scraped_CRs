/*Use the new download manager APIs introduced in change 7400*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadInfo.java b/src/com/android/providers/downloads/DownloadInfo.java
//Synthetic comment -- index 24bdbfa..336a2a0 100644

//Synthetic comment -- @@ -87,10 +87,10 @@

public void sendIntentIfRequested(Uri contentUri, Context context) {
if (mPackage != null && mClass != null) {
            Intent intent = new Intent(Downloads.DOWNLOAD_COMPLETED_ACTION);
intent.setClassName(mPackage, mClass);
if (mExtras != null) {
                intent.putExtra(Downloads.NOTIFICATION_EXTRAS, mExtras);
}
// We only send the content: URI, for security reasons. Otherwise, malicious
//     applications would have an easier time spoofing download results by








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadNotification.java b/src/com/android/providers/downloads/DownloadNotification.java
//Synthetic comment -- index 6837327..e8fed8d 100644

//Synthetic comment -- @@ -43,14 +43,18 @@

static final String LOGTAG = "DownloadNotification";
static final String WHERE_RUNNING = 
        "(" + Downloads.STATUS + " >= '100') AND (" +
        Downloads.STATUS + " <= '199') AND (" +
        Downloads.VISIBILITY + " IS NULL OR " +
        Downloads.VISIBILITY + " == '" + Downloads.VISIBILITY_VISIBLE + "' OR " +
        Downloads.VISIBILITY + " == '" + Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED + "')";
static final String WHERE_COMPLETED =
        Downloads.STATUS + " >= '200' AND " +
        Downloads.VISIBILITY + " == '" + Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED + "'";


/**
//Synthetic comment -- @@ -110,11 +114,14 @@
// Active downloads
Cursor c = mContext.getContentResolver().query(
Downloads.CONTENT_URI, new String [] {
                        Downloads._ID, Downloads.TITLE, Downloads.DESCRIPTION,
                        Downloads.NOTIFICATION_PACKAGE,
                        Downloads.NOTIFICATION_CLASS,
                        Downloads.CURRENT_BYTES, Downloads.TOTAL_BYTES,
                        Downloads.STATUS, Downloads._DATA
},
WHERE_RUNNING, null, Downloads._ID);

//Synthetic comment -- @@ -212,12 +219,17 @@
// Completed downloads
Cursor c = mContext.getContentResolver().query(
Downloads.CONTENT_URI, new String [] {
                        Downloads._ID, Downloads.TITLE, Downloads.DESCRIPTION,
                        Downloads.NOTIFICATION_PACKAGE,
                        Downloads.NOTIFICATION_CLASS,
                        Downloads.CURRENT_BYTES, Downloads.TOTAL_BYTES,
                        Downloads.STATUS, Downloads._DATA,
                        Downloads.LAST_MODIFICATION, Downloads.DESTINATION
},
WHERE_COMPLETED, null, Downloads._ID);









//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadProvider.java b/src/com/android/providers/downloads/DownloadProvider.java
//Synthetic comment -- index 23042b6..25d25e3 100644

//Synthetic comment -- @@ -77,19 +77,19 @@

private static final String[] sAppReadableColumnsArray = new String[] {
Downloads._ID,
        Downloads.APP_DATA,
Downloads._DATA,
        Downloads.MIMETYPE,
        Downloads.VISIBILITY,
        Downloads.CONTROL,
        Downloads.STATUS,
        Downloads.LAST_MODIFICATION,
        Downloads.NOTIFICATION_PACKAGE,
        Downloads.NOTIFICATION_CLASS,
        Downloads.TOTAL_BYTES,
        Downloads.CURRENT_BYTES,
        Downloads.TITLE,
        Downloads.DESCRIPTION
};

private static HashSet<String> sAppReadableColumnsSet;
//Synthetic comment -- @@ -199,34 +199,34 @@
try {
db.execSQL("CREATE TABLE " + DB_TABLE + "(" +
Downloads._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Downloads.URI + " TEXT, " +
Constants.RETRY_AFTER_X_REDIRECT_COUNT + " INTEGER, " +
                    Downloads.APP_DATA + " TEXT, " +
                    Downloads.NO_INTEGRITY + " BOOLEAN, " +
                    Downloads.FILENAME_HINT + " TEXT, " +
Constants.OTA_UPDATE + " BOOLEAN, " +
Downloads._DATA + " TEXT, " +
                    Downloads.MIMETYPE + " TEXT, " +
                    Downloads.DESTINATION + " INTEGER, " +
Constants.NO_SYSTEM_FILES + " BOOLEAN, " +
                    Downloads.VISIBILITY + " INTEGER, " +
                    Downloads.CONTROL + " INTEGER, " +
                    Downloads.STATUS + " INTEGER, " +
Constants.FAILED_CONNECTIONS + " INTEGER, " +
                    Downloads.LAST_MODIFICATION + " BIGINT, " +
                    Downloads.NOTIFICATION_PACKAGE + " TEXT, " +
                    Downloads.NOTIFICATION_CLASS + " TEXT, " +
                    Downloads.NOTIFICATION_EXTRAS + " TEXT, " +
                    Downloads.COOKIE_DATA + " TEXT, " +
                    Downloads.USER_AGENT + " TEXT, " +
                    Downloads.REFERER + " TEXT, " +
                    Downloads.TOTAL_BYTES + " INTEGER, " +
                    Downloads.CURRENT_BYTES + " INTEGER, " +
Constants.ETAG + " TEXT, " +
Constants.UID + " INTEGER, " +
                    Downloads.OTHER_UID + " INTEGER, " +
                    Downloads.TITLE + " TEXT, " +
                    Downloads.DESCRIPTION + " TEXT, " +
Constants.MEDIA_SCANNED + " BOOLEAN);");
} catch (SQLException ex) {
Log.e(Constants.TAG, "couldn't create table in downloads database");
//Synthetic comment -- @@ -262,12 +262,12 @@

ContentValues filteredValues = new ContentValues();

        copyString(Downloads.URI, values, filteredValues);
        copyString(Downloads.APP_DATA, values, filteredValues);
        copyBoolean(Downloads.NO_INTEGRITY, values, filteredValues);
        copyString(Downloads.FILENAME_HINT, values, filteredValues);
        copyString(Downloads.MIMETYPE, values, filteredValues);
        Integer i = values.getAsInteger(Downloads.DESTINATION);
if (i != null) {
if (getContext().checkCallingPermission(Downloads.PERMISSION_ACCESS_ADVANCED)
!= PackageManager.PERMISSION_GRANTED
//Synthetic comment -- @@ -275,51 +275,51 @@
&& i != Downloads.DESTINATION_CACHE_PARTITION_PURGEABLE) {
throw new SecurityException("unauthorized destination code");
}
            filteredValues.put(Downloads.DESTINATION, i);
if (i != Downloads.DESTINATION_EXTERNAL &&
                    values.getAsInteger(Downloads.VISIBILITY) == null) {
                filteredValues.put(Downloads.VISIBILITY, Downloads.VISIBILITY_HIDDEN);
}
}
        copyInteger(Downloads.VISIBILITY, values, filteredValues);
        copyInteger(Downloads.CONTROL, values, filteredValues);
        filteredValues.put(Downloads.STATUS, Downloads.STATUS_PENDING);
        filteredValues.put(Downloads.LAST_MODIFICATION, System.currentTimeMillis());
        String pckg = values.getAsString(Downloads.NOTIFICATION_PACKAGE);
        String clazz = values.getAsString(Downloads.NOTIFICATION_CLASS);
if (pckg != null && clazz != null) {
int uid = Binder.getCallingUid();
try {
if (uid == 0 ||
getContext().getPackageManager().getApplicationInfo(pckg, 0).uid == uid) {
                    filteredValues.put(Downloads.NOTIFICATION_PACKAGE, pckg);
                    filteredValues.put(Downloads.NOTIFICATION_CLASS, clazz);
}
} catch (PackageManager.NameNotFoundException ex) {
/* ignored for now */
}
}
        copyString(Downloads.NOTIFICATION_EXTRAS, values, filteredValues);
        copyString(Downloads.COOKIE_DATA, values, filteredValues);
        copyString(Downloads.USER_AGENT, values, filteredValues);
        copyString(Downloads.REFERER, values, filteredValues);
if (getContext().checkCallingPermission(Downloads.PERMISSION_ACCESS_ADVANCED)
== PackageManager.PERMISSION_GRANTED) {
            copyInteger(Downloads.OTHER_UID, values, filteredValues);
}
filteredValues.put(Constants.UID, Binder.getCallingUid());
if (Binder.getCallingUid() == 0) {
copyInteger(Constants.UID, values, filteredValues);
}
        copyString(Downloads.TITLE, values, filteredValues);
        copyString(Downloads.DESCRIPTION, values, filteredValues);

if (Constants.LOGVV) {
Log.v(Constants.TAG, "initiating download with UID "
+ filteredValues.getAsInteger(Constants.UID));
            if (filteredValues.containsKey(Downloads.OTHER_UID)) {
Log.v(Constants.TAG, "other UID " +
                        filteredValues.getAsInteger(Downloads.OTHER_UID));
}
}

//Synthetic comment -- @@ -384,7 +384,7 @@
qb.appendWhere(" AND ");
}
qb.appendWhere("( " + Constants.UID + "=" +  Binder.getCallingUid() + " OR "
                    + Downloads.OTHER_UID + "=" +  Binder.getCallingUid() + " )");
emptyWhere = false;

if (projection == null) {
//Synthetic comment -- @@ -481,16 +481,16 @@
ContentValues filteredValues;
if (Binder.getCallingPid() != Process.myPid()) {
filteredValues = new ContentValues();
            copyString(Downloads.APP_DATA, values, filteredValues);
            copyInteger(Downloads.VISIBILITY, values, filteredValues);
            Integer i = values.getAsInteger(Downloads.CONTROL);
if (i != null) {
                filteredValues.put(Downloads.CONTROL, i);
startService = true;
}
            copyInteger(Downloads.CONTROL, values, filteredValues);
            copyString(Downloads.TITLE, values, filteredValues);
            copyString(Downloads.DESCRIPTION, values, filteredValues);
} else {
filteredValues = values;
}
//Synthetic comment -- @@ -515,7 +515,7 @@
}
if (Binder.getCallingPid() != Process.myPid() && Binder.getCallingUid() != 0) {
myWhere += " AND ( " + Constants.UID + "=" +  Binder.getCallingUid() + " OR "
                            + Downloads.OTHER_UID + "=" +  Binder.getCallingUid() + " )";
}
if (filteredValues.size() > 0) {
count = db.update(DB_TABLE, filteredValues, myWhere, whereArgs);
//Synthetic comment -- @@ -571,7 +571,7 @@
}
if (Binder.getCallingPid() != Process.myPid() && Binder.getCallingUid() != 0) {
myWhere += " AND ( " + Constants.UID + "=" +  Binder.getCallingUid() + " OR "
                            + Downloads.OTHER_UID + "=" +  Binder.getCallingUid() + " )";
}
count = db.delete(DB_TABLE, myWhere, whereArgs);
break;
//Synthetic comment -- @@ -665,7 +665,7 @@
throw new FileNotFoundException("couldn't open file");
} else {
ContentValues values = new ContentValues();
            values.put(Downloads.LAST_MODIFICATION, System.currentTimeMillis());
update(uri, values, null, null);
}
return ret;








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadReceiver.java b/src/com/android/providers/downloads/DownloadReceiver.java
//Synthetic comment -- index 73eb094..c0ccad3 100644

//Synthetic comment -- @@ -71,20 +71,22 @@
intent.getData(), null, null, null, null);
if (cursor != null) {
if (cursor.moveToFirst()) {
                    int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int status = cursor.getInt(statusColumn);
                    int visibilityColumn = cursor.getColumnIndexOrThrow(Downloads.VISIBILITY);
int visibility = cursor.getInt(visibilityColumn);
if (Downloads.isStatusCompleted(status)
&& visibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) {
ContentValues values = new ContentValues();
                        values.put(Downloads.VISIBILITY, Downloads.VISIBILITY_VISIBLE);
context.getContentResolver().update(intent.getData(), values, null, null);
}

if (intent.getAction().equals(Constants.ACTION_OPEN)) {
int filenameColumn = cursor.getColumnIndexOrThrow(Downloads._DATA);
                        int mimetypeColumn = cursor.getColumnIndexOrThrow(Downloads.MIMETYPE);
String filename = cursor.getString(filenameColumn);
String mimetype = cursor.getString(mimetypeColumn);
Uri path = Uri.parse(filename);
//Synthetic comment -- @@ -106,13 +108,13 @@
}
} else {
int packageColumn =
                                cursor.getColumnIndexOrThrow(Downloads.NOTIFICATION_PACKAGE);
int classColumn =
                                cursor.getColumnIndexOrThrow(Downloads.NOTIFICATION_CLASS);
String pckg = cursor.getString(packageColumn);
String clazz = cursor.getString(classColumn);
if (pckg != null && clazz != null) {
                            Intent appIntent = new Intent(Downloads.NOTIFICATION_CLICKED_ACTION);
appIntent.setClassName(pckg, clazz);
if (intent.getBooleanExtra("multiple", true)) {
appIntent.setData(Downloads.CONTENT_URI);
//Synthetic comment -- @@ -138,14 +140,15 @@
intent.getData(), null, null, null, null);
if (cursor != null) {
if (cursor.moveToFirst()) {
                    int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int status = cursor.getInt(statusColumn);
                    int visibilityColumn = cursor.getColumnIndexOrThrow(Downloads.VISIBILITY);
int visibility = cursor.getInt(visibilityColumn);
if (Downloads.isStatusCompleted(status)
&& visibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) {
ContentValues values = new ContentValues();
                        values.put(Downloads.VISIBILITY, Downloads.VISIBILITY_VISIBLE);
context.getContentResolver().update(intent.getData(), values, null, null);
}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadService.java b/src/com/android/providers/downloads/DownloadService.java
//Synthetic comment -- index aaa5a68..4a1d2f8 100644

//Synthetic comment -- @@ -516,8 +516,8 @@
private void trimDatabase() {
Cursor cursor = getContentResolver().query(Downloads.CONTENT_URI,
new String[] { Downloads._ID },
                Downloads.STATUS + " >= '200'", null,
                Downloads.LAST_MODIFICATION);
if (cursor == null) {
// This isn't good - if we can't do basic queries in our database, nothing's gonna work
Log.e(Constants.TAG, "null cursor in trimDatabase");
//Synthetic comment -- @@ -546,33 +546,35 @@
private void insertDownload(
Cursor cursor, int arrayPos,
boolean networkAvailable, boolean networkRoaming, long now) {
        int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int failedColumn = cursor.getColumnIndexOrThrow(Constants.FAILED_CONNECTIONS);
int retryRedirect =
cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RETRY_AFTER_X_REDIRECT_COUNT));
DownloadInfo info = new DownloadInfo(
cursor.getInt(cursor.getColumnIndexOrThrow(Downloads._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.URI)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.NO_INTEGRITY)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.FILENAME_HINT)),
cursor.getString(cursor.getColumnIndexOrThrow(Downloads._DATA)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.MIMETYPE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.DESTINATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.VISIBILITY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CONTROL)),
cursor.getInt(statusColumn),
cursor.getInt(failedColumn),
retryRedirect & 0xfffffff,
retryRedirect >> 28,
                cursor.getLong(cursor.getColumnIndexOrThrow(Downloads.LAST_MODIFICATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.NOTIFICATION_PACKAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.NOTIFICATION_CLASS)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.NOTIFICATION_EXTRAS)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.COOKIE_DATA)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.USER_AGENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(Downloads.REFERER)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.TOTAL_BYTES)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CURRENT_BYTES)),
cursor.getString(cursor.getColumnIndexOrThrow(Constants.ETAG)),
cursor.getInt(cursor.getColumnIndexOrThrow(Constants.MEDIA_SCANNED)) == 1);

//Synthetic comment -- @@ -635,7 +637,7 @@

Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId);
ContentValues values = new ContentValues();
                values.put(Downloads.STATUS, Downloads.STATUS_NOT_ACCEPTABLE);
getContentResolver().update(uri, values, null, null);
info.sendIntentIfRequested(uri, this);
return;
//Synthetic comment -- @@ -654,7 +656,7 @@
if (info.mStatus != Downloads.STATUS_RUNNING) {
info.mStatus = Downloads.STATUS_RUNNING;
ContentValues values = new ContentValues();
                    values.put(Downloads.STATUS, info.mStatus);
getContentResolver().update(
ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId),
values, null, null);
//Synthetic comment -- @@ -670,7 +672,7 @@
info.mStatus = Downloads.STATUS_RUNNING_PAUSED;
Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId);
ContentValues values = new ContentValues();
                values.put(Downloads.STATUS, Downloads.STATUS_RUNNING_PAUSED);
getContentResolver().update(uri, values, null, null);
}
}
//Synthetic comment -- @@ -683,17 +685,19 @@
Cursor cursor, int arrayPos,
boolean networkAvailable, boolean networkRoaming, long now) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int failedColumn = cursor.getColumnIndexOrThrow(Constants.FAILED_CONNECTIONS);
info.mId = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads._ID));
        info.mUri = stringFromCursor(info.mUri, cursor, Downloads.URI);
info.mNoIntegrity =
                cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.NO_INTEGRITY)) == 1;
        info.mHint = stringFromCursor(info.mHint, cursor, Downloads.FILENAME_HINT);
info.mFileName = stringFromCursor(info.mFileName, cursor, Downloads._DATA);
        info.mMimeType = stringFromCursor(info.mMimeType, cursor, Downloads.MIMETYPE);
        info.mDestination = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.DESTINATION));
        int newVisibility = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.VISIBILITY));
if (info.mVisibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
&& newVisibility != Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
&& Downloads.isStatusCompleted(info.mStatus)) {
//Synthetic comment -- @@ -701,7 +705,7 @@
}
info.mVisibility = newVisibility;
synchronized (info) {
            info.mControl = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CONTROL));
}
int newStatus = cursor.getInt(statusColumn);
if (!Downloads.isStatusCompleted(info.mStatus) && Downloads.isStatusCompleted(newStatus)) {
//Synthetic comment -- @@ -713,14 +717,18 @@
cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RETRY_AFTER_X_REDIRECT_COUNT));
info.mRetryAfter = retryRedirect & 0xfffffff;
info.mRedirectCount = retryRedirect >> 28;
        info.mLastMod = cursor.getLong(cursor.getColumnIndexOrThrow(Downloads.LAST_MODIFICATION));
        info.mPackage = stringFromCursor(info.mPackage, cursor, Downloads.NOTIFICATION_PACKAGE);
        info.mClass = stringFromCursor(info.mClass, cursor, Downloads.NOTIFICATION_CLASS);
        info.mCookies = stringFromCursor(info.mCookies, cursor, Downloads.COOKIE_DATA);
        info.mUserAgent = stringFromCursor(info.mUserAgent, cursor, Downloads.USER_AGENT);
        info.mReferer = stringFromCursor(info.mReferer, cursor, Downloads.REFERER);
        info.mTotalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.TOTAL_BYTES));
        info.mCurrentBytes = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CURRENT_BYTES));
info.mETag = stringFromCursor(info.mETag, cursor, Constants.ETAG);
info.mMediaScanned =
cursor.getInt(cursor.getColumnIndexOrThrow(Constants.MEDIA_SCANNED)) == 1;
//Synthetic comment -- @@ -736,7 +744,7 @@
}
info.mStatus = Downloads.STATUS_RUNNING;
ContentValues values = new ContentValues();
                values.put(Downloads.STATUS, info.mStatus);
getContentResolver().update(
ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId),
values, null, null);








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 6e72b42..127cc46 100644

//Synthetic comment -- @@ -429,13 +429,13 @@
values.put(Constants.ETAG, headerETag);
}
if (mimeType != null) {
                            values.put(Downloads.MIMETYPE, mimeType);
}
int contentLength = -1;
if (headerContentLength != null) {
contentLength = Integer.parseInt(headerContentLength);
}
                        values.put(Downloads.TOTAL_BYTES, contentLength);
mContext.getContentResolver().update(contentUri, values, null, null);
}

//Synthetic comment -- @@ -470,7 +470,7 @@
bytesRead = entityStream.read(data);
} catch (IOException ex) {
ContentValues values = new ContentValues();
                            values.put(Downloads.CURRENT_BYTES, bytesSoFar);
mContext.getContentResolver().update(contentUri, values, null, null);
if (!mInfo.mNoIntegrity && headerETag == null) {
if (Constants.LOGV) {
//Synthetic comment -- @@ -505,9 +505,9 @@
}
if (bytesRead == -1) { // success
ContentValues values = new ContentValues();
                            values.put(Downloads.CURRENT_BYTES, bytesSoFar);
if (headerContentLength == null) {
                                values.put(Downloads.TOTAL_BYTES, bytesSoFar);
}
mContext.getContentResolver().update(contentUri, values, null, null);
if ((headerContentLength != null)
//Synthetic comment -- @@ -577,7 +577,7 @@
&& now - timeLastNotification
> Constants.MIN_PROGRESS_TIME) {
ContentValues values = new ContentValues();
                            values.put(Downloads.CURRENT_BYTES, bytesSoFar);
mContext.getContentResolver().update(
contentUri, values, null, null);
bytesNotified = bytesSoFar;
//Synthetic comment -- @@ -696,13 +696,13 @@
int status, boolean countRetry, int retryAfter, int redirectCount, boolean gotData,
String filename, String uri, String mimeType) {
ContentValues values = new ContentValues();
        values.put(Downloads.STATUS, status);
values.put(Downloads._DATA, filename);
if (uri != null) {
            values.put(Downloads.URI, uri);
}
        values.put(Downloads.MIMETYPE, mimeType);
        values.put(Downloads.LAST_MODIFICATION, System.currentTimeMillis());
values.put(Constants.RETRY_AFTER_X_REDIRECT_COUNT, retryAfter + (redirectCount << 28));
if (!countRetry) {
values.put(Constants.FAILED_CONNECTIONS, 0);








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index 4ebc818..545235e 100644

//Synthetic comment -- @@ -428,11 +428,13 @@
Downloads.CONTENT_URI,
null,
"( " +
                Downloads.STATUS + " = " + Downloads.STATUS_SUCCESS + " AND " +
                Downloads.DESTINATION + " = " + Downloads.DESTINATION_CACHE_PARTITION_PURGEABLE
+ " )",
null,
                Downloads.LAST_MODIFICATION);
if (cursor == null) {
return false;
}







