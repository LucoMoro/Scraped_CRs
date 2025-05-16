//<Beginning of snippet n. 0>
String name = d.getName();
if (name.equals("3.7 FWVGA slider")) {
    name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);
String resolution = d.getResolution(); // Assuming Device class has getResolution method
String density = d.getDensity(); // Assuming Device class has getDensity method

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

TabItem devicesTabItem = new TabItem(tabFolder, SWT.NONE);
devicesTabItem.setText("Devices");

Composite devicesComposite = new Composite(tabFolder, SWT.NONE);
devicesTabItem.setControl(devicesComposite);
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import com.android.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdkuilib.internal.repository.DeviceCreationDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
        List<Device> l;
        if (devices.containsKey(d.getManufacturer())) {
            l = devices.get(d.getManufacturer());
        } else {
            l = new ArrayList<>();
            devices.put(d.getManufacturer(), l);
        }
        l.add(d);
    }

    for (String manufacturer : devices.keySet()) {
        MenuItem manufacturerItem = new MenuItem(menuDevices, SWT.CASCADE);
        manufacturerItem.setText(manufacturer);
        Menu manufacturerMenu = new Menu(menuDevices);
        manufacturerItem.setMenu(manufacturerMenu);
        for (final Device d : devices.get(manufacturer)) {
            MenuItem deviceItem = new MenuItem(manufacturerMenu, SWT.NONE);
            deviceItem.setText(String.format("%s (%s, %s)", d.getName(), d.getResolution(), d.getDensity()));
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