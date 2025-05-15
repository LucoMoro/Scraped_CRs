//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.Args;

public class MyHttpClient {

    private static final int CONNECTION_TIMEOUT = 10000; // Example timeout value
    private static final int SOCKET_TIMEOUT = 10000; // Example timeout value

    public CloseableHttpClient createHttpClient(Context context) {
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        CloseableHttpClient client = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .build())
            .build();

        return client;
    }
}


//<End of snippet n. 0>