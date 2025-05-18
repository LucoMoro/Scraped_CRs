//<Beginning of snippet n. 0>
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.AndroidDebugBridge;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DeviceManager {
    private static final String TAG = "DeviceManager";
    private static final Set<String> WHITELISTED_COMMANDS = new HashSet<>();

    static {
        WHITELISTED_COMMANDS.add("command1");
        WHITELISTED_COMMANDS.add("command2");
        WHITELISTED_COMMANDS.add("command3"); // Enhanced Command Support
        WHITELISTED_COMMANDS.add("command4");
    }

    public static void listenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.addDeviceChangeListener(listener);
    }

    public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

    public static IDevice[] getDevices() {
        return AndroidDebugBridge.getDevices(); // Ensured valid method usage
    }

    public static void executeShellCommand(IDevice device, String command, ResultReceiver resultReceiver) {
        if (!AndroidDebugBridge.getDebugBridge().isInitialized()) {
            Log.e(TAG, "Android Debug Bridge is not initialized.");
            return;
        }

        if (!WHITELISTED_COMMANDS.contains(command)) {
            Log.e(TAG, "Command not allowed: " + command);
            return;
        }

        if (!device.isOnline()) {
            Log.e(TAG, "Device is not online: " + device);
            return;
        }

        try {
            device.executeShellCommand(command, resultReceiver);
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred while executing command: " + e.getMessage(), e);
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout while executing command on device " + device + ": " + e.getMessage(), e);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Shell command unresponsive on device " + device + ": " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error occurred while executing command: " + e.getMessage(), e);
        }
    }

    private static String buildIsServerRunningShellCommand() {
        return "yourCommand"; // Placeholder for the command builder.
    }

    private static class BooleanResultReader extends MultiLineReceiver {
        boolean result;

        public BooleanResultReader(boolean result) {
            this.result = result;
        }

        @Override
        public void processNewLines(String[] lines) {
            // Your implementation here
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}
//<End of snippet n. 0>