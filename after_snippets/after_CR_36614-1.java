
//<Beginning of snippet n. 0>


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
* This class holds methods for adding URLs management.
*/
public class UrlOpener {

    private static final boolean DEBUG =
        System.getenv("ANDROID_DEBUG_URL_OPENER") != null; //$NON-NLS-1$

public static class CanceledByUserException extends Exception {
private static final long serialVersionUID = -7669346110926032403L;

private static Map<String, UserCredentials> sRealmCache =
new HashMap<String, UserCredentials>();

    static {
        if (DEBUG) {
            Properties props = System.getProperties();
            for (String key : new String[] {
                    "http.proxyHost",           //$NON-NLS-1$
                    "http.proxyPort",           //$NON-NLS-1$
                    "https.proxyHost",          //$NON-NLS-1$
                    "https.proxyPort" }) {      //$NON-NLS-1$
                String prop = props.getProperty(key);
                if (prop != null) {
                    System.out.printf(
                            "SdkLib.UrlOpener Java.Prop %s='%s'\n",   //$NON-NLS-1$
                            key, prop);
                }
            }
        }
    }

/**
* Opens a URL. It can be a simple URL or one which requires basic
* authentication.
httpClient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
httpClient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, authpref);

        if (DEBUG) {
            try {
                ProxySelector sel = routePlanner.getProxySelector();
                if (sel != null) {
                    List<Proxy> list = sel.select(new URI(url));
                    System.out.printf(
                            "SdkLib.UrlOpener:\n  Connect to: %s\n  Proxy List: %s\n", //$NON-NLS-1$
                            url,
                            list == null ? "(null)" : Arrays.toString(list.toArray()));//$NON-NLS-1$
                }
            } catch (Exception e) {
                System.out.printf(
                        "SdkLib.UrlOpener: Failed to get proxy info for %s: %s\n", //$NON-NLS-1$
                        url, e.toString());
            }
        }

boolean trying = true;
// loop while the response is being fetched
while (trying) {
HttpResponse response = httpClient.execute(httpGet, localContext);
int statusCode = response.getStatusLine().getStatusCode();

            if (DEBUG) {
                System.out.printf("  Status: %d", statusCode); //$NON-NLS-1$
            }

// check whether any authentication is required
AuthState authenticationState = null;
if (statusCode == HttpStatus.SC_UNAUTHORIZED) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


reason = "HTTPS SSL error. You might want to force download through HTTP in the settings.";
mFetchError += ": HTTPS SSL error";
} else if (exception[0].getMessage() != null) {
                reason =
                    exception[0].getClass().getSimpleName().replace("Exception", "") //$NON-NLS-1$ //$NON-NLS-2$
                    + ' '
                    + exception[0].getMessage();
} else {
                reason = exception[0].toString();
}

monitor.logError("Failed to fetch URL %1$s, reason: %2$s", url, reason);

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        // Only change the proxy if have something in the preferences.
        // Do not erase the default settings by empty values.
        if (proxyHost != null && proxyHost.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        }
        if (proxyPort != null && proxyPort.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
        }
}

}

//<End of snippet n. 2>








