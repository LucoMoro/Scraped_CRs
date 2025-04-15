/*Move getViewParent/Index from RenderSession to Bridge.

Also update the IProjectCallback to handle expandableListView.

Change-Id:I36ca8733182bbd9d169fee3e709ebc532fef7b1e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index ca6982f..7b88a16 100644

//Synthetic comment -- @@ -340,7 +340,8 @@
}

public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
            ResourceReference itemRef, int fullPosition, int typePosition,
ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue) {
if (viewAttribute == ViewAttribute.TEXT && ((String) defaultValue).length() == 0) {
return viewRef.getName() + " " + typePosition;
//Synthetic comment -- @@ -349,7 +350,8 @@
return null;
}

    public AdapterBinding getAdapterBinding(ResourceReference adapterView, Object adapterCookie) {
return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 9b8f554..a043e6d 100644

//Synthetic comment -- @@ -131,12 +131,13 @@

public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
ResourceReference itemRef, int fullPosition, int typePosition,
ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue) {
return null;
}

public AdapterBinding getAdapterBinding(ResourceReference adapterView,
                Object adapterCookie) {
return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index ed0d67b..2df472e 100644

//Synthetic comment -- @@ -17,9 +17,9 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.MergeCookie;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.AdapterBinding.AdapterItemReference;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
//Synthetic comment -- @@ -675,12 +675,12 @@
UiViewElementNode childNode = createNode(rootNode, "ListView", false);
/*UiViewElementNode grandChildNode =*/ createNode(childNode, "LinearLayout", false);
/*UiViewElementNode greatGrandChildNode =*/ createNode(childNode, "TextView", false);
        AdapterItemReference adapterItem = new AdapterItemReference("foo");

ViewInfo root = new ViewInfo("FrameLayout", rootNode, 0, 50, 320, 480);
ViewInfo child = new ViewInfo("ListView", childNode, 0, 0, 320, 430);
root.setChildren(Collections.singletonList(child));
        ViewInfo grandChild = new ViewInfo("LinearLayout", adapterItem, 0, 0, 320, 17);
child.setChildren(Collections.singletonList(grandChild));
ViewInfo greatGrandChild = new ViewInfo("Button", null, 0, 0, 73, 17);
grandChild.setChildren(Collections.singletonList(greatGrandChild));








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index d9ffda7..3294cbe 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.rendering;

import com.android.ide.common.log.ILogger;
import com.android.ide.common.rendering.api.Bridge;
import com.android.ide.common.rendering.api.Capability;
//Synthetic comment -- @@ -321,6 +323,38 @@
}
}

// ------ Implementation

private LayoutLibrary(Bridge bridge, ILayoutBridge legacyBridge, ClassLoader classLoader,
//Synthetic comment -- @@ -462,7 +496,7 @@
for (Entry<ResourceType, Map<String, ResourceValue>> entry : map.entrySet()) {
// ugly case but works.
result.put(entry.getKey().getName(),
                    (Map<String, IResourceValue>)(Map) entry.getValue());
}

return result;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index 48309cf..f04b266 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.common.rendering.api;


import com.android.ide.common.rendering.api.Result.Status;

import java.awt.image.BufferedImage;
//Synthetic comment -- @@ -109,4 +111,27 @@
public void clearCaches(Object projectKey) {

}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 144d317..99a1267 100644

//Synthetic comment -- @@ -100,7 +100,12 @@
* @param itemRef the {@link ResourceReference} for the layout used by the adapter item.
* @param fullPosition the position of the item in the full list.
* @param typePosition the position of the item if only items of the same type are considered.
     *     If there is only one type of items, this is the same as <var>position</var>.
* @param viewRef The {@link ResourceReference} for the view we're trying to fill.
* @param ViewAttribute the attribute being queried.
* @param defaultValue the default value for this attribute. The object class matches the
//Synthetic comment -- @@ -110,7 +115,9 @@
* @see ViewAttribute#getAttributeClass()
*/
Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
            ResourceReference itemRef, int fullPosition, int typePosition,
ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue);

/**
//Synthetic comment -- @@ -118,9 +125,11 @@
* This is only called if {@link SessionParams} does not have an {@link AdapterBinding} for
* the given {@link ResourceReference} already.
*
     * @param adapterView the adapter view to return the adapter binding for.
* @param adapterCookie the view cookie for this particular view.
* @return an adapter binding for the given view or null if there's no data.
*/
    AdapterBinding getAdapterBinding(ResourceReference adapterView, Object adapterCookie);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java
//Synthetic comment -- index a2e087c..188909e 100644

//Synthetic comment -- @@ -162,29 +162,6 @@
}

/**
     * Returns the View parent.
     *
     * @param viewObject the object for which to return the parent.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the parent
     *      object in {@link Result#getData()}
     */
    public Result getViewParent(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
     * Returns the index of a given view it its parent.
     * @param viewObject the object for which to return the index.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the index in
     *      the parent in {@link Result#getData()}
     */
    public Result getViewIndex(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
* Inserts a new child in a ViewGroup object, and renders the result.
* <p/>
* The child is first inflated and then added to its new parent, at the given <var>index<var>







