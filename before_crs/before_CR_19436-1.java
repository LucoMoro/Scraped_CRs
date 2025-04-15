/*Issue 13051: Use match_parent or fill_parent based on API level

Fixes issue 13051: New layout editor always insert "match_parent",
even on older platform.

View rules can now look up the API level of the current project, and
based on that choose to use match_parent or fill_parent when they need
to manipulate the layout attributes.

Change-Id:I861e1f7f7409c40c05b1472268f120806667025c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index a17af01..a8c131b 100755

//Synthetic comment -- @@ -33,6 +33,8 @@

/**
* Returns the FQCN for which the rule was loaded.
*/
String getFqcn();

//Synthetic comment -- @@ -64,6 +66,8 @@

/**
* Displays the given message string in an alert dialog with an "OK" button.
*/
void displayAlert(String message);

//Synthetic comment -- @@ -80,5 +84,12 @@
*/
@Nullable
String displayInput(String message, @Nullable String value, @Nullable IValidator filter);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 51eb369..b712ce5 100644

//Synthetic comment -- @@ -100,9 +100,13 @@
String custom_w = "Custom...";
String curr_w = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_WIDTH);

        if (VALUE_FILL_PARENT.equals(curr_w)) {
curr_w = VALUE_MATCH_PARENT;
        } else if (!VALUE_WRAP_CONTENT.equals(curr_w) && !VALUE_MATCH_PARENT.equals(curr_w)) {
curr_w = "zcustom"; //$NON-NLS-1$
custom_w = "Custom: " + curr_w;
}
//Synthetic comment -- @@ -110,9 +114,11 @@
String custom_h = "Custom...";
String curr_h = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_HEIGHT);

        if (VALUE_FILL_PARENT.equals(curr_h)) {
curr_h = VALUE_MATCH_PARENT;
        } else if (!VALUE_WRAP_CONTENT.equals(curr_h) && !VALUE_MATCH_PARENT.equals(curr_h)) {
curr_h = "zcustom"; //$NON-NLS-1$
custom_h = "Custom: " + curr_h;
}
//Synthetic comment -- @@ -200,16 +206,18 @@
List<MenuAction> list1 = Arrays.asList(new MenuAction[] {
new MenuAction.Choices("layout_1width", "Layout Width", //$NON-NLS-1$
mapify(
                      "wrap_content", "Wrap Content", //$NON-NLS-1$
                      "match_parent", "Match Parent", //$NON-NLS-1$
"zcustom", custom_w             //$NON-NLS-1$
),
curr_w,
onChange ),
new MenuAction.Choices("layout_2height", "Layout Height", //$NON-NLS-1$
mapify(
                      "wrap_content", "Wrap Content", //$NON-NLS-1$
                      "match_parent", "Match Parent", //$NON-NLS-1$
"zcustom", custom_h             //$NON-NLS-1$
),
curr_h,
//Synthetic comment -- @@ -321,6 +329,26 @@
return concatenate(list1, list2);
}

/** Join strings into a single string with the given delimiter */
static String join(char delimiter, Collection<String> strings) {
StringBuilder sb = new StringBuilder(100);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java
//Synthetic comment -- index 4c1af19..b3cf5ce 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EDIT_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -39,17 +38,18 @@

// A DialerFilter requires a couple of nested EditTexts with fixed ids:
if (insertType == InsertType.CREATE) {
INode hint = node.appendChild(FQCN_EDIT_TEXT);
hint.setAttribute(ANDROID_URI, ATTR_TEXT, "Hint");
hint.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/hint"); //$NON-NLS-1$
            hint.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);

INode primary = node.appendChild(FQCN_EDIT_TEXT);
primary.setAttribute(ANDROID_URI, ATTR_TEXT, "Primary");
primary.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/primary"); //$NON-NLS-1$
primary.setAttribute(ANDROID_URI, ATTR_LAYOUT_BELOW,
"@android:id/hint"); //$NON-NLS-1$
            primary.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);


// What do we initialize the icon to?








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index f7fc6c5..71bd704 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;

import com.android.ide.common.api.INode;
//Synthetic comment -- @@ -38,8 +37,9 @@
super.onChildInserted(child, parent, insertType);

// The child of the ScrollView should fill in both directions
        child.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
        child.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index d797677..995d206 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;

//Synthetic comment -- @@ -33,12 +32,12 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewMetadata;
import com.android.ide.common.api.IViewMetadata.FillPreference;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -425,11 +424,12 @@
if (metadata != null) {
boolean vertical = isVertical(parent);
FillPreference fill = metadata.getFillPreference();
if (fill.fillHorizontally(vertical)) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
}
if (fill.fillVertically(vertical)) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java
//Synthetic comment -- index c83e294..aee94bc 100755

//Synthetic comment -- @@ -18,7 +18,6 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -35,6 +34,6 @@
public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

        node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index 822c1cb..e3c349a 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -36,8 +35,9 @@
super.onChildInserted(child, parent, insertType);

// The child of the ScrollView should fill in both directions
        child.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
        child.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java
//Synthetic comment -- index 40fb608..3324697 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -35,7 +34,7 @@

// A SeekBar isn't useful with wrap_content because it packs itself down to
// almost no usable width -- so just make it grow in all layouts
        node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java
//Synthetic comment -- index dc512bd..9f8ea80 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import static com.android.ide.common.layout.LayoutConstants.FQCN_FRAME_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TAB_WIDGET;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

//Synthetic comment -- @@ -45,25 +44,27 @@
super.onCreate(node, parent, insertType);

if (insertType == InsertType.CREATE) {
// Configure default Table setup as described in the Table tutorial
node.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/tabhost"); //$NON-NLS-1$
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);

INode linear = node.appendChild(FQCN_LINEAR_LAYOUT);
            linear.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
            linear.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
linear.setAttribute(ANDROID_URI, ATTR_ORIENTATION,
VALUE_VERTICAL);

INode tab = linear.appendChild(FQCN_TAB_WIDGET);
            tab.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
tab.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_WRAP_CONTENT);
tab.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/tabs"); //$NON-NLS-1$

INode frame = linear.appendChild(FQCN_FRAME_LAYOUT);
            frame.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
            frame.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
frame.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/tabcontent"); //$NON-NLS-1$
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index d84ed0d..ff6cbfe 100755

//Synthetic comment -- @@ -772,5 +772,15 @@
public IViewMetadata getMetadata(final String fqcn) {
return new ViewMetadata(mEditor.getLayoutEditor(), fqcn);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index d6161b1..50d71fc 100644

//Synthetic comment -- @@ -20,8 +20,11 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -178,4 +181,48 @@
assertEquals("Collections differ; first difference:", expectedElement, actualElement);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index 520f984..4f29932 100644

//Synthetic comment -- @@ -28,9 +28,9 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.MenuAction.Choices;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.List;

//Synthetic comment -- @@ -41,6 +41,7 @@
boolean haveBounds = dragBounds.isValid();

IViewRule rule = new LinearLayoutRule();
INode targetNode = TestNode.create("android.widget.LinearLayout").id(
"@+id/LinearLayout01").bounds(new Rect(0, 0, 240, 480));
Point dropPoint = new Point(10, 5);
//Synthetic comment -- @@ -118,6 +119,7 @@
// Check that the context menu registers the expected menu items
public void testContextMenu() {
LinearLayoutRule rule = new LinearLayoutRule();
INode node = TestNode.create("android.widget.Button").id("@+id/Button012");

List<MenuAction> contextMenu = rule.getContextMenu(node);
//Synthetic comment -- @@ -135,6 +137,7 @@
// Check that the context menu manipulates the orientation attribute
public void testOrientation() {
LinearLayoutRule rule = new LinearLayoutRule();
INode node = TestNode.create("android.widget.Button").id("@+id/Button012");

assertNull(node.getStringAttr(ANDROID_URI, ATTR_ORIENTATION));







