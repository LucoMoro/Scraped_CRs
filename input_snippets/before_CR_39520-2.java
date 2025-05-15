
//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator,
mValues.isLibrary, mValues.projectLocation);

try {
mProject.refreshLocal(DEPTH_INFINITE, new NullProgressMonitor());
} catch (CoreException e) {
}

/**
* Generate custom icons into the project based on the asset studio wizard state
*/
private void generateIcons(final IProject newProject) {

//<End of snippet n. 0>








