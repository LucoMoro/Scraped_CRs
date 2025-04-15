/*Fill FrameLayout children on drop depending on metadata

Adds a drop handler to the FrameLaoyut rule which looks up the
metadata for the dropped child, and sets the width and/or height to
fill based on that metadata.  For example, a LinearLayout dropped into
a FrameLayout (or say a ViewFlipper which inherits from it), then the
layout will fill. A button on the other hand will not fill, and an
EditText will fill horizontally but not vertically, and so on.

Change-Id:I65a9379fa7128cc9c68d9bfd1849b0f1dc0f4a86*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index 655eee2..af001c6 100755

//Synthetic comment -- @@ -16,7 +16,10 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -25,10 +28,13 @@
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewMetadata;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IViewMetadata.FillPreference;
import com.android.util.Pair;

import java.util.List;
//Synthetic comment -- @@ -159,4 +165,21 @@
actions.add(createGravityAction(children, ATTR_LAYOUT_GRAVITY));
}
}

    @Override
    public void onChildInserted(INode node, INode parent, InsertType insertType) {
        // Look at the fill preferences and fill embedded layouts etc
        String fqcn = node.getFqcn();
        IViewMetadata metadata = mRulesEngine.getMetadata(fqcn);
        if (metadata != null) {
            FillPreference fill = metadata.getFillPreference();
            String fillParent = getFillParentValueName();
            if (fill.fillHorizontally(true)) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, fillParent);
            }
            if (fill.fillVertically(false)) {
                node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, fillParent);
            }
        }
    }
}







