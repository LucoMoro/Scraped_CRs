/*avd creation dialog: add check for valid AVD names

Change-Id:I8b2dce8147b89e9bbb549840fb879add5cee6638*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index d78341b..cb5e1ad 100644

//Synthetic comment -- @@ -709,29 +709,45 @@
String error = null;
String warning = null;
boolean valid = true;
if (mAvdName.getText().isEmpty()) {
            valid = false;
}

if (mDeviceManufacturer.getSelectionIndex() < 0 || mDeviceName.getSelectionIndex() < 0) {
            valid = false;
}

if (mTarget.getSelectionIndex() < 0 || mAbi.getSelectionIndex() < 0) {
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
//Synthetic comment -- @@ -762,6 +778,9 @@
error = "SD Card path isn't valid.";
}
}

if (mForceCreation.isEnabled() && !mForceCreation.getSelection()) {
valid = false;
//Synthetic comment -- @@ -782,6 +801,10 @@
error = "GPU Emulation and Snapshot cannot be used simultaneously";
}

mOkButton.setEnabled(valid);
if (error != null) {
mStatusIcon.setImage(mImageFactory.getImageByName("reject_icon16.png")); //$NON-NLS-1$







