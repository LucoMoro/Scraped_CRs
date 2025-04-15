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
        ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);

// Create a NodeProxy.
//Synthetic comment -- @@ -94,7 +95,8 @@
public final void testCreateDup() {
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
        ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);

// NodeProxies are cached. Creating the same one twice returns the same proxy.
//Synthetic comment -- @@ -106,7 +108,8 @@
public final void testClear() {
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
        ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);

// NodeProxies are cached. Creating the same one twice returns the same proxy.








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index f4c99bd..9345f40 100644

//Synthetic comment -- @@ -163,7 +163,8 @@
private ViewInfo convertToViewInfo(ILayoutViewInfo view) {
// create the view info.
ViewInfo viewInfo = new ViewInfo(view.getName(), view.getViewKey(),
                view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

// then convert the children
ILayoutViewInfo[] children = view.getChildren();








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index d991cc8..6ffc560 100644

//Synthetic comment -- @@ -23,30 +23,40 @@
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
//Synthetic comment -- @@ -100,12 +110,22 @@
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
//Synthetic comment -- @@ -114,6 +134,15 @@
* by {@link #getDefaultPropertyValues()}.
*/
public Object getViewObject() {
        return null;
}
}







