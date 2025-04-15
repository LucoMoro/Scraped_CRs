/*Remove deprecated UiElementNode "editData" property

As requested in	another	review (#18753) the editData property on
UiElementNodes is obsolete (it was used by GLE1) and should be
removed. While there I also updated the naming style of local vars in
that class from underline to camelcase.

Change-Id:Ic5c50d07abedb1177cd018c866901f1e54cd0ec5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 1d9cacf..71d8e8f 100644

//Synthetic comment -- @@ -118,8 +118,6 @@
private IUnknownDescriptorProvider mUnknownDescProvider;
/** Error Flag */
private boolean mHasError;

/**
* Creates a new {@link UiElementNode} described by a given {@link ElementDescriptor}.
//Synthetic comment -- @@ -177,12 +175,12 @@
*/
private HashMap<AttributeDescriptor, UiAttributeNode> getInternalUiAttributes() {
if (mUiAttributes == null) {
            AttributeDescriptor[] attrList = getAttributeDescriptors();
            mUiAttributes = new HashMap<AttributeDescriptor, UiAttributeNode>(attrList.length);
            for (AttributeDescriptor desc : attrList) {
                UiAttributeNode uiNode = desc.createUiNode(this);
                if (uiNode != null) {  // Some AttributeDescriptors do not have UI associated
                    mUiAttributes.put(desc, uiNode);
}
}
}
//Synthetic comment -- @@ -273,20 +271,20 @@
* Computes a "breadcrumb trail" description for this node.
* It will look something like "Manifest > Application > .myactivity (Activity) > Intent-Filter"
*
     * @param includeRoot Whether to include the root (e.g. "Manifest") or not. Has no effect
*                     when called on the root node itself.
* @return The "breadcrumb trail" description for this node.
*/
    public String getBreadcrumbTrailDescription(boolean includeRoot) {
StringBuilder sb = new StringBuilder(getShortDescription());

        for (UiElementNode uiNode = getUiParent();
                uiNode != null;
                uiNode = uiNode.getUiParent()) {
            if (!includeRoot && uiNode.getUiParent() == null) {
break;
}
            sb.insert(0, String.format("%1$s > ", uiNode.getShortDescription())); //$NON-NLS-1$
}

return sb.toString();
//Synthetic comment -- @@ -298,11 +296,11 @@
* The XML {@link Document} is initially null. The XML {@link Document} must be set only on the
* UI root element node (this method takes care of that.)
*/
    public void setXmlDocument(Document xmlDoc) {
if (mUiParent == null) {
            mXmlDocument = xmlDoc;
} else {
            mUiParent.setXmlDocument(xmlDoc);
}
}

//Synthetic comment -- @@ -368,11 +366,11 @@
private Map<String, AttributeDescriptor> getHiddenAttributeDescriptors() {
if (mCachedHiddenAttributes == null) {
mCachedHiddenAttributes = new HashMap<String, AttributeDescriptor>();
            for (AttributeDescriptor attrDesc : getAttributeDescriptors()) {
                if (attrDesc instanceof XmlnsAttributeDescriptor) {
mCachedHiddenAttributes.put(
                            ((XmlnsAttributeDescriptor) attrDesc).getXmlNsName(),
                            attrDesc);
}
}
}
//Synthetic comment -- @@ -629,21 +627,21 @@
*/
public UiElementNode findUiChildNode(String path) {
String[] items = path.split("/");  //$NON-NLS-1$
        UiElementNode uiNode = this;
for (String item : items) {
            boolean nextSegment = false;
            for (UiElementNode c : uiNode.mUiChildren) {
if (c.getDescriptor().getXmlName().equals(item)) {
                    uiNode = c;
                    nextSegment = true;
break;
}
}
            if (!nextSegment) {
return null;
}
}
        return uiNode;
}

/**
//Synthetic comment -- @@ -675,12 +673,12 @@
* Returns the {@link UiAttributeNode} matching this attribute descriptor or
* null if not found.
*
     * @param attrDesc The {@link AttributeDescriptor} to match.
* @return the {@link UiAttributeNode} matching this attribute descriptor or null
*         if not found.
*/
    public UiAttributeNode findUiAttribute(AttributeDescriptor attrDesc) {
        return getInternalUiAttributes().get(attrDesc);
}

/**
//Synthetic comment -- @@ -692,19 +690,19 @@
* This method can be both used for populating values the first time and updating values
* after the XML model changed.
*
     * @param xmlNode The XML node to mirror
* @return Returns true if the XML structure has changed (nodes added, removed or replaced)
*/
    public boolean loadFromXmlNode(Node xmlNode) {
        boolean structureChanged = (mXmlNode != xmlNode);
        mXmlNode = xmlNode;
        if (xmlNode != null) {
            updateAttributeList(xmlNode);
            structureChanged |= updateElementList(xmlNode);
            invokeUiUpdateListeners(structureChanged ? UiUpdateState.CHILDREN_CHANGED
: UiUpdateState.ATTR_UPDATED);
}
        return structureChanged;
}

/**
//Synthetic comment -- @@ -717,18 +715,18 @@
* Rather than try to diff inflated UI nodes (as loadFromXmlNode does), we don't bother
* and reload everything. This is not subtle and should be used very rarely.
*
     * @param xmlNode The XML node or document to reload. Can be null.
*/
    public void reloadFromXmlNode(Node xmlNode) {
// The editor needs to be preserved, it is not affected by an XML change.
AndroidXmlEditor editor = getEditor();
clearContent();
setEditor(editor);
        if (xmlNode != null) {
            setXmlDocument(xmlNode.getOwnerDocument());
}
// This will reload all the XML and recreate the UI structure from scratch.
        loadFromXmlNode(xmlNode);
}

/**
//Synthetic comment -- @@ -759,28 +757,30 @@
* This is called by the UI when the embedding part needs to be committed.
*/
public void commit() {
        for (UiAttributeNode uiAttr : getInternalUiAttributes().values()) {
            uiAttr.commit();
}

        for (UiAttributeNode uiAttr : mUnknownUiAttributes) {
            uiAttr.commit();
}
}

/**
* Returns true if the part has been modified with respect to the data
* loaded from the model.
     * @return True if the part has been modified with respect to the data
     * loaded from the model.
*/
public boolean isDirty() {
        for (UiAttributeNode uiAttr : getInternalUiAttributes().values()) {
            if (uiAttr.isDirty()) {
return true;
}
}

        for (UiAttributeNode uiAttr : mUnknownUiAttributes) {
            if (uiAttr.isDirty()) {
return true;
}
}
//Synthetic comment -- @@ -809,22 +809,22 @@
}
}

        String elementName = getDescriptor().getXmlName();
Document doc = getXmlDocument();

// We *must* have a root node. If not, we need to abort.
if (doc == null) {
throw new RuntimeException(
                    String.format("Missing XML document for %1$s XML node.", elementName));
}

        // If we get here and parentXmlNode is null, the node is to be created
// as the root node of the document (which can't be null, cf check above).
if (parentXmlNode == null) {
parentXmlNode = doc;
}

        mXmlNode = doc.createElement(elementName);

Node xmlNextSibling = null;

//Synthetic comment -- @@ -851,17 +851,17 @@
// Set all initial attributes in the XML node if they are not empty.
// Iterate on the descriptor list to get the desired order and then use the
// internal values, if any.
        for (AttributeDescriptor attrDesc : getAttributeDescriptors()) {
            if (attrDesc instanceof XmlnsAttributeDescriptor) {
                XmlnsAttributeDescriptor desc = (XmlnsAttributeDescriptor) attrDesc;
Attr attr = doc.createAttributeNS(XmlnsAttributeDescriptor.XMLNS_URI,
desc.getXmlNsName());
attr.setValue(desc.getValue());
attr.setPrefix(desc.getXmlNsPrefix());
mXmlNode.getAttributes().setNamedItemNS(attr);
} else {
                UiAttributeNode uiAttr = getInternalUiAttributes().get(attrDesc);
                commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
}
}

//Synthetic comment -- @@ -883,25 +883,25 @@
// First clear the internals of the node and *then* actually deletes the XML
// node (because doing so will generate an update even and this node may be
// revisited via loadFromXmlNode).
        Node oldXmlNode = mXmlNode;
clearContent();

        Node xmlParent = oldXmlNode.getParentNode();
        if (xmlParent == null) {
            xmlParent = getXmlDocument();
}
        Node nextSibling = oldXmlNode.getNextSibling();
        oldXmlNode = xmlParent.removeChild(oldXmlNode);

// Remove following text node if it's just blank space, to account for
// the fact what we add these when we insert nodes.
if (nextSibling != null && nextSibling.getNodeType() == Node.TEXT_NODE
&& nextSibling.getNodeValue().trim().length() == 0) {
            xmlParent.removeChild(nextSibling);
}

invokeUiUpdateListeners(UiUpdateState.DELETED);
        return oldXmlNode;
}

/**
//Synthetic comment -- @@ -921,104 +921,104 @@
* </ul>
* Note that only the first case is used when populating the ui list the first time.
*
     * @param xmlNode The XML node to mirror
* @return True when the XML structure has changed.
*/
    protected boolean updateElementList(Node xmlNode) {
        boolean structureChanged = false;
        int uiIndex = 0;
        Node xmlChild = xmlNode.getFirstChild();
        while (xmlChild != null) {
            if (xmlChild.getNodeType() == Node.ELEMENT_NODE) {
                String elementName = xmlChild.getNodeName();
                UiElementNode uiNode = null;
                if (mUiChildren.size() <= uiIndex) {
// A new node is being added at the end of the list
                    ElementDescriptor desc = mDescriptor.findChildrenDescriptor(elementName,
false /* recursive */);
if (desc == null) {
// Unknown node. Create a temporary descriptor for it.
// We'll add unknown attributes to it later.
IUnknownDescriptorProvider p = getUnknownDescriptorProvider();
                        desc = p.getDescriptor(elementName);
}
                    structureChanged = true;
                    uiNode = appendNewUiChild(desc);
                    uiIndex++;
} else {
// A new node is being inserted or moved.
// Note: mandatory nodes can be created without an XML node in which case
// getXmlNode() is null.
                    UiElementNode uiChild;
int n = mUiChildren.size();
                    for (int j = uiIndex; j < n; j++) {
                        uiChild = mUiChildren.get(j);
                        if (uiChild.getXmlNode() != null && uiChild.getXmlNode() == xmlChild) {
                            if (j > uiIndex) {
// Found the same XML node at some later index, now move it here.
mUiChildren.remove(j);
                                mUiChildren.add(uiIndex, uiChild);
                                structureChanged = true;
}
                            uiNode = uiChild;
                            uiIndex++;
break;
}
}

                    if (uiNode == null) {
// Look for an unused mandatory node with no XML node attached
// referencing the same XML element name
                        for (int j = uiIndex; j < n; j++) {
                            uiChild = mUiChildren.get(j);
                            if (uiChild.getXmlNode() == null &&
                                    uiChild.getDescriptor().isMandatory() &&
                                    uiChild.getDescriptor().getXmlName().equals(elementName)) {
                                if (j > uiIndex) {
// Found it, now move it here
mUiChildren.remove(j);
                                    mUiChildren.add(uiIndex, uiChild);
}
// assign the XML node to this empty mandatory element.
                                uiChild.mXmlNode = xmlChild;
                                structureChanged = true;
                                uiNode = uiChild;
                                uiIndex++;
}
}
}

                    if (uiNode == null) {
// Inserting new node
                        ElementDescriptor desc = mDescriptor.findChildrenDescriptor(elementName,
false /* recursive */);
if (desc == null) {
// Unknown element. Simply ignore it.
AdtPlugin.log(IStatus.WARNING,
"AndroidManifest: Ignoring unknown '%s' XML element", //$NON-NLS-1$
                                    elementName);
} else {
                            structureChanged = true;
                            uiNode = insertNewUiChild(uiIndex, desc);
                            uiIndex++;
}
}
}
                if (uiNode != null) {
// If we touched an UI Node, even an existing one, refresh its content.
// For new nodes, this will populate them recursively.
                    structureChanged |= uiNode.loadFromXmlNode(xmlChild);
}
}
            xmlChild = xmlChild.getNextSibling();
}

// There might be extra UI nodes at the end if the XML node list got shorter.
        for (int index = mUiChildren.size() - 1; index >= uiIndex; --index) {
             structureChanged |= removeUiChildAtIndex(index);
}

        return structureChanged;
}

/**
//Synthetic comment -- @@ -1028,27 +1028,27 @@
* Also invokes the update listener on the node to be deleted *after* the node has
* been removed.
*
     * @param uiIndex The index of the UI child to remove, range 0 .. mUiChildren.size()-1
* @return True if the structure has changed
* @throws IndexOutOfBoundsException if index is out of mUiChildren's bounds. Of course you
*         know that could never happen unless the computer is on fire or something.
*/
    private boolean removeUiChildAtIndex(int uiIndex) {
        UiElementNode uiNode = mUiChildren.get(uiIndex);
        ElementDescriptor desc = uiNode.getDescriptor();

try {
            if (uiNode.getDescriptor().isMandatory()) {
// This is a mandatory node. Such a node must exist in the UiNode hierarchy
// even if there's no XML counterpart. However we only need to keep one.

// Check if the parent (e.g. this node) has another similar ui child node.
boolean keepNode = true;
for (UiElementNode child : mUiChildren) {
                    if (child != uiNode && child.getDescriptor() == desc) {
// We found another child with the same descriptor that is not
// the node we want to remove. This means we have one mandatory
                        // node so we can safely remove uiNode.
keepNode = false;
break;
}
//Synthetic comment -- @@ -1062,14 +1062,14 @@
// A mandatory node with no XML means it doesn't really exist, so it can't be
// deleted. So the structure will change only if the ui node is actually
// associated to an XML node.
                    boolean xmlExists = (uiNode.getXmlNode() != null);

                    uiNode.clearContent();
                    return xmlExists;
}
}

            mUiChildren.remove(uiIndex);
return true;
} finally {
// Tell listeners that a node has been removed.
//Synthetic comment -- @@ -1086,12 +1086,12 @@
* @return The new UI node that has been appended
*/
public UiElementNode appendNewUiChild(ElementDescriptor descriptor) {
        UiElementNode uiNode;
        uiNode = descriptor.createUiNode();
        mUiChildren.add(uiNode);
        uiNode.setUiParent(this);
        uiNode.invokeUiUpdateListeners(UiUpdateState.CREATED);
        return uiNode;
}

/**
//Synthetic comment -- @@ -1103,12 +1103,12 @@
* @return The new UI node.
*/
public UiElementNode insertNewUiChild(int index, ElementDescriptor descriptor) {
        UiElementNode uiNode;
        uiNode = descriptor.createUiNode();
        mUiChildren.add(index, uiNode);
        uiNode.setUiParent(this);
        uiNode.invokeUiUpdateListeners(UiUpdateState.CREATED);
        return uiNode;
}

/**
//Synthetic comment -- @@ -1233,28 +1233,8 @@
// --- for derived implementations only ---

// TODO doc
    protected void setXmlNode(Node xmlNode) {
        mXmlNode = xmlNode;
}

public void refreshUi() {
//Synthetic comment -- @@ -1330,10 +1310,10 @@
HashMap<AttributeDescriptor, UiAttributeNode> attributeMap = getInternalUiAttributes();

for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
            UiAttributeNode uiAttr = entry.getValue();
            if (uiAttr.isDirty()) {
                result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
                uiAttr.setDirty(false);
}
}
return result;
//Synthetic comment -- @@ -1523,10 +1503,10 @@
HashMap<AttributeDescriptor, UiAttributeNode> attributeMap = getInternalUiAttributes();

for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
            AttributeDescriptor uiDesc = entry.getKey();
            if (uiDesc.getXmlLocalName().equals(attrXmlName)) {
                UiAttributeNode uiAttr = entry.getValue();
                return uiAttr.getCurrentValue();
}
}
return null;







