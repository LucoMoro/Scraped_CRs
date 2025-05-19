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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

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
            if (!isProgressDialogActive()) {
                UpdaterWindow window = new UpdaterWindow(
                        getShell(),
                        new AdtConsoleSdkLog(),
                        sdk.getSdkLocation());
                window.open(); 
            } else {
                notifyUserOfActiveProgressDialog();
            }
        } else {
            String errorMessage = sdk == null ? 
                "The SDK location is invalid or not set." : 
                "The SDK location is invalid.";
            MessageDialog.openError(getShell(), "SDK Location Error", errorMessage);
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }

    private boolean isProgressDialogActive() {
        return ProgressDialog.isActive();
    }

    private Shell getShell() {
        for (IWorkbenchWindow window : AdtPlugin.getWorkbench().getWorkbenchWindows()) {
            if (window.getShell() != null && window.getShell().isVisible()) {
                return window.getShell();
            }
        }
        return null;
    }

    private void notifyUserOfActiveProgressDialog() {
        MessageDialog.openInformation(getShell(), "Information", "Cannot open SDK and AVD Manager while a progress dialog is active.");
    }
}

//<End of snippet n. 0>