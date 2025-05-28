//<Beginning of snippet n. 0>

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IResourceStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {
    
    public static IProject create(
            IProgressMonitor monitor,
            IProject project,
            IAndroidTarget target,
            ProjectPopulator projectPopulator,
            boolean isLibrary,
            String projectLocation) throws CoreException, IOException, StreamException {
        
        if (project == null || target == null || projectLocation == null || monitor == null) {
            throw new IllegalArgumentException("Required parameters are missing.");
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace.getRoot().getProject(project.getName()).exists()) {
            throw new CoreException(new IResourceStatus() {
                // Implement necessary methods here
            });
        }

        NewProjectCreator creator = new NewProjectCreator(null, null);
        
        Map<String, String> dictionary = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PARAM_SDK_TARGET, target);
        parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
        parameters.put(PARAM_IS_NEW_PROJECT, false);

        try {
            return creator.createEclipseProject(monitor, project, description, parameters,
                    dictionary, projectPopulator);
        } catch (CoreException | IOException | StreamException e) {
            // Handle resource clean-up and logging here
            throw e; // Rethrow to notify caller
        }
    }
}

//<End of snippet n. 0>