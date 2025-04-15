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
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IPageImageProvider;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -134,20 +118,14 @@
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -1102,40 +1080,6 @@
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
    public RenderSession render(UiDocumentNode model, int width, int height,
            Set<UiElementNode> explodeNodes, Integer overrideBgColor, boolean noDecor,
            LayoutLog logger, RenderingMode renderingMode) {
        if (!ensureFileValid()) {
            return null;
        }
        if (!ensureModelValid(model)) {
            return null;
        }
        LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);

        IProject iProject = mEditedFile.getProject();
        return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                overrideBgColor, noDecor, logger, null /* includeWithin */, renderingMode);
    }

    /**
* Returns the {@link LayoutLibrary} associated with this editor, if it has
* been initialized already. May return null if it has not been initialized (or has
* not finished initializing).
//Synthetic comment -- @@ -1208,7 +1152,7 @@
*
* @return LayoutBridge the layout bridge for rendering this editor's scene
*/
    private LayoutLibrary getReadyLayoutLib(boolean displayError) {
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
IAndroidTarget target = getRenderingTarget();
//Synthetic comment -- @@ -1335,15 +1279,8 @@
LayoutLibrary layoutLib) {
LayoutCanvas canvas = getCanvasControl();
Set<UiElementNode> explodeNodes = canvas.getNodesToExplode();

        // Compute the layout
Rectangle rect = getScreenBounds();

        int width = rect.width;
        int height = rect.height;

RenderLogger logger = new RenderLogger(mEditedFile.getName());

RenderingMode renderingMode = RenderingMode.NORMAL;
// FIXME set the rendering mode using ViewRule or something.
List<UiElementNode> children = model.getUiChildren();
//Synthetic comment -- @@ -1352,9 +1289,14 @@
renderingMode = RenderingMode.V_SCROLL;
}

        RenderSession session = renderWithBridge(iProject, model, layoutLib, width, height,
                explodeNodes, null /*custom background*/, false /*no decorations*/, logger,
                mIncludedWithin, renderingMode);

boolean layoutlib5 = layoutLib.supports(Capability.EMBEDDED_LAYOUT);
canvas.setSession(session, explodeNodes, layoutlib5);
//Synthetic comment -- @@ -1395,290 +1337,6 @@
model.refreshUi();
}

    private RenderSession renderWithBridge(IProject project, UiDocumentNode model,
            LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
            Integer overrideBgColor, boolean noDecor, LayoutLog logger, Reference includeWithin,
            RenderingMode renderingMode) {
        ResourceManager resManager = ResourceManager.getInstance();

        ProjectResources projectRes = resManager.getProjectResources(project);
        if (projectRes == null) {
            displayError("Missing project resources.");
            return null;
        }

        // Lazily create the project callback the first time we need it
        if (mProjectCallback == null) {
            mProjectCallback = new ProjectCallback(layoutLib, projectRes, project);
        } else {
            // Also clears the set of missing/broken classes prior to rendering
            mProjectCallback.getMissingClasses().clear();
            mProjectCallback.getUninstantiatableClasses().clear();
        }

        if (mUseExplodeMode) {
            // compute how many padding in x and y will bump the screen size
            List<UiElementNode> children = model.getUiChildren();
            if (children.size() == 1) {
                ExplodedRenderingHelper helper = new ExplodedRenderingHelper(
                        children.get(0).getXmlNode(), project);

                // there are 2 paddings for each view
                // left and right, or top and bottom.
                int paddingValue = ExplodedRenderingHelper.PADDING_VALUE * 2;

                width += helper.getWidthPadding() * paddingValue;
                height += helper.getHeightPadding() * paddingValue;
            }
        }

        Density density = mConfigComposite.getDensity();
        float xdpi = mConfigComposite.getXDpi();
        float ydpi = mConfigComposite.getYDpi();

        UiElementPullParser modelParser = new UiElementPullParser(model,
                mUseExplodeMode, explodeNodes, density, xdpi, project);
        ILayoutPullParser topParser = modelParser;

        // Code to support editing included layout
        // first reset the layout parser just in case.
        mProjectCallback.setLayoutParser(null, null);

        if (includeWithin != null) {
            // Outer layout name:
            String contextLayoutName = includeWithin.getName();

            // Find the layout file.
            ResourceValue contextLayout = getResourceResolver().findResValue(
                    LAYOUT_PREFIX + contextLayoutName , false  /* forceFrameworkOnly*/);
            if (contextLayout != null) {
                File layoutFile = new File(contextLayout.getValue());
                if (layoutFile.isFile()) {
                    try {
                        // Get the name of the layout actually being edited, without the extension
                        // as it's what IXmlPullParser.getParser(String) will receive.
                        String queryLayoutName = getLayoutResourceName();
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

        ResourceResolver resolver = getResourceResolver();
        if (resolver == null) {
            // Abort the rendering if the resources are not found.
            return null;
        }

        SessionParams params = new SessionParams(
                topParser,
                renderingMode,
                project /* projectKey */,
                width, height,
                density, xdpi, ydpi,
                resolver,
                mProjectCallback,
                mMinSdkVersion,
                mTargetSdkVersion,
                logger);

        // Request margin and baseline information.
        // TODO: Be smarter about setting this; start without it, and on the first request
        // for an extended view info, re-render in the same session, and then set a flag
        // which will cause this to create extended view info each time from then on in the
        // same session
        params.setExtendedViewInfoMode(true);

        if (noDecor) {
            params.setForceNoDecor();
        } else {
            ManifestInfo manifestInfo = ManifestInfo.get(project);
            try {
                params.setAppLabel(manifestInfo.getApplicationLabel());
                params.setAppIcon(manifestInfo.getApplicationIcon());
            } catch (Exception e) {
                // ignore.
            }
        }

        ScreenSizeQualifier ssq = mConfigComposite.getCurrentConfig().getScreenSizeQualifier();
        if (ssq != null) {
            params.setConfigScreenSize(ssq.getValue());
        }

        if (overrideBgColor != null) {
            params.setOverrideBgColor(overrideBgColor.intValue());
        }

        // set the Image Overlay as the image factory.
        params.setImageFactory(getCanvasControl().getImageOverlay());

        try {
            mProjectCallback.setLogger(logger);
            return layoutLib.createSession(params);
        } catch (RuntimeException t) {
            // Exceptions from the bridge
            displayError(t.getLocalizedMessage());
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
     * @param width the width of the drawable to be rendered
     * @param height the height of the drawable to be rendered
     * @return the image, or null if something went wrong
     */
    BufferedImage renderThemeItem(String itemName, int width, int height) {
        ResourceResolver resources = getResourceResolver();
        LayoutLibrary layoutLibrary = getLayoutLibrary();
        IProject project = getProject();
        ResourceValue drawableResourceValue = resources.findItemInTheme(itemName);
        Density density = mConfigComposite.getDensity();
        float xdpi = mConfigComposite.getXDpi();
        float ydpi = mConfigComposite.getYDpi();
        ResourceManager resManager = ResourceManager.getInstance();
        ProjectResources projectRes = resManager.getProjectResources(project);
        ProjectCallback projectCallback = new ProjectCallback(layoutLibrary, projectRes, project);
        LayoutLog silentLogger = new LayoutLog();
        DrawableParams params = new DrawableParams(drawableResourceValue, project,
                width, height,
                density, xdpi, ydpi, resources, projectCallback,
                mMinSdkVersion, mTargetSdkVersion, silentLogger);
        params.setForceNoDecor();
        Result result = layoutLibrary.renderDrawable(params);
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
        int width = parent.getBounds().w;
        int height = parent.getBounds().h;

        ResourceResolver resources = getResourceResolver();
        LayoutLibrary layoutLibrary = getLayoutLibrary();
        IProject project = getProject();
        Density density = mConfigComposite.getDensity();
        float xdpi = mConfigComposite.getXDpi();
        float ydpi = mConfigComposite.getYDpi();
        ResourceManager resManager = ResourceManager.getInstance();
        ProjectResources projectRes = resManager.getProjectResources(project);
        // TODO - use mProjectCallback? If so restore logger after use
        ProjectCallback projectCallback = new ProjectCallback(layoutLibrary, projectRes, project);
        LayoutLog silentLogger = new LayoutLog();

        UiElementNode parentNode = ((NodeProxy) parent).getNode();
        final NodeFactory nodeFactory = getCanvasControl().getNodeFactory();
        UiElementPullParser topParser = new UiElementPullParser(parentNode,
                false, Collections.<UiElementNode>emptySet(), density, xdpi, project) {
            @Override
            public String getAttributeValue(String namespace, String localName) {
                if (filter != null) {
                    Object cookie = getViewCookie();
                    if (cookie instanceof UiViewElementNode) {
                        NodeProxy node = nodeFactory.create((UiViewElementNode) cookie);
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
                project /* projectKey */,
                width, height,
                density, xdpi, ydpi,
                resources,
                projectCallback,
                mMinSdkVersion,
                mTargetSdkVersion,
                silentLogger);
        params.setLayoutOnly();
        params.setForceNoDecor();

        RenderSession session = null;
        try {
            projectCallback.setLogger(silentLogger);
            session = layoutLibrary.createSession(params);
            if (session.getResult().isSuccess()) {
                assert session.getRootViews().size() == 1;
                ViewInfo root = session.getRootViews().get(0);
                List<ViewInfo> children = root.getChildren();
                Map<INode, Rect> map = new HashMap<INode, Rect>(children.size());
                NodeFactory factory = getCanvasControl().getNodeFactory();
                for (ViewInfo info : children) {
                    if (info.getCookie() instanceof UiViewElementNode) {
                        UiViewElementNode uiNode = (UiViewElementNode) info.getCookie();
                        NodeProxy node = factory.create(uiNode);
                        map.put(node, new Rect(info.getLeft(), info.getTop(),
                                info.getRight() - info.getLeft(),
                                info.getBottom() - info.getTop()));
                    }
                }

                return map;
            }
        } catch (RuntimeException t) {
            // Exceptions from the bridge
            displayError(t.getLocalizedMessage());
            throw t;
        } finally {
            projectCallback.setLogger(null);
            if (session != null) {
                session.dispose();
            }
        }

        return null;
    }

/**
* Returns the {@link ResourceResolver} for this editor
*
//Synthetic comment -- @@ -1719,6 +1377,23 @@
return mResourceResolver;
}

/**
* Returns the resource name of this layout, NOT including the @layout/ prefix
*
//Synthetic comment -- @@ -2488,4 +2163,22 @@
public LayoutActionBar getLayoutActionBar() {
return mActionBar;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 92ff59f..f4248b5 100755

//Synthetic comment -- @@ -30,7 +30,6 @@
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -982,9 +981,14 @@
int renderWidth = Math.min(screenBounds.width, MAX_RENDER_WIDTH);
int renderHeight = Math.min(screenBounds.height, MAX_RENDER_HEIGHT);
LayoutLog silentLogger = new LayoutLog();
                session = editor.render(model, renderWidth, renderHeight,
                    null /* explodeNodes */, overrideBgColor, true /*no decorations*/,
                    silentLogger, RenderingMode.NORMAL);
} catch (Throwable t) {
// Previews can fail for a variety of reasons -- let's not bug
// the user with it








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 473b00a..b2ebbf1 100644

//Synthetic comment -- @@ -26,7 +26,6 @@

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
//Synthetic comment -- @@ -69,7 +68,6 @@
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
//Synthetic comment -- @@ -266,18 +264,19 @@
RenderSession session = null;
NodeList childNodes = documentElement.getChildNodes();
try {
                LayoutLog logger = new RenderLogger("palette");
// Important to get these sizes large enough for clients that don't support
// RenderMode.FULL_EXPAND such as 1.6
int width = 200;
int height = childNodes.getLength() == 1 ? 400 : 1600;
                Set<UiElementNode> expandNodes = Collections.<UiElementNode>emptySet();
                RenderingMode renderingMode = RenderingMode.FULL_EXPAND;

                session = editor.render(model, width, height, expandNodes,
                        overrideBgColor, true /*no decorations*/, logger,
                        renderingMode);

} catch (Throwable t) {
// If there are internal errors previewing the components just revert to plain
// icons and labels
//Synthetic comment -- @@ -439,7 +438,9 @@
*/
private RGB renderDrawableResource(String themeItemName) {
GraphicalEditorPart editor = mPalette.getEditor();
        BufferedImage image = editor.renderThemeItem(themeItemName, 100, 100);
if (image != null) {
// Use the middle pixel as the color since that works better for gradients;
// solid colors work too.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
new file mode 100644
//Synthetic comment -- index 0000000..655097d

//Synthetic comment -- @@ -0,0 +1,513 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index a76845b..1e88b43 100755

//Synthetic comment -- @@ -45,6 +45,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GCWrapper;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -1211,7 +1212,8 @@

public Map<INode, Rect> measureChildren(INode parent,
IClientRulesEngine.AttributeFilter filter) {
            Map<INode, Rect> map = mEditor.measureChildren(parent, filter);
if (map == null) {
map = Collections.emptyMap();
}







