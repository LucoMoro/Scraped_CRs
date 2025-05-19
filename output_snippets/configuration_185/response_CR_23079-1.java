//<Beginning of snippet n. 0>
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;

public static void startListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
    AndroidDebugBridge.addDeviceChangeListener(listener);
}

public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
    AndroidDebugBridge.removeDeviceChangeListener(listener);
}

public static IDevice[] getDevices() {
    return bridge.getDevices();
}

public static void checkServerStatus(IDevice device, BooleanResultReader result) {
    try {
        validateDevice(device);
        if (device.isOnline() && device.isInitialized()) { // Check if device is initialized
            String command = buildIsServerRunningShellCommand();
            device.executeShellCommand(command, result);
        } else {
            Log.e(TAG, "Device is not properly online or initialized: " + device);
        }
    } catch (TimeoutException e) {
        Log.e(TAG, "Timeout checking status of server on device " + device, e);
    } catch (ShellCommandUnresponsiveException e) {
        Log.e(TAG, "Shell command unresponsive for device " + device, e);
    } catch (IOException e) {
        Log.e(TAG, "IOException occurred while executing shell command on device " + device, e);
    } catch (IllegalStateException e) {
        Log.e(TAG, "Invalid state for device " + device, e);
    } catch (Exception e) {
        Log.e(TAG, "Unexpected error while checking server status on device " + device, e);
    }
}

private static void validateDevice(IDevice device) {
    if (device == null || !device.isOnline() || !device.isInitialized()) { // Check if device is initialized
        throw new IllegalStateException("Device must be initialized and online before executing commands.");
    }
}

private static String buildIsServerRunningShellCommand() {
    return "is_server_running_command"; // Ensure command is from the whitelist for user builds
}
//<End of snippet n. 0>