/*SDK Manager: socket read timeout.

This adds a non-null default timeouts on the HttpClient
and UrlConnection instances. Most important is the
socket read timeout which seems to be infinite by default.
The default is at 1 minute for that one, with an option
to change it via an env variable. I might want to expose
it in the options dialog later (in another CL.)

SDK Bug/Request: 26382

Change-Id:I8b1da6505ea331d0520987ab8955f39cf123202b*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index c157982..1f44e5a 100644

//Synthetic comment -- @@ -37,6 +37,9 @@
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

//Synthetic comment -- @@ -81,6 +84,11 @@
private static Map<String, UserCredentials> sRealmCache =
new HashMap<String, UserCredentials>();

static {
if (DEBUG) {
Properties props = System.getProperties();
//Synthetic comment -- @@ -97,6 +105,18 @@
}
}
}
}

/**
//Synthetic comment -- @@ -244,6 +264,9 @@

URLConnection c = u.openConnection();

if (inHeaders != null) {
for (Header header : inHeaders) {
c.setRequestProperty(header.getName(), header.getValue());
//Synthetic comment -- @@ -297,8 +320,13 @@
UserCredentials result = null;
String realm = null;

// use the simple one
        final DefaultHttpClient httpClient = new DefaultHttpClient();

// create local execution context
HttpContext localContext = new BasicHttpContext();







