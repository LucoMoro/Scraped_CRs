//<Beginning of snippet n. 0>
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
import java.io.File;

AndroidDebugBridge.addDeviceChangeListener(listener);
}

public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
    AndroidDebugBridge.removeDeviceChangeListener(listener);
}

public static IDevice[] getDevices() {
    return bridge.getDevices();
}

public void checkServerStatus(IDevice device) {
    if (device.isOnline()) {
        try {
            device.executeShellCommand(buildIsServerRunningShellCommand(),
            new BooleanResultReader(result));
        } catch (IOException e) {
            Log.e(TAG, "IOException while checking server status", e);
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout checking status of view server on device " + device, e);
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error occurred while checking server status", e);
        } finally {
            // Cleanup resources if necessary
        }
    } else {
        Log.w(TAG, "Device " + device + " is offline.");
        // Notify user about device being offline
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
// This snippet has been integrated into the previous modification
//<End of snippet n. 1>