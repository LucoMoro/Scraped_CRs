/*gltrace: Kill app manually instead of using am start -S

Change-Id:I5a0ec359145e815a2437ef766843d2cb5267c795*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 9b7544d..2b07680 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceUnixSocketNamespace;
import com.android.ddmlib.IShellOutputReceiver;
//Synthetic comment -- @@ -168,8 +169,10 @@
private void startActivity(IDevice device, String appName)
throws TimeoutException, AdbCommandRejectedException,
ShellCommandUnresponsiveException, IOException, InterruptedException {
        killApp(device, appName); // kill app if it is already running

String startAppCmd = String.format(
                "am start --opengl-trace %s -a android.intent.action.MAIN -c android.intent.category.LAUNCHER", //$NON-NLS-1$
appName);

Semaphore launchCompletionSempahore = new Semaphore(0);
//Synthetic comment -- @@ -187,6 +190,13 @@
}
}

    private void killApp(IDevice device, String appName) {
        Client client = device.getClient(appName);
        if (client != null) {
            client.kill();
        }
    }

private void setupForwarding(IDevice device, int i)
throws TimeoutException, AdbCommandRejectedException, IOException {
device.createForward(i, GLTRACE_UDS, DeviceUnixSocketNamespace.ABSTRACT);







