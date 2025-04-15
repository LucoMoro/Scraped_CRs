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
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -85,7 +90,9 @@
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -761,37 +768,244 @@

// Record children of the target right before the drop (such that we
// can find out after the drop which exact children were inserted)
                    Set<INode> children = new HashSet<INode>();
for (INode node : targetNode.getChildren()) {
                        children.add(node);
}

String label = MoveGesture.computeUndoLabel(targetNode,
elements, DND.DROP_MOVE);
                    canvas.getLayoutEditor().wrapUndoEditXmlModel(label, new Runnable() {
                        public void run() {
                            canvas.getRulesEngine().setInsertType(InsertType.MOVE);
                            int index = target.getSecond();
                            BaseLayoutRule.insertAt(targetNode, elements, false, index);
                            canvas.getClipboardSupport().deleteSelection("Remove", dragSelection);
                        }
                    });

                    // Now find out which nodes were added, and look up their
                    // corresponding CanvasViewInfos
                    final List<INode> added = new ArrayList<INode>();
                    for (INode node : targetNode.getChildren()) {
                        if (!children.contains(node)) {
                            added.add(node);
                        }
}

                    selectionManager.updateOutlineSelection(added);
}
}
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
    /** Ability to call<br>
* {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     * <p>If the bridge also supports {@link #VIEW_MANIPULATION} then those methods can use
     * an {@link com.android.layoutlib.api.LayoutScene.IAnimationListener}, otherwise they won't. */
    ANIMATE;
}







