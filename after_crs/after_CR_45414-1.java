/*Clean up new avd dialog, unify with layout manager device menu

(cherry picked from commit c3fa0391879acd10e613e3aa8d725f14f67fc149)

Change-Id:Ida667b43c7ea9398553a8a759059bb94ccffc27e*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index 2f1193b..ad0ee6f 100755

//Synthetic comment -- @@ -525,14 +525,17 @@
sb.append(name);
int pos1 = sb.length();

            String manufacturer = device.getManufacturer();
            String manu = manufacturer;
if (isUser) {
item.setImage(mUserImage);
} else if (GENERIC.equals(manu)) {
item.setImage(mGenericImage);
} else {
item.setImage(mOtherImage);
                if (!manufacturer.contains(NEXUS)) {
                    sb.append("  by ").append(manufacturer);
                }
}

Hardware hw = device.getDefaultHardware();
//Synthetic comment -- @@ -723,7 +726,7 @@
mImageFactory,
mUpdaterData.getSdkLog(),
null);
        dlg.selectInitialDevice(ci.mDevice);

if (dlg.open() == Window.OK) {
onRefresh();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index b97881e..79adba0 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.internal.widgets;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Density;
import com.android.resources.ScreenSize;
//Synthetic comment -- @@ -29,6 +30,7 @@
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.devices.Software;
import com.android.sdklib.devices.Storage;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -61,9 +63,13 @@

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvdCreationDialog extends GridDialog {

//Synthetic comment -- @@ -73,8 +79,6 @@
private AvdInfo mAvdInfo;
private boolean mHaveSystemImage;

private final TreeMap<String, IAndroidTarget> mCurrentTargets =
new TreeMap<String, IAndroidTarget>();

//Synthetic comment -- @@ -82,8 +86,7 @@

private Text mAvdName;

    private Combo mDevice;

private Combo mTarget;
private Combo mAbi;
//Synthetic comment -- @@ -148,23 +151,6 @@
mImageFactory = imageFactory;
mSdkLog = log;
mAvdInfo = editAvdInfo;
}

/** Returns the AVD Created, if successful. */
//Synthetic comment -- @@ -208,27 +194,12 @@

// --- device selection
label = new Label(parent, SWT.NONE);
        label.setText("Device:");
        tooltip = "The device this AVD will be based on";
        mDevice = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
        mDevice.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        initializeDevices();
        mDevice.addSelectionListener(new DeviceSelectionListener());

// --- api target
label = new Label(parent, SWT.NONE);
//Synthetic comment -- @@ -411,7 +382,7 @@
mSnapshot.setToolTipText("Emulator's state will be persisted between emulator executions");
mSnapshot.addSelectionListener(validateListener);
mGpuEmulation = new Button(optionsGroup, SWT.CHECK);
        mGpuEmulation.setText("Use Host GPU");
mGpuEmulation.setToolTipText("Enable hardware OpenGLES emulation");
mGpuEmulation.addSelectionListener(validateListener);

//Synthetic comment -- @@ -445,12 +416,98 @@
mStatusLabel.setText(""); //$NON-NLS-1$
}

    @Nullable
    private Device getSelectedDevice() {
        Device[] devices = (Device[]) mDevice.getData();
        if (devices != null) {
            int index = mDevice.getSelectionIndex();
            if (index != -1 && index < devices.length) {
                return devices[index];
            }
        }

        return null;
    }

    private void selectDevice(String manufacturer, String name) {
        Device[] devices = (Device[]) mDevice.getData();
        if (devices != null) {
            for (int i = 0, n = devices.length; i < n; i++) {
                Device device = devices[i];
                if (device.getManufacturer().equals(manufacturer)
                        && device.getName().equals(name)) {
                    mDevice.select(i);
                    break;
                }
            }
        }
    }

    private void selectDevice(Device device) {
        Device[] devices = (Device[]) mDevice.getData();
        if (devices != null) {
            for (int i = 0, n = devices.length; i < n; i++) {
                if (devices[i].equals(device)) {
                    mDevice.select(i);
                    break;
                }
            }
        }
    }

    private void initializeDevices() {
        assert mDevice != null;

        SdkManager sdkManager = mAvdManager.getSdkManager();
        String location = sdkManager.getLocation();
        if (sdkManager != null && location != null) {
            DeviceManager deviceManager = new DeviceManager(mSdkLog);
            List<Device> deviceList = new ArrayList<Device>(deviceManager.getDevices(location));

            // Sort
            List<Device> nexus = new ArrayList<Device>(deviceList.size());
            List<Device> other = new ArrayList<Device>(deviceList.size());
            for (Device device : deviceList) {
                if (isNexus(device)) {
                    if (!isGeneric(device)) { // Filter out repeated definitions
                        nexus.add(device);
                    }
                } else {
                    other.add(device);
                }
            }
            Collections.reverse(other);
            Collections.sort(nexus, new Comparator<Device>() {
                @Override
                public int compare(Device device1, Device device2) {
                    // Descending order of age
                    return nexusRank(device2) - nexusRank(device1);
                }
            });
            List<Device> all = nexus;
            all.addAll(other);

            Device[] devices = all.toArray(new Device[all.size()]);
            String[] labels = new String[devices.length];
            for (int i = 0, n = devices.length; i < n; i++) {
                Device device = devices[i];
                if (isNexus(device)) {
                    labels[i] = getNexusLabel(device);
                } else {
                    labels[i] = getGenericLabel(device);
                }
            }
            mDevice.setData(devices);
            mDevice.setItems(labels);
        }
    }

/**
* Can be called after the constructor to set the default device for this AVD.
* Useful especially for new AVDs.
* @param device
*/
    public void selectInitialDevice(Device device) {
mInitWithDevice = device;
}

//Synthetic comment -- @@ -517,14 +574,7 @@

@Override
public void widgetSelected(SelectionEvent arg0) {
            Device currentDevice = getSelectedDevice();
if (currentDevice != null) {
fillDeviceProperties(currentDevice);
}
//Synthetic comment -- @@ -532,7 +582,6 @@
toggleCameras();
validatePage();
}
}

private void fillDeviceProperties(Device device) {
//Synthetic comment -- @@ -577,39 +626,56 @@
}
}
mVmHeap.setText(Integer.toString(vmHeapSize));

        List<Software> allSoftware = device.getAllSoftware();
        if (allSoftware != null && !allSoftware.isEmpty()) {
            Software first = allSoftware.get(0);
            int min = first.getMinSdkLevel();;
            int max = first.getMaxSdkLevel();;
            for (int i = 1; i < allSoftware.size(); i++) {
                min = Math.min(min, first.getMinSdkLevel());
                max = Math.max(max, first.getMaxSdkLevel());
            }
            if (mCurrentTargets != null) {
                int bestApiLevel = Integer.MAX_VALUE;
                IAndroidTarget bestTarget = null;
                for (IAndroidTarget target : mCurrentTargets.values()) {
                    if (!target.isPlatform()) {
                        continue;
                    }
                    int apiLevel = target.getVersion().getApiLevel();
                    if (apiLevel >= min && apiLevel <= max) {
                        if (bestTarget == null || apiLevel < bestApiLevel) {
                            bestTarget = target;
                            bestApiLevel = apiLevel;
                        }
                    }
                }

                if (bestTarget != null) {
                    selectTarget(bestTarget);
                    reloadAbiTypeCombo();
                }
            }
        }
}

private void toggleCameras() {
mFrontCamera.setEnabled(false);
mBackCamera.setEnabled(false);
        Device d = getSelectedDevice();
        if (d != null) {
            for (Camera c : d.getDefaultHardware().getCameras()) {
                if (CameraLocation.FRONT.equals(c.getLocation())) {
                    mFrontCamera.setEnabled(true);
                }
                if (CameraLocation.BACK.equals(c.getLocation())) {
                    mBackCamera.setEnabled(true);
}
}
}
}

private void reloadTargetCombo() {
String selected = null;
int index = mTarget.getSelectionIndex();
//Synthetic comment -- @@ -623,6 +689,7 @@
boolean found = false;
index = -1;

        List<IAndroidTarget> targetData = new ArrayList<IAndroidTarget>();
SdkManager sdkManager = mAvdManager.getSdkManager();
if (sdkManager != null) {
for (IAndroidTarget target : sdkManager.getTargets()) {
//Synthetic comment -- @@ -639,6 +706,7 @@
}
mCurrentTargets.put(name, target);
mTarget.add(name);
                targetData.add(target);
if (!found) {
index++;
found = name.equals(selected);
//Synthetic comment -- @@ -647,12 +715,35 @@
}

mTarget.setEnabled(mCurrentTargets.size() > 0);
        mTarget.setData(targetData.toArray(new IAndroidTarget[targetData.size()]));

if (found) {
mTarget.select(index);
}
}

    private void selectTarget(IAndroidTarget target) {
        IAndroidTarget[] targets = (IAndroidTarget[]) mTarget.getData();
        if (targets != null) {
            for (int i = 0; i < targets.length; i++) {
                if (target == targets[i]) {
                    mTarget.select(i);
                    break;
                }
            }
        }
    }

    private IAndroidTarget getSelectedTarget() {
        IAndroidTarget[] targets = (IAndroidTarget[]) mTarget.getData();
        int index = mTarget.getSelectionIndex();
        if (targets != null && index != -1 && index < targets.length) {
            return targets[index];
        }

        return null;
    }

/**
* Reload all the abi types in the selection list
*/
//Synthetic comment -- @@ -752,7 +843,7 @@
return;
}

        if (mDevice.getSelectionIndex() < 0) {
setPageValid(false, error, warning);
return;
}
//Synthetic comment -- @@ -907,19 +998,7 @@
}

// Get the device
        Device device = getSelectedDevice();
if (device == null) {
return false;
}
//Synthetic comment -- @@ -1012,22 +1091,7 @@

private void fillExistingAvdInfo(AvdInfo avd) {
mAvdName.setText(avd.getName());
        selectDevice(avd.getDeviceManufacturer(), avd.getDeviceName());
toggleCameras();

IAndroidTarget target = avd.getTarget();
//Synthetic comment -- @@ -1172,24 +1236,8 @@
name = name.replaceAll("[^0-9a-zA-Z_-]+", " ").trim().replaceAll("[ _]+", "_");
mAvdName.setText(name);

// Select the device
        selectDevice(device);
toggleCameras();

// If there's only one target, select it by default.
//Synthetic comment -- @@ -1229,4 +1277,86 @@

return new ISystemImage[0];
}

    // Code copied from DeviceMenuListener in ADT; unify post release

    private static final String NEXUS = "Nexus";       //$NON-NLS-1$
    private static final String GENERIC = "Generic";   //$NON-NLS-1$
    private static Pattern PATTERN = Pattern.compile(
            "(\\d+\\.?\\d*)in (.+?)( \\(.*Nexus.*\\))?"); //$NON-NLS-1$

    private static int nexusRank(Device device) {
        String name = device.getName();
        if (name.endsWith(" One")) {     //$NON-NLS-1$
            return 1;
        }
        if (name.endsWith(" S")) {       //$NON-NLS-1$
            return 2;
        }
        if (name.startsWith("Galaxy")) { //$NON-NLS-1$
            return 3;
        }
        if (name.endsWith(" 7")) {       //$NON-NLS-1$
            return 4;
        }

        return 5;
    }

    private static boolean isGeneric(Device device) {
        return device.getManufacturer().equals(GENERIC);
    }

    private static boolean isNexus(Device device) {
        return device.getName().contains(NEXUS);
    }

    private static String getGenericLabel(Device d) {
        // * Replace "'in'" with '"' (e.g. 2.7" QVGA instead of 2.7in QVGA)
        // * Use the same precision for all devices (all but one specify decimals)
        // * Add some leading space such that the dot ends up roughly in the
        //   same space
        // * Add in screen resolution and density
        String name = d.getName();
        if (name.equals("3.7 FWVGA slider")) {                        //$NON-NLS-1$
            // Fix metadata: this one entry doesn't have "in" like the rest of them
            name = "3.7in FWVGA slider";                              //$NON-NLS-1$
        }

        Matcher matcher = PATTERN.matcher(name);
        if (matcher.matches()) {
            String size = matcher.group(1);
            String n = matcher.group(2);
            int dot = size.indexOf('.');
            if (dot == -1) {
                size = size + ".0";
                dot = size.length() - 2;
            }
            for (int i = 0; i < 2 - dot; i++) {
                size = ' ' + size;
            }
            name = size + "\" " + n;
        }

        return String.format(java.util.Locale.US, "%1$s (%2$s)", name,
                getResolutionString(d));
    }

    private static String getNexusLabel(Device d) {
        String name = d.getName();
        Screen screen = d.getDefaultHardware().getScreen();
        float length = (float) screen.getDiagonalLength();
        return String.format(java.util.Locale.US, "%1$s (%3$s\", %2$s)",
                name, getResolutionString(d), Float.toString(length));
    }

    @Nullable
    private static String getResolutionString(Device device) {
        Screen screen = device.getDefaultHardware().getScreen();
        return String.format(java.util.Locale.US,
                "%1$d \u00D7 %2$d: %3$s", // U+00D7: Unicode multiplication sign
                screen.getXDimension(),
                screen.getYDimension(),
                screen.getPixelDensity().getResourceValue());
    }
}







