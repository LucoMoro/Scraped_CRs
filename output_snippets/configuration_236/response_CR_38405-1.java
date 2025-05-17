//<Beginning of snippet n. 0>

import org.eclipse.core.resources.IProject;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {

    private static final String PARAM_SDK_TARGET = "sdk_target";
    private static final String PARAM_SRC_FOLDER = "src_folder";
    private static final String PARAM_IS_NEW_PROJECT = "is_new_project";
    private static final Object lock = new Object();

    public static IProject create(
            IProgressMonitor monitor,
            IProject project,
            IAndroidTarget target,
            ProjectPopulator projectPopulator,
            boolean isLibrary,
            String projectLocation)
            throws CoreException, IOException {

        if (monitor == null || project == null || target == null || projectLocation == null || projectLocation.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters.");
        }

        synchronized (lock) {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            if (workspace.getRoot().findMember(project.getName()) != null) {
                throw new CoreException(new IResourceStatus() {
                    @Override
                    public int getCode() {
                        return IResourceStatus.POST_CONFLICT; // example error code
                    }
                });
            }

            NewProjectCreator creator = new NewProjectCreator(null, null);
            Map<String, String> dictionary = null;

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(PARAM_SDK_TARGET, target);
            parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
            parameters.put(PARAM_IS_NEW_PROJECT, false);

            IProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
            try {
                subMonitor.beginTask("Creating project", IProgressMonitor.UNKNOWN);
                return creator.createEclipseProject(subMonitor, project, null, parameters, dictionary, projectPopulator);
            } catch (CoreException | IOException | StreamException e) {
                rollbackProjectCreation(project);
                throw new CoreException(new IResourceStatus() {
                    @Override
                    public int getCode() {
                        return IResourceStatus.FAILED; // indicate failure
                    }
                });
            } finally {
                subMonitor.done();
            }
        }
    }

    private static void rollbackProjectCreation(IProject project) {
        try {
            if (project != null && project.exists()) {
                project.delete(true, null);
            }
        } catch (CoreException e) {
            // Log the exception
        }
    }
}

//<End of snippet n. 0>