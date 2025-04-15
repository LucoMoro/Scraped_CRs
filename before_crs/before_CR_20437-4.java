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
    private IAndroidTarget getRenderingTarget() {
// if the SDK is null no targets are loaded.
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk == null) {
//Synthetic comment -- @@ -2323,4 +2323,13 @@
return Collections.emptyList();
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index a2e664b..f593619 100644

//Synthetic comment -- @@ -24,10 +24,9 @@
import static com.android.ide.eclipse.adt.AndroidConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.FN_RESOURCE_BASE;
import static com.android.ide.eclipse.adt.AndroidConstants.FN_RESOURCE_CLASS;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_RESOURCES;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.ROOT_ELEMENT;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -37,19 +36,23 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;
import com.android.sdklib.util.Pair;

import org.apache.xerces.parsers.DOMParser;
//Synthetic comment -- @@ -98,6 +101,7 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
//Synthetic comment -- @@ -122,6 +126,7 @@
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -230,6 +235,10 @@

/** Returns true if this represents a {@code <foo.bar.Baz>} custom view class element */
private static boolean isClassElement(XmlContext context) {
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

        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk == null) {
            return false;
        }
        IAndroidTarget target = currentSdk.getTarget(project);
        if (target == null) {
            return false;
        }
ResourceType type = parsedUrl.getFirst();
String name = parsedUrl.getSecond();

// Attempt to open files, such as layouts and drawables in @android?
if (isFileResource(type)) {
            AndroidTargetData data = currentSdk.getTargetData(target);
            if (data == null) {
return false;
}

            ProjectResources frameworkResources = data.getFrameworkResources();
Map<String, Map<String, ResourceValue>> configuredResources =
frameworkResources.getConfiguredResources(new FolderConfiguration());

//Synthetic comment -- @@ -560,22 +558,93 @@
break;
}
}
        } else if (isValueResource(type)) {
            File values = new File(target.getPath(IAndroidTarget.RESOURCES),
                    SdkConstants.FD_VALUES);
            if (values.exists()) {
                Pair<File, Integer> match = findValueDefinition(values, type, name);
                if (match != null) {
                    Path path = new Path(match.getFirst().getPath());
                    openPath(path, null, match.getSecond());
return true;
}
}
}

return false;
}

/** Opens a Java class for the given fully qualified class name */
private static boolean openJavaClass(IProject project, String fqcn) {
if (fqcn == null) {
//Synthetic comment -- @@ -604,79 +673,132 @@

/** Looks up the project member of the given type and the given name */
private static IResource findNonValueFile(IProject project, ResourceType type, String name) {
        String relativePath;
        IResource member;
        String folder = WS_RESOURCES + WS_SEP + type.getName();
        relativePath = folder + WS_SEP + name + '.' + EXT_XML;
        member = project.findMember(relativePath);
        if (member == null) {
            // Search for any file in the directory with the given basename;
            // this is necessary for files like drawables that don't have
            // .xml files. It's an error to have conflicts in basenames for
            // these resources types so this is safe.
            IResource d = project.findMember(folder);
            if (d instanceof IFolder) {
                IFolder dir = (IFolder) d;
                member = findInFolder(name, dir);
            }

            if (member == null) {
                // Still couldn't find the member; it must not be defined in a "base" directory
                // like "layout"; look in various variations
                ResourceManager manager = ResourceManager.getInstance();
                ProjectResources resources = manager.getProjectResources(project);

                ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
                for (ResourceFolderType folderType : folderTypes) {
                    if (folderType != ResourceFolderType.VALUES) {
                        List<ResourceFolder> folders = resources.getFolders(folderType);
                        for (ResourceFolder resourceFolder : folders) {
                            if (resourceFolder.getFolder() instanceof IFolderWrapper) {
                                IFolderWrapper wrapper =
                                    (IFolderWrapper) resourceFolder.getFolder();
                                IFolder iFolder = wrapper.getIFolder();
                                member = findInFolder(name, iFolder);
                                if (member != null) {
                                    break;
                                }
                            }
                        }
                    }
                }
}
}

        return member;
}

/**
     * Finds a resource of the given name in the given folder, searching for possible file
     * extensions
*/
    private static IResource findInFolder(String name, IFolder dir) {
        try {
            for (IResource child : dir.members()) {
                String fileName = child.getName();
                int index = fileName.indexOf('.');
                if (index != -1) {
                    fileName = fileName.substring(0, index);
                }
                if (fileName.equals(name)) {
                    return child;
}
}
        } catch (CoreException e) {
            AdtPlugin.log(e, ""); //$NON-NLS-1$
}

return null;
}

/**
* Finds a definition of an id attribute in layouts. (Ids can also be defined as
* resources; use {@link #findValueDefinition} to locate it there.)
*/
private static Pair<IFile, IRegion> findIdDefinition(IProject project, String id) {
// We're currently only searching in the base layout folder.
// The next step is to add global resource reference tracking (which we already
// need to detect unused resources etc) and in that case we can quickly offer
//Synthetic comment -- @@ -718,30 +840,39 @@
// Search within the files in the values folder and find the value which defines
// the given resource. To be efficient, we will only parse XML files that contain
// a string match of the given token name.

        String folderPath = AndroidConstants.WS_RESOURCES + AndroidConstants.WS_SEP
                + SdkConstants.FD_VALUES;

        IFolder f = project.getFolder(folderPath);
        if (f.exists()) {
            try {
                // Check XML files in values/
                for (IResource resource : f.members()) {
                    if (resource.exists() && !resource.isDerived() && resource instanceof IFile) {
                        IFile file = (IFile) resource;
                        // Must have an XML extension
                        if (EXT_XML.equals(file.getFileExtension())) {
                            Pair<IFile, IRegion> target = findValueInXml(type, name, file);
                            if (target != null) {
                                return target;
                            }
}
}
}
            } catch (CoreException e) {
                AdtPlugin.log(e, ""); //$NON-NLS-1$
}
}
return null;
}

//Synthetic comment -- @@ -918,39 +1049,6 @@
return Pair.of(type, name);
}

    /**
     * Searches for a resource of a "multi-file" type (like @string) where the value can
     * be found in any file within the folder containing resource of that type (in the
     * case of @string, "values", and in the case of @color, "colors", etc).
     * <p>
     * This method operates on plain {@link File} objects and is intended for searches in
     * the Android platform files; for project-relative searches use
     * {@link #findValueDefinition(File, ResourceType, String)}.
     */
    private static Pair<File, Integer> findValueDefinition(File resourceDir, ResourceType type,
            String name) {
        // Search within the files in the values folder and find the value which defines
        // the given resource. To be efficient, we will only parse XML files that contain
        // a string match of the given token name.

        // Check XML files in values/
        File[] children = resourceDir.listFiles();
        if (children == null) {
            return null;
        }
        for (File resource : children) {
            // Must have an XML extension
            if (resource.getName().endsWith(EXT_XML)) {
                Pair<File, Integer> target = findValueInXml(type, name, resource);
                if (target != null) {
                    return target;
                }
            }
        }

        return null;
    }

/** Parses the given file and locates a definition of the given resource */
private static Pair<File, Integer> findValueInXml(ResourceType type, String name, File file) {
// We can't use the StructureModelManager on files outside projects
//Synthetic comment -- @@ -981,6 +1079,9 @@
if (type == ResourceType.ID) {
// Ids are recorded in <item> tags instead of <id> tags
targetTag = "item"; //$NON-NLS-1$
}
Element root = document.getDocumentElement();
if (root.getTagName().equals(ROOT_ELEMENT)) {
//Synthetic comment -- @@ -1157,20 +1258,30 @@
return null;
}

    /** Returns the project applicable to this hyperlink detection */
    private static IProject getProject() {
IEditorPart editor = getEditor();
if (editor != null) {
IEditorInput input = editor.getEditorInput();
if (input instanceof IFileEditorInput) {
IFileEditorInput fileInput = (IFileEditorInput) input;
                return fileInput.getFile().getProject();
}
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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFolderWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFolderWrapper.java
//Synthetic comment -- index 265ea33..3c00485 100644

//Synthetic comment -- @@ -171,4 +171,9 @@

return null;
}
}







