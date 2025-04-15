/*When distributing linear layout weights, also set size to 0dp

When you use the layout actions bar "Distribute Weights" in a
LinearLayout, in addition to setting all the weights to the same
nonzero value, also set the size (the height for a vertical linear
layout and the width for a horizontal one) to 0dp, to ensure that the
widgets are all given the same total size rather than sharing the
remaining space evenly.

In addition, when adding new widgets to a LinearLayout, see if all
elements in the linear layout already have nonzero and equal weights,
and if so duplicate this weight value on the new widget as well, and
similarly also duplicate 0dp/0dip/0px if used.

Change-Id:Iac5284a866b19b27e91666dc62278c63b8dde2bb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 9061702..0ef178d 100644

//Synthetic comment -- @@ -106,6 +106,7 @@
public static final String VALUE_TRUE = "true";                             //$NON-NLS-1$
public static final String VALUE_FALSE= "false";                            //$NON-NLS-1$
public static final String VALUE_N_DP = "%ddp";                             //$NON-NLS-1$
    public static final String VALUE_ZERO_DP = "0dp";                           //$NON-NLS-1$

// Gravity values. These have the GRAVITY_ prefix in front of value because we already
// have VALUE_CENTER_HORIZONTAL defined for layouts, and its definition conflicts








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index ac4b6ff..47c5499 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_WEIGHT_SUM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ZERO_DP;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.DrawingStyle;
//Synthetic comment -- @@ -220,8 +222,18 @@
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
//Synthetic comment -- @@ -541,6 +553,40 @@
node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, fillParent);
}
}

        // If you insert into a layout that already is using layout weights,
        // and all the layout weights are the same (nonzero) value, then use
        // the same weight for this new layout as well. Also duplicate the 0dip/0px/0dp
        // sizes, if used.
        boolean duplicateWeight = true;
        boolean duplicate0dip = true;
        String sameWeight = null;
        String sizeAttribute = isVertical(parent) ? ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH;
        for (INode target : parent.getChildren()) {
            if (target == node) {
                continue;
            }
            String weight = target.getStringAttr(ANDROID_URI, ATTR_LAYOUT_WEIGHT);
            if (weight == null || weight.length() == 0) {
                duplicateWeight = false;
                break;
            } else if (sameWeight != null && !sameWeight.equals(weight)) {
                duplicateWeight = false;
            } else {
                sameWeight = weight;
            }
            String size = target.getStringAttr(ANDROID_URI, sizeAttribute);
            if (size != null && !size.startsWith("0")) { //$NON-NLS-1$
                duplicate0dip = false;
                break;
            }
        }
        if (duplicateWeight && sameWeight != null) {
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, sameWeight);
            if (duplicate0dip) {
                node.setAttribute(ANDROID_URI, sizeAttribute, VALUE_ZERO_DP);
            }
        }
}

/** A possible match position */







