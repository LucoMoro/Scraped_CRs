/*Add new AVD creation and edit dialog

Take advantage of the new device specifications and provide a much
simplified AVD creation dialog, while retaining the old dialog for use
with AVDs created prior to this.

Change-Id:I2ab3613d6a1b58a96c330dc1d8c1df36afc88058*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index cb7b90f..7c2ee66 100644

//Synthetic comment -- @@ -309,17 +309,6 @@
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
props.put("hw.sensors.proximity",
getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));

        for (Camera c : hw.getCameras()) {
            String prop;
            if (c.getLocation().equals(CameraLocation.FRONT)) {
                prop = "hw.camera.front";
            } else {
                prop = "hw.camera.back";
            }
            props.put(prop, "emulated");
        }

return props;
}

//Synthetic comment -- @@ -337,7 +326,7 @@
props.put("hw.keyboard.lid", getBooleanVal(true));
}
}
        return getHardwareProperties(d.getDefaultState());
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 46d3124..091dc0b 100644

//Synthetic comment -- @@ -152,6 +152,21 @@
public final static String AVD_INI_SNAPSHOT_PRESENT = "snapshot.present"; //$NON-NLS-1$

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index c84506b..06cdc74 100755

//Synthetic comment -- @@ -362,7 +362,9 @@
public void widgetSelected(SelectionEvent e) {
DeviceCreationDialog dlg = new DeviceCreationDialog(
mShell, manager, mUpdaterData.getImageFactory(), null);
                dlg.open();
}
});
new MenuItem(menuDevices, SWT.SEPARATOR);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..2c7d7bb

//Synthetic comment -- @@ -0,0 +1,917 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index bf2389e..58bacec 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.sdkuilib.internal.repository.sdkman2.AvdManagerWindowImpl1;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -872,7 +873,7 @@
}

private void onNew() {
        LegacyAvdEditDialog dlg = new LegacyAvdEditDialog(mTable.getShell(),
mAvdManager,
mImageFactory,
mSdkLog,
//Synthetic comment -- @@ -885,12 +886,21 @@

private void onEdit() {
AvdInfo avdInfo = getTableSelection();

        LegacyAvdEditDialog dlg = new LegacyAvdEditDialog(mTable.getShell(),
                mAvdManager,
                mImageFactory,
                mSdkLog,
                avdInfo);

if (dlg.open() == Window.OK) {
refresh(false /*reload*/);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/LegacyAvdEditDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/LegacyAvdEditDialog.java
//Synthetic comment -- index d732bdb..dc6b459 100644

//Synthetic comment -- @@ -23,10 +23,6 @@
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.devices.Abi;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
//Synthetic comment -- @@ -78,10 +74,8 @@
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;

//Synthetic comment -- @@ -105,8 +99,6 @@
private final ArrayList<String> mEditedProperties = new ArrayList<String>();
private final ImageFactory mImageFactory;
private final ISdkLog mSdkLog;
    private final DeviceManager mDeviceManager;
    private final List<Device> mDeviceList = new ArrayList<Device>();
/**
* The original AvdInfo if we're editing an existing AVD.
* Null when we're creating a new AVD.
//Synthetic comment -- @@ -119,8 +111,6 @@
private Combo mAbiTypeCombo;
private String mAbiType;

    private Combo mDeviceCombo;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
private Combo mSdCardSizeCombo;
//Synthetic comment -- @@ -240,9 +230,6 @@
mEditAvdInfo = editAvdInfo;

File hardwareDefs = null;
        mDeviceManager = new DeviceManager(log);
        mDeviceList.addAll(mDeviceManager.getUserDevices());
        mDeviceList.addAll(mDeviceManager.getDefaultDevices());

SdkManager sdkMan = avdManager.getSdkManager();
if (sdkMan != null) {
//Synthetic comment -- @@ -250,7 +237,6 @@
if (sdkPath != null) {
hardwareDefs = new File (sdkPath + File.separator +
SdkConstants.OS_SDK_TOOLS_LIB_FOLDER, SdkConstants.FN_HARDWARE_INI);
                mDeviceList.addAll(mDeviceManager.getVendorDevices(sdkPath));
}
}

//Synthetic comment -- @@ -317,32 +303,10 @@
super.widgetSelected(e);
reloadSkinCombo();
reloadAbiTypeCombo();
                reloadDeviceCombo();
validatePage();
}
});

        // Device Selection
        label = new Label(parent, SWT.NONE);
        label.setText("Device:");
        tooltip = "The device to base the AVD on. This is an optional setting and will merely " +
                "prefill the settings so they match the selected device as closely as possible.";
        label.setToolTipText(tooltip);
        mDeviceCombo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
        mDeviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mDeviceCombo.setToolTipText(tooltip);
        mDeviceCombo.setEnabled(false);
        mDeviceCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                prefillWithDeviceConfig();
                validatePage();
            }


        });

//ABI group
label = new Label(parent, SWT.NONE);
label.setText("CPU/ABI:");
//Synthetic comment -- @@ -573,7 +537,6 @@
mStatusLabel.setText(" \n "); //$NON-NLS-1$

reloadTargetCombo();
        reloadDeviceCombo();
}

/**
//Synthetic comment -- @@ -766,21 +729,6 @@

Map<String, String> props = mEditAvdInfo.getProperties();

        if (props != null) {
            // Try to match it to a device

            // The device has to be set before everything else because
            // selecting a device will modify other options
            for (int i = 0; i < mDeviceList.size(); i++){
                Device d = mDeviceList.get(i);
                if(d.getManufacturer().equals(props.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER))
                        && d.getName().equals(props.get(AvdManager.AVD_INI_DEVICE_NAME))) {
                    mDeviceCombo.select(i);
                    break;
                }
            }
        }

IAndroidTarget target = mEditAvdInfo.getTarget();
if (target != null && !mCurrentTargets.isEmpty()) {
// Try to select the target in the target combo.
//Synthetic comment -- @@ -793,7 +741,6 @@
if (target.equals(mCurrentTargets.get(mTargetCombo.getItem(i)))) {
mTargetCombo.select(i);
reloadAbiTypeCombo();
                    reloadDeviceCombo();
reloadSkinCombo();
break;
}
//Synthetic comment -- @@ -903,47 +850,10 @@
mProperties.remove(AvdManager.AVD_INI_SNAPSHOT_PRESENT);
mProperties.remove(AvdManager.AVD_INI_IMAGES_1);
mProperties.remove(AvdManager.AVD_INI_IMAGES_2);
        mProperties.remove(AvdManager.AVD_INI_DEVICE_MANUFACTURER);
        mProperties.remove(AvdManager.AVD_INI_DEVICE_NAME);

mHardwareViewer.refresh();
}

    // Sets all of the other options based on the device currently selected in mDeviceCombo
    private void prefillWithDeviceConfig() {
        Device d = getSelectedDevice();
        if (d != null) {
            Hardware hw = d.getDefaultHardware();

            // Try setting the CPU/ABI
            if (mAbiTypeCombo.isEnabled()) {
                Set<Abi> abis = hw.getSupportedAbis();
                // This is O(n*m), but the two lists should be sufficiently small.
                for (Abi abi : abis) {
                    for (int i = 0; i < mAbiTypeCombo.getItemCount(); i++) {
                        if (mAbiTypeCombo.getItem(i)
                                .equals(AvdInfo.getPrettyAbiType(abi.toString()))){
                            mAbiTypeCombo.select(i);
                        }
                    }
                }
            }

            // Set the screen resolution
            mSkinListRadio.setSelection(false);
            mSkinSizeRadio.setSelection(true);
            mSkinCombo.setEnabled(false);
            mSkinSizeWidth.setEnabled(true);
            mSkinSizeWidth.setText(Integer.toString(hw.getScreen().getXDimension()));
            mSkinSizeHeight.setEnabled(true);
            mSkinSizeHeight.setText(Integer.toString(hw.getScreen().getYDimension()));

            mProperties.putAll(DeviceManager.getHardwareProperties(d));
            mHardwareViewer.refresh();

        }
    }

@Override
protected void okPressed() {
if (createAvd()) {
//Synthetic comment -- @@ -1034,26 +944,6 @@
reloadSkinCombo();
}


    private void reloadDeviceCombo() {
        Device selectedDevice = getSelectedDevice();

        mDeviceCombo.removeAll();
        for (Device d : mDeviceList) {
            mDeviceCombo.add(d.getManufacturer() + " " + d.getName());
        }

        // Try to select the previously selected device if it still exists
        if (selectedDevice != null) {
            int index = mDeviceList.indexOf(selectedDevice);
            if (index >= 0) {
                mDeviceCombo.select(index);
            }
        }

        mDeviceCombo.setEnabled(mTargetCombo.getSelectionIndex() >= 0);
    }

private void reloadSkinCombo() {
String selected = null;
int index = mSkinCombo.getSelectionIndex();
//Synthetic comment -- @@ -1405,14 +1295,6 @@
return false;
}

        index = mDeviceCombo.getSelectionIndex();
        if (index >= 0 && index < mDeviceList.size()) {
            Device d = mDeviceList.get(index);
            // Set the properties so it gets saved to the avd's ini
            mProperties.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, d.getManufacturer());
            mProperties.put(AvdManager.AVD_INI_DEVICE_NAME, d.getName());
        }

// get the abi type
mAbiType = SdkConstants.ABI_ARMEABI;
ISystemImage[] systemImages = getSystemImages(target);
//Synthetic comment -- @@ -1506,10 +1388,6 @@

success = avdInfo != null;

        // Remove the device name and manufacturer properties so they don't show up in the hardware list
        mProperties.remove(AvdManager.AVD_INI_DEVICE_MANUFACTURER);
        mProperties.remove(AvdManager.AVD_INI_DEVICE_NAME);

if (log instanceof MessageBoxLog) {
((MessageBoxLog) log).displayResult(success);
}
//Synthetic comment -- @@ -1542,15 +1420,6 @@
return new ISystemImage[0];
}

    private Device getSelectedDevice() {
        int targetIndex = mDeviceCombo.getSelectionIndex();
        if (targetIndex >= 0 && mDeviceList.size() > targetIndex){
            return mDeviceList.get(targetIndex);
        } else {
            return null;
        }
    }

// End of hiding from SWT Designer
//$hide<<$
}







