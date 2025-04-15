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
    /** Temporary data used by the editors. This data is not sync'ed with the XML */
    private Object mEditData;

/**
* Creates a new {@link UiElementNode} described by a given {@link ElementDescriptor}.
//Synthetic comment -- @@ -177,12 +175,12 @@
*/
private HashMap<AttributeDescriptor, UiAttributeNode> getInternalUiAttributes() {
if (mUiAttributes == null) {
            AttributeDescriptor[] attr_list = getAttributeDescriptors();
            mUiAttributes = new HashMap<AttributeDescriptor, UiAttributeNode>(attr_list.length);
            for (AttributeDescriptor desc : attr_list) {
                UiAttributeNode ui_node = desc.createUiNode(this);
                if (ui_node != null) {  // Some AttributeDescriptors do not have UI associated
                    mUiAttributes.put(desc, ui_node);
}
}
}
//Synthetic comment -- @@ -273,20 +271,20 @@
* Computes a "breadcrumb trail" description for this node.
* It will look something like "Manifest > Application > .myactivity (Activity) > Intent-Filter"
*
     * @param include_root Whether to include the root (e.g. "Manifest") or not. Has no effect
*                     when called on the root node itself.
* @return The "breadcrumb trail" description for this node.
*/
    public String getBreadcrumbTrailDescription(boolean include_root) {
StringBuilder sb = new StringBuilder(getShortDescription());

        for (UiElementNode ui_node = getUiParent();
                ui_node != null;
                ui_node = ui_node.getUiParent()) {
            if (!include_root && ui_node.getUiParent() == null) {
break;
}
            sb.insert(0, String.format("%1$s > ", ui_node.getShortDescription())); //$NON-NLS-1$
}

return sb.toString();
//Synthetic comment -- @@ -298,11 +296,11 @@
* The XML {@link Document} is initially null. The XML {@link Document} must be set only on the
* UI root element node (this method takes care of that.)
*/
    public void setXmlDocument(Document xml_doc) {
if (mUiParent == null) {
            mXmlDocument = xml_doc;
} else {
            mUiParent.setXmlDocument(xml_doc);
}
}

//Synthetic comment -- @@ -368,11 +366,11 @@
private Map<String, AttributeDescriptor> getHiddenAttributeDescriptors() {
if (mCachedHiddenAttributes == null) {
mCachedHiddenAttributes = new HashMap<String, AttributeDescriptor>();
            for (AttributeDescriptor attr_desc : getAttributeDescriptors()) {
                if (attr_desc instanceof XmlnsAttributeDescriptor) {
mCachedHiddenAttributes.put(
                            ((XmlnsAttributeDescriptor) attr_desc).getXmlNsName(),
                            attr_desc);
}
}
}
//Synthetic comment -- @@ -629,21 +627,21 @@
*/
public UiElementNode findUiChildNode(String path) {
String[] items = path.split("/");  //$NON-NLS-1$
        UiElementNode ui_node = this;
for (String item : items) {
            boolean next_segment = false;
            for (UiElementNode c : ui_node.mUiChildren) {
if (c.getDescriptor().getXmlName().equals(item)) {
                    ui_node = c;
                    next_segment = true;
break;
}
}
            if (!next_segment) {
return null;
}
}
        return ui_node;
}

/**
//Synthetic comment -- @@ -675,12 +673,12 @@
* Returns the {@link UiAttributeNode} matching this attribute descriptor or
* null if not found.
*
     * @param attr_desc The {@link AttributeDescriptor} to match.
* @return the {@link UiAttributeNode} matching this attribute descriptor or null
*         if not found.
*/
    public UiAttributeNode findUiAttribute(AttributeDescriptor attr_desc) {
        return getInternalUiAttributes().get(attr_desc);
}

/**
//Synthetic comment -- @@ -692,19 +690,19 @@
* This method can be both used for populating values the first time and updating values
* after the XML model changed.
*
     * @param xml_node The XML node to mirror
* @return Returns true if the XML structure has changed (nodes added, removed or replaced)
*/
    public boolean loadFromXmlNode(Node xml_node) {
        boolean structure_changed = (mXmlNode != xml_node);
        mXmlNode = xml_node;
        if (xml_node != null) {
            updateAttributeList(xml_node);
            structure_changed |= updateElementList(xml_node);
            invokeUiUpdateListeners(structure_changed ? UiUpdateState.CHILDREN_CHANGED
: UiUpdateState.ATTR_UPDATED);
}
        return structure_changed;
}

/**
//Synthetic comment -- @@ -717,18 +715,18 @@
* Rather than try to diff inflated UI nodes (as loadFromXmlNode does), we don't bother
* and reload everything. This is not subtle and should be used very rarely.
*
     * @param xml_node The XML node or document to reload. Can be null.
*/
    public void reloadFromXmlNode(Node xml_node) {
// The editor needs to be preserved, it is not affected by an XML change.
AndroidXmlEditor editor = getEditor();
clearContent();
setEditor(editor);
        if (xml_node != null) {
            setXmlDocument(xml_node.getOwnerDocument());
}
// This will reload all the XML and recreate the UI structure from scratch.
        loadFromXmlNode(xml_node);
}

/**
//Synthetic comment -- @@ -759,28 +757,30 @@
* This is called by the UI when the embedding part needs to be committed.
*/
public void commit() {
        for (UiAttributeNode ui_attr : getInternalUiAttributes().values()) {
            ui_attr.commit();
}

        for (UiAttributeNode ui_attr : mUnknownUiAttributes) {
            ui_attr.commit();
}
}

/**
* Returns true if the part has been modified with respect to the data
* loaded from the model.
*/
public boolean isDirty() {
        for (UiAttributeNode ui_attr : getInternalUiAttributes().values()) {
            if (ui_attr.isDirty()) {
return true;
}
}

        for (UiAttributeNode ui_attr : mUnknownUiAttributes) {
            if (ui_attr.isDirty()) {
return true;
}
}
//Synthetic comment -- @@ -809,22 +809,22 @@
}
}

        String element_name = getDescriptor().getXmlName();
Document doc = getXmlDocument();

// We *must* have a root node. If not, we need to abort.
if (doc == null) {
throw new RuntimeException(
                    String.format("Missing XML document for %1$s XML node.", element_name));
}

        // If we get here and parent_xml_node is null, the node is to be created
// as the root node of the document (which can't be null, cf check above).
if (parentXmlNode == null) {
parentXmlNode = doc;
}

        mXmlNode = doc.createElement(element_name);

Node xmlNextSibling = null;

//Synthetic comment -- @@ -851,17 +851,17 @@
// Set all initial attributes in the XML node if they are not empty.
// Iterate on the descriptor list to get the desired order and then use the
// internal values, if any.
        for (AttributeDescriptor attr_desc : getAttributeDescriptors()) {
            if (attr_desc instanceof XmlnsAttributeDescriptor) {
                XmlnsAttributeDescriptor desc = (XmlnsAttributeDescriptor) attr_desc;
Attr attr = doc.createAttributeNS(XmlnsAttributeDescriptor.XMLNS_URI,
desc.getXmlNsName());
attr.setValue(desc.getValue());
attr.setPrefix(desc.getXmlNsPrefix());
mXmlNode.getAttributes().setNamedItemNS(attr);
} else {
                UiAttributeNode ui_attr = getInternalUiAttributes().get(attr_desc);
                commitAttributeToXml(ui_attr, ui_attr.getCurrentValue());
}
}

//Synthetic comment -- @@ -883,25 +883,25 @@
// First clear the internals of the node and *then* actually deletes the XML
// node (because doing so will generate an update even and this node may be
// revisited via loadFromXmlNode).
        Node old_xml_node = mXmlNode;
clearContent();

        Node xml_parent = old_xml_node.getParentNode();
        if (xml_parent == null) {
            xml_parent = getXmlDocument();
}
        Node nextSibling = old_xml_node.getNextSibling();
        old_xml_node = xml_parent.removeChild(old_xml_node);

// Remove following text node if it's just blank space, to account for
// the fact what we add these when we insert nodes.
if (nextSibling != null && nextSibling.getNodeType() == Node.TEXT_NODE
&& nextSibling.getNodeValue().trim().length() == 0) {
            xml_parent.removeChild(nextSibling);
}

invokeUiUpdateListeners(UiUpdateState.DELETED);
        return old_xml_node;
}

/**
//Synthetic comment -- @@ -921,104 +921,104 @@
* </ul>
* Note that only the first case is used when populating the ui list the first time.
*
     * @param xml_node The XML node to mirror
* @return True when the XML structure has changed.
*/
    protected boolean updateElementList(Node xml_node) {
        boolean structure_changed = false;
        int ui_index = 0;
        Node xml_child = xml_node.getFirstChild();
        while (xml_child != null) {
            if (xml_child.getNodeType() == Node.ELEMENT_NODE) {
                String element_name = xml_child.getNodeName();
                UiElementNode ui_node = null;
                if (mUiChildren.size() <= ui_index) {
// A new node is being added at the end of the list
                    ElementDescriptor desc = mDescriptor.findChildrenDescriptor(element_name,
false /* recursive */);
if (desc == null) {
// Unknown node. Create a temporary descriptor for it.
// We'll add unknown attributes to it later.
IUnknownDescriptorProvider p = getUnknownDescriptorProvider();
                        desc = p.getDescriptor(element_name);
}
                    structure_changed = true;
                    ui_node = appendNewUiChild(desc);
                    ui_index++;
} else {
// A new node is being inserted or moved.
// Note: mandatory nodes can be created without an XML node in which case
// getXmlNode() is null.
                    UiElementNode ui_child;
int n = mUiChildren.size();
                    for (int j = ui_index; j < n; j++) {
                        ui_child = mUiChildren.get(j);
                        if (ui_child.getXmlNode() != null && ui_child.getXmlNode() == xml_child) {
                            if (j > ui_index) {
// Found the same XML node at some later index, now move it here.
mUiChildren.remove(j);
                                mUiChildren.add(ui_index, ui_child);
                                structure_changed = true;
}
                            ui_node = ui_child;
                            ui_index++;
break;
}
}

                    if (ui_node == null) {
// Look for an unused mandatory node with no XML node attached
// referencing the same XML element name
                        for (int j = ui_index; j < n; j++) {
                            ui_child = mUiChildren.get(j);
                            if (ui_child.getXmlNode() == null &&
                                    ui_child.getDescriptor().isMandatory() &&
                                    ui_child.getDescriptor().getXmlName().equals(element_name)) {
                                if (j > ui_index) {
// Found it, now move it here
mUiChildren.remove(j);
                                    mUiChildren.add(ui_index, ui_child);
}
// assign the XML node to this empty mandatory element.
                                ui_child.mXmlNode = xml_child;
                                structure_changed = true;
                                ui_node = ui_child;
                                ui_index++;
}
}
}

                    if (ui_node == null) {
// Inserting new node
                        ElementDescriptor desc = mDescriptor.findChildrenDescriptor(element_name,
false /* recursive */);
if (desc == null) {
// Unknown element. Simply ignore it.
AdtPlugin.log(IStatus.WARNING,
"AndroidManifest: Ignoring unknown '%s' XML element", //$NON-NLS-1$
                                    element_name);
} else {
                            structure_changed = true;
                            ui_node = insertNewUiChild(ui_index, desc);
                            ui_index++;
}
}
}
                if (ui_node != null) {
// If we touched an UI Node, even an existing one, refresh its content.
// For new nodes, this will populate them recursively.
                    structure_changed |= ui_node.loadFromXmlNode(xml_child);
}
}
            xml_child = xml_child.getNextSibling();
}

// There might be extra UI nodes at the end if the XML node list got shorter.
        for (int index = mUiChildren.size() - 1; index >= ui_index; --index) {
             structure_changed |= removeUiChildAtIndex(index);
}

        return structure_changed;
}

/**
//Synthetic comment -- @@ -1028,27 +1028,27 @@
* Also invokes the update listener on the node to be deleted *after* the node has
* been removed.
*
     * @param ui_index The index of the UI child to remove, range 0 .. mUiChildren.size()-1
* @return True if the structure has changed
* @throws IndexOutOfBoundsException if index is out of mUiChildren's bounds. Of course you
*         know that could never happen unless the computer is on fire or something.
*/
    private boolean removeUiChildAtIndex(int ui_index) {
        UiElementNode ui_node = mUiChildren.get(ui_index);
        ElementDescriptor desc = ui_node.getDescriptor();

try {
            if (ui_node.getDescriptor().isMandatory()) {
// This is a mandatory node. Such a node must exist in the UiNode hierarchy
// even if there's no XML counterpart. However we only need to keep one.

// Check if the parent (e.g. this node) has another similar ui child node.
boolean keepNode = true;
for (UiElementNode child : mUiChildren) {
                    if (child != ui_node && child.getDescriptor() == desc) {
// We found another child with the same descriptor that is not
// the node we want to remove. This means we have one mandatory
                        // node so we can safely remove ui_node.
keepNode = false;
break;
}
//Synthetic comment -- @@ -1062,14 +1062,14 @@
// A mandatory node with no XML means it doesn't really exist, so it can't be
// deleted. So the structure will change only if the ui node is actually
// associated to an XML node.
                    boolean xml_exists = (ui_node.getXmlNode() != null);

                    ui_node.clearContent();
                    return xml_exists;
}
}

            mUiChildren.remove(ui_index);
return true;
} finally {
// Tell listeners that a node has been removed.
//Synthetic comment -- @@ -1086,12 +1086,12 @@
* @return The new UI node that has been appended
*/
public UiElementNode appendNewUiChild(ElementDescriptor descriptor) {
        UiElementNode ui_node;
        ui_node = descriptor.createUiNode();
        mUiChildren.add(ui_node);
        ui_node.setUiParent(this);
        ui_node.invokeUiUpdateListeners(UiUpdateState.CREATED);
        return ui_node;
}

/**
//Synthetic comment -- @@ -1103,12 +1103,12 @@
* @return The new UI node.
*/
public UiElementNode insertNewUiChild(int index, ElementDescriptor descriptor) {
        UiElementNode ui_node;
        ui_node = descriptor.createUiNode();
        mUiChildren.add(index, ui_node);
        ui_node.setUiParent(this);
        ui_node.invokeUiUpdateListeners(UiUpdateState.CREATED);
        return ui_node;
}

/**
//Synthetic comment -- @@ -1233,28 +1233,8 @@
// --- for derived implementations only ---

// TODO doc
    protected void setXmlNode(Node xml_node) {
        mXmlNode = xml_node;
    }

    /**
     * Sets the temporary data used by the editors.
     * @param data the data.
     *
     * @since GLE1
     * @deprecated Used by GLE1. Should be deprecated for GLE2.
     */
    @Deprecated
    public void setEditData(Object data) {
        mEditData = data;
    }

    /**
     * Returns the temporary data used by the editors for this object.
     * @return the data, or <code>null</code> if none has been set.
     */
    public Object getEditData() {
        return mEditData;
}

public void refreshUi() {
//Synthetic comment -- @@ -1330,10 +1310,10 @@
HashMap<AttributeDescriptor, UiAttributeNode> attributeMap = getInternalUiAttributes();

for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
            UiAttributeNode ui_attr = entry.getValue();
            if (ui_attr.isDirty()) {
                result |= commitAttributeToXml(ui_attr, ui_attr.getCurrentValue());
                ui_attr.setDirty(false);
}
}
return result;
//Synthetic comment -- @@ -1523,10 +1503,10 @@
HashMap<AttributeDescriptor, UiAttributeNode> attributeMap = getInternalUiAttributes();

for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
            AttributeDescriptor ui_desc = entry.getKey();
            if (ui_desc.getXmlLocalName().equals(attrXmlName)) {
                UiAttributeNode ui_attr = entry.getValue();
                return ui_attr.getCurrentValue();
}
}
return null;







