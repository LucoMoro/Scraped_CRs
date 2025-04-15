/*Use Reference Chooser rather than String dialog for "Edit Text"

Fix 13880: Dialog opened by contextual properties menu item should be
the reference chooser.

Change-Id:I88d994271217c96985a0e18c84efc16a232c2bd4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 8346d6e..131bf7a 100644

//Synthetic comment -- @@ -186,7 +186,7 @@
} else if (fullActionId.equals(EDIT_TEXT_ID)) {
String oldText = node.getStringAttr(ANDROID_URI, ATTR_TEXT);
oldText = ensureValidString(oldText);
                    String newText = mRulesEngine.displayInput("New Text:", oldText, null);
if (newText != null) {
node.editXml("Change text", new PropertySettingNodeHandler(ANDROID_URI,
ATTR_TEXT, newText));







