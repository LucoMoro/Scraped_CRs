/*Prevent NPE in Property Sheet code

Change-Id:I38d5af59962ae8d0f3f41c5f238fd00d0b712e06*/
//Synthetic comment -- diff --git a/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/AbstractTextPropertyEditor.java b/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/AbstractTextPropertyEditor.java
//Synthetic comment -- index dd651af..8b07c4d 100644

//Synthetic comment -- @@ -317,6 +317,11 @@
* @return <code>true</code> if transfer was successful.
*/
private boolean toProperty(Property property) throws Exception {
String text = m_textControl.getText();
// change property only if text was changed
if (!m_currentText.equals(text)) {







