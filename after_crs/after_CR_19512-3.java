/*Property sheet: Fix tooltip and selected property painting

This CL contains the following fixes for the property sheet:

(1) Add a custom paint to the selection background. In Eclipse 3.5 and
    3.6, selected items in the property sheet are unreadable because
    they are painted white on white (well at least they are on
    Macs). This seems to be fixed in Eclipse 3.7M3. In any case, this
    changeset adds a custom paint listener which adds a mild red
    gradient below the selection to make the property name readable.

(2) Override the builtin property sheet's display of the property
    description such that it can take the property description and
    "linearize" it (remove newlines and make it into a single line
    display item).

Change-Id:Ib9c338759f79afa51f07deacb3962129e321c16b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 09691b8..17e15ad 100644

//Synthetic comment -- @@ -38,8 +38,8 @@
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -416,7 +416,7 @@
needBreak = true;
} else if (s != null) {
if (needBreak && s.trim().length() > 0) {
                    sb.append('\n');
}
sb.append(s);
needBreak = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
//Synthetic comment -- index dadba56..85c2fef 100755

//Synthetic comment -- @@ -16,9 +16,15 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -28,6 +34,8 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetEntry;

/**
//Synthetic comment -- @@ -46,7 +54,6 @@
*/
public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {

public PropertySheetPage() {
super();
}
//Synthetic comment -- @@ -56,6 +63,77 @@
super.createControl(parent);

setupTooltip();

        // Override parent class' "set status message" behavior. The parent will set
        // the status message to the property's "getDescription()" field. That field
        // may contain newlines, which means the text gets cut off. We want to instead
        // show ALL the text, fit on a single line, and since we don't get to subclass
        // the viewer we will just replace the status message with our own, which works
        // since our mouse listener is registered later so runs later.
        final Tree tree = (Tree) getControl();
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent event) {
                Point pt = new Point(event.x, event.y);
                TreeItem item = tree.getItem(pt);
                if (item != null) {
                    Object object = item.getData();
                    if (object instanceof IPropertySheetEntry) {
                        IPropertySheetEntry entry = (IPropertySheetEntry) object;
                        String help = entry.getDescription();
                        if (help != null) {
                            // Strip out newlines to make this a single line entry
                            help = help.replace('\n', ' ');
                            // Remove repeated spaces in case there were trailing spaces
                            help = help.replaceAll("  ", " "); //NON-NLS-1$ //NON-NLS-2$
                            IActionBars actionBars = getSite().getActionBars();
                            IStatusLineManager status = actionBars.getStatusLineManager();
                            status.setMessage(help);
                        }
                    }
                }
            }
        });

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
                    gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
                    gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                    gc.fillGradientRectangle(0, rect.y, 500, rect.height, false);
                    gc.setForeground(foreground);
                    gc.setBackground(background);
                    event.detail &= ~SWT.SELECTED;
                }
            }
        });
}

/**
//Synthetic comment -- @@ -128,7 +206,10 @@
label.addListener(SWT.MouseDown, this);
Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
Rectangle rect = item.getBounds(0);
                            // Display the tooltip on the same line as the property,
                            // but offset to the right of wherever the mouse cursor was,
                            // such that it does not obscure the list of properties.
                            Point pt = tree.toDisplay(event.x + 15, rect.y);
tip.setBounds(pt.x, pt.y, size.x, size.y);
tip.setVisible(true);
}







