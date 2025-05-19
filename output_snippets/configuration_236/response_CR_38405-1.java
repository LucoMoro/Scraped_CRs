//<Beginning of snippet n. 0>

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAndroidTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ProjectManager {

    public static IProject create(
            IProgressMonitor monitor,
            IProject project,
            IAndroidTarget target,
            ProjectPopulator projectPopulator,
            boolean isLibrary,
            String projectLocation) throws CoreException, IOException {

        if (monitor == null || project == null || target == null || projectLocation == null || projectLocation.isEmpty()) {
            throw new IllegalArgumentException("Parameters cannot be null or empty.");
        }

        IProject[] existingProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject existingProject : existingProjects) {
            if (existingProject.getName().equals(project.getName())) {
                throw new CoreException(new Status(
                        IStatus.ERROR,
                        "Project already exists: " + project.getName()));
            }
        }

        try {
            ResourcesPlugin.getWorkspace().run((IWorkspaceRunnable) monitor1 -> {
                if (monitor1.isCanceled()) {
                    throw new OperationCanceledException();
                }

                NewProjectCreator creator = new NewProjectCreator(null, null);

                Map<String, String> dictionary = null;
                Map<String, Object> parameters = new HashMap<>();
                parameters.put(PARAM_SDK_TARGET, target);
                parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
                parameters.put(PARAM_IS_NEW_PROJECT, true);

                creator.createEclipseProject(monitor1, project, null, parameters, dictionary, projectPopulator);
            }, monitor);
        } catch (CoreException | IOException e) {
            // Rollback logic
            if (project.exists()) {
                try {
                    project.delete(true, null);
                } catch (CoreException deleteException) {
                    throw new CoreException(new Status(
                            IStatus.ERROR,
                            "Failed to rollback project deletion: " + deleteException.getMessage(), deleteException));
                }
            }

            throw new CoreException(new Status(
                    IStatus.ERROR,
                    "Error during project creation: " + e.getMessage(), e));
        }

        return project;
    }
}

//<End of snippet n. 0>