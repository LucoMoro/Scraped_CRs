//<Beginning of snippet n. 0>
for (CanvasViewInfo childVi : getChildren()) {
    SimpleElement e2 = childVi.toSimpleElement();
    if (e2 != null) {
        Map<String, String> unknownAttributes = childVi.getUnknownAttributes();
        for (Map.Entry<String, String> unknownEntry : unknownAttributes.entrySet()) {
            e2.addUnknownAttribute(unknownEntry.getKey(), unknownEntry.getValue());
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

    // Commit unknown attributes after known attributes
    Map<String, String> unknownAttributes = getUnknownAttributes(); // Assumed to be implemented.
    for (Map.Entry<String, String> unknownEntry : unknownAttributes.entrySet()) {
        String attrName = unknownEntry.getKey();
        String attrValue = unknownEntry.getValue();
        
        // Commit each unknown attribute value
        result |= commitUnknownAttributeToXml(attrName, attrValue); // Assume this method is defined
    }

    return result;
}

private UiAttributeNode addUnknownAttribute(String attrXmlName, String attrNsUri) {
    UiAttributeNode uiAttr = createUnknownAttribute(attrXmlName, attrNsUri); // Assume this method is defined
    if (uiAttr != null) {
        String currentValue = uiAttr.getCurrentValue();
        if (currentValue == null) {
            uiAttr.setCurrentValue(attrXmlName); // Set value on creation
        }
        return uiAttr;
    }
    return null;
}

// Both NS URI must be either null or equal.
if ((attrNsUri == null && uiDesc.getNamespaceUri() == null) ||
    (attrNsUri != null && attrNsUri.equals(uiDesc.getNamespaceUri()))) {

    // Not all attributes are editable, ignore those which are not.
    if (uiAttr instanceof IUiSettableAttributeNode) {
        String current = uiAttr.getCurrentValue();
        // Only update (and mark as dirty) if the attribute did not have any
        // value or if the value was different.
        if (override || current == null || !current.equals(value)) {
            ((IUiSettableAttributeNode) uiAttr).setCurrentValue(value);
            // mark the attribute as dirty since their internal content
            // as been modified, but not the underlying XML model
            uiAttr.setDirty(true);
            return uiAttr;
        }
    }
    // We found the attribute but it's not settable. Since attributes are
    // not duplicated, just abandon here.
    break;
}
}
return null;
}

/**
* Utility method to retrieve the internal value of an attribute.
* <p/>
//<End of snippet n. 1>