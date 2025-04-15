/*SDK Manager: fix download cache for file:// URLs.

(cherry picked from commit c2c63aba78b89bc8812790497bbfd2119be698ae)

Change-Id:I88a24b92234f60d0ad0a30b8219f534c67339010*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 84190e8..d31a286 100644

//Synthetic comment -- @@ -40,19 +40,23 @@
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
//Synthetic comment -- @@ -135,26 +139,86 @@
@Nullable Header[] headers)
throws IOException, CanceledByUserException {

        Pair<InputStream, HttpResponse> result = null;

try {
            result = openWithHttpClient(url, monitor, headers);

        } catch (Exception e) {
// If the protocol is not supported by HttpClient (e.g. file:///),
            // revert to the standard java.net.Url.open.

            try {
                result = openWithUrl(url, headers);
            } catch (Exception e2) {
            }
}

        if (result == null) {
            HttpResponse outResponse = new BasicHttpResponse(
                    new ProtocolVersion("HTTP", 1, 0),
                    404, "");  //$NON-NLS-1$;
            result = Pair.of(null, outResponse);
        }
        return result;
    }

    private static Pair<InputStream, HttpResponse> openWithUrl(
            String url,
            Header[] inHeaders) throws IOException {
        URL u = new URL(url);

        URLConnection c = u.openConnection();

        if (inHeaders != null) {
            for (Header header : inHeaders) {
                c.setRequestProperty(header.getName(), header.getValue());
            }
        }

        // Trigger the access to the resource
        // (at which point setRequestProperty can't be used anymore.)
        int code = 200;

        if (c instanceof HttpURLConnection) {
            code = ((HttpURLConnection) c).getResponseCode();
        }

        // Get the input stream. That can fail for a file:// that doesn't exist
        // in which case we set the response code to 404.
        // Also we need a buffered input stream since the caller need to use is.reset().
        InputStream is = null;
        try {
            is = new BufferedInputStream(c.getInputStream());
        } catch (Exception ignore) {
            if (is == null && code == 200) {
                code = 404;
            }
        }

        HttpResponse outResponse = new BasicHttpResponse(
                new ProtocolVersion(u.getProtocol(),  1, 0), // make up the protocol version
                code, "");  //$NON-NLS-1$;

        Map<String, List<String>> outHeaderMap = c.getHeaderFields();

        for (Entry<String, List<String>> entry : outHeaderMap.entrySet()) {
            String name = entry.getKey();
            if (name != null) {
                List<String> values = entry.getValue();
                if (!values.isEmpty()) {
                    outResponse.setHeader(name, values.get(0));
                }
            }
        }

        return Pair.of(is, outResponse);
}

private static @NonNull Pair<InputStream, HttpResponse> openWithHttpClient(
@NonNull String url,
@NonNull ITaskMonitor monitor,
            Header[] inHeaders)
throws IOException, ClientProtocolException, CanceledByUserException {
UserCredentials result = null;
String realm = null;
//Synthetic comment -- @@ -165,8 +229,8 @@
// create local execution context
HttpContext localContext = new BasicHttpContext();
final HttpGet httpGet = new HttpGet(url);
        if (inHeaders != null) {
            for (Header header : inHeaders) {
httpGet.addHeader(header);
}
}







