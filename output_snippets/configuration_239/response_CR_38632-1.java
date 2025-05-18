//<Beginning of snippet n. 0>
private NetworkInfo getFilteredNetworkInfo(NetworkStateTracker tracker, int uid) {
    NetworkInfo info = tracker.getNetworkInfo();
    if (isNetworkBlocked(tracker, uid)) {
        info.setDetailedState(DetailedState.BLOCKED, null, null);
    } else if (info.getDetailedState() == DetailedState.DISCONNECTED || info.getDetailedState() == DetailedState.BLOCKED) {
        info.setDetailedState(DetailedState.CONNECTED, null, null);
    }
    return info;
}

private boolean isNetworkBlocked(NetworkStateTracker tracker, int uid) {
    if (tracker.isBlocked(uid)) {
        return true;
    }
    // Add additional conditions here to check other criteria for being blocked
    return false; 
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
synchronized (networks) {
    for (NetworkState state : states) {
        if (state.networkInfo.isConnected() && !isNetworkBlocked(state.tracker, state.uid)) {
            final String iface = state.linkProperties.getInterfaceName();
            final NetworkIdentity ident = NetworkIdentity.buildNetworkIdentity(mContext, state);
            networks.put(ident, iface);
        }
    }
}
//<End of snippet n. 1>