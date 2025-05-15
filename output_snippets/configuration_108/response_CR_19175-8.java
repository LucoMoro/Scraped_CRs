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

/**
 * Delegate for the toolbar/menu action "Android AVD Manager".
 */
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    public void dispose() {
        // nothing to dispose.
    }

    public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
        if (sdk == null || !sdk.isSdkLocationValid()) {
            AdtPlugin.displayError("Invalid SDK Location", "Please check the SDK location in the preferences.");
            return;
        }

        Shell activeShell = AdtPlugin.getDisplay().getActiveShell();
        if (activeShell == null) {
            activeShell = Display.getDefault().getActiveShell();
        }

        // Ensure no progress dialog is active before showing the UpdaterWindow
        if (isProgressDialogActive()) {
            return;
        }

        UpdaterWindow window = new UpdaterWindow(activeShell, new AdtConsoleSdkLog(), sdk.getSdkLocation());
        window.open();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }

    private boolean isProgressDialogActive() {
        // Implementation to check if a progress dialog is currently active
        return false; // Placeholder, implement actual check as needed
    }
}

//<End of snippet n. 0>