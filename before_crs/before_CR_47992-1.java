/*Media scanning exception & Media DB constraints violation

Constraint violation failures can be occurred by time based primary declaration on log table in Media DB.
Primary key is removed and an index for time column is added to keep existing process flow.
MediaScanningService.scan method doesn't have exception catching code from contents resolver.

Signed-off-by : lg-database <lg-database@lge.com>

Change-Id:I2f41b93741c61509d5f1111f241721076d8fd7df*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c73871d..849f145 100644

//Synthetic comment -- @@ -1730,7 +1730,8 @@
}

if (fromVersion < 509) {
            db.execSQL("CREATE TABLE IF NOT EXISTS log (time DATETIME PRIMARY KEY, message TEXT);");
}

// Emulated external storage moved to user-specific paths








//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaScannerService.java b/src/com/android/providers/media/MediaScannerService.java
//Synthetic comment -- index 40bb10a..cae4853 100644

//Synthetic comment -- @@ -107,8 +107,11 @@
Log.e(TAG, "exception in MediaScanner.scan()", e);
}

            getContentResolver().delete(scanUri, null, null);

} finally {
sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED, uri));
mWakeLock.release();







