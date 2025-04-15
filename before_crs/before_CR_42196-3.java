/*wip: add systrace

Change-Id:I4564421b86ebb384e5bd2b3e2329249abd3771aa*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java
//Synthetic comment -- index 71639ec..fe8c709 100644

//Synthetic comment -- @@ -464,7 +464,7 @@
return sAdbLocation;
}

    public static String getToolsFolder2() {
return sToolsFolder;
}

//Synthetic comment -- @@ -505,6 +505,8 @@
sHprofConverter = hprofConverter.getAbsolutePath();
DdmUiPreferences.setTraceviewLocation(traceview.getAbsolutePath());

return true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceHtmlWriter.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceHtmlWriter.java
new file mode 100644
//Synthetic comment -- index 0000000..bf1f145

//Synthetic comment -- @@ -0,0 +1,161 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..98b5b34

//Synthetic comment -- @@ -0,0 +1,420 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOutputReceiver.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOutputReceiver.java
new file mode 100644
//Synthetic comment -- index 0000000..4659767

//Synthetic comment -- @@ -0,0 +1,58 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 17670e4..18b74ae 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ddmlib.ClientData;
import com.android.ddmlib.ClientData.IHprofDumpHandler;
import com.android.ddmlib.ClientData.MethodProfilingStatus;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.SyncException;
//Synthetic comment -- @@ -41,6 +42,10 @@
import com.android.ide.eclipse.ddms.editors.UiAutomatorViewer;
import com.android.ide.eclipse.ddms.i18n.Messages;
import com.android.ide.eclipse.ddms.preferences.PreferenceInitializer;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;
//Synthetic comment -- @@ -80,6 +85,8 @@
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DeviceView extends ViewPart implements IUiSelectionListener, IClientChangeListener {

//Synthetic comment -- @@ -95,6 +102,7 @@
private Action mResetAdbAction;
private Action mCaptureAction;
private Action mViewUiAutomatorHierarchyAction;
private Action mUpdateThreadAction;
private Action mUpdateHeapAction;
private Action mGcAction;
//Synthetic comment -- @@ -331,6 +339,18 @@
mViewUiAutomatorHierarchyAction.setImageDescriptor(
DdmsPlugin.getImageDescriptor("icons/uiautomator.png")); //$NON-NLS-1$

mResetAdbAction = new Action(Messages.DeviceView_Reset_ADB) {
@Override
public void run() {
//Synthetic comment -- @@ -536,6 +556,70 @@
}
};


@Override
public void setFocus() {
//Synthetic comment -- @@ -635,8 +719,11 @@
}

private void doSelectionChanged(IDevice selectedDevice) {
        mCaptureAction.setEnabled(selectedDevice != null);
        mViewUiAutomatorHierarchyAction.setEnabled(selectedDevice != null);
}

/**
//Synthetic comment -- @@ -663,6 +750,8 @@
menuManager.add(new Separator());
menuManager.add(mViewUiAutomatorHierarchyAction);
menuManager.add(new Separator());
menuManager.add(mResetAdbAction);

// and then in the toolbar
//Synthetic comment -- @@ -682,6 +771,8 @@
toolBarManager.add(mCaptureAction);
toolBarManager.add(new Separator());
toolBarManager.add(mViewUiAutomatorHierarchyAction);
}

@Override







