/*Fix drop into ScrollView

Dragging something into a ScrollView wasn't working,
because a ScrollView was not inheriting layout behavior
from its parent rule.

Change-Id:Id8c11fb033921d86a57d0f34bd7c677dbe229cac*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index cceee78..f7fc6c5 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
/**
* An {@link IViewRule} for android.widget.HorizontalScrollView.
*/
public class HorizontalScrollViewRule extends FrameLayoutRule {

@Override
public void onChildInserted(INode child, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index 0d3af83..822c1cb 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
/**
* An {@link IViewRule} for android.widget.ScrollView.
*/
public class ScrollViewRule extends FrameLayoutRule {

@Override
public void onChildInserted(INode child, INode parent, InsertType insertType) {







