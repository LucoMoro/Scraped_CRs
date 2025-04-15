/*Fix Up/Down buttons in UiTreeBlock form editor.

In the manifest editor (and actually all other form
editors), it was possible move elements up/down
using the Up/Down buttons. However this allowed a
user to move an element in a parent that would not
accept that parent. The tree block would then not
display the element any more and a user would have
had to switch to the XML view to correct this.

This fix thus makes sure a parent will accept the
node being moved. The up/down buttons are also
grayed appropriately if the action isn't possible.

SDK Bugs: 2274556, 2274575

Change-Id:If1dd61f1260063e8ecb9c48330e6c6b2dc3c7228*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java
//Synthetic comment -- index 8ab25cb..e0f6959 100644

//Synthetic comment -- @@ -314,6 +314,26 @@
return mChildren.length > 0;
}

/** Sets the list of allowed children. */
public void setChildren(ElementDescriptor[] newChildren) {
mChildren = newChildren;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java
//Synthetic comment -- index c5fa670..93e5237 100644

//Synthetic comment -- @@ -187,84 +187,141 @@
* If the tree has a selection, move it up, either in the child list or as the last child
* of the previous parent.
*/
    public void doUp(final List<UiElementNode> nodes) {
        if (nodes == null || nodes.size() < 1) {
return;
}

        final Node[] select_xml_node = { null };
        UiElementNode last_node = null;
        UiElementNode search_root = null;

        for (int i = 0; i < nodes.size(); i++) {
            final UiElementNode node = last_node = nodes.get(i);

            // the node will move either up to its parent or grand-parent
            search_root = node.getUiParent();
            if (search_root != null && search_root.getUiParent() != null) {
                search_root = search_root.getUiParent();
            }

            commitPendingXmlChanges();
            getRootNode().getEditor().wrapEditXmlModel(new Runnable() {
                public void run() {
                    Node xml_node = node.getXmlNode();
                    if (xml_node != null) {
                        Node xml_parent = xml_node.getParentNode();
                        if (xml_parent != null) {
                            UiElementNode ui_prev = node.getUiPreviousSibling();
                            if (ui_prev != null && ui_prev.getXmlNode() != null) {
                                // This node is not the first one of the parent, so it can be
                                // removed and then inserted before its previous sibling.
                                // If the previous sibling can have children, though, then it
                                // is inserted at the end of the children list.
                                Node xml_prev = ui_prev.getXmlNode();
                                if (ui_prev.getDescriptor().hasChildren()) {
                                    xml_prev.appendChild(xml_parent.removeChild(xml_node));
                                    select_xml_node[0] = xml_node;
                                } else {
                                    xml_parent.insertBefore(
                                            xml_parent.removeChild(xml_node),
                                            xml_prev);
                                    select_xml_node[0] = xml_node;
                                }
                            } else if (!(xml_parent instanceof Document) &&
                                    xml_parent.getParentNode() != null &&
                                    !(xml_parent.getParentNode() instanceof Document)) {
                                // If the node is the first one of the child list of its
                                // parent, move it up in the hierarchy as previous sibling
                                // to the parent. This is only possible if the parent of the
                                // parent is not a document.
                                Node grand_parent = xml_parent.getParentNode();
                                grand_parent.insertBefore(xml_parent.removeChild(xml_node),
                                        xml_parent);
                                select_xml_node[0] = xml_node;
                            }
                        }
                    }
}
            });
        }

        assert last_node != null; // tell Eclipse this can't be null below

        if (select_xml_node[0] == null) {
// The XML node has not been moved, we can just select the same UI node
            selectUiNode(last_node);
} else {
// The XML node has moved. At this point the UI model has been reloaded
// and the XML node has been affected to a new UI node. Find that new UI
// node and select it.
            if (search_root == null) {
                search_root = last_node.getUiRoot();
}
            if (search_root != null) {
                selectUiNode(search_root.findXmlNode(select_xml_node[0]));
}
}
}

/**
* Called when the "Down" button is selected.
*
* If the tree has a selection, move it down, either in the same child list or as the
//Synthetic comment -- @@ -275,79 +332,140 @@
return;
}

        final Node[] select_xml_node = { null };
        UiElementNode last_node = null;
        UiElementNode search_root = null;

        for (int i = nodes.size() - 1; i >= 0; i--) {
            final UiElementNode node = last_node = nodes.get(i);
            // the node will move either down to its parent or grand-parent
            search_root = node.getUiParent();
            if (search_root != null && search_root.getUiParent() != null) {
                search_root = search_root.getUiParent();
            }

            commitPendingXmlChanges();
            getRootNode().getEditor().wrapEditXmlModel(new Runnable() {
                public void run() {
                    Node xml_node = node.getXmlNode();
                    if (xml_node != null) {
                        Node xml_parent = xml_node.getParentNode();
                        if (xml_parent != null) {
                            UiElementNode uiNext = node.getUiNextSibling();
                            if (uiNext != null && uiNext.getXmlNode() != null) {
                                // This node is not the last one of the parent, so it can be
                                // removed and then inserted after its next sibling.
                                // If the next sibling is a node that can have children, though,
                                // then the node is inserted as the first child.
                                Node xml_next = uiNext.getXmlNode();
                                if (uiNext.getDescriptor().hasChildren()) {
                                    // Note: insertBefore works as append if the ref node is
                                    // null, i.e. when the node doesn't have children yet.
                                    xml_next.insertBefore(xml_parent.removeChild(xml_node),
                                            xml_next.getFirstChild());
                                    select_xml_node[0] = xml_node;
                                } else {
                                    // Insert "before after next" ;-)
                                    xml_parent.insertBefore(xml_parent.removeChild(xml_node),
                                            xml_next.getNextSibling());
                                    select_xml_node[0] = xml_node;
                                }
                            } else if (!(xml_parent instanceof Document) &&
                                    xml_parent.getParentNode() != null &&
                                    !(xml_parent.getParentNode() instanceof Document)) {
                                // This node is the last node of its parent.
                                // If neither the parent nor the grandparent is a document,
                                // then the node can be insert right after the parent.
                                Node grand_parent = xml_parent.getParentNode();
                                grand_parent.insertBefore(xml_parent.removeChild(xml_node),
                                        xml_parent.getNextSibling());
                                select_xml_node[0] = xml_node;
                            }
                        }
                    }
}
            });
        }

        assert last_node != null; // tell Eclipse this can't be null below

        if (select_xml_node[0] == null) {
// The XML node has not been moved, we can just select the same UI node
            selectUiNode(last_node);
} else {
// The XML node has moved. At this point the UI model has been reloaded
// and the XML node has been affected to a new UI node. Find that new UI
// node and select it.
            if (search_root == null) {
                search_root = last_node.getUiRoot();
}
            if (search_root != null) {
                selectUiNode(search_root.findXmlNode(select_xml_node[0]));
}
}
}

//---------------------

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java
//Synthetic comment -- index 589429c..04ab5de 100644

//Synthetic comment -- @@ -594,8 +594,8 @@
*/
private void adjustTreeButtons(ISelection selection) {
mRemoveButton.setEnabled(!selection.isEmpty() && selection instanceof ITreeSelection);
        mUpButton.setEnabled(!selection.isEmpty() && selection instanceof ITreeSelection);
        mDownButton.setEnabled(!selection.isEmpty() && selection instanceof ITreeSelection);
}

/**
//Synthetic comment -- @@ -705,6 +705,21 @@
}

/**
* Called when the "Down" button is selected.
*
* If the tree has a selection, move it down, either in the same child list or as the
//Synthetic comment -- @@ -719,6 +734,21 @@
}

/**
* Commits the current managed form (the one associated with our master part).
* As a side effect, this will commit the current UiElementDetails page.
*/
//Synthetic comment -- @@ -739,21 +769,21 @@
}

@Override
    protected void registerPages(DetailsPart detailsPart) {
// Keep a reference on the details part (the super class doesn't provide a getter
// for it.)
        mDetailsPart = detailsPart;

// The page selection mechanism does not use pages registered by association with
// a node class. Instead it uses a custom details page provider that provides a
// new UiElementDetail instance for each node instance. A limit of 5 pages is
// then set (the value is arbitrary but should be reasonable) for the internal
// page book.
        detailsPart.setPageLimit(5);

final UiTreeBlock tree = this;

        detailsPart.setPageProvider(new IDetailsPageProvider() {
public IDetailsPage getPage(Object key) {
if (key instanceof UiElementNode) {
return new UiElementDetail(tree);







