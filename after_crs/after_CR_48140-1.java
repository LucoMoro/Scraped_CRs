/*Improved XML pretty printing

This moves the XML Pretty Printer up from Eclispe to
sdk_common such that it can be accessed for example
from the builder library when merging XML files.

Change-Id:I2cb316bf1dfa5e0e665471f886f2ddc647592a6e*/




//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/XmlUtils.java b/common/src/main/java/com/android/utils/XmlUtils.java
//Synthetic comment -- index c77baee..2c45055 100644

//Synthetic comment -- @@ -15,6 +15,24 @@
*/
package com.android.utils;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.android.SdkConstants.AMP_ENTITY;
import static com.android.SdkConstants.ANDROID_NS_NAME;
import static com.android.SdkConstants.ANDROID_URI;
//Synthetic comment -- @@ -26,22 +44,13 @@
import static com.android.SdkConstants.XMLNS_PREFIX;
import static com.android.SdkConstants.XMLNS_URI;

/** XML Utilities */
public class XmlUtils {
    public static final String XML_COMMENT_BEGIN = "<!--"; //$NON-NLS-1$
    public static final String XML_COMMENT_END = "-->";    //$NON-NLS-1$
    public static final String XML_PROLOG =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";  //$NON-NLS-1$

/**
* Returns the namespace prefix matching the requested namespace URI.
* If no such declaration is found, returns the default "android" prefix for
//Synthetic comment -- @@ -280,86 +289,87 @@
}

/**
     * Returns true if the given node has one or more element children
*
     * @param node the node to test for element children
     * @return true if the node has one or more element children
     */
    public static boolean hasElementChildren(@NonNull Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Parses the given XML string as a DOM document, using the JDK parser. The parser does not
     * validate, and is namespace aware.
     *
     * @param xml            the XML content to be parsed (must be well formed)
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document, or null
     */
    @Nullable
    public static Document parseDocumentSilently(@NonNull String xml, boolean namespaceAware) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource is = new InputSource(new StringReader(xml));
        factory.setNamespaceAware(namespaceAware);
        factory.setValidating(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(is);
        } catch (Exception e) {
            // pass
            // This method is deliberately silent; will return null
        }

        return null;
    }

    /**
     * Dump an XML tree to string. This does not perform any pretty printing.
     * To perform pretty printing, use {@code XmlPrettyPrinter.prettyPrint(node)} in
     * {@code sdk_common}.
*/
public static String toXml(Node node, boolean preserveWhitespace) {
StringBuilder sb = new StringBuilder(1000);
        append(sb, node, 0);
return sb.toString();
}

    /** Dump node to string without indentation adjustments */
private static void append(
@NonNull StringBuilder sb,
@NonNull Node node,
            int indent) {
short nodeType = node.getNodeType();
switch (nodeType) {
case Node.DOCUMENT_NODE:
case Node.DOCUMENT_FRAGMENT_NODE: {
                sb.append(XML_PROLOG);
NodeList children = node.getChildNodes();
for (int i = 0, n = children.getLength(); i < n; i++) {
                    append(sb, children.item(i), indent);
}
break;
}
case Node.COMMENT_NODE:
case Node.TEXT_NODE: {
if (nodeType == Node.COMMENT_NODE) {
                    sb.append(XML_COMMENT_BEGIN);
}
String text = node.getNodeValue();
                sb.append(toXmlTextValue(text));
if (nodeType == Node.COMMENT_NODE) {
                    sb.append(XML_COMMENT_END);
}
break;
}
case Node.ELEMENT_NODE: {
sb.append('<');
Element element = (Element) node;
sb.append(element.getTagName());
//Synthetic comment -- @@ -384,26 +394,14 @@
sb.append('/');
}
sb.append('>');
if (childCount > 0) {
for (int i = 0; i < childCount; i++) {
Node child = children.item(i);
                        append(sb, child, indent + 1);
}
sb.append('<').append('/');
sb.append(element.getTagName());
sb.append('>');
}
break;
}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/XmlUtilsTest.java b/common/src/test/java/com/android/utils/XmlUtilsTest.java
//Synthetic comment -- index edf0235..02fd465 100644

//Synthetic comment -- @@ -15,21 +15,24 @@
*/
package com.android.utils;

import com.android.SdkConstants;
import com.android.annotations.Nullable;

import junit.framework.TestCase;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.android.SdkConstants.XMLNS;

@SuppressWarnings("javadoc")
public class XmlUtilsTest extends TestCase {
//Synthetic comment -- @@ -116,8 +119,27 @@
assertEquals("&lt;\"'>&amp;", sb.toString());
}

    public void testHasChildren() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        assertFalse(XmlUtils.hasElementChildren(document));
        document.appendChild(document.createElement("A"));
        Element a = document.getDocumentElement();
        assertFalse(XmlUtils.hasElementChildren(a));
        a.appendChild(document.createTextNode("foo"));
        assertFalse(XmlUtils.hasElementChildren(a));
        Element b = document.createElement("B");
        a.appendChild(b);
        assertTrue(XmlUtils.hasElementChildren(a));
        assertFalse(XmlUtils.hasElementChildren(b));
    }

    public void testToXml() throws Exception {
Document doc = createEmptyPlainDocument();
        assertNotNull(doc);
Element root = doc.createElement("myroot");
doc.appendChild(root);
root.setAttribute("foo", "bar");
//Synthetic comment -- @@ -133,28 +155,93 @@
Node text = doc.createTextNode("  This is my text  ");
child3.appendChild(text);

        String xml = XmlUtils.toXml(doc, true);
assertEquals(
"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<myroot baz=\"baz\" foo=\"bar\"><mychild/><hasComment><!--This is my comment--></hasComment><hasText>  This is my text  </hasText></myroot>",
xml);
    }

    public void testToXml2() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "    <string \n"
                + "        name=\"description_search\">Search</string>\n"
                + "    <string \n"
                + "        name=\"description_map\">Map</string>\n"
                + "    <string\n"
                + "         name=\"description_refresh\">Refresh</string>\n"
                + "    <string \n"
                + "        name=\"description_share\">Share</string>\n"
                + "</resources>";

        Document doc = parse(xml);

        String formatted = XmlUtils.toXml(doc, true);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "    <string name=\"description_search\">Search</string>\n"
                + "    <string name=\"description_map\">Map</string>\n"
                + "    <string name=\"description_refresh\">Refresh</string>\n"
                + "    <string name=\"description_share\">Share</string>\n"
                + "</resources>",
                formatted);
    }

    public void testToXml3() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<root>\n"
                + "    <!-- ============== -->\n"
                + "    <!-- Generic styles -->\n"
                + "    <!-- ============== -->\n"
                + "</root>";
        Document doc = parse(xml);

        String formatted = XmlUtils.toXml(doc, true);
        assertEquals(xml, formatted);
    }

    public void testToXml3b() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "  <!-- ============== -->\n"
                + "  <!-- Generic styles -->\n"
                + "         <!-- ============== -->\n"
                + " <string     name=\"test\">test</string>\n"
                + "</resources>";
        Document doc = parse(xml);

        String formatted = XmlUtils.toXml(doc, true);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "  <!-- ============== -->\n"
                + "  <!-- Generic styles -->\n"
                + "         <!-- ============== -->\n"
                + " <string name=\"test\">test</string>\n"
                + "</resources>",
                formatted);
    }


    public void testToXml4() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!-- ============== -->\n"
                + "<!-- Generic styles -->\n"
                + "<!-- ============== -->\n"
                + "<root/>";
        Document doc = parse(xml);

xml = XmlUtils.toXml(doc, true);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!-- ============== --><!-- Generic styles --><!-- ============== --><root/>",
                xml);
}

@Nullable
//Synthetic comment -- @@ -167,4 +254,21 @@
builder = factory.newDocumentBuilder();
return builder.newDocument();
}

    @Nullable
    private static Document parse(String xml) throws Exception {
        if (true) {
            return XmlUtils.parseDocumentSilently(xml, true);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setExpandEntityReferences(false);
        factory.setXIncludeAware(false);
        factory.setIgnoringComments(false);
        factory.setCoalescing(false);
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlAttributeSortOrder.java b/sdk_common/src/com/android/ide/common/xml/XmlAttributeSortOrder.java
new file mode 100644
//Synthetic comment -- index 0000000..ea31f8b

//Synthetic comment -- @@ -0,0 +1,208 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.xml;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import org.w3c.dom.Attr;

import java.util.Comparator;

import static com.android.SdkConstants.ATTR_COLOR;
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.ATTR_STYLE;
import static com.android.SdkConstants.XMLNS;
import static com.google.common.base.Strings.nullToEmpty;

/** Order to use when sorting attributes */
public enum XmlAttributeSortOrder {
    NO_SORTING("none"),     //$NON-NLS-1$
    ALPHABETICAL("alpha"),  //$NON-NLS-1$
    LOGICAL("logical");     //$NON-NLS-1$

    XmlAttributeSortOrder(String key) {
        this.key = key;
    }

    public final String key;

    /**
     * @return a comparator for use by this attribute sort order
     */
    @Nullable
    public Comparator<Attr> getAttributeComparator() {
        switch (this) {
            case NO_SORTING:
                return null;
            case ALPHABETICAL:
                return ALPHABETICAL_COMPARATOR;
            case LOGICAL:
            default:
                return SORTED_ORDER_COMPARATOR;
        }
    }

    /** Comparator which can be used to sort attributes in the coding style priority order */
    private static final Comparator<Attr> SORTED_ORDER_COMPARATOR = new Comparator<Attr>() {
        @Override
        public int compare(Attr attr1, Attr attr2) {
            // Namespace declarations should always go first
            String prefix1 = attr1.getPrefix();
            String prefix2 = attr2.getPrefix();
            if (XMLNS.equals(prefix1)) {
                if (XMLNS.equals(prefix2)) {
                    return 0;
                }
                return -1;
            } else if (XMLNS.equals(attr2.getPrefix())) {
                return 1;
            }

            // Sort by preferred attribute order
            String name1 = prefix1 != null ? attr1.getLocalName() : attr1.getName();
            String name2 = prefix2 != null ? attr2.getLocalName() : attr2.getName();
            return compareAttributes(prefix1, name1, prefix2, name2);
        }
    };

    /**
     * Comparator which can be used to sort attributes into alphabetical order (but xmlns
     * is always first)
     */
    private static final Comparator<Attr> ALPHABETICAL_COMPARATOR = new Comparator<Attr>() {
        @Override
        public int compare(Attr attr1, Attr attr2) {
            // Namespace declarations should always go first
            if (XMLNS.equals(attr1.getPrefix())) {
                if (XMLNS.equals(attr2.getPrefix())) {
                    return 0;
                }
                return -1;
            } else if (XMLNS.equals(attr2.getPrefix())) {
                return 1;
            }

            // Sort by name rather than local name to ensure we sort by namespaces first,
            // then by names.
            return attr1.getName().compareTo(attr2.getName());
        }
    };

    /**
     * Returns {@link Comparator} values for ordering attributes in the following
     * order:
     * <ul>
     *   <li> id
     *   <li> style
     *   <li> layout_width
     *   <li> layout_height
     *   <li> other layout params, sorted alphabetically
     *   <li> other attributes, sorted alphabetically
     * </ul>
     *
     * @param name1 the first attribute name to compare
     * @param name2 the second attribute name to compare
     * @return a negative number if name1 should be ordered before name2
     */
    public static int compareAttributes(String name1, String name2) {
        int priority1 = getAttributePriority(name1);
        int priority2 = getAttributePriority(name2);
        if (priority1 != priority2) {
            return priority1 - priority2;
        }

        // Sort remaining attributes alphabetically
        return name1.compareTo(name2);
    }

    /**
     * Returns {@link Comparator} values for ordering attributes in the following
     * order:
     * <ul>
     *   <li> id
     *   <li> style
     *   <li> layout_width
     *   <li> layout_height
     *   <li> other layout params, sorted alphabetically
     *   <li> other attributes, sorted alphabetically, first by namespace, then by name
     * </ul>
     * @param prefix1 the namespace prefix, if any, of {@code name1}
     * @param name1 the first attribute name to compare
     * @param prefix2  the namespace prefix, if any, of {@code name2}
     * @param name2 the second attribute name to compare
     * @return a negative number if name1 should be ordered before name2
     */
    public static int compareAttributes(
            @Nullable String prefix1, @NonNull String name1,
            @Nullable String prefix2, @NonNull String name2) {
        int priority1 = getAttributePriority(name1);
        int priority2 = getAttributePriority(name2);
        if (priority1 != priority2) {
            return priority1 - priority2;
        }

        int namespaceDelta = nullToEmpty(prefix1).compareTo(nullToEmpty(prefix2));
        if (namespaceDelta != 0) {
            return namespaceDelta;
        }

        // Sort remaining attributes alphabetically
        return name1.compareTo(name2);
    }


    /** Returns a sorting priority for the given attribute name */
    private static int getAttributePriority(String name) {
        if (ATTR_ID.equals(name)) {
            return 10;
        }

        if (ATTR_NAME.equals(name)) {
            return 15;
        }

        if (ATTR_STYLE.equals(name)) {
            return 20;
        }

        if (name.startsWith(ATTR_LAYOUT_RESOURCE_PREFIX)) {
            // Width and height are special cased because we (a) want width and height
            // before the other layout attributes, and (b) we want width to sort before height
            // even though it comes after it alphabetically.
            if (name.equals(ATTR_LAYOUT_WIDTH)) {
                return 30;
            }
            if (name.equals(ATTR_LAYOUT_HEIGHT)) {
                return 40;
            }

            return 50;
        }

        // "color" sorts to the end
        if (ATTR_COLOR.equals(name)) {
            return 100;
        }

        return 60;
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlFormatPreferences.java b/sdk_common/src/com/android/ide/common/xml/XmlFormatPreferences.java
new file mode 100644
//Synthetic comment -- index 0000000..34317f7

//Synthetic comment -- @@ -0,0 +1,95 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.xml;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;

import org.w3c.dom.Attr;

import java.util.Comparator;

/**
 * Formatting preferences used by the Android XML formatter.
 */
public class XmlFormatPreferences {
    /** Use the Eclipse indent (tab/space, indent size) settings? */
    public boolean useEclipseIndent = false;

    /** Remove empty lines in all cases? */
    public boolean removeEmptyLines = false;

    /** Reformat the text and comment blocks? */
    public boolean reflowText = false;

    /** Join lines when reformatting text and comment blocks? */
    public boolean joinLines = false;

    /** Can attributes appear on the same line as the opening line if there is just one of them? */
    public boolean oneAttributeOnFirstLine = true;

    /** The sorting order to use when formatting */
    public XmlAttributeSortOrder sortAttributes = XmlAttributeSortOrder.LOGICAL;

    /** Returns the comparator to use when formatting, or null for no sorting */
    @Nullable
    public Comparator<Attr> getAttributeComparator() {
        return sortAttributes.getAttributeComparator();
    }

    /** Should there be a space before the closing > or /> ? */
    public boolean spaceBeforeClose = true;

    /** The string to insert for each indentation level */
    protected String mOneIndentUnit = "    "; //$NON-NLS-1$

    /** Tab width (number of spaces to display for a tab) */
    protected int mTabWidth = -1; // -1: uninitialized

    @VisibleForTesting
    protected XmlFormatPreferences() {
    }

    /**
     * Returns a new preferences object initialized with the defaults
     *
     * @return an {@link XmlFormatPreferences} object
     */
    @NonNull
    public static XmlFormatPreferences defaults() {
        return new XmlFormatPreferences();
    }

    public String getOneIndentUnit() {
        return mOneIndentUnit;
    }

    /**
     * Returns the number of spaces used to display a single tab character
     *
     * @return the number of spaces used to display a single tab character
     */
    @SuppressWarnings("restriction") // Editor settings
    public int getTabWidth() {
        if (mTabWidth == -1) {
            mTabWidth = 4;
        }

        return mTabWidth;
    }
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlFormatStyle.java b/sdk_common/src/com/android/ide/common/xml/XmlFormatStyle.java
new file mode 100644
//Synthetic comment -- index 0000000..b796fe4

//Synthetic comment -- @@ -0,0 +1,86 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.xml;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.xml.AndroidManifest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Style to use when printing the XML. Different types of Android XML files use slightly
 * different preferred formats. For example, in layout files there is typically always a
 * newline between successive elements, whereas in a manifest file there is typically only
 * newlines between different types of elements. As another example, in resource files,
 * the format is typically much more compact: the text content of {@code <item>} tags is
 * included on the same line whereas for other layout styles the children are typically
 * placed on a line of their own.
 */
public enum XmlFormatStyle {
    /** Layout formatting style: blank lines between elements, attributes on separate lines */
    LAYOUT,

    /** Similar to layout formatting style, but no blank lines inside opening elements */
    FILE,

    /** Resource style: one line per complete element including text child content */
    RESOURCE,

    /**
     * Similar to layout style, but no newlines between related elements such as
     * successive {@code <uses-permission>} declarations, and no newlines inside
     * the second level elements (so an {@code <activity>} declaration appears as a
     * single block with no whitespace within it)
     */
    MANIFEST;

    @NonNull
    public static XmlFormatStyle get(@Nullable Node node) {
        if (node != null) {
            Document doc = (node.getNodeType() == Node.DOCUMENT_NODE)
                    ? (Document) node : node.getOwnerDocument();
            if (doc != null) {
                Element root = doc.getDocumentElement();
                if (root != null) {
                    String tag = root.getTagName();
                    if (tag.equals(SdkConstants.TAG_RESOURCES)) {
                        return RESOURCE;
                    } else if (tag.equals(AndroidManifest.NODE_MANIFEST)) {
                        return MANIFEST;
                    }

                    // How do we detect a layout vs other files such as drawables??
                    // For now, assume that capitalized tags are view names, or names
                    // with package components are custom views
                    if (Character.isUpperCase(tag.charAt(0))
                            || SdkConstants.VIEW_TAG.equals(tag)
                            || SdkConstants.VIEW_INCLUDE.equals(tag)
                            || SdkConstants.VIEW_MERGE.equals(tag)
                            || tag.indexOf('.') != -1) {
                        return LAYOUT;
                    }
                }
            }
        }

        return FILE;
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlPrettyPrinter.java b/sdk_common/src/com/android/ide/common/xml/XmlPrettyPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..37481b6

//Synthetic comment -- @@ -0,0 +1,1039 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.xml;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.utils.SdkUtils;
import com.android.utils.XmlUtils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.android.SdkConstants.TAG_COLOR;
import static com.android.SdkConstants.TAG_DIMEN;
import static com.android.SdkConstants.TAG_ITEM;
import static com.android.SdkConstants.TAG_STRING;
import static com.android.SdkConstants.TAG_STYLE;
import static com.android.SdkConstants.XMLNS;
import static com.android.utils.XmlUtils.XML_COMMENT_BEGIN;
import static com.android.utils.XmlUtils.XML_COMMENT_END;
import static com.android.utils.XmlUtils.XML_PROLOG;

/**
 * Visitor which walks over the subtree of the DOM to be formatted and pretty prints
 * the DOM into the given {@link StringBuilder}
 */
@SuppressWarnings("restriction")
public class XmlPrettyPrinter {

    /** The style to print the XML in */
    private final XmlFormatStyle mStyle;

    /** Formatting preferences to use when formatting the XML */
    private final XmlFormatPreferences mPrefs;
    /** Start node to start formatting at */
    private Node mStartNode;
    /** Start node to stop formatting after */
    private Node mEndNode;
    /** Whether the visitor is currently in range */
    private boolean mInRange;
    /** Output builder */
    private StringBuilder mOut;
    /** String to insert for a single indentation level */
    private String mIndentString;
    /** Line separator to use */
    private String mLineSeparator;
    /** If true, we're only formatting an open tag */
    private boolean mOpenTagOnly;
    /** List of indentation to use for each given depth */
    private String[] mIndentationLevels;

    /**
     * Creates a new {@link XmlPrettyPrinter}
     *
     * @param prefs the preferences to format with
     * @param style the style to format with
     * @param lineSeparator the line separator to use, such as "\n" (can be null, in which
     *            case the system default is looked up via the line.separator property)
     */
    public XmlPrettyPrinter(XmlFormatPreferences prefs, XmlFormatStyle style,
            String lineSeparator) {
        mPrefs = prefs;
        mStyle = style;
        if (lineSeparator == null) {
            lineSeparator = SdkUtils.getLineSeparator();
        }
        mLineSeparator = lineSeparator;
    }

    /**
     * Sets the indentation levels to use (indentation string to use for each depth,
     * indexed by depth
     *
     * @param indentationLevels an array of strings to use for the various indentation
     *            levels
     */
    public void setIndentationLevels(String[] indentationLevels) {
        mIndentationLevels = indentationLevels;
    }

    /**
     * Pretty-prints the given XML document, which must be well-formed. If it is not,
     * the original unformatted XML document is returned
     *
     * @param xml the XML content to format
     * @param prefs the preferences to format with
     * @param style the style to format with
     * @param lineSeparator the line separator to use, such as "\n" (can be null, in which
     *     case the system default is looked up via the line.separator property)
     * @return the formatted document (or if a parsing error occurred, returns the
     *     unformatted document)
     */
    @NonNull
    public static String prettyPrint(
            @NonNull String xml,
            @NonNull XmlFormatPreferences prefs,
            @NonNull XmlFormatStyle style,
            @Nullable String lineSeparator) {
        Document document = XmlUtils.parseDocumentSilently(xml, true);
        if (document != null) {
            XmlPrettyPrinter printer = new XmlPrettyPrinter(prefs, style, lineSeparator);
            StringBuilder sb = new StringBuilder(3 * xml.length() / 2);
            printer.prettyPrint(-1, document, null, null, sb, false /*openTagOnly*/);
            return sb.toString();
        } else {
            // Parser error: just return the unformatted content
            return xml;
        }
    }

    /**
     * Pretty prints the given node
     *
     * @param node the node, usually a document, to be printed
     * @param prefs the formatting preferences
     * @param style the formatting style to use
     * @param lineSeparator the line separator to use, or null to use the
     *            default
     * @return a formatted string
     */
    @NonNull
    public static String prettyPrint(
            @NonNull Node node,
            @NonNull XmlFormatPreferences prefs,
            @NonNull XmlFormatStyle style,
            @Nullable String lineSeparator) {
        XmlPrettyPrinter printer = new XmlPrettyPrinter(prefs, style, lineSeparator);
        StringBuilder sb = new StringBuilder(1000);
        printer.prettyPrint(-1, node, null, null, sb, false /*openTagOnly*/);
        String xml = sb.toString();
        if (node.getNodeType() == Node.DOCUMENT_NODE && !xml.startsWith("<?")) { //$NON-NLS-1$
            xml = XML_PROLOG + xml;
        }
        return xml;
    }

    /**
     * Pretty prints the given node using default styles
     *
     * @param node the node, usually a document, to be printed
     * @return the resulting formatted string
     */
    @NonNull
    public static String prettyPrint(@NonNull Node node) {
        return prettyPrint(node, XmlFormatPreferences.defaults(), XmlFormatStyle.get(node),
                SdkUtils.getLineSeparator());
    }

    /**
     * Start pretty-printing at the given node, which must either be the
     * startNode or contain it as a descendant.
     *
     * @param rootDepth the depth of the given node, used to determine indentation
     * @param root the node to start pretty printing from (which may not itself be
     *            included in the start to end node range but should contain it)
     * @param startNode the node to start formatting at
     * @param endNode the node to end formatting at
     * @param out the {@link StringBuilder} to pretty print into
     * @param openTagOnly if true, only format the open tag of the startNode (and nothing
     *     else)
     */
    public void prettyPrint(int rootDepth, Node root, Node startNode, Node endNode,
            StringBuilder out, boolean openTagOnly) {
        if (startNode == null) {
            startNode = root;
        }
        if (endNode == null) {
            endNode = root;
        }
        assert !openTagOnly || startNode == endNode;

        mStartNode = startNode;
        mOpenTagOnly = openTagOnly;
        mEndNode = endNode;
        mOut = out;
        mInRange = false;
        mIndentString = mPrefs.getOneIndentUnit();

        visitNode(rootDepth, root);
    }

    /** Visit the given node at the given depth */
    private void visitNode(int depth, Node node) {
        if (node == mStartNode) {
            mInRange = true;
        }

        if (mInRange) {
            visitBeforeChildren(depth, node);
            if (mOpenTagOnly && mStartNode == node) {
                mInRange = false;
                return;
            }
        }

        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            Node child = children.item(i);
            visitNode(depth + 1, child);
        }

        if (mInRange) {
            visitAfterChildren(depth, node);
        }

        if (node == mEndNode) {
            mInRange = false;
        }
    }

    private void visitBeforeChildren(int depth, Node node) {
        short type = node.getNodeType();
        switch (type) {
            case Node.DOCUMENT_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE:
                // Nothing to do
                break;

            case Node.ATTRIBUTE_NODE:
                // Handled as part of processing elements
                break;

            case Node.ELEMENT_NODE: {
                printOpenElementTag(depth, node);
                break;
            }

            case Node.TEXT_NODE: {
                printText(node);
                break;
            }

            case Node.CDATA_SECTION_NODE:
                printCharacterData(depth, node);
                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                printProcessingInstruction(node);
                break;

            case Node.COMMENT_NODE: {
                printComment(depth, node);
                break;
            }

            case Node.DOCUMENT_TYPE_NODE:
                printDocType(node);
                break;

            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
                break;
            default:
                assert false : type;
        }
    }

    private void visitAfterChildren(int depth, Node node) {
        short type = node.getNodeType();
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                // Handled as part of processing elements
                break;
            case Node.ELEMENT_NODE: {
                printCloseElementTag(depth, node);
                break;
            }
        }
    }

    private void printProcessingInstruction(Node node) {
        mOut.append("<?xml "); //$NON-NLS-1$
        mOut.append(node.getNodeValue().trim());
        mOut.append('?').append('>').append(mLineSeparator);
    }

    @Nullable
    protected String getSource(@NonNull Node node) {
        return null;
    }

    private void printDocType(Node node) {
        String content = getSource(node);
        if (content != null) {
            mOut.append(content);
            mOut.append(mLineSeparator);
        }
    }

    private void printCharacterData(int depth, Node node) {
        String nodeValue = node.getNodeValue();
        boolean separateLine = nodeValue.indexOf('\n') != -1;
        if (separateLine && !endsWithLineSeparator()) {
            mOut.append(mLineSeparator);
        }
        mOut.append("<![CDATA["); //$NON-NLS-1$
        mOut.append(nodeValue);
        mOut.append("]]>");       //$NON-NLS-1$
        if (separateLine) {
            mOut.append(mLineSeparator);
        }
    }

    private void printText(Node node) {
        boolean escape = true;
        String text = node.getNodeValue();

        String source = getSource(node);
        if (source != null) {
            // Get the original source string. This will contain the actual entities
            // such as "&gt;" instead of ">" which it gets turned into for the DOM nodes.
            // By operating on source we can preserve the user's entities rather than
            // having &gt; for example always turned into >.
            text = source;
            escape = false;
        }

        // Most text nodes are just whitespace for formatting (which we're replacing)
        // so look for actual text content and extract that part out
        String trimmed = text.trim();
        if (trimmed.length() > 0) {
            // TODO: Reformat the contents if it is too wide?

            // Note that we append the actual text content, NOT the trimmed content,
            // since the whitespace may be significant, e.g.
            // <string name="toast_sync_error">Sync error: <xliff:g id="error">%1$s</xliff:g>...

            // However, we should remove all blank lines in the prefix and suffix of the
            // text node, or we will end up inserting additional blank lines each time you're
            // formatting a text node within an outer element (which also adds spacing lines)
            int lastPrefixNewline = -1;
            for (int i = 0, n = text.length(); i < n; i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    lastPrefixNewline = i;
                } else if (!Character.isWhitespace(c)) {
                    break;
                }
            }
            int firstSuffixNewline = -1;
            for (int i = text.length() - 1; i >= 0; i--) {
                char c = text.charAt(i);
                if (c == '\n') {
                    firstSuffixNewline = i;
                } else if (!Character.isWhitespace(c)) {
                    break;
                }
            }
            if (lastPrefixNewline != -1 || firstSuffixNewline != -1) {
                if (firstSuffixNewline == -1) {
                    firstSuffixNewline = text.length();
                }
                text = text.substring(lastPrefixNewline + 1, firstSuffixNewline);
            }

            if (escape) {
                XmlUtils.appendXmlTextValue(mOut, text);
            } else {
                // Text is already escaped
                mOut.append(text);
            }

            if (mStyle != XmlFormatStyle.RESOURCE) {
                mOut.append(mLineSeparator);
            }
        }
    }

    private void printComment(int depth, Node node) {
        String comment = node.getNodeValue();
        boolean multiLine = comment.indexOf('\n') != -1;
        String trimmed = comment.trim();

        // See if this is an "end-of-the-line" comment, e.g. it is not a multi-line
        // comment and it appears on the same line as an opening or closing element tag;
        // if so, continue to place it as a suffix comment
        boolean isSuffixComment = false;
        if (!multiLine) {
            Node previous = node.getPreviousSibling();
            isSuffixComment = true;
            if (previous == null && node.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
                isSuffixComment = false;
            }
            while (previous != null) {
                short type = previous.getNodeType();
                if (type == Node.COMMENT_NODE) {
                    isSuffixComment = false;
                    break;
                } else if (type == Node.TEXT_NODE) {
                    if (previous.getNodeValue().indexOf('\n') != -1) {
                        isSuffixComment = false;
                        break;
                    }
                } else {
                    break;
                }
                previous = previous.getPreviousSibling();
            }
            if (isSuffixComment) {
                // Remove newline added by element open tag or element close tag
                if (endsWithLineSeparator()) {
                    removeLastLineSeparator();
                }
                mOut.append(' ');
            }
        }

        // Put the comment on a line on its own? Only if it was separated by a blank line
        // in the previous version of the document. In other words, if the document
        // adds blank lines between comments this formatter will preserve that fact, and vice
        // versa for a tightly formatted document it will preserve that convention as well.
        if (!mPrefs.removeEmptyLines && !isSuffixComment) {
            Node curr = node.getPreviousSibling();
            if (curr == null) {
                if (mOut.length() > 0 && !endsWithLineSeparator()) {
                    mOut.append(mLineSeparator);
                }
            } else if (curr.getNodeType() == Node.TEXT_NODE) {
                String text = curr.getNodeValue();
                // Count how many newlines we find in the trailing whitespace of the
                // text node
                int newLines = 0;
                for (int i = text.length() - 1; i >= 0; i--) {
                    char c = text.charAt(i);
                    if (Character.isWhitespace(c)) {
                        if (c == '\n') {
                            newLines++;
                            if (newLines == 2) {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                if (newLines >= 2) {
                    mOut.append(mLineSeparator);
                } else if (text.trim().length() == 0 && curr.getPreviousSibling() == null) {
                    // Comment before first child in node
                    mOut.append(mLineSeparator);
                }
            }
        }


        // TODO: Reformat the comment text?
        if (!multiLine) {
            if (!isSuffixComment) {
                indent(depth);
            }
            mOut.append(XML_COMMENT_BEGIN).append(' ');
            mOut.append(trimmed);
            mOut.append(' ').append(XML_COMMENT_END);
            mOut.append(mLineSeparator);
        } else {
            // Strip off blank lines at the beginning and end of the comment text.
            // Find last newline at the beginning of the text:
            int index = 0;
            int end = comment.length();
            int recentNewline = -1;
            while (index < end) {
                char c = comment.charAt(index);
                if (c == '\n') {
                    recentNewline = index;
                }
                if (!Character.isWhitespace(c)) {
                    break;
                }
                index++;
            }

            int start = recentNewline + 1;

            // Find last newline at the end of the text
            index = end - 1;
            recentNewline = -1;
            while (index > start) {
                char c = comment.charAt(index);
                if (c == '\n') {
                    recentNewline = index;
                }
                if (!Character.isWhitespace(c)) {
                    break;
                }
                index--;
            }

            end = recentNewline == -1 ? index + 1 : recentNewline;
            if (start >= end) {
                // It's a blank comment like <!-- \n\n--> - just clean it up
                if (!isSuffixComment) {
                    indent(depth);
                }
                mOut.append(XML_COMMENT_BEGIN).append(' ').append(XML_COMMENT_END);
                mOut.append(mLineSeparator);
                return;
            }

            trimmed = comment.substring(start, end);

            // When stripping out prefix and suffix blank lines we might have ended up
            // with a single line comment again so check and format single line comments
            // without newlines inside the <!-- --> delimiters
            multiLine = trimmed.indexOf('\n') != -1;
            if (multiLine) {
                indent(depth);
                mOut.append(XML_COMMENT_BEGIN);
                mOut.append(mLineSeparator);

                // See if we need to add extra spacing to keep alignment. Consider a comment
                // like this:
                // <!-- Deprecated strings - Move the identifiers to this section,
                //      and remove the actual text. -->
                // This String will be
                // " Deprecated strings - Move the identifiers to this section,\n" +
                // "     and remove the actual text. -->"
                // where the left side column no longer lines up.
                // To fix this, we need to insert some extra whitespace into the first line
                // of the string; in particular, the exact number of characters that the
                // first line of the comment was indented with!

                // However, if the comment started like this:
                // <!--
                // /** Copyright
                // -->
                // then obviously the align-indent is 0, so we only want to compute an
                // align indent when we don't find a newline before the content
                boolean startsWithNewline = false;
                for (int i = 0; i < start; i++) {
                    if (comment.charAt(i) == '\n') {
                        startsWithNewline = true;
                        break;
                    }
                }
                if (!startsWithNewline) {
                    Node previous = node.getPreviousSibling();
                    if (previous != null && previous.getNodeType() == Node.TEXT_NODE) {
                        String prevText = previous.getNodeValue();
                        int indentation = XML_COMMENT_BEGIN.length();
                        for (int i = prevText.length() - 1; i >= 0; i--) {
                            char c = prevText.charAt(i);
                            if (c == '\n') {
                                break;
                            } else {
                                indentation += (c == '\t') ? mPrefs.getTabWidth() : 1;
                            }
                        }

                        // See if the next line after the newline has indentation; if it doesn't,
                        // leave things alone. This fixes a case like this:
                        //     <!-- This is the
                        //     comment block -->
                        // such that it doesn't turn it into
                        //     <!--
                        //          This is the
                        //     comment block
                        //     -->
                        // In this case we instead want
                        //     <!--
                        //     This is the
                        //     comment block
                        //     -->
                        int minIndent = Integer.MAX_VALUE;
                        String[] lines = trimmed.split("\n"); //$NON-NLS-1$
                        // Skip line 0 since we know that it doesn't start with a newline
                        for (int i = 1; i < lines.length; i++) {
                            int indent = 0;
                            String line = lines[i];
                            for (int j = 0; j < line.length(); j++) {
                                char c = line.charAt(j);
                                if (!Character.isWhitespace(c)) {
                                    // Only set minIndent if there's text content on the line;
                                    // blank lines can exist in the comment without affecting
                                    // the overall minimum indentation boundary.
                                    if (indent < minIndent) {
                                        minIndent = indent;
                                    }
                                    break;
                                } else {
                                    indent += (c == '\t') ? mPrefs.getTabWidth() : 1;
                                }
                            }
                        }

                        if (minIndent < indentation) {
                            indentation = minIndent;

                            // Subtract any indentation that is already present on the line
                            String line = lines[0];
                            for (int j = 0; j < line.length(); j++) {
                                char c = line.charAt(j);
                                if (!Character.isWhitespace(c)) {
                                    break;
                                } else {
                                    indentation -= (c == '\t') ? mPrefs.getTabWidth() : 1;
                                }
                            }
                        }

                        for (int i = 0; i < indentation; i++) {
                            mOut.append(' ');
                        }

                        if (indentation < 0) {
                            boolean prefixIsSpace = true;
                            for (int i = 0; i < -indentation && i < trimmed.length(); i++) {
                                if (!Character.isWhitespace(trimmed.charAt(i))) {
                                    prefixIsSpace = false;
                                    break;
                                }
                            }
                            if (prefixIsSpace) {
                                trimmed = trimmed.substring(-indentation);
                            }
                        }
                    }
                }

                mOut.append(trimmed);
                mOut.append(mLineSeparator);
                indent(depth);
                mOut.append(XML_COMMENT_END);
                mOut.append(mLineSeparator);
            } else {
                mOut.append(XML_COMMENT_BEGIN).append(' ');
                mOut.append(trimmed);
                mOut.append(' ').append(XML_COMMENT_END);
                mOut.append(mLineSeparator);
            }
        }

        // Preserve whitespace after comment: See if the original document had two or
        // more newlines after the comment, and if so have a blank line between this
        // comment and the next
        Node next = node.getNextSibling();
        if (!mPrefs.removeEmptyLines && (next != null)
                && (next.getNodeType() == Node.TEXT_NODE)) {
            String text = next.getNodeValue();
            int newLinesBeforeText = 0;
            for (int i = 0, n = text.length(); i < n; i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    newLinesBeforeText++;
                    if (newLinesBeforeText == 2) {
                        // Yes
                        mOut.append(mLineSeparator);
                        break;
                    }
                } else if (!Character.isWhitespace(c)) {
                    break;
                }
            }
        }
    }

    private boolean endsWithLineSeparator() {
        int separatorLength = mLineSeparator.length();
        if (mOut.length() >= separatorLength) {
            for (int i = 0, j = mOut.length() - separatorLength; i < separatorLength; i++) {
                if (mOut.charAt(j) != mLineSeparator.charAt(i)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void removeLastLineSeparator() {
        int newLength = mOut.length() - mLineSeparator.length();
        if (newLength > 0) {
            mOut.setLength(newLength);
        }
    }

    private void printOpenElementTag(int depth, Node node) {
        Element element = (Element) node;
        if (newlineBeforeElementOpen(element, depth)) {
            mOut.append(mLineSeparator);
        }
        if (indentBeforeElementOpen(element, depth)) {
            indent(depth);
        }
        mOut.append('<').append(element.getTagName());

        NamedNodeMap attributes = element.getAttributes();
        int attributeCount = attributes.getLength();
        if (attributeCount > 0) {
            // Sort the attributes
            List<Attr> attributeList = new ArrayList<Attr>();
            for (int i = 0, n = attributeCount; i < n; i++) {
                attributeList.add((Attr) attributes.item(i));
            }
            Comparator<Attr> comparator = mPrefs.getAttributeComparator();
            if (comparator != null) {
                Collections.sort(attributeList, comparator);
            }

            // Put the single attribute on the same line as the element tag?
            boolean singleLine = mPrefs.oneAttributeOnFirstLine && attributeCount == 1
                    // In resource files we always put all the attributes (which is
                    // usually just zero, one or two) on the same line
                    || mStyle == XmlFormatStyle.RESOURCE;

            // We also place the namespace declaration on the same line as the root element,
            // but this doesn't also imply singleLine handling; subsequent attributes end up
            // on their own lines
            boolean indentNextAttribute;
            if (singleLine || (depth == 0 && XMLNS.equals(attributeList.get(0).getPrefix()))) {
                mOut.append(' ');
                indentNextAttribute = false;
            } else {
                mOut.append(mLineSeparator);
                indentNextAttribute = true;
            }

            Attr last = attributeList.get(attributeCount - 1);
            for (Attr attribute : attributeList) {
                if (indentNextAttribute) {
                    indent(depth + 1);
                }
                mOut.append(attribute.getName());
                mOut.append('=').append('"');
                XmlUtils.appendXmlAttributeValue(mOut, attribute.getValue());
                mOut.append('"');

                // Don't add a newline at the last attribute line; the > should
                // immediately follow the last attribute
                if (attribute != last) {
                    mOut.append(singleLine ? " " : mLineSeparator); //$NON-NLS-1$
                    indentNextAttribute = !singleLine;
                }
            }
        }

        boolean isClosed = isEmptyTag(element);

        // Add a space before the > or /> ? In resource files, only do this when closing the
        // element
        if (mPrefs.spaceBeforeClose && (mStyle != XmlFormatStyle.RESOURCE || isClosed)
                // in <selector> files etc still treat the <item> entries as in resource files
                && !TAG_ITEM.equals(element.getTagName())
                && (isClosed || element.getAttributes().getLength() > 0)) {
            mOut.append(' ');
        }

        if (isClosed) {
            mOut.append('/');
        }

        mOut.append('>');

        if (newlineAfterElementOpen(element, depth, isClosed)) {
            mOut.append(mLineSeparator);
        }
    }

    private void printCloseElementTag(int depth, Node node) {
        Element element = (Element) node;
        if (isEmptyTag(element)) {
            // Empty tag: Already handled as part of opening tag
            return;
        }

        // Put the closing declaration on its own line - unless it's a compact
        // resource file format
        // If the element had element children, separate the end tag from them
        if (newlineBeforeElementClose(element, depth)) {
            mOut.append(mLineSeparator);
        }
        if (indentBeforeElementClose(element, depth)) {
            indent(depth);
        }
        mOut.append('<').append('/');
        mOut.append(node.getNodeName());
        mOut.append('>');

        if (newlineAfterElementClose(element, depth)) {
            mOut.append(mLineSeparator);
        }
    }

    private boolean newlineBeforeElementOpen(Element element, int depth) {
        if (hasBlankLineAbove()) {
            return false;
        }

        if (mPrefs.removeEmptyLines || depth <= 0) {
            return false;
        }

        if (isMarkupElement(element)) {
            return false;
        }

        // See if this element should be separated from the previous element.
        // This is the case if we are not compressing whitespace (checked above),
        // or if we are not immediately following a comment (in which case the
        // newline would have been added above it), or if we are not in a formatting
        // style where
        if (mStyle == XmlFormatStyle.LAYOUT) {
            // In layouts we always separate elements
            return true;
        }

        if (mStyle == XmlFormatStyle.MANIFEST || mStyle == XmlFormatStyle.RESOURCE
                || mStyle == XmlFormatStyle.FILE) {
            Node curr = element.getPreviousSibling();

            // <style> elements are traditionally separated unless it follows a comment
            if (TAG_STYLE.equals(element.getTagName())) {
                if (curr == null
                        || curr.getNodeType() == Node.ELEMENT_NODE
                        || (curr.getNodeType() == Node.TEXT_NODE
                        && curr.getNodeValue().trim().length() == 0
                        && (curr.getPreviousSibling() == null
                        || curr.getPreviousSibling().getNodeType()
                        == Node.ELEMENT_NODE))) {
                    return true;
                }
            }

            // In all other styles, we separate elements if they have a different tag than
            // the previous one (but we don't insert a newline inside tags)
            while (curr != null) {
                short nodeType = curr.getNodeType();
                if (nodeType == Node.ELEMENT_NODE) {
                    Element sibling = (Element) curr;
                    if (!element.getTagName().equals(sibling.getTagName())) {
                        return true;
                    }
                    break;
                } else if (nodeType == Node.TEXT_NODE) {
                    String text = curr.getNodeValue();
                    if (text.trim().length() > 0) {
                        break;
                    }
                    // If there is just whitespace, continue looking for a previous sibling
                } else {
                    // Any other previous node type, such as a comment, means we don't
                    // continue looking: this element should not be separated
                    break;
                }
                curr = curr.getPreviousSibling();
            }
            if (curr == null && depth <= 1) {
                // Insert new line inside tag if it's the first element inside the root tag
                return true;
            }

            return false;
        }

        return false;
    }

    private boolean indentBeforeElementOpen(Element element, int depth) {
        if (isMarkupElement(element)) {
            return false;
        }

        if (element.getParentNode().getNodeType() == Node.ELEMENT_NODE
                && keepElementAsSingleLine(depth - 1, (Element) element.getParentNode())) {
            return false;
        }

        return true;
    }

    private boolean indentBeforeElementClose(Element element, int depth) {
        if (isMarkupElement(element)) {
            return false;
        }

        char lastOutChar = mOut.charAt(mOut.length() - 1);
        char lastDelimiterChar = mLineSeparator.charAt(mLineSeparator.length() - 1);
        return lastOutChar == lastDelimiterChar;
    }

    private boolean newlineAfterElementOpen(Element element, int depth, boolean isClosed) {
        if (hasBlankLineAbove()) {
            return false;
        }

        if (isMarkupElement(element)) {
            return false;
        }

        // In resource files we keep the child content directly on the same
        // line as the element (unless it has children). in other files, separate them
        return isClosed || !keepElementAsSingleLine(depth, element);
    }

    private boolean newlineBeforeElementClose(Element element, int depth) {
        if (hasBlankLineAbove()) {
            return false;
        }

        if (isMarkupElement(element)) {
            return false;
        }

        return depth == 0 && !mPrefs.removeEmptyLines;
    }

    private boolean hasBlankLineAbove() {
        if (mOut.length() < 2 * mLineSeparator.length()) {
            return false;
        }

        return SdkUtils.endsWith(mOut, mLineSeparator) &&
                SdkUtils.endsWith(mOut, mOut.length() - mLineSeparator.length(), mLineSeparator);
    }

    private boolean newlineAfterElementClose(Element element, int depth) {
        if (hasBlankLineAbove()) {
            return false;
        }

        if (isMarkupElement(element)) {
            return false;
        }

        return element.getParentNode().getNodeType() == Node.ELEMENT_NODE
                && !keepElementAsSingleLine(depth - 1, (Element) element.getParentNode());
    }

    private boolean isMarkupElement(Element element) {
        // The documentation suggests that the allowed tags are <u>, <b> and <i>:
        //   developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling
        // However, the full set of tags accepted by Html.fromHtml is much larger. Therefore,
        // instead consider *any* element nested inside a <string> definition to be a markup
        // element. See frameworks/base/core/java/android/text/Html.java and look for
        // HtmlToSpannedConverter#handleStartTag.

        if (mStyle != XmlFormatStyle.RESOURCE) {
            return false;
        }

        Node curr = element.getParentNode();
        while (curr != null) {
            if (TAG_STRING.equals(curr.getNodeName())) {
                return true;
            }

            curr = curr.getParentNode();
        }

        return false;
    }

    /**
     * TODO: Explain why we need to do per-tag decisions on whether to keep them on the
     * same line or not. Show that we can't just do it by depth, or by file type.
     * (style versus plurals example)
     * @param element the element whose tag we want to check
     * @return true if the element is a single line tag
     */
    private boolean isSingleLineTag(Element element) {
        String tag = element.getTagName();

        return (tag.equals(TAG_ITEM) && mStyle == XmlFormatStyle.RESOURCE)
                || tag.equals(TAG_STRING)
                || tag.equals(TAG_DIMEN)
                || tag.equals(TAG_COLOR);
    }

    private boolean keepElementAsSingleLine(int depth, Element element) {
        if (depth == 0) {
            return false;
        }

        return isSingleLineTag(element)
                || (mStyle == XmlFormatStyle.RESOURCE
                && !XmlUtils.hasElementChildren(element));
    }

    private void indent(int depth) {
        int i = 0;

        if (mIndentationLevels != null) {
            for (int j = Math.min(depth, mIndentationLevels.length - 1); j >= 0; j--) {
                String indent = mIndentationLevels[j];
                if (indent != null) {
                    mOut.append(indent);
                    i = j;
                    break;
                }
            }
        }

        for (; i < depth; i++) {
            mOut.append(mIndentString);
        }
    }

    /**
     * Returns true if the given element should be an empty tag
     *
     * @param element the element to test
     * @return true if this element should be an empty tag
     */
    protected boolean isEmptyTag(Element element) {
        if (element.getFirstChild() != null) {
            return false;
        }

        String tag = element.getTagName();
        if (TAG_STRING.equals(tag)) {
            return false;
        }

        return true;
    }
}








//Synthetic comment -- diff --git a/sdk_common/tests/src/com/android/ide/common/xml/XmlPrettyPrinterTest.java b/sdk_common/tests/src/com/android/ide/common/xml/XmlPrettyPrinterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..39cd902

//Synthetic comment -- @@ -0,0 +1,1132 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.xml;

import com.android.annotations.Nullable;
import com.android.utils.XmlUtils;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@SuppressWarnings("javadoc")
public class XmlPrettyPrinterTest extends TestCase {
    private void checkFormat(XmlFormatPreferences prefs,
            String xml,
            String expected, String delimiter, String startNodeName,
            String endNodeName) throws Exception {

        Document document = XmlUtils.parseDocumentSilently(xml, true);
        assertNotNull(document);
        XmlFormatStyle style = XmlFormatStyle.get(document);

        XmlPrettyPrinter printer = new XmlPrettyPrinter(prefs, style, delimiter);

        StringBuilder sb = new StringBuilder(1000);
        Node startNode = document;
        Node endNode = document;
        if (startNodeName != null) {
            startNode = findNode(document.getDocumentElement(), startNodeName);
        }
        if (endNodeName != null) {
            endNode = findNode(document.getDocumentElement(), endNodeName);
        }

        printer.prettyPrint(-1, document, startNode, endNode, sb, false/*openTagOnly*/);
        String formatted = sb.toString();
        if (!expected.equals(formatted)) {
            System.out.println(formatted);
        }
        assertEquals(expected, formatted);
    }

    private Node findNode(Node node, String nodeName) {
        if (node.getNodeName().equals(nodeName)) {
            return node;
        }

        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            Node child = children.item(i);
            Node result = findNode(child, nodeName);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    protected int getCaretOffset(String fileContent, String caretLocation) {
        int caretDelta = caretLocation.indexOf("^"); //$NON-NLS-1$
        assertTrue(caretLocation, caretDelta != -1);

        String caretContext = caretLocation.substring(0, caretDelta)
                + caretLocation.substring(caretDelta + 1); // +1: skip "^"
        int caretContextIndex = fileContent.indexOf(caretContext);
        assertTrue("Caret content " + caretContext + " not found in file",
                caretContextIndex != -1);
        return caretContextIndex + caretDelta;
    }

    private void checkFormat(XmlFormatPreferences prefs, String xml,
            String expected, String delimiter) throws Exception {
        checkFormat(prefs, xml, expected, delimiter, null, null);
    }

    private void checkFormat(XmlFormatPreferences prefs, String xml,
            String expected) throws Exception {
        checkFormat(prefs, xml, expected, "\n"); //$NON-NLS-1$
    }
    private void checkFormat(String xml, String expected)
            throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        checkFormat(prefs, xml, expected);
    }

    public void testLayout1() throws Exception {
        checkFormat(
                "<LinearLayout><Button></Button></LinearLayout>",

                "<LinearLayout>\n" +
                "\n" +
                "    <Button />\n" +
                "\n" +
                "</LinearLayout>");
    }

    public void testLayout2() throws Exception {
        checkFormat(
                "<LinearLayout><Button foo=\"bar\"></Button></LinearLayout>",

                "<LinearLayout>\n" +
                "\n" +
                "    <Button foo=\"bar\" />\n" +
                "\n" +
                "</LinearLayout>");
    }

    public void testLayout3() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.oneAttributeOnFirstLine = true;
        checkFormat(
                prefs,
                "<LinearLayout><Button foo=\"bar\"></Button></LinearLayout>",

                "<LinearLayout>\n" +
                "\n" +
                "    <Button foo=\"bar\" />\n" +
                "\n" +
                "</LinearLayout>");
    }

    public void testClosedElements() throws Exception {
        checkFormat(
                "<resources>\n" +
                "<item   name=\"title_container\"  type=\"id\"   />\n" +
                "<item name=\"title_logo\" type=\"id\"/>\n" +
                "</resources>\n",

                "<resources>\n" +
                "\n" +
                "    <item name=\"title_container\" type=\"id\"/>\n" +
                "    <item name=\"title_logo\" type=\"id\"/>\n" +
                "\n" +
                "</resources>");
    }

    public void testResources() throws Exception {
        checkFormat(
                "<resources><item name=\"foo\">Text value here </item></resources>",
                "<resources>\n\n" +
                "    <item name=\"foo\">Text value here </item>\n" +
                "\n</resources>");
    }

    public void testNodeTypes() throws Exception {
        // Ensures that a document with all kinds of node types is serialized correctly
        checkFormat(

                "<!--\n" +
                "/**\n" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                " */\n" +
                "-->\n" +
                "<!DOCTYPE metadata [\n" +
                "<!ELEMENT metadata (category)*>\n" +
                "<!ENTITY % ISOLat2\n" +
                "         SYSTEM \"http://www.xml.com/iso/isolat2-xml.entities\" >\n" +
                "]>\n" +
                "<LinearLayout\n" +
                "    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:orientation=\"vertical\">\n" +
                "<![CDATA[\n" +
                "This is character data!\n" +
                "<!-- This is not a comment! -->\n" +
                "and <this is not an element>\n" +
                "]]>         \n" +
                "This is text: &lt; and &amp;\n" +
                "<!-- comment 1 \"test\"... -->\n" +
                "<!-- ... comment2 -->\n" +
                "%ISOLat2;        \n" +
                "<!-- \n" +
                "Type <key>less-than</key> (&#x3C;)\n" +
                "-->        \n" +
                "</LinearLayout>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!--\n" +
                "/**\n" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                " */\n" +
                "-->\n" +
                "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:orientation=\"vertical\" >\n" +
                "<![CDATA[\n" +
                "This is character data!\n" +
                "<!-- This is not a comment! -->\n" +
                "and <this is not an element>\n" +
                "]]>\n" +
                "This is text: &lt; and &amp;\n" +
                "    <!-- comment 1 \"test\"... -->\n" +
                "    <!-- ... comment2 -->\n" +
                "%ISOLat2;        \n" +
                "<!-- Type <key>less-than</key> (&#x3C;) -->\n" +
                "\n" +
                "</LinearLayout>");
    }

    public void testWindowsDelimiters() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                "<LinearLayout><Button foo=\"bar\"></Button></LinearLayout>",

                "<LinearLayout>\r\n" +
                "\r\n" +
                "    <Button foo=\"bar\" />\r\n" +
                "\r\n" +
                "</LinearLayout>",
                "\r\n");
    }

    public void testRemoveBlanklines() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;
        checkFormat(
                prefs,
                "<foo><bar><baz1></baz1><baz2></baz2></bar><bar2></bar2><bar3><baz12></baz12></bar3></foo>",

                ""
                + "<foo>\n"
                + "    <bar>\n"
                + "        <baz1 />\n"
                + "        <baz2 />\n"
                + "    </bar>\n"
                + "    <bar2 />\n"
                + "    <bar3>\n"
                + "        <baz12 />\n"
                + "    </bar3>\n"
                + "</foo>");
    }

    public void testRange() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                "<LinearLayout><Button foo=\"bar\"></Button><CheckBox/></LinearLayout>",
                "\n" +
                "    <Button foo=\"bar\" />\n" +
                "\n" +
                "    <CheckBox />\n",
                "\n",
                "Button", "CheckBox");
    }

    public void testOpenTagOnly() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                "<LinearLayout><Button foo=\"bar\"><Foo/></Button><CheckBox/></LinearLayout>",
                "\n" +
                "    <Button foo=\"bar\" >\n" +
                "\n" +
                "        <Foo />\n" +
                "    </Button>\n",
                "\n",

                "Button", "Button");
    }

    public void testRange2() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;
        checkFormat(
                prefs,
                "<foo><bar><baz1></baz1><baz2></baz2></bar><bar2></bar2><bar3><baz12></baz12></bar3></foo>",

                "        <baz1 />\n" +
                "        <baz2 />\n" +
                "    </bar>\n" +
                "    <bar2 />\n" +
                "    <bar3>\n" +
                "        <baz12 />\n",

                "\n",
                "baz1", "baz12");
    }

    public void testEOLcomments() throws Exception {
        checkFormat(
                "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <item android:state_pressed=\"true\"\n" +
                "     android:color=\"#ffff0000\"/> <!-- pressed -->\n" +
                "    <item android:state_focused=\"true\"\n" +
                "     android:color=\"#ff0000ff\"/> <!-- focused -->\n" +
                "    <item android:color=\"#ff000000\"/> <!-- default -->\n" +
                "</selector>",

                ""
                + "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n"
                + "\n"
                + "    <item\n"
                + "        android:state_pressed=\"true\"\n"
                + "        android:color=\"#ffff0000\"/> <!-- pressed -->\n"
                + "    <item\n"
                + "        android:state_focused=\"true\"\n"
                + "        android:color=\"#ff0000ff\"/> <!-- focused -->\n"
                + "    <item android:color=\"#ff000000\"/> <!-- default -->\n"
                + "\n"
                + "</selector>");
    }

    public void testFormatColorList() throws Exception {
        checkFormat(
                "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "<item android:state_activated=\"true\" android:color=\"#FFFFFF\"/>\n" +
                "<item android:color=\"#777777\" /> <!-- not selected -->\n" +
                "</selector>",

                ""
                + "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n"
                + "\n"
                + "    <item\n"
                + "        android:state_activated=\"true\"\n"
                + "        android:color=\"#FFFFFF\"/>\n"
                + "    <item android:color=\"#777777\"/> <!-- not selected -->\n"
                + "\n"
                + "</selector>");
    }

    public void testPreserveNewlineAfterComment() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources><dimen name=\"colorstrip_height\">6dip</dimen>\n" +
                "    <!-- comment1 --><dimen name=\"title_height\">45dip</dimen>\n" +
                "\n" +
                "    <!-- comment2: newline above --><dimen name=\"now_playing_height\">90dip</dimen>\n" +
                "    <dimen name=\"text_size_small\">14sp</dimen>\n" +
                "\n" +
                "\n" +
                "    <!-- comment3: newline above and below -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "    <dimen name=\"text_size_medium\">18sp</dimen><dimen name=\"text_size_large\">22sp</dimen>\n" +
                "</resources>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <dimen name=\"colorstrip_height\">6dip</dimen>\n" +
                "    <!-- comment1 -->\n" +
                "    <dimen name=\"title_height\">45dip</dimen>\n" +
                "\n" +
                "    <!-- comment2: newline above -->\n" +
                "    <dimen name=\"now_playing_height\">90dip</dimen>\n" +
                "    <dimen name=\"text_size_small\">14sp</dimen>\n" +
                "\n" +
                "    <!-- comment3: newline above and below -->\n" +
                "\n" +
                "    <dimen name=\"text_size_medium\">18sp</dimen>\n" +
                "    <dimen name=\"text_size_large\">22sp</dimen>\n" +
                "\n" +
                "</resources>");
    }

    public void testPlurals() throws Exception {
        checkFormat(
                "<resources xmlns:xliff=\"urn:oasis:names:tc:xliff:document:1.2\">\n" +
                "<string name=\"toast_sync_error\">Sync error: <xliff:g id=\"error\">%1$s</xliff:g></string>\n" +
                "<string name=\"session_subtitle\"><xliff:g id=\"time\">%1$s</xliff:g> in <xliff:g id=\"room\">%2$s</xliff:g></string>\n" +
                "<plurals name=\"now_playing_countdown\">\n" +
                "<item quantity=\"zero\"><xliff:g id=\"remaining_time\">%2$s</xliff:g></item>\n" +
                "<item quantity=\"one\"><xliff:g id=\"number_of_days\">%1$s</xliff:g> day, <xliff:g id=\"remaining_time\">%2$s</xliff:g></item>\n" +
                "<item quantity=\"other\"><xliff:g id=\"number_of_days\">%1$s</xliff:g> days, <xliff:g id=\"remaining_time\">%2$s</xliff:g></item>\n" +
                "</plurals>\n" +
                "</resources>",

                "<resources xmlns:xliff=\"urn:oasis:names:tc:xliff:document:1.2\">\n" +
                "\n" +
                "    <string name=\"toast_sync_error\">Sync error: <xliff:g id=\"error\">%1$s</xliff:g></string>\n" +
                "    <string name=\"session_subtitle\"><xliff:g id=\"time\">%1$s</xliff:g> in <xliff:g id=\"room\">%2$s</xliff:g></string>\n" +
                "\n" +
                "    <plurals name=\"now_playing_countdown\">\n" +
                "        <item quantity=\"zero\"><xliff:g id=\"remaining_time\">%2$s</xliff:g></item>\n" +
                "        <item quantity=\"one\"><xliff:g id=\"number_of_days\">%1$s</xliff:g> day, <xliff:g id=\"remaining_time\">%2$s</xliff:g></item>\n" +
                "        <item quantity=\"other\"><xliff:g id=\"number_of_days\">%1$s</xliff:g> days, <xliff:g id=\"remaining_time\">%2$s</xliff:g></item>\n" +
                "    </plurals>\n" +
                "\n" +
                "</resources>");
    }

    public void testMultiAttributeResource() throws Exception {
        checkFormat(
                "<resources><string name=\"debug_enable_debug_logging_label\" translatable=\"false\">Enable extra debug logging?</string></resources>",

                "<resources>\n" +
                "\n" +
                "    <string name=\"debug_enable_debug_logging_label\" translatable=\"false\">Enable extra debug logging?</string>\n" +
                "\n" +
                "</resources>");
    }

    public void testMultilineCommentAlignment() throws Exception {
        checkFormat(
                "<resources>" +
                "    <!-- Deprecated strings - Move the identifiers to this section, mark as DO NOT TRANSLATE,\n" +
                "         and remove the actual text.  These will be removed in a bulk operation. -->\n" +
                "    <!-- Do Not Translate.  Unused string. -->\n" +
                "    <string name=\"meeting_invitation\"></string>\n" +
                "</resources>",

                "<resources>\n" +
                "\n" +
                "    <!--\n" +
                "         Deprecated strings - Move the identifiers to this section, mark as DO NOT TRANSLATE,\n" +
                "         and remove the actual text.  These will be removed in a bulk operation.\n" +
                "    -->\n" +
                "    <!-- Do Not Translate.  Unused string. -->\n" +
                "    <string name=\"meeting_invitation\"></string>\n" +
                "\n" +
                "</resources>");
    }

    public void testLineCommentSpacing() throws Exception {
        checkFormat(
                "<resources>\n" +
                "\n" +
                "    <dimen name=\"colorstrip_height\">6dip</dimen>\n" +
                "    <!-- comment1 -->\n" +
                "    <dimen name=\"title_height\">45dip</dimen>\n" +
                "    <!-- comment2: no newlines -->\n" +
                "    <dimen name=\"now_playing_height\">90dip</dimen>\n" +
                "    <dimen name=\"text_size_small\">14sp</dimen>\n" +
                "\n" +
                "    <!-- comment3: newline above and below -->\n" +
                "\n" +
                "    <dimen name=\"text_size_medium\">18sp</dimen>\n" +
                "    <dimen name=\"text_size_large\">22sp</dimen>\n" +
                "\n" +
                "</resources>",

                "<resources>\n" +
                "\n" +
                "    <dimen name=\"colorstrip_height\">6dip</dimen>\n" +
                "    <!-- comment1 -->\n" +
                "    <dimen name=\"title_height\">45dip</dimen>\n" +
                "    <!-- comment2: no newlines -->\n" +
                "    <dimen name=\"now_playing_height\">90dip</dimen>\n" +
                "    <dimen name=\"text_size_small\">14sp</dimen>\n" +
                "\n" +
                "    <!-- comment3: newline above and below -->\n" +
                "\n" +
                "    <dimen name=\"text_size_medium\">18sp</dimen>\n" +
                "    <dimen name=\"text_size_large\">22sp</dimen>\n" +
                "\n" +
                "</resources>");
    }

    public void testCommentHandling() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                "<foo >\n" +
                "\n" +
                "    <!-- abc\n" +
                "         def\n" +
                "         ghi -->\n" +
                "\n" +
                "    <!-- abc\n" +
                "    def\n" +
                "    ghi -->\n" +
                "    \n" +
                "<!-- abc\n" +
                "def\n" +
                "ghi -->\n" +
                "\n" +
                "</foo>",

                "<foo>\n" +
                "\n" +
                "    <!--\n" +
                "         abc\n" +
                "         def\n" +
                "         ghi\n" +
                "    -->\n" +
                "\n" +
                "\n" +
                "    <!--\n" +
                "    abc\n" +
                "    def\n" +
                "    ghi\n" +
                "    -->\n" +
                "\n" +
                "\n" +
                "    <!--\n" +
                "abc\n" +
                "def\n" +
                "ghi\n" +
                "    -->\n" +
                "\n" +
                "</foo>");
    }

    public void testCommentHandling2() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                "<foo >\n" +
                "    <!-- multi -->\n" +
                "\n" +
                "    <bar />\n" +
                "</foo>",

                "<foo>\n" +
                "\n" +
                "    <!-- multi -->\n" +
                "\n" +
                "    <bar />\n" +
                "\n" +
                "</foo>");
    }

    public void testMenus1() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                // http://code.google.com/p/android/issues/detail?id=21383
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n" +
                "\n" +
                "    <item\n" +
                "        android:id=\"@+id/menu_debug\"\n" +
                "        android:icon=\"@android:drawable/ic_menu_more\"\n" +
                "        android:showAsAction=\"ifRoom|withText\"\n" +
                "        android:title=\"@string/menu_debug\">\n" +
                "    \n" +
                "        <menu>\n" +
                "                <item\n" +
                "                    android:id=\"@+id/menu_debug_clearCache_memory\"\n" +
                "                    android:icon=\"@android:drawable/ic_menu_delete\"\n" +
                "                    android:showAsAction=\"ifRoom|withText\"\n" +
                "                    android:title=\"@string/menu_debug_clearCache_memory\"/>\n" +
                "    \n" +
                "                <item\n" +
                "                    android:id=\"@+id/menu_debug_clearCache_file\"\n" +
                "                    android:icon=\"@android:drawable/ic_menu_delete\"\n" +
                "                    android:showAsAction=\"ifRoom|withText\"\n" +
                "                    android:title=\"@string/menu_debug_clearCache_file\"/>\n" +
                "        </menu>\n" +
                "    </item>\n" +
                "</menu>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n" +
                "\n" +
                "    <item\n" +
                "        android:id=\"@+id/menu_debug\"\n" +
                "        android:icon=\"@android:drawable/ic_menu_more\"\n" +
                "        android:showAsAction=\"ifRoom|withText\"\n" +
                "        android:title=\"@string/menu_debug\">\n" +
                "        <menu>\n" +
                "            <item\n" +
                "                android:id=\"@+id/menu_debug_clearCache_memory\"\n" +
                "                android:icon=\"@android:drawable/ic_menu_delete\"\n" +
                "                android:showAsAction=\"ifRoom|withText\"\n" +
                "                android:title=\"@string/menu_debug_clearCache_memory\"/>\n" +
                "            <item\n" +
                "                android:id=\"@+id/menu_debug_clearCache_file\"\n" +
                "                android:icon=\"@android:drawable/ic_menu_delete\"\n" +
                "                android:showAsAction=\"ifRoom|withText\"\n" +
                "                android:title=\"@string/menu_debug_clearCache_file\"/>\n" +
                "        </menu>\n" +
                "    </item>\n" +
                "\n" +
                "</menu>");
    }

    public void testMenus2() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;
        checkFormat(
                prefs,
                // http://code.google.com/p/android/issues/detail?id=21346
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<layer-list xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "  <item>\n" +
                "    <shape android:shape=\"rectangle\">\n" +
                "      <stroke\n" +
                "        android:width=\"1dip\"\n" +
                "        android:color=\"@color/line_separator\"/>\n" +
                "      <solid android:color=\"@color/event_header_background\"/>\n" +
                "    </shape>\n" +
                "  </item>\n" +
                "  <item\n" +
                "    android:bottom=\"1dip\"\n" +
                "    android:top=\"1dip\">\n" +
                "    <shape android:shape=\"rectangle\">\n" +
                "      <stroke\n" +
                "        android:width=\"1dip\"\n" +
                "        android:color=\"@color/event_header_background\"/>\n" +
                "      <solid android:color=\"@color/transparent\"/>\n" +
                "    </shape>\n" +
                "  </item>\n" +
                "</layer-list>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<layer-list xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n" +
                "    <item>\n" +
                "        <shape android:shape=\"rectangle\" >\n" +
                "            <stroke\n" +
                "                android:width=\"1dip\"\n" +
                "                android:color=\"@color/line_separator\" />\n" +
                "            <solid android:color=\"@color/event_header_background\" />\n" +
                "        </shape>\n" +
                "    </item>\n" +
                "    <item\n" +
                "        android:bottom=\"1dip\"\n" +
                "        android:top=\"1dip\">\n" +
                "        <shape android:shape=\"rectangle\" >\n" +
                "            <stroke\n" +
                "                android:width=\"1dip\"\n" +
                "                android:color=\"@color/event_header_background\" />\n" +
                "            <solid android:color=\"@color/transparent\" />\n" +
                "        </shape>\n" +
                "    </item>\n" +
                "</layer-list>");
    }

    public void testMenus3() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                // http://code.google.com/p/android/issues/detail?id=21227
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n" +
                "\n" +
                "    <item\n" +
                "        android:icon=\"@android:drawable/ic_menu_more\"\n" +
                "        android:title=\"@string/account_list_menu_more\">\n" +
                "        <menu>\n" +
                "            <item\n" +
                "                android:id=\"@+id/account_list_menu_backup_restore\"\n" +
                "                android:icon=\"@android:drawable/ic_menu_save\"\n" +
                "                android:title=\"@string/account_list_menu_backup_restore\"/>\n" +
                "        </menu>\n" +
                "    </item>\n" +
                "\n" +
                "</menu>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\" >\n" +
                "\n" +
                "    <item\n" +
                "        android:icon=\"@android:drawable/ic_menu_more\"\n" +
                "        android:title=\"@string/account_list_menu_more\">\n" +
                "        <menu>\n" +
                "            <item\n" +
                "                android:id=\"@+id/account_list_menu_backup_restore\"\n" +
                "                android:icon=\"@android:drawable/ic_menu_save\"\n" +
                "                android:title=\"@string/account_list_menu_backup_restore\"/>\n" +
                "        </menu>\n" +
                "    </item>\n" +
                "\n" +
                "</menu>");

    }

    public void testColors1() throws Exception {
        checkFormat(
                XmlFormatPreferences.defaults(),
                "<resources>\n" +
                "  <color name=\"enrollment_error\">#99e21f14</color>\n" +
                "\n" +
                "  <color name=\"service_starting_up\">#99000000</color>\n" +
                "</resources>",

                "<resources>\n" +
                "\n" +
                "    <color name=\"enrollment_error\">#99e21f14</color>\n" +
                "    <color name=\"service_starting_up\">#99000000</color>\n" +
                "\n" +
                "</resources>");
    }

    public void testEclipseFormatStyle1() throws Exception {
        XmlFormatPreferences prefs = new XmlFormatPreferences() {
            @Override
            public String getOneIndentUnit() {
                return "\t";
            }

            @Override
            public int getTabWidth() {
                return 8;
            }
        };
        checkFormat(
                prefs,
                "<resources>\n" +
                "  <color name=\"enrollment_error\">#99e21f14</color>\n" +
                "\n" +
                "  <color name=\"service_starting_up\">#99000000</color>\n" +
                "</resources>",

                "<resources>\n" +
                "\n" +
                "\t<color name=\"enrollment_error\">#99e21f14</color>\n" +
                "\t<color name=\"service_starting_up\">#99000000</color>\n" +
                "\n" +
                "</resources>");
    }

    public void testEclipseFormatStyle2() throws Exception {
        XmlFormatPreferences prefs = new XmlFormatPreferences() {
            @Override
            public String getOneIndentUnit() {
                return "  ";
            }

            @Override
            public int getTabWidth() {
                return 2;
            }
        };
        prefs.useEclipseIndent = true;
        checkFormat(
                prefs,
                "<resources>\n" +
                "  <color name=\"enrollment_error\">#99e21f14</color>\n" +
                "\n" +
                "  <color name=\"service_starting_up\">#99000000</color>\n" +
                "</resources>",

                "<resources>\n" +
                "\n" +
                "  <color name=\"enrollment_error\">#99e21f14</color>\n" +
                "  <color name=\"service_starting_up\">#99000000</color>\n" +
                "\n" +
                "</resources>");
    }

    public void testNameSorting() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <attr format=\"integer\" name=\"no\" />\n" +
                "</resources>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <attr name=\"no\" format=\"integer\" />\n" +
                "\n" +
                "</resources>");
    }

    public void testStableText() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:orientation=\"vertical\">\n" +
                "    Hello World\n" +
                "\n" +
                "</LinearLayout>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:orientation=\"vertical\" >\n" +
                "    Hello World\n" +
                "\n" +
                "</LinearLayout>");
    }

    public void testResources1() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "        <string name=\"test_string\">a\n" +
                "                </string>\n" +
                "\n" +
                "</resources>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <string name=\"test_string\">a</string>\n" +
                "\n" +
                "</resources>");
    }

    public void testMarkup() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "<string name=\"welcome\">Welcome to <b>Android</b>!</string>" +
                "<string name=\"glob_settings_top_text\"><b>To install a 24 Clock Widget, " +
                "please <i>long press</i> in Home Screen.</b> Configure the Global Settings " +
                "here.</string>" +
                "" +
                "\n" +
                "</resources>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <string name=\"welcome\">Welcome to <b>Android</b>!</string>\n" +
                "    <string name=\"glob_settings_top_text\"><b>To install a 24 Clock Widget, " +
                "please <i>long press</i> in Home Screen.</b> Configure the Global Settings " +
                "here.</string>\n" +
                "\n" +
                "</resources>");
    }

    /* This test fails when run on a plain DOM; when used with the Eclipse DOM for example
       where we can get access to the original DOM, as in EclipseXmlPrettyPrinter, it works
       public void testPreserveEntities() throws Exception {
        // Ensure that entities such as &gt; in the input string are preserved in the output
        // format
        checkFormat(
                "res/values/strings.xml",
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources><string name=\"untitled\">&lt;untitled2></string>\n" +
                "<string name=\"untitled2\">&lt;untitled2&gt;</string>\n" +
                "<string name=\"untitled3\">&apos;untitled3&quot;</string></resources>\n",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <string name=\"untitled\">&lt;untitled2></string>\n" +
                "    <string name=\"untitled2\">&lt;untitled2&gt;</string>\n" +
                "    <string name=\"untitled3\">&apos;untitled3&quot;</string>\n" +
                "\n" +
                "</resources>");
    }
    */

    public void testCData1() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <string name=\"foo\"><![CDATA[bar]]></string>\n" +
                "</resources>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <string name=\"foo\"><![CDATA[bar]]></string>\n" +
                "\n" +
                "</resources>");
    }

    public void testCData2() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <string name=\"foo1\"><![CDATA[bar1\n" +
                "bar2\n" +
                "bar3]]></string>\n" +
                "    <string name=\"foo2\"><![CDATA[bar]]></string>\n" +
                "</resources>",

                //"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\n" +
                "    <string name=\"foo1\">\n" +
                "<![CDATA[bar1\n" +
                "bar2\n" +
                "bar3]]>\n" +
                "    </string>\n" +
                "    <string name=\"foo2\"><![CDATA[bar]]></string>\n" +
                "\n" +
                "</resources>");
    }

    public void testComplexString() throws Exception {
        checkFormat(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "<string name=\"progress_completed_export_all\">The database has " +
                "<b>successfully</b> been exported into: <br /><br /><font size=\"14\">" +
                "\\\"<i>%s</i>\\\"</font></string>" +
                "</resources>",

                "<resources>\n" +
                "\n" +
                "    <string name=\"progress_completed_export_all\">The database has " +
                "<b>successfully</b> been exported into: <br /><br /><font size=\"14\">" +
                "\\\"<i>%s</i>\\\"</font></string>\n" +
                "\n" +
                "</resources>");
    }


    public void testToXml() throws Exception {
        Document doc = createEmptyPlainDocument();
        assertNotNull(doc);
        Element root = doc.createElement("myroot");
        doc.appendChild(root);
        root.setAttribute("foo", "bar");
        root.setAttribute("baz", "baz");
        Element child = doc.createElement("mychild");
        root.appendChild(child);
        Element child2 = doc.createElement("hasComment");
        root.appendChild(child2);
        Node comment = doc.createComment("This is my comment");
        child2.appendChild(comment);
        Element child3 = doc.createElement("hasText");
        root.appendChild(child3);
        Node text = doc.createTextNode("  This is my text  ");
        child3.appendChild(text);

        String xml = XmlUtils.toXml(doc, true);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<myroot baz=\"baz\" foo=\"bar\"><mychild/><hasComment><!--This is my comment--></hasComment><hasText>  This is my text  </hasText></myroot>",
                xml);

        xml = XmlPrettyPrinter.prettyPrint(doc);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<myroot\n"
                + "    baz=\"baz\"\n"
                + "    foo=\"bar\" >\n"
                + "\n"
                + "    <mychild />\n"
                + "\n"
                + "    <hasComment> <!-- This is my comment -->\n"
                + "    </hasComment>\n"
                + "\n"
                + "    <hasText>\n"
                + "  This is my text  \n"
                + "    </hasText>\n"
                + "\n"
                + "</myroot>",
                xml);
    }

    public void testToXml2() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "    <string \n"
                + "        name=\"description_search\">Search</string>\n"
                + "    <string \n"
                + "        name=\"description_map\">Map</string>\n"
                + "    <string\n"
                + "         name=\"description_refresh\">Refresh</string>\n"
                + "    <string \n"
                + "        name=\"description_share\">Share</string>\n"
                + "</resources>";

        Document doc = parse(xml);

        String formatted = XmlUtils.toXml(doc, true);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "    <string name=\"description_search\">Search</string>\n"
                + "    <string name=\"description_map\">Map</string>\n"
                + "    <string name=\"description_refresh\">Refresh</string>\n"
                + "    <string name=\"description_share\">Share</string>\n"
                + "</resources>",
                formatted);

        formatted = XmlPrettyPrinter.prettyPrint(doc);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "\n"
                + "    <string name=\"description_search\">Search</string>\n"
                + "    <string name=\"description_map\">Map</string>\n"
                + "    <string name=\"description_refresh\">Refresh</string>\n"
                + "    <string name=\"description_share\">Share</string>\n"
                + "\n"
                + "</resources>",
                formatted);
    }

    public void testToXml3() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<root>\n"
                + "    <!-- ============== -->\n"
                + "    <!-- Generic styles -->\n"
                + "    <!-- ============== -->\n"
                + "</root>";
        Document doc = parse(xml);

        String formatted = XmlUtils.toXml(doc, true);
        assertEquals(xml, formatted);

        xml = XmlPrettyPrinter.prettyPrint(doc);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<root>\n"
                + "\n"
                + "    <!-- ============== -->\n"
                + "    <!-- Generic styles -->\n"
                + "    <!-- ============== -->\n"
                + "\n"
                + "</root>",
                xml);
    }

    public void testToXml3b() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "  <!-- ============== -->\n"
                + "  <!-- Generic styles -->\n"
                + "         <!-- ============== -->\n"
                + " <string     name=\"test\">test</string>\n"
                + "</resources>";
        Document doc = parse(xml);

        String formatted = XmlUtils.toXml(doc, true);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "  <!-- ============== -->\n"
                + "  <!-- Generic styles -->\n"
                + "         <!-- ============== -->\n"
                + " <string name=\"test\">test</string>\n"
                + "</resources>",
                formatted);

        xml = XmlPrettyPrinter.prettyPrint(doc);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "\n"
                + "    <!-- ============== -->\n"
                + "    <!-- Generic styles -->\n"
                + "    <!-- ============== -->\n"
                + "    <string name=\"test\">test</string>\n"
                + "\n"
                + "</resources>",
                xml);
    }


    public void testToXml4() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!-- ============== -->\n"
                + "<!-- Generic styles -->\n"
                + "<!-- ============== -->\n"
                + "<root/>";
        Document doc = parse(xml);

        xml = XmlUtils.toXml(doc, true);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!-- ============== --><!-- Generic styles --><!-- ============== --><root/>",
                xml);

        xml = XmlPrettyPrinter.prettyPrint(doc);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!-- ============== -->\n"
                + "<!-- Generic styles -->\n"
                + "<!-- ============== -->\n"
                + "<root />\n",
                xml);
    }

    public void testToXml5() throws Exception {
        String xml = ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources><attr name=\"flag_attr\">"
                + "<flag name=\"normal\" value=\"0\"/>"
                + "<flag name=\"bold\" value=\"1\"/>"
                + "<flag name=\"italic\" "
                + "value=\"2\"/></attr></resources>";

        Document doc = parse(xml);
        xml = XmlPrettyPrinter.prettyPrint(doc);
        assertEquals(""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + "\n"
                + "    <attr name=\"flag_attr\">\n"
                + "        <flag name=\"normal\" value=\"0\" />\n"
                + "        <flag name=\"bold\" value=\"1\" />\n"
                + "        <flag name=\"italic\" value=\"2\" />\n"
                + "    </attr>\n"
                + "\n"
                + "</resources>",
                xml);
    }

    @Nullable
    private static Document createEmptyPlainDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    @Nullable
    private static Document parse(String xml) throws Exception {
        if (true) {
            return XmlUtils.parseDocumentSilently(xml, true);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setExpandEntityReferences(false);
        factory.setXIncludeAware(false);
        factory.setIgnoringComments(false);
        factory.setCoalescing(false);
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}







