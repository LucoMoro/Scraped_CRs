/*New layoutlib API.

This is the new API to move to a stateful layoutlib, allowing
for faster actions on an inflated layout.

Change-Id:Ice6324c056efc6e82d5760b5f4e3d40b58938368*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java
//Synthetic comment -- index 4dbcfdc..43ee75f 100644

//Synthetic comment -- @@ -24,6 +24,11 @@
* <p/>
* <p/>{@link #getApiLevel()} gives the ability to know which methods are available.
* <p/>
 * Changes in API level 5:
 * <ul>
 * <li>new render method: {@link #startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
 * <li>deprecated {@link #computeLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
 * </ul>
* Changes in API level 4:
* <ul>
* <li>new render method: {@link #computeLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
//Synthetic comment -- @@ -43,7 +48,7 @@
*/
public interface ILayoutBridge {

    final int API_CURRENT = 5;

/**
* Returns the API level of the layout library.
//Synthetic comment -- @@ -89,8 +94,47 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
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
*/
    @Deprecated
ILayoutResult computeLayout(IXmlPullParser layoutDescription,
Object projectKey,
int screenWidth, int screenHeight, boolean renderFullSize,
//Synthetic comment -- @@ -124,6 +168,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link #startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
* @since 3
*/
@Deprecated
//Synthetic comment -- @@ -156,7 +201,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link #startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
* @since 2
*/
@Deprecated
//Synthetic comment -- @@ -188,7 +233,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link #startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
* @since 1
*/
@Deprecated








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java
//Synthetic comment -- index 2d8a210..ada71a7 100644

//Synthetic comment -- @@ -21,6 +21,9 @@
/**
* The result of a layout computation through
* {@link ILayoutLibBridge#computeLayout(IXmlPullParser, int, int, String, java.util.Map, java.util.Map, java.util.Map, IFontLoader, ILayoutLibLog, ICustomViewLoader)}
 *
 * @since 1
 * @deprecated use {@link ILayoutScene} as returned by {@link ILayoutBridge#startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, java.util.Map, java.util.Map, IProjectCallback, ILayoutLog)}
*/
public interface ILayoutResult {
/**
//Synthetic comment -- @@ -59,6 +62,7 @@

/**
* Layout information for a specific view.
     * @deprecated
*/
public interface ILayoutViewInfo {









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java
new file mode 100644
//Synthetic comment -- index 0000000..e1c6eb2

//Synthetic comment -- @@ -0,0 +1,103 @@
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
 * A object allowing interaction with an Android layout.
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
    ILayoutResult getStatus();

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
new file mode 100644
//Synthetic comment -- index 0000000..8976a77

//Synthetic comment -- @@ -0,0 +1,65 @@
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

/**
 * Layout information for a specific view object
 */
public interface ILayoutViewInfo {

    /**
     * Returns the list of children views.
     */
    ILayoutViewInfo[] getChildren();

    /**
     * Returns the key associated with the node.
     * @see IXmlPullParser#getViewKey()
     */
    Object getViewKey();

    /**
     * Returns the actual layout object. This can be used
     * to query
     */
    Object getLayoutObject();

    /**
     * Returns the name of the view.
     */
    String getName();

    /**
     * Returns the left of the view bounds.
     */
    int getLeft();

    /**
     * Returns the top of the view bounds.
     */
    int getTop();

    /**
     * Returns the right of the view bounds.
     */
    int getRight();

    /**
     * Returns the bottom of the view bounds.
     */
    int getBottom();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java b/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java
//Synthetic comment -- index cd43c56..8cf0325 100644

//Synthetic comment -- @@ -16,21 +16,30 @@

package com.android.layoutlib.api;

import org.xmlpull.v1.XmlPullParser;

import java.util.Map;

/**
 * Extended version of {@link XmlPullParser} to use with
 * {@link ILayoutLibBridge#startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
*/
public interface IXmlPullParser extends XmlPullParser {

/**
* Returns a key for the current XML node.
* <p/>This key will be passed back in the {@link ILayoutViewInfo} objects, allowing association
* of a particular XML node with its result from the layout computation.
*/
Object getViewKey();

    /**
     * Returns a custom parser for the layout of the given name.
     * @param layoutName the name of the layout.
     * @return returns a custom parser or null if no custom parsers are needed.
     *
     * @since 5
     */
    IXmlPullParser getParser(String layoutName);
}








