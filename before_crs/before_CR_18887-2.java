/*Improvements to the new Layoutlib API.

Move to classes instead of interfaces so that the layoutlib
API code can provide default implementation.

This will reduce the testing on the ADT side about whether a method
is present for the current API level or not.

Also moved all the parameters of startLayout (actually renamed createScene)
into a SceneParams class.

Change-Id:I58389cd1bed9e79f6825b7c4e5a79206496ca439*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java
//Synthetic comment -- index 103525c..70d099d 100644

//Synthetic comment -- @@ -26,8 +26,7 @@
* <p/>
* Changes in API level 5:
* <ul>
 * <li>new render method: {@link #startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
 * <li>deprecated {@link #computeLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
* </ul>
* Changes in API level 4:
* <ul>
//Synthetic comment -- @@ -45,10 +44,12 @@
* <li>new render method: {@link #computeLayout(IXmlPullParser, Object, int, int, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
* <li>deprecated {@link #computeLayout(IXmlPullParser, Object, int, int, String, Map, Map, IProjectCallback, ILayoutLog)}</li>
* </ul>
*/
public interface ILayoutBridge {

    final int API_CURRENT = 5;

/**
* Returns the API level of the layout library.
//Synthetic comment -- @@ -100,43 +101,6 @@
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
     * @return a new {@link ILayoutScene} object that contains the result of the layout.
     * @since 5
     */
    ILayoutScene startLayout(IXmlPullParser layoutDescription,
            Object projectKey,
            int screenWidth, int screenHeight, boolean renderFullSize,
            int density, float xdpi, float ydpi,
            String themeName, boolean isProjectTheme,
            Map<String, Map<String, IResourceValue>> projectResources,
            Map<String, Map<String, IResourceValue>> frameworkResources,
            IProjectCallback projectCallback, ILayoutLog logger);

    /**
     * Computes and renders a layout
     * @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
     * layout file.
     * @param projectKey An Object identifying the project. This is used for the cache mechanism.
     * @param screenWidth the screen width
     * @param screenHeight the screen height
     * @param renderFullSize if true, the rendering will render the full size needed by the
     * layout. This size is never smaller than <var>screenWidth</var> x <var>screenHeight</var>.
     * @param density the density factor for the screen.
     * @param xdpi the screen actual dpi in X
     * @param ydpi the screen actual dpi in Y
     * @param themeName The name of the theme to use.
     * @param isProjectTheme true if the theme is a project theme, false if it is a framework theme.
     * @param projectResources the resources of the project. The map contains (String, map) pairs
     * where the string is the type of the resource reference used in the layout file, and the
     * map contains (String, {@link IResourceValue}) pairs where the key is the resource name,
     * and the value is the resource value.
     * @param frameworkResources the framework resources. The map contains (String, map) pairs
     * where the string is the type of the resource reference used in the layout file, and the map
     * contains (String, {@link IResourceValue}) pairs where the key is the resource name, and the
     * value is the resource value.
     * @param projectCallback The {@link IProjectCallback} object to get information from
     * the project.
     * @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
* @deprecated use {@link #startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
* @since 4








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java
deleted file mode 100644
//Synthetic comment -- index 125266e..0000000

//Synthetic comment -- @@ -1,103 +0,0 @@
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

import java.awt.image.BufferedImage;

/**
 * An object allowing interaction with an Android layout.
 *
 * This is returned by {@link ILayoutBridge#startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, java.util.Map, java.util.Map, IProjectCallback, ILayoutLog)}
 * and can then be used for subsequent actions on the layout.
 *
 * @since 5
 *
 */
public interface ILayoutScene {

    enum LayoutStatus { SUCCESS, ERROR };

    public interface ILayoutResult {
        LayoutStatus getStatus();
        String getErrorMessage();
        Throwable getException();
    }

    public interface IAnimationListener {
        /**
         * Called when a new animation frame is available for display.
         */
        void onNewFrame(BufferedImage image);

        /**
         * Called when the animation is done playing.
         */
        void done();
    }

    /**
     * Returns the result for the original call to {@link ILayoutBridge#startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, java.util.Map, java.util.Map, IProjectCallback, ILayoutLog)}
     */
    ILayoutResult getResult();

    /**
     * Returns the {@link ILayoutViewInfo} object for the top level view.
     */
    ILayoutViewInfo getRootView();

    /**
     * Returns the rendering of the full layout.
     */
    BufferedImage getImage();

    /**
     * Re-renders the layout as-is.
     * In case of success, this should be followed by calls to {@link #getRootView()} and
     * {@link #getImage()}
     */
    ILayoutResult render();

    /**
     * Sets the value of a given property on a given object.
     * In case of success, this should be followed by a call to {@link #render()}
     * @param object
     * @param propertyName
     * @param propertyValue
     * @return
     */
    ILayoutResult setProperty(int object, String propertyName, String propertyValue);

    /**
     * TBD
     */
    ILayoutResult insertChild();

    /**
     * TBD
     */
    ILayoutResult removeChild();

    /**
     * TBD
     */
    ILayoutResult animate(int object, int animation, IAnimationListener listener);

    /**
     * Discards the layout. No more actions can be called on this object.
     */
    void dispose();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutViewInfo.java
deleted file mode 100644
//Synthetic comment -- index 4c864c0..0000000

//Synthetic comment -- @@ -1,75 +0,0 @@
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

import java.util.List;
import java.util.Map;

/**
 * Layout information for a specific view object
 */
public interface ILayoutViewInfo {

    /**
     * Returns the list of children views.
     */
    List<ILayoutViewInfo> getChildren();

    /**
     * Returns the key associated with the node.
     * @see IXmlPullParser#getViewKey()
     */
    Object getViewKey();

    /**
     * Returns the class name of the view object.
     */
    String getClassName();

    /**
     * Returns the left of the view bounds, relative to the view parent bounds.
     */
    int getLeft();

    /**
     * Returns the top of the view bounds, relative to the view parent bounds.
     */
    int getTop();

    /**
     * Returns the right of the view bounds, relative to the view parent bounds.
     */
    int getRight();

    /**
     * Returns the bottom of the view bounds, relative to the view parent bounds.
     */
    int getBottom();

    /**
     * Returns a map of default values for some properties. The map key is the property name,
     * as found in the XML.
     */
    Map<String, String> getDefaultPropertyValues();

    /**
     * Returns the actual android.view.View (or child class) object. This can be used
     * to query the object properties that are not in the XML and not in the map returned
     * by {@link #getDefaultPropertyValues()}.
     */
    Object getViewObject();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java b/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java
//Synthetic comment -- index 8cf0325..6d0f2b6 100644

//Synthetic comment -- @@ -28,7 +28,7 @@

/**
* Returns a key for the current XML node.
     * <p/>This key will be passed back in the {@link ILayoutViewInfo} objects, allowing association
* of a particular XML node with its result from the layout computation.
*/
Object getViewKey();








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java
new file mode 100644
//Synthetic comment -- index 0000000..6387146

//Synthetic comment -- @@ -0,0 +1,81 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
new file mode 100644
//Synthetic comment -- index 0000000..8e0fe73

//Synthetic comment -- @@ -0,0 +1,158 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
new file mode 100644
//Synthetic comment -- index 0000000..86e9104

//Synthetic comment -- @@ -0,0 +1,163 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
new file mode 100644
//Synthetic comment -- index 0000000..7214b3f

//Synthetic comment -- @@ -0,0 +1,84 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..e3b760a

//Synthetic comment -- @@ -0,0 +1,115 @@







