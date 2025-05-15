//<Beginning of snippet n. 0>
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
import java.io.File;

public class DeviceManager {
    private static final String TAG = "DeviceManager";
    private static AndroidDebugBridge bridge;

    public static void addDeviceListener(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.addDeviceChangeListener(listener);
    }

    public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

    public static IDevice[] getDevices() {
        return bridge.getDevices();
    }

    public static void checkServerStatus(IDevice device, boolean isUserBuild) {
        if (device.isOnline()) {
            if (isUserBuild) {
                try {
                    device.executeShellCommand(buildIsServerRunningShellCommand(),
                        new BooleanResultReader(result));
                } catch (IOException e) {
                    Log.e(TAG, "IOException while checking server status on device " + device, e);
                } catch (TimeoutException e) {
                    Log.e(TAG, "Timeout checking status of view server on device " + device, e);
                }
            } else {
                // Handle development build logic
                // (Optional: implement commands suitable for development builds)
            }
        }
    }

    private static String buildIsServerRunningShellCommand() {
        // Return appropriate shell command based on the build type
        return "ps | grep server"; // Example for fetching server status
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
    public static void checkServerStatus(IDevice device, boolean isUserBuild) {
        if (device.isOnline()) {
            if (isUserBuild) {
                try {
                    device.executeShellCommand(buildIsServerRunningShellCommand(),
                        new BooleanResultReader(result));
                } catch (TimeoutException e) {
                    Log.e(TAG, "Timeout checking status of view server on device " + device, e);
                }
            } else {
                // Handle development build logic
            }
        }
    }
//<End of snippet n. 1>