/*Downloading of a file has a small chance of failing randomly.

Due to timing issues a download could be cancelled. This happens when the
downloading state machine gets in the wrong state. There are two variables
in the state machine which gets out of sync with each other: status and
mHasActiveThread.
The DownloadService thread reads several downloads from the database
and starts to process them. For each applicable download a DowloadThread
is started. For that download mHasActiveThread is set to true and the
download is started. When the download is finished the correct status
is written to the database and mHasActiveThread is set to false.
The problem is that mHasActiveThread changes value instantly in
DownloadService but the status for all downloads are read from the
database at certain times. If the DownloadThread changes the value of
mHasActiveThread before that download has been processed in the
DownloadService thread the value of mHasActiveThread will be up to date
but the status value will be out of sync.
The download will then be restarted even if it is already completed. The
DownloadService will interpred the download as a resume but the server will
handle it as a new download. The downloading statemachine will then be
confused and terminate the download.
This is fixed by synching the changing of status and mHasActiveThread between
DownloadService and DownloadThread. The database information in DowloadService
will also be re-read when it is not up to date.

Change-Id:I8bb28b42b434b424187c918dfded4fdf3baa055b*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadInfo.java b/src/com/android/providers/downloads/DownloadInfo.java
//Synthetic comment -- index 7b29168..c0b1b8a 100644

//Synthetic comment -- @@ -471,8 +471,12 @@
mContext.getContentResolver().update(getAllDownloadsUri(), values, null, null);
}
DownloadThread downloader = new DownloadThread(mContext, mSystemFacade, this);
        mHasActiveThread = true;
        mSystemFacade.startThread(downloader);
}

public boolean isOnCache() {








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadService.java b/src/com/android/providers/downloads/DownloadService.java
//Synthetic comment -- index 95d07d6..2ca732d 100644

//Synthetic comment -- @@ -287,16 +287,24 @@
mPendingUpdate = false;
}

long now = mSystemFacade.currentTimeMillis();
boolean mustScan = false;
keepService = false;
wakeUp = Long.MAX_VALUE;
Set<Long> idsNoLongerInDatabase = new HashSet<Long>(mDownloads.keySet());

                Cursor cursor = getContentResolver().query(Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI,
                        null, null, null, null);
                if (cursor == null) {
                    continue;
}
try {
DownloadInfo.Reader reader =
//Synthetic comment -- @@ -304,82 +312,99 @@
int idColumn = cursor.getColumnIndexOrThrow(Downloads.Impl._ID);

for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        idsNoLongerInDatabase.remove(id);
                        DownloadInfo info = mDownloads.get(id);
                        if (info != null) {
                            updateDownload(reader, info, now);
                        } else {
                            info = insertDownload(reader, now);
                        }

                        if (info.shouldScanFile() && !scanFile(info, true, false)) {
                            mustScan = true;
                            keepService = true;
                        }
                        if (info.hasCompletionNotification()) {
                            keepService = true;
                        }
                        long next = info.nextAction(now);
                        if (next == 0) {
                            keepService = true;
                        } else if (next > 0 && next < wakeUp) {
                            wakeUp = next;
}
}
} finally {
cursor.close();
}

                for (Long id : idsNoLongerInDatabase) {
                    deleteDownload(id);
                }

                // is there a need to start the DownloadService? yes, if there are rows to be
                // deleted.
                if (!mustScan) {
                    for (DownloadInfo info : mDownloads.values()) {
                        if (info.mDeleted && TextUtils.isEmpty(info.mMediaProviderUri)) {
                            mustScan = true;
                            keepService = true;
                            break;
}
}
                }
                mNotifier.updateNotification(mDownloads.values());
                if (mustScan) {
                    bindMediaScanner();
                } else {
                    mMediaScannerConnection.disconnectMediaScanner();
                }

                // look for all rows with deleted flag set and delete the rows from the database
                // permanently
                for (DownloadInfo info : mDownloads.values()) {
                    if (info.mDeleted) {
                        // this row is to be deleted from the database. but does it have
                        // mediaProviderUri?
                        if (TextUtils.isEmpty(info.mMediaProviderUri)) {
                            if (info.shouldScanFile()) {
                                // initiate rescan of the file to - which will populate
                                // mediaProviderUri column in this row
                                if (!scanFile(info, false, true)) {
                                    throw new IllegalStateException("scanFile failed!");
}
} else {
                                // this file should NOT be scanned. delete the file.
Helpers.deleteFile(getContentResolver(), info.mId, info.mFileName,
info.mMimeType);
}
                        } else {
                            // yes it has mediaProviderUri column already filled in.
                            // delete it from MediaProvider database and then from downloads table
                            // in DownProvider database (the order of deletion is important).
                            getContentResolver().delete(Uri.parse(info.mMediaProviderUri), null,
                                    null);
                            // the following deletes the file and then deletes it from downloads db
                            Helpers.deleteFile(getContentResolver(), info.mId, info.mFileName,
                                    info.mMimeType);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index e85d580..4587b0b 100644

//Synthetic comment -- @@ -206,10 +206,16 @@
client = null;
}
cleanupDestination(state, finalStatus);
            notifyDownloadCompleted(finalStatus, state.mCountRetry, state.mRetryAfter,
                                    state.mGotData, state.mFilename,
                                    state.mNewUri, state.mMimeType);
            mInfo.mHasActiveThread = false;
}
}









//Synthetic comment -- diff --git a/src/com/android/providers/downloads/RealSystemFacade.java b/src/com/android/providers/downloads/RealSystemFacade.java
//Synthetic comment -- index ce86f73..4d77106 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
class RealSystemFacade implements SystemFacade {
private Context mContext;
private NotificationManager mNotificationManager;

public RealSystemFacade(Context context) {
mContext = context;
//Synthetic comment -- @@ -114,4 +115,12 @@
public void startThread(Thread thread) {
thread.start();
}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/SystemFacade.java b/src/com/android/providers/downloads/SystemFacade.java
//Synthetic comment -- index ed0d330..1c790cf 100644

//Synthetic comment -- @@ -1,4 +1,3 @@

package com.android.providers.downloads;

import android.app.Notification;
//Synthetic comment -- @@ -65,4 +64,14 @@
* Start a thread.
*/
public void startThread(Thread thread);
}








//Synthetic comment -- diff --git a/tests/src/com/android/providers/downloads/FakeSystemFacade.java b/tests/src/com/android/providers/downloads/FakeSystemFacade.java
//Synthetic comment -- index 5263015..4755526 100644

//Synthetic comment -- @@ -91,4 +91,11 @@
mStartedThreads.poll().run();
}
}
}







