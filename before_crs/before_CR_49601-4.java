/*WIP: hv via DDM

Change-Id:Ia88d107811abd8e36a0f980938c584d79565ac42*/
//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 867446d..b08adc3 100644

//Synthetic comment -- @@ -135,6 +135,10 @@
executeInBackground("Connecting device", new Runnable() {
@Override
public void run() {
IHvDevice hvDevice;
synchronized (mDevicesLock) {
hvDevice = mDevices.get(device);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DdmViewDebugDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DdmViewDebugDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..1bf9f65

//Synthetic comment -- @@ -0,0 +1,255 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index da29703..9da88cc 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
*/
public static void initDebugBridge(String adbLocation) {
if (sBridge == null) {
            AndroidDebugBridge.init(false /* debugger support */);
}
if (sBridge == null || !sBridge.isConnected()) {
sBridge = AndroidDebugBridge.createBridge(adbLocation, true);
//Synthetic comment -- @@ -438,9 +438,29 @@
connection = new DeviceConnection(window.getDevice());
connection.sendCommand("DUMP " + window.encode()); //$NON-NLS-1$
BufferedReader in = connection.getInputStream();
            ViewNode currentNode = null;
            int currentDepth = -1;
            String line;
while ((line = in.readLine()) != null) {
if ("DONE.".equalsIgnoreCase(line)) {
break;
//Synthetic comment -- @@ -458,27 +478,18 @@
currentNode = new ViewNode(window, currentNode, line.substring(depth));
currentDepth = depth;
}
            if (currentNode == null) {
                return null;
            }
            while (currentNode.parent != null) {
                currentNode = currentNode.parent;
            }
            ViewServerInfo serverInfo = getViewServerInfo(window.getDevice());
            if (serverInfo != null) {
                currentNode.protocolVersion = serverInfo.protocolVersion;
            }
            return currentNode;
        } catch (Exception e) {
            Log.e(TAG, "Unable to load window data for window " + window.getTitle() + " on device "
                    + window.getDevice());
            Log.e(TAG, e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
}
        return null;
}

public static boolean loadProfileData(Window window, ViewNode viewNode) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java
//Synthetic comment -- index e1fdab3..c38a9cc 100644

//Synthetic comment -- @@ -16,10 +16,29 @@

package com.android.hierarchyviewerlib.device;

import com.android.ddmlib.IDevice;

public class HvDeviceFactory {
public static IHvDevice create(IDevice device) {
        return new ViewServerDevice(device);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/ViewNode.java
//Synthetic comment -- index 49bdf8f..e38da00 100644

//Synthetic comment -- @@ -132,7 +132,14 @@
data = data.substring(delimIndex + 1);
delimIndex = data.indexOf(' ');
hashCode = data.substring(0, delimIndex);
        loadProperties(data.substring(delimIndex + 1).trim());

measureTime = -1;
layoutTime = -1;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/Window.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/Window.java
//Synthetic comment -- index 7298ab2..cdabd3f 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.IDevice;
import com.android.hierarchyviewerlib.device.IHvDevice;

//Synthetic comment -- @@ -27,11 +28,20 @@
private final String mTitle;
private final int mHashCode;
private final IHvDevice mHvDevice;

public Window(IHvDevice device, String title, int hashCode) {
this.mHvDevice = device;
this.mTitle = title;
this.mHashCode = hashCode;
}

public String getTitle() {
//Synthetic comment -- @@ -59,6 +69,10 @@
return mHvDevice.getDevice();
}

public static Window getFocusedWindow(IHvDevice device) {
return new Window(device, "<Focused Window>", -1);
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java
//Synthetic comment -- index bbff48c..3352df0 100644

//Synthetic comment -- @@ -234,8 +234,7 @@
.ceil(mViewport.width), (int) Math.ceil(mViewport.height));

e.gc.setAlpha(255);
                        e.gc
                                .setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_DARK_GRAY));
e.gc.setLineWidth((int) Math.ceil(2 / mScale));
e.gc.drawRectangle((int) mViewport.x, (int) mViewport.y, (int) Math







