//<Beginning of snippet n. 0>


import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

page.saveEditor(mLayoutEditor, false);

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource xmlFile = workspace.findMember(relativePath);
    if (xmlFile instanceof IFile) {
        IFile leavingFile = graphicalEditor.getEditedFile();
        Reference next = Reference.create(leavingFile);

        try {
            IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);

            if (openAlready != null) {
                if (openAlready instanceof LayoutEditor) {
                    LayoutEditor editor = (LayoutEditor) openAlready;
                    GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                    if (gEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                        gEditor.showIn(next);
                    }
                }
            } else {
                try {
                    QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
                    String state = AdtPlugin.getFileProperty(leavingFile, qname);
                    if (state != null) {
                        xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
                    }
                } catch (CoreException e) {
                    AdtPlugin.log(e, "Error setting initial state for file %s", leavingFile.getName());
                }

                if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                    try {
                        xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
                    } catch (CoreException e) {
                        AdtPlugin.log(e, "Error setting include for xmlFile %s", xmlFile.getName());
                    }
                }
            }

            EditorUtility.openInEditor(xmlFile, true);
            return;
        } catch (PartInitException ex) {
            AdtPlugin.log(ex, "Can't open %$1s", url); //$NON-NLS-1$
        }
    }
} else {
// It's not a path in the workspace; look externally

//<End of snippet n. 0>




```java
//<Beginning of snippet n. 1>


IWorkbenchPage page = sourceEditor.getEditorSite().getPage();
IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource file = workspace.findMember(relativePath);
    if (file instanceof IFile) {
        try {
            AdtPlugin.openFile((IFile) file, region);
            return;
        } catch (PartInitException ex) {
            AdtPlugin.log(ex, "Can't open %$1s", filePath); //$NON-NLS-1$
        }
    }
} else {
// It's not a path in the workspace; look externally

//<End of snippet n. 1>

