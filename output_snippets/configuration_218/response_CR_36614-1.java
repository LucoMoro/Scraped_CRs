//<Beginning of snippet n. 0>
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UrlOpener {

    public static class CanceledByUserException extends Exception {
        private static final long serialVersionUID = -7669346110926032403L;

        private static Map<String, UserCredentials> sRealmCache = new HashMap<String, UserCredentials>();

        public void openUrl(String url, String proxyHost, String proxyPort) throws IOException {
            if (isValidProxy(proxyHost, proxyPort)) {
                setProxyProperties(proxyHost, proxyPort);
            }
            // other existing code logic...
        }

        private boolean isValidProxy(String proxyHost, String proxyPort) {
            return proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty();
        }

        private void setProxyProperties(String proxyHost, String proxyPort) {
            final String JAVA_PROP_HTTP_PROXY_HOST = "http.proxyHost";  
            final String JAVA_PROP_HTTP_PROXY_PORT = "http.proxyPort";  
            final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     
            final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     

            if (proxyHost != null && !proxyHost.isEmpty()) {
                if (System.getProperty(JAVA_PROP_HTTP_PROXY_HOST) == null) {
                    System.setProperty(JAVA_PROP_HTTP_PROXY_HOST, proxyHost);
                }
                if (System.getProperty(JAVA_PROP_HTTPS_PROXY_HOST) == null) {
                    System.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
                }
            }
            if (proxyPort != null && !proxyPort.isEmpty()) {
                if (System.getProperty(JAVA_PROP_HTTP_PROXY_PORT) == null) {
                    System.setProperty(JAVA_PROP_HTTP_PROXY_PORT, proxyPort);
                }
                if (System.getProperty(JAVA_PROP_HTTPS_PROXY_PORT) == null) {
                    System.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
                }
            }
        }
    }
}
//<End of snippet n. 0>