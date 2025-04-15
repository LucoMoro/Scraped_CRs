/*Broadcast PROXY_CHANGE_ACTION when preferred proxy changes

A client should only need to subscribe for PROXY_CHANGE_ACTION
broadcasts to get updates when the proxy changes.

Change-Id:I191ea74cc7a8ca900154c89036d47dd07d376103*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index 9edce20..5e7d287 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.net.NetworkInfo;
import android.net.NetworkStateTracker;
import android.net.NetworkUtils;
import android.net.Proxy;
import android.net.wifi.WifiStateTracker;
import android.os.Binder;
import android.os.Handler;
//Synthetic comment -- @@ -46,6 +47,8 @@

import com.android.server.connectivity.Tethering;

import org.apache.http.HttpHost;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
//Synthetic comment -- @@ -95,6 +98,8 @@

private int mNumDnsEntries;

    private HttpHost mCurrentProxy;

private boolean mTestMode;
private static ConnectivityService sServiceInstance;

//Synthetic comment -- @@ -954,6 +959,7 @@
*/
if (newNet != null && newNet.getNetworkInfo().isConnected()) {
sendConnectedBroadcast(newNet.getNetworkInfo());
            sendProxyChangeBroadcast();
}
}

//Synthetic comment -- @@ -1034,6 +1040,17 @@
return newNet;
}

    private void sendProxyChangeBroadcast() {
        // Only send if proxy has actually changed
        HttpHost proxy = Proxy.getPreferredHttpHost(mContext, null);
        if ((proxy == null && mCurrentProxy != null) ||
                (proxy != null && !proxy.equals(mCurrentProxy))) {
            if (DBG) Slog.v(TAG, "Sending PROXY_CHANGE_ACTION broadcast, proxy=" + proxy);
            mCurrentProxy = proxy;
            mContext.sendBroadcast(new Intent(Proxy.PROXY_CHANGE_ACTION));
        }
    }

private void sendConnectedBroadcast(NetworkInfo info) {
Intent intent = new Intent(ConnectivityManager.CONNECTIVITY_ACTION);
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
//Synthetic comment -- @@ -1111,6 +1128,7 @@
*/
if (newNet != null && newNet.getNetworkInfo().isConnected()) {
sendConnectedBroadcast(newNet.getNetworkInfo());
            sendProxyChangeBroadcast();
}
}

//Synthetic comment -- @@ -1176,6 +1194,7 @@
thisNet.updateNetworkSettings();
handleConnectivityChange();
sendConnectedBroadcast(info);
        sendProxyChangeBroadcast();
}

private void handleScanResultsAvailable(NetworkInfo info) {







