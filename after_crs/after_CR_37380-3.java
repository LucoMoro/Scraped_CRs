/*Allow running on multiple devices with a single launch.

This patch provides the ability to launch an application on
all connected devices with a single launch. This applies only
to Run configurations (and not to Debug/Junit test).

UI changes:
The target tab in the launch configuration dialog provides
two options right now: Manual and Automatic. This CL adds
a third option that allows launching on all connected devices.
A drop down allows the user to further narrow down the list
to just physical devices or just emulators.

Change-Id:I721a4f41e59da24ae722b93e8ef801bc910f5442*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchConfiguration.java
//Synthetic comment -- index 2b02c59..5f0b7e3 100644

//Synthetic comment -- @@ -21,47 +21,47 @@

/**
* Launch configuration data. This stores the result of querying the
 * {@link ILaunchConfiguration} so that it's only done once.
*/
public class AndroidLaunchConfiguration {

/**
* Launch action. See {@link LaunchConfigDelegate#ACTION_DEFAULT},
* {@link LaunchConfigDelegate#ACTION_ACTIVITY},
* {@link LaunchConfigDelegate#ACTION_DO_NOTHING}
*/
public int mLaunchAction = LaunchConfigDelegate.DEFAULT_LAUNCH_ACTION;

    /** Target selection mode for the configuration. */
public enum TargetMode {
/** Automatic target selection mode. */
        AUTO,
/** Manual target selection mode. */
        MANUAL,
        /** All active devices */
        ALL_DEVICES,
        /** All active emulators */
        ALL_EMULATORS,
        /** All active devices and emulators */
        ALL_DEVICES_AND_EMULATORS;

        public static TargetMode getMode(int ordinal) {
            TargetMode modes[] = values();
            if (ordinal < modes.length) {
                return modes[ordinal];
}

            throw new IllegalArgumentException(String.format(
                    "Invalid ordinal (%d) for TargetMode", ordinal));
        }

        public boolean isMultiDevice() {
            return this == ALL_DEVICES
                    || this == ALL_EMULATORS
                    || this == ALL_DEVICES_AND_EMULATORS;
}
}

/**
* Target selection mode.
* @see TargetMode
//Synthetic comment -- @@ -77,12 +77,12 @@
* Indicates whether the emulator should be called with -no-boot-anim
*/
public boolean mNoBootAnim = LaunchConfigDelegate.DEFAULT_NO_BOOT_ANIM;

/**
* AVD Name.
*/
public String mAvdName = null;

public String mNetworkSpeed = EmulatorConfigTab.getSpeed(
LaunchConfigDelegate.DEFAULT_SPEED);
public String mNetworkDelay = EmulatorConfigTab.getDelay(
//Synthetic comment -- @@ -105,13 +105,7 @@
// nothing to be done here, we'll use the default value
}

        mTargetMode = parseTargetMode(config, mTargetMode);

try {
mAvdName = config.getAttribute(LaunchConfigDelegate.ATTR_AVD_NAME, mAvdName);
//Synthetic comment -- @@ -155,5 +149,28 @@
// nothing to be done here, we'll use the default value
}
}

    /**
     * Retrieve the {@link TargetMode} saved in the provided launch configuration.
     * Returns defaultMode if there are any errors while retrieving or parsing the saved setting.
     */
    public static TargetMode parseTargetMode(ILaunchConfiguration config, TargetMode defaultMode) {
        try {
            int value = config.getAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                    defaultMode.ordinal());
            return TargetMode.getMode(value);
        } catch (CoreException e) {
            // ADT R20 changes the attribute type of ATTR_TARGET_MODE to be an integer from a bool.
            // So if parsing as an integer fails, attempt parsing as a boolean.
            try {
                boolean value = config.getAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE, true);
                return value ? TargetMode.AUTO : TargetMode.MANUAL;
            } catch (CoreException e1) {
                return defaultMode;
            }
        } catch (IllegalArgumentException e) {
            return defaultMode;
        }
    }
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index c35ad2f..9d129eb 100644

//Synthetic comment -- @@ -77,9 +77,12 @@
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
//Synthetic comment -- @@ -211,7 +214,7 @@

// set default target mode
wc.setAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                        LaunchConfigDelegate.DEFAULT_TARGET_MODE.ordinal());

// default AVD: None
wc.setAttribute(LaunchConfigDelegate.ATTR_AVD_NAME, (String) null);
//Synthetic comment -- @@ -333,7 +336,7 @@
* - Use Last Launched Device/AVD set.
*       If user requested to use same device for future launches, and the last launched
*       device/avd is still present, then simply launch on the same device/avd.
         * - Manual Mode
*       Always display a UI that lets a user see the current running emulators/devices.
*       The UI must show which devices are compatibles, and allow launching new emulators
*       with compatible (and not yet running) AVD.
//Synthetic comment -- @@ -345,6 +348,9 @@
*           Count the number of compatible emulators/devices.
*           If != 1, display a UI similar to manual mode.
*           If == 1, launch the application on this AVD/device.
         * - Launch on multiple devices:
         *     From the currently active devices & emulators, filter out those that cannot run
         *     the app (by api level), and launch on all the others.
*/
IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();
IDevice deviceUsedInLastLaunch = DeviceChoiceCache.get(
//Synthetic comment -- @@ -563,6 +569,24 @@
}

AdtPlugin.printToConsole(project, message);
        } else if ((config.mTargetMode == TargetMode.ALL_DEVICES_AND_EMULATORS
                || config.mTargetMode == TargetMode.ALL_DEVICES
                || config.mTargetMode == TargetMode.ALL_EMULATORS)
                && ILaunchManager.RUN_MODE.equals(mode)) {
            // if running on multiple devices, identify all compatible devices
            boolean includeDevices = config.mTargetMode != TargetMode.ALL_EMULATORS;
            boolean includeAvds = config.mTargetMode != TargetMode.ALL_DEVICES;
            Collection<IDevice> compatibleDevices = findCompatibleDevices(devices,
                    requiredApiVersionNumber, includeDevices, includeAvds);
            if (compatibleDevices.size() == 0) {
                AdtPlugin.printErrorToConsole(project,
                      "No active compatible AVD's or devices found. "
                    + "Relaunch this configuration after connecting a device or starting an AVD.");
                stopLaunch(launchInfo);
            } else {
                multiLaunch(launchInfo, compatibleDevices);
            }
            return;
}

// bring up the device chooser.
//Synthetic comment -- @@ -607,6 +631,58 @@
}

/**
     * Returns devices that can run a app of provided API level.
     * @param devices list of devices to filter from
     * @param requiredApiVersionNumber minimum required API level that should be supported
     * @param includeDevices include physical devices in the filtered list
     * @param includeAvds include emulators in the filtered list
     * @return set of compatible devices, may be an empty set
     */
    private Collection<IDevice> findCompatibleDevices(IDevice[] devices,
            String requiredApiVersionNumber, boolean includeDevices, boolean includeAvds) {
        Set<IDevice> compatibleDevices = new HashSet<IDevice>(devices.length);
        int minApi;
        try {
            minApi = Integer.parseInt(requiredApiVersionNumber);
        } catch (NumberFormatException e) {
            minApi = 1;
        }
        AndroidVersion requiredVersion = new AndroidVersion(minApi, null);

        AvdManager avdManager = Sdk.getCurrent().getAvdManager();
        for (IDevice d: devices) {
            boolean isEmulator = d.isEmulator();
            boolean canRun = false;

            if (isEmulator) {
                if (!includeAvds) {
                    continue;
                }

                AvdInfo avdInfo = avdManager.getAvd(d.getAvdName(), true);
                if (avdInfo != null && avdInfo.getTarget() != null) {
                    canRun = avdInfo.getTarget().getVersion().canRun(requiredVersion);
                }
            } else {
                if (!includeDevices) {
                    continue;
                }

                AndroidVersion deviceVersion = Sdk.getDeviceVersion(d);
                if (deviceVersion != null) {
                    canRun = deviceVersion.canRun(requiredVersion);
                }
            }

            if (canRun) {
                compatibleDevices.add(d);
            }
        }

        return compatibleDevices;
    }

    /**
* Find a matching AVD.
*/
private AvdInfo findMatchingAvd(AvdManager avdManager, final IAndroidTarget projectTarget) {
//Synthetic comment -- @@ -815,15 +891,7 @@
* @return true if succeed
*/
private boolean simpleLaunch(DelayedLaunchInfo launchInfo, IDevice device) {
        if (!doPreLaunchActions(launchInfo, device)) {
AdtPlugin.printErrorToConsole(launchInfo.getProject(), "Launch canceled!");
stopLaunch(launchInfo);
return false;
//Synthetic comment -- @@ -835,6 +903,37 @@
return true;
}

    private boolean doPreLaunchActions(DelayedLaunchInfo launchInfo, IDevice device) {
        // API level check
        if (!checkBuildInfo(launchInfo, device)) {
            return false;
        }

        // sync app
        if (!syncApp(launchInfo, device)) {
            return false;
        }

        return true;
    }

    private void multiLaunch(DelayedLaunchInfo launchInfo, Collection<IDevice> devices) {
        for (IDevice d: devices) {
            boolean success = doPreLaunchActions(launchInfo, d);
            if (!success) {
                String deviceName = d.isEmulator() ? d.getAvdName() : d.getSerialNumber();
                AdtPlugin.printErrorToConsole(launchInfo.getProject(),
                        "Launch failed on device: " + deviceName);
                continue;
            }

            doLaunchAction(launchInfo, d);
        }

        // multiple launches are only supported for run configuration, so we can terminate
        // the launch itself
        stopLaunch(launchInfo);
    }

/**
* If needed, syncs the application and all its dependencies on the device/emulator.
//Synthetic comment -- @@ -1154,7 +1253,7 @@
}
}
}
        if (doLaunchAction(info, device)) {
// if the app is not a debug app, we need to do some clean up, as
// the process is done!
if (info.isDebugMode() == false) {
//Synthetic comment -- @@ -1167,10 +1266,16 @@
// lets stop the Launch
stopLaunch(info);
}
    }

    private boolean doLaunchAction(final DelayedLaunchInfo info, IDevice device) {
        boolean result = info.getLaunchAction().doLaunchAction(info, device);

// Monitor the logcat output on the launched device to notify
// the user if any significant error occurs that is visible from logcat
DdmsPlugin.getDefault().startLogCatMonitor(device);

        return result;
}

private boolean launchEmulator(AndroidLaunchConfiguration config, AvdInfo avdToLaunch) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java
//Synthetic comment -- index 99f582c..4a58283 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -98,6 +99,14 @@

private IAndroidTarget mProjectTarget;

    private boolean mSupportMultiDeviceLaunch;
    private Button mAllDevicesTargetButton;
    private Combo mDeviceTypeCombo;

    private static final String DEVICES_AND_EMULATORS = "Active devices and AVD's";
    private static final String EMULATORS_ONLY = "Active AVD's";
    private static final String DEVICES_ONLY = "Active devices";

/**
* Returns the emulator ready speed option value.
* @param value The index of the combo selection.
//Synthetic comment -- @@ -125,11 +134,12 @@
/**
*
*/
    public EmulatorConfigTab(boolean supportMultiDeviceLaunch) {
        mSupportMultiDeviceLaunch = supportMultiDeviceLaunch;
}

    /**
     * @wbp.parser.entryPoint
*/
@Override
public void createControl(Composite parent) {
//Synthetic comment -- @@ -165,16 +175,36 @@
targetModeGroup.setFont(font);

mManualTargetButton = new Button(targetModeGroup, SWT.RADIO);
        mManualTargetButton.setText("Always prompt to pick device");

        mAllDevicesTargetButton = new Button(targetModeGroup, SWT.RADIO);
        mAllDevicesTargetButton.setText("Launch on all compatible devices/AVD's");
        mAllDevicesTargetButton.setEnabled(mSupportMultiDeviceLaunch);

        Composite deviceTypeOffsetComp = new Composite(targetModeGroup, SWT.NONE);
        deviceTypeOffsetComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        layout = new GridLayout(1, false);
        layout.marginRight = layout.marginHeight = 0;
        layout.marginLeft = 30;
        deviceTypeOffsetComp.setLayout(layout);

        mDeviceTypeCombo = new Combo(deviceTypeOffsetComp, SWT.READ_ONLY);
        mDeviceTypeCombo.setItems(new String[] {
                DEVICES_AND_EMULATORS,
                EMULATORS_ONLY,
                DEVICES_ONLY,
        });
        mDeviceTypeCombo.select(0);
        mDeviceTypeCombo.setEnabled(false);

// add the radio button
mAutoTargetButton = new Button(targetModeGroup, SWT.RADIO);
        mAutoTargetButton.setText("Automatically pick compatible device: "
                + "Always uses preferred AVD if set below, "
                + "launches on compatible device/AVD otherwise.");
mAutoTargetButton.setSelection(true);

        SelectionListener targetModeChangeListener = new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
updateLaunchConfigurationDialog();
//Synthetic comment -- @@ -182,34 +212,43 @@
boolean auto = mAutoTargetButton.getSelection();
mPreferredAvdSelector.setEnabled(auto);
mPreferredAvdLabel.setEnabled(auto);

                boolean all = mAllDevicesTargetButton.getSelection();
                mDeviceTypeCombo.setEnabled(all);
            }
        };

        mAutoTargetButton.addSelectionListener(targetModeChangeListener);
        mAllDevicesTargetButton.addSelectionListener(targetModeChangeListener);
        mManualTargetButton.addSelectionListener(targetModeChangeListener);

        Composite avdOffsetComp = new Composite(targetModeGroup, SWT.NONE);
        avdOffsetComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
layout = new GridLayout(1, false);
layout.marginRight = layout.marginHeight = 0;
layout.marginLeft = 30;
        avdOffsetComp.setLayout(layout);

        mPreferredAvdLabel = new Label(avdOffsetComp, SWT.NONE);
mPreferredAvdLabel.setText("Select a preferred Android Virtual Device for deployment:");

// create the selector with no manager, we'll reset the manager every time this is
// displayed to ensure we have the latest one (dialog is reused but SDK could have
// been changed in between.
        mPreferredAvdSelector = new AvdSelector(avdOffsetComp,
Sdk.getCurrent().getSdkLocation(),
null /* avd manager */,
DisplayMode.SIMPLE_CHECK,
new AdtConsoleSdkLog());
mPreferredAvdSelector.setTableHeightHint(100);
        SelectionListener listener = new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
updateLaunchConfigurationDialog();
}
        };
        mPreferredAvdSelector.setSelectionListener(listener);
        mDeviceTypeCombo.addSelectionListener(listener);

// emulator size
mEmulatorOptionsGroup = new Group(topComp, SWT.NONE);
//Synthetic comment -- @@ -220,6 +259,14 @@
mEmulatorOptionsGroup.setLayout(layout);
mEmulatorOptionsGroup.setFont(font);

        // Explanation
        Label l = new Label(mEmulatorOptionsGroup, SWT.NONE);
        l.setText("If no compatible and active devices or AVD's are found, then an AVD "
                 + "might be launched. Provide options for the AVD launch below.");
        gd = new GridData();
        gd.horizontalSpan = 2;
        l.setLayoutData(gd);

// network options
new Label(mEmulatorOptionsGroup, SWT.NONE).setText("Network Speed:");

//Synthetic comment -- @@ -281,7 +328,7 @@
});

// custom command line option for emulator
        l = new Label(mEmulatorOptionsGroup, SWT.NONE);
l.setText("Additional Emulator Command Line Options");
gd = new GridData(GridData.FILL_HORIZONTAL);
gd.horizontalSpan = 2;
//Synthetic comment -- @@ -329,15 +376,32 @@
public void initializeFrom(ILaunchConfiguration configuration) {
AvdManager avdManager = Sdk.getCurrent().getAvdManager();

        TargetMode mode = AndroidLaunchConfiguration.parseTargetMode(configuration,
                LaunchConfigDelegate.DEFAULT_TARGET_MODE);

        boolean multipleDevices = mode.isMultiDevice();
        if (multipleDevices && !mSupportMultiDeviceLaunch) {
            // The launch config says to run on multiple devices, but this launch type does not
            // suppport multiple devices. In such a case, switch back to default mode.
            // This could happen if a launch config used for Run is then used for Debug.
            multipleDevices = false;
            mode = LaunchConfigDelegate.DEFAULT_TARGET_MODE;
}

        mAutoTargetButton.setSelection(mode == TargetMode.AUTO);
        mManualTargetButton.setSelection(mode == TargetMode.MANUAL);
        mAllDevicesTargetButton.setSelection(multipleDevices);

        mDeviceTypeCombo.setEnabled(multipleDevices);
        if (multipleDevices) {
            int index = 0;
            if (mode == TargetMode.ALL_EMULATORS) {
                index = 1;
            } else if (mode == TargetMode.ALL_DEVICES) {
                index = 2;
            }
            mDeviceTypeCombo.select(index);
        }

// look for the project name to get its target.
String stringValue = "";
//Synthetic comment -- @@ -447,7 +511,7 @@
@Override
public void performApply(ILaunchConfigurationWorkingCopy configuration) {
configuration.setAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                getCurrentTargetMode().ordinal());
AvdInfo avd = mPreferredAvdSelector.getSelected();
if (avd != null) {
configuration.setAttribute(LaunchConfigDelegate.ATTR_AVD_NAME, avd.getName());
//Synthetic comment -- @@ -466,13 +530,32 @@
mNoBootAnimButton.getSelection());
}

    private TargetMode getCurrentTargetMode() {
        if (mAutoTargetButton.getSelection()) {
            return TargetMode.AUTO;
        } else if (mManualTargetButton.getSelection()) {
            return TargetMode.MANUAL;
        } else {
            String selection = mDeviceTypeCombo.getText();
            if (DEVICES_AND_EMULATORS.equals(selection)) {
                return TargetMode.ALL_DEVICES_AND_EMULATORS;
            } else if (DEVICES_ONLY.equals(selection)) {
                return TargetMode.ALL_DEVICES;
            } else if (EMULATORS_ONLY.equals(selection)) {
                return TargetMode.ALL_EMULATORS;
            }
        }

        return TargetMode.AUTO;
    }

/* (non-Javadoc)
* @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
*/
@Override
public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
configuration.setAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                LaunchConfigDelegate.DEFAULT_TARGET_MODE.ordinal());
configuration.setAttribute(LaunchConfigDelegate.ATTR_SPEED,
LaunchConfigDelegate.DEFAULT_SPEED);
configuration.setAttribute(LaunchConfigDelegate.ATTR_DELAY,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigTabGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigTabGroup.java
//Synthetic comment -- index a68e500..fbf17ce 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.launch;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
//Synthetic comment -- @@ -33,7 +34,7 @@
public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
new MainLaunchConfigTab(),
                new EmulatorConfigTab(ILaunchManager.RUN_MODE.equals(mode)),
new CommonTab()
};
setTabs(tabs);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitTabGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitTabGroup.java
//Synthetic comment -- index d9a44ed..13ddcc6 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
new AndroidJUnitLaunchConfigurationTab(),
                new EmulatorConfigTab(false),
new CommonTab()
};
setTabs(tabs);







