/*Prevent adding children into AdapterViews

As reported in issue 13042, it's possible to add views into
AdapterViews such as ListView. This causes a rendering error.

The root issue is that we were relying on
ElementDescriptor#hasChildren() to determine whether a particular view
is willing to accept children, but that isn't entirely correct, since
all subclasses of AdapterView (such as ListView, Spinner, etc) will
throw an exception if you do that.

To fix this, I've added a new method to DescriptorUtils,
"canInsertChildren", which performs additional checking beyond
hasChildren(). If it has an actual view object, it will walk up the
super class chain and see if the view extends AdapterView, and if no
view object is available, it will filter out the known subclasses of
AdapterView (well, it doesn't have to filter out Spinner since that
class doesn't report that it has children).

Change-Id:I663b18fcfbe97a10c8f1aaa2d75552fb8fb148d5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AdapterViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AdapterViewRule.java
new file mode 100644
//Synthetic comment -- index 0000000..612cc23

//Synthetic comment -- @@ -0,0 +1,30 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 5b90c7f..da408fa 100644

//Synthetic comment -- @@ -37,6 +37,10 @@
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
public static final String LINEAR_LAYOUT   = "LinearLayout";        //$NON-NLS-1$
public static final String ABSOLUTE_LAYOUT = "AbsoluteLayout";      //$NON-NLS-1$

public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
public static final String ATTR_ID = "id";                          //$NON-NLS-1$
//Synthetic comment -- @@ -108,6 +112,9 @@
/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

public static final String ATTR_SRC = "src"; //$NON-NLS-1$

// like fill_parent for API 8








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 17e15ad..65cf7a0 100644

//Synthetic comment -- @@ -21,6 +21,11 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
//Synthetic comment -- @@ -836,4 +841,48 @@
return (String) params[2];
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
//Synthetic comment -- index ecf152f..f591149 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -188,7 +189,8 @@
if (location == LOCATION_ON) {
// Targeting the middle of an item means to add it as a new child
// of the given element. This is only allowed on some types of nodes.
            if (!parentNode.getDescriptor().hasChildren()) {
return false;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 18f0002..e827990 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.common.layout.Pair;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
//Synthetic comment -- @@ -754,7 +755,7 @@

UiElementNode next = node.getUiNextSibling();
if (next != null) {
            if (next.getDescriptor().hasChildren()) {
return getFirstPosition(next);
} else {
return getPositionAfter(next);
//Synthetic comment -- @@ -786,7 +787,7 @@
curr = children.get(children.size() - 1);
continue;
}
                if (curr.getDescriptor().hasChildren()) {
return getFirstPosition(curr);
} else {
if (curr == prev) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java
//Synthetic comment -- index c27c1f8..7184c85 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

//Synthetic comment -- @@ -135,4 +136,107 @@
assertEquals("@+id/LinearLayout01",
DescriptorsUtils.getFreeWidgetId(uiRoot, "LinearLayout"));
}
}







