/*Multi configuration editing fixes

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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 8f7172e..aeafa6d 100644

//Synthetic comment -- @@ -642,6 +642,7 @@
sShadow2Right       = readImage("shadow2-r.png");  //$NON-NLS-1$
sShadow2TopRight    = readImage("shadow2-tr.png"); //$NON-NLS-1$
assert sShadow2BottomLeft != null;
            assert sShadow2TopRight != null;
assert sShadow2BottomRight.getWidth() == SMALL_SHADOW_SIZE;
assert sShadow2BottomRight.getHeight() == SMALL_SHADOW_SIZE;
}








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
//Synthetic comment -- index aafa69d..cb55b59 100644

//Synthetic comment -- @@ -69,6 +69,7 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -92,9 +93,27 @@
* Represents a preview rendering of a given configuration
*/
public class RenderPreview implements IJobChangeListener {
    /** Whether previews should use large shadows */
static final boolean LARGE_SHADOWS = false;

    /**
     * Still doesn't work; get exceptions from layoutlib:
     * java.lang.IllegalStateException: After scene creation, #init() must be called
     *   at com.android.layoutlib.bridge.impl.RenderAction.acquire(RenderAction.java:151)
     * <p>
     * TODO: Investigate.
     */
    private static final boolean RENDER_ASYNC = false;

    /**
     * Height of the toolbar shown over a preview during hover. Needs to be
     * large enough to accommodate icons below.
     */
    private static final int HEADER_HEIGHT = 20;

    /** Whether to dump out rendering failures of the previews to the log */
private static final boolean DUMP_RENDER_DIAGNOSTICS = false;

private static final Image EDIT_ICON;
private static final Image ZOOM_IN_ICON;
private static final Image ZOOM_OUT_ICON;
//Synthetic comment -- @@ -118,11 +137,12 @@

/** The configuration being previewed */
private final @NonNull Configuration mConfiguration;

/** The associated manager */
private final @NonNull RenderPreviewManager mManager;
private final @NonNull LayoutCanvas mCanvas;
private @Nullable ResourceResolver mResourceResolver;
    private @Nullable Job mJob;
private @Nullable Map<ResourceType, Map<String, ResourceValue>> mConfiguredFrameworkRes;
private @Nullable Map<ResourceType, Map<String, ResourceValue>> mConfiguredProjectRes;
private @Nullable Image mThumbnail;
//Synthetic comment -- @@ -132,15 +152,20 @@
private int mX;
private int mY;
private double mScale = 1.0;

/** If non null, points to a separate file containing the source */
private @Nullable IFile mInput;

/** If included within another layout, the name of that outer layout */
private @Nullable Reference mIncludedWithin;

/** Whether the mouse is actively hovering over this preview */
private boolean mActive;

/** Whether this preview cannot be rendered because of a model error - such as
* an invalid configuration, a missing resource, an error in the XML markup, etc */
private boolean mError;

/**
* Whether this preview presents a file that has been "forked" (separate,
* not linked) from the primary layout.
//Synthetic comment -- @@ -149,9 +174,15 @@
* instead.
*/
private boolean mForked;

/** Whether in the current layout, this preview is visible */
private boolean mVisible;

    /** Whether the configuration has changed and needs to be refreshed the next time
     * this preview made visible. This corresponds to the change flags in
     * {@link ConfigurationClient}. */
    private int mDirty;

/**
* Creates a new {@linkplain RenderPreview}
*
//Synthetic comment -- @@ -172,8 +203,6 @@
mConfiguration = configuration;
mWidth = width;
mHeight = height;
}

/**
//Synthetic comment -- @@ -255,7 +284,17 @@
* @param visible whether this preview is visible
*/
public void setVisible(boolean visible) {
        if (visible != mVisible) {
            mVisible = visible;
            if (mVisible) {
                if (mDirty != 0) {
                    // Just made the render preview visible:
                    configurationChanged(mDirty);
                } else {
                    updateForkStatus();
                }
            }
        }
}

/**
//Synthetic comment -- @@ -427,11 +466,15 @@
* @param delay the delay to wait before starting the render job
*/
public void render(long delay) {
        Job job = mJob;
if (job != null) {
job.cancel();
}
        if (RENDER_ASYNC) {
            job = new AsyncRenderJob();
        } else {
            job = new RenderJob();
        }
job.schedule(delay);
job.addJobChangeListener(this);
mJob = job;
//Synthetic comment -- @@ -495,12 +538,13 @@

if (DUMP_RENDER_DIAGNOSTICS) {
if (log.hasProblems() || !render.isSuccess()) {
                AdtPlugin.log(IStatus.ERROR, "Found problems rendering preview "
                        + getDisplayName() + ": "
                        + render.getErrorMessage() + " : "
                        + log.getProblems(false));
Throwable exception = render.getException();
if (exception != null) {
                    AdtPlugin.log(exception, "Failure rendering preview " + getDisplayName());
}
}
}
//Synthetic comment -- @@ -662,7 +706,7 @@
* @return true if this preview handled (and therefore consumed) the click
*/
public boolean click(int x, int y) {
        if (y < HEADER_HEIGHT) {
int left = 0;
left += CLOSE_ICON_WIDTH;
if (x <= left) {
//Synthetic comment -- @@ -768,8 +812,7 @@
gc.setAlpha(128+32);
Color bg = mCanvas.getDisplay().getSystemColor(SWT.COLOR_WHITE);
gc.setBackground(bg);
            gc.fillRectangle(left, y, x + getWidth() - left, HEADER_HEIGHT);
gc.setAlpha(prevAlpha);

// Paint icons
//Synthetic comment -- @@ -786,7 +829,7 @@
left += EDIT_ICON_WIDTH;
}

        paintTitle(gc, x, y, true /*showFile*/);
}

/**
//Synthetic comment -- @@ -796,7 +839,7 @@
* @param x the left edge of the preview rectangle
* @param y the top edge of the preview rectangle
*/
    void paintTitle(GC gc, int x, int y, boolean showFile) {
String displayName = getDisplayName();
if (displayName != null && displayName.length() > 0) {
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
//Synthetic comment -- @@ -826,7 +869,7 @@
gc.drawText(displayName, labelLeft, labelTop, true);
}

            if (mForked && mInput != null && showFile) {
// Draw file flag, and parent folder name
labelTop += extent.y;
String fileName = mInput.getParent().getName() + File.separator + mInput.getName();
//Synthetic comment -- @@ -856,6 +899,11 @@
*            {@code CHANGE_} constants in {@link ConfigurationClient}
*/
public void configurationChanged(int flags) {
        if (!mVisible) {
            mDirty |= flags;
            return;
        }

if ((flags & (CHANGED_FOLDER | CHANGED_THEME | CHANGED_DEVICE
| CHANGED_RENDER_TARGET | CHANGED_LOCALE)) != 0) {
mResourceResolver = null;
//Synthetic comment -- @@ -888,6 +936,8 @@
mHeight = mWidth;
mWidth = temp;
}

        mDirty = 0;
}

/**
//Synthetic comment -- @@ -936,28 +986,6 @@
setUser(false);
}

@Override
public IStatus runInUIThread(IProgressMonitor monitor) {
mJob = null;
//Synthetic comment -- @@ -979,6 +1007,35 @@
}
}

    private final class AsyncRenderJob extends Job {
        public AsyncRenderJob() {
            super("RenderPreview");
            setSystem(true);
            setUser(false);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            mJob = null;

            if (mCanvas.isDisposed()) {
                return org.eclipse.core.runtime.Status.CANCEL_STATUS;
            }

            renderSync();

            // Update display
            mCanvas.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    mCanvas.redraw();
                }
            });

            return org.eclipse.core.runtime.Status.OK_STATUS;
        }
    }

/**
* Sets the input file to use for rendering. If not set, this will just be
* the same file as the configuration chooser. This is used to render other








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 9fc0681..593dbc4 100644

//Synthetic comment -- @@ -88,6 +88,11 @@
private @Nullable ScrollBarListener mListener;
private int mLayoutHeight;
private int mMaxVisibleY;
    /** Last seen state revision in this {@link RenderPreviewManager}. If less
     * than {@link #sRevision}, the previews need to be updated on next exposure */
    private static int mRevision;
    /** Current global revision count */
    private static int sRevision;

/**
* Creates a {@link RenderPreviewManager} associated with the given canvas
//Synthetic comment -- @@ -101,6 +106,15 @@
}

/**
     * Revise the global state revision counter. This will cause all layout
     * preview managers to refresh themselves to the latest revision when they
     * are next exposed.
     */
    public static void bumpRevision() {
        sRevision++;
    }

    /**
* Returns the associated chooser
*
* @return the associated chooser
//Synthetic comment -- @@ -399,7 +413,7 @@

int x = destX + destWidth / 2 - preview.getWidth() / 2;
int y = destY + destHeight - preview.getHeight();
                preview.paintTitle(gc, x, y, false /*showFile*/);
}
} else if (mMode == RenderPreviewMode.CUSTOM) {
int rootX = getX();
//Synthetic comment -- @@ -595,13 +609,15 @@
*/
public boolean recomputePreviews(boolean force) {
RenderPreviewMode newMode = AdtPrefs.getPrefs().getRenderPreviewMode();
        if (newMode == mMode && !force
                && (mRevision == sRevision
                    || mMode == RenderPreviewMode.NONE
                    || mMode == RenderPreviewMode.CUSTOM)) {
            return false;
}

mMode = newMode;
        mRevision = sRevision;

sScale = 1.0;
disposePreviews();
//Synthetic comment -- @@ -667,7 +683,7 @@
configuration.setOverrideLocale(true);
configuration.setLocale(locale, false);

            String displayName = ConfigurationChooser.getLocaleLabel(chooser, locale, false);
assert displayName != null; // it's never non null when locale is non null
configuration.setDisplayName(displayName);

//Synthetic comment -- @@ -677,7 +693,7 @@
// Make a placeholder preview for the current screen, in case we switch from it
Configuration configuration = parent;
Locale locale = configuration.getLocale();
        String label = ConfigurationChooser.getLocaleLabel(chooser, locale, false);
if (label == null) {
label = "default";
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java
//Synthetic comment -- index b1a0299..ce7e936 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LocaleManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderPreviewManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -403,6 +404,7 @@
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
IFolder folder = root.getFolder(parent.getFullPath());
manager.getResourceFolder(folder);
            RenderPreviewManager.bumpRevision();
} catch (CoreException e) {
AdtPlugin.log(e, null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 22e2325..33b22b4 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatPreferences;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderPreviewManager;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.SupportLibraryHelper;
//Synthetic comment -- @@ -275,6 +276,15 @@
}
file.create(stream, true /*force*/, null /*progress*/);
IRegion region = caretOffset != -1 ? new Region(caretOffset, 0) : null;

            // If you introduced a new locale, or new screen variations etc, ensure that
            // the list of render previews is updated if necessary
            if (file.getParent().getName().indexOf('-') != -1
                    && (folderType == ResourceFolderType.LAYOUT
                        || folderType == ResourceFolderType.VALUES)) {
                RenderPreviewManager.bumpRevision();
            }

return Pair.of(file, region);
} catch (UnsupportedEncodingException e) {
error = e.getMessage();







