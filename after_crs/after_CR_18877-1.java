/*Make ADT work with the new layoutlib API.

Change-Id:I62e3f3adc3de4cf680439c6e8bac6ac4a6b50cdb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 7485b6d..39a1a60 100755

//Synthetic comment -- @@ -22,8 +22,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutViewInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
//Synthetic comment -- @@ -77,34 +76,46 @@
*
* @param viewInfo The root of the {@link ILayoutViewInfo} hierarchy.
*/
    @Deprecated
    public CanvasViewInfo(com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo viewInfo) {
        this(viewInfo, null /*parent*/, 0 /*parentX*/, 0 /*parentY*/);
    }

    @Deprecated
    private CanvasViewInfo(com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo viewInfo,
            CanvasViewInfo parent, int parentX, int parentY) {
        this(viewInfo.getName(), viewInfo.getViewKey(),
                viewInfo.getLeft(), viewInfo.getTop(), viewInfo.getRight(), viewInfo.getBottom(),
                parent, parentX, parentY);

        if (viewInfo.getChildren() != null) {
            for (com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo child : viewInfo.getChildren()) {
                // Only use children which have a ViewKey of the correct type.
                // We can't interact with those when they have a null key or
                // an incompatible type.
                if (child.getViewKey() instanceof UiViewElementNode) {
                    mChildren.add(new CanvasViewInfo(child, this,
                            viewInfo.getLeft(), viewInfo.getTop()));
                }
            }
        }
    }

    /**
     * Constructs a {@link CanvasViewInfo} hierarchy based on a given {@link ILayoutViewInfo}
     * hierarchy. This call is recursive and builds a full tree.
     *
     * @param viewInfo The root of the {@link ILayoutViewInfo} hierarchy.
     */
public CanvasViewInfo(ILayoutViewInfo viewInfo) {
this(viewInfo, null /*parent*/, 0 /*parentX*/, 0 /*parentY*/);
}

private CanvasViewInfo(ILayoutViewInfo viewInfo, CanvasViewInfo parent,
int parentX, int parentY) {
        this(viewInfo.getClassName(), viewInfo.getViewKey(),
                viewInfo.getLeft(), viewInfo.getTop(), viewInfo.getRight(), viewInfo.getBottom(),
                parent, parentX, parentY);

if (viewInfo.getChildren() != null) {
for (ILayoutViewInfo child : viewInfo.getChildren()) {
//Synthetic comment -- @@ -112,10 +123,37 @@
// We can't interact with those when they have a null key or
// an incompatible type.
if (child.getViewKey() instanceof UiViewElementNode) {
                    mChildren.add(new CanvasViewInfo(child, this,
                            viewInfo.getLeft(), viewInfo.getTop()));
}
}
}
    }

    private CanvasViewInfo(String name, Object viewKey, int l, int t, int r, int b,
            CanvasViewInfo parent, int parentX, int parentY) {
        mParent = parent;
        mName = name;

        // The ILayoutViewInfo#getViewKey() method returns a key which depends on the
        // IXmlPullParser used to parse the layout files. In this case, the parser is
        // guaranteed to be an UiElementPullParser, which creates keys that are of type
        // UiViewElementNode.
        // We'll simply crash if the type is not right, as this is not supposed to happen
        // and nothing could work if there's a type mismatch.
        mUiViewKey  = (UiViewElementNode) viewKey;

        int x = l;
        int y = t;
        int w = r - x;
        int h = b - y;

        if (parent != null) {
            x += parentX;
            y += parentY;
        }

        mAbsRect = new Rectangle(x, y, w - 1, h - 1);

// adjust selection bounds for views which are too small to select









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index b0d5ed9..5d59802 100755

//Synthetic comment -- @@ -47,10 +47,12 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.ILayoutScene;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.ILayoutScene.ILayoutResult;
import com.android.layoutlib.api.ILayoutScene.LayoutStatus;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
//Synthetic comment -- @@ -1066,7 +1068,7 @@
// For that purpose, create a special ILayoutResult that has no image,
// no root view yet indicates success and then update the canvas with it.

                    com.android.layoutlib.api.ILayoutResult result = new com.android.layoutlib.api.ILayoutResult() {
public String getErrorMessage() {
return null;
}
//Synthetic comment -- @@ -1080,7 +1082,7 @@
}

public int getSuccess() {
                            return com.android.layoutlib.api.ILayoutResult.SUCCESS;
}
};

//Synthetic comment -- @@ -1215,23 +1217,52 @@
UiElementPullParser parser = new UiElementPullParser(getModel(),
mUseExplodeMode, explodeNodes, density, xdpi, iProject);

        String errorMessage = null; // null means success
        if (bridge.apiLevel <= 4) {
            com.android.layoutlib.api.ILayoutResult result = computeLayout(bridge, parser,
                    iProject /* projectKey */,
                    width, height, !mClippingButton.getSelection(),
                    density, xdpi, ydpi,
                    theme, isProjectTheme,
                    configuredProjectRes, frameworkResources, mProjectCallback,
                    mLogger);

            // post rendering clean up
            bridge.cleanUp();

            canvas.setResult(result, explodeNodes);

            if (result.getSuccess() != com.android.layoutlib.api.ILayoutResult.SUCCESS) {
                errorMessage = result.getErrorMessage();
                if (errorMessage == null) {
                    errorMessage = "error";
                }
            }
        } else {
            ILayoutScene scene = startLayout(bridge, parser,
                    iProject /* projectKey */,
                    width, height, !mClippingButton.getSelection(),
                    density, xdpi, ydpi,
                    theme, isProjectTheme,
                    configuredProjectRes, frameworkResources, mProjectCallback,
                    mLogger);

            canvas.setResult(scene, explodeNodes);

            ILayoutResult result = scene.getResult();
            if (result.getStatus() != LayoutStatus.SUCCESS) {
                errorMessage = result.getErrorMessage();
                if (errorMessage == null) {
                    errorMessage = "error";
                }
            }

        }

// update the UiElementNode with the layout info.
        if (errorMessage != null) {
// An error was generated. Print it.
            displayError(errorMessage);

} else {
// Success means there was no exception. But we might have detected
//Synthetic comment -- @@ -1257,7 +1288,7 @@
* allocated ILayoutResult.
*/
@SuppressWarnings("deprecation")
    private static com.android.layoutlib.api.ILayoutResult computeLayout(LayoutBridge bridge,
IXmlPullParser layoutDescription, Object projectKey,
int screenWidth, int screenHeight, boolean renderFullSize,
int density, float xdpi, float ydpi,
//Synthetic comment -- @@ -1267,6 +1298,8 @@
IProjectCallback projectCallback, ILayoutLog logger) {

if (bridge.apiLevel >= ILayoutBridge.API_CURRENT) {
            throw new IllegalArgumentException("startLayout should be called for bridge @ API 5");
        } else if (bridge.apiLevel == 4) {
// newest API with support for "render full height"
// TODO: link boolean to UI.
return bridge.bridge.computeLayout(layoutDescription,
//Synthetic comment -- @@ -1305,6 +1338,34 @@
}
}

    /**
     * Computes a layout by calling the correct computeLayout method of ILayoutBridge based on
     * the implementation API level.
     *
     * Implementation detail: the bridge's computeLayout() method already returns a newly
     * allocated ILayoutResult.
     */
    private static ILayoutScene startLayout(LayoutBridge bridge,
            IXmlPullParser layoutDescription, Object projectKey,
            int screenWidth, int screenHeight, boolean renderFullSize,
            int density, float xdpi, float ydpi,
            String themeName, boolean isProjectTheme,
            Map<String, Map<String, IResourceValue>> projectResources,
            Map<String, Map<String, IResourceValue>> frameworkResources,
            IProjectCallback projectCallback, ILayoutLog logger) {

        if (bridge.apiLevel <= 4) {
            throw new IllegalArgumentException("computeLayout should be called for bridge @ API < 5");
        } else {
            return bridge.bridge.startLayout(layoutDescription,
                    projectKey, screenWidth, screenHeight, renderFullSize,
                    density, xdpi, ydpi,
                    themeName, isProjectTheme,
                    projectResources, frameworkResources, projectCallback,
                    logger);
        }
    }

public Rectangle getBounds() {
return mConfigComposite.getScreenBounds();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 1c0c798..53e397d 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutScene;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.action.Action;
//Synthetic comment -- @@ -448,7 +448,9 @@
*            {@link #showInvisibleViews(boolean)}) where individual invisible nodes
*            are padded during certain interactions.
*/
    @Deprecated
    /* package */ void setResult(com.android.layoutlib.api.ILayoutResult result,
            Set<UiElementNode> explodedNodes) {
// disable any hover
clearHover();

//Synthetic comment -- @@ -472,6 +474,46 @@
redraw();
}

    /**
     * Sets the result of the layout rendering. The result object indicates if the layout
     * rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
     *
     * Implementation detail: the bridge's computeLayout() method already returns a newly
     * allocated ILayourResult. That means we can keep this result and hold on to it
     * when it is valid.
     *
     * @param result The new rendering result, either valid or not.
     * @param explodedNodes The set of individual nodes the layout computer was asked to
     *            explode. Note that these are independent of the explode-all mode where
     *            all views are exploded; this is used only for the mode (
     *            {@link #showInvisibleViews(boolean)}) where individual invisible nodes
     *            are padded during certain interactions.
     */
    /* package */ void setResult(ILayoutScene scene, Set<UiElementNode> explodedNodes) {
        // disable any hover
        clearHover();

        mViewHierarchy.setResult(scene, explodedNodes);
        if (mViewHierarchy.isValid()) {
            Image image = mImageOverlay.setImage(scene.getImage());

            mOutlinePage.setModel(mViewHierarchy.getRoot());

            if (image != null) {
                mHScale.setSize(image.getImageData().width, getClientArea().width);
                mVScale.setSize(image.getImageData().height, getClientArea().height);
            }

            // Pre-load the android.view.View rule in the Rules Engine. Doing it here means
            // it will be done after the first rendering is finished. Successive calls are
            // superfluous but harmless since the rule will be cached.
            mRulesEngine.preloadAndroidView();
        }

        redraw();
    }


/* package */ void setShowOutline(boolean newState) {
mShowOutline = newState;
redraw();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 8c39662..cfea41a 100644

//Synthetic comment -- @@ -20,8 +20,9 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutScene;
import com.android.layoutlib.api.ILayoutViewInfo;
import com.android.layoutlib.api.ILayoutScene.LayoutStatus;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -109,12 +110,15 @@
*            {@link LayoutCanvas#showInvisibleViews}) where individual invisible
*            nodes are padded during certain interactions.
*/
    @Deprecated
    /* package */ void setResult(com.android.layoutlib.api.ILayoutResult result,
            Set<UiElementNode> explodedNodes) {
        mIsResultValid = (result != null &&
                result.getSuccess() == com.android.layoutlib.api.ILayoutResult.SUCCESS);
mExplodedParents = false;

if (mIsResultValid && result != null) {
            com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo root = result.getRootView();
if (root == null) {
mLastValidViewInfoRoot = null;
} else {
//Synthetic comment -- @@ -139,6 +143,50 @@
}

/**
     * Sets the result of the layout rendering. The result object indicates if the layout
     * rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
     *
     * Implementation detail: the bridge's computeLayout() method already returns a newly
     * allocated ILayourResult. That means we can keep this result and hold on to it
     * when it is valid.
     *
     * @param result The new rendering result, either valid or not.
     * @param explodedNodes The set of individual nodes the layout computer was asked to
     *            explode. Note that these are independent of the explode-all mode where
     *            all views are exploded; this is used only for the mode (
     *            {@link LayoutCanvas#showInvisibleViews}) where individual invisible
     *            nodes are padded during certain interactions.
     */
    /* package */ void setResult(ILayoutScene scene, Set<UiElementNode> explodedNodes) {
        mIsResultValid = (scene != null && scene.getResult().getStatus() == LayoutStatus.SUCCESS);
        mExplodedParents = false;

        if (mIsResultValid && scene != null) {
            ILayoutViewInfo root = scene.getRootView();
            if (root == null) {
                mLastValidViewInfoRoot = null;
            } else {
                mLastValidViewInfoRoot = new CanvasViewInfo(scene.getRootView());
            }

            updateNodeProxies(mLastValidViewInfoRoot);

            // Update the data structures related to tracking invisible and exploded nodes.
            // We need to find the {@link CanvasViewInfo} objects that correspond to
            // the passed in {@link UiElementNode} keys that were re-rendered, and mark
            // them as exploded and store them in a list for rendering.
            mExplodedParents = false;
            mInvisibleParents.clear();
            addInvisibleParents(mLastValidViewInfoRoot, explodedNodes);

            // Update the selection
            mCanvas.getSelectionManager().sync(mLastValidViewInfoRoot);
        } else {
            mInvisibleParents.clear();
        }
    }

    /**
* Creates or updates the node proxy for this canvas view info.
* <p/>
* Since proxies are reused, this will update the bounds of an existing proxy when the







