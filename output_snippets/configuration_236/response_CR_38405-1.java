//<Beginning of snippet n. 0>

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.IStatus;

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
        
        if (project == null || target == null || projectLocation == null || monitor == null || project.getName().isEmpty()) {
            throw new IllegalArgumentException("Required parameters are missing or invalid.");
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace.getRoot().getProject(project.getName()).exists()) {
            throw new CoreException(new IStatus() {
                @Override
                public int getSeverity() {
                    return IStatus.ERROR;
                }

                @Override
                public String getPlugin() {
                    return "ProjectManager";
                }

                @Override
                public String getMessage() {
                    return "Project already exists.";
                }

                @Override
                public Throwable getException() {
                    return null;
                }

                @Override
                public int getCode() {
                    return 0;
                }
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
            throw new CoreException(new IStatus() {
                @Override
                public int getSeverity() {
                    return IStatus.ERROR;
                }

                @Override
                public String getPlugin() {
                    return "ProjectManager";
                }

                @Override
                public String getMessage() {
                    return "Error occurred during project creation: " + e.getMessage();
                }

                @Override
                public Throwable getException() {
                    return e;
                }

                @Override
                public int getCode() {
                    return 0;
                }
            });
        }
    }
}

//<End of snippet n. 0>