/*Improve handling of custom width/height layout attributes

Remove the "Custom..." items from the menus (which were not hooked up
anyway).

If there is a custom value (e.g. not fill_parent, match_parent or
wrap_content), add it into the menu directly (such as "42dip"), and
show it as selected.

In addition, always add a "Other..." menu item at the end. Invoking
Other will pop up a dialog asking you to enter a new custom value,
which is then applied.

This addresses issue #2 in multi-issue bug
13134: Eclipse layout editor contains many bugs

Change-Id:Ic1a84a789c53dd3a15b807a29461b80dc1b49c9f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index b712ce5..9cdfa13 100644

//Synthetic comment -- @@ -49,6 +49,8 @@
* Common IViewRule processing to all view and layout classes.
*/
public class BaseViewRule implements IViewRule {
protected IClientRulesEngine mRulesEngine;

// Cache of attributes. Key is FQCN of a node mixed with its view hierarchy
//Synthetic comment -- @@ -97,7 +99,7 @@
}
final String key = keySb.toString();

        String custom_w = "Custom...";
String curr_w = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_WIDTH);

String fillParent = getFillParentValueName();
//Synthetic comment -- @@ -107,11 +109,10 @@
} else if (!canMatchParent && VALUE_MATCH_PARENT.equals(curr_w)) {
curr_w = VALUE_FILL_PARENT;
} else if (!VALUE_WRAP_CONTENT.equals(curr_w) && !fillParent.equals(curr_w)) {
            curr_w = "zcustom"; //$NON-NLS-1$
            custom_w = "Custom: " + curr_w;
}

        String custom_h = "Custom...";
String curr_h = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_HEIGHT);

if (canMatchParent && VALUE_FILL_PARENT.equals(curr_h)) {
//Synthetic comment -- @@ -119,8 +120,7 @@
} else if (!canMatchParent && VALUE_MATCH_PARENT.equals(curr_h)) {
curr_h = VALUE_FILL_PARENT;
} else if (!VALUE_WRAP_CONTENT.equals(curr_h) && !fillParent.equals(curr_h)) {
            curr_h = "zcustom"; //$NON-NLS-1$
            custom_h = "Custom: " + curr_h;
}

IMenuCallback onChange = new IMenuCallback() {
//Synthetic comment -- @@ -135,19 +135,22 @@
final INode node = selectedNode;

if (fullActionId.equals("layout_1width")) { //$NON-NLS-1$
                    if (!valueId.startsWith("z")) {         //$NON-NLS-1$
node.editXml("Change attribute " + ATTR_LAYOUT_WIDTH, new INodeHandler() {
public void handle(INode n) {
                                n.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, valueId);
}
});
}
return;
} else if (fullActionId.equals("layout_2height")) { //$NON-NLS-1$
                    if (!valueId.startsWith("z")) {                 //$NON-NLS-1$
node.editXml("Change attribute " + ATTR_LAYOUT_HEIGHT, new INodeHandler() {
public void handle(INode n) {
                                n.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, valueId);
}
});
}
//Synthetic comment -- @@ -201,6 +204,26 @@
}
}
}
};

List<MenuAction> list1 = Arrays.asList(new MenuAction[] {
//Synthetic comment -- @@ -209,7 +232,8 @@
VALUE_WRAP_CONTENT, "Wrap Content",
canMatchParent ? VALUE_MATCH_PARENT : VALUE_FILL_PARENT,
canMatchParent ? "Match Parent" : "Fill Parent",
                      "zcustom", custom_w             //$NON-NLS-1$
),
curr_w,
onChange ),
//Synthetic comment -- @@ -218,7 +242,8 @@
VALUE_WRAP_CONTENT, "Wrap Content",
canMatchParent ? VALUE_MATCH_PARENT : VALUE_FILL_PARENT,
canMatchParent ? "Match Parent" : "Fill Parent",
                      "zcustom", custom_h             //$NON-NLS-1$
),
curr_h,
onChange ),
//Synthetic comment -- @@ -387,7 +412,12 @@
static Map<String, String> mapify(String... values) {
Map<String, String> map = new HashMap<String, String>(values.length / 2);
for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
}

return map;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index 4f29932..e3e805e 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
//Synthetic comment -- @@ -33,6 +35,7 @@
import com.android.ide.common.api.MenuAction.Choices;

import java.util.List;

/** Test the {@link LinearLayoutRule} */
public class LinearLayoutRuleTest extends LayoutTestBase {
//Synthetic comment -- @@ -134,6 +137,27 @@
// TODO: Test Properties-list
}

// Check that the context menu manipulates the orientation attribute
public void testOrientation() {
LinearLayoutRule rule = new LinearLayoutRule();







