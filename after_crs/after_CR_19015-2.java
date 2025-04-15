/*GLE: Remove obsolete IGraphicalLayoutEditor interface.

Change-Id:I96f929ce9386e88f80d15195b2ff6a498e374554*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java
deleted file mode 100755
//Synthetic comment -- index c200e47..0000000

//Synthetic comment -- @@ -1,119 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 41bf099..7dc9169 100644

//Synthetic comment -- @@ -69,7 +69,7 @@
/** Root node of the UI element hierarchy */
private UiDocumentNode mUiRootNode;

    private GraphicalEditorPart mGraphicalEditor;
private int mGraphicalEditorIndex;
/** Implementation of the {@link IContentOutlinePage} for this editor */
private IContentOutlinePage mOutline;
//Synthetic comment -- @@ -94,6 +94,15 @@
}

/**
     * Returns the {@link RulesEngine} associated with this editor
     *
     * @return the {@link RulesEngine} associated with this editor.
     */
    public RulesEngine getRulesEngine() {
        return mGraphicalEditor.getRulesEngine();
    }

    /**
* @return The root node of the UI element hierarchy
*/
@Override
//Synthetic comment -- @@ -291,20 +300,20 @@
@SuppressWarnings("unchecked")
@Override
public Object getAdapter(Class adapter) {
        // For the outline, force it to come from the Graphical Editor.
// This fixes the case where a layout file is opened in XML view first and the outline
// gets stuck in the XML outline.
if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

            if (mOutline == null && mGraphicalEditor != null) {
                mOutline = new OutlinePage2(mGraphicalEditor);
}

return mOutline;
}

if (IPropertySheetPage.class == adapter && mGraphicalEditor != null) {
            if (mPropertyPage == null) {
mPropertyPage = new PropertySheetPage2();
}

//Synthetic comment -- @@ -326,8 +335,8 @@
int caretOffset = textViewer.getTextWidget().getCaretOffset();
if (caretOffset >= 0) {
Node node = AndroidXmlEditor.getNode(textViewer.getDocument(), caretOffset);
                if (node != null && mGraphicalEditor != null) {
                    mGraphicalEditor.select(node);
}
}
}
//Synthetic comment -- @@ -605,17 +614,4 @@

return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 728c089..c84cd2a 100755

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
//Synthetic comment -- @@ -126,13 +125,9 @@
* outline and property sheet (these are registered by {@link LayoutEditor#getAdapter(Class)}).
*
* @since GLE2
*/
public class GraphicalEditorPart extends EditorPart
    implements ISelectionListener, INullSelectionListener {

/*
* Useful notes:
//Synthetic comment -- @@ -925,6 +920,9 @@
mConfigComposite.changeFileOnNewConfig(mEditedFile);
}

    /**
     * Responds to a target change for the project of the edited file
     */
public void onTargetChange() {
AndroidTargetData targetData = mConfigComposite.onXmlModelLoaded();
if (targetData != null) {
//Synthetic comment -- @@ -935,16 +933,6 @@
mConfigListener.onConfigurationChange();
}

public LayoutEditor getLayoutEditor() {
return mLayoutEditor;
}
//Synthetic comment -- @@ -1033,6 +1021,78 @@
}
}

    public void reloadPalette() {
        if (mPalette != null) {
            mPalette.reloadPalette(mLayoutEditor.getTargetData());
        }
    }

    /**
     * Renders the given model, using this editor's theme and screen settings, and returns
     * the result as a {@link LayoutScene}. Any error messages will be written to the
     * editor's error area.
     *
     * @param model the model to be rendered, which can be different than the editor's own
     *            {@link #getModel()}.
     * @param width the width to use for the layout, or -1 to use the width of the screen
     *            associated with this editor
     * @param height the height to use for the layout, or -1 to use the height of the screen
     *            associated with this editor
     * @param explodeNodes a set of nodes to explode, or null for none
     * @param transparentBackground If true, the rendering will <b>not</b> paint the
     *            normal background requested by the theme, and it will instead paint the
     *            background using a fully transparent background color
     * @return the resulting rendered image wrapped in an {@link LayoutScene}
     */
    public LayoutScene render(UiDocumentNode model, int width, int height,
            Set<UiElementNode> explodeNodes, boolean transparentBackground) {
        if (!ensureFileValid()) {
            return null;
        }
        if (!ensureModelValid(model)) {
            return null;
        }
        LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);

        IProject iProject = mEditedFile.getProject();
        return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                transparentBackground);
    }

    /**
     * Returns the {@link LayoutLibrary} associated with this editor, if it has
     * been initialized already. May return null if it has not been initialized (or has
     * not finished initializing).
     *
     * @return The {@link LayoutLibrary}, or null
     */
    public LayoutLibrary getLayoutLibrary() {
        return getReadyLayoutLib(false /*displayError*/);
    }

    /**
     * Returns the current bounds of the Android device screen, in canvas control pixels.
     *
     * @return the bounds of the screen, never null
     */
    public Rectangle getScreenBounds() {
        return mConfigComposite.getScreenBounds();
    }



    // --- private methods ---

    private void setClippingSupport(boolean b) {
        mClippingButton.setEnabled(b);
        if (b) {
            mClippingButton.setToolTipText("Toggles screen clipping on/off");
        } else {
            mClippingButton.setSelection(true);
            mClippingButton.setToolTipText("Non clipped rendering is not supported");
        }
    }

/**
* Ensure that the file associated with this editor is valid (exists and is
* synchronized). Any reasons why it is not are displayed in the editor's error area.
//Synthetic comment -- @@ -1064,7 +1124,6 @@
return true;
}

/**
* Returns a {@link LayoutLibrary} that is ready for rendering, or null if the bridge
* is not available or not ready yet (due to SDK loading still being in progress etc).
//Synthetic comment -- @@ -1217,21 +1276,6 @@
model.refreshUi();
}

private LayoutScene renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
boolean transparentBackground) {
//Synthetic comment -- @@ -1364,16 +1408,6 @@
// FIXME: get rid of the current LayoutScene if any.
}

private class ReloadListener implements ILayoutReloadListener {
/*
* Called when the file changes triggered a redraw of the layout
//Synthetic comment -- @@ -1447,10 +1481,6 @@
}
}

// ---- Error handling ----

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 6b46688..2b01b1c 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
//Synthetic comment -- @@ -111,14 +110,14 @@
private ScrollBar mVBar;
private ControlListener mControlListener;
private Listener mScrollbarListener;
    private GraphicalEditorPart mEditor;

/**
* Create the composite.
* @param parent The parent composite.
* @param editor An editor associated with this palette.
*/
    public PaletteComposite(Composite parent, GraphicalEditorPart editor) {
super(parent, SWT.BORDER | SWT.V_SCROLL);

mEditor = editor;
//Synthetic comment -- @@ -296,7 +295,7 @@
}
}

    /* package */ GraphicalEditorPart getEditor() {
return mEditor;
}

//Synthetic comment -- @@ -481,7 +480,7 @@
return mDesc;
}

        /* package */ GraphicalEditorPart getEditor() {
return mPalette.getEditor();
}

//Synthetic comment -- @@ -681,7 +680,7 @@

// Insert our target view's XML into it as a node
ElementDescriptor desc = mItem.getDescriptor();
            GraphicalEditorPart editor = mItem.getEditor();
LayoutEditor layoutEditor = editor.getLayoutEditor();

String viewName = desc.getXmlLocalName();
//Synthetic comment -- @@ -722,17 +721,15 @@
// Call the create-hooks such that we for example insert mandatory
// children into views like the DialerFilter, apply image source attributes
// to ImageButtons, etc.
            LayoutCanvas canvas = editor.getCanvasControl();
            NodeFactory nodeFactory = canvas.getNodeFactory();
            UiElementNode parent = model.getUiRoot();
            UiElementNode child = parent.getUiChildren().get(0);
            if (child instanceof UiViewElementNode) {
                UiViewElementNode childUiNode = (UiViewElementNode) child;
                NodeProxy childNode = nodeFactory.create(childUiNode);
                canvas.getRulesEngine().callCreateHooks(layoutEditor,
                        null, childNode, InsertType.CREATE);
}

boolean hasTransparency = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index a14d812..95f56e1 100755

//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -92,7 +92,7 @@
/**
* The editor which owns this {@link RulesEngine}
*/
    private GraphicalEditorPart mEditor;

/**
* Creates a new {@link RulesEngine} associated with the selected project.
//Synthetic comment -- @@ -102,7 +102,7 @@
* @param editor the editor which owns this {@link RulesEngine}
* @param project A non-null open project.
*/
    public RulesEngine(GraphicalEditorPart editor, IProject project) {
mProject = project;
mEditor = editor;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ViewMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ViewMetadata.java
//Synthetic comment -- index eeebad7..22f1168 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
*/
import com.android.ide.common.api.IViewMetadata;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;

import java.util.HashMap;
//Synthetic comment -- @@ -35,29 +34,29 @@
/** The fully qualified class name of the view whose metadata this class represents */
private String mFqcn;

    /** The {@link LayoutEditor} associated with the view we're looking up */
private LayoutEditor mEditor;

/**
     * A map from class names to {@link IViewMetadata.FillPreference} which indicates how each view
* prefers to grow when added in various layout contexts
*/
    private static final Map<String, FillPreference> mFill = new HashMap<String,FillPreference>();
static {
// Hardcoded metadata about fill preferences for various known views. We should
// work to try to get this into the platform as designtime annotations.

        mFill.put("android.widget.EditText",     FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
mFill.put("android.widget.DialerFilter", FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.SeekBar",      FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.Spinner",      FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
mFill.put("android.widget.AutoComplete", FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.ListView",     FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.GridView",     FillPreference.OPPOSITE);          //$NON-NLS-1$
        mFill.put("android.widget.Gallery",      FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.TabWidget",    FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.MapView",      FillPreference.OPPOSITE);          //$NON-NLS-1$
        mFill.put("android.widget.WebView",      FillPreference.OPPOSITE);          //$NON-NLS-1$

// In addition, all layouts are FillPreference.OPPOSITE - these are computed
// lazily rather than enumerating them here







