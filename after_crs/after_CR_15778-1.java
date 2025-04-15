/*Force recompilation after library changes and fix some clean-up/refresh issue in the builders

Also move ProjectState into SDK as it's really only managed
by the SDK and it allows us to move its constructor to package private.

Change-Id:I321ac23f756652e7082297d2ef560a654e97cf67*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/MultiApkExportAction.java
//Synthetic comment -- index f5876e9..6ff6d0b 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.PostCompilerHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.sdklib.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BaseBuilder.java
//Synthetic comment -- index 32092f8..32bfcee 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.xml.sax.SAXException;

//Synthetic comment -- @@ -423,19 +422,35 @@
}

/**
     * Recursively delete all the derived resources from a root resource. The root resource is not
     * deleted.
     * @param rootResource the root resource
     * @param monitor a progress monitor.
     * @throws CoreException
     *
*/
    protected void removeDerivedResources(IResource rootResource, IProgressMonitor monitor)
throws CoreException {
        removeDerivedResources(rootResource, false, monitor);
        //rootResource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
    }

    private void removeDerivedResources(IResource rootResource, boolean deleteRoot,
            IProgressMonitor monitor)
            throws CoreException {
        if (rootResource.exists()) {
            if (rootResource.getType() == IResource.FOLDER) {
                IFolder folder = (IFolder)rootResource;
IResource[] members = folder.members();
for (IResource member : members) {
                    removeDerivedResources(member, true /*deleteRoot*/, monitor);
}
            } else if (rootResource.isDerived()) {
                rootResource.getLocation().toFile().delete();
            }

            if (deleteRoot) {
                rootResource.getLocation().toFile().delete();
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PostCompilerBuilder.java
//Synthetic comment -- index 539c8e3..8c8a15d 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.ide.eclipse.adt.internal.project.ApkInstallManager;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index e3a6715..0bd1789 100644

//Synthetic comment -- @@ -24,8 +24,8 @@
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.FixLaunchConfig;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.project.XmlErrorHandler.BasicXmlErrorListener;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
//Synthetic comment -- @@ -47,7 +47,6 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -252,6 +251,10 @@
if (kind == FULL_BUILD) {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
Messages.Start_Full_Pre_Compiler);

                // do some clean up.
                doClean(project, monitor);

mMustCompileResources = true;
buildAidlCompilationList(project, sourceFolderPathList);
} else {
//Synthetic comment -- @@ -478,15 +481,16 @@
flc.start();
}

// record the new manifest package, and save it.
mManifestPackage = javaPackage;
saveProjectStringProperty(PROPERTY_PACKAGE, mManifestPackage);

                // force a clean
                doClean(project, monitor);
                mMustCompileResources = true;
                buildAidlCompilationList(project, sourceFolderPathList);

                saveProjectBooleanProperty(PROPERTY_COMPILE_RESOURCES , mMustCompileResources);
}

if (mMustCompileResources) {
//Synthetic comment -- @@ -513,19 +517,26 @@
protected void clean(IProgressMonitor monitor) throws CoreException {
super.clean(monitor);

        doClean(getProject(), monitor);
        if (mGenFolder != null) {
            mGenFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
        }
    }

    private void doClean(IProject project, IProgressMonitor monitor) throws CoreException {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
Messages.Removing_Generated_Classes);

// remove all the derived resources from the 'gen' source folder.
        if (mGenFolder != null) {
            removeDerivedResources(mGenFolder, monitor);
        }

// Clear the project of the generic markers
removeMarkersFromProject(project, AndroidConstants.MARKER_AAPT_COMPILE);
removeMarkersFromProject(project, AndroidConstants.MARKER_XML);
removeMarkersFromProject(project, AndroidConstants.MARKER_AIDL);

}

@Override
//Synthetic comment -- @@ -662,7 +673,7 @@
// we actually need to delete the manifest.java as it may become empty and
// in this case aapt doesn't generate an empty one, but instead doesn't
// touch it.
        manifestJavaFile.getLocation().toFile().delete();

// launch aapt: create the command line
ArrayList<String> array = new ArrayList<String>();
//Synthetic comment -- @@ -784,40 +795,6 @@
}

/**
* Creates a relative {@link IPath} from a java package.
* @param javaPackageName the java package.
*/
//Synthetic comment -- @@ -951,10 +928,7 @@
if (javaFile.exists()) {
// This confirms the java file was generated by the builder,
// we can delete the aidlFile.
                javaFile.getLocation().toFile().delete();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ResourceManagerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ResourceManagerBuilder.java
//Synthetic comment -- index fe6ac8a..871b4b8 100644

//Synthetic comment -- @@ -198,7 +198,7 @@
javaProject.setRawClasspath(entries, new SubProgressMonitor(monitor, 10));
}

            // refresh specifically the gen folder first, as it may break the build
// if it doesn't arrive in time then refresh the whole project as usual.
genFolder.refreshLocal(IResource.DEPTH_ZERO, new SubProgressMonitor(monitor, 10));
project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 10));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index f36d001..ca58dce 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
//Synthetic comment -- @@ -27,6 +26,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IResourceEventListener;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager.IResourceListener;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;

import org.eclipse.core.resources.IFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewExportPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewExportPart.java
//Synthetic comment -- index 4c75a86..138f0b1 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper.ManifestSectionPart;
import com.android.ide.eclipse.adt.internal.project.ExportHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;

import org.eclipse.core.resources.IFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchShortcut.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchShortcut.java
//Synthetic comment -- index 432827e..3c72d01 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.launch;

import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;

import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index b665a53..9c5fd1e 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectChooserHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectChooserHelper.java
//Synthetic comment -- index aa5813a..2b80d31 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper.IProjectFilter;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;

import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index ba69977..554d64d 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.properties;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java
//Synthetic comment -- index 1f5f2c6..666127e 100644

//Synthetic comment -- @@ -18,10 +18,10 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.IProjectChooserFilter;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState.LibraryState;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index fb00824..d3f1190 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
//Synthetic comment -- @@ -24,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.layoutlib.api.IResourceValue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java
similarity index 99%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/ProjectState.java
//Synthetic comment -- index d5cddc1..05045a2 100644

//Synthetic comment -- @@ -14,10 +14,9 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.internal.project.ProjectProperties;
//Synthetic comment -- @@ -173,7 +172,7 @@
*/
private final ArrayList<ProjectState> mParentProjects = new ArrayList<ProjectState>();

    ProjectState(IProject project, ProjectProperties properties) {
if (project == null || properties == null) {
throw new NullPointerException();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 9db385a..02b538e 100644

//Synthetic comment -- @@ -21,14 +21,13 @@
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IResourceEventListener;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState.LibraryDifference;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState.LibraryState;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -50,6 +49,7 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -1004,6 +1004,7 @@
private interface ActionBundle {
enum BundleType { LINK_LIBRARY, UNLINK_LIBRARY };
BundleType getType();
        IProject getProject();
};

/**
//Synthetic comment -- @@ -1028,6 +1029,10 @@
return BundleType.LINK_LIBRARY;
}

        public IProject getProject() {
            return mProject;
        }

@Override
public String toString() {
return String.format("LinkLibraryBundle: %1$s (%2$s) > %3$s", //$NON-NLS-1$
//Synthetic comment -- @@ -1056,6 +1061,10 @@
public BundleType getType() {
return BundleType.UNLINK_LIBRARY;
}

        public IProject getProject() {
            return mProject.getProject();
        }
}

private final ArrayList<ActionBundle> mActionBundleQueue = new ArrayList<ActionBundle>();
//Synthetic comment -- @@ -1107,6 +1116,11 @@
unlinkLibrary((UnlinkLibraryBundle) bundle, monitor);
break;
}

                            // force a recompile
                            bundle.getProject().build(
                                    IncrementalProjectBuilder.FULL_BUILD, monitor);

} catch (Exception e) {
AdtPlugin.log(e, "Failed to process bundle: %1$s", //$NON-NLS-1$
bundle.toString());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/ExportAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/ExportAction.java
//Synthetic comment -- index c331680..40edc5e 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.internal.project.ExportHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;

import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/ExportWizardAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/ExportWizardAction.java
//Synthetic comment -- index cfdab0f..dc5dbeb 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.wizards.export.ExportWizard;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCheckPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCheckPage.java
//Synthetic comment -- index d9e0205..d247b78 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.wizards.export;

import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.wizards.export.ExportWizard.ExportWizardPage;
import com.android.sdklib.internal.project.ApkSettings;







