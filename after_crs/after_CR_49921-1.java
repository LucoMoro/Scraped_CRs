/*Handle DownloadManager expceptions

Handles illegal state exceptions thrown by DownloadManager
on two ocassions:
* the directory sdcard/Downloads is an ordinary file
* could not create dirctory sdcard/Downloads

Change will log an error, additionally toast with information
will pop up.

Change-Id:If853d3218a188bc77c187362a70697126b90989d*/




//Synthetic comment -- diff --git a/src/com/android/browser/DownloadHandler.java b/src/com/android/browser/DownloadHandler.java
//Synthetic comment -- index dee10ae..208d4ce 100644

//Synthetic comment -- @@ -195,8 +195,17 @@
request.setMimeType(mimetype);
// set downloaded file destination to /sdcard/Download.
// or, should it be set to one of several Environment.DIRECTORY* dirs depending on mimetype?
        try {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        } catch (IllegalStateException ex) {
            // This only happens when directory Downloads can't be created or it isn't a directory
            // this is most commonly due to temporary problems with sdcard so show appropriate string
            Log.w(LOGTAG, "Exception trying to create Download dir:", ex);
            Toast.makeText(activity, R.string.download_sdcard_busy_dlg_title,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // let this downloaded file be scanned by MediaScanner - so that it can
// show up in Gallery app, for example.
request.allowScanningByMediaScanner();
request.setDescription(webAddress.getHost());







