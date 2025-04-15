/*Add preview support for GridViews

GridViews are another AdapterView subclass. This changeset adds
preview support for GridViews by modifying the ListView preview
support to also accommodate GridViews.

Change-Id:I5af0a4c7e270cfa23c609be316d01dfb188a051b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java
new file mode 100644
//Synthetic comment -- index 0000000..8f81977

//Synthetic comment -- @@ -0,0 +1,39 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index f88e7ab..282bdab 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
/** The attribute name in a <code>&lt;view class="..."&gt;</code> element. */
public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
//Synthetic comment -- @@ -180,6 +181,9 @@
/** The fully qualified class name of a TableLayout view */
public static final String FQCN_TABLE_LAYOUT = "android.widget.TableLayout"; //$NON-NLS-1$

/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 7383659..e6da86e 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.CALENDAR_VIEW;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;

import com.android.ide.common.rendering.LayoutLibrary;
//Synthetic comment -- @@ -408,22 +410,24 @@

/**
* For the given class, finds and returns the nearest super class which is a ListView
     * or an ExpandableListView, or returns null.
*
* @param clz the class of the view object
     * @return the fully qualified class name of the list view ancestor, or null if there
*         is no list view ancestor
*/
    public static String getListViewFqcn(Class<?> clz) {
String fqcn = clz.getName();
if (fqcn.endsWith(LIST_VIEW)) { // including EXPANDABLE_LIST_VIEW
return fqcn;
} else if (fqcn.startsWith(ANDROID_PKG_PREFIX)) {
return null;
}
Class<?> superClass = clz.getSuperclass();
if (superClass != null) {
            return getListViewFqcn(superClass);
} else {
// Should not happen; we would have encountered android.view.View first,
// and it should have been covered by the ANDROID_PKG_PREFIX case above.
//Synthetic comment -- @@ -477,7 +481,7 @@
// class name, otherwise return null. This is used to filter out other types
// of AdapterViews (such as Spinners) where we don't want to use the list item
// binding.
        String listFqcn = getListViewFqcn(viewObject.getClass());
if (listFqcn == null) {
return null;
}
//Synthetic comment -- @@ -491,7 +495,8 @@
return null;
}

        AdapterBinding binding = new AdapterBinding(12);
if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
binding.addItem(new DataBindingItem(LayoutMetadata.DEFAULT_EXPANDABLE_LIST_ITEM,
true /* isFramework */, 1));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index f188e90..93cee42 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;

import com.android.ide.common.api.IMenuCallback;
//Synthetic comment -- @@ -245,9 +246,10 @@
for (SelectionItem item : selection) {
UiViewElementNode node = item.getViewInfo().getUiViewNode();
String name = node.getDescriptor().getXmlLocalName();
            if (name.equals(LIST_VIEW) || name.equals(EXPANDABLE_LIST_VIEW)) {
mMenuManager.insertBefore(endId, new Separator());
                mMenuManager.insertBefore(endId, new ListViewTypeMenu(mCanvas));
return;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 07e11e6..67e6216 100644

//Synthetic comment -- @@ -16,7 +16,10 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;

import com.android.ide.common.rendering.api.AdapterBinding;
//Synthetic comment -- @@ -32,6 +35,7 @@
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Synthetic comment -- @@ -321,7 +325,22 @@
String footer = getProperty(editor, xmlNode, KEY_LV_FOOTER);
String layout = getProperty(editor, xmlNode, KEY_LV_ITEM);
if (layout != null || header != null || footer != null) {
                AdapterBinding binding = new AdapterBinding(12);

if (header != null) {
boolean isFramework = header.startsWith(ANDROID_LAYOUT_PREFIX);
//Synthetic comment -- @@ -345,7 +364,7 @@

binding.addItem(new DataBindingItem(layout, isFramework, 1));
} else if (viewObject != null) {
                    String listFqcn = ProjectCallback.getListViewFqcn(viewObject.getClass());
if (listFqcn != null) {
if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
binding.addItem(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index e522957..593648e 100644

//Synthetic comment -- @@ -48,15 +48,19 @@
public class ListViewTypeMenu extends SubmenuAction {
/** Associated canvas */
private final LayoutCanvas mCanvas;

/**
* Creates a "Preview List Content" menu
*
* @param canvas associated canvas
*/
    public ListViewTypeMenu(LayoutCanvas canvas) {
        super("Preview List Content");
mCanvas = canvas;
}

@Override
//Synthetic comment -- @@ -91,21 +95,22 @@
action = new SetListTypeAction("Multiple Choice List Item",
"simple_list_item_multiple_choice", //$NON-NLS-1$
selected);
            new Separator().fill(menu, -1);
            action = new SetListTypeAction("Simple Expandable List Item",
                    "simple_expandable_list_item_1", selected); //$NON-NLS-1$
            new ActionContributionItem(action).fill(menu, -1);
            action = new SetListTypeAction("Simple 2-Line Expandable List Item",
                    "simple_expandable_list_item_2", //$NON-NLS-1$
                    selected);
            new ActionContributionItem(action).fill(menu, -1);

            new Separator().fill(menu, -1);
            action = new PickLayoutAction("Choose Header...", KEY_LV_HEADER);
            new ActionContributionItem(action).fill(menu, -1);
            action = new PickLayoutAction("Choose Footer...", KEY_LV_FOOTER);
            new ActionContributionItem(action).fill(menu, -1);

} else {
// Should we just hide the menu item instead?
addDisabledMessageItem(







