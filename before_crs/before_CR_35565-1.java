/*PDT: Support projects that are not the entire tree.

Currently, PDT's project selector only returns the project
that is created at the root of Android tree. This patch
allows the use of smaller projects inside the Android tree.
If multiple such projects are open in the workspace, the
user is prompted to pick one.

Change-Id:I9784ad69ab3c3636dabbdf477e538e74e1f29d3c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.pdt/src/com/android/ide/eclipse/pdt/internal/DevTreeProjectProvider.java b/eclipse/plugins/com.android.ide.eclipse.pdt/src/com/android/ide/eclipse/pdt/internal/DevTreeProjectProvider.java
//Synthetic comment -- index 55ea3d0..b44d2cb 100644

//Synthetic comment -- @@ -22,45 +22,119 @@
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

/**
* Base class providing a {@link #getProject()} method to find the project matching the dev tree.
*
*/
class DevTreeProjectProvider {

protected IProject getProject() {
// Get the list of project for the current workspace
IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject[] projects = workspace.getRoot().getProjects();

// get the location of the Dev tree
String devTree = PdtPlugin.getDevTree();

if (devTree != null) {

            // look for a project that matches the location of the dev tree
            for (IProject p : projects) {
                if (p.isOpen()) {
                    try {
                        if (p.hasNature(JavaCore.NATURE_ID) == false) {
                            // ignore non Java projects
                            continue;
                        }
                    } catch (CoreException e) {
                        // failed to get the nature? skip project.
                        continue;
                    }

                    // check the location of the project
                    if (devTree.equals(p.getLocation().toOSString())) {
                        return p;
                    }
                }
}
}

return null;
}
}







