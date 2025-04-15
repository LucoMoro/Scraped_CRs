/*ndk: Show DeviceChooserDialog only when necessary.

The ADT launcher has a cache of what device was used for a particular
launch, and reuses that device if the user has specified that the same
device should be used for future launches.

This patch moves that cache out to a separate class and reuses the
same cache for the NDK launcher as well.

Change-Id:I8aa266746e33dac82c9f16ca8877230e26c45c09*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 5da67bb..c35ad2f 100644

//Synthetic comment -- @@ -79,7 +79,6 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

//Synthetic comment -- @@ -137,9 +136,6 @@
*/
private final ArrayList<Client> mUnknownClientsWaitingForDebugger = new ArrayList<Client>();

/** static instance for singleton */
private static AndroidLaunchController sThis = new AndroidLaunchController();

//Synthetic comment -- @@ -351,7 +347,7 @@
*           If == 1, launch the application on this AVD/device.
*/
IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();
        IDevice deviceUsedInLastLaunch = DeviceChoiceCache.get(
launch.getLaunchConfiguration().getName());
if (deviceUsedInLastLaunch != null) {
response.setDeviceToUse(deviceUsedInLastLaunch);
//Synthetic comment -- @@ -582,8 +578,7 @@
AdtPlugin.getDisplay().getActiveShell(),
response, launchInfo.getPackageName(), desiredProjectTarget);
if (dialog.open() == Dialog.OK) {
                        DeviceChoiceCache.put(launch.getLaunchConfiguration().getName(), response);
continueLaunch.set(true);
} else {
AdtPlugin.printErrorToConsole(project, "Launch canceled!");
//Synthetic comment -- @@ -611,40 +606,6 @@
}
}

/**
* Find a matching AVD.
*/
//Synthetic comment -- @@ -1451,7 +1412,6 @@
* @see IDeviceChangeListener#deviceDisconnected(IDevice)
*/
@Override
public void deviceDisconnected(IDevice device) {
// any pending launch on this device must be canceled.
String message = "%1$s disconnected! Cancelling '%2$s'!";








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChoiceCache.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChoiceCache.java
new file mode 100644
//Synthetic comment -- index 0000000..e726501

//Synthetic comment -- @@ -0,0 +1,76 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.launch;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.internal.launch.DeviceChooserDialog.DeviceChooserResponse;
import com.android.sdklib.internal.avd.AvdInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link DeviceChoiceCache} maps a launch configuration name to the device selected for use
 * in that launch configuration by the {@link DeviceChooserDialog}.
 */
public class DeviceChoiceCache {
    private static final Map<String, String> sDeviceUsedForLaunch = new HashMap<String, String>();

    public static IDevice get(String launchConfigName) {
        // obtain the cached entry
        String deviceName = sDeviceUsedForLaunch.get(launchConfigName);
        if (deviceName == null) {
            return null;
        }

        // verify that the device is still online
        for (IDevice device : getOnlineDevices()) {
            if (deviceName.equals(device.getAvdName()) ||
                    deviceName.equals(device.getSerialNumber())) {
                return device;
            }
        }

        return null;
    }

    public static void put(String launchConfigName, DeviceChooserResponse response) {
        if (!response.useDeviceForFutureLaunches()) {
            return;
        }

        AvdInfo avd = response.getAvdToLaunch();
        String device = null;
        if (avd != null) {
            device = avd.getName();
        } else {
            device = response.getDeviceToUse().getSerialNumber();
        }

        sDeviceUsedForLaunch.put(launchConfigName, device);
    }

    private static IDevice[] getOnlineDevices() {
        AndroidDebugBridge bridge = AndroidDebugBridge.getBridge();
        if (bridge != null) {
            return bridge.getDevices();
        } else {
            return new IDevice[0];
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java
//Synthetic comment -- index 43bb852..1c463ab 100644

//Synthetic comment -- @@ -20,12 +20,14 @@
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceUnixSocketNamespace;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.launch.DeviceChoiceCache;
import com.android.ide.eclipse.adt.internal.launch.DeviceChooserDialog;
import com.android.ide.eclipse.adt.internal.launch.DeviceChooserDialog.DeviceChooserResponse;
import com.android.ide.eclipse.adt.internal.launch.LaunchConfigDelegate;
//Synthetic comment -- @@ -134,31 +136,44 @@
return false;
}

        // Obtain device to use:
        //  - if there is only 1 device, just use that
        //  - if we have previously launched this config, and the device used is present, use that
        //  - otherwise show the DeviceChooserDialog
        final String configName = config.getName();
monitor.setTaskName(Messages.NdkGdbLaunchDelegate_Action_ObtainDevice);
        IDevice device = null;
        IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();
        if (devices.length == 1) {
            device = devices[0];
        } else if (DeviceChoiceCache.get(configName) != null) {
            device = DeviceChoiceCache.get(configName);
        } else {
            final IAndroidTarget projectTarget = Sdk.getCurrent().getTarget(project);
            final DeviceChooserResponse response = new DeviceChooserResponse();
            final boolean continueLaunch[] = new boolean[] { false };
            AdtPlugin.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    DeviceChooserDialog dialog = new DeviceChooserDialog(
                            AdtPlugin.getDisplay().getActiveShell(),
                            response,
                            manifestData.getPackage(),
                            projectTarget);
                    if (dialog.open() == Dialog.OK) {
                        DeviceChoiceCache.put(configName, response);
                        continueLaunch[0] = true;
                    }
                };
            });

            if (!continueLaunch[0]) {
                return false;
            }

            device = response.getDeviceToUse();
}

// ndk-gdb requires device > Froyo
monitor.setTaskName(Messages.NdkGdbLaunchDelegate_Action_CheckAndroidDeviceVersion);
AndroidVersion deviceVersion = Sdk.getDeviceVersion(device);







