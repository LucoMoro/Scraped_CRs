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

private static void sendCommand(String command, IDevice device, Window window, String params) {
Socket socket = null;
BufferedWriter out = null;








//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/ui/Workspace.java b/hierarchyviewer/src/com/android/hierarchyviewer/ui/Workspace.java
//Synthetic comment -- index a7db985..82375e0 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.hierarchyviewer.scene.ViewNode;
import com.android.hierarchyviewer.scene.WindowsLoader;
import com.android.hierarchyviewer.scene.ProfilesLoader;
import com.android.hierarchyviewer.ui.util.PsdFileFilter;
import com.android.hierarchyviewer.util.OS;
import com.android.hierarchyviewer.util.WorkerThread;
//Synthetic comment -- @@ -145,6 +146,7 @@
private JPanel mainPanel;
private JProgressBar progress;
private JToolBar buttonsPanel;

private JComponent deviceSelector;
private DevicesTableModel devicesTableModel;
//Synthetic comment -- @@ -154,6 +156,7 @@
private Window currentWindow = Window.FOCUSED_WINDOW;

private JButton displayNodeButton;
private JButton captureLayersButton;
private JButton invalidateButton;
private JButton requestLayoutButton;
//Synthetic comment -- @@ -202,6 +205,7 @@
actionsMap.put(StopServerAction.ACTION_NAME, new StopServerAction(this));
actionsMap.put(InvalidateAction.ACTION_NAME, new InvalidateAction(this));
actionsMap.put(RequestLayoutAction.ACTION_NAME, new RequestLayoutAction(this));
actionsMap.put(CaptureNodeAction.ACTION_NAME, new CaptureNodeAction(this));
actionsMap.put(CaptureLayersAction.ACTION_NAME, new CaptureLayersAction(this));
actionsMap.put(RefreshWindowsAction.ACTION_NAME, new RefreshWindowsAction(this));
//Synthetic comment -- @@ -210,11 +214,12 @@
private JComponent buildMainPanel() {
mainPanel = new JPanel();
mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buildToolBar(), BorderLayout.PAGE_START);
mainPanel.add(deviceSelector = buildDeviceSelector(), BorderLayout.CENTER);
mainPanel.add(buildStatusPanel(), BorderLayout.SOUTH);

        mainPanel.setPreferredSize(new Dimension(950, 800));

return mainPanel;
}
//Synthetic comment -- @@ -481,6 +486,11 @@
displayNodeButton.putClientProperty("JButton.segmentPosition", "first");
toolBar.add(displayNodeButton);

captureLayersButton = new JButton();
captureLayersButton.setAction(actionsMap.get(CaptureLayersAction.ACTION_NAME));
captureLayersButton.putClientProperty("JButton.buttonType", "segmentedTextured");
//Synthetic comment -- @@ -502,6 +512,17 @@
return toolBar;
}

private JMenuBar buildMenuBar() {
JMenuBar menuBar = new JMenuBar();

//Synthetic comment -- @@ -885,6 +906,7 @@
displayNodeButton.setEnabled(false);
captureLayersButton.setEnabled(false);
invalidateButton.setEnabled(false);
requestLayoutButton.setEnabled(false);
graphViewButton.setEnabled(false);
pixelPerfectViewButton.setEnabled(false);
//Synthetic comment -- @@ -914,6 +936,7 @@
displayNodeButton.setEnabled(false);
captureLayersButton.setEnabled(false);
invalidateButton.setEnabled(false);
graphViewButton.setEnabled(false);
pixelPerfectViewButton.setEnabled(false);
requestLayoutButton.setEnabled(false);
//Synthetic comment -- @@ -1008,6 +1031,13 @@
return new CaptureNodeTask();
}

public SwingWorker<?, ?> captureLayers() {
JFileChooser chooser = new JFileChooser();
chooser.setFileFilter(new PsdFileFilter());
//Synthetic comment -- @@ -1081,6 +1111,27 @@
}
}

private class RequestLayoutTask extends SwingWorker<Object, Void> {
private String captureParams;

//Synthetic comment -- @@ -1182,7 +1233,7 @@
WindowsResult result = get();
protocolVersion = result.protocolVersion;
serverVersion = result.serverVersion;

windowsTableModel.clear();
windowsTableModel.addWindows(result.windows);
} catch (ExecutionException e) {
//Synthetic comment -- @@ -1324,6 +1375,7 @@
public void focusChanged(ObjectSceneEvent e, Object oldFocus, Object newFocus) {
displayNodeButton.setEnabled(true);
invalidateButton.setEnabled(true);
requestLayoutButton.setEnabled(true);

Set<Object> selection = new HashSet<Object>();








//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/ui/action/DumpDisplayListAction.java b/hierarchyviewer/src/com/android/hierarchyviewer/ui/action/DumpDisplayListAction.java
new file mode 100644
//Synthetic comment -- index 0000000..3e66794

//Synthetic comment -- @@ -0,0 +1,39 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 54a5fd6..bf18965 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.actions.CapturePSDAction;
import com.android.hierarchyviewerlib.actions.DisplayViewAction;
import com.android.hierarchyviewerlib.actions.InspectScreenshotAction;
import com.android.hierarchyviewerlib.actions.InvalidateAction;
import com.android.hierarchyviewerlib.actions.LoadOverlayAction;
//Synthetic comment -- @@ -38,6 +39,8 @@
import com.android.hierarchyviewerlib.actions.RequestLayoutAction;
import com.android.hierarchyviewerlib.actions.SavePixelPerfectAction;
import com.android.hierarchyviewerlib.actions.SaveTreeViewAction;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;
//Synthetic comment -- @@ -86,8 +89,8 @@
public class HierarchyViewerApplication extends ApplicationWindow {

private static final String APP_NAME = "Hierarchy Viewer";
    private static final int INITIAL_WIDTH = 1024;
    private static final int INITIAL_HEIGHT = 768;

private static HierarchyViewerApplication sMainWindow;

//Synthetic comment -- @@ -124,6 +127,8 @@
private PixelPerfectLoupe mPixelPerfectLoupe;
private Composite mTreeViewControls;

private HierarchyViewerDirector mDirector;

/*
//Synthetic comment -- @@ -366,7 +371,7 @@

Composite innerButtonPanel = new Composite(buttonPanel, SWT.NONE);
innerButtonPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        GridLayout innerButtonPanelLayout = new GridLayout(6, true);
innerButtonPanelLayout.marginWidth = innerButtonPanelLayout.marginHeight = 2;
innerButtonPanelLayout.horizontalSpacing = innerButtonPanelLayout.verticalSpacing = 2;
innerButtonPanel.setLayout(innerButtonPanelLayout);
//Synthetic comment -- @@ -394,6 +399,10 @@
new ActionButton(innerButtonPanel, RequestLayoutAction.getAction());
requestLayout.setLayoutData(new GridData(GridData.FILL_BOTH));

SashForm mainSash = new SashForm(mTreeViewPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
Composite treeViewContainer = new Composite(mainSash, SWT.BORDER);
//Synthetic comment -- @@ -657,6 +666,14 @@
treeViewMenu.add(new Separator());
treeViewMenu.add(RefreshViewAction.getAction());
treeViewMenu.add(DisplayViewAction.getAction(getShell()));
treeViewMenu.add(new Separator());
treeViewMenu.add(InvalidateAction.getAction());
treeViewMenu.add(RequestLayoutAction.getAction());








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java
//Synthetic comment -- index 4681c40..ca3c689 100644

//Synthetic comment -- @@ -73,4 +73,8 @@
public void addSelectionListener(SelectionListener listener) {
mButton.addSelectionListener(listener);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 77f8d74..23dfbea 100644

//Synthetic comment -- @@ -166,7 +166,7 @@
return;
}
Window[] windows = DeviceBridge.loadWindows(device);
        DeviceSelectionModel.getModel().addDevice(device, windows);
if (viewServerInfo.protocolVersion >= 3) {
WindowUpdater.startListenForWindowChanges(HierarchyViewerDirector.this, device);
focusChanged(device);
//Synthetic comment -- @@ -586,6 +586,17 @@
}
}

public void loadAllViews() {
executeInBackground("Loading all views", new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DumpDisplayListAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DumpDisplayListAction.java
new file mode 100644
//Synthetic comment -- index 0000000..8b9ba29

//Synthetic comment -- @@ -0,0 +1,56 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 40cc3a9..610f7b3 100644

//Synthetic comment -- @@ -643,4 +643,18 @@
}
}

}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index d029d39..b00a1dc 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.IDevice;
import com.android.hierarchyviewerlib.device.Window;

import java.util.ArrayList;
//Synthetic comment -- @@ -29,7 +30,7 @@
*/
public class DeviceSelectionModel {

    private final HashMap<IDevice, Window[]> mDeviceMap = new HashMap<IDevice, Window[]>();

private final HashMap<IDevice, Integer> mFocusedWindowHashes = new HashMap<IDevice, Integer>();

//Synthetic comment -- @@ -44,6 +45,15 @@

private static DeviceSelectionModel sModel;

public static DeviceSelectionModel getModel() {
if (sModel == null) {
sModel = new DeviceSelectionModel();
//Synthetic comment -- @@ -57,9 +67,9 @@
}
}

    public void addDevice(IDevice device, Window[] windows) {
synchronized (mDeviceMap) {
            mDeviceMap.put(device, windows);
mDeviceList.add(device);
}
notifyDeviceConnected(device);
//Synthetic comment -- @@ -88,7 +98,12 @@
public void updateDevice(IDevice device, Window[] windows) {
boolean selectionChanged = false;
synchronized (mDeviceMap) {
            mDeviceMap.put(device, windows);
// If the selected window no longer exists, we clear the selection.
if (mSelectedDevice == device && mSelectedWindow != null) {
boolean windowStillExists = false;
//Synthetic comment -- @@ -214,9 +229,12 @@
}

public Window[] getWindows(IDevice device) {
        Window[] windows;
synchronized (mDeviceMap) {
            windows = mDeviceMap.get(device);
}
return windows;
}
//Synthetic comment -- @@ -253,4 +271,15 @@
return mSelectedWindow;
}
}
}







