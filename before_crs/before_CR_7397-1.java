/*Match the official code style guide.

This fixes a number of style violations that weren't caught by automated
tools and brings those files closer to compliance with the official style
guide for this language.*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Constants.java b/src/com/android/providers/downloads/Constants.java
//Synthetic comment -- index cffda04..c4da326 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
public static final String TAG = "DownloadManager";

/** The column that used to be used for the HTTP method of the request */
    public static final String RETRY_AFTER___REDIRECT_COUNT = "method";

/** The column that used to be used for the magic OTA update filename */
public static final String OTA_UPDATE = "otaupdate";








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadFileInfo.java b/src/com/android/providers/downloads/DownloadFileInfo.java
//Synthetic comment -- index 29cbd94..ce42388 100644

//Synthetic comment -- @@ -22,13 +22,13 @@
* Stores information about the file in which a download gets saved.
*/
public class DownloadFileInfo {
    public DownloadFileInfo(String filename, FileOutputStream stream, int status) {
        this.filename = filename;
        this.stream = stream;
        this.status = status;
    }

    String filename;
    FileOutputStream stream;
    int status;
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadInfo.java b/src/com/android/providers/downloads/DownloadInfo.java
//Synthetic comment -- index e051f41..24bdbfa 100644

//Synthetic comment -- @@ -16,81 +16,81 @@

package com.android.providers.downloads;

import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.provider.Downloads;

/**
* Stores information about an individual download.
*/
public class DownloadInfo {
    public int id;
    public String uri;
    public boolean noIntegrity;
    public String hint;
    public String filename;
    public String mimetype;
    public int destination;
    public int visibility;
    public int control;
    public int status;
    public int numFailed;
    public int retryAfter;
    public int redirectCount;
    public long lastMod;
    public String pckg;
    public String clazz;
    public String extras;
    public String cookies;
    public String userAgent;
    public String referer;
    public int totalBytes;
    public int currentBytes;
    public String etag;
    public boolean mediaScanned;

    public volatile boolean hasActiveThread;

public DownloadInfo(int id, String uri, boolean noIntegrity,
            String hint, String filename,
            String mimetype, int destination, int visibility, int control,
int status, int numFailed, int retryAfter, int redirectCount, long lastMod,
String pckg, String clazz, String extras, String cookies,
            String userAgent, String referer, int totalBytes, int currentBytes, String etag,
boolean mediaScanned) {
        this.id = id;
        this.uri = uri;
        this.noIntegrity = noIntegrity;
        this.hint = hint;
        this.filename = filename;
        this.mimetype = mimetype;
        this.destination = destination;
        this.visibility = visibility;
        this.control = control;
        this.status = status;
        this.numFailed = numFailed;
        this.retryAfter = retryAfter;
        this.redirectCount = redirectCount;
        this.lastMod = lastMod;
        this.pckg = pckg;
        this.clazz = clazz;
        this.extras = extras;
        this.cookies = cookies;
        this.userAgent = userAgent;
        this.referer = referer;
        this.totalBytes = totalBytes;
        this.currentBytes = currentBytes;
        this.etag = etag;
        this.mediaScanned = mediaScanned;
}

public void sendIntentIfRequested(Uri contentUri, Context context) {
        if (pckg != null && clazz != null) {
Intent intent = new Intent(Downloads.DOWNLOAD_COMPLETED_ACTION);
            intent.setClassName(pckg, clazz);
            if (extras != null) {
                intent.putExtra(Downloads.NOTIFICATION_EXTRAS, extras);
}
// We only send the content: URI, for security reasons. Otherwise, malicious
//     applications would have an easier time spoofing download results by
//Synthetic comment -- @@ -105,12 +105,12 @@
* be called when numFailed > 0.
*/
public long restartTime() {
        if (retryAfter > 0) {
            return lastMod + retryAfter;
}
        return lastMod +
Constants.RETRY_FIRST_DELAY *
                    (1000 + Helpers.rnd.nextInt(1001)) * (1 << (numFailed - 1));
}

/**
//Synthetic comment -- @@ -118,25 +118,25 @@
* should be started.
*/
public boolean isReadyToStart(long now) {
        if (control == Downloads.CONTROL_PAUSED) {
// the download is paused, so it's not going to start
return false;
}
        if (status == 0) {
// status hasn't been initialized yet, this is a new download
return true;
}
        if (status == Downloads.STATUS_PENDING) {
// download is explicit marked as ready to start
return true;
}
        if (status == Downloads.STATUS_RUNNING) {
// download was interrupted (process killed, loss of power) while it was running,
//     without a chance to update the database
return true;
}
        if (status == Downloads.STATUS_RUNNING_PAUSED) {
            if (numFailed == 0) {
// download is waiting for network connectivity to return before it can resume
return true;
}
//Synthetic comment -- @@ -157,20 +157,20 @@
* by checking the status.
*/
public boolean isReadyToRestart(long now) {
        if (control == Downloads.CONTROL_PAUSED) {
// the download is paused, so it's not going to restart
return false;
}
        if (status == 0) {
// download hadn't been initialized yet
return true;
}
        if (status == Downloads.STATUS_PENDING) {
// download is explicit marked as ready to start
return true;
}
        if (status == Downloads.STATUS_RUNNING_PAUSED) {
            if (numFailed == 0) {
// download is waiting for network connectivity to return before it can resume
return true;
}
//Synthetic comment -- @@ -187,10 +187,10 @@
* completion.
*/
public boolean hasCompletionNotification() {
        if (!Downloads.isStatusCompleted(status)) {
return false;
}
        if (visibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) {
return true;
}
return false;
//Synthetic comment -- @@ -203,7 +203,7 @@
if (!available) {
return false;
}
        if (destination == Downloads.DESTINATION_CACHE_PARTITION_NOROAMING) {
return !roaming;
} else {
return true;








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadNotification.java b/src/com/android/providers/downloads/DownloadNotification.java
//Synthetic comment -- index ed17ab7..6837327 100644

//Synthetic comment -- @@ -60,28 +60,28 @@
*
*/
static class NotificationItem {
        int id;  // This first db _id for the download for the app
        int totalCurrent = 0;
        int totalTotal = 0;
        int titleCount = 0;
        String packageName;  // App package name
        String description;
        String[] titles = new String[2]; // download titles.

/*
* Add a second download to this notification item.
*/
void addItem(String title, int currentBytes, int totalBytes) {
            totalCurrent += currentBytes;
            if (totalBytes <= 0 || totalTotal == -1) {
                totalTotal = -1;
} else {
                totalTotal += totalBytes;
}
            if (titleCount < 2) {
                titles[titleCount] = title;
}
            titleCount++;
}
}

//Synthetic comment -- @@ -148,9 +148,9 @@
mNotifications.get(packageName).addItem(title, progress, max);
} else {
NotificationItem item = new NotificationItem();
                item.id = c.getInt(idColumn);
                item.packageName = packageName;
                item.description = c.getString(descColumn);
String className = c.getString(classOwnerColumn);
item.addItem(title, progress, max);
mNotifications.put(packageName, item);
//Synthetic comment -- @@ -171,26 +171,26 @@
RemoteViews expandedView = new RemoteViews(
"com.android.providers.downloads",
R.layout.status_bar_ongoing_event_progress_bar);
            StringBuilder title = new StringBuilder(item.titles[0]);
            if (item.titleCount > 1) {
title.append(mContext.getString(R.string.notification_filename_separator));
                title.append(item.titles[1]);
                n.number = item.titleCount;
                if (item.titleCount > 2) {
title.append(mContext.getString(R.string.notification_filename_extras,
                            new Object[] { Integer.valueOf(item.titleCount - 2) }));
}
} else {
expandedView.setTextViewText(R.id.description, 
                        item.description);
}
expandedView.setTextViewText(R.id.title, title);
expandedView.setProgressBar(R.id.progress_bar, 
                    item.totalTotal, 
                    item.totalCurrent, 
                    item.totalTotal == -1);
expandedView.setTextViewText(R.id.progress_text, 
                    getDownloadingText(item.totalTotal, item.totalCurrent));
expandedView.setImageViewResource(R.id.appIcon,
android.R.drawable.stat_sys_download);
n.contentView = expandedView;
//Synthetic comment -- @@ -198,12 +198,12 @@
Intent intent = new Intent(Constants.ACTION_LIST);
intent.setClassName("com.android.providers.downloads",
DownloadReceiver.class.getName());
            intent.setData(Uri.parse(Downloads.CONTENT_URI + "/" + item.id));
            intent.putExtra("multiple", item.titleCount > 1);

n.contentIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

            mNotificationMgr.notify(item.id, n);

}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadProvider.java b/src/com/android/providers/downloads/DownloadProvider.java
//Synthetic comment -- index d86fdf9..23042b6 100644

//Synthetic comment -- @@ -26,10 +26,10 @@
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.SQLException;
import android.net.Uri;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
//Synthetic comment -- @@ -40,7 +40,6 @@

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;


//Synthetic comment -- @@ -201,7 +200,7 @@
db.execSQL("CREATE TABLE " + DB_TABLE + "(" +
Downloads._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
Downloads.URI + " TEXT, " +
                    Constants.RETRY_AFTER___REDIRECT_COUNT + " INTEGER, " +
Downloads.APP_DATA + " TEXT, " +
Downloads.NO_INTEGRITY + " BOOLEAN, " +
Downloads.FILENAME_HINT + " TEXT, " +








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadReceiver.java b/src/com/android/providers/downloads/DownloadReceiver.java
//Synthetic comment -- index 03a3718..73eb094 100644

//Synthetic comment -- @@ -23,18 +23,15 @@
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.provider.Downloads;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Config;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
* Receives system broadcasts (boot, network connectivity)








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadService.java b/src/com/android/providers/downloads/DownloadService.java
//Synthetic comment -- index d4b5f1e..aaa5a68 100644

//Synthetic comment -- @@ -26,20 +26,20 @@
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CharArrayBuffer;
import android.drm.mobile1.DrmRawContent;
import android.media.IMediaScannerService;
import android.net.Uri;
import android.os.RemoteException;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.provider.Downloads;
import android.util.Config;
import android.util.Log;
//Synthetic comment -- @@ -78,13 +78,13 @@
* The thread that updates the internal download list from the content
* provider.
*/
    private UpdateThread updateThread;

/**
* Whether the internal download list should be updated from the content
* provider.
*/
    private boolean pendingUpdate;

/**
* The ServiceConnection object that tells us when we're connected to and disconnected from
//Synthetic comment -- @@ -107,7 +107,7 @@
/**
* Array used when extracting strings from content provider
*/
    private CharArrayBuffer newChars;

/* ------------ Inner Classes ------------ */

//Synthetic comment -- @@ -247,10 +247,10 @@
*/
private void updateFromProvider() {
synchronized (this) {
            pendingUpdate = true;
            if (updateThread == null) {
                updateThread = new UpdateThread();
                updateThread.start();
}
}
}
//Synthetic comment -- @@ -269,12 +269,12 @@
long wakeUp = Long.MAX_VALUE;
for (;;) {
synchronized (DownloadService.this) {
                    if (updateThread != this) {
throw new IllegalStateException(
"multiple UpdateThreads in DownloadService");
}
                    if (!pendingUpdate) {
                        updateThread = null;
if (!keepService) {
stopSelf();
}
//Synthetic comment -- @@ -298,10 +298,10 @@
}
}
oldChars = null;
                        newChars = null;
return;
}
                    pendingUpdate = false;
}
boolean networkAvailable = Helpers.isNetworkAvailable(DownloadService.this);
boolean networkRoaming = Helpers.isNetworkRoaming(DownloadService.this);
//Synthetic comment -- @@ -348,7 +348,7 @@
// We're beyond the end of the cursor but there's still some
//     stuff in the local array, which can only be junk
if (Constants.LOGVV) {
                            int arrayId = ((DownloadInfo) mDownloads.get(arrayPos)).id;
Log.v(Constants.TAG, "Array update: trimming " +
arrayId + " @ "  + arrayPos);
}
//Synthetic comment -- @@ -383,7 +383,7 @@
cursor.moveToNext();
isAfterLast = cursor.isAfterLast();
} else {
                            int arrayId = mDownloads.get(arrayPos).id;

if (arrayId < id) {
// The array entry isn't in the cursor
//Synthetic comment -- @@ -549,7 +549,7 @@
int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int failedColumn = cursor.getColumnIndexOrThrow(Constants.FAILED_CONNECTIONS);
int retryRedirect =
                cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RETRY_AFTER___REDIRECT_COUNT));
DownloadInfo info = new DownloadInfo(
cursor.getInt(cursor.getColumnIndexOrThrow(Downloads._ID)),
cursor.getString(cursor.getColumnIndexOrThrow(Downloads.URI)),
//Synthetic comment -- @@ -578,38 +578,38 @@

if (Constants.LOGVV) {
Log.v(Constants.TAG, "Service adding new entry");
            Log.v(Constants.TAG, "ID      : " + info.id);
            Log.v(Constants.TAG, "URI     : " + ((info.uri != null) ? "yes" : "no"));
            Log.v(Constants.TAG, "NO_INTEG: " + info.noIntegrity);
            Log.v(Constants.TAG, "HINT    : " + info.hint);
            Log.v(Constants.TAG, "FILENAME: " + info.filename);
            Log.v(Constants.TAG, "MIMETYPE: " + info.mimetype);
            Log.v(Constants.TAG, "DESTINAT: " + info.destination);
            Log.v(Constants.TAG, "VISIBILI: " + info.visibility);
            Log.v(Constants.TAG, "CONTROL : " + info.control);
            Log.v(Constants.TAG, "STATUS  : " + info.status);
            Log.v(Constants.TAG, "FAILED_C: " + info.numFailed);
            Log.v(Constants.TAG, "RETRY_AF: " + info.retryAfter);
            Log.v(Constants.TAG, "REDIRECT: " + info.redirectCount);
            Log.v(Constants.TAG, "LAST_MOD: " + info.lastMod);
            Log.v(Constants.TAG, "PACKAGE : " + info.pckg);
            Log.v(Constants.TAG, "CLASS   : " + info.clazz);
            Log.v(Constants.TAG, "COOKIES : " + ((info.cookies != null) ? "yes" : "no"));
            Log.v(Constants.TAG, "AGENT   : " + info.userAgent);
            Log.v(Constants.TAG, "REFERER : " + ((info.referer != null) ? "yes" : "no"));
            Log.v(Constants.TAG, "TOTAL   : " + info.totalBytes);
            Log.v(Constants.TAG, "CURRENT : " + info.currentBytes);
            Log.v(Constants.TAG, "ETAG    : " + info.etag);
            Log.v(Constants.TAG, "SCANNED : " + info.mediaScanned);
}

mDownloads.add(arrayPos, info);

        if (info.status == 0
                && (info.destination == Downloads.DESTINATION_EXTERNAL
                    || info.destination == Downloads.DESTINATION_CACHE_PARTITION_PURGEABLE)
                && info.mimetype != null
                && !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING.equalsIgnoreCase(info.mimetype)) {
// Check to see if we are allowed to download this file. Only files
// that can be handled by the platform can be downloaded.
// special case DRM files, which we should always allow downloading.
//Synthetic comment -- @@ -622,18 +622,18 @@
// prevent use from using content: so it's got to be file: or
// nothing

            mimetypeIntent.setDataAndType(Uri.fromParts("file", "", null), info.mimetype);
List<ResolveInfo> list = getPackageManager().queryIntentActivities(mimetypeIntent,
PackageManager.MATCH_DEFAULT_ONLY);
//Log.i(Constants.TAG, "*** QUERY " + mimetypeIntent + ": " + list);

if (list.size() == 0) {
if (Config.LOGD) {
                    Log.d(Constants.TAG, "no application to handle MIME type " + info.mimetype);
}
                info.status = Downloads.STATUS_NOT_ACCEPTABLE;

                Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, info.id);
ContentValues values = new ContentValues();
values.put(Downloads.STATUS, Downloads.STATUS_NOT_ACCEPTABLE);
getContentResolver().update(uri, values, null, null);
//Synthetic comment -- @@ -646,29 +646,29 @@
if (info.isReadyToStart(now)) {
if (Constants.LOGV) {
Log.v(Constants.TAG, "Service spawning thread to handle new download " +
                            info.id);
}
                if (info.hasActiveThread) {
throw new IllegalStateException("Multiple threads on same download on insert");
}
                if (info.status != Downloads.STATUS_RUNNING) {
                    info.status = Downloads.STATUS_RUNNING;
ContentValues values = new ContentValues();
                    values.put(Downloads.STATUS, info.status);
getContentResolver().update(
                            ContentUris.withAppendedId(Downloads.CONTENT_URI, info.id),
values, null, null);
}
DownloadThread downloader = new DownloadThread(this, info);
                info.hasActiveThread = true;
downloader.start();
}
} else {
            if (info.status == 0
                    || info.status == Downloads.STATUS_PENDING
                    || info.status == Downloads.STATUS_RUNNING) {
                info.status = Downloads.STATUS_RUNNING_PAUSED;
                Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, info.id);
ContentValues values = new ContentValues();
values.put(Downloads.STATUS, Downloads.STATUS_RUNNING_PAUSED);
getContentResolver().update(uri, values, null, null);
//Synthetic comment -- @@ -685,63 +685,63 @@
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
int statusColumn = cursor.getColumnIndexOrThrow(Downloads.STATUS);
int failedColumn = cursor.getColumnIndexOrThrow(Constants.FAILED_CONNECTIONS);
        info.id = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads._ID));
        info.uri = stringFromCursor(info.uri, cursor, Downloads.URI);
        info.noIntegrity =
cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.NO_INTEGRITY)) == 1;
        info.hint = stringFromCursor(info.hint, cursor, Downloads.FILENAME_HINT);
        info.filename = stringFromCursor(info.filename, cursor, Downloads._DATA);
        info.mimetype = stringFromCursor(info.mimetype, cursor, Downloads.MIMETYPE);
        info.destination = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.DESTINATION));
int newVisibility = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.VISIBILITY));
        if (info.visibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
&& newVisibility != Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                && Downloads.isStatusCompleted(info.status)) {
            mNotifier.mNotificationMgr.cancel(info.id);
}
        info.visibility = newVisibility;
        synchronized(info) {
            info.control = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CONTROL));
}
int newStatus = cursor.getInt(statusColumn);
        if (!Downloads.isStatusCompleted(info.status) && Downloads.isStatusCompleted(newStatus)) {
            mNotifier.mNotificationMgr.cancel(info.id);
}
        info.status = newStatus;
        info.numFailed = cursor.getInt(failedColumn);
int retryRedirect =
                cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RETRY_AFTER___REDIRECT_COUNT));
        info.retryAfter = retryRedirect & 0xfffffff;
        info.redirectCount = retryRedirect >> 28;
        info.lastMod = cursor.getLong(cursor.getColumnIndexOrThrow(Downloads.LAST_MODIFICATION));
        info.pckg = stringFromCursor(info.pckg, cursor, Downloads.NOTIFICATION_PACKAGE);
        info.clazz = stringFromCursor(info.clazz, cursor, Downloads.NOTIFICATION_CLASS);
        info.cookies = stringFromCursor(info.cookies, cursor, Downloads.COOKIE_DATA);
        info.userAgent = stringFromCursor(info.userAgent, cursor, Downloads.USER_AGENT);
        info.referer = stringFromCursor(info.referer, cursor, Downloads.REFERER);
        info.totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.TOTAL_BYTES));
        info.currentBytes = cursor.getInt(cursor.getColumnIndexOrThrow(Downloads.CURRENT_BYTES));
        info.etag = stringFromCursor(info.etag, cursor, Constants.ETAG);
        info.mediaScanned =
cursor.getInt(cursor.getColumnIndexOrThrow(Constants.MEDIA_SCANNED)) == 1;

if (info.canUseNetwork(networkAvailable, networkRoaming)) {
if (info.isReadyToRestart(now)) {
if (Constants.LOGV) {
Log.v(Constants.TAG, "Service spawning thread to handle updated download " +
                            info.id);
}
                if (info.hasActiveThread) {
throw new IllegalStateException("Multiple threads on same download on update");
}
                info.status = Downloads.STATUS_RUNNING;
ContentValues values = new ContentValues();
                values.put(Downloads.STATUS, info.status);
getContentResolver().update(
                        ContentUris.withAppendedId(Downloads.CONTENT_URI, info.id),
values, null, null);
DownloadThread downloader = new DownloadThread(this, info);
                info.hasActiveThread = true;
downloader.start();
}
}
//Synthetic comment -- @@ -756,11 +756,11 @@
if (old == null) {
return cursor.getString(index);
}
        if (newChars == null) {
            newChars = new CharArrayBuffer(128);
}
        cursor.copyStringToBuffer(index, newChars);
        int length = newChars.sizeCopied;
if (length != old.length()) {
return cursor.getString(index);
}
//Synthetic comment -- @@ -768,7 +768,7 @@
oldChars = new CharArrayBuffer(length);
}
char[] oldArray = oldChars.data;
        char[] newArray = newChars.data;
old.getChars(0, length, oldArray, 0);
for (int i = length - 1; i >= 0; --i) {
if (oldArray[i] != newArray[i]) {
//Synthetic comment -- @@ -783,12 +783,12 @@
*/
private void deleteDownload(int arrayPos) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        if (info.status == Downloads.STATUS_RUNNING) {
            info.status = Downloads.STATUS_CANCELED;
        } else if (info.destination != Downloads.DESTINATION_EXTERNAL && info.filename != null) {
            new File(info.filename).delete();
}
        mNotifier.mNotificationMgr.cancel(info.id);

mDownloads.remove(arrayPos);
}
//Synthetic comment -- @@ -802,13 +802,13 @@
*/
private long nextAction(int arrayPos, long now) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        if (Downloads.isStatusCompleted(info.status)) {
return -1;
}
        if (info.status != Downloads.STATUS_RUNNING_PAUSED) {
return 0;
}
        if (info.numFailed == 0) {
return 0;
}
long when = info.restartTime();
//Synthetic comment -- @@ -831,10 +831,10 @@
*/
private boolean shouldScanFile(int arrayPos) {
DownloadInfo info = (DownloadInfo) mDownloads.get(arrayPos);
        return !info.mediaScanned
                && info.destination == Downloads.DESTINATION_EXTERNAL
                && Downloads.isStatusSuccess(info.status)
                && !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING.equalsIgnoreCase(info.mimetype);
}

/**
//Synthetic comment -- @@ -854,9 +854,9 @@
if (mMediaScannerService != null) {
try {
if (Constants.LOGV) {
                        Log.v(Constants.TAG, "Scanning file " + info.filename);
}
                    mMediaScannerService.scanFile(info.filename, info.mimetype);
if (cursor != null) {
ContentValues values = new ContentValues();
values.put(Constants.MEDIA_SCANNED, 1);
//Synthetic comment -- @@ -868,7 +868,7 @@
return true;
} catch (RemoteException e) {
if (Config.LOGD) {
                        Log.d(Constants.TAG, "Failed to scan file " + info.filename);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 923e36d..6e72b42 100644

//Synthetic comment -- @@ -16,23 +16,19 @@

package com.android.providers.downloads;

import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.mobile1.DrmRawContent;
import android.net.http.AndroidHttpClient;
import android.net.Uri;
import android.os.FileUtils;
import android.os.PowerManager;
import android.os.Process;
//Synthetic comment -- @@ -44,10 +40,10 @@
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
* Runs an actual download
//Synthetic comment -- @@ -66,7 +62,7 @@
* Returns the user agent provided by the initiating app, or use the default one
*/
private String userAgent() {
        String userAgent = mInfo.userAgent;
if (userAgent != null) {
}
if (userAgent == null) {
//Synthetic comment -- @@ -84,15 +80,15 @@
int finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
boolean countRetry = false;
int retryAfter = 0;
        int redirectCount = mInfo.redirectCount;
String newUri = null;
boolean gotData = false;
String filename = null;
        String mimeType = mInfo.mimetype;
FileOutputStream stream = null;
AndroidHttpClient client = null;
PowerManager.WakeLock wakeLock = null;
        Uri contentUri = Uri.parse(Downloads.CONTENT_URI + "/" + mInfo.id);

try {
boolean continuingDownload = false;
//Synthetic comment -- @@ -111,12 +107,12 @@
wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.TAG);
wakeLock.acquire();

            filename = mInfo.filename;
if (filename != null) {
if (!Helpers.isFilenameValid(filename)) {
finalStatus = Downloads.STATUS_FILE_ERROR;
notifyDownloadCompleted(
                            finalStatus, false, 0, 0, false, filename, null, mInfo.mimetype);
return;
}
// We're resuming a download that got interrupted
//Synthetic comment -- @@ -127,7 +123,7 @@
// The download hadn't actually started, we can restart from scratch
f.delete();
filename = null;
                    } else if (mInfo.etag == null && !mInfo.noIntegrity) {
// Tough luck, that's not a resumable download
if (Config.LOGD) {
Log.d(Constants.TAG,
//Synthetic comment -- @@ -136,16 +132,16 @@
f.delete();
finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
notifyDownloadCompleted(
                                finalStatus, false, 0, 0, false, filename, null, mInfo.mimetype);
return;
} else {
// All right, we'll be able to resume this download
stream = new FileOutputStream(filename, true);
bytesSoFar = (int) fileLength;
                        if (mInfo.totalBytes != -1) {
                            headerContentLength = Integer.toString(mInfo.totalBytes);
}
                        headerETag = mInfo.etag;
continuingDownload = true;
}
}
//Synthetic comment -- @@ -158,7 +154,7 @@

client = AndroidHttpClient.newInstance(userAgent());

            if (stream != null && mInfo.destination == Downloads.DESTINATION_EXTERNAL
&& !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
.equalsIgnoreCase(mimeType)) {
try {
//Synthetic comment -- @@ -181,17 +177,17 @@
http_request_loop:
while (true) {
// Prepares the request and fires it.
                HttpGet request = new HttpGet(mInfo.uri);

if (Constants.LOGV) {
                    Log.v(Constants.TAG, "initiating download for " + mInfo.uri);
}

                if (mInfo.cookies != null) {
                    request.addHeader("Cookie", mInfo.cookies);
}
                if (mInfo.referer != null) {
                    request.addHeader("Referer", mInfo.referer);
}
if (continuingDownload) {
if (headerETag != null) {
//Synthetic comment -- @@ -206,10 +202,10 @@
} catch (IllegalArgumentException ex) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "Arg exception trying to execute request for " +
                                mInfo.uri + " : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "Arg exception trying to execute request for " +
                                mInfo.id + " : " +  ex);
}
finalStatus = Downloads.STATUS_BAD_REQUEST;
request.abort();
//Synthetic comment -- @@ -217,16 +213,16 @@
} catch (IOException ex) {
if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                    } else if (mInfo.numFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
Log.d(Constants.TAG, "IOException trying to execute request for " +
                                    mInfo.uri + " : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "IOException trying to execute request for " +
                                    mInfo.id + " : " + ex);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -235,7 +231,7 @@
}

int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 503 && mInfo.numFailed < Constants.MAX_RETRIES) {
if (Constants.LOGVV) {
Log.v(Constants.TAG, "got HTTP response code 503");
}
//Synthetic comment -- @@ -256,7 +252,7 @@
} else if (retryAfter > Constants.MAX_RETRY_AFTER) {
retryAfter = Constants.MAX_RETRY_AFTER;
}
                               retryAfter += Helpers.rnd.nextInt(Constants.MIN_RETRY_AFTER + 1);
retryAfter *= 1000;
}
} catch (NumberFormatException ex) {
//Synthetic comment -- @@ -275,10 +271,10 @@
}
if (redirectCount >= Constants.MAX_REDIRECTS) {
if (Constants.LOGV) {
                            Log.d(Constants.TAG, "too many redirects for download " + mInfo.id +
                                    " at " + mInfo.uri);
} else if (Config.LOGD) {
                            Log.d(Constants.TAG, "too many redirects for download " + mInfo.id);
}
finalStatus = Downloads.STATUS_TOO_MANY_REDIRECTS;
request.abort();
//Synthetic comment -- @@ -289,7 +285,26 @@
if (Constants.LOGVV) {
Log.v(Constants.TAG, "Location :" + header.getValue());
}
                        newUri = new URI(mInfo.uri).resolve(new URI(header.getValue())).toString();
++redirectCount;
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
request.abort();
//Synthetic comment -- @@ -299,10 +314,10 @@
if ((!continuingDownload && statusCode != Downloads.STATUS_SUCCESS)
|| (continuingDownload && statusCode != 206)) {
if (Constants.LOGV) {
                        Log.d(Constants.TAG, "http error " + statusCode + " for " + mInfo.uri);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "http error " + statusCode + " for download " +
                                mInfo.id);
}
if (Downloads.isStatusError(statusCode)) {
finalStatus = statusCode;
//Synthetic comment -- @@ -318,7 +333,7 @@
} else {
// Handles the response, saves the file
if (Constants.LOGV) {
                        Log.v(Constants.TAG, "received response for " + mInfo.uri);
}

if (!continuingDownload) {
//Synthetic comment -- @@ -375,7 +390,7 @@
Log.v(Constants.TAG, "Transfer-Encoding: " + headerTransferEncoding);
}

                        if (!mInfo.noIntegrity && headerContentLength == null &&
(headerTransferEncoding == null
|| !headerTransferEncoding.equalsIgnoreCase("chunked"))
) {
//Synthetic comment -- @@ -389,23 +404,23 @@

DownloadFileInfo fileInfo = Helpers.generateSaveFile(
mContext,
                                mInfo.uri,
                                mInfo.hint,
headerContentDisposition,
headerContentLocation,
mimeType,
                                mInfo.destination,
(headerContentLength != null) ?
Integer.parseInt(headerContentLength) : 0);
                        if (fileInfo.filename == null) {
                            finalStatus = fileInfo.status;
request.abort();
break http_request_loop;
}
                        filename = fileInfo.filename;
                        stream = fileInfo.stream;
if (Constants.LOGV) {
                            Log.v(Constants.TAG, "writing " + mInfo.uri + " to " + filename);
}

ContentValues values = new ContentValues();
//Synthetic comment -- @@ -430,16 +445,19 @@
} catch (IOException ex) {
if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                        } else if (mInfo.numFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
                                Log.d(Constants.TAG, "IOException getting entity for " + mInfo.uri +
                                    " : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "IOException getting entity for download " +
                                        mInfo.id + " : " + ex);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -454,13 +472,13 @@
ContentValues values = new ContentValues();
values.put(Downloads.CURRENT_BYTES, bytesSoFar);
mContext.getContentResolver().update(contentUri, values, null, null);
                            if (!mInfo.noIntegrity && headerETag == null) {
if (Constants.LOGV) {
                                    Log.v(Constants.TAG, "download IOException for " + mInfo.uri +
" : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "download IOException for download " +
                                            mInfo.id + " : " + ex);
}
if (Config.LOGD) {
Log.d(Constants.TAG,
//Synthetic comment -- @@ -469,16 +487,16 @@
finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
} else if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                            } else if (mInfo.numFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
                                    Log.v(Constants.TAG, "download IOException for " + mInfo.uri +
" : " + ex);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "download IOException for download " +
                                            mInfo.id + " : " + ex);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -495,26 +513,26 @@
if ((headerContentLength != null)
&& (bytesSoFar
!= Integer.parseInt(headerContentLength))) {
                                if (!mInfo.noIntegrity && headerETag == null) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "mismatched content length " +
                                                mInfo.uri);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "mismatched content length for " +
                                                mInfo.id);
}
finalStatus = Downloads.STATUS_LENGTH_REQUIRED;
} else if (!Helpers.isNetworkAvailable(mContext)) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                                } else if (mInfo.numFailed < Constants.MAX_RETRIES) {
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
countRetry = true;
} else {
if (Constants.LOGV) {
                                        Log.v(Constants.TAG, "closed socket for " + mInfo.uri);
} else if (Config.LOGD) {
Log.d(Constants.TAG, "closed socket for download " +
                                                mInfo.id);
}
finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
}
//Synthetic comment -- @@ -529,7 +547,7 @@
stream = new FileOutputStream(filename, true);
}
stream.write(data, 0, bytesRead);
                                if (mInfo.destination == Downloads.DESTINATION_EXTERNAL
&& !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
.equalsIgnoreCase(mimeType)) {
try {
//Synthetic comment -- @@ -567,30 +585,30 @@
}

if (Constants.LOGVV) {
                            Log.v(Constants.TAG, "downloaded " + bytesSoFar + " for " + mInfo.uri);
}
                        synchronized(mInfo) {
                            if (mInfo.control == Downloads.CONTROL_PAUSED) {
if (Constants.LOGV) {
                                    Log.v(Constants.TAG, "paused " + mInfo.uri);
}
finalStatus = Downloads.STATUS_RUNNING_PAUSED;
request.abort();
break http_request_loop;
}
}
                        if (mInfo.status == Downloads.STATUS_CANCELED) {
if (Constants.LOGV) {
                                Log.d(Constants.TAG, "canceled " + mInfo.uri);
} else if (Config.LOGD) {
                                // Log.d(Constants.TAG, "canceled id " + mInfo.id);
}
finalStatus = Downloads.STATUS_CANCELED;
break http_request_loop;
}
}
if (Constants.LOGV) {
                        Log.v(Constants.TAG, "download completed for " + mInfo.uri);
}
finalStatus = Downloads.STATUS_SUCCESS;
}
//Synthetic comment -- @@ -602,16 +620,16 @@
}
finalStatus = Downloads.STATUS_FILE_ERROR;
// falls through to the code that reports an error
        } catch (Exception ex) { //sometimes the socket code throws unchecked exceptions
if (Constants.LOGV) {
                Log.d(Constants.TAG, "Exception for " + mInfo.uri, ex);
} else if (Config.LOGD) {
                Log.d(Constants.TAG, "Exception for id " + mInfo.id, ex);
}
finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
// falls through to the code that reports an error
} finally {
            mInfo.hasActiveThread = false;
if (wakeLock != null) {
wakeLock.release();
wakeLock = null;
//Synthetic comment -- @@ -685,17 +703,17 @@
}
values.put(Downloads.MIMETYPE, mimeType);
values.put(Downloads.LAST_MODIFICATION, System.currentTimeMillis());
        values.put(Constants.RETRY_AFTER___REDIRECT_COUNT, retryAfter + (redirectCount << 28));
if (!countRetry) {
values.put(Constants.FAILED_CONNECTIONS, 0);
} else if (gotData) {
values.put(Constants.FAILED_CONNECTIONS, 1);
} else {
            values.put(Constants.FAILED_CONNECTIONS, mInfo.numFailed + 1);
}

mContext.getContentResolver().update(
                ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.id), values, null, null);
}

/**
//Synthetic comment -- @@ -703,7 +721,7 @@
* download completed even if it's not actively watching the cursor.
*/
private void notifyThroughIntent() {
        Uri uri = Uri.parse(Downloads.CONTENT_URI + "/" + mInfo.id);
mInfo.sendIntentIfRequested(uri, mContext);
}









//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index 89a5731..4ebc818 100644

//Synthetic comment -- @@ -40,16 +40,16 @@
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

/**
* Some helper functions for the download manager
*/
public class Helpers {

    public static Random rnd = new Random(SystemClock.uptimeMillis());

/** Regex used to parse content-disposition headers */
private static final Pattern CONTENT_DISPOSITION_PATTERN =
//Synthetic comment -- @@ -412,7 +412,7 @@
if (Constants.LOGVV) {
Log.v(Constants.TAG, "file with sequence number " + sequence + " exists");
}
                sequence += rnd.nextInt(magnitude) + 1;
}
}
return null;
//Synthetic comment -- @@ -756,7 +756,7 @@
// quoted strings
if (chars[mOffset] == '\'') {
++mOffset;
                while(mOffset < chars.length) {
if (chars[mOffset] == '\'') {
if (mOffset + 1 < chars.length && chars[mOffset + 1] == '\'') {
++mOffset;







