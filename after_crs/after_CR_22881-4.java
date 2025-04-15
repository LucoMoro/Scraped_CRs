/*Add baseline/margins to ViewInfo.

Also make it so that older layoutlib that are using API5 return the
value through reflection (done in LayoutLibrary.)

Change-Id:I3a32666e525f0f1d37a13e670d1d1c659b8e2027*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 337ad5c..e814b17 100644

//Synthetic comment -- @@ -1480,6 +1480,7 @@
mMinSdkVersion,
mTargetSdkVersion,
logger);

if (noDecor) {
params.setForceNoDecor();
} else {








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index e1256fe..dd1a3dc 100644

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

//Synthetic comment -- @@ -91,6 +93,16 @@
/** classloader used to load the jar file */
private final ClassLoader mClassLoader;

    // Reflection data for older Layout Libraries.
    private Method mViewGetParentMethod;
    private Method mViewGetBaselineMethod;
    private Method mViewParentIndexOfChildMethod;
    private Class<?> mMarginLayoutParamClass;
    private Field mLeftMarginField;
    private Field mTopMarginField;
    private Field mRightMarginField;
    private Field mBottomMarginField;

/**
* Returns the {@link LoadStatus} of the loading of the layoutlib jar file.
*/
//Synthetic comment -- @@ -282,7 +294,18 @@
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

            return session;
} else if (mLegacyBridge != null) {
return createLegacySession(params);
}
//Synthetic comment -- @@ -333,10 +356,13 @@
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
//Synthetic comment -- @@ -348,30 +374,15 @@
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
//Synthetic comment -- @@ -440,7 +451,6 @@
};


// convert the map of ResourceValue into IResourceValue. Super ugly but works.

Map<String, Map<String, IResourceValue>> projectMap = convertMap(
//Synthetic comment -- @@ -584,4 +594,129 @@
// do nothing.
}
}

    private Result getViewParentWithReflection(Object viewObject) {
        // default implementation using reflection.
        try {
            if (mViewGetParentMethod == null) {
                Class<?> viewClass = Class.forName("android.view.View");
                mViewGetParentMethod = viewClass.getMethod("getParent");
            }

            return Status.SUCCESS.createResult(mViewGetParentMethod.invoke(viewObject));
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

            if (mViewGetParentMethod == null) {
                mViewGetParentMethod = viewClass.getMethod("getParent");
            }

            Object parentObject = mViewGetParentMethod.invoke(viewObject);

            if (mViewParentIndexOfChildMethod == null) {
                Class<?> viewParentClass = Class.forName("android.view.ViewParent");
                mViewParentIndexOfChildMethod = viewParentClass.getMethod("indexOfChild",
                        viewClass);
            }

            return Status.SUCCESS.createResult(
                    mViewParentIndexOfChildMethod.invoke(parentObject, viewObject));
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
            if (mViewGetBaselineMethod == null) {
                Class<?> viewClass = Class.forName("android.view.View");
                mViewGetBaselineMethod = viewClass.getMethod("getBaseline");
            }

            Object result = mViewGetBaselineMethod.invoke(viewObject);
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
//Synthetic comment -- index f4f6b5c..1af450e 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;
private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;
    private boolean mExtendedViewInfoMode = false;

/**
*
//Synthetic comment -- @@ -107,6 +108,7 @@
mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>(
params.mAdapterBindingMap);
}
        mExtendedViewInfoMode = params.mExtendedViewInfoMode;
}

public ILayoutPullParser getLayoutDescription() {
//Synthetic comment -- @@ -140,4 +142,12 @@

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
//Synthetic comment -- index 2c0c829..2671fc0 100644

//Synthetic comment -- @@ -34,6 +34,13 @@
private final Object mViewObject;
private final Object mLayoutParamsObject;

    // optional info
    private int mBaseLine = Integer.MIN_VALUE;
    private int mLeftMargin = Integer.MIN_VALUE;
    private int mTopMargin = Integer.MIN_VALUE;
    private int mRightMargin = Integer.MIN_VALUE;
    private int mBottomMargin = Integer.MIN_VALUE;

public ViewInfo(String name, Object cookie, int left, int top, int right, int bottom) {
this(name, cookie, left, top, right, bottom, null /*viewObject*/,
null /*layoutParamsObject*/);
//Synthetic comment -- @@ -62,6 +69,15 @@
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
//Synthetic comment -- @@ -130,4 +146,39 @@
public Object getLayoutParamsObject() {
return mLayoutParamsObject;
}

    /**
     * Returns the baseline value. If the value is unknown, returns {@link Integer#MIN_VALUE}.
     */
    public int getBaseLine() {
        return mBaseLine;
    }

    /**
     * Returns the left margin value. If the value is unknown, returns {@link Integer#MIN_VALUE}.
     */
    public int getLeftMargin() {
        return mLeftMargin;
    }

    /**
     * Returns the top margin value. If the value is unknown, returns {@link Integer#MIN_VALUE}.
     */
    public int getTopMargin() {
        return mTopMargin;
    }

    /**
     * Returns the right margin value. If the value is unknown, returns {@link Integer#MIN_VALUE}.
     */
    public int getRightMargin() {
        return mRightMargin;
    }

    /**
     * Returns the bottom margin value. If the value is unknown, returns {@link Integer#MIN_VALUE}.
     */
    public int getBottomMargin() {
        return mBottomMargin;
    }
}







