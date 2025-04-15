/*Update ViewInfo in the layoutlib API.

- support for View and LayoutParams
- support for default property value map.

Change-Id:I70028710b1f76329a8bd501428fbd68a14fafa1e*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 8e0fe73..85c7365 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.layoutlib.api.SceneResult.LayoutStatus;

import java.awt.image.BufferedImage;

/**
* An object allowing interaction with an Android layout.
//Synthetic comment -- @@ -81,6 +82,17 @@
return null;
}

/**
* Re-renders the layout as-is.
* In case of success, this should be followed by calls to {@link #getRootView()} and








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index d991cc8..eb0b333 100644

//Synthetic comment -- @@ -18,35 +18,47 @@

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
//Synthetic comment -- @@ -57,12 +69,12 @@
}

/**
     * Returns the key associated with the node. Can be null.
*
* @see IXmlPullParser#getViewKey()
*/
    public Object getViewKey() {
        return mKey;
}

/**
//Synthetic comment -- @@ -101,11 +113,12 @@
}

/**
     * Returns a map of default values for some properties. The map key is the property name,
     * as found in the XML.
*/
    public Map<String, String> getDefaultPropertyValues() {
        return null;
}

/**
//Synthetic comment -- @@ -113,7 +126,7 @@
* to query the object properties that are not in the XML and not in the map returned
* by {@link #getDefaultPropertyValues()}.
*/
    public Object getViewObject() {
        return null;
}
}







