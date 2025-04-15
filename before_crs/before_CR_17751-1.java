/*Make sure all marker types are removed during clean.

Change-Id:I2a045488b86d03496d766c5c9ade9bb64fcb9246*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 7cbfb96..4fe237f 100644

//Synthetic comment -- @@ -194,7 +194,7 @@
IProject project = getProject();

// Clear the project of the generic markers
        removeMarkersFromProject(project, AndroidConstants.MARKER_AAPT_COMPILE);
removeMarkersFromProject(project, AndroidConstants.MARKER_PACKAGING);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index ee9c749..d5fdea8 100644

//Synthetic comment -- @@ -542,7 +542,7 @@
removeMarkersFromProject(project, AndroidConstants.MARKER_AAPT_COMPILE);
removeMarkersFromProject(project, AndroidConstants.MARKER_XML);
removeMarkersFromProject(project, AndroidConstants.MARKER_AIDL);

}

@Override







