/*Layout Actions bar fixes

This changeset fixes a couple of layout actions bar issues:

(1) Refresh the actions bar after running one of the layout
    actions. This for example fixes the issue that if you click to
    toggle the layout orientation then the baseline button will appear
    for horizontal layouts.

(2) Fix an issue with the lazy-initialization of dropdown menus; they
    weren't actually initialized lazily because the code to determine
    whether a choice list should be a dropdown or a radio group would
    cause initialization.

(3) Fix layout gravity on RelativeLayouts; it was reading/writing the
    attribute "layout_gravity" instead of "gravity".

Change-Id:Ic41158257b3938a2e6daa8714dcd15d6bf21fa2f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/MenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/MenuAction.java
//Synthetic comment -- index e54ae36..3e912f8 100755

//Synthetic comment -- @@ -121,7 +121,7 @@
return action;
}

    public static OrderedChoices createChoices(String id, String title, String groupId,
IMenuCallback callback, List<String> titles, List<URL> iconUrls, List<String> ids,
String current, URL iconUrl, int sortPriority) {
OrderedChoices choices = new OrderedChoices(id, title, groupId, callback, titles, iconUrls,
//Synthetic comment -- @@ -131,7 +131,7 @@
return choices;
}

    public static OrderedChoices createChoices(String id, String title, String groupId,
IMenuCallback callback, ChoiceProvider provider,
String current, URL iconUrl, int sortPriority) {
OrderedChoices choices = new DelayedOrderedChoices(id, title, groupId, callback,
//Synthetic comment -- @@ -437,6 +437,7 @@
protected List<String> mTitles;
protected List<URL> mIconUrls;
protected List<String> mIds;
        private boolean mRadio;

/**
* One or more id for the checked choice(s) that will be check marked.
//Synthetic comment -- @@ -468,6 +469,26 @@
public String getCurrent() {
return mCurrent;
}

        /**
         * Set whether this choice list is best visualized as a radio group (instead of a
         * dropdown)
         *
         * @param radio true if this choice list should be visualized as a radio group
         */
        public void setRadio(boolean radio) {
            mRadio = radio;
        }

        /**
         * Returns true if this choice list is best visualized as a radio group (instead
         * of a dropdown)
         *
         * @return true if this choice list should be visualized as a radio group
         */
        public boolean isRadio() {
            return mRadio;
        }
}

/** Provides the set of choices associated with an {@link OrderedChoices} object








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index a33594c..bd6c806 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
//Synthetic comment -- @@ -113,13 +112,14 @@

// Both LinearLayout and RelativeLayout have a gravity (but RelativeLayout applies it
// to the parent whereas for LinearLayout it's on the children)
    protected MenuAction createGravityAction(final List<? extends INode> targets, final
            String attributeName) {
if (targets != null && targets.size() > 0) {
final INode first = targets.get(0);
ChoiceProvider provider = new ChoiceProvider() {
public void addChoices(List<String> titles, List<URL> iconUrls,
List<String> ids) {
                    IAttributeInfo info = first.getAttributeInfo(ANDROID_URI, attributeName);
if (info != null) {
// Generate list of possible gravity value constants
assert IAttributeInfo.Format.FLAG.in(info.getFormats());
//Synthetic comment -- @@ -134,9 +134,9 @@
return MenuAction.createChoices("_gravity", "Change Gravity", //$NON-NLS-1$
null,
new PropertyCallback(targets, "Change Gravity", ANDROID_URI,
                            attributeName),
provider,
                    first.getStringAttr(ANDROID_URI, attributeName), ICON_GRAVITY,
43);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index 0f8fa18..655eee2 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -154,7 +156,7 @@
actions.add(MenuAction.createSeparator(25));
actions.add(createMarginAction(parentNode, children));
if (children.size() > 0) {
            actions.add(createGravityAction(children, ATTR_LAYOUT_GRAVITY));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index ef7bba7..ee5a8c9 100644

//Synthetic comment -- @@ -143,6 +143,7 @@
// like fill_parent for API 8
public static final String VALUE_MATCH_PARENT = "match_parent"; //$NON-NLS-1$

    public static final String ATTR_GRAVITY = "gravity"; //$NON-NLS-1$
public static final String ATTR_WEIGHT_SUM = "weightSum"; //$NON-NLS-1$
public static final String ATTR_BASELINE_ALIGNED = "baselineAligned"; //$NON-NLS-1$
public static String ATTR_ORIENTATION = "orientation"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 3efa220..1e2d0e3 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_BASELINE_ALIGNED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
//Synthetic comment -- @@ -41,6 +42,7 @@
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IViewMetadata.FillPreference;
import com.android.ide.common.api.MenuAction.OrderedChoices;

import java.net.URL;
import java.util.ArrayList;
//Synthetic comment -- @@ -131,7 +133,7 @@
final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);
if (supportsOrientation()) {
            OrderedChoices action = MenuAction.createChoices(
ACTION_ORIENTATION, "Orientation",  //$NON-NLS-1$
null,
new PropertyCallback(Collections.singletonList(parentNode),
//Synthetic comment -- @@ -143,7 +145,9 @@
getCurrentOrientation(parentNode),
null /* icon */,
-10
            );
            action.setRadio(true);
            actions.add(action);
}
if (!isVertical(parentNode)) {
String current = parentNode.getStringAttr(ANDROID_URI, ATTR_BASELINE_ALIGNED);
//Synthetic comment -- @@ -164,7 +168,7 @@
actions.add(createMarginAction(parentNode, children));

// Gravity
            actions.add(createGravityAction(children, ATTR_LAYOUT_GRAVITY));

// Weights
IMenuCallback actionCallback = new IMenuCallback() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index b7160ed..33fe202 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ABOVE;
//Synthetic comment -- @@ -659,7 +660,8 @@
final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);

        actions.add(createGravityAction(Collections.<INode>singletonList(parentNode),
                ATTR_GRAVITY));
actions.add(MenuAction.createSeparator(25));
actions.add(createMarginAction(parentNode, children));
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index e64004d..5f2450c 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//Synthetic comment -- @@ -145,8 +146,7 @@

if (action instanceof MenuAction.OrderedChoices) {
final MenuAction.OrderedChoices choices = (OrderedChoices) action;
                    if (!choices.isRadio()) {
addDropdown(choices);
} else {
addSeparator(mLayoutToolBar);
//Synthetic comment -- @@ -191,6 +191,7 @@
public void widgetSelected(SelectionEvent e) {
toggle.getCallback().action(toggle, toggle.getId(),
button.getSelection());
                updateSelection();
}
});
if (toggle.isChecked()) {
//Synthetic comment -- @@ -214,15 +215,16 @@
@Override
public void widgetSelected(SelectionEvent e) {
menuAction.getCallback().action(menuAction, menuAction.getId(), false);
                updateSelection();
}
});
}

private void addRadio(final MenuAction.OrderedChoices choices) {
        List<URL> icons = choices.getIconUrls();
        List<String> titles = choices.getTitles();
        List<String> ids = choices.getIds();
        String current = choices.getCurrent() != null ? choices.getCurrent() : ""; //$NON-NLS-1$

assert icons != null;
assert icons.size() == titles.size();
//Synthetic comment -- @@ -239,6 +241,7 @@
public void widgetSelected(SelectionEvent e) {
if (item.getSelection()) {
choices.getCallback().action(choices, id, null);
                        updateSelection();
}
}
});
//Synthetic comment -- @@ -250,13 +253,6 @@
}

private void addDropdown(final MenuAction.OrderedChoices choices) {
final ToolItem combo = new ToolItem(mLayoutToolBar, SWT.DROP_DOWN);
URL iconUrl = choices.getIconUrl();
if (iconUrl != null) {
//Synthetic comment -- @@ -272,12 +268,23 @@
Point point = new Point(event.x, event.y);
point = combo.getDisplay().map(mLayoutToolBar, null, point);

                Menu menu = new Menu(mLayoutToolBar.getShell(), SWT.POP_UP);

                List<URL> icons = choices.getIconUrls();
                List<String> titles = choices.getTitles();
                List<String> ids = choices.getIds();
                String current = choices.getCurrent() != null ? choices.getCurrent() : ""; //$NON-NLS-1$

for (int i = 0; i < titles.size(); i++) {
String title = titles.get(i);
final String id = ids.get(i);
                    URL itemIconUrl = icons != null && icons.size() > 0 ? icons.get(i) : null;
MenuItem item = new MenuItem(menu, SWT.CHECK);
item.setText(title);
                    if (itemIconUrl != null) {
                        Image itemIcon = IconFactory.getInstance().getIcon(itemIconUrl);
                        item.setImage(itemIcon);
                    }

boolean selected = id.equals(current);
if (selected) {
//Synthetic comment -- @@ -288,6 +295,7 @@
@Override
public void widgetSelected(SelectionEvent e) {
choices.getCallback().action(choices, id, null);
                            updateSelection();
}
});
}







