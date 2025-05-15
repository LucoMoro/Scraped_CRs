
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
if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
}
} catch (IOException e) {
e.printStackTrace();

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
}
} catch (TimeoutException e) {
Log.e(TAG, "Timeout checking status of view server on device " + device);

//<End of snippet n. 1>








