//<Beginning of snippet n. 0>


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import android.net.Proxy;
import org.apache.http.client.config.RequestConfig;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

Proxy proxy = Proxy.NO_PROXY; // or specify your proxy
RequestConfig config = RequestConfig.custom()
        .setSocketTimeout(5000)
        .setConnectTimeout(5000)
        .setProxy(proxy)
        .build();

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, new TrustManager[]{
    new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
        public X509Certificate[] getAcceptedIssuers() { return null; }
    }
}, new java.security.SecureRandom());

CloseableHttpClient client = HttpClientBuilder.create()
        .setDefaultRequestConfig(config)
        .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
        .build();

return client;


//<End of snippet n. 0>