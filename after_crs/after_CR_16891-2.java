/*Don't process events for non Android projects

Project opened event are triggered for all events.
The code would then try to get the ProjectState for
the opened project whether it was an Android project
or not, filling the log with errors.

Same thing for file modification event for default.properties.
Only care if the containing project is an Android project.http://b.android.com/10651Change-Id:I35d7b546794ced41cffd382a9693294cfeb3e311*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 321b934..db71d2c 100644

//Synthetic comment -- @@ -18,7 +18,9 @@

import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
//Synthetic comment -- @@ -717,6 +719,16 @@
}

private void onProjectRemoved(IProject project, boolean deleted) {
            try {
                if (project.hasNature(AndroidConstants.NATURE_DEFAULT) == false) {
                    return;
                }
            } catch (CoreException e) {
                // this can only happen if the project does not exist or is not open, neither
                // of which can happen here since we're processing a Project removed/deleted event
                // which is processed before the project is actually removed/closed.
            }

// get the target project
synchronized (sLock) {
// Don't use getProject() as it could create the ProjectState if it's not
//Synthetic comment -- @@ -794,6 +806,15 @@
}

private void onProjectOpened(final IProject openedProject) {
            try {
                if (openedProject.hasNature(AndroidConstants.NATURE_DEFAULT) == false) {
                    return;
                }
            } catch (CoreException e) {
                // this can only happen if the project does not exist or is not open, neither
                // of which can happen here since we're processing a Project opened event.
            }

ProjectState openedState = getProjectState(openedProject);
if (openedState != null) {
if (openedState.hasLibraries()) {
//Synthetic comment -- @@ -852,7 +873,15 @@
}

public void projectRenamed(IProject project, IPath from) {
            try {
                if (project.hasNature(AndroidConstants.NATURE_DEFAULT) == false) {
                    return;
                }
            } catch (CoreException e) {
                // this can only happen if the project does not exist or is not open, neither
                // of which can happen here since we're processing a Project renamed event.
            }

// a project was renamed.
// if the project is a library, look for any project that depended on it
// and update it. (default.properties and linked source folder)
//Synthetic comment -- @@ -911,6 +940,11 @@
// reload the content of the default.properties file and update
// the target.
IProject iProject = file.getProject();

                    if (iProject.hasNature(AndroidConstants.NATURE_DEFAULT) == false) {
                        return;
                    }

ProjectState state = Sdk.getProjectState(iProject);

// get the current target







