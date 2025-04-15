/*Fix index mixing in AVD creation dialog

Change-Id:If7b142ba9da44ccc5ec4976ec2c9375f6888983f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4d24538..6be646d 100644

//Synthetic comment -- @@ -1417,29 +1417,28 @@
*/
private boolean createAvd() {
String avdName = mAvdName.getText().trim();
        int targetIndex = mTargetCombo.getSelectionIndex();

// quick check on the name and the target selection
        if (avdName.length() == 0 || targetIndex < 0) {
return false;
}

        targetIndex = mDeviceCombo.getSelectionIndex();
        if (targetIndex >= 0 && targetIndex < mDeviceList.size()) {
            Device d = mDeviceList.get(targetIndex);
            // Set the properties so it gets saved to the avd's ini
            mProperties.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, d.getManufacturer());
            mProperties.put(AvdManager.AVD_INI_DEVICE_NAME, d.getName());
        }


// resolve the target.
        String targetName = mTargetCombo.getItem(targetIndex);
IAndroidTarget target = mCurrentTargets.get(targetName);
if (target == null) {
return false;
}

// get the abi type
mAbiType = SdkConstants.ABI_ARMEABI;
ISystemImage[] systemImages = getSystemImages(target);







