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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    public void dispose() {
        // nothing to dispose.
    }
    
    public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
        if (sdk == null || !sdk.isValid()) {
            AdtPlugin.printErrorToConsole("Invalid or unset SDK location.");
            return;
        }

        Shell shell = getShell();
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
        dialog.create();
        dialog.setCancelable(true);
        dialog.setMessage("Please wait until SDK is loading ...");
        dialog.open();

        UpdaterWindow window = new UpdaterWindow(
                shell,
                new AdtConsoleSdkLog(),
                sdk.getSdkLocation());

        // Trigger the loading operation here
        window.loadSdk();
        
        dialog.close();
    }

    private Shell getShell() {
        return AdtPlugin.getActiveShell();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}
//<End of snippet n. 0>