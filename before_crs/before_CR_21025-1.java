/*Fix selection hint painting on honeycomb

The selection hints, which for example display the constraints when
you are in a RelativeLayout, are painted below or to the right of the
canvas. The positioning of this text was based on walking up the
ViewInfo hierarchy and taking the root element's bounds as the bounds
of the canvas image. The text was then placed below or to the right
of this image.

This no longer works with honeycomb since we now paint an action bar
and a system bar, and the bounds of these are not included in the root
ViewInfo, so as a result the selection hints were painted on top of
the system bar.

This changes the algorithm to use the image bounds itself rather than
the view info bounds.

Change-Id:I0e6e04608fb55af476c58a33785d5a7d5ac511c1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 95382ae..1b86c88 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.IImageFactory;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -275,4 +276,17 @@

return mAwtImage;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index a2dc88f..e61e3e9 100644

//Synthetic comment -- @@ -80,41 +80,32 @@
INode parent = node.getParent();
if (parent instanceof NodeProxy) {
NodeProxy parentNode = (NodeProxy) parent;

            // Get the top parent, to display data under it
            INode topParent = parentNode;
            while (true) {
                INode p = topParent.getParent();
                if (p == null) {
                    break;
}
                topParent = p;
            }

            Rect b = topParent.getBounds();
            if (b.isValid()) {
                List<String> infos = rulesEngine.callGetSelectionHint(parentNode, node);
                if (infos != null && infos.size() > 0) {
                    gcWrapper.useStyle(DrawingStyle.HELP);
                    double scale = mCanvas.getScale();

                    // Compute the location to display the help. This is done in
                    // layout coordinates, so we need to apply the scale in reverse
                    // when making pixel margins
                    // TODO: We could take the Canvas dimensions into account to see
                    // where there is more room.
                    // TODO: The scrollbars should take the presence of hint text
                    // into account.
                    int x, y;
                    if (b.w > b.h) {
                        x = (int) (b.x + 3 / scale);
                        y = (int) (b.y + b.h + 10 / scale);
                    } else {
                        x = (int) (b.x + b.w + 10 / scale);
                        y = (int) (b.y + 3 / scale);
                    }
                    gcWrapper.drawBoxedStrings(x, y, infos);
}
}
}
}







