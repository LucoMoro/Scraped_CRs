/*Add menu item for configuring and creating devices

Change-Id:I39e4747b818d0163d8f51c29d37424078580f247*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 5fa4020..c84506b 100755

//Synthetic comment -- @@ -19,6 +19,8 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
//Synthetic comment -- @@ -27,10 +29,12 @@
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman1.AvdManagerPage;
import com.android.sdkuilib.internal.widgets.DeviceCreationDialog;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
//Synthetic comment -- @@ -44,6 +48,11 @@
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* This is an intermediate version of the {@link AvdManagerPage}
* wrapped in its own standalone window for use from the SDK Manager 2.
//Synthetic comment -- @@ -175,61 +184,64 @@
mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

    @SuppressWarnings("unused")
    // MenuBarWrapper works using side effects
private void createMenuBar() {
Menu menuBar = new Menu(mShell, SWT.BAR);
mShell.setMenuBar(menuBar);

        // Only create the tools menu when running as standalone.
        // We don't need the tools menu when invoked from the IDE, or the SDK Manager
        // or from the AVD Chooser dialog. The only point of the tools menu is to
        // get the about box, and invoke Tools > SDK Manager, which we don't
        // need to do in these cases.
        if (mContext == AvdInvocationContext.STANDALONE) {

            MenuItem menuBarTools = new MenuItem(menuBar, SWT.CASCADE);
            menuBarTools.setText("Tools");

            Menu menuTools = new Menu(menuBarTools);
            menuBarTools.setMenu(menuTools);

            MenuItem manageSdk = new MenuItem(menuTools, SWT.NONE);
            manageSdk.setText("Manage SDK...");
            manageSdk.addSelectionListener(new SelectionAdapter() {
@Override
                public void widgetSelected(SelectionEvent event) {
                    onSdkManager();
}
            });

            try {
                new MenuBarWrapper(APP_NAME_MAC_MENU, menuTools) {
                    @Override
                    public void onPreferencesMenuSelected() {
                        SettingsDialog sd = new SettingsDialog(mShell, mUpdaterData);
                        sd.open();
}

                    @Override
                    public void onAboutMenuSelected() {
                        AboutDialog ad = new AboutDialog(mShell, mUpdaterData);
                        ad.open();
                    }

                    @Override
                    public void printError(String format, Object... args) {
                        if (mUpdaterData != null) {
                            mUpdaterData.getSdkLog().error(null, format, args);
                        }
                    }
                };
            } catch (Throwable e) {
                mUpdaterData.getSdkLog().error(e, "Failed to setup menu bar");
                e.printStackTrace();
            }
}

        MenuItem menuBarDevices = new MenuItem(menuBar, SWT.CASCADE);
        menuBarDevices.setText("Devices");
        setupDevices(menuBarDevices);
}


//Synthetic comment -- @@ -334,6 +346,62 @@
}

/**
     * Sets up the devices in the device menu.
     */
    @SuppressWarnings("unused")
    private void setupDevices(final MenuItem menuBarDevices) {
        Menu menuDevices = new Menu(menuBarDevices);
        menuBarDevices.setMenu(menuDevices);

        final DeviceManager manager = new DeviceManager(mUpdaterData.getSdkLog());

        MenuItem createDevice = new MenuItem(menuDevices, SWT.NONE);
        createDevice.setText("Create New Device");
        createDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DeviceCreationDialog dlg = new DeviceCreationDialog(
                        mShell, manager, mUpdaterData.getImageFactory(), null);
                dlg.open();
            }
        });
        new MenuItem(menuDevices, SWT.SEPARATOR);

        Map<String, List<Device>> devices = new HashMap<String, List<Device>>();
        for (Device d : manager.getDevices(mUpdaterData.getOsSdkRoot())) {
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
                                mShell, manager, mUpdaterData.getImageFactory(), d);
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..873a822

//Synthetic comment -- @@ -0,0 +1,1014 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.widgets;

import com.android.annotations.Nullable;
import com.android.resources.Density;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.ResourceEnum;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenRatio;
import com.android.resources.ScreenSize;
import com.android.resources.TouchScreen;
import com.android.sdklib.devices.Abi;
import com.android.sdklib.devices.ButtonType;
import com.android.sdklib.devices.Camera;
import com.android.sdklib.devices.CameraLocation;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Multitouch;
import com.android.sdklib.devices.Network;
import com.android.sdklib.devices.PowerType;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.devices.ScreenType;
import com.android.sdklib.devices.Sensor;
import com.android.sdklib.devices.Software;
import com.android.sdklib.devices.State;
import com.android.sdklib.devices.Storage;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.List;

public class DeviceCreationDialog extends GridDialog {

    private static final String MANUFACTURER = "User";

    private final ImageFactory mImageFactory;
    private final DeviceManager mManager;
    private List<Device> mUserDevices;

    private Device mDevice;

    private Text mDeviceName;
    private Text mDiagonalLength;
    private Text mXDimension;
    private Text mYDimension;
    private Button mKeyboard;
    private Button mDpad;
    private Button mTrackball;
    private Button mNoNav;
    private Text mRam;
    private Combo mRamCombo;
    private Combo mButtons;
    private Combo mSize;
    private Combo mDensity;
    private Combo mRatio;
    private Button mAccelerometer; // hw.accelerometer
    private Button mGyro; // hw.sensors.orientation
    private Button mGps; // hw.sensors.gps
    private Button mProximitySensor; // hw.sensors.proximity
    private Button mCameraFront;
    private Button mCameraRear;
    private Group mStateGroup;
    private Button mPortrait;
    private Label mPortraitLabel;
    private Button mPortraitNav;
    private Button mLandscape;
    private Label mLandscapeLabel;
    private Button mLandscapeNav;
    private Button mPortraitKeys;
    private Label mPortraitKeysLabel;
    private Button mPortraitKeysNav;
    private Button mLandscapeKeys;
    private Label mLandscapeKeysLabel;
    private Button mLandscapeKeysNav;

    private Button mForceCreation;
    private Label mStatusIcon;
    private Label mStatusLabel;

    private Button mOkButton;

    // The hardware instance attached to each of the states of the created
    // device
    private Hardware mHardware;
    // This contains the Software for the device. Since it has no effect on the
    // emulator whatsoever, we just use a single instance with reasonable
    // defaults.
    private static final Software mSoftware;

    static {
        mSoftware = new Software();
        mSoftware.setLiveWallpaperSupport(true);
        mSoftware.setGlVersion("2.0");
    }

    public DeviceCreationDialog(Shell parentShell,
            DeviceManager manager,
            ImageFactory imageFactory,
            @Nullable Device device) {
        super(parentShell, 2, false);
        mImageFactory = imageFactory;
        mDevice = device;
        mManager = manager;
        mUserDevices = mManager.getUserDevices();
    }

    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);

        mOkButton = getButton(IDialogConstants.OK_ID);

        if (mDevice == null) {
            getShell().setText("Create New Device");
        } else {
            if (mUserDevices.contains(mDevice)) {
                getShell().setText("Edit Device");
            } else {
                getShell().setText("Clone Device");
            }
        }

        validatePage();

        return control;
    }

    @Override
    public void createDialogContent(Composite parent) {

        ValidationListener validator = new ValidationListener();
        SizeListener sizeListener = new SizeListener();
        NavStateListener navListener = new NavStateListener();

        String tooltip = "Name of the new device";
        generateLabel("Name:", tooltip, parent);
        mDeviceName = generateText(parent, tooltip, new CreateNameModifyListener());

        tooltip = "Diagonal length of the screen in inches";
        generateLabel("Screen Size (in):", tooltip, parent);
        mDiagonalLength = generateText(parent, tooltip, sizeListener);

        tooltip = "The resolution of the device in pixels";
        generateLabel("Resolution:", tooltip, parent);
        Group dimensionGroup = new Group(parent, SWT.NONE);
        dimensionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        dimensionGroup.setLayout(new GridLayout(3, false));
        mXDimension = generateText(dimensionGroup, tooltip, sizeListener);
        new Label(dimensionGroup, SWT.NONE).setText("x");
        mYDimension = generateText(dimensionGroup, tooltip, sizeListener);

        tooltip = "The screen size bucket that the device falls into";
        generateLabel("Size:", tooltip, parent);
        mSize = generateCombo(parent, tooltip, ScreenSize.values(), 1, validator);

        tooltip = "The aspect ratio bucket the screen falls into. A \"long\" screen is wider.";
        generateLabel("Screen Ratio:", tooltip, parent);
        mRatio = generateCombo(parent, tooltip, ScreenRatio.values(), 1, validator);

        tooltip = "The pixel density bucket the device falls in";
        generateLabel("Density:", tooltip, parent);
        mDensity = generateCombo(parent, tooltip, Density.values(), 3, validator);

        generateLabel("Sensors:", "The sensors available on the device", parent);
        Group sensorGroup = new Group(parent, SWT.NONE);
        sensorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        sensorGroup.setLayout(new GridLayout(2, false));
        mAccelerometer = generateButton(sensorGroup, "Accelerometer",
                "Presence of an accelerometer", SWT.CHECK, true, validator);
        mGyro = generateButton(sensorGroup, "Gyroscope",
                "Presence of a gyroscope", SWT.CHECK, true, validator);
        mGps = generateButton(sensorGroup, "GPS", "Presence of a GPS", SWT.CHECK, true, validator);
        mProximitySensor = generateButton(sensorGroup, "Proximity Sensor",
                "Presence of a proximity sensor", SWT.CHECK, true, validator);

        generateLabel("Cameras", "The cameras available on the device", parent);
        Group cameraGroup = new Group(parent, SWT.NONE);
        cameraGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        cameraGroup.setLayout(new GridLayout(2, false));
        mCameraFront = generateButton(cameraGroup, "Front", "Presence of a front camera",
                SWT.CHECK, false, validator);
        mCameraRear = generateButton(cameraGroup, "Rear", "Presence of a rear camera",
                SWT.CHECK, true, validator);

        generateLabel("Input:", "The input hardware on the given device", parent);
        Group inputGroup = new Group(parent, SWT.NONE);
        inputGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        inputGroup.setLayout(new GridLayout(3, false));
        mKeyboard = generateButton(inputGroup, "Keyboard", "Presence of a hardware keyboard",
                SWT.CHECK, false,
                new KeyboardListener());
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        mKeyboard.setLayoutData(gridData);
        mNoNav = generateButton(inputGroup, "No Nav", "No hardware navigation",
                SWT.RADIO, true, navListener);
        mDpad = generateButton(inputGroup, "DPad", "The device has a DPad navigation element",
                SWT.RADIO, false, navListener);
        mTrackball = generateButton(inputGroup, "Trackball",
                "The device has a trackball navigation element", SWT.RADIO, false, navListener);

        tooltip = "The amount of RAM on the device";
        generateLabel("RAM:", tooltip, parent);
        Group ramGroup = new Group(parent, SWT.NONE);
        ramGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ramGroup.setLayout(new GridLayout(2, false));
        mRam = generateText(ramGroup, tooltip, validator);
        mRamCombo = new Combo(ramGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        mRamCombo.setToolTipText(tooltip);
        mRamCombo.add("MiB");
        mRamCombo.add("GiB");
        mRamCombo.select(0);
        mRamCombo.addModifyListener(validator);

        tooltip = "Type of buttons (Home, Menu, etc.) on the device. "
                + "This can be software buttons like on the Galaxy Nexus, or hardware buttons like "
                + "the capacitive buttons on the Nexus S.";
        generateLabel("Buttons:", tooltip, parent);
        mButtons = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        mButtons.setToolTipText(tooltip);
        mButtons.add("Software");
        mButtons.add("Hardware");
        mButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mButtons.select(0);
        mButtons.addModifyListener(validator);

        generateLabel("Device States:", "The available states for the given device", parent);

        mStateGroup = new Group(parent, SWT.NONE);
        mStateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mStateGroup.setLayout(new GridLayout(2, true));

        tooltip = "The device has a portait position with no keyboard available";
        mPortraitLabel = generateLabel("Portrait:", tooltip, mStateGroup);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        mPortraitLabel.setLayoutData(gridData);
        mPortrait = generateButton(mStateGroup, "Enabled", tooltip, SWT.CHECK, true,
                navListener);
        mPortraitNav = generateButton(mStateGroup, "Navigation",
                "Hardware navigation is available in this state", SWT.CHECK, true, validator);
        mPortraitNav.setEnabled(false);

        tooltip = "The device has a landscape position with no keyboard available";
        mLandscapeLabel = generateLabel("Landscape:", tooltip, mStateGroup);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        mLandscapeLabel.setLayoutData(gridData);
        mLandscape = generateButton(mStateGroup, "Enabled", tooltip, SWT.CHECK, true,
                navListener);
        mLandscapeNav = generateButton(mStateGroup, "Navigation",
                "Hardware navigation is available in this state", SWT.CHECK, true, validator);
        mLandscapeNav.setEnabled(false);

        tooltip = "The device has a portait position with a keyboard available";
        mPortraitKeysLabel = generateLabel("Portrait with keyboard:", tooltip, mStateGroup);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        mPortraitKeysLabel.setLayoutData(gridData);
        mPortraitKeysLabel.setEnabled(false);
        mPortraitKeys = generateButton(mStateGroup, "Enabled", tooltip, SWT.CHECK, true,
                navListener);
        mPortraitKeys.setEnabled(false);
        mPortraitKeysNav = generateButton(mStateGroup, "Navigation",
                "Hardware navigation is available in this state", SWT.CHECK, true, validator);
        mPortraitKeysNav.setEnabled(false);

        tooltip = "The device has a landscape position with the keyboard open";
        mLandscapeKeysLabel = generateLabel("Landscape with keyboard:", tooltip, mStateGroup);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        mLandscapeKeysLabel.setLayoutData(gridData);
        mLandscapeKeysLabel.setEnabled(false);
        mLandscapeKeys = generateButton(mStateGroup, "Enabled", tooltip, SWT.CHECK, true,
                navListener);
        mLandscapeKeys.setEnabled(false);
        mLandscapeKeysNav = generateButton(mStateGroup, "Navigation",
                "Hardware navigation is available in this state", SWT.CHECK, true, validator);
        mLandscapeKeysNav.setEnabled(false);

        mForceCreation = new Button(parent, SWT.CHECK);
        mForceCreation.setText("Override the existing device with the same name");
        mForceCreation
                .setToolTipText("There's already an AVD with the same name. Check this to delete it and replace it by the new AVD.");
        mForceCreation.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
                true, false, 2, 1));
        mForceCreation.setEnabled(false);
        mForceCreation.addSelectionListener(validator);

        // add a separator to separate from the ok/cancel button
        Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

        // add stuff for the error display
        Composite statusComposite = new Composite(parent, SWT.NONE);
        GridLayout gl;
        statusComposite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
                true, false, 3, 1));
        statusComposite.setLayout(gl = new GridLayout(2, false));
        gl.marginHeight = gl.marginWidth = 0;

        mStatusIcon = new Label(statusComposite, SWT.NONE);
        mStatusIcon.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING,
                false, false));
        mStatusLabel = new Label(statusComposite, SWT.NONE);
        mStatusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mStatusLabel.setText(""); //$NON-NLS-1$

        prefillWithDevice(mDevice);

        validatePage();
    }

    private Button generateButton(Composite parent, String text, String tooltip, int type,
            boolean selected, SelectionListener listener) {
        Button b = new Button(parent, type);
        b.setText(text);
        b.setToolTipText(tooltip);
        b.setSelection(selected);
        b.addSelectionListener(listener);
        b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return b;
    }

    /**
     * Generates a combo widget attached to the given parent, then sets the
     * tooltip, adds all of the {@link String}s returned by
     * {@link ResourceEnum#getResourceValue()} for each {@link ResourceEnum},
     * sets the combo to the given index and adds the given
     * {@link ModifyListener}.
     */
    private Combo generateCombo(Composite parent, String tooltip, ResourceEnum[] values,
            int selection,
            ModifyListener validator) {
        Combo c = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        c.setToolTipText(tooltip);
        for (ResourceEnum r : values) {
            c.add(r.getResourceValue());
        }
        c.select(selection);
        c.addModifyListener(validator);
        return c;
    }

    /** Generates a text widget with the given tooltip, parent and listener */
    private Text generateText(Composite parent, String tooltip, ModifyListener listener) {
        Text t = new Text(parent, SWT.BORDER);
        t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        t.setToolTipText(tooltip);
        t.addModifyListener(listener);
        return t;
    }

    /** Generates a label and attaches it to the given parent */
    private Label generateLabel(String text, String tooltip, Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        label.setToolTipText(tooltip);
        label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
        return label;
    }

    /**
     * Callback when the device name is changed. Enforces that device names
     * don't conflict with already existing devices unless we're editing that
     * device.
     */
    private class CreateNameModifyListener implements ModifyListener {
        @Override
        public void modifyText(ModifyEvent e) {
            String name = mDeviceName.getText();
            boolean nameCollision = false;
            for (Device d : mUserDevices) {
                if (MANUFACTURER.equals(d.getManufacturer()) && name.equals(d.getName())) {
                    nameCollision = true;
                    break;
                }
            }
            mForceCreation.setEnabled(nameCollision);
            mForceCreation.setSelection(!nameCollision);

            validatePage();
        }
    }

    /**
     * Callback attached to the diagonal length and resolution text boxes. Sets
     * the screen size and display density based on their values, then validates
     * the page.
     */
    private class SizeListener implements ModifyListener {
        @Override
        public void modifyText(ModifyEvent e) {

            if (!mDiagonalLength.getText().isEmpty()) {
                double diagonal = Double.parseDouble(mDiagonalLength.getText());
                double diagonalDp = 160.0 * diagonal;

                // Set the Screen Size
                if (diagonalDp >= 1200) {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("xlarge")));
                } else if (diagonalDp >= 800) {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("large")));
                } else if (diagonalDp >= 568) {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("normal")));
                } else {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("small")));
                }
                if (!mXDimension.getText().isEmpty() && !mYDimension.getText().isEmpty()) {

                    // Set the density based on which bucket it's closest to
                    double x = Double.parseDouble(mXDimension.getText());
                    double y = Double.parseDouble(mYDimension.getText());
                    double dpi = Math.sqrt(x * x + y * y) / diagonal;
                    double difference = Double.MAX_VALUE;
                    Density bucket = Density.MEDIUM;
                    for (Density d : Density.values()) {
                        if (Math.abs(d.getDpiValue() - dpi) < difference) {
                            difference = Math.abs(d.getDpiValue() - dpi);
                            bucket = d;
                        }
                    }
                    mDensity.select(Density.getIndex(bucket));
                }
            }
        }
    }


    /**
     * Callback attached to the keyboard checkbox.Enables / disables device
     * states based on the keyboard presence and then validates the page.
     */
    private class KeyboardListener extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent event) {
            super.widgetSelected(event);
            if (mKeyboard.getSelection()) {
                mPortraitKeys.setEnabled(true);
                mPortraitKeysLabel.setEnabled(true);
                mLandscapeKeys.setEnabled(true);
                mLandscapeKeysLabel.setEnabled(true);
            } else {
                mPortraitKeys.setEnabled(false);
                mPortraitKeysLabel.setEnabled(false);
                mLandscapeKeys.setEnabled(false);
                mLandscapeKeysLabel.setEnabled(false);
            }
            toggleNav();
            validatePage();
        }

    }

    /**
     * Listens for changes on widgets that affect nav availability and toggles
     * the nav checkboxes for device states based on them.
     */
    private class NavStateListener extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent event) {
            super.widgetSelected(event);
            toggleNav();
            validatePage();
        }
    }

    /**
     * Method that inspects all of the relevant dialog state and enables or disables the nav
     * elements accordingly.
     */
    private void toggleNav() {
        mPortraitNav.setEnabled(mPortrait.getSelection() && !mNoNav.getSelection());
        mLandscapeNav.setEnabled(mLandscape.getSelection() && !mNoNav.getSelection());
        mPortraitKeysNav.setEnabled(mPortraitKeys.getSelection() && mPortraitKeys.getEnabled()
                && !mNoNav.getSelection());
        mLandscapeKeysNav.setEnabled(mLandscapeKeys.getSelection()
                && mLandscapeKeys.getEnabled() && !mNoNav.getSelection());
        validatePage();
    }

    /**
     * Callback that validates the page on modification events or widget
     * selections
     */
    private class ValidationListener extends SelectionAdapter implements ModifyListener {
        @Override
        public void modifyText(ModifyEvent e) {
            validatePage();
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            super.widgetSelected(e);
            validatePage();
        }
    }

    /**
     * Validates all of the config options to ensure a valid device can be
     * created from them.
     *
     * @return Whether the config options will result in a valid device.
     */
    private boolean validatePage() {
        boolean valid = true;
        String error = null;
        String warning = null;
        setError(null);

        String name = mDeviceName.getText();

        /* If we're editing / cloning a device, this will get called when the name gets pre-filled
         * but the ok button won't be populated yet, so we need to skip the initial setting.
         */
        if (mOkButton != null) {
            if (mDevice == null) {
                getShell().setText("Create New Device");
                mOkButton.setText("Create Device");
            } else {
                if (mDevice.getName().equals(name)){
                    if (mUserDevices.contains(mDevice)) {
                        getShell().setText("Edit Device");
                        mOkButton.setText("Edit Device");
                    } else {
                        warning = "Only user created devices are editable.\nA clone of it will be created under " +
                        "the \"User\" category.";
                        getShell().setText("Clone Device");
                        mOkButton.setText("Clone Device");
                    }
                } else {
                    warning = "The device \"" + mDevice.getName() +"\" will be duplicated into\n" +
                            "\"" + name + "\" under the \"User\" category";
                    getShell().setText("Clone Device");
                    mOkButton.setText("Clone Device");
                }
            }
        }

        if (name.isEmpty()) {
            valid = false;
        }
        if (!validateFloat("Diagonal Length", mDiagonalLength.getText())) {
            valid = false;
        }
        if (!validateInt("Resolution", mXDimension.getText())) {
            valid = false;
        }
        if (!validateInt("Resolution", mYDimension.getText())) {
            valid = false;
        }
        if (mSize.getSelectionIndex() < 0) {
            error = "A size bucket must be selected.";
            valid = false;
        }
        if (mDensity.getSelectionIndex() < 0) {
            error = "A screen density bucket must be selected";
            valid = false;
        }
        if (mRatio.getSelectionIndex() < 0) {
            error = "A screen ratio must be selected.";
            valid = false;
        }
        if (!mNoNav.getSelection() && !mTrackball.getSelection() && !mDpad.getSelection()) {
            error = "A mode of hardware navigation, or no navigation, has to be selected.";
            valid = false;
        }
        if (!validateInt("RAM", mRam.getText())) {
            valid = false;
        }
        if (mRamCombo.getSelectionIndex() < 0) {
            error = "RAM must have a selected unit.";
            valid = false;
        }
        if (mButtons.getSelectionIndex() < 0) {
            error = "A button type must be selected.";
            valid = false;
        }
        if (mKeyboard.getSelection()) {
            if (!mPortraitKeys.getSelection()
                    && !mPortrait.getSelection()
                    && !mLandscapeKeys.getSelection()
                    && !mLandscape.getSelection()) {
                error = "At least one device state must be enabled.";
                valid = false;
            }
        } else {
            if (!mPortrait.getSelection() && !mLandscape.getSelection()) {
                error = "At least one device state must be enabled";
                valid = false;
            }
        }
        if (mForceCreation.isEnabled() && !mForceCreation.getSelection()) {
            error = "Name conflicts with an existing device.";
            valid = false;
        }

        if (mOkButton != null) {
            mOkButton.setEnabled(valid);
        }

        if (error != null) {
            setError(error);
        } else if (warning != null) {
            setWarning(warning);
        }

        return valid;
    }

    /**
     * Validates the string is a valid, positive float. If not, it sets the
     * error at the bottom of the dialog and returns false. Note this does
     * <b>not</b> unset the error message, it's up to the caller to unset it iff
     * it knows there are no errors on the page.
     */
    private boolean validateFloat(String box, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        boolean ret = true;
        try {
            double val = Double.parseDouble(value);
            if (val <= 0) {
                ret = false;
            }
        } catch (NumberFormatException e) {
            ret = false;
        }
        if (!ret) {
            setError(box + " must be a valid, positive number.");
        }
        return ret;
    }

    /**
     * Validates the string is a valid, positive integer. If not, it sets the
     * error at the bottom of the dialog and returns false. Note this does
     * <b>not</b> unset the error message, it's up to the caller to unset it iff
     * it knows there are no errors on the page.
     */
    private boolean validateInt(String box, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        boolean ret = true;
        try {
            int val = Integer.parseInt(value);
            if (val <= 0) {
                ret = false;
            }
        } catch (NumberFormatException e) {
            ret = false;
        }

        if (!ret) {
            setError(box + " must be a valid, positive integer.");
        }

        return ret;
    }

    /**
     * Sets the error to the given string. If null, removes the error message.
     */
    private void setError(@Nullable String error) {
        if (error == null) {
            mStatusIcon.setImage(null);
            mStatusLabel.setText("");
        } else {
            mStatusIcon.setImage(mImageFactory.getImageByName("reject_icon16.png"));
            mStatusLabel.setText(error);
        }
    }

    /**
     * Sets the warning message to the given string. If null, removes the
     * warning message.
     */
    private void setWarning(@Nullable String warning) {
        if (warning == null) {
            mStatusIcon.setImage(null);
            mStatusLabel.setText("");
        } else {
            mStatusIcon.setImage(mImageFactory.getImageByName("warning_icon16.png"));
            mStatusLabel.setText(warning);
        }
    }

    /** Sets the hardware for the new device */
    private void prefillWithDevice(@Nullable Device device) {
        if (device == null) {

            // Setup the default hardware instance with reasonable values for
            // the things which are configurable via this dialog.
            mHardware = new Hardware();

            Screen s = new Screen();
            s.setXdpi(316);
            s.setYdpi(316);
            s.setMultitouch(Multitouch.JAZZ_HANDS);
            s.setMechanism(TouchScreen.FINGER);
            s.setScreenType(ScreenType.CAPACITIVE);
            mHardware.setScreen(s);

            mHardware.addNetwork(Network.BLUETOOTH);
            mHardware.addNetwork(Network.WIFI);
            mHardware.addNetwork(Network.NFC);

            mHardware.addSensor(Sensor.BAROMETER);
            mHardware.addSensor(Sensor.COMPASS);
            mHardware.addSensor(Sensor.LIGHT_SENSOR);

            mHardware.setHasMic(true);
            mHardware.addInternalStorage(new Storage(4, Storage.Unit.GiB));
            mHardware.setCpu("Generic CPU");
            mHardware.setGpu("Generic GPU");

            mHardware.addSupportedAbi(Abi.ARMEABI);
            mHardware.addSupportedAbi(Abi.ARMEABI_V7A);
            mHardware.addSupportedAbi(Abi.MIPS);
            mHardware.addSupportedAbi(Abi.X86);

            mHardware.setChargeType(PowerType.BATTERY);
            return;
        }
        mHardware = device.getDefaultHardware().deepCopy();
        mDeviceName.setText(device.getName());
        mForceCreation.setSelection(true);
        Screen s = mHardware.getScreen();
        mDiagonalLength.setText(Double.toString(s.getDiagonalLength()));
        mXDimension.setText(Integer.toString(s.getXDimension()));
        mYDimension.setText(Integer.toString(s.getYDimension()));
        String size = s.getSize().getResourceValue();
        for (int i = 0; i < mSize.getItemCount(); i++) {
            if (size.equals(mSize.getItem(i))) {
                mSize.select(i);
                break;
            }
        }
        String ratio = s.getRatio().getResourceValue();
        for (int i = 0; i < mRatio.getItemCount(); i++) {
            if (ratio.equals(mRatio.getItem(i))) {
                mRatio.select(i);
                break;
            }
        }
        String density = s.getPixelDensity().getResourceValue();
        for (int i = 0; i < mDensity.getItemCount(); i++) {
            if (density.equals(mDensity.getItem(i))) {
                mDensity.select(i);
                break;
            }
        }
        mKeyboard.setSelection(!Keyboard.NOKEY.equals(mHardware.getKeyboard()));
        mDpad.setSelection(Navigation.DPAD.equals(mHardware.getNav()));
        mTrackball.setSelection(Navigation.TRACKBALL.equals(mHardware.getNav()));
        mNoNav.setSelection(Navigation.NONAV.equals(mHardware.getNav()));
        mAccelerometer.setSelection(mHardware.getSensors().contains(Sensor.ACCELEROMETER));
        mGyro.setSelection(mHardware.getSensors().contains(Sensor.GYROSCOPE));
        mGps.setSelection(mHardware.getSensors().contains(Sensor.GPS));
        mProximitySensor.setSelection(mHardware.getSensors().contains(Sensor.PROXIMITY_SENSOR));
        mCameraFront.setSelection(false);
        mCameraRear.setSelection(false);
        for (Camera c : mHardware.getCameras()) {
            if (CameraLocation.FRONT.equals(c.getLocation())) {
                mCameraFront.setSelection(true);
            } else if (CameraLocation.BACK.equals(c.getLocation())) {
                mCameraRear.setSelection(true);
            }
        }
        mRam.setText(Long.toString(mHardware.getRam().getSizeAsUnit(Storage.Unit.MiB)));
        mRamCombo.select(0);
        for (int i = 0; i < mButtons.getItemCount(); i++) {
            if (mButtons.getItem(i).equals(mHardware.getButtonType().toString())) {
                mButtons.select(i);
                break;
            }
        }

        for (State state : device.getAllStates()) {
            Button nav = null;
            if (state.getOrientation().equals(ScreenOrientation.PORTRAIT)) {
                if (state.getKeyState().equals(KeyboardState.EXPOSED)) {
                    mPortraitKeys.setSelection(true);
                    nav = mPortraitKeysNav;
                } else {
                    mPortrait.setSelection(true);
                    nav = mPortraitNav;
                }
            } else {
                if (state.getKeyState().equals(KeyboardState.EXPOSED)) {
                    mLandscapeKeys.setSelection(true);
                    nav = mLandscapeKeysNav;
                } else {
                    mLandscape.setSelection(true);
                    nav = mLandscapeNav;
                }
            }
            nav.setSelection(state.getNavState().equals(NavigationState.EXPOSED)
                    && !mHardware.getNav().equals(Navigation.NONAV));
        }
    }

    /**
     * If given a valid page, generates the corresponding device. The device is
     * then added to the user device list, replacing any previous device with
     * its given name and manufacturer, and the list is saved out to disk.
     */
    @Override
    protected void okPressed() {
        if (validatePage()) {
            Device.Builder builder = new Device.Builder();
            builder.setManufacturer("User");
            builder.setName(mDeviceName.getText());
            builder.addSoftware(mSoftware);
            Screen s = mHardware.getScreen();
            double diagonal = Double.parseDouble(mDiagonalLength.getText());
            int x = Integer.parseInt(mXDimension.getText());
            int y = Integer.parseInt(mYDimension.getText());
            s.setDiagonalLength(diagonal);
            s.setXDimension(x);
            s.setYDimension(y);
            // The diagonal DPI will be somewhere in between the X and Y dpi if
            // they differ
            double dpi = Math.sqrt(x * x + y * y) / diagonal;
            s.setXdpi(dpi);
            s.setYdpi(dpi);
            s.setPixelDensity(Density.getEnum(mDensity.getText()));
            s.setSize(ScreenSize.getEnum(mSize.getText()));
            s.setRatio(ScreenRatio.getEnum(mRatio.getText()));
            if (mAccelerometer.getSelection()) {
                mHardware.addSensor(Sensor.ACCELEROMETER);
            }
            if (mGyro.getSelection()) {
                mHardware.addSensor(Sensor.GYROSCOPE);
            }
            if (mGps.getSelection()) {
                mHardware.addSensor(Sensor.GPS);
            }
            if (mProximitySensor.getSelection()) {
                mHardware.addSensor(Sensor.PROXIMITY_SENSOR);
            }
            if (mCameraFront.getSelection()) {
                Camera c = new Camera();
                c.setAutofocus(true);
                c.setFlash(true);
                c.setLocation(CameraLocation.FRONT);
                mHardware.addCamera(c);
            }
            if (mCameraRear.getSelection()) {
                Camera c = new Camera();
                c.setAutofocus(true);
                c.setFlash(true);
                c.setLocation(CameraLocation.BACK);
                mHardware.addCamera(c);
            }
            if (mKeyboard.getSelection()) {
                mHardware.setKeyboard(Keyboard.QWERTY);
            } else {
                mHardware.setKeyboard(Keyboard.NOKEY);
            }
            if (mDpad.getSelection()) {
                mHardware.setNav(Navigation.DPAD);
            } else if (mTrackball.getSelection()) {
                mHardware.setNav(Navigation.TRACKBALL);
            } else {
                mHardware.setNav(Navigation.NONAV);
            }
            long ram = Long.parseLong(mRam.getText());
            Storage.Unit unit = Storage.Unit.getEnum(mRamCombo.getText());
            mHardware.setRam(new Storage(ram, unit));
            if (mButtons.getText().equals("Hardware")) {
                mHardware.setButtonType(ButtonType.HARD);
            } else {
                mHardware.setButtonType(ButtonType.SOFT);
            }

            // Set the first enabled state to the default state
            boolean defaultSelected = false;
            if (mPortrait.getSelection()) {
                State state = new State();
                state.setName("Portrait");
                state.setDescription("The device in portrait orientation");
                state.setOrientation(ScreenOrientation.PORTRAIT);
                if (mHardware.getNav().equals(Navigation.NONAV) || !mPortraitNav.getSelection()) {
                    state.setNavState(NavigationState.HIDDEN);
                } else {
                    state.setNavState(NavigationState.EXPOSED);
                }
                if (mHardware.getKeyboard().equals(Keyboard.NOKEY)) {
                    state.setKeyState(KeyboardState.SOFT);
                } else {
                    state.setKeyState(KeyboardState.HIDDEN);
                }
                state.setHardware(mHardware);
                if (!defaultSelected) {
                    state.setDefaultState(true);
                    defaultSelected = true;
                }
                builder.addState(state);
            }
            if (mLandscape.getSelection()) {
                State state = new State();
                state.setName("Landscape");
                state.setDescription("The device in landscape orientation");
                state.setOrientation(ScreenOrientation.LANDSCAPE);
                if (mHardware.getNav().equals(Navigation.NONAV) || !mLandscapeNav.getSelection()) {
                    state.setNavState(NavigationState.HIDDEN);
                } else {
                    state.setNavState(NavigationState.EXPOSED);
                }
                if (mHardware.getKeyboard().equals(Keyboard.NOKEY)) {
                    state.setKeyState(KeyboardState.SOFT);
                } else {
                    state.setKeyState(KeyboardState.HIDDEN);
                }
                state.setHardware(mHardware);
                if (!defaultSelected) {
                    state.setDefaultState(true);
                    defaultSelected = true;
                }
                builder.addState(state);
            }
            if (mKeyboard.getSelection()) {
                if (mPortraitKeys.getSelection()) {
                    State state = new State();
                    state.setName("Portrait with keyboard");
                    state.setDescription("The device in portrait orientation with a keyboard open");
                    state.setOrientation(ScreenOrientation.LANDSCAPE);
                    if (mHardware.getNav().equals(Navigation.NONAV)
                            || !mPortraitKeysNav.getSelection()) {
                        state.setNavState(NavigationState.HIDDEN);
                    } else {
                        state.setNavState(NavigationState.EXPOSED);
                    }
                    state.setKeyState(KeyboardState.EXPOSED);
                    state.setHardware(mHardware);
                    if (!defaultSelected) {
                        state.setDefaultState(true);
                        defaultSelected = true;
                    }
                    builder.addState(state);
                }
                if (mLandscapeKeys.getSelection()) {
                    State state = new State();
                    state.setName("Landscape with keyboard");
                    state.setDescription("The device in landscape orientation with a keyboard open");
                    state.setOrientation(ScreenOrientation.LANDSCAPE);
                    if (mHardware.getNav().equals(Navigation.NONAV)
                            || !mLandscapeKeysNav.getSelection()) {
                        state.setNavState(NavigationState.HIDDEN);
                    } else {
                        state.setNavState(NavigationState.EXPOSED);
                    }
                    state.setKeyState(KeyboardState.EXPOSED);
                    state.setHardware(mHardware);
                    if (!defaultSelected) {
                        state.setDefaultState(true);
                        defaultSelected = true;
                    }
                    builder.addState(state);
                }
            }
            Device d = builder.build();
            if (mForceCreation.isEnabled() && mForceCreation.getSelection()) {
                mManager.replaceUserDevice(d);
            } else {
                mManager.addUserDevice(d);
            }
            mManager.saveUserDevices();
            super.okPressed();
        }
    }

}







