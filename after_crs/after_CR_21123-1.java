/*Fix builder to not stop on java warning markers.

Change-Id:I98fce9ca71a49ef1d2569215fa3bec9b38a6b6f1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 451b5aa..af4d0ab 100644

//Synthetic comment -- @@ -309,17 +309,29 @@
}

// abort if there are TARGET or ADT type markers
        stopOnMarker(iProject, AndroidConstants.MARKER_TARGET, IResource.DEPTH_ZERO,
                false /*checkSeverity*/);
        stopOnMarker(iProject, AndroidConstants.MARKER_ADT, IResource.DEPTH_ZERO,
                false /*checkSeverity*/);
}

    protected void stopOnMarker(IProject project, String markerType, int depth,
            boolean checkSeverity)
throws AbortBuildException {
try {
IMarker[] markers = project.findMarkers(markerType, false /*includeSubtypes*/, depth);

if (markers.length > 0) {
                if (checkSeverity == false) {
                    throw new AbortBuildException();
                } else {
                    for (IMarker marker : markers) {
                        int severity = marker.getAttribute(IMarker.SEVERITY, -1 /*defaultValue*/);
                        if (severity == IMarker.SEVERITY_ERROR) {
                            throw new AbortBuildException();
                        }
                    }
                }
}
} catch (CoreException e) {
// don't stop, something's really screwed up and the build will break later with








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 1e818d5..1d3d13b 100644

//Synthetic comment -- @@ -641,16 +641,21 @@

IProject iProject = getProject();

        // do a (hopefully quick) search for Precompiler type markers. Those are always only
        // errors.
        stopOnMarker(iProject, AndroidConstants.MARKER_AAPT_COMPILE, IResource.DEPTH_INFINITE,
                false /*checkSeverity*/);
        stopOnMarker(iProject, AndroidConstants.MARKER_AIDL, IResource.DEPTH_INFINITE,
                false /*checkSeverity*/);
        stopOnMarker(iProject, AndroidConstants.MARKER_RENDERSCRIPT, IResource.DEPTH_INFINITE,
                false /*checkSeverity*/);
        stopOnMarker(iProject, AndroidConstants.MARKER_ANDROID, IResource.DEPTH_ZERO,
                false /*checkSeverity*/);

        // do a search for JDT markers. Those can be errors or warnings
stopOnMarker(iProject, IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER,
                IResource.DEPTH_INFINITE, true /*checkSeverity*/);
stopOnMarker(iProject, IJavaModelMarker.BUILDPATH_PROBLEM_MARKER,
                IResource.DEPTH_INFINITE, true /*checkSeverity*/);
}
}







