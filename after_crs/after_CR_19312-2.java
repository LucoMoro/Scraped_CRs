/*LayoutLib API: add capability query.

Also add a map of new layout params attribute when moving
views from one parent to another.

Change-Id:If12d861d29f1bb0ec59c15d85439630ebdad4b82*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/Capabilities.java b/layoutlib_api/src/com/android/layoutlib/api/Capabilities.java
new file mode 100644
//Synthetic comment -- index 0000000..0edc764

//Synthetic comment -- @@ -0,0 +1,40 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.layoutlib.api;

import com.android.layoutlib.api.LayoutScene.IAnimationListener;

/**
 * Enum describing the layout bridge capabilities.
 *
 */
public enum Capabilities {
    /** Ability to call {@link LayoutScene#render()} and {@link LayoutScene#render(long)}. */
    RENDER,
    /** Ability to call<br>
     * {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#moveChild(Object, Object, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#setProperty(Object, String, String)}
     * */
    VIEW_MANIPULATION,
    /** Ability to call<br>
     * {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     * <p>If the bridge also supports {@link #VIEW_MANIPULATION} then those methods can use
     * an {@link IAnimationListener}, otherwise they won't. */
    ANIMATE;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java
//Synthetic comment -- index c70524b..62a1d1d 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.layoutlib.api;

import java.util.EnumSet;
import java.util.Map;

/**
//Synthetic comment -- @@ -35,6 +36,15 @@
public abstract int getApiLevel();

/**
     * Returns an {@link EnumSet} of the supported {@link Capabilities}.
     * @return an {@link EnumSet} with the supported capabilities.
     *
     */
    public EnumSet<Capabilities> getCapabilities() {
        return EnumSet.noneOf(Capabilities.class);
    }

    /**
* Initializes the Bridge object.
*
* @param fontOsLocation the location of the fonts.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 871661e..96741c3 100644

//Synthetic comment -- @@ -164,15 +164,16 @@
* If an animation listener is passed then the rendering is done asynchronously and the
* result is sent to the listener.
* If the listener is null, then the rendering is done synchronously.
     * <p/>
* The child stays in the view hierarchy after the rendering is done. To remove it call
* {@link #removeChild(Object, int)}.
     * <p/>
* The returned {@link SceneResult} object will contain the android.view.View object for
* the newly inflated child. It is accessible through {@link SceneResult#getData()}.
*
* @param parentView the parent View object to receive the new child.
     * @param childXml an {@link IXmlPullParser} containing the content of the new child, including
     *             ViewGroup.LayoutParams attributes.
* @param index the index at which position to add the new child into the parent. -1 means at
*             the end.
* @param listener an optional {@link IAnimationListener}.
//Synthetic comment -- @@ -196,21 +197,27 @@
* If an animation listener is passed then the rendering is done asynchronously and the
* result is sent to the listener.
* If the listener is null, then the rendering is done synchronously.
     * <p/>
* The child stays in the view hierarchy after the rendering is done. To remove it call
* {@link #removeChild(Object, int)}.
     * <p/>
     * The returned {@link SceneResult} object will contain the android.view.ViewGroup.LayoutParams
     * object created from the <var>layoutParams</var> map if it was non <code>null</code>.
*
* @param parentView the parent View object to receive the child. Can be the current parent
*             already.
* @param childView the view to move.
* @param index the index at which position to add the new child into the parent. -1 means at
*             the end.
     * @param layoutParams an optional map of new ViewGroup.LayoutParams attribute. If non null,
     *             then the current layout params of the view will be removed and a new one will
     *             be inflated and set with the content of the map.
* @param listener an optional {@link IAnimationListener}.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
public SceneResult moveChild(Object parentView, Object childView, int index,
            Map<String, String> layoutParams, IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}








