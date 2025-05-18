//<Beginning of snippet n. 0>


import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

page.saveEditor(mLayoutEditor, false);

IFile leavingFile = graphicalEditor.getEditedFile();
if (leavingFile == null) {
    return;
}

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource xmlFile = workspace.findMember(relativePath);
    if (xmlFile instanceof IFile) {
        Reference next = Reference.create(leavingFile);
        try {
            IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);

            if (openAlready instanceof LayoutEditor) {
                LayoutEditor editor = (LayoutEditor) openAlready;
                GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                if (gEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                    gEditor.showIn(next);
                }
                return;
            }

            QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
            String state = AdtPlugin.getFileProperty(leavingFile, qname);
            xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
            
            if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
            }

            EditorUtility.openInEditor(xmlFile, true);
        } catch (CoreException e) {
            AdtPlugin.log(e, "Error processing file: %s", leavingFile.getName());
        }
    }
} else {
    handleExternalFile(filePath);
}

//<End of snippet n. 0>

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
            AdtPlugin.log(ex, "Can't open %s", filePath);
        }
    }
} else {
    handleExternalFile(filePath);
}

//<End of snippet n. 1>