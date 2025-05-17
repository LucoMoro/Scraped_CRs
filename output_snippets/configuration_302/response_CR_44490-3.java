//<Beginning of snippet n. 0>
String name = d.getName();
if (name.equals("3.7 FWVGA slider")) {
    name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);
String resolution = d.getScreenResolution(); 
String density = d.getScreenDensity(); 

try {
    // Display device information in a tabbed list format
    TabItem deviceInfoItem = new TabItem(tabFolder, SWT.NONE);
    deviceInfoItem.setText(name);
    String deviceInfo = "Resolution: " + resolution + "\nDensity: " + density;
    deviceInfoItem.setControl(new Label(tabFolder, SWT.NONE) {{
        setText(deviceInfo);
    }});
} catch (Exception e) {
    // Handle any exceptions that occur during display
    MessageDialog.openError(shell, "Error", "Failed to display device information: " + e.getMessage());
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.TabFolder;
import org.eclipse.swt.custom.TabItem;

shell.setSize(600, 400);

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem sitesTabItem = new TabItem(tabFolder, SWT.NONE);
sitesTabItem.setText("Sites");
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import com.android.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.widgets.DeviceCreationDialog;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.utils.ILogger;

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

GridLayout glShell = new GridLayout(2, false);
glShell.verticalSpacing = 0;
glShell.horizontalSpacing = 0;
glShell.marginWidth = 0;
glShell.marginHeight = 0;
mShell.setLayout(glShell);

mShell.setMinimumSize(new Point(500, 300));

private void createContents() {
    mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData, mDeviceManager);
    mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

MenuItem menuBarDevices = new MenuItem(menuBar, SWT.CASCADE);
menuBarDevices.setText("Devices");
setupDevices(menuBarDevices);

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

    Map<String, List<Device>> devices = new HashMap<String, List<Device>>();
    for (Device d : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
        List<Device> l;
        if (devices.containsKey(d.getManufacturer())) {
            l = devices.get(d.getManufacturer());
        } else {
            l = new ArrayList<Device>();
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
            deviceItem.setText(d.getName());
            deviceItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    DeviceCreationDialog dlg = new DeviceCreationDialog(
                            mShell, mDeviceManager, mUpdaterData.getImageFactory(), d);
                    if (dlg.open() == Window.OK) {
                        setupDevices(menuBarDevices);
                    }
                }
            });
        }
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 4>
public AvdCreationDialog(Shell shell, AvdManager avdManager) {
    if (mAvdInfo != null) {
        fillExistingAvdInfo(mAvdInfo);
    }
    validatePage();
    mStatusLabel = new Label(mStatusComposite, SWT.NONE);
    mStatusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    mStatusLabel.setText(""); 
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
public void setSettingsController(SettingsController controller) {
    mController = controller;
}
//<End of snippet n. 5>

//<Beginning of snippet n. 6>
private Button mOkButton;
private Hardware mHardware;

private static final Software mSoftware;

static {
    mUserDevices = mManager.getUserDevices();
}

@Override
protected Control createContents(Composite parent) {
    Control control = super.createContents(parent);
    mManager.addUserDevice(d);
}
mManager.saveUserDevices();
super.okPressed();
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