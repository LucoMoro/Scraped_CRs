/*SDK: wrap NPE in getAttributeNS when editing XML files.

SDK Bug: 28266

Change-Id:I239bd44ba2c3fdb0193511f765b649f85d741066*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/OutlineLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/OutlineLabelProvider.java
//Synthetic comment -- index ea3f066..f8333bb 100644

//Synthetic comment -- @@ -27,6 +27,7 @@

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeLabelProvider;
import org.w3c.dom.Element;

/** Label provider for the XML outlines and quick outlines: Use our own icons,
//Synthetic comment -- @@ -52,18 +53,18 @@
String text = super.getText(element);
if (element instanceof Element) {
Element e = (Element) element;
            String id = e.getAttributeNS(ANDROID_URI, ATTR_ID);
if (id == null || id.length() == 0) {
                id = e.getAttributeNS(ANDROID_URI, ATTR_NAME);
if (id == null || id.length() == 0) {
id = e.getAttribute(ATTR_NAME);
if (id == null || id.length() == 0) {
                        id = e.getAttributeNS(ANDROID_URI, ATTR_TEXT);
if (id != null && id.length() > 15) {
id = id.substring(0, 12) + "...";
}
if (id == null || id.length() == 0) {
                            id = e.getAttributeNS(ANDROID_URI, ATTR_SRC);
if (id != null && id.length() > 0) {
if (id.startsWith(DRAWABLE_PREFIX)) {
id = id.substring(DRAWABLE_PREFIX.length());
//Synthetic comment -- @@ -87,4 +88,20 @@
}
return text;
}
}
\ No newline at end of file







