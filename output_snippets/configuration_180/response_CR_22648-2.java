//<Beginning of snippet n. 1>
public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$
public static final String ATTR_TAG = "tag";                        //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
/** The fully qualified class name of a TableLayout view */
public static final String FQCN_TABLE_LAYOUT = "android.widget.TableLayout"; //$NON-NLS-1$
/** The fully qualified class name of a GridView view */
public static final String FQCN_GRID_VIEW = "android.widget.GridView"; //$NON-NLS-1$
/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.CALENDAR_VIEW;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW; // added
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;

/**
* For the given class, finds and returns the nearest super class which is a ListView,
* a GridView, or an ExpandableListView, or returns null.
*
* @param clz the class of the view object
* @return the fully qualified class name of the list view ancestor, or null if there
*         is no list view ancestor
*/
public static String getListViewFqcn(Class<?> clz) {
    String fqcn = clz.getName();
    if (fqcn.endsWith(LIST_VIEW) || fqcn.endsWith(GRID_VIEW)) {
        return fqcn;
    } else if (fqcn.startsWith(ANDROID_PKG_PREFIX)) {
        return null;
    }
    Class<?> superClass = clz.getSuperclass();
    if (superClass != null) {
        return getListViewFqcn(superClass);
    }
    return null;
}
//<End of snippet n. 2>

//<Beginning of snippet n. 4>
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW; // added
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;

for (SelectionItem item : selection) {
    UiViewElementNode node = item.getViewInfo().getUiViewNode();
    String name = node.getDescriptor().getXmlLocalName();
    if (name.equals(LIST_VIEW) || name.equals(EXPANDABLE_LIST_VIEW) || name.equals(GRID_VIEW)) {
        mMenuManager.insertBefore(endId, new Separator());
        mMenuManager.insertBefore(endId, new ListViewTypeMenu(mCanvas));
        return;
    } else if (name.equals(VIEW_FRAGMENT) && selection.size() == 1) {
        mMenuManager.insertBefore(endId, new Separator());
    }
}
//<End of snippet n. 4>

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
        action = new SetListTypeAction("Simple Grid Item",
                "simple_grid_item", selected); //$NON-NLS-1$
        new ActionContributionItem(action).fill(menu, -1);

        new Separator().fill(menu, -1);
        action = new PickLayoutAction("Choose Header...", KEY_LV_HEADER);
        new ActionContributionItem(action).fill(menu, -1);
        action = new PickLayoutAction("Choose Footer...", KEY_LV_FOOTER);
        new ActionContributionItem(action).fill(menu, -1);
    }
}
//<End of snippet n. 6>