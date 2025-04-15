/*Support toggling presence of keyboard during AVD creation

Change-Id:I89181f93b85ee404daed3001693d6548f4360e10*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 93c0fe5..75fe6b5 100644

//Synthetic comment -- @@ -87,6 +87,7 @@
private Combo mTarget;
private Combo mAbi;

    private Button mKeyboard;
private Combo mFrontCamera;
private Combo mBackCamera;

//Synthetic comment -- @@ -245,6 +246,13 @@
mAbi.addSelectionListener(validateListener);

label = new Label(parent, SWT.NONE);
        label.setText("Keyboard:");
        mKeyboard = new Button(parent, SWT.CHECK);
        mKeyboard.setSelection(true); // default to having a keyboard irrespective of device
        mKeyboard.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mKeyboard.setText("Hardware keyboard present");

        label = new Label(parent, SWT.NONE);
label.setText("Front Camera:");
tooltip = "";
label.setToolTipText(tooltip);
//Synthetic comment -- @@ -896,6 +904,10 @@
}
hwProps.put(AvdManager.AVD_INI_DATA_PARTITION_SIZE, mDataPartition.getText()+suffix);

        hwProps.put(HardwareProperties.HW_KEYBOARD,
                mKeyboard.getSelection() ?
                        HardwareProperties.BOOLEAN_YES : HardwareProperties.BOOLEAN_NO);

if (mFrontCamera.isEnabled()) {
hwProps.put(AvdManager.AVD_INI_CAMERA_FRONT,
mFrontCamera.getText().toLowerCase());
//Synthetic comment -- @@ -1027,6 +1039,9 @@
}
}

            mKeyboard.setSelection(
                    props.get(HardwareProperties.HW_KEYBOARD) == HardwareProperties.BOOLEAN_YES);

String cameraFront = props.get(AvdManager.AVD_INI_CAMERA_FRONT);
if (cameraFront != null) {
String[] items = mFrontCamera.getItems();







