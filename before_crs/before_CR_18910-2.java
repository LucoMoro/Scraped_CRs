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
public class ViewInfo {

    protected final Object mKey;
    protected final String mName;
    protected final int mLeft;
    protected final int mRight;
    protected final int mTop;
    protected final int mBottom;
    protected List<ViewInfo> mChildren = Collections.emptyList();

public ViewInfo(String name, Object key, int left, int top, int right, int bottom) {
mName = name;
mKey = key;
mLeft = left;
mRight = right;
mTop = top;
mBottom = bottom;
}

/**
* Sets the list of children {@link ViewInfo}.
*/
public void setChildren(List<ViewInfo> children) {
        mChildren = Collections.unmodifiableList(children);
}

/**
//Synthetic comment -- @@ -100,12 +114,22 @@
return mBottom;
}

/**
* Returns a map of default values for some properties. The map key is the property name,
* as found in the XML.
*/
public Map<String, String> getDefaultPropertyValues() {
        return null;
}

/**
//Synthetic comment -- @@ -114,6 +138,15 @@
* by {@link #getDefaultPropertyValues()}.
*/
public Object getViewObject() {
        return null;
}
}







