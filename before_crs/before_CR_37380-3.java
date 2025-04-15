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
    
    /**
     * Target selection mode for the configuration: either {@link #AUTO} or {@link #MANUAL}.
     */
public enum TargetMode {
/** Automatic target selection mode. */
        AUTO(true),
/** Manual target selection mode. */
        MANUAL(false);
        
        private boolean mValue;

        TargetMode(boolean value) {
            mValue = value;
        }
        
        public boolean getValue() {
            return mValue;
        }
        
        public static TargetMode getMode(boolean value) {
            for (TargetMode mode : values()) {
                if (mode.mValue == value) {
                    return mode;
                }
}
            
            return null;
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

        try {
            boolean value = config.getAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                    mTargetMode.getValue());
            mTargetMode = TargetMode.getMode(value);
        } catch (CoreException e) {
            // nothing to be done here, we'll use the default value
        }

try {
mAvdName = config.getAttribute(LaunchConfigDelegate.ATTR_AVD_NAME, mAvdName);
//Synthetic comment -- @@ -155,5 +149,28 @@
// nothing to be done here, we'll use the default value
}
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index c35ad2f..9d129eb 100644

//Synthetic comment -- @@ -77,9 +77,12 @@
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

/**
//Synthetic comment -- @@ -211,7 +214,7 @@

// set default target mode
wc.setAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                        LaunchConfigDelegate.DEFAULT_TARGET_MODE.getValue());

// default AVD: None
wc.setAttribute(LaunchConfigDelegate.ATTR_AVD_NAME, (String) null);
//Synthetic comment -- @@ -333,7 +336,7 @@
* - Use Last Launched Device/AVD set.
*       If user requested to use same device for future launches, and the last launched
*       device/avd is still present, then simply launch on the same device/avd.
         * - Manually Mode
*       Always display a UI that lets a user see the current running emulators/devices.
*       The UI must show which devices are compatibles, and allow launching new emulators
*       with compatible (and not yet running) AVD.
//Synthetic comment -- @@ -345,6 +348,9 @@
*           Count the number of compatible emulators/devices.
*           If != 1, display a UI similar to manual mode.
*           If == 1, launch the application on this AVD/device.
*/
IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();
IDevice deviceUsedInLastLaunch = DeviceChoiceCache.get(
//Synthetic comment -- @@ -563,6 +569,24 @@
}

AdtPlugin.printToConsole(project, message);
}

// bring up the device chooser.
//Synthetic comment -- @@ -607,6 +631,58 @@
}

/**
* Find a matching AVD.
*/
private AvdInfo findMatchingAvd(AvdManager avdManager, final IAndroidTarget projectTarget) {
//Synthetic comment -- @@ -815,15 +891,7 @@
* @return true if succeed
*/
private boolean simpleLaunch(DelayedLaunchInfo launchInfo, IDevice device) {
        // API level check
        if (checkBuildInfo(launchInfo, device) == false) {
            AdtPlugin.printErrorToConsole(launchInfo.getProject(), "Launch canceled!");
            stopLaunch(launchInfo);
            return false;
        }

        // sync the app
        if (syncApp(launchInfo, device) == false) {
AdtPlugin.printErrorToConsole(launchInfo.getProject(), "Launch canceled!");
stopLaunch(launchInfo);
return false;
//Synthetic comment -- @@ -835,6 +903,37 @@
return true;
}


/**
* If needed, syncs the application and all its dependencies on the device/emulator.
//Synthetic comment -- @@ -1154,7 +1253,7 @@
}
}
}
        if (info.getLaunchAction().doLaunchAction(info, device)) {
// if the app is not a debug app, we need to do some clean up, as
// the process is done!
if (info.isDebugMode() == false) {
//Synthetic comment -- @@ -1167,10 +1266,16 @@
// lets stop the Launch
stopLaunch(info);
}

// Monitor the logcat output on the launched device to notify
// the user if any significant error occurs that is visible from logcat
DdmsPlugin.getDefault().startLogCatMonitor(device);
}

private boolean launchEmulator(AndroidLaunchConfiguration config, AvdInfo avdToLaunch) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java
//Synthetic comment -- index 99f582c..4a58283 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -98,6 +99,14 @@

private IAndroidTarget mProjectTarget;

/**
* Returns the emulator ready speed option value.
* @param value The index of the combo selection.
//Synthetic comment -- @@ -125,11 +134,12 @@
/**
*
*/
    public EmulatorConfigTab() {
}

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
*/
@Override
public void createControl(Composite parent) {
//Synthetic comment -- @@ -165,16 +175,36 @@
targetModeGroup.setFont(font);

mManualTargetButton = new Button(targetModeGroup, SWT.RADIO);
        mManualTargetButton.setText("Manual");
        // Since there are only 2 radio buttons, we can put a listener on only one (they
        // are both called on select and unselect event.

// add the radio button
mAutoTargetButton = new Button(targetModeGroup, SWT.RADIO);
        mAutoTargetButton.setText("Automatic");
mAutoTargetButton.setSelection(true);
        mAutoTargetButton.addSelectionListener(new SelectionAdapter() {
            // called when selection changes
@Override
public void widgetSelected(SelectionEvent e) {
updateLaunchConfigurationDialog();
//Synthetic comment -- @@ -182,34 +212,43 @@
boolean auto = mAutoTargetButton.getSelection();
mPreferredAvdSelector.setEnabled(auto);
mPreferredAvdLabel.setEnabled(auto);
            }
        });

        Composite offsetComp = new Composite(targetModeGroup, SWT.NONE);
        offsetComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
layout = new GridLayout(1, false);
layout.marginRight = layout.marginHeight = 0;
layout.marginLeft = 30;
        offsetComp.setLayout(layout);

        mPreferredAvdLabel = new Label(offsetComp, SWT.NONE);
mPreferredAvdLabel.setText("Select a preferred Android Virtual Device for deployment:");

// create the selector with no manager, we'll reset the manager every time this is
// displayed to ensure we have the latest one (dialog is reused but SDK could have
// been changed in between.
        mPreferredAvdSelector = new AvdSelector(offsetComp,
Sdk.getCurrent().getSdkLocation(),
null /* avd manager */,
DisplayMode.SIMPLE_CHECK,
new AdtConsoleSdkLog());
mPreferredAvdSelector.setTableHeightHint(100);
        mPreferredAvdSelector.setSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
updateLaunchConfigurationDialog();
}
        });

// emulator size
mEmulatorOptionsGroup = new Group(topComp, SWT.NONE);
//Synthetic comment -- @@ -220,6 +259,14 @@
mEmulatorOptionsGroup.setLayout(layout);
mEmulatorOptionsGroup.setFont(font);

// network options
new Label(mEmulatorOptionsGroup, SWT.NONE).setText("Network Speed:");

//Synthetic comment -- @@ -281,7 +328,7 @@
});

// custom command line option for emulator
        Label l = new Label(mEmulatorOptionsGroup, SWT.NONE);
l.setText("Additional Emulator Command Line Options");
gd = new GridData(GridData.FILL_HORIZONTAL);
gd.horizontalSpan = 2;
//Synthetic comment -- @@ -329,15 +376,32 @@
public void initializeFrom(ILaunchConfiguration configuration) {
AvdManager avdManager = Sdk.getCurrent().getAvdManager();

        TargetMode mode = LaunchConfigDelegate.DEFAULT_TARGET_MODE; // true == automatic
        try {
            mode = TargetMode.getMode(configuration.getAttribute(
                    LaunchConfigDelegate.ATTR_TARGET_MODE, mode.getValue()));
        } catch (CoreException e) {
            // let's not do anything here, we'll use the default value
}
        mAutoTargetButton.setSelection(mode.getValue());
        mManualTargetButton.setSelection(!mode.getValue());

// look for the project name to get its target.
String stringValue = "";
//Synthetic comment -- @@ -447,7 +511,7 @@
@Override
public void performApply(ILaunchConfigurationWorkingCopy configuration) {
configuration.setAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                mAutoTargetButton.getSelection());
AvdInfo avd = mPreferredAvdSelector.getSelected();
if (avd != null) {
configuration.setAttribute(LaunchConfigDelegate.ATTR_AVD_NAME, avd.getName());
//Synthetic comment -- @@ -466,13 +530,32 @@
mNoBootAnimButton.getSelection());
}

/* (non-Javadoc)
* @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
*/
@Override
public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
configuration.setAttribute(LaunchConfigDelegate.ATTR_TARGET_MODE,
                LaunchConfigDelegate.DEFAULT_TARGET_MODE.getValue());
configuration.setAttribute(LaunchConfigDelegate.ATTR_SPEED,
LaunchConfigDelegate.DEFAULT_SPEED);
configuration.setAttribute(LaunchConfigDelegate.ATTR_DELAY,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigTabGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigTabGroup.java
//Synthetic comment -- index a68e500..fbf17ce 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
//Synthetic comment -- @@ -33,7 +34,7 @@
public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
new MainLaunchConfigTab(),
                new EmulatorConfigTab(),
new CommonTab()
};
setTabs(tabs);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitTabGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitTabGroup.java
//Synthetic comment -- index d9a44ed..13ddcc6 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
new AndroidJUnitLaunchConfigurationTab(),
                new EmulatorConfigTab(),
new CommonTab()
};
setTabs(tabs);







