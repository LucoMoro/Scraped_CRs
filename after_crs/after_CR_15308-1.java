/*Use proxy for downloads.

Uses the new Proxy method getPreferredHttpHost to easily get correct
proxy.

Change-Id:If859ad03c2ed5a896c62592aa131d0e79d1b9c02*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 1ad1d4f..bfb82cf 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;

import android.content.ContentUris;
//Synthetic comment -- @@ -27,8 +28,9 @@
import android.content.Context;
import android.content.Intent;
import android.drm.mobile1.DrmRawContent;
import android.net.http.AndroidHttpClient;
import android.net.Proxy;
import android.net.Uri;
import android.os.FileUtils;
import android.os.PowerManager;
import android.os.Process;
//Synthetic comment -- @@ -179,6 +181,10 @@
*/
http_request_loop:
while (true) {
                // Set or unset proxy, which may have changed since last GET request.
                // setDefaultProxy() supports null as proxy parameter.
                ConnRouteParams.setDefaultProxy(client.getParams(),
                        Proxy.getPreferredHttpHost(mContext, mInfo.mUri));
// Prepares the request and fires it.
HttpGet request = new HttpGet(mInfo.mUri);








