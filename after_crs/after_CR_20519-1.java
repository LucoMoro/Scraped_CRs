/*Add drop handler for SlidingDrawer

Add a drop handler for SlidingDrawers such that when dropped from the
palette they create the mandatory handle and content
children. (Without this, you get a rendering error instead.)

Change-Id:Icb521fca9cdbb1da14693d4872ef3fb00187d8f7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index e8f9261..2669a6b 100644

//Synthetic comment -- @@ -45,6 +45,8 @@

public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
public static final String ATTR_ID = "id";                          //$NON-NLS-1$
    public static final String ATTR_HANDLE = "handle";                  //$NON-NLS-1$
    public static final String ATTR_CONTENT = "content";                //$NON-NLS-1$

public static final String ATTR_LAYOUT_PREFIX = "layout_";          //$NON-NLS-1$
public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
//Synthetic comment -- @@ -117,6 +119,9 @@
/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

    /** The fully qualified class name of a Button view */
    public static final String FQCN_BUTTON = "android.widget.Button"; //$NON-NLS-1$

/** The fully qualified class name of an AdapterView */
public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java
new file mode 100644
//Synthetic comment -- index 0000000..fb0f33e

//Synthetic comment -- @@ -0,0 +1,64 @@
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
import static com.android.ide.common.layout.LayoutConstants.ATTR_CONTENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HANDLE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
 * An {@link IViewRule} for android.widget.SlidingDrawerRule which initializes new sliding
 * drawers with their mandatory children and default sizing attributes
 */
public class SlidingDrawerRule extends BaseViewRule {

    @Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
        super.onCreate(node, parent, insertType);

        if (insertType == InsertType.CREATE) {
            String matchParent = getFillParentValueName();
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, matchParent);
            node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, matchParent);

            // Create mandatory children and reference them from the handle and content
            // attributes of the sliding drawer
            String handleId = "@+id/handle";    //$NON-NLS-1$
            String contentId = "@+id/content";  //$NON-NLS-1$
            node.setAttribute(ANDROID_URI, ATTR_HANDLE, handleId);
            node.setAttribute(ANDROID_URI, ATTR_CONTENT, contentId);

            // Handle
            INode handle = node.appendChild(LayoutConstants.FQCN_BUTTON);
            handle.setAttribute(ANDROID_URI, ATTR_TEXT, "Handle");
            handle.setAttribute(ANDROID_URI, ATTR_ID, handleId);

            // Content
            INode content = node.appendChild(LayoutConstants.FQCN_LINEAR_LAYOUT);
            content.setAttribute(ANDROID_URI, ATTR_ID, contentId);
            content.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, matchParent);
            content.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, matchParent);
        }
    }
}







