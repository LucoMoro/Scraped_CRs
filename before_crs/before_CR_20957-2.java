/*Add support for adding non-String value resources

The ResourceChooser only supported adding new Strings. This changeset
lets you add other types of value resources -- dimensions, integers,
etc. It will create a new file in res/values/ if necessary, based on
the plural form of the resource name (e.g. for "string" resources it
will create "strings.xml", etc). For existing files, it will add a new
entry to the existing file, using the same indentation as the last
top-level element in the file.

Change-Id:I09272ff52af38a8a7a059d455f398befbe0d1abc*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 067c731..89edee4 100644

//Synthetic comment -- @@ -1128,11 +1128,21 @@
*         not be computed.
*/
public String getIndent(Node xmlNode) {
        assert xmlNode.getNodeType() == Node.ELEMENT_NODE;

if (xmlNode instanceof IndexedRegion) {
IndexedRegion region = (IndexedRegion)xmlNode;
            IDocument document = getStructuredSourceViewer().getDocument();
int startOffset = region.getStartOffset();
return getIndentAtOffset(document, startOffset);
}
//Synthetic comment -- @@ -1148,7 +1158,7 @@
* @return The indent-string of the given node, or "" if the indentation for some
*         reason could not be computed.
*/
    public static String getIndentAtOffset(IDocument document, int offset) {
try {
IRegion lineInformation = document.getLineInformationOfOffset(offset);
if (lineInformation != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 9ca5ae7..fedca9c 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import static com.android.ide.eclipse.adt.AndroidConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_LAYOUT;

import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IResourceDelta.CONTENT;
//Synthetic comment -- @@ -77,6 +76,7 @@
* The include finder finds other XML files that are including a given XML file, and does
* so efficiently (caching results across IDE sessions etc).
*/
public class IncludeFinder {
/** Qualified name for the per-project persistent property include-map */
private final static QualifiedName CONFIG_INCLUDES = new QualifiedName(AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -156,11 +156,7 @@
}

/**
     * Gets the list of all other layouts that are including the given layout. The
     * returned Strings are user-readable references to files, which (for example) will
     * omit the file extension suffix, as well as the layout prefix (unless it's not the
     * base layout folder, such as layout-land). In order to get an actual
     * project-relative path from this String, call {@link #getProjectRelativePath}.
*
* @param included the file that is included
* @return the files that are including the given file, or null or empty
//Synthetic comment -- @@ -879,21 +875,6 @@
return finder;
}

    /**
     * Returns the project-relative path (such as res/layout/foo.xml) of a include
     * reference (which may be "foo", or "layout-land/foo").
     *
     * @param reference the include reference, as returned by {@link #getIncludedBy}.
     * @return a project relative path pointing to the actual XML file that contained the
     *         given reference
     */
    public static String getProjectRelativePath(String reference) {
        if (!reference.contains(WS_SEP)) { //$NON-NLS-1$
            reference = SdkConstants.FD_LAYOUT + WS_SEP + reference;
        }
        return SdkConstants.FD_RESOURCES + WS_SEP + reference + '.' + EXT_XML;
    }

/** A reference to a particular file in the project */
public static class Reference {
/** The unique id referencing the file, such as (for res/layout-land/main.xml)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java
//Synthetic comment -- index 6e516e6..721e093 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
public static final String ROOT_ELEMENT = "resources";  //$NON-NLS-1$
public static final String STRING_ELEMENT = "string";  //$NON-NLS-1$

public static final String NAME_ATTR = "name"; //$NON-NLS-1$
public static final String TYPE_ATTR = "type"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 1765112..29545d5 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -323,8 +324,12 @@

/**
* Is this a resource that can be defined in any file within the "values" folder?
*/
    private static boolean isValueResource(ResourceType type) {
ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
for (ResourceFolderType folderType : folderTypes) {
if (folderType == ResourceFolderType.VALUES) {
//Synthetic comment -- @@ -336,6 +341,23 @@
}

/**
* Computes the actual exact location to jump to for a given XML context.
*
* @param context the XML context to be opened
//Synthetic comment -- @@ -739,11 +761,7 @@
/** Looks within an XML DOM document for the given resource name and returns it */
private static Pair<IFile, IRegion> findValueInDocument(
ResourceType type, String name, IFile file, Document document) {
        String targetTag = type.getName();
        if (type == ResourceType.ID) {
            // Ids are recorded in <item> tags instead of <id> tags
            targetTag = "item"; //$NON-NLS-1$
        }
Element root = document.getDocumentElement();
if (root.getTagName().equals(ROOT_ELEMENT)) {
NodeList children = root.getChildNodes();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 92647c8..1a6a9de 100644

//Synthetic comment -- @@ -16,6 +16,16 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
//Synthetic comment -- @@ -23,27 +33,54 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* A dialog to let the user select a resource based on a resource type.
*/
public class ResourceChooser extends AbstractElementListSelectionDialog {

private Pattern mProjectResourcePattern;
//Synthetic comment -- @@ -176,7 +213,7 @@
newResButton.setText(title);

// We only support adding new strings right now
        newResButton.setEnabled(mResourceType == ResourceType.STRING);

newResButton.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -185,11 +222,134 @@

if (mResourceType == ResourceType.STRING) {
createNewString();
}
}
});
}

private void createNewString() {
ExtractStringRefactoring ref = new ExtractStringRefactoring(
mProject, true /*enforceNew*/);
//Synthetic comment -- @@ -291,4 +451,85 @@
selectItemName(itemName);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 2d40cf9..a87400b 100644

//Synthetic comment -- @@ -178,7 +178,7 @@
String result = sb.toString();
String error = null;
try {
            byte[] buf = result.getBytes("UTF8");
InputStream stream = new ByteArrayInputStream(buf);
if (need_delete) {
file.delete(IResource.KEEP_HISTORY | IResource.FORCE, null /*monitor*/);







