/*Update ViewInfo in the layoutlib API.

- support for View and LayoutParams
- support for default property value map.

Change-Id:I70028710b1f76329a8bd501428fbd68a14fafa1e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index bff1606..f944650 100755

//Synthetic comment -- @@ -91,7 +91,7 @@
// UiViewElementNode.
// We'll simply crash if the type is not right, as this is not supposed to happen
// and nothing could work if there's a type mismatch.
        mUiViewKey  = (UiViewElementNode) viewInfo.getCookie();

int x = viewInfo.getLeft();
int y = viewInfo.getTop();
//Synthetic comment -- @@ -110,7 +110,7 @@
// Only use children which have a ViewKey of the correct type.
// We can't interact with those when they have a null key or
// an incompatible type.
                if (child.getCookie() instanceof UiViewElementNode) {
mChildren.add(new CanvasViewInfo(child, this, x, y));
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 8e0fe73..85c7365 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.layoutlib.api.SceneResult.LayoutStatus;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
* An object allowing interaction with an Android layout.
//Synthetic comment -- @@ -81,6 +82,17 @@
return null;
}


    /**
     * Returns a map of (XML attribute name, attribute value) containing only default attribute
     * values, for the given view Object.
     * @param viewObject the view object.
     * @return a map of the default property values or null.
     */
    public Map<String, String> getDefaultViewPropertyValues(Object viewObject) {
        return null;
    }

/**
* Re-renders the layout as-is.
* In case of success, this should be followed by calls to {@link #getRootView()} and








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index d991cc8..d740816 100644

//Synthetic comment -- @@ -18,35 +18,47 @@

import java.util.Collections;
import java.util.List;

/**
* Layout information for a specific view object
*/
public final class ViewInfo {

    private final Object mCookie;
    private final String mName;
    private final int mLeft;
    private final int mRight;
    private final int mTop;
    private final int mBottom;
    private List<ViewInfo> mChildren = Collections.emptyList();
    private final Object mViewObject;
    private final Object mLayoutParamsObject;

    public ViewInfo(String name, Object cookie, int left, int top, int right, int bottom) {
        this(name, cookie, left, top, right, bottom, null /*viewObject*/, null /*layoutParamsObject*/);
    }

    public ViewInfo(String name, Object cookie, int left, int top, int right, int bottom,
            Object viewObject, Object layoutParamsObject) {
mName = name;
        mCookie = cookie;
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
//Synthetic comment -- @@ -57,12 +69,12 @@
}

/**
     * Returns the cookie associated with the XML node. Can be null.
*
* @see IXmlPullParser#getViewKey()
*/
    public Object getCookie() {
        return mCookie;
}

/**
//Synthetic comment -- @@ -101,11 +113,12 @@
}

/**
     * Returns the actual android.view.View (or child class) object. This can be used
     * to query the object properties that are not in the XML and not in the map returned
     * by {@link #getDefaultPropertyValues()}.
*/
    public Object getViewObject() {
        return mViewObject;
}

/**
//Synthetic comment -- @@ -113,7 +126,7 @@
* to query the object properties that are not in the XML and not in the map returned
* by {@link #getDefaultPropertyValues()}.
*/
    public Object getLayoutParamsObject() {
        return mLayoutParamsObject;
}
}







