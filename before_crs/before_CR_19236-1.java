/*Rename some layout editor classes

This changeset contains only renaming of some classes (and a couple
of >100 column adjustments), no semantic changes.

The name changes are:

BaseView => BaseViewRule
BaseLayout => BaseLayoutRule
ScaleInfo => CanvasTransform
CanvasSelection => SelectionItem
PropertySheetPage2 => PropertySheetPage
OutlinePage2 => OutlinePage

Change-Id:I14d8c711b12154f4fcb2169129fd553e31fdab84*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 348d6cf..ffa43d9 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
* An {@link IViewRule} for android.widget.AbsoluteLayout and all its derived
* classes.
*/
public class AbsoluteLayoutRule extends BaseLayout {

// ==== Drag'n'drop support ====
// The AbsoluteLayout accepts any drag'n'drop anywhere on its surface.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index 979a4a2..2425ca6 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
import java.util.Map;
import java.util.Set;

public class BaseLayout extends BaseView {

@Override
public boolean onInitialize(String fqcn, IClientRulesEngine engine) {
//Synthetic comment -- @@ -258,7 +258,7 @@
}

/**
     * For use by {@link BaseLayout#addAttributes} A filter should return a
* valid replacement string.
*/
public static interface AttributeFilter {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 4bcf069..983560f 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
/**
* Common IViewRule processing to all view and layout classes.
*/
public class BaseView implements IViewRule {
protected IClientRulesEngine mRulesEngine;

// Cache of attributes. Key is FQCN of a node mixed with its view hierarchy
//Synthetic comment -- @@ -418,8 +418,8 @@
String parentFqcn = parent.getFqcn();
IViewRule parentRule = mRulesEngine.loadRule(parentFqcn);

            if (parentRule instanceof BaseLayout) {
                ((BaseLayout) parentRule).onPasteBeforeChild(parent, targetNode, elements);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java
//Synthetic comment -- index 311af84..4c1af19 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
/**
* An {@link IViewRule} for android.widget.DialerFilterRule.
*/
public class DialerFilterRule extends BaseView {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index 16b477b..46b3bb7 100755

//Synthetic comment -- @@ -33,7 +33,7 @@
* An {@link IViewRule} for android.widget.FrameLayout and all its derived
* classes.
*/
public class FrameLayoutRule extends BaseLayout {

// ==== Drag'n'drop support ====
// The FrameLayout accepts any drag'n'drop anywhere on its surface.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index 6ae6872..cceee78 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
/**
* An {@link IViewRule} for android.widget.HorizontalScrollView.
*/
public class HorizontalScrollViewRule extends BaseView {

@Override
public void onChildInserted(INode child, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IgnoredLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IgnoredLayoutRule.java
//Synthetic comment -- index af92c71..a0b56d2 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
* LinearLayout even though it extends it. Our ZoomControls rule is therefore a
* subclass of this {@link IgnoredLayoutRule} class.
*/
public abstract class IgnoredLayoutRule extends BaseLayout {
@Override
public DropFeedback onDropEnter(INode targetNode, IDragElement[] elements) {
// Do nothing; this layout rule corresponds to a layout that








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java
//Synthetic comment -- index 9ea5dc2..4338a9a 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
/**
* An {@link IViewRule} for android.widget.ImageButtonRule.
*/
public class ImageButtonRule extends BaseView {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java
//Synthetic comment -- index 9b1eac3..9d26e75 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
/**
* An {@link IViewRule} for android.widget.ImageViewRule.
*/
public class ImageViewRule extends BaseView {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 77e6d54..d797677 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
* An {@link IViewRule} for android.widget.LinearLayout and all its derived
* classes.
*/
public class LinearLayoutRule extends BaseLayout {
/**
* Add an explicit Orientation toggle to the context menu.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java
//Synthetic comment -- index d3d226c..c83e294 100755

//Synthetic comment -- @@ -29,7 +29,7 @@
* This is the "root" rule, that is used whenever there is not more specific
* rule to apply.
*/
public class ListViewRule extends BaseView {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java
//Synthetic comment -- index b8ddedd..301385a 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
* TODO: This class should be pulled out of the ADT and bundled with the add ons
* (not the core jar but an optional tool jar)
*/
public class MapViewRule extends BaseView {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 04629b2..bf708a3 100755

//Synthetic comment -- @@ -45,7 +45,7 @@
* An {@link IViewRule} for android.widget.RelativeLayout and all its derived
* classes.
*/
public class RelativeLayoutRule extends BaseLayout {

// ==== Selection ====









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index 5324278..0d3af83 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
/**
* An {@link IViewRule} for android.widget.ScrollView.
*/
public class ScrollViewRule extends BaseView {

@Override
public void onChildInserted(INode child, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java
//Synthetic comment -- index 727d231..40fb608 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
/**
* An {@link IViewRule} for android.widget.SeekBar
*/
public class SeekBarRule extends BaseView {

@Override
public void onCreate(INode node, INode parent, InsertType insertType) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ViewRule.java
//Synthetic comment -- index d645364..470b6a4 100755

//Synthetic comment -- @@ -26,6 +26,6 @@
* There is no customization here, everything that is common to all views is
* simply implemented in BaseView.
*/
public class ViewRule extends BaseView {

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 74e86a0..2124aae 100644

//Synthetic comment -- @@ -26,8 +26,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.OutlinePage2;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.PropertySheetPage2;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -315,7 +315,7 @@
if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

if (mOutline == null && mGraphicalEditor != null) {
                mOutline = new OutlinePage2(mGraphicalEditor);
}

return mOutline;
//Synthetic comment -- @@ -323,7 +323,7 @@

if (IPropertySheetPage.class == adapter && mGraphicalEditor != null) {
if (mPropertyPage == null) {
                mPropertyPage = new PropertySheetPage2();
}

return mPropertyPage;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ScaleInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
similarity index 95%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ScaleInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index 0565549..8a4ebc6 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
* Helper class to convert between control pixel coordinates and canvas coordinates.
* Takes care of the zooming and offset of the canvas.
*/
public class ScaleInfo implements ICanvasTransform {
/**
* The canvas which controls the zooming.
*/
//Synthetic comment -- @@ -44,7 +44,7 @@
/** Scrollbar widget. */
private ScrollBar mScrollbar;

    public ScaleInfo(LayoutCanvas layoutCanvas, ScrollBar scrollbar) {
mCanvas = layoutCanvas;
mScrollbar = scrollbar;
mScale = 1.0;
//Synthetic comment -- @@ -55,7 +55,7 @@
public void widgetSelected(SelectionEvent e) {
// User requested scrolling. Changes translation and redraw canvas.
mTranslate = mScrollbar.getSelection();
                ScaleInfo.this.mCanvas.redraw();
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index 680c12e..540aedb 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
*            <b>this should be a copy already - this method will not make a
*            copy</b>
*/
    public void copySelectionToClipboard(List<CanvasSelection> selection) {
SelectionManager.sanitize(selection);

if (selection.isEmpty()) {
//Synthetic comment -- @@ -91,8 +91,8 @@
}

Object[] data = new Object[] {
                CanvasSelection.getAsElements(selection),
                CanvasSelection.getAsText(mCanvas, selection)
};

Transfer[] types = new Transfer[] {
//Synthetic comment -- @@ -116,7 +116,7 @@
*            <b>this should be a copy already - this method will not make a
*            copy</b>
*/
    public void cutSelectionToClipboard(List<CanvasSelection> selection) {
copySelectionToClipboard(selection);
deleteSelection(
mCanvas.getCutLabel(),
//Synthetic comment -- @@ -134,7 +134,7 @@
*            case nothing happens. The selection list will be sanitized so
*            the caller should pass in a copy.
*/
    public void deleteSelection(String verb, final List<CanvasSelection> selection) {
SelectionManager.sanitize(selection);

if (selection.isEmpty()) {
//Synthetic comment -- @@ -143,7 +143,7 @@

// If all selected items have the same *kind* of parent, display that in the undo title.
String title = null;
        for (CanvasSelection cs : selection) {
CanvasViewInfo vi = cs.getViewInfo();
if (vi != null && vi.getParent() != null) {
if (title == null) {
//Synthetic comment -- @@ -180,7 +180,7 @@
// resetting the selection.
mCanvas.getLayoutEditor().wrapUndoEditXmlModel(title, new Runnable() {
public void run() {
                for (CanvasSelection cs : selection) {
CanvasViewInfo vi = cs.getViewInfo();
// You can't delete the root element
if (vi != null && !vi.isRoot()) {
//Synthetic comment -- @@ -202,7 +202,7 @@
*            <b>this should be a copy already - this method will not make a
*            copy</b>
*/
    public void pasteSelection(List<CanvasSelection> selection) {

SimpleXmlTransfer sxt = SimpleXmlTransfer.getInstance();
SimpleElement[] pasted = (SimpleElement[]) mClipboard.getContents(sxt);
//Synthetic comment -- @@ -224,7 +224,7 @@
SelectionManager.sanitize(selection);
CanvasViewInfo target = lastRoot;
if (selection.size() > 0) {
            CanvasSelection cs = selection.get(0);
target = cs.getViewInfo();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index a514447..ca08209 100755

//Synthetic comment -- @@ -51,7 +51,7 @@
* This class is tied to a specific {@link LayoutCanvas} instance and a root {@link MenuManager}.
* <p/>
* Two instances of this are used: one created by {@link LayoutCanvas} and the other one
 * created by {@link OutlinePage2}. Different root {@link MenuManager}s are populated, however
* they are both linked to the current selection state of the {@link LayoutCanvas}.
*/
/* package */ class DynamicContextMenu {
//Synthetic comment -- @@ -74,7 +74,7 @@
* @param canvas The {@link LayoutCanvas} providing the selection, the node factory and
*   the rules engine.
* @param rootMenu The root of the context menu displayed. In practice this may be the
     *   context menu manager of the {@link LayoutCanvas} or the one from {@link OutlinePage2}.
*/
public DynamicContextMenu(LayoutEditor editor, LayoutCanvas canvas, MenuManager rootMenu) {
mEditor = editor;
//Synthetic comment -- @@ -218,7 +218,7 @@
final TreeMap<String, ArrayList<MenuAction>> outActionsMap,
final TreeMap<String, MenuAction.Group> outGroupsMap) {
int maxMenuSelection = 0;
        for (CanvasSelection selection : mCanvas.getSelectionManager().getSelections()) {
List<MenuAction> viewActions = null;
if (selection != null) {
CanvasViewInfo vi = selection.getViewInfo();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/EmptyViewsOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/EmptyViewsOverlay.java
//Synthetic comment -- index 946c381..8df6c58 100644

//Synthetic comment -- @@ -33,21 +33,24 @@
private Color mBorderColor;

/** Vertical scaling & scrollbar information. */
    private ScaleInfo mVScale;

/** Horizontal scaling & scrollbar information. */
    private ScaleInfo mHScale;

/**
* Constructs a new {@link EmptyViewsOverlay} linked to the given view hierarchy.
*
* @param viewHierarchy The {@link ViewHierarchy} to render.
     * @param hScale The {@link ScaleInfo} to use to transfer horizontal layout
*            coordinates to screen coordinates.
     * @param vScale The {@link ScaleInfo} to use to transfer vertical layout coordinates
*            to screen coordinates.
*/
    public EmptyViewsOverlay(ViewHierarchy viewHierarchy, ScaleInfo hScale, ScaleInfo vScale) {
super();
this.mViewHierarchy = viewHierarchy;
this.mHScale = hScale;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 2c7bd73..d3a7820 100644

//Synthetic comment -- @@ -476,7 +476,7 @@
* canvas selection due to the "sanitize" pass. Can be empty but never
* null.
*/
        private final ArrayList<CanvasSelection> mDragSelection = new ArrayList<CanvasSelection>();

private SimpleElement[] mDragElements;

//Synthetic comment -- @@ -497,14 +497,14 @@
// element, *change* the selection to match the element under the
// cursor and use that. If nothing can be selected, abort the drag
// operation.
            List<CanvasSelection> selections = mCanvas.getSelectionManager().getSelections();
mDragSelection.clear();

if (!selections.isEmpty()) {
// Is the cursor on top of a selected element?
boolean insideSelection = false;

                for (CanvasSelection cs : selections) {
if (!cs.isRoot() && cs.getRect().contains(p.x, p.y)) {
insideSelection = true;
break;
//Synthetic comment -- @@ -532,7 +532,7 @@
mDragSelection.addAll(selections);
} else {
// Only drag non-root items.
                        for (CanvasSelection cs : selections) {
if (!cs.isRoot()) {
mDragSelection.add(cs);
}
//Synthetic comment -- @@ -555,9 +555,9 @@
e.doit = !mDragSelection.isEmpty();
int imageCount = mDragSelection.size();
if (e.doit) {
                mDragElements = CanvasSelection.getAsElements(mDragSelection);
GlobalCanvasDragInfo.getInstance().startDrag(mDragElements,
                        mDragSelection.toArray(new CanvasSelection[imageCount]),
mCanvas, new Runnable() {
public void run() {
mCanvas.getClipboardSupport().deleteSelection("Remove",
//Synthetic comment -- @@ -593,7 +593,7 @@
if (imageCount > 0) {
ImageData data = image.getImageData();
Rectangle imageRectangle = new Rectangle(0, 0, data.width, data.height);
                        for (CanvasSelection item : mDragSelection) {
Rectangle bounds = item.getRect();
// Some bounds can be outside the rendered rectangle (for
// example, in an absolute layout, you can have negative
//Synthetic comment -- @@ -630,7 +630,7 @@
*/
public void dragSetData(DragSourceEvent e) {
if (TextTransfer.getInstance().isSupportedType(e.dataType)) {
                e.data = CanvasSelection.getAsText(mCanvas, mDragSelection);
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index 7aee9bc..3fd98a3 100755

//Synthetic comment -- @@ -40,7 +40,7 @@
private static final GlobalCanvasDragInfo sInstance = new GlobalCanvasDragInfo();

private SimpleElement[] mCurrentElements = null;
    private CanvasSelection[] mCurrentSelection;
private Object mSourceCanvas = null;
private Runnable mRemoveSourceHandler;

//Synthetic comment -- @@ -66,7 +66,7 @@
*            source. It should only be invoked if the drag operation is a
*            move, not a copy.
*/
    public void startDrag(SimpleElement[] elements, CanvasSelection[] selection,
Object sourceCanvas, Runnable removeSourceHandler) {
mCurrentElements = elements;
mCurrentSelection = selection;
//Synthetic comment -- @@ -89,12 +89,12 @@
/** Returns the selection originally dragged.
* Can be null if the drag did not start in a canvas.
*/
    public CanvasSelection[] getCurrentSelection() {
return mCurrentSelection;
}

/**
     * Returns the object that call {@link #startDrag(SimpleElement[], CanvasSelection[], Object)}.
* Can be null.
* This is not meant to access the object indirectly, it is just meant to compare if the
* source and the destination of the drag'n'drop are the same, so object identity








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HoverOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HoverOverlay.java
//Synthetic comment -- index 9a39427..27a6024 100644

//Synthetic comment -- @@ -33,10 +33,10 @@
private Color mHoverFillColor;

/** Vertical scaling & scrollbar information. */
    private ScaleInfo mVScale;

/** Horizontal scaling & scrollbar information. */
    private ScaleInfo mHScale;

/**
* Current mouse hover border rectangle. Null when there's no mouse hover.
//Synthetic comment -- @@ -48,12 +48,12 @@
/**
* Constructs a new {@link HoverOverlay} linked to the given view hierarchy.
*
     * @param hScale The {@link ScaleInfo} to use to transfer horizontal layout
*            coordinates to screen coordinates.
     * @param vScale The {@link ScaleInfo} to use to transfer vertical layout
*            coordinates to screen coordinates.
*/
    public HoverOverlay(ScaleInfo hScale, ScaleInfo vScale) {
super();
this.mHScale = hScale;
this.mVScale = vScale;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index b920607..893ed8f 100644

//Synthetic comment -- @@ -35,10 +35,10 @@
private LayoutCanvas mCanvas;

/** Vertical scaling & scrollbar information. */
    private ScaleInfo mVScale;

/** Horizontal scaling & scrollbar information. */
    private ScaleInfo mHScale;

/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
//Synthetic comment -- @@ -47,7 +47,7 @@
* @param hScale The horizontal scale information.
* @param vScale The vertical scale information.
*/
    public ImageOverlay(LayoutCanvas canvas, ScaleInfo hScale, ScaleInfo vScale) {
this.mCanvas = canvas;
this.mHScale = hScale;
this.mVScale = vScale;
//Synthetic comment -- @@ -107,8 +107,8 @@
gc_setAlpha(gc, 128); // half-transparent
}

            ScaleInfo hi = mHScale;
            ScaleInfo vi = mVScale;

// we only anti-alias when reducing the image size.
int oldAlias = -2;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 89eb9a4..4782204 100755

//Synthetic comment -- @@ -141,10 +141,10 @@
private final NodeFactory mNodeFactory = new NodeFactory();

/** Vertical scaling & scrollbar information. */
    private ScaleInfo mVScale;

/** Horizontal scaling & scrollbar information. */
    private ScaleInfo mHScale;

/** Drag source associated with this canvas. */
private DragSource mDragSource;
//Synthetic comment -- @@ -156,7 +156,7 @@
* when page's widget is already disposed.
* Added the DisposeListener to OutlinePage2 in order to correctly dispose this page.
**/
    private OutlinePage2 mOutlinePage;

/** Delete action for the Edit or context menu. */
private Action mDeleteAction;
//Synthetic comment -- @@ -217,8 +217,8 @@
mRulesEngine = rulesEngine;

mClipboardSupport = new ClipboardSupport(this, parent);
        mHScale = new ScaleInfo(this, getHorizontalBar());
        mVScale = new ScaleInfo(this, getVerticalBar());

mGCWrapper = new GCWrapper(mHScale, mVScale);

//Synthetic comment -- @@ -286,8 +286,8 @@
// --- setup outline ---
// Get the outline associated with this editor, if any and of the right type.
Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
        if (outline instanceof OutlinePage2) {
            mOutlinePage = (OutlinePage2) outline;
}
}

//Synthetic comment -- @@ -368,7 +368,7 @@

/**
* Returns our {@link DragSourceListener}.
     * This is used by {@link OutlinePage2} to delegate drag source events.
*/
/* package */ DragSourceListener getDragListener() {
return mGestureManager.getDragSourceListener();
//Synthetic comment -- @@ -376,7 +376,7 @@

/**
* Returns our {@link DropTargetListener}.
     * This is used by {@link OutlinePage2} to delegate drop target events.
*/
/* package */ DropTargetListener getDropListener() {
return mGestureManager.getDropTargetListener();
//Synthetic comment -- @@ -408,24 +408,24 @@
}

/**
     * Returns the horizontal {@link ScaleInfo} transform object, which can map
* a layout point into a control point.
*
     * @return A {@link ScaleInfo} for mapping between layout and control
*         coordinates in the horizontal dimension.
*/
    /* package */ ScaleInfo getHorizontalTransform() {
return mHScale;
}

/**
     * Returns the vertical {@link ScaleInfo} transform object, which can map a
* layout point into a control point.
*
     * @return A {@link ScaleInfo} for mapping between layout and control
*         coordinates in the vertical dimension.
*/
    /* package */ ScaleInfo getVerticalTransform() {
return mVScale;
}

//Synthetic comment -- @@ -632,11 +632,11 @@
// see if any of the selected items are among the invisible nodes, and if so
// add them to a lazily constructed set which we pass back for rendering.
Set<UiElementNode> result = null;
        List<CanvasSelection> selections = mSelectionManager.getSelections();
if (selections.size() > 0) {
List<CanvasViewInfo> invisibleParents = mViewHierarchy.getInvisibleViews();
if (invisibleParents.size() > 0) {
                for (CanvasSelection item : selections) {
CanvasViewInfo viewInfo = item.getViewInfo();
// O(n^2) here, but both the selection size and especially the
// invisibleParents size are expected to be small
//Synthetic comment -- @@ -809,7 +809,7 @@
/**
* Helper to create the drag source for the given control.
* <p/>
     * This is static with package-access so that {@link OutlinePage2} can also
* create an exact copy of the source with the same attributes.
*/
/* package */static DragSource createDragSource(Control control) {
//Synthetic comment -- @@ -824,7 +824,7 @@
/**
* Helper to create the drop target for the given control.
* <p/>
     * This is static with package-access so that {@link OutlinePage2} can also
* create an exact copy of the drop target with the same attributes.
*/
/* package */static DropTarget createDropTarget(Control control) {
//Synthetic comment -- @@ -1007,8 +1007,8 @@
manager.add(new Action("Run My Test", IAction.AS_PUSH_BUTTON) {
@Override
public void run() {
                List<CanvasSelection> selection = mSelectionManager.getSelections();
                CanvasSelection canvasSelection = selection.get(0);
CanvasViewInfo info = canvasSelection.getViewInfo();

Object viewObject = info.getViewObject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MarqueeGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MarqueeGesture.java
//Synthetic comment -- index f9c91f4..c374b0e 100644

//Synthetic comment -- @@ -53,9 +53,9 @@
this.mCanvas = canvas;

if (toggle) {
            List<CanvasSelection> selection = canvas.getSelectionManager().getSelections();
mInitialSelection = new ArrayList<CanvasViewInfo>(selection.size());
            for (CanvasSelection item : selection) {
mInitialSelection.add(item.getViewInfo());
}
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index d36b96c..f047cbc 100644

//Synthetic comment -- @@ -572,9 +572,9 @@
// layout itself: a copy would be ok but not a move operation of the
// layout into himself.

                        CanvasSelection[] selection = mGlobalDragInfo.getCurrentSelection();
if (selection != null) {
                            for (CanvasSelection cs : selection) {
if (cs.getViewInfo() == targetVi) {
// The node that responded is one of the selection roots.
// Simply invalidate the drop feedback and move on the








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineOverlay.java
//Synthetic comment -- index 942da2b..a2c533e 100644

//Synthetic comment -- @@ -33,22 +33,25 @@
private Color mOutlineColor;

/** Vertical scaling & scrollbar information. */
    private ScaleInfo mVScale;

/** Horizontal scaling & scrollbar information. */
    private ScaleInfo mHScale;

/**
* Constructs a new {@link OutlineOverlay} linked to the given view
* hierarchy.
*
* @param viewHierarchy The {@link ViewHierarchy} to render
     * @param hScale The {@link ScaleInfo} to use to transfer horizontal layout
*            coordinates to screen coordinates
     * @param vScale The {@link ScaleInfo} to use to transfer vertical layout
*            coordinates to screen coordinates
*/
    public OutlineOverlay(ViewHierarchy viewHierarchy, ScaleInfo hScale, ScaleInfo vScale) {
super();
this.mViewHierarchy = viewHierarchy;
this.mHScale = hScale;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
similarity index 99%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 198e507..939d1f4 100755

//Synthetic comment -- @@ -76,7 +76,7 @@
*
* @since GLE2
*/
public class OutlinePage2 extends ContentOutlinePage
implements ISelectionListener, INullSelectionListener {

/**
//Synthetic comment -- @@ -108,7 +108,7 @@
*/
private DropTarget mDropTarget;

    public OutlinePage2(GraphicalEditorPart graphicalEditorPart) {
super();
mGraphicalEditorPart = graphicalEditorPart;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index ba7b7b5..fc4d5d1 100755

//Synthetic comment -- @@ -700,7 +700,7 @@
AndroidTargetData data = layoutEditor.getTargetData();
DocumentDescriptor documentDescriptor;
if (data == null) {
                documentDescriptor = new DocumentDescriptor("temp", null /*children*/); //$NON-NLS-1$
} else {
documentDescriptor = data.getLayoutDescriptors().getDescriptor();
}
//Synthetic comment -- @@ -793,7 +793,7 @@
&& !ImageUtils.containsDarkPixels(cropped);
cropped = ImageUtils.createDropShadow(cropped,
hasTransparency ? 3 : 5 /* shadowSize */,
                                    !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f /* alpha */,
0x000000 /* shadowRgb */);

Display display = getControl().getDisplay();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
similarity index 96%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage2.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
//Synthetic comment -- index 1259e65..dadba56 100755

//Synthetic comment -- @@ -29,7 +29,6 @@
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

/**
* A customized property sheet page for the graphical layout editor v2.
//Synthetic comment -- @@ -45,10 +44,10 @@
*
* @since GLE2
*/
public class PropertySheetPage2 extends PropertySheetPage {


    public PropertySheetPage2() {
super();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java
similarity index 94%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java
//Synthetic comment -- index de9abe9..f830cb9 100755

//Synthetic comment -- @@ -32,7 +32,7 @@
/**
* Represents one selection in {@link LayoutCanvas}.
*/
/* package */ class CanvasSelection {

/** Current selected view info. Can be null. */
private final CanvasViewInfo mCanvasViewInfo;
//Synthetic comment -- @@ -47,12 +47,12 @@
private final String mName;

/**
     * Creates a new {@link CanvasSelection} object.
* @param canvasViewInfo The view info being selected. Must not be null.
* @param gre the rules engine
* @param nodeFactory the node factory
*/
    public CanvasSelection(CanvasViewInfo canvasViewInfo,
RulesEngine gre,
NodeFactory nodeFactory) {

//Synthetic comment -- @@ -160,11 +160,11 @@
* Gets the XML text from the given selection for a text transfer.
* The returned string can be empty but not null.
*/
    /* package */ static String getAsText(LayoutCanvas canvas, List<CanvasSelection> selection) {
StringBuilder sb = new StringBuilder();

LayoutEditor layoutEditor = canvas.getLayoutEditor();
        for (CanvasSelection cs : selection) {
CanvasViewInfo vi = cs.getViewInfo();
UiViewElementNode key = vi.getUiViewNode();
Node node = key.getXmlNode();
//Synthetic comment -- @@ -186,10 +186,10 @@
* @param items Items to wrap in elements
* @return An array of wrapper elements. Never null.
*/
    /* package */ static SimpleElement[] getAsElements(List<CanvasSelection> items) {
ArrayList<SimpleElement> elements = new ArrayList<SimpleElement>();

        for (CanvasSelection cs : items) {
CanvasViewInfo vi = cs.getViewInfo();

SimpleElement e = vi.toSimpleElement();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 136414e..5e93adb 100644

//Synthetic comment -- @@ -60,10 +60,10 @@
private LayoutCanvas mCanvas;

/** The current selection list. The list is never null, however it can be empty. */
    private final LinkedList<CanvasSelection> mSelections = new LinkedList<CanvasSelection>();

/** An unmodifiable view of {@link #mSelections}. */
    private final List<CanvasSelection> mUnmodifiableSelection =
Collections.unmodifiableList(mSelections);

/** Barrier set when updating the selection to prevent from recursively
//Synthetic comment -- @@ -98,13 +98,13 @@
}

/**
     * Returns the native {@link CanvasSelection} list.
*
     * @return An immutable list of {@link CanvasSelection}. Can be empty but not null.
* @see #getSelection() {@link #getSelection()} to retrieve a {@link TreeViewer}
*                      compatible {@link ISelection}.
*/
    /* package */ List<CanvasSelection> getSelections() {
return mUnmodifiableSelection;
}

//Synthetic comment -- @@ -114,8 +114,8 @@
*
* @return A copy of the current selection. Never null.
*/
    /* package */ List<CanvasSelection> getSnapshot() {
        return new ArrayList<CanvasSelection>(mSelections);
}

/**
//Synthetic comment -- @@ -129,7 +129,7 @@

ArrayList<TreePath> paths = new ArrayList<TreePath>();

        for (CanvasSelection cs : mSelections) {
CanvasViewInfo vi = cs.getViewInfo();
if (vi != null) {
ArrayList<Object> segments = new ArrayList<Object>();
//Synthetic comment -- @@ -184,7 +184,7 @@

// Create a list of all currently selected view infos
Set<CanvasViewInfo> oldSelected = new HashSet<CanvasViewInfo>();
                for (CanvasSelection cs : mSelections) {
oldSelected.add(cs.getViewInfo());
}

//Synthetic comment -- @@ -263,7 +263,7 @@
// Otherwise we select the item under the cursor.

if (!isCycleClick && !isMultiClick) {
                for (CanvasSelection cs : mSelections) {
if (cs.getRect().contains(p.x, p.y)) {
// The cursor is inside the selection. Don't change anything.
return;
//Synthetic comment -- @@ -395,7 +395,7 @@

/** Returns true if the view hierarchy is showing exploded items. */
private boolean hasExplodedItems() {
        for (CanvasSelection item : mSelections) {
if (item.getViewInfo().isExploded()) {
return true;
}
//Synthetic comment -- @@ -528,8 +528,8 @@
return false;
}

        for (ListIterator<CanvasSelection> it = mSelections.listIterator(); it.hasNext(); ) {
            CanvasSelection s = it.next();
if (canvasViewInfo == s.getViewInfo()) {
it.remove();
return true;
//Synthetic comment -- @@ -544,8 +544,8 @@
* Callers are responsible for calling redraw() and updateOulineSelection() after.
*/
private void deselectAll(List<CanvasViewInfo> canvasViewInfos) {
        for (ListIterator<CanvasSelection> it = mSelections.listIterator(); it.hasNext(); ) {
            CanvasSelection s = it.next();
if (canvasViewInfos.contains(s.getViewInfo())) {
it.remove();
}
//Synthetic comment -- @@ -556,8 +556,8 @@
/* package */ void sync(CanvasViewInfo lastValidViewInfoRoot) {
// Check if the selection is still the same (based on the object keys)
// and eventually recompute their bounds.
        for (ListIterator<CanvasSelection> it = mSelections.listIterator(); it.hasNext(); ) {
            CanvasSelection s = it.next();

// Check if the selected object still exists
ViewHierarchy viewHierarchy = mCanvas.getViewHierarchy();
//Synthetic comment -- @@ -623,13 +623,13 @@
*      given list is going to be altered and we should never alter the user-made selection.
*      Instead the caller should provide its own copy.
*/
    /* package */ static void sanitize(List<CanvasSelection> selection) {
if (selection.isEmpty()) {
return;
}

        for (Iterator<CanvasSelection> it = selection.iterator(); it.hasNext(); ) {
            CanvasSelection cs = it.next();
CanvasViewInfo vi = cs.getViewInfo();
UiViewElementNode key = vi == null ? null : vi.getUiViewNode();
Node node = key == null ? null : key.getXmlNode();
//Synthetic comment -- @@ -640,9 +640,9 @@
}

if (vi != null) {
                for (Iterator<CanvasSelection> it2 = selection.iterator();
it2.hasNext(); ) {
                    CanvasSelection cs2 = it2.next();
if (cs != cs2) {
CanvasViewInfo vi2 = cs2.getViewInfo();
if (vi.isParent(vi2)) {
//Synthetic comment -- @@ -665,8 +665,8 @@
mCanvas.redraw();
}

    private CanvasSelection createSelection(CanvasViewInfo vi) {
        return new CanvasSelection(vi, mCanvas.getRulesEngine(),
mCanvas.getNodeFactory());
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index 696ca49..94c917c 100644

//Synthetic comment -- @@ -46,11 +46,11 @@
*/
public void paint(SelectionManager selectionManager, GCWrapper gcWrapper,
RulesEngine rulesEngine) {
        List<CanvasSelection> selections = selectionManager.getSelections();
int n = selections.size();
if (n > 0) {
boolean isMultipleSelection = n > 1;
            for (CanvasSelection s : selections) {
if (s.isRoot()) {
// The root selection is never painted
continue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutRuleTest.java
similarity index 83%
rename from eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java
rename to eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutRuleTest.java
//Synthetic comment -- index 7540e4e..7255f66 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.BaseLayout.AttributeFilter;

import java.util.Arrays;
import java.util.HashMap;
//Synthetic comment -- @@ -34,7 +34,7 @@
// TODO: Check equals() but not == strings by using new String("") to prevent interning
// TODO: Rename BaseLayout to BaseLayoutRule, and tests too of course

public class BaseLayoutTest extends LayoutTestBase {

/** Provides test data used by other test cases */
private IDragElement[] createSampleElements() {
//Synthetic comment -- @@ -51,18 +51,18 @@
return elements;
}

    /** Test {@link BaseLayout#collectIds}: Check that basic lookup of id works */
public final void testCollectIds1() {
IDragElement[] elements = TestDragElement.create(TestDragElement.create(
"android.widget.Button", new Rect(0, 0, 100, 80)).id("@+id/Button01"));
Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        Map<String, Pair<String, String>> ids = new BaseLayout().collectIds(idMap, elements);
assertEquals(1, ids.size());
assertEquals("@+id/Button01", ids.keySet().iterator().next());
}

/**
     * Test {@link BaseLayout#collectIds}: Check that with the wrong URI we
* don't pick up the ID
*/
public final void testCollectIds2() {
//Synthetic comment -- @@ -71,42 +71,42 @@
"@+id/Button01"));

Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        Map<String, Pair<String, String>> ids = new BaseLayout().collectIds(idMap, elements);
assertEquals(0, ids.size());
}

/**
     * Test {@link BaseLayout#normalizeId(String)}
*/
public final void testNormalizeId() {
        assertEquals("foo", new BaseLayout().normalizeId("foo"));
        assertEquals("@+id/name", new BaseLayout().normalizeId("@id/name"));
        assertEquals("@+id/name", new BaseLayout().normalizeId("@+id/name"));
}

/**
     * Test {@link BaseLayout#collectExistingIds}
*/
public final void testCollectExistingIds1() {
Set<String> existing = new HashSet<String>();
INode node = TestNode.create("android.widget.Button").id("@+id/Button012").add(
TestNode.create("android.widget.Button").id("@+id/Button2"));

        new BaseLayout().collectExistingIds(node, existing);

assertEquals(2, existing.size());
assertContainsSame(Arrays.asList("@+id/Button2", "@+id/Button012"), existing);
}

/**
     * Test {@link BaseLayout#collectIds}: Check that with multiple elements and
* some children we still pick up all the right id's
*/
public final void testCollectIds3() {
Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();

IDragElement[] elements = createSampleElements();
        Map<String, Pair<String, String>> ids = new BaseLayout().collectIds(idMap, elements);
assertEquals(5, ids.size());
assertContainsSame(Arrays.asList("@+id/Button01", "@+id/Button02", "@+id/Button011",
"@+id/Button012", "@+id/LinearLayout01"), ids.keySet());
//Synthetic comment -- @@ -119,11 +119,11 @@
}

/**
     * Test {@link BaseLayout#remapIds}: Ensure that it identifies a conflict
*/
public final void testRemapIds1() {
Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        BaseLayout baseLayout = new BaseLayout();
IDragElement[] elements = createSampleElements();
baseLayout.collectIds(idMap, elements);
INode node = TestNode.create("android.widget.Button").id("@+id/Button012").add(
//Synthetic comment -- @@ -142,10 +142,10 @@


/**
     * Test {@link BaseLayout#getDropIdMap}
*/
public final void testGetDropIdMap() {
        BaseLayout baseLayout = new BaseLayout();
IDragElement[] elements = createSampleElements();
INode node = TestNode.create("android.widget.Button").id("@+id/Button012").add(
TestNode.create("android.widget.Button").id("@+id/Button2"));
//Synthetic comment -- @@ -160,7 +160,7 @@
}

public final void testAddAttributes1() {
        BaseLayout layout = new BaseLayout();

// First try with no filter
IDragElement oldElement = TestDragElement.create("a.w.B").id("@+id/foo");
//Synthetic comment -- @@ -178,7 +178,7 @@

public final void testAddAttributes2() {
// Test filtering
        BaseLayout layout = new BaseLayout();

// First try with no filter
IDragElement oldElement = TestDragElement.create("a.w.B").id("@+id/foo");
//Synthetic comment -- @@ -203,7 +203,7 @@
}

public final void testFindNewId() {
        BaseLayout baseLayout = new BaseLayout();
Set<String> existing = new HashSet<String>();
assertEquals("@+id/Widget01", baseLayout.findNewId("a.w.Widget", existing));

//Synthetic comment -- @@ -218,11 +218,11 @@
}

public final void testDefaultAttributeFilter() {
        assertEquals("true", BaseLayout.DEFAULT_ATTR_FILTER.replace("myuri", "layout_alignRight",
"true"));
        assertEquals(null, BaseLayout.DEFAULT_ATTR_FILTER.replace(ANDROID_URI,
"layout_alignRight", "true"));
        assertEquals("true", BaseLayout.DEFAULT_ATTR_FILTER.replace(ANDROID_URI,
"myproperty", "true"));
}

//Synthetic comment -- @@ -234,7 +234,7 @@
"value2a").set("uri", "childprop2b", "value2b"));
INode newNode = TestNode.create("a.w.B").id("@+id/foo");
Map<String, Pair<String, String>> idMap = new HashMap<String, Pair<String, String>>();
        BaseLayout layout = new BaseLayout();
layout.addInnerElements(newNode, oldElement, idMap);
assertEquals(2, newNode.getChildren().length);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewRuleTest.java
similarity index 64%
rename from eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewTest.java
rename to eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewRuleTest.java
//Synthetic comment -- index 3634569..d19e9bd 100644

//Synthetic comment -- @@ -21,12 +21,12 @@

import junit.framework.TestCase;

public class BaseViewTest extends TestCase {
public final void testPrettyName() {
        assertEquals(null, BaseView.prettyName(null));
        assertEquals("", BaseView.prettyName(""));
        assertEquals("Foo", BaseView.prettyName("foo"));
        assertEquals("Foo bar", BaseView.prettyName("foo_bar"));
// TODO: We should check this to capitalize each initial word
// assertEquals("Foo Bar", BaseView.prettyName("foo_bar"));
// TODO: We should also handle camelcase properties
//Synthetic comment -- @@ -34,9 +34,9 @@
}

public final void testJoin() {
        assertEquals("foo", BaseView.join('|', Arrays.asList("foo")));
        assertEquals("", BaseView.join('|', Collections.<String>emptyList()));
        assertEquals("foo,bar", BaseView.join(',', Arrays.asList("foo", "bar")));
        assertEquals("foo|bar", BaseView.join('|', Arrays.asList("foo", "bar")));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java
//Synthetic comment -- index a864764..0f2ab21 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.IAttributeInfo;

/** Test/mock implementation of {@link IAttributeInfo} */
//Synthetic comment -- @@ -26,27 +28,27 @@
}

public String getDeprecatedDoc() {
        BaseLayoutTest.fail("Not supported yet in tests");
return null;
}

public String[] getEnumValues() {
        BaseLayoutTest.fail("Not supported yet in tests");
return null;
}

public String[] getFlagValues() {
        BaseLayoutTest.fail("Not supported yet in tests");
return null;
}

public Format[] getFormats() {
        BaseLayoutTest.fail("Not supported yet in tests");
return null;
}

public String getJavaDoc() {
        BaseLayoutTest.fail("Not supported yet in tests");
return null;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PointTestCases.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PointTestCases.java
//Synthetic comment -- index e68d2a5..c6a0578 100644

//Synthetic comment -- @@ -66,24 +66,24 @@
}

@Override
        ScaleInfo getHorizontalTransform() {
ScrollBar scrollBar = new List(this, SWT.V_SCROLL|SWT.H_SCROLL).getHorizontalBar();
            return new TestScaleInfo(scrollBar, mScaleX, mTranslateX);
}

@Override
        ScaleInfo getVerticalTransform() {
ScrollBar scrollBar = new List(this, SWT.V_SCROLL|SWT.H_SCROLL).getVerticalBar();
            return new TestScaleInfo(scrollBar, mScaleY, mTranslateY);
}
}

    static class TestScaleInfo extends ScaleInfo {
float mScale;

float mTranslate;

        public TestScaleInfo(ScrollBar scrollBar, float scale, float translate) {
super(null, scrollBar);
this.mScale = scale;
this.mTranslate = translate;







