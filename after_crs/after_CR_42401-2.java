/*Close XML editors when files are deleted or projects are closed

The existing hooks for closing the editor when the underlying file is
deleted was not working correctly, and was inefficient (since each
editor added their own global resource listener, so every editor would
find out about every other editor's file changes).

Instead, this generalizes the single editor listener which was used to
initialize editor types such that it also listens for deletion, and
then finds any open editor mapped to that file.  It also hooks up to
the pre-close events for projects and closes all files related to the
project as well.

This will hopefully fix this issue as well:
20836: Can't delete a layout XML via Package Explorer > Delete if it
       has an error

Finally, it removes a bunch of now obsolete logging code for an issue
which seems to be permanently fixed.

Change-Id:I90b38984639a605755f2d67ca2413cc925f730d1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index ce6ef7c..71d02d0 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -61,6 +62,7 @@
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
//Synthetic comment -- @@ -128,15 +130,6 @@
* The activator class controls the plug-in life cycle
*/
public class AdtPlugin extends AbstractUIPlugin implements ILogger {
/** The plug-in ID */
public static final String PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$

//Synthetic comment -- @@ -1508,7 +1501,7 @@

if (mResourceMonitor != null) {
try {
                setupEditors(mResourceMonitor);
ResourceManager.setup(mResourceMonitor);
LintDeltaProcessor.startListening(mResourceMonitor);
} catch (Throwable t) {
//Synthetic comment -- @@ -1570,125 +1563,103 @@
}

/**
     * Sets up the editor resource listener.
     * <p>
     * The listener handles:
     * <ul>
     * <li> Discovering newly created files, and ensuring that if they are in an Android
     *      project, they default to the right XML editor.
     * <li> Discovering deleted files, and closing the corresponding editors if necessary.
     *      This is only done for XML files, since other editors such as Java editors handles
     *      it on their own.
     * <ul>
*
* This is called by the {@link AdtPlugin} during initialization.
*
* @param monitor The main Resource Monitor object.
*/
    public void setupEditors(GlobalProjectMonitor monitor) {
monitor.addFileListener(new IFileListener() {
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
int kind, @Nullable String extension, int flags) {
                if (flags == IResourceDelta.MARKERS || !AdtConstants.EXT_XML.equals(extension)) {
                    // ONLY the markers changed, or not XML file: not relevant to this listener
return;
}

                if (kind == IResourceDelta.REMOVED) {
                    AdtUtils.closeEditors(file, false /*save*/);
                    return;
                }

                // The resources files must have a file path similar to
                //    project/res/.../*.xml
                // There is no support for sub folders, so the segment count must be 4
                if (file.getFullPath().segmentCount() == 4) {
                    // check if we are inside the res folder.
                    String segment = file.getFullPath().segment(1);
                    if (segment.equalsIgnoreCase(SdkConstants.FD_RESOURCES)) {
                        // we are inside a res/ folder, get the ResourceFolderType of the
                        // parent folder.
                        String[] folderSegments = file.getParent().getName().split(
                                AndroidConstants.RES_QUALIFIER_SEP);

                        // get the enum for the resource type.
                        ResourceFolderType type = ResourceFolderType.getTypeByName(
                                folderSegments[0]);

                        if (type != null) {
                            if (kind == IResourceDelta.ADDED) {
                                // A new file {@code /res/type-config/some.xml} was added.
                                // All the /res XML files are handled by the same common editor now.
                                IDE.setDefaultEditor(file, CommonXmlEditor.ID);
}
} else {
                            // if the res folder is null, this means the name is invalid,
                            // in this case we remove whatever android editors that was set
                            // as the default editor.
                            IEditorDescriptor desc = IDE.getDefaultEditor(file);
                            String editorId = desc.getId();
                            if (editorId.startsWith(AdtConstants.EDITORS_NAMESPACE)) {
                                // reset the default editor.
                                IDE.setDefaultEditor(file, null);
}
}
}
}
}
        }, IResourceDelta.ADDED | IResourceDelta.REMOVED);

        monitor.addProjectListener(new IProjectListener() {
            @Override
            public void projectClosed(IProject project) {
                // Close any editors referencing this project
                AdtUtils.closeEditors(project, true /*save*/);
}

            @Override
            public void projectDeleted(IProject project) {
                // Close any editors referencing this project
                AdtUtils.closeEditors(project, false /*save*/);
}

            @Override
            public void projectOpenedWithWorkspace(IProject project) {
            }

            @Override
            public void allProjectsOpenedWithWorkspace() {
            }

            @Override
            public void projectOpened(IProject project) {
            }

            @Override
            public void projectRenamed(IProject project, IPath from) {
            }
        });
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 178d4be..caf617a 100644

//Synthetic comment -- @@ -59,12 +59,14 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
//Synthetic comment -- @@ -1176,4 +1178,124 @@
return new File[0];
}
}

    /**
     * Closes all open editors that are showing a file for the given project. This method
     * should be called when a project is closed or deleted.
     * <p>
     * This method can be called from any thread, but if it is not called on the GUI thread
     * the editor will be closed asynchronously.
     *
     * @param project the project to close all editors for
     * @param save whether unsaved editors should be saved first
     */
    public static void closeEditors(@NonNull final IProject project, final boolean save) {
        final Display display = AdtPlugin.getDisplay();
        if (display == null || display.isDisposed()) {
            return;
        }
        if (display.getThread() != Thread.currentThread()) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    closeEditors(project, save);
                }
            });
            return;
        }

        // Close editors for removed files
        IWorkbench workbench = PlatformUI.getWorkbench();
        for (IWorkbenchWindow window : workbench.getWorkbenchWindows()) {
            for (IWorkbenchPage page : window.getPages()) {
                List<IEditorReference> matching = null;
                for (IEditorReference ref : page.getEditorReferences()) {
                    boolean close = false;
                    try {
                        IEditorInput input = ref.getEditorInput();
                        if (input instanceof IFileEditorInput) {
                            IFileEditorInput fileInput = (IFileEditorInput) input;
                            if (project.equals(fileInput.getFile().getProject())) {
                                close = true;
                            }
                        }
                    } catch (PartInitException ex) {
                        close = true;
                    }
                    if (close) {
                        if (matching == null) {
                            matching = new ArrayList<IEditorReference>(2);
                        }
                        matching.add(ref);
                    }
                }
                if (matching != null) {
                    IEditorReference[] refs = new IEditorReference[matching.size()];
                    page.closeEditors(matching.toArray(refs), save);
                }
            }
        }
    }

    /**
     * Closes all open editors for the given file. Note that a file can be open in
     * more than one editor, for example by using Open With on the file to choose different
     * editors.
     * <p>
     * This method can be called from any thread, but if it is not called on the GUI thread
     * the editor will be closed asynchronously.
     *
     * @param file the file whose editors should be closed.
     * @param save whether unsaved editors should be saved first
     */
    public static void closeEditors(@NonNull final IFile file, final boolean save) {
        final Display display = AdtPlugin.getDisplay();
        if (display == null || display.isDisposed()) {
            return;
        }
        if (display.getThread() != Thread.currentThread()) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    closeEditors(file, save);
                }
            });
            return;
        }

        // Close editors for removed files
        IWorkbench workbench = PlatformUI.getWorkbench();
        for (IWorkbenchWindow window : workbench.getWorkbenchWindows()) {
            for (IWorkbenchPage page : window.getPages()) {
                List<IEditorReference> matching = null;
                for (IEditorReference ref : page.getEditorReferences()) {
                    boolean close = false;
                    try {
                        IEditorInput input = ref.getEditorInput();
                        if (input instanceof IFileEditorInput) {
                            IFileEditorInput fileInput = (IFileEditorInput) input;
                            if (file.equals(fileInput.getFile())) {
                                close = true;
                            }
                        }
                    } catch (PartInitException ex) {
                        close = true;
                    }
                    if (close) {
                        // Found
                        if (matching == null) {
                            matching = new ArrayList<IEditorReference>(2);
                        }
                        matching.add(ref);
                        // We don't break here in case the file is
                        // opened multiple times with different editors.
                    }
                }
                if (matching != null) {
                    IEditorReference[] refs = new IEditorReference[matching.size()];
                    page.closeEditors(matching.toArray(refs), save);
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 2df069f..465c1e6 100644

//Synthetic comment -- @@ -35,9 +35,6 @@
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
//Synthetic comment -- @@ -52,7 +49,6 @@
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
//Synthetic comment -- @@ -105,7 +101,7 @@
* source editor. This can be a no-op if desired.
*/
@SuppressWarnings("restriction") // Uses XML model, which has no non-restricted replacement yet
public abstract class AndroidXmlEditor extends FormEditor {

/** Icon used for the XML source page. */
public static final String ICON_XML_PAGE = "editor_page_source"; //$NON-NLS-1$
//Synthetic comment -- @@ -171,7 +167,6 @@
*/
public AndroidXmlEditor() {
super();
}

@Override
//Synthetic comment -- @@ -528,36 +523,6 @@
}

/**
* Returns the {@link IFile} matching the editor's input or null.
*/
@Nullable
//Synthetic comment -- @@ -587,7 +552,6 @@
xml_model.releaseFromRead();
}
}

if (mTargetListener != null) {
AdtPlugin.getDefault().removeTargetListener(mTargetListener);
//Synthetic comment -- @@ -804,38 +768,6 @@
*/
private void createTextEditor() {
try {
mTextEditor = new StructuredTextEditor();
int index = addPage(mTextEditor, getEditorInput());
mTextPageIndex = index;
//Synthetic comment -- @@ -843,15 +775,6 @@
setPageImage(index,
IconFactory.getInstance().getIcon(ICON_XML_PAGE));

if (!(mTextEditor.getTextViewer().getDocument() instanceof IStructuredDocument)) {
Status status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Error opening the Android XML editor. Is the document an XML file?");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/binaryxml/BinaryXMLDescriber.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/binaryxml/BinaryXMLDescriber.java
//Synthetic comment -- index cff2a6e..00bf7b0 100644

//Synthetic comment -- @@ -16,9 +16,6 @@

package com.android.ide.eclipse.adt.internal.editors.binaryxml;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
//Synthetic comment -- @@ -63,18 +60,10 @@
short type = buf.getShort();
short headerSize = buf.getShort();
int size = buf.getInt(); // chunk size
if (type == RES_XML_TYPE && headerSize == RES_XML_HEADER_SIZE) {
status = VALID;
}
}
return status;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 350fef1..16daae3 100644

//Synthetic comment -- @@ -227,6 +227,10 @@
IStructuredModel model = null;
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
            if (modelManager == null) {
                // This can happen if incremental lint is running right as Eclipse is shutting down
                return null;
            }
model = modelManager.getModelForRead(file);
if (model instanceof IDOMModel) {
context.setProperty(MODEL_PROPERTY, model);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/GlobalProjectMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/GlobalProjectMonitor.java
//Synthetic comment -- index b623cec..7cb4e94 100644

//Synthetic comment -- @@ -516,5 +516,4 @@
notifyResourceEventEnd();
}
};
}







