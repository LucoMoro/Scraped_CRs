
//<Beginning of snippet n. 0>


}
}

        for (UiAttributeNode attr : uiNode.getUnknownUiAttributes()) {
            String value = attr.getCurrentValue();
            if (value != null && value.length() > 0) {
                AttributeDescriptor attrDesc = attr.getDescriptor();
                SimpleAttribute a = new SimpleAttribute(
                        attrDesc.getNamespaceUri(),
                        attrDesc.getXmlLocalName(),
                        value);
                e.addAttribute(a);
            }
        }

for (CanvasViewInfo childVi : getChildren()) {
SimpleElement e2 = childVi.toSimpleElement();
if (e2 != null) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
public boolean commitDirtyAttributesToXml() {
boolean result = false;

        for (UiAttributeNode uiAttr : getInternalUiAttributes().values()) {
if (uiAttr.isDirty()) {
result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
uiAttr.setDirty(false);
}
}

        for (UiAttributeNode uiAttr : getUnknownUiAttributes()) {
            if (uiAttr.isDirty()) {
                result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
                uiAttr.setDirty(false);
            }
        }

return result;
}

// in which case we just create a new custom one.

uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
            setUiAttrValue(value, override, uiAttr);
}

return uiAttr;
// Both NS URI must be either null or equal.
if ((attrNsUri == null && uiDesc.getNamespaceUri() == null) ||
(attrNsUri != null && attrNsUri.equals(uiDesc.getNamespaceUri()))) {
                    return setUiAttrValue(value, override, uiAttr);
}
}
}
return null;
}

    private UiAttributeNode setUiAttrValue(String value, boolean override,
            UiAttributeNode uiAttr) {
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

        return null;
    }

/**
* Utility method to retrieve the internal value of an attribute.
* <p/>

//<End of snippet n. 1>








