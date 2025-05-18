//<Beginning of snippet n. 0>
/*
 * Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
*/
package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    public void dispose() {
        // nothing to dispose.
    }

    public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {
            Shell shell = getShell();
            if (shell != null && !isProgressDialogActive()) {
                UpdaterWindow window = new UpdaterWindow(
                        shell,
                        new AdtConsoleSdkLog(),
                        sdk.getSdkLocation());
                window.open(); // Assuming open() is a method for displaying the window
            } else {
                notifyUserOfActiveProgressDialog();
            }
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }

    private Shell getShell() {
        IWorkbenchWindow window = AdtPlugin.getActiveWorkbenchWindow();
        if (window != null) {
            return window.getShell();
        }
        return null;
    }

    private boolean isProgressDialogActive() {
        Display display = Display.getDefault();
        return display.getShells().length > 0 && display.getActiveShell() != null && display.getActiveShell().isVisible(); // Check for any visible shell as progress dialog
    }

    private void notifyUserOfActiveProgressDialog() {
        Shell shell = getShell();
        if (shell != null) {
            // Assuming MessageDialog is a utility class for showing dialog boxes
            MessageDialog.openInformation(shell, "Information", "Please wait until the current operation completes before opening the SDK or AVD Manager."); 
        }
    }
}
//<End of snippet n. 0>