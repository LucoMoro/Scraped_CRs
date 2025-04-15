/*Disable PostComp in ADT except on Export or Debug

This patch adds a check in PostCompilerBuilder so if a flag is
not explicitly set (as is done by ExportHelper and Launch) then
it will skip and not do packaging. This should speed up the incremental
build on file save at the cost of slightly longer builds when pushing
to the debugger.

Change-Id:I2993f91a9b53e767e77da82de61b2699dd1127a4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index d89acb5..1b0abda 100644

//Synthetic comment -- @@ -255,6 +255,11 @@
"1.6", //$NON-NLS-1$
};

    /** Flag to pass to PostCompiler builder that sets if it runs or not.
     *  Set this flag in whenever calling build if PostCompiler is to run
     */
    public final static String FULL_BUILD_REQUESTED = "RunPostCompiler"; //$NON-NLS-1$

/** The base URL where to find the Android class & manifest documentation */
public static final String CODESITE_BASE_URL = "http://code.google.com/android";  //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java
//Synthetic comment -- index 10ec15f..cdf48ad 100644

//Synthetic comment -- @@ -26,9 +26,9 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.export.ApkData;
import com.android.sdklib.internal.export.MultiApkExportHelper;
import com.android.sdklib.internal.export.MultiApkExportHelper.ExportException;
import com.android.sdklib.internal.export.MultiApkExportHelper.Target;
import com.android.sdklib.internal.export.ProjectConfig;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;

//Synthetic comment -- @@ -38,7 +38,6 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
//Synthetic comment -- @@ -214,7 +213,7 @@
}

// build the project, mainly for the java compilation. The rest is handled below.
                ProjectHelper.build(project, monitor);

// store the resolved project in the map.
resolvedProjects.put(projectConfig, projectState);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 71c38f8..2e7e824 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.AaptExecException;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.ide.eclipse.adt.internal.build.AaptResultException;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.BuildHelper.ResourceMarker;
import com.android.ide.eclipse.adt.internal.build.DexException;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.build.NativeLibInJarException;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.ApkInstallManager;
//Synthetic comment -- @@ -205,6 +205,11 @@
@Override
protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
throws CoreException {
        // Check to see if a full build was requested. If not, we can skip
        if (!args.containsKey(AdtConstants.FULL_BUILD_REQUESTED)) {
            return null;
        }

// get a project object
IProject project = getProject();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java
//Synthetic comment -- index 6364dcd..54b827f 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.launch;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
//Synthetic comment -- @@ -28,7 +28,6 @@
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
//Synthetic comment -- @@ -140,9 +139,10 @@
return;
}

        // make sure the project is built and PostCompilerBuilder runs.
        // This is a synchronous call which returns when the
// build is done.
        ProjectHelper.build(project, monitor, true);

// check if the project has errors, and abort in this case.
if (ProjectHelper.hasError(project, true)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 3d0e088..0312900 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
//Synthetic comment -- @@ -37,7 +37,6 @@
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
//Synthetic comment -- @@ -84,7 +83,8 @@

// the export, takes the output of the precompiler & Java builders so it's
// important to call build in case the auto-build option of the workspace is disabled.
        // Also enable post compilation
        ProjectHelper.build(project, monitor, true);

// if either key or certificate is null, ensure the other is null.
if (key == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 22af8b0..bc30341 100644

//Synthetic comment -- @@ -16,12 +16,13 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;
import com.android.util.Pair;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -33,6 +34,7 @@
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
//Synthetic comment -- @@ -45,6 +47,8 @@

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
* Utility class to manipulate Project parameters/properties.
//Synthetic comment -- @@ -808,4 +812,40 @@
}
return (IFile) r;
}

    /**
     * Loops through the builders listed in the project's build spec and calls them
     * with a map if the fullBuild flag is set to true.
     * @param project The project to be built.
     * @param monitor A eclipse runtime progress monitor to be updated by the builders.
     * @param fullBuild If set to true a map will be sent to the builders to tell
     *                  the PostCompilerBuilder not to run
     */
    public static void build(IProject project, IProgressMonitor monitor, boolean enablePostC)
                            throws CoreException {
        if (enablePostC) {
            // Create the map to pass to the builders
            Map<String,String> args = new TreeMap<String,String>();
            args.put(AdtConstants.FULL_BUILD_REQUESTED, ""); //$NON-NLS-1$

            // Get the list of builders
            IProjectDescription desc = project.getDescription();
            ICommand[] commands = desc.getBuildSpec();

            // Loop over and execute in order
            for (int i = 0; i < commands.length; ++i) {
                project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
                              commands[i].getBuilderName(), args, monitor);
            }
        } else {
            // If we don't need to pass a map, we'll just do a build
            project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        }
    }

    public static void build(IProject project, IProgressMonitor monitor)
                             throws CoreException {
        // Disable full building by default
        build(project, monitor, false);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/FixImportsJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/FixImportsJob.java
//Synthetic comment -- index ae40790..1adeeaa 100755

//Synthetic comment -- @@ -16,11 +16,12 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.eclipse.adt.internal.project.ProjectHelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
//Synthetic comment -- @@ -80,7 +81,7 @@
if (javaProject == null || !javaProject.isOpen()) {
return Status.CANCEL_STATUS;
}
        ProjectHelper.build(project, monitor);
IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
for (int i = 0; i < markers.length; i++) {
IMarker marker = markers[i];







