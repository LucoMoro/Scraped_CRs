/*Replaced deprecated Config.LOGD with Config.DEBUG, so DebugOutput will only be enabled when in DEBUG mode

Change-Id:I625021d792d2ebfd2d19d5ab0ad7e1901f698306*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadProvider.java b/src/com/android/providers/downloads/DownloadProvider.java
//Synthetic comment -- index f0190fd..3ace412 100644

//Synthetic comment -- @@ -255,7 +255,7 @@
SQLiteDatabase db = mOpenHelper.getWritableDatabase();

if (sURIMatcher.match(uri) != DOWNLOADS) {
            if (Config.LOGD) {
Log.d(Constants.TAG, "calling insert on an unknown/invalid URI: " + uri);
}
throw new IllegalArgumentException("Unknown/Invalid URI " + uri);
//Synthetic comment -- @@ -342,7 +342,7 @@
ret = Uri.parse(Downloads.CONTENT_URI + "/" + rowID);
context.getContentResolver().notifyChange(uri, null);
} else {
            if (Config.LOGD) {
Log.d(Constants.TAG, "couldn't insert into downloads database");
}
}
//Synthetic comment -- @@ -533,7 +533,7 @@
break;
}
default: {
                if (Config.LOGD) {
Log.d(Constants.TAG, "updating unknown/invalid URI: " + uri);
}
throw new UnsupportedOperationException("Cannot update URI: " + uri);
//Synthetic comment -- @@ -585,7 +585,7 @@
break;
}
default: {
                if (Config.LOGD) {
Log.d(Constants.TAG, "deleting unknown/invalid URI: " + uri);
}
throw new UnsupportedOperationException("Cannot delete URI: " + uri);








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadReceiver.java b/src/com/android/providers/downloads/DownloadReceiver.java
//Synthetic comment -- index 2065c64..94a798b 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
try {
context.startActivity(activityIntent);
} catch (ActivityNotFoundException ex) {
                            if (Config.LOGD) {
Log.d(Constants.TAG, "no activity for " + mimetype, ex);
}
// nothing anyone can do about this, but we're in a clean state,








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadService.java b/src/com/android/providers/downloads/DownloadService.java
//Synthetic comment -- index a246d29..1b0d863 100644

//Synthetic comment -- @@ -480,7 +480,7 @@
// when running the simulator).
return;
}
        HashSet<String> fileSet = new HashSet();
for (int i = 0; i < files.length; i++) {
if (files[i].getName().equals(Constants.KNOWN_SPURIOUS_FILENAME)) {
continue;
//Synthetic comment -- @@ -631,7 +631,7 @@
//Log.i(Constants.TAG, "*** QUERY " + mimetypeIntent + ": " + list);

if (ri == null) {
                if (Config.LOGD) {
Log.d(Constants.TAG, "no application to handle MIME type " + info.mMimeType);
}
info.mStatus = Downloads.STATUS_NOT_ACCEPTABLE;
//Synthetic comment -- @@ -876,7 +876,7 @@
}
return true;
} catch (RemoteException e) {
                    if (Config.LOGD) {
Log.d(Constants.TAG, "Failed to scan file " + info.mFileName);
}
}








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 1ad1d4f..22086df 100644

//Synthetic comment -- @@ -19,8 +19,6 @@
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;

import android.content.ContentUris;
import android.content.ContentValues;
//Synthetic comment -- @@ -38,7 +36,6 @@
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -128,7 +125,7 @@
filename = null;
} else if (mInfo.mETag == null && !mInfo.mNoIntegrity) {
// Tough luck, that's not a resumable download
                        if (Config.LOGD) {
Log.d(Constants.TAG,
"can't resume interrupted non-resumable download"); 
}
//Synthetic comment -- @@ -206,7 +203,7 @@
if (Constants.LOGV) {
Log.d(Constants.TAG, "Arg exception trying to execute request for " +
mInfo.mUri + " : " + ex);
                    } else if (Config.LOGD) {
Log.d(Constants.TAG, "Arg exception trying to execute request for " +
mInfo.mId + " : " +  ex);
}
//Synthetic comment -- @@ -230,7 +227,7 @@
if (Constants.LOGV) {
Log.d(Constants.TAG, "IOException trying to execute request for " +
mInfo.mUri + " : " + ex);
                        } else if (Config.LOGD) {
Log.d(Constants.TAG, "IOException trying to execute request for " +
mInfo.mId + " : " + ex);
}
//Synthetic comment -- @@ -283,7 +280,7 @@
if (Constants.LOGV) {
Log.d(Constants.TAG, "too many redirects for download " + mInfo.mId +
" at " + mInfo.mUri);
                        } else if (Config.LOGD) {
Log.d(Constants.TAG, "too many redirects for download " + mInfo.mId);
}
finalStatus = Downloads.STATUS_TOO_MANY_REDIRECTS;
//Synthetic comment -- @@ -306,7 +303,7 @@
header.getValue() +
" for " +
mInfo.mUri);
                            } else if (Config.LOGD) {
Log.d(Constants.TAG,
"Couldn't resolve redirect URI for download " +
mInfo.mId);
//Synthetic comment -- @@ -325,7 +322,7 @@
|| (continuingDownload && statusCode != 206)) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "http error " + statusCode + " for " + mInfo.mUri);
                    } else if (Config.LOGD) {
Log.d(Constants.TAG, "http error " + statusCode + " for download " +
mInfo.mId);
}
//Synthetic comment -- @@ -400,7 +397,7 @@
(headerTransferEncoding == null
|| !headerTransferEncoding.equalsIgnoreCase("chunked"))
) {
                            if (Config.LOGD) {
Log.d(Constants.TAG, "can't know size of download, giving up");
}
finalStatus = Downloads.STATUS_LENGTH_REQUIRED;
//Synthetic comment -- @@ -468,7 +465,7 @@
mInfo.mUri +
" : " +
ex);
                            } else if (Config.LOGD) {
Log.d(Constants.TAG, "IOException getting entity for download " +
mInfo.mId + " : " + ex);
}
//Synthetic comment -- @@ -496,11 +493,11 @@
if (Constants.LOGV) {
Log.v(Constants.TAG, "download IOException for " + mInfo.mUri +
" : " + ex);
                                } else if (Config.LOGD) {
Log.d(Constants.TAG, "download IOException for download " +
mInfo.mId + " : " + ex);
}
                                if (Config.LOGD) {
Log.d(Constants.TAG,
"can't resume interrupted download with no ETag");
}
//Synthetic comment -- @@ -514,7 +511,7 @@
if (Constants.LOGV) {
Log.v(Constants.TAG, "download IOException for " + mInfo.mUri +
" : " + ex);
                                } else if (Config.LOGD) {
Log.d(Constants.TAG, "download IOException for download " +
mInfo.mId + " : " + ex);
}
//Synthetic comment -- @@ -537,7 +534,7 @@
if (Constants.LOGV) {
Log.d(Constants.TAG, "mismatched content length " +
mInfo.mUri);
                                    } else if (Config.LOGD) {
Log.d(Constants.TAG, "mismatched content length for " +
mInfo.mId);
}
//Synthetic comment -- @@ -550,7 +547,7 @@
} else {
if (Constants.LOGV) {
Log.v(Constants.TAG, "closed socket for " + mInfo.mUri);
                                    } else if (Config.LOGD) {
Log.d(Constants.TAG, "closed socket for download " +
mInfo.mId);
}
//Synthetic comment -- @@ -620,7 +617,7 @@
if (mInfo.mStatus == Downloads.STATUS_CANCELED) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "canceled " + mInfo.mUri);
                            } else if (Config.LOGD) {
// Log.d(Constants.TAG, "canceled id " + mInfo.mId);
}
finalStatus = Downloads.STATUS_CANCELED;
//Synthetic comment -- @@ -635,7 +632,7 @@
break;
}
} catch (FileNotFoundException ex) {
            if (Config.LOGD) {
Log.d(Constants.TAG, "FileNotFoundException for " + filename + " : " +  ex);
}
finalStatus = Downloads.STATUS_FILE_ERROR;
//Synthetic comment -- @@ -643,7 +640,7 @@
} catch (RuntimeException ex) { //sometimes the socket code throws unchecked exceptions
if (Constants.LOGV) {
Log.d(Constants.TAG, "Exception for " + mInfo.mUri, ex);
            } else if (Config.LOGD) {
Log.d(Constants.TAG, "Exception for id " + mInfo.mId, ex);
}
finalStatus = Downloads.STATUS_UNKNOWN_ERROR;








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index d8f262c..cdef75f 100644

//Synthetic comment -- @@ -94,7 +94,7 @@
if (destination == Downloads.DESTINATION_EXTERNAL
|| destination == Downloads.DESTINATION_CACHE_PARTITION_PURGEABLE) {
if (mimeType == null) {
                if (Config.LOGD) {
Log.d(Constants.TAG, "external download with no mime type not allowed");
}
return new DownloadFileInfo(null, null, Downloads.STATUS_NOT_ACCEPTABLE);
//Synthetic comment -- @@ -118,7 +118,7 @@
//Log.i(Constants.TAG, "*** FILENAME QUERY " + intent + ": " + list);

if (ri == null) {
                    if (Config.LOGD) {
Log.d(Constants.TAG, "no handler found for type " + mimeType);
}
return new DownloadFileInfo(null, null, Downloads.STATUS_NOT_ACCEPTABLE);
//Synthetic comment -- @@ -167,7 +167,7 @@
}
if (!discardPurgeableFiles(context,
contentLength - blockSize * ((long) availableBlocks - 4))) {
                    if (Config.LOGD) {
Log.d(Constants.TAG,
"download aborted - not enough free space in internal storage");
}
//Synthetic comment -- @@ -181,7 +181,7 @@
String root = Environment.getExternalStorageDirectory().getPath();
base = new File(root + Constants.DEFAULT_DL_SUBDIR);
if (!base.isDirectory() && !base.mkdir()) {
                    if (Config.LOGD) {
Log.d(Constants.TAG, "download aborted - can't create base directory "
+ base.getPath());
}
//Synthetic comment -- @@ -189,7 +189,7 @@
}
stat = new StatFs(base.getPath());
} else {
                if (Config.LOGD) {
Log.d(Constants.TAG, "download aborted - no external storage");
}
return new DownloadFileInfo(null, null, Downloads.STATUS_FILE_ERROR);
//Synthetic comment -- @@ -200,7 +200,7 @@
* Put a bit of margin (in case creating the file grows the system by a few blocks).
*/
if (stat.getBlockSize() * ((long) stat.getAvailableBlocks() - 4) < contentLength) {
                if (Config.LOGD) {
Log.d(Constants.TAG, "download aborted - not enough free space");
}
return new DownloadFileInfo(null, null, Downloads.STATUS_FILE_ERROR);
//Synthetic comment -- @@ -545,7 +545,7 @@
} catch (RuntimeException ex) {
if (Constants.LOGV) {
Log.d(Constants.TAG, "invalid selection [" + selection + "] triggered " + ex);
            } else if (Config.LOGD) {
Log.d(Constants.TAG, "invalid selection triggered " + ex);
}
throw ex;







