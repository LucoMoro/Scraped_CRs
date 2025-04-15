/*LayoutLib API: update adapter binding callbacks.

Change-Id:I85be09fd7541da366c858ea04d7c2c4b53948475*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 1b9b103..ca6982f 100644

//Synthetic comment -- @@ -339,16 +339,17 @@
return constructor.newInstance(constructorParameters);
}

    public String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
            int fullPosition, int typePosition, ResourceReference viewRef, String viewClass) {
        if (viewClass.contains("TextView")) {
return viewRef.getName() + " " + typePosition;
}

return null;
}

    public AdapterBinding getAdapterBinding(ResourceReference adapterView) {
return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 5b4bf38..ec8b717 100644

//Synthetic comment -- @@ -265,7 +265,7 @@
synchronized (mDynamicIds) {
Integer value = mDynamicIds.get(name);
if (value == null) {
                value = new Integer(++mDynamicSeed);
mDynamicIds.put(name, value);
mRevDynamicIds.put(value, name);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 18e7bac..7da7717 100644

//Synthetic comment -- @@ -129,12 +129,13 @@
return null;
}

        public String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
                int fullPosition, int typePosition, ResourceReference viewRef, String viewClass) {
return null;
}

        public AdapterBinding getAdapterBinding(ResourceReference adapterView) {
return null;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 1a16c48..144d317 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.resources.ResourceType;
import com.android.util.Pair;

/**
* Callback for project information needed by the Layout Library.
* Classes implementing this interface provide methods giving access to some project data, like
//Synthetic comment -- @@ -26,6 +28,23 @@
*/
public interface IProjectCallback {

/**
* Loads a custom view with the given constructor signature and arguments.
* @param name The fully qualified name of the class.
//Synthetic comment -- @@ -77,17 +96,22 @@
/**
* Returns the value of an item used by an adapter.
* @param adapterView The {@link ResourceReference} for the adapter view info.
* @param itemRef the {@link ResourceReference} for the layout used by the adapter item.
* @param fullPosition the position of the item in the full list.
* @param typePosition the position of the item if only items of the same type are considered.
*     If there is only one type of items, this is the same as <var>position</var>.
* @param viewRef The {@link ResourceReference} for the view we're trying to fill.
     * @param viewClass the class name of the view we're trying to fill.
* @return the item value or null if there's no value.
*/
    String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
            int fullPosition, int typePosition,
            ResourceReference viewRef, String viewClass);

/**
* Returns an adapter binding for a given adapter view.
//Synthetic comment -- @@ -95,7 +119,8 @@
* the given {@link ResourceReference} already.
*
* @param adapterView the adapter view to return the adapter binding for.
* @return an adapter binding for the given view or null if there's no data.
*/
    AdapterBinding getAdapterBinding(ResourceReference adapterView);
}







