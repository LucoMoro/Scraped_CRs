/*Subject: Force sync the downloaded file to the storage before
completion. (Updated)

This will writing the downlaoded file to the strage (sdcard in default).
It can prevent file corruption if the user remove the sdcard unsafely
while download completed.

Refine the code design, style and import statements.*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 923e36d..f99e0ca 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
//Synthetic comment -- @@ -34,6 +35,7 @@
import android.net.http.AndroidHttpClient;
import android.net.Uri;
import android.os.FileUtils;
import android.os.PowerManager;
import android.os.Process;
import android.provider.Downloads;
//Synthetic comment -- @@ -42,11 +44,13 @@
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
//Synthetic comment -- @@ -654,7 +658,27 @@
} else if (Downloads.isStatusSuccess(finalStatus)) {
// make sure the file is readable
FileUtils.setPermissions(filename, 0644, -1, -1);
                }
}
notifyDownloadCompleted(finalStatus, countRetry, retryAfter, redirectCount,
gotData, filename, newUri, mimeType);







