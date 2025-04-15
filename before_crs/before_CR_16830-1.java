/*Browser fails after mobile network -> wifi changed

When connection changes from mobile network using proxy
to wifi during an active browser session, the cashed
proxy setting will not update resulting in an attempt
to connect via wifi using a proxy.

Change-Id:Ic6b4e356ebd3a67a032fd91300192042e89017a0*/
//Synthetic comment -- diff --git a/core/java/android/net/http/RequestQueue.java b/core/java/android/net/http/RequestQueue.java
//Synthetic comment -- index a31639f..acd31ed 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.WebAddress;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
//Synthetic comment -- @@ -232,11 +233,27 @@
new BroadcastReceiver() {
@Override
public void onReceive(Context ctx, Intent intent) {
                            setProxyConfig();
}
};
            mContext.registerReceiver(mProxyChangeReceiver,
                                      new IntentFilter(Proxy.PROXY_CHANGE_ACTION));
}
// we need to resample the current proxy setup
setProxyConfig();







