/*27869: Lint rule for LinearLayout which doesn't specify orientation. DO NOT MERGE

This adds a check for cases where you have a LinearLayout which
does not specify an orientation (meaning it's horizontal), yet
it contains multiple children, and one of the children prior to
the last one sets its width to fill_parent or match_parent (without
a weight).

This is a common pitfall for new developers who create the layout
and assume it's vertical, and at runtime only see the first child,
not realizing the remainder are off screen to the right.

Change-Id:Ic62e48464d7d37e3492719db9c18bc3097ae8382*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/InefficientWeightDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/InefficientWeightDetectorTest.java
//Synthetic comment -- index 644bb10..c642ab1 100644

//Synthetic comment -- @@ -27,14 +27,16 @@

public void testWeights() throws Exception {
assertEquals(
            "res/layout/inefficient_weight.xml:3: Error: Wrong orientation? No orientation specified, and the default is horizontal, yet this layout has multiple children where at least one has layout_width=\"match_parent\" [Orientation]\n" +
            "<LinearLayout\n" +
            "^\n" +
"res/layout/inefficient_weight.xml:10: Warning: Use a layout_width of 0dip instead of match_parent for better performance [InefficientWeight]\n" +
"     android:layout_width=\"match_parent\"\n" +
"     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"res/layout/inefficient_weight.xml:24: Warning: Use a layout_height of 0dip instead of wrap_content for better performance [InefficientWeight]\n" +
"      android:layout_height=\"wrap_content\"\n" +
"      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "1 errors, 2 warnings\n",
lintFiles("res/layout/inefficient_weight.xml"));
}

//Synthetic comment -- @@ -122,6 +124,4 @@

lintFiles("res/layout/wrong0dp.xml"));
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index b226afc..762446b 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 139;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -77,6 +77,7 @@
issues.add(InefficientWeightDetector.NESTED_WEIGHTS);
issues.add(InefficientWeightDetector.BASELINE_WEIGHTS);
issues.add(InefficientWeightDetector.WRONG_0DP);
        issues.add(InefficientWeightDetector.ORIENTATION);
issues.add(ScrollViewChildDetector.ISSUE);
issues.add(DeprecationDetector.ISSUE);
issues.add(ObsoleteLayoutParamsDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InefficientWeightDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InefficientWeightDetector.java
//Synthetic comment -- index 48e9dc5..fefe461 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import static com.android.SdkConstants.ATTR_ORIENTATION;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.RADIO_GROUP;
import static com.android.SdkConstants.VALUE_FILL_PARENT;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VALUE_VERTICAL;
import static com.android.SdkConstants.VIEW;
import static com.android.SdkConstants.VIEW_FRAGMENT;
//Synthetic comment -- @@ -48,6 +50,7 @@
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//Synthetic comment -- @@ -115,6 +118,24 @@
InefficientWeightDetector.class,
Scope.RESOURCE_FILE_SCOPE);

    /** Missing explicit orientation */
    public static final Issue ORIENTATION = Issue.create(
            "Orientation", //$NON-NLS-1$
            "Checks that LinearLayouts with multiple children set the orientation",

            "The default orientation of a LinearLayout is horizontal. It's pretty easy to "
            + "believe that the layout is vertical, add multiple children to it, and wonder "
            + "why only the first child is visible (when the subsequent children are "
            + "off screen to the right). This lint rule helps pinpoint this issue by "
            + "warning whenever a LinearLayout is used with an implicit orientation "
            + "and multiple children.",

            Category.CORRECTNESS,
            2,
            Severity.ERROR,
            InefficientWeightDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/**
* Map from element to whether that element has a non-zero linear layout
* weight or has an ancestor which does
//Synthetic comment -- @@ -170,8 +191,40 @@
}
}

        String orientation = element.getAttributeNS(ANDROID_URI, ATTR_ORIENTATION);
        if (children.size() >= 2 && (orientation == null || orientation.isEmpty())
                && context.isEnabled(ORIENTATION)) {
            // See if at least one of the children, except the last one, sets layout_width
            // to match_parent (or fill_parent), in an implicitly horizontal layout, since
            // that might mean the last child won't be visible. This is a source of confusion
            // for new Android developers.
            boolean maxWidthSet = false;
            Iterator<Element> iterator = children.iterator();
            while (iterator.hasNext()) {
                Element child = iterator.next();
                if (!iterator.hasNext()) { // Don't check the last one
                    break;
                }
                String width = child.getAttributeNS(ANDROID_URI, ATTR_LAYOUT_WIDTH);
                if (VALUE_MATCH_PARENT.equals(width) || VALUE_FILL_PARENT.equals(width)) {
                    // Also check that weights are not set here; this affects the computation
                    // a bit and the child may not fill up the whole linear layout
                    if (!child.hasAttributeNS(ANDROID_URI, ATTR_LAYOUT_WEIGHT)) {
                        maxWidthSet = true;
                        break;
                    }
                }
            }
            if (maxWidthSet) {
                String message = "Wrong orientation? No orientation specified, and the default "
                        + "is horizontal, yet this layout has multiple children where at "
                        + "least one has layout_width=\"match_parent\"";
                context.report(ORIENTATION, element, context.getLocation(element), message, null);
            }
        }

if (context.isEnabled(BASELINE_WEIGHTS) && weightChild != null
                && !VALUE_VERTICAL.equals(orientation)
&& !element.hasAttributeNS(ANDROID_URI, ATTR_BASELINE_ALIGNED)) {
// See if all the children are layouts
boolean allChildrenAreLayouts = !children.isEmpty();
//Synthetic comment -- @@ -200,7 +253,7 @@
if (context.isEnabled(INEFFICIENT_WEIGHT)
&& weightChild != null && !multipleWeights) {
String dimension;
            if (VALUE_VERTICAL.equals(orientation)) {
dimension = ATTR_LAYOUT_HEIGHT;
} else {
dimension = ATTR_LAYOUT_WIDTH;







