/*Improve hyperlink resolution for configurations

The hyperlink resolver would only look for values in the base values/
folder. This did not work well for resources that are ONLY defined in
specific configurations. This changeset improves the search to look in
all eligible resource folders. It also uses support from the layout
library to more quickly identify the right files to check.

The hyperlink resolver now also considers the configuration chooser in
the designtab (if opened), and uses this configuration
first. Therefore, if you for example have chosen to view a particular
language, hyperlink resolution will jump to the specific translation
string rather than the base string as before.

A few other fixes are included:

- Look in the current file for @id references to resolve first (common
  in a layout where you have @id references to attachments)
- Fix search for @attr/ attributes
- Fix bug where attributes inside a custom view class would be ignored
  and it would jump to the custom class instead

Change-Id:Ia97a3f45bf454cc28378387b219af0fdd855a902*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 9090baf..73ad04a 100644

//Synthetic comment -- @@ -1409,7 +1409,7 @@
* target set.
*
*/
    public IAndroidTarget getRenderingTarget() {
// if the SDK is null no targets are loaded.
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk == null) {
//Synthetic comment -- @@ -2323,4 +2323,13 @@
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
//Synthetic comment -- index a2e664b..f593619 100644

//Synthetic comment -- @@ -24,10 +24,9 @@
import static com.android.ide.eclipse.adt.AndroidConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.FN_RESOURCE_BASE;
import static com.android.ide.eclipse.adt.AndroidConstants.FN_RESOURCE_CLASS;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.ROOT_ELEMENT;
import static com.android.sdklib.SdkConstants.FD_VALUES;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -37,19 +36,23 @@
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
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.util.Pair;

import org.apache.xerces.parsers.DOMParser;
//Synthetic comment -- @@ -98,6 +101,7 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
//Synthetic comment -- @@ -122,6 +126,7 @@
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -230,6 +235,10 @@

/** Returns true if this represents a {@code <foo.bar.Baz>} custom view class element */
private static boolean isClassElement(XmlContext context) {
        if (context.getAttribute() != null) {
            // Don't match the outer element if the user is hovering over a specific attribute
            return false;
        }
// If the element looks like a fully qualified class name (e.g. it's a custom view
// element) offer it as a link
String tag = context.getElement().getTagName();
//Synthetic comment -- @@ -479,7 +488,6 @@
status.setErrorMessage(message);
}

/**
* Opens a framework resource's declaration
*
//Synthetic comment -- @@ -494,25 +502,15 @@
return false;
}

ResourceType type = parsedUrl.getFirst();
String name = parsedUrl.getSecond();

// Attempt to open files, such as layouts and drawables in @android?
if (isFileResource(type)) {
            ProjectResources frameworkResources = getResources(project, true /* framework */);
            if (frameworkResources == null) {
return false;
}
Map<String, Map<String, ResourceValue>> configuredResources =
frameworkResources.getConfiguredResources(new FolderConfiguration());

//Synthetic comment -- @@ -560,22 +558,93 @@
break;
}
}

            // Attempt to find files via ProjectResources.getSourceFiles(); this
            // is done after the above search since this search won't resolve references
            FolderConfiguration configuration = getConfiguration();
            List<ResourceFile> sourceFiles = frameworkResources.getSourceFiles(type, name,
                    configuration);
            for (ResourceFile file : sourceFiles) {
                String location = file.getFile().getOsLocation();
                if (new File(location).exists()) {
                    Path path = new Path(location);
                    openPath(path, null, -1);
return true;
}
}

        } else if (isValueResource(type)) {
            FolderConfiguration configuration = getConfiguration();
            Pair<File, Integer> match = findFrameworkValueByConfig(project, type, name,
                    configuration);
            if (match == null && configuration != null) {
                match = findFrameworkValueByConfig(project, type, name, null);
            }

            if (match != null) {
                Path path = new Path(match.getFirst().getPath());
                openPath(path, null, match.getSecond());
                return true;
            }
}

return false;
}

    /** Return the set of matching source files for the given resource type and name */
    private static List<ResourceFile> getResourceFiles(IProject project,
            ResourceType type, String name, boolean framework,
            FolderConfiguration configuration) {
        ProjectResources resources = getResources(project, framework);
        if (resources == null) {
            return null;
        }
        List<ResourceFile> sourceFiles = resources.getSourceFiles(type, name, configuration);
        if (sourceFiles != null) {
            if (sourceFiles.size() > 1 && configuration == null) {
                // Sort all the files in the base values/ folder first, followed by
                // everything else
                List<ResourceFile> first = new ArrayList<ResourceFile>();
                List<ResourceFile> second = new ArrayList<ResourceFile>();
                for (ResourceFile file : sourceFiles) {
                    if (FD_VALUES.equals(file.getFolder().getFolder().getName())) {
                        // Found match in value
                        first.add(file);
                    } else {
                        second.add(file);
                    }
                }
                first.addAll(second);
                sourceFiles = first;
            }
        }

        return sourceFiles;
    }

    /** Searches for the given resource for a specific configuration (which may be null) */
    private static Pair<File, Integer> findFrameworkValueByConfig(IProject project,
            ResourceType type, String name, FolderConfiguration configuration) {
        List<ResourceFile> sourceFiles = getResourceFiles(project, type, name, true /* framework*/,
                configuration);
        if (sourceFiles != null) {
            for (ResourceFile resourceFile : sourceFiles) {
                if (resourceFile.getFile() instanceof FileWrapper) {
                    File file = ((FileWrapper) resourceFile.getFile());
                    if (file.getName().endsWith(EXT_XML)) {
                        // Must have an XML extension
                        Pair<File, Integer> match = findValueInXml(type, name, file);
                        if (match != null) {
                            return match;
                        }
                    }
                }
            }
        }

        return null;
    }

/** Opens a Java class for the given fully qualified class name */
private static boolean openJavaClass(IProject project, String fqcn) {
if (fqcn == null) {
//Synthetic comment -- @@ -604,79 +673,132 @@

/** Looks up the project member of the given type and the given name */
private static IResource findNonValueFile(IProject project, ResourceType type, String name) {
        ProjectResources resources = getResources(project, false /* not framework */);
        if (resources == null) {
            return null;
        }
        FolderConfiguration configuration = getConfiguration();
        if (configuration != null) {
            IResource file = findFileByConfig(type, name, resources, configuration);
            if (file != null) {
                return file;
}
}

        return findFileByConfig(type, name, resources, null);
}

/**
     * Find a file for a given named resource, associated with a given folder
     * configuration
*/
    private static IResource findFileByConfig(ResourceType type, String name,
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

    /** Returns the {@link IAndroidTarget} to be used for looking up system resources */
    private static IAndroidTarget getTarget(IProject project) {
        IEditorPart editor = getEditor();
        if (editor != null) {
            if (editor instanceof LayoutEditor) {
                LayoutEditor layoutEditor = (LayoutEditor) editor;
                GraphicalEditorPart graphicalEditor = layoutEditor.getGraphicalEditor();
                if (graphicalEditor != null) {
                    return graphicalEditor.getRenderingTarget();
                }
            }
        }

        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk == null) {
            return null;
        }

        return currentSdk.getTarget(project);
    }

    /** Return either the project resources or the framework resources (or null) */
    private static ProjectResources getResources(IProject project, boolean framework) {
        if (framework) {
            IAndroidTarget target = getTarget(project);
            if (target == null) {
                return null;
            }
            AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
            if (data == null) {
                return null;
            }
            return data.getFrameworkResources();
        } else {
            return ResourceManager.getInstance().getProjectResources(project);
        }
    }

    /**
* Finds a definition of an id attribute in layouts. (Ids can also be defined as
* resources; use {@link #findValueDefinition} to locate it there.)
*/
private static Pair<IFile, IRegion> findIdDefinition(IProject project, String id) {
        // FIRST look in the same file as the originating request, that's where you usually
        // want to jump
        IFile self = getFile();
        if (self != null && EXT_XML.equals(self.getFileExtension())) {
            Pair<IFile, IRegion> target = findIdInXml(id, self);
            if (target != null) {
                return target;
            }
        }

// We're currently only searching in the base layout folder.
// The next step is to add global resource reference tracking (which we already
// need to detect unused resources etc) and in that case we can quickly offer
//Synthetic comment -- @@ -718,30 +840,39 @@
// Search within the files in the values folder and find the value which defines
// the given resource. To be efficient, we will only parse XML files that contain
// a string match of the given token name.
        FolderConfiguration configuration = getConfiguration();
        Pair<IFile, IRegion> target = findValueByConfig(project, type, name, configuration);
        if (target != null) {
            return target;
        }

        if (configuration != null) {
            // Try searching without configuration too; more potential matches
            return findValueByConfig(project, type, name, configuration);
        }

        return null;
    }

    /** Searches for the given resource for a specific configuration (which may be null) */
    private static Pair<IFile, IRegion> findValueByConfig(IProject project,
            ResourceType type, String name, FolderConfiguration configuration) {
        List<ResourceFile> sourceFiles = getResourceFiles(project, type, name,
                false /* not framework*/, configuration);
        if (sourceFiles != null) {
            for (ResourceFile resourceFile : sourceFiles) {
                if (resourceFile.getFile() instanceof IFileWrapper) {
                    IFile file = ((IFileWrapper) resourceFile.getFile()).getIFile();
                    if (EXT_XML.equals(file.getFileExtension())) {
                        Pair<IFile, IRegion> target = findValueInXml(type, name, file);
                        if (target != null) {
                            return target;
}
}
}
}
}

return null;
}

//Synthetic comment -- @@ -918,39 +1049,6 @@
return Pair.of(type, name);
}

/** Parses the given file and locates a definition of the given resource */
private static Pair<File, Integer> findValueInXml(ResourceType type, String name, File file) {
// We can't use the StructureModelManager on files outside projects
//Synthetic comment -- @@ -981,6 +1079,9 @@
if (type == ResourceType.ID) {
// Ids are recorded in <item> tags instead of <id> tags
targetTag = "item"; //$NON-NLS-1$
        } else if (type == ResourceType.ATTR) {
            // Attributes seem to be defined in <public> tags
            targetTag = "public"; //$NON-NLS-1$
}
Element root = document.getDocumentElement();
if (root.getTagName().equals(ROOT_ELEMENT)) {
//Synthetic comment -- @@ -1157,20 +1258,30 @@
return null;
}

    /** Returns the file where the link request originated */
    private static IFile getFile() {
IEditorPart editor = getEditor();
if (editor != null) {
IEditorInput input = editor.getEditorInput();
if (input instanceof IFileEditorInput) {
IFileEditorInput fileInput = (IFileEditorInput) input;
                return fileInput.getFile();
}
}

return null;
}

    /** Returns the project applicable to this hyperlink detection */
    private static IProject getProject() {
        IFile file = getFile();
        if (file != null) {
            return file.getProject();
        }

        return null;
    }

/**
* Hyperlink implementation which delays computing the actual file and offset target
* until it is asked to open the hyperlink








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







