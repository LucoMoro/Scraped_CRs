/*Don't perform DNS lookup in android.net.Proxy.isLocalHost

This fixes degrade introduced by:
Switch to using public APIs instead of private ones.
536ff5a6d700a80dbd75adb737ec4b560fbed2dc

Change-Id:I63cbea82d85d55d933bcfc9e7a311d1aa2324955*/
//Synthetic comment -- diff --git a/core/java/android/net/Proxy.java b/core/java/android/net/Proxy.java
//Synthetic comment -- index 22c30a5..7f46920 100644

//Synthetic comment -- @@ -24,9 +24,7 @@
import android.provider.Settings;
import android.util.Log;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import junit.framework.Assert;

//Synthetic comment -- @@ -162,15 +160,16 @@
final URI uri = URI.create(url);
final String host = uri.getHost();
if (host != null) {
                if (host.equalsIgnoreCase("localhost")) {
                    return true;
                }
                if (InetAddress.getByName(host).isLoopbackAddress()) {
return true;
}
}
        } catch (UnknownHostException uex) {
            // Ignore (INetworkSystem.ipStringToByteArray)
} catch (IllegalArgumentException iex) {
// Ignore (URI.create)
}







