/*Use new android.net.Proxy method to select HTTP proxy

Change-Id:I25345b08332bff1dad845160f053ed16103179db*/
//Synthetic comment -- diff --git a/src/com/android/exchange/EasSyncService.java b/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index 3eb48c6..1d3959b 100644

//Synthetic comment -- @@ -78,6 +78,7 @@
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
//Synthetic comment -- @@ -1125,7 +1126,8 @@
HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
HttpConnectionParams.setSoTimeout(params, timeout);
HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpClient client = new DefaultHttpClient(getClientConnectionManager(), params);
return client;
}








