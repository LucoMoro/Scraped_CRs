/*EthernetDataTracker: fix NFS no respond after suspend/resume issue

Mounting NFS as root filesystem, resume from suspend state, it will report
"nfs: server 10.192.*.* not responding, still trying". This is caused by
clearing ethernet interface addresses. so the netd will not pass ethernet's
link up message to framework. In this case, no need to clear it for we keep
on connecting to the same network and no need to do dhcp again. Here add one
propertity ro.ethernet.clear.ip (default is "yes") to solve this issue. If you
are using nfs as filesystem, you can set this propertity to "no" to fix it.

Signed-off-by: Jianzheng Zhou <jianzheng.zhou@freescale.com>*/




//Synthetic comment -- diff --git a/core/java/android/net/EthernetDataTracker.java b/core/java/android/net/EthernetDataTracker.java
//Synthetic comment -- index 0cc78c9..aad753d 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
//Synthetic comment -- @@ -45,6 +46,7 @@
private AtomicBoolean mDefaultRouteSet = new AtomicBoolean(false);

private static boolean mLinkUp;
    private static boolean mClearIp;
private LinkProperties mLinkProperties;
private LinkCapabilities mLinkCapabilities;
private NetworkInfo mNetworkInfo;
//Synthetic comment -- @@ -77,6 +79,7 @@
if (mIface.equals(iface) && mLinkUp != up) {
Log.d(TAG, "Interface " + iface + " link " + (up ? "up" : "down"));
mLinkUp = up;
                mClearIp = "yes".equals(SystemProperties.get("ro.ethernet.clear.ip", "yes"));
mTracker.mNetworkInfo.setIsAvailable(up);

// use DHCP
//Synthetic comment -- @@ -145,13 +148,15 @@
msg = mCsHandler.obtainMessage(EVENT_STATE_CHANGED, mNetworkInfo);
msg.sendToTarget();

	if (mClearIp) {
            IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
            INetworkManagementService service = INetworkManagementService.Stub.asInterface(b);
            try {
                service.clearInterfaceAddresses(mIface);
	        } catch (Exception e) {
                    Log.e(TAG, "Failed to clear addresses or disable ipv6" + e);
		}
	}
}

private void interfaceRemoved(String iface) {







