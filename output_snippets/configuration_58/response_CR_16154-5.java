//<Beginning of snippet n. 0>


import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import java.util.List;

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
        LoupeFeature loupe = new LoupeFeature(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        deviceSelector.terminate();
        loupe.terminate();
        ImageLoader.dispose();
        display.dispose();
    }
}

//<End of snippet n. 1>


//<Beginning of snippet n. 2>


package com.android.hierarchyviewerlib;

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;

public class ComponentRegistry {

    private static DeviceSelectionModel deviceSelectionModel;
    private static HierarchyViewerDirector director;

    public static HierarchyViewerDirector getDirector() {
        return director;
    }

    public static void setDirector(HierarchyViewerDirector hierarchyViewerDirector) {
        director = hierarchyViewerDirector;
    }

    public static void setDeviceSelectionModel(DeviceSelectionModel selectionModel) {
        deviceSelectionModel = selectionModel;
    }

    public static DeviceSelectionModel getDeviceSelectionModel() {
        return deviceSelectionModel;
    }
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
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;

public class HierarchyViewerApplicationDirector implements IDeviceChangeListener {

    public static final String TAG = "hierarchyviewer";

    public void terminate() {
        WindowUpdater.terminate();
    }

    public void deviceChanged(IDevice device) {
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

    private void focusChanged(IDevice device) {
        // Handle focus change
    }

    private void executeInBackground(Runnable runnable) {
        new Thread(runnable).start();
    }
}

//<End of snippet n. 3>


//<Beginning of snippet n. 4>


package com.android.hierarchyviewerlib;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;

public class ViewServerConnection {

    private IDevice device;

    public ViewServerConnection(IDevice device) {
        this.device = device;
    }

    public int getServerVersion() {
        int server = -1;
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("SERVER");
            server = Integer.parseInt(connection.getInputStream().readLine());
        } catch (IOException e) {
            Log.e(TAG, "Unable to get view server version from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return server;
    }

    public int getProtocolVersion() {
        int protocol = -1;
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("PROTOCOL");
            protocol = Integer.parseInt(connection.getInputStream().readLine());
        } catch (IOException e) {
            Log.e(TAG, "Unable to get view server protocol version from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return protocol;
    }

    public int getFocus() {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("GET_FOCUS");
            String line = connection.getInputStream().readLine();
            if (line.length() == 0) {
                return -1;
            }
            return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
        } catch (IOException e) {
            return -1;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}

//<End of snippet n. 4>


//<Beginning of snippet n. 5>


package com.android.hierarchyviewerlib;

import org.eclipse.swt.widgets.Shell;

public class LoupeFeature {
    private Shell shell;

    public LoupeFeature(Shell shell) {
        this.shell = shell;
    }

    public void activateLoupe() {
        // Implement loupe activation behavior
    }

    public void terminate() {
        // Clean up resources associated with the loupe feature
    }
}

//<End of snippet n. 5>


//<Beginning of snippet n. 6>


package com.android.hierarchyviewerlib.models;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String name;
    private List<TreeNode> children;

    public TreeNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }
}

//<End of snippet n. 6>