/*Merge 580ecb7a from master. do not merge.

Add baseline/margins to ViewInfo.

Also make it so that older layoutlib that are using API5 return the
value through reflection (done in LayoutLibrary.)

Change-Id:Ied102625430b53f9b5e62aa738c8c598054cc266*/
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

import static com.android.ide.common.rendering.api.Result.Status.NOT_IMPLEMENTED;

import com.android.ide.common.log.ILogger;
import com.android.ide.common.rendering.api.Bridge;
//Synthetic comment -- @@ -48,11 +48,13 @@
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

//Synthetic comment -- @@ -91,6 +93,16 @@
/** classloader used to load the jar file */
private final ClassLoader mClassLoader;

/**
* Returns the {@link LoadStatus} of the loading of the layoutlib jar file.
*/
//Synthetic comment -- @@ -282,7 +294,18 @@
*/
public RenderSession createSession(SessionParams params) {
if (mBridge != null) {
            return mBridge.createSession(params);
} else if (mLegacyBridge != null) {
return createLegacySession(params);
}
//Synthetic comment -- @@ -333,10 +356,13 @@
*/
public Result getViewParent(Object viewObject) {
if (mBridge != null) {
            return mBridge.getViewParent(viewObject);
}

        return NOT_IMPLEMENTED.createResult();
}

/**
//Synthetic comment -- @@ -348,30 +374,15 @@
*/
public Result getViewIndex(Object viewObject) {
if (mBridge != null) {
            return mBridge.getViewIndex(viewObject);
}

        return NOT_IMPLEMENTED.createResult();
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
    public int getViewBaseline(Object viewObject) {
        if (mBridge != null) {
            return mBridge.getViewBaseline(viewObject);
        }

        return -1;
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
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index c044353..ad2dd38 100644

//Synthetic comment -- @@ -143,8 +143,11 @@
*
* @return the baseline value or -1 if not applicable to the view object or if this layout
*     library does not implement this method.
*/
    public int getViewBaseline(Object viewObject) {
        return -1;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index 6620571..a7ab7ae 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
* {@link RenderSession#setProperty(Object, String, String)}<br>
* The method that receives an animation listener can only use it if the
* ANIMATED_VIEW_MANIPULATION, or FULL_ANIMATED_VIEW_MANIPULATION is also supported.
     * */
VIEW_MANIPULATION,
/** Ability to play animations with<br>
* {@link RenderSession#animate(Object, String, boolean, IAnimationListener)}
//Synthetic comment -- @@ -60,5 +60,6 @@
* see {@link RenderSession#moveChild(Object, Object, int, java.util.Map, IAnimationListener)}
*/
FULL_ANIMATED_VIEW_MANIPULATION,
    ADAPTER_BINDING;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Result.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Result.java
//Synthetic comment -- index 6152a28..a739e79 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
ERROR_RENDER,
ERROR_ANIM_NOT_FOUND,
ERROR_NOT_A_DRAWABLE,
ERROR_UNKNOWN;

private Result mResult;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index f4f6b5c..1af450e 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;
private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;

/**
*
//Synthetic comment -- @@ -107,6 +108,7 @@
mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>(
params.mAdapterBindingMap);
}
}

public ILayoutPullParser getLayoutDescription() {
//Synthetic comment -- @@ -140,4 +142,12 @@

return Collections.unmodifiableMap(mAdapterBindingMap);
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ViewInfo.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ViewInfo.java
//Synthetic comment -- index 2c0c829..2671fc0 100644

//Synthetic comment -- @@ -34,6 +34,13 @@
private final Object mViewObject;
private final Object mLayoutParamsObject;

public ViewInfo(String name, Object cookie, int left, int top, int right, int bottom) {
this(name, cookie, left, top, right, bottom, null /*viewObject*/,
null /*layoutParamsObject*/);
//Synthetic comment -- @@ -62,6 +69,15 @@
}
}

/**
* Returns the list of children views. This is never null, but can be empty.
*/
//Synthetic comment -- @@ -130,4 +146,39 @@
public Object getLayoutParamsObject() {
return mLayoutParamsObject;
}
}







