/*avoid repetition of retrying download

If isReadyToStart returns 'true' for STATUS_INSUFFICIENT_SPACE_ERROR,
DownloadProvider retries downloading after it gets requests from
browser to download contents.  The retrying loop won't stop until
its completion (recovering from space error), or user stops
downloading.  This retry loop impacts to network traffic, battery
consumption and packet charge.  So, change this method to return
'false' for the error in order to stop the loop.

Change-Id:Ia3466db60d86c5900842c7c28d294898ae3ff2bc*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadInfo.java b/src/com/android/providers/downloads/DownloadInfo.java
//Synthetic comment -- index 00b1045..47b7bc5 100644

//Synthetic comment -- @@ -312,11 +312,8 @@
// is the media mounted?
return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
case Downloads.Impl.STATUS_INSUFFICIENT_SPACE_ERROR:
                // avoids repetition of retrying download
                return false;
}
return false;
}







