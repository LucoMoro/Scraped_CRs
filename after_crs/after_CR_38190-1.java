/*Prevent a possible NPE when creating a project.

This can happen when adding a jar file to a project
during creation. The jar file triggers a change to a project
that doesn't technically exist yet.

Change-Id:I6de1e1be1f057cbc1921f3a2074aa45684c93bbb*/




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







