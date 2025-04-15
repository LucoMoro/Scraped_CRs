/*Revert "Fixinghttp://code.google.com/p/android/issues/detail?id=11503."

This reverts commit 3f1697067c8e69d456a69367d434579f3b96e8e4.

Change-Id:Id3e3052e456ef708ff9b42d8466169081016cea5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index 931cd7a..42739e2 100755

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
*
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -17,29 +17,18 @@
package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtPlugin.CheckSdkErrorHandler;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
* Delegate for the toolbar/menu action "Android AVD Manager".
//Synthetic comment -- @@ -47,10 +36,6 @@
*/
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    private boolean mFinished;

    private Shell mShell;

public void dispose() {
// nothing to dispose.
}
//Synthetic comment -- @@ -60,13 +45,13 @@
}

public void run(IAction action) {
        Sdk sdk = getSdk();
if (sdk != null) {

// Runs the updater window, directing all logs to the ADT console.

UpdaterWindow window = new UpdaterWindow(
                    getShell(),
new AdtConsoleSdkLog(),
sdk.getSdkLocation());

//Synthetic comment -- @@ -143,88 +128,4 @@
public void setActivePart(IAction action, IWorkbenchPart targetPart) {
// nothing to do.
}

    private Shell getShell() {

        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                mShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            }
        });
        return mShell;
    }

    private Sdk getSdk() {
        String sdkLocation = AdtPrefs.getPrefs().getOsSdkFolder();
        if (sdkLocation == null || sdkLocation.length() == 0) {
            return null;
        }
        boolean isValid = AdtPlugin.getDefault().checkSdkLocationAndId(sdkLocation,
                new CheckSdkErrorHandler() {

                    @Override
                    public boolean handleWarning(String message) {
                        return true;
                    }

                    @Override
                    public boolean handleError(String message) {
                        return false;
                    }
                });
        if (isValid) {
            return waitForSDK();
        }
        return null;
    }

    private Sdk waitForSDK() {
        final Job waitJob = new Job("Android SDK") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                monitor.beginTask("Please wait until SDK is loading ...", 10);

                while (Sdk.getCurrent() == null) {
                    if (monitor.isCanceled()) {
                        monitor.done();
                        return Status.CANCEL_STATUS;
                    }
                    try {
                        Thread.sleep(250);
                        monitor.worked(1);
                    } catch (InterruptedException e) {
                        monitor.done();
                        return Status.CANCEL_STATUS;
                    }
                }
                monitor.done();
                return Status.OK_STATUS;
            }

        };
        waitJob.setUser(true);
        waitJob.setPriority(Job.SHORT);
        mFinished = false;
        waitJob.addJobChangeListener(new JobChangeAdapter() {

            public void done(IJobChangeEvent event) {
                mFinished = true;
            }
        });
        waitJob.schedule();
        while (!mFinished) {
            Display display = Display.getCurrent();
            if (!display.readAndDispatch()) {
                display.sleep();
            }
            // don't join a waiting or sleeping job when suspended (deadlock risk)
            if (Job.getJobManager().isSuspended() && waitJob.getState() != Job.RUNNING) {
                break;
            }
        }
        return Sdk.getCurrent();
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index da3a550..d7c4048 100755

//Synthetic comment -- @@ -325,7 +325,7 @@
setMessage(null);
setControl(scrolledComposite);

        Control workingSetControl = mWorkingSetGroup.createControl(composite);;
workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Validate. This will complain about the first empty field.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java
//Synthetic comment -- index 790d8d8..33ed218 100644

//Synthetic comment -- @@ -30,6 +30,9 @@
/**
* Copied from
* org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne$WorkingSetGroup
*/
public class WorkingSetGroup {








