//<Beginning of snippet n. 0>
private NetworkInfo getFilteredNetworkInfo(NetworkStateTracker tracker, int uid) {
    NetworkInfo info = tracker.getNetworkInfo();
    if (isNetworkBlocked(tracker, uid)) {
        info = new NetworkInfo(info);
        info.setDetailedState(DetailedState.BLOCKED, null, null);
    } else if (info.getDetailedState() == DetailedState.CONNECTED && tracker.isBackgroundDataRestricted()) {
        info.setDetailedState(DetailedState.BLOCKED, null, null);
    }
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
    if (state.networkInfo.isConnected()) {
        final String iface = state.linkProperties.getInterfaceName();
        final NetworkIdentity ident = NetworkIdentity.buildNetworkIdentity(mContext, state);
        if (!isNetworkBlocked(tracker, ident.getUid())) {
            networks.put(ident, iface);
        }
    }
}
//<End of snippet n. 1>