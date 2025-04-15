/*35184: Initialize new projects to use JDK 6

Some of the templates require JDK 6 (because they specify @Override on
methods implementing an interface). Rather than attempting to figure
out what the default is and conditionally adding these annotations,
always tie the project to JDK 6 instead, since it's a better language.

This fixes issue 35184: Using the wizard for a JellyBean only
multi-fragment project creates uncompilable code

Change-Id:I434d7b7335ac51110c6044f4ac4765a5a8813c3d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 2da1fb5..d618098 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
//Synthetic comment -- @@ -37,6 +38,13 @@
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
//Synthetic comment -- @@ -304,6 +312,8 @@
NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
mValues.isLibrary, mValues.projectLocation);

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());
} catch (CoreException e) {
//Synthetic comment -- @@ -318,6 +328,41 @@
}

/**
* Generate custom icons into the project based on the asset studio wizard state
*/
private void generateIcons(final IProject newProject) {







