/*Fix NPE in the DDMS plugin device view.

Change-Id:I771cc215e21f4deb93c85eabce649b275fab3ef4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index a13f3f2..fc6156d 100644

//Synthetic comment -- @@ -246,9 +246,6 @@
@Override
public void createPartControl(Composite parent) {
mParentShell = parent.getShell();
        ClientData.setHprofDumpHandler(new HProfHandler(mParentShell));
        AndroidDebugBridge.addClientChangeListener(this);
        ClientData.setMethodProfilingHandler(new MethodProfilingHandler(mParentShell));

mDeviceList = new DevicePanel(DdmsPlugin.getImageLoader(), USE_SELECTED_DEBUG_PORT);
mDeviceList.createPanel(parent);
//Synthetic comment -- @@ -425,6 +422,10 @@
}

placeActions();
}

@Override







