//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.client.config.RequestConfig;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class HttpClientWrapper {

    private static final int CONNECTION_TIMEOUT = 3000; // Example value
    private static final int SOCKET_TIMEOUT = 3000; // Example value

    public CloseableHttpClient createHttpClient(Context context) {
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setSocketBufferSize(8192)
            .build();

        CloseableHttpClient client = HttpClients.custom()
            .setDefaultRequestConfig(config)
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build();

        return client;
    }

    private Proxy getProxy(Context context) {
        String proxyHost = android.net.Proxy.getDefaultHost();
        int proxyPort = android.net.Proxy.getDefaultPort();
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }
}

//<End of snippet n. 0>