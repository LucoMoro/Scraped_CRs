/*Migrate Fragment and ListView metadata to the tools namespace

The persistent metadata storing AdapterView and Fragment bindings
predates the new tools namespace handled by aapt, and uses XML
comments. This changeset updates the code to using tools: attributes
instead.

Note that it doesn't migrate old settings; I could not find a way to
do that performantly (and in some cases the code which reads a setting
has no access to the document containing the metadata, only to parsed
XML nodes, so it cannot mutate it on demand to the new format.)
However, this shouldn't be a big deal; for fragments for example the
fragment message states what needs to be done to set up a binding.

This CL also fixes a couple of bugs around setting up these views
which should make switching views faster (it used to do the work
twice), and avoids a rare concurrent modification exception.

Change-Id:Id6a8a9a1649c1b9f6f5fc6a9fbc3a6e5b0512dd6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index b2ec72f..d25b27c 100644

//Synthetic comment -- @@ -710,7 +710,7 @@
@NonNull final Element element,
@NonNull final String description,
@NonNull final String name,
            @Nullable final String value,
final boolean reveal,
final boolean appendValue) {
editor.wrapUndoEditXmlModel(description, new Runnable() {
//Synthetic comment -- @@ -721,15 +721,17 @@
// Add in new prefix...
prefix = XmlUtils.lookupNamespacePrefix(element,
TOOLS_URI, TOOLS_PREFIX);
                    if (value != null) {
                        // ...and ensure that the header is formatted such that
                        // the XML namespace declaration is placed in the right
                        // position and wrapping is applied etc.
                        editor.scheduleNodeReformat(editor.getUiRootNode(),
                                true /*attributesOnly*/);
                    }
}

String v = value;
                if (appendValue && v != null) {
String prev = element.getAttributeNS(TOOLS_URI, name);
if (prev.length() > 0) {
v = prev + ',' + value;
//Synthetic comment -- @@ -738,10 +740,14 @@

// Use the non-namespace form of set attribute since we can't
// reference the namespace until the model has been reloaded
                if (v != null) {
                    element.setAttribute(prefix + ':' + name, v);
                } else {
                    element.removeAttribute(prefix + ':' + name);
                }

UiElementNode rootUiNode = editor.getUiRootNode();
                if (rootUiNode != null && v != null) {
final UiElementNode uiNode = rootUiNode.findXmlNode(element);
if (uiNode != null) {
editor.scheduleNodeReformat(uiNode, true /*attributesOnly*/);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 0befe8a..1a46b88 100644

//Synthetic comment -- @@ -27,12 +27,10 @@

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
import com.android.sdklib.SdkConstants;

import org.kxml2.io.KXmlParser;

import java.io.File;

//Synthetic comment -- @@ -55,6 +53,12 @@
/** The layout to be shown for the current {@code <fragment>} tag. Usually null. */
private String mFragmentLayout = null;

    /**
     * Creates a new {@link ContextPullParser}
     *
     * @param projectCallback the associated callback
     * @param file the file to be parsed
     */
public ContextPullParser(IProjectCallback projectCallback, File file) {
super();
mProjectCallback = projectCallback;
//Synthetic comment -- @@ -86,7 +90,7 @@

// At designtime, replace fragments with includes.
if (name.equals(VIEW_FRAGMENT)) {
            mFragmentLayout = LayoutMetadata.getProperty(this, KEY_FRAGMENT_LAYOUT);
if (mFragmentLayout != null) {
return VIEW_INCLUDE;
}
//Synthetic comment -- @@ -116,58 +120,4 @@

return value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index d27f3d8..d8e8220 100644

//Synthetic comment -- @@ -556,8 +556,7 @@
// Look for user-recorded preference for layout to be used for previews
if (adapterCookie instanceof UiViewElementNode) {
UiViewElementNode uiNode = (UiViewElementNode) adapterCookie;
            AdapterBinding binding = LayoutMetadata.getNodeBinding(viewObject, uiNode);
if (binding != null) {
return binding;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index a22b29a..3250beb 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;
import static com.android.util.XmlUtils.ANDROID_URI;
import static org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML.ContentTypeID_XML;

//Synthetic comment -- @@ -614,18 +615,31 @@
// Check attribute map
NamedNodeMap attributes1 = element1.getAttributes();
NamedNodeMap attributes2 = element2.getAttributes();

        List<Attr> attributeNodes1 = new ArrayList<Attr>();
        for (int i = 0, n = attributes1.getLength(); i < n; i++) {
            Attr attribute = (Attr) attributes1.item(i);
            // Ignore tools uri namespace attributes for equivalency test
            if (TOOLS_URI.equals(attribute.getNamespaceURI())) {
                continue;
            }
            attributeNodes1.add(attribute);
        }
        List<Attr> attributeNodes2 = new ArrayList<Attr>();
        for (int i = 0, n = attributes2.getLength(); i < n; i++) {
            Attr attribute = (Attr) attributes2.item(i);
            // Ignore tools uri namespace attributes for equivalency test
            if (TOOLS_URI.equals(attribute.getNamespaceURI())) {
                continue;
            }
            attributeNodes2.add(attribute);
        }

        if (attributeNodes1.size() != attributeNodes2.size()) {
return false;
}

if (attributes1.getLength() > 0) {
Collections.sort(attributeNodes1, ATTRIBUTE_COMPARATOR);
Collections.sort(attributeNodes2, ATTRIBUTE_COMPARATOR);
for (int i = 0; i < attributeNodes1.size(); i++) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
//Synthetic comment -- index 0b0faa0..b332713 100644

//Synthetic comment -- @@ -16,12 +16,14 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
//Synthetic comment -- @@ -43,7 +45,6 @@
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -86,6 +87,9 @@

SelectionItem first = selections.get(0);
UiViewElementNode node = first.getViewInfo().getUiViewNode();
        if (node == null) {
            return;
        }
Element element = (Element) node.getXmlNode();

String selected = getSelectedLayout();
//Synthetic comment -- @@ -176,7 +180,8 @@
* @param element the element for the fragment tag
* @return the fully qualified fragment class name, or null
*/
    @Nullable
    public static String getFragmentClass(@NonNull Element element) {
String fqcn = element.getAttribute(ATTR_CLASS);
if (fqcn == null || fqcn.length() == 0) {
fqcn = element.getAttributeNS(ANDROID_URI, ATTR_NAME);
//Synthetic comment -- @@ -194,10 +199,10 @@
* @param node the node corresponding to the {@code <fragment>} element
* @return the resource path to a layout to render for this fragment, or null
*/
    @Nullable
    public static String getFragmentLayout(@NonNull Node node) {
        String layout = LayoutMetadata.getProperty(
                node, LayoutMetadata.KEY_FRAGMENT_LAYOUT);
if (layout != null) {
return layout;
}
//Synthetic comment -- @@ -206,13 +211,16 @@
}

/** Returns the name of the currently displayed layout in the fragment, or null */
    @Nullable
private String getSelectedLayout() {
SelectionManager selectionManager = mCanvas.getSelectionManager();
for (SelectionItem item : selectionManager.getSelections()) {
UiViewElementNode node = item.getViewInfo().getUiViewNode();
            if (node != null) {
                String layout = getFragmentLayout(node.getXmlNode());
                if (layout != null) {
                    return layout;
                }
}
}
return null;
//Synthetic comment -- @@ -223,16 +231,18 @@
*
* @param layout the layout resource name to show in this fragment
*/
    public void setNewLayout(@Nullable String layout) {
LayoutEditorDelegate delegate = mCanvas.getEditorDelegate();
GraphicalEditorPart graphicalEditor = delegate.getGraphicalEditor();
SelectionManager selectionManager = mCanvas.getSelectionManager();

        for (SelectionItem item : selectionManager.getSnapshot()) {
UiViewElementNode node = item.getViewInfo().getUiViewNode();
            if (node != null) {
                Node xmlNode = node.getXmlNode();
                LayoutMetadata.setProperty(delegate.getEditor(), xmlNode, KEY_FRAGMENT_LAYOUT,
                        layout);
            }
}

// Refresh
//Synthetic comment -- @@ -255,7 +265,9 @@

@Override
public void run() {
            if (isChecked()) {
                setNewLayout(mLayout);
            }
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 9b4713d..9fef48e 100644

//Synthetic comment -- @@ -16,15 +16,19 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NUM_COLUMNS;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.DataBindingItem;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -38,11 +42,15 @@
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Design-time metadata lookup for layouts, such as fragment and AdapterView bindings.
 */
@SuppressWarnings("restriction") // XML DOM model
public class LayoutMetadata {
/** The default layout to use for list items in expandable list views */
//Synthetic comment -- @@ -65,31 +73,26 @@
/** The property key, included in comments, which references a fragment layout to show */
public static final String KEY_FRAGMENT_LAYOUT = "layout";        //$NON-NLS-1$

    /** Utility class, do not create instances */
private LayoutMetadata() {
}

/**
* Returns the given property of the given DOM node, or null
*
* @param document the document to look up and read lock the model for
* @param node the XML node to associate metadata with
* @param name the name of the property to look up
* @return the value stored with the given node and name, or null
     * @deprecated this method gets metadata using the old comment-based style; should
     *      only be used for migration at this point
*/
    @Deprecated
    @Nullable
    public static String getProperty(
            @Nullable IDocument document,
            @NonNull Node node,
            @NonNull String name) {
IStructuredModel model = null;
try {
if (document != null) {
//Synthetic comment -- @@ -112,6 +115,25 @@
}

/**
     * Returns the given property specified in the <b>current</b> element being
     * processed by the given pull parser.
     *
     * @param parser the pull parser, which must be in the middle of processing
     *            the target element
     * @param name the property name to look up
     * @return the property value, or null if not defined
     */
    @Nullable
    public static String getProperty(@NonNull XmlPullParser parser, @NonNull String name) {
        String value = parser.getAttributeValue(TOOLS_URI, name);
        if (value != null && value.isEmpty()) {
            value = null;
        }

        return value;
    }

    /**
* Returns the given property specified in the given XML comment
*
* @param name the name of the property to look up
//Synthetic comment -- @@ -146,8 +168,11 @@
* @param node the XML node to associate metadata with
* @param name the name of the property to set
* @param value the value to store for the given node and name, or null to remove it
     * @deprecated this method sets metadata using the old comment-based style; should
     *      only be used for migration at this point
*/
    @Deprecated
    public static void setProperty(IDocument document, Node node, String name, String value) {
// Reserved characters: [,-=]
assert name.indexOf('-') == -1;
assert value == null || value.indexOf('-') == -1;
//Synthetic comment -- @@ -268,7 +293,7 @@
}

/** Finds the comment node associated with the given node, or null if not found */
    private static Node findComment(Node node) {
NodeList children = node.getChildNodes();
for (int i = 0, n = children.getLength(); i < n; i++) {
Node child = children.item(i);
//Synthetic comment -- @@ -286,14 +311,25 @@
/**
* Returns the given property of the given DOM node, or null
*
* @param node the XML node to associate metadata with
* @param name the name of the property to look up
* @return the value stored with the given node and name, or null
*/
    @Nullable
    public static String getProperty(
            @NonNull Node node,
            @NonNull String name) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String value = element.getAttributeNS(TOOLS_URI, name);
            if (value != null && value.isEmpty()) {
                value = null;
            }

            return value;
        }

        return null;
}

/**
//Synthetic comment -- @@ -305,9 +341,20 @@
* @param name the name of the property to set
* @param value the value to store for the given node and name, or null to remove it
*/
    public static void setProperty(
            @NonNull AndroidXmlEditor editor,
            @NonNull final Node node,
            @NonNull final String name,
            @Nullable final String value) {
        // Clear out the old metadata
IDocument document = editor.getStructuredSourceViewer().getDocument();
        setProperty(document, node, name, null);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            AdtUtils.setToolsAttribute(editor, element, "Bind View", name, value,
                    false /*reveal*/, false /*append*/);
        }
}

/** Strips out @layout/ or @android:layout/ from the given layout reference */
//Synthetic comment -- @@ -329,73 +376,70 @@
* @param uiNode the ui node corresponding to the view object
* @return a binding, or null
*/
    public static AdapterBinding getNodeBinding(Object viewObject, UiViewElementNode uiNode) {
        Node xmlNode = uiNode.getXmlNode();

        String header = getProperty(xmlNode, KEY_LV_HEADER);
        String footer = getProperty(xmlNode, KEY_LV_FOOTER);
        String layout = getProperty(xmlNode, KEY_LV_ITEM);
        if (layout != null || header != null || footer != null) {
            int count = 12;
            // If we're dealing with a grid view, multiply the list item count
            // by the number of columns to ensure we have enough items
            if (xmlNode instanceof Element && xmlNode.getNodeName().endsWith(GRID_VIEW)) {
                Element element = (Element) xmlNode;
                String columns = element.getAttributeNS(ANDROID_URI, ATTR_NUM_COLUMNS);
                int multiplier = 2;
                if (columns != null && columns.length() > 0) {
                    int c = Integer.parseInt(columns);
                    if (c >= 1 && c <= 10) {
                        multiplier = c;
}
}
                count *= multiplier;
}
            AdapterBinding binding = new AdapterBinding(count);

            if (header != null) {
                boolean isFramework = header.startsWith(ANDROID_LAYOUT_PREFIX);
                binding.addHeader(new ResourceReference(stripLayoutPrefix(header),
                        isFramework));
            }

            if (footer != null) {
                boolean isFramework = footer.startsWith(ANDROID_LAYOUT_PREFIX);
                binding.addFooter(new ResourceReference(stripLayoutPrefix(footer),
                        isFramework));
            }

            if (layout != null) {
                boolean isFramework = layout.startsWith(ANDROID_LAYOUT_PREFIX);
                if (isFramework) {
                    layout = layout.substring(ANDROID_LAYOUT_PREFIX.length());
                } else if (layout.startsWith(LAYOUT_PREFIX)) {
                    layout = layout.substring(LAYOUT_PREFIX.length());
                }

                binding.addItem(new DataBindingItem(layout, isFramework, 1));
            } else if (viewObject != null) {
                String listFqcn = ProjectCallback.getListAdapterViewFqcn(viewObject.getClass());
                if (listFqcn != null) {
                    if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
                        binding.addItem(
                                new DataBindingItem(DEFAULT_EXPANDABLE_LIST_ITEM,
                                true /* isFramework */, 1));
                    } else {
                        binding.addItem(
                                new DataBindingItem(DEFAULT_LIST_ITEM,
                                true /* isFramework */, 1));
                    }
                }
            } else {
                binding.addItem(
                        new DataBindingItem(DEFAULT_LIST_ITEM,
                        true /* isFramework */, 1));
            }
            return binding;
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index d08bb91..d840c30 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_LV_HEADER;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_LV_ITEM;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
//Synthetic comment -- @@ -148,7 +150,9 @@

@Override
public void run() {
            if (isChecked()) {
                setNewType(KEY_LV_ITEM, ANDROID_LAYOUT_PREFIX + mLayout);
            }
}
}

//Synthetic comment -- @@ -204,33 +208,35 @@
}
}

    @Nullable
private String getSelectedLayout() {
String layout = null;
SelectionManager selectionManager = mCanvas.getSelectionManager();
for (SelectionItem item : selectionManager.getSelections()) {
UiViewElementNode node = item.getViewInfo().getUiViewNode();
            if (node != null) {
                Node xmlNode = node.getXmlNode();
                layout = LayoutMetadata.getProperty(xmlNode, KEY_LV_ITEM);
                if (layout != null) {
                    return layout;
                }
}
}

return null;
}

    private void setNewType(@NonNull String type, @Nullable String layout) {
LayoutEditorDelegate delegate = mCanvas.getEditorDelegate();
GraphicalEditorPart graphicalEditor = delegate.getGraphicalEditor();
SelectionManager selectionManager = mCanvas.getSelectionManager();

        for (SelectionItem item : selectionManager.getSnapshot()) {
UiViewElementNode node = item.getViewInfo().getUiViewNode();
            if (node != null) {
                Node xmlNode = node.getXmlNode();
                LayoutMetadata.setProperty(delegate.getEditor(), xmlNode, type, layout);
            }
}

// Refresh








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 2a9666b..1e5281d 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.util.XmlUtils.XMLNS_ANDROID;
import static com.android.util.XmlUtils.XMLNS_URI;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java
//Synthetic comment -- index 7a2c387..538adc9 100644

//Synthetic comment -- @@ -18,6 +18,8 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;

import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -26,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.util.Pair;
import com.android.util.XmlUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
//Synthetic comment -- @@ -33,18 +36,22 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 */
@SuppressWarnings({"restriction", "javadoc", "deprecation"}) // XML DOM model
public class LayoutMetadataTest extends AdtProjectTest {

    public void testOldMetadata1() throws Exception {
Pair<IDocument, UiElementNode> pair = getNode("metadata.xml", "listView1");
IDocument document = pair.getFirst();
UiElementNode uiNode = pair.getSecond();
Node node = uiNode.getXmlNode();

        assertNull(LayoutMetadata.getProperty(document, node, "foo"));
String before =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
"        android:layout_height=\"wrap_content\">\n" +
//Synthetic comment -- @@ -52,7 +59,7 @@
assertEquals(before, getText(document, node));

// Set the property
        LayoutMetadata.setProperty(document, node,
"listitem", "@android:layout/simple_list_item_checked");
String after =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
//Synthetic comment -- @@ -62,7 +69,7 @@
assertEquals(after, getText(document, node));

// Set a second property
        LayoutMetadata.setProperty(document, node,
"listheader", "@android:layout/browser_link_context_header");
after =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
//Synthetic comment -- @@ -75,7 +82,7 @@
assertEquals(after, getText(document, node));

// Set list item to a different layout
        LayoutMetadata.setProperty(document, node,
"listitem", "@android:layout/simple_list_item_single_choice");
after =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
//Synthetic comment -- @@ -88,7 +95,7 @@
assertEquals(after, getText(document, node));

// Set header to a different layout
        LayoutMetadata.setProperty(document, node,
"listheader", "@layout/foo");
after =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
//Synthetic comment -- @@ -101,7 +108,7 @@
assertEquals(after, getText(document, node));

// Clear out list item
        LayoutMetadata.setProperty(document, node,
"listitem", null);
after =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
//Synthetic comment -- @@ -111,7 +118,7 @@
assertEquals(after, getText(document, node));

// Clear out list header
        LayoutMetadata.setProperty(document, node,
"listheader", null);
after =
"<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
//Synthetic comment -- @@ -128,14 +135,13 @@
UiElementNode uiNode = pair.getSecond();
Node node = uiNode.getXmlNode();

        assertNull(LayoutMetadata.getProperty(document, node, "foo"));
String before =
"<Button android:text=\"Button\" android:id=\"@+id/button1\"/>";
assertEquals(before, getText(document, node));

// Set the property
        LayoutMetadata.setProperty(document, node,
"listitem", "@android:layout/simple_list_item_checked");
String after =
"<Button android:text=\"Button\" android:id=\"@+id/button1\">\n" +
//Synthetic comment -- @@ -144,6 +150,23 @@
assertEquals(after, getText(document, node));
}

    public void testMetadata1() throws Exception {
        Pair<IDocument, UiElementNode> pair = getNode("metadata.xml", "listView1");
        UiElementNode uiNode = pair.getSecond();
        Node node = uiNode.getXmlNode();

        assertNull(LayoutMetadata.getProperty(node, "foo"));

        Element element = (Element) node;
        String prefix = XmlUtils.lookupNamespacePrefix(element, TOOLS_URI, null);
        if (prefix == null) {
            // Add in new prefix...
            prefix = XmlUtils.lookupNamespacePrefix(element,
                    TOOLS_URI, TOOLS_PREFIX);
        }
        element.setAttribute(prefix + ':' + "foo", "bar");
    }

// ==== Test utilities ====

private static String getText(IDocument document, Node node) throws Exception {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilitiesTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilitiesTest.java
//Synthetic comment -- index 1fc358b..710725e 100644

//Synthetic comment -- @@ -15,6 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

//Synthetic comment -- @@ -72,6 +75,10 @@
assertFalse(DomUtilities.isEquivalent(root1, root2));
foo2.setAttribute("attribute1", "value1");
assertTrue(DomUtilities.isEquivalent(root1, root2));
        foo2.setAttributeNS(TOOLS_URI, "foo", "bar");
        assertTrue(DomUtilities.isEquivalent(root1, root2));
        foo2.setAttributeNS(ANDROID_URI, "foo", "bar");
        assertFalse(DomUtilities.isEquivalent(root1, root2));

// TODO - test different tag names
// TODO - test different name spaces!







