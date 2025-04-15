/*Don't process events for non Android projects

Project opened event are triggered for all events.
The code would then try to get the ProjectState for
the opened project whether it was an Android project
or not, filling the log with errors.

Same thing for file modification event for default.properties.
Only care if the containing project is an Android project.

Change-Id:I35d7b546794ced41cffd382a9693294cfeb3e311*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 321b934..43fd229 100644

//Synthetic comment -- @@ -18,7 +18,9 @@

import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
//Synthetic comment -- @@ -794,6 +796,15 @@
}

private void onProjectOpened(final IProject openedProject) {
ProjectState openedState = getProjectState(openedProject);
if (openedState != null) {
if (openedState.hasLibraries()) {
//Synthetic comment -- @@ -911,6 +922,11 @@
// reload the content of the default.properties file and update
// the target.
IProject iProject = file.getProject();
ProjectState state = Sdk.getProjectState(iProject);

// get the current target







