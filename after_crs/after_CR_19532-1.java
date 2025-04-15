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
    private static final String ZCUSTOM = "zcustom"; //$NON-NLS-1$

protected IClientRulesEngine mRulesEngine;

// Cache of attributes. Key is FQCN of a node mixed with its view hierarchy
//Synthetic comment -- @@ -97,7 +99,7 @@
}
final String key = keySb.toString();

        String custom_w = null;
String curr_w = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_WIDTH);

String fillParent = getFillParentValueName();
//Synthetic comment -- @@ -107,11 +109,10 @@
} else if (!canMatchParent && VALUE_MATCH_PARENT.equals(curr_w)) {
curr_w = VALUE_FILL_PARENT;
} else if (!VALUE_WRAP_CONTENT.equals(curr_w) && !fillParent.equals(curr_w)) {
            custom_w = curr_w;
}

        String custom_h = null;
String curr_h = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_HEIGHT);

if (canMatchParent && VALUE_FILL_PARENT.equals(curr_h)) {
//Synthetic comment -- @@ -119,8 +120,7 @@
} else if (!canMatchParent && VALUE_MATCH_PARENT.equals(curr_h)) {
curr_h = VALUE_FILL_PARENT;
} else if (!VALUE_WRAP_CONTENT.equals(curr_h) && !fillParent.equals(curr_h)) {
            custom_h = curr_h;
}

IMenuCallback onChange = new IMenuCallback() {
//Synthetic comment -- @@ -135,19 +135,22 @@
final INode node = selectedNode;

if (fullActionId.equals("layout_1width")) { //$NON-NLS-1$
                    final String newAttrValue = getValue(valueId);
                    if (newAttrValue != null) {
node.editXml("Change attribute " + ATTR_LAYOUT_WIDTH, new INodeHandler() {
public void handle(INode n) {
                                n.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, newAttrValue);
}
});
}
return;
} else if (fullActionId.equals("layout_2height")) { //$NON-NLS-1$
                    // Ask the user
                    final String newAttrValue = getValue(valueId);
                    if (newAttrValue != null) {
node.editXml("Change attribute " + ATTR_LAYOUT_HEIGHT, new INodeHandler() {
public void handle(INode n) {
                                n.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, newAttrValue);
}
});
}
//Synthetic comment -- @@ -201,6 +204,26 @@
}
}
}

            /**
             * Returns the value (which will ask the user if the value is the special
             * {@link #ZCUSTOM} marker
             */
            private String getValue(final String valueId) {
                final String newAttrValue;
                if (valueId.equals(ZCUSTOM)) {
                    String value = mRulesEngine.displayInput(
                            "Set custom layout attribute value (example: 50dip)", "", null);
                    if (value != null && value.trim().length() > 0) {
                        newAttrValue = value.trim();
                    } else {
                        newAttrValue = null;
                    }
                } else {
                    newAttrValue = valueId;
                }
                return newAttrValue;
            }
};

List<MenuAction> list1 = Arrays.asList(new MenuAction[] {
//Synthetic comment -- @@ -209,7 +232,8 @@
VALUE_WRAP_CONTENT, "Wrap Content",
canMatchParent ? VALUE_MATCH_PARENT : VALUE_FILL_PARENT,
canMatchParent ? "Match Parent" : "Fill Parent",
                      custom_w, custom_w,
                      ZCUSTOM, "Other..."
),
curr_w,
onChange ),
//Synthetic comment -- @@ -218,7 +242,8 @@
VALUE_WRAP_CONTENT, "Wrap Content",
canMatchParent ? VALUE_MATCH_PARENT : VALUE_FILL_PARENT,
canMatchParent ? "Match Parent" : "Fill Parent",
                      custom_h, custom_h,
                      ZCUSTOM, "Other..."
),
curr_h,
onChange ),
//Synthetic comment -- @@ -387,7 +412,12 @@
static Map<String, String> mapify(String... values) {
Map<String, String> map = new HashMap<String, String>(values.length / 2);
for (int i = 0; i < values.length; i += 2) {
            String key = values[i];
            if (key == null) {
                continue;
            }
            String value = values[i + 1];
            map.put(key, value);
}

return map;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index 4f29932..e3e805e 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
//Synthetic comment -- @@ -33,6 +35,7 @@
import com.android.ide.common.api.MenuAction.Choices;

import java.util.List;
import java.util.Map;

/** Test the {@link LinearLayoutRule} */
public class LinearLayoutRuleTest extends LayoutTestBase {
//Synthetic comment -- @@ -134,6 +137,27 @@
// TODO: Test Properties-list
}

    public void testContextMenuCustom() {
        LinearLayoutRule rule = new LinearLayoutRule();
        initialize(rule, "android.widget.LinearLayout");
        INode node = TestNode.create("android.widget.Button").id("@+id/Button012")
            .set(ANDROID_URI, ATTR_LAYOUT_WIDTH, "42dip")
            .set(ANDROID_URI, ATTR_LAYOUT_HEIGHT, "50sp");

        List<MenuAction> contextMenu = rule.getContextMenu(node);
        assertEquals(4, contextMenu.size());
        assertEquals("Layout Width", contextMenu.get(0).getTitle());
        MenuAction menuAction = contextMenu.get(0);
        assertTrue(menuAction instanceof MenuAction.Choices);
        MenuAction.Choices choices = (Choices) menuAction;
        Map<String, String> items = choices.getChoices();
        assertTrue(items.containsKey("42dip"));
        assertTrue(items.containsValue("42dip"));
        assertEquals("Other...", items.get("zcustom"));
        assertEquals("Match Parent", items.get("match_parent"));
        assertEquals("42dip", choices.getCurrent());
    }

// Check that the context menu manipulates the orientation attribute
public void testOrientation() {
LinearLayoutRule rule = new LinearLayoutRule();







