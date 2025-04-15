/*ADT: Fix NPE in resource editor on Eclipse 3.6

SDK Bug: 2814467

The fix is a workaround around Eclipse issuehttps://bugs.eclipse.org/bugs/show_bug.cgi?id=318108Change-Id:I5684db244d5663ffa47407bd356a5bd233578612*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java
//Synthetic comment -- index 5dc8544..0b78a22 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
//Synthetic comment -- @@ -80,11 +81,13 @@
if (desc != manifestDescriptors.getManifestElement() &&
desc != manifestDescriptors.getApplicationElement()) {
Element elem = (Element) getXmlNode();
                String attr = _Element_getAttributeNS(elem,
                                    SdkConstants.NS_RESOURCES,
                                    AndroidManifestDescriptors.ANDROID_NAME_ATTR);
if (attr == null || attr.length() == 0) {
                    attr = _Element_getAttributeNS(elem,
                                    SdkConstants.NS_RESOURCES,
                                    AndroidManifestDescriptors.ANDROID_LABEL_ATTR);
}
if (attr != null && attr.length() > 0) {
return String.format("%1$s (%2$s)", attr, getDescriptor().getUiName());
//Synthetic comment -- @@ -94,5 +97,30 @@

return String.format("%1$s", getDescriptor().getUiName());
}

    /**
     * Retrieves an attribute value by local name and namespace URI.
     * <br>Per [<a href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>]
     * , applications must use the value <code>null</code> as the
     * <code>namespaceURI</code> parameter for methods if they wish to have
     * no namespace.
     * <p/>
     * Note: This is a wrapper around {@link Element#getAttributeNS(String, String)}.
     * In some versions of webtools, the getAttributeNS implementation crashes with an NPE.
     * This wrapper will return null instead.
     *
     * @see Element#getAttributeNS(String, String)
     * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=318108">https://bugs.eclipse.org/bugs/show_bug.cgi?id=318108</a>
     * @return The result from {@link Element#getAttributeNS(String, String)} or or an empty string.
     */
    private String _Element_getAttributeNS(Element element,
            String namespaceURI,
            String localName) {
        try {
            return element.getAttributeNS(namespaceURI, localName);
        } catch (Exception ignore) {
            return "";
        }
    }
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 9cce764..280f518 100644

//Synthetic comment -- @@ -204,22 +204,28 @@
// just using the UI name below.
Element elem = (Element) mXmlNode;

            String attr = _Element_getAttributeNS(elem,
                                SdkConstants.NS_RESOURCES,
                                AndroidManifestDescriptors.ANDROID_NAME_ATTR);
if (attr == null || attr.length() == 0) {
                attr = _Element_getAttributeNS(elem,
                                SdkConstants.NS_RESOURCES,
                                AndroidManifestDescriptors.ANDROID_LABEL_ATTR);
}
if (attr == null || attr.length() == 0) {
                attr = _Element_getAttributeNS(elem,
                                SdkConstants.NS_RESOURCES,
                                XmlDescriptors.PREF_KEY_ATTR);
}
if (attr == null || attr.length() == 0) {
                attr = _Element_getAttributeNS(elem,
                                null, // no namespace
                                ResourcesDescriptors.NAME_ATTR);
}
if (attr == null || attr.length() == 0) {
                attr = _Element_getAttributeNS(elem,
                                SdkConstants.NS_RESOURCES,
                                LayoutDescriptors.ID_ATTR);

if (attr != null && attr.length() > 0) {
for (String prefix : ID_PREFIXES) {
//Synthetic comment -- @@ -239,6 +245,31 @@
}

/**
     * Retrieves an attribute value by local name and namespace URI.
     * <br>Per [<a href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>]
     * , applications must use the value <code>null</code> as the
     * <code>namespaceURI</code> parameter for methods if they wish to have
     * no namespace.
     * <p/>
     * Note: This is a wrapper around {@link Element#getAttributeNS(String, String)}.
     * In some versions of webtools, the getAttributeNS implementation crashes with an NPE.
     * This wrapper will return null instead.
     *
     * @see Element#getAttributeNS(String, String)
     * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=318108">https://bugs.eclipse.org/bugs/show_bug.cgi?id=318108</a>
     * @return The result from {@link Element#getAttributeNS(String, String)} or an empty string.
     */
    private String _Element_getAttributeNS(Element element,
            String namespaceURI,
            String localName) {
        try {
            return element.getAttributeNS(namespaceURI, localName);
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
* Computes a "breadcrumb trail" description for this node.
* It will look something like "Manifest > Application > .myactivity (Activity) > Intent-Filter"
*







