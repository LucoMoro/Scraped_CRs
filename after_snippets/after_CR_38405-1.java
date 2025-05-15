
//<Beginning of snippet n. 0>


import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
return project;
}

    public static void create(
IProgressMonitor monitor,
            final IProject project,
IAndroidTarget target,
            final ProjectPopulator projectPopulator,
boolean isLibrary,
String projectLocation)
                throws CoreException {
        final NewProjectCreator creator = new NewProjectCreator(null, null);

        final Map<String, String> dictionary = null;
        final Map<String, Object> parameters = new HashMap<String, Object>();
parameters.put(PARAM_SDK_TARGET, target);
parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
parameters.put(PARAM_IS_NEW_PROJECT, false);
}
}

        IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {
            @Override
            public void run(IProgressMonitor submonitor) throws CoreException {
                try {
                    creator.createEclipseProject(submonitor, project, description, parameters,
                            dictionary, projectPopulator);
                } catch (IOException e) {
                    throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                            "Unexpected error while creating project", e));
                } catch (StreamException e) {
                    throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                            "Unexpected error while creating project", e));
                }
            }
        };

        ResourcesPlugin.getWorkspace().run(workspaceRunnable, monitor);
}

/**

//<End of snippet n. 0>








