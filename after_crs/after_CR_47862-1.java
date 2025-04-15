/*Media scanning exception & Media DB constraints violation

Constraint violation failures can be occurred by time based primary declaration on log table in Media DB.
Primary key is removed and an index for time column is added to keep existing process flow.
MediaScanningService.scan method doesn't have exception catching code from contents resolver.

Signed-off-by : lg-database <lg-database@lge.com>

Change-Id:Ic68ce6eeffbf52f71586baf47f3f01349bf89b17*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index d1f53b7..e352b7f 100644

//Synthetic comment -- @@ -1730,12 +1730,8 @@
}

if (fromVersion < 509) {
db.execSQL("CREATE TABLE IF NOT EXISTS log (time DATETIME, message TEXT);");
db.execSQL("CREATE INDEX IF NOT EXISTS log_idx ON log(time);");+            
}

// Emulated external storage moved to user-specific paths








//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaScannerService.java b/src/com/android/providers/media/MediaScannerService.java
//Synthetic comment -- index 4752ee6..c4e2d58 100644

//Synthetic comment -- @@ -107,16 +107,11 @@
Log.e(TAG, "exception in MediaScanner.scan()", e);
}

try {
getContentResolver().delete(scanUri, null, null);
} catch (Exception e) {
Log.e(TAG, "Exception in getContentResolver().delete()", e);
}
} finally {
sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED, uri));
mWakeLock.release();







