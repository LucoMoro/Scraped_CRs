/*Disabling action buttons in device panel.
Before selecting a target device.

Change-Id:I704d8af3d5f89dcbc5eb8e5e6b6090ddd8561e22*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 56e7475..31ae3b8 100644

//Synthetic comment -- @@ -471,6 +471,9 @@

placeActions();

        // disabling all action buttons
        selectionChanged(null, null);

ClientData.setHprofDumpHandler(new HProfHandler(mParentShell));
AndroidDebugBridge.addClientChangeListener(this);
ClientData.setMethodProfilingHandler(new MethodProfilingHandler(mParentShell) {







