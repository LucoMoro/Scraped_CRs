/*Add RAM, VM heap and internal storage size settings to AVDs

Change-Id:Idb8807e07fddc912144b962de0a01b729071c92b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 091dc0b..99ede54 100644

//Synthetic comment -- @@ -167,6 +167,21 @@
public final static String AVD_INI_CAMERA_BACK = "hw.camera.back"; //$NON-NLS-1$

/**
     * AVD/config.ini key name representing the amount of RAM the emulated device should have
     */
    public final static String AVD_INI_RAM_SIZE = "hw.ramSize";

    /**
     * AVD/config.ini key name representing the amount of memory available to applications by default
     */
    public final static String AVD_INI_VM_HEAP_SIZE = "vm.heapSize";

    /**
     * AVD/config.ini key name representing the size of the data partition
     */
    public final static String AVD_INI_DATA_PARTITION_SIZE = "disk.dataPartition.size";

    /**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 1c0b167..e89f113 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.sdkuilib.internal.widgets;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Density;
import com.android.resources.ScreenSize;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.ISystemImage;
//Synthetic comment -- @@ -26,7 +28,9 @@
import com.android.sdklib.devices.CameraLocation;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.devices.Storage;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
//Synthetic comment -- @@ -89,6 +93,12 @@
private Button mSnapshot;
private Button mGpuEmulation;

    private Text mRam;
    private Text mVmHeap;

    private Text mDataPartition;
    private Combo mDataPartitionSize;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
private Combo mSdCardSizeCombo;
//Synthetic comment -- @@ -258,20 +268,51 @@

toggleCameras();

        // --- memory options group
label = new Label(parent, SWT.NONE);
        label.setText("Memory Options:");


        Group memoryGroup = new Group(parent, SWT.BORDER);
        memoryGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        memoryGroup.setLayout(new GridLayout(4, false));

        label = new Label(memoryGroup, SWT.NONE);
        label.setText("RAM:");
        tooltip = "The amount of RAM the emulated device should have in MiB";
        label.setToolTipText(tooltip);
        mRam = new Text(memoryGroup, SWT.BORDER);
        mRam.addVerifyListener(mDigitVerifier);
        mRam.addModifyListener(validateListener);
        mRam.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        label = new Label(memoryGroup, SWT.NONE);
        label.setText("VM Heap:");
        tooltip = "The amount of memory, in MiB, available to typical Android applications";
        label.setToolTipText(tooltip);
        mVmHeap = new Text(memoryGroup, SWT.BORDER);
        mVmHeap.addVerifyListener(mDigitVerifier);
        mVmHeap.addModifyListener(validateListener);
        mVmHeap.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mVmHeap.setToolTipText(tooltip);

        // --- Data partition group
        label = new Label(parent, SWT.NONE);
        label.setText("Internal Storage:");
        tooltip = "The size of the data partition on the device.";
        Group storageGroup = new Group(parent, SWT.NONE);
        storageGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        storageGroup.setLayout(new GridLayout(2, false));
        mDataPartition = new Text(storageGroup, SWT.BORDER);
        mDataPartition.setText("200");
        mDataPartition.addVerifyListener(mDigitVerifier);
        mDataPartition.addModifyListener(validateListener);
        mDataPartition.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mDataPartitionSize = new Combo(storageGroup, SWT.READ_ONLY | SWT.DROP_DOWN);
        mDataPartitionSize.add("MiB");
        mDataPartitionSize.add("GiB");
        mDataPartitionSize.select(0);
        mDataPartitionSize.addModifyListener(validateListener);

// --- sd card group
label = new Label(parent, SWT.NONE);
//Synthetic comment -- @@ -331,6 +372,19 @@
mSdCardSizeRadio.setSelection(true);
enableSdCardWidgets(true);

        // --- avd options group
        label = new Label(parent, SWT.NONE);
        label.setText("Emulation Options:");
        Group optionsGroup = new Group(parent, SWT.NONE);
        optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        optionsGroup.setLayout(new GridLayout(2, true));
        mSnapshot = new Button(optionsGroup, SWT.CHECK);
        mSnapshot.setText("Snapshot");
        mSnapshot.setToolTipText("Emulator's state will be persisted between emulator executions");
        mGpuEmulation = new Button(optionsGroup, SWT.CHECK);
        mGpuEmulation.setText("GPU Emulation");
        mGpuEmulation.setToolTipText("Enable hardware OpenGLES emulation");

// --- force creation group
mForceCreation = new Button(parent, SWT.CHECK);
mForceCreation.setText("Override the existing AVD with the same name");
//Synthetic comment -- @@ -425,6 +479,58 @@

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
                Hardware hw = currentDevice.getDefaultHardware();
                Long ram = hw.getRam().getSizeAsUnit(Storage.Unit.MiB);
                mRam.setText(Long.toString(ram));

                // Set the default VM heap size. This is based on the Android CDD minimums for each
                // screen size and density.
                Screen s = hw.getScreen();
                ScreenSize size = s.getSize();
                Density density = s.getPixelDensity();
                int vmHeapSize = 32;
                if (size.equals(ScreenSize.XLARGE)) {
                    switch (density) {
                        case LOW:
                        case MEDIUM:
                            vmHeapSize = 32;
                            break;
                        case TV:
                        case HIGH:
                            vmHeapSize = 64;
                            break;
                        case XHIGH:
                        case XXHIGH:
                            vmHeapSize = 128;
                    }
                } else {
                    switch (density) {
                        case LOW:
                        case MEDIUM:
                            vmHeapSize = 16;
                            break;
                        case TV:
                        case HIGH:
                            vmHeapSize = 32;
                            break;
                        case XHIGH:
                        case XXHIGH:
                            vmHeapSize = 64;

                    }
                }
                mVmHeap.setText(Integer.toString(vmHeapSize));
            }

toggleCameras();
validatePage();
}
//Synthetic comment -- @@ -596,6 +702,19 @@
valid = false;
}

        if (mRam.getText().isEmpty()) {
            valid = false;
        }

        if (mVmHeap.getText().isEmpty()) {
            valid = false;
        }

        if (mDataPartition.getText().isEmpty() || mDataPartitionSize.getSelectionIndex() < 0) {
            valid = false;
            error = "Data partition must be a valid file size.";
        }

// validate sdcard size or file
if (mSdCardSizeRadio.getSelection()) {
if (!mSdCardSize.getText().isEmpty() && mSdCardSizeCombo.getSelectionIndex() >= 0) {
//Synthetic comment -- @@ -755,6 +874,24 @@
hwProps.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, mDeviceManufacturer.getText());
hwProps.put(AvdManager.AVD_INI_DEVICE_NAME, mDeviceName.getText());

        // Although the device has this information, some devices have more RAM than we'd want to
        // allocate to an emulator.
        hwProps.put(AvdManager.AVD_INI_RAM_SIZE, mRam.getText());
        hwProps.put(AvdManager.AVD_INI_VM_HEAP_SIZE, mVmHeap.getText());

        String suffix;
        switch (mDataPartitionSize.getSelectionIndex()) {
            case 0:
                suffix = "M";
                break;
            case 1:
                suffix = "G";
                break;
            default:
                suffix = "K";
        }
        hwProps.put(AvdManager.AVD_INI_DATA_PARTITION_SIZE, mDataPartition.getText()+suffix);

if (mFrontCamera.isEnabled()) {
hwProps.put(AvdManager.AVD_INI_CAMERA_FRONT,
mFrontCamera.getText().toLowerCase());
//Synthetic comment -- @@ -856,6 +993,32 @@
mSdCardFile.setText(sdcard);
}

            String ramSize = props.get(AvdManager.AVD_INI_RAM_SIZE);
            if (ramSize != null) {
                mRam.setText(ramSize);
            }

            String vmHeapSize = props.get(AvdManager.AVD_INI_VM_HEAP_SIZE);
            if (vmHeapSize != null) {
                mVmHeap.setText(vmHeapSize);
            }

            String dataPartitionSize = props.get(AvdManager.AVD_INI_DATA_PARTITION_SIZE);
            if (dataPartitionSize != null) {
                mDataPartition.setText(
                        dataPartitionSize.substring(0, dataPartitionSize.length() - 1));
                switch (dataPartitionSize.charAt(dataPartitionSize.length() - 1)) {
                    case 'M':
                        mDataPartitionSize.select(0);
                        break;
                    case 'G':
                        mDataPartitionSize.select(1);
                        break;
                    default:
                        mDataPartitionSize.select(-1);
                }
            }

String cameraFront = props.get(AvdManager.AVD_INI_CAMERA_FRONT);
if (cameraFront != null) {
String[] items = mFrontCamera.getItems();







