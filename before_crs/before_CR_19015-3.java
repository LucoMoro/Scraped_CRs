/*GLE: Remove obsolete IGraphicalLayoutEditor interface.

Change-Id:I96f929ce9386e88f80d15195b2ff6a498e374554*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java
deleted file mode 100755
//Synthetic comment -- index c200e47..0000000

//Synthetic comment -- @@ -1,119 +0,0 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.common.layoutlib.LayoutLibrary;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.LayoutScene;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.IEditorPart;

import java.util.Set;

/**
 * Interface defining what {@link LayoutEditor} expects from a GraphicalLayoutEditor part.
 *
 * @since GLE2
 */
public interface IGraphicalLayoutEditor extends IEditorPart {

    /**
     * Opens and initialize the editor with a new file.
     * @param file the file being edited.
     */
    abstract void openFile(IFile file);

    /**
     * Resets the editor with a replacement file.
     * @param file the replacement file.
     */
    abstract void replaceFile(IFile file);

    /**
     * Resets the editor with a replacement file coming from a config change in the config
     * selector.
     * @param file the replacement file.
     */
    abstract void changeFileOnNewConfig(IFile file);

    /**
     * Responds to a target change for the project of the edited file
     */
    abstract void onTargetChange();

    /**
     * Callback for XML model changed. Only update/recompute the layout if the editor is visible
     */
    abstract void onXmlModelChanged();

    /**
     * Responds to a page change that made the Graphical editor page the activated page.
     */
    abstract void activated();

    /**
     * Responds to a page change that made the Graphical editor page the deactivated page
     */
    abstract void deactivated();

    abstract void reloadPalette();

    abstract void recomputeLayout();

    abstract UiDocumentNode getModel();

    abstract LayoutEditor getLayoutEditor();

    /**
     * Returns the {@link LayoutLibrary} associated with this editor, if it has
     * been initialized already. May return null if it has not been initialized (or has
     * not finished initializing).
     *
     * @return The {@link LayoutLibrary}, or null
     */
    abstract LayoutLibrary getLayoutLibrary();

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
    abstract LayoutScene render(UiDocumentNode model,
            int width, int height, Set<UiElementNode> explodeNodes, boolean transparentBackground);

    /**
     * Returns the current bounds of the Android device screen, in canvas control pixels
     *
     * @return the bounds of the screen, never null
     */
    abstract Rectangle getScreenBounds();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 41bf099..7dc9169 100644

//Synthetic comment -- @@ -69,7 +69,7 @@
/** Root node of the UI element hierarchy */
private UiDocumentNode mUiRootNode;

    private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
/** Implementation of the {@link IContentOutlinePage} for this editor */
private IContentOutlinePage mOutline;
//Synthetic comment -- @@ -94,6 +94,15 @@
}

/**
* @return The root node of the UI element hierarchy
*/
@Override
//Synthetic comment -- @@ -291,20 +300,20 @@
@SuppressWarnings("unchecked")
@Override
public Object getAdapter(Class adapter) {
        // for the outline, force it to come from the Graphical Editor.
// This fixes the case where a layout file is opened in XML view first and the outline
// gets stuck in the XML outline.
if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

            if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
                mOutline = new OutlinePage2((GraphicalEditorPart) mGraphicalEditor);
}

return mOutline;
}

if (IPropertySheetPage.class == adapter && mGraphicalEditor != null) {
            if (mPropertyPage == null && mGraphicalEditor instanceof GraphicalEditorPart) {
mPropertyPage = new PropertySheetPage2();
}

//Synthetic comment -- @@ -326,8 +335,8 @@
int caretOffset = textViewer.getTextWidget().getCaretOffset();
if (caretOffset >= 0) {
Node node = AndroidXmlEditor.getNode(textViewer.getDocument(), caretOffset);
                if (node != null && mGraphicalEditor instanceof GraphicalEditorPart) {
                    ((GraphicalEditorPart)mGraphicalEditor).select(node);
}
}
}
//Synthetic comment -- @@ -605,17 +614,4 @@

return null;
}

    /**
     * Returns the {@link RulesEngine} associated with this editor
     *
     * @return the {@link RulesEngine} associated with this editor, or null
     */
    public RulesEngine getRulesEngine() {
        if (mGraphicalEditor instanceof GraphicalEditorPart) {
            return ((GraphicalEditorPart) mGraphicalEditor).getRulesEngine();
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index d7d71da..55d296d 100755

//Synthetic comment -- @@ -23,7 +23,6 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
//Synthetic comment -- @@ -134,13 +133,9 @@
* outline and property sheet (these are registered by {@link LayoutEditor#getAdapter(Class)}).
*
* @since GLE2
 *
 * TODO List:
 * - display error icon
 * - Completely rethink the property panel
*/
public class GraphicalEditorPart extends EditorPart
    implements IGraphicalLayoutEditor, ISelectionListener, INullSelectionListener {

/*
* Useful notes:
//Synthetic comment -- @@ -933,6 +928,9 @@
mConfigComposite.changeFileOnNewConfig(mEditedFile);
}

public void onTargetChange() {
AndroidTargetData targetData = mConfigComposite.onXmlModelLoaded();
if (targetData != null) {
//Synthetic comment -- @@ -943,16 +941,6 @@
mConfigListener.onConfigurationChange();
}

    private void setClippingSupport(boolean b) {
        mClippingButton.setEnabled(b);
        if (b) {
            mClippingButton.setToolTipText("Toggles screen clipping on/off");
        } else {
            mClippingButton.setSelection(true);
            mClippingButton.setToolTipText("Non clipped rendering is not supported");
        }
    }

public LayoutEditor getLayoutEditor() {
return mLayoutEditor;
}
//Synthetic comment -- @@ -1041,6 +1029,78 @@
}
}

/**
* Ensure that the file associated with this editor is valid (exists and is
* synchronized). Any reasons why it is not are displayed in the editor's error area.
//Synthetic comment -- @@ -1072,7 +1132,6 @@
return true;
}


/**
* Returns a {@link LayoutLibrary} that is ready for rendering, or null if the bridge
* is not available or not ready yet (due to SDK loading still being in progress etc).
//Synthetic comment -- @@ -1225,21 +1284,6 @@
model.refreshUi();
}

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

private LayoutScene renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
boolean transparentBackground) {
//Synthetic comment -- @@ -1417,16 +1461,6 @@
// FIXME: get rid of the current LayoutScene if any.
}

    public Rectangle getScreenBounds() {
        return mConfigComposite.getScreenBounds();
    }

    public void reloadPalette() {
        if (mPalette != null) {
            mPalette.reloadPalette(mLayoutEditor.getTargetData());
        }
    }

private class ReloadListener implements ILayoutReloadListener {
/*
* Called when the file changes triggered a redraw of the layout
//Synthetic comment -- @@ -1500,10 +1534,6 @@
}
}

    public LayoutLibrary getLayoutLibrary() {
        return getReadyLayoutLib(false /*displayError*/);
    }

// ---- Error handling ----

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 6b46688..2b01b1c 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
//Synthetic comment -- @@ -111,14 +110,14 @@
private ScrollBar mVBar;
private ControlListener mControlListener;
private Listener mScrollbarListener;
    private IGraphicalLayoutEditor mEditor;

/**
* Create the composite.
* @param parent The parent composite.
* @param editor An editor associated with this palette.
*/
    public PaletteComposite(Composite parent, IGraphicalLayoutEditor editor) {
super(parent, SWT.BORDER | SWT.V_SCROLL);

mEditor = editor;
//Synthetic comment -- @@ -296,7 +295,7 @@
}
}

    /* package */ IGraphicalLayoutEditor getEditor() {
return mEditor;
}

//Synthetic comment -- @@ -481,7 +480,7 @@
return mDesc;
}

        /* package */ IGraphicalLayoutEditor getEditor() {
return mPalette.getEditor();
}

//Synthetic comment -- @@ -681,7 +680,7 @@

// Insert our target view's XML into it as a node
ElementDescriptor desc = mItem.getDescriptor();
            IGraphicalLayoutEditor editor = mItem.getEditor();
LayoutEditor layoutEditor = editor.getLayoutEditor();

String viewName = desc.getXmlLocalName();
//Synthetic comment -- @@ -722,17 +721,15 @@
// Call the create-hooks such that we for example insert mandatory
// children into views like the DialerFilter, apply image source attributes
// to ImageButtons, etc.
            if (editor instanceof GraphicalEditorPart) {
                LayoutCanvas canvas = ((GraphicalEditorPart) editor).getCanvasControl();
                NodeFactory nodeFactory = canvas.getNodeFactory();
                UiElementNode parent = model.getUiRoot();
                UiElementNode child = parent.getUiChildren().get(0);
                if (child instanceof UiViewElementNode) {
                    UiViewElementNode childUiNode = (UiViewElementNode) child;
                    NodeProxy childNode = nodeFactory.create(childUiNode);
                    canvas.getRulesEngine().callCreateHooks(layoutEditor,
                            null, childNode, InsertType.CREATE);
                }
}

boolean hasTransparency = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index a14d812..95f56e1 100755

//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -92,7 +92,7 @@
/**
* The editor which owns this {@link RulesEngine}
*/
    private IGraphicalLayoutEditor mEditor;

/**
* Creates a new {@link RulesEngine} associated with the selected project.
//Synthetic comment -- @@ -102,7 +102,7 @@
* @param editor the editor which owns this {@link RulesEngine}
* @param project A non-null open project.
*/
    public RulesEngine(IGraphicalLayoutEditor editor, IProject project) {
mProject = project;
mEditor = editor;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ViewMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ViewMetadata.java
//Synthetic comment -- index eeebad7..22f1168 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
*/
import com.android.ide.common.api.IViewMetadata;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;

import java.util.HashMap;
//Synthetic comment -- @@ -35,29 +34,29 @@
/** The fully qualified class name of the view whose metadata this class represents */
private String mFqcn;

    /** The {@link IGraphicalLayoutEditor} associated with the view we're looking up */
private LayoutEditor mEditor;

/**
     * A map from class names to {@link FillPreference} which indicates how each view
* prefers to grow when added in various layout contexts
*/
    private static final Map<String,FillPreference> mFill = new HashMap<String,FillPreference>();
static {
// Hardcoded metadata about fill preferences for various known views. We should
// work to try to get this into the platform as designtime annotations.

        mFill.put("android.widget.EditText", FillPreference.WIDTH_IN_VERTICAL);     //$NON-NLS-1$
mFill.put("android.widget.DialerFilter", FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.SeekBar", FillPreference.WIDTH_IN_VERTICAL);      //$NON-NLS-1$
        mFill.put("android.widget.Spinner", FillPreference.WIDTH_IN_VERTICAL);      //$NON-NLS-1$
mFill.put("android.widget.AutoComplete", FillPreference.WIDTH_IN_VERTICAL); //$NON-NLS-1$
        mFill.put("android.widget.ListView", FillPreference.WIDTH_IN_VERTICAL);     //$NON-NLS-1$
        mFill.put("android.widget.GridView", FillPreference.OPPOSITE);              //$NON-NLS-1$
        mFill.put("android.widget.Gallery", FillPreference.WIDTH_IN_VERTICAL);      //$NON-NLS-1$
        mFill.put("android.widget.TabWidget", FillPreference.WIDTH_IN_VERTICAL);    //$NON-NLS-1$
        mFill.put("android.widget.MapView", FillPreference.OPPOSITE);               //$NON-NLS-1$
        mFill.put("android.widget.WebView", FillPreference.OPPOSITE);               //$NON-NLS-1$

// In addition, all layouts are FillPreference.OPPOSITE - these are computed
// lazily rather than enumerating them here







