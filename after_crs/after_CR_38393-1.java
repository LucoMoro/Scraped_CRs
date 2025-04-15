/*NPW: Create project under a workspace runnable.

This makes sure that the project creation is done atomically.

Change-Id:Idf8c6c31ee5d50e0ee7a8ef66c2e44ce69351f85*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 5cb011d..dac4889 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -63,6 +64,7 @@
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
//Synthetic comment -- @@ -800,18 +802,18 @@
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
//Synthetic comment -- @@ -830,8 +832,23 @@
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







