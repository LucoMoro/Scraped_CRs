//<Beginning of snippet n. 0>
} else {
// no error, remove existing markers.
try {
    if (project.isAccessible()) {
        project.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
    }
} catch (CoreException e2) {
    return e2.getStatus();
}
@Override
protected IStatus run(IProgressMonitor monitor) {
    try {
        if (project.isAccessible()) {
            project.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
        }
    } catch (CoreException e2) {
        return e2.getStatus();
    }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public final static IMarker markResource(IResource resource, String markerId,
        String message, int lineNumber, int startOffset, int endOffset, int severity) {
    if (!resource.isAccessible()) return null;
    try {
        IMarker marker = resource.createMarker(markerId);
        marker.setAttribute(IMarker.MESSAGE, message);
    } catch (CoreException e) {
        // Log the exception instead of returning null silently
        Activator.log(e);
        return null;
    }
    return marker; // return the created marker
}

public final static IMarker markResource(IResource resource, String markerId,
        String message, int severity) {
    return markResource(resource, markerId, message, -1, severity);
}

public final static IMarker markProject(IProject project, String markerId,
        String message, int severity, int priority) throws CoreException {
    if (!project.isAccessible()) return null;
    IMarker marker = project.createMarker(markerId);
    marker.setAttribute(IMarker.MESSAGE, message);
    marker.setAttribute(IMarker.SEVERITY, severity);
    return marker;
}
//<End of snippet n. 1>