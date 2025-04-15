/*Background data allowed with Restrict Background data setting enabled

When open SUPL or DUN on restrict back ground data enabled, Gmail
was received unexpectedly.
The rootcause of this issue is that the shared rmnet0 really is in
a CONNECTED but BLOCKED state in this case. However, it was treated
as a DISCONNECTED state, and the restrict iprule on rmnet0 was removed.

Change-Id:I0a9156683656eff3e7a7c41b9d5b9861e6f2cf52*/
//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index b7dc4a2..8852d46 100644

//Synthetic comment -- @@ -742,7 +742,7 @@
*/
private NetworkInfo getFilteredNetworkInfo(NetworkStateTracker tracker, int uid) {
NetworkInfo info = tracker.getNetworkInfo();
        if (isNetworkBlocked(tracker, uid)) {
// network is blocked; clone and override state
info = new NetworkInfo(info);
info.setDetailedState(DetailedState.BLOCKED, null, null);








//Synthetic comment -- diff --git a/services/java/com/android/server/net/NetworkPolicyManagerService.java b/services/java/com/android/server/net/NetworkPolicyManagerService.java
//Synthetic comment -- index 8c0f1e0..f271805 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.ConnectivityManager.TYPE_WIMAX;
import static android.net.NetworkPolicy.LIMIT_DISABLED;
import static android.net.NetworkPolicy.SNOOZE_NEVER;
import static android.net.NetworkPolicy.WARNING_DISABLED;
//Synthetic comment -- @@ -773,7 +774,8 @@
final HashMap<NetworkIdentity, String> networks = Maps.newHashMap();
for (NetworkState state : states) {
// stash identity and iface away for later use
            if (state.networkInfo.isConnected()) {
final String iface = state.linkProperties.getInterfaceName();
final NetworkIdentity ident = NetworkIdentity.buildNetworkIdentity(mContext, state);
networks.put(ident, iface);







