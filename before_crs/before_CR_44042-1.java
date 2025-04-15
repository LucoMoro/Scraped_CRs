/*Multi configuration editing bug fixes

Change-Id:Id7c544611cfd74ed4d6aa1f307321ae0522e2dff*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index fc81ac4..8afcdbb 100644

//Synthetic comment -- @@ -910,6 +910,7 @@
if (mGraphicalEditor != null) {
mGraphicalEditor.onTargetChange();
mGraphicalEditor.reloadPalette();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 09adc64..b9c7745 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import static com.android.SdkConstants.ANDROID_STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser.NAME_CONFIG_STATE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -156,10 +155,13 @@
public static Configuration create(@NonNull ConfigurationChooser chooser,
@NonNull IFile file) {
Configuration configuration = copy(chooser.getConfiguration());
        String data = AdtPlugin.getFileProperty(file, NAME_CONFIG_STATE);
        if (data != null) {
            configuration.initialize(data);
        } else {
ProjectResources resources = chooser.getResources();
ConfigurationMatcher matcher = new ConfigurationMatcher(chooser, configuration, file,
resources, false);
//Synthetic comment -- @@ -167,7 +169,7 @@
configuration.mEditedConfig = new FolderConfiguration();
}
matcher.adaptConfigSelection(true /*needBestMatch*/);
        }

return configuration;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index 38f2e67..ce78800 100644

//Synthetic comment -- @@ -488,7 +488,7 @@
*/
public void setFile(IFile file) {
mEditedFile = file;
        initializeConfiguration();
}

/**
//Synthetic comment -- @@ -656,6 +656,7 @@
try {
updateDevices();
updateTargets();
} finally {
mDisableUpdates--;
}
//Synthetic comment -- @@ -695,6 +696,7 @@
if (mSdkChanged) {
updateDevices();
updateTargets();
mSdkChanged = false;
}

//Synthetic comment -- @@ -709,6 +711,7 @@
if (mProjectTarget != null) {
targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mProjectTarget, null);
updateTargets();
}

if (targetStatus == LoadStatus.LOADED) {
//Synthetic comment -- @@ -730,7 +733,7 @@
targetData = Sdk.getCurrent().getTargetData(mProjectTarget);

// get the file stored state
                    initializeConfiguration();
boolean loadedConfigData = mConfiguration.getDevice() != null &&
mConfiguration.getDeviceState() != null;

//Synthetic comment -- @@ -851,8 +854,9 @@
return false;
}

    private void initializeConfiguration() {
        if (mConfiguration.getDevice() == null) {
String data = AdtPlugin.getFileProperty(mEditedFile, NAME_CONFIG_STATE);
if (mInitialState != null) {
data = mInitialState;
//Synthetic comment -- @@ -860,6 +864,7 @@
}
if (data != null) {
mConfiguration.initialize(data);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index 717347f..ad5bd52 100644

//Synthetic comment -- @@ -134,12 +134,6 @@
resizeScrollbar();
}

    public void setClientSize(int clientSize) {
        mClientSize = clientSize;
        mScrollbar.setPageIncrement(clientSize);
        resizeScrollbar();
    }

private void resizeScrollbar() {
// scaled image size
int sx = (int) (mScale * mFullSize);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 81fd8d2..bf64848 100644

//Synthetic comment -- @@ -82,6 +82,7 @@
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
//Synthetic comment -- @@ -120,7 +121,7 @@

private static final boolean DEBUG = false;

    /* package */ static final String PREFIX_CANVAS_ACTION = "canvas_action_";

/** The layout editor that uses this layout canvas. */
private final LayoutEditorDelegate mEditorDelegate;
//Synthetic comment -- @@ -387,9 +388,23 @@

if (clientWidth == 0) {
clientWidth = imageWidth;
}
if (clientHeight == 0) {
clientHeight = imageHeight;
}

mHScale.setSize(imageWidth, fullWidth, clientWidth);
//Synthetic comment -- @@ -414,8 +429,7 @@
Boolean zoomed = coordinator.isEditorMaximized();
if (mWasZoomed != zoomed) {
if (mWasZoomed != null) {
                            LayoutActionBar actionBar = mEditorDelegate.getGraphicalEditor()
                                    .getLayoutActionBar();
if (actionBar.isZoomingAllowed()) {
setFitScale(true /*onlyZoomOut*/, true /*allowZoomIn*/);
}
//Synthetic comment -- @@ -457,7 +471,7 @@
} else {
// Zooming actions
char c = e.character;
            LayoutActionBar actionBar = mEditorDelegate.getGraphicalEditor().getLayoutActionBar();
if (c == '1' && actionBar.isZoomingAllowed()) {
setScale(1, true);
} else if (c == '0' && actionBar.isZoomingAllowed()) {
//Synthetic comment -- @@ -572,12 +586,12 @@
}

/** Returns the Rules Engine, associated with the current project. */
    /* package */ RulesEngine getRulesEngine() {
return mRulesEngine;
}

/** Sets the Rules Engine, associated with the current project. */
    /* package */ void setRulesEngine(RulesEngine rulesEngine) {
mRulesEngine = rulesEngine;
}

//Synthetic comment -- @@ -585,7 +599,7 @@
* Returns the factory to use to convert from {@link CanvasViewInfo} or from
* {@link UiViewElementNode} to {@link INode} proxies.
*/
    /* package */ NodeFactory getNodeFactory() {
return mNodeFactory;
}

//Synthetic comment -- @@ -594,7 +608,7 @@
*
* @return The GCWrapper used to paint view rules
*/
    /* package */ GCWrapper getGcWrapper() {
return mGCWrapper;
}

//Synthetic comment -- @@ -652,7 +666,7 @@
* @return A {@link CanvasTransform} for mapping between layout and control
*         coordinates in the horizontal dimension.
*/
    /* package */ CanvasTransform getHorizontalTransform() {
return mHScale;
}

//Synthetic comment -- @@ -663,7 +677,7 @@
* @return A {@link CanvasTransform} for mapping between layout and control
*         coordinates in the vertical dimension.
*/
    /* package */ CanvasTransform getVerticalTransform() {
return mVScale;
}

//Synthetic comment -- @@ -711,6 +725,11 @@
return mSelectAllAction;
}

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
//Synthetic comment -- @@ -726,7 +745,7 @@
*            {@link #showInvisibleViews(boolean)}) where individual invisible nodes
*            are padded during certain interactions.
*/
    /* package */ void setSession(RenderSession session, Set<UiElementNode> explodedNodes,
boolean layoutlib5) {
// disable any hover
clearHover();
//Synthetic comment -- @@ -737,7 +756,7 @@
session.isAlphaChannelImage());

mOutlinePage.setModel(mViewHierarchy.getRoot());
            mEditorDelegate.getGraphicalEditor().setModel(mViewHierarchy.getRoot());

if (image != null) {
updateScrollBars();
//Synthetic comment -- @@ -765,8 +784,7 @@
void ensureZoomed() {
if (mZoomFitNextImage && getClientArea().height > 0) {
mZoomFitNextImage = false;
            LayoutActionBar actionBar = mEditorDelegate.getGraphicalEditor()
                    .getLayoutActionBar();
if (actionBar.isZoomingAllowed()) {
setFitScale(true, true /*allowZoomIn*/);
}
//Synthetic comment -- @@ -789,7 +807,7 @@
return mHScale.getScale();
}

    /* package */ void setScale(double scale, boolean redraw) {
if (scale <= 0.0) {
scale = 1.0;
}
//Synthetic comment -- @@ -896,7 +914,7 @@
* @param canvasY Y in the canvas coordinates
* @return A new {@link Point} in control client coordinates (not display coordinates)
*/
    /* package */ Point layoutToControlPoint(int canvasX, int canvasY) {
int x = mHScale.translate(canvasX);
int y = mVScale.translate(canvasY);
return new Point(x, y);
//Synthetic comment -- @@ -911,7 +929,7 @@
* <p/>
* Returns null if there's no action for the given id.
*/
    /* package */ IAction getAction(String actionId) {
String prefix = PREFIX_CANVAS_ACTION;
if (mMenuManager == null ||
actionId == null ||
//Synthetic comment -- @@ -997,7 +1015,7 @@
* @param show When true, any invisible parent nodes are padded and highlighted
*            ("exploded"), and when false any formerly exploded nodes are hidden.
*/
    /* package */ void showInvisibleViews(boolean show) {
if (mShowInvisible == show) {
return;
}
//Synthetic comment -- @@ -1058,14 +1076,14 @@
/**
* Clears the hover.
*/
    /* package */ void clearHover() {
mHoverOverlay.clearHover();
}

/**
* Hover on top of a known child.
*/
    /* package */ void hover(MouseEvent e) {
// Check if a button is pressed; no hovers during drags
if ((e.stateMask & SWT.BUTTON_MASK) != 0) {
clearHover();
//Synthetic comment -- @@ -1117,7 +1135,7 @@
* @param url The layout attribute url of the form @layout/foo
*/
private void showInclude(String url) {
        GraphicalEditorPart graphicalEditor = mEditorDelegate.getGraphicalEditor();
IPath filePath = graphicalEditor.findResourceFile(url);
if (filePath == null) {
// Should not be possible - if the URL had been bad, then we wouldn't
//Synthetic comment -- @@ -1217,7 +1235,7 @@
* @return the layout resource name of this layout
*/
public String getLayoutResourceName() {
        GraphicalEditorPart graphicalEditor = mEditorDelegate.getGraphicalEditor();
return graphicalEditor.getLayoutResourceName();
}

//Synthetic comment -- @@ -1228,7 +1246,7 @@
*/
/*
public String getMe() {
        GraphicalEditorPart graphicalEditor = mEditorDelegate.getGraphicalEditor();
IFile editedFile = graphicalEditor.getEditedFile();
return editedFile.getProjectRelativePath().toOSString();
}
//Synthetic comment -- @@ -1355,11 +1373,11 @@
copyActionAttributes(mSelectAllAction, ActionFactory.SELECT_ALL);
}

    /* package */ String getCutLabel() {
return mCutAction.getText();
}

    /* package */ String getDeleteLabel() {
// verb "Delete" from the DELETE action's title
return mDeleteAction.getText();
}
//Synthetic comment -- @@ -1374,7 +1392,7 @@
hasSelection = false;
}

        StyledText errorLabel = mEditorDelegate.getGraphicalEditor().getErrorLabel();
mCutAction.setEnabled(hasSelection);
mCopyAction.setEnabled(hasSelection || errorLabel.getSelectionCount() > 0);
mDeleteAction.setEnabled(hasSelection);
//Synthetic comment -- @@ -1517,7 +1535,7 @@
private void setupStaticMenuActions(IMenuManager manager) {
manager.removeAll();

        manager.add(new SelectionManager.SelectionMenu(mEditorDelegate.getGraphicalEditor()));
manager.add(new Separator());
manager.add(mCutAction);
manager.add(mCopyAction);
//Synthetic comment -- @@ -1545,7 +1563,7 @@
/**
* Deletes the selection. Equivalent to pressing the Delete key.
*/
    /* package */ void delete() {
mDeleteAction.run();
}

//Synthetic comment -- @@ -1564,7 +1582,7 @@
*            {@link ViewElementDescriptor} to add as root to the current
*            empty XML document.
*/
    /* package */ void createDocumentRoot(String rootFqcn) {

// Need a valid empty document to create the new root
final UiDocumentNode uiDoc = mEditorDelegate.getUiRootNode();
//Synthetic comment -- @@ -1618,8 +1636,7 @@
*/
public Margins getInsets(String fqcn) {
if (ViewMetadataRepository.INSETS_SUPPORTED) {
            ConfigurationChooser configComposite =
                    mEditorDelegate.getGraphicalEditor().getConfigurationChooser();
String theme = configComposite.getThemeName();
Density density = configComposite.getConfiguration().getDensity();
return ViewMetadataRepository.getInsets(fqcn, density, theme);
//Synthetic comment -- @@ -1676,7 +1693,8 @@

/** Ensures that the configuration previews are up to date for this canvas */
public void syncPreviewMode() {
        if (mImageOverlay != null && mImageOverlay.getImage() != null) {
if (mPreviewManager.recomputePreviews(false)) {
// Zoom when syncing modes
mZoomFitNextImage = true;







