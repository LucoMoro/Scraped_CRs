//<Beginning of snippet n. 0>


import android.content.Context;
import android.database.Cursor;
import android.net.Proxy;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.client.config.RequestConfig;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

public class HttpClientProvider {
    
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;

    public CloseableHttpClient createHttpClient(Context context) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();

        String proxyHost = Proxy.getDefaultHost();
        int proxyPort = Proxy.getDefaultPort();
        if (proxyHost != null && proxyPort > 0) {
            return HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setConnectionTimeToLive(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .setProxy(new HttpHost(proxyHost, proxyPort))
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setSocketTimeout(SOCKET_TIMEOUT)
                            .setConnectTimeout(CONNECTION_TIMEOUT)
                            .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                            .build())
                    .build();
        } else {
            return HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setConnectionTimeToLive(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setSocketTimeout(SOCKET_TIMEOUT)
                            .setConnectTimeout(CONNECTION_TIMEOUT)
                            .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                            .build())
                    .build();
        }
    }
}


//<End of snippet n. 0>