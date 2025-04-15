/*AVD: display devices in a tab+list.

Change-Id:I70c6e0fa0b9622e8050e5d949674377e5ac0ffad*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java
//Synthetic comment -- index 32f8e9d..95aeb4c 100644

//Synthetic comment -- @@ -205,9 +205,9 @@
//   same space
// * Add in screen resolution and density
String name = d.getName();
        if (name.equals("3.7 FWVGA slider")) {
// Fix metadata: this one entry doesn't have "in" like the rest of them
            name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 2b9f072..77f82b1 100755

//Synthetic comment -- @@ -54,7 +54,6 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -110,7 +109,6 @@
shell.setSize(600, 400);

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem sitesTabItem = new TabItem(tabFolder, SWT.NONE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index ae6ba1c..36e01ba 100755

//Synthetic comment -- @@ -18,7 +18,6 @@


import com.android.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
//Synthetic comment -- @@ -27,30 +26,27 @@
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

/**
* This is an intermediate version of the {@link AvdManagerPage}
//Synthetic comment -- @@ -169,10 +165,6 @@
});

GridLayout glShell = new GridLayout(2, false);
        glShell.verticalSpacing = 0;
        glShell.horizontalSpacing = 0;
        glShell.marginWidth = 0;
        glShell.marginHeight = 0;
mShell.setLayout(glShell);

mShell.setMinimumSize(new Point(500, 300));
//Synthetic comment -- @@ -184,8 +176,36 @@

private void createContents() {

        mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData, mDeviceManager);
        mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

@SuppressWarnings("unused")
//Synthetic comment -- @@ -242,10 +262,6 @@
e.printStackTrace();
}
}

        MenuItem menuBarDevices = new MenuItem(menuBar, SWT.CASCADE);
        menuBarDevices.setText("Devices");
        setupDevices(menuBarDevices);
}


//Synthetic comment -- @@ -350,62 +366,6 @@
}

/**
     * Sets up the devices in the device menu.
     */
    @SuppressWarnings("unused")
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
                        if(dlg.open() == Window.OK) {
                            setupDevices(menuBarDevices);
                        }
                    }
                });
            }
        }
    }

    /**
* Initializes settings.
* This must be called after addExtraPages(), which created a settings page.
* Iterate through all the pages to find the first (and supposedly unique) setting page,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
new file mode 100755
//Synthetic comment -- index 0000000..d97f316

//Synthetic comment -- @@ -0,0 +1,802 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4006ac6..657dbda 100644

//Synthetic comment -- @@ -133,6 +133,7 @@
}
}
};

public AvdCreationDialog(Shell shell,
AvdManager avdManager,
//Synthetic comment -- @@ -174,6 +175,8 @@

if (mAvdInfo != null) {
fillExistingAvdInfo(mAvdInfo);
}

validatePage();
//Synthetic comment -- @@ -433,7 +436,15 @@
mStatusLabel = new Label(mStatusComposite, SWT.NONE);
mStatusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mStatusLabel.setText(""); //$NON-NLS-1$

}

/**
//Synthetic comment -- @@ -1135,6 +1146,38 @@
}
}

/**
* Returns the list of system images of a target.
* <p/>
//Synthetic comment -- @@ -1161,5 +1204,4 @@

return new ISystemImage[0];
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index ab8e1c9..42d85eb 100644

//Synthetic comment -- @@ -438,6 +438,7 @@
public void setSettingsController(SettingsController controller) {
mController = controller;
}
/**
* Sets the table grid layout data.
*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
//Synthetic comment -- index ed52999..82972cf 100644

//Synthetic comment -- @@ -118,12 +118,15 @@

private Button mOkButton;

    // The hardware instance attached to each of the states of the created
    // device
private Hardware mHardware;
    // This contains the Software for the device. Since it has no effect on the
    // emulator whatsoever, we just use a single instance with reasonable
    // defaults.
private static final Software mSoftware;

static {
//Synthetic comment -- @@ -143,6 +146,15 @@
mUserDevices = mManager.getUserDevices();
}

@Override
protected Control createContents(Composite parent) {
Control control = super.createContents(parent);
//Synthetic comment -- @@ -1053,6 +1065,7 @@
mManager.addUserDevice(d);
}
mManager.saveUserDevices();
super.okPressed();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java
//Synthetic comment -- index fbb31ce..7e8c161 100755

//Synthetic comment -- @@ -30,7 +30,7 @@
*/
public final class GridLayoutBuilder {

    private static GridLayout mGL;

private GridLayoutBuilder() {
mGL = new GridLayout();
//Synthetic comment -- @@ -41,7 +41,7 @@
*/
static public GridLayoutBuilder create(Composite parent) {
GridLayoutBuilder glh = new GridLayoutBuilder();
        parent.setLayout(GridLayoutBuilder.mGL);
return glh;
}








