/*wip: add systrace

Change-Id:I4564421b86ebb384e5bd2b3e2329249abd3771aa*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 17670e4..d58e70c 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -95,6 +96,7 @@
private Action mResetAdbAction;
private Action mCaptureAction;
private Action mViewUiAutomatorHierarchyAction;
private Action mUpdateThreadAction;
private Action mUpdateHeapAction;
private Action mGcAction;
//Synthetic comment -- @@ -331,6 +333,18 @@
mViewUiAutomatorHierarchyAction.setImageDescriptor(
DdmsPlugin.getImageDescriptor("icons/uiautomator.png")); //$NON-NLS-1$

mResetAdbAction = new Action(Messages.DeviceView_Reset_ADB) {
@Override
public void run() {
//Synthetic comment -- @@ -536,6 +550,12 @@
}
};


@Override
public void setFocus() {
//Synthetic comment -- @@ -635,8 +655,11 @@
}

private void doSelectionChanged(IDevice selectedDevice) {
        mCaptureAction.setEnabled(selectedDevice != null);
        mViewUiAutomatorHierarchyAction.setEnabled(selectedDevice != null);
}

/**
//Synthetic comment -- @@ -663,6 +686,8 @@
menuManager.add(new Separator());
menuManager.add(mViewUiAutomatorHierarchyAction);
menuManager.add(new Separator());
menuManager.add(mResetAdbAction);

// and then in the toolbar
//Synthetic comment -- @@ -682,6 +707,8 @@
toolBarManager.add(mCaptureAction);
toolBarManager.add(new Separator());
toolBarManager.add(mViewUiAutomatorHierarchyAction);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..cd8507c

//Synthetic comment -- @@ -0,0 +1,347 @@







