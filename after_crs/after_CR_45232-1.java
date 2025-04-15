/*AVD Manager: use device RAM/VM when creating AVD.

Also set default target if there's only one in the current SDK.

SDK Bug: 38785

Change-Id:I76d92ea4c43f94275f3d4373251a87ec3a6610a1*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 657dbda..e3bd5f7 100644

//Synthetic comment -- @@ -519,47 +519,7 @@
}

if (currentDevice != null) {
                fillDeviceProperties(currentDevice);
}

toggleCameras();
//Synthetic comment -- @@ -568,6 +528,50 @@

}

    private void fillDeviceProperties(Device device) {
        Hardware hw = device.getDefaultHardware();
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

private void toggleCameras() {
mFrontCamera.setEnabled(false);
mBackCamera.setEnabled(false);
//Synthetic comment -- @@ -1148,7 +1152,9 @@

private void fillInitialDeviceInfo(Device device) {
String name = device.getManufacturer();
        if (!name.equals("Generic") &&      // TODO define & use constants
                !name.equals("User") &&
                device.getName().indexOf(name) == -1) {
name = " by " + name;
} else {
name = "";
//Synthetic comment -- @@ -1158,6 +1164,7 @@
name = name.replaceAll("[^0-9a-zA-Z_-]+", " ").trim().replaceAll("[ _]+", "_");
mAvdName.setText(name);

        // Select the manufacturer of the device
String manufacturer = device.getManufacturer();
for (int i = 0; i < mDeviceManufacturer.getItemCount(); i++) {
if (mDeviceManufacturer.getItem(i).equals(manufacturer)) {
//Synthetic comment -- @@ -1167,6 +1174,7 @@
}
reloadDeviceNameCombo();

        // Select the device
String deviceName = device.getName();
for (int i = 0; i < mDeviceName.getItemCount(); i++) {
if (mDeviceName.getItem(i).equals(deviceName)) {
//Synthetic comment -- @@ -1176,6 +1184,15 @@
}
toggleCameras();

        // If there's only one target, select it by default.
        // TODO: if there are more than 1 target, select the higher platform target as
        // a likely default.
        if (mTarget.getItemCount() == 1) {
            mTarget.select(0);
            reloadAbiTypeCombo();
        }

        fillDeviceProperties(device);
}

/**







