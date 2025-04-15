/*ADT: Fix NPE in resource editor on Eclipse 3.6

SDK Bug: 2814467

The fix is a workaround around Eclipse issuehttps://bugs.eclipse.org/bugs/show_bug.cgi?id=318108Change-Id:I5684db244d5663ffa47407bd356a5bd233578612*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java
//Synthetic comment -- index 5dc8544..399b7c7 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

import org.w3c.dom.Element;

/**
//Synthetic comment -- @@ -80,11 +81,13 @@
if (desc != manifestDescriptors.getManifestElement() &&
desc != manifestDescriptors.getApplicationElement()) {
Element elem = (Element) getXmlNode();
                String attr = elem.getAttributeNS(SdkConstants.NS_RESOURCES,
                                                  AndroidManifestDescriptors.ANDROID_NAME_ATTR);
if (attr == null || attr.length() == 0) {
                    attr = elem.getAttributeNS(SdkConstants.NS_RESOURCES,
                                               AndroidManifestDescriptors.ANDROID_LABEL_ATTR);
}
if (attr != null && attr.length() > 0) {
return String.format("%1$s (%2$s)", attr, getDescriptor().getUiName());
//Synthetic comment -- @@ -94,5 +97,30 @@

return String.format("%1$s", getDescriptor().getUiName());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 9cce764..5a444d4 100644

//Synthetic comment -- @@ -204,22 +204,28 @@
// just using the UI name below.
Element elem = (Element) mXmlNode;

            String attr = elem.getAttributeNS(SdkConstants.NS_RESOURCES,
                                              AndroidManifestDescriptors.ANDROID_NAME_ATTR);
if (attr == null || attr.length() == 0) {
                attr = elem.getAttributeNS(SdkConstants.NS_RESOURCES,
                                           AndroidManifestDescriptors.ANDROID_LABEL_ATTR);
}
if (attr == null || attr.length() == 0) {
                attr = elem.getAttributeNS(SdkConstants.NS_RESOURCES,
                                           XmlDescriptors.PREF_KEY_ATTR);
}
if (attr == null || attr.length() == 0) {
                attr = elem.getAttribute(ResourcesDescriptors.NAME_ATTR);
}
if (attr == null || attr.length() == 0) {
                attr = elem.getAttributeNS(SdkConstants.NS_RESOURCES,
                                           LayoutDescriptors.ID_ATTR);

if (attr != null && attr.length() > 0) {
for (String prefix : ID_PREFIXES) {
//Synthetic comment -- @@ -237,6 +243,31 @@

return String.format("%1$s", mDescriptor.getUiName());
}

/**
* Computes a "breadcrumb trail" description for this node.







