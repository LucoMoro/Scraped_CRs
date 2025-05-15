//<Beginning of snippet n. 0>

new file mode 100644


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$
public static final String ATTR_TAG = "tag";                        //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
/** The fully qualified class name of a TableLayout view */
public static final String FQCN_TABLE_LAYOUT = "android.widget.TableLayout"; //$NON-NLS-1$

/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

/** The fully qualified class name of a GridView view */
public static final String FQCN_GRID_VIEW = "android.widget.GridView"; //$NON-NLS-1$


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.CALENDAR_VIEW;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW; // Added GridView constant
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;


/**
* For the given class, finds and returns the nearest super class which is a ListView
* or an ExpandableListView or a GridView, or returns null.
*
* @param clz the class of the view object
* @return the fully qualified class name of the list view ancestor, or null if there
*         is no list view ancestor
*/
public static String getListViewFqcn(Class<?> clz) {
    String fqcn = clz.getName();
    if (fqcn.endsWith(LIST_VIEW) || fqcn.endsWith(GRID_VIEW)) { // including EXPANDABLE_LIST_VIEW
        return fqcn;
    } else if (fqcn.startsWith(ANDROID_PKG_PREFIX)) {
        return null;
    }
    Class<?> superClass = clz.getSuperclass();
    if (superClass != null) {
        return getListViewFqcn(superClass);
    } else {
        return null;
    }
}

AdapterBinding binding = new AdapterBinding(12);
if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
    binding.addItem(new DataBindingItem(LayoutMetadata.DEFAULT_EXPANDABLE_LIST_ITEM, true, 1));
} else if (listFqcn.endsWith(GRID_VIEW)) {
    binding.addItem(new DataBindingItem(LayoutMetadata.DEFAULT_GRID_ITEM, true, 1)); // Implemented binding for GridView
}

//<End of snippet n. 2>








//<Beginning of snippet n. 3>


public static final String VIEW_FRAGMENT = "fragment";    //$NON-NLS-1$

/**
* The XML name of the special {@code <requestFocus>} layout tag.
* A synthetic element with that name is created as part of the view descriptors list
* returned by {@link #getViewDescriptors()}.
*/
public static final String REQUEST_FOCUS = "requestFocus";//$NON-NLS-1$

/**
* The XML name of the special {@code <view>} layout tag. This is used to add generic
* views with a class attribute to specify the view.
* 
* @param knownLayouts  A list of all known layout view descriptors, used to find the
*   FrameLayout descriptor and extract its layout attributes.
*/
private ViewElementDescriptor createRequestFocus() {
    String xml_name = REQUEST_FOCUS;

    // Create the include descriptor
    return new ViewElementDescriptor(
            xml_name,  // xml_name
            xml_name, // ui_name
            xml_name, // "class name"; the GLE only treats this as an element tag
            "Requests focus for the parent element or one of its descendants", // tooltip
            null,  // sdk_url
            null,  // attributes
            null,  // layout attributes
            null,  // children
            false  /* mandatory */);
}

/**
* Creates and returns a new {@code <requestFocus>} descriptor.
*/

//<End of snippet n. 3>










//<Beginning of snippet n. 4>



import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW; // Added GridView support
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;

for (SelectionItem item : selection) {
    UiViewElementNode node = item.getViewInfo().getUiViewNode();
    String name = node.getDescriptor().getXmlLocalName();
    if (name.equals(LIST_VIEW) || name.equals(EXPANDABLE_LIST_VIEW) || name.equals(GRID_VIEW)) { // Added GridView support
        mMenuManager.insertBefore(endId, new Separator());
        mMenuManager.insertBefore(endId, new ListViewTypeMenu(mCanvas));
        return;
    } else if (name.equals(VIEW_FRAGMENT) && selection.size() == 1) {
        mMenuManager.insertBefore(endId, new Separator());
    }
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW; // added GridView import
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;

import com.android.ide.common.rendering.api.AdapterBinding;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

String footer = getProperty(editor, xmlNode, KEY_LV_FOOTER);
String layout = getProperty(editor, xmlNode, KEY_LV_ITEM);
if (layout != null || header != null || footer != null) {
    AdapterBinding binding = new AdapterBinding(12);

    if (header != null) {
        boolean isFramework = header.startsWith(ANDROID_LAYOUT_PREFIX);
        binding.addItem(new DataBindingItem(layout, isFramework, 1));
    } else if (viewObject != null) {
        String listFqcn = ProjectCallback.getListViewFqcn(viewObject.getClass());
        if (listFqcn != null) {
            if (listFqcn.endsWith(EXPANDABLE_LIST_VIEW)) {
                binding.addItem(new DataBindingItem(LayoutMetadata.DEFAULT_EXPANDABLE_LIST_ITEM, true, 1));
            } else if (listFqcn.endsWith(GRID_VIEW)) { // Added binding logic for GridView
                binding.addItem(new DataBindingItem(LayoutMetadata.DEFAULT_GRID_ITEM, true, 1));
            }
        }
    }
}

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


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
public void run() {
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
addDisabledMessageItem("Disable this menu item."); // Placeholder for a disabled message

}

//<End of snippet n. 6>
