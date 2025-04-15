/*Integrate 3f18a506 into tools_r10

Fix builder to not stop on java warning markers.

Change-Id:I8e9b2dba20524aedd3c3e533d74695ccfc0e9c46*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 451b5aa..af4d0ab 100644

//Synthetic comment -- @@ -309,17 +309,29 @@
}

// abort if there are TARGET or ADT type markers
        stopOnMarker(iProject, AndroidConstants.MARKER_TARGET, IResource.DEPTH_ZERO);
        stopOnMarker(iProject, AndroidConstants.MARKER_ADT, IResource.DEPTH_ZERO);
}

    protected void stopOnMarker(IProject project, String markerType, int depth)
throws AbortBuildException {
try {
IMarker[] markers = project.findMarkers(markerType, false /*includeSubtypes*/, depth);

if (markers.length > 0) {
                throw new AbortBuildException();
}
} catch (CoreException e) {
// don't stop, something's really screwed up and the build will break later with








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 1e818d5..1d3d13b 100644

//Synthetic comment -- @@ -641,16 +641,21 @@

IProject iProject = getProject();

        // do a (hopefully quick) search for Precompiler type markers.
        stopOnMarker(iProject, AndroidConstants.MARKER_AAPT_COMPILE, IResource.DEPTH_INFINITE);
        stopOnMarker(iProject, AndroidConstants.MARKER_AIDL, IResource.DEPTH_INFINITE);
        stopOnMarker(iProject, AndroidConstants.MARKER_RENDERSCRIPT, IResource.DEPTH_INFINITE);
        stopOnMarker(iProject, AndroidConstants.MARKER_ANDROID, IResource.DEPTH_ZERO);

        // do a search for JDT markers.
stopOnMarker(iProject, IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER,
                IResource.DEPTH_INFINITE);
stopOnMarker(iProject, IJavaModelMarker.BUILDPATH_PROBLEM_MARKER,
                IResource.DEPTH_INFINITE);
}
}







