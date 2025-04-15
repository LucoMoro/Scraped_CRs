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
 * <li>Bridge should extend {@link LayoutBridge} instead of implementing {@link ILayoutBridge}.</li>
* </ul>
* Changes in API level 4:
* <ul>
//Synthetic comment -- @@ -45,10 +44,12 @@
* <li>new render method: {@link #computeLayout(IXmlPullParser, Object, int, int, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
* <li>deprecated {@link #computeLayout(IXmlPullParser, Object, int, int, String, Map, Map, IProjectCallback, ILayoutLog)}</li>
* </ul>
 * @Deprecated Extend {@link LayoutBridge} instead.
*/
@Deprecated
public interface ILayoutBridge {

    final int API_CURRENT = 4;

/**
* Returns the API level of the layout library.
//Synthetic comment -- @@ -100,43 +101,6 @@
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








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutViewInfo.java
deleted file mode 100644
//Synthetic comment -- index 4c864c0..0000000

//Synthetic comment -- @@ -1,75 +0,0 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java b/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java
//Synthetic comment -- index 8cf0325..6d0f2b6 100644

//Synthetic comment -- @@ -28,7 +28,7 @@

/**
* Returns a key for the current XML node.
     * <p/>This key will be passed back in the {@link ViewInfo} objects, allowing association
* of a particular XML node with its result from the layout computation.
*/
Object getViewKey();








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java
new file mode 100644
//Synthetic comment -- index 0000000..c70524b

//Synthetic comment -- @@ -0,0 +1,78 @@
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

import java.util.Map;

/**
 * Entry point of the Layout Lib. Extensions of this class provide a method to compute
 * and render a layout.
 */
public abstract class LayoutBridge {

    public final static int API_CURRENT = 5;

    /**
     * Returns the API level of the layout library.
     * <p/>
     * While no methods will ever be removed, some may become deprecated, and some new ones
     * will appear.
     */
    public abstract int getApiLevel();

    /**
     * Initializes the Bridge object.
     *
     * @param fontOsLocation the location of the fonts.
     * @param enumValueMap map attrName => { map enumFlagName => Integer value }.
     * @return true if success.
     */
    public boolean init(String fontOsLocation, Map<String, Map<String, Integer>> enumValueMap) {
        return false;
    }

    /**
     * Prepares the layoutlib to unloaded.
     */
    public boolean dispose() {
        return false;
    }

    /**
     * Starts a layout session by inflating and rendering it. The method returns a
     * {@link LayoutScene} on which further actions can be taken.
     *
     * @return a new {@link ILayoutScene} object that contains the result of the scene creation and
     * first rendering.
     */
    public LayoutScene createScene(SceneParams params) {
        return null;
    }

    /**
     * Clears the resource cache for a specific project.
     * <p/>This cache contains bitmaps and nine patches that are loaded from the disk and reused
     * until this method is called.
     * <p/>The cache is not configuration dependent and should only be cleared when a
     * resource changes (at this time only bitmaps and 9 patches go into the cache).
     *
     * @param projectKey the key for the project.
     */
    public void clearCaches(Object projectKey) {

    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
new file mode 100644
//Synthetic comment -- index 0000000..8e0fe73

//Synthetic comment -- @@ -0,0 +1,158 @@
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

import com.android.layoutlib.api.SceneResult.LayoutStatus;

import java.awt.image.BufferedImage;

/**
 * An object allowing interaction with an Android layout.
 *
 * This is returned by {@link LayoutBridge#createScene(SceneParams)}.
 * and can then be used for subsequent actions on the layout.
 *
 * @since 5
 *
 */
public class LayoutScene {

    public interface IAnimationListener {
        /**
         * Called when a new animation frame is available for display.
         */
        void onNewFrame(BufferedImage image);

        /**
         * Called when the animation is done playing.
         */
        void done();

        /**
         * Returns true if the animation is canceled.
         */
        void isCanceled();
    }

    /**
     * Returns the last operation result.
     */
    public SceneResult getResult() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
    }

    /**
     * Returns the {@link ViewInfo} object for the top level view.
     * <p>
     * This is reset to a new instance every time {@link #render()} is called and can be
     * <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link LayoutStatus#ERROR} or {@link LayoutStatus#NOT_IMPLEMENTED}.
     * <p/>
     * This can be safely modified by the caller.
     */
    public ViewInfo getRootView() {
        return null;
    }

    /**
     * Returns the rendering of the full layout.
     * <p>
     * This is reset to a new instance every time {@link #render()} is called and can be
     * <code>null</code> if the call failed (and the method returned a {@link SceneResult} with
     * {@link LayoutStatus#ERROR} or {@link LayoutStatus#NOT_IMPLEMENTED}.
     * <p/>
     * This can be safely modified by the caller.
     */
    public BufferedImage getImage() {
        return null;
    }

    /**
     * Re-renders the layout as-is.
     * In case of success, this should be followed by calls to {@link #getRootView()} and
     * {@link #getImage()} to access the result of the rendering.
     *
     * @return a {@link SceneResult} indicating the status of the action.
     */
    public SceneResult render() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
    }

    /**
     * Sets the value of a given property on a given object.
     * <p/>
     * This does nothing more than change the property. To render the scene in its new state, a
     * call to {@link #render()} is required.
     * <p/>
     * Any amount of actions can be taken on the scene before {@link #render()} is called.
     *
     * @param object
     * @param propertyName
     * @param propertyValue
     *
     * @return a {@link SceneResult} indicating the status of the action.
     */
    public SceneResult setProperty(int object, String propertyName, String propertyValue) {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
    }

    /**
     * Inserts a new child in a ViewGroup object.
     * <p/>
     * This does nothing more than change the layouy. To render the scene in its new state, a
     * call to {@link #render()} is required.
     * <p/>
     * Any amount of actions can be taken on the scene before {@link #render()} is called.
     *
     * @return a {@link SceneResult} indicating the status of the action.
     */
    public SceneResult insertChild() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
    }

    /**
     * Removes a child from a ViewGroup object.
     * <p/>
     * This does nothing more than change the layouy. To render the scene in its new state, a
     * call to {@link #render()} is required.
     * <p/>
     * Any amount of actions can be taken on the scene before {@link #render()} is called.
     *
     * @return a {@link SceneResult} indicating the status of the action.
     */
    public SceneResult removeChild() {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
    }

    /**
     * Starts playing an given animation on a given object.
     * <p/>
     * The animation playback is asynchronous and the rendered frame is sent vi the
     * <var>listener</var>.
     *
     * @return a {@link SceneResult} indicating the status of the action.
     */
    public SceneResult animate(int object, int animation, IAnimationListener listener) {
        return new SceneResult(LayoutStatus.NOT_IMPLEMENTED);
    }

    /**
     * Discards the layout. No more actions can be called on this object.
     */
    public void dispose() {
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
new file mode 100644
//Synthetic comment -- index 0000000..86e9104

//Synthetic comment -- @@ -0,0 +1,163 @@
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

import java.util.Map;

public class SceneParams {

    private final IXmlPullParser mLayoutDescription;
    private final Object mProjectKey;
    private final int mScreenWidth;
    private final int mScreenHeight;
    private final boolean mRenderFullSize;
    private final int mDensity;
    private final float mXdpi;
    private final float mYdpi;
    private final String mThemeName;
    private final boolean mIsProjectTheme;
    private final Map<String, Map<String, IResourceValue>> mProjectResources;
    private final Map<String, Map<String, IResourceValue>> mFrameworkResources;
    private final IProjectCallback mProjectCallback;
    private final ILayoutLog mLogger;

    /**
     *
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
     */
    public SceneParams(IXmlPullParser layoutDescription,
            Object projectKey,
            int screenWidth, int screenHeight, boolean renderFullSize,
            int density, float xdpi, float ydpi,
            String themeName, boolean isProjectTheme,
            Map<String, Map<String, IResourceValue>> projectResources,
            Map<String, Map<String, IResourceValue>> frameworkResources,
            IProjectCallback projectCallback, ILayoutLog logger) {
        mLayoutDescription = layoutDescription;
        mProjectKey = projectKey;
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mRenderFullSize = renderFullSize;
        mDensity = density;
        mXdpi = xdpi;
        mYdpi = ydpi;
        mThemeName = themeName;
        mIsProjectTheme = isProjectTheme;
        mProjectResources = projectResources;
        mFrameworkResources = frameworkResources;
        mProjectCallback = projectCallback;
        mLogger = logger;
    }

    /**
     * Copy constructor.
     */
    public SceneParams(SceneParams params) {
        mLayoutDescription = params.mLayoutDescription;
        mProjectKey = params.mProjectKey;
        mScreenWidth = params.mScreenWidth;
        mScreenHeight = params.mScreenHeight;
        mRenderFullSize = params.mRenderFullSize;
        mDensity = params.mDensity;
        mXdpi = params.mXdpi;
        mYdpi = params.mYdpi;
        mThemeName = params.mThemeName;
        mIsProjectTheme = params.mIsProjectTheme;
        mProjectResources = params.mProjectResources;
        mFrameworkResources = params.mFrameworkResources;
        mProjectCallback = params.mProjectCallback;
        mLogger = params.mLogger;
    }

    public IXmlPullParser getLayoutDescription() {
        return mLayoutDescription;
    }

    public Object getProjectKey() {
        return mProjectKey;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public boolean getRenderFullSize() {
        return mRenderFullSize;
    }

    public int getDensity() {
        return mDensity;
    }

    public float getXdpi() {
        return mXdpi;
    }

    public float getYdpi() {
        return mYdpi;
    }

    public String getThemeName() {
        return mThemeName;
    }

    public boolean getIsProjectTheme() {
        return mIsProjectTheme;
    }

    public Map<String, Map<String, IResourceValue>> getProjectResources() {
        return mProjectResources;
    }

    public Map<String, Map<String, IResourceValue>> getFrameworkResources() {
        return mFrameworkResources;
    }

    public IProjectCallback getProjectCallback() {
        return mProjectCallback;
    }

    public ILayoutLog getLogger() {
        return mLogger;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
new file mode 100644
//Synthetic comment -- index 0000000..7214b3f

//Synthetic comment -- @@ -0,0 +1,84 @@
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
 * Scene result class.
 */
public class SceneResult {

    private final LayoutStatus mStatus;
    private final String mErrorMessage;
    private final Throwable mThrowable;

    public enum LayoutStatus { SUCCESS, ERROR, NOT_IMPLEMENTED };

    /**
     * Creates a successful {@link SceneResult} object.
     */
    public SceneResult() {
        mStatus = LayoutStatus.SUCCESS;
        mErrorMessage = null;
        mThrowable = null;
    }

    /**
     * Creates an error {@link SceneResult} object with the given message.
     */
    public SceneResult(String errorMessage) {
        mStatus = LayoutStatus.ERROR;
        mErrorMessage = errorMessage;
        mThrowable = null;
    }

    /**
     * Creates an error {@link SceneResult} object with the given message and {@link Throwable}
     */
    public SceneResult(String errorMessage, Throwable t) {
        mStatus = LayoutStatus.ERROR;
        mErrorMessage = errorMessage;
        mThrowable = t;
    }

    /*package*/ SceneResult(LayoutStatus status) {
        mStatus = LayoutStatus.NOT_IMPLEMENTED;
        mErrorMessage = null;
        mThrowable = null;
    }

    /**
     * Returns the status. This is never null.
     */
    public LayoutStatus getStatus() {
        return mStatus;
    }

    /**
     * Returns the error message. This can be null if the status is {@link LayoutStatus#SUCCESS}.
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Returns the exception. This can be null.
     */
    public Throwable getException() {
        return mThrowable;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..3e7d907

//Synthetic comment -- @@ -0,0 +1,122 @@
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

import java.util.ArrayList;
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
    protected List<ViewInfo> mChildren;;

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
        mChildren = new ArrayList<ViewInfo>();
        mChildren.addAll(children);
        mChildren = Collections.unmodifiableList(mChildren);
    }

    /**
     * Returns the list of children views. This is never null, but can be empty.
     */
    public List<ViewInfo> getChildren() {
        return mChildren;
    }

    /**
     * Returns the key associated with the node. Can be null.
     *
     * @see IXmlPullParser#getViewKey()
     */
    public Object getViewKey() {
        return null;
    }

    /**
     * Returns the class name of the view object. Can be null.
     */
    public String getClassName() {
        return null;
    }

    /**
     * Returns the left of the view bounds, relative to the view parent bounds.
     */
    public int getLeft() {
        return 0;
    }

    /**
     * Returns the top of the view bounds, relative to the view parent bounds.
     */
    public int getTop() {
        return 0;
    }

    /**
     * Returns the right of the view bounds, relative to the view parent bounds.
     */
    public int getRight() {
        return 0;
    }

    /**
     * Returns the bottom of the view bounds, relative to the view parent bounds.
     */
    public int getBottom() {
        return 0;
    }

    /**
     * Returns a map of default values for some properties. The map key is the property name,
     * as found in the XML.
     */
    public Map<String, String> getDefaultPropertyValues() {
        return null;
    }

    /**
     * Returns the actual android.view.View (or child class) object. This can be used
     * to query the object properties that are not in the XML and not in the map returned
     * by {@link #getDefaultPropertyValues()}.
     */
    public Object getViewObject() {
        return null;
    }
}







