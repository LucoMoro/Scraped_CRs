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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        // let this downloaded file be scanned by MediaScanner - so that it can 
// show up in Gallery app, for example.
request.allowScanningByMediaScanner();
request.setDescription(webAddress.getHost());







