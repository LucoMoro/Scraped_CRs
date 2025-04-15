/*ADT GLE2: cut/copy/delete/select-all global actions.

Also provides a linked context menu for the canvas.
Paste action is in the menu but will be implemented in a next CL.

Change-Id:Iccd3663b5e0db16f44ae2f9a0c2c9271926fe8c2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index 0bd1789..c50244d 100644

//Synthetic comment -- @@ -346,9 +346,9 @@
errorListener);

if (errorListener.mHasXmlError == true) {
                    // There was an error in the manifest, its file has been marked
                    // by the XmlErrorHandler. The stopBuild() call below will abort
                    // this with an exception.
String msg = String.format(Messages.s_Contains_Xml_Error,
SdkConstants.FN_ANDROID_MANIFEST_XML);
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project, msg);
//Synthetic comment -- @@ -357,9 +357,13 @@
stopBuild(msg);
}

                // Get the java package from the parser.
                // This can be null if the parsing failed because the resource is out of sync,
                // in which case the error will already have been logged anyway.
                if (parser != null) {
                    javaPackage = parser.getPackage();
                    minSdkVersion = parser.getMinSdkVersionString();
                }
}

if (minSdkVersion != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index fd2ee77..68e1146 100644

//Synthetic comment -- @@ -632,7 +632,10 @@
* Returns a version of the model that has been shared for edit.
* <p/>
* Callers <em>must</em> call model.releaseFromEdit() when done, typically
     * in a try..finally clause. Because of this, it is highly recommended
     * to <b>NOT</b> use this method directly and instead use the wrapper
     * {@link #editXmlModel(Runnable)} which executes a runnable into a
     * properly configured model and then performs whatever cleanup is necessary.
*
* @return The model for the XML document or null if cannot be obtained from the editor
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java
//Synthetic comment -- index 2be5b10..3a8e454 100755

//Synthetic comment -- @@ -22,7 +22,6 @@

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.ui.IEditorPart;

/**
//Synthetic comment -- @@ -97,6 +96,4 @@
abstract UiDocumentNode getModel();

abstract LayoutEditor getLayoutEditor();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 591282b..4805e37 100755

//Synthetic comment -- @@ -21,14 +21,14 @@
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
//Synthetic comment -- @@ -39,9 +39,9 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutBridge;
//Synthetic comment -- @@ -63,26 +63,22 @@
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
//Synthetic comment -- @@ -143,9 +139,6 @@
/** Reference to the file being edited. Can also be used to access the {@link IProject}. */
private IFile mEditedFile;

/** The configuration composite at the top of the layout editor. */
private ConfigurationComposite mConfigComposite;

//Synthetic comment -- @@ -221,7 +214,6 @@
public void createPartControl(Composite parent) {

Display d = parent.getDisplay();

GridLayout gl = new GridLayout(1, false);
parent.setLayout(gl);
//Synthetic comment -- @@ -295,8 +287,6 @@
mSashError.setWeights(new int[] { 80, 20 });
mSashError.setMaximizedControl(mCanvasViewer.getControl());

// Initialize the state
reloadPalette();

//Synthetic comment -- @@ -334,42 +324,6 @@

}

/**
* Switches the stack to display the error label and hide the canvas.
* @param errorFormat The new error to display if not null.
//Synthetic comment -- @@ -389,7 +343,6 @@

@Override
public void dispose() {
getSite().getPage().removeSelectionListener(this);
getSite().setSelectionProvider(null);

//Synthetic comment -- @@ -403,11 +356,6 @@
mReloadListener = null;
}

super.dispose();
}

//Synthetic comment -- @@ -878,10 +826,6 @@
}
}

public LayoutEditor getLayoutEditor() {
return mLayoutEditor;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 82c3fe4..d9172c8 100755

//Synthetic comment -- @@ -26,12 +26,15 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -51,6 +54,7 @@
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
//Synthetic comment -- @@ -73,6 +77,10 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
//Synthetic comment -- @@ -110,11 +118,12 @@
*
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
* - outline should include same context menu + delete/copy/paste ops.
* - outline should include drop support (from canvas or from palette)
 * - handle empty root:
 *    - Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
      - We must be able to move/copy/cut the top root element (mostly between documents).
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

//Synthetic comment -- @@ -220,6 +229,21 @@
* invoking ourselves. */
private boolean mInsideUpdateSelection;

    /** Delete action for the Edit or context menu. */
    private Action mDeleteAction;

    /** Select-All action for the Edit or context menu. */
    private Action mSelectAllAction;

    /** Paste action for the Edit or context menu. */
    private Action mPasteAction;

    /** Cut action for the Edit or context menu. */
    private Action mCutAction;

    /** Copy action for the Edit or context menu. */
    private Action mCopyAction;


public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -278,6 +302,7 @@
}
});

        // --- setup drag'n'drop ---
// DND Reference: http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html

mDropTarget = new DropTarget(this, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
//Synthetic comment -- @@ -292,6 +317,11 @@
} );
mSource.addDragListener(new CanvasDragSourceListener());

        // --- setup context menu ---
        setupGlobalActionHandlers();
        createContextMenu();

        // --- setup outline ---
// Get the outline associated with this editor, if any and of the right type.
Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
if (outline instanceof OutlinePage2) {
//Synthetic comment -- @@ -299,8 +329,6 @@
}
}

@Override
public void dispose() {
super.dispose();
//Synthetic comment -- @@ -441,59 +469,6 @@
}

/**
* Transforms a point, expressed in SWT display coordinates
* (e.g. from a Drag'n'Drop {@link Event}, not local {@link Control} coordinates)
* into the canvas' image coordinates according to the current zoom and scroll.
//Synthetic comment -- @@ -520,7 +495,7 @@
* where each {@link TreePath} item is actually a {@link CanvasViewInfo}.
*/
public ISelection getSelection() {
        if (mSelections.isEmpty()) {
return TreeSelection.EMPTY;
}

//Synthetic comment -- @@ -545,6 +520,11 @@
* Sets the selection. It must be an {@link ITreeSelection} where each segment
* of the tree path is a {@link CanvasViewInfo}. A null selection is considered
* as an empty selection.
     * <p/>
     * This method is invoked by {@link LayoutCanvasViewer#setSelection(ISelection)}
     * in response to an <em>outside</em> selection (compatible with ours) that has
     * changed. Typically it means the outline selection has changed and we're
     * synchronizing ours to match.
*/
public void setSelection(ISelection selection) {
if (mInsideUpdateSelection) {
//Synthetic comment -- @@ -563,7 +543,7 @@

if (treeSel.isEmpty()) {
// Clear existing selection, if any
                    if (!mSelections.isEmpty()) {
mSelections.clear();
mAltSelection = null;
redraw();
//Synthetic comment -- @@ -610,7 +590,9 @@

if (changed) {
redraw();
                    updateMenuActions();
}

}
} finally {
mInsideUpdateSelection = false;
//Synthetic comment -- @@ -1050,7 +1032,7 @@
mAltSelection = null;

// reset (multi)selection if any
        if (!mSelections.isEmpty()) {
if (mSelections.size() == 1 && mSelections.getFirst().getViewInfo() == vi) {
// CanvasSelection remains the same, don't touch it.
return;
//Synthetic comment -- @@ -1178,7 +1160,7 @@
}
}

        if (!parent.getChildren().isEmpty()) {
// then add all children that match the position
for (CanvasViewInfo child : parent.getChildren()) {
r = child.getSelectionRect();
//Synthetic comment -- @@ -1231,6 +1213,10 @@
}
}
});

            // Update menu actions that depend on the selection
            updateMenuActions();

} finally {
mInsideUpdateSelection = false;
}
//Synthetic comment -- @@ -1267,7 +1253,7 @@

mDragSelection.clear();

            if (!mSelections.isEmpty()) {
// Is the cursor on top of a selected element?
int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);
//Synthetic comment -- @@ -1290,45 +1276,11 @@
}
}

            sanitizeSelection(mDragSelection);

            e.doit = !mDragSelection.isEmpty();
if (e.doit) {
                mDragElements = getSelectionAsElements(mDragSelection);
GlobalCanvasDragInfo.getInstance().startDrag(
mDragElements,
mDragSelection.toArray(new CanvasSelection[mDragSelection.size()]),
//Synthetic comment -- @@ -1345,7 +1297,7 @@
*/
public void dragSetData(DragSourceEvent e) {
if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
                e.data = getSelectionAsText(mDragSelection);
return;
}

//Synthetic comment -- @@ -1359,107 +1311,6 @@
e.doit = false;
}

/**
* Callback invoked when the drop has been finished either way.
* On a successful move, remove the originating elements.
//Synthetic comment -- @@ -1478,7 +1329,7 @@
// Create an edit-XML wrapper, which takes a runnable
mLayoutEditor.editXmlModel(new Runnable() {
public void run() {
                                        deleteSelection("Remove", mDragSelection);
}
});
}
//Synthetic comment -- @@ -1490,23 +1341,483 @@
mDragElements = null;
GlobalCanvasDragInfo.getInstance().stopDrag();
}
    }

    /**
     * Sanitizes the selection for a copy/cut or drag operation.
     * <p/>
     * Sanitizes the list to make sure all elements have a valid XML attached to it,
     * that is remove element that have no XML to avoid having to make repeated such
     * checks in various places after.
     * <p/>
     * In case of multiple selection, we also need to remove all children when their
     * parent is already selected since parents will always be added with all their
     * children.
     * <p/>
     *
     * @param selection The selection list to be sanitized <b>in-place</b>.
     *      The <code>selection</code> argument should not be {@link #mSelections} -- the
     *      given list is going to be altered and we should never alter the user-made selection.
     *      Instead the caller should provide its own copy.
     */
    private void sanitizeSelection(List<CanvasSelection> selection) {
        if (selection.isEmpty()) {
            return;
}

        for (Iterator<CanvasSelection> it = selection.iterator(); it.hasNext(); ) {
            CanvasSelection cs = it.next();
            CanvasViewInfo vi = cs.getViewInfo();
            UiViewElementNode key = vi == null ? null : vi.getUiViewKey();
            Node node = key == null ? null : key.getXmlNode();
            if (node == null) {
                // Missing ViewInfo or view key or XML, discard this.
                it.remove();
                continue;
            }

            if (vi != null) {
                for (Iterator<CanvasSelection> it2 = selection.iterator();
                     it2.hasNext(); ) {
                    CanvasSelection cs2 = it2.next();
                    if (cs != cs2) {
                        CanvasViewInfo vi2 = cs2.getViewInfo();
                        if (vi.isParent(vi2)) {
                            // vi2 is a parent for vi. Remove vi.
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
}

    /**
     * Get the XML text from the given selection for a text transfer.
     * The returned string can be empty but not null.
     */
    private String getSelectionAsText(List<CanvasSelection> selection) {
        StringBuilder sb = new StringBuilder();

        for (CanvasSelection cs : selection) {
            CanvasViewInfo vi = cs.getViewInfo();
            UiViewElementNode key = vi.getUiViewKey();
            Node node = key.getXmlNode();
            String t = getXmlTextFromEditor(mLayoutEditor, node);
            if (t != null) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(t);
            }
        }

        return sb.toString();
    }

    /**
     * Get the XML text directly from the editor.
     */
    private String getXmlTextFromEditor(AndroidXmlEditor editor, Node xml_node) {
        String data = null;
        IStructuredModel model = editor.getModelForRead();
        try {
            IStructuredDocument sse_doc = editor.getStructuredDocument();
            if (xml_node instanceof NodeContainer) {
                // The easy way to get the source of an SSE XML node.
                data = ((NodeContainer) xml_node).getSource();
            } else  if (xml_node instanceof IndexedRegion && sse_doc != null) {
                // Try harder.
                IndexedRegion region = (IndexedRegion) xml_node;
                int start = region.getStartOffset();
                int end = region.getEndOffset();

                if (end > start) {
                    data = sse_doc.get(start, end - start);
                }
            }
        } catch (BadLocationException e) {
            // the region offset was invalid. ignore.
        } finally {
            model.releaseFromRead();
        }
        return data;
    }

    private SimpleElement[] getSelectionAsElements(List<CanvasSelection> mDragSelection) {
        ArrayList<SimpleElement> elements = new ArrayList<SimpleElement>();

        for (CanvasSelection cs : mDragSelection) {
            CanvasViewInfo vi = cs.getViewInfo();

            SimpleElement e = transformToSimpleElement(vi);
            elements.add(e);
        }

        return elements.toArray(new SimpleElement[elements.size()]);
    }

    private SimpleElement transformToSimpleElement(CanvasViewInfo vi) {

        UiViewElementNode uiNode = vi.getUiViewKey();

        String fqcn = SimpleXmlTransfer.getFqcn(uiNode.getDescriptor());
        String parentFqcn = null;
        Rect bounds = new Rect(vi.getAbsRect());
        Rect parentBounds = null;

        UiElementNode uiParent = uiNode.getUiParent();
        if (uiParent != null) {
            parentFqcn = SimpleXmlTransfer.getFqcn(uiParent.getDescriptor());
        }
        if (vi.getParent() != null) {
            parentBounds = new Rect(vi.getParent().getAbsRect());
        }

        SimpleElement e = new SimpleElement(fqcn, parentFqcn, bounds, parentBounds);

        for (UiAttributeNode attr : uiNode.getUiAttributes()) {
            String value = attr.getCurrentValue();
            if (value != null && value.length() > 0) {
                AttributeDescriptor attrDesc = attr.getDescriptor();
                SimpleAttribute a = new SimpleAttribute(
                        attrDesc.getNamespaceUri(),
                        attrDesc.getXmlLocalName(),
                        value);
                e.addAttribute(a);
            }
        }

        for (CanvasViewInfo childVi : vi.getChildren()) {
            SimpleElement e2 = transformToSimpleElement(childVi);
            if (e2 != null) {
                e.addInnerElement(e2);
            }
        }

        return e;
    }

    //---------------

    private void createContextMenu() {

        MenuManager mm = new MenuManager();
        createMenuAction(mm);
        setMenu(mm.createContextMenu(this));
        /*
        Menu menu = new Menu(this);

        ISharedImages wbImages = PlatformUI.getWorkbench().getSharedImages();

        final MenuItem cutItem = new MenuItem (menu, SWT.PUSH);
        cutItem.setText("Cut");
        cutItem.setImage(wbImages.getImage(ISharedImages.IMG_TOOL_CUT));
        cutItem.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                // TODO
            }
        });

        menu.addMenuListener(new MenuAdapter() {
            @Override
            public void menuShown(MenuEvent e) {
                cutItem.setEnabled(!mSelections.isEmpty());
                super.menuShown(e);
            }
        });

        setMenu(menu);
        */

    }

    /** Update menu actions that depends on the selection. */
    private void updateMenuActions() {

        boolean hasSelection = !mSelections.isEmpty();

        mCutAction.setEnabled(hasSelection);
        mCopyAction.setEnabled(hasSelection);
        mDeleteAction.setEnabled(hasSelection);
        mSelectAllAction.setEnabled(hasSelection);

        // The paste operation is only available if we can paste our custom type.
        // We do not currently support pasting random text (e.g. XML). Maybe later.
        SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();
        boolean hasSxt = false;
        for (TransferData td : mClipboard.getAvailableTypes()) {
            if (sxt.isSupportedType(td)) {
                hasSxt = true;
                break;
            }
        }
        mPasteAction.setEnabled(hasSxt);
    }

    /**
     * Invoked by the constructor to add our cut/copy/paste/delete/select-all
     * handlers in the global action handlers of this editor's site.
     * <p/>
     * This will enable the menu items under the global Edit menu and make them
     * invoke our actions as needed. As a benefit, the corresponding shortcut
     * accelerators will do what one would expect.
     */
    private void setupGlobalActionHandlers() {
        // Get the global action bar for this editor (i.e. the menu bar)
        IActionBars actionBars = mLayoutEditor.getEditorSite().getActionBars();

        TextActionHandler tah = new TextActionHandler(actionBars);

        mCutAction = new Action() {
            @Override
            public void run() {
                cutSelectionToClipboard(new ArrayList<CanvasSelection>(mSelections));
            }
        };

        tah.setCutAction(mCutAction);
        copyActionAttributes(mCutAction, ActionFactory.CUT);

        mCopyAction = new Action() {
            @Override
            public void run() {
                copySelectionToClipboard(new ArrayList<CanvasSelection>(mSelections));
            }
        };

        tah.setCopyAction(mCopyAction);
        copyActionAttributes(mCopyAction, ActionFactory.COPY);

        mPasteAction = new Action() {
            @Override
            public void run() {
                onPaste();
            }
        };

        tah.setPasteAction(mPasteAction);
        copyActionAttributes(mPasteAction, ActionFactory.PASTE);

        mDeleteAction = new Action() {
            @Override
            public void run() {
                deleteSelection(
                        mDeleteAction.getText(), // verb "Delete" from the DELETE action's title
                        new ArrayList<CanvasSelection>(mSelections));
            }
        };

        tah.setDeleteAction(mDeleteAction);
        copyActionAttributes(mDeleteAction, ActionFactory.DELETE);

        mSelectAllAction = new Action() {
            @Override
            public void run() {
                onSelectAll();
            }
        };

        tah.setSelectAllAction(mSelectAllAction);
        copyActionAttributes(mSelectAllAction, ActionFactory.SELECT_ALL);
    }

    /**
     * Helper for {@link #setupGlobalActionHandlers()}.
     * Copies the action attributes form the given {@link ActionFactory}'s action to
     * our action.
     * <p/>
     * {@link ActionFactory} provides access to the standard global actions in Ecipse.
     * <p/>
     * This allows us to grab the standard labels and icons for the
     * global actions such as copy, cut, paste, delete and select-all.
     */
    private void copyActionAttributes(Action action, ActionFactory factory) {
        IWorkbenchAction wa = factory.create(mLayoutEditor.getEditorSite().getWorkbenchWindow());
        action.setEnabled(false);
        action.setText(wa.getText());
        action.setDescription(wa.getDescription());
        action.setImageDescriptor(wa.getImageDescriptor());
        action.setHoverImageDescriptor(wa.getHoverImageDescriptor());
        action.setDisabledImageDescriptor(wa.getDisabledImageDescriptor());
    }

    /**
     * Invoked by the constructor to create our *static* context menu.
     * <p/>
     * The content of the menu itself does not change. However the state of the
     * various items is controlled by their associated actions.
     * <p/>
     * For cut/copy/paste/delete/select-all, we explicitely reuse the actions
     * created by {@link #setupGlobalActionHandlers()}, so this method must be
     * invoked after that one.
     */
    private void createMenuAction(IMenuManager manager) {
        manager.removeAll();

        manager.add(mCutAction);
        manager.add(mCopyAction);
        manager.add(mPasteAction);

        manager.add(new Separator());

        manager.add(mDeleteAction);
        manager.add(mSelectAllAction);

        // TODO add view-sensitive menu items.
    }

    /**
     * Invoked by {@link #mSelectAllAction}. It clears the selection and then
     * selects everything (all views and all their children).
     */
    private void onSelectAll() {
        // First clear the current selection, if any.
        mSelections.clear();
        mAltSelection = null;

        // Now select everything if there's a valid layout
        if (mIsResultValid && mLastValidResult != null) {
            selectAllViewInfos(mLastValidViewInfoRoot);
            redraw();
        }

        fireSelectionChanged();
    }

    /**
     * Perform the "Copy" action, either from the Edit menu or from the context menu.
     * Invoked by {@link #mCopyAction}.
     * <p/>
     * This sanitizes the selection, so it must be a copy. It then inserts the selection
     * both as text and as {@link SimpleElement}s in the clipboard.
     */
    private void copySelectionToClipboard(List<CanvasSelection> selection) {
        sanitizeSelection(selection);

        if (selection.isEmpty()) {
            return;
        }

        Object[] data = new Object[] {
                getSelectionAsElements(selection),
                getSelectionAsText(selection)
        };

        Transfer[] types = new Transfer[] {
                SimpleXmlTransfer.getInstance(),
                TextTransfer.getInstance()
        };

        mClipboard.setContents(data, types);
    }

    /**
     * Perform the "Cut" action, either from the Edit menu or from the context menu.
     * Invoked by {@link #mCutAction}.
     * <p/>
     * This sanitizes the selection, so it must be a copy.
     * It uses the {@link #copySelectionToClipboard(List)} method to copy the selection
     * to the clipboard.
     * Finally it uses {@link #deleteSelection(String, List)} to delete the selection
     * with a "Cut" verb for the title.
     */
    private void cutSelectionToClipboard(List<CanvasSelection> selection) {
        copySelectionToClipboard(selection);
        deleteSelection(
                mCutAction.getText(), // verb "Cut" from the CUT action's title
                selection);
    }

    /**
     * Deletes the given selection.
     * <p/>
     * This can either be invoked directly by {@link #mDeleteAction}, or as
     * an implementation detail as part of {@link #mCutAction} or also when removing
     * the elements after a successful "MOVE" drag'n'drop.
     *
     * @param verb A translated verb for the action. Will be used for the undo/redo title.
     *   Typically this should be {@link Action#getText()} for either
     *   {@link #mCutAction} or {@link #mDeleteAction}.
     * @param selection The selection. Must not be null. Can be empty, in which case nothing
     *   happens. The selection list will be sanitized so the caller should give a copy of
     *   {@link #mSelections}, directly or indirectly.
     */
    private void deleteSelection(String verb, final List<CanvasSelection> selection) {
        sanitizeSelection(selection);

        if (selection.isEmpty()) {
            return;
        }

        // If all selected items have the same *kind* of parent, display that in the undo title.
        String title = null;
        for (CanvasSelection cs : selection) {
            CanvasViewInfo vi = cs.getViewInfo();
            if (vi != null && vi.getParent() != null) {
                if (title == null) {
                    title = vi.getParent().getName();
                } else if (!title.equals(vi.getParent().getName())) {
                    // More than one kind of parent selected.
                    title = null;
                    break;
                }
            }
        }

        if (title != null) {
            // Typically the name is an FQCN. Just get the last segment.
            int pos = title.lastIndexOf('.');
            if (pos > 0 && pos < title.length() - 1) {
                title = title.substring(pos + 1);
            }
        }
        boolean multiple = mSelections.size() > 1;
        if (title == null) {
            title = String.format(
                        multiple ? "%1$s elements" : "%1$s element",
                        verb);
        } else {
            title = String.format(
                        multiple ? "%1$s elements from %2$s" : "%1$s element from %2$s",
                        verb, title);
        }

        // Implementation note: we don't clear the internal selection after removing
        // the elements. An update XML model event should happen when the model gets released
        // which will trigger a recompute of the layout, thus reloading the model thus
        // resetting the selection.
        mLayoutEditor.wrapUndoRecording(title, new Runnable() {
            public void run() {
                mLayoutEditor.editXmlModel(new Runnable() {
                    public void run() {
                        for (CanvasSelection cs : selection) {
                            CanvasViewInfo vi = cs.getViewInfo();
                            if (vi != null) {
                                UiViewElementNode ui = vi.getUiViewKey();
                                if (ui != null) {
                                    ui.deleteXmlNode();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * Perform the "Paste" action, either from the Edit menu or from the context menu.
     */
    private void onPaste() {
        // TODO need to defer that to GRE.
        // TODO list:
        // - Paste as "in first parent, before first select child" (defined by scripts).
        // - If there's nothing selected, use root element as the parent.
        // - handle empty root:
        //   Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
        AdtPlugin.displayWarning("Canvas Paste", "Not implemented yet");
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java
//Synthetic comment -- index ba4fd5d..2261fe0 100644

//Synthetic comment -- @@ -55,7 +55,7 @@

/**
* Creates a new Copy or Cut action.
     *
* @param selected The UI node to cut or copy. It *must* have a non-null XML node.
* @param performCut True if the operation is cut, false if it is copy.
*/
//Synthetic comment -- @@ -66,7 +66,7 @@

/**
* Creates a new Copy or Cut action.
     *
* @param selected The UI nodes to cut or copy. They *must* have a non-null XML node.
*                 The list becomes owned by the {@link CopyCutAction}.
* @param performCut True if the operation is cut, false if it is copy.
//Synthetic comment -- @@ -77,16 +77,16 @@
mEditor = editor;
mClipboard = clipboard;
mXmlCommit = xmlCommit;

ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
if (performCut) {
setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
            setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_HOVER));
setDisabledImageDescriptor(
images.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
} else {
setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
            setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_HOVER));
setDisabledImageDescriptor(
images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
}
//Synthetic comment -- @@ -117,21 +117,21 @@
ArrayList<UiElementNode> nodesToCut = mPerformCut ? new ArrayList<UiElementNode>() : null;

for (UiElementNode uiNode : mUiNodes) {
            try {
Node xml_node = uiNode.getXmlNode();
if (xml_node == null) {
return;
}

String data = getXmlTextFromEditor(xml_node);

// In the unlikely event that IStructuredDocument failed to extract the text
// directly from the editor, try to fall back on a direct XML serialization
// of the XML node. This uses the generic Node interface with no SSE tricks.
if (data == null) {
data = getXmlTextFromSerialization(xml_node);
}

if (data != null) {
allText.append(data);
if (mPerformCut) {
//Synthetic comment -- @@ -139,7 +139,7 @@
nodesToCut.add(uiNode);
}
}

} catch (Exception e) {
AdtPlugin.log(e, "CopyCutAction failed for UI node %1$s", //$NON-NLS-1$
uiNode.getBreadcrumbTrailDescription(true));
//Synthetic comment -- @@ -172,7 +172,7 @@
IndexedRegion region = (IndexedRegion) xml_node;
int start = region.getStartOffset();
int end = region.getEndOffset();

if (end > start) {
data = sse_doc.get(start, end - start);
}
//Synthetic comment -- @@ -184,7 +184,7 @@
}
return data;
}

/**
* Direct XML serialization of the XML node.
* <p/>







