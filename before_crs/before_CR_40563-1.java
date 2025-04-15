/*Add RAM, VM heap and internal storage size settings to AVDs

Change-Id:Idb8807e07fddc912144b962de0a01b729071c92b*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 091dc0b..99ede54 100644

//Synthetic comment -- @@ -167,6 +167,21 @@
public final static String AVD_INI_CAMERA_BACK = "hw.camera.back"; //$NON-NLS-1$

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 1c0b167..e89f113 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.sdkuilib.internal.widgets;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.ISystemImage;
//Synthetic comment -- @@ -26,7 +28,9 @@
import com.android.sdklib.devices.CameraLocation;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
//Synthetic comment -- @@ -89,6 +93,12 @@
private Button mSnapshot;
private Button mGpuEmulation;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
private Combo mSdCardSizeCombo;
//Synthetic comment -- @@ -258,20 +268,51 @@

toggleCameras();

        // --- avd options group
label = new Label(parent, SWT.NONE);
        label.setText("Options:");
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING,
                false, false));
        Group optionsGroup = new Group(parent, SWT.NONE);
        optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        optionsGroup.setLayout(new GridLayout(2, true));
        mSnapshot = new Button(optionsGroup, SWT.CHECK);
        mSnapshot.setText("Snapshot");
        mSnapshot.setToolTipText("Emulator's state will be persisted between emulator executions");
        mGpuEmulation = new Button(optionsGroup, SWT.CHECK);
        mGpuEmulation.setText("GPU Emulation");
        mGpuEmulation.setToolTipText("Enable hardware OpenGLES emulation");

// --- sd card group
label = new Label(parent, SWT.NONE);
//Synthetic comment -- @@ -331,6 +372,19 @@
mSdCardSizeRadio.setSelection(true);
enableSdCardWidgets(true);

// --- force creation group
mForceCreation = new Button(parent, SWT.CHECK);
mForceCreation.setText("Override the existing AVD with the same name");
//Synthetic comment -- @@ -425,6 +479,58 @@

@Override
public void widgetSelected(SelectionEvent arg0) {
toggleCameras();
validatePage();
}
//Synthetic comment -- @@ -596,6 +702,19 @@
valid = false;
}

// validate sdcard size or file
if (mSdCardSizeRadio.getSelection()) {
if (!mSdCardSize.getText().isEmpty() && mSdCardSizeCombo.getSelectionIndex() >= 0) {
//Synthetic comment -- @@ -755,6 +874,24 @@
hwProps.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, mDeviceManufacturer.getText());
hwProps.put(AvdManager.AVD_INI_DEVICE_NAME, mDeviceName.getText());

if (mFrontCamera.isEnabled()) {
hwProps.put(AvdManager.AVD_INI_CAMERA_FRONT,
mFrontCamera.getText().toLowerCase());
//Synthetic comment -- @@ -856,6 +993,32 @@
mSdCardFile.setText(sdcard);
}

String cameraFront = props.get(AvdManager.AVD_INI_CAMERA_FRONT);
if (cameraFront != null) {
String[] items = mFrontCamera.getItems();







