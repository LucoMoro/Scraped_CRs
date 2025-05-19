//<Beginning of snippet n. 0>
for (CanvasViewInfo childVi : getChildren()) {
    SimpleElement e2 = childVi.toSimpleElement();
    if (e2 != null) {
        for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
            UiAttributeNode uiAttr = entry.getValue();
            if (uiAttr.isDirty()) {
                result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
                uiAttr.setDirty(false);
            }
            String attrXmlName = entry.getKey().getName();
            String attrNsUri = entry.getKey().getNamespaceUri();
            if (!hasExistingUnknownAttribute(attrXmlName, attrNsUri)) {
                uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
                uiAttr.setCurrentValue(uiAttr.getCurrentValue());
            }
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public boolean commitDirtyAttributesToXml() {
    boolean result = false;
    HashMap<AttributeDescriptor, UiAttributeNode> attributeMap = getInternalUiAttributes();

    for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
        UiAttributeNode uiAttr = entry.getValue();
        if (uiAttr.isDirty()) {
            result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
            uiAttr.setDirty(false);
        }
    }

    for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
        UiAttributeNode uiAttr = entry.getValue();
        String attrXmlName = entry.getKey().getName();
        String attrNsUri = entry.getKey().getNamespaceUri();
        if (!hasExistingUnknownAttribute(attrXmlName, attrNsUri)) {
            uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
            uiAttr.setCurrentValue(uiAttr.getCurrentValue());
        }
    }

    return result;
}

if ((attrNsUri == null && uiDesc.getNamespaceUri() == null) ||
    (attrNsUri != null && attrNsUri.equals(uiDesc.getNamespaceUri()))) {
    
    if (uiAttr instanceof IUiSettableAttributeNode) {
        String current = uiAttr.getCurrentValue();
        if (override || current == null || !current.equals(value)) {
            ((IUiSettableAttributeNode) uiAttr).setCurrentValue(value);
            uiAttr.setDirty(true);
            return uiAttr;
        }
    }
}
return null;
//<End of snippet n. 1>