//<Beginning of snippet n. 0>


import android.content.Context;
import android.database.Cursor;
import android.net.Proxy;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpClient {

    private Context context;
    
    public MyHttpClient(Context context) {
        this.context = context;
    }

    public HttpURLConnection getHttpClient() throws Exception {
        String proxyAddress = Proxy.getDefaultHost();
        int proxyPort = Proxy.getDefaultPort();

        URL url = new URL("http://example.com"); // The target URL to connect to
        HttpURLConnection connection;

        if (proxyAddress != null && proxyPort != -1) {
            connection = (HttpURLConnection) new URL(url.toString()).openConnection(new java.net.Proxy(java.net.Proxy.Type.HTTP, new java.net.InetSocketAddress(proxyAddress, proxyPort)));
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(timeout);
        connection.setRequestProperty("Accept", "application/json");

        return connection;
    }
}


//<End of snippet n. 0>