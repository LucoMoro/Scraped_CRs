/*Fix bug in editor open utility

The code to open a file (called by Go To Declaration, Show Include
etc) handles two scenarios:

(1) The file is in the workspace - open using Eclipse IFile mechanism
(2) The file is outside the workspace - open using the fallback
    external storage (which means you get a plain XML editor)

There's a third scenario: the file is not in the workspace, but is
part of a project in the workspace so it does have a valid IFile.
(This can happen if you import a project but choose not to copy the
contents into the workspace).  This changeset adjusts the code to open
up an editor such that it handles this third scenario and we get our
own XML editors for these types of files.

Change-Id:I040e1b899cd38bbda3fcf3475cc4dfb541d10016*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index c87b669..4c5aaf6 100755

//Synthetic comment -- @@ -38,7 +38,6 @@
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -902,53 +901,56 @@
page.saveEditor(mLayoutEditor, false);

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
        IFile xmlFile = null;
IPath workspacePath = workspace.getLocation();
if (workspacePath.isPrefixOf(filePath)) {
IPath relativePath = filePath.makeRelativeTo(workspacePath);
            xmlFile = (IFile) workspace.findMember(relativePath);
        } else if (filePath.isAbsolute()) {
            xmlFile = workspace.getFileForLocation(filePath);
        }
        if (xmlFile != null) {
            IFile leavingFile = graphicalEditor.getEditedFile();
            Reference next = Reference.create(graphicalEditor.getEditedFile());

            try {
                IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);

                // Show the included file as included within this click source?
                if (openAlready != null) {
                    if (openAlready instanceof LayoutEditor) {
                        LayoutEditor editor = (LayoutEditor)openAlready;
                        GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                        if (gEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                            gEditor.showIn(next);
}
}
                } else {
                    try {
                        // Set initial state of a new file
                        // TODO: Only set rendering target portion of the state
                        QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
                        String state = AdtPlugin.getFileProperty(leavingFile, qname);
                        xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE,
                                state);
                    } catch (CoreException e) {
                        // pass
                    }

                    if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                        try {
                            xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
                        } catch (CoreException e) {
                            // pass - worst that can happen is that we don't
                            //start with inclusion
                        }
                    }
}

                EditorUtility.openInEditor(xmlFile, true);
                return;
            } catch (PartInitException ex) {
                AdtPlugin.log(ex, "Can't open %$1s", url); //$NON-NLS-1$
}
} else {
// It's not a path in the workspace; look externally








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 069d475..ae84f13 100644

//Synthetic comment -- @@ -540,16 +540,22 @@
IWorkbenchPage page = sourceEditor.getEditorSite().getPage();
IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
        IFile file = null;
if (workspacePath.isPrefixOf(filePath)) {
IPath relativePath = filePath.makeRelativeTo(workspacePath);
            IResource member = workspace.findMember(relativePath);
            if (member instanceof IFile) {
                file = (IFile) member;
            }
        } else if (filePath.isAbsolute()) {
            file = workspace.getFileForLocation(filePath);
        }
        if (file != null) {
            try {
                AdtPlugin.openFile(file, region);
                return;
            } catch (PartInitException ex) {
                AdtPlugin.log(ex, "Can't open %$1s", filePath); //$NON-NLS-1$
}
} else {
// It's not a path in the workspace; look externally







