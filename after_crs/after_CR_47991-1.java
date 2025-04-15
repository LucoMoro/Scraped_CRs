/*Media scanning exception & Media DB constraints violation

Constraint violation failures can be occurred by time based primary declaration on log table in Media DB.
Primary key is removed and an index for time column is added to keep existing process flow.
MediaScanningService.scan method doesn't have exception catching code from contents resolver.

Change-Id:I4ca7c33e43c922c186fa776ce08064288dbcf316Sifned-off-by: lg-database <lg-database@lge.com>*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c73871d..d1f53b7 100644

//Synthetic comment -- @@ -1730,7 +1730,12 @@
}

if (fromVersion < 509) {
	    // Constraint violation failures can be occurred by time based primary declaration on log table.
            // Primary key is removed and an index for time column is added to keep existing process flow.
            // lg-database START [lg-database@lge.com]
            db.execSQL("CREATE TABLE IF NOT EXISTS log (time DATETIME, message TEXT);");
            db.execSQL("CREATE INDEX IF NOT EXISTS log_idx ON log(time);");+            
	    // lg-database END
}

// Emulated external storage moved to user-specific paths








//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaScannerService.java b/src/com/android/providers/media/MediaScannerService.java
//Synthetic comment -- index 40bb10a..4752ee6 100644

//Synthetic comment -- @@ -107,7 +107,15 @@
Log.e(TAG, "exception in MediaScanner.scan()", e);
}

	    // There was no exception catching code from contents resolver.
            // Accessing DB or file can make exceptions anytime.
            // lg-database START[lg-database@lge.com]
            try {
                getContentResolver().delete(scanUri, null, null);
            } catch (Exception e) {
                Log.e(TAG, "Exception in getContentResolver().delete()", e);
            }
            // lg-database END

} finally {
sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED, uri));







