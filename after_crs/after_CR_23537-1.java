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

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -239,9 +238,6 @@
return String.format("Set bounds to (x = %d, y = %d, width = %s, height = %s)",
mRulesEngine.pxToDp(newBounds.x - parentBounds.x),
mRulesEngine.pxToDp(newBounds.y - parentBounds.y),
                resizeState.getWidthAttribute(), resizeState.getHeightAttribute());
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index 4e6ea98..fa87fa4 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.DrawingStyle;
//Synthetic comment -- @@ -44,10 +43,12 @@
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.Segment;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.MenuAction.ChoiceProvider;
import com.android.ide.common.layout.relative.MarginType;
import com.android.sdklib.SdkConstants;
import com.android.util.Pair;

//Synthetic comment -- @@ -575,31 +576,15 @@

// ---- Resizing ----

    /** Creates a new {@link ResizeState} object to track resize state */
    protected ResizeState createResizeState(INode layout, INode node) {
        return new ResizeState(this, layout, node);
}

@Override
public DropFeedback onResizeBegin(INode child, INode parent,
SegmentType horizontalEdge, SegmentType verticalEdge) {
        ResizeState state = createResizeState(parent, child);
state.horizontalEdgeType = horizontalEdge;
state.verticalEdgeType = verticalEdge;

//Synthetic comment -- @@ -633,6 +618,17 @@
Rect b = resizeState.bounds;
gc.drawRect(b);

                    if (resizeState.horizontalFillSegment != null) {
                        gc.useStyle(DrawingStyle.GUIDELINE);
                        Segment s = resizeState.horizontalFillSegment;
                        gc.drawLine(s.from, s.at, s.to, s.at);
                    }
                    if (resizeState.verticalFillSegment != null) {
                        gc.useStyle(DrawingStyle.GUIDELINE);
                        Segment s = resizeState.verticalFillSegment;
                        gc.drawLine(s.at, s.from, s.at, s.to);
                    }

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

        // Match on fill bounds
        state.horizontalFillSegment = null;
        state.fillHeight = false;
        if (state.horizontalEdgeType == SegmentType.BOTTOM && !state.wrapHeight) {
            Rect parentBounds = parent.getBounds();
            state.horizontalFillSegment = new Segment(parentBounds.y2(), newBounds.x,
                newBounds.x2(),
                null /*node*/, null /*id*/, SegmentType.BOTTOM, MarginType.NO_MARGIN);
            if (Math.abs(newBounds.y2() - parentBounds.y2()) < getMaxMatchDistance()) {
                state.fillHeight = true;
                newBounds.h = parentBounds.y2() - newBounds.y;
            }
        }
        state.verticalFillSegment = null;
        state.fillWidth = false;
        if (state.verticalEdgeType == SegmentType.RIGHT && !state.wrapWidth) {
            Rect parentBounds = parent.getBounds();
            state.verticalFillSegment = new Segment(parentBounds.x2(), newBounds.y,
                newBounds.y2(),
                null /*node*/, null /*id*/, SegmentType.RIGHT, MarginType.NO_MARGIN);
            if (Math.abs(newBounds.x2() - parentBounds.x2()) < getMaxMatchDistance()) {
                state.fillWidth = true;
                newBounds.w = parentBounds.x2() - newBounds.x;
            }
        }

feedback.message = getResizeUpdateMessage(state, child, parent,
newBounds, state.horizontalEdgeType, state.verticalEdgeType);
}
//Synthetic comment -- @@ -767,10 +788,8 @@
*/
protected String getResizeUpdateMessage(ResizeState resizeState, INode child, INode parent,
Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {
        String width = resizeState.getWidthAttribute();
        String height = resizeState.getHeightAttribute();

// U+00D7: Unicode for multiplication sign
return String.format("Resize to %s \u00D7 %s", width, height);
//Synthetic comment -- @@ -790,15 +809,13 @@
*/
protected void setNewSizeBounds(ResizeState resizeState, INode node, INode layout,
Rect oldBounds, Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {
        if (verticalEdge != null
            && (newBounds.w != oldBounds.w || resizeState.wrapWidth || resizeState.fillWidth)) {
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, resizeState.getWidthAttribute());
}
        if (horizontalEdge != null
            && (newBounds.h != oldBounds.h || resizeState.wrapHeight || resizeState.fillHeight)) {
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, resizeState.getHeightAttribute());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 4b13b84..77cd7fe 100644

//Synthetic comment -- @@ -25,9 +25,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_WEIGHT_SUM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.DrawingStyle;
//Synthetic comment -- @@ -685,60 +683,128 @@
}
}

    /** Custom resize state used during linear layout resizing */
    private class LinearResizeState extends ResizeState {
        /** Whether the node should be assigned a new weight */
        public boolean useWeight;
        /** Weight sum to be applied to the parent */
        private float mNewWeightSum;
        /** The weight to be set on the node (provided {@link #useWeight} is true) */
        private float mWeight;
        /** Map from nodes to preferred bounds of nodes where the weights have been cleared */
        public final Map<INode, Rect> unweightedSizes;
        /** Total required size required by the siblings <b>without</b> weights */
        public int totalLength;
        /** List of nodes which should have their weights cleared */
        public List<INode> mClearWeights;

        private LinearResizeState(BaseLayoutRule rule, INode layout, INode node) {
            super(rule, layout, node);

            unweightedSizes = mRulesEngine.measureChildren(layout,
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

            // Compute total required size required by the siblings *without* weights
            totalLength = 0;
            final boolean isVertical = isVertical(layout);
            for (Map.Entry<INode, Rect> entry : unweightedSizes.entrySet()) {
                Rect preferredSize = entry.getValue();
                if (isVertical) {
                    totalLength += preferredSize.h;
                } else {
                    totalLength += preferredSize.w;
                }
            }
        }

        /** Resets the computed state */
        void reset() {
            mNewWeightSum = -1;
            useWeight = false;
            mClearWeights = null;
        }

        /** Sets a weight to be applied to the node */
        void setWeight(float weight) {
            useWeight = true;
            mWeight = weight;
        }

        /** Sets a weight sum to be applied to the parent layout */
        void setWeightSum(float weightSum) {
            mNewWeightSum = weightSum;
        }

        /** Marks that the given node should be cleared when applying the new size */
        void clearWeight(INode n) {
            if (mClearWeights == null) {
                mClearWeights = new ArrayList<INode>();
            }
            mClearWeights.add(n);
        }

        /** Applies the state to the nodes */
        public void apply() {
            assert useWeight;

            String value = mWeight > 0 ? formatFloatAttribute(mWeight) : null;
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, value);

            if (mClearWeights != null) {
                for (INode n : mClearWeights) {
                    if (getWeight(n) > 0.0f) {
                        n.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, null);
                    }
                }
            }

            if (mNewWeightSum > 0.0) {
                layout.setAttribute(ANDROID_URI, ATTR_WEIGHT_SUM,
                        formatFloatAttribute(mNewWeightSum));
            }
        }
}

@Override
    protected ResizeState createResizeState(INode layout, INode node) {
        return new LinearResizeState(this, layout, node);
    }

    protected void updateResizeState(LinearResizeState resizeState, final INode node, INode layout,
            Rect oldBounds, Rect newBounds, SegmentType horizontalEdge,
SegmentType verticalEdge) {
        // Update the resize state.
        // This method attempts to compute a new layout weight to be used in the direction
        // of the linear layout. If the superclass has already determined that we can snap to
        // a wrap_content or match_parent boundary, we prefer that. Otherwise, we attempt to
        // compute a layout weight - which can fail if the size is too big (not enough room),
        // or if the size is too small (smaller than the natural width of the node), and so on.
        // In that case this method just aborts, which will leave the resize state object
        // in such a state that it will call the superclass to resize instead, which will fall
        // back to device independent pixel sizing.
        resizeState.reset();

if (oldBounds.equals(newBounds)) {
return;
}

        // If we're setting the width/height to wrap_content/match_parent in the dimension of the
// linear layout, then just apply wrap_content and clear weights.
        boolean isVertical = isVertical(layout);
if (!isVertical && verticalEdge != null) {
            if (resizeState.wrapWidth || resizeState.fillWidth) {
                resizeState.clearWeight(node);
return;
}
if (newBounds.w == oldBounds.w) {
//Synthetic comment -- @@ -747,12 +813,8 @@
}

if (isVertical && horizontalEdge != null) {
            if (resizeState.wrapHeight || resizeState.fillHeight) {
                resizeState.clearWeight(node);
return;
}
if (newBounds.h == oldBounds.h) {
//Synthetic comment -- @@ -760,44 +822,30 @@
}
}

        // Compute weight sum
float sum = getWeightSum(layout);
if (sum <= 0.0f) {
sum = 1.0f;
            resizeState.setWeightSum(sum);
}

        // If the new size of the node is smaller than its preferred/wrap_content size,
        // then we cannot use weights to size it; switch to pixel-based sizing instead
        Map<INode, Rect> sizes = resizeState.unweightedSizes;
        Rect nodePreferredSize = sizes.get(node);
        if (nodePreferredSize != null) {
            if (horizontalEdge != null && newBounds.h < nodePreferredSize.h ||
                    verticalEdge != null && newBounds.w < nodePreferredSize.w) {
                return;
}
}

Rect layoutBounds = layout.getBounds();
        int remaining = (isVertical ? layoutBounds.h : layoutBounds.w) - resizeState.totalLength;
Rect nodeBounds = sizes.get(node);
if (nodeBounds == null) {
return;
}

if (remaining > 0) {
int missing = 0;
//Synthetic comment -- @@ -812,7 +860,7 @@
// smaller size.
missing = newBounds.h - resizeState.wrapBounds.h;
remaining += nodeBounds.h - resizeState.wrapBounds.h;
                    resizeState.wrapHeight = true;
}
} else {
if (newBounds.w > nodeBounds.w) {
//Synthetic comment -- @@ -820,61 +868,85 @@
} else if (newBounds.w > resizeState.wrapBounds.w) {
missing = newBounds.w - resizeState.wrapBounds.w;
remaining += nodeBounds.w - resizeState.wrapBounds.w;
                    resizeState.wrapWidth = true;
}
}
if (missing > 0) {
// (weight / weightSum) * remaining = missing, so
// weight = missing * weightSum / remaining
float weight = missing * sum / remaining;
                resizeState.setWeight(weight);
}
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden in this layout in order to make resizing affect the layout_weight
     * attribute instead of the layout_width (for horizontal LinearLayouts) or
     * layout_height (for vertical LinearLayouts).
     */
    @Override
    protected void setNewSizeBounds(ResizeState state, final INode node, INode layout,
            Rect oldBounds, Rect newBounds, SegmentType horizontalEdge,
            SegmentType verticalEdge) {
        LinearResizeState resizeState = (LinearResizeState) state;
        updateResizeState(resizeState, node, layout, oldBounds, newBounds,
                horizontalEdge, verticalEdge);

        if (resizeState.useWeight) {
            resizeState.apply();

            // Handle resizing in the opposite dimension of the layout
            final boolean isVertical = isVertical(layout);
            if (!isVertical && horizontalEdge != null) {
                if (newBounds.h != oldBounds.h || resizeState.wrapHeight
                        || resizeState.fillHeight) {
                    node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
                            resizeState.getHeightAttribute());
}
}
            if (isVertical && verticalEdge != null) {
                if (newBounds.w != oldBounds.w || resizeState.wrapWidth || resizeState.fillWidth) {
                    node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
                            resizeState.getWidthAttribute());
                }
}
        } else {
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WEIGHT, null);
            super.setNewSizeBounds(resizeState, node, layout, oldBounds, newBounds,
                    horizontalEdge, verticalEdge);
}
}

@Override
    protected String getResizeUpdateMessage(ResizeState state, INode child, INode parent,
Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {
        LinearResizeState resizeState = (LinearResizeState) state;
        updateResizeState(resizeState, child, parent, child.getBounds(), newBounds,
horizontalEdge, verticalEdge);

        if (resizeState.useWeight) {
            String weight = formatFloatAttribute(resizeState.mWeight);
            String dimension = String.format("layout weight %1$s", weight);

            String width;
            String height;
            if (isVertical(parent)) {
                width = resizeState.getWidthAttribute();
                height = dimension;
            } else {
                width = dimension;
                height = resizeState.getHeightAttribute();
            }

            // U+00D7: Unicode for multiplication sign
            return String.format("Resize to %s \u00D7 %s", width, height);
        } else {
            return super.getResizeUpdateMessage(state, child, parent, newBounds,
                    horizontalEdge, verticalEdge);
        }
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ResizeState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ResizeState.java
new file mode 100644
//Synthetic comment -- index 0000000..11f3ec6

//Synthetic comment -- @@ -0,0 +1,117 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.Segment;
import com.android.ide.common.api.SegmentType;

/** State held during resizing operations */
class ResizeState {
    /**
     * The associated rule
     */
    private final BaseLayoutRule mRule;

    /**
     * The node being resized
     */
    public final INode node;

     /**
      * The layout containing the resized node
      */
    public final INode layout;

    /** The proposed resized bounds of the node */
    public Rect bounds;

    /** The preferred wrap_content bounds of the node */
    public Rect wrapBounds;

    /** The suggested horizontal fill_parent guideline position */
    public Segment horizontalFillSegment;

    /** The suggested vertical fill_parent guideline position */
    public Segment verticalFillSegment;

    /** The type of horizontal edge being resized, or null */
    public SegmentType horizontalEdgeType;

    /** The type of vertical edge being resized, or null */
    public SegmentType verticalEdgeType;

    /** Whether the user has snapped to the wrap_content width */
    public boolean wrapWidth;

    /** Whether the user has snapped to the wrap_content height */
    public boolean wrapHeight;

    /** Whether the user has snapped to the match_parent width */
    public boolean fillWidth;

    /** Whether the user has snapped to the match_parent height */
    public boolean fillHeight;

    /**
     * Constructs a new {@link ResizeState}
     *
     * @param rule the associated rule
     * @param layout the parent layout containing the resized node
     * @param node the node being resized
     */
    ResizeState(BaseLayoutRule rule, INode layout, INode node) {
        mRule = rule;

        this.layout = layout;
        this.node = node;
    }

    /**
     * Returns the width attribute to be set to match the new bounds
     *
     * @return the width string, never null
     */
    public String getWidthAttribute() {
        if (wrapWidth) {
            return VALUE_WRAP_CONTENT;
        } else if (fillWidth) {
            return mRule.getFillParentValueName();
        } else {
            return String.format(VALUE_N_DP, mRule.mRulesEngine.pxToDp(bounds.w));
        }
    }

    /**
     * Returns the height attribute to be set to match the new bounds
     *
     * @return the height string, never null
     */
    public String getHeightAttribute() {
        if (wrapHeight) {
            return VALUE_WRAP_CONTENT;
        } else if (fillHeight) {
            return mRule.getFillParentValueName();
        } else {
            return String.format(VALUE_N_DP, mRule.mRulesEngine.pxToDp(bounds.h));
        }
    }
}
\ No newline at end of file







