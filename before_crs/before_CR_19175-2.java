/*The change fixes the 11503 issue -http://code.google.com/p/android/issues/detail?id=11503.It works in the following way:

    * if location of Android SDK has not been set or location isn't valid, action shows the old message
    * if there is valid SDK which isn't loaded, action show progress dialog with the "Please wait until SDK is loading ..." message
      (the progress dialog is cancelable)
    * when SDK is loaded, action show the standard "Android SDK and AVD manager" dialog

Note: AvdManagerAction.java, line 65

The problem can be reproduced if you start some non-modal progress dialog and click
on Open SDK and AVD manager action when progress dialog is active.
When progress dialog be finished, it will close SDK and AVD Manager dialog.
AdtPlugin.getDisplay().getActiveShell() have used shell of progress dialog.
getShell() returns the shell of an active workbench window.
If we would use AdtPlugin.getDisplay().getActiveShell() then the SDK and AVD dialog
would be open whent progress dialog finising and just close.

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







