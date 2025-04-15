/*Make ADT work with the new layoutlib API.

Change-Id:I62e3f3adc3de4cf680439c6e8bac6ac4a6b50cdb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 7485b6d..39a1a60 100755

//Synthetic comment -- @@ -22,8 +22,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
//Synthetic comment -- @@ -77,34 +76,46 @@
*
* @param viewInfo The root of the {@link ILayoutViewInfo} hierarchy.
*/
public CanvasViewInfo(ILayoutViewInfo viewInfo) {
this(viewInfo, null /*parent*/, 0 /*parentX*/, 0 /*parentY*/);
}

private CanvasViewInfo(ILayoutViewInfo viewInfo, CanvasViewInfo parent,
int parentX, int parentY) {
        mParent = parent;
        mName = viewInfo.getName();

        // The ILayoutViewInfo#getViewKey() method returns a key which depends on the
        // IXmlPullParser used to parse the layout files. In this case, the parser is
        // guaranteed to be an UiElementPullParser, which creates keys that are of type
        // UiViewElementNode.
        // We'll simply crash if the type is not right, as this is not supposed to happen
        // and nothing could work if there's a type mismatch.
        mUiViewKey  = (UiViewElementNode) viewInfo.getViewKey();

        int x = viewInfo.getLeft();
        int y = viewInfo.getTop();
        int w = viewInfo.getRight() - x;
        int h = viewInfo.getBottom() - y;

        if (parent != null) {
            x += parentX;
            y += parentY;
        }

        mAbsRect = new Rectangle(x, y, w - 1, h - 1);

if (viewInfo.getChildren() != null) {
for (ILayoutViewInfo child : viewInfo.getChildren()) {
//Synthetic comment -- @@ -112,10 +123,37 @@
// We can't interact with those when they have a null key or
// an incompatible type.
if (child.getViewKey() instanceof UiViewElementNode) {
                    mChildren.add(new CanvasViewInfo(child, this, x, y));
}
}
}

// adjust selection bounds for views which are too small to select









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index b0d5ed9..5d59802 100755

//Synthetic comment -- @@ -47,10 +47,12 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutBridge;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
//Synthetic comment -- @@ -1066,7 +1068,7 @@
// For that purpose, create a special ILayoutResult that has no image,
// no root view yet indicates success and then update the canvas with it.

                    ILayoutResult result = new ILayoutResult() {
public String getErrorMessage() {
return null;
}
//Synthetic comment -- @@ -1080,7 +1082,7 @@
}

public int getSuccess() {
                            return ILayoutResult.SUCCESS;
}
};

//Synthetic comment -- @@ -1215,23 +1217,52 @@
UiElementPullParser parser = new UiElementPullParser(getModel(),
mUseExplodeMode, explodeNodes, density, xdpi, iProject);

        ILayoutResult result = computeLayout(bridge, parser,
                iProject /* projectKey */,
                width, height, !mClippingButton.getSelection(),
                density, xdpi, ydpi,
                theme, isProjectTheme,
                configuredProjectRes, frameworkResources, mProjectCallback,
                mLogger);

        // post rendering clean up
        bridge.cleanUp();

        canvas.setResult(result, explodeNodes);

// update the UiElementNode with the layout info.
        if (result.getSuccess() != ILayoutResult.SUCCESS) {
// An error was generated. Print it.
            displayError(result.getErrorMessage());

} else {
// Success means there was no exception. But we might have detected
//Synthetic comment -- @@ -1257,7 +1288,7 @@
* allocated ILayoutResult.
*/
@SuppressWarnings("deprecation")
    private static ILayoutResult computeLayout(LayoutBridge bridge,
IXmlPullParser layoutDescription, Object projectKey,
int screenWidth, int screenHeight, boolean renderFullSize,
int density, float xdpi, float ydpi,
//Synthetic comment -- @@ -1267,6 +1298,8 @@
IProjectCallback projectCallback, ILayoutLog logger) {

if (bridge.apiLevel >= ILayoutBridge.API_CURRENT) {
// newest API with support for "render full height"
// TODO: link boolean to UI.
return bridge.bridge.computeLayout(layoutDescription,
//Synthetic comment -- @@ -1305,6 +1338,34 @@
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
import com.android.layoutlib.api.ILayoutResult;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.action.Action;
//Synthetic comment -- @@ -448,7 +448,9 @@
*            {@link #showInvisibleViews(boolean)}) where individual invisible nodes
*            are padded during certain interactions.
*/
    /* package */ void setResult(ILayoutResult result, Set<UiElementNode> explodedNodes) {
// disable any hover
clearHover();

//Synthetic comment -- @@ -472,6 +474,46 @@
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
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -109,12 +110,15 @@
*            {@link LayoutCanvas#showInvisibleViews}) where individual invisible
*            nodes are padded during certain interactions.
*/
    /* package */ void setResult(ILayoutResult result, Set<UiElementNode> explodedNodes) {
        mIsResultValid = (result != null && result.getSuccess() == ILayoutResult.SUCCESS);
mExplodedParents = false;

if (mIsResultValid && result != null) {
            ILayoutViewInfo root = result.getRootView();
if (root == null) {
mLastValidViewInfoRoot = null;
} else {
//Synthetic comment -- @@ -139,6 +143,50 @@
}

/**
* Creates or updates the node proxy for this canvas view info.
* <p/>
* Since proxies are reused, this will update the bounds of an existing proxy when the







