/*Added a New Folder action in File Explorer.

This action allows the user to create a new folder if the current
selection in the tree is a folder. It executes the "mkdir" command
on the device. If an error occurs during execution, a popup is
displayed.

Change-Id:Id94485502849679ee9c753882b3e46f30e0edd1e*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 2dc1cf0..ee4ea9d 100644

//Synthetic comment -- @@ -17,19 +17,20 @@
package com.android.ddms;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.AndroidDebugBridge.IClientChangeListener;
import com.android.ddmlib.ClientData.IHprofDumpHandler;
import com.android.ddmlib.ClientData.MethodProfilingStatus;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.AllocationPanel;
import com.android.ddmuilib.DevicePanel;
import com.android.ddmuilib.EmulatorControlPanel;
import com.android.ddmuilib.HeapPanel;
import com.android.ddmuilib.ITableFocusListener;
//Synthetic comment -- @@ -40,7 +41,6 @@
import com.android.ddmuilib.SysinfoPanel;
import com.android.ddmuilib.TablePanel;
import com.android.ddmuilib.ThreadPanel;
import com.android.ddmuilib.DevicePanel.IUiSelectionListener;
import com.android.ddmuilib.actions.ToolItemAction;
import com.android.ddmuilib.explorer.DeviceExplorer;
import com.android.ddmuilib.handler.BaseFileHandler;
//Synthetic comment -- @@ -1508,9 +1508,19 @@
deleteAction.item.setText("Delete"); //$NON-NLS-1$
}

// device explorer
mExplorer = new DeviceExplorer();
            mExplorer.setActions(pushAction, pullAction, deleteAction);

pullAction.item.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -1536,6 +1546,14 @@
});
deleteAction.setEnabled(false);

Composite parent = new Composite(mExplorerShell, SWT.NONE);
parent.setLayoutData(new GridData(GridData.FILL_BOTH));









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index 4cf0056..5ef5428 100644

//Synthetic comment -- @@ -389,7 +389,7 @@
* Returns an escaped version of the entry name.
* @param entryName
*/
        private String escape(String entryName) {
return sEscapePattern.matcher(entryName).replaceAll("\\\\$1"); //$NON-NLS-1$
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/explorer/DeviceExplorer.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/explorer/DeviceExplorer.java
//Synthetic comment -- index a0febeb..5bbe66d 100644

//Synthetic comment -- @@ -19,23 +19,28 @@
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.FileListingService;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.FileListingService.FileEntry;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmuilib.DdmUiPreferences;
import com.android.ddmuilib.ImageLoader;
import com.android.ddmuilib.Panel;
import com.android.ddmuilib.SyncProgressHelper;
import com.android.ddmuilib.TableHelper;
import com.android.ddmuilib.SyncProgressHelper.SyncRunnable;
import com.android.ddmuilib.actions.ICommonAction;
import com.android.ddmuilib.console.DdmConsole;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
//Synthetic comment -- @@ -96,6 +101,7 @@
private ICommonAction mPushAction;
private ICommonAction mPullAction;
private ICommonAction mDeleteAction;

private Image mFileImage;
private Image mFolderImage;
//Synthetic comment -- @@ -134,12 +140,14 @@
* @param pushAction
* @param pullAction
* @param deleteAction
*/
public void setActions(ICommonAction pushAction, ICommonAction pullAction,
            ICommonAction deleteAction) {
mPushAction = pushAction;
mPullAction = pullAction;
mDeleteAction = deleteAction;
}

/**
//Synthetic comment -- @@ -201,6 +209,7 @@
mPullAction.setEnabled(false);
mPushAction.setEnabled(false);
mDeleteAction.setEnabled(false);
return;
}
if (sel instanceof IStructuredSelection) {
//Synthetic comment -- @@ -212,7 +221,9 @@
mPullAction.setEnabled(true);
mPushAction.setEnabled(selection.size() == 1);
if (selection.size() == 1) {
                            setDeleteEnabledState((FileEntry)element);
} else {
mDeleteAction.setEnabled(false);
}
//Synthetic comment -- @@ -617,6 +628,82 @@

}

/**
* Force a full refresh of the explorer.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/FileExplorerView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/FileExplorerView.java
//Synthetic comment -- index b3e08a2..57e5370 100644

//Synthetic comment -- @@ -110,8 +110,18 @@
deleteAction.setImageDescriptor(loader.loadDescriptor("delete.png")); //$NON-NLS-1$
deleteAction.setEnabled(false);

// set up the actions in the explorer
        mExplorer.setActions(pushAction, pullAction, deleteAction);

// and in the ui
IActionBars actionBars = getViewSite().getActionBars();
//Synthetic comment -- @@ -122,11 +132,15 @@
menuManager.add(pushAction);
menuManager.add(new Separator());
menuManager.add(deleteAction);

toolBarManager.add(pullAction);
toolBarManager.add(pushAction);
toolBarManager.add(new Separator());
toolBarManager.add(deleteAction);

mExplorer.createPanel(parent);








