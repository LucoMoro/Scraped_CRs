/*Add baseline/margins to ViewInfo.

Also make it so that older layoutlib that are using API5 return the
value through reflection (done in LayoutLibrary.)

Change-Id:I3a32666e525f0f1d37a13e670d1d1c659b8e2027*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index e1256fe..d8ee352 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.rendering;

import static com.android.ide.common.rendering.api.Result.Status.ERROR_REFLECTION;

import com.android.ide.common.log.ILogger;
import com.android.ide.common.rendering.api.Bridge;
//Synthetic comment -- @@ -48,11 +48,13 @@
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//Synthetic comment -- @@ -91,6 +93,15 @@
/** classloader used to load the jar file */
private final ClassLoader mClassLoader;

    private Method sViewGetParentMethod;
    private Method sViewParentIndexOfChildMethod;
    private Class<?> mMarginLayoutParamClass;

    private Field mLeftMarginField;
    private Field mTopMarginField;
    private Field mRightMarginField;
    private Field mBottomMarginField;

/**
* Returns the {@link LoadStatus} of the loading of the layoutlib jar file.
*/
//Synthetic comment -- @@ -282,7 +293,16 @@
*/
public RenderSession createSession(SessionParams params) {
if (mBridge != null) {
            RenderSession session = mBridge.createSession(params);
            if (params.getExtendedViewInfoMode() &&
                    mBridge.getCapabilities().contains(Capability.EXTENDED_VIEWINFO) == false) {
                // Extended view info was requested but the layoutlib does not support it.
                // Add it manually.
                List<ViewInfo> infoList = session.getRootViews();
                for (ViewInfo info : infoList) {
                    addExtendedViewInfo(info);
                }
            }
} else if (mLegacyBridge != null) {
return createLegacySession(params);
}
//Synthetic comment -- @@ -333,10 +353,13 @@
*/
public Result getViewParent(Object viewObject) {
if (mBridge != null) {
            Result r = mBridge.getViewParent(viewObject);
            if (r.isSuccess()) {
                return r;
            }
}

        return getViewParentWithReflection(viewObject);
}

/**
//Synthetic comment -- @@ -348,30 +371,15 @@
*/
public Result getViewIndex(Object viewObject) {
if (mBridge != null) {
            Result r = mBridge.getViewIndex(viewObject);
            if (r.isSuccess()) {
                return r;
            }
}

        return getViewIndexReflection(viewObject);
}

// ------ Implementation

private LayoutLibrary(Bridge bridge, ILayoutBridge legacyBridge, ClassLoader classLoader,
//Synthetic comment -- @@ -440,7 +448,6 @@
};


// convert the map of ResourceValue into IResourceValue. Super ugly but works.

Map<String, Map<String, IResourceValue>> projectMap = convertMap(
//Synthetic comment -- @@ -551,6 +558,10 @@
ViewInfo viewInfo = new ViewInfo(view.getName(), view.getViewKey(),
view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

        // set the extended value to a given value indicating the actual values are unknown.
        viewInfo.setExtendedInfo(Integer.MIN_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

// then convert the children
ILayoutViewInfo[] children = view.getChildren();
if (children != null) {
//Synthetic comment -- @@ -584,4 +595,129 @@
// do nothing.
}
}

    private Result getViewParentWithReflection(Object viewObject) {
        // default implementation using reflection.
        try {
            if (sViewGetParentMethod == null) {
                Class<?> viewClass = Class.forName("android.view.View");
                sViewGetParentMethod = viewClass.getMethod("getParent");
            }

            return Status.SUCCESS.createResult(sViewGetParentMethod.invoke(viewObject));
        } catch (Exception e) {
            // Catch all for the reflection calls.
            return ERROR_REFLECTION.createResult(null, e);
        }
    }

    /**
     * Utility method returning the index of a given view in its parent.
     * @param viewObject the object for which to return the index.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the index in
     *      the parent in {@link Result#getData()}
     */
    private Result getViewIndexReflection(Object viewObject) {
        // default implementation using reflection.
        try {
            Class<?> viewClass = Class.forName("android.view.View");

            if (sViewGetParentMethod == null) {
                sViewGetParentMethod = viewClass.getMethod("getParent");
            }

            Object parentObject = sViewGetParentMethod.invoke(viewObject);

            if (sViewParentIndexOfChildMethod == null) {
                Class<?> viewParentClass = Class.forName("android.view.ViewParent");
                sViewParentIndexOfChildMethod = viewParentClass.getMethod("indexOfChild",
                        viewClass);
            }

            return Status.SUCCESS.createResult(
                    sViewParentIndexOfChildMethod.invoke(parentObject, viewObject));
        } catch (Exception e) {
            // Catch all for the reflection calls.
            return ERROR_REFLECTION.createResult(null, e);
        }
    }

    private void addExtendedViewInfo(ViewInfo info) {
        computeExtendedViewInfo(info);

        List<ViewInfo> children = info.getChildren();
        for (ViewInfo child : children) {
            addExtendedViewInfo(child);
        }
    }

    private void computeExtendedViewInfo(ViewInfo info) {
        Object viewObject = info.getViewObject();
        Object params = info.getLayoutParamsObject();

        int baseLine = getViewBaselineReflection(viewObject);
        int leftMargin = 0;
        int topMargin = 0;
        int rightMargin = 0;
        int bottomMargin = 0;

        try {
            if (mMarginLayoutParamClass == null) {
                mMarginLayoutParamClass = Class.forName(
                        "android.view.ViewGroup$MarginLayoutParams");

                mLeftMarginField = mMarginLayoutParamClass.getField("leftMargin");
                mTopMarginField = mMarginLayoutParamClass.getField("topMargin");
                mRightMarginField = mMarginLayoutParamClass.getField("rightMargin");
                mBottomMarginField = mMarginLayoutParamClass.getField("bottomMargin");
            }

            if (mMarginLayoutParamClass.isAssignableFrom(params.getClass())) {

                leftMargin = (Integer)mLeftMarginField.get(params);
                topMargin = (Integer)mTopMarginField.get(params);
                rightMargin = (Integer)mRightMarginField.get(params);
                bottomMargin = (Integer)mBottomMarginField.get(params);
            }

        } catch (Exception e) {
            // just use 'unknown' value.
            leftMargin = Integer.MIN_VALUE;
            topMargin = Integer.MIN_VALUE;
            rightMargin = Integer.MIN_VALUE;
            bottomMargin = Integer.MIN_VALUE;
        }

        info.setExtendedInfo(baseLine, leftMargin, topMargin, rightMargin, bottomMargin);
    }

    /**
     * Utility method returning the baseline value for a given view object. This basically returns
     * View.getBaseline().
     *
     * @param viewObject the object for which to return the index.
     *
     * @return the baseline value or -1 if not applicable to the view object or if this layout
     *     library does not implement this method.
     */
    private int getViewBaselineReflection(Object viewObject) {
        // default implementation using reflection.
        try {
            if (sViewGetParentMethod == null) {
                Class<?> viewClass = Class.forName("android.view.View");
                sViewGetParentMethod = viewClass.getMethod("getParent");
            }

            Object result = sViewGetParentMethod.invoke(viewObject);
            if (result instanceof Integer) {
                return ((Integer)result).intValue();
            }

        } catch (Exception e) {
            // Catch all for the reflection calls.
        }

        return Integer.MIN_VALUE;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index c044353..ad2dd38 100644

//Synthetic comment -- @@ -143,8 +143,11 @@
*
* @return the baseline value or -1 if not applicable to the view object or if this layout
*     library does not implement this method.
     *
     * @deprecated use the extended ViewInfo.
*/
    @Deprecated
    public Result getViewBaseline(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index 6620571..a7ab7ae 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
* {@link RenderSession#setProperty(Object, String, String)}<br>
* The method that receives an animation listener can only use it if the
* ANIMATED_VIEW_MANIPULATION, or FULL_ANIMATED_VIEW_MANIPULATION is also supported.
     */
VIEW_MANIPULATION,
/** Ability to play animations with<br>
* {@link RenderSession#animate(Object, String, boolean, IAnimationListener)}
//Synthetic comment -- @@ -60,5 +60,6 @@
* see {@link RenderSession#moveChild(Object, Object, int, java.util.Map, IAnimationListener)}
*/
FULL_ANIMATED_VIEW_MANIPULATION,
    ADAPTER_BINDING,
    EXTENDED_VIEWINFO;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Result.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Result.java
//Synthetic comment -- index 6152a28..a739e79 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
ERROR_RENDER,
ERROR_ANIM_NOT_FOUND,
ERROR_NOT_A_DRAWABLE,
        ERROR_REFLECTION,
ERROR_UNKNOWN;

private Result mResult;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index f4f6b5c..8f45600 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;
private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;
    private boolean mExtendedViewInfoMode = false;

/**
*
//Synthetic comment -- @@ -140,4 +141,12 @@

return Collections.unmodifiableMap(mAdapterBindingMap);
}

    public void setExtendedViewInfoMode(boolean mode) {
        mExtendedViewInfoMode = mode;
    }

    public boolean getExtendedViewInfoMode() {
        return mExtendedViewInfoMode;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ViewInfo.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ViewInfo.java
//Synthetic comment -- index 2c0c829..b67734c 100644

//Synthetic comment -- @@ -34,6 +34,14 @@
private final Object mViewObject;
private final Object mLayoutParamsObject;

    // optional info
    private int mBaseLine = -1;
    private int mLeftMargin = -1;
    private int mTopMargin = -1;
    private int mRightMargin = -1;
    private int mBottomMargin = -1;


public ViewInfo(String name, Object cookie, int left, int top, int right, int bottom) {
this(name, cookie, left, top, right, bottom, null /*viewObject*/,
null /*layoutParamsObject*/);
//Synthetic comment -- @@ -62,6 +70,15 @@
}
}

    public void setExtendedInfo(int baseLine, int leftMargin, int topMargin,
            int rightMargin, int bottomMargin) {
        mBaseLine = baseLine;
        mLeftMargin = leftMargin;
        mTopMargin = topMargin;
        mRightMargin = rightMargin;
        mBottomMargin = bottomMargin;
    }

/**
* Returns the list of children views. This is never null, but can be empty.
*/
//Synthetic comment -- @@ -130,4 +147,24 @@
public Object getLayoutParamsObject() {
return mLayoutParamsObject;
}

    public int getBaseLine() {
        return mBaseLine;
    }

    public int getLeftMargin() {
        return mLeftMargin;
    }

    public int getTopMargin() {
        return mTopMargin;
    }

    public int getRightMargin() {
        return mRightMargin;
    }

    public int getBottomMargin() {
        return mBottomMargin;
    }
}







