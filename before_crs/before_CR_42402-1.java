/*Avoid "Marker id: 123456789 not found" errors

We have some code which runs asynchronously to add or clean up
error markers. In some cases this code runs after a project has
been closed or deleted, which results in errors. Guard these
marker access points with an IResource.isAccessible() check
(which performs both exists(), and for projects, isOpen()).

Change-Id:Ie4884db13b1e00236c1c219d95c3544fcdee9610*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseClasspathContainerInitializer.java
//Synthetic comment -- index 403b17b..ebcf9e3 100644

//Synthetic comment -- @@ -81,7 +81,7 @@
} else {
// no error, remove existing markers.
try {
                if (project.exists()) {
project.deleteMarkers(markerType, true,
IResource.DEPTH_INFINITE);
}
//Synthetic comment -- @@ -92,8 +92,10 @@
@Override
protected IStatus run(IProgressMonitor monitor) {
try {
                            project.deleteMarkers(markerType, true,
                                    IResource.DEPTH_INFINITE);
} catch (CoreException e2) {
return e2.getStatus();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java
//Synthetic comment -- index d51530c..a7db983 100644

//Synthetic comment -- @@ -131,8 +131,13 @@
* @param severity the severity of the marker.
* @return the IMarker that was added or null if it failed to add one.
*/
public final static IMarker markResource(IResource resource, String markerId,
String message, int lineNumber, int startOffset, int endOffset, int severity) {
try {
IMarker marker = resource.createMarker(markerId);
marker.setAttribute(IMarker.MESSAGE, message);
//Synthetic comment -- @@ -176,6 +181,7 @@
* @param severity the severity of the marker.
* @return the IMarker that was added or null if it failed to add one.
*/
public final static IMarker markResource(IResource resource, String markerId,
String message, int severity) {
return markResource(resource, markerId, message, -1, severity);
//Synthetic comment -- @@ -185,16 +191,21 @@
* Adds a marker to an {@link IProject}. This method does not catch {@link CoreException}, like
* {@link #markResource(IResource, String, String, int)}.
*
     * @param resource the file to be marked
* @param markerId The id of the marker to add.
* @param message the message associated with the mark
* @param severity the severity of the marker.
* @param priority the priority of the marker
* @return the IMarker that was added.
     * @throws CoreException
*/
public final static IMarker markProject(IProject project, String markerId,
String message, int severity, int priority) throws CoreException {
IMarker marker = project.createMarker(markerId);
marker.setAttribute(IMarker.MESSAGE, message);
marker.setAttribute(IMarker.SEVERITY, severity);







