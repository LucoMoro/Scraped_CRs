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
//Synthetic comment -- index c0b57ac..00caccd 100644

//Synthetic comment -- @@ -22,7 +22,6 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.DensityQualifier;
//Synthetic comment -- @@ -31,8 +30,6 @@
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
//Synthetic comment -- @@ -44,7 +41,6 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenSize;
import com.android.resources.UiMode;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -954,96 +950,6 @@
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
     *
     * @return the screen bounds
     */
    public Rect getScreenBounds() {
        return getScreenBounds(mFullConfig);
    }

    /**
     * Gets the orientation from the given configuration
     *
     * @param config the configuration to look up
     * @return the bounds
     */
    @NonNull
    public static Rect getScreenBounds(FolderConfiguration config) {
        // get the orientation from the given device config
        ScreenOrientationQualifier qual = config.getScreenOrientationQualifier();
        ScreenOrientation orientation = ScreenOrientation.PORTRAIT;
        if (qual != null) {
            orientation = qual.getValue();
        }

        // get the device screen dimension
        ScreenDimensionQualifier qual2 = config.getScreenDimensionQualifier();
        int s1, s2;
        if (qual2 != null) {
            s1 = qual2.getValue1();
            s2 = qual2.getValue2();
        } else {
            s1 = 480;
            s2 = 320;
        }

        switch (orientation) {
            default:
            case PORTRAIT:
                return new Rect(0, 0, s2, s1);
            case LANDSCAPE:
                return new Rect(0, 0, s1, s2);
            case SQUARE:
                return new Rect(0, 0, s1, s1);
        }
    }

    /**
* Get the next cyclical state after the given state
*
* @param from the state to start with








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 9b8eeea..67a890f 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import static com.android.SdkConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser.NAME_CONFIG_STATE;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor.viewNeedsPackage;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_EAST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_WEST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.STATE_COLLAPSED;
//Synthetic comment -- @@ -40,7 +41,6 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
//Synthetic comment -- @@ -1316,15 +1316,6 @@
}

/**
     * Returns the current bounds of the Android device screen, in canvas control pixels.
     *
     * @return the bounds of the screen, never null
     */
    public Rect getScreenBounds() {
        return mConfigChooser.getConfiguration().getScreenBounds();
    }

    /**
* Returns the scale to multiply pixels in the layout coordinate space with to obtain
* the corresponding dip (device independent pixel)
*
//Synthetic comment -- @@ -1518,7 +1509,6 @@
LayoutLibrary layoutLib) {
LayoutCanvas canvas = getCanvasControl();
Set<UiElementNode> explodeNodes = canvas.getNodesToExplode();
        Rect rect = getScreenBounds();
RenderLogger logger = new RenderLogger(mEditedFile.getName());
RenderingMode renderingMode = RenderingMode.NORMAL;
// FIXME set the rendering mode using ViewRule or something.
//Synthetic comment -- @@ -1530,7 +1520,6 @@

RenderSession session = RenderService.create(this)
.setModel(model)
            .setSize(rect.w, rect.h)
.setLog(logger)
.setRenderingMode(renderingMode)
.setIncludedWithin(mIncludedWithin)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..1df9d62

//Synthetic comment -- @@ -0,0 +1,131 @@








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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index ad4b94d..46168b7 100644

//Synthetic comment -- @@ -1000,14 +1000,11 @@
// This is important since when we fill the size of certain views (like
// a SeekBar), we want it to at most be the width of the screen, and for small
// screens the RENDER_WIDTH was wider.
                Rect screenBounds = editor.getScreenBounds();
                int renderWidth = Math.min(screenBounds.w, MAX_RENDER_WIDTH);
                int renderHeight = Math.min(screenBounds.h, MAX_RENDER_HEIGHT);
LayoutLog silentLogger = new LayoutLog();

session = RenderService.create(editor)
.setModel(model)
                    .setSize(renderWidth, renderHeight)
.setLog(silentLogger)
.setOverrideBgColor(overrideBgColor)
.setDecorations(false)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 5ca8e9c..c92ce81 100644

//Synthetic comment -- @@ -265,7 +265,7 @@

session = RenderService.create(editor)
.setModel(model)
                    .setSize(width, height)
.setRenderingMode(RenderingMode.FULL_EXPAND)
.setLog(new RenderLogger("palette"))
.setOverrideBgColor(overrideBgColor)
//Synthetic comment -- @@ -440,7 +440,7 @@
ResourceResolver resources = editor.getResourceResolver();
ResourceValue resourceValue = resources.findItemInTheme(themeItemName);
BufferedImage image = RenderService.create(editor)
            .setSize(100, 100)
.renderDrawable(resourceValue);
if (image != null) {
// Use the middle pixel as the color since that works better for gradients;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 4811235..07f640b 100644

//Synthetic comment -- @@ -28,7 +28,6 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
//Synthetic comment -- @@ -39,7 +38,6 @@
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -529,9 +527,7 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
FolderConfiguration config = mConfiguration.getFullConfig();
        RenderService renderService = RenderService.create(editor, config, resolver);
        ScreenSizeQualifier screenSize = config.getScreenSizeQualifier();
        renderService.setScreen(screenSize, mConfiguration.getXDpi(), mConfiguration.getYDpi());

if (mIncludedWithin != null) {
renderService.setIncludedWithin(mIncludedWithin);
//Synthetic comment -- @@ -569,8 +565,6 @@
} else {
renderService.setModel(editor.getModel());
}
        Rect rect = Configuration.getScreenBounds(config);
        renderService.setSize(rect.w, rect.h);
RenderLogger log = new RenderLogger(getDisplayName());
renderService.setLog(log);
RenderSession session = renderService.createRenderSession();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 2a1f3b7..065e327 100644

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
//Synthetic comment -- @@ -89,21 +83,14 @@
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

private UiDocumentNode mModel;
    private int mWidth = -1;
    private int mHeight = -1;
    private boolean mUseExplodeMode;
private Reference mIncludedWithin;
private RenderingMode mRenderingMode = RenderingMode.NORMAL;
private LayoutLog mLogger;
//Synthetic comment -- @@ -120,60 +107,37 @@
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
//Synthetic comment -- @@ -202,21 +166,6 @@
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
//Synthetic comment -- @@ -237,7 +186,7 @@
* @return a {@link RenderService} which can perform rendering services
*/
public static RenderService create(GraphicalEditorPart editor,
            FolderConfiguration configuration, ResourceResolver resolver) {
RenderService renderService = new RenderService(editor, configuration, resolver);

return renderService;
//Synthetic comment -- @@ -287,16 +236,34 @@
}

/**
     * Sets the width and height to be used during rendering (which might be adjusted if
* the {@link #setRenderingMode(RenderingMode)} is {@link RenderingMode#FULL_EXPAND}.
*
     * @param width the width in pixels of the layout to be rendered
     * @param height the height in pixels of the layout to be rendered
* @return this (such that chains of setters can be stringed together)
*/
    public RenderService setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
return this;
}

//Synthetic comment -- @@ -376,7 +343,7 @@
* @return the {@link RenderSession} resulting from rendering the current model
*/
public RenderSession createRenderSession() {
        assert mModel != null && mWidth != -1 && mHeight != -1 : "Incomplete service config";
finishConfiguration();

if (mResourceResolver == null) {
//Synthetic comment -- @@ -384,26 +351,10 @@
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
//Synthetic comment -- @@ -441,13 +392,11 @@
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
//Synthetic comment -- @@ -469,10 +418,6 @@
}
}

        if (mScreenSize != null) {
            params.setConfigScreenSize(mScreenSize.getValue());
        }

if (mOverrideBgColor != null) {
params.setOverrideBgColor(mOverrideBgColor.intValue());
}
//Synthetic comment -- @@ -508,8 +453,10 @@

finishConfiguration();

        DrawableParams params = new DrawableParams(drawableResourceValue, mProject, mWidth, mHeight,
                mDensity, mXdpi, mYdpi, mResourceResolver, mProjectCallback, mMinSdkVersion,
mTargetSdkVersion, mLogger);
params.setForceNoDecor();
Result result = mLayoutLib.renderDrawable(params);
//Synthetic comment -- @@ -534,14 +481,13 @@
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
//Synthetic comment -- @@ -577,13 +523,11 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java
//Synthetic comment -- index d2d7878..41d4cd1 100644

//Synthetic comment -- @@ -200,7 +200,7 @@
XmlProperty xmlProperty = (XmlProperty) property;
GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
RenderService service = RenderService.create(graphicalEditor);
                            service.setSize(SAMPLE_SIZE, SAMPLE_SIZE);
BufferedImage drawable = service.renderDrawable(resValue);
if (drawable != null) {
swtImage = SwtUtils.convertToSwt(gc.getDevice(), drawable,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
//Synthetic comment -- index eeaca0c..afd1df9 100644

//Synthetic comment -- @@ -166,7 +166,7 @@

if (image == null) {
RenderService renderService = RenderService.create(mEditor);
                            renderService.setSize(WIDTH, HEIGHT);
image = renderService.renderDrawable(drawable);
}
}








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
//Synthetic comment -- index c14e7f7..726d9c9 100644

//Synthetic comment -- @@ -18,12 +18,12 @@
import static com.android.ide.common.resources.configuration.LanguageQualifier.FAKE_LANG_VALUE;
import static com.android.ide.common.resources.configuration.RegionQualifier.FAKE_REGION_VALUE;

import com.android.ide.common.api.Rect;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.resources.Density;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.utils.StdLogger;

import java.lang.reflect.Constructor;
//Synthetic comment -- @@ -97,9 +97,9 @@
configuration.toPersistentString());

assertEquals(Density.MEDIUM, configuration.getDensity());
        assertEquals(145.0f, configuration.getXDpi(), 0.001);
        assertEquals(145.0f, configuration.getYDpi(), 0.001);
        assertEquals(new Rect(0, 0, 320, 480), configuration.getScreenBounds());
}

public void testCopy() throws Exception {








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
//Synthetic comment -- index 0000000..72a438a

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







