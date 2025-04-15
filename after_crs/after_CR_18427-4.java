/*Fixing Issue 11557:   SWTError: No more handles exception on Eclipse

Change-Id:I3eb6ddd0b11f8d16c6f441333ddd8c5a8e9dd477*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4b3a3f3..883a40b 100755

//Synthetic comment -- @@ -449,6 +449,10 @@
mReloadListener = null;
}

        if (mCanvasViewer != null) {
            mCanvasViewer.dispose();
            mCanvasViewer = null;
        }
super.dispose();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 8991f55..c210a6a 100755

//Synthetic comment -- @@ -196,7 +196,7 @@

/** GC wrapper given to the IViewRule methods. The GC itself is only defined in the
*  context of {@link #onPaint(PaintEvent)}; otherwise it is null. */
    private GCWrapper mGCWrapper;

/** Default font used on the canvas. Do not dispose, it's a system font. */
private Font mFont;
//Synthetic comment -- @@ -249,7 +249,13 @@
/** List of clients listening to selection changes. */
private final ListenerList mSelectionListeners = new ListenerList();

    /**
     * The current Outline Page, to set its model.
     * It isn't possible to call OutlinePage2.dispose() in this.dispose().
     * this.dispose() is called from GraphicalEditorPart.dispose(),
     * when page's widget is already disposed.
     * Added the DisposeListener to OutlinePage2 in order to correctly dispose this page.
     **/
private OutlinePage2 mOutlinePage;

/** Barrier set when updating the selection to prevent from recursively
//Synthetic comment -- @@ -381,9 +387,9 @@
public void dispose() {
super.dispose();

        if (mSelectionFgColor != null) {
            mSelectionFgColor.dispose();
            mSelectionFgColor = null;
}

if (mOutlineColor != null) {
//Synthetic comment -- @@ -420,6 +426,16 @@
mClipboard.dispose();
mClipboard = null;
}

        if (mImage != null) {
            mImage.dispose();
            mImage = null;
        }

        if (mGCWrapper != null) {
            mGCWrapper.dispose();
            mGCWrapper = null;
        }
}

/**
//Synthetic comment -- @@ -899,6 +915,9 @@
* The image *can* be null, which is the case when we are dealing with an empty document.
*/
private void setImage(BufferedImage awtImage) {
        if (mImage != null) {
            mImage.dispose();
        }
if (awtImage == null) {
mImage = null;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java
//Synthetic comment -- index 1978c34..f509c19 100755

//Synthetic comment -- @@ -114,4 +114,11 @@
public void refresh() {
// ignore
}

    public void dispose() {
        if (mCanvas != null) {
            mCanvas.dispose();
            mCanvas = null;
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 5a7f631..f589651 100755

//Synthetic comment -- @@ -48,6 +48,8 @@
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
//Synthetic comment -- @@ -160,6 +162,12 @@

// Listen to selection changes from the layout editor
getSite().getPage().addSelectionListener(this);
        getControl().addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                dispose();
            }
        });
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/SectionHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/SectionHelper.java
//Synthetic comment -- index ac4cd03..23e19c0 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
//Synthetic comment -- @@ -40,13 +42,13 @@

/**
* Helper for the AndroidManifest form editor.
 *
* Helps create a new SectionPart with sensible default parameters,
* create default layout or add typical widgets.
 *
* IMPORTANT: This is NOT a generic class. It makes a lot of assumptions on the
* UI as used by the form editor for the AndroidManifest.
 *
* TODO: Consider moving to a common package.
*/
public final class SectionHelper {
//Synthetic comment -- @@ -63,7 +65,7 @@
* It's up to the caller to call setText() and setDescription().
* <p/>
* The section style includes a description and a title bar by default.
         *
* @param body The parent (e.g. FormPage body)
* @param toolkit Form Toolkit
*/
//Synthetic comment -- @@ -77,7 +79,7 @@
* <p/>
* The section style includes a description and a title bar by default.
* You can add extra styles, like Section.TWISTIE.
         *
* @param body The parent (e.g. FormPage body).
* @param toolkit Form Toolkit.
* @param extra_style Extra styles (on top of description and title bar).
//Synthetic comment -- @@ -91,12 +93,12 @@
}

// Create non-static methods of helpers just for convenience

/**
* Creates a new composite with a TableWrapLayout set with a given number of columns.
         *
* If the parent composite is a Section, the new composite is set as a client.
         *
* @param toolkit Form Toolkit
* @param numColumns Desired number of columns.
* @return The new composite.
//Synthetic comment -- @@ -108,12 +110,12 @@
/**
* Creates a label widget.
* If the parent layout if a TableWrapLayout, maximize it to span over all columns.
         *
* @param parent The parent (e.g. composite from CreateTableLayout())
* @param toolkit Form Toolkit
* @param label The string for the label.
* @param tooltip An optional tooltip for the label and text. Can be null.
         * @return The new created label
*/
public Label createLabel(Composite parent, FormToolkit toolkit, String label,
String tooltip) {
//Synthetic comment -- @@ -122,15 +124,15 @@

/**
* Creates two widgets: a label and a text field.
         *
* This expects the parent composite to have a TableWrapLayout with 2 columns.
         *
* @param parent The parent (e.g. composite from CreateTableLayout())
* @param toolkit Form Toolkit
* @param label The string for the label.
* @param value The initial value of the text field. Can be null.
* @param tooltip An optional tooltip for the label and text. Can be null.
         * @return The new created Text field (the label is not returned)
*/
public Text createLabelAndText(Composite parent, FormToolkit toolkit, String label,
String value, String tooltip) {
//Synthetic comment -- @@ -139,9 +141,9 @@

/**
* Creates a FormText widget.
         *
* This expects the parent composite to have a TableWrapLayout with 2 columns.
         *
* @param parent The parent (e.g. composite from CreateTableLayout())
* @param toolkit Form Toolkit
* @param isHtml True if the form text will contain HTML that must be interpreted as
//Synthetic comment -- @@ -175,16 +177,16 @@
} catch (Exception e) {
AdtPlugin.log(e, "Error when invoking Section.reflow");
}

section.layout(true /* changed */, true /* all */);
}
}

/**
* Creates a new composite with a TableWrapLayout set with a given number of columns.
     *
* If the parent composite is a Section, the new composite is set as a client.
     *
* @param composite The parent (e.g. a Section or SectionPart)
* @param toolkit Form Toolkit
* @param numColumns Desired number of columns.
//Synthetic comment -- @@ -205,9 +207,9 @@

/**
* Creates a new composite with a GridLayout set with a given number of columns.
     *
* If the parent composite is a Section, the new composite is set as a client.
     *
* @param composite The parent (e.g. a Section or SectionPart)
* @param toolkit Form Toolkit
* @param numColumns Desired number of columns.
//Synthetic comment -- @@ -228,15 +230,15 @@

/**
* Creates two widgets: a label and a text field.
     *
* This expects the parent composite to have a TableWrapLayout with 2 columns.
     *
* @param parent The parent (e.g. composite from CreateTableLayout())
* @param toolkit Form Toolkit
* @param label_text The string for the label.
* @param value The initial value of the text field. Can be null.
* @param tooltip An optional tooltip for the label and text. Can be null.
     * @return The new created Text field (the label is not returned)
*/
static public Text createLabelAndText(Composite parent, FormToolkit toolkit, String label_text,
String value, String tooltip) {
//Synthetic comment -- @@ -252,12 +254,12 @@
/**
* Creates a label widget.
* If the parent layout if a TableWrapLayout, maximize it to span over all columns.
     *
* @param parent The parent (e.g. composite from CreateTableLayout())
* @param toolkit Form Toolkit
* @param label_text The string for the label.
* @param tooltip An optional tooltip for the label and text. Can be null.
     * @return The new created label
*/
static public Label createLabel(Composite parent, FormToolkit toolkit, String label_text,
String tooltip) {
//Synthetic comment -- @@ -275,9 +277,9 @@

/**
* Associates a tooltip with a control.
     *
* This mirrors the behavior from org.eclipse.pde.internal.ui.editor.text.PDETextHover
     *
* @param control The control to which associate the tooltip.
* @param tooltip The tooltip string. Can use \n for multi-lines. Will not display if null.
*/
//Synthetic comment -- @@ -285,8 +287,8 @@
if (control == null || tooltip == null || tooltip.length() == 0) {
return;
}

        // Some kinds of controls already properly implement tooltip display.
if (control instanceof Button) {
control.setToolTipText(tooltip);
return;
//Synthetic comment -- @@ -299,7 +301,7 @@
Point sz = ic.computeSizeHint();
ic.setSize(sz.x, sz.y);
ic.setVisible(false); // initially hidden

control.addMouseTrackListener(new MouseTrackListener() {
public void mouseEnter(MouseEvent e) {
}
//Synthetic comment -- @@ -313,13 +315,18 @@
ic.setVisible(true);
}
});
        control.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                ic.dispose();
            }
        });
}

/**
* Creates a FormText widget.
     *
* This expects the parent composite to have a TableWrapLayout with 2 columns.
     *
* @param parent The parent (e.g. composite from CreateTableLayout())
* @param toolkit Form Toolkit
* @param isHtml True if the form text will contain HTML that must be interpreted as







