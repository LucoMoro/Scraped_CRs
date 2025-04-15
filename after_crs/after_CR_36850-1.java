/*IPv4 Tethering and IPv4 service routes while connected via 464xlat

When connected via 464xlat, there are two network interfaces.  The main
interface (such as "rmnet0") handles IPv6 and the dependent interface ("clat")
handles IPv4.  MMS adds a route to force traffic out the cell network
interface, but if it's an IPv4 service, the route has to point out the clat
interface.  For IPv4 tethering, the iptables MASQUERADE rule needs to reference
the clat interface as well.

Change-Id:Ie451e7b893e11a10e8aaf0dd4166cfd5ff3267e9*/




//Synthetic comment -- diff --git a/core/java/android/net/LinkProperties.java b/core/java/android/net/LinkProperties.java
//Synthetic comment -- index 75646fd..41939f6 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.net.ProxyProperties;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.SystemProperties;
import android.text.TextUtils;

import java.net.InetAddress;
//Synthetic comment -- @@ -52,6 +53,7 @@
public class LinkProperties implements Parcelable {

String mIfaceName;
    private String mSecondaryIfaceIPv4Name, mSecondaryIfaceIPv6Name;
private Collection<LinkAddress> mLinkAddresses = new ArrayList<LinkAddress>();
private Collection<InetAddress> mDnses = new ArrayList<InetAddress>();
private Collection<RouteInfo> mRoutes = new ArrayList<RouteInfo>();
//Synthetic comment -- @@ -80,6 +82,12 @@
public LinkProperties(LinkProperties source) {
if (source != null) {
mIfaceName = source.getInterfaceName();
	    if(source.hasSecondaryIPv4Interface()) {
		mSecondaryIfaceIPv4Name = source.getIPv4InterfaceName();
	    }
	    if(source.hasSecondaryIPv6Interface()) {
		mSecondaryIfaceIPv6Name = source.getIPv6InterfaceName();
	    }
for (LinkAddress l : source.getLinkAddresses()) mLinkAddresses.add(l);
for (InetAddress i : source.getDnses()) mDnses.add(i);
for (RouteInfo r : source.getRoutes()) mRoutes.add(r);
//Synthetic comment -- @@ -88,6 +96,7 @@
}
}

    // primary physical interface
public void setInterfaceName(String iface) {
mIfaceName = iface;
}
//Synthetic comment -- @@ -96,6 +105,54 @@
return mIfaceName;
}

    // secondary physical or logical interface carrying IPv4 traffic
    public void setSecondaryIPv4InterfaceName(String iface) {
	mSecondaryIfaceIPv4Name = iface;
    }

    public boolean checkForClat() {
	if(mSecondaryIfaceIPv4Name != null) {
	    return mSecondaryIfaceIPv4Name.equals("clat");
	}
	String ipv4_compat = SystemProperties.get("net.ipv4.compat");
	if(ipv4_compat != null && ipv4_compat.equals("clat")) {
	    mSecondaryIfaceIPv4Name = "clat";
	    return true;
	}
	return false;
    }

    public String getIPv4InterfaceName() {
	checkForClat();
	if(mSecondaryIfaceIPv4Name == null) {
	    return mIfaceName;
	} else {
	    return mSecondaryIfaceIPv4Name;
	}
    }

    public boolean hasSecondaryIPv4Interface() {
	checkForClat();
	return mSecondaryIfaceIPv4Name == null;
    }

    // secondary physical or logical interface carrying IPv6 traffic
    public void setSecondaryIPv6InterfaceName(String iface) {
	mSecondaryIfaceIPv6Name = iface;
    }

    public String getIPv6InterfaceName() {
	if(mSecondaryIfaceIPv6Name == null) {
	    return mIfaceName;
	} else {
	    return mSecondaryIfaceIPv6Name;
	}
    }

    public boolean hasSecondaryIPv6Interface() {
	return mSecondaryIfaceIPv6Name == null;
    }

public Collection<InetAddress> getAddresses() {
Collection<InetAddress> addresses = new ArrayList<InetAddress>();
for (LinkAddress linkAddress : mLinkAddresses) {
//Synthetic comment -- @@ -136,6 +193,8 @@

public void clear() {
mIfaceName = null;
	mSecondaryIfaceIPv4Name = null;
	mSecondaryIfaceIPv6Name = null;
mLinkAddresses.clear();
mDnses.clear();
mRoutes.clear();
//Synthetic comment -- @@ -153,6 +212,12 @@
@Override
public String toString() {
String ifaceName = (mIfaceName == null ? "" : "InterfaceName: " + mIfaceName + " ");
	if(mSecondaryIfaceIPv4Name != null) {
	    ifaceName += "[v4: "+mSecondaryIfaceIPv4Name+"] ";
	}
	if(mSecondaryIfaceIPv6Name != null) {
	    ifaceName += "[v6: "+mSecondaryIfaceIPv6Name+"] ";
	}

String linkAddresses = "LinkAddresses: [";
for (LinkAddress addr : mLinkAddresses) linkAddresses += addr.toString() + ",";
//Synthetic comment -- @@ -357,6 +422,8 @@
*/
public int hashCode() {
return ((null == mIfaceName) ? 0 : mIfaceName.hashCode()
		+ ((mSecondaryIfaceIPv4Name == null) ? 0 : mSecondaryIfaceIPv4Name.hashCode())
		+ ((mSecondaryIfaceIPv6Name == null) ? 0 : mSecondaryIfaceIPv6Name.hashCode())
+ mLinkAddresses.size() * 31
+ mDnses.size() * 37
+ mRoutes.size() * 41
//Synthetic comment -- @@ -369,6 +436,8 @@
*/
public void writeToParcel(Parcel dest, int flags) {
dest.writeString(getInterfaceName());
	dest.writeString(mSecondaryIfaceIPv4Name);
	dest.writeString(mSecondaryIfaceIPv6Name);
dest.writeInt(mLinkAddresses.size());
for(LinkAddress linkAddress : mLinkAddresses) {
dest.writeParcelable(linkAddress, flags);
//Synthetic comment -- @@ -408,6 +477,14 @@
return null;
}
}
		String v4iface = in.readString();
		if(v4iface != null) {
		    netProp.setSecondaryIPv4InterfaceName(v4iface);
		}
		String v6iface = in.readString();
		if(v6iface != null) {
		    netProp.setSecondaryIPv6InterfaceName(v6iface);
		}
int addressCount = in.readInt();
for (int i=0; i<addressCount; i++) {
netProp.addLinkAddress((LinkAddress)in.readParcelable(null));








//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index b7dc4a2..ecc97b4 100644

//Synthetic comment -- @@ -1253,11 +1253,11 @@
}

private boolean addRoute(LinkProperties p, RouteInfo r, boolean toDefaultTable) {
        return modifyRoute(p, r, 0, ADD, toDefaultTable);
}

private boolean removeRoute(LinkProperties p, RouteInfo r, boolean toDefaultTable) {
        return modifyRoute(p, r, 0, REMOVE, toDefaultTable);
}

private boolean addRouteToAddress(LinkProperties lp, InetAddress addr) {
//Synthetic comment -- @@ -1283,13 +1283,13 @@
bestRoute = RouteInfo.makeHostRoute(addr, bestRoute.getGateway());
}
}
        return modifyRoute(lp, bestRoute, 0, doAdd, toDefaultTable);
}

    private boolean modifyRoute(LinkProperties lp, RouteInfo r, int cycleCount,
boolean doAdd, boolean toDefaultTable) {
        if ((lp == null) || (r == null)) {
            if (DBG) log("modifyRoute got unexpected null: " + lp + ", " + r);
return false;
}

//Synthetic comment -- @@ -1297,6 +1297,16 @@
loge("Error modifying route - too much recursion");
return false;
}
	String ifaceName;
	if(r.getDestination().getAddress() instanceof Inet4Address) {
	    ifaceName = lp.getIPv4InterfaceName();
	} else {
	    ifaceName = lp.getIPv6InterfaceName();
	}
	if(ifaceName == null) {
	    loge("Error modifying route - no interface name");
	    return false;
	}

if (r.isHostRoute() == false) {
RouteInfo bestRoute = RouteInfo.selectBestRoute(lp.getRoutes(), r.getGateway());
//Synthetic comment -- @@ -1309,7 +1319,7 @@
// route to it's gateway
bestRoute = RouteInfo.makeHostRoute(r.getGateway(), bestRoute.getGateway());
}
                modifyRoute(lp, bestRoute, cycleCount+1, doAdd, toDefaultTable);
}
}
if (doAdd) {








//Synthetic comment -- diff --git a/services/java/com/android/server/connectivity/Tethering.java b/services/java/com/android/server/connectivity/Tethering.java
//Synthetic comment -- index cc1df4f..9836bc1 100644

//Synthetic comment -- @@ -1323,7 +1323,7 @@
linkProperties = mConnService.getLinkProperties(upType);
} catch (RemoteException e) { }
if (linkProperties != null) {
                        iface = linkProperties.getIPv4InterfaceName();
String[] dnsServers = mDefaultDnsServers;
Collection<InetAddress> dnses = linkProperties.getDnses();
if (dnses != null) {







