//<Beginning of snippet n. 0>

//   same space
// * Add in screen resolution and density
String name = d.getName();
if (name.equals("3.7 FWVGA slider")) {
// Fix metadata: this one entry doesn't have "in" like the rest of them
    name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);
String resolution = d.getResolution(); // Assuming there's a method to get resolution
String density = d.getDensity(); // Assuming there's a method to get density
String displayInfo = String.format("%s - Resolution: %s, Density: %s", name, resolution, density);
System.out.println(displayInfo);

//<End of snippet n. 0>


//<Beginning of snippet n. 1>

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.List;

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem devicesTabItem = new TabItem(tabFolder, SWT.NONE);
devicesTabItem.setText("Devices");

List deviceList = new List(tabFolder, SWT.BORDER | SWT.V_SCROLL);
deviceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
devicesTabItem.setControl(deviceList);

for (Device d : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
    String name = d.getName();
    String resolution = d.getResolution(); // Assuming method exists
    String density = d.getDensity(); // Assuming method exists
    deviceList.add(String.format("%s - Resolution: %s, Density: %s", name, resolution, density));
}

//<End of snippet n. 1>


//<Beginning of snippet n. 2>

import com.android.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdkuilib.internal.repository.DeviceCreationDialog;
import com.android.sdkuilib.internal.widgets.DeviceCreationDialog;
import com.android.utils.ILogger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ... other code ...

private void setupDevices(final MenuItem menuBarDevices) {
    Menu menuDevices = new Menu(menuBarDevices);
    menuBarDevices.setMenu(menuDevices);
    
    addDeviceMenuItems(menuDevices);
}

private void addDeviceMenuItems(Menu menuDevices) {
    Map<String, List<Device>> devices = new HashMap<>();
    for (Device d : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
        devices.computeIfAbsent(d.getManufacturer(), k -> new ArrayList<>()).add(d);
    }

    for (String manufacturer : devices.keySet()) {
        MenuItem manufacturerItem = new MenuItem(menuDevices, SWT.CASCADE);
        manufacturerItem.setText(manufacturer);
        Menu manufacturerMenu = new Menu(menuDevices);
        manufacturerItem.setMenu(manufacturerMenu);
        
        for (final Device d : devices.get(manufacturer)) {
            MenuItem deviceItem = new MenuItem(manufacturerMenu, SWT.NONE);
            deviceItem.setText(d.getName());
            deviceItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    DeviceCreationDialog dlg = new DeviceCreationDialog(mShell, mDeviceManager, mUpdaterData.getImageFactory(), d);
                    dlg.open();
                }
            });
        }
    }
}

//<End of snippet n. 2>


//<Beginning of snippet n. 4>

// ... previous code ...

}

/**
 * Returns the list of system images of a target.
 * <p/>
 * @return new ISystemImage[0];
 */

//<End of snippet n. 4>


//<Beginning of snippet n. 5>

public void setSettingsController(SettingsController controller) {
    mController = controller;
}

//<End of snippet n. 5>


//<Beginning of snippet n. 6>

private Button mOkButton;

// The hardware instance attached to each of the states of the created device
private Hardware mHardware;

// ... other code ...

@Override
protected Control createContents(Composite parent) {
    Control control = super.createContents(parent);
    mManager.addUserDevice(d);
    return control;
}

//<End of snippet n. 6>


//<Beginning of snippet n. 7>

public final class GridLayoutBuilder {

    private static GridLayout mGL;

    private GridLayoutBuilder() {
        mGL = new GridLayout();
    }

    static public GridLayoutBuilder create(Composite parent) {
        GridLayoutBuilder glh = new GridLayoutBuilder();
        parent.setLayout(GridLayoutBuilder.mGL);
        return glh;
    }

//<End of snippet n. 7>