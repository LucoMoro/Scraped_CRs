/*avd creation dialog: add check for valid AVD names

Change-Id:I8b2dce8147b89e9bbb549840fb879add5cee6638*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index d78341b..cb5e1ad 100644

//Synthetic comment -- @@ -709,29 +709,45 @@
String error = null;
String warning = null;
boolean valid = true;

if (mAvdName.getText().isEmpty()) {
            error = "AVD Name cannot be empty";
            setPageValid(false, error, warning);
            return;
        }

        String avdName = mAvdName.getText();
        if (!AvdManager.RE_AVD_NAME.matcher(avdName).matches()) {
            error = String.format(
                    "AVD name '%1$s' contains invalid characters.\nAllowed characters are: %2$s",
                    avdName, AvdManager.CHARS_AVD_NAME);
            setPageValid(false, error, warning);
            return;
}

if (mDeviceManufacturer.getSelectionIndex() < 0 || mDeviceName.getSelectionIndex() < 0) {
            setPageValid(false, error, warning);
            return;
}

if (mTarget.getSelectionIndex() < 0 || mAbi.getSelectionIndex() < 0) {
            setPageValid(false, error, warning);
            return;
}

if (mRam.getText().isEmpty()) {
            setPageValid(false, error, warning);
            return;
}

if (mVmHeap.getText().isEmpty()) {
            setPageValid(false, error, warning);
            return;
}

if (mDataPartition.getText().isEmpty() || mDataPartitionSize.getSelectionIndex() < 0) {
            error = "Invalid Data partition size.";
            setPageValid(false, error, warning);
}

// validate sdcard size or file
//Synthetic comment -- @@ -762,6 +778,9 @@
error = "SD Card path isn't valid.";
}
}
        if (!valid) {
            setPageValid(valid, error, warning);
        }

if (mForceCreation.isEnabled() && !mForceCreation.getSelection()) {
valid = false;
//Synthetic comment -- @@ -782,6 +801,10 @@
error = "GPU Emulation and Snapshot cannot be used simultaneously";
}

        setPageValid(valid, error, warning);
    }

    private void setPageValid(boolean valid, String error, String warning) {
mOkButton.setEnabled(valid);
if (error != null) {
mStatusIcon.setImage(mImageFactory.getImageByName("reject_icon16.png")); //$NON-NLS-1$







