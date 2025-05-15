//<Beginning of snippet n. 0>


import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

page.saveEditor(mLayoutEditor, false);

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
IFile xmlFile = null; // Declare xmlFile in scope for error handling

if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource resource = workspace.findMember(relativePath);
    
    if (resource instanceof IFile) {
        xmlFile = (IFile) resource;
    } else {
        xmlFile = null; // Handle non-IFile resources gracefully
    }
    
    try {
        IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);

        if (openAlready != null) {
            if (openAlready instanceof LayoutEditor) {
                LayoutEditor editor = (LayoutEditor) openAlready;
                GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                if (gEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                    gEditor.showIn(Reference.create(xmlFile));
                }
            }
        } else {
            try {
                QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
                String state = AdtPlugin.getFileProperty(xmlFile, qname);
                xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
            } catch (CoreException e) {
                AdtPlugin.log(e, "Error setting initial state for %s", xmlFile.getName());
            }

            if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                try {
                    xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, Reference.create(xmlFile));
                } catch (CoreException e) {
                    AdtPlugin.log(e, "Error setting include property for %s", xmlFile.getName());
                }
            }
        }

        EditorUtility.openInEditor(xmlFile, true);
        return;
    } catch (PartInitException ex) {
        AdtPlugin.log(ex, "Can't open %$1s", xmlFile != null ? xmlFile.getLocation() : "");
    }
} else {
    // Handle external file lookup
    IFileStore fileStore = EFS.getLocalFileSystem().getStore(filePath);
    try {
        if (fileStore.fetchInfo().exists()) {
            AdtPlugin.openFileAsExternal(fileStore);
        } else {
            AdtPlugin.log(new FileNotFoundException("File not found: " + filePath), "Can't open external file");
        }
    } catch (CoreException e) {
        AdtPlugin.log(e, "Error opening external file %s", filePath);
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


IWorkbenchPage page = sourceEditor.getEditorSite().getPage();
IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
IFile file = null;

if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource resource = workspace.findMember(relativePath);
    
    if (resource instanceof IFile) {
        file = (IFile) resource;
        try {
            AdtPlugin.openFile(file, region);
            return;
        } catch (PartInitException ex) {
            AdtPlugin.log(ex, "Can't open %$1s", filePath); //$NON-NLS-1$
        }
    }
} else {
    // Handle external file lookup
    IFileStore fileStore = EFS.getLocalFileSystem().getStore(filePath);
    try {
        if (fileStore.fetchInfo().exists()) {
            AdtPlugin.openFileAsExternal(fileStore);
        } else {
            AdtPlugin.log(new FileNotFoundException("File not found: " + filePath), "Can't open external file");
        }
    } catch (CoreException e) {
        AdtPlugin.log(e, "Error opening external file %s", filePath);
    }
}

//<End of snippet n. 1>