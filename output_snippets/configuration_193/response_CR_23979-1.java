//<Beginning of snippet n. 0>
for (CanvasViewInfo childVi : getChildren()) {
    SimpleElement e2 = childVi.toSimpleElement();
    if (e2 != null) {
        // Iterating over unknown attributes and initializing
        for (UnknownAttribute unknownAttr : getUnknownAttributes()) {
            String apiKey = unknownAttr.getApiKey();
            unknownAttr.setCurrentValue(apiKey != null ? apiKey : "");
            unknownAttr.setDirty(true); // Mark as dirty upon initialization
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

    // Handling unknown attributes
    for (UnknownAttribute unknownAttr : getUnknownAttributes()) {
        if (unknownAttr.isDirty()) {
            result |= commitAttributeToXml(unknownAttr, unknownAttr.getCurrentValue());
            unknownAttr.setDirty(false);
        }
    }
    return result;
}

uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
if (uiAttr != null) {
    // Immediately set the value on the unknown attribute upon creation
    uiAttr.setCurrentValue(attrXmlName); // Set the actual attribute name, not a placeholder
}

return uiAttr;

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
    break;
}
}
return null;
}
//<End of snippet n. 1>