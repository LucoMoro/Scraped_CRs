/*SDK: wrap NPE in getAttributeNS when editing XML files.

SDK Bug: 28266

Change-Id:I239bd44ba2c3fdb0193511f765b649f85d741066*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/OutlineLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/OutlineLabelProvider.java
//Synthetic comment -- index ea3f066..4ac3a1d 100644

//Synthetic comment -- @@ -52,7 +52,11 @@
String text = super.getText(element);
if (element instanceof Element) {
Element e = (Element) element;
            String id = e.getAttributeNS(ANDROID_URI, ATTR_ID);
if (id == null || id.length() == 0) {
id = e.getAttributeNS(ANDROID_URI, ATTR_NAME);
if (id == null || id.length() == 0) {
//Synthetic comment -- @@ -87,4 +91,4 @@
}
return text;
}
}
\ No newline at end of file







