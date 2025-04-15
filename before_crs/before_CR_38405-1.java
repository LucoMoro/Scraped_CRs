/*NPW: Create project under a workspace runnable.

This makes sure that the project creation is done atomically.

(cherry picked from commit 55b42b37afc532db41f4a14e5ec71de50d840bd0)

Change-Id:I82a6856d874dde46bd1c6abb1a5eba505c3e573e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 5cb011d..dac4889 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -63,6 +64,7 @@
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
//Synthetic comment -- @@ -800,18 +802,18 @@
return project;
}

    public static IProject create(
IProgressMonitor monitor,
            IProject project,
IAndroidTarget target,
            ProjectPopulator projectPopulator,
boolean isLibrary,
String projectLocation)
                throws CoreException, IOException, StreamException {
        NewProjectCreator creator = new NewProjectCreator(null, null);

        Map<String, String> dictionary = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
parameters.put(PARAM_SDK_TARGET, target);
parameters.put(PARAM_SRC_FOLDER, SdkConstants.FD_SOURCES);
parameters.put(PARAM_IS_NEW_PROJECT, false);
//Synthetic comment -- @@ -830,8 +832,23 @@
}
}

        return creator.createEclipseProject(monitor, project, description, parameters,
                dictionary, projectPopulator);
}

/**







