/*Pass webview's useragent when downloading via context menu

Currently null is passed as useragent to the Download Manager when
downloading via Browser context menu (causes the Download
Provider to use an empty useragent HTTP header).
This commit simply passes the webview's useragent instead.

This applies to the usecase when choosing save image/link
via the context menu (long press).

Change-Id:Ie9cc5e81630d0dcefe4708980146cc5ed867e4ad*/
//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index ebfd56f..e2f7b06 100644

//Synthetic comment -- @@ -512,8 +512,8 @@
case R.id.save_link_context_menu_id:
case R.id.download_context_menu_id:
DownloadHandler.onDownloadStartNoStream(
                                        mActivity, url, null, null, null,
                                        view.isPrivateBrowsingEnabled());
break;
}
break;
//Synthetic comment -- @@ -1434,8 +1434,9 @@
}
});
menu.findItem(R.id.download_context_menu_id).
                        setOnMenuItemClickListener(
                                new Download(mActivity, extra, webview.isPrivateBrowsingEnabled()));
menu.findItem(R.id.set_wallpaper_context_menu_id).
setOnMenuItemClickListener(new WallpaperHandler(mActivity,
extra));
//Synthetic comment -- @@ -2165,6 +2166,7 @@
private Activity mActivity;
private String mText;
private boolean mPrivateBrowsing;
private static final String FALLBACK_EXTENSION = "dat";
private static final String IMAGE_BASE_FORMAT = "yyyy-MM-dd-HH-mm-ss-";

//Synthetic comment -- @@ -2173,16 +2175,18 @@
if (DataUri.isDataUri(mText)) {
saveDataUri();
} else {
                DownloadHandler.onDownloadStartNoStream(mActivity, mText, null,
null, null, mPrivateBrowsing);
}
return true;
}

        public Download(Activity activity, String toDownload, boolean privateBrowsing) {
mActivity = activity;
mText = toDownload;
mPrivateBrowsing = privateBrowsing;
}

/**







