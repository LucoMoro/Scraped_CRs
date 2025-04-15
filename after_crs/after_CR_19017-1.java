/*ADT/Layoutlib: update API to control how layout expands.

Previous API was a single on/off to let the layouts expand at
render time depending on how much space they needed.

The new API can now control expansion is horizontal and/or
vertical (or not at all)

Basic implementation in the editor, with a manual detect of
"ScrollView" as top element. We should make the ViewRule handle
this somehow.

Change-Id:Idc503bc0d1d3df98fbf01cc84625952ca55a8afb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 728c089..f04fcc1 100755

//Synthetic comment -- @@ -52,6 +52,7 @@
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
//Synthetic comment -- @@ -1322,10 +1323,23 @@
UiElementPullParser parser = new UiElementPullParser(model,
mUseExplodeMode, explodeNodes, density, xdpi, iProject);

        RenderingMode renderingMode = RenderingMode.NORMAL;
        if (mClippingButton.getSelection() == false) {
            renderingMode = RenderingMode.FULL_EXPAND;
        } else {
            // FIXME set the rendering mode using ViewRule or something.
            List<UiElementNode> children = model.getUiChildren();
            if (children.size() > 0 &&
                    children.get(0).getDescriptor().getXmlLocalName().equals("ScrollView")) {
                renderingMode = RenderingMode.V_SCROLL;
            }
        }

SceneParams params = new SceneParams(
parser,
iProject /* projectKey */,
                width, height,
                renderingMode,
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index b7d648a..024e9e3 100644

//Synthetic comment -- @@ -32,14 +32,13 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.tests.SdkTestCase;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FolderWrapper;
//Synthetic comment -- @@ -206,7 +205,7 @@
null /*projectKey*/,
320,
480,
                    RenderingMode.NORMAL,
160, //density
160, //xdpi
160, // ydpi








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index f4c99bd..e1b8241 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.layoutlib.api.SceneParams.RenderingMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
//Synthetic comment -- @@ -82,7 +83,8 @@
// Final ILayoutBridge API added support for "render full height"
result = mBridge.computeLayout(
params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(),
                    params.getRenderingMode() == RenderingMode.FULL_EXPAND ? true : false,
params.getDensity(), params.getXdpi(), params.getYdpi(),
params.getThemeName(), params.getIsProjectTheme(),
params.getProjectResources(), params.getFrameworkResources(),








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 6b8f781..36c7a0a 100644

//Synthetic comment -- @@ -20,11 +20,34 @@

public class SceneParams {

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

private IXmlPullParser mLayoutDescription;
private Object mProjectKey;
private int mScreenWidth;
private int mScreenHeight;
    private RenderingMode mRenderingMode;
private int mDensity;
private float mXdpi;
private float mYdpi;
//Synthetic comment -- @@ -44,8 +67,7 @@
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
* @param screenWidth the screen width
* @param screenHeight the screen height
     * @param renderingMode The rendering mode.
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
//Synthetic comment -- @@ -65,7 +87,7 @@
*/
public SceneParams(IXmlPullParser layoutDescription,
Object projectKey,
            int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
String themeName, boolean isProjectTheme,
Map<String, Map<String, IResourceValue>> projectResources,
//Synthetic comment -- @@ -75,7 +97,7 @@
mProjectKey = projectKey;
mScreenWidth = screenWidth;
mScreenHeight = screenHeight;
        mRenderingMode = renderingMode;
mDensity = density;
mXdpi = xdpi;
mYdpi = ydpi;
//Synthetic comment -- @@ -96,7 +118,7 @@
mProjectKey = params.mProjectKey;
mScreenWidth = params.mScreenWidth;
mScreenHeight = params.mScreenHeight;
        mRenderingMode = params.mRenderingMode;
mDensity = params.mDensity;
mXdpi = params.mXdpi;
mYdpi = params.mYdpi;
//Synthetic comment -- @@ -131,8 +153,8 @@
return mScreenHeight;
}

    public RenderingMode getRenderingMode() {
        return mRenderingMode;
}

public int getDensity() {







