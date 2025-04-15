/*Allow blank strings for the text property input

The input-dialog for setting the text property was requiring the
string to be non-empty, or it would reject the input. That's not right
- it should be possible to set the text to blank (which is common for
text fields for example), since blank will clear the attribute.

This code was just cut & pasted from the similar code for inputting
id's where the id -should- be nonempty.

The code was also not handling the case where the user cancels the
dialog for custom string properties; we need a null check.

Change-Id:I31a2ae07b99ebc59d15b76f68c514d97294a736b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 5079b4c..8346d6e 100644

//Synthetic comment -- @@ -187,7 +187,7 @@
String oldText = node.getStringAttr(ANDROID_URI, ATTR_TEXT);
oldText = ensureValidString(oldText);
String newText = mRulesEngine.displayInput("New Text:", oldText, null);
                    if (newText != null) {
node.editXml("Change text", new PropertySettingNodeHandler(ANDROID_URI,
ATTR_TEXT, newText));
}
//Synthetic comment -- @@ -242,7 +242,9 @@
} else {
assert prop.isStringEdit();
// We've already received the value outside the undo block
                                    if (customValue != null) {
                                        n.setAttribute(ANDROID_URI, actionId, customValue);
                                    }
}
}
});







