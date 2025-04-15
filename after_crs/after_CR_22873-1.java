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

    /**
     * Checks whether this descriptor can accept the given descriptor type
     * as a direct child.
     *
     * @return True if this descriptor can accept children of the given descriptor type.
     *   False if not accepted, no children allowed, or target is null.
     */
    public boolean acceptChild(ElementDescriptor target) {
        if (target != null && mChildren.length > 0) {
            String targetXmlName = target.getXmlName();
            for (ElementDescriptor child : mChildren) {
                if (child.getXmlName().equals(targetXmlName)) {
                    return true;
                }
            }
        }

        return false;
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
    public void doUp(final List<UiElementNode> uiNodes) {
        if (uiNodes == null || uiNodes.size() < 1) {
return;
}

        final Node[]          selectXmlNode = { null };
        final UiElementNode[] uiLastNode    = { null };
        final UiElementNode[] uiSearchRoot  = { null };

        commitPendingXmlChanges();
        getRootNode().getEditor().wrapEditXmlModel(new Runnable() {
            public void run() {
                for (int i = 0; i < uiNodes.size(); i++) {
                    UiElementNode uiNode = uiLastNode[0] = uiNodes.get(i);
                    doUpInternal(uiNode, selectXmlNode, uiSearchRoot, false /*testOnly*/);
}
            }
        });

        assert uiLastNode[0] != null; // tell Eclipse this can't be null below

        if (selectXmlNode[0] == null) {
// The XML node has not been moved, we can just select the same UI node
            selectUiNode(uiLastNode[0]);
} else {
// The XML node has moved. At this point the UI model has been reloaded
// and the XML node has been affected to a new UI node. Find that new UI
// node and select it.
            if (uiSearchRoot[0] == null) {
                uiSearchRoot[0] = uiLastNode[0].getUiRoot();
}
            if (uiSearchRoot[0] != null) {
                selectUiNode(uiSearchRoot[0].findXmlNode(selectXmlNode[0]));
}
}
}

/**
     * Checks whether the "up" action can be performed on all items.
     *
     * @return True if the up action can be carried on *all* items.
     */
    public boolean canDoUp(final List<UiElementNode> uiNodes) {
        if (uiNodes == null || uiNodes.size() < 1) {
            return false;
        }

        final Node[]          selectXmlNode = { null };
        final UiElementNode[] uiSearchRoot  = { null };

        commitPendingXmlChanges();

        for (int i = 0; i < uiNodes.size(); i++) {
            if (!doUpInternal(uiNodes.get(i), selectXmlNode, uiSearchRoot, true /*testOnly*/)) {
                return false;
            }
        }

        return true;
    }

    private boolean doUpInternal(
            UiElementNode uiNode,
            final Node[] outSelectXmlNode,
            final UiElementNode[] outUiSearchRoot,
            boolean testOnly) {
        // the node will move either up to its parent or grand-parent
        outUiSearchRoot[0] = uiNode.getUiParent();
        if (outUiSearchRoot[0] != null && outUiSearchRoot[0].getUiParent() != null) {
            outUiSearchRoot[0] = outUiSearchRoot[0].getUiParent();
        }
        Node xmlNode = uiNode.getXmlNode();
        ElementDescriptor nodeDesc = uiNode.getDescriptor();
        if (xmlNode == null || nodeDesc == null) {
            return false;
        }
        UiElementNode uiParentNode = uiNode.getUiParent();
        Node xmlParent = uiParentNode == null ? null : uiParentNode.getXmlNode();
        if (xmlParent == null) {
            return false;
        }

        UiElementNode uiPrev = uiNode.getUiPreviousSibling();
        if (uiPrev != null && uiPrev.getXmlNode() != null) {
            // This node is not the first one of the parent.
            Node xmlPrev = uiPrev.getXmlNode();
            if (uiPrev.getDescriptor().acceptChild(nodeDesc)) {
                // If the previous sibling can accept this child, then it
                // is inserted at the end of the children list.
                if (testOnly) {
                    return true;
                }
                xmlPrev.appendChild(xmlParent.removeChild(xmlNode));
                outSelectXmlNode[0] = xmlNode;
            } else {
                // This node is not the first one of the parent, so it can be
                // removed and then inserted before its previous sibling.
                if (testOnly) {
                    return true;
                }
                xmlParent.insertBefore(
                        xmlParent.removeChild(xmlNode),
                        xmlPrev);
                outSelectXmlNode[0] = xmlNode;
            }
        } else if (uiParentNode != null && !(xmlParent instanceof Document)) {
            UiElementNode uiGrandParent = uiParentNode.getUiParent();
            Node xmlGrandParent = uiGrandParent == null ? null : uiGrandParent.getXmlNode();
            ElementDescriptor grandDesc =
                uiGrandParent == null ? null : uiGrandParent.getDescriptor();

            if (xmlGrandParent != null &&
                    !(xmlGrandParent instanceof Document) &&
                    grandDesc != null &&
                    grandDesc.acceptChild(nodeDesc)) {
                // If the node is the first one of the child list of its
                // parent, move it up in the hierarchy as previous sibling
                // to the parent. This is only possible if the parent of the
                // parent is not a document.
                // The parent node must actually accept this kind of child.

                if (testOnly) {
                    return true;
                }
                xmlGrandParent.insertBefore(
                        xmlParent.removeChild(xmlNode),
                        xmlParent);
                outSelectXmlNode[0] = xmlNode;
            }
        }

        return false;
    }

    /**
* Called when the "Down" button is selected.
*
* If the tree has a selection, move it down, either in the same child list or as the
//Synthetic comment -- @@ -275,79 +332,140 @@
return;
}

        final Node[]          selectXmlNode = { null };
        final UiElementNode[] uiLastNode    = { null };
        final UiElementNode[] uiSearchRoot  = { null };

        commitPendingXmlChanges();
        getRootNode().getEditor().wrapEditXmlModel(new Runnable() {
            public void run() {
                for (int i = nodes.size() - 1; i >= 0; i--) {
                    final UiElementNode node = uiLastNode[0] = nodes.get(i);
                    doDownInternal(node, selectXmlNode, uiSearchRoot, false /*testOnly*/);
}
            }
        });

        assert uiLastNode[0] != null; // tell Eclipse this can't be null below

        if (selectXmlNode[0] == null) {
// The XML node has not been moved, we can just select the same UI node
            selectUiNode(uiLastNode[0]);
} else {
// The XML node has moved. At this point the UI model has been reloaded
// and the XML node has been affected to a new UI node. Find that new UI
// node and select it.
            if (uiSearchRoot[0] == null) {
                uiSearchRoot[0] = uiLastNode[0].getUiRoot();
}
            if (uiSearchRoot[0] != null) {
                selectUiNode(uiSearchRoot[0].findXmlNode(selectXmlNode[0]));
}
}
}

    /**
     * Checks whether the "down" action can be performed on all items.
     *
     * @return True if the down action can be carried on *all* items.
     */
    public boolean canDoDown(final List<UiElementNode> uiNodes) {
        if (uiNodes == null || uiNodes.size() < 1) {
            return false;
        }

        final Node[]          selectXmlNode = { null };
        final UiElementNode[] uiSearchRoot  = { null };

        commitPendingXmlChanges();

        for (int i = 0; i < uiNodes.size(); i++) {
            if (!doDownInternal(uiNodes.get(i), selectXmlNode, uiSearchRoot, true /*testOnly*/)) {
                return false;
            }
        }

        return true;
    }

    private boolean doDownInternal(
            final UiElementNode uiNode,
            final Node[] outSelectXmlNode,
            final UiElementNode[] outUiSearchRoot,
            boolean testOnly) {
        // the node will move either down to its parent or grand-parent
        outUiSearchRoot[0] = uiNode.getUiParent();
        if (outUiSearchRoot[0] != null && outUiSearchRoot[0].getUiParent() != null) {
            outUiSearchRoot[0] = outUiSearchRoot[0].getUiParent();
        }

        Node xmlNode = uiNode.getXmlNode();
        ElementDescriptor nodeDesc = uiNode.getDescriptor();
        if (xmlNode == null || nodeDesc == null) {
            return false;
        }
        UiElementNode uiParentNode = uiNode.getUiParent();
        Node xmlParent = uiParentNode == null ? null : uiParentNode.getXmlNode();
        if (xmlParent == null) {
            return false;
        }

        UiElementNode uiNext = uiNode.getUiNextSibling();
        if (uiNext != null && uiNext.getXmlNode() != null) {
            // This node is not the last one of the parent.
            Node xmlNext = uiNext.getXmlNode();
            // If the next sibling is a node that can have children, though,
            // then the node is inserted as the first child.
            if (uiNext.getDescriptor().acceptChild(nodeDesc)) {
                if (testOnly) {
                    return true;
                }
                // Note: insertBefore works as append if the ref node is
                // null, i.e. when the node doesn't have children yet.
                xmlNext.insertBefore(
                        xmlParent.removeChild(xmlNode),
                        xmlNext.getFirstChild());
                outSelectXmlNode[0] = xmlNode;
            } else {
                // This node is not the last one of the parent, so it can be
                // removed and then inserted after its next sibling.

                if (testOnly) {
                    return true;
                }
                // Insert "before after next" ;-)
                xmlParent.insertBefore(
                        xmlParent.removeChild(xmlNode),
                        xmlNext.getNextSibling());
                outSelectXmlNode[0] = xmlNode;
            }
        } else if (uiParentNode != null && !(xmlParent instanceof Document)) {
            UiElementNode uiGrandParent = uiParentNode.getUiParent();
            Node xmlGrandParent = uiGrandParent == null ? null : uiGrandParent.getXmlNode();
            ElementDescriptor grandDesc =
                uiGrandParent == null ? null : uiGrandParent.getDescriptor();

            if (xmlGrandParent != null &&
                    !(xmlGrandParent instanceof Document) &&
                    grandDesc != null &&
                    grandDesc.acceptChild(nodeDesc)) {
                // This node is the last node of its parent.
                // If neither the parent nor the grandparent is a document,
                // then the node can be inserted right after the parent.
                // The parent node must actually accept this kind of child.
                if (testOnly) {
                    return true;
                }
                xmlGrandParent.insertBefore(
                        xmlParent.removeChild(xmlNode),
                        xmlParent.getNextSibling());
                outSelectXmlNode[0] = xmlNode;
            }
        }

        return false;
    }

//---------------------

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java
//Synthetic comment -- index 589429c..04ab5de 100644

//Synthetic comment -- @@ -594,8 +594,8 @@
*/
private void adjustTreeButtons(ISelection selection) {
mRemoveButton.setEnabled(!selection.isEmpty() && selection instanceof ITreeSelection);
        mUpButton.setEnabled(canDoTreeUp(selection));
        mDownButton.setEnabled(canDoTreeDown(selection));
}

/**
//Synthetic comment -- @@ -705,6 +705,21 @@
}

/**
     * Checks whether the "up" action can be done on the current selection.
     *
     * @param selection The current tree selection.
     * @return True if all the selected nodes can be moved up.
     */
    protected boolean canDoTreeUp(ISelection selection) {
        if (!selection.isEmpty() && selection instanceof ITreeSelection) {
            ArrayList<UiElementNode> selected = filterSelection((ITreeSelection) selection);
            return mUiTreeActions.canDoUp(selected);
        }

        return false;
    }

    /**
* Called when the "Down" button is selected.
*
* If the tree has a selection, move it down, either in the same child list or as the
//Synthetic comment -- @@ -719,6 +734,21 @@
}

/**
     * Checks whether the "down" action can be done on the current selection.
     *
     * @param selection The current tree selection.
     * @return True if all the selected nodes can be moved down.
     */
    protected boolean canDoTreeDown(ISelection selection) {
        if (!selection.isEmpty() && selection instanceof ITreeSelection) {
            ArrayList<UiElementNode> selected = filterSelection((ITreeSelection) selection);
            return mUiTreeActions.canDoDown(selected);
        }

        return false;
    }

    /**
* Commits the current managed form (the one associated with our master part).
* As a side effect, this will commit the current UiElementDetails page.
*/
//Synthetic comment -- @@ -739,21 +769,21 @@
}

@Override
    protected void registerPages(DetailsPart inDetailsPart) {
// Keep a reference on the details part (the super class doesn't provide a getter
// for it.)
        mDetailsPart = inDetailsPart;

// The page selection mechanism does not use pages registered by association with
// a node class. Instead it uses a custom details page provider that provides a
// new UiElementDetail instance for each node instance. A limit of 5 pages is
// then set (the value is arbitrary but should be reasonable) for the internal
// page book.
        inDetailsPart.setPageLimit(5);

final UiTreeBlock tree = this;

        inDetailsPart.setPageProvider(new IDetailsPageProvider() {
public IDetailsPage getPage(Object key) {
if (key instanceof UiElementNode) {
return new UiElementDetail(tree);







