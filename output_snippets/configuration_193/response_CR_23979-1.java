//<Beginning of snippet n. 0>
}
}

for (CanvasViewInfo childVi : getChildren()) {
    SimpleElement e2 = childVi.toSimpleElement();
    if (e2 != null) {
        HashMap<String, String> unknownAttributes = childVi.getUnknownAttributes();
        for (Entry<String, String> unknownEntry : unknownAttributes.entrySet()) {
            e2.setAttribute(unknownEntry.getKey(), unknownEntry.getValue());
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
        if (uiAttr.isDirty()) {
            result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
            uiAttr.setDirty(false);
        } else {
            String unknownAttrName = uiAttr.getUnknownName();
            if (unknownAttrName != null) {
                String unknownAttrValue = uiAttr.getCurrentValue();
                UiAttributeNode newUnknownAttr = addUnknownAttribute(unknownAttrName, unknownAttrName, uiAttr.getNamespaceUri());
                newUnknownAttr.setCurrentValue(unknownAttrValue); // Setting the value correctly
                result |= commitAttributeToXml(newUnknownAttr, unknownAttrValue);
            }
        }
    }

    return result;
}

// in which case we just create a new custom one.

uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);

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
}
return null;
}

/**
* Utility method to retrieve the internal value of an attribute.
* <p/>
//<End of snippet n. 1>