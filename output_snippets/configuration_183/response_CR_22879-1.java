//<Beginning of snippet n. 0>


import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchPage;

page.saveEditor(mLayoutEditor, false);

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();

IFile validFile = null;

if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource xmlFile = workspace.findMember(relativePath);
    if (xmlFile instanceof IFile) {
        validFile = (IFile) xmlFile;
    }
} 

if (validFile == null) {
    validFile = handleExternalFile(filePath);
}

if (validFile != null) {
    IFile leavingFile = graphicalEditor.getEditedFile();
    Reference next = Reference.create(leavingFile);

    try {
        IEditorPart openAlready = EditorUtility.isOpenInEditor(validFile);
        if (openAlready != null) {
            if (openAlready instanceof LayoutEditor) {
                LayoutEditor editor = (LayoutEditor) openAlready;
                GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                if (gEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                    gEditor.showIn(next);
                }
            }
        } else {
            setInitialState(validFile, leavingFile);
            if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                try {
                    validFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
                } catch (CoreException e) {
                    AdtPlugin.log(e, "Failed to set inclusion property on xmlFile");
                }
            }
        }
        EditorUtility.openInEditor(validFile, true);
    } catch (PartInitException ex) {
        AdtPlugin.log(ex, "Can't open %$1s", validFile.getLocation().toString()); //$NON-NLS-1$
    }
}

// Consolidated method for setting initial state
private void setInitialState(IResource xmlFile, IFile leavingFile) {
    try {
        QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
        String state = AdtPlugin.getFileProperty(leavingFile, qname);
        xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
    } catch (CoreException e) {
        AdtPlugin.log(e, "Failed to set initial state on xmlFile");
    }
}

private IFile handleExternalFile(IPath filePath) {
    IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
    IPath workspacePath = workspace.getLocation();
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource file = workspace.findMember(relativePath);

    if (file instanceof IFile) {
        return (IFile) file;
    }
    return null;
}


//<End of snippet n. 0>




 // <Beginning of snippet n. 1>


IWorkbenchPage page = sourceEditor.getEditorSite().getPage();
IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
IFile validFile = null;

if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource file = workspace.findMember(relativePath);
    if (file instanceof IFile) {
        validFile = (IFile) file;
    }
} 

if (validFile == null) {
    validFile = handleExternalFile(filePath);
}

if (validFile != null) {
    try {
        AdtPlugin.openFile(validFile, region);
    } catch (PartInitException ex) {
        AdtPlugin.log(ex, "Can't open %$1s", filePath); //$NON-NLS-1$
    }
}

//<End of snippet n. 1>
