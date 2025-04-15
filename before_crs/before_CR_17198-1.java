/*Pass webview's useragent when downloading via context menu

Currently null is passed as useragent to the Download Provider when
downloading via Browser context menu (causes the Download
Provider to fallback to the default Download Provider useragent).
This commit simply passes the webview's useragent instead.

This applies to the usecase when choosing save image/link
via the context menu (long press).

Change-Id:Ie9cc5e81630d0dcefe4708980146cc5ed867e4ad*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 29e333a..922fbbf 100644

//Synthetic comment -- @@ -1778,7 +1778,7 @@
private String mText;

public boolean onMenuItemClick(MenuItem item) {
            onDownloadStartNoStream(mText, null, null, null, -1);
return true;
}

//Synthetic comment -- @@ -2254,7 +2254,7 @@
break;
case R.id.save_link_context_menu_id:
case R.id.download_context_menu_id:
                            onDownloadStartNoStream(url, null, null, null, -1);
break;
}
break;
//Synthetic comment -- @@ -3596,6 +3596,15 @@
return null;
}

protected static final Pattern ACCEPTED_URI_SCHEMA = Pattern.compile(
"(?i)" + // switch on case insensitive matching
"(" +    // begin group for schema







