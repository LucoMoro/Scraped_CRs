//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyvieweruilib.DeviceSelector;
import com.android.hierarchyvieweruilib.PixelPerfectView;
import com.android.hierarchyvieweruilib.Loupe;
import com.android.hierarchyvieweruilib.Tree;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIThread {
    public static void runUI() {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        DeviceSelector deviceSelector = new DeviceSelector(shell);
        
        PixelPerfectView pixelPerfectView = new PixelPerfectView(shell);
        Loupe loupe = new Loupe(shell);
        Tree tree = new Tree(shell);
        
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        deviceSelector.terminate();
        pixelPerfectView.terminate();
        loupe.terminate();
        tree.terminate();
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

private static DeviceSelectionModel deviceSelectionModel;

public static HierarchyViewerDirector getDirector() {
    return director;
}
public static void setDeviceSelectionModel(DeviceSelectionModel deviceSelectionModel) {
    ComponentRegistry.deviceSelectionModel = deviceSelectionModel;
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

/**
 * This is the class where most of the logic resides.
 */

public static final String TAG = "hierarchyviewer";

public void terminate() {
    WindowUpdater.terminate();
}
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

focusedWindow);
}
});
}
//<End of snippet n. 3>










//<Beginning of snippet n. 4>


try {
    connection = new DeviceConnection(device);
    connection.sendCommand("SERVER");
    server = Integer.parseInt(connection.getInputStream().readLine());
} catch (IOException e) {
    Log.e(TAG, "Unable to get view server version from device " + device);
} finally {
    if (connection != null) {
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("PROTOCOL");
            protocol = Integer.parseInt(connection.getInputStream().readLine());
        } catch (IOException e) {
            Log.e(TAG, "Unable to get view server protocol version from device " + device);
        } finally {
            if (connection != null) {
                connection = new DeviceConnection(device);
                connection.sendCommand("GET_FOCUS");
                String line = connection.getInputStream().readLine();
                if (line.length() == 0) {
                    return -1;
                }
                return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
            }
            return -1;
        }
    }
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>

new file mode 100644


//<End of snippet n. 5>










//<Beginning of snippet n. 6>

new file mode 100644


//<End of snippet n. 6>










//<Beginning of snippet n. 7>



public void widgetDefaultSelected(SelectionEvent e) {
    // TODO: Double click to open view hierarchy
}

public void widgetSelected(SelectionEvent e) {
    // TODO: Implement selection handling
}

//<End of snippet n. 7>










//<Beginning of snippet n. 8>

new file mode 100644


//<End of snippet n. 8>










//<Beginning of snippet n. 9>

new file mode 100644


//<End of snippet n. 9>










//<Beginning of snippet n. 10>

new file mode 100644


//<End of snippet n. 10>