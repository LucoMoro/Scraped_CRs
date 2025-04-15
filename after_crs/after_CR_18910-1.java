/*Update ViewInfo in the layoutlib API.

- support for View and LayoutParams
- support for default property value map.

Change-Id:I70028710b1f76329a8bd501428fbd68a14fafa1e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java
//Synthetic comment -- index ad39fe6..236d21e 100755

//Synthetic comment -- @@ -47,7 +47,8 @@
public final void testCreateCanvasViewInfo() {
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
        ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120,
                null /*viewObject*/, null /*layoutParamsObject*/);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);

// Create a NodeProxy.
//Synthetic comment -- @@ -94,7 +95,8 @@
public final void testCreateDup() {
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
        ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120,
                null /*viewObject*/, null /*layoutParamsObject*/);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);

// NodeProxies are cached. Creating the same one twice returns the same proxy.
//Synthetic comment -- @@ -106,7 +108,8 @@
public final void testClear() {
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
        ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120,
                null /*viewObject*/, null /*layoutParamsObject*/);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);

// NodeProxies are cached. Creating the same one twice returns the same proxy.








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index f4c99bd..9345f40 100644

//Synthetic comment -- @@ -163,7 +163,8 @@
private ViewInfo convertToViewInfo(ILayoutViewInfo view) {
// create the view info.
ViewInfo viewInfo = new ViewInfo(view.getName(), view.getViewKey(),
                view.getLeft(), view.getTop(), view.getRight(), view.getBottom(),
                null /*viewObject*/, null /*layoutParamsObject*/);

// then convert the children
ILayoutViewInfo[] children = view.getChildren();








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index d991cc8..6ffc560 100644

//Synthetic comment -- @@ -23,30 +23,40 @@
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
//Synthetic comment -- @@ -100,12 +110,22 @@
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
//Synthetic comment -- @@ -114,6 +134,15 @@
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







