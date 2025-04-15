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
    public static final String ATTR_CHECKED = "checked";                //$NON-NLS-1$

public static final String ATTR_LAYOUT_PREFIX = "layout_";          //$NON-NLS-1$
public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
//Synthetic comment -- @@ -122,6 +123,9 @@
/** The fully qualified class name of a Button view */
public static final String FQCN_BUTTON = "android.widget.Button"; //$NON-NLS-1$

    /** The fully qualified class name of a RadioButton view */
    public static final String FQCN_RADIO_BUTTON = "android.widget.RadioButton"; //$NON-NLS-1$

/** The fully qualified class name of an AdapterView */
public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java
new file mode 100644
//Synthetic comment -- index 0000000..039b495

//Synthetic comment -- @@ -0,0 +1,46 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CHECKED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
 * An {@link IViewRule} for android.widget.RadioGroup which initializes the radio group
 * with some radio buttons
 */
public class RadioGroupRule extends LinearLayoutRule {
    @Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
        super.onCreate(node, parent, insertType);

        if (insertType == InsertType.CREATE) {
            for (int i = 0; i < 3; i++) {
                INode handle = node.appendChild(LayoutConstants.FQCN_RADIO_BUTTON);
                handle.setAttribute(ANDROID_URI, ATTR_ID, String.format("@+id/radio%d", i));
                if (i == 0) {
                    handle.setAttribute(ANDROID_URI, ATTR_CHECKED, VALUE_TRUE);
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java
//Synthetic comment -- index fb0f33e..15c3b4c 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
* An {@link IViewRule} for android.widget.SlidingDrawerRule which initializes new sliding
* drawers with their mandatory children and default sizing attributes
*/
public class SlidingDrawerRule extends BaseLayoutRule {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java
//Synthetic comment -- index a13f42f..8ec53db 100755

//Synthetic comment -- @@ -16,7 +16,13 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
* An {@link IViewRule} for android.widget.ZoomControls.
//Synthetic comment -- @@ -24,4 +30,15 @@
public class WebViewRule extends IgnoredLayoutRule {
// A WebView is not a general purpose AbsoluteLayout you should drop stuff
// into; it's an AbsoluteLayout for implementation purposes.

    @Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
        super.onCreate(node, parent, insertType);

        if (insertType == InsertType.CREATE) {
            String matchParent = getFillParentValueName();
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, matchParent);
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, matchParent);
        }
    }
}







