
//<Beginning of snippet n. 0>


return constructor.newInstance(constructorParameters);
}

    public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
            ResourceReference itemRef, int fullPosition, int typePosition,
            ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue) {
        if (viewAttribute == ViewAttribute.TEXT && ((String) defaultValue).length() == 0) {
return viewRef.getName() + " " + typePosition;
}

return null;
}

    public AdapterBinding getAdapterBinding(ResourceReference adapterView, Object adapterCookie) {
return null;
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


synchronized (mDynamicIds) {
Integer value = mDynamicIds.get(name);
if (value == null) {
                value = Integer.valueOf(++mDynamicSeed);
mDynamicIds.put(name, value);
mRevDynamicIds.put(value, name);
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


return null;
}

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

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


import com.android.resources.ResourceType;
import com.android.util.Pair;

import java.net.URL;

/**
* Callback for project information needed by the Layout Library.
* Classes implementing this interface provide methods giving access to some project data, like
*/
public interface IProjectCallback {

    public enum ViewAttribute {
        TEXT(String.class),
        IS_CHECKED(Boolean.class),
        SRC(URL.class),
        COLOR(Integer.class);

        private final Class<?> mClass;

        private ViewAttribute(Class<?> theClass) {
            mClass = theClass;
        }

        public Class<?> getAttributeClass() {
            return mClass;
        }
    }

/**
* Loads a custom view with the given constructor signature and arguments.
* @param name The fully qualified name of the class.
/**
* Returns the value of an item used by an adapter.
* @param adapterView The {@link ResourceReference} for the adapter view info.
     * @param adapterCookie the view cookie for this particular view.
* @param itemRef the {@link ResourceReference} for the layout used by the adapter item.
* @param fullPosition the position of the item in the full list.
* @param typePosition the position of the item if only items of the same type are considered.
*     If there is only one type of items, this is the same as <var>position</var>.
* @param viewRef The {@link ResourceReference} for the view we're trying to fill.
     * @param ViewAttribute the attribute being queried.
     * @param defaultValue the default value for this attribute. The object class matches the
     *      class associated with the {@link ViewAttribute}.
* @return the item value or null if there's no value.
     *
     * @see ViewAttribute#getAttributeClass()
*/
    Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
            ResourceReference itemRef, int fullPosition, int typePosition,
            ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue);

/**
* Returns an adapter binding for a given adapter view.
* the given {@link ResourceReference} already.
*
* @param adapterView the adapter view to return the adapter binding for.
     * @param adapterCookie the view cookie for this particular view.
* @return an adapter binding for the given view or null if there's no data.
*/
    AdapterBinding getAdapterBinding(ResourceReference adapterView, Object adapterCookie);
}

//<End of snippet n. 3>








