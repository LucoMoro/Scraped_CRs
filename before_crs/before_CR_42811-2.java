/*net.dns.search : Set the system property net.dns.search

Set the system property net.dns.search to the domain name
provided by the DhcpInfo class when connected to the regular
wifi network.

Change-Id:I1a72676ce18d8e2ce49cf1762887b0f35e72b26c*/
//Synthetic comment -- diff --git a/core/java/android/net/DhcpInfoInternal.java b/core/java/android/net/DhcpInfoInternal.java
//Synthetic comment -- index f3508c1..385abb9 100644

//Synthetic comment -- @@ -117,6 +117,9 @@
} else {
Log.d(TAG, "makeLinkProperties with empty dns2!");
}
return p;
}

//Synthetic comment -- @@ -140,6 +143,10 @@
addRoute(route);
}
}
}

/**








//Synthetic comment -- diff --git a/core/java/android/net/LinkProperties.java b/core/java/android/net/LinkProperties.java
old mode 100644
new mode 100755
//Synthetic comment -- index 75646fd..9ba07ed

//Synthetic comment -- @@ -56,6 +56,7 @@
private Collection<InetAddress> mDnses = new ArrayList<InetAddress>();
private Collection<RouteInfo> mRoutes = new ArrayList<RouteInfo>();
private ProxyProperties mHttpProxy;

public static class CompareResult<T> {
public Collection<T> removed = new ArrayList<T>();
//Synthetic comment -- @@ -85,6 +86,7 @@
for (RouteInfo r : source.getRoutes()) mRoutes.add(r);
mHttpProxy = (source.getHttpProxy() == null)  ?
null : new ProxyProperties(source.getHttpProxy());
}
}

//Synthetic comment -- @@ -134,12 +136,20 @@
return mHttpProxy;
}

public void clear() {
mIfaceName = null;
mLinkAddresses.clear();
mDnses.clear();
mRoutes.clear();
mHttpProxy = null;
}

/**








//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
old mode 100644
new mode 100755
//Synthetic comment -- index ad1dfb2..6bd2972

//Synthetic comment -- @@ -2552,7 +2552,11 @@
String network = nt.getNetworkInfo().getTypeName();
synchronized (mDnsLock) {
if (!mDnsOverridden) {
                        changed = updateDns(network, p.getInterfaceName(), dnses, "");
}
}
} else {







