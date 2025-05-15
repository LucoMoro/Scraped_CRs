//<Beginning of snippet n. 0>


/*
 * Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Display;

/**
* Delegate for the toolbar/menu action "Android AVD Manager".
*/
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    private ProgressMonitorDialog progressDialog;

    public void dispose() {
        // nothing to dispose.
    }

    public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {
            Shell activeShell = AdtPlugin.getDisplay().getActiveShell();

            // Check if the progress dialog is not active
            if (progressDialog == null || progressDialog.isCanceled()) {
                progressDialog = new ProgressMonitorDialog(activeShell);
                progressDialog.setCancelable(true);

                try {
                    progressDialog.run(true, true, monitor -> {
                        // Runs the updater window, directing all logs to the ADT console.
                        UpdaterWindow window = new UpdaterWindow(
                            activeShell,
                            new AdtConsoleSdkLog(),
                            sdk.getSdkLocation()
                        );

                        monitor.beginTask("Opening SDK and AVD Manager...", IProgressMonitor.UNKNOWN);
                        window.open();
                        monitor.done();
                    });
                } catch (Exception e) {
                    // Error handling for invalid SDK location
                    // Inform the user without disrupting the workflow
                    Display.getDefault().asyncExec(() -> {
                        // Show error message dialog
                    });
                } finally {
                    if (progressDialog != null && !progressDialog.getShell().isDisposed()) {
                        progressDialog.close();
                    }
                }
            }
        } else {
            // Handle case where sdk is null
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}

//<End of snippet n. 0>