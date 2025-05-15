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

public class ProjectCreator {

    public static IProject create(
            IProgressMonitor monitor,
            IProject project,
            IAndroidTarget target,
            ProjectPopulator projectPopulator,
            boolean isLibrary,
            String projectLocation) throws CoreException, IOException, StreamException {

        if (projectLocation == null || projectLocation.isEmpty()) {
            throw new IllegalArgumentException("Project location cannot be null or empty");
        }

        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        workspace.run(new IWorkspaceRunnable() {
            @Override
            public void run(IProgressMonitor monitor) throws CoreException {
                NewProjectCreator creator = new NewProjectCreator(null, null);

                Map<String, String> dictionary = null;
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put(PARAM_SDK_TARGET, target);
                parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
                parameters.put(PARAM_IS_NEW_PROJECT, true);

                creator.createEclipseProject(monitor, project, description, parameters, dictionary, projectPopulator);
            }
        }, monitor);

        return project;
    }
}

//<End of snippet n. 0>