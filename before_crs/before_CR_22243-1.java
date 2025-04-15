/*Add Table layout actions

Add "add row" and "remove row" layout actions to the TableLayout and
to the TableRow view rules.

Also, add 4 rows into TableView created through the palette.

Whenhttps://review.source.android.com//#change,22077is integrated
I'll also make it add these TableRows when creating a TableLayout
through the New XML File Wizard.

Change-Id:I906eb6ab479c3781d3d8eb0a536cec67459ddec2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java
//Synthetic comment -- index 6ac670f..c3b3bfa 100644

//Synthetic comment -- @@ -19,11 +19,15 @@

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.List;

/**
* An {@link IViewRule} for android.widget.TableLayout.
//Synthetic comment -- @@ -33,6 +37,13 @@
// the default is vertical, not horizontal
// The fill of all children should be wrap_content

@Override
protected boolean isVertical(INode node) {
// Tables are always vertical
//Synthetic comment -- @@ -67,4 +78,69 @@
null, addTab));
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java
//Synthetic comment -- index e734f4a..13e648e 100644

//Synthetic comment -- @@ -15,9 +15,14 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
* An {@link IViewRule} for android.widget.TableRow.
//Synthetic comment -- @@ -40,4 +45,17 @@
// respectively.
}

}







