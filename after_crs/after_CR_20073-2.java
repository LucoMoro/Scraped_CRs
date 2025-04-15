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
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.common.layout;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;

/** Rule for AdapterView subclasses that don't have more specific rules */
public class AdapterViewRule extends BaseLayoutRule {
    @Override
    public DropFeedback onDropEnter(INode targetNode, IDragElement[] elements) {
        // You are not allowed to insert children into AdapterViews; you must
        // use the dedicated addView methods etc dynamically
        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 5b90c7f..da408fa 100644

//Synthetic comment -- @@ -37,6 +37,10 @@
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
public static final String LINEAR_LAYOUT   = "LinearLayout";        //$NON-NLS-1$
public static final String ABSOLUTE_LAYOUT = "AbsoluteLayout";      //$NON-NLS-1$
    public static final String LIST_VIEW = "ListView";                  //$NON-NLS-1$
    public static final String GALLERY = "Gallery";                     //$NON-NLS-1$
    public static final String GRID_VIEW = "GridView";                  //$NON-NLS-1$
    public static final String EXPANDABLE_LIST_VIEW = "ExpandableListView";//$NON-NLS-1$

public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
public static final String ATTR_ID = "id";                          //$NON-NLS-1$
//Synthetic comment -- @@ -108,6 +112,9 @@
/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

    /** The fully qualified class name of an AdapterView */
    public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$

public static final String ATTR_SRC = "src"; //$NON-NLS-1$

// like fill_parent for API 8








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 17e15ad..65cf7a0 100644

//Synthetic comment -- @@ -21,6 +21,11 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_ADAPTER_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GALLERY;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
//Synthetic comment -- @@ -836,4 +841,48 @@
return (String) params[2];
}

    /**
     * Returns true if the given descriptor represents a view that not only can have
     * children but which allows us to <b>insert</b> children. Some views, such as
     * ListView (and in general all AdapterViews), disallow children to be inserted except
     * through the dedicated AdapterView interface to do it.
     *
     * @param descriptor the descriptor for the view in question
     * @param viewObject an actual instance of the view, or null if not available
     * @return true if the descriptor describes a view which allows insertion of child
     *         views
     */
    public static boolean canInsertChildren(ElementDescriptor descriptor, Object viewObject) {
        if (descriptor.hasChildren()) {
            if (viewObject != null) {
                // We have a view object; see if it derives from an AdapterView
                Class<?> clz = viewObject.getClass();
                while (clz != null) {
                    if (clz.getName().equals(FQCN_ADAPTER_VIEW)) {
                        return false;
                    }
                    clz = clz.getSuperclass();
                }
            } else {
                // No view object, so we can't easily look up the class and determine
                // whether it's an AdapterView; instead, look at the fixed list of builtin
                // concrete subclasses of AdapterView
                String viewName = descriptor.getXmlLocalName();
                if (viewName.equals(LIST_VIEW) || viewName.equals(EXPANDABLE_LIST_VIEW)
                        || viewName.equals(GALLERY) || viewName.equals(GRID_VIEW)) {

                    // We should really also enforce that
                    // LayoutConstants.ANDROID_URI.equals(descriptor.getNameSpace())
                    // here and if not, return true, but it turns out the getNameSpace()
                    // for elements are often "".

                    return false;
                }
            }

            return true;
        }

        return false;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
//Synthetic comment -- index ecf152f..f591149 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -188,7 +189,8 @@
if (location == LOCATION_ON) {
// Targeting the middle of an item means to add it as a new child
// of the given element. This is only allowed on some types of nodes.
            if (!DescriptorsUtils.canInsertChildren(parentNode.getDescriptor(),
                    parent.getViewObject())) {
return false;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 18f0002..e827990 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.common.layout.Pair;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
//Synthetic comment -- @@ -754,7 +755,7 @@

UiElementNode next = node.getUiNextSibling();
if (next != null) {
            if (DescriptorsUtils.canInsertChildren(next.getDescriptor(), null)) {
return getFirstPosition(next);
} else {
return getPositionAfter(next);
//Synthetic comment -- @@ -786,7 +787,7 @@
curr = children.get(children.size() - 1);
continue;
}
                if (DescriptorsUtils.canInsertChildren(curr.getDescriptor(), null)) {
return getFirstPosition(curr);
} else {
if (curr == prev) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java
//Synthetic comment -- index c27c1f8..7184c85 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

//Synthetic comment -- @@ -135,4 +136,107 @@
assertEquals("@+id/LinearLayout01",
DescriptorsUtils.getFreeWidgetId(uiRoot, "LinearLayout"));
}

    private static ElementDescriptor createDesc(String name, String fqn, boolean hasChildren) {
        if (hasChildren) {
            return new ViewElementDescriptor(name, name, fqn, "", "", new AttributeDescriptor[0],
                    new AttributeDescriptor[0], new ElementDescriptor[1], false);
        } else {
            return new ViewElementDescriptor(name, fqn);
        }
    }

    public void testCanInsertChildren() throws Exception {
        assertFalse(DescriptorsUtils.canInsertChildren(createDesc("android:Button",
                "android.widget.Button", false), null));
        assertTrue(DescriptorsUtils.canInsertChildren(createDesc("android:LinearLayout",
                "android.view.LinearLayout", true), null));
        assertFalse(DescriptorsUtils.canInsertChildren(createDesc("android:ListView",
                "android.widget.ListView", true), null));
        assertFalse(DescriptorsUtils.canInsertChildren(createDesc("android:ExpandableListView",
                "android.widget.ExpandableListView", true), null));
        assertFalse(DescriptorsUtils.canInsertChildren(createDesc("android:Gallery",
                "android.widget.Gallery", true), null));
        assertFalse(DescriptorsUtils.canInsertChildren(createDesc("android:GridView",
                "android.widget.GridView", true), null));

        // This isn't the Android one (missing android: namespace prefix):
        // This test is disabled since I had to remove the namespace enforcement
        // (see namespace-related comment in canInsertChildren)
        //assertTrue(DescriptorsUtils.canInsertChildren(createDesc("mynamespace:ListView",
        //        "android.widget.ListView", true), null));

        // Custom view without known view object
        assertTrue(DescriptorsUtils.canInsertChildren(createDesc("MyView",
                "foo.bar.MyView", true), null));

        // Custom view with known view object that extends AdapterView
        Object view = new MyClassLoader().findClass("foo.bar.MyView").newInstance();
        assertFalse(DescriptorsUtils.canInsertChildren(createDesc("MyView",
                "foo.bar.MyView", true), view));
    }

    /** Test class loader which finds foo.bar.MyView extends android.widget.AdapterView */
    private static class MyClassLoader extends ClassLoader {
        public MyClassLoader() {
            super(null);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (name.equals("foo.bar.MyView")) {
                // Simple class stub compiled by javac and dumped as bytes:
                //package foo.bar;
                //public class MyView extends android.widget.AdapterView {
                //    public MyView() {
                //        super(null);
                //    }
                //}
                byte[] classData = new byte[] {
                        -54,-2,-70,-66,0,0,0,49,0,17,10,0,3,0,13,7,0,14,7,0,15,1,0,6,60,105,110,
                        105,116,62,1,0,3,40,41,86,1,0,4,67,111,100,101,1,0,15,76,105,110,101,78,
                        117,109,98,101,114,84,97,98,108,101,1,0,18,76,111,99,97,108,86,97,114,
                        105,97,98,108,101,84,97,98,108,101,1,0,4,116,104,105,115,1,0,16,76,102,
                        111,111,47,98,97,114,47,77,121,86,105,101,119,59,1,0,10,83,111,117,114,
                        99,101,70,105,108,101,1,0,11,77,121,86,105,101,119,46,106,97,118,97,12,
                        0,4,0,16,1,0,14,102,111,111,47,98,97,114,47,77,121,86,105,101,119,1,0,
                        26,97,110,100,114,111,105,100,47,119,105,100,103,101,116,47,65,100,97,
                        112,116,101,114,86,105,101,119,1,0,28,40,76,97,110,100,114,111,105,100,
                        47,99,111,110,116,101,110,116,47,67,111,110,116,101,120,116,59,41,86,0,
                        33,0,2,0,3,0,0,0,0,0,1,0,1,0,4,0,5,0,1,0,6,0,0,0,52,0,2,0,1,0,0,0,6,42,
                        1,-73,0,1,-79,0,0,0,2,0,7,0,0,0,10,0,2,0,0,0,9,0,5,0,10,0,8,0,0,0,12,0,
                        1,0,0,0,6,0,9,0,10,0,0,0,1,0,11,0,0,0,2,0,12
                };
                return defineClass("foo.bar.MyView", classData, 0, classData.length);
            }
            if (name.equals("android.widget.AdapterView")) {
                // Simple class stub compiled by javac and dumped as bytes:
                //package android.widget;
                //public class AdapterView {
                //    public AdapterView(android.content.Context context) { }
                //}
                byte[] classData = new byte[] {
                        -54,-2,-70,-66,0,0,0,49,0,19,10,0,3,0,15,7,0,16,7,0,17,1,0,6,60,105,110,
                        105,116,62,1,0,28,40,76,97,110,100,114,111,105,100,47,99,111,110,116,101,
                        110,116,47,67,111,110,116,101,120,116,59,41,86,1,0,4,67,111,100,101,1,0,
                        15,76,105,110,101,78,117,109,98,101,114,84,97,98,108,101,1,0,18,76,111,
                        99,97,108,86,97,114,105,97,98,108,101,84,97,98,108,101,1,0,4,116,104,105,
                        115,1,0,28,76,97,110,100,114,111,105,100,47,119,105,100,103,101,116,47,
                        65,100,97,112,116,101,114,86,105,101,119,59,1,0,7,99,111,110,116,101,120,
                        116,1,0,25,76,97,110,100,114,111,105,100,47,99,111,110,116,101,110,116,
                        47,67,111,110,116,101,120,116,59,1,0,10,83,111,117,114,99,101,70,105,108,
                        101,1,0,16,65,100,97,112,116,101,114,86,105,101,119,46,106,97,118,97,12,
                        0,4,0,18,1,0,26,97,110,100,114,111,105,100,47,119,105,100,103,101,116,
                        47,65,100,97,112,116,101,114,86,105,101,119,1,0,16,106,97,118,97,47,108,
                        97,110,103,47,79,98,106,101,99,116,1,0,3,40,41,86,0,33,0,2,0,3,0,0,0,0,0,
                        1,0,1,0,4,0,5,0,1,0,6,0,0,0,57,0,1,0,2,0,0,0,5,42,-73,0,1,-79,0,0,0,2,0,
                        7,0,0,0,6,0,1,0,0,0,8,0,8,0,0,0,22,0,2,0,0,0,5,0,9,0,10,0,0,0,0,0,5,0,11,
                        0,12,0,1,0,1,0,13,0,0,0,2,0,14
                };
                return defineClass("android.widget.AdapterView", classData, 0, classData.length);
            }

            return super.findClass(name);
        }
    }
}







