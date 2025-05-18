//<Beginning of snippet n. 0>
String name = d.getName();
if (name.equals("3.7 FWVGA slider")) {
    name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);

// Add screen resolution and density retrieval
int screenWidth = d.getScreenWidth();
int screenHeight = d.getScreenHeight();
float density = d.getDensity();

// Validation checks for screen resolution and density
if (screenWidth <= 0 || screenHeight <= 0 || density < 0) {
    throw new IllegalArgumentException("Invalid device attributes");
}

String displayInfo = name + " (Resolution: " + screenWidth + "x" + screenHeight + ", Density: " + density + ")";
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.List;

shell.setSize(600, 400);

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
tabItem.setText("Devices");

// Implement logic to display devices in tab format (For simplicity, just adding the displayInfo for now)
tabItem.setText(displayInfo);
tabFolder.setSelection(tabItem);

// Implement a list view as well
Composite listView = new Composite(shell, SWT.NONE);
listView.setLayout(new GridLayout(1, false));
Button listViewButton = new Button(listView, SWT.PUSH);
listViewButton.setText("Show Devices List");

// List for displaying device information
List deviceList = new List(listView, SWT.BORDER | SWT.V_SCROLL);
deviceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

listViewButton.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        deviceList.removeAll();
        for (Device d : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
            String deviceInfo = d.getName() + " (Resolution: " + d.getScreenWidth() + "x" + d.getScreenHeight() + ", Density: " + d.getDensity() + ")";
            deviceList.add(deviceInfo);
        }
    }
});

//<End of snippet n. 1>
//<Beginning of snippet n. 2>
import com.android.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdkuilib.internal.repository.DeviceCreationDialog;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.utils.ILogger;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

private void setupDevices(final MenuItem menuBarDevices) {
    Menu menuDevices = new Menu(menuBarDevices);
    menuBarDevices.setMenu(menuDevices);

    MenuItem createDevice = new MenuItem(menuDevices, SWT.NONE);
    createDevice.setText("Create New Device");
    createDevice.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            DeviceCreationDialog dlg = new DeviceCreationDialog(
                    mShell, mDeviceManager, mUpdaterData.getImageFactory(), null);
            if (dlg.open() == Window.OK) {
                setupDevices(menuBarDevices);
            }
        }
    });
    new MenuItem(menuDevices, SWT.SEPARATOR);

    Map<String, List<Device>> devices = new HashMap<>();
    for (Device d : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
        List<Device> l = devices.computeIfAbsent(d.getManufacturer(), k -> new ArrayList<>());
        l.add(d);
    }

    for (String manufacturer : devices.keySet()) {
        MenuItem manufacturerItem = new MenuItem(menuDevices, SWT.CASCADE);
        manufacturerItem.setText(manufacturer);
        Menu manufacturerMenu = new Menu(menuDevices);
        manufacturerItem.setMenu(manufacturerMenu);
        for (final Device d : devices.get(manufacturer)) {
            MenuItem deviceItem = new MenuItem(manufacturerMenu, SWT.NONE);
            deviceItem.setText(d.getName() + " (Resolution: " + d.getScreenWidth() + "x" + d.getScreenHeight() + ", Density: " + d.getDensity() + ")");
            deviceItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    DeviceCreationDialog dlg = new DeviceCreationDialog(
                            mShell, mDeviceManager, mUpdaterData.getImageFactory(), d);
                    if(dlg.open() == Window.OK) {
                        setupDevices(menuBarDevices);
                    }
                }
            });
        }
    }
}
//<End of snippet n. 2>