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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {
    public static IProject create(IProgressMonitor monitor,
                                   IProject project,
                                   IAndroidTarget target,
                                   ProjectPopulator projectPopulator,
                                   boolean isLibrary,
                                   String projectLocation)
            throws CoreException, IOException, StreamException {
        
        if (monitor == null || project == null || target == null || projectLocation == null) {
            throw new IllegalArgumentException("Monitor, project, target, and project location must not be null");
        }

        NewProjectCreator creator = new NewProjectCreator(null, null);
        Map<String, String> dictionary = new HashMap<>();
        Map<String, Object> parameters = new HashMap<>();
        
        parameters.put(PARAM_SDK_TARGET, target);
        parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
        parameters.put(PARAM_IS_NEW_PROJECT, false);

        try {
            // Using a sub-progress monitor to track the creation process
            monitor.beginTask("Creating project...", IProgressMonitor.UNKNOWN);
            IProject createdProject = creator.createEclipseProject(monitor, project, description, parameters, dictionary, projectPopulator);
            if (createdProject == null) {
                throw new CoreException(new IResourceStatus() {
                    @Override
                    public int getCode() {
                        return IResourceStatus.FAILED;
                    }
                });
            }
            return createdProject;
        } catch (CoreException | IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error creating project", e));
        } finally {
            monitor.done();
        }
    }
}
//<End of snippet n. 0>