//<Beginning of snippet n. 0>


import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;

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

/**
 * This is the central point for getting access to the various parts of the hierarchy viewer.
 */

private static HierarchyViewerDirector director;
private static DeviceSelectionModel deviceSelectionModel;

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
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;

public class HierarchyViewerDirector {
    public static final String TAG = "hierarchyviewer";

    public void terminate() {
        WindowUpdater.terminate();
    }

    public void updateDevice(IDevice device) {
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
        // Logic to handle focus changes
    }
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


// Improved error handling for device connection.
try {
    DeviceConnection connection = new DeviceConnection(device);
    connection.sendCommand("SERVER");
    server = Integer.parseInt(connection.getInputStream().readLine());
} catch (IOException e) {
    Log.e(TAG, "Unable to get view server version from device " + device, e);
} finally {
    if (connection != null) {
        try {
            connection.sendCommand("PROTOCOL");
            protocol = Integer.parseInt(connection.getInputStream().readLine());
        } catch (IOException e) {
            Log.e(TAG, "Unable to get view server protocol version from device " + device, e);
        } finally {
            if (connection != null) {
                connection.sendCommand("GET_FOCUS");
                String line = connection.getInputStream().readLine();
                if (line.length() == 0) {
                    return -1;
                }
                return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
            }
        }
    }
}

//<End of snippet n. 4>










//<Beginning of snippet n. 7>



public void widgetDefaultSelected(SelectionEvent e) {
    // Logic to open view hierarchy on double click could be implemented here.
    System.out.println("Double click to open view hierarchy");
}

public void widgetSelected(SelectionEvent e) {
    // Logic to handle widget selection events can be implemented here.
    System.out.println("Widget selected: " + e.getSource());
}

//<End of snippet n. 7>