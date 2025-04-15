/*Update Layoutlib_api to use a class for h/w config.

Change-Id:Iead02d468590407ec274357f1a1c57ed8d5cc24c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 9553bc8..c89a81b 100644

//Synthetic comment -- @@ -79,7 +79,6 @@
private boolean mIncreaseExistingPadding = false;
private LayoutDescriptors mDescriptors;
private final Density mDensity;

/**
* Number of pixels to pad views with in exploded-rendering mode.
//Synthetic comment -- @@ -114,18 +113,16 @@
*            nodes are not individually exploded (but they may all be exploded with the
*            explodeRendering parameter.
* @param density the density factor for the screen.
* @param project Project containing this layout.
*/
public UiElementPullParser(UiElementNode top, boolean explodeRendering,
Set<UiElementNode> explodeNodes,
            Density density, IProject project) {
super();
mRoot = top;
mExplodedRendering = explodeRendering;
mExplodeNodes = explodeNodes;
mDensity = density;
if (mExplodedRendering) {
// get the layout descriptor
IAndroidTarget target = Sdk.getCurrent().getTarget(project);
//Synthetic comment -- @@ -631,13 +628,13 @@
f *= (float)mDensity.getDpiValue() / Density.DEFAULT_DENSITY;
break;
case COMPLEX_UNIT_PT:
                            f *= mDensity.getDpiValue() * (1.0f / 72);
break;
case COMPLEX_UNIT_IN:
                            f *= mDensity.getDpiValue();
break;
case COMPLEX_UNIT_MM:
                            f *= mDensity.getDpiValue() * (1.0f / 25.4f);
break;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index c0b57ac..00caccd 100644

//Synthetic comment -- @@ -22,7 +22,6 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.DensityQualifier;
//Synthetic comment -- @@ -31,8 +30,6 @@
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.ide.common.resources.configuration.NightModeQualifier;
import com.android.ide.common.resources.configuration.RegionQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
//Synthetic comment -- @@ -44,7 +41,6 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ScreenSize;
import com.android.resources.UiMode;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -954,96 +950,6 @@
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
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
//Synthetic comment -- @@ -1316,15 +1316,6 @@
}

/**
* Returns the scale to multiply pixels in the layout coordinate space with to obtain
* the corresponding dip (device independent pixel)
*
//Synthetic comment -- @@ -1518,7 +1509,6 @@
LayoutLibrary layoutLib) {
LayoutCanvas canvas = getCanvasControl();
Set<UiElementNode> explodeNodes = canvas.getNodesToExplode();
RenderLogger logger = new RenderLogger(mEditedFile.getName());
RenderingMode renderingMode = RenderingMode.NORMAL;
// FIXME set the rendering mode using ViewRule or something.
//Synthetic comment -- @@ -1530,7 +1520,6 @@

RenderSession session = RenderService.create(this)
.setModel(model)
.setLog(logger)
.setRenderingMode(renderingMode)
.setIncludedWithin(mIncludedWithin)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..1df9d62

//Synthetic comment -- @@ -0,0 +1,131 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.HardwareConfig;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.devices.ButtonType;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.Screen;

public class HardwareConfigHelper {

    private final Device mDevice;
    private ScreenOrientation mScreenOrientation = ScreenOrientation.PORTRAIT;

    // optional
    private int mMaxRenderWidth = -1;
    private int mMaxRenderHeight = -1;
    private int mOverrideRenderWidth = -1;
    private int mOverrideRenderHeight = -1;

    public HardwareConfigHelper(Device device) {
        mDevice = device;
    }

    public HardwareConfigHelper setOrientation(ScreenOrientation screenOrientation) {
        mScreenOrientation = screenOrientation;
        return this;
    }

    /**
     * Overrides the width and height to be used during rendering.
     *
     * A value of -1 will make the rendering use the normal width and height coming from the
     * {@link Device} object.
     *
     * @param overrideRenderWidth the width in pixels of the layout to be rendered
     * @param overrideRenderHeight the height in pixels of the layout to be rendered
     * @return this (such that chains of setters can be stringed together)
     */
    public HardwareConfigHelper setOverrideRenderSize(int overrideRenderWidth,
            int overrideRenderHeight) {
        mOverrideRenderWidth = overrideRenderWidth;
        mOverrideRenderHeight = overrideRenderHeight;
        return this;
    }

    /**
     * Sets the max width and height to be used during rendering.
     *
     * A value of -1 will make the rendering use the normal width and height coming from the
     * {@link Device} object.
     *
     * @param maxRenderWidth the max width in pixels of the layout to be rendered
     * @param maxRenderHeight the max height in pixels of the layout to be rendered
     * @return this (such that chains of setters can be stringed together)
     */
    public HardwareConfigHelper setMaxRenderSize(int maxRenderWidth, int maxRenderHeight) {
        mMaxRenderWidth = maxRenderWidth;
        mMaxRenderHeight = maxRenderHeight;
        return this;
    }


    public HardwareConfig getConfig() {
        Screen screen = mDevice.getDefaultHardware().getScreen();

        // compute width and height to take orientation into account.
        int x = screen.getXDimension();
        int y = screen.getYDimension();
        int width, height;

        if (x > y) {
            if (mScreenOrientation == ScreenOrientation.LANDSCAPE) {
                width = x;
                height = y;
            } else {
                width = y;
                height = x;
            }
        } else {
            if (mScreenOrientation == ScreenOrientation.LANDSCAPE) {
                width = y;
                height = x;
            } else {
                width = x;
                height = y;
            }
        }

        if (mOverrideRenderHeight != -1) {
            width = mOverrideRenderWidth;
        }

        if (mOverrideRenderHeight != -1) {
            height = mOverrideRenderHeight;
        }

        if (mMaxRenderWidth != -1) {
            width = mMaxRenderWidth;
        }

        if (mMaxRenderHeight != -1) {
            height = mMaxRenderHeight;
        }

        return new HardwareConfig(
                width,
                height,
                screen.getPixelDensity(),
                (float) screen.getXdpi(),
                (float) screen.getYdpi(),
                screen.getSize(),
                mScreenOrientation,
                mDevice.getDefaultHardware().getButtonType() == ButtonType.SOFT);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index d5b46b4..1b1bd23 100644

//Synthetic comment -- @@ -33,6 +33,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.Screen;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
import com.google.common.base.Strings;

//Synthetic comment -- @@ -708,7 +710,9 @@
// compute average dpi of X and Y
ConfigurationChooser chooser = mEditor.getConfigurationChooser();
Configuration config = chooser.getConfiguration();
        Device device = config.getDevice();
        Screen screen = device.getDefaultHardware().getScreen();
        double dpi = (screen.getXdpi() + screen.getYdpi()) / 2.;

// get the monitor dpi
float monitor = AdtPrefs.getPrefs().getMonitorDensity();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index ad4b94d..46168b7 100644

//Synthetic comment -- @@ -1000,14 +1000,11 @@
// This is important since when we fill the size of certain views (like
// a SeekBar), we want it to at most be the width of the screen, and for small
// screens the RENDER_WIDTH was wider.
LayoutLog silentLogger = new LayoutLog();

session = RenderService.create(editor)
.setModel(model)
                    .setMaxRenderSize(MAX_RENDER_WIDTH, MAX_RENDER_HEIGHT)
.setLog(silentLogger)
.setOverrideBgColor(overrideBgColor)
.setDecorations(false)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 5ca8e9c..c92ce81 100644

//Synthetic comment -- @@ -265,7 +265,7 @@

session = RenderService.create(editor)
.setModel(model)
                    .setOverrideRenderSize(width, height)
.setRenderingMode(RenderingMode.FULL_EXPAND)
.setLog(new RenderLogger("palette"))
.setOverrideBgColor(overrideBgColor)
//Synthetic comment -- @@ -440,7 +440,7 @@
ResourceResolver resources = editor.getResourceResolver();
ResourceValue resourceValue = resources.findItemInTheme(themeItemName);
BufferedImage image = RenderService.create(editor)
            .setOverrideRenderSize(100, 100)
.renderDrawable(resourceValue);
if (image != null) {
// Use the middle pixel as the color since that works better for gradients;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 4811235..07f640b 100644

//Synthetic comment -- @@ -28,7 +28,6 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
//Synthetic comment -- @@ -39,7 +38,6 @@
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -529,9 +527,7 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
FolderConfiguration config = mConfiguration.getFullConfig();
        RenderService renderService = RenderService.create(editor, mConfiguration, resolver);

if (mIncludedWithin != null) {
renderService.setIncludedWithin(mIncludedWithin);
//Synthetic comment -- @@ -569,8 +565,6 @@
} else {
renderService.setModel(editor.getModel());
}
RenderLogger log = new RenderLogger(getDisplayName());
renderService.setLog(log);
RenderSession session = renderService.createRenderSession();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 2a1f3b7..065e327 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.HardwareConfig;
import com.android.ide.common.rendering.api.IImageFactory;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
//Synthetic comment -- @@ -34,12 +35,9 @@
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration;
//Synthetic comment -- @@ -53,11 +51,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
//Synthetic comment -- @@ -89,21 +83,14 @@
private final ResourceResolver mResourceResolver;
private final int mMinSdkVersion;
private final int mTargetSdkVersion;
private final LayoutLibrary mLayoutLib;
private final IImageFactory mImageFactory;
    private final HardwareConfigHelper mHardwareConfigHelper;

// The following fields are optional or configurable using the various chained
// setters:

private UiDocumentNode mModel;
private Reference mIncludedWithin;
private RenderingMode mRenderingMode = RenderingMode.NORMAL;
private LayoutLog mLogger;
//Synthetic comment -- @@ -120,60 +107,37 @@
mImageFactory = canvas.getImageOverlay();
ConfigurationChooser chooser = editor.getConfigurationChooser();
Configuration config = chooser.getConfiguration();
        FolderConfiguration folderConfig = config.getFullConfig();

        mHardwareConfigHelper = new HardwareConfigHelper(config.getDevice());
        mHardwareConfigHelper.setOrientation(
                folderConfig.getScreenOrientationQualifier().getValue());

mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
mResourceResolver = editor.getResourceResolver();
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
}

    private RenderService(GraphicalEditorPart editor,
            Configuration configuration, ResourceResolver resourceResolver) {
mEditor = editor;

mProject = editor.getProject();
LayoutCanvas canvas = editor.getCanvasControl();
mImageFactory = canvas.getImageOverlay();
        FolderConfiguration folderConfig = configuration.getFullConfig();

        mHardwareConfigHelper = new HardwareConfigHelper(configuration.getDevice());
        mHardwareConfigHelper.setOrientation(
                folderConfig.getScreenOrientationQualifier().getValue());

mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
mResourceResolver =  resourceResolver != null ? resourceResolver : editor.getResourceResolver();
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
}

/**
//Synthetic comment -- @@ -202,21 +166,6 @@
}

/**
* Creates a new {@link RenderService} associated with the given editor.
*
* @param editor the editor to provide configuration data such as the render target
//Synthetic comment -- @@ -237,7 +186,7 @@
* @return a {@link RenderService} which can perform rendering services
*/
public static RenderService create(GraphicalEditorPart editor,
            Configuration configuration, ResourceResolver resolver) {
RenderService renderService = new RenderService(editor, configuration, resolver);

return renderService;
//Synthetic comment -- @@ -287,16 +236,34 @@
}

/**
     * Overrides the width and height to be used during rendering (which might be adjusted if
* the {@link #setRenderingMode(RenderingMode)} is {@link RenderingMode#FULL_EXPAND}.
*
     * A value of -1 will make the rendering use the normal width and height coming from the
     * {@link Configuration#getDevice()} object.
     *
     * @param overrideRenderWidth the width in pixels of the layout to be rendered
     * @param overrideRenderHeight the height in pixels of the layout to be rendered
* @return this (such that chains of setters can be stringed together)
*/
    public RenderService setOverrideRenderSize(int overrideRenderWidth, int overrideRenderHeight) {
        mHardwareConfigHelper.setOverrideRenderSize(overrideRenderWidth, overrideRenderHeight);
        return this;
    }

    /**
     * Sets the max width and height to be used during rendering (which might be adjusted if
     * the {@link #setRenderingMode(RenderingMode)} is {@link RenderingMode#FULL_EXPAND}.
     *
     * A value of -1 will make the rendering use the normal width and height coming from the
     * {@link Configuration#getDevice()} object.
     *
     * @param maxRenderWidth the max width in pixels of the layout to be rendered
     * @param maxRenderHeight the max height in pixels of the layout to be rendered
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setMaxRenderSize(int maxRenderWidth, int maxRenderHeight) {
        mHardwareConfigHelper.setMaxRenderSize(maxRenderWidth, maxRenderHeight);
return this;
}

//Synthetic comment -- @@ -376,7 +343,7 @@
* @return the {@link RenderSession} resulting from rendering the current model
*/
public RenderSession createRenderSession() {
        assert mModel != null : "Incomplete service config";
finishConfiguration();

if (mResourceResolver == null) {
//Synthetic comment -- @@ -384,26 +351,10 @@
return null;
}

        HardwareConfig hardwareConfig = mHardwareConfigHelper.getConfig();

UiElementPullParser modelParser = new UiElementPullParser(mModel,
                false, mExpandNodes, hardwareConfig.getDensity(), mProject);
ILayoutPullParser topParser = modelParser;

// Code to support editing included layout
//Synthetic comment -- @@ -441,13 +392,11 @@
topParser,
mRenderingMode,
mProject /* projectKey */,
                hardwareConfig,
mResourceResolver,
mProjectCallback,
mMinSdkVersion,
mTargetSdkVersion,
mLogger);

// Request margin and baseline information.
//Synthetic comment -- @@ -469,10 +418,6 @@
}
}

if (mOverrideBgColor != null) {
params.setOverrideBgColor(mOverrideBgColor.intValue());
}
//Synthetic comment -- @@ -508,8 +453,10 @@

finishConfiguration();

        HardwareConfig hardwareConfig = mHardwareConfigHelper.getConfig();

        DrawableParams params = new DrawableParams(drawableResourceValue, mProject, hardwareConfig,
                mResourceResolver, mProjectCallback, mMinSdkVersion,
mTargetSdkVersion, mLogger);
params.setForceNoDecor();
Result result = mLayoutLib.renderDrawable(params);
//Synthetic comment -- @@ -534,14 +481,13 @@
public Map<INode, Rect> measureChildren(INode parent,
final IClientRulesEngine.AttributeFilter filter) {
finishConfiguration();
        HardwareConfig hardwareConfig = mHardwareConfigHelper.getConfig();

final NodeFactory mNodeFactory = mEditor.getCanvasControl().getNodeFactory();
UiElementNode parentNode = ((NodeProxy) parent).getNode();
UiElementPullParser topParser = new UiElementPullParser(parentNode,
                false, Collections.<UiElementNode>emptySet(), hardwareConfig.getDensity(),
                mProject) {
@Override
public String getAttributeValue(String namespace, String localName) {
if (filter != null) {
//Synthetic comment -- @@ -577,13 +523,11 @@
topParser,
RenderingMode.FULL_EXPAND,
mProject /* projectKey */,
                hardwareConfig,
mResourceResolver,
mProjectCallback,
mMinSdkVersion,
mTargetSdkVersion,
mLogger);
params.setLayoutOnly();
params.setForceNoDecor();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java
//Synthetic comment -- index d2d7878..41d4cd1 100644

//Synthetic comment -- @@ -200,7 +200,7 @@
XmlProperty xmlProperty = (XmlProperty) property;
GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
RenderService service = RenderService.create(graphicalEditor);
                            service.setOverrideRenderSize(SAMPLE_SIZE, SAMPLE_SIZE);
BufferedImage drawable = service.renderDrawable(resValue);
if (drawable != null) {
swtImage = SwtUtils.convertToSwt(gc.getDevice(), drawable,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
//Synthetic comment -- index eeaca0c..afd1df9 100644

//Synthetic comment -- @@ -166,7 +166,7 @@

if (image == null) {
RenderService renderService = RenderService.create(mEditor);
                            renderService.setOverrideRenderSize(WIDTH, HEIGHT);
image = renderService.renderDrawable(drawable);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 7a6eef4..2b364a5 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.SdkConstants;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.HardwareConfig;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderSession;
//Synthetic comment -- @@ -250,20 +251,25 @@
configuredProject, configuredFramework,
"Theme", false /*isProjectTheme*/);

            HardwareConfig hardwareConfig = new HardwareConfig(
320,
480,
Density.MEDIUM,
160, //xdpi
160, // ydpi
                    ScreenSize.NORMAL,
                    ScreenOrientation.PORTRAIT,
                    false /*software buttons */);

            RenderSession session = layoutLib.createSession(new SessionParams(
                    parser,
                    RenderingMode.NORMAL,
                    null /*projectKey*/,
                    hardwareConfig,
resolver,
projectCallBack,
1, // minSdkVersion
1, // targetSdkVersion
null //logger
));









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index 998c166..5cac663 100644

//Synthetic comment -- @@ -170,7 +170,6 @@
false, // explodedView
null, // explodeNodes
Density.MEDIUM, // density (default from ConfigurationComposite)
null // iProject
);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationTest.java
//Synthetic comment -- index c14e7f7..726d9c9 100644

//Synthetic comment -- @@ -18,12 +18,12 @@
import static com.android.ide.common.resources.configuration.LanguageQualifier.FAKE_LANG_VALUE;
import static com.android.ide.common.resources.configuration.RegionQualifier.FAKE_REGION_VALUE;

import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.LanguageQualifier;
import com.android.resources.Density;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Screen;
import com.android.utils.StdLogger;

import java.lang.reflect.Constructor;
//Synthetic comment -- @@ -97,9 +97,9 @@
configuration.toPersistentString());

assertEquals(Density.MEDIUM, configuration.getDensity());
        Screen screen = configuration.getDevice().getDefaultHardware().getScreen();
        assertEquals(145.0f, screen.getXdpi(), 0.001);
        assertEquals(145.0f, screen.getYdpi(), 0.001);
}

public void testCopy() throws Exception {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java
//Synthetic comment -- index 346d67d..b566ad3 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.rendering.api;


/**
* Rendering parameters for {@link Bridge#renderDrawable(DrawableParams)}
//Synthetic comment -- @@ -32,11 +31,7 @@
*
* @param drawable the {@link ResourceValue} identifying the drawable.
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
    * @param hardwareConfig the {@link HardwareConfig}.
* @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
//Synthetic comment -- @@ -47,13 +42,12 @@
public DrawableParams(
ResourceValue drawable,
Object projectKey,
            HardwareConfig hardwareConfig,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
LayoutLog log) {
        super(projectKey, hardwareConfig,
renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);
mDrawable = drawable;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/HardwareConfig.java b/layoutlib_api/src/com/android/ide/common/rendering/api/HardwareConfig.java
new file mode 100644
//Synthetic comment -- index 0000000..72a438a

//Synthetic comment -- @@ -0,0 +1,91 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenSize;

/**
 * Hardware configuration for the rendering.
 * This is immutable.
 *
 * @since 9
 */
public class HardwareConfig {

    private final int mScreenWidth;
    private final int mScreenHeight;
    private final Density mDensity;
    private final float mXdpi;
    private final float mYdpi;
    private final ScreenOrientation mOrientation;
    private ScreenSize mScreenSize;

    private final boolean mSoftwareButtons;

    public HardwareConfig(
            int screenWidth,
            int screenHeight,
            Density density,
            float xdpi,
            float ydpi,
            ScreenSize screenSize,
            ScreenOrientation orientation,
            boolean softwareButtons) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mDensity = density;
        mXdpi = xdpi;
        mYdpi = ydpi;
        mScreenSize = screenSize;
        mOrientation = orientation;
        mSoftwareButtons = softwareButtons;
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

    public ScreenSize getScreenSize() {
        return mScreenSize;
    }

    public ScreenOrientation getOrientation() {
        return mOrientation;
    }

    public boolean hasSoftwareButtons() {
        return mSoftwareButtons;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index f89dcfe..2e53f14 100644

//Synthetic comment -- @@ -29,11 +29,7 @@
public final static long DEFAULT_TIMEOUT = 250; //ms

private final Object mProjectKey;
    private final HardwareConfig mHardwareConfig;
private final RenderResources mRenderResources;
private final IProjectCallback mProjectCallback;
private final int mMinSdkVersion;
//Synthetic comment -- @@ -46,7 +42,6 @@

private IImageFactory mImageFactory = null;

private String mAppIcon = null;
private String mAppLabel = null;
private String mLocale = null;
//Synthetic comment -- @@ -55,11 +50,7 @@
/**
*
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
     * @param hardwareConfig the {@link HardwareConfig}.
* @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
//Synthetic comment -- @@ -69,18 +60,13 @@
*/
public RenderParams(
Object projectKey,
            HardwareConfig hardwareConfig,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
LayoutLog log) {
mProjectKey = projectKey;
        mHardwareConfig = hardwareConfig;
mRenderResources = renderResources;
mProjectCallback = projectCallback;
mMinSdkVersion = minSdkVersion;
//Synthetic comment -- @@ -95,11 +81,7 @@
*/
public RenderParams(RenderParams params) {
mProjectKey = params.mProjectKey;
        mHardwareConfig = params.mHardwareConfig;
mRenderResources = params.mRenderResources;
mProjectCallback = params.mProjectCallback;
mMinSdkVersion = params.mMinSdkVersion;
//Synthetic comment -- @@ -109,7 +91,6 @@
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
mImageFactory = params.mImageFactory;
mAppIcon = params.mAppIcon;
mAppLabel = params.mAppLabel;
mLocale = params.mLocale;
//Synthetic comment -- @@ -129,10 +110,6 @@
mImageFactory = imageFactory;
}

public void setAppIcon(String appIcon) {
mAppIcon = appIcon;
}
//Synthetic comment -- @@ -153,6 +130,10 @@
return mProjectKey;
}

    public HardwareConfig getHardwareConfig() {
        return mHardwareConfig;
    }

public int getMinSdkVersion() {
return mMinSdkVersion;
}
//Synthetic comment -- @@ -161,24 +142,44 @@
return mTargetSdkVersion;
}

    /**
     * @deprecated Use {@link #getHardwareConfig()}
     */
    @Deprecated
public int getScreenWidth() {
        return mHardwareConfig.getScreenWidth();
}

    /**
     * @deprecated Use {@link #getHardwareConfig()}
     */
    @Deprecated
public int getScreenHeight() {
        return mHardwareConfig.getScreenHeight();
}

    /**
     * @deprecated Use {@link #getHardwareConfig()}
     */
    @Deprecated
public Density getDensity() {
        return mHardwareConfig.getDensity();
}

    /**
     * @deprecated Use {@link #getHardwareConfig()}
     */
    @Deprecated
public float getXdpi() {
        return mHardwareConfig.getXdpi();
}

    /**
     * @deprecated Use {@link #getHardwareConfig()}
     */
    @Deprecated
public float getYdpi() {
        return mHardwareConfig.getYdpi();
}

public RenderResources getResources() {
//Synthetic comment -- @@ -209,8 +210,12 @@
return mImageFactory;
}

    /**
     * @deprecated Use {@link #getHardwareConfig()}
     */
    @Deprecated
public ScreenSize getConfigScreenSize() {
        return mHardwareConfig.getScreenSize();
}

public String getAppIcon() {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index e3edbd2..f440de1 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.ide.common.rendering.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
//Synthetic comment -- @@ -52,7 +50,6 @@

private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;
private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;
private boolean mExtendedViewInfoMode = false;
//Synthetic comment -- @@ -80,26 +77,22 @@
ILayoutPullParser layoutDescription,
RenderingMode renderingMode,
Object projectKey,
            HardwareConfig hardwareConfig,
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
LayoutLog log) {
        super(projectKey, hardwareConfig,
renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);

mLayoutDescription = layoutDescription;
mRenderingMode = renderingMode;
}

public SessionParams(SessionParams params) {
super(params);
mLayoutDescription = params.mLayoutDescription;
mRenderingMode = params.mRenderingMode;
if (params.mAdapterBindingMap != null) {
mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>(
params.mAdapterBindingMap);
//Synthetic comment -- @@ -115,10 +108,6 @@
return mRenderingMode;
}

public void setLayoutOnly() {
mLayoutOnly = true;
}







