/*Multi configuration editing bug fixes

Change-Id:Id7c544611cfd74ed4d6aa1f307321ae0522e2dff*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonMatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonMatchingStrategy.java
//Synthetic comment -- index 55d463b..224c28f 100755

//Synthetic comment -- @@ -47,23 +47,35 @@
IFile file = fileInput.getFile();
if (file.getParent().getName().startsWith(FD_RES_LAYOUT)) {
ResourceFolder resFolder = ResourceManager.getInstance().getResourceFolder(file);
                if (resFolder != null && resFolder.getType() == ResourceFolderType.LAYOUT) {
                    if (AdtPrefs.getPrefs().isSharedLayoutEditor()) {
                        LayoutEditorMatchingStrategy m = new LayoutEditorMatchingStrategy();
                        return m.matches(editorRef, fileInput);
                    } else {
                        // Skip files that don't match by name (see below). However, for
                        // layout files we can't just use editorRef.getName(), since
                        // the name sometimes includes the parent folder name (when the
                        // files are in layout- folders.
                        if (!(editorRef.getName().endsWith(file.getName()) &&
                                editorRef.getId().equals(CommonXmlEditor.ID))) {
                            return false;
                        }
                    }
                }
            } else {
                // Per the IEditorMatchingStrategy documentation, editorRef.getEditorInput()
                // is expensive so try exclude files that definitely don't match, such
                // as those with the wrong extension or wrong file name
                if (!(file.getName().equals(editorRef.getName()) &&
                        editorRef.getId().equals(CommonXmlEditor.ID))) {
                    return false;
}
}

            try {
                return input.equals(editorRef.getEditorInput());
            } catch (PartInitException e) {
                AdtPlugin.log(e, null);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index fc81ac4..a07e55e 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintRunner;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -753,7 +754,8 @@
@Override
public String delegateGetPartName() {
IEditorInput editorInput = getEditor().getEditorInput();
        if (!AdtPrefs.getPrefs().isSharedLayoutEditor()
              && editorInput instanceof IFileEditorInput) {
IFileEditorInput fileInput = (IFileEditorInput) editorInput;
IFile file = fileInput.getFile();
IContainer parent = file.getParent();
//Synthetic comment -- @@ -910,6 +912,7 @@
if (mGraphicalEditor != null) {
mGraphicalEditor.onTargetChange();
mGraphicalEditor.reloadPalette();
            mGraphicalEditor.getCanvasControl().syncPreviewMode();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 09adc64..b9c7745 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import static com.android.SdkConstants.ANDROID_STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -156,10 +155,13 @@
public static Configuration create(@NonNull ConfigurationChooser chooser,
@NonNull IFile file) {
Configuration configuration = copy(chooser.getConfiguration());
        // Ideally, we'd pick the configuration the user has most recently configured
        // for the outer layout. But this doesn't always work as expected, so for now
        // always compute the best fit.
        //String data = AdtPlugin.getFileProperty(file, NAME_CONFIG_STATE);
        //if (data != null) {
        //    configuration.initialize(data);
        //} else {
ProjectResources resources = chooser.getResources();
ConfigurationMatcher matcher = new ConfigurationMatcher(chooser, configuration, file,
resources, false);
//Synthetic comment -- @@ -167,7 +169,7 @@
configuration.mEditedConfig = new FolderConfiguration();
}
matcher.adaptConfigSelection(true /*needBestMatch*/);
        //}

return configuration;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index 38f2e67..ce78800 100644

//Synthetic comment -- @@ -488,7 +488,7 @@
*/
public void setFile(IFile file) {
mEditedFile = file;
        ensureInitialized();
}

/**
//Synthetic comment -- @@ -656,6 +656,7 @@
try {
updateDevices();
updateTargets();
            ensureInitialized();
} finally {
mDisableUpdates--;
}
//Synthetic comment -- @@ -695,6 +696,7 @@
if (mSdkChanged) {
updateDevices();
updateTargets();
                    ensureInitialized();
mSdkChanged = false;
}

//Synthetic comment -- @@ -709,6 +711,7 @@
if (mProjectTarget != null) {
targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mProjectTarget, null);
updateTargets();
                    ensureInitialized();
}

if (targetStatus == LoadStatus.LOADED) {
//Synthetic comment -- @@ -730,7 +733,7 @@
targetData = Sdk.getCurrent().getTargetData(mProjectTarget);

// get the file stored state
                    ensureInitialized();
boolean loadedConfigData = mConfiguration.getDevice() != null &&
mConfiguration.getDeviceState() != null;

//Synthetic comment -- @@ -851,8 +854,9 @@
return false;
}

    /** Ensures that the configuration has been initialized */
    public void ensureInitialized() {
        if (mConfiguration.getDevice() == null && mEditedFile != null) {
String data = AdtPlugin.getFileProperty(mEditedFile, NAME_CONFIG_STATE);
if (mInitialState != null) {
data = mInitialState;
//Synthetic comment -- @@ -860,6 +864,7 @@
}
if (data != null) {
mConfiguration.initialize(data);
                mConfiguration.syncFolderConfig();
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index 717347f..ad5bd52 100644

//Synthetic comment -- @@ -134,12 +134,6 @@
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
//Synthetic comment -- @@ -120,7 +121,7 @@

private static final boolean DEBUG = false;

    static final String PREFIX_CANVAS_ACTION = "canvas_action_"; //$NON-NLS-1$

/** The layout editor that uses this layout canvas. */
private final LayoutEditorDelegate mEditorDelegate;
//Synthetic comment -- @@ -387,9 +388,23 @@

if (clientWidth == 0) {
clientWidth = imageWidth;
                Shell shell = getShell();
                if (shell != null) {
                    org.eclipse.swt.graphics.Point size = shell.getSize();
                    if (size.x > 0) {
                        clientWidth = size.x * 70 / 100;
                    }
                }
}
if (clientHeight == 0) {
clientHeight = imageHeight;
                Shell shell = getShell();
                if (shell != null) {
                    org.eclipse.swt.graphics.Point size = shell.getSize();
                    if (size.y > 0) {
                        clientWidth = size.y * 80 / 100;
                    }
                }
}

mHScale.setSize(imageWidth, fullWidth, clientWidth);
//Synthetic comment -- @@ -414,8 +429,7 @@
Boolean zoomed = coordinator.isEditorMaximized();
if (mWasZoomed != zoomed) {
if (mWasZoomed != null) {
                            LayoutActionBar actionBar = getGraphicalEditor().getLayoutActionBar();
if (actionBar.isZoomingAllowed()) {
setFitScale(true /*onlyZoomOut*/, true /*allowZoomIn*/);
}
//Synthetic comment -- @@ -457,7 +471,7 @@
} else {
// Zooming actions
char c = e.character;
            LayoutActionBar actionBar = getGraphicalEditor().getLayoutActionBar();
if (c == '1' && actionBar.isZoomingAllowed()) {
setScale(1, true);
} else if (c == '0' && actionBar.isZoomingAllowed()) {
//Synthetic comment -- @@ -572,12 +586,12 @@
}

/** Returns the Rules Engine, associated with the current project. */
    RulesEngine getRulesEngine() {
return mRulesEngine;
}

/** Sets the Rules Engine, associated with the current project. */
    void setRulesEngine(RulesEngine rulesEngine) {
mRulesEngine = rulesEngine;
}

//Synthetic comment -- @@ -585,7 +599,7 @@
* Returns the factory to use to convert from {@link CanvasViewInfo} or from
* {@link UiViewElementNode} to {@link INode} proxies.
*/
    NodeFactory getNodeFactory() {
return mNodeFactory;
}

//Synthetic comment -- @@ -594,7 +608,7 @@
*
* @return The GCWrapper used to paint view rules
*/
    GCWrapper getGcWrapper() {
return mGCWrapper;
}

//Synthetic comment -- @@ -652,7 +666,7 @@
* @return A {@link CanvasTransform} for mapping between layout and control
*         coordinates in the horizontal dimension.
*/
    CanvasTransform getHorizontalTransform() {
return mHScale;
}

//Synthetic comment -- @@ -663,7 +677,7 @@
* @return A {@link CanvasTransform} for mapping between layout and control
*         coordinates in the vertical dimension.
*/
    CanvasTransform getVerticalTransform() {
return mVScale;
}

//Synthetic comment -- @@ -711,6 +725,11 @@
return mSelectAllAction;
}

    /** Returns the associated {@link GraphicalEditorPart} */
    GraphicalEditorPart getGraphicalEditor() {
        return mEditorDelegate.getGraphicalEditor();
    }

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
//Synthetic comment -- @@ -726,7 +745,7 @@
*            {@link #showInvisibleViews(boolean)}) where individual invisible nodes
*            are padded during certain interactions.
*/
    void setSession(RenderSession session, Set<UiElementNode> explodedNodes,
boolean layoutlib5) {
// disable any hover
clearHover();
//Synthetic comment -- @@ -737,7 +756,7 @@
session.isAlphaChannelImage());

mOutlinePage.setModel(mViewHierarchy.getRoot());
            getGraphicalEditor().setModel(mViewHierarchy.getRoot());

if (image != null) {
updateScrollBars();
//Synthetic comment -- @@ -765,8 +784,7 @@
void ensureZoomed() {
if (mZoomFitNextImage && getClientArea().height > 0) {
mZoomFitNextImage = false;
            LayoutActionBar actionBar = getGraphicalEditor().getLayoutActionBar();
if (actionBar.isZoomingAllowed()) {
setFitScale(true, true /*allowZoomIn*/);
}
//Synthetic comment -- @@ -789,7 +807,7 @@
return mHScale.getScale();
}

    void setScale(double scale, boolean redraw) {
if (scale <= 0.0) {
scale = 1.0;
}
//Synthetic comment -- @@ -896,7 +914,7 @@
* @param canvasY Y in the canvas coordinates
* @return A new {@link Point} in control client coordinates (not display coordinates)
*/
    Point layoutToControlPoint(int canvasX, int canvasY) {
int x = mHScale.translate(canvasX);
int y = mVScale.translate(canvasY);
return new Point(x, y);
//Synthetic comment -- @@ -911,7 +929,7 @@
* <p/>
* Returns null if there's no action for the given id.
*/
    IAction getAction(String actionId) {
String prefix = PREFIX_CANVAS_ACTION;
if (mMenuManager == null ||
actionId == null ||
//Synthetic comment -- @@ -997,7 +1015,7 @@
* @param show When true, any invisible parent nodes are padded and highlighted
*            ("exploded"), and when false any formerly exploded nodes are hidden.
*/
    void showInvisibleViews(boolean show) {
if (mShowInvisible == show) {
return;
}
//Synthetic comment -- @@ -1058,14 +1076,14 @@
/**
* Clears the hover.
*/
    void clearHover() {
mHoverOverlay.clearHover();
}

/**
* Hover on top of a known child.
*/
    void hover(MouseEvent e) {
// Check if a button is pressed; no hovers during drags
if ((e.stateMask & SWT.BUTTON_MASK) != 0) {
clearHover();
//Synthetic comment -- @@ -1117,7 +1135,7 @@
* @param url The layout attribute url of the form @layout/foo
*/
private void showInclude(String url) {
        GraphicalEditorPart graphicalEditor = getGraphicalEditor();
IPath filePath = graphicalEditor.findResourceFile(url);
if (filePath == null) {
// Should not be possible - if the URL had been bad, then we wouldn't
//Synthetic comment -- @@ -1217,7 +1235,7 @@
* @return the layout resource name of this layout
*/
public String getLayoutResourceName() {
        GraphicalEditorPart graphicalEditor = getGraphicalEditor();
return graphicalEditor.getLayoutResourceName();
}

//Synthetic comment -- @@ -1228,7 +1246,7 @@
*/
/*
public String getMe() {
        GraphicalEditorPart graphicalEditor = getGraphicalEditor();
IFile editedFile = graphicalEditor.getEditedFile();
return editedFile.getProjectRelativePath().toOSString();
}
//Synthetic comment -- @@ -1355,11 +1373,11 @@
copyActionAttributes(mSelectAllAction, ActionFactory.SELECT_ALL);
}

    String getCutLabel() {
return mCutAction.getText();
}

    String getDeleteLabel() {
// verb "Delete" from the DELETE action's title
return mDeleteAction.getText();
}
//Synthetic comment -- @@ -1374,7 +1392,7 @@
hasSelection = false;
}

        StyledText errorLabel = getGraphicalEditor().getErrorLabel();
mCutAction.setEnabled(hasSelection);
mCopyAction.setEnabled(hasSelection || errorLabel.getSelectionCount() > 0);
mDeleteAction.setEnabled(hasSelection);
//Synthetic comment -- @@ -1517,7 +1535,7 @@
private void setupStaticMenuActions(IMenuManager manager) {
manager.removeAll();

        manager.add(new SelectionManager.SelectionMenu(getGraphicalEditor()));
manager.add(new Separator());
manager.add(mCutAction);
manager.add(mCopyAction);
//Synthetic comment -- @@ -1545,7 +1563,7 @@
/**
* Deletes the selection. Equivalent to pressing the Delete key.
*/
    void delete() {
mDeleteAction.run();
}

//Synthetic comment -- @@ -1564,7 +1582,7 @@
*            {@link ViewElementDescriptor} to add as root to the current
*            empty XML document.
*/
    void createDocumentRoot(String rootFqcn) {

// Need a valid empty document to create the new root
final UiDocumentNode uiDoc = mEditorDelegate.getUiRootNode();
//Synthetic comment -- @@ -1618,8 +1636,7 @@
*/
public Margins getInsets(String fqcn) {
if (ViewMetadataRepository.INSETS_SUPPORTED) {
            ConfigurationChooser configComposite = getGraphicalEditor().getConfigurationChooser();
String theme = configComposite.getThemeName();
Density density = configComposite.getConfiguration().getDensity();
return ViewMetadataRepository.getInsets(fqcn, density, theme);
//Synthetic comment -- @@ -1676,7 +1693,8 @@

/** Ensures that the configuration previews are up to date for this canvas */
public void syncPreviewMode() {
        if (mImageOverlay != null && mImageOverlay.getImage() != null &&
            getGraphicalEditor().getConfigurationChooser().getResources() != null) {
if (mPreviewManager.recomputePreviews(false)) {
// Zoom when syncing modes
mZoomFitNextImage = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index aafa69d..699c548 100644

//Synthetic comment -- @@ -786,7 +786,7 @@
left += EDIT_ICON_WIDTH;
}

        paintTitle(gc, x, y, true /*showFile*/);
}

/**
//Synthetic comment -- @@ -796,7 +796,7 @@
* @param x the left edge of the preview rectangle
* @param y the top edge of the preview rectangle
*/
    void paintTitle(GC gc, int x, int y, boolean showFile) {
String displayName = getDisplayName();
if (displayName != null && displayName.length() > 0) {
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
//Synthetic comment -- @@ -826,7 +826,7 @@
gc.drawText(displayName, labelLeft, labelTop, true);
}

            if (mForked && mInput != null && showFile) {
// Draw file flag, and parent folder name
labelTop += extent.y;
String fileName = mInput.getParent().getName() + File.separator + mInput.getName();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 9fc0681..876ae88 100644

//Synthetic comment -- @@ -399,7 +399,7 @@

int x = destX + destWidth / 2 - preview.getWidth() / 2;
int y = destY + destHeight - preview.getHeight();
                preview.paintTitle(gc, x, y, false /*showFile*/);
}
} else if (mMode == RenderPreviewMode.CUSTOM) {
int rootX = getX();







