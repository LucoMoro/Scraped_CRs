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

import java.io.File;

/**
//Synthetic comment -- @@ -272,15 +274,15 @@
public final static String MARKER_ATTR_TYPE_PROVIDER = "provider"; //$NON-NLS-1$

/**
     * Preferred compiler level, i.e. "1.5".
*/
    public final static String COMPILER_COMPLIANCE_PREFERRED = "1.5"; //$NON-NLS-1$
/**
* List of valid compiler level, i.e. "1.5" and "1.6"
*/
public final static String[] COMPILER_COMPLIANCE = {
        "1.5", //$NON-NLS-1$
        "1.6", //$NON-NLS-1$
};

/** The base URL where to find the Android class & manifest documentation */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index a733708..146570c 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.builders.PostCompilerBuilder;
//Synthetic comment -- @@ -48,6 +51,10 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

import java.util.ArrayList;
//Synthetic comment -- @@ -450,6 +457,45 @@
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
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 2da1fb5..aa7e134 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
//Synthetic comment -- @@ -37,6 +39,7 @@
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
//Synthetic comment -- @@ -304,6 +307,13 @@
NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
mValues.isLibrary, mValues.projectLocation);

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());
} catch (CoreException e) {







