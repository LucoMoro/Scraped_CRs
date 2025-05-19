//<Beginning of snippet n. 0>
} else {
// no error, remove existing markers.
try {
    if (project.exists() && project.isAccessible()) {
        project.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
    }
} catch (CoreException e2) {
    throw new CoreException(new Status(IStatus.ERROR, "Marker", "Error deleting markers", e2));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public final static IMarker markResource(IResource resource, String markerId,
String message, int lineNumber, int startOffset, int endOffset, int severity) throws CoreException {
    if (!isResourceAccessible(resource)) {
        throw new CoreException(new Status(IStatus.ERROR, "Marker", "Resource is not accessible"));
    }
    try {
        IMarker marker = resource.createMarker(markerId);
        marker.setAttribute(IMarker.MESSAGE, message);
        // additional attributes can be set here...
        return marker;
    } catch (CoreException e) {
        throw new CoreException(new Status(IStatus.ERROR, "Marker", "Error creating marker", e));
    }
}

public final static IMarker markResource(IResource resource, String markerId,
String message, int severity) throws CoreException {
    return markResource(resource, markerId, message, -1, severity);
}

public final static IMarker markProject(IProject project, String markerId,
String message, int severity, int priority) throws CoreException {
    if (!isProjectAccessible(project)) {
        throw new CoreException(new Status(IStatus.ERROR, "Marker", "Project is not accessible"));
    }
    try {
        IMarker marker = project.createMarker(markerId);
        marker.setAttribute(IMarker.MESSAGE, message);
        marker.setAttribute(IMarker.SEVERITY, severity);
        return marker;
    } catch (CoreException e) {
        throw new CoreException(new Status(IStatus.ERROR, "Marker", "Error creating project marker", e));
    }
}

private static boolean isResourceAccessible(IResource resource) {
    return resource.exists() && resource.isAccessible();
}

private static boolean isProjectAccessible(IProject project) {
    return project.exists() && project.isAccessible();
}
//<End of snippet n. 1>