//<Beginning of snippet n. 0>


import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.device.DeviceBridge;
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











/<Beginning of snippet n. 1>


import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyvieweruilib.DeviceSelector;
import com.android.hierarchyvieweruilib.Loupe;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIThread {
    public static void runUI() {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        DeviceSelector deviceSelector = new DeviceSelector(shell);
        Loupe loupe = new Loupe(shell); 
        loupe.setEnabled(true); // Enable Loupe functionality
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











/<Beginning of snippet n. 2>


package com.android.hierarchyviewerlib;

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;

import java.util.ArrayList;
import java.util.List;

public class ComponentRegistry {

    private static DeviceSelectionModel deviceSelectionModel;
    private static HierarchyViewerDirector director;
    private static List<UIComponent> uiComponents = new ArrayList<>(); // UI hierarchy representation

    public static HierarchyViewerDirector getDirector() {
        return director;
    }

    public static void setDirector(HierarchyViewerDirector direc) {
        director = direc;
    }

    public static void setDeviceSelectionModel(DeviceSelectionModel deviceModel) {
        deviceSelectionModel = deviceModel;
    }

    public static DeviceSelectionModel getDeviceSelectionModel() {
        return deviceSelectionModel;
    }
    
    public static void addUIComponent(UIComponent component) {
        uiComponents.add(component); // Add component to the hierarchy
    }

    public static List<UIComponent> getUIComponents() {
        return uiComponents; // Return current UI hierarchy
    }
}

//<End of snippet n. 2>











/<Beginning of snippet n. 3>


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

    public void loadWindowInfo(IDevice device) {
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











/<Beginning of snippet n. 4>


private int getFocus(IDevice device) {
    DeviceConnection connection = null;
    try {
        connection = new DeviceConnection(device);
        connection.sendCommand("SERVER");
        int server = Integer.parseInt(connection.getInputStream().readLine());
        connection.sendCommand("PROTOCOL");
        int protocol = Integer.parseInt(connection.getInputStream().readLine());
        connection.sendCommand("GET_FOCUS");
        String line = connection.getInputStream().readLine();
        if (line == null || line.length() == 0) { // Enhanced null check
            return -1;
        }
        return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
    } catch (IOException e) {
        Log.e(TAG, "Error getting focus from device " + device, e); // Include exception in log
    } finally {
        if (connection != null) {
            try {
                connection.close(); 
            } catch (IOException e) {
                Log.e(TAG, "Error closing connection" + device, e); // Include exception in log
            }
        }
    }
    return -1;
}

//<End of snippet n. 4>
