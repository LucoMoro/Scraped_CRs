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
import com.android.ide.common.rendering.api.RenderParams;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -1660,11 +1660,11 @@
configuredProjectRes, frameworkResources,
theme, isProjectTheme);

        RenderParams params = new RenderParams(
topParser,
iProject /* projectKey */,
width, height,
                renderingMode,
density, xdpi, ydpi,
resolver,
mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 08aef31..b73bff8 100755

//Synthetic comment -- @@ -24,12 +24,13 @@

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 1d92f3a..d6057fc 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 0fa0df9..f18ec36 100644

//Synthetic comment -- @@ -19,10 +19,10 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderParams;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -207,12 +207,12 @@
configuredProject, configuredFramework,
"Theme", false /*isProjectTheme*/);

            RenderSession session = layoutLib.createSession(new RenderParams(
parser,
null /*projectKey*/,
320,
480,
                    RenderingMode.NORMAL,
Density.MEDIUM,
160, //xdpi
160, // ydpi








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 43444de..591e787 100644

//Synthetic comment -- @@ -21,15 +21,15 @@
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderParams;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.RenderParams.RenderingMode;
import com.android.ide.common.rendering.api.Result.Status;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.common.rendering.legacy.ILegacyPullParser;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.layoutlib.api.ILayoutBridge;
//Synthetic comment -- @@ -58,7 +58,7 @@
* Use {@link #load(String, ILogger)} to load the jar file.
* <p/>
* Use the layout library with:
 * {@link #init(String, Map)}, {@link #supports(Capability)}, {@link #createSession(RenderParams)},
* {@link #dispose()}, {@link #clearCaches(Object)}.
*
* <p/>
//Synthetic comment -- @@ -274,9 +274,9 @@
* @return a new {@link ILayoutScene} object that contains the result of the scene creation and
* first rendering or null if {@link #getStatus()} doesn't return {@link LoadStatus#LOADED}.
*
     * @see Bridge#createSession(RenderParams)
*/
    public RenderSession createSession(RenderParams params) {
if (mBridge != null) {
return mBridge.createSession(params);
} else if (mLegacyBridge != null) {
//Synthetic comment -- @@ -338,7 +338,7 @@
return apiLevel;
}

    private RenderSession createLegacySession(RenderParams params) {
if (params.getLayoutDescription() instanceof IXmlPullParser == false) {
throw new IllegalArgumentException("Parser must be of type ILegacyPullParser");
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index 755c736..48309cf 100644

//Synthetic comment -- @@ -17,6 +17,9 @@
package com.android.ide.common.rendering.api;


import java.io.File;
import java.util.EnumSet;
import java.util.Map;
//Synthetic comment -- @@ -78,11 +81,21 @@
* @return a new {@link RenderSession} object that contains the result of the scene creation and
* first rendering.
*/
    public RenderSession createSession(RenderParams params) {
return null;
}

/**
* Clears the resource cache for a specific project.
* <p/>This cache contains bitmaps and nine patches that are loaded from the disk and reused
* until this method is called.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java
new file mode 100644
//Synthetic comment -- index 0000000..fc47aea

//Synthetic comment -- @@ -0,0 +1,74 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index 296982c..2cfe770 100644

//Synthetic comment -- @@ -19,39 +19,18 @@
import com.android.resources.Density;
import com.android.resources.ScreenSize;


public class RenderParams {

public final static long DEFAULT_TIMEOUT = 250; //ms

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
private final Object mProjectKey;
private final int mScreenWidth;
private final int mScreenHeight;
    private final RenderingMode mRenderingMode;
private final Density mDensity;
private final float mXdpi;
private final float mYdpi;
//Synthetic comment -- @@ -75,12 +54,9 @@

/**
*
     * @param layoutDescription the {@link ILayoutPullParser} letting the LayoutLib Bridge visit the
     * layout file.
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
* @param screenWidth the screen width
* @param screenHeight the screen height
     * @param renderingMode The rendering mode.
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
//Synthetic comment -- @@ -100,19 +76,17 @@
* @param targetSdkVersion the targetSdkVersion of the project
* @param log the object responsible for displaying warning/errors to the user.
*/
    public RenderParams(ILayoutPullParser layoutDescription,
Object projectKey,
            int screenWidth, int screenHeight, RenderingMode renderingMode,
Density density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
LayoutLog log) {
        mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
mScreenWidth = screenWidth;
mScreenHeight = screenHeight;
        mRenderingMode = renderingMode;
mDensity = density;
mXdpi = xdpi;
mYdpi = ydpi;
//Synthetic comment -- @@ -129,11 +103,9 @@
* Copy constructor.
*/
public RenderParams(RenderParams params) {
        mLayoutDescription = params.mLayoutDescription;
mProjectKey = params.mProjectKey;
mScreenWidth = params.mScreenWidth;
mScreenHeight = params.mScreenHeight;
        mRenderingMode = params.mRenderingMode;
mDensity = params.mDensity;
mXdpi = params.mXdpi;
mYdpi = params.mYdpi;
//Synthetic comment -- @@ -186,10 +158,6 @@
mForceNoDecor = true;
}

    public ILayoutPullParser getLayoutDescription() {
        return mLayoutDescription;
    }

public Object getProjectKey() {
return mProjectKey;
}
//Synthetic comment -- @@ -210,10 +178,6 @@
return mScreenHeight;
}

    public RenderingMode getRenderingMode() {
        return mRenderingMode;
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
ERROR_UNKNOWN;

private Result mResult;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
new file mode 100644
//Synthetic comment -- index 0000000..9446ff5

//Synthetic comment -- @@ -0,0 +1,115 @@







