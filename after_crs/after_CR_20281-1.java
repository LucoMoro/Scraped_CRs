/*More fine grained layoutlib Capability for animation support.

Make the distinction between playing animation, animating
view insert/delete/move inside the same viewgroup and animating
move across layouts.

Change-Id:Ia9a6e4e53425a66a74ddd39796b04ed8c78d4a5a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 4870894..b6f0b9c 100755

//Synthetic comment -- @@ -26,6 +26,11 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.IAnimationListener;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.Result.Status;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -85,7 +90,9 @@
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -761,37 +768,244 @@

// Record children of the target right before the drop (such that we
// can find out after the drop which exact children were inserted)
                    Set<INode> childrenBefore = new HashSet<INode>();
for (INode node : targetNode.getChildren()) {
                        childrenBefore.add(node);
}

String label = MoveGesture.computeUndoLabel(targetNode,
elements, DND.DROP_MOVE);
                    MovePerformer performer = new MovePerformer(canvas, targetNode,
                            target.getSecond(), elements, dragSelection, label, childrenBefore);
                    if (!performer.move()) {
                        selectAdded(targetNode, childrenBefore);
}
}
}
}
}

    private void selectAdded(INode targetNode, Set<INode> childrenBefore) {
        // Now find out which nodes were added, and look up their
        // corresponding CanvasViewInfos
        List<INode> added = new ArrayList<INode>();
        for (INode node : targetNode.getChildren()) {
            if (!childrenBefore.contains(node)) {
                added.add(node);
            }
        }

        LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
        SelectionManager selectionManager = canvas.getSelectionManager();
        selectionManager.updateOutlineSelection(added);
    }

    /**
     * Handler which performs the actual XML edit to implement the move operation,
     * possibly asynchronously while animating the transition
     */
    private class MovePerformer implements Runnable, IAnimationListener {
        private INode mTargetNode;
        private final int mIndex;
        private final LayoutCanvas mCanvas;
        final SimpleElement[] mElements;
        final ArrayList<SelectionItem> mDragSelection;
        private final String mLabel;
        private Set<INode> mChildrenBefore;

        public MovePerformer(LayoutCanvas canvas, INode targetNode, int index,
                SimpleElement[] elements, ArrayList<SelectionItem> dragSelection,
                String label, Set<INode> childrenBefore) {
            super();
            mCanvas = canvas;
            mTargetNode = targetNode;
            mIndex = index;
            mElements = elements;
            mDragSelection = dragSelection;
            mLabel = label;
            mChildrenBefore = childrenBefore;
        }

        public boolean move() {
            // FIXME: check if the old and new viewgroup is the same to check if Capability.ANIMATED_VIEW_MANIPULATION is enough.
            boolean animate = mGraphicalEditorPart.renderingSupports(
                    Capability.FULL_ANIMATED_VIEW_MANIPULATION);
            if (mDragSelection.size() != 1) {
                animate = false;
            }

            if (animate && animateMove()) {
                // The model is updated when the animation is completed
                return true;
            } else {
                // Animation somehow failed: just move the element directly
                performModelMove();
                return false;
            }
        }

        private boolean animateMove() {
            SelectionItem draggedItem = mDragSelection.get(0);
            CanvasViewInfo viewInfo = draggedItem.getViewInfo();
            CanvasViewInfo targetVi = mCanvas.getViewHierarchy().findViewInfoFor(mTargetNode);
            if (viewInfo != null && targetVi != null) {
                Object view = viewInfo.getViewObject();
                Object targetView = targetVi.getViewObject();
                //Object parentView = parentViewInfo.getViewObject();
                if (view != null && targetView != null) {
                    int targetIndex = mIndex;
                    Object oldParentView = getViewParent(view);
                    if (oldParentView == targetView) { // moving within same parent?
                        // If the move is within the same parent, then the
                        // target view index might be affected (because mIndex
                        // is a pre-removal index, and the target index for the layout library
                        // is a post-removal index)

                        //int selfIndex = viewInfo.getSiblingIndex();
                        int selfIndex = getViewIndex(targetView, view);
                        if (mIndex >= selfIndex) {
                            targetIndex--;
                        }
                    }

                    RenderSession session = mCanvas.getViewHierarchy().getSession();
                    Result result = session.moveChild(targetView, view, targetIndex,
                            null /* layoutParams */, this);
                    if (result.isSuccess()) {
                        return true;
                    }
                }
            }

            return false;
        }

        private void performModelMove() {
            mCanvas.getLayoutEditor().wrapUndoEditXmlModel(mLabel, this);
        }

        // ---- Implements Runnable ----

        public void run() {
            // This method should be called within an XML edit lock!

            if (mTargetNode == null) {
                // We've already performed the model move.
                return;
            }

            try {
                mCanvas.getRulesEngine().setInsertType(InsertType.MOVE);
                BaseLayoutRule.insertAt(mTargetNode, mElements, false, mIndex);
                mCanvas.getClipboardSupport().deleteSelection("Remove", mDragSelection);
                selectAdded(mTargetNode, mChildrenBefore);
                mTargetNode = null;
            } catch (Throwable t) {
                AdtPlugin.log(t, null);
            }
        }

        // ---- Implements IAnimationListener ----

        private boolean mPendingDrawing = false;

        public void done(final Result result) {
            // Must run on the AWT thread
            mCanvas.getDisplay().asyncExec(new Runnable() {
                public void run() {
                    if (result.getStatus() == Status.SUCCESS) {
                        refreshViewHieararchy();
                        // Will be replaced by model update anyway
                    }

                    // Update the model
                    performModelMove();
                }
            });
        }

        public boolean isCanceled() {
            return false;
        }

        public void onNewFrame(RenderSession session) {
            ImageOverlay overlay = mCanvas.getImageOverlay();
            overlay.setImage(session.getImage());
            synchronized (this) {
                if (mPendingDrawing == false) {
                    mCanvas.getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            //refreshViewHieararchy();
                            drawImage();
                        }
                    });

                    mPendingDrawing = true;
                }
            }
        }

        /**
         * Called from the UI thread from the asyncRunnable.
         */
        public void drawImage() {
            // get last image
            synchronized (this) {
                mPendingDrawing = false;
            }

            mCanvas.redraw();
        }

        private void refreshViewHieararchy() {
            ViewHierarchy viewHierarchy = mCanvas.getViewHierarchy();
            // this just refreshes the CanvasViewInfo hierarchy; make a more
            // explicit refresh() method on ViewHiearchy so I don't rely on
            // side effects like this
            mCanvas.setSession(viewHierarchy.getSession(), Collections
                    .<UiElementNode> emptySet());
        }
    }

    /**
     * Get the child index of the given view;
     */
    private static int getViewIndex(Object parent, Object view) {
        // The index needs to be exactly right, and there is a chance that the
        // CanvasViewInfos are not representing all children in the
        // object graph (since we for example skip null-cookie nodes).
        // If the layoutlib always creates a ViewInfo for all views, we can make an
        // accurate sibling index in the CanvasViewInfos by incrementing the sibling
        // counter even when we skip creating a CanvasViewInfo from a ViewInfo list.
        try {
            Class<?> viewGroupClass = Class.forName("android.view.ViewGroup", false, //$NON-NLS-1$
                    parent.getClass().getClassLoader());
            Class<?> viewClass = Class.forName("android.view.View", false, //$NON-NLS-1$
                    view.getClass().getClassLoader());
            Method method = viewGroupClass.getMethod("indexOfChild", viewClass); //$NON-NLS-1$
            return (Integer) method.invoke(parent, view);
        } catch (Exception e) {
            AdtPlugin.log(e, null);
        }

        return -1;
    }

    /** Get the view parent of the given object */
    private static Object getViewParent(Object view) {
        // We can also do this via CanvasViewInfo.getParent().getViewCookie()
        // but see comment under #getViewIndex for why this may not always be correct
        try {
            Method method = view.getClass().getMethod("getParent", (Class<?>[]) null);
            return method.invoke(view);
        } catch (Exception e) {
            AdtPlugin.log(e, null);
            return null;
        }
    }



/**
* Returns the {@link CanvasViewInfo} for the currently selected item, or null if
* there are no or multiple selected items








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index abbab45..0de4c90 100644

//Synthetic comment -- @@ -39,9 +39,20 @@
* {@link LayoutScene#setProperty(Object, String, String)}
* */
VIEW_MANIPULATION,
    /** Ability to play animations with<br>
* {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     */
    PLAY_ANIMATION,
    /**
     * Ability to manipulate views with animation, as long as the view does not change parent.
     * {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     */
    ANIMATED_VIEW_MANIPULATION,
    /**
     * Ability to move views (even into a different ViewGroup) with animation.
     * see {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     */
    FULL_ANIMATED_VIEW_MANIPULATION;
}







