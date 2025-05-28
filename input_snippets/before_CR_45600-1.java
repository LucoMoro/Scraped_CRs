
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
HttpConnectionParams.setSoTimeout(params, timeout);
HttpConnectionParams.setSocketBufferSize(params, 8192);
HttpClient client = new DefaultHttpClient(getClientConnectionManager(), params);
return client;
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>



@Override
public Bundle autoDiscover(String userName, String password) throws RemoteException {
            return new EasSyncService().tryAutodiscover(userName, password);
}

@Override

//<End of snippet n. 1>








