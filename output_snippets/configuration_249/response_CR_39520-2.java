//<Beginning of snippet n. 0>

import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;

public class ProjectInitializer {

    private IProject mProject;
    private Values mValues;
    
    public void createNewProject(IProgressMonitor monitor, ProjectPopulator projectPopulator) {
        mValues.target = "1.6"; // Set target to JDK 6
        
        NewProjectCreator.create(monitor, mProject, mValues.target, projectPopulator, mValues.isLibrary, mValues.projectLocation);
        
        try {
            mProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException e) {
            Logger.log("Error refreshing project: " + e.getMessage()); 
            // Notify the user here instead of just logging
            UserNotifier.notifyUser("Critical error occurred while refreshing the project. Please check the logs.");
        }
    }
    
    /**
    * Generate custom icons into the project based on the asset studio wizard state
    */
    private void generateIcons(final IProject newProject) {
        // Implementation for icon generation
    }
}

//<End of snippet n. 0>