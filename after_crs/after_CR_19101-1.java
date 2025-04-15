/*Extract layout constants into a separate constants class

Combine the constants in BaseView and a LayoutConstants class over in
the internal packages into a new LayoutConstants class, and reference
these constants elsewhere (statically imported). This was suggested by
in the feedback to review #18971.

Change-Id:I40b76f8f6045c34a98f7a2363f96d2942360d1f3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 708b67b..348d6cf 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java
//Synthetic comment -- index 412e495..979a4a2 100644

//Synthetic comment -- @@ -16,17 +16,21 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.Arrays;
import java.util.HashMap;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java
//Synthetic comment -- index 92f0543..4bcf069 100644

//Synthetic comment -- @@ -16,6 +16,13 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IClientRulesEngine;
//Synthetic comment -- @@ -44,49 +51,6 @@
public class BaseView implements IViewRule {
protected IClientRulesEngine mRulesEngine;

// Cache of attributes. Key is FQCN of a node mixed with its view hierarchy
// parent. Values are a custom map as needed by getContextMenu.
private Map<String, Map<String, Prop>> mAttributesMap =








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java
//Synthetic comment -- index 54b511e..311af84 100644

//Synthetic comment -- @@ -16,6 +16,14 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EDIT_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -32,12 +40,12 @@
// A DialerFilter requires a couple of nested EditTexts with fixed ids:
if (insertType == InsertType.CREATE) {
INode hint = node.appendChild(FQCN_EDIT_TEXT);
            hint.setAttribute(ANDROID_URI, ATTR_TEXT, "Hint");
hint.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/hint"); //$NON-NLS-1$
hint.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);

INode primary = node.appendChild(FQCN_EDIT_TEXT);
            primary.setAttribute(ANDROID_URI, ATTR_TEXT, "Primary");
primary.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/primary"); //$NON-NLS-1$
primary.setAttribute(ANDROID_URI, ATTR_LAYOUT_BELOW,
"@android:id/hint"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index 547dc04..6ae6872 100644

//Synthetic comment -- @@ -16,6 +16,14 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -42,8 +50,8 @@
// Insert a horizontal linear layout which is commonly used with horizontal scrollbars
// as described by the documentation for HorizontalScrollbars.
INode linearLayout = node.appendChild(FQCN_LINEAR_LAYOUT);
            linearLayout.setAttribute(ANDROID_URI, ATTR_ORIENTATION,
                    VALUE_HORIZONTAL);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java
//Synthetic comment -- index 7a23f17..9ea5dc2 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java
//Synthetic comment -- index 67cd422..9b1eac3 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
similarity index 67%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutConstants.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index f662411..c815f2a 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
*
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -14,8 +14,7 @@
* limitations under the License.
*/

package com.android.ide.common.layout;

/**
* A bunch of constants that map to either:
//Synthetic comment -- @@ -26,7 +25,6 @@
* </ul>
*/
public class LayoutConstants {
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
public static final String LINEAR_LAYOUT   = "LinearLayout";        //$NON-NLS-1$
public static final String ABSOLUTE_LAYOUT = "AbsoluteLayout";      //$NON-NLS-1$
//Synthetic comment -- @@ -61,6 +59,36 @@
public static final String VALUE_TRUE = "true";                             //$NON-NLS-1$
public static final String VALUE_N_DIP = "%ddip";                           //$NON-NLS-1$

    /**
     * Namespace for the Android resource XML, i.e.
     * "http://schemas.android.com/apk/res/android"
     */
    public static String ANDROID_URI = "http://schemas.android.com/apk/res/android"; //$NON-NLS-1$

    /** The fully qualified class name of an EditText view */
    public static final String FQCN_EDIT_TEXT = "android.widget.EditText"; //$NON-NLS-1$

    /** The fully qualified class name of a LinearLayout view */
    public static final String FQCN_LINEAR_LAYOUT = "android.widget.LinearLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a FrameLayout view */
    public static final String FQCN_FRAME_LAYOUT = "android.widget.FrameLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a TableRow view */
    public static final String FQCN_TABLE_ROW = "android.widget.TableRow"; //$NON-NLS-1$

    /** The fully qualified class name of a TabWidget view */
    public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

    public static final String ATTR_SRC = "src"; //$NON-NLS-1$

    // like fill_parent for API 8
    public static final String VALUE_MATCH_PARENT = "match_parent"; //$NON-NLS-1$

    public static String ATTR_ORIENTATION = "orientation"; //$NON-NLS-1$

    public static String VALUE_HORIZONTAL = "horizontal"; //$NON-NLS-1$

    public static String VALUE_VERTICAL = "vertical"; //$NON-NLS-1$

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index e31424c..77e6d54 100644

//Synthetic comment -- @@ -16,6 +16,14 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -25,12 +33,12 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewMetadata;
import com.android.ide.common.api.IViewMetadata.FillPreference;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -41,10 +49,6 @@
* classes.
*/
public class LinearLayoutRule extends BaseLayout {
/**
* Add an explicit Orientation toggle to the context menu.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java
//Synthetic comment -- index 347057a..d3d226c 100755

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java
//Synthetic comment -- index 2eff369..b8ddedd 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 96e6aa6..04629b2 100755

//Synthetic comment -- @@ -16,19 +16,22 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IFeedbackPainter;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INode.IAttribute;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.Collections;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index 5ce8bad..5324278 100644

//Synthetic comment -- @@ -16,6 +16,12 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java
//Synthetic comment -- index b6d184f..727d231 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java
//Synthetic comment -- index f47da68..dc512bd 100755

//Synthetic comment -- @@ -16,6 +16,18 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_FRAME_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TAB_WIDGET;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -41,8 +53,8 @@
INode linear = node.appendChild(FQCN_LINEAR_LAYOUT);
linear.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);
linear.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, VALUE_FILL_PARENT);
            linear.setAttribute(ANDROID_URI, ATTR_ORIENTATION,
                    VALUE_VERTICAL);

INode tab = linear.appendChild(FQCN_TAB_WIDGET);
tab.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, VALUE_FILL_PARENT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java
//Synthetic comment -- index a5d772c..6ac670f 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_ROW;

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 0e1e8bd..09691b8 100644

//Synthetic comment -- @@ -16,10 +16,18 @@

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
//Synthetic comment -- @@ -30,8 +38,8 @@
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -668,27 +676,27 @@
boolean fill = ui_node.getDescriptor().hasChildren() &&
ui_node.getUiParent() instanceof UiDocumentNode;
ui_node.setAttributeValue(
                ATTR_LAYOUT_WIDTH,
SdkConstants.NS_RESOURCES,
                fill ? VALUE_FILL_PARENT : VALUE_WRAP_CONTENT,
false /* override */);
ui_node.setAttributeValue(
                ATTR_LAYOUT_HEIGHT,
SdkConstants.NS_RESOURCES,
                fill ? VALUE_FILL_PARENT : VALUE_WRAP_CONTENT,
false /* override */);

String widget_id = getFreeWidgetId(ui_node);
if (widget_id != null) {
ui_node.setAttributeValue(
                    ATTR_ID,
SdkConstants.NS_RESOURCES,
widget_id,
false /* override */);
}

ui_node.setAttributeValue(
                ATTR_TEXT,
SdkConstants.NS_RESOURCES,
widget_id,
false /*override*/);
//Synthetic comment -- @@ -697,14 +705,14 @@
UiElementNode ui_parent = ui_node.getUiParent();
if (ui_parent != null &&
ui_parent.getDescriptor().getXmlLocalName().equals(
                            RELATIVE_LAYOUT)) {
UiElementNode ui_previous = ui_node.getUiPreviousSibling();
if (ui_previous != null) {
                    String id = ui_previous.getAttributeValue(ATTR_ID);
if (id != null && id.length() > 0) {
id = id.replace("@+", "@");                     //$NON-NLS-1$ //$NON-NLS-2$
ui_node.setAttributeValue(
                                ATTR_LAYOUT_BELOW,
SdkConstants.NS_RESOURCES,
id,
false /* override */);
//Synthetic comment -- @@ -804,7 +812,7 @@
params[2] = generated;
}

        String id = uiRoot.getAttributeValue(ATTR_ID);
if (id != null) {
id = id.replace("@+id/", "");                               //$NON-NLS-1$ $NON-NLS-2$
id = id.replace("@id/", "");                                //$NON-NLS-1$ $NON-NLS-2$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 2b01b1c..d6c8098 100755

//Synthetic comment -- @@ -16,15 +16,19 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layoutlib.LayoutLibrary;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
//Synthetic comment -- @@ -689,19 +693,19 @@
// Set up a proper name space
Attr attr = document.createAttributeNS(XmlnsAttributeDescriptor.XMLNS_URI,
"xmlns:android"); //$NON-NLS-1$
            attr.setValue(ANDROID_URI);
element.getAttributes().setNamedItemNS(attr);

element.setAttributeNS(SdkConstants.NS_RESOURCES,
                    ATTR_LAYOUT_WIDTH, VALUE_WRAP_CONTENT);
element.setAttributeNS(SdkConstants.NS_RESOURCES,
                    ATTR_LAYOUT_HEIGHT, VALUE_WRAP_CONTENT);

// This doesn't apply to all, but doesn't seem to cause harm and makes for a
// better experience with text-oriented views like buttons and texts
UiElementNode uiRoot = layoutEditor.getUiRootNode();
String text = DescriptorsUtils.getFreeWidgetId(uiRoot, viewName);
            element.setAttributeNS(SdkConstants.NS_RESOURCES, ATTR_TEXT, text);

document.appendChild(element);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java
//Synthetic comment -- index 7853410..279c42e 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -56,8 +58,8 @@
// Drop preview
"useStyle(DROP_PREVIEW), drawRect(Rect[-22,-50,105,80])");

        assertEquals("-22dip", inserted.getStringAttr(ANDROID_URI, "layout_x"));
        assertEquals("-50dip", inserted.getStringAttr(ANDROID_URI, "layout_y"));

// Without drag bounds we should just draw guide lines instead
inserted = dragInto(new Rect(0, 0, 0, 0), new Point(30, -10), 4, -1,
//Synthetic comment -- @@ -66,8 +68,8 @@
"useStyle(GUIDELINE), drawLine(30,0,30,480), drawLine(0,-10,240,-10)",
// Drop preview
"useStyle(DROP_PREVIEW), drawLine(30,-10,240,-10), drawLine(30,-10,30,480)");
        assertEquals("30dip", inserted.getStringAttr(ANDROID_URI, "layout_x"));
        assertEquals("-10dip", inserted.getStringAttr(ANDROID_URI, "layout_y"));
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbstractLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbstractLayoutRuleTest.java
//Synthetic comment -- index 96cdbb6..b99d25d 100644

//Synthetic comment -- @@ -16,6 +16,8 @@


package com.android.ide.common.layout;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -35,9 +37,6 @@
* Common layout helpers from LayoutRule tests
*/
public abstract class AbstractLayoutRuleTest extends TestCase {
/**
* Helper function used by tests to drag a button into a canvas containing
* the given children.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java
//Synthetic comment -- index dad1420..3b399c9 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -64,7 +67,7 @@
*/
public final void testCollectIds2() {
IDragElement[] elements = TestDragElement.create(TestDragElement.create(
                "android.widget.Button", new Rect(0, 0, 100, 80)).set("myuri", ATTR_ID,
"@+id/Button01"));

Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
//Synthetic comment -- @@ -217,9 +220,9 @@
public final void testDefaultAttributeFilter() {
assertEquals("true", BaseLayout.DEFAULT_ATTR_FILTER.replace("myuri", "layout_alignRight",
"true"));
        assertEquals(null, BaseLayout.DEFAULT_ATTR_FILTER.replace(ANDROID_URI,
"layout_alignRight", "true"));
        assertEquals("true", BaseLayout.DEFAULT_ATTR_FILTER.replace(ANDROID_URI,
"myproperty", "true"));
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index 9796104..5ba8995 100644

//Synthetic comment -- @@ -16,15 +16,21 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.MenuAction.Choices;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.List;

//Synthetic comment -- @@ -84,7 +90,7 @@
rule.onDropped(targetNode, elements, feedback, dropPoint);
assertEquals(1, targetNode.getChildren().length);
assertEquals("@+id/Button01", targetNode.getChildren()[0].getStringAttr(
                ANDROID_URI, ATTR_ID));
}

// Utility for other tests
//Synthetic comment -- @@ -92,9 +98,9 @@
int insertIndex, int currentIndex,
String... graphicsFragments) {
INode linearLayout = TestNode.create("android.widget.LinearLayout").id(
                "@+id/LinearLayout01").bounds(new Rect(0, 0, 240, 480)).set(ANDROID_URI,
                ATTR_ORIENTATION,
                vertical ? VALUE_VERTICAL : VALUE_HORIZONTAL)
.add(
TestNode.create("android.widget.Button").id("@+id/Button01").bounds(
new Rect(0, 0, 100, 80)),
//Synthetic comment -- @@ -131,7 +137,7 @@
LinearLayoutRule rule = new LinearLayoutRule();
INode node = TestNode.create("android.widget.Button").id("@+id/Button012");

        assertNull(node.getStringAttr(ANDROID_URI, ATTR_ORIENTATION));

List<MenuAction> contextMenu = rule.getContextMenu(node);
assertEquals(4, contextMenu.size());
//Synthetic comment -- @@ -142,14 +148,14 @@

MenuAction.Choices choices = (Choices) orientationAction;
IMenuCallback callback = choices.getCallback();
        callback.action(orientationAction, VALUE_VERTICAL, true);

        String orientation = node.getStringAttr(ANDROID_URI,
                ATTR_ORIENTATION);
        assertEquals(VALUE_VERTICAL, orientation);
        callback.action(orientationAction, VALUE_HORIZONTAL, true);
        orientation = node.getStringAttr(ANDROID_URI, ATTR_ORIENTATION);
        assertEquals(VALUE_HORIZONTAL, orientation);
}

public void testDragInEmptyWithBounds() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java
//Synthetic comment -- index cc47df1..f365b23 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -76,7 +78,7 @@
// Drop preview
"useStyle(DROP_PREVIEW), drawRect(Rect[0,0,105,80])");

        assertEquals("true", inserted.getStringAttr(ANDROID_URI,
"layout_alignParentTop"));
}

//Synthetic comment -- @@ -144,7 +146,7 @@
String[] elements = attachment.split("=");
String name = "layout_" + elements[0];
String value = elements[1];
                assertEquals(value, inserted.getStringAttr(ANDROID_URI, name));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java
//Synthetic comment -- index b113ced..2c65317 100644

//Synthetic comment -- @@ -15,6 +15,9 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.Rect;

//Synthetic comment -- @@ -79,7 +82,7 @@
}

public TestDragElement id(String id) {
        return set(ANDROID_URI, ATTR_ID, id);
}

public static TestDragElement create(String fqn, Rect bounds) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
//Synthetic comment -- index 21250de..14430a5 100644

//Synthetic comment -- @@ -15,6 +15,9 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -50,7 +53,7 @@
}

public TestNode id(String id) {
        return set(ANDROID_URI, ATTR_ID, id);
}

public TestNode set(String uri, String name, String value) {







