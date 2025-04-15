/*Force download progress bar to show progress.

Somehow progress bars in list viewws have problems with their
indeterminate state (whether the state is indeterminate is
actually indeterminate, how ironic). This forces the state to
not be indeterminate.

This fixeshttp://code.google.com/p/android/issues/detail?id=1118*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserDownloadAdapter.java b/src/com/android/browser/BrowserDownloadAdapter.java
//Synthetic comment -- index 68d3a83..16cb982 100644

//Synthetic comment -- @@ -180,6 +180,7 @@
sb.append("/");
sb.append(Formatter.formatFileSize(mContext, totalBytes));
sb.append(")");
pb.setProgress(progressAmount);
} else {
pb.setIndeterminate(true);







