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
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -61,6 +62,7 @@
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
//Synthetic comment -- @@ -128,15 +130,6 @@
* The activator class controls the plug-in life cycle
*/
public class AdtPlugin extends AbstractUIPlugin implements ILogger {
    /**
     * Temporary logging code to help track down
     * http://code.google.com/p/android/issues/detail?id=15003
     *
     * Deactivated right now.
     * TODO remove this and associated logging code once we're done with issue 15003.
     */
    public static final boolean DEBUG_XML_FILE_INIT = false;

/** The plug-in ID */
public static final String PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$

//Synthetic comment -- @@ -1508,7 +1501,7 @@

if (mResourceMonitor != null) {
try {
                setupDefaultEditor(mResourceMonitor);
ResourceManager.setup(mResourceMonitor);
LintDeltaProcessor.startListening(mResourceMonitor);
} catch (Throwable t) {
//Synthetic comment -- @@ -1570,125 +1563,103 @@
}

/**
     * Sets up the editor to register default editors for resource files when needed.
*
* This is called by the {@link AdtPlugin} during initialization.
*
* @param monitor The main Resource Monitor object.
*/
    public void setupDefaultEditor(GlobalProjectMonitor monitor) {
monitor.addFileListener(new IFileListener() {

            /* (non-Javadoc)
             * Sent when a file changed.
             * @param file The file that changed.
             * @param markerDeltas The marker deltas for the file.
             * @param kind The change kind. This is equivalent to
             * {@link IResourceDelta#accept(IResourceDeltaVisitor)}
             *
             * @see IFileListener#fileChanged
             */
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
int kind, @Nullable String extension, int flags) {
                if (flags == IResourceDelta.MARKERS) {
                    // ONLY the markers changed: not relevant to this listener
return;
}

                if (AdtConstants.EXT_XML.equals(extension)) {
                    // The resources files must have a file path similar to
                    //    project/res/.../*.xml
                    // There is no support for sub folders, so the segment count must be 4
                    if (DEBUG_XML_FILE_INIT) {
                        AdtPlugin.log(IStatus.INFO, "fileChanged %1$s",
                            file.getFullPath().toOSString());
                    }

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
                                    resourceXmlAdded(file, type);
                                } else if (kind == IResourceDelta.CHANGED) {
                                    resourceXmlChanged(file, type);
                                }
                            } else {
                                if (DEBUG_XML_FILE_INIT) {
                                    AdtPlugin.log(IStatus.INFO, "  The resource folder was null");
                                }

                                // if the res folder is null, this means the name is invalid,
                                // in this case we remove whatever android editors that was set
                                // as the default editor.
                                IEditorDescriptor desc = IDE.getDefaultEditor(file);
                                String editorId = desc.getId();
                                if (DEBUG_XML_FILE_INIT) {
                                    AdtPlugin.log(IStatus.INFO, "Old editor id=%1$s", editorId);
                                }
                                if (editorId.startsWith(AdtConstants.EDITORS_NAMESPACE)) {
                                    // reset the default editor.
                                    IDE.setDefaultEditor(file, null);
                                    if (DEBUG_XML_FILE_INIT) {
                                        AdtPlugin.log(IStatus.INFO, "  Resetting editor id to default");
                                    }
                                }
}
} else {
                            if (DEBUG_XML_FILE_INIT) {
                                AdtPlugin.log(IStatus.INFO, "    Not in resources/, ignoring");
}
}
}
}
}

            /**
             * A new file {@code /res/type-config/some.xml} was added.
             *
             * @param file The file added to the workspace. Guaranteed to be a *.xml file.
             * @param type The resource type.
             */
            private void resourceXmlAdded(IFile file, ResourceFolderType type) {
                if (DEBUG_XML_FILE_INIT) {
                    AdtPlugin.log(IStatus.INFO, "resourceAdded %1$s - type=%1$s",
                        file.getFullPath().toOSString(), type);
                }
                // All the /res XML files are handled by the same common editor now.
                IDE.setDefaultEditor(file, CommonXmlEditor.ID);
}

            /**
             * An existing file {@code /res/type-config/some.xml} was changed.
             *
             * @param file The file added to the workspace. Guaranteed to be a *.xml file.
             * @param type The resource type.
             */
            private void resourceXmlChanged(IFile file, ResourceFolderType type) {
                // Nothing to do here anymore.
                // This used to be useful to detect that a /res/xml/something.xml
                // changed from an empty XML to one with a root now that OtherXmlEditor
                // could handle. Since OtherXmlEditor is now a default, it will always
                // handle such files and we don't need this anymore.

                //if (DEBUG_XML_FILE_INIT) {
                //    AdtPlugin.log(IStatus.INFO, "resourceChanged %1$s - type=%1$s",
                //        file.getFullPath().toOSString(), type);
                //}
}

        }, IResourceDelta.ADDED | IResourceDelta.CHANGED);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 178d4be..caf617a 100644

//Synthetic comment -- @@ -59,12 +59,14 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
//Synthetic comment -- @@ -1176,4 +1178,124 @@
return new File[0];
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 2df069f..465c1e6 100644

//Synthetic comment -- @@ -35,9 +35,6 @@
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
//Synthetic comment -- @@ -52,7 +49,6 @@
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
//Synthetic comment -- @@ -105,7 +101,7 @@
* source editor. This can be a no-op if desired.
*/
@SuppressWarnings("restriction") // Uses XML model, which has no non-restricted replacement yet
public abstract class AndroidXmlEditor extends FormEditor implements IResourceChangeListener {

/** Icon used for the XML source page. */
public static final String ICON_XML_PAGE = "editor_page_source"; //$NON-NLS-1$
//Synthetic comment -- @@ -171,7 +167,6 @@
*/
public AndroidXmlEditor() {
super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
}

@Override
//Synthetic comment -- @@ -528,36 +523,6 @@
}

/**
     * Notifies this listener that some resource changes
     * are happening, or have already happened.
     *
     * Closes all project files on project close.
     * @see IResourceChangeListener
     */
    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            IFile file = getInputFile();
            if (file != null && file.getProject().equals(event.getResource())) {
                final IEditorInput input = getEditorInput();
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        // FIXME understand why this code is accessing the current window's pages,
                        // if that's *this* instance, we have a local pages member from the super
                        // class we can use directly. If this is justified, please explain.
                        IWorkbenchPage[] windowPages = getSite().getWorkbenchWindow().getPages();
                        for (int i = 0; i < windowPages.length; i++) {
                            IEditorPart editorPart = windowPages[i].findEditor(input);
                            windowPages[i].closeEditor(editorPart, true);
                        }
                    }
                });
            }
        }
    }

    /**
* Returns the {@link IFile} matching the editor's input or null.
*/
@Nullable
//Synthetic comment -- @@ -587,7 +552,6 @@
xml_model.releaseFromRead();
}
}
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);

if (mTargetListener != null) {
AdtPlugin.getDefault().removeTargetListener(mTargetListener);
//Synthetic comment -- @@ -804,38 +768,6 @@
*/
private void createTextEditor() {
try {
            if (AdtPlugin.DEBUG_XML_FILE_INIT) {
                AdtPlugin.log(
                        IStatus.ERROR,
                        "%s.createTextEditor: input=%s %s",
                        this.getClass(),
                        getEditorInput() == null ? "null" : getEditorInput().getClass(),
                        getEditorInput() == null ? "null" : getEditorInput().toString()
                        );

                org.eclipse.core.runtime.IAdaptable adaptable= getEditorInput();
                IFile file1 = (IFile)adaptable.getAdapter(IFile.class);
                org.eclipse.core.runtime.IPath location= file1.getFullPath();
                org.eclipse.core.resources.IWorkspaceRoot workspaceRoot= ResourcesPlugin.getWorkspace().getRoot();
                IFile file2 = workspaceRoot.getFile(location);

                try {
                    org.eclipse.core.runtime.content.IContentDescription desc = file2.getContentDescription();
                    org.eclipse.core.runtime.content.IContentType type = desc.getContentType();

                    AdtPlugin.log(IStatus.ERROR,
                            "file %s description %s %s; contentType %s %s",
                            file2,
                            desc == null ? "null" : desc.getClass(),
                            desc == null ? "null" : desc.toString(),
                            type == null ? "null" : type.getClass(),
                            type == null ? "null" : type.toString());

                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }

mTextEditor = new StructuredTextEditor();
int index = addPage(mTextEditor, getEditorInput());
mTextPageIndex = index;
//Synthetic comment -- @@ -843,15 +775,6 @@
setPageImage(index,
IconFactory.getInstance().getIcon(ICON_XML_PAGE));

            if (AdtPlugin.DEBUG_XML_FILE_INIT) {
                AdtPlugin.log(IStatus.ERROR, "Found document class: %1$s, file=%2$s",
                        mTextEditor.getTextViewer().getDocument() != null ?
                                mTextEditor.getTextViewer().getDocument().getClass() :
                                "null",
                                getEditorInput()
                        );
            }

if (!(mTextEditor.getTextViewer().getDocument() instanceof IStructuredDocument)) {
Status status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Error opening the Android XML editor. Is the document an XML file?");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/binaryxml/BinaryXMLDescriber.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/binaryxml/BinaryXMLDescriber.java
//Synthetic comment -- index cff2a6e..00bf7b0 100644

//Synthetic comment -- @@ -16,9 +16,6 @@

package com.android.ide.eclipse.adt.internal.editors.binaryxml;

import com.android.ide.eclipse.adt.AdtPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
//Synthetic comment -- @@ -63,18 +60,10 @@
short type = buf.getShort();
short headerSize = buf.getShort();
int size = buf.getInt(); // chunk size
            if (AdtPlugin.DEBUG_XML_FILE_INIT) {
                AdtPlugin.log(IStatus.ERROR, "BinaryXML: type 0x%04x, headerSize 0x%04x, size 0x%08x", type, headerSize, size);
            }
if (type == RES_XML_TYPE && headerSize == RES_XML_HEADER_SIZE) {
status = VALID;
}
}
        if (AdtPlugin.DEBUG_XML_FILE_INIT) {
            AdtPlugin.log(IStatus.ERROR, "BinaryXML status: %d (%s)",
                    status,
                    status == VALID ? "VALID" : "INVALID");
        }
return status;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 350fef1..16daae3 100644

//Synthetic comment -- @@ -227,6 +227,10 @@
IStructuredModel model = null;
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
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







