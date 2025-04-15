/*Add support for including within specific configurations

The current support for includes are limited to including a view
within the "base" layouts (defined in layouts/). This changeset
generalizes this to track ALL include-sources (such as an include from
the landscape version) and offers to let you view and edit the current
view within each of those specific configurations.  The configuration
will be switched to one that is compatible with the outer, including
view.  It is possible that this will switch to a different inner view;
that seems better than trying to force editing an inner view in an
outer view that won't actually be shown at runtime.

We should enhance the configuration chooser such that it will only
offer options in its various combo boxes that are compatible with the
inner view as well. That is not addressed by this changeset.

Change-Id:Id5171d367cf65e2403bbac1640a179de4b420bd6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index ee6aeca..84b29c0 100644

//Synthetic comment -- @@ -1978,5 +1978,29 @@

return false;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 8ccf557..d702255 100755

//Synthetic comment -- @@ -22,9 +22,11 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -214,9 +216,9 @@
* view.
*/
private void insertShowIncludedMenu(String beforeId) {
        IProject project = mEditor.getProject();
        String me = mCanvas.getLayoutResourceName();
        final List<String> includedBy = IncludeFinder.get(project).getIncludedBy(me);

Action includeAction = new Action("Show Included In", IAction.AS_DROP_DOWN_MENU) {
@Override
//Synthetic comment -- @@ -238,9 +240,9 @@
public Menu getMenu(Menu parent) {
mMenu = new Menu(parent);
if (includedBy != null && includedBy.size() > 0) {
                            for (final String s : includedBy) {
                                String title = s;
                                IAction action = new ShowWithinAction(title, s);
new ActionContributionItem(action).fill(mMenu, -1);
}
new Separator().fill(mMenu, -1);
//Synthetic comment -- @@ -261,27 +263,27 @@
}

private class ShowWithinAction extends Action {
        private String mId;

        public ShowWithinAction(String title, String id) {
super(title, IAction.AS_RADIO_BUTTON);
            mId = id;
}

@Override
public boolean isChecked() {
            String within = mEditor.getGraphicalEditor().getIncludedWithinId();
if (within == null) {
                return mId == null;
} else {
                return within.equals(mId);
}
}

@Override
public void run() {
if (!isChecked()) {
                mEditor.getGraphicalEditor().showIn(mId);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ce21726..8292526 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomButton;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -165,7 +166,7 @@
new QualifiedName(AdtPlugin.PLUGIN_ID, "initialstate");//$NON-NLS-1$

/**
     * Session-property on files which specifies the inclusion-context (name of another layout
* which should be "including" this layout) when the file is opened
*/
public final static QualifiedName NAME_INCLUDE =
//Synthetic comment -- @@ -200,10 +201,10 @@
private StyledText mErrorLabel;

/**
     * The resource name of a file that should surround this file (e.g. include this file
* visually), or null if not applicable
*/
    private String mIncludedWithinId;

private Map<String, Map<String, IResourceValue>> mConfiguredFrameworkRes;
private Map<String, Map<String, IResourceValue>> mConfiguredProjectRes;
//Synthetic comment -- @@ -986,8 +987,8 @@
// requested that it should be opened as included within another file
if (mEditedFile != null) {
try {
                mIncludedWithinId = (String) mEditedFile.getSessionProperty(NAME_INCLUDE);
                if (mIncludedWithinId != null) {
// Only use once
mEditedFile.setSessionProperty(NAME_INCLUDE, null);
}
//Synthetic comment -- @@ -1476,16 +1477,15 @@
// Code to support editing included layout

// Outer layout name:
        String contextLayoutName = mIncludedWithinId;
        if (contextLayoutName != null) {
// Find the layout file.
Map<String, IResourceValue> layouts = configuredProjectRes.get(
ResourceType.LAYOUT.getName());
IResourceValue contextLayout = layouts.get(contextLayoutName);
if (contextLayout != null) {
                String path = contextLayout.getValue();

                File layoutFile = new File(path);
if (layoutFile.isFile()) {
try {
// Get the name of the layout actually being edited, without the extension
//Synthetic comment -- @@ -2019,11 +2019,20 @@
* file has an include tag referencing this view, and the set of views that have this
* property can be found using the {@link IncludeFinder}.
*
     * @param relativePath project-relative path to the file to open as a surrounding context,
*   or null to show the file standalone
*/
    public void showIn(String relativePath) {
        mIncludedWithinId = relativePath;
recomputeLayout();
}

//Synthetic comment -- @@ -2033,7 +2042,7 @@
*
* @return the resource name of an including layout, or null
*/
    public String getIncludedWithinId() {
        return mIncludedWithinId;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index f33c296..6ec844a 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.eclipse.adt.AndroidConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IResourceDelta.CONTENT;
//Synthetic comment -- @@ -37,6 +38,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;
import com.android.sdklib.io.StreamException;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -79,6 +81,7 @@
* The include finder finds other XML files that are including a given XML file, and does
* so efficiently (caching results across IDE sessions etc).
*/
public class IncludeFinder {
/** Qualified name for the per-project persistent property include-map */
private final static QualifiedName CONFIG_INCLUDES = new QualifiedName(AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -151,19 +154,47 @@
* @param includer the resource name to return included layouts for
* @return the layouts included by the given resource
*/
    public List<String> getIncludesFrom(String includer) {
ensureInitialized();

return mIncludes.get(includer);
}

/**
     * Gets the list of all other layouts that are including the given layout
*
* @param included the file that is included
* @return the files that are including the given file, or null or empty
*/
    public List<String> getIncludedBy(String included) {
ensureInitialized();
return mIncludedBy.get(included);
}
//Synthetic comment -- @@ -287,6 +318,9 @@
}

if (c == '}') {
break;
}
assert c == ',';
//Synthetic comment -- @@ -386,32 +420,18 @@
* Scans the given {@link ResourceFile} and if it is a layout resource, updates the
* includes in it.
*
     * @param resourceFile the {@link ResourceFile} to be scanned for includes
* @param singleUpdate true if this is a single file being updated, false otherwise
*            (e.g. during initial project scanning)
* @return true if we updated the includes for the resource file
*/
    @SuppressWarnings("restriction")
private boolean updateFileIncludes(ResourceFile resourceFile, boolean singleUpdate) {
        String folderName = resourceFile.getFolder().getFolder().getName();
        if (!folderName.equals(SdkConstants.FD_LAYOUT)) {
            // For now we only track layouts in the main layout/ folder;
            // consider merging the various configurations and doing something
            // clever in Show Include.
            return false;
        }

ResourceType[] resourceTypes = resourceFile.getResourceTypes();
for (ResourceType type : resourceTypes) {
if (type == ResourceType.LAYOUT) {
ensureInitialized();

                String name = resourceFile.getFile().getName();
                int baseEnd = name.length() - EXT_XML.length() - 1; // -1: the dot
                if (baseEnd > 0) {
                    name = name.substring(0, baseEnd);
                }

List<String> includes = Collections.emptyList();
if (resourceFile.getFile() instanceof IFileWrapper) {
IFile file = ((IFileWrapper) resourceFile.getFile()).getIFile();
//Synthetic comment -- @@ -446,13 +466,14 @@
includes = findIncludes(xml);
}

                if (includes.equals(getIncludesFrom(name))) {
// Common case -- so avoid doing settings flush etc
return false;
}

boolean detectCycles = singleUpdate;
                setIncluded(name, includes, detectCycles);

if (singleUpdate) {
saveSettings();
//Synthetic comment -- @@ -614,6 +635,54 @@
ResourceManager.getInstance().addListener(sListener);
}

/** Listener of resource file saves, used to update layout inclusion data structures */
private static class ResourceListener implements IResourceListener {
public void fileChanged(IProject project, ResourceFile file, int eventType) {
//Synthetic comment -- @@ -883,4 +952,144 @@
finder.mIncludedBy = new HashMap<String, List<String>>();
return finder;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 02f26aa..443171e 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -743,7 +744,6 @@
IPath relativePath = Sdk.makeRelativeTo(filePath, workspacePath);
IResource xmlFile = workspace.findMember(relativePath);
if (xmlFile != null) {
                String nextName = getLayoutResourceName();
IFile leavingFile = graphicalEditor.getEditedFile();
try {
// TODO - only consider this if we're going to open a new file...
//Synthetic comment -- @@ -755,17 +755,19 @@
// pass
}

try {
IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);
if (openAlready != null) {
if (openAlready instanceof LayoutEditor) {
LayoutEditor editor = (LayoutEditor)openAlready;
GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                            gEditor.showIn(nextName);
}
} else {
try {
                            xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, nextName);
} catch (CoreException e) {
// pass - worst that can happen is that we don't start with inclusion
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index c3581c4..8ae5c4d 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -388,7 +389,10 @@
return node.getShortDescription();
} else if (element == null && vi != null) {
// It's an inclusion-context
                return mGraphicalEditorPart.getIncludedWithinId();
}

return element == null ? "(null)" : element.toString();  //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinderTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinderTest.java
//Synthetic comment -- index 4c0f83e..24fa0ae 100644

//Synthetic comment -- @@ -42,6 +42,12 @@
IncludeFinder.decodeMap(s).get("foo").toString());
}

public void testEncodeDecode2() throws Exception {
// Test ending with just a key
String s = "bar,key1=>{value1,value2},key2=>{value3,value4}";







