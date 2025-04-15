/*Fix live manipulation of <include> elements

This changeset fixes bugs related to dragging included views around in
the canvas:

First, the <include> tag would get rewritten to <null>. This happened
because the layout editor treats the FQCN and the XML node name as
equivalent, but in the case of the include tag the FQCN was set to
null instead of "include".

Second, the "layout" attribute would not get copied because it is NOT
in the Android namespace, and the code to copy attributes ended up
with an empty-string namespace which was not handled correctly.

Third, when copied the "layout" attribute would end up with the
namespace "ns:" because the code to create attribute nodes always
created namespaced attribute nodes rather than a plain attribute node
when the namespace is null.

Change-Id:Ibd34212517615aa8ec79abe14bca765cdca525f6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 500c8ba..2021533 100644

//Synthetic comment -- @@ -314,7 +314,7 @@
// Create the include descriptor
ViewElementDescriptor desc = new ViewElementDescriptor(xml_name,  // xml_name
xml_name, // ui_name
                VIEW_INCLUDE, // "class name"; the GLE only treats this as an element tag
"Lets you statically include XML layouts inside other XML layouts.",  // tooltip
null, // sdk_url
attributes.toArray(new AttributeDescriptor[attributes.size()]),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index a78da12..b57901f 100644

//Synthetic comment -- @@ -1370,10 +1370,16 @@
// Add or replace an attribute
Document doc = element.getOwnerDocument();
if (doc != null) {
                    Attr attr;
                    if (attrNsUri != null && attrNsUri.length() > 0) {
                        attr = doc.createAttributeNS(attrNsUri, attrLocalName);
                        attr.setPrefix(lookupNamespacePrefix(element, attrNsUri));
                        attrMap.setNamedItemNS(attr);
                    } else {
                        attr = doc.createAttribute(attrLocalName);
                        attrMap.setNamedItem(attr);
                    }
attr.setValue(newValue);
return true;
}
}
//Synthetic comment -- @@ -1529,8 +1535,11 @@
// Try with all internal attributes
UiAttributeNode uiAttr = setInternalAttrValue(
getInternalUiAttributes().values(), attrXmlName, attrNsUri, value, override);
        if (uiAttr != null) {
            return uiAttr;
        }

        // Look at existing unknown (a.k.a. custom) attributes
uiAttr = setInternalAttrValue(
getUnknownUiAttributes(), attrXmlName, attrNsUri, value, override);

//Synthetic comment -- @@ -1539,6 +1548,7 @@
// in which case we just create a new custom one.

uiAttr = addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);
            // FIXME: The will create the attribute, but not actually set the value on it...
}

return uiAttr;
//Synthetic comment -- @@ -1550,6 +1560,14 @@
String attrNsUri,
String value,
boolean override) {

        // For namespace less attributes (like the "layout" attribute of an <include> tag
        // we may be passed "" as the namespace (during an attribute copy), and it
        // should really be null instead.
        if (attrNsUri != null && attrNsUri.length() == 0) {
            attrNsUri = null;
        }

for (UiAttributeNode uiAttr : attributes) {
AttributeDescriptor uiDesc = uiAttr.getDescriptor();








