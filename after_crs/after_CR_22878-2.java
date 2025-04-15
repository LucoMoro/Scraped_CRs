/*Form editor up/down move accross siblings.

Up/down in the tree editor work at the XML level.
In a manifest, the tree block filters elements so
that only certain types are visible. Take this
into account when moving elements up/down so that
only siblings that match a given display filter
are used.

SDK Bugs: 2274556, 2274575

Change-Id:I81412b70f146a40169c47cba277fa3b3fceb7be3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java
//Synthetic comment -- index 93e5237..7b3f378 100644

//Synthetic comment -- @@ -187,7 +187,9 @@
* If the tree has a selection, move it up, either in the child list or as the last child
* of the previous parent.
*/
    public void doUp(
            final List<UiElementNode> uiNodes,
            final ElementDescriptor[] descriptorFilters) {
if (uiNodes == null || uiNodes.size() < 1) {
return;
}
//Synthetic comment -- @@ -201,7 +203,12 @@
public void run() {
for (int i = 0; i < uiNodes.size(); i++) {
UiElementNode uiNode = uiLastNode[0] = uiNodes.get(i);
                    doUpInternal(
                            uiNode,
                            descriptorFilters,
                            selectXmlNode,
                            uiSearchRoot,
                            false /*testOnly*/);
}
}
});
//Synthetic comment -- @@ -229,7 +236,9 @@
*
* @return True if the up action can be carried on *all* items.
*/
    public boolean canDoUp(
            List<UiElementNode> uiNodes,
            ElementDescriptor[] descriptorFilters) {
if (uiNodes == null || uiNodes.size() < 1) {
return false;
}
//Synthetic comment -- @@ -240,7 +249,12 @@
commitPendingXmlChanges();

for (int i = 0; i < uiNodes.size(); i++) {
            if (!doUpInternal(
                    uiNodes.get(i),
                    descriptorFilters,
                    selectXmlNode,
                    uiSearchRoot,
                    true /*testOnly*/)) {
return false;
}
}
//Synthetic comment -- @@ -250,8 +264,9 @@

private boolean doUpInternal(
UiElementNode uiNode,
            ElementDescriptor[] descriptorFilters,
            Node[] outSelectXmlNode,
            UiElementNode[] outUiSearchRoot,
boolean testOnly) {
// the node will move either up to its parent or grand-parent
outUiSearchRoot[0] = uiNode.getUiParent();
//Synthetic comment -- @@ -270,6 +285,14 @@
}

UiElementNode uiPrev = uiNode.getUiPreviousSibling();

        // Only accept a sibling that has an XML attached and
        // is part of the allowed descriptor filters.
        while (uiPrev != null &&
                (uiPrev.getXmlNode() == null || !matchDescFilter(descriptorFilters, uiPrev))) {
            uiPrev = uiPrev.getUiPreviousSibling();
        }

if (uiPrev != null && uiPrev.getXmlNode() != null) {
// This node is not the first one of the parent.
Node xmlPrev = uiPrev.getXmlNode();
//Synthetic comment -- @@ -321,13 +344,32 @@
return false;
}

    private boolean matchDescFilter(
            ElementDescriptor[] descriptorFilters,
            UiElementNode uiNode) {
        if (descriptorFilters == null || descriptorFilters.length < 1) {
            return true;
        }

        ElementDescriptor desc = uiNode.getDescriptor();

        for (ElementDescriptor filter : descriptorFilters) {
            if (filter.equals(desc)) {
                return true;
            }
        }
        return false;
    }

/**
* Called when the "Down" button is selected.
*
* If the tree has a selection, move it down, either in the same child list or as the
* first child of the next parent.
*/
    public void doDown(
            final List<UiElementNode> nodes,
            final ElementDescriptor[] descriptorFilters) {
if (nodes == null || nodes.size() < 1) {
return;
}
//Synthetic comment -- @@ -341,7 +383,12 @@
public void run() {
for (int i = nodes.size() - 1; i >= 0; i--) {
final UiElementNode node = uiLastNode[0] = nodes.get(i);
                    doDownInternal(
                            node,
                            descriptorFilters,
                            selectXmlNode,
                            uiSearchRoot,
                            false /*testOnly*/);
}
}
});
//Synthetic comment -- @@ -369,7 +416,9 @@
*
* @return True if the down action can be carried on *all* items.
*/
    public boolean canDoDown(
            List<UiElementNode> uiNodes,
            ElementDescriptor[] descriptorFilters) {
if (uiNodes == null || uiNodes.size() < 1) {
return false;
}
//Synthetic comment -- @@ -380,7 +429,12 @@
commitPendingXmlChanges();

for (int i = 0; i < uiNodes.size(); i++) {
            if (!doDownInternal(
                    uiNodes.get(i),
                    descriptorFilters,
                    selectXmlNode,
                    uiSearchRoot,
                    true /*testOnly*/)) {
return false;
}
}
//Synthetic comment -- @@ -389,9 +443,10 @@
}

private boolean doDownInternal(
            UiElementNode uiNode,
            ElementDescriptor[] descriptorFilters,
            Node[] outSelectXmlNode,
            UiElementNode[] outUiSearchRoot,
boolean testOnly) {
// the node will move either down to its parent or grand-parent
outUiSearchRoot[0] = uiNode.getUiParent();
//Synthetic comment -- @@ -411,6 +466,14 @@
}

UiElementNode uiNext = uiNode.getUiNextSibling();

        // Only accept a sibling that has an XML attached and
        // is part of the allowed descriptor filters.
        while (uiNext != null &&
                (uiNext.getXmlNode() == null || !matchDescFilter(descriptorFilters, uiNext))) {
            uiNext = uiNext.getUiNextSibling();
        }

if (uiNext != null && uiNext.getXmlNode() != null) {
// This node is not the last one of the parent.
Node xmlNext = uiNext.getXmlNode();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiTreeBlock.java
//Synthetic comment -- index 04ab5de..dd8cbd3 100644

//Synthetic comment -- @@ -95,7 +95,8 @@
*  manipulated by this tree view. In general this is the manifest UI node. */
private UiElementNode mUiRootNode;
/** The descriptor of the elements to be displayed as root in this tree view. All elements
     *  of the same type in the root will be displayed. Can be null or empty to mean everything
     *  can be displayed. */
private ElementDescriptor[] mDescriptorFilters;
/** The title for the master-detail part (displayed on the top "tab" on top of the tree) */
private String mTitle;
//Synthetic comment -- @@ -700,7 +701,7 @@
ISelection selection = mTreeViewer.getSelection();
if (!selection.isEmpty() && selection instanceof ITreeSelection) {
ArrayList<UiElementNode> selected = filterSelection((ITreeSelection) selection);
            mUiTreeActions.doUp(selected, mDescriptorFilters);
}
}

//Synthetic comment -- @@ -713,7 +714,7 @@
protected boolean canDoTreeUp(ISelection selection) {
if (!selection.isEmpty() && selection instanceof ITreeSelection) {
ArrayList<UiElementNode> selected = filterSelection((ITreeSelection) selection);
            return mUiTreeActions.canDoUp(selected, mDescriptorFilters);
}

return false;
//Synthetic comment -- @@ -729,7 +730,7 @@
ISelection selection = mTreeViewer.getSelection();
if (!selection.isEmpty() && selection instanceof ITreeSelection) {
ArrayList<UiElementNode> selected = filterSelection((ITreeSelection) selection);
            mUiTreeActions.doDown(selected, mDescriptorFilters);
}
}

//Synthetic comment -- @@ -742,7 +743,7 @@
protected boolean canDoTreeDown(ISelection selection) {
if (!selection.isEmpty() && selection instanceof ITreeSelection) {
ArrayList<UiElementNode> selected = filterSelection((ITreeSelection) selection);
            return mUiTreeActions.canDoDown(selected, mDescriptorFilters);
}

return false;







