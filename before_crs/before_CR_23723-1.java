/*Add two more layout actions for linear layouts

Add two more actions:

(1) Clear All Weights: This removes all the layout weights in a
    layout, and converts and 0-sized views to wrap_content.

(2) Assign All Weight: This adds all the weight in the layout to the
    selected view(s) and removes it from the remaining views.

Change-Id:Id2a27299d99f77ef4056b7e1745373d52a9331f7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 47c5499..00b8e53 100644

//Synthetic comment -- @@ -67,6 +67,8 @@
private static final String ACTION_WEIGHT = "_weight"; //$NON-NLS-1$
private static final String ACTION_DISTRIBUTE = "_distribute"; //$NON-NLS-1$
private static final String ACTION_BASELINE = "_baseline"; //$NON-NLS-1$

private static final URL ICON_HORIZONTAL =
LinearLayoutRule.class.getResource("hlinear.png"); //$NON-NLS-1$
//Synthetic comment -- @@ -78,6 +80,10 @@
LinearLayoutRule.class.getResource("distribute.png"); //$NON-NLS-1$
private static final URL ICON_BASELINE =
LinearLayoutRule.class.getResource("baseline.png"); //$NON-NLS-1$

/**
* Add an explicit Orientation toggle to the context menu.
//Synthetic comment -- @@ -184,7 +190,8 @@
final Boolean newValue) {
parentNode.editXml("Change Weight", new INodeHandler() {
public void handle(INode n) {
                            if (action.getId().equals(ACTION_WEIGHT)) {
String weight =
children.get(0).getStringAttr(ANDROID_URI, ATTR_LAYOUT_WEIGHT);
if (weight == null || weight.length() == 0) {
//Synthetic comment -- @@ -198,45 +205,16 @@
ATTR_LAYOUT_WEIGHT, weight);
}
}
                            } else if (action.getId().equals(ACTION_DISTRIBUTE)) {
                                // Any XML to get weight sum?
                                String weightSum = parentNode.getStringAttr(ANDROID_URI,
                                        ATTR_WEIGHT_SUM);
                                double sum = -1.0;
                                if (weightSum != null) {
                                    // Distribute
                                    try {
                                        sum = Double.parseDouble(weightSum);
                                    } catch (NumberFormatException nfe) {
                                        // Just keep using the default
                                    }
                                }
                                INode[] targets = parentNode.getChildren();
                                int numTargets = targets.length;
                                double share;
                                if (sum <= 0.0) {
                                    // The sum will be computed from the children, so just
                                    // use arbitrary amount
                                    share = 1.0;
                                } else {
                                    share = sum / numTargets;
                                }
                                String value = formatFloatAttribute((float) share);
                                String sizeAttribute = isVertical(parentNode) ?
                                        ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH;
                                for (INode target : targets) {
                                    target.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, value);
                                    // Also set the width/height to 0dp to ensure actual equal
                                    // size (without this, only the remaining space is
                                    // distributed)
                                    if (VALUE_WRAP_CONTENT.equals(
                                            target.getStringAttr(ANDROID_URI, sizeAttribute))) {
                                        target.setAttribute(ANDROID_URI,
                                                sizeAttribute, VALUE_ZERO_DP);
                                    }
                                }
} else {
                                assert action.getId().equals(ACTION_BASELINE);
}
}
});
//Synthetic comment -- @@ -245,8 +223,61 @@
actions.add(MenuAction.createSeparator(50));
actions.add(MenuAction.createAction(ACTION_DISTRIBUTE, "Distribute Weights Evenly",
null, actionCallback, ICON_DISTRIBUTE, 60));
actions.add(MenuAction.createAction(ACTION_WEIGHT, "Change Layout Weight", null,
                    actionCallback, ICON_WEIGHTS, 70));
}
}








