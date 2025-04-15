/*Prevent NPE when server is not running.

Change-Id:Iea1ba8fd78aca61611e73a29fe8b08b96783b248*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 33cb5e9..8a730bd 100644

//Synthetic comment -- @@ -194,8 +194,11 @@
if (device.isOnline()) {
device.executeShellCommand(buildIsServerRunningShellCommand(),
new BooleanResultReader(result));
                if (!result[0]) {
                    ViewServerInfo serverInfo = loadViewServerInfo(device);
                    if (serverInfo != null && serverInfo.protocolVersion > 2) {
                        result[0] = true;
                    }
}
}
} catch (TimeoutException e) {







