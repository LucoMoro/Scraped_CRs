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
//Synthetic comment -- index c0b57ac..f9a8ff1 100644

//Synthetic comment -- @@ -953,49 +953,6 @@
return Density.MEDIUM;
}


/**
* Returns the bounds of the screen








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..0799e35

//Synthetic comment -- @@ -0,0 +1,52 @@
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

    public HardwareConfigHelper(Device device) {
        mDevice = device;
    }

    public HardwareConfigHelper setOrientation(ScreenOrientation screenOrientation) {
        mScreenOrientation = screenOrientation;
        return this;
    }

    public HardwareConfig getConfig() {
        Screen screen = mDevice.getDefaultHardware().getScreen();

        return new HardwareConfig(
                screen.getXDimension(),
                screen.getYDimension(),
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 4811235..e298b3e 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -529,9 +528,7 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
FolderConfiguration config = mConfiguration.getFullConfig();
        RenderService renderService = RenderService.create(editor, mConfiguration, resolver);

if (mIncludedWithin != null) {
renderService.setIncludedWithin(mIncludedWithin);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 2a1f3b7..8776b75 100644

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
//Synthetic comment -- @@ -89,13 +83,9 @@
private final ResourceResolver mResourceResolver;
private final int mMinSdkVersion;
private final int mTargetSdkVersion;
private final LayoutLibrary mLayoutLib;
private final IImageFactory mImageFactory;
    private final HardwareConfigHelper mHardwareConfigHelper;

// The following fields are optional or configurable using the various chained
// setters:
//Synthetic comment -- @@ -103,7 +93,6 @@
private UiDocumentNode mModel;
private int mWidth = -1;
private int mHeight = -1;
private Reference mIncludedWithin;
private RenderingMode mRenderingMode = RenderingMode.NORMAL;
private LayoutLog mLogger;
//Synthetic comment -- @@ -120,60 +109,37 @@
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
//Synthetic comment -- @@ -202,21 +168,6 @@
}

/**
* Creates a new {@link RenderService} associated with the given editor.
*
* @param editor the editor to provide configuration data such as the render target
//Synthetic comment -- @@ -237,7 +188,7 @@
* @return a {@link RenderService} which can perform rendering services
*/
public static RenderService create(GraphicalEditorPart editor,
            Configuration configuration, ResourceResolver resolver) {
RenderService renderService = new RenderService(editor, configuration, resolver);

return renderService;
//Synthetic comment -- @@ -384,26 +335,10 @@
return null;
}

        HardwareConfig hardwareConfig = mHardwareConfigHelper.getConfig();

UiElementPullParser modelParser = new UiElementPullParser(mModel,
                false, mExpandNodes, hardwareConfig.getDensity(), mProject);
ILayoutPullParser topParser = modelParser;

// Code to support editing included layout
//Synthetic comment -- @@ -441,13 +376,11 @@
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
//Synthetic comment -- @@ -469,10 +402,6 @@
}
}

if (mOverrideBgColor != null) {
params.setOverrideBgColor(mOverrideBgColor.intValue());
}
//Synthetic comment -- @@ -508,8 +437,10 @@

finishConfiguration();

        HardwareConfig hardwareConfig = mHardwareConfigHelper.getConfig();

        DrawableParams params = new DrawableParams(drawableResourceValue, mProject, hardwareConfig,
                mResourceResolver, mProjectCallback, mMinSdkVersion,
mTargetSdkVersion, mLogger);
params.setForceNoDecor();
Result result = mLayoutLib.renderDrawable(params);
//Synthetic comment -- @@ -534,14 +465,13 @@
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
//Synthetic comment -- @@ -577,13 +507,11 @@
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
//Synthetic comment -- index c14e7f7..6ad87f9 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.resources.Density;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Screen;
import com.android.utils.StdLogger;

import java.lang.reflect.Constructor;
//Synthetic comment -- @@ -97,8 +98,9 @@
configuration.toPersistentString());

assertEquals(Density.MEDIUM, configuration.getDensity());
        Screen screen = configuration.getDevice().getDefaultHardware().getScreen();
        assertEquals(145.0f, screen.getXdpi(), 0.001);
        assertEquals(145.0f, screen.getYdpi(), 0.001);
assertEquals(new Rect(0, 0, 320, 480), configuration.getScreenBounds());
}









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
//Synthetic comment -- index 0000000..18b0dd7

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

    public boolean isSoftwareButtons() {
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







