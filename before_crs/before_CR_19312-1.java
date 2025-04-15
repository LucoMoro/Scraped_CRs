/*LayoutLib API: add capability query.

Also add a map of new layout params attribute when moving
views from one parent to another.

Change-Id:If12d861d29f1bb0ec59c15d85439630ebdad4b82*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/Capabilities.java b/layoutlib_api/src/com/android/layoutlib/api/Capabilities.java
new file mode 100644
//Synthetic comment -- index 0000000..0edc764

//Synthetic comment -- @@ -0,0 +1,40 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java
//Synthetic comment -- index c70524b..62a1d1d 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.layoutlib.api;

import java.util.Map;

/**
//Synthetic comment -- @@ -35,6 +36,15 @@
public abstract int getApiLevel();

/**
* Initializes the Bridge object.
*
* @param fontOsLocation the location of the fonts.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 871661e..fe836d3 100644

//Synthetic comment -- @@ -172,7 +172,8 @@
* the newly inflated child. It is accessible through {@link SceneResult#getData()}.
*
* @param parentView the parent View object to receive the new child.
     * @param childXml an {@link IXmlPullParser} containing the content of the new child.
* @param index the index at which position to add the new child into the parent. -1 means at
*             the end.
* @param listener an optional {@link IAnimationListener}.
//Synthetic comment -- @@ -205,12 +206,15 @@
* @param childView the view to move.
* @param index the index at which position to add the new child into the parent. -1 means at
*             the end.
* @param listener an optional {@link IAnimationListener}.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult moveChild(Object parentView, Object childView, int index,
            IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}








