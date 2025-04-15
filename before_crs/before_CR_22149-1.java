/*New LayoutLib API to handle data binding in ExpandableListView.

Change-Id:Ic59acce7485887b4efde76697a85848e6bf91a97*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4700560..acb6572 100644

//Synthetic comment -- @@ -1533,11 +1533,13 @@

// ---------------------------------------
// Data binding DEBUG
//        AdapterBinding binding = new AdapterBinding();
//        binding.addHeader(new ResourceReference("header", false));
//        binding.addFooter(new ResourceReference("footer", false));
//        binding.addItem(new AdapterItemReference("listitem", false, 3));
//        binding.addItem(new AdapterItemReference("separator"));
//        params.addAdapterBinding(new ResourceReference("listview"), binding);
// ---------------------------------------









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/AdapterBinding.java b/layoutlib_api/src/com/android/ide/common/rendering/api/AdapterBinding.java
//Synthetic comment -- index 52fbdcb..9481246 100644

//Synthetic comment -- @@ -17,42 +17,18 @@
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
//Synthetic comment -- @@ -75,15 +51,15 @@
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

//Synthetic comment -- @@ -98,4 +74,8 @@
public ResourceReference getFooterAt(int index) {
return mFooters.get(index);
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DataBindingItem.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DataBindingItem.java
new file mode 100644
//Synthetic comment -- index 0000000..93569bd

//Synthetic comment -- @@ -0,0 +1,96 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java
//Synthetic comment -- index 5659b54..f22f51e 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
/**
* A resource reference. This contains the String ID of the resource and whether this is a framework
* reference.
*
*/
public class ResourceReference {







