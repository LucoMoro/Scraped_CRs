/*Match the official code style guide.

This fixes a number of style violations that weren't caught by automated
tools and brings those files closer to compliance with the official style
guide for this language.*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Constants.java b/src/com/android/providers/downloads/Constants.java
//Synthetic comment -- index cffda04..c4da326 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
public static final String TAG = "DownloadManager";

/** The column that used to be used for the HTTP method of the request */
    public static final String RETRY_AFTER_X_REDIRECT_COUNT = "method";

/** The column that used to be used for the magic OTA update filename */
public static final String OTA_UPDATE = "otaupdate";








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadFileInfo.java b/src/com/android/providers/downloads/DownloadFileInfo.java
//Synthetic comment -- index 29cbd94..ce42388 100644

//Synthetic comment -- @@ -22,13 +22,13 @@
* Stores information about the file in which a download gets saved.
*/
public class DownloadFileInfo {
    String mFileName;
    FileOutputStream mStream;
    int mStatus;

    public DownloadFileInfo(String fileName, FileOutputStream stream, int status) {
        mFileName = fileName;
        mStream = stream;
        mStatus = status;
    }
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadInfo.java b/src/com/android/providers/downloads/DownloadInfo.java
//Synthetic comment -- index e051f41..24bdbfa 100644

//Synthetic comment -- @@ -16,81 +16,81 @@

package com.android.providers.downloads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Downloads;

/**
* Stores information about an individual download.
*/
public class DownloadInfo {
    public int mId;
    public String mUri;
    public boolean mNoIntegrity;
    public String mHint;
    public String mFileName;
    public String mMimeType;
    public int mDestination;
    public int mVisibility;
    public int mControl;
    public int mStatus;
    public int mNumFailed;
    public int mRetryAfter;
    public int mRedirectCount;
    public long mLastMod;
    public String mPackage;
    public String mClass;
    public String mExtras;
    public String mCookies;
    public String mUserAgent;
    public String mReferer;
    public int mTotalBytes;
    public int mCurrentBytes;
    public String mETag;
    public boolean mMediaScanned;

    public volatile boolean mHasActiveThread;

public DownloadInfo(int id, String uri, boolean noIntegrity,
            String hint, String fileName,
            String mimeType, int destination, int visibility, int control,
int status, int numFailed, int retryAfter, int redirectCount, long lastMod,
String pckg, String clazz, String extras, String cookies,
            String userAgent, String referer, int totalBytes, int currentBytes, String eTag,
boolean mediaScanned) {
        mId = id;
        mUri = uri;
        mNoIntegrity = noIntegrity;
        mHint = hint;
        mFileName = fileName;
        mMimeType = mimeType;
        mDestination = destination;
        mVisibility = visibility;
        mControl = control;
        mStatus = status;
        mNumFailed = numFailed;
        mRetryAfter = retryAfter;
        mRedirectCount = redirectCount;
        mLastMod = lastMod;
        mPackage = pckg;
        mClass = clazz;
        mExtras = extras;
        mCookies = cookies;
        mUserAgent = userAgent;
        mReferer = referer;
        mTotalBytes = totalBytes;
        mCurrentBytes = currentBytes;
        mETag = eTag;
        mMediaScanned = mediaScanned;
}

public void sendIntentIfRequested(Uri contentUri, Context context) {
        if (mPackage != null && mClass != null) {
Intent intent = new Intent(Downloads.DOWNLOAD_COMPLETED_ACTION);
            intent.setClassName(mPackage, mClass);
            if (mExtras != null) {
                intent.putExtra(Downloads.NOTIFICATION_EXTRAS, mExtras);
}
// We only send the content: URI, for security reasons. Otherwise, malicious
//     applications would have an easier time spoofing download results by
//Synthetic comment -- @@ -105,12 +105,12 @@
* be called when numFailed > 0.
*/
public long restartTime() {
        if (mRetryAfter > 0) {
            return mLastMod + mRetryAfter;
}
        return mLastMod +
Constants.RETRY_FIRST_DELAY *
                    (1000 + Helpers.sRandom.nextInt(1001)) * (1 << (mNumFailed - 1));
}

/**
//Synthetic comment -- @@ -118,25 +118,25 @@
* should be started.
*/
public boolean isReadyToStart(long now) {
        if (mControl == Downloads.CONTROL_PAUSED) {
// the download is paused, so it's not going to start
return false;
}
        if (mStatus == 0) {
// status hasn't been initialized yet, this is a new download
return true;
}
        if (mStatus == Downloads.STATUS_PENDING) {
// download is explicit marked as ready to start
return true;
}
        if (mStatus == Downloads.STATUS_RUNNING) {
// download was interrupted (process killed, loss of power) while it was running,
//     without a chance to update the database
return true;
}
        if (mStatus == Downloads.STATUS_RUNNING_PAUSED) {
            if (mNumFailed == 0) {
// download is waiting for network connectivity to return before it can resume
return true;
}
//Synthetic comment -- @@ -157,20 +157,20 @@
* by checking the status.
*/
public boolean isReadyToRestart(long now) {
        if (mControl == Downloads.CONTROL_PAUSED) {
// the download is paused, so it's not going to restart
return false;
}
        if (mStatus == 0) {
// download hadn't been initialized yet
return true;
}
        if (mStatus == Downloads.STATUS_PENDING) {
// download is explicit marked as ready to start
return true;
}
        if (mStatus == Downloads.STATUS_RUNNING_PAUSED) {
            if (mNumFailed == 0) {
// download is waiting for network connectivity to return before it can resume
return true;
}
//Synthetic comment -- @@ -187,10 +187,10 @@
* completion.
*/
public boolean hasCompletionNotification() {
        if (!Downloads.isStatusCompleted(mStatus)) {
return false;
}
        if (mVisibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) {
return true;
}
return false;
//Synthetic comment -- @@ -203,7 +203,7 @@
if (!available) {
return false;
}
        if (mDestination == Downloads.DESTINATION_CACHE_PARTITION_NOROAMING) {
return !roaming;
} else {
return true;








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadNotification.java b/src/com/android/providers/downloads/DownloadNotification.java
//Synthetic comment -- index ed17ab7..6837327 100644

//Synthetic comment -- @@ -60,28 +60,28 @@
*
*/
static class NotificationItem {
        int mId;  // This first db _id for the download for the app
        int mTotalCurrent = 0;
        int mTotalTotal = 0;
        int mTitleCount = 0;
        String mPackageName;  // App package name
        String mDescription;
        String[] mTitles = new String[2]; // download titles.

/*
* Add a second download to this notification item.
*/
void addItem(String title, int currentBytes, int totalBytes) {
            mTotalCurrent += currentBytes;
            if (totalBytes <= 0 || mTotalTotal == -1) {
                mTotalTotal = -1;
} else {
                mTotalTotal += totalBytes;
}
            if (mTitleCount < 2) {
                mTitles[mTitleCount] = title;
}
            mTitleCount++;
}
}

//Synthetic comment -- @@ -148,9 +148,9 @@
mNotifications.get(packageName).addItem(title, progress, max);
} else {
NotificationItem item = new NotificationItem();
                item.mId = c.getInt(idColumn);
                item.mPackageName = packageName;
                item.mDescription = c.getString(descColumn);
String className = c.getString(classOwnerColumn);
item.addItem(title, progress, max);
mNotifications.put(packageName, item);
//Synthetic comment -- @@ -171,26 +171,26 @@
RemoteViews expandedView = new RemoteViews(
"com.android.providers.downloads",
R.layout.status_bar_ongoing_event_progress_bar);
            StringBuilder title = new StringBuilder(item.mTitles[0]);
            if (item.mTitleCount > 1) {
title.append(mContext.getString(R.string.notification_filename_separator));
                title.append(item.mTitles[1]);
                n.number = item.mTitleCount;
                if (item.mTitleCount > 2) {
title.append(mContext.getString(R.string.notification_filename_extras,
                            new Object[] { Integer.valueOf(item.mTitleCount - 2) }));
}
} else {
expandedView.setTextViewText(R.id.description, 
                        item.mDescription);
}
expandedView.setTextViewText(R.id.title, title);
expandedView.setProgressBar(R.id.progress_bar, 
                    item.mTotalTotal,
                    item.mTotalCurrent,
                    item.mTotalTotal == -1);
expandedView.setTextViewText(R.id.progress_text, 
                    getDownloadingText(item.mTotalTotal, item.mTotalCurrent));
expandedView.setImageViewResource(R.id.appIcon,
android.R.drawable.stat_sys_download);
n.contentView = expandedView;
//Synthetic comment -- @@ -198,12 +198,12 @@
Intent intent = new Intent(Constants.ACTION_LIST);
intent.setClassName("com.android.providers.downloads",
DownloadReceiver.class.getName());
            intent.setData(Uri.parse(Downloads.CONTENT_URI + "/" + item.mId));
            intent.putExtra("multiple", item.mTitleCount > 1);

n.contentIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

            mNotificationMgr.notify(item.mId, n);

}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadProvider.java b/src/com/android/providers/downloads/DownloadProvider.java
//Synthetic comment -- index d86fdf9..23042b6 100644

//Synthetic comment -- @@ -26,10 +26,10 @@
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
//Synthetic comment -- @@ -40,7 +40,6 @@

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;


//Synthetic comment -- @@ -201,7 +200,7 @@
db.execSQL("CREATE TABLE " + DB_TABLE + "(" +
Downloads._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
Downloads.URI + " TEXT, " +
                    Constants.RETRY_AFTER_X_REDIRECT_COUNT + " INTEGER, " +
Downloads.APP_DATA + " TEXT, " +
Downloads.NO_INTEGRITY + " BOOLEAN, " +
Downloads.FILENAME_HINT + " TEXT, " +








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadReceiver.java b/src/com/android/providers/downloads/DownloadReceiver.java
//Synthetic comment -- index 03a3718..73eb094 100644

//Synthetic comment -- @@ -23,18 +23,15 @@
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Downloads;
import android.util.Config;
import android.util.Log;

import java.io.File;

/**
* Receives system broadcasts (boot, network connectivity)








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadService.java b/src/com/android/providers/downloads/DownloadService.java
//Synthetic comment -- index d4b5f1e..aaa5a68 100644

//Synthetic comment -- @@ -26,20 +26,20 @@
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.drm.mobile1.DrmRawContent;
import android.media.IMediaScannerService;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.provider.Downloads;
import android.util.Config;
import android.util.Log;
//Synthetic comment -- @@ -78,13 +78,13 @@
* The thread that updates the internal download list from the content
* provider.
*/
    private UpdateThread mUpdateThread;

/**
* Whether the internal download list should be updated from the content
* provider.
*/
    private boolean mPendingUpdate;

/**
* The ServiceConnection object that tells us when we're connected to and disconnected from
//Synthetic comment -- @@ -107,7 +107,7 @@
/**
* Array used when extracting strings from content provider
*/
    private CharArrayBuffer mNewChars;

/* ------------ Inner Classes ------------ */

//Synthetic comment -- @@ -247,10 +247,10 @@
*/
private void updateFromProvider() {
synchronized (this) {
            mPendingUpdate = true;
            if (mUpdateThread == null) {
                mUpdateThread = new UpdateThread();
                mUpdateThread.start();
}
}
}
//Synthetic comment -- @@ -269,12 +269,12 @@
long wakeUp = Long.MAX_VALUE;
for (;;) {
synchronized (DownloadService.this) {
                    if (mUpdateThread != this) {
throw new IllegalStateException(
"multiple UpdateThreads in DownloadService");
}
                    if (!mPendingUpdate) {
                        mUpdateThread = null;
if (!keepService) {
stopSelf();
}
//Synthetic comment -- @@ -298,10 +298,10 @@
}
}
oldChars = null;
                        mNewChars = null;
return;
}
                    mPendingUpdate = false;
}
boolean networkAvailable = Helpers.isNetworkAvailable(DownloadService.this);
boolean networkRoaming = Helpers.isNetworkRoaming(DownloadService.this);
//Synthetic comment -- @@ -348,7 +348,7 @@
// We're beyond the end of the cursor but there's still some
//     stuff in the local array, which can only be junk
if (Constants.LOGVV) {
                            int arrayId = ((DownloadInfo) mDownloads.get(arrayPos)).mId;
Log.v(Constants.TAG, "Array update: trimming " +
arrayId + " @ "  + arrayPos);
}
//Synthetic comment -- @@ -383,7 +383,7 @@
cursor.moveToNext();
isAfterLast = cursor.isAfterLast();
} else {
                            int arrayId = mDownloads.get(arrayPos).mId;

if (arrayId < id) {
// The array entry isn't in the cursor
//Synthetic comment -- @@ -549,7 +549,7 @@
int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int failedColumn = cursor.getColumnIndexOrThrow(Constants.FAILED_CONNECTIONS);
int retryRedirect =
                cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RETRY_AFTER_X_REDIRECT_COUNT));
DownloadInfo info = new DownloadInfo(
cursor.getInt(cursor.getColumnIndexOrThrow(Downloads._ID)),
cursor.getString(cursor.getColumnIndexOrThrow(Downloads.URI)),
//Synthetic comment -- @@ -578,38 +578,38 @@

if (Constants.LOGVV) {
Log.v(Constants.TAG, "Service adding new entry");
            Log.v(Constants.TAG, "ID      : " + info.mId);
            Log.v(Constants.TAG, "URI     : " + ((info.mUri != null) ? "yes" : "no"));
            Log.v(Constants.TAG, "NO_INTEG: " + info.mNoIntegrity);
            Log.v(Constants.TAG, "HINT    : " + info.mHint);
            Log.v(Constants.TAG, "FILENAME: " + info.mFileName);
            Log.v(Constants.TAG, "MIMETYPE: " + info.mMimeType);
            Log.v(Constants.TAG, "DESTINAT: " + info.mDestination);
            Log.v(Constants.TAG, "VISIBILI: " + info.mVisibility);
            Log.v(Constants.TAG, "CONTROL : " + info.mControl);
            Log.v(Constants.TAG, "STATUS  : " + info.mStatus);
            Log.v(Constants.TAG, "FAILED_C: " + info.mNumFailed);
            Log.v(Constants.TAG, "RETRY_AF: " + info.mRetryAfter);
            Log.v(Constants.TAG, "REDIRECT: " + info.mRedirectCount);
            Log.v(Constants.TAG, "LAST_MOD: " + info.mLastMod);
            Log.v(Constants.TAG, "PACKAGE : " + info.mPackage);
            Log.v(Constants.TAG, "CLASS   : " + info.mClass);
            Log.v(Constants.TAG, "COOKIES : " + ((info.mCookies != null) ? "yes" : "no"));
            Log.v(Constants.TAG, "AGENT   : " + info.mUserAgent);
            Log.v(Constants.TAG, "REFERER : " + ((info.mReferer != null) ? "yes" : "no"));
            Log.v(Constants.TAG, "TOTAL   : " + info.mTotalBytes);
            Log.v(Constants.TAG, "CURRENT : " + info.mCurrentBytes);
            Log.v(Constants.TAG, "ETAG    : " + info.mETag);
            Log.v(Constants.TAG, "SCANNED : " + info.mMediaScanned);
}

mDownloads.add(arrayPos, info);

        if (info.mStatus == 0
                && (info.mDestination == Downloads.DESTINATION_EXTERNAL
                    || info.mDestination == Downloads.DESTINATION_CACHE_PARTITION_PURGEABLE)
                && info.mMimeType != null
                && !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING.equalsIgnoreCase(info.mMimeType)) {
// Check to see if we are allowed to download this file. Only files
// that can be handled by the platform can be downloaded.
// special case DRM files, which we should always allow downloading.
//Synthetic comment -- @@ -622,18 +622,18 @@
// prevent use from using content: so it's got to be file: or
// nothing

            mimetypeIntent.setDataAndType(Uri.fromParts("file", "", null), info.mMimeType);
List<ResolveInfo> list = getPackageManager().queryIntentActivities(mimetypeIntent,
PackageManager.MATCH_DEFAULT_ONLY);
//Log.i(Constants.TAG, "*** QUERY " + mimetypeIntent + ": " + list);

if (list.size() == 0) {
if (Config.LOGD) {
                    Log.d(Constants.TAG, "no application to handle MIME type " + info.mMimeType);
}
                info.mStatus = Downloads.STATUS_NOT_ACCEPTABLE;

                Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId);
ContentValues values = new ContentValues();
values.put(Downloads.STATUS, Downloads.STATUS_NOT_ACCEPTABLE);
getContentResolver().update(uri, values, null, null);
//Synthetic comment -- @@ -646,29 +646,29 @@
if (info.isReadyToStart(now)) {
if (Constants.LOGV) {
Log.v(Constants.TAG, "Service spawning thread to handle new download " +
                            info.mId);
}
                if (info.mHasActiveThread) {
throw new IllegalStateException("Multiple threads on same download on insert");
}
                if (info.mStatus != Downloads.STATUS_RUNNING) {
                    info.mStatus = Downloads.STATUS_RUNNING;
ContentValues values = new ContentValues();
                    values.put(Downloads.STATUS, info.mStatus);
getContentResolver().update(
                            ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId),
values, null, null);
}
DownloadThread downloader = new DownloadThread(this, info);
                info.mHasActiveThread = true;
downloader.start();
}
} else {
            if (info.mStatus == 0
                    || info.mStatus == Downloads.STATUS_PENDING
                    || info.mStatus == Downloads.STATUS_RUNNING) {
                info.mStatus = Downloads.STATUS_RUNNING_PAUSED;
                Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId);
ContentValues values = new ContentValues();
values.put(Downloads.STATUS, Downloads.STATUS_RUNNING_PAUSED);
getContentResolver().update(uri, values, null, null);
//Synthetic comment -- @@ -685,63 +685,63 @@
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
            mNotifier.mNotificationMgr.cancel(info.mId);
}
        info.mVisibility = newVisibility;
        synchronized (info) {
            info.mControl = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CONTROL));
}
int newStatus = cursor.getInt(statusColumn);
        if (!Downloads.isStatusCompleted(info.mStatus) && Downloads.isStatusCompleted(newStatus)) {
            mNotifier.mNotificationMgr.cancel(info.mId);
}
        info.mStatus = newStatus;
        info.mNumFailed = cursor.getInt(failedColumn);
int retryRedirect =
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

if (info.canUseNetwork(networkAvailable, networkRoaming)) {
if (info.isReadyToRestart(now)) {
if (Constants.LOGV) {
Log.v(Constants.TAG, "Service spawning thread to handle updated download " +
                            info.mId);
}
                if (info.mHasActiveThread) {
throw new IllegalStateException("Multiple threads on same download on update");
}
                info.mStatus = Downloads.STATUS_RUNNING;
ContentValues values = new ContentValues();
                values.put(Downloads.STATUS, info.mStatus);
getContentResolver().update(
                        ContentUris.withAppendedId(Downloads.CONTENT_URI, info.mId),
values, null, null);
DownloadThread downloader = new DownloadThread(this, info);
                info.mHasActiveThread = true;
downloader.start();
}
}
//Synthetic comment -- @@ -756,11 +756,11 @@
if (old == null) {
return cursor.getString(index);
}
        if (mNewChars == null) {
            mNewChars = new CharArrayBuffer(128);
}
        cursor.copyStringToBuffer(index, mNewChars);
        int length = mNewChars.sizeCopied;
if (length != old.length()) {
return cursor.getString(index);
}
//Synthetic comment -- @@ -768,7 +768,7 @@
oldChars = new CharArrayBuffer(length);
}
char[] oldArray = oldChars.data;
        char[] newArray = mNewChars.data;
old.getChars(0, length, oldArray, 0);
for (int i = length - 1; i >= 0; --i) {
if (oldArray[i] != newArray[i]) {
//Synthetic comment -- @@ -783,12 +783,12 @@
*/
private void deleteDownload(int arrayPos) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        if (info.mStatus == Downloads.STATUS_RUNNING) {
            info.mStatus = Downloads.STATUS_CANCELED;
        } else if (info.mDestination != Downloads.DESTINATION_EXTERNAL && info.mFileName != null) {
            new File(info.mFileName).delete();
}
        mNotifier.mNotificationMgr.cancel(info.mId);

mDownloads.remove(arrayPos);
}
//Synthetic comment -- @@ -802,13 +802,13 @@
*/
private long nextAction(int arrayPos, long now) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        if (Downloads.isStatusCompleted(info.mStatus)) {
return -1;
}
        if (info.mStatus != Downloads.STATUS_RUNNING_PAUSED) {
return 0;
}
        if (info.mNumFailed == 0) {
return 0;
}
long when = info.restartTime();
//Synthetic comment -- @@ -831,10 +831,10 @@
*/
private boolean shouldScanFile(int arrayPos) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        return !info.mMediaScanned
                && info.mDestination == Downloads.DESTINATION_EXTERNAL
                && Downloads.isStatusSuccess(info.mStatus)
                && !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING.equalsIgnoreCase(info.mMimeType);
}

/**
//Synthetic comment -- @@ -854,9 +854,9 @@
if (mMediaScannerService != null) {
try {
if (Constants.LOGV) {
                        Log.v(Constants.TAG, "Scanning file " + info.mFileName);
}
                    mMediaScannerService.scanFile(info.mFileName, info.mMimeType);
if (cursor != null) {
ContentValues values = new ContentValues();
values.put(Constants.MEDIA_SCANNED, 1);
//Synthetic comment -- @@ -868,7 +868,7 @@
return true;
} catch (RemoteException e) {
if (Config.LOGD) {
                        Log.d(Constants.TAG, "Failed to scan file " + info.mFileName);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 923e36d..6e72b42 100644

//Synthetic comment -- @@ -16,23 +16,19 @@

package com.android.providers.downloads;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.drm.mobile1.DrmRawContent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.FileUtils;
import android.os.PowerManager;
import android.os.Process;
//Synthetic comment -- @@ -44,10 +40,10 @@
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
* Runs an actual download
//Synthetic comment -- @@ -66,7 +62,7 @@
* Returns the user agent provided by the initiating app, or use the default one
*/
private String userAgent() {
        String userAgent = mInfo.mUserAgent;
if (userAgent != null) {
}
if (userAgent == null) {
//Synthetic comment -- @@ -84,15 +80,15 @@
int finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
boolean countRetry = false;
int retryAfter = 0;
        int redirectCount = mInfo.mRedirectCount;
String newUri = null;
boolean gotData = false;
String filename = null;
        String mimeType = mInfo.mMimeType;
FileOutputStream stream = null;
AndroidHttpClient client = null;
PowerManager.WakeLock wakeLock = null;
        Uri contentUri = Uri.parse(Downloads.CONTENT_URI + "/" + mInfo.mId);

try {
boolean continuingDownload = false;
//Synthetic comment -- @@ -111,12 +107,12 @@
wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.TAG);
wakeLock.acquire();

            filename = mInfo.mFileName;
if (filename != null) {
if (!Helpers.isFilenameValid(filename)) {
finalStatus = Downloads.STATUS_FILE_ERROR;
notifyDownloadCompleted(
                            finalStatus, false, 0, 0, false, filename, null, mInfo.mMimeType);
return;
}
// We're resuming a download that got interrupted
//Synthetic comment -- @@ -127,7 +123,7 @@
// The download hadn't actually started, we can restart from scratch
f.delete();
filename = null;
                    } else if (mInfo.mETag == null && !mInfo.mNoIntegrity) {
// Tough luck, that's not a resumable download
if (Config.LOGD) {
Log.d(Constants.TAG,
//Synthetic comment -- @@ -136,16 +132,16 @@
f.delete();
finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
notifyDownloadCompleted(
                                finalStatus, false, 0, 0, false, filename, null, mInfo.mMimeType);
return;
} else {
// All right, we'll be able to resume this download
stream = new FileOutputStream(filename, true);
bytesSoFar = (int) fileLength;
                        if (mInfo.mTotalBytes != -1) {
                            headerContentLength = Integer.toString(mInfo.mTotalBytes);
}
                        headerETag = mInfo.mETag;
continuingDownload = true;
}
}
//Synthetic comment -- @@ -158,7 +154,7 @@

client = AndroidHttpClient.newInstance(userAgent());

            if (stream != null && mInfo.mDestination == Downloads.DESTINATION_EXTERNAL
&& !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
.equalsIgnoreCase(mimeType)) {
try {
//Synthetic comment -- @@ -181,17 +177,17 @@
http_request_loop:
while (true) {
// Prepares the request and fires it.
                HttpGet request = new HttpGet(mInfo.mUri);

if (Constants.LOGV) {
                    Log.v(Constants.TAG, "initiating download for " + mInfo.mUri);
}

                if (mInfo.mCookies != null) {
                    request.addHeader("Cookie", mInfo.mCookies);
}
                if (mInfo.mReferer != null) {
                    request.addHeader("Referer", mInfo.mReferer);
}
if (continuingDownload) {
if (headerETag != null) {
//Synthetic comment -- @@ -206,10 +202,10 @@
} catch (IllegalArgumentException ex) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "Arg exception trying to execute request for " +
                                mInfo.mUri + " : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "Arg exception trying to execute request for " +
                                mInfo.mId + " : " +  ex);
}
finalStatus = Downloads.STATUS_BAD_REQUEST;
request.abort();
//Synthetic comment -- @@ -217,16 +213,16 @@
} catch (IOException ex) {
if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                    } else if (mInfo.mNumFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
Log.d(Constants.TAG, "IOException trying to execute request for " +
                                    mInfo.mUri + " : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "IOException trying to execute request for " +
                                    mInfo.mId + " : " + ex);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -235,7 +231,7 @@
}

int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 503 && mInfo.mNumFailed < Constants.MAX_RETRIES) {
if (Constants.LOGVV) {
Log.v(Constants.TAG, "got HTTP response code 503");
}
//Synthetic comment -- @@ -256,7 +252,7 @@
} else if (retryAfter > Constants.MAX_RETRY_AFTER) {
retryAfter = Constants.MAX_RETRY_AFTER;
}
                               retryAfter += Helpers.sRandom.nextInt(Constants.MIN_RETRY_AFTER + 1);
retryAfter *= 1000;
}
} catch (NumberFormatException ex) {
//Synthetic comment -- @@ -275,10 +271,10 @@
}
if (redirectCount >= Constants.MAX_REDIRECTS) {
if (Constants.LOGV) {
                            Log.d(Constants.TAG, "too many redirects for download " + mInfo.mId +
                                    " at " + mInfo.mUri);
} else if (Config.LOGD) {
                            Log.d(Constants.TAG, "too many redirects for download " + mInfo.mId);
}
finalStatus = Downloads.STATUS_TOO_MANY_REDIRECTS;
request.abort();
//Synthetic comment -- @@ -289,7 +285,26 @@
if (Constants.LOGVV) {
Log.v(Constants.TAG, "Location :" + header.getValue());
}
                        try {
                            newUri = new URI(mInfo.mUri).
                                    resolve(new URI(header.getValue())).
                                    toString();
                        } catch(URISyntaxException ex) {
                            if (Constants.LOGV) {
                                Log.d(Constants.TAG,
                                        "Couldn't resolve redirect URI " +
                                        header.getValue() +
                                        " for " +
                                        mInfo.mUri);
                            } else if (Config.LOGD) {
                                Log.d(Constants.TAG,
                                        "Couldn't resolve redirect URI for download " +
                                        mInfo.mId);
                            }
                            finalStatus = Downloads.STATUS_BAD_REQUEST;
                            request.abort();
                            break http_request_loop;
                        }
++redirectCount;
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
request.abort();
//Synthetic comment -- @@ -299,10 +314,10 @@
if ((!continuingDownload && statusCode != Downloads.STATUS_SUCCESS)
|| (continuingDownload && statusCode != 206)) {
if (Constants.LOGV) {
                        Log.d(Constants.TAG, "http error " + statusCode + " for " + mInfo.mUri);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "http error " + statusCode + " for download " +
                                mInfo.mId);
}
if (Downloads.isStatusError(statusCode)) {
finalStatus = statusCode;
//Synthetic comment -- @@ -318,7 +333,7 @@
} else {
// Handles the response, saves the file
if (Constants.LOGV) {
                        Log.v(Constants.TAG, "received response for " + mInfo.mUri);
}

if (!continuingDownload) {
//Synthetic comment -- @@ -375,7 +390,7 @@
Log.v(Constants.TAG, "Transfer-Encoding: " + headerTransferEncoding);
}

                        if (!mInfo.mNoIntegrity && headerContentLength == null &&
(headerTransferEncoding == null
|| !headerTransferEncoding.equalsIgnoreCase("chunked"))
) {
//Synthetic comment -- @@ -389,23 +404,23 @@

DownloadFileInfo fileInfo = Helpers.generateSaveFile(
mContext,
                                mInfo.mUri,
                                mInfo.mHint,
headerContentDisposition,
headerContentLocation,
mimeType,
                                mInfo.mDestination,
(headerContentLength != null) ?
Integer.parseInt(headerContentLength) : 0);
                        if (fileInfo.mFileName == null) {
                            finalStatus = fileInfo.mStatus;
request.abort();
break http_request_loop;
}
                        filename = fileInfo.mFileName;
                        stream = fileInfo.mStream;
if (Constants.LOGV) {
                            Log.v(Constants.TAG, "writing " + mInfo.mUri + " to " + filename);
}

ContentValues values = new ContentValues();
//Synthetic comment -- @@ -430,16 +445,19 @@
} catch (IOException ex) {
if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                        } else if (mInfo.mNumFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
                                Log.d(Constants.TAG,
                                        "IOException getting entity for " +
                                        mInfo.mUri +
                                        " : " +
                                        ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "IOException getting entity for download " +
                                        mInfo.mId + " : " + ex);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -454,13 +472,13 @@
ContentValues values = new ContentValues();
values.put(Downloads.CURRENT_BYTES, bytesSoFar);
mContext.getContentResolver().update(contentUri, values, null, null);
                            if (!mInfo.mNoIntegrity && headerETag == null) {
if (Constants.LOGV) {
                                    Log.v(Constants.TAG, "download IOException for " + mInfo.mUri +
" : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "download IOException for download " +
                                            mInfo.mId + " : " + ex);
}
if (Config.LOGD) {
Log.d(Constants.TAG,
//Synthetic comment -- @@ -469,16 +487,16 @@
finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
} else if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                            } else if (mInfo.mNumFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
                                    Log.v(Constants.TAG, "download IOException for " + mInfo.mUri +
" : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "download IOException for download " +
                                            mInfo.mId + " : " + ex);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -495,26 +513,26 @@
if ((headerContentLength != null)
&& (bytesSoFar
!= Integer.parseInt(headerContentLength))) {
                                if (!mInfo.mNoIntegrity && headerETag == null) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "mismatched content length " +
                                                mInfo.mUri);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "mismatched content length for " +
                                                mInfo.mId);
}
finalStatus = Downloads.STATUS_LENGTH_REQUIRED;
} else if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                                } else if (mInfo.mNumFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
                                        Log.v(Constants.TAG, "closed socket for " + mInfo.mUri);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "closed socket for download " +
                                                mInfo.mId);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -529,7 +547,7 @@
stream = new FileOutputStream(filename, true);
}
stream.write(data, 0, bytesRead);
                                if (mInfo.mDestination == Downloads.DESTINATION_EXTERNAL
&& !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
.equalsIgnoreCase(mimeType)) {
try {
//Synthetic comment -- @@ -567,30 +585,30 @@
}

if (Constants.LOGVV) {
                            Log.v(Constants.TAG, "downloaded " + bytesSoFar + " for " + mInfo.mUri);
}
                        synchronized (mInfo) {
                            if (mInfo.mControl == Downloads.CONTROL_PAUSED) {
if (Constants.LOGV) {
                                    Log.v(Constants.TAG, "paused " + mInfo.mUri);
}
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
request.abort();
break http_request_loop;
}
}
                        if (mInfo.mStatus == Downloads.STATUS_CANCELED) {
if (Constants.LOGV) {
                                Log.d(Constants.TAG, "canceled " + mInfo.mUri);
} else if (Config.LOGD) {
                                // Log.d(Constants.TAG, "canceled id " + mInfo.mId);
}
finalStatus = Downloads.STATUS_CANCELED;
break http_request_loop;
}
}
if (Constants.LOGV) {
                        Log.v(Constants.TAG, "download completed for " + mInfo.mUri);
}
finalStatus = Downloads.STATUS_SUCCESS;
}
//Synthetic comment -- @@ -602,16 +620,16 @@
}
finalStatus = Downloads.STATUS_FILE_ERROR;
// falls through to the code that reports an error
        } catch (RuntimeException ex) { //sometimes the socket code throws unchecked exceptions
if (Constants.LOGV) {
                Log.d(Constants.TAG, "Exception for " + mInfo.mUri, ex);
} else if (Config.LOGD) {
                Log.d(Constants.TAG, "Exception for id " + mInfo.mId, ex);
}
finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
// falls through to the code that reports an error
} finally {
            mInfo.mHasActiveThread = false;
if (wakeLock != null) {
wakeLock.release();
wakeLock = null;
//Synthetic comment -- @@ -685,17 +703,17 @@
}
values.put(Downloads.MIMETYPE, mimeType);
values.put(Downloads.LAST_MODIFICATION, System.currentTimeMillis());
        values.put(Constants.RETRY_AFTER_X_REDIRECT_COUNT, retryAfter + (redirectCount << 28));
if (!countRetry) {
values.put(Constants.FAILED_CONNECTIONS, 0);
} else if (gotData) {
values.put(Constants.FAILED_CONNECTIONS, 1);
} else {
            values.put(Constants.FAILED_CONNECTIONS, mInfo.mNumFailed + 1);
}

mContext.getContentResolver().update(
                ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), values, null, null);
}

/**
//Synthetic comment -- @@ -703,7 +721,7 @@
* download completed even if it's not actively watching the cursor.
*/
private void notifyThroughIntent() {
        Uri uri = Uri.parse(Downloads.CONTENT_URI + "/" + mInfo.mId);
mInfo.sendIntentIfRequested(uri, mContext);
}









//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index 89a5731..4ebc818 100644

//Synthetic comment -- @@ -40,16 +40,16 @@
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Some helper functions for the download manager
*/
public class Helpers {

    public static Random sRandom = new Random(SystemClock.uptimeMillis());

/** Regex used to parse content-disposition headers */
private static final Pattern CONTENT_DISPOSITION_PATTERN =
//Synthetic comment -- @@ -412,7 +412,7 @@
if (Constants.LOGVV) {
Log.v(Constants.TAG, "file with sequence number " + sequence + " exists");
}
                sequence += sRandom.nextInt(magnitude) + 1;
}
}
return null;
//Synthetic comment -- @@ -756,7 +756,7 @@
// quoted strings
if (chars[mOffset] == '\'') {
++mOffset;
                while (mOffset < chars.length) {
if (chars[mOffset] == '\'') {
if (mOffset + 1 < chars.length && chars[mOffset + 1] == '\'') {
++mOffset;







