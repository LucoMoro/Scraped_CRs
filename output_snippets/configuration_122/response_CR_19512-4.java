//<Beginning of snippet n. 0>
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyDescription {
    private StringBuilder sb;
    private boolean needBreak;

    public PropertyDescription() {
        sb = new StringBuilder();
        needBreak = true;
    }

    public String formatPropertyDescription(String s) {
        if (s != null) {
            if (needBreak && s.trim().length() > 0) {
                sb.append(' ');
            }
            sb.append(s.replaceAll("\\n", " "));
            needBreak = false;
        }
        return sb.toString();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {
    private Tree tree;

    public PropertySheetPage() {
        super();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        setupTooltip();
    }

    private void setupTooltip() {
        tree.addListener(SWT.MouseDown, event -> {
            TreeItem item = tree.getItem(new Point(event.x, event.y));
            if (item != null) {
                Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                Rectangle rect = item.getBounds(0);
                Point pt = tree.toDisplay(rect.x, rect.y);
                tip.setBounds(pt.x, pt.y, size.x, size.y);
                tip.setVisible(true);
                
                // Set custom paint for the selected item
                tree.addPaintListener(e -> {
                    Rectangle clip = e.gc.getClipping();
                    GC gc = e.gc;
                    Color color = new Color(Display.getCurrent(), 255, 0, 0);
                    gc.setBackground(color);
                    gc.fillGradientRectangle(clip.x, clip.y, clip.width, clip.height, true);
                    color.dispose();
                });
            }
        });
    }
}
//<End of snippet n. 1>