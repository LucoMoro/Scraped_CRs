/*Don't perform DNS lookup in android.net.Proxy.isLocalHost

This fixes degrade introduced by:
Switch to using public APIs instead of private ones.
536ff5a6d700a80dbd75adb737ec4b560fbed2dc

Change-Id:I63cbea82d85d55d933bcfc9e7a311d1aa2324955*/




//Synthetic comment -- diff --git a/core/java/android/net/Proxy.java b/core/java/android/net/Proxy.java
//Synthetic comment -- index 22c30a5..c1fa5b4 100644

//Synthetic comment -- @@ -24,9 +24,7 @@
import android.provider.Settings;
import android.util.Log;

import java.net.URI;

import junit.framework.Assert;

//Synthetic comment -- @@ -162,15 +160,15 @@
final URI uri = URI.create(url);
final String host = uri.getHost();
if (host != null) {
                // TODO: InetAddress.isLoopbackAddress should be used to check
                // for localhost. However no public factory methods exist which
                // can be used without triggering DNS lookup if host is not localhost.
                if (host.equalsIgnoreCase("localhost") ||
                        host.equals("127.0.0.1") ||
                        host.equals("[::1]")) {
return true;
}
}
} catch (IllegalArgumentException iex) {
// Ignore (URI.create)
}







