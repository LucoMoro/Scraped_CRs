/*Handling adb exceptions properly

Change-Id:I1209ee81adc99c232280371aa6206345e87a1094*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 4edf67f..7a5a6f7 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.io.BufferedReader;
//Synthetic comment -- @@ -163,8 +164,15 @@
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
}
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout checking status of view server on device " + device);
} catch (IOException e) {
Log.e(TAG, "Unable to check status of view server on device " + device);
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to check status of view server on device " + device);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Unable to execute command to check status of view server on device "
                    + device);
}
return result[0];
}
//Synthetic comment -- @@ -180,8 +188,14 @@
device.executeShellCommand(buildStartServerShellCommand(port),
new BooleanResultReader(result));
}
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout starting view server on device " + device);
} catch (IOException e) {
Log.e(TAG, "Unable to start view server on device " + device);
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to start view server on device " + device);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Unable to execute command to start view server on device " + device);
}
return result[0];
}
//Synthetic comment -- @@ -193,8 +207,14 @@
device.executeShellCommand(buildStopServerShellCommand(), new BooleanResultReader(
result));
}
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout stopping view server on device " + device);
} catch (IOException e) {
Log.e(TAG, "Unable to stop view server on device " + device);
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to stop view server on device " + device);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Unable to execute command to stop view server on device " + device);
}
return result[0];
}







