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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;

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
            String sdkLocation = sdk.getSdkLocation();
            if (sdkLocation == null || sdkLocation.isEmpty()) {
                MessageBox messageBox = new MessageBox(AdtPlugin.getDisplay().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
                messageBox.setMessage("SDK location is invalid or not set.");
                messageBox.open();
                return;
            }

            Shell shell = AdtPlugin.getDisplay().getActiveShell();
            if (progressDialog != null && progressDialog.getShell() != null && !progressDialog.getShell().isDisposed()) {
                return; // Prevent access if a progress dialog is active
            }

            progressDialog = new ProgressMonitorDialog(shell);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Please wait until SDK is loading ...");

            try {
                progressDialog.run(true, true, monitor -> {
                    UpdaterWindow window = new UpdaterWindow(shell, new AdtConsoleSdkLog(), sdkLocation);
                    window.open();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}

//<End of snippet n. 0>