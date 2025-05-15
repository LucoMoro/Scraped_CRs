
//<Beginning of snippet n. 0>


import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.hierarchyviewer.scene.VersionLoader;

import java.io.IOException;
import java.io.File;
AndroidDebugBridge.addDeviceChangeListener(listener);
}

public static IDevice[] getDevices() {
return bridge.getDevices();
}
if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
                if (!result[0]) {
                    if (VersionLoader.loadProtocolVersion(device) > 2) {
                        result[0] = true;
                    }
                }
}
} catch (IOException e) {
e.printStackTrace();

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
                if (!result[0] && loadViewServerInfo(device).protocolVersion > 2) {
                    result[0] = true;
                }
}
} catch (TimeoutException e) {
Log.e(TAG, "Timeout checking status of view server on device " + device);

//<End of snippet n. 1>








