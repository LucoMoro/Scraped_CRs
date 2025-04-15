/*Add preview support for GridViews

GridViews are another AdapterView subclass. This changeset adds
preview support for GridViews by modifying the ListView preview
support to also accommodate GridViews.

Change-Id:I5af0a4c7e270cfa23c609be316d01dfb188a051b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java
new file mode 100644
//Synthetic comment -- index 0000000..8f81977

//Synthetic comment -- @@ -0,0 +1,39 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NUM_COLUMNS;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
 * An {@link IViewRule} for android.widget.GridView
 */
public class GridViewRule extends BaseViewRule {

    @Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
        super.onCreate(node, parent, insertType);

        node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, getFillParentValueName());
        node.setAttribute(ANDROID_URI, ATTR_NUM_COLUMNS, "3"); //$NON-NLS-1$
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index dedd97b..cee6f97 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$
public static final String ATTR_TAG = "tag";                        //$NON-NLS-1$
    public static final String ATTR_NUM_COLUMNS = "numColumns";         //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
//Synthetic comment -- @@ -182,6 +183,9 @@
/** The fully qualified class name of a TableLayout view */
public static final String FQCN_TABLE_LAYOUT = "android.widget.TableLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a GridView view */
    public static final String FQCN_GRID_VIEW = "android.widget.GridView"; //$NON-NLS-1$

/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index f6877f1..a20cc6c 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.CALENDAR_VIEW;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;

//Synthetic comment -- @@ -439,22 +441,24 @@

/**
* For the given class, finds and returns the nearest super class which is a ListView
     * or an ExpandableListView or a GridView (which uses a list adapter), or returns null.
*
* @param clz the class of the view object
     * @return the fully qualified class name of the list ancestor, or null if there
*         is no list view ancestor
*/
    public static String getListAdapterViewFqcn(Class<?> clz) {
String fqcn = clz.getName();
if (fqcn.endsWith(LIST_VIEW)) { // including EXPANDABLE_LIST_VIEW
return fqcn;
        } else if (fqcn.equals(FQCN_GRID_VIEW)) {
                return fqcn;
} else if (fqcn.startsWith(ANDROID_PKG_PREFIX)) {
return null;
}
Class<?> superClass = clz.getSuperclass();
if (superClass != null) {
            return getListAdapterViewFqcn(superClass);
} else {
// Should not happen; we would have encountered android.view.View first,
// and it should have been covered by the ANDROID_PKG_PREFIX case above.
//Synthetic comment -- @@ -508,7 +512,7 @@
// class name, otherwise return null. This is used to filter out other types
// of AdapterViews (such as Spinners) where we don't want to use the list item
// binding.
        String listFqcn = getListAdapterViewFqcn(viewObject.getClass());
if (listFqcn == null) {
return null;
}
//Synthetic comment -- @@ -522,7 +526,8 @@
return null;
}

        int count = listFqcn.endsWith(GRID_VIEW) ? 24 : 12;
        AdapterBinding binding = new AdapterBinding(count);
if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
binding.addItem(new DataBindingItem(LayoutMetadata.DEFAULT_EXPANDABLE_LIST_ITEM,
true /* isFramework */, 1));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 893849b..90b95ee 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;

//Synthetic comment -- @@ -246,9 +247,10 @@
for (SelectionItem item : selection) {
UiViewElementNode node = item.getViewInfo().getUiViewNode();
String name = node.getDescriptor().getXmlLocalName();
            boolean isGrid = name.equals(GRID_VIEW);
            if (name.equals(LIST_VIEW) || name.equals(EXPANDABLE_LIST_VIEW) || isGrid) {
mMenuManager.insertBefore(endId, new Separator());
                mMenuManager.insertBefore(endId, new ListViewTypeMenu(mCanvas, isGrid));
return;
} else if (name.equals(VIEW_FRAGMENT) && selection.size() == 1) {
mMenuManager.insertBefore(endId, new Separator());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 7e1f88e..d2c4985 100644

//Synthetic comment -- @@ -16,7 +16,10 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NUM_COLUMNS;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;

import com.android.ide.common.rendering.api.AdapterBinding;
//Synthetic comment -- @@ -32,6 +35,7 @@
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Synthetic comment -- @@ -320,7 +324,22 @@
String footer = getProperty(editor, xmlNode, KEY_LV_FOOTER);
String layout = getProperty(editor, xmlNode, KEY_LV_ITEM);
if (layout != null || header != null || footer != null) {
                int count = 12;
                // If we're dealing with a grid view, multiply the list item count
                // by the number of columns to ensure we have enough items
                if (xmlNode instanceof Element && xmlNode.getNodeName().endsWith(GRID_VIEW)) {
                    Element element = (Element) xmlNode;
                    String columns = element.getAttributeNS(ANDROID_URI, ATTR_NUM_COLUMNS);
                    int multiplier = 2;
                    if (columns != null && columns.length() > 0) {
                        int c = Integer.parseInt(columns);
                        if (c >= 1 && c <= 10) {
                            multiplier = c;
                        }
                    }
                    count *= multiplier;
                }
                AdapterBinding binding = new AdapterBinding(count);

if (header != null) {
boolean isFramework = header.startsWith(ANDROID_LAYOUT_PREFIX);
//Synthetic comment -- @@ -344,7 +363,7 @@

binding.addItem(new DataBindingItem(layout, isFramework, 1));
} else if (viewObject != null) {
                    String listFqcn = ProjectCallback.getListAdapterViewFqcn(viewObject.getClass());
if (listFqcn != null) {
if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
binding.addItem(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index 5476c60..b90b869 100644

//Synthetic comment -- @@ -48,15 +48,19 @@
public class ListViewTypeMenu extends SubmenuAction {
/** Associated canvas */
private final LayoutCanvas mCanvas;
    /** When true, this menu is for a grid rather than a simple list */
    private boolean mGrid;

/**
* Creates a "Preview List Content" menu
*
* @param canvas associated canvas
     * @param isGrid whether the menu is for a grid rather than a list
*/
    public ListViewTypeMenu(LayoutCanvas canvas, boolean isGrid) {
        super(isGrid ? "Preview Grid Content" : "Preview List Content");
mCanvas = canvas;
        mGrid = isGrid;
}

@Override
//Synthetic comment -- @@ -91,21 +95,22 @@
action = new SetListTypeAction("Multiple Choice List Item",
"simple_list_item_multiple_choice", //$NON-NLS-1$
selected);
            if (!mGrid) {
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
            }
} else {
// Should we just hide the menu item instead?
addDisabledMessageItem(







