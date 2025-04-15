/*New LayoutLib API to handle data binding in ExpandableListView.

Change-Id:Ic59acce7485887b4efde76697a85848e6bf91a97*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4700560..acb6572 100644

//Synthetic comment -- @@ -1533,11 +1533,13 @@

// ---------------------------------------
// Data binding DEBUG
//        AdapterBinding binding = new AdapterBinding(3);
//        binding.addHeader(new ResourceReference("header", false));
//        binding.addFooter(new ResourceReference("footer", false));
//        DataBindingItem item = new DataBindingItem("groupitem", false, 3);
//        binding.addItem(item);
//        item.addChild(new DataBindingItem("separator", false, 1));
//        item.addChild(new DataBindingItem("listitem", false, 3));
//        params.addAdapterBinding(new ResourceReference("listview"), binding);
// ---------------------------------------









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/AdapterBinding.java b/layoutlib_api/src/com/android/ide/common/rendering/api/AdapterBinding.java
//Synthetic comment -- index 52fbdcb..9481246 100644

//Synthetic comment -- @@ -17,42 +17,18 @@
package com.android.ide.common.rendering.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* Describe the content of the dynamic android.widget.Adapter used to fill
* android.widget.AdapterView
*/
public class AdapterBinding implements Iterable<DataBindingItem> {

private final int mRepeatCount;
private final List<ResourceReference> mHeaders = new ArrayList<ResourceReference>();
    private final List<DataBindingItem> mItems = new ArrayList<DataBindingItem>();
private final List<ResourceReference> mFooters = new ArrayList<ResourceReference>();

public AdapterBinding(int repeatCount) {
//Synthetic comment -- @@ -75,15 +51,15 @@
return mHeaders.get(index);
}

    public void addItem(DataBindingItem item) {
        mItems.add(item);
}

public int getItemCount() {
return mItems.size();
}

    public DataBindingItem getItemAt(int index) {
return mItems.get(index);
}

//Synthetic comment -- @@ -98,4 +74,8 @@
public ResourceReference getFooterAt(int index) {
return mFooters.get(index);
}

    public Iterator<DataBindingItem> iterator() {
        return mItems.iterator();
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DataBindingItem.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DataBindingItem.java
new file mode 100644
//Synthetic comment -- index 0000000..93569bd

//Synthetic comment -- @@ -0,0 +1,96 @@
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

import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A data binding item. It contain a {@link ResourceReference} to the view used to represent it.
 * It also contains how many items of this type the AdapterView should display.
 *
 * It can also contain an optional list of children in case the AdapterView is an
 * ExpandableListView. In this case, the count value is used as a repeat count for the children,
 * similar to {@link AdapterBinding#getRepeatCount()}.
 *
 */
public class DataBindingItem implements Iterable<DataBindingItem> {
    private final ResourceReference mReference;
    private final int mCount;
    private List<DataBindingItem> mChildren;

    public DataBindingItem(ResourceReference reference, int count) {
        mReference = reference;
        mCount = count;
    }

    public DataBindingItem(String name, boolean platformLayout, int count) {
        this(new ResourceReference(name, platformLayout), count);
    }

    public DataBindingItem(String name, boolean platformLayout) {
        this(name, platformLayout, 1);
    }

    public DataBindingItem(String name, int count) {
        this(name, false /*platformLayout*/, count);
    }

    public DataBindingItem(String name) {
        this(name, false /*platformLayout*/, 1);
    }

    /**
     * Returns the {@link ResourceReference} for the view. The {@link ResourceType} for the
     * referenced resource is implied to be {@link ResourceType#LAYOUT}.
     */
    public ResourceReference getViewReference() {
        return mReference;
    }

    /**
     * The repeat count for this object or the repeat count for the children if there are any.
     */
    public int getCount() {
        return mCount;
    }

    public void addChild(DataBindingItem child) {
        if (mChildren == null) {
            mChildren = new ArrayList<DataBindingItem>();
        }

        mChildren.add(child);
    }

    public List<DataBindingItem> getChildren() {
        if (mChildren != null) {
            return mChildren;
        }

        return Collections.emptyList();
    }

    public Iterator<DataBindingItem> iterator() {
        List<DataBindingItem> list = getChildren();
        return list.iterator();
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java
//Synthetic comment -- index 5659b54..f22f51e 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
/**
* A resource reference. This contains the String ID of the resource and whether this is a framework
* reference.
 * This is an immutable class.
*
*/
public class ResourceReference {







