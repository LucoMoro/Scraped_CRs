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
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
//Synthetic comment -- @@ -1322,10 +1323,23 @@
UiElementPullParser parser = new UiElementPullParser(model,
mUseExplodeMode, explodeNodes, density, xdpi, iProject);

SceneParams params = new SceneParams(
parser,
iProject /* projectKey */,
                width, height, !mClippingButton.getSelection(),
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index b7d648a..024e9e3 100644

//Synthetic comment -- @@ -32,14 +32,13 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.tests.SdkTestCase;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.SceneResult.LayoutStatus;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FolderWrapper;
//Synthetic comment -- @@ -206,7 +205,7 @@
null /*projectKey*/,
320,
480,
                    false, //renderFullSize
160, //density
160, //xdpi
160, // ydpi








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index f4c99bd..e1b8241 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
//Synthetic comment -- @@ -82,7 +83,8 @@
// Final ILayoutBridge API added support for "render full height"
result = mBridge.computeLayout(
params.getLayoutDescription(), params.getProjectKey(),
                    params.getScreenWidth(), params.getScreenHeight(), params.getRenderFullSize(),
params.getDensity(), params.getXdpi(), params.getYdpi(),
params.getThemeName(), params.getIsProjectTheme(),
params.getProjectResources(), params.getFrameworkResources(),








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 6b8f781..36c7a0a 100644

//Synthetic comment -- @@ -20,11 +20,34 @@

public class SceneParams {

private IXmlPullParser mLayoutDescription;
private Object mProjectKey;
private int mScreenWidth;
private int mScreenHeight;
    private boolean mRenderFullSize;
private int mDensity;
private float mXdpi;
private float mYdpi;
//Synthetic comment -- @@ -44,8 +67,7 @@
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
* @param screenWidth the screen width
* @param screenHeight the screen height
     * @param renderFullSize if true, the rendering will render the full size needed by the
     * layout. This size is never smaller than <var>screenWidth</var> x <var>screenHeight</var>.
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
//Synthetic comment -- @@ -65,7 +87,7 @@
*/
public SceneParams(IXmlPullParser layoutDescription,
Object projectKey,
            int screenWidth, int screenHeight, boolean renderFullSize,
int density, float xdpi, float ydpi,
String themeName, boolean isProjectTheme,
Map<String, Map<String, IResourceValue>> projectResources,
//Synthetic comment -- @@ -75,7 +97,7 @@
mProjectKey = projectKey;
mScreenWidth = screenWidth;
mScreenHeight = screenHeight;
        mRenderFullSize = renderFullSize;
mDensity = density;
mXdpi = xdpi;
mYdpi = ydpi;
//Synthetic comment -- @@ -96,7 +118,7 @@
mProjectKey = params.mProjectKey;
mScreenWidth = params.mScreenWidth;
mScreenHeight = params.mScreenHeight;
        mRenderFullSize = params.mRenderFullSize;
mDensity = params.mDensity;
mXdpi = params.mXdpi;
mYdpi = params.mYdpi;
//Synthetic comment -- @@ -131,8 +153,8 @@
return mScreenHeight;
}

    public boolean getRenderFullSize() {
        return mRenderFullSize;
}

public int getDensity() {







