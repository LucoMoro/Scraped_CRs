/*Fix build.

Switch to using public APIs instead of private ones.

Change-Id:Ia3468d94b3dadeb6a000ce419ee08f7e7ba5dc67*/
//Synthetic comment -- diff --git a/core/java/android/net/Proxy.java b/core/java/android/net/Proxy.java
//Synthetic comment -- index 39d4ac1..22c30a5 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package android.net;

import org.apache.harmony.luni.platform.INetworkSystem;
import org.apache.harmony.luni.platform.Platform;
import org.apache.http.HttpHost;

import android.content.ContentResolver;
//Synthetic comment -- @@ -44,8 +42,6 @@
static final public String PROXY_CHANGE_ACTION =
"android.intent.action.PROXY_CHANGE";

    static final private INetworkSystem NETIMPL = Platform.getNetworkSystem();

/**
* Return the proxy host set by the user.
* @param ctx A Context used to get the settings for the proxy host.
//Synthetic comment -- @@ -169,8 +165,7 @@
if (host.equalsIgnoreCase("localhost")) {
return true;
}
                if (InetAddress.getByAddress(NETIMPL.ipStringToByteArray(host))
                        .isLoopbackAddress()) {
return true;
}
}







