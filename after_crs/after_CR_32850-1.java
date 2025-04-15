/*Disabling "capture button" in device panel.
Before selecting a target device.

Change-Id:I704d8af3d5f89dcbc5eb8e5e6b6090ddd8561e22*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 56e7475..3da6333 100644

//Synthetic comment -- @@ -305,6 +305,7 @@
dlg.open(mDeviceList.getSelectedDevice());
}
};
        mCaptureAction.setEnabled(false);
mCaptureAction.setToolTipText(Messages.DeviceView_Screen_Capture_Tooltip);
mCaptureAction.setImageDescriptor(loader.loadDescriptor("capture.png")); //$NON-NLS-1$








