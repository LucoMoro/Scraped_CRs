/*Drop handler for RadioGroup, WebView

Add a drop handler for radiogroup which puts a few radio buttons in
it.

Make WebView default to match parent in both dimensions.

Also fix superclass of the SlidingDrawerRule.

Change-Id:I05467bd06f074692603c236b9f3fd947fe7e63fc*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 2669a6b..2a87441 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
public static final String ATTR_ID = "id";                          //$NON-NLS-1$
public static final String ATTR_HANDLE = "handle";                  //$NON-NLS-1$
public static final String ATTR_CONTENT = "content";                //$NON-NLS-1$

public static final String ATTR_LAYOUT_PREFIX = "layout_";          //$NON-NLS-1$
public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
//Synthetic comment -- @@ -122,6 +123,9 @@
/** The fully qualified class name of a Button view */
public static final String FQCN_BUTTON = "android.widget.Button"; //$NON-NLS-1$

/** The fully qualified class name of an AdapterView */
public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java
new file mode 100644
//Synthetic comment -- index 0000000..039b495

//Synthetic comment -- @@ -0,0 +1,46 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java
//Synthetic comment -- index fb0f33e..15c3b4c 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
* An {@link IViewRule} for android.widget.SlidingDrawerRule which initializes new sliding
* drawers with their mandatory children and default sizing attributes
*/
public class SlidingDrawerRule extends BaseViewRule {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java
//Synthetic comment -- index a13f42f..8ec53db 100755

//Synthetic comment -- @@ -16,7 +16,13 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.IViewRule;

/**
* An {@link IViewRule} for android.widget.ZoomControls.
//Synthetic comment -- @@ -24,4 +30,15 @@
public class WebViewRule extends IgnoredLayoutRule {
// A WebView is not a general purpose AbsoluteLayout you should drop stuff
// into; it's an AbsoluteLayout for implementation purposes.
}







