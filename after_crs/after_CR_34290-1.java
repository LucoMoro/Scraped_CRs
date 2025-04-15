/*UserAgent not propagated from Browser to DownloadProvider

Browser does not propagate the userAgent to DownloadProvider
for downloads. When downloads are started the default userAgent
in DownloadProvider is used instead of using the userAgent of
the Browser. This makes downloads on some services / web pages
fail.

This is solved this by including the Browser userAgent in the
request to the DownloadManager.

Change-Id:If4e0f3c29eb7fd5fc585dd01987ae7f0b54ffc32*/




//Synthetic comment -- diff --git a/src/com/android/browser/DownloadHandler.java b/src/com/android/browser/DownloadHandler.java
//Synthetic comment -- index 6e2c786..31212b4 100644

//Synthetic comment -- @@ -204,6 +204,7 @@
// old percent-encoded url.
String cookies = CookieManager.getInstance().getCookie(url, privateBrowsing);
request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", userAgent);
request.setNotificationVisibility(
DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
if (mimetype == null) {







