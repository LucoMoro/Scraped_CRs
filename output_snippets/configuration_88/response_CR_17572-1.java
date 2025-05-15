//<Beginning of snippet n. 0>
import android.content.Context;
import android.net.Proxy;
import android.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class HttpClientFactory {
    private Context context;
    
    public HttpClientFactory(Context context) {
        this.context = context;
    }

    public HttpURLConnection createHttpClient() throws IOException {
        String proxyHost = Proxy.getDefaultHost();
        int proxyPort = Proxy.getDefaultPort();
        
        URL url = new URL("http://your.api.endpoint"); // Replace with actual URL
        HttpURLConnection connection;
        
        if (proxyHost != null) {
            connection = (HttpURLConnection) url.openConnection(new java.net.Proxy(java.net.Proxy.Type.HTTP, new java.net.InetSocketAddress(proxyHost, proxyPort)));
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(timeout);
        connection.setRequestProperty("User-Agent", "YourUserAgent"); // Optional
        return connection;
    }
}
//<End of snippet n. 0>