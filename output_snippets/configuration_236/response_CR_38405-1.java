//<Beginning of snippet n. 0>

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource; 
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;

public static IProject create(
        IProgressMonitor monitor,
        IProject project,
        IAndroidTarget target,
        ProjectPopulator projectPopulator,
        boolean isLibrary,
        String projectLocation)
        throws CoreException, IOException, StreamException {
    
    if (project == null || target == null || projectLocation == null || monitor.isCanceled()) {
        throw new IllegalArgumentException("Input parameters cannot be null and monitor must not be canceled.");
    }

    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    
    if (project.exists()) {
        throw new CoreException(new Status(IResourceStatus.ERROR_EXISTS, ResourcesPlugin.PI_RESOURCES, "Project already exists."));
    }

    try {
        workspace.run((IRunnableWithProgress) monitor1 -> {
            // Set up the creation parameters
            NewProjectCreator creator = new NewProjectCreator(null, null);
            Map<String, String> dictionary = null;
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(PARAM_SDK_TARGET, target);
            parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
            parameters.put(PARAM_IS_NEW_PROJECT, false);

            try {
                creator.createEclipseProject(monitor1, project, description, parameters, dictionary, projectPopulator);
            } catch (Exception e) {
                // Rollback or handle exceptions as necessary
                throw new CoreException(new Status(IResourceStatus.ERROR_FAILED, ResourcesPlugin.PI_RESOURCES, "Project creation failed.", e));
            }
        }, monitor);
    } catch (OperationCanceledException e) {
        throw e; // Propagate cancellation
    }

    return project;
}

//<End of snippet n. 0>