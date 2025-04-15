/*Move rendering code into RenderService class

This CL moves the various rendering-related code in
GraphicalEditorPart into a separate RenderService class, which can be
configured for different purposes:

- Rendering a layout shown in the canvas
- Rendering palette previews
- Rendering a preview of a node during a drag from a palette
- Rendering a theme drawable
- Rendering layout-only to measure preferred sizes

Once configured, the rendering service can be used repeatedly and off
the UI thread to for example render all the palette previews without
blocking editor startup. This will be addressed in a follow-up CL.

Change-Id:I851148d80c3a4dc9e4b5b66c9838ae49809ea03c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index efbfe59..88624bc 100644

//Synthetic comment -- @@ -17,53 +17,37 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_STRING_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IPageImageProvider;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -134,20 +118,14 @@
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -1102,40 +1080,6 @@
}

/**
* Returns the {@link LayoutLibrary} associated with this editor, if it has
* been initialized already. May return null if it has not been initialized (or has
* not finished initializing).
//Synthetic comment -- @@ -1208,7 +1152,7 @@
*
* @return LayoutBridge the layout bridge for rendering this editor's scene
*/
    LayoutLibrary getReadyLayoutLib(boolean displayError) {
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
IAndroidTarget target = getRenderingTarget();
//Synthetic comment -- @@ -1335,15 +1279,8 @@
LayoutLibrary layoutLib) {
LayoutCanvas canvas = getCanvasControl();
Set<UiElementNode> explodeNodes = canvas.getNodesToExplode();
Rectangle rect = getScreenBounds();
RenderLogger logger = new RenderLogger(mEditedFile.getName());
RenderingMode renderingMode = RenderingMode.NORMAL;
// FIXME set the rendering mode using ViewRule or something.
List<UiElementNode> children = model.getUiChildren();
//Synthetic comment -- @@ -1352,9 +1289,14 @@
renderingMode = RenderingMode.V_SCROLL;
}

        RenderSession session = RenderService.create(this)
            .setModel(model)
            .setSize(rect.width, rect.height)
            .setLog(logger)
            .setRenderingMode(renderingMode)
            .setIncludedWithin(mIncludedWithin)
            .setNodesToExpand(explodeNodes)
            .createRenderSession();

boolean layoutlib5 = layoutLib.supports(Capability.EMBEDDED_LAYOUT);
canvas.setSession(session, explodeNodes, layoutlib5);
//Synthetic comment -- @@ -1395,290 +1337,6 @@
model.refreshUi();
}

/**
* Returns the {@link ResourceResolver} for this editor
*
//Synthetic comment -- @@ -1719,6 +1377,23 @@
return mResourceResolver;
}

    /** Returns a project callback, and optionally resets it */
    ProjectCallback getProjectCallback(boolean reset, LayoutLibrary layoutLibrary) {
        // Lazily create the project callback the first time we need it
        if (mProjectCallback == null) {
            ResourceManager resManager = ResourceManager.getInstance();
            IProject project = getProject();
            ProjectResources projectRes = resManager.getProjectResources(project);
            mProjectCallback = new ProjectCallback(layoutLibrary, projectRes, project);
        } else if (reset) {
            // Also clears the set of missing/broken classes prior to rendering
            mProjectCallback.getMissingClasses().clear();
            mProjectCallback.getUninstantiatableClasses().clear();
        }

        return mProjectCallback;
    }

/**
* Returns the resource name of this layout, NOT including the @layout/ prefix
*
//Synthetic comment -- @@ -2488,4 +2163,22 @@
public LayoutActionBar getLayoutActionBar() {
return mActionBar;
}

    /**
     * Returns the target SDK version
     *
     * @return the target SDK version
     */
    public int getTargetSdkVersion() {
        return mTargetSdkVersion;
    }

    /**
     * Returns the minimum SDK version
     *
     * @return the minimum SDK version
     */
    public int getMinSdkVersion() {
        return mMinSdkVersion;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 92ff59f..f4248b5 100755

//Synthetic comment -- @@ -30,7 +30,6 @@
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -982,9 +981,14 @@
int renderWidth = Math.min(screenBounds.width, MAX_RENDER_WIDTH);
int renderHeight = Math.min(screenBounds.height, MAX_RENDER_HEIGHT);
LayoutLog silentLogger = new LayoutLog();

                session = RenderService.create(editor)
                    .setModel(model)
                    .setSize(renderWidth, renderHeight)
                    .setLog(silentLogger)
                    .setOverrideBgColor(overrideBgColor)
                    .setDecorations(false)
                    .createRenderSession();
} catch (Throwable t) {
// Previews can fail for a variety of reasons -- let's not bug
// the user with it








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 473b00a..b2ebbf1 100644

//Synthetic comment -- @@ -26,7 +26,6 @@

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
//Synthetic comment -- @@ -69,7 +68,6 @@
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
//Synthetic comment -- @@ -266,18 +264,19 @@
RenderSession session = null;
NodeList childNodes = documentElement.getChildNodes();
try {
// Important to get these sizes large enough for clients that don't support
// RenderMode.FULL_EXPAND such as 1.6
int width = 200;
int height = childNodes.getLength() == 1 ? 400 : 1600;

                session = RenderService.create(editor)
                    .setModel(model)
                    .setSize(width, height)
                    .setRenderingMode(RenderingMode.FULL_EXPAND)
                    .setLog(new RenderLogger("palette"))
                    .setOverrideBgColor(overrideBgColor)
                    .setDecorations(false)
                    .createRenderSession();
} catch (Throwable t) {
// If there are internal errors previewing the components just revert to plain
// icons and labels
//Synthetic comment -- @@ -439,7 +438,9 @@
*/
private RGB renderDrawableResource(String themeItemName) {
GraphicalEditorPart editor = mPalette.getEditor();
        BufferedImage image = RenderService.create(editor)
            .setSize(100, 100)
            .renderThemeItem(themeItemName);
if (image != null) {
// Use the middle pixel as the color since that works better for gradients;
// solid colors work too.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
new file mode 100644
//Synthetic comment -- index 0000000..655097d

//Synthetic comment -- @@ -0,0 +1,513 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.IImageFactory;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.Density;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The {@link RenderService} provides rendering and layout information for
 * Android layouts. This is a wrapper around the layout library.
 */
public class RenderService {
    /** Reference to the file being edited. Can also be used to access the {@link IProject}. */
    private final GraphicalEditorPart mEditor;

    // The following fields are inferred from the editor and not customizable by the
    // client of the render service:

    private final IProject mProject;
    private final ProjectCallback mProjectCallback;
    private final ResourceResolver mResourceResolver;
    private final int mMinSdkVersion;
    private final int mTargetSdkVersion;
    private final LayoutLibrary mLayoutLib;
    private final IImageFactory mImageFactory;
    private final Density mDensity;
    private final float mXdpi;
    private final float mYdpi;
    private final ScreenSizeQualifier mScreenSize;

    // The following fields are optional or configurable using the various chained
    // setters:

    private UiDocumentNode mModel;
    private int mWidth = -1;
    private int mHeight = -1;
    private boolean mUseExplodeMode;
    private Reference mIncludedWithin;
    private RenderingMode mRenderingMode = RenderingMode.NORMAL;
    private LayoutLog mLogger;
    private Integer mOverrideBgColor;
    private boolean mShowDecorations = true;
    private Set<UiElementNode> mExpandNodes = Collections.<UiElementNode>emptySet();

    /** Use the {@link #create} factory instead */
    private RenderService(GraphicalEditorPart editor) {
        mEditor = editor;

        mProject = editor.getProject();
        LayoutCanvas canvas = editor.getCanvasControl();
        mImageFactory = canvas.getImageOverlay();
        ConfigurationComposite config = editor.getConfigurationComposite();
        mDensity = config.getDensity();
        mXdpi = config.getXDpi();
        mYdpi = config.getYDpi();
        mScreenSize = config.getCurrentConfig().getScreenSizeQualifier();
        mLayoutLib = editor.getReadyLayoutLib(true /*displayError*/);
        mResourceResolver = editor.getResourceResolver();
        mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
        mMinSdkVersion = editor.getMinSdkVersion();
        mTargetSdkVersion = editor.getTargetSdkVersion();
    }

    /**
     * Creates a new {@link RenderService} associated with the given editor.
     *
     * @param editor the editor to provide configuration data such as the render target
     * @return a {@link RenderService} which can perform rendering services
     */
    public static RenderService create(GraphicalEditorPart editor) {
        RenderService renderService = new RenderService(editor);

        return renderService;
    }

    /**
     * Renders the given model, using this editor's theme and screen settings, and returns
     * the result as a {@link RenderSession}.
     *
     * @param model the model to be rendered, which can be different than the editor's own
     *            {@link #getModel()}.
     * @param width the width to use for the layout, or -1 to use the width of the screen
     *            associated with this editor
     * @param height the height to use for the layout, or -1 to use the height of the screen
     *            associated with this editor
     * @param explodeNodes a set of nodes to explode, or null for none
     * @param overrideBgColor If non-null, use the given color as a background to render over
     *        rather than the normal background requested by the theme
     * @param noDecor If true, don't draw window decorations like the system bar
     * @param logger a logger where rendering errors are reported
     * @param renderingMode the {@link RenderingMode} to use for rendering
     * @return the resulting rendered image wrapped in an {@link RenderSession}
     */

    /**
     * Sets the {@link LayoutLog} to be used during rendering. If none is specified, a
     * silent logger will be used.
     *
     * @param logger the log to be used
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setLog(LayoutLog logger) {
        mLogger = logger;
        return this;
    }

    /**
     * Sets the model to be rendered, which can be different than the editor's own
     * {@link GraphicalEditorPart#getModel()}.
     *
     * @param model the model to be rendered
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setModel(UiDocumentNode model) {
        mModel = model;
        return this;
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

    /**
     * Sets the {@link RenderingMode} to be used during rendering. If none is specified,
     * the default is {@link RenderingMode#NORMAL}.
     *
     * @param renderingMode the rendering mode to be used
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setRenderingMode(RenderingMode renderingMode) {
        mRenderingMode = renderingMode;
        return this;
    }

    /**
     * Sets the overriding background color to be used, if any. The color should be a
     * bitmask of AARRGGBB. The default is null.
     *
     * @param overrideBgColor the overriding background color to be used in the rendering,
     *            in the form of a AARRGGBB bitmask, or null to use no custom background.
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setOverrideBgColor(Integer overrideBgColor) {
        mOverrideBgColor = overrideBgColor;
        return this;
    }

    /**
     * Sets whether the rendering should include decorations such as a system bar, an
     * application bar etc depending on the SDK target and theme. The default is true.
     *
     * @param showDecorations true if the rendering should include system bars etc.
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setDecorations(boolean showDecorations) {
        mShowDecorations = showDecorations;
        return this;
    }

    /**
     * Sets the nodes to expand during rendering. These will be padded with approximately
     * 20 pixels and also highlighted by the {@link EmptyViewsOverlay}. The default is an
     * empty collection.
     *
     * @param nodesToExpand the nodes to be expanded
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setNodesToExpand(Set<UiElementNode> nodesToExpand) {
        mExpandNodes = nodesToExpand;
        return this;
    }

    /**
     * Sets the {@link Reference} to an outer layout that this layout should be rendered
     * within. The outer layout <b>must</b> contain an include tag which points to this
     * layout. The default is null.
     *
     * @param includedWithin a reference to an outer layout to render this layout within
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setIncludedWithin(Reference includedWithin) {
        mIncludedWithin = includedWithin;
        return this;
    }

    /** Initializes any remaining optional fields after all setters have been called */
    private void finishConfiguration() {
        if (mLogger == null) {
            // Silent logging
            mLogger = new LayoutLog();
        }
    }

    /**
     * Renders the model and returns the result as a {@link RenderSession}.
     * @return the {@link RenderSession} resulting from rendering the current model
     */
    public RenderSession createRenderSession() {
        assert mModel != null && mWidth != -1 && mHeight != -1 : "Incomplete service config";
        finishConfiguration();

        if (mResourceResolver == null) {
            // Abort the rendering if the resources are not found.
            return null;
        }

        ProjectResources projectRes = ResourceManager.getInstance().getProjectResources(mProject);
        if (projectRes == null) {
            mLogger.error(null, "Missing project resources.", null);
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
        // first reset the layout parser just in case.
        mProjectCallback.setLayoutParser(null, null);

        if (mIncludedWithin != null) {
            // Outer layout name:
            String contextLayoutName = mIncludedWithin.getName();

            // Find the layout file.
            ResourceValue contextLayout = mResourceResolver.findResValue(
                    LAYOUT_PREFIX + contextLayoutName , false  /* forceFrameworkOnly*/);
            if (contextLayout != null) {
                File layoutFile = new File(contextLayout.getValue());
                if (layoutFile.isFile()) {
                    try {
                        // Get the name of the layout actually being edited, without the extension
                        // as it's what IXmlPullParser.getParser(String) will receive.
                        String queryLayoutName = mEditor.getLayoutResourceName();
                        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
                        topParser = new ContextPullParser(mProjectCallback);
                        topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        topParser.setInput(new FileReader(layoutFile));
                    } catch (XmlPullParserException e) {
                        AdtPlugin.log(e, ""); //$NON-NLS-1$
                    } catch (FileNotFoundException e) {
                        // this will not happen since we check above.
                    }
                }
            }
        }

        SessionParams params = new SessionParams(
                topParser,
                mRenderingMode,
                mProject /* projectKey */,
                width, height,
                mDensity, mXdpi, mYdpi,
                mResourceResolver,
                mProjectCallback,
                mMinSdkVersion,
                mTargetSdkVersion,
                mLogger);

        // Request margin and baseline information.
        // TODO: Be smarter about setting this; start without it, and on the first request
        // for an extended view info, re-render in the same session, and then set a flag
        // which will cause this to create extended view info each time from then on in the
        // same session
        params.setExtendedViewInfoMode(true);

        if (!mShowDecorations) {
            params.setForceNoDecor();
        } else {
            ManifestInfo manifestInfo = ManifestInfo.get(mProject);
            try {
                params.setAppLabel(manifestInfo.getApplicationLabel());
                params.setAppIcon(manifestInfo.getApplicationIcon());
            } catch (Exception e) {
                // ignore.
            }
        }

        if (mScreenSize != null) {
            params.setConfigScreenSize(mScreenSize.getValue());
        }

        if (mOverrideBgColor != null) {
            params.setOverrideBgColor(mOverrideBgColor.intValue());
        }

        // set the Image Overlay as the image factory.
        params.setImageFactory(mImageFactory);

        try {
            mProjectCallback.setLogger(mLogger);
            return mLayoutLib.createSession(params);
        } catch (RuntimeException t) {
            // Exceptions from the bridge
            mLogger.error(null, t.getLocalizedMessage(), t, null);
            throw t;
        } finally {
            mProjectCallback.setLogger(null);
        }
    }

    /**
     * Renders the given resource which should refer to a drawable and returns it
     * as an image
     *
     * @param itemName the theme item to be looked up and rendered
     * @return the image, or null if something went wrong
     */
    public BufferedImage renderThemeItem(String itemName) {
        finishConfiguration();

        ResourceValue drawableResourceValue = mResourceResolver.findItemInTheme(itemName);
        DrawableParams params = new DrawableParams(drawableResourceValue, mProject, mWidth, mHeight,
                mDensity, mXdpi, mYdpi, mResourceResolver, mProjectCallback, mMinSdkVersion,
                mTargetSdkVersion, mLogger);
        params.setForceNoDecor();
        Result result = mLayoutLib.renderDrawable(params);
        if (result != null && result.isSuccess()) {
            Object data = result.getData();
            if (data instanceof BufferedImage) {
                return (BufferedImage) data;
            }
        }

        return null;
    }

    /**
     * Measure the children of the given parent node, applying the given filter to the
     * pull parser's attribute values.
     *
     * @param parent the parent node to measure children for
     * @param filter the filter to apply to the attribute values
     * @return a map from node children of the parent to new bounds of the nodes
     */
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
                    Object cookie = getViewCookie();
                    if (cookie instanceof UiViewElementNode) {
                        NodeProxy node = mNodeFactory.create((UiViewElementNode) cookie);
                        if (node != null) {
                            String value = filter.getAttribute(node, namespace, localName);
                            if (value != null) {
                                return value;
                            }
                            // null means no preference, not "unset".
                        }
                    }
                }

                return super.getAttributeValue(namespace, localName);
            }

            /**
             * The parser usually assumes that the top level node is a document node that
             * should be skipped, and that's not the case when we render in the middle of
             * the tree, so override {@link UiElementPullParser#onNextFromStartDocument}
             * to change this behavior
             */
            @Override
            public void onNextFromStartDocument() {
                mParsingState = START_TAG;
            }
        };

        SessionParams params = new SessionParams(
                topParser,
                RenderingMode.FULL_EXPAND,
                mProject /* projectKey */,
                width, height,
                mDensity, mXdpi, mYdpi,
                mResourceResolver,
                mProjectCallback,
                mMinSdkVersion,
                mTargetSdkVersion,
                mLogger);
        params.setLayoutOnly();
        params.setForceNoDecor();

        RenderSession session = null;
        try {
            mProjectCallback.setLogger(mLogger);
            session = mLayoutLib.createSession(params);
            if (session.getResult().isSuccess()) {
                assert session.getRootViews().size() == 1;
                ViewInfo root = session.getRootViews().get(0);
                List<ViewInfo> children = root.getChildren();
                Map<INode, Rect> map = new HashMap<INode, Rect>(children.size());
                for (ViewInfo info : children) {
                    if (info.getCookie() instanceof UiViewElementNode) {
                        UiViewElementNode uiNode = (UiViewElementNode) info.getCookie();
                        NodeProxy node = mNodeFactory.create(uiNode);
                        map.put(node, new Rect(info.getLeft(), info.getTop(),
                                info.getRight() - info.getLeft(),
                                info.getBottom() - info.getTop()));
                    }
                }

                return map;
            }
        } catch (RuntimeException t) {
            // Exceptions from the bridge
            mLogger.error(null, t.getLocalizedMessage(), t, null);
            throw t;
        } finally {
            mProjectCallback.setLogger(null);
            if (session != null) {
                session.dispose();
            }
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index a76845b..1e88b43 100755

//Synthetic comment -- @@ -45,6 +45,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GCWrapper;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -1211,7 +1212,8 @@

public Map<INode, Rect> measureChildren(INode parent,
IClientRulesEngine.AttributeFilter filter) {
            RenderService renderService = RenderService.create(mEditor);
            Map<INode, Rect> map = renderService.measureChildren(parent, filter);
if (map == null) {
map = Collections.emptyMap();
}







