/*Clean up new avd dialog, unify with layout manager device menu

Change-Id:Iaa6cf80bfb0b6c3d3d150f6c6f853ec420315db2*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index 2f1193b..ad0ee6f 100755

//Synthetic comment -- @@ -525,14 +525,17 @@
sb.append(name);
int pos1 = sb.length();

            String manu = device.getManufacturer();
if (isUser) {
item.setImage(mUserImage);
} else if (GENERIC.equals(manu)) {
item.setImage(mGenericImage);
} else {
item.setImage(mOtherImage);
                sb.append("  by ").append(device.getManufacturer());
}

Hardware hw = device.getDefaultHardware();
//Synthetic comment -- @@ -723,7 +726,7 @@
mImageFactory,
mUpdaterData.getSdkLog(),
null);
        dlg.setlectInitialDevice(ci.mDevice);

if (dlg.open() == Window.OK) {
onRefresh();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index b97881e..79adba0 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.internal.widgets;

import com.android.SdkConstants;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Density;
import com.android.resources.ScreenSize;
//Synthetic comment -- @@ -29,6 +30,7 @@
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.devices.Storage;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -61,9 +63,13 @@

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AvdCreationDialog extends GridDialog {

//Synthetic comment -- @@ -73,8 +79,6 @@
private AvdInfo mAvdInfo;
private boolean mHaveSystemImage;

    // A map from manufacturers to their list of devices.
    private Map<String, List<Device>> mDeviceMap;
private final TreeMap<String, IAndroidTarget> mCurrentTargets =
new TreeMap<String, IAndroidTarget>();

//Synthetic comment -- @@ -82,8 +86,7 @@

private Text mAvdName;

    private Combo mDeviceManufacturer;
    private Combo mDeviceName;

private Combo mTarget;
private Combo mAbi;
//Synthetic comment -- @@ -148,23 +151,6 @@
mImageFactory = imageFactory;
mSdkLog = log;
mAvdInfo = editAvdInfo;

        mDeviceMap = new TreeMap<String, List<Device>>();

        SdkManager sdkMan = avdManager.getSdkManager();
        if (sdkMan != null && sdkMan.getLocation() != null) {
            List<Device> devices = (new DeviceManager(log)).getDevices(sdkMan.getLocation());
            for (Device d : devices) {
                List<Device> list;
                if (mDeviceMap.containsKey(d.getManufacturer())) {
                    list = mDeviceMap.get(d.getManufacturer());
                } else {
                    list = new ArrayList<Device>();
                    mDeviceMap.put(d.getManufacturer(), list);
                }
                list.add(d);
            }
        }
}

/** Returns the AVD Created, if successful. */
//Synthetic comment -- @@ -208,27 +194,12 @@

// --- device selection
label = new Label(parent, SWT.NONE);
        label.setText("Device\nManufacturer:");
        tooltip = "The manufacturer of the device this AVD will be based on";
        mDeviceManufacturer = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
        for (String manufacturer : mDeviceMap.keySet()) {
            mDeviceManufacturer.add(manufacturer);
        }
        mDeviceManufacturer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mDeviceManufacturer.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                reloadDeviceNameCombo();
                validatePage();
            }
        });

        label = new Label(parent, SWT.NONE);
        label.setText("Device Name:");
        tooltip = "The name of the device this AVD will be based on";
        mDeviceName = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
        mDeviceName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mDeviceName.addSelectionListener(new DeviceSelectionListener());

// --- api target
label = new Label(parent, SWT.NONE);
//Synthetic comment -- @@ -411,7 +382,7 @@
mSnapshot.setToolTipText("Emulator's state will be persisted between emulator executions");
mSnapshot.addSelectionListener(validateListener);
mGpuEmulation = new Button(optionsGroup, SWT.CHECK);
        mGpuEmulation.setText("GPU Emulation");
mGpuEmulation.setToolTipText("Enable hardware OpenGLES emulation");
mGpuEmulation.addSelectionListener(validateListener);

//Synthetic comment -- @@ -445,12 +416,98 @@
mStatusLabel.setText(""); //$NON-NLS-1$
}

/**
* Can be called after the constructor to set the default device for this AVD.
* Useful especially for new AVDs.
* @param device
*/
    public void setlectInitialDevice(Device device) {
mInitWithDevice = device;
}

//Synthetic comment -- @@ -517,14 +574,7 @@

@Override
public void widgetSelected(SelectionEvent arg0) {
            Device currentDevice = null;
            for (Device d : mDeviceMap.get(mDeviceManufacturer.getText())) {
                if (d.getName().equals(mDeviceName.getText())) {
                    currentDevice = d;
                    break;
                }
            }

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
}

private void toggleCameras() {
mFrontCamera.setEnabled(false);
mBackCamera.setEnabled(false);
        if (mDeviceName.getSelectionIndex() >= 0) {
            List<Device> devices = mDeviceMap.get(mDeviceManufacturer.getText());
            for (Device d : devices) {
                if (mDeviceName.getText().equals(d.getName())) {
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

}
}

    private void reloadDeviceNameCombo() {
        mDeviceName.removeAll();
        if (mDeviceMap.containsKey(mDeviceManufacturer.getText())) {
            for (final Device d : mDeviceMap.get(mDeviceManufacturer.getText())) {
                mDeviceName.add(d.getName());
            }
        }

    }

private void reloadTargetCombo() {
String selected = null;
int index = mTarget.getSelectionIndex();
//Synthetic comment -- @@ -623,6 +689,7 @@
boolean found = false;
index = -1;

SdkManager sdkManager = mAvdManager.getSdkManager();
if (sdkManager != null) {
for (IAndroidTarget target : sdkManager.getTargets()) {
//Synthetic comment -- @@ -639,6 +706,7 @@
}
mCurrentTargets.put(name, target);
mTarget.add(name);
if (!found) {
index++;
found = name.equals(selected);
//Synthetic comment -- @@ -647,12 +715,35 @@
}

mTarget.setEnabled(mCurrentTargets.size() > 0);

if (found) {
mTarget.select(index);
}
}

/**
* Reload all the abi types in the selection list
*/
//Synthetic comment -- @@ -752,7 +843,7 @@
return;
}

        if (mDeviceManufacturer.getSelectionIndex() < 0 || mDeviceName.getSelectionIndex() < 0) {
setPageValid(false, error, warning);
return;
}
//Synthetic comment -- @@ -907,19 +998,7 @@
}

// Get the device
        List<Device> devices = mDeviceMap.get(mDeviceManufacturer.getText());
        if (devices == null) {
            return false;
        }

        Device device = null;
        for (Device d : devices) {
            if (mDeviceName.getText().equals(d.getName())) {
                device = d;
                break;
            }
        }

if (device == null) {
return false;
}
//Synthetic comment -- @@ -1012,22 +1091,7 @@

private void fillExistingAvdInfo(AvdInfo avd) {
mAvdName.setText(avd.getName());
        String manufacturer = avd.getDeviceManufacturer();
        for (int i = 0; i < mDeviceManufacturer.getItemCount(); i++) {
            if (mDeviceManufacturer.getItem(i).equals(manufacturer)) {
                mDeviceManufacturer.select(i);
                break;
            }
        }
        reloadDeviceNameCombo();

        String deviceName = avd.getDeviceName();
        for (int i = 0; i < mDeviceName.getItemCount(); i++) {
            if (mDeviceName.getItem(i).equals(deviceName)) {
                mDeviceName.select(i);
                break;
            }
        }
toggleCameras();

IAndroidTarget target = avd.getTarget();
//Synthetic comment -- @@ -1172,24 +1236,8 @@
name = name.replaceAll("[^0-9a-zA-Z_-]+", " ").trim().replaceAll("[ _]+", "_");
mAvdName.setText(name);

        // Select the manufacturer of the device
        String manufacturer = device.getManufacturer();
        for (int i = 0; i < mDeviceManufacturer.getItemCount(); i++) {
            if (mDeviceManufacturer.getItem(i).equals(manufacturer)) {
                mDeviceManufacturer.select(i);
                break;
            }
        }
        reloadDeviceNameCombo();

// Select the device
        String deviceName = device.getName();
        for (int i = 0; i < mDeviceName.getItemCount(); i++) {
            if (mDeviceName.getItem(i).equals(deviceName)) {
                mDeviceName.select(i);
                break;
            }
        }
toggleCameras();

// If there's only one target, select it by default.
//Synthetic comment -- @@ -1229,4 +1277,86 @@

return new ISystemImage[0];
}
}







