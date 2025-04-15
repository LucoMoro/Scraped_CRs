/*Change in media scanner to set correct date

When the media scanner scanned a single file, the modified
date (DATE_MODIFIED) was not correctly set in the content
provider. It was set as milliseconds but should have been
set as seconds. This caused downloaded media items to display
wrongly in the camera album, since the date was wrong.

Change-Id:I24cd92215c26f579eb33a4a3890f96c6ef9ec8c0*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 4296afb..84a67cf 100644

//Synthetic comment -- @@ -1203,8 +1203,12 @@
prescan(path);

File file = new File(path);

            // lastModified is in milliseconds on Files.
            long lastModifiedSeconds = file.lastModified() / 1000;

// always scan the file, so we can return the content://media Uri for existing files
            return mClient.doScanFile(path, mimeType, lastModifiedSeconds, file.length(), true);
} catch (RemoteException e) {
Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
return null;







