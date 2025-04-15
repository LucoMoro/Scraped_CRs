/*Synchronize layout metadata across layout configurations

If you choose a preview layout to use for a ListView or GridView, or
if you choose a fragment layout to use for a <layout> tag, until now
that setting has only applied to the exact layout you are currently
editing.

This changeset makes the setting also get synchronized to all the
other layout variations (-land, -xlarge etc) of the layout, such that
you don't have to find yourself tweaking this setting each time you
open some new variation of the layout after changing preview settings.

(Also deletes a bunch of the old comment-based metadata code.)

Change-Id:I712213920a8956901da157eb94611b2372368fcc*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 4ef469d..e45e68e 100644

//Synthetic comment -- @@ -18,11 +18,13 @@

import static com.android.SdkConstants.TOOLS_PREFIX;
import static com.android.SdkConstants.TOOLS_URI;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper.IProjectFilter;
//Synthetic comment -- @@ -34,6 +36,10 @@
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -47,6 +53,7 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
//Synthetic comment -- @@ -68,10 +75,16 @@
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -80,6 +93,7 @@
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
//Synthetic comment -- @@ -349,6 +363,47 @@
}

/**
* Attempts to convert the given {@link URL} into a {@link File}.
*
* @param url the {@link URL} to be converted
//Synthetic comment -- @@ -652,6 +707,23 @@
}

/**
* Sets the given tools: attribute in the given XML editor document, adding
* the tools name space declaration if necessary, formatting the affected
* document region, and optionally comma-appending to an existing value and
//Synthetic comment -- @@ -803,6 +875,97 @@
}

/**
* Returns the Android version and code name of the given API level
*
* @param api the api level
//Synthetic comment -- @@ -828,7 +991,7 @@
case 15: return "API 15: Android 4.0.3 (IceCreamSandwich)";
case 16: return "API 16: Android 4.1 (Jelly Bean)";
// If you add more versions here, also update #getBuildCodes and
            // LintConstants#HIGHEST_KNOWN_API

default: {
// Consult SDK manager to see if we know any more (later) names,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index b364f57..145036b 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.utils.Pair;

//Synthetic comment -- @@ -184,6 +185,31 @@
}

/**
* Returns the XML DOM node corresponding to the given offset of the given
* document.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index e3ae04e..82172fc 100644

//Synthetic comment -- @@ -28,24 +28,27 @@
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.DataBindingItem;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//Synthetic comment -- @@ -63,8 +66,6 @@

/** The string to start metadata comments with */
private static final String COMMENT_PROLOGUE = " Preview: ";
    /** The string to end metadata comments with */
    private static final String COMMENT_EPILOGUE = " ";
/** The property key, included in comments, which references a list item layout */
public static final String KEY_LV_ITEM = "listitem";        //$NON-NLS-1$
/** The property key, included in comments, which references a list header layout */
//Synthetic comment -- @@ -79,43 +80,6 @@
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
                IModelManager modelManager = StructuredModelManager.getModelManager();
                model = modelManager.getExistingModelForRead(document);
            }

            Node comment = findComment(node);
            if (comment != null) {
                String text = comment.getNodeValue();
                return getProperty(name, text);
            }

            return null;
        } finally {
            if (model != null) {
                model.releaseFromRead();
            }
        }
    }

    /**
* Returns the given property specified in the <b>current</b> element being
* processed by the given pull parser.
*
//Synthetic comment -- @@ -135,124 +99,25 @@
}

/**
     * Returns the given property specified in the given XML comment
*
     * @param name the name of the property to look up
     * @param text the comment text for an XML node
     * @return the value stored with the given node and name, or null
     */
    public static String getProperty(String name, String text) {
        assert text.startsWith(COMMENT_PROLOGUE);
        String valuesString = text.substring(COMMENT_PROLOGUE.length());
        String[] values = valuesString.split(","); //$NON-NLS-1$
        if (values.length == 1) {
            valuesString = values[0].trim();
            if (valuesString.indexOf('\n') != -1) {
                values = valuesString.split("\n"); //$NON-NLS-1$
            }
        }
        String target = name + '=';
        for (int j = 0; j < values.length; j++) {
            String value = values[j].trim();
            if (value.startsWith(target)) {
                return value.substring(target.length()).trim();
            }
        }
        return null;
    }

    /**
     * Sets the given property of the given DOM node to a given value, or if null clears
     * the property.
     *
     * @param document the document to look up and write lock the model for
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
        assert name.indexOf(',') == -1;
        assert value == null || value.indexOf(',') == -1;
        assert name.indexOf('=') == -1;
        assert value == null || value.indexOf('=') == -1;

        IStructuredModel model = null;
        try {
            IModelManager modelManager = StructuredModelManager.getModelManager();
            model = modelManager.getExistingModelForEdit(document);
            if (model instanceof IDOMModel) {
                IDOMModel domModel = (IDOMModel) model;
                Document domDocument = domModel.getDocument();
                assert node.getOwnerDocument() == domDocument;
            }

            Document doc = node.getOwnerDocument();
            Node commentNode = findComment(node);

            String commentText = null;
            if (commentNode != null) {
                String text = commentNode.getNodeValue();
                assert text.startsWith(COMMENT_PROLOGUE);
                String valuesString = text.substring(COMMENT_PROLOGUE.length());
                String[] values = valuesString.split(","); //$NON-NLS-1$
                if (values.length == 1) {
                    valuesString = values[0].trim();
                    if (valuesString.indexOf('\n') != -1) {
                        values = valuesString.split("\n"); //$NON-NLS-1$
                    }
                }
                String target = name + '=';
                List<String> preserve = new ArrayList<String>();
                for (int j = 0; j < values.length; j++) {
                    String v = values[j].trim();
                    if (v.length() == 0) {
                        continue;
                    }
                    if (!v.startsWith(target)) {
                        preserve.add(v.trim());
                    }
                }
                if (value != null) {
                    preserve.add(name + '=' + value.trim());
                }
                if (preserve.size() > 0) {
                    if (preserve.size() > 1) {
                        Collections.sort(preserve);
                        String firstLineIndent = AndroidXmlEditor.getIndent(document, commentNode);
                        String oneIndentLevel = "    "; //$NON-NLS-1$
                        StringBuilder sb = new StringBuilder();
                        sb.append(COMMENT_PROLOGUE);
                        sb.append('\n');
                        for (String s : preserve) {
                            sb.append(firstLineIndent);
                            sb.append(oneIndentLevel);
                            sb.append(s);
                            sb.append('\n');
                        }
                        sb.append(firstLineIndent);
                        sb.append(COMMENT_EPILOGUE);
                        commentText = sb.toString();
                    } else {
                        commentText = COMMENT_PROLOGUE + preserve.get(0) + COMMENT_EPILOGUE;
                    }
                }
            } else if (value != null) {
                commentText = COMMENT_PROLOGUE + name + '=' + value + COMMENT_EPILOGUE;
            }

            if (commentText == null) {
                if (commentNode != null) {
// Remove the comment, along with surrounding whitespace if applicable
Node previous = commentNode.getPreviousSibling();
if (previous != null && previous.getNodeType() == Node.TEXT_NODE) {
                        String text = previous.getNodeValue();
                        if (text.trim().length() == 0) {
node.removeChild(previous);
}
}
//Synthetic comment -- @@ -260,55 +125,15 @@
Node first = node.getFirstChild();
if (first != null && first.getNextSibling() == null
&& first.getNodeType() == Node.TEXT_NODE) {
                        String text = first.getNodeValue();
                        if (text.trim().length() == 0) {
node.removeChild(first);
}
}
}
                return;
            }

            if (commentNode != null) {
                commentNode.setNodeValue(commentText);
            } else {
                commentNode = doc.createComment(commentText);
                String firstLineIndent = AndroidXmlEditor.getIndent(document, node);
                Node firstChild = node.getFirstChild();
                boolean indentAfter = firstChild == null
                        || firstChild.getNodeType() != Node.TEXT_NODE
                        || firstChild.getNodeValue().indexOf('\n') == -1;
                String oneIndentLevel = "    "; //$NON-NLS-1$
                node.insertBefore(doc.createTextNode('\n' + firstLineIndent + oneIndentLevel),
                        firstChild);
                node.insertBefore(commentNode, firstChild);
                if (indentAfter) {
                    node.insertBefore(doc.createTextNode('\n' + firstLineIndent), firstChild);
                }
            }
        } finally {
            if (model != null) {
                model.releaseFromEdit();
}
}
}

    /** Finds the comment node associated with the given node, or null if not found */
    private static Node findComment(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.COMMENT_NODE) {
                String text = child.getNodeValue();
                if (text.startsWith(COMMENT_PROLOGUE)) {
                    return child;
                }
            }
        }

        return null;
    }

/**
* Returns the given property of the given DOM node, or null
*
//Synthetic comment -- @@ -343,21 +168,120 @@
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
private static String stripLayoutPrefix(String layout) {
if (layout.startsWith(ANDROID_LAYOUT_RESOURCE_PREFIX)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java
//Synthetic comment -- index a75fc96..fa9e18f 100644

//Synthetic comment -- @@ -45,111 +45,6 @@
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
            "    </ListView>";
        assertEquals(before, getText(document, node));

        // Set the property
        LayoutMetadata.setProperty(document, node,
                "listitem", "@android:layout/simple_list_item_checked");
        String after =
            "<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
            "        android:layout_height=\"wrap_content\">\n" +
            "        <!-- Preview: listitem=@android:layout/simple_list_item_checked -->\n" +
            "    </ListView>";
        assertEquals(after, getText(document, node));

        // Set a second property
        LayoutMetadata.setProperty(document, node,
                "listheader", "@android:layout/browser_link_context_header");
        after =
            "<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
            "        android:layout_height=\"wrap_content\">\n" +
            "        <!-- Preview: \n" +
            "            listheader=@android:layout/browser_link_context_header\n" +
            "            listitem=@android:layout/simple_list_item_checked\n" +
            "         -->\n" +
            "    </ListView>";
        assertEquals(after, getText(document, node));

        // Set list item to a different layout
        LayoutMetadata.setProperty(document, node,
                "listitem", "@android:layout/simple_list_item_single_choice");
        after =
            "<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
            "        android:layout_height=\"wrap_content\">\n" +
            "        <!-- Preview: \n" +
            "            listheader=@android:layout/browser_link_context_header\n" +
            "            listitem=@android:layout/simple_list_item_single_choice\n" +
            "         -->\n" +
            "    </ListView>";
        assertEquals(after, getText(document, node));

        // Set header to a different layout
        LayoutMetadata.setProperty(document, node,
                "listheader", "@layout/foo");
        after =
            "<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
            "        android:layout_height=\"wrap_content\">\n" +
            "        <!-- Preview: \n" +
            "            listheader=@layout/foo\n" +
            "            listitem=@android:layout/simple_list_item_single_choice\n" +
            "         -->\n" +
            "    </ListView>";
        assertEquals(after, getText(document, node));

        // Clear out list item
        LayoutMetadata.setProperty(document, node,
                "listitem", null);
        after =
            "<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
            "        android:layout_height=\"wrap_content\">\n" +
            "        <!-- Preview: listheader=@layout/foo -->\n" +
            "    </ListView>";
        assertEquals(after, getText(document, node));

        // Clear out list header
        LayoutMetadata.setProperty(document, node,
                "listheader", null);
        after =
            "<ListView android:layout_width=\"match_parent\" android:id=\"@+id/listView1\"\n" +
            "        android:layout_height=\"wrap_content\"></ListView>";
        assertEquals(after, getText(document, node));

        // Check node expansion on the button which doesn't have an end tag:
        before = "<Button android:text=\"Button\" android:id=\"@+id/button1\"/>";
    }

    public void testMetadata2() throws Exception {
        Pair<IDocument, UiElementNode> pair = getNode("metadata.xml", "button1");
        IDocument document = pair.getFirst();
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
            "        <!-- Preview: listitem=@android:layout/simple_list_item_checked -->\n" +
            "    </Button>";
        assertEquals(after, getText(document, node));
    }

public void testMetadata1() throws Exception {
Pair<IDocument, UiElementNode> pair = getNode("metadata.xml", "listView1");
UiElementNode uiNode = pair.getSecond();







