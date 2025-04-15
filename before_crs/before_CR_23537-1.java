/*Guidelines for match_parent, and linear layout weight fixes

First, add guidelines to allow snapping to "match_parent" (or
fill_parent, depending on the API level).

Second, fix the linear layout resizing scheme to handle corner cases a
bit better (corner cases such as resizing to a smaller size than the
wrap_content bounds, or resizing inside a layout that is "full"). Also
split up the resizing code into a compute-method and an apply-method
such that we can display feedback for the current weight during the
resizing operation.

Change-Id:Idd2917230870d26f94473dabc1a2a6becc3ba738*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 111434f..de03a19 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_X;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_Y;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -239,9 +238,6 @@
return String.format("Set bounds to (x = %d, y = %d, width = %s, height = %s)",
mRulesEngine.pxToDp(newBounds.x - parentBounds.x),
mRulesEngine.pxToDp(newBounds.y - parentBounds.y),
                resizeState.wrapWidth ?
                        VALUE_WRAP_CONTENT : Integer.toString(mRulesEngine.pxToDp(newBounds.w)),
                resizeState.wrapHeight ?
                        VALUE_WRAP_CONTENT : Integer.toString(mRulesEngine.pxToDp(newBounds.h)));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index 4e6ea98..fa87fa4 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.DrawingStyle;
//Synthetic comment -- @@ -44,10 +43,12 @@
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.MenuAction.ChoiceProvider;
import com.android.sdklib.SdkConstants;
import com.android.util.Pair;

//Synthetic comment -- @@ -575,31 +576,15 @@

// ---- Resizing ----

    /** State held during resizing operations */
    protected static class ResizeState {
        /** The proposed resized bounds of the node */
        public Rect bounds;

        /** The preferred wrap_content bounds of the node */
        public Rect wrapBounds;

        /** The type of horizontal edge being resized, or null */
        public SegmentType horizontalEdgeType;

        /** The type of vertical edge being resized, or null */
        public SegmentType verticalEdgeType;

        /** Whether the user has snapped to the wrap_content width */
        public boolean wrapWidth;

        /** Whether the user has snapped to the wrap_content height */
        public boolean wrapHeight;
}

@Override
public DropFeedback onResizeBegin(INode child, INode parent,
SegmentType horizontalEdge, SegmentType verticalEdge) {
        ResizeState state = new ResizeState();
state.horizontalEdgeType = horizontalEdge;
state.verticalEdgeType = verticalEdge;

//Synthetic comment -- @@ -633,6 +618,17 @@
Rect b = resizeState.bounds;
gc.drawRect(b);

if (resizeState.wrapBounds != null) {
gc.useStyle(DrawingStyle.GUIDELINE);
int wrapWidth = resizeState.wrapBounds.w;
//Synthetic comment -- @@ -703,7 +699,6 @@
return 20;
}


@Override
public void onResizeUpdate(DropFeedback feedback, INode child, INode parent,
Rect newBounds, int modifierMask) {
//Synthetic comment -- @@ -735,6 +730,32 @@
}
}

feedback.message = getResizeUpdateMessage(state, child, parent,
newBounds, state.horizontalEdgeType, state.verticalEdgeType);
}
//Synthetic comment -- @@ -767,10 +788,8 @@
*/
protected String getResizeUpdateMessage(ResizeState resizeState, INode child, INode parent,
Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {
        String width = resizeState.wrapWidth ? VALUE_WRAP_CONTENT :
                    String.format(VALUE_N_DP, mRulesEngine.pxToDp(newBounds.w));
        String height = resizeState.wrapHeight ? VALUE_WRAP_CONTENT :
            String.format(VALUE_N_DP, mRulesEngine.pxToDp(newBounds.h));

// U+00D7: Unicode for multiplication sign
return String.format("Resize to %s \u00D7 %s", width, height);
//Synthetic comment -- @@ -790,15 +809,13 @@
*/
protected void setNewSizeBounds(ResizeState resizeState, INode node, INode layout,
Rect oldBounds, Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {
        if (verticalEdge != null && (newBounds.w != oldBounds.w || resizeState.wrapWidth)) {
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
                    resizeState.wrapWidth ? VALUE_WRAP_CONTENT :
                        String.format(VALUE_N_DP, mRulesEngine.pxToDp(newBounds.w)));
}
        if (horizontalEdge != null && (newBounds.h != oldBounds.h || resizeState.wrapHeight)) {
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
                    resizeState.wrapHeight ? VALUE_WRAP_CONTENT :
                        String.format(VALUE_N_DP, mRulesEngine.pxToDp(newBounds.h)));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 4b13b84..77cd7fe 100644

//Synthetic comment -- @@ -25,9 +25,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_WEIGHT_SUM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.DrawingStyle;
//Synthetic comment -- @@ -685,60 +683,128 @@
}
}

    @Override
    public DropFeedback onResizeBegin(INode child, INode parent, SegmentType horizontalEdge,
            SegmentType verticalEdge) {
        return super.onResizeBegin(child, parent, horizontalEdge, verticalEdge);
}

    /**
     * {@inheritDoc}
     * <p>
     * Overridden in this layout in order to make resizing affect the layout_weight
     * attribute instead of the layout_width (for horizontal LinearLayouts) or
     * layout_height (for vertical LinearLayouts).
     */
@Override
    protected void setNewSizeBounds(ResizeState resizeState, INode node, INode layout,
            Rect previousBounds, Rect newBounds, SegmentType horizontalEdge,
SegmentType verticalEdge) {
        final Rect oldBounds = node.getBounds();
if (oldBounds.equals(newBounds)) {
return;
}
        // Handle resizing in the opposite dimension of the layout
        boolean isVertical = isVertical(layout);
        if (!isVertical && horizontalEdge != null) {
            if (newBounds.h != oldBounds.h || resizeState.wrapHeight) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
                        resizeState.wrapHeight ? VALUE_WRAP_CONTENT :
                            String.format(VALUE_N_DP, mRulesEngine.pxToDp(newBounds.h)));
            }
            if (verticalEdge == null) {
                return;
            }
            // else: fall through to compute a dynamic weight
        }
        if (isVertical && verticalEdge != null) {
            if (newBounds.w != oldBounds.w || resizeState.wrapWidth) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
                        resizeState.wrapWidth ? VALUE_WRAP_CONTENT :
                            String.format(VALUE_N_DP, mRulesEngine.pxToDp(newBounds.w)));
            }
            if (horizontalEdge == null) {
                return;
            }
        }

        // If we're setting the width/height to wrap_content in the dimension of the
// linear layout, then just apply wrap_content and clear weights.
if (!isVertical && verticalEdge != null) {
            if (resizeState.wrapWidth) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_WRAP_CONTENT);
                // Clear weight
                if (getWeight(node) > 0.0f) {
                    node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, null);
                }
return;
}
if (newBounds.w == oldBounds.w) {
//Synthetic comment -- @@ -747,12 +813,8 @@
}

if (isVertical && horizontalEdge != null) {
            if (resizeState.wrapHeight) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_WRAP_CONTENT);
                // Clear weight
                if (getWeight(node) > 0.0f) {
                    node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, null);
                }
return;
}
if (newBounds.h == oldBounds.h) {
//Synthetic comment -- @@ -760,44 +822,30 @@
}
}

float sum = getWeightSum(layout);
if (sum <= 0.0f) {
sum = 1.0f;
            layout.setAttribute(ANDROID_URI, ATTR_WEIGHT_SUM, formatFloatAttribute(sum));
}

        Map<INode, Rect> sizes = mRulesEngine.measureChildren(layout,
                new IClientRulesEngine.AttributeFilter() {
                    public String getAttribute(INode n, String namespace, String localName) {
                        // Clear out layout weights; we need to measure the unweighted sizes
                        // of the children
                        if (ATTR_LAYOUT_WEIGHT.equals(localName)
                                && SdkConstants.NS_RESOURCES.equals(namespace)) {
                            return ""; //$NON-NLS-1$
                        }

                        return null;
                    }
                });
        int totalLength = 0;
        for (Map.Entry<INode, Rect> entry : sizes.entrySet()) {
            Rect preferredSize = entry.getValue();
            if (isVertical) {
                totalLength += preferredSize.h;
            } else {
                totalLength += preferredSize.w;
}
}

Rect layoutBounds = layout.getBounds();
        int remaining = (isVertical ? layoutBounds.h : layoutBounds.w) - totalLength;
Rect nodeBounds = sizes.get(node);
if (nodeBounds == null) {
            super.setNewSizeBounds(resizeState, node, layout, oldBounds, newBounds, horizontalEdge,
                    verticalEdge);
return;
}
        assert nodeBounds != null;

if (remaining > 0) {
int missing = 0;
//Synthetic comment -- @@ -812,7 +860,7 @@
// smaller size.
missing = newBounds.h - resizeState.wrapBounds.h;
remaining += nodeBounds.h - resizeState.wrapBounds.h;
                    node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_WRAP_CONTENT);
}
} else {
if (newBounds.w > nodeBounds.w) {
//Synthetic comment -- @@ -820,61 +868,85 @@
} else if (newBounds.w > resizeState.wrapBounds.w) {
missing = newBounds.w - resizeState.wrapBounds.w;
remaining += nodeBounds.w - resizeState.wrapBounds.w;
                    node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_WRAP_CONTENT);
}
}
if (missing > 0) {
// (weight / weightSum) * remaining = missing, so
// weight = missing * weightSum / remaining
float weight = missing * sum / remaining;
                String value = weight > 0 ? formatFloatAttribute(weight) : null;
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, value);
}
        } else {
            // TODO: This algorithm should be refined.
            // One possible solution is to clear the weights and sizes of all children
            // to the left or right of the resized node (depending on whether the right
            // or left edge was resized - the key point being that the other edge should
            // not move).

            // There is no leftover space after adding up the wrap-content sizes of the
            // children. In that case, just make the weight of this child the same proportion
            // of the sum-of-weights as its new size is out of the parent size.

            // Use actual sum of weights, not the declared sum on the parent layout,
            // to get the proportions right
            float otherSum = 0.0f;
            for (INode child : layout.getChildren()) {
                if (child != node) {
                    otherSum += getWeight(child);
}
}

            float newSize = isVertical ? newBounds.h : newBounds.w;
            float totalSize = isVertical ? layoutBounds.h : layoutBounds.w;
            float weight;
            if (newSize >= totalSize) {
                // The new view was resized to something larger than the layout itself;
                // that obviously can't be achieved with layout weights, so just pick
                // something large to give it a lot of space but not all.
                weight = 10 * otherSum;
            } else {
                weight = newSize * otherSum / (totalSize - newSize);
}
            String value = weight > 0 ? formatFloatAttribute(weight) : null;
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, value);
            String fill = getFillParentValueName();
            node.setAttribute(ANDROID_URI, isVertical ? ATTR_LAYOUT_WEIGHT : ATTR_LAYOUT_WIDTH,
                    fill);
}
}

@Override
    protected String getResizeUpdateMessage(ResizeState resizeState, INode child, INode parent,
Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {
        return super.getResizeUpdateMessage(resizeState, child, parent, newBounds,
horizontalEdge, verticalEdge);
        // TODO: Change message to display the current layout weight instead
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ResizeState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ResizeState.java
new file mode 100644
//Synthetic comment -- index 0000000..11f3ec6

//Synthetic comment -- @@ -0,0 +1,117 @@
\ No newline at end of file







