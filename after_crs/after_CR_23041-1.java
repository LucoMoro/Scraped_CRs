/*First pass tying into per-interface DNS cache

Change-Id:Iacce225d392786e37fa2dbf9647045fca0b70e64*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index 555975f..3e3c797 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkManagementService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
//Synthetic comment -- @@ -111,6 +112,9 @@

private boolean mTestMode;
private static ConnectivityService sServiceInstance;

    private INetworkManagementService mNetd;

private static final int ENABLED  = 1;
private static final int DISABLED = 0;

//Synthetic comment -- @@ -1278,6 +1282,9 @@
}

void systemReady() {
        IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
        mNetd = INetworkManagementService.Stub.asInterface(b);

synchronized(this) {
mSystemReady = true;
if (mInitialBroadcast != null) {
//Synthetic comment -- @@ -1470,6 +1477,11 @@
NetworkStateTracker nt = mNetTrackers[netType];
if (nt != null && nt.getNetworkInfo().isConnected() && !nt.isTeardownRequested()) {
String[] dnsList = nt.getNameServers();
            try {
                mNetd.setDnsServersForInterface(Integer.toString(netType), dnsList);
            } catch (IllegalStateException e) {
                Slog.e(TAG, "exception setting dns servers: " + e);
            }
if (mNetAttributes[netType].isDefault()) {
int j = 1;
for (String dns : dnsList) {
//Synthetic comment -- @@ -1481,6 +1493,10 @@
SystemProperties.set("net.dns" + j++, dns);
}
}
                try {
                    mNetd.setDefaultInterfaceForDns(Integer.toString(netType));
                } catch (IllegalStateException e) {
                    Slog.e(TAG, "exception setting default dns interface: " + e);}
for (int k=j ; k<mNumDnsEntries; k++) {
if (DBG) Slog.d(TAG, "erasing net.dns" + k);
SystemProperties.set("net.dns" + k, "");







