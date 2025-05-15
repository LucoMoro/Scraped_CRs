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

/**
* Delegate for the toolbar/menu action "Android AVD Manager".
*/
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    public void dispose() {
        // nothing to dispose.
    }

    public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {
            if (UpdaterWindow.isActive()) {
                // Prevent opening if a progress dialog is active
                return;
            }
            
            // Runs the updater window, directing all logs to the ADT console.
            String sdkLocation = sdk.getSdkLocation();
            if (sdkLocation == null || sdkLocation.isEmpty()) {
                // Handle error scenario 
                AdtPlugin.displayError("Invalid SDK Location", "Please set a valid Android SDK location.");
                return;
            }

            UpdaterWindow window = new UpdaterWindow(
                    getShell(),
                    new AdtConsoleSdkLog(),
                    sdkLocation);
            window.setCancelable(true);
            window.open();
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }

    private Shell getShell() {
        return AdtPlugin.getDisplay().getActiveShell();
    }
}

//<End of snippet n. 0>