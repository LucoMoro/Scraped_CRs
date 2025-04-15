/*Add a way to get the adapter binding through the project callback.

Change-Id:I0de48e3519f38a63c3462f5eb0c7f6c2a29d0e6f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index de52133..1b9b103 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.ResourceReference;
//Synthetic comment -- @@ -346,4 +347,8 @@

return null;
}

    public AdapterBinding getAdapterBinding(ResourceReference adapterView) {
        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index f5c843c..18e7bac 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.AndroidConstants;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderSession;
//Synthetic comment -- @@ -132,6 +133,10 @@
int fullPosition, int typePosition, ResourceReference viewRef, String viewClass) {
return null;
}

        public AdapterBinding getAdapterBinding(ResourceReference adapterView) {
            return null;
        }
}

public void testApiDemos() throws IOException, XmlPullParserException {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/AdapterBinding.java b/layoutlib_api/src/com/android/ide/common/rendering/api/AdapterBinding.java
new file mode 100644
//Synthetic comment -- index 0000000..52fbdcb

//Synthetic comment -- @@ -0,0 +1,101 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.rendering.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe the content of the dynamic android.widget.Adapter used to fill
 * android.widget.AdapterView
 */
public class AdapterBinding {

    /**
     * An AdapterItemReference. On top of being a {@link ResourceReference}, it contains how many
     * items of this type the data binding should display.
     */
    public static class AdapterItemReference extends ResourceReference {
        private final int mCount;

        public AdapterItemReference(String name, boolean platformLayout, int count) {
            super(name, platformLayout);
            mCount = count;
        }

        public AdapterItemReference(String name, boolean platformLayout) {
            this(name, platformLayout, 1);
        }

        public AdapterItemReference(String name) {
            this(name, false /*platformLayout*/, 1);
        }

        public int getCount() {
            return mCount;
        }
    }

    private final int mRepeatCount;
    private final List<ResourceReference> mHeaders = new ArrayList<ResourceReference>();
    private final List<AdapterItemReference> mItems = new ArrayList<AdapterItemReference>();
    private final List<ResourceReference> mFooters = new ArrayList<ResourceReference>();

    public AdapterBinding(int repeatCount) {
        mRepeatCount = repeatCount;
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    public void addHeader(ResourceReference layoutInfo) {
        mHeaders.add(layoutInfo);
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    public ResourceReference getHeaderAt(int index) {
        return mHeaders.get(index);
    }

    public void addItem(AdapterItemReference itemInfo) {
        mItems.add(itemInfo);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public AdapterItemReference getItemAt(int index) {
        return mItems.get(index);
    }

    public void addFooter(ResourceReference layoutInfo) {
        mFooters.add(layoutInfo);
    }

    public int getFooterCount() {
        return mFooters.size();
    }

    public ResourceReference getFooterAt(int index) {
        return mFooters.get(index);
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 161a1a5..1a16c48 100644

//Synthetic comment -- @@ -88,4 +88,14 @@
String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
int fullPosition, int typePosition,
ResourceReference viewRef, String viewClass);

    /**
     * Returns an adapter binding for a given adapter view.
     * This is only called if {@link SessionParams} does not have an {@link AdapterBinding} for
     * the given {@link ResourceReference} already.
     *
     * @param adapterView the adapter view to return the adapter binding for.
     * @return an adapter binding for the given view or null if there's no data.
     */
    AdapterBinding getAdapterBinding(ResourceReference adapterView);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index e80378e..f4f6b5c 100644

//Synthetic comment -- @@ -18,10 +18,8 @@

import com.android.resources.Density;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
//Synthetic comment -- @@ -52,86 +50,6 @@
}
}

private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;







