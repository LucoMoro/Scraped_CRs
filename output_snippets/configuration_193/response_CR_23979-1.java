//<Beginning of snippet n. 0>
}
}

for (CanvasViewInfo childVi : getChildren()) {
    SimpleElement e2 = childVi.toSimpleElement();
    if (e2 != null) {
        // Handle unknown attributes here if necessary
    }
}
//<End of snippet n. 0>


//<Beginning of snippet n. 1>
public boolean commitDirtyAttributesToXml() {
    boolean result = false;
    HashMap<AttributeDescriptor, UiAttributeNode> attributeMap = getInternalUiAttributes();
    HashMap<String, UiAttributeNode> unknownAttributes = new HashMap<>();

    for (Entry<AttributeDescriptor, UiAttributeNode> entry : attributeMap.entrySet()) {
        UiAttributeNode uiAttr = entry.getValue();
        if (uiAttr.isDirty()) {
            result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
            uiAttr.setDirty(false);
        }
    }

    for (Entry<String, UiAttributeNode> entry : unknownAttributes.entrySet()) {
        String attrXmlName = entry.getKey();
        UiAttributeNode uiAttr = entry.getValue();
        
        if (uiAttr != null && uiAttr.isDirty()) {
            result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
            uiAttr.setDirty(false);
        } else {
            uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
            uiAttr.setCurrentValue(uiAttr.getCurrentValue());
        }
    }

    return result;
}

public UiAttributeNode addUnknownAttribute(String attrXmlName, String attrName, String attrNsUri) {
    // Preserves the original namespace conditions
    if ((attrNsUri == null && uiDesc.getNamespaceUri() == null) ||
        (attrNsUri != null && attrNsUri.equals(uiDesc.getNamespaceUri()))) {
        
        // Not all attributes are editable, ignore those which are not.
        if (uiAttr instanceof IUiSettableAttributeNode) {
            String current = uiAttr.getCurrentValue();
            // Only update (and mark as dirty) if the attribute did not have any
            // value or if the value was different.
            if (override || current == null || !current.equals(value)) {
                ((IUiSettableAttributeNode) uiAttr).setCurrentValue(value);
                uiAttr.setDirty(true);
                return uiAttr;
            }
        }
    }
    return null;
}

/**
* Utility method to retrieve the internal value of an attribute.
* <p/>
//<End of snippet n. 1>