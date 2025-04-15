/*Prevent ADT NPE when project has no project.prop file.

There's an internal NPE when initializing the
library classpath container if a project has
neither a default.properties or a project.properties.

See SDK Bug: 29632 for details.

Change-Id:I5e3fda0b84beaf47d59fb102439695a32491e5f8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/LibraryClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/LibraryClasspathContainerInitializer.java
//Synthetic comment -- index ef47494..5d38bf1 100644

//Synthetic comment -- @@ -172,6 +172,10 @@

// check if the project has a valid target.
ProjectState state = Sdk.getProjectState(iProject);

/*
* At this point we're going to gather a list of all that need to go in the







