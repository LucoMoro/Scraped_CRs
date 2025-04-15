/*Update ViewInfo in the layoutlib API.

- support for View and LayoutParams
- support for default property value map.

Change-Id:I70028710b1f76329a8bd501428fbd68a14fafa1e*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index d991cc8..bdb14e5 100644

//Synthetic comment -- @@ -23,30 +23,44 @@
/**
* Layout information for a specific view object
*/
public final class ViewInfo {

    private final Object mKey;
    private final String mName;
    private final int mLeft;
    private final int mRight;
    private final int mTop;
    private final int mBottom;
    private List<ViewInfo> mChildren = Collections.emptyList();
    private Map<String, String> mDefaultPropertyValues = Collections.emptyMap();
    private final Object mViewObject;
    private final Object mLayoutParamsObject;

public ViewInfo(String name, Object key, int left, int top, int right, int bottom) {
        this(name, key, left, top, right, bottom, null /*viewObject*/, null /*layoutParamsObject*/);
    }

    public ViewInfo(String name, Object key, int left, int top, int right, int bottom,
            Object viewObject, Object layoutParamsObject) {
mName = name;
mKey = key;
mLeft = left;
mRight = right;
mTop = top;
mBottom = bottom;
        mViewObject = viewObject;
        mLayoutParamsObject = layoutParamsObject;
}

/**
* Sets the list of children {@link ViewInfo}.
*/
public void setChildren(List<ViewInfo> children) {
        if (children != null) {
            mChildren = Collections.unmodifiableList(children);
        } else {
            mChildren = Collections.emptyList();
        }
}

/**
//Synthetic comment -- @@ -100,12 +114,22 @@
return mBottom;
}

    public void setDefaultPropertyValues(Map<String, String> map) {
        if (map != null) {
            mDefaultPropertyValues = Collections.unmodifiableMap(map);
        } else {
            mDefaultPropertyValues = Collections.emptyMap();
        }
    }

/**
* Returns a map of default values for some properties. The map key is the property name,
* as found in the XML.
     * <p>
     * This is never null, but can be empty if no default property values were found.
*/
public Map<String, String> getDefaultPropertyValues() {
        return mDefaultPropertyValues;
}

/**
//Synthetic comment -- @@ -114,6 +138,15 @@
* by {@link #getDefaultPropertyValues()}.
*/
public Object getViewObject() {
        return mViewObject;
    }

    /**
     * Returns the actual android.view.View (or child class) object. This can be used
     * to query the object properties that are not in the XML and not in the map returned
     * by {@link #getDefaultPropertyValues()}.
     */
    public Object getLayoutParamsObject() {
        return mLayoutParamsObject;
}
}







