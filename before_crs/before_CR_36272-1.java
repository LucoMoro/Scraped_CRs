/*Add nullness metadata

Change-Id:I4496147aaf1d101ab2762ef1e5dbd96f66b602a3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index 2da7678..9b64d4f 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML.ContentTypeID_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.util.Pair;
//Synthetic comment -- @@ -70,7 +72,8 @@
* @param node2 the second node to test
* @return the nearest common parent of the two given nodes
*/
    public static Node getCommonAncestor(Node node1, Node node2) {
while (node2 != null) {
Node current = node1;
while (current != null && current != node2) {
//Synthetic comment -- @@ -92,13 +95,14 @@
* @param node the node to search from
* @return all elements in the subtree formed by the node parameter
*/
    public static List<Element> getAllElements(Node node) {
List<Element> elements = new ArrayList<Element>(64);
addElements(node, elements);
return elements;
}

    private static void addElements(Node node, List<Element> elements) {
if (node instanceof Element) {
elements.add((Element) node);
}
//Synthetic comment -- @@ -116,7 +120,7 @@
* @param node the node to test
* @return the depth in the document
*/
    public static int getDepth(Node node) {
int depth = -1;
while (node != null) {
depth++;
//Synthetic comment -- @@ -132,7 +136,7 @@
* @param node the node to test for element children
* @return true if the node has one or more element children
*/
    public static boolean hasElementChildren(Node node) {
NodeList children = node.getChildNodes();
for (int i = 0, n = children.getLength(); i < n; i++) {
if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
//Synthetic comment -- @@ -151,7 +155,8 @@
* @param offset The offset to look up the node for
* @return The node containing the offset, or null
*/
    public static Node getNode(IDocument document, int offset) {
Node node = null;
IModelManager modelManager = StructuredModelManager.getModelManager();
if (modelManager == null) {
//Synthetic comment -- @@ -204,7 +209,8 @@
*         return the parent. Note that the method can also return null if no
*         document or model could be obtained or if the offset is invalid.
*/
    public static Pair<Node, Node> getNodeContext(IDocument document, int offset) {
Node node = null;
IModelManager modelManager = StructuredModelManager.getModelManager();
if (modelManager == null) {
//Synthetic comment -- @@ -283,7 +289,8 @@
* @return the node which surrounds the given offset, or the node adjacent to the offset
*    where the side depends on the forward parameter
*/
    public static Node getNode(IDocument document, int offset, boolean forward) {
Node node = getNode(document, offset);

if (node instanceof IndexedRegion) {
//Synthetic comment -- @@ -318,8 +325,9 @@
* @param endOffset the ending offset of the range
* @return a pair of begin+end elements, or null
*/
    public static Pair<Element, Element> getElementRange(IDocument document, int beginOffset,
            int endOffset) {
Element beginElement = null;
Element endElement = null;
Node beginNode = getNode(document, beginOffset, true);
//Synthetic comment -- @@ -383,7 +391,8 @@
* @param node the starting node
* @return the next sibling element, or null
*/
    public static Element getNextElement(Node node) {
while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
node = node.getNextSibling();
}
//Synthetic comment -- @@ -397,7 +406,8 @@
* @param node the starting node
* @return the previous sibling element, or null
*/
    public static Element getPreviousElement(Node node) {
while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
node = node.getPreviousSibling();
}
//Synthetic comment -- @@ -411,7 +421,8 @@
* @param node the starting node
* @return the closest parent element, or null
*/
    public static Element getParentElement(Node node) {
while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
node = node.getParentNode();
}
//Synthetic comment -- @@ -426,7 +437,8 @@
* @param attrValue the value to be escaped
* @return the escaped value
*/
    public static String toXmlAttributeValue(String attrValue) {
for (int i = 0, n = attrValue.length(); i < n; i++) {
char c = attrValue.charAt(i);
if (c == '"' || c == '\'' || c == '<' || c == '&') {
//Synthetic comment -- @@ -446,7 +458,8 @@
* @param sb the string builder
* @param attrValue the attribute value to be appended and escaped
*/
    public static void appendXmlAttributeValue(StringBuilder sb, String attrValue) {
int n = attrValue.length();
// &, ", ' and < are illegal in attributes; see http://www.w3.org/TR/REC-xml/#NT-AttValue
// (' legal in a " string and " is legal in a ' string but here we'll stay on the safe
//Synthetic comment -- @@ -474,7 +487,7 @@
* @param sb the string builder
* @param textValue the text value to be appended and escaped
*/
    public static void appendXmlTextValue(StringBuilder sb, String textValue) {
for (int i = 0, n = textValue.length(); i < n; i++) {
char c = textValue.charAt(i);
if (c == '<') {
//Synthetic comment -- @@ -488,7 +501,7 @@
}

/** Utility used by {@link #getFreeWidgetId(Element)} */
    private static void addLowercaseIds(Element root, Set<String> seen) {
if (root.hasAttributeNS(ANDROID_URI, ATTR_ID)) {
String id = root.getAttributeNS(ANDROID_URI, ATTR_ID);
if (id.startsWith(NEW_ID_PREFIX)) {
//Synthetic comment -- @@ -514,7 +527,10 @@
* @return a unique id, never null, which does not include the {@code @id/} prefix
* @see DescriptorsUtils#getFreeWidgetId
*/
    public static String getFreeWidgetId(Element element, Set<String> reserved, String prefix) {
Set<String> ids = new HashSet<String>();
if (reserved != null) {
for (String id : reserved) {
//Synthetic comment -- @@ -545,7 +561,8 @@
* @param element the parent element
* @return a list of child elements, possibly empty but never null
*/
    public static List<Element> getChildren(Element element) {
// Convenience to avoid lots of ugly DOM access casting
NodeList children = element.getChildNodes();
// An iterator would have been more natural (to directly drive the child list
//Synthetic comment -- @@ -568,7 +585,7 @@
* @param elements the elements to be tested
* @return true if the elements are contiguous siblings with no gaps
*/
    public static boolean isContiguous(List<Element> elements) {
if (elements.size() > 1) {
// All elements must be siblings (e.g. same parent)
Node parent = elements.get(0).getParentNode();
//Synthetic comment -- @@ -621,7 +638,7 @@
* @param element2 the second element to compare
* @return true if the two element hierarchies are logically equal
*/
    public static boolean isEquivalent(Element element1, Element element2) {
if (element1 == null || element2 == null) {
return false;
}
//Synthetic comment -- @@ -713,7 +730,8 @@
* @param document the document to search for an equivalent element in
* @return an equivalent element, or null
*/
    public static Element findCorresponding(Element element, Document document) {
// Make sure the method is called correctly -- the element is for a different
// document than the one we are searching
assert element.getOwnerDocument() != document;
//Synthetic comment -- @@ -736,7 +754,8 @@
}

/** Helper method for {@link #findCorresponding(Element, Document)} */
    private static Element findCorresponding(Element element, String targetId) {
String id = element.getAttributeNS(ANDROID_URI, ATTR_ID);
if (id != null) { // Work around DOM bug
if (id.equals(targetId)) {
//Synthetic comment -- @@ -772,7 +791,8 @@
* @param xml the XML content to be parsed (must be well formed)
* @return the DOM document, or null
*/
    public static Document parseStructuredDocument(String xml) {
IStructuredModel model = createStructuredModel(xml);
if (model instanceof IDOMModel) {
IDOMModel domModel = (IDOMModel) model;
//Synthetic comment -- @@ -788,7 +808,8 @@
* @param xml the XML content to be parsed (must be well formed)
* @return the structured model
*/
    public static IStructuredModel createStructuredModel(String xml) {
IStructuredModel model = createEmptyModel();
IStructuredDocument document = model.getStructuredDocument();
model.aboutToChangeModel();
//Synthetic comment -- @@ -803,6 +824,7 @@
*
* @return a new Eclipse XML model
*/
public static IStructuredModel createEmptyModel() {
IModelManager modelManager = StructuredModelManager.getModelManager();
return modelManager.createUnManagedStructuredModelFor(ContentTypeID_XML);
//Synthetic comment -- @@ -813,6 +835,7 @@
*
* @return an empty Eclipse XML document
*/
public static Document createEmptyDocument() {
IStructuredModel model = createEmptyModel();
if (model instanceof IDOMModel) {
//Synthetic comment -- @@ -832,7 +855,8 @@
*            silently return null
* @return the DOM document, or null
*/
    public static Document parseDocument(String xml, boolean logParserErrors) {
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
InputSource is = new InputSource(new StringReader(xml));
factory.setNamespaceAware(true);







