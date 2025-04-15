/*Add layout unit tests

Add layout unit tests, and some infrastructure for testing.  Also fix
some formatting errors (>100 column lines) in the previous commit.

Change-Id:I3eabf30998ab7deb84df57e4d0c10cf57ee399d5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 6d640a1..e2d64eb 100644

//Synthetic comment -- @@ -54,7 +54,11 @@
});
}

    void drawFeedback(
            IGraphics gc,
            INode targetNode,
            IDragElement[] elements,
            DropFeedback feedback) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java
//Synthetic comment -- index 9d8e0c2..13b335e 100644

//Synthetic comment -- @@ -355,7 +355,8 @@
if (value != null && value.length() > 0) {
newNode.setAttribute(uri, name, value);

                if (uri.equals(ANDROID_URI) && name.equals(ATTR_ID) &&
                        oldId != null && !oldId.equals(value)) {
newId = value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java
//Synthetic comment -- index 950ea2b..9bd82a7 100644

//Synthetic comment -- @@ -143,7 +143,10 @@

IMenuCallback onChange = new IMenuCallback() {

            public void action(
                    final MenuAction action,
                    final String valueId,
                    final Boolean newValue) {
String fullActionId = action.getId();
boolean isProp = fullActionId.startsWith("@prop@");
final String actionId = isProp ? fullActionId.substring(6) : fullActionId;
//Synthetic comment -- @@ -460,7 +463,11 @@
// ignore
}

    public void onDropped(
            INode targetNode,
            IDragElement[] elements,
            DropFeedback feedback,
            Point p) {
// ignore
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index f3efb64..16b477b 100755

//Synthetic comment -- @@ -51,7 +51,11 @@
});
}

    void drawFeedback(
            IGraphics gc,
            INode targetNode,
            IDragElement[] elements,
            DropFeedback feedback) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 7a960b1..3e5bdcf 100755

//Synthetic comment -- @@ -1152,7 +1152,6 @@
int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);

if (e.button == 3) {
// Right click button is used to display a context menu.
// If there's an existing selection and the click is anywhere in this selection








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..715966c

//Synthetic comment -- @@ -0,0 +1,72 @@
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

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

/** Test the {@link AbsoluteLayoutRule} */
public class AbsoluteLayoutRuleTest extends AbstractLayoutRuleTest {
    // Utility for other tests
    protected void dragInto(Rect dragBounds, Point dragPoint, int insertIndex, int currentIndex,
            String... graphicsFragments) {
        INode layout = TestNode.create("android.widget.AbsoluteLayout").id("@+id/AbsoluteLayout01")
                .bounds(new Rect(0, 0, 240, 480)).add(
                        TestNode.create("android.widget.Button").id("@+id/Button01").bounds(
                                new Rect(0, 0, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button02").bounds(
                                new Rect(0, 100, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button03").bounds(
                                new Rect(0, 200, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button04").bounds(
                                new Rect(0, 300, 100, 80)));

        super.dragInto(new AbsoluteLayoutRule(), layout, dragBounds, dragPoint, insertIndex,
                currentIndex, graphicsFragments);
    }

    public void testDragMiddle() {
        dragInto(
                // Bounds of the dragged item
                new Rect(0, 0, 105, 80),
                // Drag point
                new Point(30, -10),
                // Expected insert location: We just append in absolute layout
                4,
                // Not dragging one of the existing children
                -1,
                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop preview
                "useStyle(DROP_PREVIEW), drawRect(Rect[30,-10,105,80])");

        // Without drag bounds we should just draw guide lines instead
        dragInto(
                new Rect(0, 0, 0, 0),
                new Point(30, -10),
                4,
                -1,
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",
                // Guideline
                "useStyle(GUIDELINE), drawLine(30,0,30,480), drawLine(0,-10,240,-10)",
                // Drop preview
                "useStyle(DROP_PREVIEW), drawLine(30,-10,240,-10), drawLine(30,-10,30,480)");
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbstractLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbstractLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4287996

//Synthetic comment -- @@ -0,0 +1,151 @@
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

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

/**
 * Common layout helpers from LayoutRule tests
 */
public abstract class AbstractLayoutRuleTest extends TestCase {
    /**
     * Helper function used by tests to drag a button into a canvas containing
     * the given children.
     *
     * @param rule The rule to test on
     * @param targetNode The target layout node to drag into
     * @param dragBounds The (original) bounds of the dragged item
     * @param dropPoint The drag point we should drag to and drop
     * @param insertIndex The expected insert position we end up with after
     *            dropping at the dropPoint
     * @param currentIndex If the dragged widget is already in the canvas this
     *            should be its child index; if not, pass in -1
     * @param graphicsFragments This is a varargs array of String fragments we
     *            expect to see in the graphics output on the drag over event.
     */
    protected void dragInto(IViewRule rule, INode targetNode, Rect dragBounds, Point dropPoint,
            int insertIndex, int currentIndex, String... graphicsFragments) {

        String draggedButtonId = (currentIndex == -1) ? "@+id/DraggedButton" : targetNode
                .getChildren()[currentIndex].getStringAttr(BaseLayout.ANDROID_URI,
                BaseLayout.ATTR_ID);

        IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button", dragBounds).id(draggedButtonId));

        // Enter target
        DropFeedback feedback = rule.onDropEnter(targetNode, elements);
        assertNotNull(feedback);
        assertFalse(feedback.invalidTarget);
        assertNotNull(feedback.painter);

        // Move near top left corner of the target
        feedback = rule.onDropMove(targetNode, elements, feedback, dropPoint);
        assertNotNull(feedback);
        assertFalse(feedback.invalidTarget);

        // Paint feedback and make sure it's what we expect
        TestGraphics graphics = new TestGraphics();
        assertNotNull(feedback.painter);
        feedback.painter.paint(graphics, targetNode, feedback);
        String drawn = graphics.getDrawn().toString();

        // Check that each graphics fragment is drawn
        for (String fragment : graphicsFragments) {
            if (!drawn.contains(fragment)) {
                // Get drawn-output since unit test truncates message in below
                // contains-assertion
                System.out.println("Could not find: " + fragment);
                System.out.println("Full graphics output: " + drawn);
            }
            assertTrue(fragment + " not found; full=" + drawn, drawn.contains(fragment));
        }

        // Attempt a drop
        int childrenCountBefore = targetNode.getChildren().length;
        rule.onDropped(targetNode, elements, feedback, dropPoint);

        if (currentIndex == -1) {
            // Inserting new from outside
            assertEquals(childrenCountBefore+1, targetNode.getChildren().length);
        } else {
            // Moving from existing; must remove in old position first
            ((TestNode) targetNode).removeChild(currentIndex);

            assertEquals(childrenCountBefore, targetNode.getChildren().length);
        }
        // Ensure that it's inserted in the right place
        String actualId = targetNode.getChildren()[insertIndex].getStringAttr(
                BaseLayout.ANDROID_URI, BaseLayout.ATTR_ID);
        if (!draggedButtonId.equals(actualId)) {
            // Using assertEquals instead of fail to get nice diff view on test
            // failure
            List<String> childrenIds = new ArrayList<String>();
            for (INode child : targetNode.getChildren()) {
                childrenIds.add(child.getStringAttr(BaseLayout.ANDROID_URI, BaseLayout.ATTR_ID));
            }
            int index = childrenIds.indexOf(draggedButtonId);
            String message = "Button found at index " + index + " instead of " + insertIndex
                    + " among " + childrenIds;
            System.out.println(message);
            assertEquals(message, draggedButtonId, actualId);
        }
    }

    /**
     * Utility method for asserting that two collections contain exactly the
     * same elements (regardless of order)
     */
    public static void assertContainsSame(Collection<String> expected, Collection<String> actual) {
        if (expected.size() != actual.size()) {
            fail("Collection sizes differ; expected " + expected.size() + " but was "
                    + actual.size());
        }

        // Sort prior to comparison to ensure we have the same elements
        // regardless of order
        List<String> expectedList = new ArrayList<String>(expected);
        Collections.sort(expectedList);
        List<String> actualList = new ArrayList<String>(actual);
        Collections.sort(actualList);
        // Instead of just assertEquals(expectedList, actualList);
        // we iterate one element at a time so we can show the first
        // -difference-.
        for (int i = 0; i < expectedList.size(); i++) {
            String expectedElement = expectedList.get(i);
            String actualElement = actualList.get(i);
            if (!expectedElement.equals(actualElement)) {
                System.out.println("Expected items: " + expectedList);
                System.out.println("Actual items  : " + actualList);
            }
            assertEquals("Collections differ; first difference:", expectedElement, actualElement);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java
new file mode 100644
//Synthetic comment -- index 0000000..dad1420

//Synthetic comment -- @@ -0,0 +1,240 @@
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

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.BaseLayout.AttributeFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// TODO: Check assertions
// TODO: Check equals() but not == strings by using new String("") to prevent interning
// TODO: Rename BaseLayout to BaseLayoutRule, and tests too of course

public class BaseLayoutTest extends AbstractLayoutRuleTest {

    /** Provides test data used by other test cases */
    private IDragElement[] createSampleElements() {
        IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button", new Rect(0, 0, 100, 80)).id("@+id/Button01"),
                TestDragElement.create("android.widget.LinearLayout", new Rect(0, 80, 100, 280))
                        .id("@+id/LinearLayout01").add(
                                TestDragElement.create("android.widget.Button",
                                        new Rect(0, 80, 100, 80)).id("@+id/Button011"),
                                TestDragElement.create("android.widget.Button",
                                        new Rect(0, 180, 100, 80)).id("@+id/Button012")),
                TestDragElement.create("android.widget.Button", new Rect(100, 0, 100, 80)).id(
                        "@+id/Button02"));
        return elements;
    }

    /** Test {@link BaseLayout#collectIds}: Check that basic lookup of id works */
    public final void testCollectIds1() {
        IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button", new Rect(0, 0, 100, 80)).id("@+id/Button01"));
        Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        Map<String, Pair<String, String>> ids = new BaseLayout().collectIds(idMap, elements);
        assertEquals(1, ids.size());
        assertEquals("@+id/Button01", ids.keySet().iterator().next());
    }

    /**
     * Test {@link BaseLayout#collectIds}: Check that with the wrong URI we
     * don't pick up the ID
     */
    public final void testCollectIds2() {
        IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button", new Rect(0, 0, 100, 80)).set("myuri", BaseView.ATTR_ID,
                "@+id/Button01"));

        Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        Map<String, Pair<String, String>> ids = new BaseLayout().collectIds(idMap, elements);
        assertEquals(0, ids.size());
    }

    /**
     * Test {@link BaseLayout#normalizeId(String)}
     */
    public final void testNormalizeId() {
        assertEquals("foo", new BaseLayout().normalizeId("foo"));
        assertEquals("@+id/name", new BaseLayout().normalizeId("@id/name"));
        assertEquals("@+id/name", new BaseLayout().normalizeId("@+id/name"));
    }

    /**
     * Test {@link BaseLayout#collectExistingIds}
     */
    public final void testCollectExistingIds1() {
        Set<String> existing = new HashSet<String>();
        INode node = TestNode.create("android.widget.Button").id("@+id/Button012").add(
                TestNode.create("android.widget.Button").id("@+id/Button2"));

        new BaseLayout().collectExistingIds(node, existing);

        assertEquals(2, existing.size());
        assertContainsSame(Arrays.asList("@+id/Button2", "@+id/Button012"), existing);
    }

    /**
     * Test {@link BaseLayout#collectIds}: Check that with multiple elements and
     * some children we still pick up all the right id's
     */
    public final void testCollectIds3() {
        Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();

        IDragElement[] elements = createSampleElements();
        Map<String, Pair<String, String>> ids = new BaseLayout().collectIds(idMap, elements);
        assertEquals(5, ids.size());
        assertContainsSame(Arrays.asList("@+id/Button01", "@+id/Button02", "@+id/Button011",
                "@+id/Button012", "@+id/LinearLayout01"), ids.keySet());

        // Make sure the Pair has the right stuff too;
        // (having the id again in the pair seems redundant; see if I really
        // need it in the implementation)
        assertEquals(Pair.of("@+id/LinearLayout01", "android.widget.LinearLayout"), ids
                .get("@+id/LinearLayout01"));
    }

    /**
     * Test {@link BaseLayout#remapIds}: Ensure that it identifies a conflict
     */
    public final void testRemapIds1() {
        Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        BaseLayout baseLayout = new BaseLayout();
        IDragElement[] elements = createSampleElements();
        baseLayout.collectIds(idMap, elements);
        INode node = TestNode.create("android.widget.Button").id("@+id/Button012").add(
                TestNode.create("android.widget.Button").id("@+id/Button2"));

        assertEquals(5, idMap.size());
        Map<String, Pair<String, String>> remapped = baseLayout.remapIds(node, idMap);
        // 4 original from the sample elements, plus overlap with one
        // (Button012) - one new
        // button added in
        assertEquals(6, remapped.size());

        // TODO: I'm a little confused about what exactly this method should do;
        // check with Raphael.
    }


    /**
     * Test {@link BaseLayout#getDropIdMap}
     */
    public final void testGetDropIdMap() {
        BaseLayout baseLayout = new BaseLayout();
        IDragElement[] elements = createSampleElements();
        INode node = TestNode.create("android.widget.Button").id("@+id/Button012").add(
                TestNode.create("android.widget.Button").id("@+id/Button2"));

        Map<String, Pair<String, String>> idMap = baseLayout.getDropIdMap(node, elements, true);
        assertContainsSame(Arrays.asList("@+id/Button01", "@+id/Button012", "@+id/Button011",
                "@id/Button012", "@+id/Button02", "@+id/LinearLayout01"), idMap
                .keySet());

        // TODO: I'm a little confused about what exactly this method should do;
        // check with Raphael.
    }

    public final void testAddAttributes1() {
        BaseLayout layout = new BaseLayout();

        // First try with no filter
        IDragElement oldElement = TestDragElement.create("a.w.B").id("@+id/foo");
        INode newNode = TestNode.create("a.w.B").id("@+id/foo").set("u", "key", "value").set("u",
                "nothidden", "nothiddenvalue");
        ;
        AttributeFilter filter = null;
        // No references in this test case
        Map<String, Pair<String, String>> idMap = null;

        layout.addAttributes(newNode, oldElement, idMap, filter);
        assertEquals("value", newNode.getStringAttr("u", "key"));
        assertEquals("nothiddenvalue", newNode.getStringAttr("u", "nothidden"));
    }

    public final void testAddAttributes2() {
        // Test filtering
        BaseLayout layout = new BaseLayout();

        // First try with no filter
        IDragElement oldElement = TestDragElement.create("a.w.B").id("@+id/foo");
        INode newNode = TestNode.create("a.w.B").id("@+id/foo").set("u", "key", "value").set("u",
                "hidden", "hiddenvalue");
        AttributeFilter filter = new AttributeFilter() {

            public String replace(String attributeUri, String attributeName,
                    String attributeValue) {
                if (attributeName.equals("hidden")) {
                    return null;
                }

                return attributeValue;
            }
        };
        // No references in this test case
        Map<String, Pair<String, String>> idMap = null;

        layout.addAttributes(newNode, oldElement, idMap, filter);
        assertEquals("value", newNode.getStringAttr("u", "key"));
    }

    public final void testFindNewId() {
        BaseLayout baseLayout = new BaseLayout();
        Set<String> existing = new HashSet<String>();
        assertEquals("@+id/Widget01", baseLayout.findNewId("a.w.Widget", existing));

        existing.add("@+id/Widget01");
        assertEquals("@+id/Widget02", baseLayout.findNewId("a.w.Widget", existing));

        existing.add("@+id/Widget02");
        assertEquals("@+id/Widget03", baseLayout.findNewId("a.w.Widget", existing));

        existing.remove("@+id/Widget02");
        assertEquals("@+id/Widget02", baseLayout.findNewId("a.w.Widget", existing));
    }

    public final void testDefaultAttributeFilter() {
        assertEquals("true", BaseLayout.DEFAULT_ATTR_FILTER.replace("myuri", "layout_alignRight",
                "true"));
        assertEquals(null, BaseLayout.DEFAULT_ATTR_FILTER.replace(BaseLayout.ANDROID_URI,
                "layout_alignRight", "true"));
        assertEquals("true", BaseLayout.DEFAULT_ATTR_FILTER.replace(BaseLayout.ANDROID_URI,
                "myproperty", "true"));
    }

    public final void testAddInnerElements() {
        IDragElement oldElement = TestDragElement.create("root").add(
                TestDragElement.create("a.w.B").id("@+id/child1")
                        .set("uri", "childprop1", "value1"),
                TestDragElement.create("a.w.B").id("@+id/child2").set("uri", "childprop2a",
                        "value2a").set("uri", "childprop2b", "value2b"));
        INode newNode = TestNode.create("a.w.B").id("@+id/foo");
        Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        BaseLayout layout = new BaseLayout();
        layout.addInnerElements(newNode, oldElement, idMap);
        assertEquals(2, newNode.getChildren().length);

        assertEquals("value2b", newNode.getChildren()[1].getStringAttr("uri", "childprop2b"));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewTest.java
new file mode 100644
//Synthetic comment -- index 0000000..c5e40c2

//Synthetic comment -- @@ -0,0 +1,43 @@
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

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

public class BaseViewTest extends TestCase {
    public final void testPrettyName() {
        assertEquals(null, BaseView.prettyName(null));
        assertEquals("", BaseView.prettyName(""));
        assertEquals("Foo", BaseView.prettyName("foo"));
        assertEquals("Foo bar", BaseView.prettyName("foo_bar"));
        // TODO: We should check this to capitalize each initial word
        // assertEquals("Foo Bar", BaseView.prettyName("foo_bar"));
        // TODO: We should also handle camelcase properties
        // assertEquals("Foo Bar", BaseView.prettyName("fooBar"));
    }

    public final void testJoin() {
        assertEquals("foo", BaseView.join('|', Arrays.asList("foo")));
        assertEquals("", BaseView.join('|', Collections.<String>emptyList()));
        assertEquals(null, null);
        assertEquals("foo,bar", BaseView.join(',', Arrays.asList("foo", "bar")));
        assertEquals("foo|bar", BaseView.join('|', Arrays.asList("foo", "bar")));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/FrameLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/FrameLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..eb9c44b

//Synthetic comment -- @@ -0,0 +1,63 @@
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

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

/** Test the {@link FrameLayoutRule} */
public class FrameLayoutRuleTest extends AbstractLayoutRuleTest {
    // Utility for other tests
    protected void dragInto(Rect dragBounds, Point dragPoint, int insertIndex, int currentIndex,
            String... graphicsFragments) {
        INode layout = TestNode.create("android.widget.FrameLayout").id("@+id/FrameLayout01")
                .bounds(new Rect(0, 0, 240, 480)).add(
                        TestNode.create("android.widget.Button").id("@+id/Button01").bounds(
                                new Rect(0, 0, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button02").bounds(
                                new Rect(0, 100, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button03").bounds(
                                new Rect(0, 200, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button04").bounds(
                                new Rect(0, 300, 100, 80)));

        super.dragInto(new FrameLayoutRule(), layout, dragBounds, dragPoint, insertIndex,
                currentIndex, graphicsFragments);
    }

    public void testDragMiddle() {
        dragInto(
        // Bounds of the dragged item
                new Rect(0, 0, 105, 80),
                // Drag point
                new Point(30, -10),
                // Expected insert location: We just append in absolute layout
                4,
                // Not dragging one of the existing children
                -1,
                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop Preview
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,0,105,80])]");
        // Without drag bounds we should just draw guide lines instead
        dragInto(new Rect(0, 0, 0, 0), new Point(30, -10), 4, -1,
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",
                "useStyle(DROP_PREVIEW), drawLine(1,0,1,480), drawLine(0,1,240,1)");
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..462069b

//Synthetic comment -- @@ -0,0 +1,308 @@
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

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.MenuAction.Choices;

import java.util.List;

/** Test the {@link LinearLayoutRule} */
public class LinearLayoutRuleTest extends AbstractLayoutRuleTest {
    // Utility for other tests
    protected void dragIntoEmpty(Rect dragBounds) {
        boolean haveBounds = dragBounds.isValid();

        IViewRule rule = new LinearLayoutRule();
        INode targetNode = TestNode.create("android.widget.LinearLayout").id(
        "@+id/LinearLayout01").bounds(new Rect(0, 0, 240, 480));
        Point dropPoint = new Point(10, 5);

        IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button", dragBounds).id("@+id/Button01"));

        // Enter target
        DropFeedback feedback = rule.onDropEnter(targetNode, elements);
        assertNotNull(feedback);
        assertFalse(feedback.invalidTarget);
        assertNotNull(feedback.painter);

        feedback = rule.onDropMove(targetNode, elements, feedback, dropPoint);
        assertNotNull(feedback);
        assertFalse(feedback.invalidTarget);

        // Paint feedback and make sure it's what we expect
        TestGraphics graphics = new TestGraphics();
        assertNotNull(feedback.painter);
        feedback.painter.paint(graphics, targetNode, feedback);
        assertEquals(
        // Expect to see a recipient rectangle around the bounds of the
                // LinearLayout,
                // as well as a single vertical line as a drop preview located
                // along the left
                // edge (for this horizontal linear layout) showing insert
                // position at index 0,
                // and finally a rectangle for the bounds of the inserted button
                // centered over
                // the middle
                "[useStyle(DROP_RECIPIENT), "
                        +
                        // Bounds rectangle
                        "drawRect(Rect[0,0,240,480]), " + "useStyle(DROP_ZONE), "
                        + "useStyle(DROP_ZONE_ACTIVE), " + "useStyle(DROP_PREVIEW), " +
                        // Insert position line
                        "drawLine(1,0,1,480)" + (haveBounds ?
                        // Outline of dragged node centered over position line
                        ", useStyle(DROP_PREVIEW), " + "drawRect(Rect[-49,0,100,80])"
                                // Nothing when we don't have bounds
                                : "") + "]", graphics.getDrawn().toString());

        // Attempt a drop
        assertEquals(0, targetNode.getChildren().length);
        rule.onDropped(targetNode, elements, feedback, dropPoint);
        assertEquals(1, targetNode.getChildren().length);
        assertEquals("@+id/Button01", targetNode.getChildren()[0].getStringAttr(
                BaseLayout.ANDROID_URI, BaseLayout.ATTR_ID));
    }

    // Utility for other tests
    protected void dragInto(boolean vertical, Rect dragBounds, Point dragPoint,
            int insertIndex, int currentIndex,
            String... graphicsFragments) {
        INode linearLayout = TestNode.create("android.widget.LinearLayout").id(
                "@+id/LinearLayout01").bounds(new Rect(0, 0, 240, 480)).set(BaseLayout.ANDROID_URI,
                LinearLayoutRule.ATTR_ORIENTATION,
                vertical ? LinearLayoutRule.VALUE_VERTICAL : LinearLayoutRule.VALUE_HORIZONTAL)
                .add(
                        TestNode.create("android.widget.Button").id("@+id/Button01").bounds(
                                new Rect(0, 0, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button02").bounds(
                                new Rect(0, 100, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button03").bounds(
                                new Rect(0, 200, 100, 80)),
                        TestNode.create("android.widget.Button").id("@+id/Button04").bounds(
                                new Rect(0, 300, 100, 80)));

        super.dragInto(new LinearLayoutRule(), linearLayout, dragBounds, dragPoint, insertIndex,
                currentIndex, graphicsFragments);
    }

    // Check that the context menu registers the expected menu items
    public void testContextMenu() {
        LinearLayoutRule rule = new LinearLayoutRule();
        INode node = TestNode.create("android.widget.Button").id("@+id/Button012");

        List<MenuAction> contextMenu = rule.getContextMenu(node);
        assertEquals(4, contextMenu.size());
        assertEquals("Layout Width", contextMenu.get(0).getTitle());
        assertEquals("Layout Height", contextMenu.get(1).getTitle());
        assertEquals("Properties", contextMenu.get(2).getTitle());
        assertEquals("Orientation", contextMenu.get(3).getTitle());

        MenuAction propertiesMenu = contextMenu.get(2);
        assertTrue(propertiesMenu.getClass().getName(), propertiesMenu instanceof MenuAction.Group);
        // TODO: Test Properties-list
    }

    // Check that the context menu manipulates the orientation attribute
    public void testOrientation() {
        LinearLayoutRule rule = new LinearLayoutRule();
        INode node = TestNode.create("android.widget.Button").id("@+id/Button012");

        assertNull(node.getStringAttr(BaseLayout.ANDROID_URI, LinearLayoutRule.ATTR_ORIENTATION));

        List<MenuAction> contextMenu = rule.getContextMenu(node);
        assertEquals(4, contextMenu.size());
        MenuAction orientationAction = contextMenu.get(3);

        assertTrue(orientationAction.getClass().getName(),
                orientationAction instanceof MenuAction.Choices);

        MenuAction.Choices choices = (Choices) orientationAction;
        IMenuCallback callback = choices.getCallback();
        callback.action(orientationAction, LinearLayoutRule.VALUE_VERTICAL, true);

        String orientation = node.getStringAttr(BaseLayout.ANDROID_URI,
                LinearLayoutRule.ATTR_ORIENTATION);
        assertEquals(LinearLayoutRule.VALUE_VERTICAL, orientation);
        callback.action(orientationAction, LinearLayoutRule.VALUE_HORIZONTAL, true);
        orientation = node.getStringAttr(BaseLayout.ANDROID_URI, LinearLayoutRule.ATTR_ORIENTATION);
        assertEquals(LinearLayoutRule.VALUE_HORIZONTAL, orientation);
    }

    public void testDragInEmptyWithBounds() {
        dragIntoEmpty(new Rect(0, 0, 100, 80));
    }

    public void testDragInEmptyWithoutBounds() {
        dragIntoEmpty(new Rect(0, 0, 0, 0));
    }

    public void testDragInVerticalTop() {
        dragInto(true,
        // Bounds of the dragged item
                new Rect(0, 0, 105, 80),
                // Drag point
                new Point(30, -10),
                // Expected insert location
                0,
                // Not dragging one of the existing children
                -1,
                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop zones
                "useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,90,240,90), "
                        + "drawLine(0,190,240,190), drawLine(0,290,240,290)",

                // Active nearest line
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,0,240,0)",

                // Preview of the dropped rectangle
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,-40,105,80])");

        // Without drag bounds it should be identical except no preview
        // rectangle
        dragInto(true,
                new Rect(0, 0, 0, 0), // Invalid
                new Point(30, -10), 0, -1,
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,0,240,0)");
    }

    public void testDragInVerticalBottom() {
        dragInto(true,
        // Bounds of the dragged item
                new Rect(0, 0, 105, 80),
                // Drag point
                new Point(30, 500),
                // Expected insert location
                4,
                // Not dragging one of the existing children
                -1,
                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop zones
                "useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,90,240,90), "
                        + "drawLine(0,190,240,190), drawLine(0,290,240,290)",

                // Active nearest line
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,381,240,381)",

                // Preview of the dropped rectangle
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,341,105,80])");

        // Check without bounds too
        dragInto(true, new Rect(0, 0, 105, 80), new Point(30, 500), 4, -1,
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,341,105,80])");
    }

    public void testDragInVerticalMiddle() {
        dragInto(true,
        // Bounds of the dragged item
                new Rect(0, 0, 105, 80),
                // Drag point
                new Point(0, 170),
                // Expected insert location
                2,
                // Not dragging one of the existing children
                -1,
                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop zones
                "useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,90,240,90), "
                        + "drawLine(0,190,240,190), drawLine(0,290,240,290)",

                // Active nearest line
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,190,240,190)",

                // Preview of the dropped rectangle
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,150,105,80])");

        // Check without bounds too
        dragInto(true, new Rect(0, 0, 105, 80), new Point(0, 170), 2, -1,
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,150,105,80])");
    }

    public void testDragInVerticalMiddleSelfPos() {
        // Drag the 2nd button, down to the position between 3rd and 4th
        dragInto(true,
        // Bounds of the dragged item
                new Rect(0, 100, 100, 80),
                // Drag point
                new Point(0, 250),
                // Expected insert location
                2,
                // Dragging 1st item
                1,
                // Bounds rectangle

                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop zones - these are different because we exclude drop
                // zones around the
                // dragged item itself (it doesn't make sense to insert directly
                // before or after
                // myself
                "useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,290,240,290), "
                        + "drawLine(0,381,240,381)",

                // Preview line along insert axis
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,290,240,290)",

                // Preview of dropped rectangle
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,250,100,80])");

        // Test dropping on self (no position change):
        dragInto(true,
        // Bounds of the dragged item
                new Rect(0, 100, 100, 80),
                // Drag point
                new Point(0, 210),
                // Expected insert location
                1,
                // Dragging from same pos
                1,
                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop zones - these are different because we exclude drop
                // zones around the
                // dragged item itself (it doesn't make sense to insert directly
                // before or after
                // myself
                "useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,290,240,290), "
                        + "drawLine(0,381,240,381)",

                // No active nearest line when you're over the self pos!

                // Preview of the dropped rectangle
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawRect(Rect[0,100,100,80])");
    }

    // Left to test:
    // Check inserting at last pos with multiple children
    // Check inserting with no bounds rectangle for dragged element
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttribute.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttribute.java
new file mode 100644
//Synthetic comment -- index 0000000..fc59ba8

//Synthetic comment -- @@ -0,0 +1,54 @@
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

import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.INode.IAttribute;

/** Test/mock implementation of {@link IAttribute} and {@link IDragAttribute} */
public class TestAttribute implements IAttribute, IDragAttribute {
    private String mUri;

    private String mName;

    private String mValue;

    public TestAttribute(String mUri, String mName, String mValue) {
        super();
        this.mName = mName;
        this.mUri = mUri;
        this.mValue = mValue;
    }

    public String getName() {
        return mName;
    }

    public String getUri() {
        return mUri;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "TestAttribute [name=" + mName + ", uri=" + mUri + ", value=" + mValue + "]";
    }


}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..a864764

//Synthetic comment -- @@ -0,0 +1,56 @@
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

import com.android.ide.common.api.IAttributeInfo;

/** Test/mock implementation of {@link IAttributeInfo} */
public class TestAttributeInfo implements IAttributeInfo {
    private final String mName;

    public TestAttributeInfo(String name) {
        this.mName = name;
    }

    public String getDeprecatedDoc() {
        BaseLayoutTest.fail("Not supported yet in tests");
        return null;
    }

    public String[] getEnumValues() {
        BaseLayoutTest.fail("Not supported yet in tests");
        return null;
    }

    public String[] getFlagValues() {
        BaseLayoutTest.fail("Not supported yet in tests");
        return null;
    }

    public Format[] getFormats() {
        BaseLayoutTest.fail("Not supported yet in tests");
        return null;
    }

    public String getJavaDoc() {
        BaseLayoutTest.fail("Not supported yet in tests");
        return null;
    }

    public String getName() {
        return mName;
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestColor.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestColor.java
new file mode 100644
//Synthetic comment -- index 0000000..449ad5e

//Synthetic comment -- @@ -0,0 +1,36 @@
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

import com.android.ide.common.api.IColor;

public class TestColor implements IColor {
    private int mRgb;

    public TestColor(int rgb) {
        this.mRgb = rgb;
    }

    public int getRgb() {
        return mRgb;
    }

    @Override
    public String toString() {
        return String.format("#%6x", mRgb);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java
new file mode 100644
//Synthetic comment -- index 0000000..b113ced

//Synthetic comment -- @@ -0,0 +1,142 @@
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

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Test/mock implementation of {@link IDragElement} */
public class TestDragElement implements IDragElement {
    private Rect mRect;

    private final String mFqcn;

    private Map<String, TestAttribute> mAttributes = new HashMap<String, TestAttribute>();

    private List<TestDragElement> mChildren = new ArrayList<TestDragElement>();

    private TestDragElement mParent;

    public TestDragElement(String mFqcn, Rect mRect, List<TestDragElement> mChildren,
            TestDragElement mParent) {
        super();
        this.mRect = mRect;
        this.mFqcn = mFqcn;
        this.mChildren = mChildren;
        this.mParent = mParent;
    }

    public TestDragElement(String fqn) {
        this(fqn, null, null, null);
    }

    public TestDragElement setBounds(Rect bounds) {
        this.mRect = bounds;

        return this;
    }

    // Builder stuff
    public TestDragElement set(String uri, String name, String value) {
        if (mAttributes == null) {
            mAttributes = new HashMap<String, TestAttribute>();
        }

        mAttributes.put(uri + name, new TestAttribute(uri, name, value));

        return this;
    }

    public TestDragElement add(TestDragElement... children) {
        if (mChildren == null) {
            mChildren = new ArrayList<TestDragElement>();
        }

        for (TestDragElement child : children) {
            mChildren.add(child);
            child.mParent = this;
        }

        return this;
    }

    public TestDragElement id(String id) {
        return set(BaseView.ANDROID_URI, BaseView.ATTR_ID, id);
    }

    public static TestDragElement create(String fqn, Rect bounds) {
        return create(fqn).setBounds(bounds);
    }

    public static TestDragElement create(String fqn) {
        return new TestDragElement(fqn);
    }

    public static IDragElement[] create(TestDragElement... elements) {
        return elements;
    }

    // ==== IDragElement ====

    public IDragAttribute getAttribute(String uri, String localName) {
        if (mAttributes == null) {
            return new TestAttribute(uri, localName, "");
        }

        return mAttributes.get(uri + localName);
    }

    public IDragAttribute[] getAttributes() {
        return mAttributes.values().toArray(new IDragAttribute[mAttributes.size()]);
    }

    public Rect getBounds() {
        return mRect;
    }

    public String getFqcn() {
        return mFqcn;
    }

    public IDragElement[] getInnerElements() {
        if (mChildren == null) {
            return new IDragElement[0];
        }

        return mChildren.toArray(new IDragElement[mChildren.size()]);
    }

    public Rect getParentBounds() {
        return mParent != null ? mParent.getBounds() : null;
    }

    public String getParentFqcn() {
        return mParent != null ? mParent.getFqcn() : null;
    }

    @Override
    public String toString() {
        return "TestDragElement [fqn=" + mFqcn + ", attributes=" + mAttributes + ", bounds="
                + mRect + "]";
    }


}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestGraphics.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestGraphics.java
new file mode 100644
//Synthetic comment -- index 0000000..b82f309

//Synthetic comment -- @@ -0,0 +1,148 @@
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

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.IColor;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: Create box of ascii art

public class TestGraphics implements IGraphics {
    /** List of things we have drawn */
    private List<String> mDrawn = new ArrayList<String>();

    private IColor mBackground = new TestColor(0x000000);

    private IColor mForeground = new TestColor(0xFFFFFF);

    private int mAlpha = 128;

    /** Return log of graphics calls */
    public List<String> getDrawn() {
        return Collections.unmodifiableList(mDrawn);
    }

    /** Wipe out log of graphics calls */
    public void clear() {
        mDrawn.clear();
    }

    // ==== IGraphics ====

    public void drawBoxedStrings(int x, int y, List<?> strings) {
        mDrawn.add("drawBoxedStrings(" + x + "," + y + "," + strings + ")");
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        mDrawn.add("drawLine(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    public void drawLine(Point p1, Point p2) {
        mDrawn.add("drawLine(" + p1 + "," + p2 + ")");
    }

    public void drawRect(int x1, int y1, int x2, int y2) {
        mDrawn.add("drawRect(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    public void drawRect(Point p1, Point p2) {
        mDrawn.add("drawRect(" + p1 + "," + p2 + ")");
    }

    public void drawRect(Rect r) {
        mDrawn.add("drawRect(" + rectToString(r) + ")");
    }

    public void drawString(String string, int x, int y) {
        mDrawn.add("drawString(" + x + "," + y + "," + string + ")");
    }

    public void drawString(String string, Point topLeft) {
        mDrawn.add("drawString(" + string + "," + topLeft + ")");
    }

    public void fillRect(int x1, int y1, int x2, int y2) {
        mDrawn.add("fillRect(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    public void fillRect(Point p1, Point p2) {
        mDrawn.add("fillRect(" + p1 + "," + p2 + ")");
    }

    public void fillRect(Rect r) {
        mDrawn.add("fillRect(" + rectToString(r) + ")");
    }

    public int getAlpha() {
        return mAlpha;
    }

    public IColor getBackground() {
        return mBackground;
    }

    public int getFontHeight() {
        return 12;
    }

    public IColor getForeground() {
        return mForeground;
    }

    public IColor registerColor(int rgb) {
        mDrawn.add("registerColor(" + Integer.toHexString(rgb) + ")");
        return new TestColor(rgb);
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
        mDrawn.add("setAlpha(" + alpha + ")");
    }

    public void setBackground(IColor color) {
        mDrawn.add("setBackground(" + color + ")");
        mBackground = color;
    }

    public void setForeground(IColor color) {
        mDrawn.add("setForeground(" + color + ")");
        mForeground = color;
    }

    public void setLineStyle(LineStyle style) {
        mDrawn.add("setLineStyle(" + style + ")");
    }

    public void setLineWidth(int width) {
        mDrawn.add("setLineWidth(" + width + ")");
    }

    public void useStyle(DrawingStyle style) {
        mDrawn.add("useStyle(" + style + ")");
    }

    private static String rectToString(Rect rect) {
        return "Rect[" + rect.x + "," + rect.y + "," + rect.w + "," + rect.h + "]";
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
new file mode 100644
//Synthetic comment -- index 0000000..21250de

//Synthetic comment -- @@ -0,0 +1,164 @@
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

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Test/mock implementation of {@link INode} */
public class TestNode implements INode {
    private TestNode mParent;

    private final List<TestNode> mChildren = new ArrayList<TestNode>();

    private final String mFqcn;

    private Rect mBounds = new Rect(); // Invalid bounds initially

    private Map<String, IAttribute> mAttributes = new HashMap<String, IAttribute>();

    private Map<String, IAttributeInfo> mAttributeInfos = new HashMap<String, IAttributeInfo>();

    public TestNode(String fqcn) {
        this.mFqcn = fqcn;
    }

    public TestNode bounds(Rect bounds) {
        this.mBounds = bounds;

        return this;
    }

    public TestNode id(String id) {
        return set(BaseView.ANDROID_URI, BaseView.ATTR_ID, id);
    }

    public TestNode set(String uri, String name, String value) {
        setAttribute(uri, name, value);

        return this;
    }

    public TestNode add(TestNode child) {
        mChildren.add(child);
        child.mParent = this;

        return this;
    }

    public TestNode add(TestNode... children) {
        for (TestNode child : children) {
            mChildren.add(child);
            child.mParent = this;
        }

        return this;
    }

    public static TestNode create(String fcqn) {
        return new TestNode(fcqn);
    }

    public void removeChild(int index) {
        TestNode removed = mChildren.remove(index);
        removed.mParent = null;
    }

    // ==== INODE ====

    public INode appendChild(String viewFqcn) {
        return insertChildAt(viewFqcn, mChildren.size());
    }

    public void editXml(String undoName, INodeHandler callback) {
        callback.handle(this);
    }

    public IAttributeInfo getAttributeInfo(String uri, String attrName) {
        return mAttributeInfos.get(uri + attrName);
    }

    public Rect getBounds() {
        return mBounds;
    }

    public INode[] getChildren() {
        return mChildren.toArray(new INode[mChildren.size()]);
    }

    public IAttributeInfo[] getDeclaredAttributes() {
        return mAttributeInfos.values().toArray(new IAttributeInfo[mAttributeInfos.size()]);
    }

    public String getFqcn() {
        return mFqcn;
    }

    public IAttribute[] getLiveAttributes() {
        return mAttributes.values().toArray(new IAttribute[mAttributes.size()]);
    }

    public INode getParent() {
        return mParent;
    }

    public INode getRoot() {
        TestNode curr = this;
        while (curr.mParent != null) {
            curr = curr.mParent;
        }

        return curr;
    }

    public String getStringAttr(String uri, String attrName) {
        IAttribute attr = mAttributes.get(uri + attrName);
        if (attr == null) {
            return null;
        }

        return attr.getValue();
    }

    public INode insertChildAt(String viewFqcn, int index) {
        TestNode child = new TestNode(viewFqcn);
        if (index == -1) {
            mChildren.add(child);
        } else {
            mChildren.add(index, child);
        }
        child.mParent = this;
        return child;
    }

    public boolean setAttribute(String uri, String localName, String value) {
        mAttributes.put(uri + localName, new TestAttribute(uri, localName, value));
        return true;
    }

    @Override
    public String toString() {
        return "TestNode [fqn=" + mFqcn + ", infos=" + mAttributeInfos
                + ", attributes=" + mAttributes + ", bounds=" + mBounds + "]";
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/ZoomControlsRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/ZoomControlsRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..ee08633

//Synthetic comment -- @@ -0,0 +1,50 @@
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

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;

/** Test the {@link ZoomControlsRule} */
public class ZoomControlsRuleTest extends AbstractLayoutRuleTest {
    public void testDoNothing() {
        String draggedButtonId = "@+id/DraggedButton";

        IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button").id(draggedButtonId));

        INode layout = TestNode.create("android.widget.ZoomControls").id("@+id/ZoomControls01")
        .bounds(new Rect(0, 0, 240, 480)).add(
                TestNode.create("android.widget.Button").id("@+id/Button01").bounds(
                        new Rect(0, 0, 100, 80)),
                TestNode.create("android.widget.Button").id("@+id/Button02").bounds(
                        new Rect(0, 100, 100, 80)),
                TestNode.create("android.widget.Button").id("@+id/Button03").bounds(
                        new Rect(0, 200, 100, 80)),
                TestNode.create("android.widget.Button").id("@+id/Button04").bounds(
                        new Rect(0, 300, 100, 80)));

        ZoomControlsRule rule = new ZoomControlsRule();

        // Enter target
        DropFeedback feedback = rule.onDropEnter(layout, elements);
        // Zoom controls don't respond to drags
        assertNull(feedback);
    }
}







