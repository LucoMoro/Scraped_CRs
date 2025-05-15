
//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.net.Proxy;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
HttpConnectionParams.setSoTimeout(params, timeout);
HttpConnectionParams.setSocketBufferSize(params, 8192);
        DefaultHttpClient client = new DefaultHttpClient(getClientConnectionManager(), params);
        Proxy.setHttpClientProxy(client);
return client;
}


//<End of snippet n. 0>








