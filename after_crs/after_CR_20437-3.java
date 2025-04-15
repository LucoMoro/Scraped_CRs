/*Improve hyperlink resolution for configurations

The hyperlink resolver would only look for values in the base values/
folder. This did not work well for resources that are ONLY defined in
specific configurations. This changeset improves the search to look in
all eligible resource folders.

It now also considers the configuration chooser in the designtab (if
opened), and uses this configuration first. Therefore, if you for
example have chosen to view a particular language, hyperlink
resolution will jump to the specific translation string rather than
the base string as before.

Change-Id:Ia97a3f45bf454cc28378387b219af0fdd855a902*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 263b6c8..2be283d 100644

//Synthetic comment -- @@ -2291,4 +2291,13 @@
return Collections.emptyList();
}
}

    /**
     * Return this editor's current configuration
     *
     * @return the current configuration
     */
    public FolderConfiguration getConfiguration() {
        return mConfigComposite.getCurrentConfig();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index a2e664b..76b0118 100644

//Synthetic comment -- @@ -37,15 +37,19 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -98,6 +102,7 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
//Synthetic comment -- @@ -604,6 +609,15 @@

/** Looks up the project member of the given type and the given name */
private static IResource findNonValueFile(IProject project, ResourceType type, String name) {
        ProjectResources resources = ResourceManager.getInstance().getProjectResources(project);
        FolderConfiguration configuration = getConfiguration();
        if (configuration != null) {
            IResource file = findFileByConfiguration(type, name, resources, configuration);
            if (file != null) {
                return file;
            }
        }

String relativePath;
IResource member;
String folder = WS_RESOURCES + WS_SEP + type.getName();
//Synthetic comment -- @@ -623,9 +637,6 @@
if (member == null) {
// Still couldn't find the member; it must not be defined in a "base" directory
// like "layout"; look in various variations
ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
for (ResourceFolderType folderType : folderTypes) {
if (folderType != ResourceFolderType.VALUES) {
//Synthetic comment -- @@ -646,10 +657,71 @@
}
}

        if (configuration == null && member == null) {
            member = findFileByConfiguration(type, name, resources, configuration);
        }

return member;
}

/**
     * Find a file for a given named resource, associated with a given folder
     * configuration
     */
    private static IResource findFileByConfiguration(ResourceType type, String name,
            ProjectResources resources, FolderConfiguration configuration) {
        List<ResourceFile> sourceFiles = resources.getSourceFiles(type, name, configuration);
        if (sourceFiles != null) {
            for (ResourceFile resourceFile : sourceFiles) {
                if (resourceFile.getFile() instanceof IFileWrapper) {
                    return ((IFileWrapper) resourceFile.getFile()).getIFile();
                }
            }
        }
        return null;
    }

    /**
     * Returns the current configuration, if the associated UI editor has been initialized
     * and has an associated configuration
     *
     * @return the configuration for this file, or null
     */
    private static FolderConfiguration getConfiguration() {
        IEditorPart editor = getEditor();
        if (editor != null) {
            if (editor instanceof LayoutEditor) {
                LayoutEditor layoutEditor = (LayoutEditor) editor;
                GraphicalEditorPart graphicalEditor = layoutEditor.getGraphicalEditor();
                if (graphicalEditor != null) {
                    return graphicalEditor.getConfiguration();
                } else {
                    // TODO: Could try a few more things to get the configuration:
                    // (1) try to look at the file.getPersistentProperty(NAME_CONFIG_STATE)
                    //    which will return previously saved state. This isn't necessary today
                    //    since no editors seem to be lazily initialized.
                    // (2) attempt to use the configuration from any of the other open
                    //    files, especially files in the same directory as this one.
                }
            }

            // Create a configuration from the current file
            IEditorInput editorInput = editor.getEditorInput();
            if (editorInput instanceof FileEditorInput) {
                IFile file = ((FileEditorInput) editorInput).getFile();
                IProject project = file.getProject();
                ProjectResources pr = ResourceManager.getInstance().getProjectResources(project);
                ResourceFolder resFolder = pr.getResourceFolder((IFolder) file.getParent());
                if (resFolder != null) {
                    return resFolder.getConfiguration();
                }
            }
        }

        return null;
    }

    /**
* Finds a resource of the given name in the given folder, searching for possible file
* extensions
*/
//Synthetic comment -- @@ -718,6 +790,14 @@
// Search within the files in the values folder and find the value which defines
// the given resource. To be efficient, we will only parse XML files that contain
// a string match of the given token name.
        FolderConfiguration configuration = getConfiguration();
        if (configuration != null) {
            Pair<IFile, IRegion> target = findValueDefinitionByConfiguration(project, type, name,
                    configuration);
            if (target != null) {
                return target;
            }
        }

String folderPath = AndroidConstants.WS_RESOURCES + AndroidConstants.WS_SEP
+ SdkConstants.FD_VALUES;
//Synthetic comment -- @@ -731,7 +811,7 @@
IFile file = (IFile) resource;
// Must have an XML extension
if (EXT_XML.equals(file.getFileExtension())) {
                            Pair<IFile, IRegion> target = findValueInXml(type, name, file, false);
if (target != null) {
return target;
}
//Synthetic comment -- @@ -742,19 +822,47 @@
AdtPlugin.log(e, ""); //$NON-NLS-1$
}
}

        if (configuration == null) {
            return findValueDefinitionByConfiguration(project, type, name, null);
        }

        return null;
    }

    /** Searches for the given resource for a specific configuration (which may be null) */
    private static Pair<IFile, IRegion> findValueDefinitionByConfiguration(IProject project,
            ResourceType type, String name, FolderConfiguration configuration) {
        ProjectResources resources = ResourceManager.getInstance().getProjectResources(project);
        List<ResourceFile> sourceFiles = resources.getSourceFiles(type, name, configuration);
        if (sourceFiles != null) {
            for (ResourceFile resourceFile : sourceFiles) {
                if (resourceFile.getFile() instanceof IFileWrapper) {
                    IFile file = ((IFileWrapper) resourceFile.getFile()).getIFile();
                    if (EXT_XML.equals(file.getFileExtension())) {
                        boolean skipSkim = true; // getSourceFiles() only return matching files
                        Pair<IFile, IRegion> target = findValueInXml(type, name, file, skipSkim);
                        if (target != null) {
                            return target;
                        }
                    }
                }
            }
        }

return null;
}

/** Parses the given file and locates a definition of the given resource */
private static Pair<IFile, IRegion> findValueInXml(
            ResourceType type, String name, IFile file, boolean skipSkim) {
IStructuredModel model = null;
try {
model = StructuredModelManager.getModelManager().getExistingModelForRead(file);
if (model == null) {
// There is no open or cached model for the file; see if the file looks
// like it's interesting (content contains the String name we are looking for)
                if (skipSkim || AdtPlugin.fileContains(file, name)) {
// Yes, so parse content
model = StructuredModelManager.getModelManager().getModelForRead(file);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java
//Synthetic comment -- index dc022c7..a444cdc 100644

//Synthetic comment -- @@ -123,4 +123,9 @@

return null;
}

    @Override
    public String toString() {
        return mFile.toString();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFolderWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFolderWrapper.java
//Synthetic comment -- index 265ea33..3c00485 100644

//Synthetic comment -- @@ -171,4 +171,9 @@

return null;
}

    @Override
    public String toString() {
        return mFolder.toString();
    }
}







