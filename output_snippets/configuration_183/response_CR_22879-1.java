//<Beginning of snippet n. 0>


import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

page.saveEditor(mLayoutEditor, false);

IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
IPath workspacePath = workspace.getLocation();
if (workspacePath.isPrefixOf(filePath)) {
    IPath relativePath = filePath.makeRelativeTo(workspacePath);
    IResource xmlFile = workspace.findMember(relativePath);
    if (xmlFile != null) {
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
                    xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
                } catch (CoreException e) {
                    AdtPlugin.log(e, "Could not set session property for %s", xmlFile.getName());
                }

                if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                    try {
                        xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
                    } catch (CoreException e) {
                        AdtPlugin.log(e, "Could not set include property for %s", xmlFile.getName());
                    }
                }
            }

            EditorUtility.openInEditor(xmlFile, true);
            return;
        } catch (PartInitException ex) {
            AdtPlugin.log(ex, "Can't open %$1s", url); //$NON-NLS-1$
        }
    } else {
        // Handle the scenario for imported project files not in the workspace
        IFileStore fileStore = EFS.getStore(url);
        if (fileStore.fetchInfo().exists()) {
            try {
                // Open from external source
                EditorUtility.openInEditor(fileStore, true);
                return;
            } catch (PartInitException ex) {
                AdtPlugin.log(ex, "Can't open external file %$1s", url); //$NON-NLS-1$
            }
        }
    }
} else {
    // It's not a path in the workspace; look externally

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
            AdtPlugin.log(ex, "Can't open %$1s", filePath); //$NON-NLS-1$
        }
    } else {
        // Handle case for imported project files not in the workspace
        IFileStore fileStore = EFS.getStore(url);
        if (fileStore.fetchInfo().exists()) {
            try {
                // Open from external source
                AdtPlugin.openFile(fileStore, region);
                return;
            } catch (PartInitException ex) {
                AdtPlugin.log(ex, "Can't open external file %$1s", url); //$NON-NLS-1$
            }
        }
    }
} else {
    // It's not a path in the workspace; look externally

//<End of snippet n. 1>