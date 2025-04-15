/*Cleanup dos2unix in plugins *.java (ADT, GLd, NDK)

Change-Id:Ic54d7e5b77bf6912b54855c5b30d8fcf790ff311*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AvdManagerAction.java
//Synthetic comment -- index dd96078..2c7eafd 100755

//Synthetic comment -- @@ -1,76 +1,76 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.AvdManagerWindow;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Delegate for the toolbar/menu action "AVD Manager".
 * It displays the AVD Manager window.
 */
public class AvdManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    @Override
    public void dispose() {
        // nothing to dispose.
    }

    @Override
    public void init(IWorkbenchWindow window) {
        // no init
    }

    @Override
    public void run(IAction action) {
        final Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {

            // Runs the updater window, directing all logs to the ADT console.

            AvdManagerWindow window = new AvdManagerWindow(
                    AdtPlugin.getDisplay().getActiveShell(),
                    new AdtConsoleSdkLog(),
                    sdk.getSdkLocation(),
                    AvdInvocationContext.IDE);
            window.open();
        } else {
            AdtPlugin.displayError("Android SDK",
                    "Location of the Android SDK has not been setup in the preferences.");
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // nothing related to the current selection.
    }

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index ce9030e..9daa43c 100755

//Synthetic comment -- @@ -1,226 +1,226 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.actions;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;

/**
 * Delegate for the toolbar/menu action "Android SDK Manager".
 * It displays the Android SDK Manager.
 */
public class SdkManagerAction implements IWorkbenchWindowActionDelegate, IObjectActionDelegate {

    @Override
    public void dispose() {
        // nothing to dispose.
    }

    @Override
    public void init(IWorkbenchWindow window) {
        // no init
    }

    @Override
    public void run(IAction action) {
        if (!openAdtSdkManager()) {
            AdtPlugin.displayError(
                    "Android SDK",
                    "Location of the Android SDK has not been setup in the preferences.");
        }
    }

    /**
     * Opens the SDK Manager as an external application.
     * This call is asynchronous, it doesn't wait for the manager to be closed.
     *
     * @return True if the application was found and executed. False if it could not
     *   be located or could not be launched.
     */
    public static boolean openExternalSdkManager() {
        final Sdk sdk = Sdk.getCurrent();
        if (sdk == null) {
            return false;
        }

        File androidBat = FileOp.append(
                sdk.getSdkLocation(),
                SdkConstants.FD_TOOLS,
                SdkConstants.androidCmdName());

        if (!androidBat.exists()) {
            return false;
        }

        try {
            final AdtConsoleSdkLog logger = new AdtConsoleSdkLog();

            String command[] = new String[] {
                    androidBat.getAbsolutePath(),
                    "sdk"   //$NON-NLS-1$
            };
            Process process = Runtime.getRuntime().exec(command);
            GrabProcessOutput.grabProcessOutput(
                    process,
                    Wait.ASYNC,
                    new IProcessOutput() {
                        @Override
                        public void out(@Nullable String line) {
                            // Ignore stdout
                        }

                        @Override
                        public void err(@Nullable String line) {
                            if (line != null) {
                                logger.printf("[SDK Manager] %s", line);
                            }
                        }
                    });
        } catch (Exception ignore) {
        }

        return true;
    }

    /**
     * Opens the SDK Manager bundled within ADT.
     * The call is blocking and does not return till the SD Manager window is closed.
     *
     * @return True if the SDK location is known and the SDK Manager was started.
     *   False if the SDK location is not set and we can't open a SDK Manager to
     *   manage files in an unknown location.
     */
    public static boolean openAdtSdkManager() {
        final Sdk sdk = Sdk.getCurrent();
        if (sdk == null) {
            return false;
        }

        // Runs the updater window, directing only warning/errors logs to the ADT console
        // (normal log is just dropped, which is fine since the SDK Manager has its own
        // log window now.)

        SdkUpdaterWindow window = new SdkUpdaterWindow(
                AdtPlugin.getDisplay().getActiveShell(),
                new AdtConsoleSdkLog() {
                    @Override
                    public void printf(String msgFormat, Object... args) {
                        // Do not show non-error/warning log in Eclipse.
                    };
                },
                sdk.getSdkLocation(),
                SdkInvocationContext.IDE);

        ISdkChangeListener listener = new ISdkChangeListener() {
            @Override
            public void onSdkLoaded() {
                // Ignore initial load of the SDK.
            }

            /**
             * Unload all we can from the SDK before new packages are installed.
             * Typically we need to get rid of references to dx from platform-tools
             * and to any platform resource data.
             * <p/>
             * {@inheritDoc}
             */
            @Override
            public void preInstallHook() {

                // TODO we need to unload as much of as SDK as possible. Otherwise
                // on Windows we end up with Eclipse locking some files and we can't
                // replace them.
                //
                // At this point, we know what the user wants to install so it would be
                // possible to pass in flags to know what needs to be unloaded. Typically
                // we need to:
                // - unload dex if platform-tools is going to be updated. There's a vague
                //   attempt below at removing any references to dex and GCing. Seems
                //   to do the trick.
                // - unload any target that is going to be updated since it may have
                //   resource data used by a current layout editor (e.g. data/*.ttf
                //   and various data/res/*.xml).
                //
                // Most important we need to make sure there isn't a build going on
                // and if there is one, either abort it or wait for it to complete and
                // then we want to make sure we don't get any attempt to use the SDK
                // before the postInstallHook is called.

                if (sdk != null) {
                    sdk.unloadTargetData(true /*preventReload*/);

                    DexWrapper dx = sdk.getDexWrapper();
                    dx.unload();
                }
            }

            /**
             * Nothing to do. We'll reparse the SDK later in onSdkReload.
             * <p/>
             * {@inheritDoc}
             */
            @Override
            public void postInstallHook() {
            }

            /**
             * Reparse the SDK in case anything was add/removed.
             * <p/>
             * {@inheritDoc}
             */
            @Override
            public void onSdkReload() {
                AdtPlugin.getDefault().reparseSdk();
            }
        };

        window.addListener(listener);
        window.open();

        return true;
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // nothing related to the current selection.
    }

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sourcelookup/AdtSourceLookupDirector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sourcelookup/AdtSourceLookupDirector.java
//Synthetic comment -- index e0e236b..bcb9c48 100755

//Synthetic comment -- @@ -1,119 +1,119 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/

package com.android.ide.eclipse.adt.internal.sourcelookup;

import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.DefaultSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.DirectorySourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.ExternalArchiveSourceContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import java.io.File;

public class AdtSourceLookupDirector extends JavaSourceLookupDirector {

    @Override
    public void initializeDefaults(ILaunchConfiguration configuration) throws CoreException {
        dispose();
        setLaunchConfiguration(configuration);
        String projectName =
            configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                ""); //$NON-NLS-1$
        if (projectName != null && projectName.length() > 0) {
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            if (project != null && project.isOpen()) {
                ProjectState state = Sdk.getProjectState(project);
                if (state == null) {
                    initDefaults();
                    return;
                }
                IAndroidTarget target = state.getTarget();
                if (target == null) {
                    initDefaults();
                    return;
                }
                String path = target.getPath(IAndroidTarget.ANDROID_JAR);
                if (path == null) {
                    initDefaults();
                    return;
                }
                IJavaProject javaProject = JavaCore.create(project);
                if (javaProject != null && javaProject.isOpen()) {
                    IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
                    IClasspathEntry androidEntry = null;
                    for (int i = 0; i < entries.length; i++) {
                        IClasspathEntry entry = entries[i];
                        if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY
                                && path.equals(entry.getPath().toString())) {
                            androidEntry = entry;
                            break;
                        }
                    }
                    if (androidEntry != null) {
                        IPath sourceAttachmentPath = androidEntry.getSourceAttachmentPath();
                        if (sourceAttachmentPath != null) {
                            String androidSrc = sourceAttachmentPath.toString();
                            if (androidSrc != null && androidSrc.trim().length() > 0) {
                                File srcFile = new File(androidSrc);
                                ISourceContainer adtContainer = null;
                                if (srcFile.isFile()) {
                                    adtContainer = new ExternalArchiveSourceContainer(androidSrc,
                                            true);
                                }
                                if (srcFile.isDirectory()) {
                                    adtContainer = new DirectorySourceContainer(srcFile, false);
                                }
                                if (adtContainer != null) {
                                    ISourceContainer defaultContainer =
                                        new DefaultSourceContainer();
                                    setSourceContainers(new ISourceContainer[] {
                                            adtContainer, defaultContainer
                                    });
                                    initializeParticipants();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        initDefaults();
    }

    private void initDefaults() {
        setSourceContainers(new ISourceContainer[] {
            new DefaultSourceContainer()
        });
        initializeParticipants();
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/IUpdateWizardDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/IUpdateWizardDialog.java
//Synthetic comment -- index 1aa8c2f..c49b589 100755

//Synthetic comment -- @@ -1,30 +1,30 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.ui;

import org.eclipse.jface.wizard.WizardDialog;


/**
 * An interface that enables a client to update {@link WizardDialog} after its creation.
 */
public interface IUpdateWizardDialog {
    /**
     * Invoked after {@link WizardDialog#create()} to let the caller update the dialog.
     */
    public void updateWizardDialog(WizardDialogEx dialog);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/WizardDialogEx.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/WizardDialogEx.java
//Synthetic comment -- index 22141af..ee1ac97 100755

//Synthetic comment -- @@ -1,46 +1,46 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * A {@link WizardDialog} that gives access to some inner controls.
 */
public final class WizardDialogEx extends WizardDialog {

    /**
     * @see WizardDialog#WizardDialog(Shell, IWizard)
     */
    public WizardDialogEx(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
    }

    /**
     * Returns the cancel button.
     * <p/>
     * Note: there is already a protected, deprecated method that does the same thing.
     * To avoid overriding a deprecated method, the name as be changed to ...Ex.
     */
    public Button getCancelButtonEx() {
        return getButton(IDialogConstants.CANCEL_ID);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gldebugger/GlTracePlugin.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gldebugger/GlTracePlugin.java
//Synthetic comment -- index dafb661..188b480 100644

//Synthetic comment -- @@ -1,130 +1,130 @@
/*
 ** Copyright 2011, The Android Open Source Project
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 **
 **     http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 */

package com.android.ide.eclipse.gldebugger;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GlTracePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "com.android.ide.eclipse.gldebugger"; //$NON-NLS-1$

    // The shared instance
    private static GlTracePlugin plugin;

    private MessageConsole mConsole;
    private MessageConsoleStream mConsoleStream;

    /**
     * The constructor
     */
    public GlTracePlugin() {
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        mConsole = new MessageConsole("OpenGL Trace View", null);
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {
                mConsole });

        mConsoleStream = mConsole.newMessageStream();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static GlTracePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public void logMessage(String message) {
        mConsoleStream.println(message);

        Display.getDefault().asyncExec(sShowConsoleRunnable);
    }

    private static Runnable sShowConsoleRunnable = new Runnable() {
        @Override
        public void run() {
            showConsoleView();
        };
    };

    private static void showConsoleView() {
        IWorkbenchWindow w = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (w != null) {
            IWorkbenchPage page = w.getActivePage();
            if (page != null) {
                try {
                    page.showView(IConsoleConstants.ID_CONSOLE_VIEW, null,
                            IWorkbenchPage.VIEW_VISIBLE);
                } catch (PartInitException e) {
                    // ignore
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/Activator.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/Activator.java
//Synthetic comment -- index c3b2fc4..e165df1 100644

//Synthetic comment -- @@ -1,84 +1,84 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.net.URL;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "com.android.ide.eclipse.ndk"; //$NON-NLS-1$

    // The shared instance
    private static Activator mPlugin;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        mPlugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        mPlugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return mPlugin;
    }

    public static <T> T getService(Class<T> clazz) {
        BundleContext context = mPlugin.getBundle().getBundleContext();
        ServiceReference ref = context.getServiceReference(clazz.getName());
        return (ref != null) ? (T) context.getService(ref) : null;
    }

    public static Bundle getBundle(String id) {
        for (Bundle bundle : mPlugin.getBundle().getBundleContext().getBundles()) {
            if (bundle.getSymbolicName().equals(id)) {
                return bundle;
            }
        }
        return null;
    }

    public static IStatus newStatus(Exception e) {
        return new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e);
    }

    public static void log(Exception e) {
        mPlugin.getLog().log(newStatus(e));
    }

    public static URL findFile(IPath path) {
        return FileLocator.find(mPlugin.getBundle(), path, null);
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkManager.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkManager.java
//Synthetic comment -- index 95739d4..98fccff 100644

//Synthetic comment -- @@ -1,74 +1,74 @@
/*
 * Copyright (C) 2010, 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.TemplateEngine;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

import java.io.File;
import java.util.Map;

public class NdkManager {

    public static final String NDK_LOCATION = "ndkLocation"; //$NON-NLS-1$

    public static final String LIBRARY_NAME = "libraryName"; //$NON-NLS-1$

    public static String getNdkLocation() {
        return Activator.getDefault().getPreferenceStore().getString(NDK_LOCATION);
    }

    public static boolean isNdkLocationValid() {
        String location = getNdkLocation();
        if (location.length() == 0)
            return false;

        return isValidNdkLocation(location);
    }

    public static boolean isValidNdkLocation(String location) {
        File dir = new File(location);
        if (!dir.isDirectory())
            return false;

        // Must contain the ndk-build script which we call to build
        if (!new File(dir, "ndk-build").isFile()) //$NON-NLS-1$
            return false;

        return true;
    }

    public static void addNativeSupport(final IProject project, Map<String, String> templateArgs,
            IProgressMonitor monitor)
            throws CoreException {
        // Launch our template to set up the project contents
        TemplateCore template = TemplateEngine.getDefault().getTemplateById("AddNdkSupport"); //$NON-NLS-1$
        Map<String, String> valueStore = template.getValueStore();
        valueStore.put("projectName", project.getName()); //$NON-NLS-1$
        valueStore.putAll(templateArgs);
        template.executeTemplateProcesses(monitor, false);

        // refresh project resources
        project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 10));
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/build/NdkEnvSupplier.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/build/NdkEnvSupplier.java
//Synthetic comment -- index 7aff75c..6564553 100644

//Synthetic comment -- @@ -1,121 +1,121 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.build;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.envvar.IBuildEnvironmentVariable;
import org.eclipse.cdt.managedbuilder.envvar.IConfigurationEnvironmentVariableSupplier;
import org.eclipse.cdt.managedbuilder.envvar.IEnvironmentVariableProvider;
import org.eclipse.core.runtime.Platform;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NdkEnvSupplier implements IConfigurationEnvironmentVariableSupplier {

    private static Map<String, IBuildEnvironmentVariable> mEnvVars;

    private synchronized void init() {
        if (mEnvVars != null)
            return;

        mEnvVars = new HashMap<String, IBuildEnvironmentVariable>();

        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            // For Windows, need to add a shell to the path
            IBuildEnvironmentVariable path = new IBuildEnvironmentVariable() {
                @Override
                public String getName() {
                    return "PATH"; //$NON-NLS-1$
                }

                @Override
                public String getValue() {
                    // I'm giving MSYS precedence over Cygwin. I'm biased that
                    // way :)
                    // TODO using the default paths for now, need smarter ways
                    // to get at them
                    // Alternatively the user can add the bin to their path
                    // themselves.
                    File bin = new File("C:\\MinGW\\msys\\1.0\\bin"); //$NON-NLS-1$
                    if (bin.isDirectory()) {
                        return bin.getAbsolutePath();
                    }

                    bin = new File("C:\\cygwin\\bin"); //$NON-NLS-1$
                    if (bin.isDirectory())
                        return bin.getAbsolutePath();

                    return null;
                }

                @Override
                public int getOperation() {
                    return ENVVAR_PREPEND;
                }

                @Override
                public String getDelimiter() {
                    return ";"; //$NON-NLS-1$
                }
            };
            if (path.getValue() != null)
                mEnvVars.put(path.getName(), path);

            // Since we're using real paths, need to tell cygwin it's OK
            IBuildEnvironmentVariable cygwin = new IBuildEnvironmentVariable() {
                @Override
                public String getName() {
                    return "CYGWIN"; //$NON-NLS-1$
                }

                @Override
                public String getValue() {
                    return "nodosfilewarning"; //$NON-NLS-1$
                }

                @Override
                public int getOperation() {
                    return ENVVAR_REPLACE;
                }

                @Override
                public String getDelimiter() {
                    return null;
                }
            };

            mEnvVars.put(cygwin.getName(), cygwin);
        }
    }

    @Override
    public IBuildEnvironmentVariable getVariable(String variableName,
            IConfiguration configuration, IEnvironmentVariableProvider provider) {
        init();
        return mEnvVars.get(variableName);
    }

    @Override
    public IBuildEnvironmentVariable[] getVariables(
            IConfiguration configuration, IEnvironmentVariableProvider provider) {
        init();
        return mEnvVars.values().toArray(new IBuildEnvironmentVariable[mEnvVars.size()]);
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/discovery/NdkDiscoveredPathInfo.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/discovery/NdkDiscoveredPathInfo.java
//Synthetic comment -- index 576668f..83ce7f4 100644

//Synthetic comment -- @@ -1,203 +1,203 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.discovery;

import com.android.ide.eclipse.ndk.internal.Activator;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.make.core.scannerconfig.IDiscoveredPathManager.IDiscoveredPathInfo;
import org.eclipse.cdt.make.core.scannerconfig.IDiscoveredPathManager.IDiscoveredScannerInfoSerializable;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NdkDiscoveredPathInfo implements IDiscoveredPathInfo {

    private final IProject mProject;
    private long mLastUpdate = IFile.NULL_STAMP;
    private IPath[] mIncludePaths;
    private Map<String, String> mSymbols;
    private boolean mNeedReindexing = false;
    private static final IPath ANDROID_MK = new Path("jni/Android.mk");

    // Keys for preferences
    public static final String LAST_UPDATE = "lastUpdate"; //$NON-NLS-1$

    public NdkDiscoveredPathInfo(IProject project) {
        this.mProject = project;
        load();
    }

    @Override
    public IProject getProject() {
        return mProject;
    }

    @Override
    public IPath[] getIncludePaths() {
        if (mNeedReindexing) {
            // Call for a reindex
            // TODO this is probably a bug. a new include path should trigger
            // reindexing anyway, no?
            // BTW, can't do this in the update since the indexer runs before
            // this gets called
            CCorePlugin.getIndexManager().reindex(CoreModel.getDefault().create(mProject));
            mNeedReindexing = false;
        }
        return mIncludePaths;
    }

    void setIncludePaths(List<String> pathStrings) {
        mIncludePaths = new IPath[pathStrings.size()];
        int i = 0;
        for (String path : pathStrings)
            mIncludePaths[i++] = new Path(path);
        mNeedReindexing = true;
    }

    @Override
    public Map<String, String> getSymbols() {
        if (mSymbols == null)
            mSymbols = new HashMap<String, String>();
        return mSymbols;
    }

    void setSymbols(Map<String, String> symbols) {
        this.mSymbols = symbols;
    }

    @Override
    public IDiscoveredScannerInfoSerializable getSerializable() {
        return null;
    }

    public void update(IProgressMonitor monitor) throws CoreException {
        if (!needUpdating())
            return;

        new NdkDiscoveryUpdater(this).runUpdate(monitor);

        if (mIncludePaths != null && mSymbols != null) {
            recordUpdate();
            save();
        }
    }

    private boolean needUpdating() {
        if (mLastUpdate == IFile.NULL_STAMP)
            return true;
        return mProject.getFile(ANDROID_MK).getLocalTimeStamp() > mLastUpdate;
    }

    private void recordUpdate() {
        mLastUpdate = mProject.getFile(ANDROID_MK).getLocalTimeStamp();
    }

    public void delete() {
        mLastUpdate = IFile.NULL_STAMP;
    }

    private File getInfoFile() {
        File stateLoc = Activator.getDefault().getStateLocation().toFile();
        return new File(stateLoc, mProject.getName() + ".pathInfo"); //$NON-NLS-1$
    }

    private void save() {
        try {
            File infoFile = getInfoFile();
            infoFile.getParentFile().mkdirs();
            PrintStream out = new PrintStream(infoFile);

            // timestamp
            out.print("t,"); //$NON-NLS-1$
            out.print(mLastUpdate);
            out.println();

            for (IPath include : mIncludePaths) {
                out.print("i,"); //$NON-NLS-1$
                out.print(include.toPortableString());
                out.println();
            }

            for (Entry<String, String> symbol : mSymbols.entrySet()) {
                out.print("d,"); //$NON-NLS-1$
                out.print(symbol.getKey());
                out.print(","); //$NON-NLS-1$
                out.print(symbol.getValue());
                out.println();
            }

            out.close();
        } catch (IOException e) {
            Activator.log(e);
        }

    }

    private void load() {
        try {
            File infoFile = getInfoFile();
            if (!infoFile.exists())
                return;

            long timestamp = IFile.NULL_STAMP;
            List<IPath> includes = new ArrayList<IPath>();
            Map<String, String> defines = new HashMap<String, String>();

            BufferedReader reader = new BufferedReader(new FileReader(infoFile));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                switch (line.charAt(0)) {
                    case 't':
                        timestamp = Long.valueOf(line.substring(2));
                        break;
                    case 'i':
                        includes.add(Path.fromPortableString(line.substring(2)));
                        break;
                    case 'd':
                        int n = line.indexOf(',', 2);
                        if (n == -1)
                            defines.put(line.substring(2), ""); //$NON-NLS-1$
                        else
                            defines.put(line.substring(2, n), line.substring(n + 1));
                        break;
                }
            }
            reader.close();

            mLastUpdate = timestamp;
            mIncludePaths = includes.toArray(new IPath[includes.size()]);
            mSymbols = defines;
        } catch (IOException e) {
            Activator.log(e);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/discovery/NdkScannerInfoCollector.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/discovery/NdkScannerInfoCollector.java
//Synthetic comment -- index 14daa25..29f3e7f 100644

//Synthetic comment -- @@ -1,103 +1,103 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.discovery;

import org.eclipse.cdt.make.core.scannerconfig.IDiscoveredPathManager.IDiscoveredPathInfo;
import org.eclipse.cdt.make.core.scannerconfig.IScannerInfoCollector3;
import org.eclipse.cdt.make.core.scannerconfig.IScannerInfoCollectorCleaner;
import org.eclipse.cdt.make.core.scannerconfig.InfoContext;
import org.eclipse.cdt.make.core.scannerconfig.ScannerInfoTypes;
import org.eclipse.cdt.managedbuilder.scannerconfig.IManagedScannerInfoCollector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import java.util.List;
import java.util.Map;

public class NdkScannerInfoCollector implements IScannerInfoCollector3,
        IScannerInfoCollectorCleaner, IManagedScannerInfoCollector {

    private NdkDiscoveredPathInfo mPathInfo;

    @Override
    public void contributeToScannerConfig(Object resource, Map scannerInfo) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public List getCollectedScannerInfo(Object resource, ScannerInfoTypes type) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void setProject(IProject project) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void updateScannerConfiguration(IProgressMonitor monitor) throws CoreException {
        mPathInfo.update(monitor);
    }

    @Override
    public IDiscoveredPathInfo createPathInfoObject() {
        return mPathInfo;
    }

    @Override
    public Map<String, String> getDefinedSymbols() {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public List getIncludePaths() {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void setInfoContext(InfoContext context) {
        mPathInfo = new NdkDiscoveredPathInfo(context.getProject());
    }

    @Override
    public void deleteAllPaths(IResource resource) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void deleteAllSymbols(IResource resource) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void deletePath(IResource resource, String path) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void deleteSymbol(IResource resource, String symbol) {
        throw new Error("Not implemented"); //$NON-NLS-1$
    }

    @Override
    public void deleteAll(IResource resource) {
        mPathInfo.delete();
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/templates/SetFolders.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/templates/SetFolders.java
//Synthetic comment -- index a7b762d..2e8f714 100644

//Synthetic comment -- @@ -1,105 +1,105 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.templates;

import com.android.ide.eclipse.ndk.internal.Messages;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.IPathEntry;
import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import java.util.ArrayList;
import java.util.List;

public class SetFolders extends ProcessRunner {

    @Override
    public void process(TemplateCore template, ProcessArgument[] args, String processId,
            IProgressMonitor monitor)
            throws ProcessFailureException {
        String projectName = null;
        String[] sourceFolders = null;
        String[] outputFolders = null;

        for (ProcessArgument arg : args) {
            String argName = arg.getName();
            if (argName.equals("projectName")) { //$NON-NLS-1$
                projectName = arg.getSimpleValue();
            } else if (argName.equals("sourceFolders")) { //$NON-NLS-1$
                sourceFolders = arg.getSimpleArrayValue();
            } else if (argName.equals("outputFolders")) { //$NON-NLS-1$
                outputFolders = arg.getSimpleArrayValue();
            }
        }

        // Get the project
        if (projectName == null)
            throw new ProcessFailureException(Messages.SetFolders_Missing_project_name);

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        if (!project.exists())
            throw new ProcessFailureException(Messages.SetFolders_Project_does_not_exist);

        // Create the folders
        if (sourceFolders == null && outputFolders == null)
            throw new ProcessFailureException(Messages.SetFolders_No_folders);

        try {
            // Add them in
            ICProject cproject = CCorePlugin.getDefault().getCoreModel().create(project);
            IPathEntry[] pathEntries = cproject.getRawPathEntries();
            List<IPathEntry> newEntries = new ArrayList<IPathEntry>(pathEntries.length);
            for (IPathEntry pathEntry : pathEntries) {
                // remove the old source and output entries
                if (pathEntry.getEntryKind() != IPathEntry.CDT_SOURCE
                        && pathEntry.getEntryKind() != IPathEntry.CDT_OUTPUT) {
                    newEntries.add(pathEntry);
                }
            }
            if (sourceFolders != null)
                for (String sourceFolder : sourceFolders) {
                    IFolder folder = project.getFolder(new Path(sourceFolder));
                    if (!folder.exists())
                        folder.create(true, true, monitor);
                    newEntries.add(CoreModel.newSourceEntry(folder.getFullPath()));
                }
            if (outputFolders != null)
                for (String outputFolder : outputFolders) {
                    IFolder folder = project.getFolder(new Path(outputFolder));
                    if (!folder.exists())
                        folder.create(true, true, monitor);
                    newEntries.add(CoreModel.newOutputEntry(folder.getFullPath()));
                }
            cproject.setRawPathEntries(newEntries.toArray(new IPathEntry[newEntries.size()]),
                    monitor);
        } catch (CoreException e) {
            throw new ProcessFailureException(e);
        }
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/templates/SimpleFile.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/templates/SimpleFile.java
//Synthetic comment -- index f42653c..7f249ca 100644

//Synthetic comment -- @@ -1,125 +1,125 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.templates;

import com.android.ide.eclipse.ndk.internal.Activator;
import com.android.ide.eclipse.ndk.internal.Messages;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SimpleFile extends ProcessRunner {

    private static final class FileOp {
        public String source;
        public String destination;
    }

    @Override
    public void process(TemplateCore template, ProcessArgument[] args, String processId,
            IProgressMonitor monitor)
            throws ProcessFailureException {

        // Fetch the args
        String projectName = null;
        List<FileOp> fileOps = new ArrayList<FileOp>();

        for (ProcessArgument arg : args) {
            if (arg.getName().equals("projectName")) //$NON-NLS-1$
                projectName = arg.getSimpleValue();
            else if (arg.getName().equals("files")) { //$NON-NLS-1$
                ProcessArgument[][] files = arg.getComplexArrayValue();
                for (ProcessArgument[] file : files) {
                    FileOp op = new FileOp();
                    for (ProcessArgument fileArg : file) {
                        if (fileArg.getName().equals("source")) //$NON-NLS-1$
                            op.source = fileArg.getSimpleValue();
                        else if (fileArg.getName().equals("destination")) //$NON-NLS-1$
                            op.destination = fileArg.getSimpleValue();
                    }
                    if (op.source == null || op.destination == null)
                        throw new ProcessFailureException(Messages.SimpleFile_Bad_file_operation);
                    fileOps.add(op);
                }
            }
        }

        if (projectName == null)
            throw new ProcessFailureException(Messages.SimpleFile_No_project_name);
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        if (!project.exists())
            throw new ProcessFailureException(Messages.SimpleFile_Project_does_not_exist);

        // Find bundle to find source files
        Bundle bundle = Activator.getBundle(template.getTemplateInfo().getPluginId());
        if (bundle == null)
            throw new ProcessFailureException(Messages.SimpleFile_Bundle_not_found);

        try {
            for (FileOp op : fileOps) {
                IFile destFile = project.getFile(new Path(op.destination));
                if (destFile.exists())
                    // don't overwrite files if they exist already
                    continue;

                // Make sure parent folders are created
                mkDirs(project, destFile.getParent(), monitor);

                URL sourceURL = FileLocator.find(bundle, new Path(op.source), null);
                if (sourceURL == null)
                    throw new ProcessFailureException(Messages.SimpleFile_Could_not_fine_source
                            + op.source);

                TemplatedInputStream in = new TemplatedInputStream(sourceURL.openStream(),
                        template.getValueStore());
                destFile.create(in, true, monitor);
                in.close();
            }
        } catch (IOException e) {
            throw new ProcessFailureException(e);
        } catch (CoreException e) {
            throw new ProcessFailureException(e);
        }

    }

    private void mkDirs(IProject project, IContainer container, IProgressMonitor monitor)
            throws CoreException {
        if (container.exists())
            return;
        mkDirs(project, container.getParent(), monitor);
        ((IFolder) container).create(true, true, monitor);
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/templates/TemplatedInputStream.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/templates/TemplatedInputStream.java
//Synthetic comment -- index 3e3d28a..129caa3 100644

//Synthetic comment -- @@ -1,87 +1,87 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Reads from a template substituting marked values from the supplied Map.
 */
public class TemplatedInputStream extends InputStream {

    private final InputStream mIn;
    private final Map<String, String> mMap;
    private char[] mSub;
    private int mPos;
    private int mMark;

    public TemplatedInputStream(InputStream in, Map<String, String> map) {
        this.mIn = in;
        this.mMap = map;
    }

    @Override
    public int read() throws IOException {
        // if from a mark, return the char
        if (mMark != 0) {
            int c = mMark;
            mMark = 0;
            return c;
        }

        // return char from sub layer if available
        if (mSub != null) {
            char c = mSub[mPos++];
            if (mPos >= mSub.length)
                mSub = null;
            return c;
        }

        int c = mIn.read();
        if (c == '%') {
            // check if it's a sub
            c = mIn.read();
            if (c == '{') {
                // it's a sub
                StringBuffer buff = new StringBuffer();
                for (c = mIn.read(); c != '}' && c >= 0; c = mIn.read())
                    buff.append((char) c);
                String str = mMap.get(buff.toString());
                if (str != null) {
                    mSub = str.toCharArray();
                    mPos = 0;
                }
                return read(); // recurse to get the real char
            } else {
                // not a sub
                mMark = c;
                return '%';
            }
        }

        return c;
    }

    @Override
    public void close() throws IOException {
        super.close();
        mIn.close();
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizard.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizard.java
//Synthetic comment -- index 0f6ac71..a79eb31 100644

//Synthetic comment -- @@ -1,109 +1,109 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.wizards;

import com.android.ide.eclipse.ndk.internal.Activator;
import com.android.ide.eclipse.ndk.internal.NdkManager;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AddNativeWizard extends Wizard {

    private final IProject mProject;
    private final IWorkbenchWindow mWindow;

    private AddNativeWizardPage mAddNativeWizardPage;
    private Map<String, String> mTemplateArgs = new HashMap<String, String>();

    public AddNativeWizard(IProject project, IWorkbenchWindow window) {
        mProject = project;
        mWindow = window;
        mTemplateArgs.put(NdkManager.LIBRARY_NAME, project.getName());
    }

    @Override
    public void addPages() {
        mAddNativeWizardPage = new AddNativeWizardPage(mTemplateArgs);
        addPage(mAddNativeWizardPage);
    }

    @Override
    public boolean performFinish() {
        // Switch to C/C++ Perspective
        try {
            mWindow.getWorkbench().showPerspective(CUIPlugin.ID_CPERSPECTIVE, mWindow);
        } catch (WorkbenchException e1) {
            Activator.log(e1);
        }

        mAddNativeWizardPage.updateArgs(mTemplateArgs);

        IRunnableWithProgress op = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException,
                    InterruptedException {
                IWorkspaceRunnable op1 = new IWorkspaceRunnable() {
                    @Override
                    public void run(IProgressMonitor monitor1) throws CoreException {
                        // Convert to CDT project
                        CCorePlugin.getDefault().convertProjectToCC(mProject, monitor1,
                                MakeCorePlugin.MAKE_PROJECT_ID);
                        // Set up build information
                        new NdkWizardHandler().convertProject(mProject, monitor1);
                        // Run the template
                        NdkManager.addNativeSupport(mProject, mTemplateArgs, monitor1);
                    }
                };
                // TODO run from a job
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                try {
                    workspace.run(op1, workspace.getRoot(), 0, new NullProgressMonitor());
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                }
            }
        };
        try {
            getContainer().run(false, true, op);
            return true;
        } catch (InterruptedException e) {
            Activator.log(e);
            return false;
        } catch (InvocationTargetException e) {
            Activator.log(e);
            return false;
        }
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizardPage.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizardPage.java
//Synthetic comment -- index 79ae2f2..65af270 100644

//Synthetic comment -- @@ -1,82 +1,82 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.wizards;

import com.android.ide.eclipse.ndk.internal.Messages;
import com.android.ide.eclipse.ndk.internal.NdkManager;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.Map;

public class AddNativeWizardPage extends WizardPage {

    private final String defaultLibraryName;

    private Text libraryNameText;

    public AddNativeWizardPage(Map<String, String> templateArgs) {
        super("addNativeWizardPage"); //$NON-NLS-1$
        setDescription(Messages.AddNativeWizardPage_Description);
        setTitle(Messages.AddNativeWizardPage_Title);

        defaultLibraryName = templateArgs.get(NdkManager.LIBRARY_NAME);
        if (!NdkManager.isNdkLocationValid()) {
            setErrorMessage(Messages.AddNativeWizardPage_Location_not_valid);
        }
    }

    @Override
    public boolean isPageComplete() {
        return NdkManager.isNdkLocationValid();
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(2, false));

        Label lblLibraryName = new Label(container, SWT.NONE);
        lblLibraryName.setText(Messages.AddNativeWizardPage_LibraryName);

        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        composite.setLayout(new GridLayout(3, false));

        Label lblLib = new Label(composite, SWT.NONE);
        lblLib.setText("lib"); //$NON-NLS-1$

        libraryNameText = new Text(composite, SWT.BORDER);
        libraryNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        libraryNameText.setText(defaultLibraryName);

        Label lblso = new Label(composite, SWT.NONE);
        lblso.setText(".so"); //$NON-NLS-1$
    }

    public void updateArgs(Map<String, String> templateArgs) {
        templateArgs.put(NdkManager.LIBRARY_NAME, libraryNameText.getText());
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/NdkWizardHandler.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/NdkWizardHandler.java
//Synthetic comment -- index ccef732..fa0b92b 100644

//Synthetic comment -- @@ -1,41 +1,41 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.wizards;

import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.ui.wizards.STDWizardHandler;

public class NdkWizardHandler extends STDWizardHandler {

    public NdkWizardHandler() {
        super(null, null);
    }

    @Override
    public IToolChain[] getSelectedToolChains() {
        IToolChain[] tcs = ManagedBuildManager.getRealToolChains();
        for (IToolChain tc : tcs) {
            if (tc.getId().equals("com.android.toolchain.gcc")) //$NON-NLS-1$
                return new IToolChain[] {
                    tc
                };
        }
        return super.getSelectedToolChains();
    }

}







