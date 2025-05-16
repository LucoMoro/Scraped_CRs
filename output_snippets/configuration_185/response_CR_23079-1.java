//<Beginning of snippet n. 0>

import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
import java.io.File;

public class DeviceManager {
    
    public static void startListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.addDeviceChangeListener(listener);
    }

    public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

    public static IDevice[] getDevices() {
        return bridge.getDevices();
    }

    public static void checkAndExecuteCommand(IDevice device) {
        if (device != null && device.isOnline()) {
            try {
                device.executeShellCommand(buildIsServerRunningShellCommand(), new BooleanResultReader(result));
                Log.i(TAG, "Successfully checked if server is running on device " + device);
            } catch (IOException e) {
                Log.e(TAG, "IOException occurred: " + e.getMessage(), e);
            } catch (TimeoutException e) {
                Log.e(TAG, "Timeout checking status of view server on device " + device, e);
            }
        } else {
            Log.w(TAG, "Device is offline or null");
        }
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static void checkAndExecuteCommand(IDevice device) {
    if (device != null && device.isOnline()) {
        try {
            device.executeShellCommand(buildIsServerRunningShellCommand(), new BooleanResultReader(result));
            Log.i(TAG, "Successfully checked if server is running on device " + device);
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout checking status of view server on device " + device, e);
        }
    } else {
        Log.w(TAG, "Device is offline or null");
    }
}

//<End of snippet n. 1>