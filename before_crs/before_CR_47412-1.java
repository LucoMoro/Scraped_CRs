/*Add namespace elements from code completion lazily

The XmlUtils.lookupNamespace() method would unconditionally insert a
namespace declaration if called with a namespace that is not already
declared in the document.

This had the negative effect that simply bringing up the code
completion dialog on a custom view would immediately insert the app
namespace declaration, even if the user didn't select one of the app
namespace attributes. Furthermore, this edit would sometimes result in
the caret being placed in the wrong place after insertion.

This changeset adds a new parameter to the lookupNamespace() method,
"create", which lets the caller deliberately decide whether the new
namespace element should be created.

Second, code completion now only inserts the namespace declaration as
part of the completion apply handler, meaning you only get the
namespace if you pick one of the custom attributes. It also uses a
document position to ensure that the insert and caret positions are
preserved properly and take the namespace insertion into account.

Change-Id:I21cf678df454b09460139fe35d33ca88b8e91757*/
//Synthetic comment -- diff --git a/common/src/com/android/utils/XmlUtils.java b/common/src/com/android/utils/XmlUtils.java
//Synthetic comment -- index 94b7405..0969eb1 100644

//Synthetic comment -- @@ -42,7 +42,9 @@
/**
* Returns the namespace prefix matching the requested namespace URI.
* If no such declaration is found, returns the default "android" prefix for
     * the Android URI, and "app" for other URI's.
*
* @param node The current node. Must not be null.
* @param nsUri The namespace URI of which the prefix is to be found,
//Synthetic comment -- @@ -53,24 +55,47 @@
@NonNull
public static String lookupNamespacePrefix(@NonNull Node node, @NonNull String nsUri) {
String defaultPrefix = ANDROID_URI.equals(nsUri) ? ANDROID_NS_NAME : APP_PREFIX;
        return lookupNamespacePrefix(node, nsUri, defaultPrefix);
}

/**
     * Returns the namespace prefix matching the requested namespace URI.
     * If no such declaration is found, returns the default "android" prefix.
*
* @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found,
     *              e.g. {@link SdkConstants#ANDROID_URI}
     * @param defaultPrefix The default prefix (root) to use if the namespace
     *              is not found. If null, do not create a new namespace
     *              if this URI is not defined for the document.
     * @return The first prefix declared or the provided prefix (possibly with
     *              a number appended to avoid conflicts with existing prefixes.
*/
public static String lookupNamespacePrefix(
            @Nullable Node node, @Nullable String nsUri, @Nullable String defaultPrefix) {
// Note: Node.lookupPrefix is not implemented in wst/xml/core NodeImpl.java
// The following code emulates this simple call:
//   String prefix = node.lookupPrefix(NS_RESOURCES);
//Synthetic comment -- @@ -140,7 +165,7 @@
while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
node = node.getNextSibling();
}
            if (node != null) {
// This doesn't work:
//Attr attr = doc.createAttributeNS(XMLNS_URI, prefix);
//attr.setPrefix(XMLNS);








//Synthetic comment -- diff --git a/common/tests/src/com/android/utils/XmlUtilsTest.java b/common/tests/src/com/android/utils/XmlUtilsTest.java
//Synthetic comment -- index ea33346..0e9289b 100644

//Synthetic comment -- @@ -15,11 +15,14 @@
*/
package com.android.utils;

import com.android.SdkConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -55,8 +58,31 @@
assertEquals("customPrefix", prefix);

prefix = XmlUtils.lookupNamespacePrefix(baz,
                "http://schemas.android.com/tools", "tools");
assertEquals("tools", prefix);
}

public void testToXmlAttributeValue() throws Exception {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index c98e94f..223e5e5 100644

//Synthetic comment -- @@ -753,11 +753,11 @@
editor.wrapUndoEditXmlModel(description, new Runnable() {
@Override
public void run() {
                String prefix = XmlUtils.lookupNamespacePrefix(element, TOOLS_URI, null);
if (prefix == null) {
// Add in new prefix...
prefix = XmlUtils.lookupNamespacePrefix(element,
                            TOOLS_URI, TOOLS_PREFIX);
if (value != null) {
// ...and ensure that the header is formatted such that
// the XML namespace declaration is placed in the right
//Synthetic comment -- @@ -880,11 +880,11 @@
Document doc = domModel.getDocument();
if (doc != null && element.getOwnerDocument() == doc) {
String prefix = XmlUtils.lookupNamespacePrefix(element, TOOLS_URI,
                                    null);
if (prefix == null) {
// Add in new prefix...
prefix = XmlUtils.lookupNamespacePrefix(element,
                                        TOOLS_URI, TOOLS_PREFIX);
}

String v = value;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 25f69de..e397932 100644

//Synthetic comment -- @@ -565,6 +565,7 @@
for (Object choice : choices) {
String keyword = null;
String nsPrefix = null;
Image icon = null;
String tooltip = null;
if (choice instanceof ElementDescriptor) {
//Synthetic comment -- @@ -582,11 +583,11 @@

// Get the namespace URI for the attribute. Note that some attributes
// do not have a namespace and thus return null here.
                String nsUri = ((AttributeDescriptor)choice).getNamespaceUri();
if (nsUri != null) {
nsPrefix = nsUriMap.get(nsUri);
if (nsPrefix == null) {
                        nsPrefix = XmlUtils.lookupNamespacePrefix(currentNode, nsUri);
nsUriMap.put(nsUri, nsPrefix);
}
}
//Synthetic comment -- @@ -680,7 +681,9 @@
icon,                               // Image image
displayString,                      // displayString
null,                               // IContextInformation contextInformation
                    tooltip                             // String additionalProposalInfo
));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/CompletionProposal.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/CompletionProposal.java
//Synthetic comment -- index 74b7dd8..c1d2b10 100644

//Synthetic comment -- @@ -15,20 +15,29 @@
*/
package com.android.ide.eclipse.adt.internal.editors;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -54,18 +63,21 @@
private final AndroidContentAssist mAssist;
private final Object mChoice;
private final int mCursorPosition;
    private final int mReplacementOffset;
private final int mReplacementLength;
private final String mReplacementString;
private final Image mImage;
private final String mDisplayString;
private final IContextInformation mContextInformation;
private String mAdditionalProposalInfo;

CompletionProposal(AndroidContentAssist assist,
Object choice, String replacementString, int replacementOffset,
int replacementLength, int cursorPosition, Image image, String displayString,
            IContextInformation contextInformation, String additionalProposalInfo) {
assert replacementString != null;
assert replacementOffset >= 0;
assert replacementLength >= 0;
//Synthetic comment -- @@ -81,6 +93,8 @@
mDisplayString = displayString;
mContextInformation = contextInformation;
mAdditionalProposalInfo = additionalProposalInfo;
}

@Override
//Synthetic comment -- @@ -151,6 +165,39 @@
@Override
public void apply(IDocument document) {
try {
document.replace(mReplacementOffset, mReplacementLength, mReplacementString);
} catch (BadLocationException x) {
// ignore








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java
//Synthetic comment -- index fa9e18f..c71064e 100644

//Synthetic comment -- @@ -53,11 +53,11 @@
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







