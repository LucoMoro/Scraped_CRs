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

import static com.android.SdkConstants.AMP_ENTITY;
import static com.android.SdkConstants.ANDROID_NS_NAME;
import static com.android.SdkConstants.ANDROID_URI;
//Synthetic comment -- @@ -26,22 +44,13 @@
import static com.android.SdkConstants.XMLNS_PREFIX;
import static com.android.SdkConstants.XMLNS_URI;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.google.common.base.Splitter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;

/** XML Utilities */
public class XmlUtils {
/**
* Returns the namespace prefix matching the requested namespace URI.
* If no such declaration is found, returns the default "android" prefix for
//Synthetic comment -- @@ -280,86 +289,87 @@
}

/**
     * Dump an XML tree to string. This isn't going to do a beautiful job pretty
     * printing the XML; it's intended mostly for non-user editable files and
     * for debugging. If true, preserve whitespace exactly as in the DOM
     * document (typically used for a DOM which is already formatted), otherwise
     * this method will insert some newlines here and there (for example, one
     * per element and one per attribute.)
*
     * @param node the node (which can be a document, an element, a text node,
     *            etc.
     * @param preserveWhitespace whether to preserve the whitespace (text nodes)
     *            in the DOM
     * @return a string version of the file
*/
public static String toXml(Node node, boolean preserveWhitespace) {
StringBuilder sb = new StringBuilder(1000);

        append(sb, node, 0, preserveWhitespace);

return sb.toString();
}

    private static void indent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("    ");
        }
    }

private static void append(
@NonNull StringBuilder sb,
@NonNull Node node,
            int indent,
            boolean preserveWhitespace) {
short nodeType = node.getNodeType();
switch (nodeType) {
case Node.DOCUMENT_NODE:
case Node.DOCUMENT_FRAGMENT_NODE: {
                sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"); //$NON-NLS-1$
NodeList children = node.getChildNodes();
for (int i = 0, n = children.getLength(); i < n; i++) {
                    append(sb, children.item(i), indent, preserveWhitespace);
}
break;
}
case Node.COMMENT_NODE:
case Node.TEXT_NODE: {
if (nodeType == Node.COMMENT_NODE) {
                    if (!preserveWhitespace) {
                        indent(sb, indent);
                    }
                    sb.append("<!--"); //$NON-NLS-1$
                    if (!preserveWhitespace) {
                        sb.append('\n');
                    }
}
String text = node.getNodeValue();
                if (!preserveWhitespace) {
                    text = text.trim();
                    for (String line : Splitter.on('\n').split(text)) {
                        indent(sb, indent + 1);
                        sb.append(toXmlTextValue(line));
                        sb.append('\n');
                    }
                } else {
                    sb.append(toXmlTextValue(text));
                }
if (nodeType == Node.COMMENT_NODE) {
                    if (!preserveWhitespace) {
                        indent(sb, indent);
                    }
                    sb.append("-->"); //$NON-NLS-1$
                    if (!preserveWhitespace) {
                        sb.append('\n');
                    }
}
break;
}
case Node.ELEMENT_NODE: {
                if (!preserveWhitespace) {
                    indent(sb, indent);
                }
sb.append('<');
Element element = (Element) node;
sb.append(element.getTagName());
//Synthetic comment -- @@ -384,26 +394,14 @@
sb.append('/');
}
sb.append('>');
                if (!preserveWhitespace) {
                    sb.append('\n');
                }
if (childCount > 0) {
for (int i = 0; i < childCount; i++) {
Node child = children.item(i);
                        append(sb, child, indent + 1, preserveWhitespace);
                    }
                    if (!preserveWhitespace) {
                        if (sb.charAt(sb.length() - 1) != '\n') {
                            sb.append('\n');
                        }
                        indent(sb, indent);
}
sb.append('<').append('/');
sb.append(element.getTagName());
sb.append('>');
                    if (!preserveWhitespace) {
                        sb.append('\n');
                    }
}
break;
}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/XmlUtilsTest.java b/common/src/test/java/com/android/utils/XmlUtilsTest.java
//Synthetic comment -- index edf0235..02fd465 100644

//Synthetic comment -- @@ -15,21 +15,24 @@
*/
package com.android.utils;

import static com.android.SdkConstants.XMLNS;

import com.android.SdkConstants;
import com.android.annotations.Nullable;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class XmlUtilsTest extends TestCase {
//Synthetic comment -- @@ -116,8 +119,27 @@
assertEquals("&lt;\"'>&amp;", sb.toString());
}

    public void testNew() throws Exception {
Document doc = createEmptyPlainDocument();
Element root = doc.createElement("myroot");
doc.appendChild(root);
root.setAttribute("foo", "bar");
//Synthetic comment -- @@ -133,28 +155,93 @@
Node text = doc.createTextNode("  This is my text  ");
child3.appendChild(text);

        String xml = XmlUtils.toXml(doc, false);
assertEquals(
"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<myroot baz=\"baz\" foo=\"bar\">\n" +
                "    <mychild/>\n" +
                "    <hasComment>\n" +
                "        <!--\n" +
                "            This is my comment\n" +
                "        -->\n" +
                "    </hasComment>\n" +
                "    <hasText>\n" +
                "            This is my text\n" +
                "    </hasText>\n" +
                "</myroot>\n",
xml);

xml = XmlUtils.toXml(doc, true);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<myroot baz=\"baz\" foo=\"bar\"><mychild/><hasComment><!--This is my comment--></hasComment><hasText>  This is my text  </hasText></myroot>",
               xml);

}

@Nullable
//Synthetic comment -- @@ -167,4 +254,21 @@
builder = factory.newDocumentBuilder();
return builder.newDocument();
}
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlAttributeSortOrder.java b/sdk_common/src/com/android/ide/common/xml/XmlAttributeSortOrder.java
new file mode 100644
//Synthetic comment -- index 0000000..ea31f8b

//Synthetic comment -- @@ -0,0 +1,208 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlFormatPreferences.java b/sdk_common/src/com/android/ide/common/xml/XmlFormatPreferences.java
new file mode 100644
//Synthetic comment -- index 0000000..34317f7

//Synthetic comment -- @@ -0,0 +1,95 @@








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlFormatStyle.java b/sdk_common/src/com/android/ide/common/xml/XmlFormatStyle.java
new file mode 100644
//Synthetic comment -- index 0000000..b796fe4

//Synthetic comment -- @@ -0,0 +1,86 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/XmlPrettyPrinter.java b/sdk_common/src/com/android/ide/common/xml/XmlPrettyPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..37481b6

//Synthetic comment -- @@ -0,0 +1,1039 @@








//Synthetic comment -- diff --git a/sdk_common/tests/src/com/android/ide/common/xml/XmlPrettyPrinterTest.java b/sdk_common/tests/src/com/android/ide/common/xml/XmlPrettyPrinterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..39cd902

//Synthetic comment -- @@ -0,0 +1,1132 @@







