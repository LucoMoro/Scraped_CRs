/*Fixed release of wakelock

In some situations, when not handled exceptions occurred,
wakelock was not released. Now wakelock is released always.

Change-Id:I356cdfbcbfb9594e4577d8b2c9f54d5560158d48*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaScannerService.java b/src/com/android/providers/media/MediaScannerService.java
//Synthetic comment -- index 99bd734..f995a51 100644

//Synthetic comment -- @@ -88,6 +88,7 @@
// don't sleep while scanning
mWakeLock.acquire();

        try {
ContentValues values = new ContentValues();
values.put(MediaStore.MEDIA_SCANNER_VOLUME, volumeName);
Uri scanUri = getContentResolver().insert(MediaStore.getMediaScannerUri(), values);
//Synthetic comment -- @@ -95,21 +96,21 @@
Uri uri = Uri.parse("file://" + directories[0]);
sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_STARTED, uri));

        if (volumeName.equals(MediaProvider.EXTERNAL_VOLUME)) {
            openDatabase(volumeName);
}

        MediaScanner scanner = createMediaScanner();
        scanner.scanDirectories(directories, volumeName);

getContentResolver().delete(scanUri, null, null);

sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED, uri));
        } catch (Exception e) {
            Log.e(TAG, "exception in MediaScanner.scan()", e);
        } finally {
            mWakeLock.release();
        }
}

@Override







