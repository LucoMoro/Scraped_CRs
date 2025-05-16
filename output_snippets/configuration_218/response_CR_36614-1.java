//<Beginning of snippet n. 0>
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* This class holds methods for adding URLs management.
*/
public class UrlOpener {

    public static class CanceledByUserException extends Exception {
        private static final long serialVersionUID = -7669346110926032403L;

        private static Map<String, UserCredentials> sRealmCache = new HashMap<String, UserCredentials>();

        /**
        * Opens a URL. It can be a simple URL or one which requires basic
        * authentication.
        httpClient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
        httpClient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, authpref);

        boolean trying = true;
        // loop while the response is being fetched
        while (trying) {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            int statusCode = response.getStatusLine().getStatusCode();

            // check whether any authentication is required
            AuthState authenticationState = null;
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                // implementation continued...
            }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
            reason = "HTTPS SSL error. You might want to force download through HTTP in the settings.";
            mFetchError += ": HTTPS SSL error";
        } else if (exception[0].getMessage() != null) {
            reason = exception[0].getMessage();
        } else {
            // We don't know what's wrong. Let's give the exception class at least.
            reason = String.format("Unknown (%1$s)", exception[0].getClass().getName());
        }

        monitor.logError("Failed to fetch URL %1$s, reason: %2$s", url, reason);
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
        final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_HOST, proxyHost);
            props.setProperty(JAVA_PROP_HTTP_PROXY_PORT, proxyPort);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
        } else {
            System.out.println("Proxy host or port not set; defaults will be retained.");
        }
    }
//<End of snippet n. 2>