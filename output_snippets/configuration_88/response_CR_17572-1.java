//<Beginning of snippet n. 0>


import android.content.Context;
import android.net.Proxy;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import org.apache.http.HttpHost;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpClientProvider {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 5000;
    private static final Logger LOGGER = Logger.getLogger(HttpClientProvider.class.getName());

    private static class StrictHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
        }
    }

    public CloseableHttpClient createHttpClient(Context context) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        try {
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                SSLContextBuilder.create().setProtocol("TLSv1.2").build(),
                new StrictHostnameVerifier()
            );

            CloseableHttpClient client = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setProxy(new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort()))
                    .setSSLSocketFactory(socketFactory)
                    .build();
            return client;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException occurred while creating HttpClient", e);
            // Handle exception appropriately here
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "General exception occurred", e);
            // Handle exception appropriately here
            return null;
        }
    }
}

//<End of snippet n. 0>