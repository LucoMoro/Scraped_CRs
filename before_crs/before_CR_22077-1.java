/*Enforce at most one child on ScrollViews, and add LinearLayouts

This changeset fixes issue

15899: Scroll view can only have one child and editor should enforce
       this.

First, it modifies the view rules for ScrollView and HorizontalView to
refuse to add children to the view if there is already a child
there. It also updates the drawing code to not offer drop previews
other than the target highlighting rectangle.

Second, it modifies the New XML File wizard to insert a vertical
LinearLayout as the new child when a ScrollView or
HorizontalScrollView is created. (This was already done by the view
rules when the scroll views were dragged in from the palette.)

Change-Id:Ide825cbf28b46177983d6c5f2ea8c2848147c711*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index af001c6..66e2a3f 100755

//Synthetic comment -- @@ -62,7 +62,7 @@
});
}

    void drawFeedback(
IGraphics gc,
INode targetNode,
IDragElement[] elements,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index f73c114..62ea6f9 100644

//Synthetic comment -- @@ -23,9 +23,15 @@
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
* An {@link IViewRule} for android.widget.HorizontalScrollView.
//Synthetic comment -- @@ -55,4 +61,32 @@
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 73cb229..84375f4 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
public static final String RADIO_GROUP = "RadioGroup";              //$NON-NLS-1$
public static final String EXPANDABLE_LIST_VIEW = "ExpandableListView";//$NON-NLS-1$
public static final String GESTURE_OVERLAY_VIEW = "GestureOverlayView";//$NON-NLS-1$

public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
public static final String ATTR_HINT = "hint";                      //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index bf57456..385dcc5 100644

//Synthetic comment -- @@ -21,9 +21,15 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
* An {@link IViewRule} for android.widget.ScrollView.
//Synthetic comment -- @@ -52,4 +58,32 @@
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index a797bd0..583202a 100644

//Synthetic comment -- @@ -17,6 +17,8 @@

package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;

//Synthetic comment -- @@ -208,6 +210,18 @@
}

/**
* The minimum API level required by the current SDK target to support this feature.
*
* @return the minimum API level
//Synthetic comment -- @@ -248,10 +262,21 @@

return String.format(
"android:orientation=\"vertical\"\n"       //$NON-NLS-1$
                            + "android:layout_width=\"%1$s\"\n"            //$NON-NLS-1$
                            + "android:layout_height=\"%1$s\"", //$NON-NLS-1$
fill, fill);
}
},
new TypeInfo("Values",                                              // UI name
"An XML file with simple values: colors, strings, dimensions, etc.", // tooltip








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index a5a24d7..cdf3348 100644

//Synthetic comment -- @@ -146,12 +146,13 @@

String attrs = type.getDefaultAttrs(mMainPage.getProject());

        return createXmlFile(file, xmlns, root, attrs);
}

/** Creates a new file using the given root element, namespace and root attributes */
private static Pair<IFile, IRegion> createXmlFile(IFile file, String xmlns,
            String root, String rootAttributes) {
String name = file.getFullPath().toString();
boolean need_delete = false;

//Synthetic comment -- @@ -180,6 +181,10 @@

sb.append(">\n");                            //$NON-NLS-1$

// The insertion line
sb.append("    ");                           //$NON-NLS-1$
int caretOffset = sb.length();
//Synthetic comment -- @@ -239,7 +244,7 @@
root = type.getRootSeed().toString();
}
String attrs = type.getDefaultAttrs(project);
        return createXmlFile(file, xmlns, root, attrs);
}

private static boolean createWsParentDirectory(IContainer wsPath) {







