/*35184: Initialize new projects to use JDK 6

Some of the templates require JDK 6 (because they specify @Override on
methods implementing an interface). Rather than attempting to figure
out what the default is and conditionally adding these annotations,
always tie the project to JDK 6 instead, since it's a better language.

This fixes issue 35184: Using the wizard for a JellyBean only
multi-fragment project creates uncompilable code

Change-Id:I434d7b7335ac51110c6044f4ac4765a5a8813c3d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index 1aa4221..8ac1012 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.ide.eclipse.adt.internal.build.builders.ResourceManagerBuilder;
import com.android.sdklib.SdkConstants;

import org.eclipse.jdt.core.JavaCore;

import java.io.File;

/**
//Synthetic comment -- @@ -272,15 +274,15 @@
public final static String MARKER_ATTR_TYPE_PROVIDER = "provider"; //$NON-NLS-1$

/**
     * Preferred compiler level, i.e. "1.6".
*/
    public final static String COMPILER_COMPLIANCE_PREFERRED = JavaCore.VERSION_1_6;
/**
* List of valid compiler level, i.e. "1.5" and "1.6"
*/
public final static String[] COMPILER_COMPLIANCE = {
        JavaCore.VERSION_1_5,
        JavaCore.VERSION_1_6,
};

/** The base URL where to find the Android class & manifest documentation */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index a733708..146570c 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.eclipse.adt.internal.project;

import static com.android.ide.eclipse.adt.AdtConstants.COMPILER_COMPLIANCE_PREFERRED;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.builders.PostCompilerBuilder;
//Synthetic comment -- @@ -48,6 +51,10 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;

import java.util.ArrayList;
//Synthetic comment -- @@ -450,6 +457,45 @@
}

/**
     * Makes the given project use JDK 6 (or more specifically,
     * {@link AdtConstants#COMPILER_COMPLIANCE_PREFERRED} as the compilation
     * target, regardless of what the default IDE JDK level is, provided a JRE
     * of the given level is installed.
     *
     * @param javaProject the Java project
     * @throws CoreException if the IDE throws an exception setting the compiler
     *             level
     */
    @SuppressWarnings("restriction") // JDT API for setting compliance options
    public static void enforcePreferredCompilerCompliance(@NonNull IJavaProject javaProject)
            throws CoreException {
        String compliance = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
        if (compliance == null ||
                JavaModelUtil.isVersionLessThan(compliance, COMPILER_COMPLIANCE_PREFERRED)) {
            IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
            for (int i = 0; i < types.length; i++) {
                IVMInstallType type = types[i];
                IVMInstall[] installs = type.getVMInstalls();
                for (int j = 0; j < installs.length; j++) {
                    IVMInstall install = installs[j];
                    if (install instanceof IVMInstall2) {
                        IVMInstall2 install2 = (IVMInstall2) install;
                        // Java version can be 1.6.0, and preferred is 1_6
                        if (install2.getJavaVersion().startsWith(COMPILER_COMPLIANCE_PREFERRED)) {
                            Map<String, String> options = javaProject.getOptions(false);
                            JavaCore.setComplianceOptions(COMPILER_COMPLIANCE_PREFERRED, options);
                            JavaModelUtil.setDefaultClassfileOptions(options,
                                    COMPILER_COMPLIANCE_PREFERRED);
                            javaProject.setOptions(options);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
* Returns a {@link IProject} by its running application name, as it returned by the AVD.
* <p/>
* <var>applicationName</var> will in most case be the package declared in the manifest, but








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index dac4889..38feebe 100644

//Synthetic comment -- @@ -628,6 +628,7 @@
final IJavaProject javaProject = JavaCore.create(project);
Display.getDefault().syncExec(new WorksetAdder(javaProject,
mValues.workingSets));
                        ProjectHelper.enforcePreferredCompilerCompliance(javaProject);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 2da1fb5..aa7e134 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
//Synthetic comment -- @@ -37,6 +39,7 @@
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
//Synthetic comment -- @@ -304,6 +307,13 @@
NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
mValues.isLibrary, mValues.projectLocation);

            // For new projects, ensure that we're actually using the preferred compliance,
            // not just the default one
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
            if (javaProject != null) {
                ProjectHelper.enforcePreferredCompilerCompliance(javaProject);
            }

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());
} catch (CoreException e) {







