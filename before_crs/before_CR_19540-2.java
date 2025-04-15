/*Refactor: rename PaletteComposite into PaletteControl

Change-Id:I7ce573d729255a07a65857a7f17d6a1cbb7f96d8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index a964c13..0c8257c 100644

//Synthetic comment -- @@ -193,7 +193,7 @@
private SashForm mSashError;

/** The palette displayed on the left of the sash. */
    private PaletteComposite mPalette;

/** The layout canvas displayed to the right of the sash. */
private LayoutCanvasViewer mCanvasViewer;
//Synthetic comment -- @@ -394,7 +394,7 @@
mSashPalette = new SashForm(parent, SWT.HORIZONTAL);
mSashPalette.setLayoutData(new GridData(GridData.FILL_BOTH));

        mPalette = new PaletteComposite(mSashPalette, this);

mSashError = new SashForm(mSashPalette, SWT.VERTICAL | SWT.BORDER);
mSashError.setLayoutData(new GridData(GridData.FILL_BOTH));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 2571753..5413f99 100755

//Synthetic comment -- @@ -83,7 +83,7 @@
import javax.xml.parsers.ParserConfigurationException;

/**
 * A palette composite for the {@link GraphicalEditorPart}.
* <p/>
* The palette contains several groups, each with a UI name (e.g. layouts and views) and each
* with a list of element descriptors.
//Synthetic comment -- @@ -104,7 +104,7 @@
*     - This would only be useful with meaningful icons. Out current 1-letter icons are not enough
*       to get rid of text labels.
*/
public class PaletteComposite extends Composite {


/** The parent grid layout that contains all the {@link Toggle} and {@link Item} widgets. */
//Synthetic comment -- @@ -119,7 +119,7 @@
* @param parent The parent composite.
* @param editor An editor associated with this palette.
*/
    public PaletteComposite(Composite parent, GraphicalEditorPart editor) {
super(parent, SWT.BORDER | SWT.V_SCROLL);

mEditor = editor;
//Synthetic comment -- @@ -428,9 +428,9 @@
private boolean mMouseIn;
private DragSource mSource;
private final ElementDescriptor mDesc;
        public PaletteComposite mPalette;

        public Item(Composite parent, PaletteComposite palette, ElementDescriptor desc) {
super(parent, SWT.NONE);
mPalette = palette;
mDesc = desc;







