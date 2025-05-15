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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;

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
                // Display error message for invalid SDK location
                System.err.println("Invalid Android SDK location.");
                return;
            }

            // Manage progress dialog
            if (progressDialog == null || progressDialog.getShell().isDisposed()) {
                progressDialog = new ProgressMonitorDialog(getShell());  // Ensure correct shell context
                progressDialog.setCancelable(true);  // Make the dialog cancelable
                progressDialog.open();

                // Logic to load SDK or AVD Manager
                try {
                    // Add code to simulate SDK loading
                    // Example: loadSdk(sdkLocation);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!progressDialog.getShell().isDisposed()) {
                        progressDialog.close(); // Ensure the dialog is closed properly
                    }
                }

                // Open Updater Window post SDK loading
                UpdaterWindow window = new UpdaterWindow(
                        getShell(),
                        new AdtConsoleSdkLog(),
                        sdkLocation);
                window.open();
                
            } else {
                // Handle the case where the progress dialog is already active
                System.err.println("An SDK loading operation is already in progress.");
            }
        } 
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}

//<End of snippet n. 0>