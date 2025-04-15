/*Revert "Fixinghttp://code.google.com/p/android/issues/detail?id=11503."

This reverts commit 3f1697067c8e69d456a69367d434579f3b96e8e4.

Change-Id:Id3e3052e456ef708ff9b42d8466169081016cea5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index 931cd7a..42739e2 100755

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -17,29 +17,18 @@
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
//Synthetic comment -- @@ -47,10 +36,6 @@
*/
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

public void dispose() {
// nothing to dispose.
}
//Synthetic comment -- @@ -60,13 +45,13 @@
}

public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
if (sdk != null) {

// Runs the updater window, directing all logs to the ADT console.

UpdaterWindow window = new UpdaterWindow(
                    AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
sdk.getSdkLocation());

//Synthetic comment -- @@ -143,88 +128,4 @@
public void setActivePart(IAction action, IWorkbenchPart targetPart) {
// nothing to do.
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index da3a550..d7c4048 100755

//Synthetic comment -- @@ -325,7 +325,7 @@
setMessage(null);
setControl(scrolledComposite);

        Control workingSetControl = mWorkingSetGroup.createControl(composite);
workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Validate. This will complain about the first empty field.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/WorkingSetGroup.java
//Synthetic comment -- index 790d8d8..33ed218 100644

//Synthetic comment -- @@ -30,6 +30,9 @@
/**
* Copied from
* org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne$WorkingSetGroup
 *
 * Creates the working set group with controls that allow
 * the selection of working sets
*/
public class WorkingSetGroup {








