//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;

CloseableHttpClient client = HttpClients.custom()
        .setSSLContext(SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build())
        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        .build();

String scheme = uri.getScheme();
if ("httpts".equalsIgnoreCase(scheme)) {
    // Implement handling for httpts, establish a connection and manage errors
}

return client;
//<End of snippet n. 0>




``` 

```java
//<Beginning of snippet n. 1>




@Override
public Bundle autoDiscover(String userName, String password) throws RemoteException {
    return new EasSyncService().tryAutodiscover(userName, password);
}

@Override

//<End of snippet n. 1>
