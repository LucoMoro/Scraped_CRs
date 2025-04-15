/*Fix index mixing in AVD creation dialog

Change-Id:If7b142ba9da44ccc5ec4976ec2c9375f6888983f*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4d24538..6be646d 100644

//Synthetic comment -- @@ -1417,29 +1417,28 @@
*/
private boolean createAvd() {
String avdName = mAvdName.getText().trim();
        int index = mTargetCombo.getSelectionIndex();

// quick check on the name and the target selection
        if (avdName.length() == 0 || index < 0) {
return false;
}

// resolve the target.
        String targetName = mTargetCombo.getItem(index);
IAndroidTarget target = mCurrentTargets.get(targetName);
if (target == null) {
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







