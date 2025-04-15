/*Preserve unknown attributes

This changeset fixes the following bug:
17762: Dragging and Dropping a MapView in the outline of the layout
       editor wipes out the Maps Api Key

The root problem is that the MapView is an "unknown" view for the
tool; it does not have a metadata descriptor for it, so all of its
attributes are recorded as "unknown" attributes.

Unknown attributes are stored on the ui nodes, but not always applied.
This changeset adds iteration over the unknown attributes in a few
places where the descriptor attributes were processed.

Change-Id:Ib42d2d833712a857a09f56e588dfa85d2b960d28*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index c37ffe8..6766f9f 100755

//Synthetic comment -- @@ -474,6 +474,18 @@
}
}

for (CanvasViewInfo childVi : getChildren()) {
SimpleElement e2 = childVi.toSimpleElement();
if (e2 != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 6506b58..1d3bda0 100644

//Synthetic comment -- @@ -1539,15 +1539,21 @@
*/
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
return result;
}

//Synthetic comment -- @@ -1682,7 +1688,7 @@
// in which case we just create a new custom one.

uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
            // FIXME: The will create the attribute, but not actually set the value on it...
}

return uiAttr;
//Synthetic comment -- @@ -1709,24 +1715,7 @@
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
//Synthetic comment -- @@ -1734,6 +1723,24 @@
return null;
}

/**
* Utility method to retrieve the internal value of an attribute.
* <p/>







