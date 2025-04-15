/*Add DownloadManager Test

Bug 3030013

Exercise DownloadManager's enqueue, query, remove methods, and
the ACTION_DOWNLOAD_COMPLETE broadcast receiver.

Change-Id:I93202e99850a9225fcbe76408a59be0b97df5fcc*/
//Synthetic comment -- diff --git a/tests/src/android/webkit/cts/CtsTestServer.java b/tests/src/android/webkit/cts/CtsTestServer.java
//Synthetic comment -- index 075f98d..49603bc 100644

//Synthetic comment -- @@ -79,6 +79,7 @@

public static final String FAVICON_PATH = "/favicon.ico";
public static final String USERAGENT_PATH = "/useragent.html";
public static final String ASSET_PREFIX = "/assets/";
public static final String FAVICON_ASSET_PATH = ASSET_PREFIX + "webkit/favicon.png";
public static final String APPCACHE_PATH = "/appcache.html";
//Synthetic comment -- @@ -350,6 +351,10 @@
return sb.toString();
}

public String getLastRequestUrl() {
return mLastQuery;
}
//Synthetic comment -- @@ -503,6 +508,10 @@
}
response.setEntity(createEntity("<html><head><title>" + agent + "</title></head>" +
"<body>" + agent + "</body></html>"));
} else if (path.equals(SHUTDOWN_PREFIX)) {
response = createResponse(HttpStatus.SC_OK);
// We cannot close the socket here, because we need to respond.







