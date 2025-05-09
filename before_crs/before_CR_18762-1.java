/*Fix XML editing whitespace handling

This changeset fixes three problems related to the way whitespace is
handled by the visual editor.

(1) The editor would insert a newline text node after a newly inserted
element, but this element was appended rather than inserted
immediately after the element, which meant that if you inserted your
element anywhere in the *middle* of the child list, you would
accumulate blank lines at the end of the parent's element and have no
separator before the current element.

The fix is simple - use insertBefore() rather than appendChild() when
inserting the new node.

(2) On deletion, no XML text nodes were deleted. This means that if
you inserted 4 elements, then deleted them, you would end up with 4
blank text nodes.

The fix here is to look at the sibling when we are about to delete,
and if it looks like a pure whitespace node, remove it as well.

(3) When nested content (like a LinearLayout) was inserted, there
would be no newline inserted before the child, so that first element
would end up on the same line as the parent.

The fix here is to detect this scenario (when we are inserting an
element into an element that has no children) and insert a newline
*before* the element (as well as after, as is the case for all newly
inserted elements).

Longer term we should do more advanced things with formatting, such as
indenting nested content, and making sure that when we insert and
remove whitespace nodes we always adjust the indentation
correctly. But for now, this changeset makes the XML generated by the
editor much more readable and consistent.
Change-Id:I2e65152568092a775074a606e3f5e54c680611c6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 55cc90b..1d9cacf 100644

//Synthetic comment -- @@ -833,11 +833,20 @@
xmlNextSibling = uiNextSibling.getXmlNode();
}

parentXmlNode.insertBefore(mXmlNode, xmlNextSibling);

// Insert a separator after the tag, to make it easier to read
        Text sep = doc.createTextNode("\n");
        parentXmlNode.appendChild(sep);

// Set all initial attributes in the XML node if they are not empty.
// Iterate on the descriptor list to get the desired order and then use the
//Synthetic comment -- @@ -881,8 +890,16 @@
if (xml_parent == null) {
xml_parent = getXmlDocument();
}
old_xml_node = xml_parent.removeChild(old_xml_node);

invokeUiUpdateListeners(UiUpdateState.DELETED);
return old_xml_node;
}
//Synthetic comment -- @@ -1227,6 +1244,7 @@
* @since GLE1
* @deprecated Used by GLE1. Should be deprecated for GLE2.
*/
public void setEditData(Object data) {
mEditData = data;
}







