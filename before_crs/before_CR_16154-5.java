/*Adding pixel perfect view, loupe and tree.

Change-Id:I9be3e9037dec5eeb240608ba8c6329fd77689bbe*/
//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 427add4..6acbdff 100644

//Synthetic comment -- @@ -19,20 +19,22 @@
import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;

public class HierarchyViewerApplication {
public static void main(String[] args) {
HierarchyViewerDirector director = new HierarchyViewerApplicationDirector();
ComponentRegistry.setDirector(director);
        director.initDebugBridge();
ComponentRegistry.setDeviceSelectionModel(new DeviceSelectionModel());
director.startListenForDevices();
director.populateDeviceSelectionModel();

UIThread.runUI();

director.stopListenForDevices();
director.stopDebugBridge();
director.terminate();
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index f59f6c4..485fbb5 100644

//Synthetic comment -- @@ -18,24 +18,88 @@

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyvieweruilib.DeviceSelector;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIThread {
public static void runUI() {
Display display = new Display();
Shell shell = new Shell(display);
shell.setLayout(new FillLayout());
DeviceSelector deviceSelector = new DeviceSelector(shell);
shell.open();
        while (!shell.isDisposed()) {
if (!display.readAndDispatch()) {
display.sleep();
}
}
deviceSelector.terminate();
ImageLoader.dispose();
display.dispose();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java
//Synthetic comment -- index 0f463d9..528c35c 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.hierarchyviewerlib;

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;

/**
* This is the central point for getting access to the various parts of the
//Synthetic comment -- @@ -29,6 +30,8 @@

private static DeviceSelectionModel deviceSelectionModel;

public static HierarchyViewerDirector getDirector() {
return director;
}
//Synthetic comment -- @@ -44,4 +47,12 @@
public static void setDeviceSelectionModel(DeviceSelectionModel deviceSelectionModel) {
ComponentRegistry.deviceSelectionModel = deviceSelectionModel;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index e60a6f2..c21da49 100644

//Synthetic comment -- @@ -16,15 +16,21 @@

package com.android.hierarchyviewerlib;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.device.WindowUpdater;
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;

/**
* This is the class where most of the logic resides.
*/
//Synthetic comment -- @@ -33,6 +39,8 @@

public static final String TAG = "hierarchyviewer";

public void terminate() {
WindowUpdater.terminate();
}
//Synthetic comment -- @@ -74,17 +82,19 @@
return;
}
}
            ViewServerInfo viewServerInfo = DeviceBridge.loadViewServerInfo(device);
executeInBackground(new Runnable() {
public void run() {
Window[] windows = DeviceBridge.loadWindows(device);
ComponentRegistry.getDeviceSelectionModel().addDevice(device, windows);
}
});
            if (viewServerInfo.protocolVersion >= 3) {
                WindowUpdater.startListenForWindowChanges(this, device);
                focusChanged(device);
            }
}
}

//Synthetic comment -- @@ -124,5 +134,62 @@
focusedWindow);
}
});
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 7a5a6f7..001f98b 100644

//Synthetic comment -- @@ -263,8 +263,11 @@
try {
connection = new DeviceConnection(device);
connection.sendCommand("SERVER");
            server = Integer.parseInt(connection.getInputStream().readLine());
        } catch (IOException e) {
Log.e(TAG, "Unable to get view server version from device " + device);
} finally {
if (connection != null) {
//Synthetic comment -- @@ -275,8 +278,11 @@
try {
connection = new DeviceConnection(device);
connection.sendCommand("PROTOCOL");
            protocol = Integer.parseInt(connection.getInputStream().readLine());
        } catch (IOException e) {
Log.e(TAG, "Unable to get view server protocol version from device " + device);
} finally {
if (connection != null) {
//Synthetic comment -- @@ -369,7 +375,7 @@
connection = new DeviceConnection(device);
connection.sendCommand("GET_FOCUS");
String line = connection.getInputStream().readLine();
            if (line.length() == 0) {
return -1;
}
return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
//Synthetic comment -- @@ -382,4 +388,46 @@
}
return -1;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
new file mode 100644
//Synthetic comment -- index 0000000..00453ee

//Synthetic comment -- @@ -0,0 +1,186 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
new file mode 100644
//Synthetic comment -- index 0000000..6963b25

//Synthetic comment -- @@ -0,0 +1,196 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java
//Synthetic comment -- index a948d12..60d8e6d 100644

//Synthetic comment -- @@ -243,7 +243,10 @@

public void widgetDefaultSelected(SelectionEvent e) {
// TODO: Double click to open view hierarchy

}

public void widgetSelected(SelectionEvent e) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java
new file mode 100644
//Synthetic comment -- index 0000000..e4345e1

//Synthetic comment -- @@ -0,0 +1,297 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java
new file mode 100644
//Synthetic comment -- index 0000000..eb1df88

//Synthetic comment -- @@ -0,0 +1,271 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
new file mode 100644
//Synthetic comment -- index 0000000..a85c8eb

//Synthetic comment -- @@ -0,0 +1,199 @@







