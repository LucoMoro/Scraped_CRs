/*Handle nested folder case

Change-Id:Id6d6a5899e8255d18192adbc766d8d9a0e6825d0Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/app/DownloadManager.java b/core/java/android/app/DownloadManager.java
//Synthetic comment -- index 6cf4dd0..32e40ee 100644

//Synthetic comment -- @@ -499,7 +499,7 @@
" already exists and is not a directory");
}
} else {
                if (!file.mkdirs()) {
throw new IllegalStateException("Unable to create directory: "+
file.getAbsolutePath());
}







