/*Supporting IPv4v6 and IPv6 bearer types for ATL

Change-Id:Ife8c72d9975ac58b9e4091cb95ad5a9763e01c3f*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index e3db559..82c3785 100755

//Synthetic comment -- @@ -36,6 +36,9 @@
import android.net.NetworkUtils;
import android.net.SntpClient;
import android.net.Uri;
import android.net.LinkProperties;
import android.net.LinkAddress;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -72,8 +75,10 @@
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.Collection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Inet6Address;

/**
* A GPS implementation of LocationProvider used by LocationManager.
//Synthetic comment -- @@ -524,15 +529,24 @@
apnName = defaultApn;
}
agpsConnInfo.mAPN = apnName;

                    LinkProperties lp = mConnMgr.getLinkProperties(agpsConnInfo.mCMConnType);
                    Collection<LinkAddress> las = lp.getLinkAddresses();
                    boolean isV4 = false;
                    boolean isV6 = false;
                    for (LinkAddress la : las) {
                        if (la.getAddress() instanceof Inet6Address) {
                            agpsConnInfo.mBearerType = agpsConnInfo.BEARER_IPV6;
                            isV6 = true;
                        } else {
                            agpsConnInfo.mBearerType = agpsConnInfo.BEARER_IPV4;
                            isV4 = true;
                        }
                        if (isV4 && isV6) {
                            agpsConnInfo.mBearerType = agpsConnInfo.BEARER_IPV4V6;
                            break;
                        }
                    }

if (agpsConnInfo.mIpAddr != null) {
boolean route_result;







