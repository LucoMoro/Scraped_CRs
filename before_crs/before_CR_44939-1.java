/*Update Layoutlib_api to use a class for h/w config.

Change-Id:Iead02d468590407ec274357f1a1c57ed8d5cc24c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 9553bc8..c89a81b 100644

//Synthetic comment -- @@ -79,7 +79,6 @@
private boolean mIncreaseExistingPadding = false;
private LayoutDescriptors mDescriptors;
private final Density mDensity;
    private final float mXdpi;

/**
* Number of pixels to pad views with in exploded-rendering mode.
//Synthetic comment -- @@ -114,18 +113,16 @@
*            nodes are not individually exploded (but they may all be exploded with the
*            explodeRendering parameter.
* @param density the density factor for the screen.
     * @param xdpi the screen actual dpi in X
* @param project Project containing this layout.
*/
public UiElementPullParser(UiElementNode top, boolean explodeRendering,
Set<UiElementNode> explodeNodes,
            Density density, float xdpi, IProject project) {
super();
mRoot = top;
mExplodedRendering = explodeRendering;
mExplodeNodes = explodeNodes;
mDensity = density;
        mXdpi = xdpi;
if (mExplodedRendering) {
// get the layout descriptor
IAndroidTarget target = Sdk.getCurrent().getTarget(project);
//Synthetic comment -- @@ -631,13 +628,13 @@
f *= (float)mDensity.getDpiValue() / Density.DEFAULT_DENSITY;
break;
case COMPLEX_UNIT_PT:
                            f *= mXdpi * (1.0f / 72);
break;
case COMPLEX_UNIT_IN:
                            f *= mXdpi;
break;
case COMPLEX_UNIT_MM:
                            f *= mXdpi * (1.0f / 25.4f);
break;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index c0b57ac..f9a8ff1 100644

//Synthetic comment -- @@ -953,49 +953,6 @@
return Density.MEDIUM;
}

    /**
     * Returns the current device xdpi.
     *
     * @return the x dpi as a float
     */
    public float getXDpi() {
        Device device = getDevice();
        if (device != null) {
            State currState = getDeviceState();
            if (currState == null) {
                currState = device.getDefaultState();
            }
            float dpi = (float) currState.getHardware().getScreen().getXdpi();
            if (!Float.isNaN(dpi)) {
                return dpi;
            }
        }

        // get the pixel density as the density.
        return getDensity().getDpiValue();
    }

    /**
     * Returns the current device ydpi.
     *
     * @return the y dpi as a float
     */
    public float getYDpi() {
        Device device = getDevice();
        if (device != null) {
            State currState = getDeviceState();
            if (currState == null) {
                currState = device.getDefaultState();
            }
            float dpi = (float) currState.getHardware().getScreen().getYdpi();
            if (!Float.isNaN(dpi)) {
                return dpi;
            }
        }

        // get the pixel density as the density.
        return getDensity().getDpiValue();
    }

/**
* Returns the bounds of the screen








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..0799e35

//Synthetic comment -- @@ -0,0 +1,52 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index d5b46b4..1b1bd23 100644

//Synthetic comment -- @@ -33,6 +33,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
import com.google.common.base.Strings;

//Synthetic comment -- @@ -708,7 +710,9 @@
// compute average dpi of X and Y
ConfigurationChooser chooser = mEditor.getConfigurationChooser();
Configuration config = chooser.getConfiguration();
        float dpi = (config.getXDpi() + config.getYDpi()) / 2.f;

// get the monitor dpi
float monitor = AdtPrefs.getPrefs().getMonitorDensity();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 4811235..e298b3e 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -529,9 +528,7 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
FolderConfiguration config = mConfiguration.getFullConfig();
        RenderService renderService = RenderService.create(editor, config, resolver);
        ScreenSizeQualifier screenSize = config.getScreenSizeQualifier();
        renderService.setScreen(screenSize, mConfiguration.getXDpi(), mConfiguration.getYDpi());

if (mIncludedWithin != null) {
renderService.setIncludedWithin(mIncludedWithin);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 2a1f3b7..8776b75 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.IImageFactory;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
//Synthetic comment -- @@ -34,12 +35,9 @@
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration;
//Synthetic comment -- @@ -53,11 +51,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.ButtonType;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.Hardware;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
//Synthetic comment -- @@ -89,13 +83,9 @@
private final ResourceResolver mResourceResolver;
private final int mMinSdkVersion;
private final int mTargetSdkVersion;
    private final boolean mSoftwareButtons;
private final LayoutLibrary mLayoutLib;
private final IImageFactory mImageFactory;
    private final Density mDensity;
    private float mXdpi;
    private float mYdpi;
    private ScreenSizeQualifier mScreenSize;

// The following fields are optional or configurable using the various chained
// setters:
//Synthetic comment -- @@ -103,7 +93,6 @@
private UiDocumentNode mModel;
private int mWidth = -1;
private int mHeight = -1;
    private boolean mUseExplodeMode;
private Reference mIncludedWithin;
private RenderingMode mRenderingMode = RenderingMode.NORMAL;
private LayoutLog mLogger;
//Synthetic comment -- @@ -120,60 +109,37 @@
mImageFactory = canvas.getImageOverlay();
ConfigurationChooser chooser = editor.getConfigurationChooser();
Configuration config = chooser.getConfiguration();
        mDensity = config.getDensity();
        mXdpi = config.getXDpi();
        mYdpi = config.getYDpi();
        mScreenSize = chooser.getConfiguration().getFullConfig().getScreenSizeQualifier();
mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
mResourceResolver = editor.getResourceResolver();
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
        mSoftwareButtons = getButtonType(config) == ButtonType.SOFT;
}

    private RenderService(GraphicalEditorPart editor, FolderConfiguration configuration,
            ResourceResolver resourceResolver) {
mEditor = editor;

mProject = editor.getProject();
LayoutCanvas canvas = editor.getCanvasControl();
mImageFactory = canvas.getImageOverlay();
        Configuration config = editor.getConfigurationChooser().getConfiguration();
        mXdpi = config.getXDpi();
        mYdpi = config.getYDpi();
mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
mResourceResolver =  resourceResolver != null ? resourceResolver : editor.getResourceResolver();
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
        mSoftwareButtons = getButtonType(config) == ButtonType.SOFT;

        // TODO: Look up device etc and offer additional configuration options here?
        Density density = Density.MEDIUM;
        DensityQualifier densityQualifier = configuration.getDensityQualifier();
        if (densityQualifier != null) {
            // just a sanity check
            Density d = densityQualifier.getValue();
            if (d != Density.NODPI) {
                density = d;
            }
        }
        mDensity = density;
        mScreenSize = configuration.getScreenSizeQualifier();
    }

    @NonNull
    private static ButtonType getButtonType(@NonNull Configuration configuration) {
        Device device = configuration.getDevice();
        if (device != null) {
            Hardware hardware = device.getDefaultHardware();
            if (hardware != null) {
                return hardware.getButtonType();
            }
        }

        return ButtonType.SOFT;
}

/**
//Synthetic comment -- @@ -202,21 +168,6 @@
}

/**
     * Sets the screen size and density to use for rendering
     *
     * @param screenSize the screen size
     * @param xdpi the x density
     * @param ydpi the y density
     * @return this, for constructor chaining
     */
    public RenderService setScreen(ScreenSizeQualifier screenSize, float xdpi, float ydpi) {
        mXdpi = xdpi;
        mYdpi = ydpi;
        mScreenSize = screenSize;
        return this;
    }

    /**
* Creates a new {@link RenderService} associated with the given editor.
*
* @param editor the editor to provide configuration data such as the render target
//Synthetic comment -- @@ -237,7 +188,7 @@
* @return a {@link RenderService} which can perform rendering services
*/
public static RenderService create(GraphicalEditorPart editor,
            FolderConfiguration configuration, ResourceResolver resolver) {
RenderService renderService = new RenderService(editor, configuration, resolver);

return renderService;
//Synthetic comment -- @@ -384,26 +335,10 @@
return null;
}

        int width = mWidth;
        int height = mHeight;
        if (mUseExplodeMode) {
            // compute how many padding in x and y will bump the screen size
            List<UiElementNode> children = mModel.getUiChildren();
            if (children.size() == 1) {
                ExplodedRenderingHelper helper = new ExplodedRenderingHelper(
                        children.get(0).getXmlNode(), mProject);

                // there are 2 paddings for each view
                // left and right, or top and bottom.
                int paddingValue = ExplodedRenderingHelper.PADDING_VALUE * 2;

                width += helper.getWidthPadding() * paddingValue;
                height += helper.getHeightPadding() * paddingValue;
            }
        }

UiElementPullParser modelParser = new UiElementPullParser(mModel,
                mUseExplodeMode, mExpandNodes, mDensity, mXdpi, mProject);
ILayoutPullParser topParser = modelParser;

// Code to support editing included layout
//Synthetic comment -- @@ -441,13 +376,11 @@
topParser,
mRenderingMode,
mProject /* projectKey */,
                width, height,
                mDensity, mXdpi, mYdpi,
mResourceResolver,
mProjectCallback,
mMinSdkVersion,
mTargetSdkVersion,
                mSoftwareButtons,
mLogger);

// Request margin and baseline information.
//Synthetic comment -- @@ -469,10 +402,6 @@
}
}

        if (mScreenSize != null) {
            params.setConfigScreenSize(mScreenSize.getValue());
        }

if (mOverrideBgColor != null) {
params.setOverrideBgColor(mOverrideBgColor.intValue());
}
//Synthetic comment -- @@ -508,8 +437,10 @@

finishConfiguration();

        DrawableParams params = new DrawableParams(drawableResourceValue, mProject, mWidth, mHeight,
                mDensity, mXdpi, mYdpi, mResourceResolver, mProjectCallback, mMinSdkVersion,
mTargetSdkVersion, mLogger);
params.setForceNoDecor();
Result result = mLayoutLib.renderDrawable(params);
//Synthetic comment -- @@ -534,14 +465,13 @@
public Map<INode, Rect> measureChildren(INode parent,
final IClientRulesEngine.AttributeFilter filter) {
finishConfiguration();

        int width = parent.getBounds().w;
        int height = parent.getBounds().h;

final NodeFactory mNodeFactory = mEditor.getCanvasControl().getNodeFactory();
UiElementNode parentNode = ((NodeProxy) parent).getNode();
UiElementPullParser topParser = new UiElementPullParser(parentNode,
                false, Collections.<UiElementNode>emptySet(), mDensity, mXdpi, mProject) {
@Override
public String getAttributeValue(String namespace, String localName) {
if (filter != null) {
//Synthetic comment -- @@ -577,13 +507,11 @@
topParser,
RenderingMode.FULL_EXPAND,
mProject /* projectKey */,
                width, height,
                mDensity, mXdpi, mYdpi,
mResourceResolver,
mProjectCallback,
mMinSdkVersion,
mTargetSdkVersion,
                mSoftwareButtons,
mLogger);
params.setLayoutOnly();
params.setForceNoDecor();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 7a6eef4..2b364a5 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.SdkConstants;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderSession;
//Synthetic comment -- @@ -250,20 +251,25 @@
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
resolver,
projectCallBack,
1, // minSdkVersion
1, // targetSdkVersion
                    false, // softwareButtons
null //logger
));









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index 998c166..5cac663 100644

//Synthetic comment -- @@ -170,7 +170,6 @@
false, // explodedView
null, // explodeNodes
Density.MEDIUM, // density (default from ConfigurationComposite)
                    Density.MEDIUM.getDpiValue(), // xdpi (default from ConfigurationComposite)
null // iProject
);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationTest.java
//Synthetic comment -- index c14e7f7..6ad87f9 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.resources.Density;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.utils.StdLogger;

import java.lang.reflect.Constructor;
//Synthetic comment -- @@ -97,8 +98,9 @@
configuration.toPersistentString());

assertEquals(Density.MEDIUM, configuration.getDensity());
        assertEquals(145.0f, configuration.getXDpi(), 0.001);
        assertEquals(145.0f, configuration.getYDpi(), 0.001);
assertEquals(new Rect(0, 0, 320, 480), configuration.getScreenBounds());
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java
//Synthetic comment -- index 346d67d..b566ad3 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.rendering.api;

import com.android.resources.Density;

/**
* Rendering parameters for {@link Bridge#renderDrawable(DrawableParams)}
//Synthetic comment -- @@ -32,11 +31,7 @@
*
* @param drawable the {@link ResourceValue} identifying the drawable.
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
    * @param screenWidth the screen width
    * @param screenHeight the screen height
    * @param density the density factor for the screen.
    * @param xdpi the screen actual dpi in X
    * @param ydpi the screen actual dpi in Y
* @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
//Synthetic comment -- @@ -47,13 +42,12 @@
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








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/HardwareConfig.java b/layoutlib_api/src/com/android/ide/common/rendering/api/HardwareConfig.java
new file mode 100644
//Synthetic comment -- index 0000000..18b0dd7

//Synthetic comment -- @@ -0,0 +1,91 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index f89dcfe..2e53f14 100644

//Synthetic comment -- @@ -29,11 +29,7 @@
public final static long DEFAULT_TIMEOUT = 250; //ms

private final Object mProjectKey;
    private final int mScreenWidth;
    private final int mScreenHeight;
    private final Density mDensity;
    private final float mXdpi;
    private final float mYdpi;
private final RenderResources mRenderResources;
private final IProjectCallback mProjectCallback;
private final int mMinSdkVersion;
//Synthetic comment -- @@ -46,7 +42,6 @@

private IImageFactory mImageFactory = null;

    private ScreenSize mConfigScreenSize = null;
private String mAppIcon = null;
private String mAppLabel = null;
private String mLocale = null;
//Synthetic comment -- @@ -55,11 +50,7 @@
/**
*
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
     * @param screenWidth the screen width
     * @param screenHeight the screen height
     * @param density the density factor for the screen.
     * @param xdpi the screen actual dpi in X
     * @param ydpi the screen actual dpi in Y
* @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
//Synthetic comment -- @@ -69,18 +60,13 @@
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
mRenderResources = renderResources;
mProjectCallback = projectCallback;
mMinSdkVersion = minSdkVersion;
//Synthetic comment -- @@ -95,11 +81,7 @@
*/
public RenderParams(RenderParams params) {
mProjectKey = params.mProjectKey;
        mScreenWidth = params.mScreenWidth;
        mScreenHeight = params.mScreenHeight;
        mDensity = params.mDensity;
        mXdpi = params.mXdpi;
        mYdpi = params.mYdpi;
mRenderResources = params.mRenderResources;
mProjectCallback = params.mProjectCallback;
mMinSdkVersion = params.mMinSdkVersion;
//Synthetic comment -- @@ -109,7 +91,6 @@
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
mImageFactory = params.mImageFactory;
        mConfigScreenSize = params.mConfigScreenSize;
mAppIcon = params.mAppIcon;
mAppLabel = params.mAppLabel;
mLocale = params.mLocale;
//Synthetic comment -- @@ -129,10 +110,6 @@
mImageFactory = imageFactory;
}

    public void setConfigScreenSize(ScreenSize size) {
        mConfigScreenSize  = size;
    }

public void setAppIcon(String appIcon) {
mAppIcon = appIcon;
}
//Synthetic comment -- @@ -153,6 +130,10 @@
return mProjectKey;
}

public int getMinSdkVersion() {
return mMinSdkVersion;
}
//Synthetic comment -- @@ -161,24 +142,44 @@
return mTargetSdkVersion;
}

public int getScreenWidth() {
        return mScreenWidth;
}

public int getScreenHeight() {
        return mScreenHeight;
}

public Density getDensity() {
        return mDensity;
}

public float getXdpi() {
        return mXdpi;
}

public float getYdpi() {
        return mYdpi;
}

public RenderResources getResources() {
//Synthetic comment -- @@ -209,8 +210,12 @@
return mImageFactory;
}

public ScreenSize getConfigScreenSize() {
        return mConfigScreenSize;
}

public String getAppIcon() {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index e3edbd2..f440de1 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.ide.common.rendering.api;

import com.android.resources.Density;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
//Synthetic comment -- @@ -52,7 +50,6 @@

private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;
    private final boolean mSoftwareButtons;
private boolean mLayoutOnly = false;
private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;
private boolean mExtendedViewInfoMode = false;
//Synthetic comment -- @@ -80,26 +77,22 @@
ILayoutPullParser layoutDescription,
RenderingMode renderingMode,
Object projectKey,
            int screenWidth, int screenHeight,
            Density density, float xdpi, float ydpi,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
            boolean softwareButtons,
LayoutLog log) {
        super(projectKey, screenWidth, screenHeight, density, xdpi, ydpi,
renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);

mLayoutDescription = layoutDescription;
mRenderingMode = renderingMode;
        mSoftwareButtons = softwareButtons;
}

public SessionParams(SessionParams params) {
super(params);
mLayoutDescription = params.mLayoutDescription;
mRenderingMode = params.mRenderingMode;
        mSoftwareButtons = params.mSoftwareButtons;
if (params.mAdapterBindingMap != null) {
mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>(
params.mAdapterBindingMap);
//Synthetic comment -- @@ -115,10 +108,6 @@
return mRenderingMode;
}

    public boolean hasSoftwareButtons() {
        return mSoftwareButtons;
    }

public void setLayoutOnly() {
mLayoutOnly = true;
}







