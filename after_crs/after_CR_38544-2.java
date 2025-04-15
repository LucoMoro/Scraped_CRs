/*Added default devices to the avd creation dialog

Change-Id:Id8f0c659b9b0ff4a2ff8605265338cc2a5b05d1a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 128a854..ac5fe5e 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
//Synthetic comment -- @@ -81,7 +80,7 @@
return devices;
}

    public List<Device> getDefaultDevices() {
synchronized (this) {
if (mDefaultDevices == null) {
try {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 0cf52cf..4d24538 100644

//Synthetic comment -- @@ -243,6 +243,7 @@
File hardwareDefs = null;
mDeviceManager = new DeviceManager(log);
mDeviceList.addAll(mDeviceManager.getUserDevices());
        mDeviceList.addAll(mDeviceManager.getDefaultDevices());

SdkManager sdkMan = avdManager.getSdkManager();
if (sdkMan != null) {







