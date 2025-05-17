//<Beginning of snippet n. 0>
private NetworkInfo getFilteredNetworkInfo(NetworkStateTracker tracker, int uid) {
    NetworkInfo info = tracker.getNetworkInfo();
    if (info == null) {
        Log.e("NetworkState", "Failed to retrieve network information");
        return null;
    }
    if (isNetworkBlocked(tracker, uid)) {
        info = new NetworkInfo(info);
        info.setDetailedState(DetailedState.BLOCKED, null, null);
        Log.d("NetworkState", "Network is CONNECTED but BLOCKED");
    } else if (info.getDetailedState() == DetailedState.CONNECTED) {
        Log.d("NetworkState", "Network is CONNECTED");
    }
    return info;
}

private boolean isNetworkBlocked(NetworkStateTracker tracker, int uid) {
    NetworkInfo info = tracker.getNetworkInfo();
    if (info == null) {
        Log.e("NetworkState", "Failed to retrieve network information for blocking check");
        return false;
    }
    boolean blocked = /* logic to determine if network is blocked based on uid */;
    if (blocked && info.getDetailedState() == DetailedState.CONNECTED) {
        Log.d("NetworkState", "Network is CONNECTED but BLOCKED");
    }
    return blocked;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.ConnectivityManager.TYPE_WIMAX;
import static android.net.NetworkPolicy.LIMIT_DISABLED;
import static android.net.NetworkPolicy.SNOOZE_NEVER;
import static android.net.NetworkPolicy.WARNING_DISABLED;
final HashMap<NetworkIdentity, String> networks = Maps.newHashMap();
for (NetworkState state : states) {
    if (state.networkInfo != null) {
        if (state.networkInfo.isConnected()) {
            final String iface = state.linkProperties.getInterfaceName();
            final NetworkIdentity ident = NetworkIdentity.buildNetworkIdentity(mContext, state);
            networks.put(ident, iface);
        } else {
            Log.w("NetworkState", "Network is DISCONNECTED");
        }
    } else {
        Log.w("NetworkState", "Network information is null");
    }
}
//<End of snippet n. 1>