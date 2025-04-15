/*Fix cannot create EAS account when using a APN with proxy

The platform supports APN with proxy by default. But it's just for
the scheme type is "http" and "https". For "httpts", it does not work.

Change-Id:I8be38a01c8125e4a04cad965bf5412fd9549b186*/




//Synthetic comment -- diff --git a/exchange2/src/com/android/exchange/EasSyncService.java b/exchange2/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index 657bc84..6480811 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.net.Proxy;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
//Synthetic comment -- @@ -77,12 +78,14 @@

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//Synthetic comment -- @@ -1239,6 +1242,17 @@
HttpConnectionParams.setSoTimeout(params, timeout);
HttpConnectionParams.setSocketBufferSize(params, 8192);
HttpClient client = new DefaultHttpClient(getClientConnectionManager(), params);

        if (mTrustSsl) {
            HttpHost httpHost = Proxy.getPreferredHttpHost(mContext, "httpts://" + mHostAddress);
            if (httpHost != null) {
                if (Eas.USER_LOG) {
                    userLog("Using proxy:" + httpHost.toString());
                }
                ConnRouteParams.setDefaultProxy(client.getParams(), httpHost);
            }
        }

return client;
}









//Synthetic comment -- diff --git a/exchange2/src/com/android/exchange/ExchangeService.java b/exchange2/src/com/android/exchange/ExchangeService.java
//Synthetic comment -- index be2ad65..5944cf7 100644

//Synthetic comment -- @@ -356,7 +356,10 @@

@Override
public Bundle autoDiscover(String userName, String password) throws RemoteException {
            EasSyncService service = new EasSyncService();
            // Must init the mContext that is used in getHttpClient.
            service.mContext = ExchangeService.this;
            return service.tryAutodiscover(userName, password);
}

@Override







