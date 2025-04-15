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
//Synthetic comment -- index 8286d3b..02ef055 100644

//Synthetic comment -- @@ -1978,5 +1978,29 @@

return false;
}

    /**
     * Resets the configuration chooser to reflect the given file configuration. This is
     * intended to be used by the "Show Included In" functionality where the user has
     * picked a non-default configuration (such as a particular landscape layout) and the
     * configuration chooser must be switched to a landscape layout. This method will
     * trigger a model change.
     * <p>
     * This will NOT trigger a redraw event!
     * <p>
     * FIXME: We are currently setting the configuration file to be the configuration for
     * the "outer" (the including) file, rather than the inner file, which is the file the
     * user is actually editing. We need to refine this, possibly with a way for the user
     * to choose which configuration they are editing. And in particular, we should be
     * filtering the configuration chooser to only show options in the outer configuration
     * that are compatible with the inner included file.
     *
     * @param file the file to be configured
     */
    public void resetConfigFor(IFile file) {
        setFile(file);
        mEditedConfig = null;
        onXmlModelLoaded();
    }
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 8ccf557..d702255 100755

//Synthetic comment -- @@ -22,9 +22,11 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -214,9 +216,9 @@
* view.
*/
private void insertShowIncludedMenu(String beforeId) {
        IFile file = mEditor.getGraphicalEditor().getEditedFile();
        IProject project = file.getProject();
        final List<Reference> includedBy = IncludeFinder.get(project).getIncludedBy(file);

Action includeAction = new Action("Show Included In", IAction.AS_DROP_DOWN_MENU) {
@Override
//Synthetic comment -- @@ -238,9 +240,9 @@
public Menu getMenu(Menu parent) {
mMenu = new Menu(parent);
if (includedBy != null && includedBy.size() > 0) {
                            for (final Reference reference : includedBy) {
                                String title = reference.getDisplayName();
                                IAction action = new ShowWithinAction(title, reference);
new ActionContributionItem(action).fill(mMenu, -1);
}
new Separator().fill(mMenu, -1);
//Synthetic comment -- @@ -261,27 +263,27 @@
}

private class ShowWithinAction extends Action {
        private Reference mReference;

        public ShowWithinAction(String title, Reference reference) {
super(title, IAction.AS_RADIO_BUTTON);
            mReference = reference;
}

@Override
public boolean isChecked() {
            Reference within = mEditor.getGraphicalEditor().getIncludedWithin();
if (within == null) {
                return mReference == null;
} else {
                return within.equals(mReference);
}
}

@Override
public void run() {
if (!isChecked()) {
                mEditor.getGraphicalEditor().showIn(mReference);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4c97f62..ede9115 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomButton;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -165,7 +166,7 @@
new QualifiedName(AdtPlugin.PLUGIN_ID, "initialstate");//$NON-NLS-1$

/**
     * Session-property on files which specifies the inclusion-context (reference to another layout
* which should be "including" this layout) when the file is opened
*/
public final static QualifiedName NAME_INCLUDE =
//Synthetic comment -- @@ -200,10 +201,10 @@
private StyledText mErrorLabel;

/**
     * The resource reference to a file that should surround this file (e.g. include this file
* visually), or null if not applicable
*/
    private Reference mIncludedWithin;

private Map<String, Map<String, ResourceValue>> mConfiguredFrameworkRes;
private Map<String, Map<String, ResourceValue>> mConfiguredProjectRes;
//Synthetic comment -- @@ -986,8 +987,8 @@
// requested that it should be opened as included within another file
if (mEditedFile != null) {
try {
                mIncludedWithin = (Reference) mEditedFile.getSessionProperty(NAME_INCLUDE);
                if (mIncludedWithin != null) {
// Only use once
mEditedFile.setSessionProperty(NAME_INCLUDE, null);
}
//Synthetic comment -- @@ -1480,16 +1481,15 @@
// Code to support editing included layout

// Outer layout name:
        if (mIncludedWithin != null) {
            String contextLayoutName = mIncludedWithin.getName();

// Find the layout file.
Map<String, ResourceValue> layouts = configuredProjectRes.get(
ResourceType.LAYOUT.getName());
ResourceValue contextLayout = layouts.get(contextLayoutName);
if (contextLayout != null) {
                File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
try {
// Get the name of the layout actually being edited, without the extension
//Synthetic comment -- @@ -2023,11 +2023,20 @@
* file has an include tag referencing this view, and the set of views that have this
* property can be found using the {@link IncludeFinder}.
*
     * @param includeWithin reference to a file to include as a surrounding context,
*   or null to show the file standalone
*/
    public void showIn(Reference includeWithin) {
        mIncludedWithin = includeWithin;

        if (includeWithin != null) {
            IFile file = includeWithin.getFile();

            // Update configuration
            if (file != null) {
                mConfigComposite.resetConfigFor(file);
            }
        }
recomputeLayout();
}

//Synthetic comment -- @@ -2037,7 +2046,7 @@
*
* @return the resource name of an including layout, or null
*/
    public Reference getIncludedWithin() {
        return mIncludedWithin;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 0581cc0..dd8edb2 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.eclipse.adt.AndroidConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_LAYOUT;
import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IResourceDelta.CONTENT;
//Synthetic comment -- @@ -37,6 +38,8 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -74,6 +77,7 @@
* The include finder finds other XML files that are including a given XML file, and does
* so efficiently (caching results across IDE sessions etc).
*/
@SuppressWarnings("restriction")
public class IncludeFinder {
/** Qualified name for the per-project persistent property include-map */
private final static QualifiedName CONFIG_INCLUDES = new QualifiedName(AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -146,19 +150,47 @@
* @param includer the resource name to return included layouts for
* @return the layouts included by the given resource
*/
    private List<String> getIncludesFrom(String includer) {
ensureInitialized();

return mIncludes.get(includer);
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
*/
    public List<Reference> getIncludedBy(IResource included) {
        ensureInitialized();
        String mapKey = getMapKey(included);
        List<String> result = mIncludedBy.get(mapKey);
        if (result == null) {
            String name = getResourceName(included);
            if (!name.equals(mapKey)) {
                result = mIncludedBy.get(name);
            }
        }

        if (result != null && result.size() > 0) {
            List<Reference> references = new ArrayList<Reference>(result.size());
            for (String s : result) {
                references.add(new Reference(mProject, s));
            }
            return references;
        } else {
            return null;
        }
    }

    /** For test suite only -- do not call */
    @VisibleForTesting
    /* package */ List<String> getIncludedBy(String included) {
ensureInitialized();
return mIncludedBy.get(included);
}
//Synthetic comment -- @@ -282,6 +314,9 @@
}

if (c == '}') {
                            if (i < end-1 && encoded.charAt(i+1) == ',') {
                                i++;
                            }
break;
}
assert c == ',';
//Synthetic comment -- @@ -381,32 +416,18 @@
* Scans the given {@link ResourceFile} and if it is a layout resource, updates the
* includes in it.
*
     * @param resourceFile the {@link ResourceFile} to be scanned for includes (doesn't
     *            have to be only layout XML files; this method will filter the type)
* @param singleUpdate true if this is a single file being updated, false otherwise
*            (e.g. during initial project scanning)
* @return true if we updated the includes for the resource file
*/
private boolean updateFileIncludes(ResourceFile resourceFile, boolean singleUpdate) {
ResourceType[] resourceTypes = resourceFile.getResourceTypes();
for (ResourceType type : resourceTypes) {
if (type == ResourceType.LAYOUT) {
ensureInitialized();

List<String> includes = Collections.emptyList();
if (resourceFile.getFile() instanceof IFileWrapper) {
IFile file = ((IFileWrapper) resourceFile.getFile()).getIFile();
//Synthetic comment -- @@ -441,13 +462,14 @@
includes = findIncludes(xml);
}

                String key = getMapKey(resourceFile);
                if (includes.equals(getIncludesFrom(key))) {
// Common case -- so avoid doing settings flush etc
return false;
}

boolean detectCycles = singleUpdate;
                setIncluded(key, includes, detectCycles);

if (singleUpdate) {
saveSettings();
//Synthetic comment -- @@ -609,6 +631,54 @@
ResourceManager.getInstance().addListener(sListener);
}

    private static String getMapKey(ResourceFile resourceFile) {
        IAbstractFile file = resourceFile.getFile();
        String name = file.getName();
        String folderName = file.getParentFolder().getName();
        return getMapKey(folderName, name);
    }

    private static String getMapKey(IResource resourceFile) {
        String folderName = resourceFile.getParent().getName();
        String name = resourceFile.getName();
        return getMapKey(folderName, name);
    }

    private static String getResourceName(IResource resourceFile) {
        String name = resourceFile.getName();
        int baseEnd = name.length() - EXT_XML.length() - 1; // -1: the dot
        if (baseEnd > 0) {
            name = name.substring(0, baseEnd);
        }

        return name;
    }

    private static String getMapKey(String folderName, String name) {
        int baseEnd = name.length() - EXT_XML.length() - 1; // -1: the dot
        if (baseEnd > 0) {
            name = name.substring(0, baseEnd);
        }

        // Create a map key for the given resource file
        // This will map
        //     /res/layout/foo.xml => "foo"
        //     /res/layout-land/foo.xml => "-land/foo"

        if (FD_LAYOUT.equals(folderName)) {
            // Normal case -- keep just the basename
            return name;
        } else {
            // Store the relative path from res/ on down, so
            // /res/layout-land/foo.xml becomes "layout-land/foo"
            //if (folderName.startsWith(FD_LAYOUT)) {
            //    folderName = folderName.substring(FD_LAYOUT.length());
            //}

            return folderName + WS_SEP + name;
        }
    }

/** Listener of resource file saves, used to update layout inclusion data structures */
private static class ResourceListener implements IResourceListener {
public void fileChanged(IProject project, ResourceFile file, int eventType) {
//Synthetic comment -- @@ -802,4 +872,144 @@
finder.mIncludedBy = new HashMap<String, List<String>>();
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
        if (!reference.contains(WS_SEP)) { //NON-NLS-1$
            reference = SdkConstants.FD_LAYOUT + WS_SEP + reference;
        }
        return SdkConstants.FD_RESOURCES + WS_SEP + reference + '.' + EXT_XML;
    }

    /** A reference to a particular file in the project */
    public static class Reference {
        /** The unique id referencing the file, such as (for res/layout-land/main.xml)
         * "layout-land/main") */
        private final String mId;

        /** The project containing the file */
        private final IProject mProject;

        /** The resource name of the file, such as (for res/layout/main.xml) "main" */
        private String mName;

        /** Creates a new include reference */
        private Reference(IProject project, String id) {
            super();
            mProject = project;
            mId = id;
        }

        /**
         * Returns the id identifying the given file within the project
         *
         * @return the id identifying the given file within the project
         */
        public String getId() {
            return mId;
        }

        /**
         * Returns the {@link IFile} in the project for the given file. May return null if
         * there is an error in locating the file or if the file no longer exists.
         *
         * @return the project file, or null
         */
        public IFile getFile() {
            String reference = mId;
            if (!reference.contains(WS_SEP)) {
                reference = SdkConstants.FD_LAYOUT + WS_SEP + reference;
            }

            String projectPath = SdkConstants.FD_RESOURCES + WS_SEP + reference + '.' + EXT_XML;
            IResource member = mProject.findMember(projectPath);
            if (member instanceof IFile) {
                return (IFile) member;
            }

            return null;
        }

        /**
         * Returns a description of this reference, suitable to be shown to the user
         *
         * @return a display name for the reference
         */
        public String getDisplayName() {
            // The ID is deliberately kept in a pretty user-readable format but we could
            // consider prepending layout/ on ids that don't have it (to make the display
            // more uniform) or ripping out all layout[-constraint] prefixes out and
            // instead prepending @ etc.
            return mId;
        }

        /**
         * Returns the name of the reference, suitable for resource lookup. For example,
         * for "res/layout/main.xml", as well as for "res/layout-land/main.xml", this
         * would be "main".
         *
         * @return the resource name of the reference
         */
        public String getName() {
            if (mName == null) {
                mName = mId;
                int index = mName.lastIndexOf(WS_SEP);
                if (index != -1) {
                    mName = mName.substring(index + 1);
                }
            }

            return mName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mId == null) ? 0 : mId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Reference other = (Reference) obj;
            if (mId == null) {
                if (other.mId != null)
                    return false;
            } else if (!mId.equals(other.mId))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Reference [getId()=" + getId() // NON-NLS-1$
                    + ", getDisplayName()=" + getDisplayName() // NON-NLS-1$
                    + ", getName()=" + getName() // NON-NLS-1$
                    + ", getFile()=" + getFile() + "]"; // NON-NLS-1$
        }

        /**
         * Creates a reference to the given file
         *
         * @param file the file to create a reference for
         * @return a reference to the given file
         */
        public static Reference create(IFile file) {
            return new Reference(file.getProject(), getMapKey(file));
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 02f26aa..443171e 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -743,7 +744,6 @@
IPath relativePath = Sdk.makeRelativeTo(filePath, workspacePath);
IResource xmlFile = workspace.findMember(relativePath);
if (xmlFile != null) {
IFile leavingFile = graphicalEditor.getEditedFile();
try {
// TODO - only consider this if we're going to open a new file...
//Synthetic comment -- @@ -755,17 +755,19 @@
// pass
}

                Reference next = Reference.create(graphicalEditor.getEditedFile());

try {
IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);
if (openAlready != null) {
if (openAlready instanceof LayoutEditor) {
LayoutEditor editor = (LayoutEditor)openAlready;
GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                            gEditor.showIn(next);
}
} else {
try {
                            xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
} catch (CoreException e) {
// pass - worst that can happen is that we don't start with inclusion
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index c3581c4..8ae5c4d 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -388,7 +389,10 @@
return node.getShortDescription();
} else if (element == null && vi != null) {
// It's an inclusion-context
                Reference includedWithin = mGraphicalEditorPart.getIncludedWithin();
                if (includedWithin != null) {
                    return includedWithin.getDisplayName();
                }
}

return element == null ? "(null)" : element.toString();  //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinderTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinderTest.java
//Synthetic comment -- index 4c0f83e..24fa0ae 100644

//Synthetic comment -- @@ -42,6 +42,12 @@
IncludeFinder.decodeMap(s).get("foo").toString());
}

    public void testNoBlanks() throws Exception {
        // Make sure we skip the },
        String s = "foo=>{bar,baz},bar";
        assertNull(IncludeFinder.decodeMap(s).get(""));
    }

public void testEncodeDecode2() throws Exception {
// Test ending with just a key
String s = "bar,key1=>{value1,value2},key2=>{value3,value4}";







