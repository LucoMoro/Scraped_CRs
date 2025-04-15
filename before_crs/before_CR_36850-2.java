/*IPv4 Tethering and IPv4 service routes while connected via 464xlat

When connected via 464xlat, there are two network interfaces.  The main
interface (such as "rmnet0") handles IPv6 and the dependent interface ("clat")
handles IPv4.  MMS adds a route to force traffic out the cell network
interface, but if it's an IPv4 service, the route has to point out the clat
interface.  For IPv4 tethering, the iptables MASQUERADE rule needs to reference
the clat interface as well.

Signed-off-by: Daniel Drown <dan-android@drown.org>
Change-Id:Ie451e7b893e11a10e8aaf0dd4166cfd5ff3267e9*/
//Synthetic comment -- diff --git a/core/java/android/net/LinkProperties.java b/core/java/android/net/LinkProperties.java
//Synthetic comment -- index 75646fd..41939f6 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.net.ProxyProperties;
import android.os.Parcelable;
import android.os.Parcel;
import android.text.TextUtils;

import java.net.InetAddress;
//Synthetic comment -- @@ -52,6 +53,7 @@
public class LinkProperties implements Parcelable {

String mIfaceName;
private Collection<LinkAddress> mLinkAddresses = new ArrayList<LinkAddress>();
private Collection<InetAddress> mDnses = new ArrayList<InetAddress>();
private Collection<RouteInfo> mRoutes = new ArrayList<RouteInfo>();
//Synthetic comment -- @@ -80,6 +82,12 @@
public LinkProperties(LinkProperties source) {
if (source != null) {
mIfaceName = source.getInterfaceName();
for (LinkAddress l : source.getLinkAddresses()) mLinkAddresses.add(l);
for (InetAddress i : source.getDnses()) mDnses.add(i);
for (RouteInfo r : source.getRoutes()) mRoutes.add(r);
//Synthetic comment -- @@ -88,6 +96,7 @@
}
}

public void setInterfaceName(String iface) {
mIfaceName = iface;
}
//Synthetic comment -- @@ -96,6 +105,54 @@
return mIfaceName;
}

public Collection<InetAddress> getAddresses() {
Collection<InetAddress> addresses = new ArrayList<InetAddress>();
for (LinkAddress linkAddress : mLinkAddresses) {
//Synthetic comment -- @@ -136,6 +193,8 @@

public void clear() {
mIfaceName = null;
mLinkAddresses.clear();
mDnses.clear();
mRoutes.clear();
//Synthetic comment -- @@ -153,6 +212,12 @@
@Override
public String toString() {
String ifaceName = (mIfaceName == null ? "" : "InterfaceName: " + mIfaceName + " ");

String linkAddresses = "LinkAddresses: [";
for (LinkAddress addr : mLinkAddresses) linkAddresses += addr.toString() + ",";
//Synthetic comment -- @@ -357,6 +422,8 @@
*/
public int hashCode() {
return ((null == mIfaceName) ? 0 : mIfaceName.hashCode()
+ mLinkAddresses.size() * 31
+ mDnses.size() * 37
+ mRoutes.size() * 41
//Synthetic comment -- @@ -369,6 +436,8 @@
*/
public void writeToParcel(Parcel dest, int flags) {
dest.writeString(getInterfaceName());
dest.writeInt(mLinkAddresses.size());
for(LinkAddress linkAddress : mLinkAddresses) {
dest.writeParcelable(linkAddress, flags);
//Synthetic comment -- @@ -408,6 +477,14 @@
return null;
}
}
int addressCount = in.readInt();
for (int i=0; i<addressCount; i++) {
netProp.addLinkAddress((LinkAddress)in.readParcelable(null));








//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index b7dc4a2..ecc97b4 100644

//Synthetic comment -- @@ -1253,11 +1253,11 @@
}

private boolean addRoute(LinkProperties p, RouteInfo r, boolean toDefaultTable) {
        return modifyRoute(p.getInterfaceName(), p, r, 0, ADD, toDefaultTable);
}

private boolean removeRoute(LinkProperties p, RouteInfo r, boolean toDefaultTable) {
        return modifyRoute(p.getInterfaceName(), p, r, 0, REMOVE, toDefaultTable);
}

private boolean addRouteToAddress(LinkProperties lp, InetAddress addr) {
//Synthetic comment -- @@ -1283,13 +1283,13 @@
bestRoute = RouteInfo.makeHostRoute(addr, bestRoute.getGateway());
}
}
        return modifyRoute(lp.getInterfaceName(), lp, bestRoute, 0, doAdd, toDefaultTable);
}

    private boolean modifyRoute(String ifaceName, LinkProperties lp, RouteInfo r, int cycleCount,
boolean doAdd, boolean toDefaultTable) {
        if ((ifaceName == null) || (lp == null) || (r == null)) {
            if (DBG) log("modifyRoute got unexpected null: " + ifaceName + ", " + lp + ", " + r);
return false;
}

//Synthetic comment -- @@ -1297,6 +1297,16 @@
loge("Error modifying route - too much recursion");
return false;
}

if (r.isHostRoute() == false) {
RouteInfo bestRoute = RouteInfo.selectBestRoute(lp.getRoutes(), r.getGateway());
//Synthetic comment -- @@ -1309,7 +1319,7 @@
// route to it's gateway
bestRoute = RouteInfo.makeHostRoute(r.getGateway(), bestRoute.getGateway());
}
                modifyRoute(ifaceName, lp, bestRoute, cycleCount+1, doAdd, toDefaultTable);
}
}
if (doAdd) {








//Synthetic comment -- diff --git a/services/java/com/android/server/connectivity/Tethering.java b/services/java/com/android/server/connectivity/Tethering.java
//Synthetic comment -- index cc1df4f..9836bc1 100644

//Synthetic comment -- @@ -1323,7 +1323,7 @@
linkProperties = mConnService.getLinkProperties(upType);
} catch (RemoteException e) { }
if (linkProperties != null) {
                        iface = linkProperties.getInterfaceName();
String[] dnsServers = mDefaultDnsServers;
Collection<InetAddress> dnses = linkProperties.getDnses();
if (dnses != null) {







