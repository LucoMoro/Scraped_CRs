/*Improve hyperlink resolution for configurations

The hyperlink resolver would only look for values in the base values/
folder. This did not work well for resources that are ONLY defined in
specific configurations. This changeset improves the search to look in
all eligible resource folders.

Change-Id:Ia97a3f45bf454cc28378387b219af0fdd855a902*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 263b6c8..2be283d 100644

//Synthetic comment -- @@ -2291,4 +2291,13 @@
return Collections.emptyList();
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index a2e664b..76b0118 100644

//Synthetic comment -- @@ -37,15 +37,19 @@
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
//Synthetic comment -- @@ -98,6 +102,7 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
//Synthetic comment -- @@ -604,6 +609,15 @@

/** Looks up the project member of the given type and the given name */
private static IResource findNonValueFile(IProject project, ResourceType type, String name) {
String relativePath;
IResource member;
String folder = WS_RESOURCES + WS_SEP + type.getName();
//Synthetic comment -- @@ -623,9 +637,6 @@
if (member == null) {
// Still couldn't find the member; it must not be defined in a "base" directory
// like "layout"; look in various variations
                ResourceManager manager = ResourceManager.getInstance();
                ProjectResources resources = manager.getProjectResources(project);

ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
for (ResourceFolderType folderType : folderTypes) {
if (folderType != ResourceFolderType.VALUES) {
//Synthetic comment -- @@ -646,10 +657,71 @@
}
}

return member;
}

/**
* Finds a resource of the given name in the given folder, searching for possible file
* extensions
*/
//Synthetic comment -- @@ -718,6 +790,14 @@
// Search within the files in the values folder and find the value which defines
// the given resource. To be efficient, we will only parse XML files that contain
// a string match of the given token name.

String folderPath = AndroidConstants.WS_RESOURCES + AndroidConstants.WS_SEP
+ SdkConstants.FD_VALUES;
//Synthetic comment -- @@ -731,7 +811,7 @@
IFile file = (IFile) resource;
// Must have an XML extension
if (EXT_XML.equals(file.getFileExtension())) {
                            Pair<IFile, IRegion> target = findValueInXml(type, name, file);
if (target != null) {
return target;
}
//Synthetic comment -- @@ -742,19 +822,47 @@
AdtPlugin.log(e, ""); //$NON-NLS-1$
}
}
return null;
}

/** Parses the given file and locates a definition of the given resource */
private static Pair<IFile, IRegion> findValueInXml(
            ResourceType type, String name, IFile file) {
IStructuredModel model = null;
try {
model = StructuredModelManager.getModelManager().getExistingModelForRead(file);
if (model == null) {
// There is no open or cached model for the file; see if the file looks
// like it's interesting (content contains the String name we are looking for)
                if (AdtPlugin.fileContains(file, name)) {
// Yes, so parse content
model = StructuredModelManager.getModelManager().getModelForRead(file);
}








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







