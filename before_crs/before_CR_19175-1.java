/*Fixing the 11503 issue

Change-Id:I7d23d117b9cbf1a93854e2a3645809c1046ad0ec*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index e0de79a..5124eec 100755

//Synthetic comment -- @@ -17,16 +17,27 @@
package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
* Delegate for the toolbar/menu action "Android AVD Manager".
//Synthetic comment -- @@ -34,6 +45,10 @@
*/
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

public void dispose() {
// nothing to dispose.
}
//Synthetic comment -- @@ -43,13 +58,13 @@
}

public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
if (sdk != null) {

// Runs the updater window, directing all logs to the ADT console.

UpdaterWindow window = new UpdaterWindow(
                    AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
sdk.getSdkLocation());
window.addListeners(new UpdaterWindow.ISdkListener() {
//Synthetic comment -- @@ -73,4 +88,88 @@
public void setActivePart(IAction action, IWorkbenchPart targetPart) {
// nothing to do.
}
}







