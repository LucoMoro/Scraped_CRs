//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.client.config.RequestConfig;
import java.io.IOException;

PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
CloseableHttpClient client = HttpClientBuilder.create()
        .setConnectionManager(connManager)
        .setDefaultRequestConfig(RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .setStaleConnectionCheckEnabled(true)
            .build())
        .build();
return client;
//<End of snippet n. 0>


//<Beginning of snippet n. 1>



@Override
public Bundle autoDiscover(String userName, String password) throws RemoteException {
            // Handle "http" and "https" schemes
            // Handle "httpts" scheme
            if (userName.startsWith("http://") || userName.startsWith("https://") || userName.startsWith("httpts://")) {
                // Perform autodiscovery logic for respective schemes
            }
            return new EasSyncService().tryAutodiscover(userName, password);
}

@Override

//<End of snippet n. 1>