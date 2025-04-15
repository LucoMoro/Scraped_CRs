/*hv: Refactor to interact with the device via IHvDevice

Hierarchy Viewer currently interacts with a View Server that
is present on the device. This is available only on eng devices
due to security restrictions on the View Server.

Rather than use this custom view server, we ought to use DDM
for communication with the device. Such a scheme has a number of
benefits apart from security.

This CL is primarily just a refactoring of the existing host
side code. The main objective is to hide the communication to
the device behind a IHvDevice interface. The ViewServerDevice
implementation of this interface allows communicating with
existing devices that use the ViewServer implementation on
the device. A subsequent CL will provide a new implementation
of this interface that communicates via DDM.

Change-Id:I7d63e5a59c6ec9c96dbd07af9dc03f93779fd2ec*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java
//Synthetic comment -- index e950c60..597f7a4 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.hierarchyviewer;

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.IHvDevice;
import com.android.hierarchyviewerlib.models.Window;
import com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectTreeView;
import com.android.ide.eclipse.hierarchyviewer.views.PropertyView;

//Synthetic comment -- @@ -97,7 +97,7 @@
}

@Override
    public void loadPixelPerfectData(IHvDevice device) {
super.loadPixelPerfectData(device);

// The windows tab hides the tree tab, so let's bring the tree tab








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index dd5ea84..3257e1c 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
import com.android.hierarchyviewerlib.actions.RequestLayoutAction;
import com.android.hierarchyviewerlib.actions.SavePixelPerfectAction;
import com.android.hierarchyviewerlib.actions.SaveTreeViewAction;
import com.android.hierarchyviewerlib.device.IHvDevice;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;
//Synthetic comment -- @@ -669,9 +669,9 @@
treeViewMenu.add(new Separator());
treeViewMenu.add(RefreshViewAction.getAction());
treeViewMenu.add(DisplayViewAction.getAction(getShell()));

        IHvDevice hvDevice = DeviceSelectionModel.getModel().getSelectedDevice();
        if (hvDevice.supportsDisplayListDump()) {
treeViewMenu.add(DumpDisplayListAction.getAction());
dumpDisplayList.setVisible(true);
} else {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 2e03f11..867446d 100644

//Synthetic comment -- @@ -16,22 +16,20 @@

package com.android.hierarchyviewerlib;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.HvDeviceFactory;
import com.android.hierarchyviewerlib.device.IHvDevice;
import com.android.hierarchyviewerlib.device.WindowUpdater;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.Window;
import com.android.hierarchyviewerlib.ui.CaptureDisplay;
import com.android.hierarchyviewerlib.ui.TreeView;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
//Synthetic comment -- @@ -42,15 +40,15 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//Synthetic comment -- @@ -78,6 +76,9 @@

private String mFilterText = ""; //$NON-NLS-1$

    private static final Object mDevicesLock = new Object();
    private Map<IDevice, IHvDevice> mDevices = new HashMap<IDevice, IHvDevice>(10);

public void terminate() {
WindowUpdater.terminate();
mPixelPerfectRefreshTimer.cancel();
//Synthetic comment -- @@ -134,60 +135,46 @@
executeInBackground("Connecting device", new Runnable() {
@Override
public void run() {
                IHvDevice hvDevice;
                synchronized (mDevicesLock) {
                    hvDevice = mDevices.get(device);
                    if (hvDevice == null) {
                        hvDevice = HvDeviceFactory.create(device);
                        hvDevice.initializeViewDebug();
                        hvDevice.addWindowChangeListener(getDirector());
                        mDevices.put(device, hvDevice);
                    } else {
                        // attempt re-initializing view server if device state has changed
                        hvDevice.initializeViewDebug();
}
}

                DeviceSelectionModel.getModel().addDevice(hvDevice);
                focusChanged(device);
}
});
}

@Override
public void deviceDisconnected(final IDevice device) {
executeInBackground("Disconnecting device", new Runnable() {
@Override
public void run() {
                IHvDevice hvDevice;
                synchronized (mDevicesLock) {
                    hvDevice = mDevices.get(device);
                    if (hvDevice != null) {
                        mDevices.remove(device);
                    }
}

                if (hvDevice == null) {
                    return;
                }

                hvDevice.terminateViewDebug();
                hvDevice.removeWindowChangeListener(getDirector());
                DeviceSelectionModel.getModel().removeDevice(hvDevice);
if (PixelPerfectModel.getModel().getDevice() == device) {
PixelPerfectModel.getModel().setData(null, null, null);
}
//Synthetic comment -- @@ -201,25 +188,13 @@
}

@Override
public void windowsChanged(final IDevice device) {
executeInBackground("Refreshing windows", new Runnable() {
@Override
public void run() {
                IHvDevice hvDevice = getHvDevice(device);
                hvDevice.reloadWindows();
                DeviceSelectionModel.getModel().updateDevice(hvDevice);
}
});
}
//Synthetic comment -- @@ -229,12 +204,20 @@
executeInBackground("Updating focus", new Runnable() {
@Override
public void run() {
                IHvDevice hvDevice = getHvDevice(device);
                int focusedWindow = hvDevice.getFocusedWindow();
                DeviceSelectionModel.getModel().updateFocusedWindow(hvDevice, focusedWindow);
}
});
}

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        if ((changeMask & IDevice.CHANGE_STATE) != 0 && device.isOnline()) {
            deviceConnected(device);
        }
    }

public void refreshPixelPerfect() {
final IDevice device = PixelPerfectModel.getModel().getDevice();
if (device != null) {
//Synthetic comment -- @@ -253,7 +236,7 @@
executeInBackground("Refreshing pixel perfect screenshot", new Runnable() {
@Override
public void run() {
                        Image screenshotImage = getScreenshotImage(getHvDevice(device));
if (screenshotImage != null) {
PixelPerfectModel.getModel().setImage(screenshotImage);
}
//Synthetic comment -- @@ -273,8 +256,9 @@
executeInBackground("Refreshing pixel perfect tree", new Runnable() {
@Override
public void run() {
                    IHvDevice hvDevice = getHvDevice(device);
ViewNode viewNode =
                            hvDevice.loadWindowData(Window.getFocusedWindow(hvDevice));
if (viewNode != null) {
PixelPerfectModel.getModel().setTree(viewNode);
}
//Synthetic comment -- @@ -284,64 +268,43 @@
}
}

    public void loadPixelPerfectData(final IHvDevice hvDevice) {
executeInBackground("Loading pixel perfect data", new Runnable() {
@Override
public void run() {
                Image screenshotImage = getScreenshotImage(hvDevice);
if (screenshotImage != null) {
ViewNode viewNode =
                            hvDevice.loadWindowData(Window.getFocusedWindow(hvDevice));
if (viewNode != null) {
                        PixelPerfectModel.getModel().setData(hvDevice.getDevice(),
                                screenshotImage, viewNode);
}
}
}
});
}

    private IHvDevice getHvDevice(IDevice device) {
        synchronized (mDevicesLock) {
            return mDevices.get(device);
}
    }

    private Image getScreenshotImage(IHvDevice hvDevice) {
        return (hvDevice == null) ? null : hvDevice.getScreenshotImage();
}

public void loadViewTreeData(final Window window) {
executeInBackground("Loading view hierarchy", new Runnable() {
@Override
public void run() {
mFilterText = ""; //$NON-NLS-1$

                IHvDevice hvDevice = window.getHvDevice();
                ViewNode viewNode = hvDevice.loadWindowData(window);
if (viewNode != null) {
                    hvDevice.loadProfileData(window, viewNode);
viewNode.setViewCount();
TreeViewModel.getModel().setData(window, viewNode);
}
//Synthetic comment -- @@ -393,7 +356,8 @@
}

public Image loadCapture(ViewNode viewNode) {
        IHvDevice hvDevice = viewNode.window.getHvDevice();
        final Image image = hvDevice.loadCapture(viewNode.window, viewNode);
if (image != null) {
viewNode.image = image;

//Synthetic comment -- @@ -423,7 +387,11 @@
executeInBackground("Refreshing windows", new Runnable() {
@Override
public void run() {
                IHvDevice[] hvDevicesA = DeviceSelectionModel.getModel().getDevices();
                IDevice[] devicesA = new IDevice[hvDevicesA.length];
                for (int i = 0; i < hvDevicesA.length; i++) {
                    devicesA[i] = hvDevicesA[i].getDevice();
                }
IDevice[] devicesB = DeviceBridge.getDevices();
HashSet<IDevice> deviceSet = new HashSet<IDevice>();
for (int i = 0; i < devicesB.length; i++) {
//Synthetic comment -- @@ -452,7 +420,7 @@
}

public void inspectScreenshot() {
        IHvDevice device = DeviceSelectionModel.getModel().getSelectedDevice();
if (device != null) {
loadPixelPerfectData(device);
}
//Synthetic comment -- @@ -562,7 +530,8 @@
executeInBackground("Saving window layers", new Runnable() {
@Override
public void run() {
                                IHvDevice hvDevice = getHvDevice(window.getDevice());
                                PsdFile psdFile = hvDevice.captureLayers(window);
if (psdFile != null) {
String extensionedFileName = fileName;
if (!extensionedFileName.toLowerCase().endsWith(".psd")) { //$NON-NLS-1$
//Synthetic comment -- @@ -595,7 +564,8 @@
executeInBackground("Invalidating view", new Runnable() {
@Override
public void run() {
                    IHvDevice hvDevice = getHvDevice(selectedNode.viewNode.window.getDevice());
                    hvDevice.invalidateView(selectedNode.viewNode);
}
});
}
//Synthetic comment -- @@ -607,7 +577,8 @@
executeInBackground("Request layout", new Runnable() {
@Override
public void run() {
                    IHvDevice hvDevice = getHvDevice(selectedNode.viewNode.window.getDevice());
                    hvDevice.requestLayout(selectedNode.viewNode);
}
});
}
//Synthetic comment -- @@ -619,7 +590,8 @@
executeInBackground("Dump displaylist", new Runnable() {
@Override
public void run() {
                    IHvDevice hvDevice = getHvDevice(selectedNode.viewNode.window.getDevice());
                    hvDevice.outputDisplayList(selectedNode.viewNode);
}
});
}
//Synthetic comment -- @@ -640,7 +612,8 @@
}

private void loadViewRecursive(ViewNode viewNode) {
        IHvDevice hvDevice = getHvDevice(viewNode.window.getDevice());
        Image image = hvDevice.loadCapture(viewNode.window, viewNode);
if (image == null) {
return;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InspectScreenshotAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InspectScreenshotAction.java
//Synthetic comment -- index 708c7b1..388c057 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.hierarchyviewerlib.actions;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.IHvDevice;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.Window;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -65,27 +65,27 @@
}

@Override
    public void deviceChanged(IHvDevice device) {
// pass
}

@Override
    public void deviceConnected(IHvDevice device) {
// pass
}

@Override
    public void deviceDisconnected(IHvDevice device) {
// pass
}

@Override
    public void focusChanged(IHvDevice device) {
// pass
}

@Override
    public void selectionChanged(final IHvDevice device, final Window window) {
Display.getDefault().syncExec(new Runnable() {
@Override
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadViewHierarchyAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadViewHierarchyAction.java
//Synthetic comment -- index f2dbaee..6666315 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.hierarchyviewerlib.actions;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.IHvDevice;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.Window;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -65,27 +65,27 @@
}

@Override
    public void deviceChanged(IHvDevice device) {
// pass
}

@Override
    public void deviceConnected(IHvDevice device) {
// pass
}

@Override
    public void deviceDisconnected(IHvDevice device) {
// pass
}

@Override
    public void focusChanged(IHvDevice device) {
// pass
}

@Override
    public void selectionChanged(final IHvDevice device, final Window window) {
Display.getDefault().syncExec(new Runnable() {
@Override
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/AbstractHvDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/AbstractHvDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..e330168

//Synthetic comment -- @@ -0,0 +1,67 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyviewerlib.device;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractHvDevice implements IHvDevice {
    private static final String TAG = "HierarchyViewer";

    @Override
    public Image getScreenshotImage() {
        IDevice device = getDevice();
        final AtomicReference<Image> imageRef = new AtomicReference<Image>();

        try {
            final RawImage screenshot = device.getScreenshot();
            if (screenshot == null) {
                return null;
            }
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    ImageData imageData =
                            new ImageData(screenshot.width, screenshot.height, screenshot.bpp,
                                    new PaletteData(screenshot.getRedMask(), screenshot
                                            .getGreenMask(), screenshot.getBlueMask()), 1,
                                    screenshot.data);
                    imageRef.set(new Image(Display.getDefault(), imageData));
                }
            });
            return imageRef.get();
        } catch (IOException e) {
            Log.e(TAG, "Unable to load screenshot from device " + device.getName());
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout loading screenshot from device " + device.getName());
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to load screenshot from device " + device.getName());
        }
        return null;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 10308a3..da29703 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.Window;
import com.android.hierarchyviewerlib.ui.util.PsdFile;

import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -353,7 +355,7 @@
* This loads the list of windows from the specified device. The format is:
* hashCode1 title1 hashCode2 title2 ... hashCodeN titleN DONE.
*/
    public static Window[] loadWindows(IHvDevice hvDevice, IDevice device) {
ArrayList<Window> windows = new ArrayList<Window>();
DeviceConnection connection = null;
ViewServerInfo serverInfo = getViewServerInfo(device);
//Synthetic comment -- @@ -378,7 +380,7 @@
id = Integer.parseInt(windowId, 16);
}

                    Window w = new Window(hvDevice, line.substring(index + 1), id);
windows.add(w);
}
}
//Synthetic comment -- @@ -387,7 +389,7 @@
// get the focused window, which was done using a special type of
// window with hash code -1.
if (serverInfo.protocolVersion < 3) {
                windows.add(Window.getFocusedWindow(hvDevice));
}
} catch (Exception e) {
Log.e(TAG, "Unable to load the window list from device " + device);
//Synthetic comment -- @@ -448,7 +450,9 @@
depth++;
}
while (depth <= currentDepth) {
                    if (currentNode != null) {
                        currentNode = currentNode.parent;
                    }
currentDepth--;
}
currentNode = new ViewNode(window, currentNode, line.substring(depth));
//Synthetic comment -- @@ -557,7 +561,6 @@

try {
connection = new DeviceConnection(window.getDevice());
connection.sendCommand("CAPTURE_LAYERS " + window.encode()); //$NON-NLS-1$

in =
//Synthetic comment -- @@ -583,7 +586,10 @@
} catch (Exception ex) {
}
}

            if (connection != null) {
                connection.close();
            }
}

return null;
//Synthetic comment -- @@ -634,7 +640,9 @@
Log.e(TAG, "Unable to invalidate view " + viewNode + " in window " + viewNode.window
+ " on device " + viewNode.window.getDevice());
} finally {
            if (connection != null) {
                connection.close();
            }
}
}

//Synthetic comment -- @@ -647,7 +655,9 @@
Log.e(TAG, "Unable to request layout for node " + viewNode + " in window "
+ viewNode.window + " on device " + viewNode.window.getDevice());
} finally {
            if (connection != null) {
                connection.close();
            }
}
}

//Synthetic comment -- @@ -661,7 +671,9 @@
Log.e(TAG, "Unable to dump displaylist for node " + viewNode + " in window "
+ viewNode.window + " on device " + viewNode.window.getDevice());
} finally {
            if (connection != null) {
                connection.close();
            }
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java
new file mode 100644
//Synthetic comment -- index 0000000..e1fdab3

//Synthetic comment -- @@ -0,0 +1,25 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyviewerlib.device;

import com.android.ddmlib.IDevice;

public class HvDeviceFactory {
    public static IHvDevice create(IDevice device) {
        return new ViewServerDevice(device);
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/IHvDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/IHvDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..fe8d1ba

//Synthetic comment -- @@ -0,0 +1,56 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyviewerlib.device;

import com.android.ddmlib.IDevice;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.Window;
import com.android.hierarchyviewerlib.ui.util.PsdFile;

import org.eclipse.swt.graphics.Image;

/** Represents a device that can perform view debug operations. */
public interface IHvDevice {
    /**
     * Initializes view debugging on the device.
     * @return true if the on device component was successfully initialized
     */
    boolean initializeViewDebug();
    boolean reloadWindows();

    void terminateViewDebug();
    boolean isViewDebugEnabled();
    boolean supportsDisplayListDump();

    Window[] getWindows();
    int getFocusedWindow();

    IDevice getDevice();

    Image getScreenshotImage();
    ViewNode loadWindowData(Window window);
    void loadProfileData(Window window, ViewNode viewNode);
    Image loadCapture(Window window, ViewNode viewNode);
    PsdFile captureLayers(Window window);
    void invalidateView(ViewNode viewNode);
    void requestLayout(ViewNode viewNode);
    void outputDisplayList(ViewNode viewNode);

    void addWindowChangeListener(IWindowChangeListener l);
    void removeWindowChangeListener(IWindowChangeListener l);
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewServerDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewServerDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..4bd5d6b

//Synthetic comment -- @@ -0,0 +1,149 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyviewerlib.device;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.Window;
import com.android.hierarchyviewerlib.ui.util.PsdFile;

import org.eclipse.swt.graphics.Image;

public class ViewServerDevice extends AbstractHvDevice {
    static final String TAG = "ViewServerDevice";

    final IDevice mDevice;
    private ViewServerInfo mViewServerInfo;
    private Window[] mWindows;

    public ViewServerDevice(IDevice device) {
        mDevice = device;
    }

    @Override
    public boolean initializeViewDebug() {
        if (!mDevice.isOnline()) {
            return false;
        }

        DeviceBridge.setupDeviceForward(mDevice);

        return reloadWindows();
    }

    @Override
    public boolean reloadWindows() {
        if (!DeviceBridge.isViewServerRunning(mDevice)) {
            if (!DeviceBridge.startViewServer(mDevice)) {
                Log.e(TAG, "Unable to debug device: " + mDevice.getName());
                DeviceBridge.removeDeviceForward(mDevice);
                return false;
            }
        }

        mViewServerInfo = DeviceBridge.loadViewServerInfo(mDevice);
        if (mViewServerInfo == null) {
            return false;
        }

        mWindows = DeviceBridge.loadWindows(this, mDevice);
        return true;
    }

    @Override
    public boolean supportsDisplayListDump() {
        return mViewServerInfo != null && mViewServerInfo.protocolVersion >= 4;
    }

    @Override
    public void terminateViewDebug() {
        DeviceBridge.removeDeviceForward(mDevice);
        DeviceBridge.removeViewServerInfo(mDevice);
    }

    @Override
    public boolean isViewDebugEnabled() {
        return mViewServerInfo != null;
    }

    @Override
    public Window[] getWindows() {
        return mWindows;
    }

    @Override
    public int getFocusedWindow() {
        return DeviceBridge.getFocusedWindow(mDevice);
    }

    @Override
    public IDevice getDevice() {
        return mDevice;
    }

    @Override
    public ViewNode loadWindowData(Window window) {
        return DeviceBridge.loadWindowData(window);
    }

    @Override
    public void loadProfileData(Window window, ViewNode viewNode) {
        DeviceBridge.loadProfileData(window, viewNode);
    }

    @Override
    public Image loadCapture(Window window, ViewNode viewNode) {
        return DeviceBridge.loadCapture(window, viewNode);
    }

    @Override
    public PsdFile captureLayers(Window window) {
        return DeviceBridge.captureLayers(window);
    }

    @Override
    public void invalidateView(ViewNode viewNode) {
        DeviceBridge.invalidateView(viewNode);
    }

    @Override
    public void requestLayout(ViewNode viewNode) {
        DeviceBridge.requestLayout(viewNode);
    }

    @Override
    public void outputDisplayList(ViewNode viewNode) {
        DeviceBridge.outputDisplayList(viewNode);
    }

    @Override
    public void addWindowChangeListener(IWindowChangeListener l) {
        if (mViewServerInfo.protocolVersion >= 3) {
            WindowUpdater.startListenForWindowChanges(l, mDevice);
        }
    }

    @Override
    public void removeWindowChangeListener(IWindowChangeListener l) {
        if (mViewServerInfo.protocolVersion >= 3) {
            WindowUpdater.stopListenForWindowChanges(l, mDevice);
        }
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/Window.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/Window.java
deleted file mode 100644
//Synthetic comment -- index af79081..0000000

//Synthetic comment -- @@ -1,77 +0,0 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index b00a1dc..9ac9b40 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.hierarchyviewerlib.models;

import com.android.hierarchyviewerlib.device.IHvDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
* This class stores the list of windows for each connected device. It notifies
//Synthetic comment -- @@ -29,17 +29,14 @@
* in the device selector.
*/
public class DeviceSelectionModel {
    private final Map<IHvDevice, DeviceInfo> mDeviceMap = new HashMap<IHvDevice, DeviceInfo>(10);
    private final Map<IHvDevice, Integer> mFocusedWindowHashes =
            new HashMap<IHvDevice, Integer>(20);

private final ArrayList<IWindowChangeListener> mWindowChangeListeners =
new ArrayList<IWindowChangeListener>();

    private IHvDevice mSelectedDevice;

private Window mSelectedWindow;

//Synthetic comment -- @@ -47,11 +44,9 @@

private static class DeviceInfo {
Window[] windows;

        private DeviceInfo(Window[] windows) {
this.windows = windows;
}
}
public static DeviceSelectionModel getModel() {
//Synthetic comment -- @@ -61,51 +56,40 @@
return sModel;
}

    public void addDevice(IHvDevice hvDevice) {
synchronized (mDeviceMap) {
            DeviceInfo info = new DeviceInfo(hvDevice.getWindows());
            mDeviceMap.put(hvDevice, info);
}

        notifyDeviceConnected(hvDevice);
}

    public void removeDevice(IHvDevice hvDevice) {
boolean selectionChanged = false;
synchronized (mDeviceMap) {
            mDeviceMap.remove(hvDevice);
            mFocusedWindowHashes.remove(hvDevice);
            if (mSelectedDevice == hvDevice) {
                mSelectedDevice = null;
                mSelectedWindow = null;
                selectionChanged = true;
}
}
        notifyDeviceDisconnected(hvDevice);
if (selectionChanged) {
notifySelectionChanged(mSelectedDevice, mSelectedWindow);
}
}

    public void updateDevice(IHvDevice hvDevice) {
boolean selectionChanged = false;
synchronized (mDeviceMap) {
            Window[] windows = hvDevice.getWindows();
            mDeviceMap.put(hvDevice, new DeviceInfo(windows));

// If the selected window no longer exists, we clear the selection.
            if (mSelectedDevice == hvDevice && mSelectedWindow != null) {
boolean windowStillExists = false;
for (int i = 0; i < windows.length && !windowStillExists; i++) {
if (windows[i].equals(mSelectedWindow)) {
//Synthetic comment -- @@ -119,7 +103,8 @@
}
}
}

        notifyDeviceChanged(hvDevice);
if (selectionChanged) {
notifySelectionChanged(mSelectedDevice, mSelectedWindow);
}
//Synthetic comment -- @@ -128,7 +113,7 @@
/*
* Change which window has focus and notify the listeners.
*/
    public void updateFocusedWindow(IHvDevice device, int focusedWindow) {
Integer oldValue = null;
synchronized (mDeviceMap) {
oldValue = mFocusedWindowHashes.put(device, new Integer(focusedWindow));
//Synthetic comment -- @@ -141,15 +126,15 @@
}

public static interface IWindowChangeListener {
        public void deviceConnected(IHvDevice device);

        public void deviceChanged(IHvDevice device);

        public void deviceDisconnected(IHvDevice device);

        public void focusChanged(IHvDevice device);

        public void selectionChanged(IHvDevice device, Window window);
}

private IWindowChangeListener[] getWindowChangeListenerList() {
//Synthetic comment -- @@ -165,7 +150,7 @@
return listeners;
}

    private void notifyDeviceConnected(IHvDevice device) {
IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
//Synthetic comment -- @@ -174,7 +159,7 @@
}
}

    private void notifyDeviceChanged(IHvDevice device) {
IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
//Synthetic comment -- @@ -183,7 +168,7 @@
}
}

    private void notifyDeviceDisconnected(IHvDevice device) {
IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
//Synthetic comment -- @@ -192,7 +177,7 @@
}
}

    private void notifyFocusChanged(IHvDevice device) {
IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
//Synthetic comment -- @@ -201,7 +186,7 @@
}
}

    private void notifySelectionChanged(IHvDevice device, Window window) {
IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
//Synthetic comment -- @@ -222,27 +207,28 @@
}
}

    public IHvDevice[] getDevices() {
synchronized (mDeviceMap) {
            Set<IHvDevice> devices = mDeviceMap.keySet();
            return devices.toArray(new IHvDevice[devices.size()]);
}
}

    public Window[] getWindows(IHvDevice device) {
synchronized (mDeviceMap) {
DeviceInfo info = mDeviceMap.get(device);
if (info != null) {
                return info.windows;
}
}

        return null;
}

// Returns the window that currently has focus or -1. Note that this means
// that a window with hashcode -1 gets highlighted. If you remember, this is
// the infamous <Focused Window>
    public int getFocusedWindow(IHvDevice device) {
synchronized (mDeviceMap) {
Integer focusedWindow = mFocusedWindowHashes.get(device);
if (focusedWindow == null) {
//Synthetic comment -- @@ -252,7 +238,7 @@
}
}

    public void setSelection(IHvDevice device, Window window) {
synchronized (mDeviceMap) {
mSelectedDevice = device;
mSelectedWindow = window;
//Synthetic comment -- @@ -260,7 +246,7 @@
notifySelectionChanged(device, window);
}

    public IHvDevice getSelectedDevice() {
synchronized (mDeviceMap) {
return mSelectedDevice;
}
//Synthetic comment -- @@ -271,15 +257,4 @@
return mSelectedWindow;
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
//Synthetic comment -- index 81331ed..a425b47 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.IDevice;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
//Synthetic comment -- index 279b5fd..6dac1e6 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.hierarchyviewerlib.models;

import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Rectangle;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/ViewNode.java
similarity index 99%
rename from hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
rename to hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/ViewNode.java
//Synthetic comment -- index 4ab4fc6..49bdf8f 100644

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.hierarchyviewerlib.models;

import org.eclipse.swt.graphics.Image;









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/Window.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/Window.java
new file mode 100644
//Synthetic comment -- index 0000000..7298ab2

//Synthetic comment -- @@ -0,0 +1,103 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.IDevice;
import com.android.hierarchyviewerlib.device.IHvDevice;

/**
 * Used for storing a window from the window manager service on the device.
 * These are the windows that the device selector shows.
 */
public class Window {
    private final String mTitle;
    private final int mHashCode;
    private final IHvDevice mHvDevice;

    public Window(IHvDevice device, String title, int hashCode) {
        this.mHvDevice = device;
        this.mTitle = title;
        this.mHashCode = hashCode;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getHashCode() {
        return mHashCode;
    }

    public String encode() {
        return Integer.toHexString(mHashCode);
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public IHvDevice getHvDevice() {
        return mHvDevice;
    }

    public IDevice getDevice() {
        return mHvDevice.getDevice();
    }

    public static Window getFocusedWindow(IHvDevice device) {
        return new Window(device, "<Focused Window>", -1);
    }

    /*
     * After each refresh of the windows in the device selector, the windows are
     * different instances and automatically reselecting the same window doesn't
     * work in the device selector unless the equals method is defined here.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Window other = (Window) obj;
        if (mHvDevice == null) {
            if (other.mHvDevice != null)
                return false;
        } else if (!mHvDevice.getDevice().getSerialNumber().equals(
                other.mHvDevice.getDevice().getSerialNumber()))
            return false;

        if (mHashCode != other.mHashCode)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result +
                ((mHvDevice == null) ? 0 : mHvDevice.getDevice().getSerialNumber().hashCode());
        result = prime * result + mHashCode;
        return result;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index fb277e8..7d4fdba 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.ViewNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java
//Synthetic comment -- index 84841ef..c3154c8 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.hierarchyviewerlib.ui;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.IHvDevice;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.Window;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
//Synthetic comment -- @@ -69,8 +69,8 @@
private class ContentProvider implements ITreeContentProvider, ILabelProvider, IFontProvider {
@Override
public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof IHvDevice && mDoTreeViewStuff) {
                Window[] list = mModel.getWindows((IHvDevice) parentElement);
if (list != null) {
return list;
}
//Synthetic comment -- @@ -88,8 +88,8 @@

@Override
public boolean hasChildren(Object element) {
            if (element instanceof IHvDevice && mDoTreeViewStuff) {
                Window[] list = mModel.getWindows((IHvDevice) element);
if (list != null) {
return list.length != 0;
}
//Synthetic comment -- @@ -117,8 +117,8 @@

@Override
public Image getImage(Object element) {
            if (element instanceof IHvDevice) {
                if (((IHvDevice) element).getDevice().isEmulator()) {
return mEmulatorImage;
}
return mDeviceImage;
//Synthetic comment -- @@ -128,8 +128,8 @@

@Override
public String getText(Object element) {
            if (element instanceof IHvDevice) {
                return ((IHvDevice) element).getDevice().getName();
} else if (element instanceof Window) {
return ((Window) element).getTitle();
}
//Synthetic comment -- @@ -139,7 +139,7 @@
@Override
public Font getFont(Object element) {
if (element instanceof Window) {
                int focusedWindow = mModel.getFocusedWindow(((Window) element).getHvDevice());
if (focusedWindow == ((Window) element).getHashCode()) {
return mBoldFont;
}
//Synthetic comment -- @@ -263,7 +263,7 @@
}

@Override
    public void deviceConnected(final IHvDevice device) {
Display.getDefault().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -274,7 +274,7 @@
}

@Override
    public void deviceChanged(final IHvDevice device) {
Display.getDefault().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -289,7 +289,7 @@
}

@Override
    public void deviceDisconnected(final IHvDevice device) {
Display.getDefault().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -299,7 +299,7 @@
}

@Override
    public void focusChanged(final IHvDevice device) {
Display.getDefault().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -314,15 +314,15 @@
}

@Override
    public void selectionChanged(IHvDevice device, Window window) {
// pass
}

@Override
public void widgetDefaultSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
        if (selection instanceof IHvDevice && mDoPixelPerfectStuff) {
            HierarchyViewerDirector.getDirector().loadPixelPerfectData((IHvDevice) selection);
} else if (selection instanceof Window && mDoTreeViewStuff) {
HierarchyViewerDirector.getDirector().loadViewTreeData((Window) selection);
}
//Synthetic comment -- @@ -331,10 +331,10 @@
@Override
public void widgetSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
        if (selection instanceof IHvDevice) {
            mModel.setSelection((IHvDevice) selection, null);
} else if (selection instanceof Window) {
            mModel.setSelection(((Window) selection).getHvDevice(), (Window) selection);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java
//Synthetic comment -- index 533b840..069fb61 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.hierarchyviewerlib.ui;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.swt.SWT;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectTree.java
//Synthetic comment -- index e9848d8..f2b0189 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.hierarchyviewerlib.ui;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.jface.viewers.ILabelProvider;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java
//Synthetic comment -- index 919d178..90d2405 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.hierarchyviewerlib.ui;

import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.models.ViewNode.Property;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.TreeColumnResizer;









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index 9449ccc..5617239 100644

//Synthetic comment -- @@ -18,9 +18,9 @@

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.models.ViewNode.ProfileRating;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Rectangle;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java
//Synthetic comment -- index b196aaf..3c3b718 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.hierarchyviewerlib.ui.util;

import com.android.hierarchyviewerlib.models.ViewNode;

import java.util.ArrayList;








