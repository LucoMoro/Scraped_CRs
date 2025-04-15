/*Fix error when closing a library and project(s) using it at the same time.

By the time the job launched to unlink the linked source folder is running
the main project will be closed and its properties cannot be edited.

Change-Id:I1e5d9f4eb4899d58d3de3d5cc5f422e42d2db71e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index ef044ba..ff34f99 100644

//Synthetic comment -- @@ -1140,6 +1140,13 @@
try {
IProject project = projectState.getProject();

                    // if the library and the main project are closed at the same time, this
                    // is likely to return false since this is run in a new job.
                    if (project.isOpen() == false) {
                        // cannot change the description of closed projects.
                        return Status.OK_STATUS;
                    }

// remove the library to the list of dynamic references
IProjectDescription projectDescription = project.getDescription();
IProject[] refs = projectDescription.getDynamicReferences();
//Synthetic comment -- @@ -1190,6 +1197,8 @@

return Status.OK_STATUS;
} catch (CoreException e) {
                    AdtPlugin.log(e,"Failure when unlinking %1$s from %2$s",
                            libraryProject.getName(), projectState.getProject().getName());
return e.getStatus();
}
}







