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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Base class providing a {@link #getProject()} method to find the project matching the dev tree.
*
*/
class DevTreeProjectProvider {
protected IProject getProject() {
// Get the list of project for the current workspace
IWorkspace workspace = ResourcesPlugin.getWorkspace();
        List<IProject> projects = Arrays.asList(workspace.getRoot().getProjects());
        if (projects.size() == 0) {
            return null;
        }

// get the location of the Dev tree
String devTree = PdtPlugin.getDevTree();
        IPath devTreePath = null;
if (devTree != null) {
            devTreePath = new Path(devTree);
        }

        // filter the list of projects in workspace by 2 conditions:
        //        1. Only look at Java projects
        //        2. If dev tree location is set, only look at projects within the dev tree

        List<IProject> devTreeProjects = new ArrayList<IProject>(projects.size());

        for (IProject p: projects) {
            if (!p.isOpen()) {
                continue;
            }

            if (!hasJavaNature(p)) {
                continue;
            }

            if (devTreePath == null || devTreePath.isPrefixOf(p.getLocation())) {
                devTreeProjects.add(p);
            }
        }

        return selectProject(devTreeProjects);
    }

    private IProject selectProject(List<IProject> devTreeProjects) {
        if (devTreeProjects.size() == 0) {
            return null;
        }

        if (devTreeProjects.size() == 1) {
            return devTreeProjects.get(0);
        }

        // if there are multiple choices, prompt the user
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            return null;
        }

        ListDialog dlg = new ListDialog(window.getShell());
        dlg.setMessage("Select Project");
        dlg.setInput(devTreeProjects);
        dlg.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return ((List<?>) inputElement).toArray();
            }
        });
        dlg.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((IProject) element).getName();
            }
        });
        dlg.setHelpAvailable(false);

        if (dlg.open() == Window.OK) {
            Object[] selectedMatches = dlg.getResult();
            if (selectedMatches.length > 0) {
                return (IProject) selectedMatches[0];
}
}

return null;
}

    private boolean hasJavaNature(IProject p) {
        try {
            return p.hasNature(JavaCore.NATURE_ID);
        } catch (CoreException e) {
            return false;
        }
    }
}







