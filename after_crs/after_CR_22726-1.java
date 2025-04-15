/*hierarchyviewer can dump displaylists for selected nodes.

In hierarchyviewer (and hierarchyviewer1):
The displaylist for the selected node (if there is one) is output
into logcat when the "Dump DisplayList" button is clicked.

Change-Id:I1996bbd1cbe32d8bc25708453c777800385fe444*/




//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/scene/ViewManager.java b/hierarchyviewer/src/com/android/hierarchyviewer/scene/ViewManager.java
//Synthetic comment -- index df2a63e..ba346df 100644

//Synthetic comment -- @@ -35,6 +35,10 @@
sendCommand("REQUEST_LAYOUT", device, window, params);
}

    public static void outputDisplayList(IDevice device, Window window, String params) {
        sendCommand("OUTPUT_DISPLAYLIST", device, window, params);
    }

private static void sendCommand(String command, IDevice device, Window window, String params) {
Socket socket = null;
BufferedWriter out = null;








//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/ui/Workspace.java b/hierarchyviewer/src/com/android/hierarchyviewer/ui/Workspace.java
//Synthetic comment -- index a7db985..82375e0 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.hierarchyviewer.scene.ViewNode;
import com.android.hierarchyviewer.scene.WindowsLoader;
import com.android.hierarchyviewer.scene.ProfilesLoader;
import com.android.hierarchyviewer.ui.action.DumpDisplayListAction;
import com.android.hierarchyviewer.ui.util.PsdFileFilter;
import com.android.hierarchyviewer.util.OS;
import com.android.hierarchyviewer.util.WorkerThread;
//Synthetic comment -- @@ -145,6 +146,7 @@
private JPanel mainPanel;
private JProgressBar progress;
private JToolBar buttonsPanel;
    private JToolBar commandButtonsPanel;

private JComponent deviceSelector;
private DevicesTableModel devicesTableModel;
//Synthetic comment -- @@ -154,6 +156,7 @@
private Window currentWindow = Window.FOCUSED_WINDOW;

private JButton displayNodeButton;
    private JButton dumpDisplayListButton;
private JButton captureLayersButton;
private JButton invalidateButton;
private JButton requestLayoutButton;
//Synthetic comment -- @@ -202,6 +205,7 @@
actionsMap.put(StopServerAction.ACTION_NAME, new StopServerAction(this));
actionsMap.put(InvalidateAction.ACTION_NAME, new InvalidateAction(this));
actionsMap.put(RequestLayoutAction.ACTION_NAME, new RequestLayoutAction(this));
        actionsMap.put(DumpDisplayListAction.ACTION_NAME, new DumpDisplayListAction(this));
actionsMap.put(CaptureNodeAction.ACTION_NAME, new CaptureNodeAction(this));
actionsMap.put(CaptureLayersAction.ACTION_NAME, new CaptureLayersAction(this));
actionsMap.put(RefreshWindowsAction.ACTION_NAME, new RefreshWindowsAction(this));
//Synthetic comment -- @@ -210,11 +214,12 @@
private JComponent buildMainPanel() {
mainPanel = new JPanel();
mainPanel.setLayout(new BorderLayout());
        commandButtonsPanel = buildToolBar();
        mainPanel.add(commandButtonsPanel, BorderLayout.PAGE_START);
mainPanel.add(deviceSelector = buildDeviceSelector(), BorderLayout.CENTER);
mainPanel.add(buildStatusPanel(), BorderLayout.SOUTH);

        mainPanel.setPreferredSize(new Dimension(1200, 800));

return mainPanel;
}
//Synthetic comment -- @@ -481,6 +486,11 @@
displayNodeButton.putClientProperty("JButton.segmentPosition", "first");
toolBar.add(displayNodeButton);

        dumpDisplayListButton = new JButton();
        dumpDisplayListButton.setAction(actionsMap.get(DumpDisplayListAction.ACTION_NAME));
        dumpDisplayListButton.putClientProperty("JButton.buttonType", "segmentedTextured");
        dumpDisplayListButton.putClientProperty("JButton.segmentPosition", "middle");

captureLayersButton = new JButton();
captureLayersButton.setAction(actionsMap.get(CaptureLayersAction.ACTION_NAME));
captureLayersButton.putClientProperty("JButton.buttonType", "segmentedTextured");
//Synthetic comment -- @@ -502,6 +512,17 @@
return toolBar;
}

    private void setupProtocolDependentToolbar() {
        // Some functionality is only enabled in certain versions of the protocol.
        // Add/remove those buttons here
        if (protocolVersion < 4) {
            commandButtonsPanel.remove(dumpDisplayListButton);
        } else if (dumpDisplayListButton.getParent() == null) {
            commandButtonsPanel.add(dumpDisplayListButton,
                    commandButtonsPanel.getComponentCount() - 1);
        }
    }

private JMenuBar buildMenuBar() {
JMenuBar menuBar = new JMenuBar();

//Synthetic comment -- @@ -885,6 +906,7 @@
displayNodeButton.setEnabled(false);
captureLayersButton.setEnabled(false);
invalidateButton.setEnabled(false);
            dumpDisplayListButton.setEnabled(false);
requestLayoutButton.setEnabled(false);
graphViewButton.setEnabled(false);
pixelPerfectViewButton.setEnabled(false);
//Synthetic comment -- @@ -914,6 +936,7 @@
displayNodeButton.setEnabled(false);
captureLayersButton.setEnabled(false);
invalidateButton.setEnabled(false);
            dumpDisplayListButton.setEnabled(false);
graphViewButton.setEnabled(false);
pixelPerfectViewButton.setEnabled(false);
requestLayoutButton.setEnabled(false);
//Synthetic comment -- @@ -1008,6 +1031,13 @@
return new CaptureNodeTask();
}

    public SwingWorker<?, ?> outputDisplayList() {
        if (scene.getFocusedObject() == null) {
            return null;
        }
        return new DumpDisplayListTask();
    }

public SwingWorker<?, ?> captureLayers() {
JFileChooser chooser = new JFileChooser();
chooser.setFileFilter(new PsdFileFilter());
//Synthetic comment -- @@ -1081,6 +1111,27 @@
}
}

    private class DumpDisplayListTask extends SwingWorker<Object, Void> {
        private String captureParams;

        private DumpDisplayListTask() {
            captureParams = scene.getFocusedObject().toString();
            beginTask();
        }

        @Override
        @WorkerThread
        protected Object doInBackground() throws Exception {
            ViewManager.outputDisplayList(currentDevice, currentWindow, captureParams);
            return null;
        }

        @Override
        protected void done() {
            endTask();
        }
    }

private class RequestLayoutTask extends SwingWorker<Object, Void> {
private String captureParams;

//Synthetic comment -- @@ -1182,7 +1233,7 @@
WindowsResult result = get();
protocolVersion = result.protocolVersion;
serverVersion = result.serverVersion;
                setupProtocolDependentToolbar();
windowsTableModel.clear();
windowsTableModel.addWindows(result.windows);
} catch (ExecutionException e) {
//Synthetic comment -- @@ -1324,6 +1375,7 @@
public void focusChanged(ObjectSceneEvent e, Object oldFocus, Object newFocus) {
displayNodeButton.setEnabled(true);
invalidateButton.setEnabled(true);
            dumpDisplayListButton.setEnabled(true);
requestLayoutButton.setEnabled(true);

Set<Object> selection = new HashSet<Object>();








//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/ui/action/DumpDisplayListAction.java b/hierarchyviewer/src/com/android/hierarchyviewer/ui/action/DumpDisplayListAction.java
new file mode 100644
//Synthetic comment -- index 0000000..3e66794

//Synthetic comment -- @@ -0,0 +1,39 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.hierarchyviewer.ui.action;

import com.android.hierarchyviewer.ui.Workspace;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class DumpDisplayListAction extends BackgroundAction {
    public static final String ACTION_NAME = "dumpDisplayList";
    private Workspace mWorkspace;

    public DumpDisplayListAction(Workspace workspace) {
        putValue(NAME, "Dump DisplayList");
        putValue(SHORT_DESCRIPTION, "Dump DisplayList");
        putValue(LONG_DESCRIPTION, "Dump DisplayList");
        this.mWorkspace = workspace;
    }

    public void actionPerformed(ActionEvent e) {
        executeBackgroundTask(mWorkspace.outputDisplayList());
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 54a5fd6..bf18965 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.actions.CapturePSDAction;
import com.android.hierarchyviewerlib.actions.DisplayViewAction;
import com.android.hierarchyviewerlib.actions.DumpDisplayListAction;
import com.android.hierarchyviewerlib.actions.InspectScreenshotAction;
import com.android.hierarchyviewerlib.actions.InvalidateAction;
import com.android.hierarchyviewerlib.actions.LoadOverlayAction;
//Synthetic comment -- @@ -38,6 +39,8 @@
import com.android.hierarchyviewerlib.actions.RequestLayoutAction;
import com.android.hierarchyviewerlib.actions.SavePixelPerfectAction;
import com.android.hierarchyviewerlib.actions.SaveTreeViewAction;
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;
//Synthetic comment -- @@ -86,8 +89,8 @@
public class HierarchyViewerApplication extends ApplicationWindow {

private static final String APP_NAME = "Hierarchy Viewer";
    private static final int INITIAL_WIDTH = 1280;
    private static final int INITIAL_HEIGHT = 800;

private static HierarchyViewerApplication sMainWindow;

//Synthetic comment -- @@ -124,6 +127,8 @@
private PixelPerfectLoupe mPixelPerfectLoupe;
private Composite mTreeViewControls;

    private ActionButton dumpDisplayList;

private HierarchyViewerDirector mDirector;

/*
//Synthetic comment -- @@ -366,7 +371,7 @@

Composite innerButtonPanel = new Composite(buttonPanel, SWT.NONE);
innerButtonPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        GridLayout innerButtonPanelLayout = new GridLayout(7, true);
innerButtonPanelLayout.marginWidth = innerButtonPanelLayout.marginHeight = 2;
innerButtonPanelLayout.horizontalSpacing = innerButtonPanelLayout.verticalSpacing = 2;
innerButtonPanel.setLayout(innerButtonPanelLayout);
//Synthetic comment -- @@ -394,6 +399,10 @@
new ActionButton(innerButtonPanel, RequestLayoutAction.getAction());
requestLayout.setLayoutData(new GridData(GridData.FILL_BOTH));

        dumpDisplayList =
                new ActionButton(innerButtonPanel, DumpDisplayListAction.getAction());
        dumpDisplayList.setLayoutData(new GridData(GridData.FILL_BOTH));

SashForm mainSash = new SashForm(mTreeViewPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
Composite treeViewContainer = new Composite(mainSash, SWT.BORDER);
//Synthetic comment -- @@ -657,6 +666,14 @@
treeViewMenu.add(new Separator());
treeViewMenu.add(RefreshViewAction.getAction());
treeViewMenu.add(DisplayViewAction.getAction(getShell()));
        // Only make the DumpDisplayList action visible if the protocol supports it.
        ViewServerInfo info = DeviceSelectionModel.getModel().getSelectedDeviceInfo();
        if (info != null && info.protocolVersion >= 4) {
            treeViewMenu.add(DumpDisplayListAction.getAction());
            dumpDisplayList.setVisible(true);
        } else {
            dumpDisplayList.setVisible(false);
        }
treeViewMenu.add(new Separator());
treeViewMenu.add(InvalidateAction.getAction());
treeViewMenu.add(RequestLayoutAction.getAction());








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java
//Synthetic comment -- index 4681c40..ca3c689 100644

//Synthetic comment -- @@ -73,4 +73,8 @@
public void addSelectionListener(SelectionListener listener) {
mButton.addSelectionListener(listener);
}

    public void setVisible(boolean visible) {
        mButton.setVisible(visible);
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 77f8d74..23dfbea 100644

//Synthetic comment -- @@ -166,7 +166,7 @@
return;
}
Window[] windows = DeviceBridge.loadWindows(device);
        DeviceSelectionModel.getModel().addDevice(device, windows, viewServerInfo);
if (viewServerInfo.protocolVersion >= 3) {
WindowUpdater.startListenForWindowChanges(HierarchyViewerDirector.this, device);
focusChanged(device);
//Synthetic comment -- @@ -586,6 +586,17 @@
}
}

    public void dumpDisplayListForCurrentNode() {
        final DrawableViewNode selectedNode = TreeViewModel.getModel().getSelection();
        if (selectedNode != null) {
            executeInBackground("Dump displaylist", new Runnable() {
                public void run() {
                    DeviceBridge.outputDisplayList(selectedNode.viewNode);
                }
            });
        }
    }

public void loadAllViews() {
executeInBackground("Loading all views", new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DumpDisplayListAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DumpDisplayListAction.java
new file mode 100644
//Synthetic comment -- index 0000000..8b9ba29

//Synthetic comment -- @@ -0,0 +1,56 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.hierarchyviewerlib.actions;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class DumpDisplayListAction extends SelectedNodeEnabledAction implements ImageAction {

    private static DumpDisplayListAction sAction;

    private Image mImage;

    private DumpDisplayListAction() {
        super("Dump DisplayList");
        ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
        setToolTipText("Request the view to output its displaylist to logcat");
    }

    public static DumpDisplayListAction getAction() {
        if (sAction == null) {
            sAction = new DumpDisplayListAction();
        }
        return sAction;
    }

    @Override
    public void run() {
        HierarchyViewerDirector.getDirector().dumpDisplayListForCurrentNode();
    }

    public Image getImage() {
        return mImage;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 40cc3a9..610f7b3 100644

//Synthetic comment -- @@ -643,4 +643,18 @@
}
}

    public static void outputDisplayList(ViewNode viewNode) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("OUTPUT_DISPLAYLIST " +
                    viewNode.window.encode() + " " + viewNode); //$NON-NLS-1$
        } catch (Exception e) {
            Log.e(TAG, "Unable to dump displaylist for node " + viewNode + " in window "
                    + viewNode.window + " on device " + viewNode.window.getDevice());
        } finally {
            connection.close();
        }
    }

}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index d029d39..b00a1dc 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.IDevice;
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.device.Window;

import java.util.ArrayList;
//Synthetic comment -- @@ -29,7 +30,7 @@
*/
public class DeviceSelectionModel {

    private final HashMap<IDevice, DeviceInfo> mDeviceMap = new HashMap<IDevice, DeviceInfo>();

private final HashMap<IDevice, Integer> mFocusedWindowHashes = new HashMap<IDevice, Integer>();

//Synthetic comment -- @@ -44,6 +45,15 @@

private static DeviceSelectionModel sModel;

    private static class DeviceInfo {
        Window[] windows;
        ViewServerInfo viewServerInfo;

        private DeviceInfo(Window[] windows, ViewServerInfo viewServerInfo) {
            this.windows = windows;
            this.viewServerInfo = viewServerInfo;
        }
    }
public static DeviceSelectionModel getModel() {
if (sModel == null) {
sModel = new DeviceSelectionModel();
//Synthetic comment -- @@ -57,9 +67,9 @@
}
}

    public void addDevice(IDevice device, Window[] windows, ViewServerInfo info) {
synchronized (mDeviceMap) {
            mDeviceMap.put(device, new DeviceInfo(windows, info));
mDeviceList.add(device);
}
notifyDeviceConnected(device);
//Synthetic comment -- @@ -88,7 +98,12 @@
public void updateDevice(IDevice device, Window[] windows) {
boolean selectionChanged = false;
synchronized (mDeviceMap) {
            DeviceInfo oldDeviceInfo = mDeviceMap.get(device);
            ViewServerInfo oldViewServerInfo = null;
            if (oldDeviceInfo != null) {
                oldViewServerInfo = oldDeviceInfo.viewServerInfo;
            }
            mDeviceMap.put(device, new DeviceInfo(windows, oldViewServerInfo));
// If the selected window no longer exists, we clear the selection.
if (mSelectedDevice == device && mSelectedWindow != null) {
boolean windowStillExists = false;
//Synthetic comment -- @@ -214,9 +229,12 @@
}

public Window[] getWindows(IDevice device) {
        Window[] windows = null;
synchronized (mDeviceMap) {
            DeviceInfo info = mDeviceMap.get(device);
            if (info != null) {
                windows = mDeviceMap.get(device).windows;
            }
}
return windows;
}
//Synthetic comment -- @@ -253,4 +271,15 @@
return mSelectedWindow;
}
}

    public ViewServerInfo getSelectedDeviceInfo() {
        synchronized (mDeviceMap) {
            ViewServerInfo viewServerInfo = null;
            if (mSelectedDevice != null) {
                return mDeviceMap.get(mSelectedDevice).viewServerInfo;
            }
            return null;
        }
    }

}







