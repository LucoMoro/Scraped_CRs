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
            IWorkbenchWindow window = AdtPlugin.getActiveWorkbenchWindow();
            if (window != null && window.getShell() != null) {
                if (!UpdaterWindow.isActive() && !ProgressDialog.isActive()) {
                    UpdaterWindow updaterWindow = new UpdaterWindow(
                            getShell(),
                            new AdtConsoleSdkLog(),
                            sdk.getSdkLocation());
                    updaterWindow.addCancelListener(e -> {
                        if (updaterWindow.isActive()) {
                            updaterWindow.close();
                        }
                    });
                    updaterWindow.open();
                }
            }
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }

    private Shell getShell() {
        return AdtPlugin.getActiveWorkbenchWindow().getShell();
    }

//<End of snippet n. 0>