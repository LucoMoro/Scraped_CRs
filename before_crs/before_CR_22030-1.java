/*15736: Eclipse crashes in layout editor when browsing element's properties

This changeset attempts to fix issue 15736. The root bug is an SWT bug
which can cause a JVM crash in some scenarios. However, it seems to be
triggered by code which performs a gradient paint in the property
sheet (which is in fact done to work around another SWT bug...).

Using a gradient is not necessary - a plain background paint will do -
so this changeset simplifies the code to only fill the background.  It
also leaves the selection flag on the event alone such that other
parts of the list (such as category items) are not affected.

Change-Id:Ica32fe1e6f49f1e4154c3ffad9cae9799fcc4723*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
//Synthetic comment -- index d33fffc..daf9655 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -98,39 +97,18 @@
// Fix the selection background. In Eclipse 3.5 and 3.6, the selection color
// is white, painted on top of a white or light blue background (table striping),
// which is practically unreadable.  This is fixed in 3.7M3, but we need a workaround
        // for earlier releases. The following code performs custom painting of this region,
        // and is based on the snippet "draw a custom gradient selection for tree" (snippet 226)
        // from http://www.eclipse.org/swt/snippets/ .
tree.addListener(SWT.EraseItem, new Listener() {
public void handleEvent(Event event) {
                if ((event.detail & SWT.SELECTED) != 0) {
GC gc = event.gc;
                    Rectangle area = tree.getClientArea();
                    int columnCount = tree.getColumnCount();
                    if (event.index == columnCount - 1 || columnCount == 0) {
                        int width = area.x + area.width - event.x;
                        if (width > 0) {
                            Region region = new Region();
                            gc.getClipping(region);
                            region.add(event.x, event.y, width, event.height);
                            gc.setClipping(region);
                            region.dispose();
                        }
                    }
                    gc.setAdvanced(true);
                    if (gc.getAdvanced()) {
                        gc.setAlpha(127);
                    }
Rectangle rect = event.getBounds();
                    Color foreground = gc.getForeground();
Color background = gc.getBackground();
Display display = tree.getDisplay();
                    gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_SELECTION));
                    gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                    gc.fillGradientRectangle(0, rect.y, 500, rect.height, false);
                    gc.setForeground(foreground);
gc.setBackground(background);
                    event.detail &= ~SWT.SELECTED;
}
}
});







