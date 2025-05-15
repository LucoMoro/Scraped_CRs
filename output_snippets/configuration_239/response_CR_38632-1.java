//<Beginning of snippet n. 0>
private NetworkInfo getFilteredNetworkInfo(NetworkStateTracker tracker, int uid) {
    NetworkInfo info = tracker.getNetworkInfo();
    if (isNetworkBlocked(tracker, uid) || (info.isConnected() && info.getDetailedState() == DetailedState.BLOCKED)) {
        // network is blocked; clone and override state
        info = new NetworkInfo(info);
        info.setDetailedState(DetailedState.BLOCKED, null, null);
    }
    // Ensure iprule is maintained even if CONNECTED but BLOCKED logic
    maintainIpRule(tracker, uid, info);
    return info;
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
    // stash identity and iface away for later use
    if (state.networkInfo.isConnected() || (state.networkInfo.getDetailedState() == DetailedState.BLOCKED)) {
        final String iface = state.linkProperties.getInterfaceName();
        final NetworkIdentity ident = NetworkIdentity.buildNetworkIdentity(mContext, state);
        networks.put(ident, iface);
    }
}
//<End of snippet n. 1>