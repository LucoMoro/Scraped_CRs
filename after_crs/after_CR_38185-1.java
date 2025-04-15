/*Prevent a possible NPE when creating a project.

This can happen when adding a jar file to a project
during creation. The jar file triggers a change to a project
that doesn't technically exist yet.

(cherry picked from commit 0396787a07b8a50985c24cdf4ea4b120a6a2d812)

Change-Id:I2713b1dac3a543784618b0774e613c8e74cad942*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 4f9bb09..3fed2e0 100644

//Synthetic comment -- @@ -1115,17 +1115,18 @@

ProjectState state = Sdk.getProjectState(iProject);

                if (state != null) {
                    Collection<ProjectState> parents = state.getFullParentProjects();
                    for (ProjectState s : parents) {
                        javaProject = BaseProjectHelper.getJavaProject(s.getProject());
                        if (javaProject != null) {
                            projectList.add(javaProject);
                        }
}

                    ProjectHelper.updateProjects(
                            projectList.toArray(new IJavaProject[projectList.size()]));
}
} catch (CoreException e) {
// This can't happen as it's only for closed project (or non existing)
// but in that case we can't get a fileChanged on this file.







