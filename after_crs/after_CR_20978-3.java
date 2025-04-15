/*Add to layoutlib the ability to simply render a Drawable.

RenderParams is now a base class. SessionParams extends it
(and contains the layout and the rendering mode which are not
part of the base class).

DrawableParams is used for the new action and adds a reference
to a ResourceValue.

Change-Id:Ieacf4da91fda95df1d25a32ae0953bd9d8028113*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f09ef99..bfbbf31 100644

//Synthetic comment -- @@ -26,11 +26,11 @@
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -1660,11 +1660,11 @@
configuredProjectRes, frameworkResources,
theme, isProjectTheme);

        SessionParams params = new SessionParams(
topParser,
                renderingMode,
iProject /* projectKey */,
width, height,
density, xdpi, ydpi,
resolver,
mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 08aef31..b73bff8 100755

//Synthetic comment -- @@ -24,12 +24,13 @@

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.MenuAction.Toggle;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 1d92f3a..d6057fc 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 0fa0df9..f18ec36 100644

//Synthetic comment -- @@ -19,10 +19,10 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -207,12 +207,12 @@
configuredProject, configuredFramework,
"Theme", false /*isProjectTheme*/);

            RenderSession session = layoutLib.createSession(new SessionParams(
parser,
                    RenderingMode.NORMAL,
null /*projectKey*/,
320,
480,
Density.MEDIUM,
160, //xdpi
160, // ydpi








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 43444de..d9ffda7 100644

//Synthetic comment -- @@ -19,17 +19,18 @@
import com.android.ide.common.log.ILogger;
import com.android.ide.common.rendering.api.Bridge;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.Result.Status;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.rendering.legacy.ILegacyPullParser;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.layoutlib.api.ILayoutBridge;
//Synthetic comment -- @@ -41,6 +42,7 @@
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.resources.ResourceType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
//Synthetic comment -- @@ -58,7 +60,7 @@
* Use {@link #load(String, ILogger)} to load the jar file.
* <p/>
* Use the layout library with:
 * {@link #init(String, Map)}, {@link #supports(Capability)}, {@link #createSession(SessionParams)},
* {@link #dispose()}, {@link #clearCaches(Object)}.
*
* <p/>
//Synthetic comment -- @@ -274,9 +276,9 @@
* @return a new {@link ILayoutScene} object that contains the result of the scene creation and
* first rendering or null if {@link #getStatus()} doesn't return {@link LoadStatus#LOADED}.
*
     * @see Bridge#createSession(SessionParams)
*/
    public RenderSession createSession(SessionParams params) {
if (mBridge != null) {
return mBridge.createSession(params);
} else if (mLegacyBridge != null) {
//Synthetic comment -- @@ -287,6 +289,20 @@
}

/**
     * Renders a Drawable. If the rendering is successful, the result image is accessible through
     * {@link Result#getData()}. It is of type {@link BufferedImage}
     * @param params the rendering parameters.
     * @return the result of the action.
     */
    public Result renderDrawable(DrawableParams params) {
        if (mBridge != null) {
            return mBridge.renderDrawable(params);
        }

        return Status.NOT_IMPLEMENTED.createResult();
    }

    /**
* Clears the resource cache for a specific project.
* <p/>This cache contains bitmaps and nine patches that are loaded from the disk and reused
* until this method is called.
//Synthetic comment -- @@ -338,7 +354,7 @@
return apiLevel;
}

    private RenderSession createLegacySession(SessionParams params) {
if (params.getLayoutDescription() instanceof IXmlPullParser == false) {
throw new IllegalArgumentException("Parser must be of type ILegacyPullParser");
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index 755c736..48309cf 100644

//Synthetic comment -- @@ -17,6 +17,9 @@
package com.android.ide.common.rendering.api;


import com.android.ide.common.rendering.api.Result.Status;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.Map;
//Synthetic comment -- @@ -78,11 +81,21 @@
* @return a new {@link RenderSession} object that contains the result of the scene creation and
* first rendering.
*/
    public RenderSession createSession(SessionParams params) {
return null;
}

/**
     * Renders a Drawable. If the rendering is successful, the result image is accessible through
     * {@link Result#getData()}. It is of type {@link BufferedImage}
     * @param params the rendering parameters.
     * @return the result of the action.
     */
    public Result renderDrawable(DrawableParams params) {
        return Status.NOT_IMPLEMENTED.createResult();
    }

    /**
* Clears the resource cache for a specific project.
* <p/>This cache contains bitmaps and nine patches that are loaded from the disk and reused
* until this method is called.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java
new file mode 100644
//Synthetic comment -- index 0000000..766b3be

//Synthetic comment -- @@ -0,0 +1,78 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.ide.common.rendering.api;

import com.android.resources.Density;

/**
 * Rendering parameters for {@link Bridge#renderDrawable(DrawableParams)}
 *
 */
public class DrawableParams extends RenderParams {

    private final ResourceValue mDrawable;

    /**
    * Builds a param object with all the necessary parameters to render a drawable with
    * {@link Bridge#renderDrawable(DrawableParams)}
    *
    * @param drawable the {@link ResourceValue} identifying the drawable.
    * @param projectKey An Object identifying the project. This is used for the cache mechanism.
    * @param screenWidth the screen width
    * @param screenHeight the screen height
    * @param density the density factor for the screen.
    * @param xdpi the screen actual dpi in X
    * @param ydpi the screen actual dpi in Y
    * @param themeName The name of the theme to use.
    * @param isProjectTheme true if the theme is a project theme, false if it is a framework theme.
    * @param projectResources the resources of the project. The map contains (String, map) pairs
    * where the string is the type of the resource reference used in the layout file, and the
    * map contains (String, {@link ResourceValue}) pairs where the key is the resource name,
    * and the value is the resource value.
    * @param frameworkResources the framework resources. The map contains (String, map) pairs
    * where the string is the type of the resource reference used in the layout file, and the map
    * contains (String, {@link ResourceValue}) pairs where the key is the resource name, and the
    * value is the resource value.
    * @param projectCallback The {@link IProjectCallback} object to get information from
    * the project.
    * @param minSdkVersion the minSdkVersion of the project
    * @param targetSdkVersion the targetSdkVersion of the project
    * @param log the object responsible for displaying warning/errors to the user.
    */
    public DrawableParams(
            ResourceValue drawable,
            Object projectKey,
            int screenWidth, int screenHeight,
            Density density, float xdpi, float ydpi,
            RenderResources renderResources,
            IProjectCallback projectCallback,
            int minSdkVersion, int targetSdkVersion,
            LayoutLog log) {
        super(projectKey, screenWidth, screenHeight, density, xdpi, ydpi,
                renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);
        mDrawable = drawable;
    }

    public DrawableParams(DrawableParams params) {
        super(params);
        mDrawable = params.mDrawable;
    }

    public ResourceValue getDrawable() {
        return mDrawable;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index 296982c..2cfe770 100644

//Synthetic comment -- @@ -19,39 +19,18 @@
import com.android.resources.Density;
import com.android.resources.ScreenSize;

/**
 * Base class for rendering parameters. This include the generic parameters but not what needs
 * to be rendered or additional parameters.
 *
 */
public abstract class RenderParams {

public final static long DEFAULT_TIMEOUT = 250; //ms

private final Object mProjectKey;
private final int mScreenWidth;
private final int mScreenHeight;
private final Density mDensity;
private final float mXdpi;
private final float mYdpi;
//Synthetic comment -- @@ -75,12 +54,9 @@

/**
*
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
* @param screenWidth the screen width
* @param screenHeight the screen height
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
//Synthetic comment -- @@ -100,19 +76,17 @@
* @param targetSdkVersion the targetSdkVersion of the project
* @param log the object responsible for displaying warning/errors to the user.
*/
    public RenderParams(
Object projectKey,
            int screenWidth, int screenHeight,
Density density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
LayoutLog log) {
mProjectKey = projectKey;
mScreenWidth = screenWidth;
mScreenHeight = screenHeight;
mDensity = density;
mXdpi = xdpi;
mYdpi = ydpi;
//Synthetic comment -- @@ -129,11 +103,9 @@
* Copy constructor.
*/
public RenderParams(RenderParams params) {
mProjectKey = params.mProjectKey;
mScreenWidth = params.mScreenWidth;
mScreenHeight = params.mScreenHeight;
mDensity = params.mDensity;
mXdpi = params.mXdpi;
mYdpi = params.mYdpi;
//Synthetic comment -- @@ -186,10 +158,6 @@
mForceNoDecor = true;
}

public Object getProjectKey() {
return mProjectKey;
}
//Synthetic comment -- @@ -210,10 +178,6 @@
return mScreenHeight;
}

public Density getDensity() {
return mDensity;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Result.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Result.java
//Synthetic comment -- index 172a7e7..6152a28 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
ERROR_NOT_INFLATED,
ERROR_RENDER,
ERROR_ANIM_NOT_FOUND,
        ERROR_NOT_A_DRAWABLE,
ERROR_UNKNOWN;

private Result mResult;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
new file mode 100644
//Synthetic comment -- index 0000000..9446ff5

//Synthetic comment -- @@ -0,0 +1,115 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.ide.common.rendering.api;

import com.android.resources.Density;

/**
 * Rendering parameters for a {@link RenderSession}.
 *
 */
public class SessionParams extends RenderParams {

    public static enum RenderingMode {
        NORMAL(false, false),
        V_SCROLL(false, true),
        H_SCROLL(true, false),
        FULL_EXPAND(true, true);

        private final boolean mHorizExpand;
        private final boolean mVertExpand;

        private RenderingMode(boolean horizExpand, boolean vertExpand) {
            mHorizExpand = horizExpand;
            mVertExpand = vertExpand;
        }

        public boolean isHorizExpand() {
            return mHorizExpand;
        }

        public boolean isVertExpand() {
            return mVertExpand;
        }
    }


    private final ILayoutPullParser mLayoutDescription;
    private final RenderingMode mRenderingMode;

    /**
    *
    * @param layoutDescription the {@link ILayoutPullParser} letting the LayoutLib Bridge visit the
    * layout file.
    * @param renderingMode The rendering mode.
    * @param projectKey An Object identifying the project. This is used for the cache mechanism.
    * @param screenWidth the screen width
    * @param screenHeight the screen height
    * @param density the density factor for the screen.
    * @param xdpi the screen actual dpi in X
    * @param ydpi the screen actual dpi in Y
    * @param themeName The name of the theme to use.
    * @param isProjectTheme true if the theme is a project theme, false if it is a framework theme.
    * @param projectResources the resources of the project. The map contains (String, map) pairs
    * where the string is the type of the resource reference used in the layout file, and the
    * map contains (String, {@link ResourceValue}) pairs where the key is the resource name,
    * and the value is the resource value.
    * @param frameworkResources the framework resources. The map contains (String, map) pairs
    * where the string is the type of the resource reference used in the layout file, and the map
    * contains (String, {@link ResourceValue}) pairs where the key is the resource name, and the
    * value is the resource value.
    * @param projectCallback The {@link IProjectCallback} object to get information from
    * the project.
    * @param minSdkVersion the minSdkVersion of the project
    * @param targetSdkVersion the targetSdkVersion of the project
    * @param log the object responsible for displaying warning/errors to the user.
    */
   public SessionParams(
           ILayoutPullParser layoutDescription,
           RenderingMode renderingMode,
           Object projectKey,
           int screenWidth, int screenHeight,
           Density density, float xdpi, float ydpi,
           RenderResources renderResources,
           IProjectCallback projectCallback,
           int minSdkVersion, int targetSdkVersion,
           LayoutLog log) {
       super(projectKey, screenWidth, screenHeight, density, xdpi, ydpi,
               renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);

       mLayoutDescription = layoutDescription;
       mRenderingMode = renderingMode;

   }

   public SessionParams(SessionParams params) {
       super(params);
       mLayoutDescription = params.mLayoutDescription;
       mRenderingMode = params.mRenderingMode;
   }

   public ILayoutPullParser getLayoutDescription() {
       return mLayoutDescription;
   }

   public RenderingMode getRenderingMode() {
       return mRenderingMode;
   }



}







