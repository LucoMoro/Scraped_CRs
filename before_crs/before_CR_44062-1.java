/*Change the global monitor to handle non android project.

Project listeners don't receive notifications on non-android
projects.
file/folder listener have a boolean to tell them what the
project is.
Raw listeners have to check it manually so the only
user now does so.

Change-Id:I7068176099d28d979d31070854a2a646bca1204e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index a7ef6c6..9e17f0f 100644

//Synthetic comment -- @@ -1580,7 +1580,10 @@
monitor.addFileListener(new IFileListener() {
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
                    int kind, @Nullable String extension, int flags) {
if (flags == IResourceDelta.MARKERS || !SdkConstants.EXT_XML.equals(extension)) {
// ONLY the markers changed, or not XML file: not relevant to this listener
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index d9e798e..4e4429d 100644

//Synthetic comment -- @@ -175,7 +175,7 @@
*/
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
                int kind, @Nullable String extension, int flags) {
// This listener only cares about .class files and AndroidManifest.xml files
if (!(SdkConstants.EXT_CLASS.equals(extension)
|| SdkConstants.EXT_XML.equals(extension)
//Synthetic comment -- @@ -186,15 +186,7 @@
// get the file's project
IProject project = file.getProject();

            boolean hasAndroidNature = false;
            try {
                hasAndroidNature = project.hasNature(AdtConstants.NATURE_DEFAULT);
            } catch (CoreException e) {
                // do nothing if the nature cannot be queried.
                return;
            }

            if (hasAndroidNature) {
// project is an Android project, it's the one being affected
// directly by its own file change.
processFileChanged(file, project, extension);
//Synthetic comment -- @@ -204,16 +196,14 @@

for (IProject p : referencingProjects) {
try {
                        hasAndroidNature = p.hasNature(AdtConstants.NATURE_DEFAULT);
} catch (CoreException e) {
// do nothing if the nature cannot be queried.
                        continue;
                    }

                    if (hasAndroidNature) {
                        // the changed project is a dependency on an Android project,
                        // update the main project.
                        processFileChanged(file, p, extension);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index b1bfa88..55ebf59 100644

//Synthetic comment -- @@ -321,8 +321,8 @@
mMarkerMonitor = new IFileListener() {
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
                        int kind, @Nullable String extension, int flags) {
                    if (file.equals(inputFile)) {
processMarkerChanges(markerDeltas);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java
//Synthetic comment -- index df8d9af..ebb9a59 100644

//Synthetic comment -- @@ -167,9 +167,10 @@
@Override
public void fileChanged(@NonNull IFile file,
@NonNull IMarkerDelta[] markerDeltas,
                    int kind, @Nullable String extension, int flags) {
                if (flags == IResourceDelta.MARKERS) {
                    // ONLY the markers changed. Ignore these since they happen
// when we add markers for lint errors found in the current file,
// which would cause us to repeatedly enter this method over and over
// again.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java
//Synthetic comment -- index 6554cc2..ab5ae40 100644

//Synthetic comment -- @@ -84,9 +84,9 @@
*/
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
            int kind, @Nullable String extension, int flags) {
        if (flags == IResourceDelta.MARKERS) {
            // Only the markers changed: not relevant
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/GlobalProjectMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/GlobalProjectMonitor.java
//Synthetic comment -- index 7cb4e94..674a601 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

//Synthetic comment -- @@ -76,9 +77,10 @@
*            not have an extension
* @param flags the {@link IResourceDelta#getFlags()} value with details
*            on what changed in the file
*/
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
                int kind, @Nullable String extension, int flags);
}

/**
//Synthetic comment -- @@ -138,8 +140,9 @@
* Sent when a folder changed.
* @param folder The file that was changed
* @param kind The change kind. This is equivalent to {@link IResourceDelta#getKind()}
*/
        public void folderChanged(IFolder folder, int kind);
}

/**
//Synthetic comment -- @@ -208,6 +211,8 @@

private IWorkspace mWorkspace;

/**
* Delta visitor for resource changes.
*/
//Synthetic comment -- @@ -226,7 +231,7 @@
|| (bundle.kindMask & kind) != 0) {
try {
bundle.listener.fileChanged((IFile)r, delta.getMarkerDeltas(), kind,
                                    r.getFileExtension(), delta.getFlags());
} catch (Throwable t) {
AdtPlugin.log(t,"Failed to call IFileListener.fileChanged");
}
//Synthetic comment -- @@ -240,7 +245,7 @@
if (bundle.kindMask == ListenerBundle.MASK_NONE
|| (bundle.kindMask & kind) != 0) {
try {
                            bundle.listener.folderChanged((IFolder)r, kind);
} catch (Throwable t) {
AdtPlugin.log(t,"Failed to call IFileListener.folderChanged");
}
//Synthetic comment -- @@ -248,11 +253,27 @@
}
return true;
} else if (type == IResource.PROJECT) {
int flags = delta.getFlags();

if ((flags & IResourceDelta.OPEN) != 0) {
// the project is opening or closing.
                    IProject project = (IProject)r;

if (project.isOpen()) {
// notify the listeners.
//Synthetic comment -- @@ -491,9 +512,15 @@
// notify the listeners.
for (IProjectListener pl : mProjectListeners) {
try {
                        pl.projectDeleted(project);
                    } catch (Throwable t) {
                        AdtPlugin.log(t,"Failed to call IProjectListener.projectDeleted");
}
}
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index e1a12d7..1e12861 100644

//Synthetic comment -- @@ -422,6 +422,15 @@
for (IResourceDelta delta : projectDeltas) {
if (delta.getResource() instanceof IProject) {
IProject project = (IProject) delta.getResource();
IdeScanningContext context =
new IdeScanningContext(getProjectResources(project), project, true);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 1a299d9..d9020f8 100644

//Synthetic comment -- @@ -880,16 +880,6 @@
}

private void onProjectRemoved(IProject removedProject, boolean deleted) {
            try {
                if (removedProject.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
                    return;
                }
            } catch (CoreException e) {
                // this can only happen if the project does not exist or is not open, neither
                // of which can happen here since we're processing a Project removed/deleted event
                // which is processed before the project is actually removed/closed.
            }

if (DEBUG) {
System.out.println(">>> CLOSED: " + removedProject.getName());
}
//Synthetic comment -- @@ -963,15 +953,6 @@
}

private void onProjectOpened(final IProject openedProject) {
            try {
                if (openedProject.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
                    return;
                }
            } catch (CoreException e) {
                // this can only happen if the project does not exist or is not open, neither
                // of which can happen here since we're processing a Project opened event.
            }


ProjectState openedState = getProjectState(openedProject);
if (openedState != null) {
//Synthetic comment -- @@ -1052,7 +1033,11 @@
private IFileListener mFileListener = new IFileListener() {
@Override
public void fileChanged(final @NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
                int kind, @Nullable String extension, int flags) {
if (SdkConstants.FN_PROJECT_PROPERTIES.equals(file.getName()) &&
file.getParent() == file.getProject()) {
try {
//Synthetic comment -- @@ -1060,10 +1045,6 @@
// the target.
IProject iProject = file.getProject();

                    if (iProject.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
                        return;
                    }

ProjectState state = Sdk.getProjectState(iProject);

// get the current target







