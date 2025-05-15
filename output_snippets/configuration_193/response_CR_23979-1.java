//<Beginning of snippet n. 0>
}
}

for (CanvasViewInfo childVi : getChildren()) {
    SimpleElement e2 = childVi.toSimpleElement();
    if (e2 != null) {
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

    // Processing unknown attributes
    for (Entry<String, String> unknownAttribute : getUnknownAttributes().entrySet()) {
        String attrXmlName = unknownAttribute.getKey();
        String attrNsUri = unknownAttribute.getValue();
        UiAttributeNode unknownAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);

        if (unknownAttr != null) {
            unknownAttr.setCurrentValue(unknownAttribute.getValue());
            unknownAttr.setDirty(true);
            result |= commitAttributeToXml(unknownAttr, unknownAttr.getCurrentValue());
        }
    }

    return result;
}

// in which case we just create a new custom one.

uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
// FIXME: The will create the attribute, but not actually set the value on it...
if (uiAttr != null) {
    uiAttr.setCurrentValue(attrXmlName); // Setting current value for unknown attribute
}

return uiAttr;
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
            // has been modified, but not the underlying XML model
            uiAttr.setDirty(true);
            return uiAttr;
        }
    }

    // We found the attribute but it's not settable. Since attributes are
    // not duplicated, just abandon here.
    break;
}
}
}
return null;
}

/**
* Utility method to retrieve the internal value of an attribute.
* <p/>
//<End of snippet n. 1>