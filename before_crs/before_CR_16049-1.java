/*Handling Timeout exceptions

Change-Id:I1209ee81adc99c232280371aa6206345e87a1094*/
//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 4edf67f..d86602c 100644

//Synthetic comment -- @@ -163,6 +163,8 @@
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
}
} catch (IOException e) {
Log.e(TAG, "Unable to check status of view server on device " + device);
}
//Synthetic comment -- @@ -180,6 +182,8 @@
device.executeShellCommand(buildStartServerShellCommand(port),
new BooleanResultReader(result));
}
} catch (IOException e) {
Log.e(TAG, "Unable to start view server on device " + device);
}
//Synthetic comment -- @@ -193,6 +197,8 @@
device.executeShellCommand(buildStopServerShellCommand(), new BooleanResultReader(
result));
}
} catch (IOException e) {
Log.e(TAG, "Unable to stop view server on device " + device);
}







