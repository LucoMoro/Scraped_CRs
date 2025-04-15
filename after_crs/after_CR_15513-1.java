/*ADT GLE2: OutlinePage NPE fix.

Change-Id:Ib54c5af251eb0a21fa43e9ecfa0878004516fc87*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 06bd9ad..033732d 100755

//Synthetic comment -- @@ -66,6 +66,12 @@
*/
public class OutlinePage2 implements IContentOutlinePage {

    /**
     * The current TreeViewer. This is created in {@link #createControl(Composite)}.
     * It is entirely possible for callbacks to be invoked *before* the tree viewer
     * is created, for example if a non-yet shown canvas is modified and it refreshes
     * the model of a non-yet shown outline.
     */
private TreeViewer mTreeViewer;

/**
//Synthetic comment -- @@ -131,9 +137,11 @@
public void setModel(CanvasViewInfo rootViewInfo) {
mRootWrapper.setRoot(rootViewInfo);

        if (mTreeViewer != null) {
            Object[] expanded = mTreeViewer.getExpandedElements();
            mTreeViewer.refresh();
            mTreeViewer.setExpandedElements(expanded);
        }
}

public Control getControl() {







