/*Allow HV to work on user builds.

Change-Id:If0f2a79b54abf9a9d8782827b083f10d5d2f2a7e*/
//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/device/DeviceBridge.java b/hierarchyviewer/src/com/android/hierarchyviewer/device/DeviceBridge.java
//Synthetic comment -- index 2d2cea1..f585ea6 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
import java.io.File;
//Synthetic comment -- @@ -56,10 +57,6 @@
AndroidDebugBridge.addDeviceChangeListener(listener);
}

    public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

public static IDevice[] getDevices() {
return bridge.getDevices();
}
//Synthetic comment -- @@ -71,6 +68,11 @@
if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
}
} catch (IOException e) {
e.printStackTrace();








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 610f7b3..33cb5e9 100644

//Synthetic comment -- @@ -194,6 +194,9 @@
if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
}
} catch (TimeoutException e) {
Log.e(TAG, "Timeout checking status of view server on device " + device);







