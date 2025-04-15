/*SDK Manager: fix download cache for file:// URLs.

(cherry picked from commit c2c63aba78b89bc8812790497bbfd2119be698ae)

Change-Id:I88a24b92234f60d0ad0a30b8219f534c67339010*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 84190e8..d31a286 100644

//Synthetic comment -- @@ -40,19 +40,23 @@
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
//Synthetic comment -- @@ -135,26 +139,86 @@
@Nullable Header[] headers)
throws IOException, CanceledByUserException {

try {
            return openWithHttpClient(url, monitor, headers);

        } catch (ClientProtocolException e) {
// If the protocol is not supported by HttpClient (e.g. file:///),
            // revert to the standard java.net.Url.open

            URL u = new URL(url);
            InputStream is = u.openStream();
            HttpResponse response = new BasicHttpResponse(
                    new ProtocolVersion(u.getProtocol(), 1, 0),
                    200, "");
            return Pair.of(is, response);
}
}

private static @NonNull Pair<InputStream, HttpResponse> openWithHttpClient(
@NonNull String url,
@NonNull ITaskMonitor monitor,
            Header[] headers)
throws IOException, ClientProtocolException, CanceledByUserException {
UserCredentials result = null;
String realm = null;
//Synthetic comment -- @@ -165,8 +229,8 @@
// create local execution context
HttpContext localContext = new BasicHttpContext();
final HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (Header header : headers) {
httpGet.addHeader(header);
}
}







