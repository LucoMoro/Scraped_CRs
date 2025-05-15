
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


synchronized (mDynamicIds) {
Integer value = mDynamicIds.get(name);
if (value == null) {
                value = new Integer(++mDynamicSeed);
mDynamicIds.put(name, value);
mRevDynamicIds.put(value, name);
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


import com.android.resources.ResourceType;
import com.android.util.Pair;

/**
* Callback for project information needed by the Layout Library.
* Classes implementing this interface provide methods giving access to some project data, like
*/
public interface IProjectCallback {

/**
* Loads a custom view with the given constructor signature and arguments.
* @param name The fully qualified name of the class.
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
* the given {@link ResourceReference} already.
*
* @param adapterView the adapter view to return the adapter binding for.
* @return an adapter binding for the given view or null if there's no data.
*/
    AdapterBinding getAdapterBinding(ResourceReference adapterView);
}

//<End of snippet n. 3>








