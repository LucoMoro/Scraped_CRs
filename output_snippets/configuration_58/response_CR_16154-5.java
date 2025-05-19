//<Beginning of snippet n. 0>


import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.device.WindowUpdater;

public class HierarchyViewerApplication {
    public static void main(String[] args) {
        HierarchyViewerDirector director = new HierarchyViewerDirector();
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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



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
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


package com.android.hierarchyviewerlib;

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;

private static DeviceSelectionModel deviceSelectionModel;
private static HierarchyViewerDirector director;

public static HierarchyViewerDirector getDirector() {
    return director;
}

public static void setDeviceSelectionModel(DeviceSelectionModel deviceSelectionModel) {
    ComponentRegistry.deviceSelectionModel = deviceSelectionModel;
}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>



package com.android.hierarchyviewerlib;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.device.WindowUpdater;

public class HierarchyViewer {
    public static final String TAG = "hierarchyviewer";

    public void terminate() {
        WindowUpdater.terminate();
    }

    public void initiateWindowMonitor(IDevice device) {
        ViewServerInfo viewServerInfo = DeviceBridge.loadViewServerInfo(device);
        executeInBackground(new Runnable() {
            public void run() {
                Window[] windows = DeviceBridge.loadWindows(device);
                ComponentRegistry.getDeviceSelectionModel().addDevice(device, windows);
            }
        });
        if (viewServerInfo.protocolVersion >= 3) {
            WindowUpdater.startListenForWindowChanges(this, device);
        }
    }
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


import java.io.IOException;

public int getViewServerFocus(IDevice device) {
    DeviceConnection connection = null;
    int server = -1;
    try {
        connection = new DeviceConnection(device);
        connection.sendCommand("SERVER");
        server = Integer.parseInt(connection.getInputStream().readLine());
    } catch (IOException e) {
        Log.e(TAG, "Unable to get view server version from device " + device);
        // Handle error and attempt recovery
        handleError(e);
        return -1; 
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing connection: " + e.getMessage());
            }
        }
    }
    return server;
}

private void handleError(IOException e) {
    // Implement recovery actions here
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


import java.util.ArrayList;

public class ViewHierarchy {
    private Node root;

    private class Node {
        String component;
        ArrayList<Node> children;

        Node(String component) {
            this.component = component;
            this.children = new ArrayList<>();
        }
    }

    public void addComponent(String parentComponent, String component) {
        Node parentNode = findNode(root, parentComponent);
        if (parentNode != null) {
            parentNode.children.add(new Node(component));
        }
    }

    private Node findNode(Node node, String component) {
        if (node == null) return null;
        if (node.component.equals(component)) return node;
        for (Node child : node.children) {
            Node found = findNode(child, component);
            if (found != null) return found;
        }
        return null;
    }

    public void updateViewHierarchy() {
        // Implement logic to support dynamic updates to the view hierarchy
    }
}

//<End of snippet n. 5>










//<Beginning of snippet n. 7>



public void widgetDefaultSelected(SelectionEvent e) {
    if (e.detail == SWT.DOUBLE_CLICK) {
        openViewHierarchy();
    }
}

private void openViewHierarchy() {
    // Logic to open the view hierarchy goes here
    ViewHierarchy viewHierarchy = new ViewHierarchy();
    // Add logic to retrieve and populate view hierarchy
}

public void widgetSelected(SelectionEvent e) {
    // Additional logic for when a widget is selected
}

//<End of snippet n. 7>