/*Add Up/Down Icons

Add Up/Down icons to the outline context menu for the Move Up and Move Down actions.

Change-Id:I1d94871034946eb166968c76fbab61be5f1f23e5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index b16f0e5..18f0002 100755

//Synthetic comment -- @@ -16,13 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static org.eclipse.jface.action.IAction.AS_PUSH_BUTTON;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.layout.Pair;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
//Synthetic comment -- @@ -126,7 +125,8 @@
};

/** Action for moving items up in the tree */
    private Action mMoveUpAction = new Action("Move Up\t-", AS_PUSH_BUTTON) {

@Override
public String getId() {
//Synthetic comment -- @@ -145,7 +145,8 @@
};

/** Action for moving items down in the tree */
    private Action mMoveDownAction = new Action("Move Down\t+", AS_PUSH_BUTTON) {

@Override
public String getId() {







