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
* @param severity the severity of the marker.
* @return the IMarker that was added or null if it failed to add one.
*/
public final static IMarker markResource(IResource resource, String markerId,
                                         String message, int lineNumber, int startOffset, int endOffset, int severity) {
    try {
        if (resource.isAccessible()) {
            IMarker marker = resource.createMarker(markerId);
            marker.setAttribute(IMarker.MESSAGE, message);
        }
    } catch (CoreException e) {
        // Handle exception (e.g., log the error).
        return null;
    }
    return null; // Return null if marker creation failed.
}

public final static IMarker markResource(IResource resource, String markerId,
                                         String message, int severity) {
    return markResource(resource, markerId, message, -1, severity);
}

/**
 * Adds a marker to an {@link IProject}. This method does not catch {@link CoreException}, like
 * {@link #markResource(IResource, String, String, int)}.
 *
 * @param project the project to be marked
 * @param markerId The id of the marker to add.
 * @param message the message associated with the mark
 * @param severity the severity of the marker.
 * @param priority the priority of the marker
 * @return the IMarker that was added.
 * @throws CoreException
 */
public final static IMarker markProject(IProject project, String markerId,
                                        String message, int severity, int priority) throws CoreException {
    if (project.isAccessible()) {
        IMarker marker = project.createMarker(markerId);
        marker.setAttribute(IMarker.MESSAGE, message);
        marker.setAttribute(IMarker.SEVERITY, severity);
        return marker;
    }
    // Handle inaccessible project case, possibly log it.
    return null; // Return null if project is not accessible.
}
//<End of snippet n. 1>