/*Move the resource resolution code into ide-common.

Also move the LayoutLib API to use a new class for all resource
info instead of 2 maps, one string, and a boolean.

The goal is to move resource resolution code into ADT
so that we can use it to better display resource information
in the UI.

Change-Id:Iad1c1719ab0b08d1a7d0987b92d4be1d3a895adf*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 1783ab3..2a457e9 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
//Synthetic comment -- @@ -1624,14 +1625,19 @@
}
}

Params params = new Params(
topParser,
iProject /* projectKey */,
width, height,
renderingMode,
density, xdpi, ydpi,
                theme, isProjectTheme,
                configuredProjectRes, frameworkResources, mProjectCallback,
logger);

if (transparentBackground) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index a1b11ae..0f6b508 100644

//Synthetic comment -- @@ -112,10 +112,13 @@
@Override
public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
if (CONTAINER_ID.equals(containerPath.toString())) {
            JavaCore.setClasspathContainer(new Path(CONTAINER_ID),
                    new IJavaProject[] { project },
                    new IClasspathContainer[] { allocateAndroidContainer(project) },
                    new NullProgressMonitor());
}
}

//Synthetic comment -- @@ -182,6 +185,9 @@

try {
AdtPlugin plugin = AdtPlugin.getDefault();

synchronized (Sdk.getLock()) {
boolean sdkIsLoaded = plugin.getSdkLoadStatus() == LoadStatus.LOADED;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 2fee75b..05b360f 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.KeyboardStateQualifier;
//Synthetic comment -- @@ -199,6 +200,10 @@

ProjectCallBack projectCallBack = new ProjectCallBack();

RenderSession session = layoutLib.createSession(new Params(
parser,
null /*projectKey*/,
//Synthetic comment -- @@ -208,10 +213,7 @@
160, //density
160, //xdpi
160, // ydpi
                    "Theme", //themeName
                    false, //isProjectTheme
                    configuredProject,
                    configuredFramework,
projectCallBack,
null //logger
));








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index a757c20..815ee19 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.ide.common.rendering.api.Result.Status;
import com.android.ide.common.rendering.legacy.ILegacyCallback;
import com.android.ide.common.rendering.legacy.ILegacyPullParser;
import com.android.ide.common.sdk.LoadStatus;
import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.ILayoutLog;
//Synthetic comment -- @@ -339,6 +340,12 @@
throw new IllegalArgumentException("Project callback must be of type ILegacyCallback");
}

int apiLevel = getLegacyApiLevel();

// create a log wrapper since the older api requires a ILayoutLog
//Synthetic comment -- @@ -358,13 +365,15 @@
}
};

// convert the map of ResourceValue into IResourceValue. Super ugly but works.
@SuppressWarnings("unchecked")
Map<String, Map<String, IResourceValue>> projectMap =
            (Map<String, Map<String, IResourceValue>>)(Map) params.getProjectResources();
@SuppressWarnings("unchecked")
Map<String, Map<String, IResourceValue>> frameworkMap =
            (Map<String, Map<String, IResourceValue>>)(Map) params.getFrameworkResources();

ILayoutResult result = null;

//Synthetic comment -- @@ -376,7 +385,7 @@
params.getScreenWidth(), params.getScreenHeight(),
params.getRenderingMode() == RenderingMode.FULL_EXPAND ? true : false,
params.getDensity(), params.getXdpi(), params.getYdpi(),
                    params.getThemeName(), params.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(),
logWrapper);
//Synthetic comment -- @@ -386,7 +395,7 @@
(IXmlPullParser) params.getLayoutDescription(), params.getProjectKey(),
params.getScreenWidth(), params.getScreenHeight(),
params.getDensity(), params.getXdpi(), params.getYdpi(),
                    params.getThemeName(), params.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(), logWrapper);
} else if (apiLevel == 2) {
//Synthetic comment -- @@ -394,7 +403,7 @@
result = mLegacyBridge.computeLayout(
(IXmlPullParser) params.getLayoutDescription(), params.getProjectKey(),
params.getScreenWidth(), params.getScreenHeight(),
                    params.getThemeName(), params.isProjectTheme(),
projectMap, frameworkMap,
(IProjectCallback) params.getProjectCallback(), logWrapper);
} else {
//Synthetic comment -- @@ -403,8 +412,8 @@

// change the string if it's a custom theme to make sure we can
// differentiate them
            String themeName = params.getThemeName();
            if (params.isProjectTheme()) {
themeName = "*" + themeName; //$NON-NLS-1$
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
new file mode 100644
//Synthetic comment -- index 0000000..fb67b1b

//Synthetic comment -- @@ -0,0 +1,504 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Params.java
//Synthetic comment -- index 59e790e..3cede41 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.rendering.api;

import java.util.Map;

public class Params {

//Synthetic comment -- @@ -45,20 +44,17 @@
}
}

    private ILayoutPullParser mLayoutDescription;
    private Object mProjectKey;
    private int mScreenWidth;
    private int mScreenHeight;
    private RenderingMode mRenderingMode;
    private int mDensity;
    private float mXdpi;
    private float mYdpi;
    private String mThemeName;
    private boolean mIsProjectTheme;
    private Map<String, Map<String, ResourceValue>> mProjectResources;
    private Map<String, Map<String, ResourceValue>> mFrameworkResources;
    private IProjectCallback mProjectCallback;
    private LayoutLog mLog;

private boolean mCustomBackgroundEnabled;
private int mCustomBackgroundColor;
//Synthetic comment -- @@ -95,9 +91,7 @@
Object projectKey,
int screenWidth, int screenHeight, RenderingMode renderingMode,
int density, float xdpi, float ydpi,
            String themeName, boolean isProjectTheme,
            Map<String, Map<String, ResourceValue>> projectResources,
            Map<String, Map<String, ResourceValue>> frameworkResources,
IProjectCallback projectCallback, LayoutLog log) {
mLayoutDescription = layoutDescription;
mProjectKey = projectKey;
//Synthetic comment -- @@ -107,10 +101,7 @@
mDensity = density;
mXdpi = xdpi;
mYdpi = ydpi;
        mThemeName = themeName;
        mIsProjectTheme = isProjectTheme;
        mProjectResources = projectResources;
        mFrameworkResources = frameworkResources;
mProjectCallback = projectCallback;
mLog = log;
mCustomBackgroundEnabled = false;
//Synthetic comment -- @@ -129,10 +120,7 @@
mDensity = params.mDensity;
mXdpi = params.mXdpi;
mYdpi = params.mYdpi;
        mThemeName = params.mThemeName;
        mIsProjectTheme = params.mIsProjectTheme;
        mProjectResources = params.mProjectResources;
        mFrameworkResources = params.mFrameworkResources;
mProjectCallback = params.mProjectCallback;
mLog = params.mLog;
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
//Synthetic comment -- @@ -186,20 +174,8 @@
return mYdpi;
}

    public String getThemeName() {
        return mThemeName;
    }

    public boolean isProjectTheme() {
        return mIsProjectTheme;
    }

    public Map<String, Map<String, ResourceValue>> getProjectResources() {
        return mProjectResources;
    }

    public Map<String, Map<String, ResourceValue>> getFrameworkResources() {
        return mFrameworkResources;
}

public IProjectCallback getProjectCallback() {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java
new file mode 100644
//Synthetic comment -- index 0000000..e371d5a

//Synthetic comment -- @@ -0,0 +1,167 @@







