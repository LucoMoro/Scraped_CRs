/*Make using GPU Emulation and Snapshots an error

Change-Id:I0e77931a034af5d005e73c4f54818595c0720ab3*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index e89f113..6f3ec0e 100644

//Synthetic comment -- @@ -381,9 +381,11 @@
mSnapshot = new Button(optionsGroup, SWT.CHECK);
mSnapshot.setText("Snapshot");
mSnapshot.setToolTipText("Emulator's state will be persisted between emulator executions");
mGpuEmulation = new Button(optionsGroup, SWT.CHECK);
mGpuEmulation.setText("GPU Emulation");
mGpuEmulation.setToolTipText("Enable hardware OpenGLES emulation");

// --- force creation group
mForceCreation = new Button(parent, SWT.CHECK);
//Synthetic comment -- @@ -758,6 +760,11 @@
mAvdName.getText());
}

mOkButton.setEnabled(valid);
if (error != null) {
mStatusIcon.setImage(mImageFactory.getImageByName("reject_icon16.png")); //$NON-NLS-1$







